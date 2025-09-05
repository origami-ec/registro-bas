/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.proxy.HibernateProxy;

/**
 *
 * @author User
 */
public class HiberUtil {

    public static Serializable getProxyId(Object entity) {
        Serializable id = ((HibernateProxy) entity).getHibernateLazyInitializer().getIdentifier();
        return id;
    }

    public static <T> T unproxy(T entity) {
        if (entity == null) {
            return null;
        }
        if (Hibernate.isInitialized(entity)) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        } else {
            return null;
        }
        return entity;
    }

    public static boolean isProxy(Object entity) {
        return entity instanceof HibernateProxy;
    }

    public static Transaction thread_requireTransaction() {
        Transaction t1 = HibernateUtil.getSessionFactory().getCurrentSession().getTransaction();
        if (t1 == null) {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        } else {
            if (!t1.isActive()) {
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            }
        }
        return HibernateUtil.getSessionFactory().getCurrentSession().getTransaction();
    }

    public static Transaction requireTransaction() {
        Transaction t1 = HiberUtil.getSession().getTransaction();
        if (t1 == null) {
            HiberUtil.getSession().beginTransaction();
        } else {
            if (!t1.isActive()) {
                HiberUtil.getSession().beginTransaction();
            }
        }
        return HiberUtil.getSession().getTransaction();
    }

    public static void thread_commit() {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.flush();
        if (sess.getTransaction() != null) {
            if (sess.getTransaction().isActive()) {
                sess.getTransaction().commit();
            }
        }
    }

    public static void rollback() {
        HibernateUtil.rollbackOnly.set(Boolean.TRUE);
    }

    public static void rollbackOnlyCheck() {
        if (haySessionActiva() && HibernateUtil.rollbackOnly.get()) {
            Session sess = getSession();
            if (sess.getTransaction() != null) {
                /**/
            }
            HibernateUtil.rollbackOnly.set(false);
        }
    }

    public static Session getSession() {
        if (haySessionActiva()) {
            //System.out.println("Session activa: ");
            return HibernateUtil.getSessionFactory().getCurrentSession();
        } else {
            //System.out.println("abriendo session: ");
            ManagedSessionContext.bind(HibernateUtil.getSessionFactory().openSession());
        }
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public static Boolean haySessionActiva() {
        return ManagedSessionContext.hasBind(HibernateUtil.getSessionFactory());
    }

    public static void flushAndCommit() throws HibernateException, Exception {
        if (haySessionActiva()) {
            Session sess = getSession();
            if (sess.getTransaction() != null) {
//                System.out.println("isJoinedToTransaction: " + sess.isJoinedToTransaction());
                if (sess.isDirty() && sess.isJoinedToTransaction()) {
                    sess.flush();
                }
                if (sess.getTransaction().isActive() && HibernateUtil.rollbackOnly.get() == false) {
                    sess.getTransaction().commit();
                }
            }
        }
    }

    public static void closeSession() {
        if (haySessionActiva()) {
            Session sess = HiberUtil.getSession();
            ManagedSessionContext.unbind(HibernateUtil.getSessionFactory());
            sess.close();
        }
    }

    public static void newTransaction() {
        try {
            flushAndCommit();
            closeSession();
            requireTransaction();
        } catch (Exception ex) {
            Logger.getLogger(HiberUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeSession();
        }
    }

}
