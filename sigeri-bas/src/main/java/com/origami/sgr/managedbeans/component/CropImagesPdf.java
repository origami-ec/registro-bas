/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.models.index.TbBlob;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class CropImagesPdf implements Serializable {

    @Inject
    private ArchivosService archivoHist;
    @Inject
    private ServletSession ss;

    private static final Logger LOG = Logger.getLogger(CropImagesPdf.class.getName());

    private Long idBlodSelect;
    private Long id;
    private Integer numRepertorio;
    private Integer numInscripcion;
    private Long fechaIns;
    private Date fechaInscripcion;
    private Boolean enableCrop = false;
    private String textAux;

    private List<TbBlob> archivos;
    private List<Integer> paginas;
    private TbBlob seleccionado;

//    @PostConstruct
    public void initView() {
        //id=187&numRepertorio=17269&numInscripcion=189&fechaIns=1515128400000
        fechaInscripcion = new Date(fechaIns);
        this.archivos = archivoHist.getImageMovimiento(id, numRepertorio, numInscripcion, fechaInscripcion);
        if (Utils.isNotEmpty(archivos)) {
            idBlodSelect = archivos.get(0).getIdBLob();
            generarPaguinas();
        }
    }

    public void generarPaguinas() {
        try {
            seleccionado = archivos.get(archivos.indexOf(new TbBlob(idBlodSelect)));
            paginas = new ArrayList<>();
            for (int i = 1; i <= seleccionado.getImagenes(); i++) {
                paginas.add(i);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "paginas", e);
        }
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        try {
            this.textAux = archivoHist.getTextOfImage(ss.getNombreDocumento(), e.getX1(), e.getX2(), e.getY1(), e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                // Mostramos el texto
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            Logger.getLogger(CropImagesPdf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UIComponent findComponent(final String id) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        for (UIComponent component : root.getChildren()) {
            if (component.getId().contains(id)) {
                return component;
            }
        }
        return null;
    }

    public void selectObject(RegEnteJudiciales e) {
        PrimeFaces.current().dialog().closeDynamic(e);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Long getFechaIns() {
        return fechaIns;
    }

    public void setFechaIns(Long fechaIns) {
        this.fechaIns = fechaIns;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public List<TbBlob> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<TbBlob> archivos) {
        this.archivos = archivos;
    }

    public Long getIdBlodSelect() {
        return idBlodSelect;
    }

    public void setIdBlodSelect(Long idBlodSelect) {
        this.idBlodSelect = idBlodSelect;
    }

    public List<Integer> getPaginas() {
        return paginas;
    }

    public void setPaginas(List<Integer> paginas) {
        this.paginas = paginas;
    }

    public TbBlob getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(TbBlob seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Boolean getEnableCrop() {
        return enableCrop;
    }

    public void setEnableCrop(Boolean enableCrop) {
        this.enableCrop = enableCrop;
    }

    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

}
