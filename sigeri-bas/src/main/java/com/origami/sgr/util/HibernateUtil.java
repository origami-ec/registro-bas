/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author User
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    public static ThreadLocal<Boolean> yaIniciada = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false; //To change body of generated methods, choose Tools | Templates.
        }
    };
    public static ThreadLocal<Boolean> rollbackOnly = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false; //To change body of generated methods, choose Tools | Templates.
        }
    };

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        finally{
            
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
