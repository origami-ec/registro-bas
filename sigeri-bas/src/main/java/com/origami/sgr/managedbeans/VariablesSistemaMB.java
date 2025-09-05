/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.RegTablaCuantia;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
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
public class VariablesSistemaMB implements Serializable {

    @Inject
    private Entitymanager em;

    protected Valores valor;
    protected LazyModel<Valores> valores;
    protected LazyModel<RegTablaCuantia> cuantias;
    protected RegTablaCuantia cuantia;

    @PostConstruct
    protected void iniView() {
        try {
            valores = new LazyModel(Valores.class, "code", "ASC");
            cuantias = new LazyModel(RegTablaCuantia.class, "valor1", "ASC");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgEdit(Valores temp) {
        valor = temp;
        JsfUti.update("formEdicion");
        JsfUti.executeJS("PF('dlgEdicion').show();");
    }

    public void showDlgNuevo() {
        valor = new Valores();
        JsfUti.update("formEdicion");
        JsfUti.executeJS("PF('dlgEdicion').show();");
    }

    public void guardar() {
        try {
            if (valor.getCode() != null && !valor.getCode().isEmpty()) {
                em.persist(valor);
                this.iniView();
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgEdicion').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar todos los campos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgEditCuantia(RegTablaCuantia temp) {
        cuantia = temp;
        JsfUti.update("formEdicionCuantia");
        JsfUti.executeJS("PF('dlgEdicionCuantia').show();");
    }

    public void showDlgNuevaCuantia() {
        cuantia = new RegTablaCuantia();
        JsfUti.update("formEdicionCuantia");
        JsfUti.executeJS("PF('dlgEdicionCuantia').show();");
    }

    public void guardarCuantia() {
        try {
            if (cuantia.getValor1() != null && cuantia.getExceso() != null) {
                em.persist(cuantia);
                this.iniView();
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgEdicionCuantia').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar todos los campos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteCuantia(RegTablaCuantia temp) {
        try {
            em.delete(temp);
            this.iniView();
            JsfUti.update("mainForm");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Valores getValor() {
        return valor;
    }

    public void setValor(Valores valor) {
        this.valor = valor;
    }

    public LazyModel<Valores> getValores() {
        return valores;
    }

    public void setValores(LazyModel<Valores> valores) {
        this.valores = valores;
    }

    public RegTablaCuantia getCuantia() {
        return cuantia;
    }

    public void setCuantia(RegTablaCuantia cuantia) {
        this.cuantia = cuantia;
    }

    public LazyModel<RegTablaCuantia> getCuantias() {
        return cuantias;
    }

    public void setCuantias(LazyModel<RegTablaCuantia> cuantias) {
        this.cuantias = cuantias;
    }

}
