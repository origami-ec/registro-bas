/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.auto;

import com.origami.config.SisVars;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.MsgFormatoNotificacion;
import com.origami.sgr.entities.MsgTipoFormatoNotificacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.Email;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@Stateless
@javax.enterprise.context.Dependent
public class EnvioCorreoTimer {

    private static final Logger LOG = Logger.getLogger(EnvioCorreoTimer.class.getName());

    @Inject
    private Entitymanager manager;

    /**
     *
     */
    //@Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "7", dayOfMonth = "*", year = "*", minute = "0", second = "0", persistent = false)
//    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "7-22", dayOfMonth = "*", year = "*", minute = "*/2", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTask() {
        System.out.println("Timer event: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (SisVars.enviar) {
            //    buscarCertificadosExpirados();
            //  buscarInscripcionesExpirados();
        }
    }

    private void buscarCertificadosExpirados() {
        String sql = "SELECT DISTINCT ht FROM RegpTareasTramite tt INNER JOIN tt.tramite ht LEFT JOIN tt.certificado rc"
                + " WHERE cast(ht.fechaEntrega as date) = cast(:fechaEntrega as date) AND ht.tipoTramite = 1 "
                + "AND rc.id IS NULL AND ht.fechaNotificacion IS NULL AND tt.estado = true "
                + "AND tt.realizado = false ";
        List<HistoricoTramites> tramites = manager.findAll(sql, new String[]{"fechaEntrega"}, new Object[]{new Date()});

        if (Utils.isNotEmpty(tramites)) {
            System.out.println("Certificados a notificar " + tramites.size());
            Map<String, Object> map = new HashMap();
            map.put("descripcion", "CORREO_TRAMITE_CONTENIDO_VACIO");
            MsgTipoFormatoNotificacion tfn = (MsgTipoFormatoNotificacion) manager.findObjectByParameter(MsgTipoFormatoNotificacion.class, map);
            map = new HashMap(); //
            map.put("estado", 1);
            map.put("tipo", tfn);
            MsgFormatoNotificacion fn = (MsgFormatoNotificacion) manager.findObjectByParameter(MsgFormatoNotificacion.class, map);
            Long ultimoEnviado = null;
            for (HistoricoTramites tramite : tramites) {
                boolean enviar = true;
                Object id = null;
                try {
                    id = manager.getNativeQuery("SELECT id FROM flow.tareas_activas WHERE proc_inst_id = ?", new Object[]{tramite.getIdProceso()});
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Error al consultar tarea: " + tramite.getNumTramite(), e);
                }
                if (id == null) {
                    tramite.setFechaNotificacion(new Date());
                    manager.update(tramite);
                    continue;
                }
                if (tramite.getNumTramite().equals(ultimoEnviado)) {
                    continue;
                }
                ultimoEnviado = tramite.getNumTramite();
                // VERIFICAMOS SI EL LISTADO DE TAREAS HAY ALGUNA REALIZADA PARA NO ENVIARLA
                if (Utils.isNotEmpty(tramite.getRegpTareasTramiteCollection())) {
                    for (RegpTareasTramite tt : tramite.getRegpTareasTramiteCollection()) {
                        if (tt.getCertificado() != null) {
                            enviar = false;
                            break;
                        }
                    }
                }
                // ENVIAR CORREO 
                if (tramite.getSolicitante().getCorreo1() != null && enviar) {
//                    System.out.println("Enviar Correo: " + tramite.getNumTramite() + " correo " + tramite.getSolicitante().getCorreo1() + " proceso " + tramite.getIdProceso());
                    try {
                        String contenido = fn.getHeader()
                                + "<br/>Estimado usuario, pedimos mil disculpas, por motivo de cambio de sistema su trámite No. <bold>" + tramite.getNumTramite()
                                + "</bold> no ha podido ser despachado; por favor, acercarse en 48 horas laborables para retirarlo.<br/><br/>"
                                + "Agradecemos su comprención.<br/>"
                                + fn.getFooter();
//                        enviarCorreo("navarroangelr@gmail.com", fn.getAsunto() + ": " + tramite.getNumTramite(), contenido);
                        enviarCorreo(tramite.getSolicitante().getCorreo1(), fn.getAsunto() + ": " + tramite.getNumTramite(), contenido);
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Error al enviar correo: " + tramite.getNumTramite(), e);
                    }
                }
                tramite.setFechaNotificacion(new Date());
                manager.update(tramite);
            }
        } else {
            System.out.println("Certificados Sin datos...");
        }
    }

    private void buscarInscripcionesExpirados() {
        String sql = "SELECT DISTINCT ht FROM RegpTareasTramite tt INNER JOIN tt.tramite ht LEFT JOIN tt.movimiento mv"
                + " WHERE cast(ht.fechaEntrega as date) = cast(:fechaEntrega as date) AND ht.tipoTramite <> 1 "
                + "AND mv.fechaInscripcion IS NULL AND ht.fechaNotificacion IS NULL AND tt.estado = true "
                + "AND tt.realizado = false ";
        List<HistoricoTramites> tramites = manager.findAll(sql, new String[]{"fechaEntrega"}, new Object[]{new Date()});

        if (Utils.isNotEmpty(tramites)) {
            System.out.println("Inscripciones a notificar " + tramites.size());
            Map<String, Object> map = new HashMap();
            map.put("descripcion", "CORREO_TRAMITE_CONTENIDO_VACIO");
            MsgTipoFormatoNotificacion tfn = (MsgTipoFormatoNotificacion) manager.findObjectByParameter(MsgTipoFormatoNotificacion.class, map);
            map = new HashMap(); //
            map.put("estado", 1);
            map.put("tipo", tfn);
            MsgFormatoNotificacion fn = (MsgFormatoNotificacion) manager.findObjectByParameter(MsgFormatoNotificacion.class, map);
            List<RegpNotaDevolutiva> notaDevolutivaAnalisis = null;
            Long ultimoEnviado = null;
            for (HistoricoTramites tramite : tramites) {
                boolean enviar = true;
                Object id = null;
                try {
                    id = manager.getNativeQuery("SELECT id FROM flow.tareas_activas WHERE proc_inst_id = ?", new Object[]{tramite.getIdProceso()});
                    notaDevolutivaAnalisis = manager.findAll(Querys.getNotaDevolutivaByTramite, new String[]{"idTramite"}, new Object[]{tramite.getId()});
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Error al consultar tarea: " + tramite.getNumTramite(), e);
                }
                if (id == null) {
                    tramite.setFechaNotificacion(new Date());
                    manager.update(tramite);
                    continue;
                }
                if (Utils.isNotEmpty(notaDevolutivaAnalisis)) {
                    tramite.setFechaNotificacion(new Date());
                    manager.update(tramite);
                    continue;
                }
                if (tramite.getNumTramite().equals(ultimoEnviado)) {
                    continue;
                }
                ultimoEnviado = tramite.getNumTramite();
                // VERIFICAMOS SI EL LISTADO DE TAREAS HAY ALGUNA REALIZADA PARA NO ENVIARLA
                if (Utils.isNotEmpty(tramite.getRegpTareasTramiteCollection())) {
                    for (RegpTareasTramite tt : tramite.getRegpTareasTramiteCollection()) {
                        if (tt.getMovimiento() != null) {
                            if (tt.getMovimiento().getFechaInscripcion() != null) {
                                enviar = false;
                                break;
                            }
                        }
                    }
                }
                // ENVIAR CORREO
                if (tramite.getSolicitante().getCorreo1() != null && enviar) {
                    try {
                        String contenido = fn.getHeader()
                                + "<br/>Estimado usuario, pedimos mil disculpas, por motivo de cambio de sistema su trámite No. <bold>" + tramite.getNumTramite()
                                + "</bold> no ha podido ser despachado; por favor, acercarse en 48 horas laborables para retirarlo.<br/><br/>"
                                + "Agradecemos su comprención.<br/>"
                                + fn.getFooter();
//                        enviarCorreo("navarroangelr@gmail.com", fn.getAsunto() + ": " + tramite.getNumTramite(), contenido);
                        enviarCorreo(tramite.getSolicitante().getCorreo1(), fn.getAsunto() + ": " + tramite.getNumTramite(), contenido);
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Error al enviar correo: " + tramite.getNumTramite(), e);
                    }
                }
                tramite.setFechaNotificacion(new Date());
                manager.update(tramite);
            }
        } else {
            System.out.println("Inscripciones Sin datos...");
        }
    }

//    @Asynchronous
    public void enviarCorreo(String correo, String asunto, String contenido) {
        Email send = new Email(correo, asunto, contenido, null);
        send.sendMail();
    }
}
