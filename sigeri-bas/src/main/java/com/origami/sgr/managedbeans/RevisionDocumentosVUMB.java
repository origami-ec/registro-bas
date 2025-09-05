/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.DepartamentoUsuario;
import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegistroSolicitudRequisitos;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.SolicitudDocumento;
import com.origami.sgr.entities.SolicitudServicios;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class RevisionDocumentosVUMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(RevisionDocumentosVUMB.class.getName());

    @Inject
    private FirmaDigitalLocal firmaDigitalLocal;
    
    @Inject
    protected RegistroPropiedadServices rps;
    
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    private HistoricoTramites ht;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private List<SolicitudDocumento> solicitudDocumentos;
    private SolicitudServicios solicitudServicios;
    private String observaciones;
    private int tipo;
    private Boolean asignar;
    private List<GeDepartamento> departamentos;
    private GeDepartamento departamento;
    private DepartamentoUsuario usuarioSeleccionado;
    private List<DepartamentoUsuario> usuariosRol;
    private Observaciones ultimaObservacion;
    private Observaciones observacion;
    protected HashMap<String, Object> par;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(ht);
//                procedimientoRequisitoList = requisitosService.getListaRequisitos(tramite.getTipoTramite().getId());

                map = new HashMap();
                map.put("numTramite", tramite);
                ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                observacion.setIdTramite(ht);
                map = new HashMap();
                map.put("tramite.id", ht.getId());
                solicitudServicios = (SolicitudServicios) manager.findObjectByParameter(SolicitudServicios.class, map);

                map = new HashMap();
                map.put("solicitud.id", solicitudServicios.getId());
                registroRequisitos = manager.findObjectByParameterList(RegistroSolicitudRequisitos.class, map);

                asignar = Boolean.FALSE;
                if (!Utils.isEmpty(ht.getObservacionesCollection())) {
                    ultimaObservacion = new ArrayList<>(ht.getObservacionesCollection()).get(ht.getObservacionesCollection().size() - 1);
                }
                docs = itl.getDocumentosTramite(ht.getId());
            } else {
                JsfUti.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void abriDlogo(int t) {
        if (verificarArchivosFirmados()) {
            tipo = t;
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(this.getTaskDataByTaskID().getName());
            observacion.setUserCre(session.getName_user());
//        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
//        PrimeFaces.current().ajax().update(":frmDlgObser");
            PrimeFaces.current().executeScript("PF('dlgAsignarAtender').show()");
            PrimeFaces.current().ajax().update("frmDlgAsignarAtender");
        } else {
            JsfUti.messageError(null, "Revisión Documentos", "Debe firmar todos los documentos");
        }

    }

    public void completarTarea() {
        try {
            if (Utils.isEmptyString(observaciones)) {
                JsfUti.messageError(null, "Revisión Documentos", "Ingrese una observación");
                return;
            }
            if (asignar) {
                if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
                    JsfUti.messageError(null, "", "Escoja un usuario");
                    return;
                }
            }
            par = new HashMap<>();
            switch (tipo) {
                case 0:
                    /*Rechaza y notifica las observaciones  */
                    //getParamts().put("usuario_3", session.getNameUser());
                    par.put("aprobado", 0);
                    break;
                case 1:
                    /*Genera liquidación*/
                    //getParamts().put("usuario_5", asignar ? usuarioSeleccionado.getUsuario().getUsuario() : session.getNameUser());
                    par.put("aprobado", 2);
                    break;
                case 2:
                    /*Genera informe*/
                    par.put("aprobado", 1);
                    break;
            }
            observacion.setObservacion(observaciones);
            manager.merge(ht);
            rps.guardarObservaciones(ht, session.getName_user(), observaciones, this.getTaskDataByTaskID().getName());
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, null, "ERROR DE APLICACIÓN");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void asignarAtender(Boolean a) {
        String script, update;
        asignar = a;
        if (asignar) {
            departamentos = manager.findAllEntCopy(Querys.getGeDepartamentos);
            departamento = new GeDepartamento();
            usuarioSeleccionado = new DepartamentoUsuario();
            script = "PF('dlgAsignar').show()";
            update = "frmDlgAsignar";
        } else {
            script = "PF('dlgObservaciones').show()";
            update = "frmDlgObser";
        }
        PrimeFaces.current().executeScript(script);
        PrimeFaces.current().ajax().update(update);
    }

    public void loadUsuariosDepartamento() {
        if (departamento != null && departamento.getId() != null) {
            usuarioSeleccionado = new DepartamentoUsuario();
            map = new HashMap();
            map.put("departamento.id", departamento.getId());
            usuariosRol = manager.findObjectByParameterList(DepartamentoUsuario.class, map);
        }
    }

    public void generarSolicitudPDFFirma() {
        try {

            map = new HashMap();
            map.put("ID", solicitudServicios.getId());
            map.put("LOGO_URL", JsfUti.getRealPath("/resources/image/origami.png"));
            map.put("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            map.put("HEADER_URL", JsfUti.getRealPath("/resources/image/origami.png"));
            map.put("FOOTER_URL", JsfUti.getRealPath("/resources/image/origami.png"));
            String motivo = "SolicitudAdministrativa-Tramite #" + ht.getNumTramite().toString();
            String archivo = firmaDigitalLocal.generarDocumento("administrativo/SolicitudMaterialOficina.jasper", motivo, map);
            Integer[] posicionFirma = Utils.getFontPosition(archivo, "FUNCIONARIO");
            Long oid = rps.guardarArchivo(archivo);
            firmaDigitalLocal.grabarFirmaDocumento(null, solicitudServicios.getId(), oid, "firma_administrativo", "firma_pendiente", motivo, ht.getNumTramite(), posicionFirma);
            //downLoadDocument(oid);
            solicitudServicios.setDocumento(oid);
            manager.merge(solicitudServicios);
            JsfUti.messageInfo(null, "Puede firmar su solicitud para continuar con el trámite", "");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUti.messageError(null, Messages.error, "");
        }
    }
//
//    public void downLoadDocument(SolicitudBienes data) {
//        if (data.getDocumento() != null) {
//            this.downLoadDocument(data.getDocumento());
//        } else {
//            generarSolicitudPDF(data.getId());
//        }
//    }

    private Boolean verificarArchivosFirmados() {
        Boolean finalizaTarea = Boolean.FALSE;
        List<DocumentoFirma> docs = firmaDigitalLocal.documentosXtramite(ht.getNumTramite());
        if (Utils.isNotEmpty(docs)) {
            map = new HashMap();
            map.put("codename", "firma_realizada");
            CtlgItem ci = (CtlgItem) manager.findObjectByParameter(CtlgItem.class, map);
            for (DocumentoFirma fd : docs) {
                if (fd.getEstado().getId().equals(ci.getId())) {
                    finalizaTarea = Boolean.TRUE;
                } else {
                    finalizaTarea = Boolean.FALSE;
                    break;
                }
            }
        } else {
            return finalizaTarea;
        }
        return finalizaTarea;
    }

    public void downLoadDocument() {
        if (solicitudServicios.getDocumento() != null) {
            this.downLoadDocument(solicitudServicios.getDocumento());
        } else {
            JsfUti.messageError(null, "No existen documentos firmados electronicamente", "");
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public DepartamentoUsuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(DepartamentoUsuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<DepartamentoUsuario> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<DepartamentoUsuario> usuariosRol) {
        this.usuariosRol = usuariosRol;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public List<SolicitudDocumento> getSolicitudDocumentos() {
        return solicitudDocumentos;
    }

    public void setSolicitudDocumentos(List<SolicitudDocumento> solicitudDocumentos) {
        this.solicitudDocumentos = solicitudDocumentos;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

//</editor-fold>
    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

}
