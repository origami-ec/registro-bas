/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DocumentosRp implements Serializable {

    public static final long serialVersionUID = 1L;

    @PostConstruct
    protected void iniView() {

    }

    public void descargarDocumento(String url) {
        JsfUti.redirectNewTab("/sgr/DescargarDocsRepositorio?id=" + url);
    }

}
