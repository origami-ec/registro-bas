/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.session.UserSession;
import com.origami.sgr.entities.AclLogin;
import com.origami.sgr.entities.AclRegistroUser;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.GeDepartamento; 
import com.origami.sgr.lazymodels.GeDepartamentosLazy;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB(beanName = "manager")
    private Entitymanager em;

    @Inject
    protected UserSession us;

    private LazyModel<AclUser> allUsers;
    private LazyModel<AclUser> usuariosLazy;
    private AclUser acluser;
    private AclRol rol;
    private CatEnte ente;
    private List<AclRol> rolsUser;
    private String passOne;
    private String passTwo;
    private String cedula;
    private String usuario;
    private String motivo;

    private String palabra;
    private String cambio;

    private LazyModel<AclRol> roles;
    private GeDepartamentosLazy depts;
    private GeDepartamento dept;

    @PostConstruct
    protected void iniView() {
        allUsers = new LazyModel(AclUser.class);
        usuariosLazy =  new LazyModel(AclUser.class);
        acluser = new AclUser();
        roles = new LazyModel<>(AclRol.class, "nombre", "ASC");
        depts = new GeDepartamentosLazy();
        rol = new AclRol();
        dept = new GeDepartamento();
    }

    public void selectUsuario(AclUser u) {
        PrimeFaces.current().dialog().closeDynamic(u);
    }

    public void inicializarVariables() {
        acluser = new AclUser();
        ente = new CatEnte();
        rolsUser = new ArrayList<>();
        rol = null;
        passOne = "";
        passTwo = "";
        cedula = "";
        usuario = "";
    }

    public void showDlgConsulta(AclUser us, String urlFacelet) {
        acluser = us;
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "60%");
        options.put("closable", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public void selectEnte(SelectEvent event) {
        CatEnte en = (CatEnte) event.getObject();
        if (en != null) {
            if (acluser.getId() != null) {
                acluser.setEnte(en);
                em.update(acluser);
                usuariosLazy = new LazyModel(AclUser.class);
            }
        }
    }

    public void showDlgNewUser() {
        this.inicializarVariables();
        JsfUti.update("frmNewUsr");
        JsfUti.executeJS("PF('dlgNUsr').show();");
    }

    public void showDlgEditUser(AclUser u) {
        acluser = u;
        passOne = "";
        passTwo = "";
        JsfUti.update("formCambioClave");
        JsfUti.executeJS("PF('dlgCambioClave').show();");
    }

    public void showDlgRegistroUser(AclUser u) {
        acluser = u;
        JsfUti.update("formEstadoUser");
        JsfUti.executeJS("PF('dlgEstadoUser').show();");
    }

    public void showDlgViewUser(AclUser u) {
        acluser = u;
        JsfUti.update("formInfoUser");
        JsfUti.executeJS("PF('dlgInfoUser').show();");
    }

    public void nuevaClave() {
        passOne = RandomStringUtils.randomAlphanumeric(10);
        passTwo = passOne;
    }

    public void cambioClave() {
        try {
            if (acluser.getUsuario() != null) {
                if (Utils.isEmpty(passOne).length() != 0 && Utils.isEmpty(passTwo).length() != 0) {
                    passOne = Utils.encriptaEnMD5(passOne);
                    acluser.setClave(passOne);
                    acluser.setCaducadaPass(true);
                    em.update(acluser);
                    usuariosLazy = new LazyModel(AclUser.class);
                    AclLogin aclLogin = new AclLogin();
                    aclLogin.setEvento("Cambio de clave");
                    aclLogin.setIpUserSession(us.getAclLogin().getIpUserSession());
                    aclLogin.setUserSessionId(BigInteger.valueOf(acluser.getId()));
                    aclLogin.setUserSessionName(us.getName_user());
                    aclLogin.setFechaDoLogin(new Date());
                    aclLogin.setMacClient(us.getAclLogin().getMacClient());
                    em.persist(aclLogin);
                    this.inicializarVariables();
                    JsfUti.messageInfo(null, Messages.transaccionOK, "");
                    JsfUti.executeJS("PF('dlgCambioClave').hide();");
                    JsfUti.update("formUsuarios");

                } else {
                    JsfUti.messageError(null, "Debe genarar la nueva clave", "");
                }
            } else {
                JsfUti.messageError(null, Messages.faltanCampos, "");
            }
        } catch (Exception e) {
            Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgEditarRoles(AclUser u) {
        this.inicializarVariables();
        acluser = u;
        if (acluser.getAclRolCollection() != null) {
            for (AclRol r : acluser.getAclRolCollection()) {
                rolsUser.add(r);
            }
        }
        JsfUti.update("formCambioRol");
        JsfUti.executeJS("PF('dlgRoles').show();");
    }

    public void agregarRol() {
        if (rol == null) {
            JsfUti.messageInfo(null, Messages.campoVacio, "");
        } else {
            if (rolsUser.contains(rol)) {
                JsfUti.messageInfo(null, Messages.elementoRepetido, "");
            } else {
                rolsUser.add(rol);
            }
        }
    }

    public void eliminarRol(AclRol r) {
        rolsUser.remove(r);
    }

    public void editarRoles() {
        try {
            if (rolsUser.isEmpty()) {
                JsfUti.messageInfo(null, Messages.noListaVacia, "");
            } else {
                acluser.setAclRolCollection(rolsUser);
                em.update(acluser);
                usuariosLazy = new LazyModel(AclUser.class);
                this.inicializarVariables();
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
                JsfUti.executeJS("PF('dlgRoles').hide();");
                JsfUti.update("formUsuarios");
            }
        } catch (Exception e) {
            Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDetalleUser(AclUser u) {
        if (u.getEnte() == null) {
            JsfUti.messageError(null, Messages.noAsignadoPersona, "");
        } else {
            this.inicializarVariables();
            ente = u.getEnte();
            JsfUti.update("formDetalleCliente");
            JsfUti.executeJS("PF('dlgDetalleClient').show();");
        }
    }

    public void buscarEnte() {
        if (cedula != null) {
            ente = (CatEnte) em.find(Querys.getEnteByIdent, new String[]{"ciRuc"}, new Object[]{cedula});
            if (ente == null) {
                ente = new CatEnte();
                JsfUti.messageError(null, Messages.sinCoincidencias, "");
            }
        } else {
            JsfUti.messageError(null, Messages.campoVacio, "");
        }
    }

    public Boolean userDisponible() {
        AclUser u = (AclUser) em.find(Querys.getAclUserByUser, new String[]{"user"}, new Object[]{usuario});
        return u == null;
    }

    public void comprobarUsuario() {
        if (usuario != null) {
            if (this.userDisponible()) {
                JsfUti.messageInfo(null, Messages.userDisponible, "");
            } else {
                JsfUti.messageError(null, Messages.enteExiste, "");
            }
        } else {
            JsfUti.messageError(null, Messages.campoVacio, "");
        }
    }

    public void guardarNuevoUser() {
        try {
            if (ente.getId() != null) {
                if (usuario != null) {
                    if (this.userDisponible()) {
                        if (!rolsUser.isEmpty()) {
                            passOne = RandomStringUtils.randomAlphanumeric(10);
                            passTwo = passOne;
//                                if (passOne.equals(passTwo)) {
                            acluser.setSisEnabled(true);
                            acluser.setUserIsDirector(false);
                            acluser.setUsuario(usuario);
                            passOne = Utils.encriptaEnMD5(passOne);
                            acluser.setClave(passOne);
                            acluser.setPass(passOne); // setteo de validacion para usuarios nuevos
                            acluser.setCaducadaPass(true);
                            acluser.setFechaActPass(new Date());
                            acluser.setEnte(ente);
                            acluser.setAclRolCollection(rolsUser);
                            acluser = (AclUser) em.persist(acluser);
                            if (acluser.getId() != null) {
                                usuariosLazy =  new LazyModel(AclUser.class);
                                // this.inicializarVariables();
                                JsfUti.messageInfo(null, Messages.transaccionOK, "");
                                JsfUti.update("formUsuarios");
                                JsfUti.executeJS("PF('dlgNUsr').hide();");
                                JsfUti.update("fmrInfoUserPass");
                                JsfUti.executeJS("PF('dlgInfoUserPass').show();");
                            } else {
                                JsfUti.messageError(null, Messages.problemaConexion, "");
                            }
//                                } else {
//                                    JsfUti.messageError(null, Messages.noCoincidenClaves, "");
//                                }
                        } else {
                            JsfUti.messageInfo(null, "El usuario debe tener ingresado mínimo un Rol.", "");
                        }
                    } else {
                        JsfUti.messageError(null, Messages.enteExiste, "");
                    }
                } else {
                    JsfUti.messageError(null, "Debe ingresar el nombre de Usuario.", "");
                }
            } else {
                JsfUti.messageError(null, Messages.noAsignadoPersona, "");
            }
        } catch (Exception e) {
            Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void updatePassMd5() {
        try {
            List<AclUser> users = em.findAll(AclUser.class);
            for (AclUser u : users) {
                u.setClave(Utils.encriptaEnMD5(u.getPass()));
                em.update(u);
            }
            JsfUti.messageInfo(null, "Fin!!!", "");
        } catch (Exception e) {
            Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgRol() {
        rol = new AclRol();
        JsfUti.update("formRolsUser");
        JsfUti.executeJS("PF('dlgRolUser').show();");
    }

    public void showDlgEditRol(AclRol ro) {
        rol = ro;
        JsfUti.update("formRolsUser");
        JsfUti.executeJS("PF('dlgRolUser').show();");
    }

    public List<GeDepartamento> getDepartamentos() {
        return em.findAllEntCopy(Querys.getGeDepartamentos);
    }

    public void saveRoles() {
        try {
            if (rol == null) {
                JsfUti.messageWarning(null, "Ingrese el rol.", "");
                return;
            }
            if (rol.getNombre() == null) {
                JsfUti.messageWarning(null, "Ingrese el nombre del rol.", "");
                return;
            }
            if (rol.getDepartamento() == null) {
                JsfUti.messageWarning(null, "Ingrese el departamento", "");
                return;
            }
            em.persist(rol);
            roles = new LazyModel<>(AclRol.class, "nombre", "ASC");
            JsfUti.update("formUsuarios:accMantenimiento:dtRoles");
            JsfUti.executeJS("PF('dlgRolUser').hide();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgDept() {
        dept = new GeDepartamento();
        JsfUti.update("formDepts");
        JsfUti.executeJS("PF('dlgDepts').show();");
    }

    public void showDlgEditDept(GeDepartamento de) {
        dept = de;
        JsfUti.update("formDepts");
        JsfUti.executeJS("PF('dlgDepts').show();");
    }

    public void saveDepts() {
        try {
            if (dept == null) {
                JsfUti.messageWarning(null, "Ingrese el departamento", "");
                return;
            }
            if (dept.getNombre() == null && !dept.getNombre().isEmpty()) {
                JsfUti.messageWarning(null, "Ingrese el nombre del departamento", "");
                return;
            }
            em.persist(dept);
            depts = new GeDepartamentosLazy();
            JsfUti.update("formUsuarios:accMantenimiento:dtDepts");
            JsfUti.executeJS("PF('dlgDepts').hide();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void registrarUsuario() {
        AclRegistroUser reg;
        try {
            if (motivo != null) {
                reg = new AclRegistroUser();
                reg.setMotivo(motivo);
                reg.setUsuario(acluser);
                reg.setUserIngreso(us.getUserId());
                reg.setFechaIngreso(new Date());
                em.persist(reg);
                em.update(acluser);
                usuariosLazy =  new LazyModel(AclUser.class);
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
                JsfUti.executeJS("PF('dlgEstadoUser').hide();");
                JsfUti.update("formUsuarios");
            } else {
                JsfUti.messageWarning(null, "Ingrese el motivo de cambio de estado.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public LazyModel<AclUser> getUsuariosLazy() {
        return usuariosLazy;
    }

    public void setUsuariosLazy(LazyModel<AclUser> usuariosLazy) {
        this.usuariosLazy = usuariosLazy;
    }

    public AclUser getAcluser() {
        return acluser;
    }

    public void setAcluser(AclUser acluser) {
        this.acluser = acluser;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public String getPassOne() {
        return passOne;
    }

    public void setPassOne(String passOne) {
        this.passOne = passOne;
    }

    public String getPassTwo() {
        return passTwo;
    }

    public void setPassTwo(String passTwo) {
        this.passTwo = passTwo;
    }

    public AclRol getRol() {
        return rol;
    }

    public void setRol(AclRol rol) {
        this.rol = rol;
    }

    public List<AclRol> getListRols() {
        return em.findAll(Querys.getAclRolByEstado, new String[]{"estado"}, new Object[]{true});
    }

    public List<AclRol> getRolsUser() {
        return rolsUser;
    }

    public void setRolsUser(List<AclRol> rolsUser) {
        this.rolsUser = rolsUser;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LazyModel<AclUser> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(LazyModel<AclUser> allUsers) {
        this.allUsers = allUsers;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public LazyModel<AclRol> getRoles() {
        return roles;
    }

    public void setRoles(LazyModel<AclRol> roles) {
        this.roles = roles;
    }

    public GeDepartamentosLazy getDepts() {
        return depts;
    }

    public void setDepts(GeDepartamentosLazy depts) {
        this.depts = depts;
    }

    public GeDepartamento getDept() {
        return dept;
    }

    public void setDept(GeDepartamento dept) {
        this.dept = dept;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

}
