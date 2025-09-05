/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DevolutivaRp extends BpmManageBeanBaseRoot implements Serializable {
    
    public static final Long serialVersionUID = 2L;

    private static final Logger LOG = Logger.getLogger(DevolutivaRp.class.getName());
    
    @EJB(beanName = "registroPropiedad")
    private RegistroPropiedadServices reg;
    
    @PostConstruct
    protected void iniView() {
        try {
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
}
