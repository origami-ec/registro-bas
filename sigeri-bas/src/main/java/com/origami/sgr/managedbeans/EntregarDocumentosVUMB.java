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
import com.origami.sgr.util.JsfUti;
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
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class EntregarDocumentosVUMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(EntregarDocumentosVUMB.class.getName());

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

    private void loadModel() {
//        files = new ArrayList<>();
//        notificacion = new Notificacion();
//        notificacion.setTitulo(this.tarea.getName());
//        notificacion.setContenido("Estimad@ " + solicitudServicios.getEnteSolicitante().getNombreCompleto());
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(this.getTaskDataByTaskID().getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            if (validar()) {
                // enviarCorreo(notificacion.getTitulo());
                //actualizar solicitud
                solicitudServicios.setFinalizado(Boolean.TRUE);
                manager.merge(solicitudServicios);
                observacion.setObservacion(observaciones);
//                if (saveTramite() == null) {
//                    return;
//                }
                reg.guardarObservaciones(ht, session.getName_user(), "TAREA REALIZADA", this.getTaskDataByTaskID().getName());
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    private Boolean validar() {
//        if (Utils.isEmptyString(notificacion.getContenido())) {
//            JsfUtil.addErrorMessage("", "Ingrese el contenido de la notificación");
//            return Boolean.FALSE;
//        }
        return Boolean.TRUE;
    }

    public void enviarCorreo(String asunto) {
//        Correo c = new Correo();
//        List<CorreoArchivo> archivos = new ArrayList<>();
//        if (!Utils.isEmpty(filesSend)) {
//            for (Documento d : filesSend) {
//                CorreoArchivo correoArchivo = new CorreoArchivo();
//                correoArchivo.setNombreArchivo(d.getArchivoNombre());
//                System.out.println("PathPDF: " + d.getArchivoNombre());
//                correoArchivo.setTipoArchivo("pdf");
//                correoArchivo.setArchivoBase64("");
//                archivos.add(correoArchivo);
//            }
//            c.setArchivos(archivos);
//        }
//        c.setDestinatario(solicitudServicios.getEnteSolicitante().getEmail());
//        c.setAsunto(asunto);
//        c.setMensaje(Utils.mailHtmlNotificacion("TRÁMITE N° " + tramite.getNumTramite() + " - " + tramite.getTipoTramite().getDescripcion(),
//                notificacion.getContenido(),
//                "Gracias por la Atención Brindada", "Este correo fue enviado de forma automática y no requiere respuesta."));
//        katalinaService.enviarCorreo(c);
//        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito");
    }

    public void files() {
//        if (tramite == null) {
//            return;
//        }
//        Object id = ReflexionEntity.getIdFromEntity(tramite);
//        if (id == null) {
//            return;
//        }
//        files = uploadDoc.fileEntiti(tramite);
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
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

    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }
//</editor-fold>

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }
 
}
