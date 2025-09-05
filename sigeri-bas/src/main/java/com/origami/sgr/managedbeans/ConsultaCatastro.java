/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.models.Predio;
import com.origami.sgr.services.interfaces.CatastroService;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ConsultaCatastro implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConsultaCatastro.class.getName());

    @Inject
    private CatastroService cat;

    protected Integer tipo = 1;
    protected Integer render = 0;
    protected String ingreso;
    protected Predio seleccionado;
    protected List<Predio> respuesta;

    @PostConstruct
    protected void iniView() {
        try {

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultar() {
        try {
            respuesta = cat.buscarPredioCatastro(tipo, ingreso);
            if (respuesta.isEmpty()) {
                JsfUti.messageWarning(null, "No se encontraron datos con los valores ingresados.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void seleccionar(Predio temp) {
        try {
            seleccionado = temp;
            JsfUti.update("formPredio");
            JsfUti.executeJS("PF('dlgPredio').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public Integer getRender() {
        return render;
    }

    public void setRender(Integer render) {
        this.render = render;
    }

    public List<Predio> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(List<Predio> respuesta) {
        this.respuesta = respuesta;
    }

    public Predio getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Predio seleccionado) {
        this.seleccionado = seleccionado;
    }

}
