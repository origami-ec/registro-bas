/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.ReflexionEntity;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.WhereClause;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Origami Solutions
 */
@Singleton(name = "manager")
@Interceptors(value = {HibernateEjbInterceptor.class})
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TransactionManager implements Entitymanager {

    @Override
    public void evict(Object obj) {
        if (obj == null) {
            return;
        }
        HiberUtil.getSession().evict(obj);
    }

    @Override
    public void detach(Object obj) {
        if (obj == null) {
            return;
        }
        HiberUtil.getSession().detach(obj);
    }

    @Override
    public <T> T find(Class<T> entity, Object id) {
        T ob = null;
        try {
            Session sess = HiberUtil.getSession();
            ob = (T) sess.get(entity, (Serializable) id);
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

//    @Override
    public <T> T findLock(Class<T> entity, Object id) {
        T ob = null;
        try {
            HiberUtil.newTransaction();
            Session sess = HiberUtil.getSession();
            //ob = sess.find(entity, id, LockModeType.PESSIMISTIC_READ);
            ob = (T) sess.get(entity, (Serializable) id, LockMode.OPTIMISTIC);
//            ob = (T) sess.get(entity, (Serializable) id, LockOptions.READ);
            //Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public <T> List<T> findAll(Class<T> entity) {
        List result = null;
        try {
            Session sess = HiberUtil.getSession();
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
            result = (List<T>) q.getResultList();
            result.size();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Object find(String query, String[] par, Object[] val) {
        Object ob = null;
        try {
            // Modificado 
//            javax.persistence.Query createQuery = this.getEntityManger().createQuery(query).setMaxResults(1);
            javax.persistence.Query createQuery = HiberUtil.getSession().createQuery(query).setMaxResults(1);
            for (int i = 0; i < par.length; i++) {
                createQuery.setParameter(par[i], val[i]);
            }
            try {
                ob = createQuery.getSingleResult();
            } catch (NoResultException ex) {
                ex.toString();
                return null;
            }
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public <T extends Object> T findNoProxy(Class<T> entity, Object id) {
        T ob = null;
        try {
            Session sess = HiberUtil.getSession();
            ob = (T) sess.get(entity, (Serializable) id);
            Hibernate.initialize(ob);
            ob = (T) EntityBeanCopy.clone(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public List findMax(String query, String[] par, Object[] val, Integer max) {
        Session sess;
        javax.persistence.Query q;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            if (max != null) {
                q.setMaxResults(max);
            }
            for (int i = 0; i < par.length; i++) {
                q.setParameter(par[i], val[i]);
            }
            result = (List) q.getResultList();
            result.size();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public List findFirstAndMaxResult(String query, String[] par, Object[] val, Integer first, Integer max) {
        Session sess;
        Query q;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            if (max != null) {
                q.setMaxResults(max);
            }
            if (first != null) {
                q.setFirstResult(first);
            }
            for (int i = 0; i < par.length; i++) {
                q.setParameter(par[i], val[i]);
            }
            result = (List) q.getResultList();
            result.size();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public List findAll(String query) {
        List l = null;
        try {
            Session sess = HiberUtil.getSession();
            Query q = sess.createQuery(query);
            l = (List) q.getResultList();
            l.size();
            Hibernate.initialize(l);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return l;
    }

    @Override
    public List findAll(String query, String[] par, Object[] val) {
        Session sess;
        Query q;
        List l = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            for (int i = 0; i < par.length; i++) {
                q.setParameter(par[i], val[i]);
            }
            l = (List) q.getResultList();
            l.size();
            Hibernate.initialize(l);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return l;
    }

    @Override
    public <T> List<T> findAllEntCopy(Class<T> entity) {
        List result = null;
        try {
            Session sess = HiberUtil.getSession();
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
            result = (List<T>) q.getResultList();
            result.size();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public List findAllEntCopy(String query, String[] par, Object[] val) {
        List list = null;
        try {
            Session sess = HiberUtil.getSession();
            sess.enableFilter("activos");
            sess.enableFilter("activosString");
            Query q = sess.createQuery(query);
            for (int i = 0; i < par.length; i++) {
                q.setParameter(par[i], val[i]);
            }
            list = (List) EntityBeanCopy.clone(q.getResultList());
            Hibernate.initialize(list);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public List findAllEntCopy(String query) {
        List l = null;
        try {
            Session sess = HiberUtil.getSession();
            Query q = sess.createQuery(query);
            l = (List) EntityBeanCopy.clone(q.getResultList());
            if (l != null) {
                l.size();
            }
            Hibernate.initialize(l);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return l;
    }

    @Override
    public Object persist(Object o) {
        Object ob = null;
        try {
            HiberUtil.requireTransaction();
            Session sess = HiberUtil.getSession();
            getNativeQuery("SELECT audit.set_appuser(?)", new Object[]{"admin"}); // TODO: VERIFICAR
            if (ReflexionEntity.getIdFromEntity(o) == null) {
                ob = sess.merge(o); // RETORNA EL OBJETO PERSISTIDO
                sess.flush();
            } else {
                sess.update(o);
                sess.flush();
                ob = o;
            }
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public Object saveAll(Object entity) {
        Object ob = null;
        try {
            HiberUtil.requireTransaction();
            Session sess = HiberUtil.getSession();
            ob = sess.save(entity); // RETORNA EL ID DEL OBJETO
            sess.flush();
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public boolean delete(Object o) {
        if (o == null) {
            return true;
        }
        Session sess;
        boolean flag;
        try {
            HiberUtil.requireTransaction();
            sess = HiberUtil.getSession();
            sess.delete(o);
            sess.flush();
            flag = true;
        } catch (HibernateException e) {
            flag = false;
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }
    
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return true;
        }
        Session sess;
        boolean flag;
        try {
            HiberUtil.requireTransaction();
            sess = HiberUtil.getSession();
            sess.remove(o);
            sess.flush();
            flag = true;
        } catch (HibernateException e) {
            flag = false;
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public boolean update(Object o) {
        HiberUtil.requireTransaction();
        Session sess = HiberUtil.getSession();
        boolean flag = false;
        if (o == null) {
            System.out.println("update object null");
            return flag;
        }
        try {
            sess.update(o);
            sess.flush();
            flag = true;
        } catch (HibernateException e) {
            System.out.println("Exception update intentando merge. " + e.getMessage());
            try {
                sess.merge(o);
                sess.flush();
                flag = true;
            } catch (HibernateException ex) {
                Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception update TransactionManager intentando merge. " + e.getMessage());
            }
        }
        return flag;
    }

    @Override
    public Object merge(Object o) {
        HiberUtil.requireTransaction();
        Session sess = HiberUtil.getSession();
        try {
            o = sess.merge(o);
            sess.flush();
            Hibernate.initialize(o);
            return o;
        } catch (HibernateException ex) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean saveOrUpdate(Object o) {
        HiberUtil.requireTransaction();
        Session sess = HiberUtil.getSession();
        boolean flag;
        try {
            sess.saveOrUpdate(o);
            sess.flush();
            flag = true;
        } catch (HibernateException ex) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
            flag = false;
        }
        return flag;
    }

    @Override
    public Object getNativeQuery(String query) {
        Session sess;
        Query q;
        Object ob = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            ob = (Object) q.getSingleResult();
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public Object getNativeQuery(String query, Object[] val) {
        Session sess;
        Query q;
        Object ob = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            for (int i = 0; i < val.length; i++) {
                q.setParameter(i + 1, val[i]);
            }
            q.setMaxResults(1);
            ob = (Object) q.uniqueResult();
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public int updateNativeQuery(String query, Object[] val) {
        Session sess;
        Query q;
        try {
            HiberUtil.requireTransaction();
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            for (int i = 0; i < val.length; i++) {
                q.setParameter(i + 1, val[i]);
            }
            return q.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }
    
    @Override
    public int executeNativeQuery(String query) {
        Session sess;
        Query q;
        try {
            HiberUtil.requireTransaction();
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            return q.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    @Override
    public List getSqlQuery(String query) {
        Session sess;
        Query q;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            result = q.getResultList();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public <T> List<T> getSqlQueryParametros(Class<T> clase, String query) {
        javax.persistence.Query q;
        List<T> result = null;
        try {
            q = getEntityManger().createNativeQuery(query, clase);
            result = q.getResultList();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public <T> List<T> getSqlQueryValues(Class<T> clase, String query, Object[] values) {
//        javax.persistence.Query q;
        List<T> result = null;
        try {
//            q = getEntityManger().createNativeQuery(query);
            Query q = getSession().createSQLQuery(query);
            int position = 1;
            if (values != null) {
                for (Object value : values) {
                    q.setParameter(position, value);
                    position++;
                }
            }
            if (clase != null) {
                result = q.setResultTransformer(Transformers.aliasToBean(clase)).list();
            } else {
                result = q.getResultList();
            }
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    @Override
    public <T> T getSqlQueryObjectValues(Class<T> clase, String query, Object[] values) {
        try {
            Object ob;
            Query q = getSession().createSQLQuery(query).setMaxResults(1);
            int position = 1;
            if (values != null) {
                for (Object value : values) {
                    q.setParameter(position, value);
                    position++;
                }
            }
            if (clase != null) {
                ob = q.setResultTransformer(Transformers.aliasToBean(clase)).uniqueResult();
            } else {
                ob = q.uniqueResult();
            }
            Hibernate.initialize(ob);
            return (T) ob;
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public <T> List<T> getSqlQueryParametros(Class<T> clase, String query, String[] params, Object[] values) {
        Query q;
        List<T> result = null;
        try {
            q = getSession().createSQLQuery(query);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i], values[i]);
            }
            if (clase != null) {
                result = q.setResultTransformer(Transformers.aliasToBean(clase)).list();
            } else {
                result = q.getResultList();
            }
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public List<Object[]> getManyColumnsResults(String query, String[] params, Object[] values) {
        Session sess;
        Query q;
        List l = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i], values[i]);
            }
            l = q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return l;
    }

    @Override
    public List<Object[]> getManyColumnsResults(String query) {
        Session sess;
        Query q;
        List l = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            l = q.getResultList();
            Hibernate.initialize(l);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return l;
    }

    @Override
    public Session getSession() {
        Session sess = null;
        try {
            sess = HiberUtil.getSession();
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return sess;
    }

    @Override
    public Transaction requiredTransaction() {
        try {
            return HiberUtil.requireTransaction();
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List findAllByEntities(String query, String[] entitiesNames, Object[] entities) {
        Session sess;
        Query q;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            for (int i = 0; i < entitiesNames.length; i++) {
                q.setParameter(entitiesNames[i], entities[i]);
            }
            result = q.getResultList();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public boolean saveList(List entities) {
        Session sess;
        try {
            HiberUtil.requireTransaction();
            sess = HiberUtil.getSession();
            entities.forEach((entitie) -> {
                sess.merge(entitie);
            });
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteList(List entities) {
        Session sess;
        try {
            HiberUtil.requireTransaction();
            sess = HiberUtil.getSession();
            entities.forEach((entitie) -> {
                sess.delete(entitie);
            });
            sess.flush();
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    @Override
    public Object find(String query) {
        Session sess;
        Query q;
        Object result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(query);
            result = q.getSingleResult();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public <T> List<T> findAllOrdered(Class<T> entity, String[] order, Boolean[] prior) {
        Session sess;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            javax.persistence.Query cq = sess.getEntityManagerFactory().createEntityManager().createQuery(query);
            for (int i = 0; i < order.length; i++) {
                if (prior[i] == true) {
                    query.orderBy(builder.asc(entityRoot.get(order[i])));
                } else {
                    query.orderBy(builder.desc(entityRoot.get(order[i])));
                }
            }
            result = cq.getResultList();
            if (result != null) {
                result.size();
                Hibernate.initialize(result);
            }
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public <T> List<T> findAllOrdEntCopy(Class<T> entity, String[] order, Boolean[] prior) {
        Session sess = HiberUtil.getSession();
        List<T> list = null;
        try {
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            for (int i = 0; i < order.length; i++) {
                if (prior[i] == true) {
                    query.orderBy(builder.asc(entityRoot.get(order[i])));
                } else {
                    query.orderBy(builder.desc(entityRoot.get(order[i])));
                }
            }
            javax.persistence.Query cq = sess.getEntityManagerFactory().createEntityManager().createQuery(query);

            list = (List<T>) EntityBeanCopy.clone(cq.getResultList());
            if (list != null) {
                list.size();
            }
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public <T> T findObjectByParameter(Class entity, Map<String, Object> paramt) {
        Session sess = HiberUtil.getSession();
        T ob = null;
        try {
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
            try {
                q.setMaxResults(1);
                ob = (T) q.getSingleResult();
            } catch (NoResultException ex) {
                ex.toString();
                return null;
            }
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public <T> List<T> findObjectByParameterList(Class entity, Map<String, Object> paramt) {
        Session sess = HiberUtil.getSession();
        List<T> ob = null;
        try {
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
            ob = (List<T>) q.getResultList();
            if (ob != null) {
                ob.size();
                Hibernate.initialize(ob);
            }
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public <T> List<T> findAllObjectOrder(Class entity, String[] order, Boolean ascDes) {
        Session sess = HiberUtil.getSession();
        List<T> list = null;
        try {
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            for (String ord : order) {
                if (ascDes == null || !ascDes) {
                    query.orderBy(builder.desc(entityRoot.get(ord)));
                } else {
                    query.orderBy(builder.asc(entityRoot.get(ord)));
                }
            }
            javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
            list = (List<T>) q.getResultList();
            if (list != null) {
                list.size();
                Hibernate.initialize(list);
            }
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public <T> List<T> findObjectByParameterOrderList(Class entity, Map<String, Object> paramt, String[] order, Boolean ascDes) {
        Session sess = HiberUtil.getSession();
        List<T> result = null;
        try {
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            for (String ord : order) {
                if (ascDes == null || !ascDes) {
                    query.orderBy(builder.desc(entityRoot.get(ord)));
                } else {
                    query.orderBy(builder.asc(entityRoot.get(ord)));
                }
            }
            javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
            result = (List<T>) q.getResultList();
            result.size();
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Object findObjectByParameter(String query, Map<String, Object> paramt) {
        Session sess = HiberUtil.getSession();
        Object ob = null;
        try {
            Query q = sess.createQuery(query).setMaxResults(1);
            paramt.entrySet().forEach((entrySet) -> {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            });
            try {
                ob = (Object) q.getSingleResult();
            } catch (Exception e) {
                System.out.println(e);
                return ob;
            }
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    private Object[] getType(Object value) {
        try {
            if (value instanceof Collection || value instanceof List) {
                return ((Collection) value).toArray();
            } else if (value instanceof Set) {
                return ((Set) value).toArray();
            } else if (value instanceof Object[]) {
                return (Object[]) value;
            }
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public void executeFunction(String function, Map<String, Object> paramt, Boolean tipoVoid) {
        Session sess;
        try {
            String parametros = "";
            if (paramt != null) {
                for (Map.Entry<String, Object> entrySet : paramt.entrySet()) {
                    parametros = parametros + entrySet.getValue() + ",";
                }
                parametros = parametros.substring(0, parametros.length() - 1);
            }
            this.requiredTransaction();
            sess = HiberUtil.getSession();
            if (tipoVoid) {
                sess.createSQLQuery("SELECT " + function + "();");
            } else {
                sess.createSQLQuery("SELECT " + function + "(" + parametros + ");").uniqueResult();
            }
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void executeFunction(String function) {
        Object o = null;
        try {
            HiberUtil.newTransaction();
            this.requiredTransaction();
            Session sess = HiberUtil.getSession();
            sess.doWork((Connection connection) -> {
                CallableStatement callableStatement;
                callableStatement = connection.prepareCall("{call " + function + "() }");
                callableStatement.executeUpdate();
            });
        } catch (Exception e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List findNamedQuery(String namedQuery, Map<String, Object> paramt) {
        Session sess;
        Query q;
        List ob = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createQuery(namedQuery);
            paramt.entrySet().forEach((entrySet) -> {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            });
            ob = q.getResultList();
            ob.size();
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public <T> T findObjectNamedQuery(String namedQuery, Map<String, Object> paramt) {
        T ob = null;
        try {
            Session sess1 = HiberUtil.getSession();
            Query q1 = sess1.getNamedQuery(namedQuery);
            paramt.entrySet().forEach((entrySet) -> {
                q1.setParameter(entrySet.getKey(), entrySet.getValue());
            });
            ob = (T) q1.getSingleResult();
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public EntityManager getEntityManger() {
        return getSession().getEntityManagerFactory().createEntityManager();
    }

    private EntityManager getEntityManger(Session sess) {
        return sess.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public Object find(Connection c, String sql, List<Object> paramt) throws SQLException {
        Object ob = null;
        try {
            if (c != null) {
                PreparedStatement ps = c.prepareCall(sql);
                int countParamt = 1;
                for (Object object : paramt) {
                    ps.setObject(countParamt, object);
                    countParamt++;
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if ("bytea".equalsIgnoreCase(rs.getMetaData().getColumnTypeName(1))) {
                        ob = rs.getBytes(1);
                    } else {
                        ob = rs.getObject(1);
                    }
                }
                rs.close();
                ps.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return ob;
    }

    @Override
    public <T> T find(Connection c, String sql, List<Object> paramt, Class objectTranform) throws SQLException {
        T ob = null;
        try {
            if (c != null) {
                PreparedStatement ps = c.prepareCall(sql);
                int countParamt = 1;
                for (Object object : paramt) {
                    ps.setObject(countParamt, object);
                    countParamt++;
                }
                ResultSet rs = ps.executeQuery();
                ob = (T) objectTranform.newInstance();
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        ReflexionEntity.setCampo(ob,
                                Utils.transformUpperCase(metaData.getColumnName(i)),
                                rs.getObject(i));
                    }
                }
                rs.close();
                ps.close();
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return ob;
    }

    @Override
    public List findList(Connection c, String sql, List<Object> paramt, Class objectTranform) throws SQLException {
        List list = null;
        try {
            if (c != null) {
                try (PreparedStatement ps = c.prepareCall(sql)) {
                    int countParamt = 1;
                    for (Object object : paramt) {
                        ps.setObject(countParamt, object);
                        countParamt++;
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Object ob = objectTranform.newInstance();
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            ResultSetMetaData metaData = rs.getMetaData();
                            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                                ReflexionEntity.setCampo(ob,
                                        Utils.transformUpperCase(metaData.getColumnName(i)),
                                        rs.getObject(i));
                            }
                            list.add(ob);
                        }
                    }
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            c.close();
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    private Predicate[] getPredicates(Root root, CriteriaBuilder builder, Map<String, Object> filters) {
        if (filters == null) {
            return null;
        }
        List<Predicate> predicates = new ArrayList<>();
        filters.entrySet().forEach((filEntry) -> {
            if (filEntry.getValue() instanceof Date) {
                Date date = (Date) filEntry.getValue();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                date = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                predicates.add(builder.between(root.get(filEntry.getKey()), date, cal.getTime()));
            } else {
                String key = filEntry.getKey();
                Join join = null;
                if (filEntry.getKey().contains(".")) {
                    String[] split = key.split("\\.");
                    int index = 0;
                    try {
                        for (String sp : split) {
                            if (index == 0) { // PRIMER RECORRIDO SETEA CRITERIA PRINCIPAL
                                join = root.join(sp);
                            } else if (index < (split.length - 1)) {
                                join = join.join(sp);
                            } else {
                                if (filEntry.getValue().equals(Date.class)) {
                                    Date date = (Date) filEntry.getValue();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(date);
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    cal.add(Calendar.SECOND, -1);
                                    predicates.add(builder.between(join.get(sp), date, cal.getTime()));
                                } else {
                                    if (NumberUtils.isNumber(filEntry.getValue().toString())) {
                                        predicates.add(builder.equal(join.get(sp), ReflexionEntity.instanceConsString(filEntry.getValue().getClass(), filEntry.getValue().toString().trim()).toString()));
                                    } else if (filEntry.getValue() instanceof Object[]) {
                                        predicates.add(join.get(sp).in(((Object[]) filEntry.getValue())));
                                    } else {
                                        if (filEntry.getValue() instanceof WhereClause) {
                                            WhereClause c = (WhereClause) filEntry.getValue();
                                            if (c.getCondition().equalsIgnoreCase("notEqual") || c.getCondition().equalsIgnoreCase("noEquals")) {
                                                predicates.add(builder.notEqual(join.get(sp), c.getValue()));
                                            } else if (c.getCondition().equalsIgnoreCase("or")) {
                                                builder.or(
                                                        builder.equal(builder.upper(join.get(sp)), c.getValue()),
                                                        builder.equal(builder.upper(join.get(sp)), c.getValue1()));
                                            } else {
                                                predicates.add(builder.equal(join.get(sp), filEntry.getValue()));
                                            }
                                        } else {
                                            predicates.add(builder.equal(join.get(sp), filEntry.getValue()));
                                        }
                                    }
                                }
                            }
                            index++;
                        }
                    } catch (Exception e) {
                        Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
                    }
                } else {
                    if (filEntry.getValue() instanceof WhereClause) {
                        WhereClause c = (WhereClause) filEntry.getValue();
                        if (c.getCondition().equalsIgnoreCase("notEqual") || c.getCondition().equalsIgnoreCase("noEquals")) {
                            predicates.add(builder.notEqual(root.get(filEntry.getKey()), c.getValue()));
                        } else if (c.getCondition().equalsIgnoreCase("or")) {
                            predicates.add(builder.or(builder.equal(builder.upper(root.get(filEntry.getKey())), c.getValue()),
                                    builder.equal(builder.upper(root.get(c.getFieldOr())), c.getValue1())));
                        }
                    } else {
                        predicates.add(builder.equal(root.get(filEntry.getKey()), filEntry.getValue()));
                    }
                }
            }
        });
        return toArrayPredicates(predicates);
    }

    private Predicate[] toArrayPredicates(List<Predicate> predicates) {
        if (Utils.isNotEmpty(predicates)) {
            Predicate[] result = new Predicate[predicates.size()];
            return predicates.toArray(result);
        }
        return null;
    }

    @Override
    public List findNativeQueryFirstAndMaxResult(Class clazz, String query, Object[] val, Integer first, Integer max) {
        Session sess;
        Query q;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            if (max != null) {
                q.setMaxResults(max);
            }
            if (first != null) {
                q.setFirstResult(first);
            }
            if (val != null) {
                for (int i = 1; i <= val.length; i++) {
                    q.setParameter(i, val[i - 1]);
                }
            }
            if (clazz != null) {
                result = q.setResultTransformer(Transformers.aliasToBean(clazz)).list();
            } else {
                result = (List) q.getResultList();
            }
            result.size();
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, "query " + query + " parametros: " + Arrays.toString(val), e);
        }
        return result;
    }

    @Override
    public Object getNativeQueryXpress(String query) {
        Session sess;
        Query q;
        Object ob = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            ob = (Object) q.getSingleResult();
            Hibernate.initialize(ob);
        } catch (HibernateException | NoResultException e) {
            //Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public List getNativeQueryParameter(Class clazz, String query, Object[] val) {
        Session sess;
        Query q;
        List result = null;
        try {
            sess = HiberUtil.getSession();
            q = sess.createSQLQuery(query);
            if (val != null) {
                for (int i = 0; i < val.length; i++) {
                    q.setParameter(i + 1, val[i]);
                }
            }
            result = q.setResultTransformer(Transformers.aliasToBean(clazz)).list();
            result.size();
        } catch (HibernateException e) {
            Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

}
