/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.DepartamentoUsuario;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegistroSolicitudRequisitos;
import com.origami.sgr.entities.SolicitudServicios;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class AprobacionInformeJefeVUMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(AprobacionInformeJefeVUMB.class.getName());

    @Inject
    private FirmaDigitalLocal firmaDigitalLocal;

    private HistoricoTramites ht;
    protected String observaciones;
    private List<HistoricoTramites> historicoTramite;
    private SolicitudServicios solicitudServicios;
    private List<DepartamentoUsuario> usuariosRol;
    private DepartamentoUsuario usuarioSeleccionado;
    private List<GeDepartamento> departamentos;
    private GeDepartamento departamento;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private Boolean asignar;
    //Para la subida de archivos
    private UploadedFile file;
    private File FILE;
    private Observaciones ultimaObservacion;
    private int aprueba;
    private Observaciones observacion;
    protected HashMap<String, Object> par;

    private int tipo;

    @PostConstruct
    public void initView() {
        if (this.session.getTaskID() != null) {
            if (!JsfUti.isAjaxRequest()) {
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
            }
        }
    }

    public void abriDlogo(int t) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(this.getTaskDataByTaskID().getName());
        observacion.setUserCre(session.getName());
        tipo = t;
        switch (tipo) {
            case 0:
            /*RECHAZO DE LIQUIDACION*/
            case 3:
                //DEBE ESCOGER AL USUARIO
                /*Generar Informe */
                PrimeFaces.current().executeScript("PF('dlgAsignar').show()");
                PrimeFaces.current().ajax().update("frmDlgAsignar");
                break;
            case 1:
            /*Aprobación Informe VA AL DIRECTOR*/
            case 2:
                /*ENTREGA DE DOCUMENTOS*/
                PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
                PrimeFaces.current().ajax().update(":frmDlgObser");
                break;
        }

    }

    public void completarTarea() {
        try {
            if (validar()) {
                par = new HashMap<>();
                switch (tipo) {
                    case 0:
                        /*Genera Informe */
                        par.put("usuario_4", (asignar && usuarioSeleccionado != null && usuarioSeleccionado.getId() != null)
                                ? usuarioSeleccionado.getUsuario().getUsuario() : session.getName_user());
                        par.put("informe", 2);
                        break;
                    case 1:
                        /*Aprobación Informe VA AL DIRECTOR*/
                        par.put("usuario_7", session.getName_user());
                        par.put("informe", 2);
                        break;
                    case 2:
                        /*ENTREGA DE DOCUMENTOS*/
                        par.put("usuario_8", session.getName_user());
                        par.put("informe", 2);
                        break;
                    case 3:
                        //DEBE ESCOGER AL USUARIO
                        /*RECHAZO DE LIQUIDACION*/
                        par.put("usuario_5", (asignar && usuarioSeleccionado != null && usuarioSeleccionado.getId() != null)
                                ? usuarioSeleccionado.getUsuario().getUsuario() : session.getName_user());
                        par.put("informe", 2);
                        break;
                }
                reg.guardarObservaciones(ht, session.getName_user(), observaciones, this.getTaskDataByTaskID().getName());
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, null, "ERROR DE APLICACIÓN");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private Boolean validar() {
        if (observaciones.isEmpty()) {
            JsfUti.messageError(null, "Aprobación Informe Jefe", "Ingrese una observación");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
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

    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            //   FILE = Utils.copyFileServer(file, SisVars.rutaRepositorioArchivo);
            JsfUti.messageInfo(null, "Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUti.messageError(null, null, "Ocurrió un error al subir el archivo");
        }
    }

    public void loadUsuariosDepartamento() {
        if (departamento != null && departamento.getId() != null) {
            usuarioSeleccionado = new DepartamentoUsuario();
            map = new HashMap();
            map.put("departamento.id", departamento.getId());
            usuariosRol = manager.findObjectByParameterList(DepartamentoUsuario.class, map);
        }
    }

    public void enviarFirma() {
        try {
            String motivo = "SolicitudServicios-" + ht.getNumTramite();
            String archivo = firmaDigitalLocal.generarDocumento(solicitudServicios.getDocumento(), motivo);
            if (archivo != null) {
                Integer[] posicionFirma = Utils.getFontPosition(archivo, "AUTORIZADO");
                firmaDigitalLocal.grabarFirmaDocumento(null, solicitudServicios.getId(), solicitudServicios.getDocumento(), "firma_administrativo", "firma_pendiente", motivo, ht.getNumTramite(), posicionFirma);
                JsfUti.messageInfo(null, "Listo puede proceder a su firma", "");
                //this.continuar();
            } else {
                JsfUti.messageWarning(null, "Intente nuevamente", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUti.messageError(null, Messages.error, "");
        }
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Boolean getAsignar() {
        return asignar;
    }

    public void setAsignar(Boolean asignar) {
        this.asignar = asignar;
    }

    public List<DepartamentoUsuario> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<DepartamentoUsuario> usuariosRol) {
        this.usuariosRol = usuariosRol;
    }

    public DepartamentoUsuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(DepartamentoUsuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public File getFILE() {
        return FILE;
    }

    public void setFILE(File FILE) {
        this.FILE = FILE;
    }

    public List<HistoricoTramites> getHistoricoTramite() {
        return historicoTramite;
    }

    public void setHistoricoTramite(List<HistoricoTramites> historicoTramite) {
        this.historicoTramite = historicoTramite;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

//</editor-fold>
    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }
}
