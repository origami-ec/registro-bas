/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.beans.CertificadosRp;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.origami.sgr.entities.UsuariosApp;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.util.logging.Level;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class UsuariosAppMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(CertificadosRp.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;

    @Inject
    private Entitymanager em;

    @Inject
    protected RegistroPropiedadServices reg;

    private LazyModel<UsuariosApp> lazy;
    private String obs;
    private UsuariosApp usuarioApp;

    @PostConstruct
    protected void iniView() {
        load();
    }

    public void load() {
        lazy = new LazyModel<>(UsuariosApp.class);
        obs = "";
    }

    public void rechazarDlg(UsuariosApp usuario) {
        this.usuarioApp = usuario;
        JsfUti.update("formObs");
        JsfUti.executeJS("PF('dlgObervaciones').show();");
    }

    public void autorizar(UsuariosApp usuario) {
        usuario.setAprueba(Boolean.TRUE);
        Boolean x = reg.actualizarUsuarioApp(usuario, "");
        if (x) {
            JsfUti.messageInfo(null, "Usuario actualizado", "");
        } else {
            JsfUti.messageError(null, "Intente nuevamente", "");
        }
        usuarioApp = null;
        JsfUti.update("mainForm");
    }

    public void rechazar() {
        usuarioApp.setAprueba(Boolean.FALSE);
        Boolean x = reg.actualizarUsuarioApp(usuarioApp, obs);
        if (x) {
            JsfUti.messageInfo(null, "Usuario actualizado", "");
        } else {
            JsfUti.messageError(null, "Intente nuevamente", "");
        }
        JsfUti.executeJS("PF('dlgObervaciones').hide();");
        usuarioApp = null;
        JsfUti.update("mainForm");

    }

    public void descargar(UsuariosApp usuario) {
        try {
            if (usuario.getDocumento() != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + usuario.getDocumento() + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public LazyModel<UsuariosApp> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<UsuariosApp> lazy) {
        this.lazy = lazy;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public UsuariosApp getUsuarioApp() {
        return usuarioApp;
    }

    public void setUsuarioApp(UsuariosApp usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

}
