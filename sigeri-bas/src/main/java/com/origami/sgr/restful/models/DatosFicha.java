package com.origami.sgr.restful.models;

import java.io.Serializable;
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
public class DatosFicha implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long   numFicha;
    private String parroquia;
    private String fechaApertura;
    private String linderos;
    private String tipoPredio;
    private String codigoPredial;
    protected List<DatosPropietariosFicha> propietarios = new ArrayList<>();
    protected List<DatosMovimientosFicha>  movimientos = new ArrayList<>();
    
    public DatosFicha() {
        
    }
    
    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getCodigoPredial() {
        return codigoPredial;
    }

    public void setCodigoPredial(String codigoPredial) {
        this.codigoPredial = codigoPredial;
    }

    public List<DatosMovimientosFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<DatosMovimientosFicha> movimientos) {
        this.movimientos = movimientos;
    }

    public List<DatosPropietariosFicha> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<DatosPropietariosFicha> propietarios) {
        this.propietarios = propietarios;
    }

}
