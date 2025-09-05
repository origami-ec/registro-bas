/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dfcalderio
 */
@Named
@ViewScoped
public class ReporteArchivo implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReporteArchivo.class.getName());

    @Inject
    protected Entitymanager em;
    @Inject
    protected ServletSession ss;
    @Inject
    private UserSession us;

    protected Map map;
    protected RegRegistrador registrador;
    protected Date fechaActa = new Date();
    protected Date fechaDesde = new Date();
    protected Date fechaHasta = new Date();
    protected Boolean historico = true;
    protected Boolean inicio = true;
    protected String libro;
    protected Calendar cal = Calendar.getInstance();
    protected Integer folio = 0, desde = 0, hasta = 0, anio = 0, tomo = 0, inscripcionDesde = 0, inscripcionHasta = 0;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected Date desdeIndice, hastaIndice;
    protected RegLibro libroIndice;
    protected Integer tipoIndice = 0, tipoOrder = 1, tipoRepertorio = 0;
    protected boolean excel = false;

    @PostConstruct
    protected void iniView() {
        map = new HashMap();
        map.put("actual", Boolean.TRUE);
        registrador = (RegRegistrador) em.findObjectByParameter(RegRegistrador.class, map);
    }

    public void imprimirRepertorios(Boolean encuadernacion, Integer margen) {
        try {
            if (fechaDesde != null && fechaHasta != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setEncuadernacion(encuadernacion);
                ss.setMargen(margen);
                if (historico) {    //PROPIEDAD
                    ss.setNombreReporte("Repertorios");
                } else {            //MERCANTIL
                    ss.setNombreReporte("RepertoriosMercantil");
                }
                ss.setNombreSubCarpeta("registro");
                //ss.setNombreReporte("Repertorios");
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                ss.agregarParametro("FECHA_DESDE", sdf.format(fechaDesde));
                fechaHasta = Utils.sumarRestarDiasFecha(fechaHasta, 1);
                ss.agregarParametro("FECHA_HASTA", sdf.format(fechaHasta));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRepertorio() {
        try {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                return;
            }
            switch (tipoRepertorio) {
                case 0:
                    JsfUti.messageWarning(null, "Debe seleccionar el tipo de Repertorio.", "");
                    break;
                case 1:
                    this.llenarDatos();
                    ss.setNombreReporte("Repertorios");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                case 2:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertoriosMercantil");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                case 3:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertorioVacio");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                case 4:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertorioVacioMercantil");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void generarRepertorioXls() {
        try {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                return;
            }
            switch (tipoRepertorio) {
                case 0:
                    JsfUti.messageWarning(null, "Debe seleccionar el tipo de Repertorio.", "");
                    break;
                case 1:
                    this.llenarDatos();
                    ss.setNombreReporte("Repertorios");
                    ss.setIgnorarGraficos(false);
                    ss.setIgnorarPagineo(false);
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
                    break;
                case 2:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertoriosMercantil");
                    ss.setIgnorarGraficos(false);
                    ss.setIgnorarPagineo(false);
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
                    break;
                case 3:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertorioVacio");
                    ss.setIgnorarGraficos(false);
                    ss.setIgnorarPagineo(false);
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
                    break;
                case 4:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertorioVacioMercantil");
                    ss.setIgnorarGraficos(false);
                    ss.setIgnorarPagineo(false);
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void generarRepertorioWord() {
        try {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                return;
            }
            switch (tipoRepertorio) {
                case 0:
                    JsfUti.messageWarning(null, "Debe seleccionar el tipo de Repertorio.", "");
                    break;
                case 1:
                    this.llenarDatos();
                    ss.setNombreReporte("Repertorios");
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoWord");
                    break;
                case 2:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertoriosMercantil");
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoWord");
                    break;
                case 3:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertorioVacio");
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoWord");
                    break;
                case 4:
                    this.llenarDatos();
                    ss.setNombreReporte("RepertorioVacioMercantil");
                    JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoWord");
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarDatos() {
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        //ss.setEncuadernacion(true);
        //ss.setMargen(30);
        ss.setNombreSubCarpeta("registro");
        ss.agregarParametro("FECHA_REPERTORIO", fechaDesde);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        ss.agregarParametro("FECHA_DESDE", sdf.format(fechaDesde));
        fechaHasta = Utils.sumarRestarDiasFecha(fechaHasta, 1);
        ss.agregarParametro("FECHA_HASTA", sdf.format(fechaHasta));
        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
    }

    public void imprimirActaInicio() {
        try {
            if (libro == null || libro.isEmpty()) {
                libro = "REPERTORIO";
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            if (inicio) {
                ss.setNombreReporte("Repertorio_apertura");
            } else {
                ss.setNombreReporte("Repertorio_cierre");
            }
            ss.setNombreSubCarpeta("registro");
            sdf = new SimpleDateFormat("EEEEE dd 'de' MMMMM 'del' yyyy");
            ss.agregarParametro("FECHA_STRING", sdf.format(fechaActa));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo-registro-propiedad-playas.png"));
            ss.agregarParametro("FOLIO", folio);
            ss.agregarParametro("DESDE", desde);
            ss.agregarParametro("HASTA", hasta);
            ss.agregarParametro("LIBRO", libro);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteIndices() {
        try {
            switch (tipoIndice) {
                case 1:
                    if (desdeIndice != null && hastaIndice != null) {
                        if (desdeIndice.equals(hastaIndice) || desdeIndice.before(hastaIndice)) {
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            ss.instanciarParametros();
                            ss.setTieneDatasource(true);
                            //ss.setEncuadernacion(true);
                            ss.setMargen(30);
                            ss.setNombreReporte("IndiceEspecifico");
                            ss.setNombreSubCarpeta("registro");
                            if (libroIndice == null) {
                                ss.agregarParametro("LIBRO", 0L);
                                ss.agregarParametro("LIBRO_NAME", "TODOS LOS LIBROS");
                            } else {
                                ss.agregarParametro("LIBRO", libroIndice.getId());
                                ss.agregarParametro("LIBRO_NAME", libroIndice.getNombre());
                            }
                            ss.agregarParametro("DESDE", desdeIndice);
                            ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hastaIndice, 1));
                            ss.agregarParametro("CADENA_HASTA", df.format(hastaIndice));
                            ss.agregarParametro("USER_NAME", us.getName_user());
                            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                        } else {
                            JsfUti.messageWarning(null, "Fecha desde debe ser menor a fecha hasta.", "");
                            return;
                        }
                    } else {
                        JsfUti.messageWarning(null, "Debe ingresar fecha desde y hasta.", "");
                        return;
                    }
                    break;
                case 2:
                    if (desdeIndice != null && hastaIndice != null) {
                        if (desdeIndice.equals(hastaIndice) || desdeIndice.before(hastaIndice)) {
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            ss.instanciarParametros();
                            ss.setTieneDatasource(true);
                            //ss.setEncuadernacion(true);
                            ss.setMargen(30);
                            ss.setNombreReporte("IndiceGeneral");
                            ss.setNombreSubCarpeta("registro");
                            ss.agregarParametro("PROPIEDAD", true);
                            ss.agregarParametro("DESDE", desdeIndice);
                            ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hastaIndice, 1));
                            ss.agregarParametro("CADENA_HASTA", df.format(hastaIndice));
                            ss.agregarParametro("USER_NAME", us.getName_user());
                            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                        } else {
                            JsfUti.messageWarning(null, "Fecha desde debe ser menor a fecha hasta.", "");
                            return;
                        }
                    } else {
                        JsfUti.messageWarning(null, "Debe ingresar fecha desde y hasta.", "");
                        return;
                    }
                    break;
                case 3:
                    if (desdeIndice != null && hastaIndice != null) {
                        if (desdeIndice.equals(hastaIndice) || desdeIndice.before(hastaIndice)) {
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            ss.instanciarParametros();
                            ss.setTieneDatasource(true);
                            //ss.setEncuadernacion(true);
                            ss.setMargen(30);
                            ss.setNombreReporte("IndiceGeneral");
                            ss.setNombreSubCarpeta("registro");
                            ss.agregarParametro("PROPIEDAD", false);
                            ss.agregarParametro("DESDE", desdeIndice);
                            ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hastaIndice, 1));
                            ss.agregarParametro("CADENA_HASTA", df.format(hastaIndice));
                            ss.agregarParametro("USER_NAME", us.getName_user());
                            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                        } else {
                            JsfUti.messageWarning(null, "Fecha desde debe ser menor a fecha hasta.", "");
                            return;
                        }
                    } else {
                        JsfUti.messageWarning(null, "Debe ingresar fecha desde y hasta.", "");
                        return;
                    }
                    break;
                default:
                    JsfUti.messageWarning(null, "Debe seleccionar el tipo de reporte.", "");
                    break;
            }
            if (excel) {
                ss.setOnePagePerSheet(false);
                JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
            setExcel(false);
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<RegLibro> getLibros() {
        return em.findAllEntCopy(Querys.getRegLibroListall);
    }

    public Date getFechaActa() {
        return fechaActa;
    }

    public void setFechaActa(Date fechaActa) {
        this.fechaActa = fechaActa;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Boolean getHistorico() {
        return historico;
    }

    public void setHistorico(Boolean historico) {
        this.historico = historico;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Boolean getInicio() {
        return inicio;
    }

    public void setInicio(Boolean inicio) {
        this.inicio = inicio;
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

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public Integer getTipoIndice() {
        return tipoIndice;
    }

    public void setTipoIndice(Integer tipoIndice) {
        this.tipoIndice = tipoIndice;
    }

    public Date getDesdeIndice() {
        return desdeIndice;
    }

    public void setDesdeIndice(Date desdeIndice) {
        this.desdeIndice = desdeIndice;
    }

    public Date getHastaIndice() {
        return hastaIndice;
    }

    public void setHastaIndice(Date hastaIndice) {
        this.hastaIndice = hastaIndice;
    }

    public RegLibro getLibroIndice() {
        return libroIndice;
    }

    public void setLibroIndice(RegLibro libroIndice) {
        this.libroIndice = libroIndice;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getTomo() {
        return tomo;
    }

    public void setTomo(Integer tomo) {
        this.tomo = tomo;
    }

    public Integer getInscripcionDesde() {
        return inscripcionDesde;
    }

    public void setInscripcionDesde(Integer inscripcionDesde) {
        this.inscripcionDesde = inscripcionDesde;
    }

    public Integer getInscripcionHasta() {
        return inscripcionHasta;
    }

    public void setInscripcionHasta(Integer inscripcionHasta) {
        this.inscripcionHasta = inscripcionHasta;
    }

    public boolean isExcel() {
        return excel;
    }

    public void setExcel(boolean excel) {
        this.excel = excel;
    }

    public Integer getTipoOrder() {
        return tipoOrder;
    }

    public void setTipoOrder(Integer tipoOrder) {
        this.tipoOrder = tipoOrder;
    }

    public Integer getTipoRepertorio() {
        return tipoRepertorio;
    }

    public void setTipoRepertorio(Integer tipoRepertorio) {
        this.tipoRepertorio = tipoRepertorio;
    }

}
