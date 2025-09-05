/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental;

import com.origami.documental.entities.TbBlob;
import com.origami.documental.entities.TbCarpetas;
import com.origami.documental.entities.TbData;
import com.origami.documental.entities.TbDataIntervinientes;
import com.origami.documental.entities.TbDepartamentos;
import com.origami.documental.entities.TbMarginacion;
import com.origami.documental.entities.TbMarginacionBit;
import com.origami.documental.entities.TbTipoDocCab;
import com.origami.documental.entities.TbTipoDocDet;
import com.origami.documental.entities.TbTipoDocListas;
import com.origami.documental.entities.TbTipoDocPref;
import com.origami.documental.entities.TbTipoDocSep;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author ANGEL NAVARRO
 */
public class HibernateDocumental {

    private static SessionFactory sessionFactory;
    private static Map<String, SessionFactory> containerSessionFactory;

    private static final String URL = "jdbc:mysql://172.16.84.52:3306/%s?useSSL=false&useUnicode=true"
            + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERDB = "sisapp";
    private static final String PASSDB = "sisapp98";
    
    /*private static final String URL = "jdbc:mysql://200.124.246.174:3307/%s?useSSL=false&useUnicode=true"
            + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERDB = "sisapp";
    private static final String PASSDB = "sisapp98";*/
    
    private static Properties propsConnection(String url) {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.put("hibernate.connection.username", USERDB);
        properties.put("hibernate.connection.password", PASSDB);
        properties.put("hibernate.connection.url", url);
        properties.put("current_session_context_class", "thread");
        properties.put("hibernate.connection.pool_size", 2);
        properties.put("hibernate.c3p0.min_size", 2);
        properties.put("hibernate.c3p0.max_size", 6);
        properties.put("hibernate.c3p0.max_statements", 20);
        properties.put("hibernate.c3p0.acquire_increment", 1);
        properties.put("hibernate.c3p0.initialPoolSize", 1);
        properties.put("hibernate.c3p0.maxStatementsPerConnection", 15);
        properties.put("hibernate.c3p0.acquireRetryAttempts", 5);
        properties.put("hibernate.c3p0.idle_test_period", 120);
        properties.put("hibernate.c3p0.timeout", 180);
        properties.put("hibernate.c3p0.unreturnedConnectionTimeout", 30);
        properties.put("hibernate.transaction.auto_close_session", true);
        properties.put("hibernate.c3p0.privilegeSpawnedThreads", true);
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.c3p0.contextClassLoaderSource", "library");
        properties.put("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
        properties.put("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
        properties.put("hibernate.query.factory_class", "org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory");
        
        return properties;
    }

    private static void getSessionFactoryDoc() {
        try {
            Configuration configuration = new Configuration();
            configuration = configuration.addAnnotatedClass(TbData.class);
            configuration = configuration.addAnnotatedClass(TbDataIntervinientes.class);
            configuration = configuration.addAnnotatedClass(TbCarpetas.class);
            configuration = configuration.addAnnotatedClass(TbDepartamentos.class);
            configuration = configuration.addAnnotatedClass(TbTipoDocCab.class);
            configuration = configuration.addAnnotatedClass(TbTipoDocDet.class);
            configuration = configuration.addAnnotatedClass(TbTipoDocListas.class);
            configuration = configuration.addAnnotatedClass(TbTipoDocPref.class);
            configuration = configuration.addAnnotatedClass(TbTipoDocSep.class);
            configuration.setProperties(propsConnection(String.format(URL, "doc_data_lata")));
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSessionData(String db) {
        try {
            if (containerSessionFactory == null) {
                containerSessionFactory = new HashMap<>();
            }
            if (containerSessionFactory.containsKey(db)) {
                try {
                    return containerSessionFactory.get(db).getCurrentSession();
                } catch (HibernateException e) {
                    return containerSessionFactory.get(db).openSession();
                }
            }
            Configuration configuration = new Configuration();
            if (db.contains("data")) {
                configuration = configuration.addAnnotatedClass(TbData.class);
                configuration = configuration.addAnnotatedClass(TbDataIntervinientes.class);
                configuration = configuration.addAnnotatedClass(TbCarpetas.class);
                configuration = configuration.addAnnotatedClass(TbDepartamentos.class);
                configuration = configuration.addAnnotatedClass(TbTipoDocCab.class);
                configuration = configuration.addAnnotatedClass(TbTipoDocDet.class);
                configuration = configuration.addAnnotatedClass(TbTipoDocListas.class);
                configuration = configuration.addAnnotatedClass(TbTipoDocPref.class);
                configuration = configuration.addAnnotatedClass(TbTipoDocSep.class);
            } else {
                configuration = configuration.addAnnotatedClass(TbBlob.class);
                configuration = configuration.addAnnotatedClass(TbMarginacion.class);
                configuration = configuration.addAnnotatedClass(TbMarginacionBit.class);
            }
            configuration.setProperties(propsConnection(String.format(URL, db)));
            SessionFactory sessionFactoryData = configuration.buildSessionFactory();
            containerSessionFactory.put(db, sessionFactoryData);
            try {
                return sessionFactoryData.getCurrentSession();
            } catch (HibernateException e) {
                return sessionFactoryData.openSession();
            }
        } catch (HibernateException ex) {
            // Log the exception.
            System.err.println("Data Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            getSessionFactoryDoc();
        }
        return sessionFactory;
    }

    public static Session getSession() {
        if (sessionFactory == null) {
            getSessionFactoryDoc();
        }
        return sessionFactory.openSession();
    }

    public static void closeSession() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            Session currentSession;
            try {
                currentSession = sessionFactory.getCurrentSession();
                if (currentSession != null) {
                    if (currentSession.isOpen()) {
                        currentSession.close();
                    }
                }
            } catch (HibernateException hibernateException) {
            }
            sessionFactory.close();
        }
        if (containerSessionFactory != null) {
            containerSessionFactory.values().forEach((sf) -> {
                if (sf.isOpen()) {
                    Session currentSession;
                    try {
                        currentSession = sf.getCurrentSession();
                        if (currentSession != null) {
                            if (currentSession.isOpen()) {
                                currentSession.close();
                            }
                        }
                    } catch (HibernateException hibernateException) {
                    }
                    sf.close();
                }
            });
        }
    }
}
