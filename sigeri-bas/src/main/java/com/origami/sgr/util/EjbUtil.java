/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.config.SisVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Fernando
 */
public abstract class EjbUtil {
    
    /**
     * ctx.lookup("java:global/#{nombreProyecto}/#{component}"); ejemplo:
     *
     * @param component Object
     * @return Object
     */
    public static Object getEjb(String component) {
        InitialContext ctx;
        Object o1 = null;
        try {
            ctx = new InitialContext();
            o1 = ctx.lookup(SisVars.ejbRuta + component);	
        } catch (NamingException ex) {
            System.out.println(ex);
        }
        return o1;
    }

    public static EJBContext getEjbContext() {
        EJBContext context = null;
        try {
            context = (EJBContext) new InitialContext().lookup("java:comp/EJBContext");
        } catch (NamingException ex) {
            Logger.getLogger(EjbUtil.class.getName()).log(Level.SEVERE, null, ex);

        }
        return context;
    }

    public static SessionContext getSessionContext() {
        SessionContext context = null;
        try {
            context = (SessionContext) new InitialContext().lookup("java:comp/env/sessionContext");
        } catch (NamingException ex) {
            Logger.getLogger(EjbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return context;
    }
    
}
