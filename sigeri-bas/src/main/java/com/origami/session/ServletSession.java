/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.session;

import com.origami.sgr.util.EntityBeanCopy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author origami-idea
 */
@Named
@SessionScoped
public class ServletSession implements Serializable {

    private List<Map> reportes;
    private Map parametros = null;
    private Boolean tieneDatasource;
    private String nombreReporte;
    private String nombreDocumento;
    private String rutaDocumento;
    private String nombreSubCarpeta;
    private String contentType;
    private byte[] reportePDF;
    private Boolean encuadernacion = Boolean.FALSE;
    private Boolean encuadernacionV2 = Boolean.FALSE;
    private List dataSource;
    private Boolean agregarReporte = Boolean.FALSE;
    private Boolean fondoBlanco = Boolean.TRUE;
    private Boolean onePagePerSheet = Boolean.FALSE;
    private Boolean imprimir = Boolean.FALSE;
    private Boolean ignorarGraficos = Boolean.TRUE;
    private Boolean ignorarPagineo = Boolean.TRUE;
    private Boolean generaFile = Boolean.FALSE;
    private String urlWebService;
    private Integer margen;
    //FIRMAR DE CERTICADOS
    private Long idCertificado;
    private Boolean firmarCertificado = Boolean.FALSE;
    private Boolean reimpresionCertificado = Boolean.FALSE;
    private Object tempData;

    public void instanciarParametros() {
        this.borrarDatos();
        parametros = new HashMap();
    }

    public void agregarParametro(String nombre, Object value) {
        parametros.put(nombre, value);
    }

    public boolean tieneParametro(Object parametro) {
        if (parametros == null) {
            return false;
        }
        return parametros.containsKey(parametro);
    }

    public List<Map> getReportes() {
        return reportes;
    }

    public void setReportes(List<Map> reportes) {
        this.reportes = reportes;
    }

    /**
     * En el <code>Map</code> debe esta incluido un parametro con
     * <code>nombreReporte</code> donde se va tomar el nombre del reporte que se
     * va agregar al final del primero.
     * <p>
     * Si el reporte seencuentra en la misma carpeta tomara en el nombre de la
     * variable <code>nombreSubCarpeta</code> para el caso que se encuentre en
     * otra carpeta se debe incluir otro parametro <code>nombreSubCarpeta</code>
     *
     * @param map Object
     */
    public void addParametrosReportes(Map map) {
        if (reportes == null) {
            reportes = new ArrayList<>();
        }
        reportes.add(map);
    }

    public boolean estaVacio() {
        if (parametros != null) {
            return parametros.isEmpty();
        } else {
            return true;
        }
    }

    public Object retornarValor(Object parametro) {
        return parametros.get(parametro);
    }

    public void borrarDatos() {
        parametros = null;
        tieneDatasource = null;
        nombreReporte = null;
        nombreDocumento = null;
        rutaDocumento = null;
        nombreSubCarpeta = null;
        contentType = null;
        reportePDF = null;
        encuadernacion = Boolean.FALSE;
        encuadernacionV2 = Boolean.FALSE;
        agregarReporte = Boolean.FALSE;
        generaFile = Boolean.FALSE;
        reportes = null;
        idCertificado = null;
        firmarCertificado = Boolean.FALSE;
        ignorarGraficos = Boolean.TRUE;
        ignorarPagineo = Boolean.TRUE;
        urlWebService = null;
    }

    public void borrarParametros() {
        parametros = null;
        reportes = null;
        tieneDatasource = null;
    }

    public Boolean validarCantidadDeParametrosDelServlet() {
        return parametros != null && !parametros.isEmpty();
    }

    public Object getParametro(Object parametro) {
        if (parametros == null) {
            return null;
        }
        return parametros.get(parametro);
    }
    
    public Map getParametros() {
        return parametros;
    }

    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    public Boolean getTieneDatasource() {
        return tieneDatasource;
    }

    public void setTieneDatasource(Boolean tieneDatasource) {
        this.tieneDatasource = tieneDatasource;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public String getNombreSubCarpeta() {
        return nombreSubCarpeta;
    }

    public void setNombreSubCarpeta(String nombreSubCarpeta) {
        this.nombreSubCarpeta = nombreSubCarpeta;
    }

    public byte[] getReportePDF() {
        return reportePDF;
    }

    public void setReportePDF(byte[] reportePDF) {
        this.reportePDF = reportePDF;
    }

    public Boolean getEncuadernacion() {
        return encuadernacion;
    }

    public void setEncuadernacion(Boolean encuadernacion) {
        this.encuadernacion = encuadernacion;
    }

    public Boolean getEncuadernacionV2() {
        return encuadernacionV2;
    }

    public void setEncuadernacionV2(Boolean encuadernacionV2) {
        this.encuadernacionV2 = encuadernacionV2;
    }

    public List getDataSource() {
        return dataSource;
    }

    public void setDataSource(List dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getAgregarReporte() {
        return agregarReporte;
    }

    public void setAgregarReporte(Boolean agregarReporte) {
        this.agregarReporte = agregarReporte;
    }

    public Boolean getFondoBlanco() {
        return fondoBlanco;
    }

    public void setFondoBlanco(Boolean fondoBlanco) {
        this.fondoBlanco = fondoBlanco;
    }

    public Boolean getOnePagePerSheet() {
        return onePagePerSheet;
    }

    public void setOnePagePerSheet(Boolean onePagePerSheet) {
        this.onePagePerSheet = onePagePerSheet;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getMargen() {
        return margen;
    }

    public void setMargen(Integer margen) {
        this.margen = margen;
    }

    public Boolean getFirmarCertificado() {
        return firmarCertificado;
    }

    public void setFirmarCertificado(Boolean firmarCertificado) {
        this.firmarCertificado = firmarCertificado;
    }

    public Long getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(Long idCertificado) {
        this.idCertificado = idCertificado;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public Boolean getReimpresionCertificado() {
        return reimpresionCertificado;
    }

    public void setReimpresionCertificado(Boolean reimpresionCertificado) {
        this.reimpresionCertificado = reimpresionCertificado;
    }

    public Boolean getIgnorarGraficos() {
        return ignorarGraficos;
    }

    public void setIgnorarGraficos(Boolean ignorarGraficos) {
        this.ignorarGraficos = ignorarGraficos;
    }

    public Boolean getIgnorarPagineo() {
        return ignorarPagineo;
    }

    public void setIgnorarPagineo(Boolean ignorarPagineo) {
        this.ignorarPagineo = ignorarPagineo;
    }

    public Object getTempData() {
        return tempData;
    }

    public Boolean getGeneraFile() {
        return generaFile;
    }

    public void setGeneraFile(Boolean generaFile) {
        this.generaFile = generaFile;
    }

    public void setTempData(Object tempData) {
        try {
            Object newInstance = tempData.getClass().newInstance();
            EntityBeanCopy.cloneClass(tempData, newInstance);
            this.tempData = tempData;
        } catch (Exception ex) {
            Logger.getLogger(ServletSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    public void setUrlWebService(String urlWebService) {
        this.urlWebService = urlWebService;
    }

}
