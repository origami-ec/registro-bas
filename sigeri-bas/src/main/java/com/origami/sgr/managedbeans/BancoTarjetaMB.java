/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenTipoEntidadBancaria;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI2
 */
@Named
@ViewScoped
public class BancoTarjetaMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(BancoTarjetaMB.class.getName());

    @Inject
    private Entitymanager em;

    private LazyModel<RenEntidadBancaria> lazy;
    private LazyModel<RenEntidadBancaria> lazyTC;
    private RenEntidadBancaria bancoTC;
    private Long tipo;

    @PostConstruct
    public void init() {
        try {
            bancoTC = new RenEntidadBancaria();

            lazy = new LazyModel(RenEntidadBancaria.class, "descripcion", "ASC");
            lazy.addFilter("tipo", new RenTipoEntidadBancaria(1L));

            lazyTC = new LazyModel(RenEntidadBancaria.class, "descripcion", "ASC");
            lazyTC.addFilter("tipo", new RenTipoEntidadBancaria(2L));

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editar(RenEntidadBancaria bc) {
        this.bancoTC = bc;
        JsfUti.executeJS("PF('dlgEntidadBancaria').show();");
        JsfUti.update("formBanco");
    }

    public void showDlgNewEntidad() {
        this.bancoTC = new RenEntidadBancaria();
        JsfUti.executeJS("PF('dlgEntidadBancaria').show();");
        JsfUti.update("formBanco");
    }

    public void save_edit() {
        try {
            Boolean edit = false;
            if (bancoTC.getId() != null) {
                bancoTC.setTipo(new RenTipoEntidadBancaria(tipo));
                edit = em.update(bancoTC);
            } else {
                bancoTC.setEstado(true);
                bancoTC.setTipo(new RenTipoEntidadBancaria(tipo));
                bancoTC.setFechaIngreso(new Date());
                bancoTC = (RenEntidadBancaria) em.merge(bancoTC);
            }
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgEntidadBancaria').hide();");
            JsfUti.messageInfo(null, "Información", "Datos " + (edit ? "Editado" : "Creado") + " con exito...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RenEntidadBancaria getBancoTC() {
        return bancoTC;
    }

    public void setBancoTC(RenEntidadBancaria bancoTC) {
        this.bancoTC = bancoTC;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public LazyModel<RenEntidadBancaria> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RenEntidadBancaria> lazy) {
        this.lazy = lazy;
    }

    public LazyModel<RenEntidadBancaria> getLazyTC() {
        return lazyTC;
    }

    public void setLazyTC(LazyModel<RenEntidadBancaria> lazyTC) {
        this.lazyTC = lazyTC;
    }

}
