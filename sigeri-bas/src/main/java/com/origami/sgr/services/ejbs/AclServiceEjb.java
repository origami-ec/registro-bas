/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.acl.AccessLevelRequest;
import com.origami.sgr.acl.RespuestaAcceso;
import com.origami.sgr.acl.entity.AclUrl;
import com.origami.sgr.acl.service.AclCache;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.services.interfaces.AclService;
import com.origami.sgr.util.HiberUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 *
 * @author Anyelo
 */
@Singleton(name = "aclService")
@Lock(LockType.READ)
@Interceptors(value = {HibernateEjbInterceptor.class})
public class AclServiceEjb implements AclService {

    @EJB
    private AclCache cacheServ;

    /**
     * devuelve la lista de roles que tinen permiso para acceder a la url
     * ingresada como parametro
     *
     * @param urlEnt AclUrl
     * @return possible object is {@link List }
     */
    @Override
    public List<AclRol> getRoles_url(AclUrl urlEnt) {
        Session sess = HiberUtil.getSession();
        Class entity = AclRol.class;
        List<AclRol> result = null;
//        Criteria crit = sess.createCriteria(AclRol.class);
//        crit.createAlias("urlHasRolColl", "uhr1");
//        crit.add(Restrictions.eq("uhr1.url", urlEnt));
        //
        CriteriaBuilder builder = sess.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(entity);
        Root entityRoot = query.from(entity);
        entityRoot.join("urlHasRolColl", JoinType.LEFT).alias("uhr1");
        query.select(entityRoot);
        query.where(builder.equal(entityRoot.get("uhr1.url"), urlEnt));
        javax.persistence.Query cq = sess.getEntityManagerFactory().createEntityManager().createQuery(query);
        result = cq.getResultList();
        if (result != null) {
            result.size();
            Hibernate.initialize(result);
        }
        return result;
    }

    /**
     * devuelve la lista de roles que tinen permiso para acceder a la url
     * ingresada como parametro
     *
     * @param idUrl Long
     * @return possible object is {@link List }
     */
    @Override
    public List<AclRol> getRoles_url(Long idUrl) {
        Session sess = HiberUtil.getSession();
        AclUrl urlEnt = (AclUrl) sess.load(AclUrl.class, idUrl);
        return this.getRoles_url(urlEnt);
    }

    /**
     * consulta en la memoria cache el nivel de acceso con el que cuenta el
     * usuario logeado
     *
     * @param alreq AccessLevelRequest
     * @return possible object is {@link RespuestaAcceso }
     */
    @Override
    public RespuestaAcceso checkAccessLevel(AccessLevelRequest alreq) {
        RespuestaAcceso respuesta = new RespuestaAcceso();
        // si el acl esta deshabilitado, dar acceso
        if (!Boolean.TRUE.equals(cacheServ.getEnabled())) {
            // acceso libre:
            respuesta.setAccessGranted(true);
            return respuesta;
        }
        // si es Super User, dar accesos:
        if (alreq.getEsSuperUser()) {
            respuesta.setAccessGranted(true);
            return respuesta;
        }
        // consulta cache-transaccional:
        Boolean ok = cacheServ.checkAccess(alreq.getUrlAcceso(), alreq.getIdUser(), alreq.getRolesIds());
        respuesta.setAccessGranted(ok);
        return respuesta;
    }

    public EntityManager getEntityManger() {
        return HiberUtil.getSession().getEntityManagerFactory().createEntityManager();
    }
}
