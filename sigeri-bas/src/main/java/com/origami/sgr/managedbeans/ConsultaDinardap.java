/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class ConsultaDinardap implements Serializable {

    @Inject
    private RegistroPropiedadServices reg;

    protected PubPersona persona;
    protected String identificacion;
    protected String fechaNacimiento;
    protected SimpleDateFormat sdf;
    protected Integer consulta = 1;

    @PostConstruct
    protected void initView() {
        try {
            persona = new PubPersona();
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarDinardap() {
        try {
            if (!identificacion.isEmpty()) {
                persona = reg.buscarDinardap(identificacion);
                if (persona != null && !persona.getCiRuc().isEmpty()) {
                    JsfUti.messageInfo(null, "Los datos encontrados son los siguientes.", "");
                    if (persona.getFechaNacimientoLong() != null) {
                        fechaNacimiento = sdf.format(new Date(persona.getFechaNacimientoLong()));
                    }
                } else {
                    JsfUti.messageError(null, "NO se encontró información con la identificación ingresada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar campo CI/RUC.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Integer getConsulta() {
        return consulta;
    }

    public void setConsulta(Integer consulta) {
        this.consulta = consulta;
    }

    public PubPersona getPersona() {
        return persona;
    }

    public void setPersona(PubPersona persona) {
        this.persona = persona;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

}
