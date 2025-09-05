/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.mail.entities.Correo;
import com.origami.mail.entities.CorreoUsuarios;
import com.origami.sgr.entities.MsgFormatoNotificacion;
import com.origami.sgr.entities.MsgTipoFormatoNotificacion;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class ConfigurarCorreos implements Serializable {

    @Inject
    private Entitymanager em;

    protected CorreoUsuarios correo;
    protected MsgFormatoNotificacion notificacion;
    protected MsgTipoFormatoNotificacion tipoFormato;
    protected LazyModel<Correo> logs;
    protected LazyModel<CorreoUsuarios> correos;
    protected LazyModel<MsgFormatoNotificacion> formatos;
    protected List<MsgTipoFormatoNotificacion> tiposFormatos;

    @PostConstruct
    protected void iniView() {
        try {
            logs = new LazyModel(Correo.class, "fechaEnvio", "DESC");
            correos = new LazyModel(CorreoUsuarios.class, "id", "ASC");
            formatos = new LazyModel(MsgFormatoNotificacion.class, "id", "ASC");
            tiposFormatos = em.findAll(Querys.getTiposNotificacionesMail);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgNuevo() {
        correo = new CorreoUsuarios();
        JsfUti.update("formEdicion");
        JsfUti.executeJS("PF('dlgEdicion').show();");
    }

    public void showDlgEdit(CorreoUsuarios temp) {
        correo = temp;
        JsfUti.update("formEdicion");
        JsfUti.executeJS("PF('dlgEdicion').show();");
    }

    public void guardarCorreo() {
        try {
            if (correo.getServidor() != null && correo.getPuerto() != null && correo.getUsuario() != null
                    && correo.getClave() != null && correo.getSsl() != null && correo.getRazonSocial() != null) {
                em.persist(correo);
                this.iniView();
                JsfUti.update("mainForm:tabViewCorreos:dtCorreos");
                JsfUti.executeJS("PF('dlgEdicion').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar todos los campos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgNuevoFormato() {
        notificacion = new MsgFormatoNotificacion();
        JsfUti.update("formEdicionFormato");
        JsfUti.executeJS("PF('dlgEdicionFormato').show();");
    }

    public void showDlgEditFormato(MsgFormatoNotificacion temp) {
        notificacion = temp;
        JsfUti.update("formEdicionFormato");
        JsfUti.executeJS("PF('dlgEdicionFormato').show();");
    }

    public void guardarFormato() {
        try {
            if (notificacion.getHeader() != null && notificacion.getFooter() != null && notificacion.getAsunto() != null
                    && notificacion.getTipo() != null) {
                em.persist(notificacion);
                this.iniView();
                JsfUti.update("mainForm:tabViewCorreos:dtFormatos");
                JsfUti.executeJS("PF('dlgEdicionFormato').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar todos los campos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public LazyModel<Correo> getLogs() {
        return logs;
    }

    public void setLogs(LazyModel<Correo> logs) {
        this.logs = logs;
    }

    public CorreoUsuarios getCorreo() {
        return correo;
    }

    public void setCorreo(CorreoUsuarios correo) {
        this.correo = correo;
    }

    public MsgFormatoNotificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(MsgFormatoNotificacion notificacion) {
        this.notificacion = notificacion;
    }

    public LazyModel<CorreoUsuarios> getCorreos() {
        return correos;
    }

    public void setCorreos(LazyModel<CorreoUsuarios> correos) {
        this.correos = correos;
    }

    public LazyModel<MsgFormatoNotificacion> getFormatos() {
        return formatos;
    }

    public void setFormatos(LazyModel<MsgFormatoNotificacion> formatos) {
        this.formatos = formatos;
    }

    public MsgTipoFormatoNotificacion getTipoFormato() {
        return tipoFormato;
    }

    public void setTipoFormato(MsgTipoFormatoNotificacion tipoFormato) {
        this.tipoFormato = tipoFormato;
    }

    public List<MsgTipoFormatoNotificacion> getTiposFormatos() {
        return tiposFormatos;
    }

    public void setTiposFormatos(List<MsgTipoFormatoNotificacion> tiposFormatos) {
        this.tiposFormatos = tiposFormatos;
    }

}
