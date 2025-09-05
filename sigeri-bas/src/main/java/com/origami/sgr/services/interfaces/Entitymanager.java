/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Origami Solutions
 */
@Local
public interface Entitymanager {

    public <T extends Object> T find(Class<T> entity, Object id);

    public <T extends Object> List<T> findAll(Class<T> entity);

    public Object find(String query, String[] par, Object[] val);

    public List findMax(String query, String[] par, Object[] val, Integer max);

    public List findAll(String query);

    public <T extends Object> T findNoProxy(Class<T> entity, Object id);

    public List findAll(String query, String[] par, Object[] val);

    public <T> List<T> findAllEntCopy(Class<T> entity);

    public List findAllEntCopy(String query, String[] par, Object[] val);

    public List findAllEntCopy(String query);

    public Object persist(Object o);

    public Object saveAll(Object entity);

    public boolean delete(Object o);
    
    public boolean remove(Object o);

    public boolean update(Object o);

    public Object merge(Object o);

    public Object getNativeQuery(String query);

    public Object getNativeQuery(String query, Object[] val);

    public int updateNativeQuery(String query, Object[] val);
    
    public int executeNativeQuery(String query);

    public List getSqlQuery(String query);

    public <T> List<T> getSqlQueryParametros(Class<T> clase, String query);

    public <T> List<T> getSqlQueryValues(Class<T> clase, String query, Object[] values);
    
    public <T> T getSqlQueryObjectValues(Class<T> clase, String query, Object[] values);

    public <T> List<T> getSqlQueryParametros(Class<T> clase, String query, String[] params, Object[] values);

    public List<Object[]> getManyColumnsResults(String query, String[] params, Object[] values);

    public List<Object[]> getManyColumnsResults(String query);

    public Session getSession();

    public Transaction requiredTransaction();

    public List findAllByEntities(String query, String[] entitiesNames, Object[] entities);

    public boolean saveList(List entities);

    public boolean deleteList(List entities);

    public Object find(String query);

    public List findFirstAndMaxResult(String query, String[] par, Object[] val, Integer first, Integer max);

    public <T> List<T> findAllOrdered(Class<T> entity, String[] order, Boolean[] prior);

    public <T> List<T> findAllOrdEntCopy(Class<T> entity, String[] order, Boolean[] prior);

    public <T> T findObjectByParameter(Class entity, Map<String, Object> paramt);

    public <T> List<T> findAllObjectOrder(Class entity, String[] order, Boolean ascDes);

    public <T> List<T> findObjectByParameterList(Class entity, Map<String, Object> paramt);

    public <T> List<T> findObjectByParameterOrderList(Class entity, Map<String, Object> paramt, String[] order, Boolean ascDes);

    public Object findObjectByParameter(String query, Map<String, Object> paramt);

    public void executeFunction(String function, Map<String, Object> paramt, Boolean tipoVoid);
    
    public void executeFunction(String function);

    public List findNamedQuery(String namedQuery, Map<String, Object> paramt);

    public <T> T findObjectNamedQuery(String namedQuery, Map<String, Object> paramt);

    public EntityManager getEntityManger();

    public List findList(Connection c, String sql, List<Object> paramt, Class objectTranform) throws SQLException;

    public Object find(Connection c, String sql, List<Object> paramt) throws SQLException;

    public <T> T find(Connection c, String sql, List<Object> paramt, Class objectTranform) throws SQLException;

    public boolean saveOrUpdate(Object o);

    public List findNativeQueryFirstAndMaxResult(Class clazz, String query, Object[] val, Integer first, Integer max);

    public void evict(Object obj);

    public void detach(Object obj);
    
    public Object getNativeQueryXpress(String query);
    
    public List getNativeQueryParameter(Class clazz, String query, Object[] val);
    
}
