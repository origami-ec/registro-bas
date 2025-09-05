/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.List;
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
public class CatalogoItemMB implements Serializable {

    @Inject
    private Entitymanager em;

    protected LazyModel<CtlgItem> lazy;
    protected CtlgItem item;

    protected List<CtlgCatalogo> catalogos;
    protected CtlgCatalogo catalogo;
    protected CtlgCatalogo nuevo;

    @PostConstruct
    protected void iniView() {
        try {
            lazy = new LazyModel(CtlgItem.class, "valor", "ASC");
            catalogos = em.findAll(Querys.getCtlgCatalogos);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateLazy() {
        try {
            if (catalogo != null && catalogo.getId() != null) {
                lazy = new LazyModel(CtlgItem.class, "valor", "ASC");
                lazy.addFilter("catalogo", catalogo);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgCatalogo() {
        nuevo = new CtlgCatalogo();
        JsfUti.update("formCatalogo");
        JsfUti.executeJS("PF('dlgCatalogo').show();");
    }

    public void showDlgItem(CtlgItem temp) {
        item = temp;
        JsfUti.update("formItem");
        JsfUti.executeJS("PF('dlgItem').show();");
    }

    public void showDlgNew() {
        try {
            if (catalogo == null || catalogo.getId() == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el catalogo para registrar un nuevo item.", "");
                return;
            }
            item = new CtlgItem();
            item.setCatalogo(catalogo);
            JsfUti.update("formItem");
            JsfUti.executeJS("PF('dlgItem').show();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardarCatalogo() {
        try {
            if (nuevo.getNombre() != null & !nuevo.getNombre().isEmpty()) {
                em.persist(nuevo);
                catalogos = em.findAll(Querys.getCtlgCatalogos);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgCatalogo').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar todos los campos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardarItem() {
        try {
            if (item.getValor() != null && !item.getValor().isEmpty() && item.getCodename()!= null && !item.getCodename().isEmpty()) {
                em.persist(item);
                this.updateLazy();
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgItem').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar todos los campos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public CtlgItem getItem() {
        return item;
    }

    public void setItem(CtlgItem item) {
        this.item = item;
    }

    public LazyModel<CtlgItem> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<CtlgItem> lazy) {
        this.lazy = lazy;
    }

    public List<CtlgCatalogo> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(List<CtlgCatalogo> catalogos) {
        this.catalogos = catalogos;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
    }

    public CtlgCatalogo getNuevo() {
        return nuevo;
    }

    public void setNuevo(CtlgCatalogo nuevo) {
        this.nuevo = nuevo;
    }

}
