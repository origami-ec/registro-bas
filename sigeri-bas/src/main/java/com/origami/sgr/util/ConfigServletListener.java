/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.config.PropertiesLoader;
import com.origami.config.SisVars;
import com.origami.documental.HibernateDocumental;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
//import jodd.datetime.JDateTimeDefault;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.hibernate.HibernateException;

/**
 * Web application lifecycle listener.
 *
 * @author Fernando
 */
@WebListener()
public class ConfigServletListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ConfigServletListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "ERROR");
        System.setProperty("org.apache.catalina.STRICT_SERVLET_COMPLIANCE", "false");

        System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
        // timezone y locale default para el sistema
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
        //JDateTimeDefault.timeZone = TimeZone.getTimeZone("America/Guayaquil");
        Locale.setDefault(new Locale("es", "EC"));

        SisVars.urlbase = sce.getServletContext().getContextPath() + "/";
        SisVars.urlbaseFaces = sce.getServletContext().getContextPath() + SisVars.facesUrl + "/";

        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("dd MM yyyy HH mm ss");
        ConvertUtils.register(dateConverter, java.util.Date.class);
        //System.out.println("//// DATE CONVERTER REGISTERED");
        // carga de propiedades
        PropertiesLoader props1 = new PropertiesLoader(sce.getServletContext());
        props1.load();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            HiberUtil.closeSession();
            HibernateUtil.getSessionFactory().close();
            HibernateDocumental.closeSession();
            //HiberUtil.closeSession();
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, "Error al cerrar sessionFactory.", e);
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver driver;
        // clear drivers
        while (drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error remover drivers.", ex);
            }
        }
        System.out.println("close session factory!!!");
    }

}
