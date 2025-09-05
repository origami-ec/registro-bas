/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclLogin;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class RecuperarClave implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecuperarClave.class.getName());

    private String pass;
    private String passConfirm;
    @Inject
    private UserSession sess;
    @Inject
    private Entitymanager manager;

    @PostConstruct
    public void initView() {
        if (!JsfUti.isAjaxRequest()) {
            if (sess.getUserId() == null) {
                JsfUti.redirect(SisVars.urlbase);
                return;
            }
        }
    }

    public String getPass() {
        return pass;
    }

    public Boolean validar() {
        if (sess.getUserId() == null) {
            JsfUti.redirect(SisVars.urlbase);
            return false;
        }
        if (Utils.isEmpty(pass).isEmpty()) {
            JsfUti.messageError(null, "Debe Ingresar la contraseña", "");
            return false;
        }
        if (Utils.isEmpty(passConfirm).isEmpty()) {
            JsfUti.messageError(null, "Debe confirmar la contraseña", "");
            return false;
        }
        if (!pass.equals(passConfirm)) {
            JsfUti.messageError(null, "La contraseña es igual a la anterior por favor ingrese una nueva.", "");
            return false;
        }
        if (sess.getActKey().equals(Utils.encriptaEnMD5(pass))) {
            JsfUti.messageError(null, "La contraseña es igual a la anterior por favor ingrese una nueva.", "");
            return false;
        }
        if (pass.length() <= 7) {
            System.out.println("Test. 4");
            JsfUti.messageError(null, "La contraseña debe tener por lo menos 8 caracteres.", "");
            return false;
        }
        return true;
    }

    public void actualizarPass() {
        try {
            if (validar()) {
                AclUser user = manager.find(AclUser.class, sess.getUserId());
                user.setFechaActPass(new Date());
                user.setCaducadaPass(false);
                user.setClave(Utils.encriptaEnMD5(pass));
                manager.merge(user);
                sess.setActKey(null);
                sess.setUserId(null);
                AclLogin aclLogin = new AclLogin();
                aclLogin.setEvento("Cambio de clave");
                aclLogin.setIpUserSession(sess.getDatosEquipo());
                aclLogin.setUserSessionId(BigInteger.valueOf(user.getId()));
                aclLogin.setUserSessionName(sess.getName_user());
                aclLogin.setFechaDoLogin(new Date());
                aclLogin.setMacClient(sess.getMACAddressEquipo());
                manager.persist(aclLogin);
                JsfUti.redirect(SisVars.urlbase);
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }

}
