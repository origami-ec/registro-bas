/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans.noposeebien;

import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.Serializable;
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
 * @author gutya
 */
@Named
@ViewScoped
public class AprobarCertificadoNoPoseeBienRp extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(AprobarCertificadoNoPoseeBienRp.class.getName());

    @Inject
    private RegistroPropiedadServices rps;
    
    @Inject
    private UserSession us;

    @Inject
    private AsynchronousService as;

    protected String observacion = "";
    private LazyModel tareas;
    protected AclUser user;
    protected RegpLiquidacion liquidacion;
    protected RegRegistrador registrador;
    private RegpTareasTramite regpTareasTramiteRechazo;
    private String taskId;
    private String nameTask;
    private String tramite;

    @PostConstruct
    public void init() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("detalle.liquidacion.certificadoSinFlujo", Boolean.TRUE);
        filters.put("realizado", Boolean.TRUE);
        filters.put("revisado", Boolean.FALSE);
        tareas = new LazyModel(RegpTareasTramite.class, filters, "id", "DESC");
        registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
    }

    public void realizarTarea(RegpTareasTramite ta) {
        try {
            if (ta.getRealizado()) {
                JsfUti.messageWarning(null, "Tarea de Contrato ya fue concluida.", "");
            } else {
                ss.instanciarParametros();
                ss.agregarParametro("tarea", ta.getId());
                ss.agregarParametro("tramite", ta.getDetalle().getLiquidacion().getNumTramiteRp());
                JsfUti.redirectFaces("/procesos/registro/certificarNoPoseeBien.xhtml");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String usuario(Long id) {
        AclUser user = (AclUser) manager.find(Querys.getAclUserByID, new String[]{"id"}, new Object[]{id});
        if (user != null) {
            return user.getUsuario();
        } else {
            return "";
        }
    }

    public void entregaTramiteTarea(RegpTareasTramite ta) {
        try {
            map = new HashMap();
            map.put("numTramite", ta.getDetalle().getLiquidacion().getNumTramiteRp());
            HistoricoTramites ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);

            if (ta.getRealizado()) {
                if (Utils.isEmpty(rps.listarObservacionesPorTramite(ht))) {
                    ss.instanciarParametros();
                    ss.agregarParametro("tarea", ta.getId());
                    ss.agregarParametro("tramite", ta.getDetalle().getLiquidacion().getNumTramiteRp());
                    JsfUti.redirectFaces("/procesos/registro/entregaCertificadoNoPoseer.xhtml");
                } else {
                    JsfUti.messageWarning(null, "Trámite ha sido Entregado", "");
                }
            } else {
                JsfUti.messageWarning(null, "Tarea de no ha sido Concluida", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void verCertificado(RegCertificado ce, Boolean firma) {
        try {
            switch (ce.getTipoDocumento()) {
                case "C01"://NO POSEER BIENES
                    this.llenarParametros(ce, false);
                    ss.setNombreReporte("CertificadoNoBienes");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                case "C08"://COPIA DE RAZON DE INSCRIPCION
                    this.llenarParametrosRazon(ce, false);
                    ss.setNombreReporte("CopiaRazonInscripcion");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                case "C09": //UNICO BIEN DESDE NO POSEER BIENES
                    this.llenarParametros(ce, false);
                    ss.setNombreReporte("CertificadoSiPoseeBien");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                default:
                    JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void aprobarGenerarCertificado(RegCertificado ce) {
        try {
            tramite = ce.getNumTramite().toString();
            switch (ce.getTipoDocumento()) {
                case "C01"://NO POSEER BIEN
                    this.llenarParametros(ce, true);
                    ss.setNombreReporte("FDCertificadoNoBienes");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                case "C08"://COPIA DE RAZON DE INSCRIPCION
                    this.llenarParametrosRazon(ce, true);
                    ss.setNombreReporte("FDCopiaRazonInscripcion");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                case "C09"://UNICO BIEN
                    this.llenarParametros(ce, true);
                    ss.setNombreReporte("FDCertificadoSiPoseeBien");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
            }
            if (ce.getTareaTramite() != null) {
                ce.getTareaTramite().setRevisado(Boolean.TRUE);
                ce.getTareaTramite().setFechaRevision(new Date());
                manager.merge(ce.getTareaTramite());
                tareaFirmaCertificado(ce);
                //as.enviarCorreoTramiteFinalizado(ce.getTareaTramite().getDetalle().getLiquidacion());
                List<File> archivos = as.generarFirmaDigitalArchivos(ce.getNumTramite());
                as.enviarCorreoTramiteFinalizado(ce.getTareaTramite().getDetalle().getLiquidacion(), archivos);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void tareaFirmaCertificado(RegCertificado ce) {
        taskId = rps.getTaskIdFromNumTramite(ce.getNumTramite());
        nameTask = rps.getNameTaskFromNumTramite(ce.getNumTramite());
        if (nameTask != null) {
            if (!nameTask.isEmpty() && nameTask.contains("Firma")) {
                if (!taskId.isEmpty()) {
                    rps.guardarObservaciones(liquidacion.getTramite(), session.getName_user(), "APROBADO", "Firma Certificado");
                    //FINALIZA LA TAREA DE FIRMA CERTIFICADO
                    try {
                        HashMap<String, Object> par = new HashMap<>();
                        par.put("entregaDocumentos", itl.getCandidateUserByRolName("entrega_documento"));
                        this.reasignarTarea(taskId, session.getName_user());
                        this.completeTask(taskId, par);
                    } catch (Exception ex) {
                        Logger.getLogger(CertificarNoPoseeBien.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                rps.guardarObservaciones(ce.getTareaTramite().getTramite(), session.getName_user(), "APROBADO", Constantes.certificadoExpress);
            }
        } else {
            rps.guardarObservaciones(ce.getTareaTramite().getTramite(), session.getName_user(), "APROBADO", Constantes.certificadoExpress);
        }
    }

    public void llenarParametrosRazon(RegCertificado ce, boolean flag) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            if (flag) {
                ss.setIdCertificado(ce.getId());
                ss.setFirmarCertificado(registrador.isFirmaElectronica());
            }
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header_loja.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark_loja.png"));
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer_loja.png"));
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");

            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void llenarParametros(RegCertificado ce, boolean flag) {
        try {
            user = manager.find(AclUser.class, us.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            if (flag) {
                ss.setIdCertificado(ce.getId());
                ss.setFirmarCertificado(registrador.isFirmaElectronica());
            }
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header_loja.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark_loja.png"));
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer_loja.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void observacionCertificado(RegpTareasTramite regpTareasTramite) {
        this.regpTareasTramiteRechazo = regpTareasTramite;
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");

    }

    public void mantenerEstado() {
        JsfUti.executeJS("PF('dlgObsvsCertificados').hide();");
        guardarObservacion();
    }

    public void guardarObservacion() {
        try {
            regpTareasTramiteRechazo.setFechaFin(null);
            regpTareasTramiteRechazo.setRealizado(Boolean.FALSE);
            manager.merge(regpTareasTramiteRechazo);
            if (observacion == null || observacion.isEmpty()) {
                rps.guardarObservaciones(regpTareasTramiteRechazo.getTramite(), session.getName_user(), regpTareasTramiteRechazo.getCertificado().getClaseCertificado(), regpTareasTramiteRechazo.getCertificado().getClaseCertificado());
            } else {
                rps.guardarObservaciones(regpTareasTramiteRechazo.getTramite(), session.getName_user(), observacion, regpTareasTramiteRechazo.getCertificado().getClaseCertificado());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public LazyModel getTareas() {
        return tareas;
    }

    public void setTareas(LazyModel tareas) {
        this.tareas = tareas;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

}
