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
import com.origami.session.ServletSession;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class VisorDocuments implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private DocumentsEjb doc;
    @Inject
    private ArchivosService archivo;

    private Boolean enableOCR = false;
    protected String nombreBD = "doc_blob_lata";
    private Long idTransaccion, idPadre;
    private BigInteger idBlob;
    private LazyModelDocs<TbBlob> blobsLazy;
    private Map<String, Object> pms;
    private Integer page = 0;
    private String textAux;

    @PostConstruct
    protected void load() {
        try {
            if (ss.tieneParametro("id_transaccion") && ss.tieneParametro("id_padre")) {
                pms = new HashMap<>();
                idTransaccion = Long.parseLong(ss.getParametro("id_transaccion").toString());
                idPadre = Long.parseLong(ss.getParametro("id_padre").toString());
                String sql1 = "select b.id_blob from doc_data_lata.tb_data a, doc_blob_lata.tb_blob_reg b "
                        + "where a.id_blob_reg = b.id_blob_reg and a.id_transaccion = ?";
                pms.put("1", idTransaccion);
                idBlob = doc.find(nombreBD, sql1, pms);
                if (idBlob != null) {
                    pms.clear();
                    pms.put("1", idPadre);
                    pms.put("2", idBlob.longValue());
                    String sql2 = "select a.position from (select id_blob, id_transaccion,ord_salida, ROW_NUMBER() "
                            + "over(partition by id_transaccion order by ord_salida) as position "
                            + "from doc_blob_lata.tb_blob where id_transaccion = ? order by ord_salida) as a "
                            + "where a.id_blob = ?";
                    BigInteger position = doc.find(nombreBD, sql2, pms);
                    page = position.intValue();
                    if (page > 0) {
                        page = page - 1;
                    }
                    pms.clear();
                    pms.put("idTransaccion", idPadre);
                    blobsLazy = new LazyModelDocs<>(nombreBD, TbBlob.class, pms, "ordSalida");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        try {
            this.textAux = archivo.getTextOfImage(this.blobsLazy.getRowData().getStreamBuffer(),
                    e.getX1(), e.getX2(), e.getY1(), e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public LazyModelDocs<TbBlob> getBlobsLazy() {
        return blobsLazy;
    }

    public void setBlobsLazy(LazyModelDocs<TbBlob> blobsLazy) {
        this.blobsLazy = blobsLazy;
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

}
