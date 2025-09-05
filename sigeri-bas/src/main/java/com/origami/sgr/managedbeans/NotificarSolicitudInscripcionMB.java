/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
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
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudActo;
import com.origami.sgr.entities.PubSolicitudRequisito;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegRequisitos;
import com.origami.sgr.entities.RegRequisitosActos;
import java.util.List;
import com.origami.sgr.models.ActosEnLinea;
import com.origami.sgr.models.ActosRequisito;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 *
 * @author EDWIN
 */
@Named
@ViewScoped
public class NotificarSolicitudInscripcionMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(NotificarSolicitudInscripcionMB.class.getName());

    @Inject
    private RegistroPropiedadServices rps;

    @Inject
    private AsynchronousService as;

    protected HashMap<String, Object> par;
    protected String observacion = "";
    protected HistoricoTramites ht;
    private Boolean solicitudAprobada;
    private RegpNotaDevolutiva notaDevolutiva;
    private List<Observaciones> observacionesTramites;
    private AclUser us;
    private PubSolicitud solicitud;
    private List<PubSolicitudActo> actos;
    private List<PubSolicitudRequisito> requisitos, requisitosNotificar, requisitosNotificados;

    private List<ActosEnLinea> actosEnLinea;
    private List<ActosRequisito> actosRequisitos, requisitosSeleccionados;
    private ActosEnLinea acto;

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
                    requisitos = manager.findObjectByParameterList(PubSolicitudRequisito.class, map);

                    map = new HashMap();
                    map.put("solicitud.id", solicitud.getId());
                    map.put("tipo", Constantes.TIPO_REQUISITO_NOTIFICAR);
                    requisitosNotificados = manager.findObjectByParameterList(PubSolicitudRequisito.class, map);

                    solicitudAprobada = Boolean.TRUE;
                    observacionesTramites = rps.listarObservacionesPorTramite(ht);
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    PubSolicitudActo solicitudActo = actos.get(0);

                    actosEnLinea = (List<ActosEnLinea>) manager.getNativeQueryParameter(ActosEnLinea.class, Querys.getRegActoInscripcionesList,
                            null);
                    acto = new ActosEnLinea(new BigInteger(solicitudActo.getActo().getId().toString()), solicitudActo.getActo().getNombre(), Boolean.FALSE, Boolean.FALSE);
                    cargarRequisitos();
                    requisitosNotificar = new ArrayList<>();
                    requisitosSeleccionados = new ArrayList<>();

                    String actosObs = "";
                    for (PubSolicitudActo a : actos) {
                        actosObs = a.getActo().getNombre() + actosObs;
                    }
                    solicitud.setNotificacion("Estimad@ <b>" + solicitud.getSolNombres() + " " + solicitud.getSolApellidos()
                            + "</b> se generaron observaciones para su solicitud de <b>" + actosObs + "</b>");
                    solicitud.setNotificacion(solicitud.getNotificacion() + "<br/><br/> DEBE INGRESAR UNA NUEVA SOLICITUD CON LAS OBSERVACIONES CORREGIDAS.");
                    solicitud.setTieneNotificacion(Boolean.TRUE);
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

    public void cargarRequisitos() {
        actosRequisitos = (List<ActosRequisito>) manager.getNativeQueryParameter(ActosRequisito.class, Querys.getRegActoRequisitos,
                new Object[]{acto.getId()});
    }

    public void selccionarRequisitos() {
        if (Utils.isNotEmpty(requisitosSeleccionados)) {
            for (ActosRequisito ar : requisitosSeleccionados) {
                Boolean agregar = Boolean.TRUE;
                if (Utils.isNotEmpty(requisitosNotificar)) {
                    for (PubSolicitudRequisito psr : requisitosNotificar) {
                        if (psr.getRequisito().getId() == ar.getIdRequisito().longValue()) {
                            agregar = Boolean.FALSE;
                            break;
                        }
                    }
                }
                if (agregar) {
                    PubSolicitudRequisito solicitudRequisito = new PubSolicitudRequisito();
                    solicitudRequisito.setActo(new RegActo(ar.getIdActo().longValue(), ar.getActo()));
                    solicitudRequisito.setRequisito(new RegRequisitos(ar.getIdRequisito().longValue(), ar.getRequisito()));
                    solicitudRequisito.setRequisitosActos(new RegRequisitosActos(ar.getRequisitoActo().longValue()));
                    solicitudRequisito.setSolicitud(solicitud);
                    solicitudRequisito.setFecha(new Date());
                    solicitudRequisito.setTipo(Constantes.TIPO_REQUISITO_NOTIFICAR);
                    requisitosNotificar.add(solicitudRequisito);
                }
            }
        }
    }

    public void removerRequisito(int index) {
        requisitosNotificar.remove(index);
    }

    public void guardarObservacion() {
        try {
            rps.guardarObservaciones(ht, session.getName_user(), observacion, this.getTaskDataByTaskID().getName());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            if (observacion != null) {
                if (Utils.isNotEmpty(requisitosNotificar)) {
                    if (guardarNotificacion()) {
                        par = new HashMap<>();
                        actualizarSolicitud();
                        solicitud.setRequisitos(requisitosNotificar());
                        as.enviarCorreoSolicitudIncripcionObservaciones(solicitud);
                        this.guardarObservacion();
                        this.reasignarTarea(this.getTaskId(), session.getName_user());
                        this.completeTask(this.getTaskId(), par);
                        this.continuar();
                    } else {
                        JsfUti.messageWarning(null, "Intente nuevamente ", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "Debe escoger los requisitos a corregir", "");
                }
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observacion.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void actualizarSolicitud() {
        manager.merge(solicitud);
        if (Utils.isNotEmpty(requisitosNotificados)) {
            for (PubSolicitudRequisito psr : requisitosNotificados) {
                psr.setTipo(Constantes.TIPO_REQUISITO_NOTIFICADO);
                manager.persist(psr);
            }
        }
        for (PubSolicitudRequisito psr : requisitosNotificar) {
            manager.persist(psr);
        }

    }

    private Boolean guardarNotificacion() {
        try {

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<PubSolicitud> future = executorService.submit(() -> {
                PubSolicitud solicitudREST = new PubSolicitud();
                solicitudREST.setNumeroTramite(solicitud.getTramite().getNumTramite());
                solicitudREST.setNotificacion(solicitud.getNotificacion());
                solicitudREST.setRequisitos(requisitosNotificar());
                return rps.actualizarSolicitudVentanilla(solicitudREST);
            });
            PubSolicitud solicitudREST = future.get(60, TimeUnit.SECONDS);
            if (solicitudREST != null && solicitudREST.getId() != null) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(NotificarSolicitudInscripcionMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Boolean.FALSE;
    }

    private List<ActosRequisito> requisitosNotificar() {
        List<ActosRequisito> ars = new ArrayList<>();
        for (PubSolicitudRequisito psr : requisitosNotificar) {
            ActosRequisito ar = new ActosRequisito();
            ar.setActo(psr.getActo().getNombre());
            ar.setIdActo(new BigInteger(psr.getActo().getId().toString()));
            ar.setIdRequisito(new BigInteger(psr.getRequisito().getId().toString()));
            ar.setRequerido(Boolean.TRUE);
            ar.setRequisito(psr.getRequisito().getRequisito());
            ar.setRequisitoActo(new BigInteger(psr.getRequisitosActos().getId().toString()));
            ars.add(ar);
        }
        return ars;
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

    public List<PubSolicitudRequisito> getRequisitosNotificar() {
        return requisitosNotificar;
    }

    public void setRequisitosNotificar(List<PubSolicitudRequisito> requisitosNotificar) {
        this.requisitosNotificar = requisitosNotificar;
    }

    public List<ActosEnLinea> getActosEnLinea() {
        return actosEnLinea;
    }

    public void setActosEnLinea(List<ActosEnLinea> actosEnLinea) {
        this.actosEnLinea = actosEnLinea;
    }

    public List<ActosRequisito> getActosRequisitos() {
        return actosRequisitos;
    }

    public void setActosRequisitos(List<ActosRequisito> actosRequisitos) {
        this.actosRequisitos = actosRequisitos;
    }

    public ActosEnLinea getActo() {
        return acto;
    }

    public void setActo(ActosEnLinea acto) {
        this.acto = acto;
    }

    public List<ActosRequisito> getRequisitosSeleccionados() {
        return requisitosSeleccionados;
    }

    public void setRequisitosSeleccionados(List<ActosRequisito> requisitosSeleccionados) {
        this.requisitosSeleccionados = requisitosSeleccionados;
    }

}
