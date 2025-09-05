/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.acl.service;

import com.origami.sgr.acl.cache.UrlRule;
import com.origami.sgr.acl.entity.AclUrl;
import com.origami.sgr.services.ejbs.HibernateEjbInterceptor;
import com.origami.sgr.util.HiberUtil;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 *
 * @author Fernando
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Interceptors(value = {HibernateEjbInterceptor.class})
public class AclCacheServ extends CacheUrlSuper implements AclCache {

    @Override
    public Boolean checkAccess(String url, Long idUser, List<Long> rolesIds) {
        // obtener regla del cache hashmap
        UrlRule rule = this.getRulesMap().get(url);
        // si no existe regla para esta url es pública, dar acceso libre:
        if (rule == null) {
            return true;
        } // si existe regla, comprobar:
        else {
            // si es url libre de roles y es usuario logueado, aceptar acceso
            if (rule.getRolesMap().isEmpty() && idUser != null) {
                return true;
            }
            // comprobar si algun rol hace match:
            if (rolesIds != null) {
                for (Long cdRolId : rolesIds) {
                    if (rule.getRolesMap().containsKey(cdRolId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public AclCacheServ() {
    }

    @Override
    public void clear() {
        this.setRulesMap(null);
    }

    @Override
    protected synchronized void initMap() {
        if (getRulesMap() != null) {
            return;
        }
        ConcurrentMap<String, UrlRule> urlMap = new ConcurrentHashMap<>();
        List<AclUrl> urls = this.listUrls();
        for (AclUrl cdUrl : urls) {
            UrlRule rule = new UrlRule();
            rule.setId(cdUrl.getId());
            rule.setUrl(cdUrl.getUrl());
            // add to map
            urlMap.put(cdUrl.getUrl(), rule);
        }
        // set as cache object:
        this.setRulesMap(urlMap);
    }

    protected List<AclUrl> listUrls() {
        List result = null;
        Session sess = HiberUtil.getSession();
        Class entity = AclUrl.class;
//        Criteria crit = sess.createCriteria(AclUrl.class);
//        crit.add(Restrictions.eq("sisEnabled", true));

        CriteriaBuilder builder = sess.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(entity);
        Root entityRoot = query.from(entity);
        query.select(entityRoot);
        query.where(builder.equal(entityRoot.get("sisEnabled"), true));
        javax.persistence.Query cq = sess.getEntityManagerFactory().createEntityManager().createQuery(query);

        result = cq.getResultList();
        if (result != null) {
            result.size();
            Hibernate.initialize(result);
        }

        return result;
    }

}
