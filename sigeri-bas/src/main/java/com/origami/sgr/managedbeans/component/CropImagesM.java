/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.documental.ejbs.DocumentsEjb;
import com.origami.documental.entities.TbBlob;
import com.origami.documental.entities.TbData;
import com.origami.documental.lazy.LazyModelDocs;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class CropImagesM implements Serializable {

    private static final Logger LOG = Logger.getLogger(CropImagesM.class.getName());

    @Inject
    private ArchivosService archivoHist;

    @Inject
    private DocumentsEjb de;

    private Long id;
    private Integer numRepertorio;
    private Integer numInscripcion;
    private Long fechaIns;
    private Date fechaInscripcion;
    private Boolean enableOCR = false;
    private String textAux;

    private Long idTransaccion;
    private Long idFilter;
    private List<TbData> archivos;
    private LazyModelDocs<TbBlob> blobsLazy;
    private Map<String, Object> pms;
    protected String dbData = "doc_data_lata";
    protected String dbBlob = "doc_blob_lata";

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    public void initView() {
        try {
            fechaInscripcion = new Date(fechaIns);
            archivos = de.getDataFiles(this.dbData, fechaInscripcion, id, numInscripcion, numRepertorio);
            if (Utils.isNotEmpty(archivos)) {
                pms = new HashMap<>();
                TbData data = archivos.get(0);
                idTransaccion = data.getIdTransaccion();
                if (data.getIdPadre() != null && data.getIdPadre().longValue() > 0l) {
                    try {
                        pms.put("1", data.getIdBlobReg());
                        String sql = "SELECT b.ord_salida FROM " + this.dbBlob + ".tb_blob_reg br "
                                + "INNER JOIN " + this.dbBlob + ".tb_blob b ON b.id_blob = br.id_blob WHERE br.id_blob_reg = ?";
                        Integer page = de.find(this.dbBlob, sql, pms);
                        pms.clear();
                        pms.put("idTransaccion", data.getIdPadre().longValue());
                        final DataGrid d = (DataGrid) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmMainCrop:dgPaguinas");
                        d.setFirst(page - 1);
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Init view", e);
                    }
                } else {
                    pms.put("idTransaccion", idTransaccion);
                }
                blobsLazy = new LazyModelDocs<>(this.dbBlob, TbBlob.class, pms);
            }
            pms = new HashMap<>();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void refrescBlobs() {
        if (idTransaccion != null) {
            pms = new HashMap<>();
            pms.put("idTransaccion", idTransaccion);
            blobsLazy = new LazyModelDocs<>(this.dbBlob, TbBlob.class, pms);
        }
        pms = new HashMap<>();
    }

    public void buscarBlobs() {
        if (idFilter != null) {
            pms = new HashMap<>();
            pms.put("idTransaccion", idFilter);
            archivos = de.getDataFiles(pms);
            if (Utils.isNotEmpty(archivos)) {
                idTransaccion = archivos.get(0).getIdTransaccion();
                Map<String, Object> pmsTemp = new HashMap<>();
                pmsTemp.put("idTransaccion", idTransaccion);
                blobsLazy = new LazyModelDocs<>(this.dbBlob, TbBlob.class, pmsTemp);
            }
        }
    }

    public void borrarParam() {
        pms.clear();
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        try {
            this.textAux = archivoHist.getTextOfImage(this.blobsLazy.getRowData().getStreamBuffer(), 
                    e.getX1(), e.getX2(), e.getY1(), e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                // Mostramos el texto
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
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

    public Boolean getEnableOCR() {
        return enableOCR;
    }

    public void setEnableOCR(Boolean enableOCR) {
        this.enableOCR = enableOCR;
    }

    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

    public List<TbData> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<TbData> archivos) {
        this.archivos = archivos;
    }

    public DocumentsEjb getDe() {
        return de;
    }

    public void setDe(DocumentsEjb de) {
        this.de = de;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LazyModelDocs<TbBlob> getBlobsLazy() {
        return blobsLazy;
    }

    public void setBlobsLazy(LazyModelDocs<TbBlob> blobsLazy) {
        this.blobsLazy = blobsLazy;
    }

    public Long getIdFilter() {
        return idFilter;
    }

    public void setIdFilter(Long idFilter) {
        this.idFilter = idFilter;
    }

}
