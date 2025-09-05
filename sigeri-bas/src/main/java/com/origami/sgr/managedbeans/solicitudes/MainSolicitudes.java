/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.solicitudes;

import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class MainSolicitudes implements Serializable {

    private static final Logger LOG = Logger.getLogger(MainSolicitudes.class.getName());
    private String url;

    public void initView() {
        try {

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "initView", e);
        }
    }

    public void sendPage() {
        if (url == null) {
            JsfUti.messageError(null, "Debe seleccionar el formulario", null);
            return;
        }
        JsfUti.redirectFaces(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
