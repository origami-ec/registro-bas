/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.test;

import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.Imagen;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.SecuenciaRepertorio;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class TestMb extends BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger(TestMb.class.getName());

    @Inject
    private SeqGenMan sec;

    protected String protocol = "smtp";
    protected String auth = "true";
    protected String tls = "true";
    protected String host = SisVars.smtp_Host;
    protected String port = SisVars.smtp_Port;
    protected String user = SisVars.correo;
    protected String pass = SisVars.pass;
    protected String to = "";
    protected String subject = "";
    protected String content = "";

    protected Long tramite;
    protected HistoricoTramites ht;
    protected List<TareaWF> tareas;
    protected String usuario = "admin";
    protected Integer anio;
    protected Integer cantidad;
    protected Integer secuencia = 0;

    @PostConstruct
    protected void iniView() {
        try {
            tareas = new ArrayList<>();
            anio = Calendar.getInstance().get(Calendar.YEAR);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarCorreo() {
        try {
            System.out.println("PARAMETROS PRUEBA CORREO:");
            System.out.println(protocol);
            System.out.println(auth);
            System.out.println(tls);
            System.out.println(host);
            System.out.println(port);
            System.out.println(user);
            System.out.println(pass);
            System.out.println(to);
            System.out.println(subject);
            System.out.println(content);

            MailTest test = new MailTest(protocol, auth, tls, host, port, user, pass, to, subject, content);

            if (test.sendMail()) {
                JsfUti.messageInfo(null, "sin errores.", "");
            } else {
                JsfUti.messageWarning(null, "CON ERRORES.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "CON ERRORES.", "");
        }
    }

    public void borrarProceso() {
        try {
            if (tramite == null) {
                JsfUti.messageWarning(null, "Debe ingresar numero de tramite.", "");
                return;
            }

            map = new HashMap();
            map.put("numTramite", tramite);
            ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);

            if (ht == null || ht.getId() == null) {
                JsfUti.messageWarning(null, "No se ha encontrado numero de tramite.", "");
                return;
            }

            System.out.println("id tramite: " + ht.getId());
            System.out.println("nro tramite: " + ht.getNumTramite());
            System.out.println("id proceso: " + ht.getIdProceso());

            this.engine.deleteProcessInstance(ht.getIdProceso(), "Eliminado para pruebas.");
            JsfUti.messageInfo(null, "parece que es exito.", "");

        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "CON ERRORES.", "");
        }
    }

    public void buscarArchivos() {
        try {
            if (tramite == null) {
                JsfUti.messageWarning(null, "Debe ingresar numero de tramite.", "");
                return;
            }

            List<ArchivoDocs> archivos = doc.buscarArchivos(Constantes.indexacionHabilitantes, tramite.toString());
            for (ArchivoDocs temp : archivos) {
                System.out.println("// archivo: " + temp.getNombre());
                for (Imagen img : temp.getImagenes()) {
                    System.out.println("// " + img.getUrlWebService());
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "CON ERRORES.", "");
        }
    }

    public void buscarTramites() {
        try {
            StringBuilder query = new StringBuilder("SELECT id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
            query.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
            query.append("blocked, task_id \"taskId\", proc_inst_id \"procInstId\", form_key \"formKey\", priority, name_ \"name\", candidate ");
            query.append("FROM flow.tareas_activas WHERE assignee = ? and to_char(fecha_ingreso, 'yyyy') = ? ");

            tareas = manager.getNativeQueryParameter(TareaWF.class, query.toString(), new Object[]{usuario, anio.toString()});

            JsfUti.messageInfo(null, "Se encontraron " + tareas.size() + " tramites activos.", "Para el usuario " + usuario + " en el periodo " + anio);
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "CON ERRORES.", "");
        }
    }

    public void borrarTramites() {
        try {
            StringBuilder query = new StringBuilder("SELECT id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
            query.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
            query.append("blocked, task_id \"taskId\", proc_inst_id \"procInstId\", form_key \"formKey\", priority, name_ \"name\", candidate ");
            query.append("FROM flow.tareas_activas WHERE assignee = ? and to_char(fecha_ingreso, 'yyyy') = ? limit 100");

            tareas = manager.getNativeQueryParameter(TareaWF.class, query.toString(), new Object[]{usuario, anio.toString()});

            if (tareas.isEmpty()) {
                JsfUti.messageWarning(null, "No se han encontrado tramites.", "");
                return;
            }
            tareas.forEach(tw -> {
                this.deleteProcessInstance(tw.getProcInstId(), "Tramite realizado en sistema anterior.");
            });

            JsfUti.messageInfo(null, "Se borran hasta 100 tramites a la vez.", "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "CON ERRORES.", "");
        }
    }

    public void secuenciaRepertorio() {
        try {
            SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
            secuencia = sr.getRepertorio();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTls() {
        return tls;
    }

    public void setTls(String tls) {
        this.tls = tls;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

}
