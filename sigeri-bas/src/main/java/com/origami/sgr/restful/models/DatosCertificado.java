package com.origami.sgr.restful.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asilva
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatosCertificado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private BigInteger numCertificado;
    private String tipoCertificado;
    private Long   numFichaRegistral;
    private String fechaEmision;
    private String url;
    private Long numTramite;
    private String solvencia;
    private String linderosRegistrales;
    private String parroquia;
    private String claveCatastral;
    
    protected List<DatosPropietariosFicha> propietarios = new ArrayList<>();
    protected List<DatosMovimientosFicha>  movimientos = new ArrayList<>();

    public DatosCertificado() {
    }

    public BigInteger getNumCertificado() {
        return numCertificado;
    }

    public void setNumCertificado(BigInteger numCertificado) {
        this.numCertificado = numCertificado;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(String tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public Long getNumFichaRegistral() {
        return numFichaRegistral;
    }

    public void setNumFichaRegistral(Long numFichaRegistral) {
        this.numFichaRegistral = numFichaRegistral;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getSolvencia() {
        return solvencia;
    }

    public void setSolvencia(String solvencia) {
        this.solvencia = solvencia;
    }

    public String getLinderosRegistrales() {
        return linderosRegistrales;
    }

    public void setLinderosRegistrales(String linderosRegistrales) {
        this.linderosRegistrales = linderosRegistrales;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public List<DatosPropietariosFicha> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<DatosPropietariosFicha> propietarios) {
        this.propietarios = propietarios;
    }

    public List<DatosMovimientosFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<DatosMovimientosFicha> movimientos) {
        this.movimientos = movimientos;
    }

}
