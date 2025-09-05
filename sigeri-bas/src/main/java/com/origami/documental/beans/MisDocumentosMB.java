package com.origami.documental.beans;

import com.origami.config.SisVars;
import com.origami.documental.lazy.LazyModelWS;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.ArchivoIndexCampoDto;
import com.origami.documental.models.ArchivoIndexDto;
import com.origami.documental.models.Indexacion;
import com.origami.documental.models.IndexacionCampo;
import com.origami.documental.services.DocumentalService;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class MisDocumentosMB implements Serializable {

    @Inject
    protected DocumentalService doc;
    @Inject
    protected UserSession session;
    @Inject
    protected ServletSession ss;

    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private List<Indexacion> indices;
    private List<ArchivoDocs> archivosBusqueda;
    private ArchivoDocs archivo;
    private Indexacion indice;
    private String numTramite;

    @PostConstruct
    public void init() {
        try {
            indice = new Indexacion();
            indices = doc.getIndices();
            lazyArchivos = new LazyModelWS<>(SisVars.urlOrigamiDocs + "misDocumentos?usuario.referenceId="
                    + session.getUserId(), session.getToken());
            lazyArchivos.setEntitiArray(ArchivoDocs[].class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminarArchivo(String archivoId) {
        ArchivoDocs temp = doc.eliminarArchivo(new ArchivoDocs(archivoId));
        if (temp != null) {
            JsfUti.messageInfo(null, "Archivo eliminado con éxito", "");
        } else {
            JsfUti.messageError(null, "Intente nuevamente", "");
        }
        init();
    }

    public void consultarArchivo(String archivoId) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("archivoId", archivoId);
        JsfUti.redirectFaces("procesos/documentos/documento.xhtml");
    }

    public void dlgConvertirArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
        JsfUti.executeJS("PF('dlgConversion').show()");
        JsfUti.update("frmConversion");
    }

    public void convertirArchivo() {
        if (Utils.isNotEmptyString(archivo.getConvertir())) {
            archivo = doc.convertirArchivo(new ArchivoDocs(archivo.getId(), archivo.getConvertir()));
            if (archivo != null && Utils.isNotEmptyString(archivo.getRutaConvertido())) {
                JsfUti.redirectNewTab(Utils.getUrlDownloadFile(archivo.getRutaConvertido()));
            } else {
                JsfUti.messageError(null, "Intente nuevamente", "");
            }
        } else {
            JsfUti.messageError(null, "Debe escoger el tipo de conversión", "");
        }

    }

    public void visualizarDocumento(ArchivoDocs docs) {
        JsfUti.redirectNewTab(SisVars.urlbase + "/documental/digitalizacion/viewerDocs.xhtml?archivoId=" + docs.getId());
    }

    public void buscarArchivos() {
        if (indice == null || indice.get_id() == null) {
            JsfUti.messageError(null, Messages.escojaIndice, "");
            return;
        }
        archivosBusqueda = new ArrayList<>();
        ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
        archivoIndex.setNumTramite(numTramite.trim());
        archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo().trim());
        archivoIndex.setEstado(Boolean.TRUE);
        archivoIndex.setTipoIndexacion(indice.getDescripcion());
        List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
        for (IndexacionCampo ic : indice.getCampos()) {
            detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(),
                    ic.getDetalle(), ic.getObligatorio()));
        }
        archivoIndex.setDetalles(detalles);

        archivosBusqueda = doc.busquedaAvanzada(archivoIndex);
        if (archivosBusqueda == null) {
            JsfUti.messageError(null, "No se encontraron datos", "");
            archivosBusqueda = new ArrayList<>();
        }
    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {
    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public void getPDF(String ruta) {
        if (ruta == null) {
            JsfUti.messageInfo(null, "Error.", "No existe el documento");
            return;
        }
        String[] tipo = ruta.split("\\.");
        String lastOne = tipo[tipo.length - 1];

        if (lastOne.equals("zip")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo ZIP no se puede visualizar, Debe descargarlo");
            return;
        } else if (lastOne.equals("doc") || lastOne.equals("docx") || lastOne.equals("xls") || lastOne.equals("xlsx")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo " + tipo[1] + " no se puede visualizar, Debe descargarlo");
            JsfUti.redirectNewTab(Utils.getUrlDownloadFile(ruta));
            return;
        }
        ss.borrarDatos();
        ss.setUrlWebService(Utils.getUrlViewFile(ruta));
        JsfUti.redirectNewTab(SisVars.urlbase + "ReporteWS");
    }

    public LazyModelWS<ArchivoDocs> getLazyArchivos() {
        return lazyArchivos;
    }

    public void setLazyArchivos(LazyModelWS<ArchivoDocs> lazyArchivos) {
        this.lazyArchivos = lazyArchivos;
    }

    public List<Indexacion> getIndices() {
        return indices;
    }

    public void setIndices(List<Indexacion> indices) {
        this.indices = indices;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Indexacion getIndice() {
        return indice;
    }

    public void setIndice(Indexacion indice) {
        this.indice = indice;
    }

    public List<ArchivoDocs> getArchivosBusqueda() {
        return archivosBusqueda;
    }

    public void setArchivosBusqueda(List<ArchivoDocs> archivosBusqueda) {
        this.archivosBusqueda = archivosBusqueda;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

}
