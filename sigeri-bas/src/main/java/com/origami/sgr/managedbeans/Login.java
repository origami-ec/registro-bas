/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclLogin;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class Login implements Serializable {

    @EJB
    private Entitymanager manager;

    private String user;
    private String pass;
    private List<Long> roles, depts;
    private AclLogin aclLogin;

    @Inject
    private UserSession sess;

    public void doLogin() {
        try {
            if (user != null && pass != null) {
                //AclUser u = (AclUser) manager.find(Querys.getUsuariobyUserPass, new String[]{"user", "pass"}, new Object[]{user, pass});
                pass = Utils.encriptaEnMD5(pass);
                AclUser u = (AclUser) manager.find(Querys.getUsuariobyUserClave, new String[]{"user", "clave"}, new Object[]{user, pass});
                if (u != null) {
                    if (u.isCaducadaPass()) {
                        sess.setUserId(u.getId());
                        redirectRecuperarClave();
                        return;
                    } else {
                        Calendar c = Calendar.getInstance();
                        c.setTime(u.getFechaActPass());
                        Map<String, Object> map = new HashMap();
                        map.put("code", Constantes.diasCaducidadPass);
                        Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                        if (valor == null) {
                            JsfUti.messageError(null, "No se ha podido validar el usuario.", "");
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, "Error variable no encontrada");
                            return;
                        }
                        c.add(Calendar.DAY_OF_MONTH, valor.getValorNumeric().intValue());
                        if (c.getTime().compareTo(new Date()) <= 0) {
                            sess.setUserId(u.getId());
                            u.setCaducadaPass(true);
                            manager.merge(u);
                            redirectRecuperarClave();
                            return;
                        }
                    }
                    aclLogin = new AclLogin();
                    //sess = (UserSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSession");
                    sess.setUserId(u.getId());
                    sess.setName_user(user);
                    sess.setTemp(true);
                    sess.setEsDirector(u.getUserIsDirector());
                    if (u.getEnte() != null) {
                        sess.setNombrePersonaLogeada(u.getEnte().getNombres() + " " + u.getEnte().getApellidos());
                    } else {
                        sess.setNombrePersonaLogeada("");
                    }
                    roles = new ArrayList<>();
                    depts = new ArrayList<>();
                    if (!u.getAclRolCollection().isEmpty()) {
                        for (AclRol r : u.getAclRolCollection()) {
                            roles.add(r.getId());
                            if (r.getDepartamento() != null) {
                                depts.add(r.getDepartamento().getId());
                            }
                        }
                    }
                    sess.setRoles(roles);
                    sess.setDepts(depts);
                    sess.setEsSuperUser(u.getEsSuperUser());

                    aclLogin.setIpUserSession(sess.getDatosEquipo());
                    aclLogin.setUserSessionId(BigInteger.valueOf(u.getId()));
                    aclLogin.setUserSessionName(sess.getName_user());
                    aclLogin.setFechaDoLogin(new Date());
                    aclLogin.setMacClient(sess.getMACAddressEquipo());
                    aclLogin = (AclLogin) manager.persist(aclLogin);
                    sess.setAclLogin(aclLogin);
                    sess.setIdAclLogin(aclLogin.getId());
                    manager.getNativeQuery("SELECT audit.set_appuser(?)", new Object[]{sess.getName_user()});
                    if (u.getImagenPerfil() != null) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/image/usuarios") + File.separator + u.getId() + ".jpg";
                        File f = new File(u.getImagenPerfil());
                        if (f.length() != 0L) {
                            try {
                                byte[] resultado = new byte[(int) f.length()];
                                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(f));
                                stream.read(resultado);
                                FileImageOutputStream imageOutput;
                                imageOutput = new FileImageOutputStream(new File(path));
                                imageOutput.write(resultado, 0, resultado.length);
                                imageOutput.flush();
                                imageOutput.close();
                                sess.setPerfil("/resources/image/usuarios/" + u.getId() + ".jpg");
                            } catch (IOException e) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
                            }
                        } else {
                            sess.setPerfil("/resources/image/avatar.jpg");
                        }
                    } else {
                        sess.setPerfil("/resources/image/avatar.jpg");
                    }
                    if (sess.getRoles().contains(53L)) {
                        sess.setUrlSolicitada("procesos/dashBoardStatistics.xhtml");
                    }
                    sess.redirectUrlSolicitada();
                } else {
                    JsfUti.messageError(null, Messages.credencialesInvalidas, "");
                }
            } else {
                JsfUti.messageError(null, Messages.credencialesInvalidas, "");
            }
        } catch (Exception e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void redirectRecuperarClave() {
        sess.setActKey(pass);
        JsfUti.messageError(null, "Su contraseña a caducado", "");
        JsfUti.redirect(SisVars.urlbase + "admin/manage/recuperarClave.xhtml");
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

    public UserSession getSess() {
        return sess;
    }

    public void setSess(UserSession sess) {
        this.sess = sess;
    }
}
