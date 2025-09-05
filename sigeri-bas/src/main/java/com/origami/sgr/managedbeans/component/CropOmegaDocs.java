/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.Imagen;
import com.origami.documental.services.DocumentalService;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class CropOmegaDocs implements Serializable {

    @Inject
    private DocumentalService doc;
    @Inject
    private ArchivosService arc;

    private String tramite;
    private String transaccion;
    private String textAux;
    private Boolean enableOCR = false;
    private List<Imagen> imagenes;
    private Integer pagina = 0;
    private Integer indice = 0;
    private List<ArchivoDocs> archivos;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    public void initView() {
        try {
            pagina = 0;
            if (indice != null) {
                pagina = indice;
            }
            if (transaccion != null && tramite != null) {
                archivos = doc.buscarArchivos(transaccion, tramite);
                imagenes = archivos.get(pagina).getImagenes();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void seleccionarImagenes(ArchivoDocs doc) {
        try {
            imagenes = doc.getImagenes();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void descargarDocumento(ArchivoDocs doc) {
        try {
            JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/pdf/" + doc.getNombre());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectPage(PageEvent event) {
        try {
            pagina = event.getPage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        try {
            //String archivo = imagenes.get(pagina).getArchivo();
            String archivo = imagenes.get(pagina).getUrlWebService();
            this.textAux = arc.getTextOfImage(archivo, e.getX1(), e.getX2(),
                    e.getY1(), e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                // Mostramos el texto
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            System.out.println(e);
        }
    }

    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public Boolean getEnableOCR() {
        return enableOCR;
    }

    public void setEnableOCR(Boolean enableOCR) {
        this.enableOCR = enableOCR;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public List<ArchivoDocs> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoDocs> archivos) {
        this.archivos = archivos;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

}
