package com.origami.sgr.restful.models;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asilva
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatosPropietariosFicha implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String  ci;
    private String  nombre;

    public DatosPropietariosFicha() {
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
