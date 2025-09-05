/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.HibernateUtil;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Fernando
 */
public class HibernateEjbInterceptor {

    public static String UserRegistryKey = "usuario";

    public static ThreadLocal<Boolean> inicioTomado = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public HibernateEjbInterceptor() {
    }

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        Object result = null;
        Boolean esPuntoEntrada = true;
        Exception ex = null;
        //SessionContext context = sessionContext;// FerEjb.getSessionContext();
        if (HibernateUtil.yaIniciada.get()) {
            esPuntoEntrada = false;
        } else {
            HibernateUtil.yaIniciada.set(true);
        }
        try {
            result = ctx.proceed();
            //System.out.println("Resultado: " + result); Obtiene la clase que se esta ejecutado
            //System.out.println("esPuntoEntrada " + esPuntoEntrada + " result -> " + result);
            if (esPuntoEntrada) {
                HiberUtil.flushAndCommit();
            }
        } catch (Exception e) {
//            System.out.println(e);
            HiberUtil.rollback();
            ex = e;
            throw e;
        } finally {
            if (esPuntoEntrada) {
                HiberUtil.rollbackOnlyCheck();
                HiberUtil.closeSession();
                HibernateUtil.yaIniciada.set(false);
                //System.out.println(Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", new Date()) + " -> session cerrada");
            }
        }

        if (ex != null) {
            throw ex;
        }

        return result;
    }

}
