/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.TarUsuarioTareas;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.primefaces.event.SelectEvent;
import com.origami.sgr.entities.TarTareasAsignadas;
import java.math.BigInteger;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class TareasUsuarios implements Serializable {

    private static final Logger LOG = Logger.getLogger(TareasUsuarios.class.getName());

    @Inject
    private Entitymanager manager;

    private LazyModel<TarUsuarioTareas> usuariosTareas;
    private TarUsuarioTareas seleccionado;
    private Boolean edicion = false;
    private List<AclRol> roles;
    private AclRol rolFilter;
    private Long rolTemp = 0L;
    private Boolean estado;
    private Map map;

    @PostConstruct
    public void initView() {
        try {
            if (!JsfUti.isAjaxRequest()) {
                usuariosTareas = new LazyModel<>(TarUsuarioTareas.class, "id", "ASC");
                reloadUsuario();
                //roles = manager.findAll(Querys.getAclRolByEstado, new String[]{"estado"}, new Object[]{true});
                roles = manager.findAll(Querys.getAclRolAsignacion, new String[]{"estado"}, new Object[]{true});
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void reloadUsuario() {
        rolTemp = 0L;
        seleccionado = new TarUsuarioTareas();
        seleccionado.setUsuario(new AclUser());
        seleccionado.getUsuario().setEnte(new CatEnte());
        seleccionado.setRol(new AclRol());
    }

    public void buscarUsuario(String url) {
        JsfUti.openDialogFrame(url);
    }

    public void procesar(SelectEvent event) {
        try {
            AclUser user = (AclUser) event.getObject();
            map = new HashMap();
            map.put("usuario", user);
            TarUsuarioTareas tut = (TarUsuarioTareas) manager.findObjectByParameter(TarUsuarioTareas.class, map);
            if (tut == null) {
                seleccionado.setUsuario(user);
            } else {
                JsfUti.messageWarning(null, "Usuario ya esta registrado.",
                        "Usuario: " + tut.getUsuario().getUsuario() + "; Rol: " + tut.getRol().getNombre());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarUser() {
        if (rolFilter != null) {
            map = new HashMap<>();
            map.put("rol", rolFilter);
            usuariosTareas = new LazyModel<>(TarUsuarioTareas.class, "id", "ASC");
            usuariosTareas.setFilterss(map);
        } else {
            usuariosTareas = new LazyModel<>(TarUsuarioTareas.class, "id", "ASC");
        }
        JsfUti.update("frmMain:dtgTerasUsuarios");
    }

    public void guardar() {
        if (seleccionado.getRol() == null) {
            JsfUti.messageError(null, "", "Debe seleccionar el rol.");
            return;
        }
        if (seleccionado.getUsuario() == null) {
            JsfUti.messageError(null, "", "Debe seleccionar el usuario.");
            return;
        }
        try {
            if (seleccionado.getId() == null) {
                seleccionado.setEstado(true);
                seleccionado.setFecha(new Date());
                seleccionado = (TarUsuarioTareas) manager.persist(seleccionado);
            } else {
                seleccionado.setFecha(new Date());
                manager.persist(seleccionado);
            }
            if (seleccionado.getEstado()) {
                map = new HashMap();
                map.put("usuarioTareas", seleccionado);
                map.put("fecha", new Date());
                TarTareasAsignadas tta = (TarTareasAsignadas) manager.findObjectByParameter(TarTareasAsignadas.class, map);
                if (tta == null) {
                    tta = new TarTareasAsignadas();
                    tta.setCantidad(BigInteger.ZERO);
                    tta.setFecha(new Date());
                    tta.setPeso(BigInteger.ZERO);
                    tta.setUsuarioTareas(seleccionado);
                    manager.persist(tta);
                }
            }
            JsfUti.messageInfo(null, "", "Datos almacenados correctamente.");
        } catch (Exception e) {
            JsfUti.messageError(null, "", "Error al guadar los datos.");
            LOG.log(Level.SEVERE, "", e);
        }
        reloadUsuario();
    }

    public void dlgSeleccionado(TarUsuarioTareas tta) {
        reloadUsuario();
        try {
            if (tta != null) {
                Hibernate.initialize(tta);
                Hibernate.initialize(tta.getRol());
                seleccionado = tta;
                rolTemp = tta.getRol().getId();
                estado = tta.getEstado();
            }
        } catch (HibernateException he) {
            LOG.log(Level.SEVERE, "", he);
        }
        JsfUti.executeJS("PF('usuarioTar').show()");
        JsfUti.update("frmDetUser");
    }

    public List<AclRol> getRoles() {
        return roles;
    }

    public void setRoles(List<AclRol> roles) {
        this.roles = roles;
    }

    public LazyModel<TarUsuarioTareas> getUsuariosTareas() {
        return usuariosTareas;
    }

    public void setUsuariosTareas(LazyModel<TarUsuarioTareas> usuariosTareas) {
        this.usuariosTareas = usuariosTareas;
    }

    public TarUsuarioTareas getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(TarUsuarioTareas seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Boolean getEdicion() {
        return edicion;
    }

    public void setEdicion(Boolean edicion) {
        this.edicion = edicion;
    }

    public AclRol getRolFilter() {
        return rolFilter;
    }

    public void setRolFilter(AclRol rolFilter) {
        this.rolFilter = rolFilter;
    }

}
