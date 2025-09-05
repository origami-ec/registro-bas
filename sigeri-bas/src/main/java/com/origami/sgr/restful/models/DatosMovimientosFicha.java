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
public class DatosMovimientosFicha implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String  libro;
    private String  acto;
    private String  fechaInscripcion;
    private Integer repertorio;
    private Integer inscripcion;
    private String  observacion;
    protected List<DatosIntervinientes> intervinientes = new ArrayList<>();
    
    public DatosMovimientosFicha() {
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<DatosIntervinientes> getIntervinientes() {
        return intervinientes;
    }

    public void setIntervinientes(List<DatosIntervinientes> intervinientes) {
        this.intervinientes = intervinientes;
    }

}
