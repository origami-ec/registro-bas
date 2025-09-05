/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.beans;

import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.ArchivoIndexCampoDto;
import com.origami.documental.models.ArchivoIndexDto;
import com.origami.documental.models.Documentos;
import com.origami.documental.models.Indexacion;
import com.origami.documental.models.IndexacionCampo;
import com.origami.documental.models.Nota;
import com.origami.documental.models.Tesauro;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class ArchivosMB extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ArchivosMB.class.getName());

    private UploadedFile file;
    private Date fecha;
    private ArchivoDocs archivo;
    private Nota nota;

    private Indexacion indice;
    private List<Indexacion> indices;
    private List<UploadedFile> uploadedfiles;
    private List<Tesauro> sugerencias;
    private Documentos archivoDocuments;

    private Long nroTramite;
    private HistoricoTramites tramite;

    @PostConstruct
    public void init() {
        try {
            sugerencias = new ArrayList<>();
            archivos = new ArrayList();
            fecha = new Date();
            nota = new Nota();
            tramite = null;
            //indices = doc.getIndices();
            indice = doc.getIndiceByDescripcion(Constantes.indexacionHabilitantes);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarArchivo(FileUploadEvent event) {
        try {
            if (this.validaciones()) {
                this.file = null;
                //System.out.println("event.getFile().getContentType():  " + event.getFile().getContentType());
                UploadedFile upfile = event.getFile();
                if (upfile != null && upfile.getContent() != null && upfile.getContent().length > 0 && upfile.getFileName() != null) {
                    //System.out.println("file: " + upfile.getContentType());
                    this.file = upfile;
                    JsfUti.messageInfo(null, "Archivo cargado correctamente.", "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarArchivoMasivo(FileUploadEvent event) {
        if (event != null) {
            uploadedfiles.add(event.getFile());
            JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
        }
    }

    public void grabarArchivo() {
        try {
            if (this.validaciones()) {
                if (file != null) {
                    archivo = doc.guardarArchivo(file, this.crearAchivoDto(file.getContentType()));
                    if (archivo != null) {
                        if (Utils.isNotEmpty(archivo.getImagenes())) {
                            /*ss.borrarDatos();
                            ss.instanciarParametros();
                            ss.agregarParametro("archivoId", archivo.getId());*/
                            JsfUti.redirectFaces("/documental/digitalizacion/viewerDocs.xhtml?archivoId=" + archivo.getId());
                        } else if (Utils.isNotEmptyString(archivo.getId())) {
                            JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
                        } else {
                            JsfUti.messageError(null, Messages.intenteNuevamente, "");
                        }
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                    }
                } else {
                    JsfUti.messageError(null, Messages.subirArchivo, "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void grabarArchivoMasivo() {
        if (validaciones()) {
            if (Utils.isNotEmpty(archivos)) {
                ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
                archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo());
                archivoIndex.setEstado(Boolean.TRUE);
                archivoIndex.setTipoIndexacion(indice.getDescripcion());
                List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
                for (IndexacionCampo ic : indice.getCampos()) {
                    detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(),
                            ic.getDetalle(), ic.getObligatorio()));
                }
                archivoIndex.setDetalles(detalles);
                for (UploadedFile f : uploadedfiles) {
                    archivo = doc.guardarArchivo(f, archivoIndex);
                    if (archivo != null) {
                        if (Utils.isEmpty(archivo.getImagenes())) {
                            JsfUti.messageError(null, Messages.intenteNuevamente, "");
                            return;
                        }
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                        return;
                    }
                }
                JsfUti.messageError(null, "Archivos cargados correctamente", "");
            } else {
                JsfUti.messageError(null, Messages.subirArchivo, "");
            }
        }
    }

    public ArchivoIndexDto crearAchivoDto(String contentType) {
        try {
            ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
            //archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo());
            archivoIndex.setDetalleDocumento(file.getFileName());
            archivoIndex.setEstado(Boolean.TRUE);
            archivoIndex.setTipoIndexacion(indice.getDescripcion());
            archivoIndex.setFormatoUpload(contentType);
            archivoIndex.setNumTramite(tramite.getNumTramite().toString());
            if (tramite.getTipoTramite() != null) {
                archivoIndex.setTramite(tramite.getTipoTramite().getDescripcion());
            }
            List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
            for (IndexacionCampo ic : indice.getCampos()) {
                detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(),
                        archivoIndex.getNumTramite(), ic.getObligatorio()));
            }
            archivoIndex.setDetalles(detalles);
            return archivoIndex;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
            return null;
        }
    }

    public Boolean validaciones() {
        /*if (indice == null) {
            JsfUti.messageError(null, Messages.escojaIndice, "");
            return Boolean.FALSE;
        }
        if (Utils.isEmptyString(indice.getDescripcionArchivo())) {
            JsfUti.messageError(null, Messages.descripcionArchivo, "");
            return Boolean.FALSE;
        }
        for (IndexacionCampo ic : indice.getCampos()) {
            if (ic.getObligatorio()) {
                if (Utils.isEmptyString(ic.getDetalle())) {
                    JsfUti.messageError(null, Messages.camposObligatorios, "");
                    return Boolean.FALSE;
                }
            }
        }*/
        if (tramite == null || tramite.getId() == null) {
            JsfUti.messageError(null, "Debe ingresar un número de trámite correcto.", "");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {

    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public void validarCampoTesauro() {
        sugerencias = new ArrayList<>();
        if (indice != null && Utils.isNotEmptyString(indice.getDescripcionArchivo())) {
            String[] palabras = indice.getDescripcionArchivo().split(" ");
            for (String s : palabras) {
                s = s.trim().toLowerCase();
                Tesauro tes = doc.validarIndexacionTesauro(new Tesauro(s));
                if (tes != null) {
                    if (!sugerencias.contains(tes)) {
                        sugerencias.add(tes);
                    }
                }
            }
        }
    }

    public void abrirDocuments() {
        try {
            archivoDocuments = new Documentos();
            archivoDocuments.setEstado("0");
            archivoDocuments.setFecha(new Date());
            archivoDocuments.setUsuario(session.getName_user());
            archivoDocuments.setIp(session.getIpClient());
            archivoDocuments = doc.guardarDocumento(archivoDocuments);
            JsfUti.messageInfo(null, "Datos generados correctamente", "");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarArchivo(Documentos documentos) {
        try {
            if (validaciones()) {
                String contentType = "application/pdf";
                String nombreArchivo = new Date().getTime() + "_"
                        + StringUtils.stripAccents(indice.getDescripcionArchivo()).replace(" ", "_")
                                .replace("-", "_").concat(".pdf");
                archivo = doc.guardarArchivo(contentType, nombreArchivo, documentos.getArchivo(), crearAchivoDto(contentType));
                if (archivo != null) {
                    if (Utils.isNotEmpty(archivo.getImagenes())) {
                        ss.borrarDatos();
                        ss.instanciarParametros();
                        ss.agregarParametro("archivoId", archivo.getId());
                        JsfUti.redirectFaces("documental/digitalizacion/viewerDocs.xhtml");
                    } else if (Utils.isNotEmptyString(archivo.getId())) {
                        JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                    }
                } else {
                    JsfUti.messageError(null, Messages.intenteNuevamente, "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void getDownloadFile(Long idDocuments) {
        JsfUti.redirectNewTab(SisVars.urlOrigamiZuul + "documents/pdf/" + idDocuments);
    }

    public void buscarTramite() {
        try {
            if (nroTramite != null) {
                map = new HashMap();
                map.put("numTramite", nroTramite);
                tramite = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                if (tramite != null && tramite.getId() != null) {
                    JsfUti.messageInfo(null, "Trámite Nro: " + tramite.getNumTramite() + " encontrado.", "");
                } else {
                    tramite = null;
                    JsfUti.messageError(null, "No se encuentra el número de trámite.", "");
                }
            } else {
                JsfUti.messageError(null, "Debe ingresar número de trámite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public List<Indexacion> getIndices() {
        return indices;
    }

    public void setIndices(List<Indexacion> indices) {
        this.indices = indices;
    }

    public Indexacion getIndice() {
        return indice;
    }

    public void setIndice(Indexacion indice) {
        this.indice = indice;
    }

    public List<UploadedFile> getUploadedfiles() {
        return uploadedfiles;
    }

    public void setUploadedfiles(List<UploadedFile> uploadedfiles) {
        this.uploadedfiles = uploadedfiles;
    }

    public Documentos getArchivoDocuments() {
        return archivoDocuments;
    }

    public void setArchivoDocuments(Documentos archivoDocuments) {
        this.archivoDocuments = archivoDocuments;
    }

    public List<Tesauro> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(List<Tesauro> sugerencias) {
        this.sugerencias = sugerencias;
    }

    public Long getNroTramite() {
        return nroTramite;
    }

    public void setNroTramite(Long nroTramite) {
        this.nroTramite = nroTramite;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

}
