/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.services.interfaces.SessionServiceLocal;
import com.origami.session.UserSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 *
 * @author Anyelo
 */
@Stateless(name = "sessionServices")
public class SessionService implements SessionServiceLocal {

    @Override
    public Object getExternalContextObject(String key) {
        try {
            return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
        } catch (Exception e) {
            Logger.getLogger(SessionService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public UserSession getSessionContext() {
        try {
            return BeanProvider.getContextualReference(UserSession.class);
        } catch (Exception e) {
            Logger.getLogger(SessionService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

}
