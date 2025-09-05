/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.publica;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */

@Named
@ViewScoped
public class PubSolicitudTramite implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(PubSolicitudTramite.class.getName());
    
    @PostConstruct
    protected void iniView() {
        
    }
    
}
