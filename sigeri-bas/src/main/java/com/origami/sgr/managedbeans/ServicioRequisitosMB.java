/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.session.UserSession;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.Servicio;
import com.origami.sgr.entities.ServicioRequisito;
import com.origami.sgr.entities.ServicioTipo;
import com.origami.sgr.entities.TipoContribuyentes;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class ServicioRequisitosMB implements Serializable {

    @Inject
    private Entitymanager em;
    @Inject
    private UserSession userSession;

    private ServicioRequisito servicioRequisito;
    private ServicioTipo servicioTipo;
    private LazyModel<ServicioRequisito> lazy;
    private List<Servicio> servicios;
    private List<ServicioTipo> servicioTipos;
    private Servicio servicio;
    private boolean view = false;
    private String imagenRequisito;

    private UploadedFile file;
    private List<GeDepartamento> departamentos;
    private String imagenItem;
    private TipoContribuyentes tipoContribuyente;
    private List<TipoContribuyentes> tipoContribuyentes;

    private List<ServicioRequisito> listDocServicioRequisito;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        System.out.println("init");
        servicioRequisito = new ServicioRequisito();
        departamentos = em.findAllEntCopy(Querys.getGeDepartamentos);
        this.lazy = new LazyModel<>(ServicioRequisito.class);
        this.lazy.getFilterss().put("activo", true);
        // this.lazy.getSorteds().put("servicioTipo.servicio.departamento.nombre",
        // "ASC");
        servicios = em.findAllEntCopy(Querys.getServiciosActivos);
        imagenRequisito = "";

    }

    public void loadServicioRequisitos() {
        this.view = Boolean.FALSE;
        lazy = new LazyModel<>(ServicioRequisito.class);
        lazy.getFilterss().put("activo", true);
        //lazy.getSorteds().put("servicioTipo", "ASC");
        servicios = em.findAllEntCopy(Querys.getServiciosActivos);
    }

    public void abrirDlg(ServicioRequisito re) {
        view = Boolean.FALSE;
        listDocServicioRequisito = new ArrayList<>();
        servicio = new Servicio();
        if (re != null) {
            servicioRequisito = re;
            servicioRequisito.setServicioTipo(re.getServicioTipo());
            servicio = servicioRequisito.getServicioTipo().getServicio();
            cargarServicioTipos();
            listDocServicioRequisito.add(re);
        } else {
            servicioRequisito = new ServicioRequisito();
        }
        JsfUti.executeJS("PF('dlgNuevoRequisito').show();");
        JsfUti.update("formNuevoRequisito");
        //JsfUtil.update("panelServicioTipo");
    }

    public void vaciarFormulario() {
        servicioRequisito = new ServicioRequisito();
        servicio = new Servicio();
        JsfUti.update("formNuevoRequisito");
    }

    public void cargarServicioTipos() {
        if (servicio != null) {
            servicioTipos = em.findAllEntCopy(Querys.getServiciosTipos, new String[]{"servicio"}, new Object[]{servicio.getId()});
        }
    }

    public Boolean validarCampos() {

        if (servicioRequisito.getNombre().equals("")) {
            JsfUti.messageWarning(null, "NOMBRE", "Ingrese el nombre del requisito");
            return false;
        }
        if (servicio == null || servicio.getId() == null) {
            JsfUti.messageWarning(null, "SERVICIO", "Seleccione un servicio");
            return false;
        }
        if (servicioRequisito.getServicioTipo() == null || servicioRequisito.getServicioTipo().getId() == null) {
            JsfUti.messageWarning(null, "TIPO", "Seleccione el tipo de contribuyente");
            return false;
        }

        return true;
    }

    public void saveUpdate() {
        try {
            boolean edit = servicioRequisito.getId() != null;
            if (validarCampos()) {
                if (edit) {

                    servicioRequisito.setUsuarioModifica(userSession.getName_user());
                    servicioRequisito.setFechaModifica(new Date());
                    em.persist(servicioRequisito);

                    PrimeFaces.current().executeScript("PF('dlgNuevoRequisito').hide()");
                    PrimeFaces.current().ajax().update("requisitosServiciosList");
                    JsfUti.messageInfo(null, "Requisito", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                } else {
                    servicioRequisito.setUsuarioCreacion(userSession.getName_user());
                    servicioRequisito.setFechaCreacion(new Date());
                    servicioRequisito.setActivo(true);

                    em.persist(servicioRequisito);

                    JsfUti.executeJS("PF('dlgNuevoRequisito').hide()");
                    PrimeFaces.current().ajax().update("requisitosServiciosList");
                    JsfUti.messageInfo(null, "Requisito", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                }
            }
        } catch (Exception ex) {
            JsfUti.messageWarning(null, "", "La Transacción no se pudo completar");
        }
    }

    public void delete(ServicioRequisito servicioRequisito) {
        servicioRequisito.setActivo(Boolean.FALSE);
        em.persist(servicioRequisito);
        JsfUti.messageInfo(null, "Requisito", servicioRequisito.getNombre() + " eliminado con éxito");
        PrimeFaces.current().ajax().update("requisitosServiciosList");
    }

    public void selectionServicio(ServicioTipo ser) {
        servicioRequisito.setServicioTipo(ser);
        JsfUti.messageInfo(null, "", "El Servicio se seleccion correctamente");
    }

    public void ver(ServicioRequisito s) {
        servicioRequisito = new ServicioRequisito();
        view = Boolean.TRUE;
        servicioRequisito = s;
        JsfUti.executeJS("PF('dlgNuevoRequisito').show();");
        JsfUti.update("formNuevoRequisito");
    }

    public void handleFileUploadItem(FileUploadEvent event) {
        try {
            imagenItem = event.getFile().getFileName();
            System.out.println("Imagen Item:" + imagenItem);
            //File f = Utils.copyFileServer(event.getFile(), SisVars.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS);
            //servicioRequisito.setUrlDocumento(SisVars.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS + f.getName());
            servicioRequisito.setNombreDocumento(imagenItem);
            PrimeFaces.current().ajax().update("messages");
            JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente");
            // servicioRequisito.setUrlDocumento(SisVars.wsMedia + "resource/image/" +
            // f.getName());
        } catch (Exception ex) {
            JsfUti.messageError(null, "Requisitos", "Ocurrió un error al subir el archivo");
        }
    }

    public void eliminarDocumento(ServicioRequisito re) {
        //FilesUtil.eliminarArchivoServer(re.getUrlDocumento());
        re.setUrlDocumento(null);
        re.setNombreDocumento("");
        // ventanillaService.save(re);
        imagenItem = "";
        //  JsfUtil.addSuccessMessage("Requisito", "Documento eliminado con éxito");
        PrimeFaces.current().ajax().update("formNuevoRequisito");
    }

    public ServicioRequisito getServicioRequisito() {
        return servicioRequisito;
    }

    public void setServicioRequisito(ServicioRequisito servicioRequisito) {
        this.servicioRequisito = servicioRequisito;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipo servicioTipo) {
        this.servicioTipo = servicioTipo;
    }

    public LazyModel<ServicioRequisito> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ServicioRequisito> lazy) {
        this.lazy = lazy;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public List<ServicioTipo> getServicioTipos() {
        return servicioTipos;
    }

    public void setServicioTipos(List<ServicioTipo> servicioTipos) {
        this.servicioTipos = servicioTipos;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getImagenRequisito() {
        return imagenRequisito;
    }

    public void setImagenRequisito(String imagenRequisito) {
        this.imagenRequisito = imagenRequisito;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

    public TipoContribuyentes getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(TipoContribuyentes tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public List<TipoContribuyentes> getTipoContribuyentes() {
        return tipoContribuyentes;
    }

    public void setTipoContribuyentes(List<TipoContribuyentes> tipoContribuyentes) {
        this.tipoContribuyentes = tipoContribuyentes;
    }

    public List<ServicioRequisito> getListDocServicioRequisito() {
        return listDocServicioRequisito;
    }

    public void setListDocServicioRequisito(List<ServicioRequisito> listDocServicioRequisito) {
        this.listDocServicioRequisito = listDocServicioRequisito;
    }

}
