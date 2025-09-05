/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author eduar
 */
public class Predio implements Serializable {

    public static final long serialVersionUID = 1L;

    private String clavecatastral;
    private String clavecdato;
    private Long ciu;
    private String manzanacooperativa;
    private String lotecooperativa;
    private String sector;
    private String direccion;
    private String calleprincipal;
    private String identificacionpropietario;
    private String nombrepropietario;
    private String frente;
    private String fondo;
    private String areaterreno;
    private BigDecimal areaconst;
    private String estadopredio;
    private BigDecimal avaluopredio;

    public Predio() {
    }

    public String getClavecatastral() {
        return clavecatastral;
    }

    public void setClavecatastral(String clavecatastral) {
        this.clavecatastral = clavecatastral;
    }

    public String getClavecdato() {
        return clavecdato;
    }

    public void setClavecdato(String clavecdato) {
        this.clavecdato = clavecdato;
    }

    public Long getCiu() {
        return ciu;
    }

    public void setCiu(Long ciu) {
        this.ciu = ciu;
    }

    public String getManzanacooperativa() {
        return manzanacooperativa;
    }

    public void setManzanacooperativa(String manzanacooperativa) {
        this.manzanacooperativa = manzanacooperativa;
    }

    public String getLotecooperativa() {
        return lotecooperativa;
    }

    public void setLotecooperativa(String lotecooperativa) {
        this.lotecooperativa = lotecooperativa;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCalleprincipal() {
        return calleprincipal;
    }

    public void setCalleprincipal(String calleprincipal) {
        this.calleprincipal = calleprincipal;
    }

    public String getIdentificacionpropietario() {
        return identificacionpropietario;
    }

    public void setIdentificacionpropietario(String identificacionpropietario) {
        this.identificacionpropietario = identificacionpropietario;
    }

    public String getNombrepropietario() {
        return nombrepropietario;
    }

    public void setNombrepropietario(String nombrepropietario) {
        this.nombrepropietario = nombrepropietario;
    }

    public String getFrente() {
        return frente;
    }

    public void setFrente(String frente) {
        this.frente = frente;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public String getAreaterreno() {
        return areaterreno;
    }

    public void setAreaterreno(String areaterreno) {
        this.areaterreno = areaterreno;
    }

    public BigDecimal getAreaconst() {
        return areaconst;
    }

    public void setAreaconst(BigDecimal areaconst) {
        this.areaconst = areaconst;
    }

    public String getEstadopredio() {
        return estadopredio;
    }

    public void setEstadopredio(String estadopredio) {
        this.estadopredio = estadopredio;
    }

    public BigDecimal getAvaluopredio() {
        return avaluopredio;
    }

    public void setAvaluopredio(BigDecimal avaluopredio) {
        this.avaluopredio = avaluopredio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Predio{");
        sb.append("clavecatastral=").append(clavecatastral);
        sb.append(", clavecdato=").append(clavecdato);
        sb.append(", ciu=").append(ciu);
        sb.append(", manzanacooperativa=").append(manzanacooperativa);
        sb.append(", lotecooperativa=").append(lotecooperativa);
        sb.append(", sector=").append(sector);
        sb.append(", direccion=").append(direccion);
        sb.append(", calleprincipal=").append(calleprincipal);
        sb.append(", identificacionpropietario=").append(identificacionpropietario);
        sb.append(", nombrepropietario=").append(nombrepropietario);
        sb.append(", frente=").append(frente);
        sb.append(", areaterreno=").append(areaterreno);
        sb.append(", areaconst=").append(areaconst);
        sb.append(", estadopredio=").append(estadopredio);
        sb.append(", avaluopredio=").append(avaluopredio);
        sb.append('}');
        return sb.toString();
    }

}
