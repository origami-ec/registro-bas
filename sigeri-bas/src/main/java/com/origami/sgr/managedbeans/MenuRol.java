/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.AppMenu;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.PubGuiMenu;
import com.origami.sgr.entities.PubGuiMenuRol;
import com.origami.sgr.entities.PubGuiMenuTipoAcceso;
import com.origami.sgr.entities.PubGuiMenubar;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.MenuServiceLocal;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Angel Navarro
 */
@Named
@ViewScoped
public class MenuRol implements Serializable {

    private static final Logger LOG = Logger.getLogger(MenuRol.class.getName());

    @Inject
    private MenuServiceLocal menuServ;
    
    @Inject
    private Entitymanager em;
    
    @Inject
    private AppMenu appmenu;

    private List<PubGuiMenu> padre;
    private List<PubGuiMenuRol> roles;
    private PubGuiMenu menu;
    private PubGuiMenuRol menuRol;
    private AclRol rol;
    private List<AclRol> rols;

    @PostConstruct
    public void initView() {
        padre = menuServ.getMenusOrdenados(new PubGuiMenubar(1));
        menu = new PubGuiMenu();
        roles = new ArrayList<>();
    }

    public void iniciarMenu() {
        this.appmenu.clearMenuWorkflow();
        this.appmenu.getMenuWorkflow();
        JsfUti.redirectFaces("/admin/config/menuRol.xhtml");
    }
    
    public void showDlgEditMenu(PubGuiMenu menu) {
        this.menu = menu;
        JsfUti.update("formEditMenu");
        JsfUti.executeJS("PF('dlgEditMenu').show()");
    }

    public void actualizarMenu() {
        try {
            menu = menuServ.guardar(menu);
            if (menu != null) {
                padre = menuServ.getMenusOrdenados(new PubGuiMenubar(1));
                menu = new PubGuiMenu();
                JsfUti.executeJS("PF('dlgEditMenu').hide()");
                JsfUti.update("frmMenuRol");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgEditPermisos(PubGuiMenu menu) {
        this.menu = menu;
        this.roles = menuServ.getAccesosMenuRol(menu);
        JsfUti.update("frmPermisos");
        JsfUti.executeJS("PF('dlgPermisos').show()");
    }

    public void updatePermisos() {
        try {
            if (!roles.isEmpty()) {
                for (PubGuiMenuRol mr : roles) {
                    em.merge(mr);
                }
                padre = menuServ.getMenusOrdenados(new PubGuiMenubar(1));
                menu = new PubGuiMenu();
                roles = new ArrayList<>();
                JsfUti.executeJS("PF('dlgPermisos').hide()");
                JsfUti.update("frmMenuRol");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getNombreRol(Long idRol) {
        if (idRol == null) {
            return "";
        }
        return menuServ.getRolName(idRol);
    }

    public void agregarPermiso() {
        if (rol != null && rol.getId() != null) {
            menuRol = new PubGuiMenuRol();
            menuRol.setMenu(menu);
            menuRol.setRol(rol.getId());
            roles.add(menuRol);
        }
    }

    public void eliminarPermiso(int indice) {
        try {
            PubGuiMenuRol mr = roles.remove(indice);
            if (mr.getId() != null) {
                em.detach(mr.getMenu());
                //em.delete(mr);
                //em.remove(mr);
                menuServ.borrarMenu(mr);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNuevoMenu(PubGuiMenu me) {
        rol = null;
        rols = new ArrayList<>();
        menu = new PubGuiMenu();
        if (me != null) {
            menu.setMenuPadre(me);
        } else {
            menu.setMenubar(new PubGuiMenubar(1));
        }
        JsfUti.update("formNewMenu");
        JsfUti.executeJS("PF('dlgNewMenu').show()");
    }

    public void agregarRol() {
        if (rol != null) {
            rols.add(rol);
        }
    }

    public void eliminarRol(int indice) {
        rols.remove(indice);
    }

    public void saveNewMenu() {
        PubGuiMenuRol mr;
        try {
            menu = (PubGuiMenu) em.persist(menu);
            if (menu != null) {
                for (AclRol r : rols) {
                    mr = new PubGuiMenuRol();
                    mr.setMenu(menu);
                    mr.setRol(r.getId());
                    em.persist(mr);
                }
                padre = menuServ.getMenusOrdenados(new PubGuiMenubar(1));
                menu = new PubGuiMenu();
                roles = new ArrayList<>();
                JsfUti.executeJS("PF('dlgNewMenu').hide()");
                JsfUti.update("frmMenuRol");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void nuevoMenu(Boolean esPadre, PubGuiMenu menu) {
        rols = new ArrayList<>();
        this.menu = new PubGuiMenu();
        this.menu.setMenuPadre(menu);
        JsfUti.executeJS("PF('dlgNewMenu').show()");
        JsfUti.update("formNewMenu");
        JsfUti.update("formNewMenu");
    }

    public List<AclRol> getListRols() {
        return em.findAll(Querys.getAclRolByEstado, new String[]{"estado"}, new Object[]{true});
    }

    public List<PubGuiMenuTipoAcceso> getAccesos() {
        return (List<PubGuiMenuTipoAcceso>) EntityBeanCopy.clone(menuServ.getMenuAccesoList());
    }

    public List<PubGuiMenubar> getMenuBars() {
        return (List<PubGuiMenubar>) EntityBeanCopy.clone(menuServ.getMenuBarList());
    }

    public List<PubGuiMenu> getHijos(PubGuiMenu padre) {
        return menuServ.getMenusOrdenados(padre);
    }

    public List<PubGuiMenu> getPadre() {
        return padre;
    }

    public void setPadre(List<PubGuiMenu> padre) {
        this.padre = padre;
    }

    public PubGuiMenu getMenu() {
        return menu;
    }

    public void setMenu(PubGuiMenu menu) {
        this.menu = menu;
    }

    public AclRol getRol() {
        return rol;
    }

    public void setRol(AclRol rol) {
        this.rol = rol;
    }

    public PubGuiMenuRol getMenuRol() {
        return menuRol;
    }

    public void setMenuRol(PubGuiMenuRol menuRol) {
        this.menuRol = menuRol;
    }

    public List<PubGuiMenuRol> getRoles() {
        return roles;
    }

    public void setRoles(List<PubGuiMenuRol> roles) {
        this.roles = roles;
    }

    public List<AclRol> getRols() {
        return rols;
    }

    public void setRols(List<AclRol> rols) {
        this.rols = rols;
    }

}
