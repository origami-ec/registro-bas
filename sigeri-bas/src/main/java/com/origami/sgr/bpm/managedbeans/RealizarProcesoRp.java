/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.restful.BalconServicios;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class RealizarProcesoRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(RealizarProcesoRp.class.getName());

    @Inject
    private SeqGenMan sec;

    @Inject
    private AsynchronousService as;

    protected HashMap<String, Object> par;
    protected String observacion = "", mensaje = "";
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpTareasTramite> tareas;
    protected List<Observaciones> observacionesTramites;
    protected RegRegistrador registrador;
    protected RegpTareasTramite rtt;
    protected AclUser user;
    protected RegpNotaDevolutiva notaDevolutiva;
    protected Boolean solicitudAprobada;
    //protected Boolean certificador = false;
    protected CtlgItem tipo;
    protected AclRol rol;
    protected AclUser usuario;
    protected List<AclUser> users = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    tareas = reg.getTareasTramite(ht.getId());
                    observacionesTramites = reg.listarObservacionesPorTramite(ht);
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    map = new HashMap();
                    map.put("actual", Boolean.TRUE);
                    registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                    solicitudAprobada = Boolean.TRUE;
                    user = manager.find(AclUser.class, session.getUserId());
                    users = itl.getUsuariosByRolName("indexacion");
                    this.cargarArchivos(Constantes.indexacionHabilitantes, tramite);
                    StringBuffer validarInscripcion = itl.validarInscripcion(liquidacion);
                    if (validarInscripcion != null) {
                        advertencias = validarInscripcion.toString();
                    }
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /*public void validaRoles() {
        //ID ROL CERTIFICADOR
        if (session.getRoles().contains(2L)) {
            certificador = true;
        } else if (session.getRoles().contains(15L)) {
            certificador = true;
        } else if (session.getRoles().contains(17L)) {
            certificador = true;
        }
    }*/
    public void redirectFichaNueva() {
        if (this.fichasDisponibles()) {
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("tramite", ht.getId());
            JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
        } else {
            JsfUti.messageWarning(null, "Ya no tiene Fichas disponibles para crear.", "");
        }
    }

    public boolean fichasDisponibles() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getFicha()) {
                tt.setFicha(true);
                manager.update(tt);
                return true;
            }
        }
        return false;
    }

    public void realizarTarea(RegpTareasTramite ta) {
        try {
            tipo = null;
            rtt = null;
            if (ta.getRealizado()) {
                JsfUti.messageWarning(null, "Tarea de trámite ya fue concluida.", "");
            //} else if (archivos.isEmpty() && !liquidacion.getTramiteOnline()) {
                //JsfUti.messageError(null, "NO hay documentos habilitantes digitalizados.", "");
            } else {
                if (ta.getDetalle().getActo().getSolvencia()) {
                    rtt = ta;
                    JsfUti.update("formCertf");
                    JsfUti.executeJS("PF('dlgCertificado').show();");
                } else {
                    ss.instanciarParametros();
                    ss.agregarParametro("tarea", ta.getId());
                    ss.agregarParametro("taskID", this.getTaskId());
                    ss.agregarParametro("habilitar", true);
                    session.setTaskID(this.getTaskId());
                    JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void verMovimiento(RegpTareasTramite ta, Boolean esArchivo) {
        try {
            if (ta.getRevisado()) {
                JsfUti.messageWarning(null, "Contrato ya fue indexado.", "");
            } else if (liquidacion.getInscripcion() && ta.getDetalle().getActo().getId() == 11L) {
                ta.setRevisado(true);
                manager.update(ta);
                JsfUti.update("mainForm");
                JsfUti.messageInfo(null, "Documento marcado como indexado.", "");
            } else if (liquidacion.getInscripcion() && ta.getDetalle().getActo().getId() == 19L) {
                ta.setRevisado(true);
                manager.update(ta);
                JsfUti.update("mainForm");
                JsfUti.messageInfo(null, "Documento marcado como indexado.", "");
            } else if (liquidacion.getInscripcion() && ta.getDetalle().getActo().getSolvencia()) {
                rtt = ta;
                JsfUti.update("formCertf");
                JsfUti.executeJS("PF('dlgCertificado').show();");
            } else {
                ss.instanciarParametros();
                ss.agregarParametro("tarea", ta.getId());
                ss.agregarParametro("taskID", this.getTaskId());
                ss.agregarParametro("habilitar", false);
                ss.agregarParametro("archivo", esArchivo);
                session.setTaskID(this.getTaskId());
                JsfUti.redirectFaces("/procesos/registro/inscribirRevision.xhtml");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void verMovimientoArchivo(RegpTareasTramite ta) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("tarea", ta.getId());
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("habilitar", false);
            ss.agregarParametro("archivo", true);
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarObservacion() {
        try {
            if (observacion == null || observacion.isEmpty()) {
                reg.guardarObservaciones(ht, session.getName_user(), "TAREA REALIZADA", this.getTaskDataByTaskID().getName());
            } else {
                reg.guardarObservaciones(ht, session.getName_user(), observacion, this.getTaskDataByTaskID().getName());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            if (this.validaTareas()) {
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                par = new HashMap<>();
                par.put("aprobado", solicitudAprobada ? 1 : 2);
                par.put("registrador", itl.getUsuarioByRolName("registrador"));
                /*if (liquidacion.getEsRegistroPropiedad()) {
                    par.put("digitalizador", itl.getUsuarioByRolName("digitalizador"));
                }*/
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Debe de realizar todas las tareas que le corresponden.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaRechazoJuridico() {
        try {
            par = new HashMap<>();
            this.guardarObservacion();
            par.put("aprobado", solicitudAprobada ? 1 : 2);
            par.put("secretaria", itl.getCandidateUserByRolName("secretario_registral")); //FIRMA INSCRIPCION
            par.put("entregaDocumentos", itl.getCandidateUserByRolName("entrega_documento"));
            if (!solicitudAprobada) {
                this.guardarNotaDevolutiva();
                observacion = "NOTA DEVOLUTIVA: " + notaDevolutiva.getAsunto();
            }
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaRevisionInscripcion(Boolean revisionAprobada) {
        try {
            if (revisionAprobada) {
                this.completarTareaRevisionRp(revisionAprobada);
                /*if (!this.validaTareas(true)) {
                    JsfUti.messageWarning(null, "Debe de realizar todas las tareas que le corresponden.", "");
                } else {
                    this.completarTareaRevisionRp(revisionAprobada);
                }*/
            } else {
                JsfUti.update("formObs");
                JsfUti.executeJS("PF('dlgObsvs').show();");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void observacionCertificado() {
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");

    }

    public void mantenerEstado() {
        //this.cambiarEstadoHT();
        //this.continuar();
        try {
            this.guardarObservacion();
            observacionesTramites = reg.listarObservacionesPorTramite(ht);
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgObsvsCertificados').hide();");
            JsfUti.messageInfo(null, "Observacion guardada con exito.", "");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cambiarEstadoHT() {
        try {
            if (observacion != null && !observacion.isEmpty()) {
                ht.setIdProcesoTemp(observacion);
                manager.update(ht);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaRevisionRp(Boolean revisionAprobada) {
        try {
            par = new HashMap<>();
            //par.put("secretaria", itl.getCandidateUserByRolName("secretario_registral")); //FIRMA INSCRIPCION
            if (revisionAprobada) {
                par.put("revision", 1);
                this.guardarObservacion();
                /*if (liquidacion.getCertificado() && !liquidacion.getInscripcion()) {
                    List<File> archivos = as.generarFirmaDigitalArchivos(ht.getNumTramite());
                    as.enviarCorreoTramiteFinalizado(liquidacion, archivos);
                } else {
                    as.enviarCorreoTramiteFinalizado(liquidacion);
                    as.generarFirmaDigital(ht.getNumTramite());
                }*/
            } else {
                // RECHAZADA ENVIA A: ANALISIS y ACTUALIZA LOS CAMPOS DE REALIZADO Y REVISADO
                this.inactivarTareasRealizas();
                this.guardarObservacion();
                par.put("revision", 2);
            }
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            /*if (revisionAprobada && liquidacion.getCertificado() && !liquidacion.getInscripcion()) {
                if (!liquidacion.getSolicitante().getCorreo1().isEmpty()) {
                    finalizarTarea(ht.getNumTramite());
                }
            }*/
            this.continuar();
        } catch (Exception ex) {
            Logger.getLogger(RealizarProcesoRp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZA LA TAREA DE ENTREGA DE TRAMITES - SE LA HACE XK EL CERTIFICADO SE ENVIA AL CORREO
    public void finalizarTarea(Long numTramite) {
        Map map1 = new HashMap();
        map1.put("numTramite", numTramite);
        HistoricoTramites ht1 = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map1);
        String taskId = reg.getTaskIdFromNumTramite(numTramite);
        map1 = new HashMap();
        map1.put("numTramiteRp", numTramite);
        RegpLiquidacion liq = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map1);
        String obs = "Se envio el documento al correo: "
                + (!liq.getSolicitante().getCorreo1().isEmpty() ? liq.getSolicitante().getCorreo1() : "") + " - " + liq.getSolicitante().getNombreCompleto();

        if (!taskId.isEmpty()) {
            reg.guardarObservaciones(ht1, Constantes.tramiteDescargado, obs, "Entrega Certificado");

            try {
                HashMap<String, Object> par1 = new HashMap<>();
                this.reasignarTarea(taskId, Constantes.tramiteDescargado);
                this.completeTask(taskId, par1);
            } catch (Exception ex) {
                Logger.getLogger(BalconServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (liq.getCertificadoSinFlujo()) {
                reg.guardarObservaciones(ht1, Constantes.tramiteDescargado, obs, Constantes.certificadoExpress);
            }
        }
    }

    public void completarTareaArchivo() {
        try {
            par = new HashMap<>();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaInscripciones() {
        try {
            par = new HashMap<>();
            if (this.validaTareas()) { //TODAS LAS TAREAS
                par.put("revision", 2);
                mensaje = "Todos los documentos fueron realizados. La tarea pasa a firma de documentos.";
            } else {                    // FALTAN CERTIFICADOS
                par.put("revision", 1);
                map = new HashMap<>();
                map.put("nombre", "certificador");
                rol = (AclRol) manager.findObjectByParameter(AclRol.class, map);
                usuario = sec.getUserForTramite(rol.getId(), 1);
                par.put("certificador", usuario.getUsuario());
                mensaje = "Faltan de realizar documentos. Usuario certificador asignado: " + usuario.getUsuario().toUpperCase();
            }
            JsfUti.update("formMsjs");
            JsfUti.executeJS("PF('dlgMensajes').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaJefe() {
        try {
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaTareas(Boolean isRevision) {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRealizado()) {
                return false;
            }
            if (isRevision) {
                if (!tt.getRevisado()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validarTareasArchivo() {
        RegMovimiento movimiento;
        for (RegpTareasTramite tt : tareas) {
            movimiento = reg.getMovimientoFromTarea(tt.getId());
            if (movimiento.getNumTomo() == null) {
                return false;
            }
            if (movimiento.getNumTomo().length() <= 0) {
                return false;
            }
            if (movimiento.getFolioInicio() == null) {
                return false;
            }
            if (movimiento.getFolioFin() == null) {
                return false;
            }
        }
        return true;
    }

    public void generarInscripciones() {
        RegMovimiento movimiento;
        for (RegpTareasTramite tt : tareas) {
            movimiento = reg.getMovimientoFromTarea(tt.getId());
            if (movimiento != null) {
                reg.generarInscripcion(movimiento);
            }
            tt.setRevisado(Boolean.TRUE);
            tt.setFechaRevision(new Date());
            manager.merge(tt);
        }
        tareas = reg.getTareasTramite(ht.getId());
    }

    public void inactivarTareasRealizas() {
        for (RegpTareasTramite tt : tareas) {
            if (tt.getDetalle().getActo().getSolvencia()) {
                tt.setRealizado(Boolean.FALSE);
                tt.setRevisado(Boolean.FALSE);
                manager.persist(tt);
            }
        }
    }

    public void actaInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("P_MOVIMIENTO", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.setNombreReporte("ActaInscripcion");
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setEncuadernacion(Boolean.FALSE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void razonInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                String juridico = itl.getUsuarioByRolName("asesor_juridico");
                String jefe_inscripcion = itl.getUsuarioByRolName("jefe_inscripcion");
                ss.agregarParametro("ID_MOV", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
                //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
                //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("REVISOR_LEGAL", juridico.toUpperCase());
                ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.toUpperCase());
                ss.setNombreReporte("RazonInscripcion");
                ss.setNombreSubCarpeta("registro");
                ss.setTieneDatasource(true);
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocumentos(RegpTareasTramite tt) {
        rtt = tt;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void generarCertificado() {
        try {
            RegCertificado ce = reg.getCertificadoFromTarea(rtt.getId());
            if (ce != null) {
                this.llenarParametros(ce);
                switch (ce.getTipoDocumento()) {
                    case "C01": //NO POSEER BIENES
                        ss.setNombreReporte("CertificadoNoBienes");
                        break;
                    case "C02": //CERTIFICADO DE SOLVENCIA
                        ss.setNombreReporte("CertificadoSolvencia");
                        break;
                    case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                        ss.setNombreReporte("CertificadoHistoriaDominio");
                        break;
                    case "C04": //CERTIFICADO DE FICHA PERSONAL (MERCANTIL)
                        ss.setNombreReporte("CertificadoFichaPersonal");
                        break;
                    case "C05": //CERTIFICADO MERCANTIL 
                        ss.setNombreReporte("CertificadoMercantil");
                        break;
                    case "C06": //CERTIFICADO DE RAZON DE INSCRIPCION
                        this.llenarParametrosRazon(ce);
                        break;
                    case "C07": //CERTIFICADO GENERAL 
                        ss.setNombreReporte("CertificadoGeneral");
                        break;
                    default:
                        JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
                        return;
                }
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el certificado para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            user = manager.find(AclUser.class, session.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosRazon(RegCertificado ce) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosInforme(RegCertificado ce) {
        try {
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("NOMBRE", ce.getBeneficiario());
            ss.agregarParametro("BUSQUEDA", ce.getLinderosRegistrales());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("FDCertificadoInformeBienes");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void rechazar() {
        try {
            this.solicitudAprobada = false;
            notaDevolutiva = new RegpNotaDevolutiva();
            notaDevolutiva.setTramite(ht);
            notaDevolutiva.setFechaIngreso(new Date());
            notaDevolutiva.setFecha(Constantes.nombreCiudad + ", " + Utils.convertirFechaLetra(new Date()));
            notaDevolutiva.setRealizado(session.getName_user());
            notaDevolutiva.setElaborado(session.getName_user());
            notaDevolutiva.setPara(ht.getSolicitante());
            notaDevolutiva.setAsunto("Observaciones de Trámite # " + ht.getNumTramite().toString());
            notaDevolutiva.setDetalle(Constantes.contenidoDevolutivaCertificado);
            notaDevolutiva.setFirma(registrador.getNombreReportes());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cancelar() {
        this.solicitudAprobada = true;
        this.notaDevolutiva = null;
    }

    public void seleccionarCertificado() {
        try {
            if (tipo == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de certificacion.", "");
                return;
            }
            ss.instanciarParametros();
            ss.agregarParametro("tarea", rtt.getId());
            ss.agregarParametro("tipo", tipo.getCodename());
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("archivo", false);
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void certificarEnIndexacion() {
        try {
            if (tipo == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de certificacion.", "");
                return;
            }
            ss.instanciarParametros();
            ss.agregarParametro("tarea", rtt.getId());
            ss.agregarParametro("tipo", tipo.getCodename());
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("archivo", true);
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<CtlgItem> getTiposCertificados() {
        return manager.findAllEntCopy(Querys.getCtlgItemCertificados);
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion=" + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /*public boolean validarTareas() {
        if (this.certificador) {
            for (RegpTareasTramite tt : tareas) {
                if (!tt.getRealizado()) {
                    return false;
                }
            }
            return true;
        } else {
            for (RegpTareasTramite tt : tareas) {
                if (!tt.getRealizado() && !tt.getDetalle().getActo().getSolvencia()) {
                    return false;
                }
            }
            return true;
        }
    }*/
    public boolean validaTareas() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRealizado()) {
                return false;
            }
        }
        return true;
    }

    public void completarTareaDevolutiva() {
        try {
            if (!solicitudAprobada && notaDevolutiva.getId() != null) {
                observacion = "NOTA DEVOLUTIVA: " + notaDevolutiva.getAsunto();
                as.enviarCorreoNotaDevolutiva(liquidacion, SisVars.rutaDevolutivas + liquidacion.getNumTramiteRp()
                        + "-" + notaDevolutiva.getId().toString() + ".pdf", session.getName_user());

                String rolVentanila = "entrega_tramites";
                par = new HashMap<>();
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                par.put("ventanilla", itl.getCandidateUserByRolName(rolVentanila));
                par.put("aprobado", solicitudAprobada ? 1 : 2);
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "No se pudo completar este proceso.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarNotaDevolutiva() {
        try {
            Calendar cal = Calendar.getInstance();
            String anio = String.valueOf(cal.get(Calendar.YEAR));
            notaDevolutiva.setNumNotaDevolutiva(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaNotaDevolutiva).toString() + "-" + anio);
            notaDevolutiva = (RegpNotaDevolutiva) manager.persist(notaDevolutiva);
            if (notaDevolutiva != null && notaDevolutiva.getId() != null) {
                this.imprimirNotaDvolutiva(notaDevolutiva.getId());
            } else {
                JsfUti.messageWarning(null, "No se pudo guardar los datos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirNotaDvolutiva(Long idNotaDevolutiva) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();

            ss.setGeneraFile(true);
            ss.setRutaDocumento(SisVars.rutaDevolutivas + liquidacion.getNumTramiteRp() + "-" + idNotaDevolutiva.toString() + ".pdf");

            ss.setTieneDatasource(true);
            ss.setNombreReporte("NotaDevolutiva");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", idNotaDevolutiva);
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", user.getEnte().getNombresApellidos()
                    + Constantes.saltoLinea + Constantes.tituloCertificador);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");

        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void completarTareaIndexar() {
        try {
            if (this.validaIndexado()) {
                par = new HashMap<>();
                par.put("revision", 2);
                this.completarTareaJefe();
            } else {
                JsfUti.messageWarning(null, "Faltan de indexar tareas del contrato.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaIndexado() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRevisado()) {
                return false;
            }
        }
        return true;
    }

    public void reasignarIndexacion() {
        try {
            if (usuario != null) {
                this.reasignarTarea(this.getTaskId(), usuario.getUsuario());
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar un funcionario.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgConfirmacion() {
        try {
            manager.merge(liquidacion);
            JsfUti.update("formConfirmacion");
            JsfUti.executeJS("PF('dlgConfirmacion').show();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public RegpTareasTramite getRtt() {
        return rtt;
    }

    public void setRtt(RegpTareasTramite rtt) {
        this.rtt = rtt;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public Boolean getSolicitudAprobada() {
        return solicitudAprobada;
    }

    public void setSolicitudAprobada(Boolean solicitudAprobada) {
        this.solicitudAprobada = solicitudAprobada;
    }

    public CtlgItem getTipo() {
        return tipo;
    }

    public void setTipo(CtlgItem tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<AclUser> getUsers() {
        return users;
    }

    public void setUsers(List<AclUser> users) {
        this.users = users;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

}
