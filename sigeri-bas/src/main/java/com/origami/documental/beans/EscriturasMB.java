/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.beans;

import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.ArchivoIndexCampoDto;
import com.origami.documental.models.ArchivoIndexDto;
import com.origami.documental.models.Indexacion;
import com.origami.documental.models.IndexacionCampo;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.NamedItem;
import com.origami.sgr.util.Constantes;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author origami
 */
@Named
@ViewScoped
public class EscriturasMB extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(EscriturasMB.class.getName());

    protected Indexacion indice;
    protected UploadedFile file;
    protected ArchivoDocs archivo;
    protected RegMovimiento movimiento;
    protected List<RegLibro> regLibroList;
    protected RegLibro libro;
    protected Date inscripcionDesde;
    protected Date inscripcionHasta;
    protected SimpleDateFormat sdf;
    protected Calendar calendar;
    protected Integer desde;
    protected Integer hasta;

    protected Calendar cal;
    protected LazyModel<RegMovimiento> foliosLazy;
    protected Integer inscripcion, repertorio, anioDesde, anioHasta;
    protected String contrato;

    @PostConstruct
    public void initView() {
        try {
            cal = Calendar.getInstance();
            calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(2023, 8, 11); //fecha de salida a produccion - 11/09/2023
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            regLibroList = manager.findAllEntCopy(Querys.getRegLibroList);
            indice = doc.getIndiceByDescripcion(Constantes.indexacionInscripciones);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarFolios() {
        try {
            foliosLazy = new LazyModel(RegMovimiento.class);
            if (libro != null && libro.getId() != null) {
                foliosLazy.addFilter("libro", libro);
            }
            if (inscripcionDesde != null && inscripcionHasta != null) {
                if (!inscripcionDesde.after(inscripcionHasta)) {
                    if (inscripcionHasta.after(calendar.getTime())) {
                        foliosLazy.addFilter("fechaInscripcion:between", Arrays.asList(inscripcionDesde,
                                this.getFechaHasta(inscripcionHasta)));
                    } else {
                        foliosLazy.addFilter("fechaInscripcion:between", Arrays.asList(inscripcionDesde, inscripcionHasta));
                    }
                }
            }
            if (!contrato.isEmpty()) {
                foliosLazy.addFilter("acto.nombre:startsWith", contrato.toUpperCase());
            }
            if (inscripcion != null && inscripcion > 0) {
                foliosLazy.addFilter("numInscripcion", inscripcion);
            }
            if (repertorio != null && repertorio > 0) {
                foliosLazy.addFilter("numRepertorio", repertorio);
            }
            if (anioDesde != null && anioDesde > 0 && anioHasta != null && anioHasta > 0) {
                foliosLazy.addFilter("fechaInscripcion:between", Arrays.asList(this.fechaInicioAnio(anioDesde),
                        this.fechaFinAnio(anioHasta)));
            } else if (anioDesde != null && anioDesde > 0) {
                foliosLazy.addFilter("fechaInscripcion:between", Arrays.asList(this.fechaInicioAnio(anioDesde),
                        this.fechaFinAnio(cal.get(Calendar.YEAR))));
            } else if (anioHasta != null && anioHasta > 0) {
                foliosLazy.addFilter("fechaInscripcion:between", Arrays.asList(this.fechaInicioAnio(anioHasta),
                        this.fechaFinAnio(anioHasta)));
            }
            foliosLazy.addSorted("fechaInscripcion", "ASC");
            JsfUti.update("formMain:dtFolios");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Date getFechaHasta(Date actual) {
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(actual);
        fecha.add(Calendar.DAY_OF_MONTH, 1);
        fecha.add(Calendar.SECOND, -1);
        return fecha.getTime();
    }

    public Date fechaInicioAnio(Integer anio) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(anio, 0, 1, 0, 0, 0);
        return fecha.getTime();
    }

    public Date fechaFinAnio(Integer anio) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(anio, 11, 31, 23, 59, 59);
        return fecha.getTime();
    }

    public void showDlgFolioSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            if (movimiento.getValorUuid() == null) {
                movimiento.setValorUuid(Utils.getValorUuid());
                manager.update(movimiento);
            }
            JsfUti.update("formMovRegSelec");
            JsfUti.executeJS("PF('dlgMovRegSelec').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarPdfFolios() {
        try {
            if (desde == null && hasta == null) {
                JsfUti.messageError(null, "Debe ingresa el desde y hasta.", "");
                return;
            }
            if (desde > hasta) {
                JsfUti.messageError(null, "Desde no debe ser mayor al hasta.", "");
                return;
            }
            ss.instanciarParametros();
            ss.setNombreReporte("foliacion");
            ss.setTieneDatasource(false);
            ss.setNombreSubCarpeta("archivos");
            ss.agregarParametro("ciRuc", ("Folio:" + desde + "-" + hasta));
            List<NamedItem> list = new ArrayList<>();
            for (int i = desde; i <= hasta; i++) {
                list.add(new NamedItem(new BigInteger(String.valueOf(i))));
            }
            ss.setDataSource(list);
            ss.agregarParametro("desde", desde);
            ss.agregarParametro("hasta", hasta);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
//            JsfUti.redirectNewTab(SisVars.urlbase + "PdfFolio");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void updateMovimiento() {
        try {
            if (movimiento.getFolioInicio() != null && movimiento.getFolioFin() != null
                    && !movimiento.getNumTomo().isEmpty()) {
                manager.update(movimiento);
                JsfUti.update("formMain");
                JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
                JsfUti.messageInfo(null, "Se actualizó el movimiento con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar los 3 campos obligatorios(*).", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void documentalDSM(RegMovimiento mov) {
        try {
            if (mov.getFolioInicio() != null && mov.getFolioFin() != null && !mov.getNumTomo().isEmpty()) {
                Long result = 0L;
                //Long result = rps.insertDSM(mov);
                if (result != null) {
                    mov.setFolioAnterior(result.intValue());
                    manager.update(mov);
                    JsfUti.messageInfo(null, "Se registró el indice DSM.", "Id de transaccion: " + result);
                } else {
                    JsfUti.messageWarning(null, "No se puedo registrar el indice DSM.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Primero debe estar foliada la inscripcion antes de indexar.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageWarning(null, "ERROR DE TRANSACCION.", "");
        }
    }

    public void visualizarDocumento(String archivoId) {
        if (archivoId != null && !archivoId.isEmpty()) {
            JsfUti.redirectNewTab(SisVars.urlbase + "/documental/digitalizacion/viewerDocs.xhtml?archivoId=" + archivoId);
        } else {
            JsfUti.messageWarning(null, "No se encuentra documento digitalizado.", "");
        }
    }
    
    public void verHistorial(RegMovimiento mov) {
        try {
            if (mov.getApp() == null || mov.getApp().isEmpty()) {
                JsfUti.messageWarning(null, "No se ha digitalizado la inscripción.", "");
                return;
            }
            if (mov.getValorUuid() != null && !mov.getValorUuid().isEmpty()) {
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/digitalizacion/reviewDocs.xhtml?transaccion="
                        + Constantes.indexacionInscripciones + "&tramite=" + mov.getValorUuid());
            } else {
                JsfUti.messageWarning(null, "No se puede visualizar el documento.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void indexarMovimiento(Long movimientoId) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("movimientoId", movimientoId);
        JsfUti.redirectFacesNewTab("/documental/indexacion/consultaIndice.xhtml");
    }

    public void cargarArchivo(FileUploadEvent event) {
        try {
            this.file = null;
            UploadedFile upfile = event.getFile();
            if (upfile != null && upfile.getContent() != null && upfile.getContent().length > 0 && upfile.getFileName() != null) {
                this.file = upfile;
                JsfUti.messageInfo(null, "Archivo cargado correctamente.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void grabarArchivo() {
        try {
            if (file != null) {
                archivo = doc.guardarArchivo(file, this.crearAchivoDto(file.getContentType()));
                if (archivo != null) {
                    if (Utils.isNotEmptyString(archivo.getId())) {
                        movimiento.setApp(archivo.getId());
                        manager.update(movimiento);
                        JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
                        JsfUti.update("formMain");
                        JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                    }
                } else {
                    JsfUti.messageError(null, Messages.intenteNuevamente, "");
                }
            } else {
                JsfUti.messageError(null, Messages.subirArchivo, "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public ArchivoIndexDto crearAchivoDto(String contentType) {
        try {
            ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
            archivoIndex.setDetalleDocumento(file.getFileName());
            archivoIndex.setEstado(Boolean.TRUE);
            archivoIndex.setTipoIndexacion(indice.getDescripcion());
            archivoIndex.setFormatoUpload(contentType);
            if (movimiento.getTramite() != null) {
                archivoIndex.setNumTramite(movimiento.getNumeroTramite().toString());
            }
            if (movimiento.getActo() != null) {
                archivoIndex.setTramite(movimiento.getActo().getNombre());
            }
            List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
            for (IndexacionCampo ic : indice.getCampos()) {
                detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(),
                        movimiento.getValorUuid(), ic.getObligatorio()));
            }
            archivoIndex.setDetalles(detalles);
            return archivoIndex;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
            return null;
        }
    }

    public void eliminarArchivo(String archivoId) {
        ArchivoDocs temp = doc.eliminarArchivo(new ArchivoDocs(archivoId));
        if (temp != null) {
            movimiento.setApp(null);
            manager.update(movimiento);
            JsfUti.messageInfo(null, "Archivo eliminado con éxito", "");
        } else {
            JsfUti.messageError(null, "Intente nuevamente", "");
        }
    }

    public LazyModel<RegMovimiento> getFoliosLazy() {
        return foliosLazy;
    }

    public void setFoliosLazy(LazyModel<RegMovimiento> foliosLazy) {
        this.foliosLazy = foliosLazy;
    }

    public List<RegLibro> getRegLibroList() {
        return regLibroList;
    }

    public void setRegLibroList(List<RegLibro> regLibroList) {
        this.regLibroList = regLibroList;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public Date getInscripcionDesde() {
        return inscripcionDesde;
    }

    public void setInscripcionDesde(Date inscripcionDesde) {
        this.inscripcionDesde = inscripcionDesde;
    }

    public Date getInscripcionHasta() {
        return inscripcionHasta;
    }

    public void setInscripcionHasta(Date inscripcionHasta) {
        this.inscripcionHasta = inscripcionHasta;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Integer getDesde() {
        return desde;
    }

    public void setDesde(Integer desde) {
        this.desde = desde;
    }

    public Integer getHasta() {
        return hasta;
    }

    public void setHasta(Integer hasta) {
        this.hasta = hasta;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
    }

}
