/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.session.UserSession;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.Servicio;
import com.origami.sgr.entities.ServicioTipo;
import com.origami.sgr.entities.TipoContribuyentes;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class TipoContribuyenteMB implements Serializable {

    @Inject
    private Entitymanager em;
    @Inject
    private UserSession userSession;

    private List<Servicio> servicios;

    private List<GeDepartamento> departamentos;

    private List<TipoContribuyentes> tiposContribuyentes;

    private ServicioTipo servicioTipo;

    private LazyModel<ServicioTipo> lazyServicioTipo;

    private Boolean view;

    @PostConstruct
    public void init() {
        if (!JsfUti.isAjaxRequest()) {
            loadModel();
        }
    }

    public void loadModel() {
        servicios = em.findAll(Servicio.class); //cambiar x filtro d los activos
        departamentos = em.findAllEntCopy(Querys.getGeDepartamentos);
        tiposContribuyentes = em.findAll(TipoContribuyentes.class); //cambiar x filtro d los activos
        servicioTipo = new ServicioTipo();

        this.lazyServicioTipo = new LazyModel<>(ServicioTipo.class);
        this.lazyServicioTipo.getFilterss().put("estado", true);
    }

    public void vaciarFormulario() {
        servicioTipo = new ServicioTipo();
        JsfUti.update("formNuevoItem");
    }

    public void abrirDlg(ServicioTipo data) {
        this.view = Boolean.FALSE;
        if (data != null) {
            this.servicioTipo = data;
        } else {
            this.servicioTipo = new ServicioTipo();

        }
        JsfUti.executeJS("PF('dlgNuevoTipo').show();");
        JsfUti.update("formNuevoItem");
    }

    public Boolean validarCampos() {

        if (servicioTipo.getServicio() == null || servicioTipo.getServicio().getId() == null) {
            JsfUti.messageError(null, "SERVICIO", "Seleccione un servicio");
            return false;
        }
        if (servicioTipo.getTipoContribuyentes() == null || servicioTipo.getTipoContribuyentes().getId() == null) {
            JsfUti.messageError(null, "TIPO", "Seleccione el tipo de contribuyente");
            return false;
        }

        if (servicioTipo.getServicio() != null || servicioTipo.getServicio().getId() != null
                && (servicioTipo.getTipoContribuyentes() != null || servicioTipo.getTipoContribuyentes().getId() != null)) {

            Map<String, Object> params = new HashMap<>();
            params.put("tipo", servicioTipo.getTipoContribuyentes().getId());
            params.put("servicio", servicioTipo.getServicio().getId());
//            List<ServicioTipo> listRest = (List<ServicioTipo>) ventanillaService
//                    .findAllQuery("SELECT s FROM ServicioTipo s  WHERE servicio.id=:servicio AND tipoContribuyentes.id =:tipo AND estado = true", params);
//
//            if (!Utils.isEmpty(listRest)) {
//                JsfUti.messageError(null, "Error",
//                        "Ya existe el servicio " + servicioTipo.getServicio().getNombre() + " con el tipo de contribuyente " + servicioTipo.getTipoContribuyentes().getNombre());
//                return false;
//            }
        }

        return true;
    }

    public void guardarItem() {
        try {
            boolean edit = servicioTipo.getId() != null;

            if (validarCampos()) {
                if (edit) {

                    em.persist(servicioTipo);

                    PrimeFaces.current().executeScript("PF('dlgNuevoTipo').hide()");
                    PrimeFaces.current().ajax().update("dtDatos");
                    JsfUti.messageInfo(null, "SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                } else {

                    System.out.println("entro add");

                    servicioTipo.setUsuarioCreacion(userSession.getName_user());
                    servicioTipo.setFechaCreacion(new Date());
                    servicioTipo.setEstado(true);

                    em.persist(servicioTipo);

                    PrimeFaces.current().executeScript("PF('dlgNuevoTipo').hide()");
                    PrimeFaces.current().ajax().update("dtDatos");
                    JsfUti.messageInfo(null, "SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();

                }
            }

        } catch (Exception e) {
            JsfUti.messageError(null, "Error.", "La Transacción no se pudo completar");
            e.printStackTrace();
        }

    }

    public void delete(ServicioTipo servicioTipo) {

        servicioTipo.setEstado(Boolean.FALSE);
        em.persist(servicioTipo);

        JsfUti.messageInfo(null, "Servicio", "Servicio: " + servicioTipo.getServicio().getNombre() + ", Tipo de contribuyente:" + servicioTipo.getTipoContribuyentes().getNombre() + " eliminada con éxito");
        PrimeFaces.current().ajax().update("dtDatos");
    }

    public void ver(ServicioTipo s) {
        this.servicioTipo = new ServicioTipo();
        this.view = Boolean.TRUE;
        this.servicioTipo = s;
        JsfUti.executeJS("PF('dlgNuevoTipo').show();");
        JsfUti.update("formNuevoItem");
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipo servicioTipo) {
        this.servicioTipo = servicioTipo;
    }

    public LazyModel<ServicioTipo> getLazyServicioTipo() {
        return lazyServicioTipo;
    }

    public void setLazyServicioTipo(LazyModel<ServicioTipo> lazyServicioTipo) {
        this.lazyServicioTipo = lazyServicioTipo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<TipoContribuyentes> getTiposContribuyentes() {
        return tiposContribuyentes;
    }

    public void setTiposContribuyentes(List<TipoContribuyentes> tiposContribuyentes) {
        this.tiposContribuyentes = tiposContribuyentes;
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

}
