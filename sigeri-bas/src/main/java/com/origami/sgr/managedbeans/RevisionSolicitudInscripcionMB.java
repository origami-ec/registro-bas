/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudActo;
import com.origami.sgr.entities.PubSolicitudRequisito;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.origami.config.SisVars;
import java.util.Date;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.models.Sms;
import com.origami.sgr.util.Constantes;
import java.util.logging.Logger;

/**
 *
 * @author EDWIN
 */
@Named
@ViewScoped
public class RevisionSolicitudInscripcionMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(RevisionSolicitudInscripcionMB.class.getName());

    @Inject
    private RegistroPropiedadServices rps;

    @Inject
    private AsynchronousService as;

    protected HashMap<String, Object> par;
    protected String observacion = "";
    protected HistoricoTramites ht;
    protected Boolean tieneNotificacion = false;
    private Boolean solicitudAprobada;
    private RegpNotaDevolutiva notaDevolutiva;
    private List<Observaciones> observacionesTramites;
    private PubSolicitud solicitud;
    private List<PubSolicitudActo> actos;
    private List<PubSolicitudRequisito> requisitos, requisitosSeleccionados;
    private Long tramite;
    private RegpLiquidacion liquidacion;

    @PostConstruct

    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    map = new HashMap();
                    map.put("tramite.numTramite", tramite);
                    solicitud = (PubSolicitud) manager.findObjectByParameter(PubSolicitud.class, map);
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);

                    map = new HashMap();
                    map.put("solicitud.id", solicitud.getId());
                    actos = manager.findObjectByParameterList(PubSolicitudActo.class, map);

                    map = new HashMap();
                    map.put("solicitud.id", solicitud.getId());
                    map.put("tipo", Constantes.TIPO_REQUISITO);
                    requisitos = manager.findObjectByParameterOrderList(PubSolicitudRequisito.class, map, new String[]{"fecha"}, Boolean.FALSE);

                    solicitudAprobada = Boolean.TRUE;
                    observacionesTramites = rps.listarObservacionesPorTramite(ht);
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    liquidacion = new RegpLiquidacion();
                    if (ss.getParametros() != null && ss.getParametros().get("tramiteProforma") != null) {
                        tramite = (Long) ss.getParametros().get("tramiteProforma");
                        map = new HashMap();
                        map.put("numTramiteRp", tramite);
                        liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);

                    } else if (solicitud.getNumeroTramiteInscripcion() != null) {
                        tramite = solicitud.getNumeroTramiteInscripcion();
                        map = new HashMap();
                        map.put("numTramiteRp", tramite);
                        liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);

                    }
                    requisitosSeleccionados = new ArrayList<>();

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

    public void showDlgAprobado(Boolean estaAprobada) {
        this.solicitudAprobada = estaAprobada;
        if (solicitudAprobada) {
            observacion = "Requisitos correctos";
            completarTarea();
        } else {
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        }
    }

    public void guardarObservacion() {
        try {
            rps.guardarObservaciones(ht, session.getName_user(), observacion, this.getTaskDataByTaskID().getName());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarNotificacionPago(String tipo) {
        //if (Utils.isNotEmpty(requisitosSeleccionados)) {
        if (liquidacion != null && liquidacion.getId() != null) {
            observacion = "Se relaciona el número de trámite " + liquidacion.getNumTramiteRp();
            solicitud.setNumeroTramiteInscripcion(liquidacion.getNumTramiteRp());
            manager.merge(solicitud);
            Sms sms;
            switch (tipo) {
                case "NORMAL":
                    as.enviarCorreoProformaTramite(liquidacion);
                    break;
                case "SMS":
                    sms = new Sms();
                    sms.setDestinatario(liquidacion.getSolicitante().getTelefono1());
                    sms.setTopic(Constantes.TOPIC_SMS);
                    sms.setMensaje(Constantes.SMS_TRAMITE);
                    as.enviarCorreoProformaInscripcionSmsTramite(liquidacion, sms);
                    break;
                case "LINK":

                    PubSolicitud link = new PubSolicitud();
                    link.setNumeroTramite(solicitud.getTramite().getNumTramite());
                    link.setNumeroTramiteInscripcion(liquidacion.getNumTramiteRp());
                    link = rps.actualizarSolicitudVentanillaNumTramiteInscripcion(link);
                    if (link != null) {
                        String urlLink = SisVars.dominioVentanilla + "inscripciones/pagoInscripcion?code1=" + solicitud.getTramite().getNumTramite() + "&code2=" + solicitud.getSolCedula() + "&code3=" + Utils.encriptaEnMD5(solicitud.getTramite().getNumTramite().toString() + "_" + solicitud.getSolCedula() + "_" + "INSCRIPCION");
                        sms = new Sms();
                        sms.setDestinatario(liquidacion.getSolicitante().getTelefono1());
                        sms.setTopic(Constantes.TOPIC_SMS);
                        sms.setMensaje(Constantes.SMS_TRAMITE);
                        sms.setLink(urlLink);
                        //as.enviarCorreoProformaInscripcionSmsTramite(liquidacion, sms);
                        as.enviarCorreoProformaInscripcionLinkTramite(liquidacion, sms);
                    } else {
                        JsfUti.messageWarning(null, "Intente nuevamente", "");
                    }

                    break;
                default:
                    break;
            }

            completarTarea();
        } else {
            JsfUti.messageWarning(null, "Debe asociar un número de tramite", "");
        }
//        } else {
//            JsfUti.messageWarning(null, "Debe generar el único pdf, Porfi.", "");
//        }

    }

    public void completarTarea() {
        try {
            if (observacion != null) {
                par = new HashMap<>();
                if (!solicitudAprobada) {
                    par.put("revision", 0);
                } else {
                    par.put("revision", 1);
                }
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observacion.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                        + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadDocument(Long oid) {
        try {
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarUnicoArchivoPDF() {
        if (Utils.isNotEmpty(requisitosSeleccionados)) {
            Long time = new Date().getTime();
            List<File> rutasTemp = new ArrayList<>();
            try {
                for (PubSolicitudRequisito psr : requisitosSeleccionados) {
                    String ruta = SisVars.rutaTemporales + time + "_" + psr.getRequisito().getRequisito().replace(" ", "_").toLowerCase() + ".pdf";
                    File file = new File(ruta);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        ou.streamFile(psr.getDocumento(), fos);
                        fos.close();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    rutasTemp.add(file);
                }
                String archivoUnido = (liquidacion != null && liquidacion.getId() != null
                        ? liquidacion.getNumTramiteRp().toString() : solicitud.getSolNombres().replace(" ", "_").toLowerCase()) + ".pdf";
                String archivoTiff = (liquidacion != null && liquidacion.getId() != null
                        ? liquidacion.getNumTramiteRp().toString() : solicitud.getSolNombres().replace(" ", "_").toLowerCase()) + ".tiff";
                Utils.unirPdf(rutasTemp, SisVars.rutaTemporales + archivoUnido);
                //Utils.convertPDFtoTIFF(SisVars.rutaTemporales + archivoUnido, SisVars.rutaTemporales + archivoTiff);
                ss.instanciarParametros();
                //ss.setNombreDocumento(SisVars.rutaTemporales + archivoUnido);
                ss.setNombreDocumento(SisVars.rutaTemporales + archivoTiff);
                ss.instanciarParametros();
                ss.setContentType("tiff");
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            JsfUti.messageWarning(null, "", "Debe escoger los requisitos a generar.");
        }
    }

    public void buscarTramite() {
        try {
            if (tramite != null) {
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                if (liquidacion == null) {
                    liquidacion = new RegpLiquidacion();
                    JsfUti.messageWarning(null, "No se encuentra el tramite.", "");
                } else {
                    JsfUti.update("mainForm");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de Tramite.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void abrirPaginaProforma() {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("solicitante", solicitud.getSolCedula());
        ss.agregarParametro("beneficiario", solicitud.getBenDocumento());
        JsfUti.redirectNewTab("/sigeri-playas/procesos/registro/iniciarTramiteRp.xhtml");
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getSolicitudAprobada() {
        return solicitudAprobada;
    }

    public void setSolicitudAprobada(Boolean solicitudAprobada) {
        this.solicitudAprobada = solicitudAprobada;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public Boolean getTieneNotificacion() {
        return tieneNotificacion;
    }

    public void setTieneNotificacion(Boolean tieneNotificacion) {
        this.tieneNotificacion = tieneNotificacion;
    }

    public PubSolicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(PubSolicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<PubSolicitudActo> getActos() {
        return actos;
    }

    public void setActos(List<PubSolicitudActo> actos) {
        this.actos = actos;
    }

    public List<PubSolicitudRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<PubSolicitudRequisito> requisitos) {
        this.requisitos = requisitos;
    }

    public List<PubSolicitudRequisito> getRequisitosSeleccionados() {
        return requisitosSeleccionados;
    }

    public void setRequisitosSeleccionados(List<PubSolicitudRequisito> requisitosSeleccionados) {
        this.requisitosSeleccionados = requisitosSeleccionados;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

}
