/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.session.UserSession;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.GeTipoTramite;
import com.origami.sgr.entities.Servicio;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils; 
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

/**
 *
 * @author ANDY
 */
@ViewScoped
@Named
public class ServiciosMB implements Serializable {

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession userSession;

    private Servicio servicio;
    private String abreviatura;
    private List<GeDepartamento> departamentos;
    private LazyModel<Servicio> lazyServicios;
    private boolean view = false;
    private String imagenItem;
    private List<Servicio> listDocServicio;
    private List<GeTipoTramite> listTipoTramite;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        departamentos = em.findAllEntCopy(Querys.getGeDepartamentos);
        listTipoTramite = em.findAll(Querys.getGeTipoTramites);
        this.lazyServicios = new LazyModel<>(Servicio.class);
        this.lazyServicios.getFilterss().put("activo", true);
    }

    public void vaciarFormulario() {
        servicio = new Servicio();
        servicio.setDepartamento(null);
        servicio.setEnLinea(Boolean.FALSE);
        abreviatura = "";
        JsfUti.update("formNuevoItem");
    }

    public void abrirDlg(Servicio data) {

        this.view = Boolean.FALSE;
        this.listDocServicio = new ArrayList<>();
        if (data != null) {
            this.servicio = data;
            abreviatura = servicio.getAbreviatura();
            listDocServicio.add(data);
        } else {
            this.servicio = new Servicio();
            abreviatura = "";
        }
        JsfUti.executeJS("PF('dlgNuevoServicio').show();");
        JsfUti.update("formNuevoItem");
    }

    public Boolean validarCampos() {
        if (Utils.isEmptyString(servicio.getNombre())) {
            JsfUti.messageWarning(null, "SERVICIO", "Debe ingresar un nombre al servicio");
            return false;
        }

        if (Utils.isEmptyString(servicio.getAbreviatura())) {
            JsfUti.messageWarning(null, "ABREVIATURA", "Debe ingresar una abreviatura al servicio");
            return false;
        }

        if (servicio.getTipoTramite() == null || servicio.getTipoTramite().getId() == null) {
            JsfUti.messageWarning(null, "TIPO", "Seleccione el tipo de trámite");
            return false;
        }

        return true;
    }

    public void guardarItem() {
        try {
            boolean edit = servicio.getId() != null;
            if (validarCampos()) {
                if (!existeAbreviatura()) {
                    if (edit) {
                        servicio.setUsuarioModifica(userSession.getName_user());
                        servicio.setFechaModificacion(new Date());

                        em.persist(servicio);

                        PrimeFaces.current().executeScript("PF('dlgNuevoServicio').hide()");
                        PrimeFaces.current().ajax().update("dtDatos");
                        JsfUti.messageWarning(null, "SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                        vaciarFormulario();
                    } else {

                        servicio.setUsuarioCreacion(userSession.getName_user());
                        servicio.setFechaCreacion(new Date());
                        servicio.setActivo(true);

                        em.persist(servicio);

                        PrimeFaces.current().executeScript("PF('dlgNuevoServicio').hide()");
                        PrimeFaces.current().ajax().update("dtDatos");
                        JsfUti.messageInfo(null, "SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                        vaciarFormulario();

                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, "Error.", "La Transacción no se pudo completar");
            e.printStackTrace();
        }

    }

    public Boolean existeAbreviatura() {
        if (abreviatura != null && !abreviatura.isEmpty()) {
            if (!abreviatura.equals(servicio.getAbreviatura())) {
                return validarExistente();
            }
        } else {
            return validarExistente();
        }
        return Boolean.FALSE;
    }

    private Boolean validarExistente() {

        Map<String, Object> params = new HashMap<>();
        params.put("abreviatura", servicio.getAbreviatura());

        List<Servicio> listRest = em.findAllEntCopy(Querys.getServiciosAbrv);

        if (!Utils.isEmpty(listRest)) {
            JsfUti.messageWarning(null, "Error",
                    "Ya existe " + servicio.getAbreviatura() + ", debe ingresar otra abreviatura");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void delete(Servicio servicio) {
        System.out.println("delete");
        servicio.setActivo(Boolean.FALSE);
        em.persist(servicio);
        JsfUti.messageInfo(null, "Servicio", servicio.getNombre() + " eliminada con éxito");
        PrimeFaces.current().ajax().update("dtDatos");
    }

    public void ver(Servicio s) {
        this.servicio = new Servicio();
        this.view = Boolean.TRUE;
        this.servicio = s;
        abreviatura = servicio.getAbreviatura();
        JsfUti.executeJS("PF('dlgNuevoServicio').show();");
        JsfUti.update("formNuevoItem");
    }

    public void handleFileUploadItem(FileUploadEvent event) {
//        try {
        imagenItem = event.getFile().getFileName();

//            File f = Utils.copyFileServer(event.getFile(), SisVars.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS);
//            servicio.setUrlImagen(SisVars.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS + f.getName());
//            servicio.setNombreImagen(imagenItem);
        PrimeFaces.current().ajax().update("messages");
        JsfUti.messageWarning(null, "Información", "El archivo se subió correctamente");
        JsfUti.update("formNuevoItem:imagen-item");
//        } catch (IOException ex) {
//            JsfUtil.addErrorMessage("Servicio", "Ocurrió un error al subir el archivo");
//        }
    }

    public void eliminarDocumento(Servicio s) {
        //FileUtil.eliminarArchivoServer(s.getUrlImagen());
        s.setUrlImagen(null);
        // ventanillaService.save(s);
        imagenItem = "";
        JsfUti.messageInfo(null, "Requisito", "Documento eliminado con éxito");
        PrimeFaces.current().ajax().update("formNuevoItem");
    }

    public Servicio getItem() {
        return servicio;
    }

    public void setItem(Servicio item) {
        this.servicio = item;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

    public LazyModel<Servicio> getLazyServicios() {
        return lazyServicios;
    }

    public void setLazyServicios(LazyModel<Servicio> lazyServicios) {
        this.lazyServicios = lazyServicios;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getImagenItem() {
        return imagenItem;
    }

    public void setImagenItem(String imagenItem) {
        this.imagenItem = imagenItem;
    }

    public List<Servicio> getListDocServicio() {
        return listDocServicio;
    }

    public void setListDocServicio(List<Servicio> listDocServicio) {
        this.listDocServicio = listDocServicio;
    }

    public List<GeTipoTramite> getListTipoTramite() {
        return listTipoTramite;
    }

    public void setListTipoTramite(List<GeTipoTramite> listTipoTramite) {
        this.listTipoTramite = listTipoTramite;
    }

}
