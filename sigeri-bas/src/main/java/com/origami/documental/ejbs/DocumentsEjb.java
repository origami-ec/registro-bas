/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.ejbs;

import com.origami.documental.HibernateDocumental;
import com.origami.documental.entities.TbData;
import com.origami.documental.entities.TbLibrerias;
import com.origami.documental.entities.TbParametrosSgr;
import com.origami.documental.entities.TbTipoDocCab;
import com.origami.documental.entities.TbTipoDocDet;
import com.origami.documental.entities.TbUsuarios;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.ReflexionEntity;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author ANGEL NAVARRO
 */
@Stateless(name = "DocumentsEjb")
@Dependent
public class DocumentsEjb implements Serializable {

    private static final Logger LOG = Logger.getLogger(DocumentsEjb.class.getName());

    @Inject
    private UserSession session;
    @Inject
    private Entitymanager manager;

    public EntityManager getEntityManger(Session sess) {
        if (sess == null) {
            return HibernateDocumental.getSession().getEntityManagerFactory().createEntityManager();
        } else {
            return sess.getEntityManagerFactory().createEntityManager();
        }
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
                if (key.contains(".")) {
                    String[] split = key.split("\\.");
                    int index = 0;
                    for (String sp : split) {
                        if (index == 0) { // PRIMER RECORRIDO SETEA CRITERIA PRINCIPAL
                            join = root.join(sp);
                        } else if (index < (split.length - 1)) {
                            join = join.join(sp);
                        } else {
                            if (filEntry.getValue() instanceof Number) {
                                predicates.add(builder.like(join.get(sp), "%" + filEntry.getValue() + "%"));
                            } else {
                                predicates.add(builder.equal(join.get(sp), filEntry.getValue()));
                            }
                        }
                        index++;
                    }
                } else {
                    if (filEntry.getValue() instanceof Number) {
                        predicates.add(builder.equal(root.get(filEntry.getKey()), filEntry.getValue()));
                    } else {
                        predicates.add(builder.like(root.get(filEntry.getKey()), "%" + filEntry.getValue() + "%"));
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

    public <T> T find(Class<T> entity, Object id) {
        T ob = null;
        try {
            try (Session sess = HibernateDocumental.getSession()) {
                ob = (T) sess.get(entity, (Serializable) id);
                Hibernate.initialize(ob);
            }
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public <T> T find(String base, Class<T> entity, Object id) {
        T ob = null;
        try {
            Session sess;
            if (base == null) {
                sess = HibernateDocumental.getSession();
            } else {
                sess = HibernateDocumental.getSessionData(base);
            }
            ob = (T) sess.get(entity, (Serializable) id);
            Hibernate.initialize(ob);
            sess.close();
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public <T> T findP(Class entity, Map<String, Object> paramt) {
        Session sess = HibernateDocumental.getSession();
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
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public List findAll(Class entity, Map<String, Object> paramt) {
        List result = null;
        try (Session sess = HibernateDocumental.getSession()) {
            try {
                CriteriaBuilder builder = sess.getCriteriaBuilder();
                CriteriaQuery query = builder.createQuery(entity);
                Root entityRoot = query.from(entity);
                if (paramt != null) {
                    Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                    if (predicates != null) {
                        query.where(predicates);
                    }
                }
                query.select(entityRoot);
                javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
                result = q.getResultList();
                result.size();
                Hibernate.initialize(result);
            } catch (HibernateException he) {
                LOG.log(Level.SEVERE, "", he);
            }
        }
        return result;
    }

    public List findAll(String sqlQuery, Map<String, Object> paramt) {
        List result = null;
        try (Session sess = HibernateDocumental.getSession()) {
            try {
                Query sq = sess.createQuery(sqlQuery);
                if (paramt != null) {
                    for (Map.Entry<String, Object> entry : paramt.entrySet()) {
                        sq.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                result = sq.getResultList();
                result.size();
                Hibernate.initialize(result);
            } catch (HibernateException he) {
                LOG.log(Level.SEVERE, "", he);
            }
        }
        return result;
    }

    public <T> T find(String sqlQuery, Map<String, Object> paramt) {
        Object result = null;
        try (Session sess = HibernateDocumental.getSession()) {
            try {
                Query sq = sess.createQuery(sqlQuery);
                if (paramt != null) {
                    for (Map.Entry<String, Object> entry : paramt.entrySet()) {
                        sq.setParameter(Integer.valueOf(entry.getKey()), entry.getValue());
                    }
                }
                result = sq.getSingleResult();
                Hibernate.initialize(result);
            } catch (HibernateException he) {
                LOG.log(Level.SEVERE, "", he);
            }
        }
        return (T) result;
    }

    public <T> T findObject(Class entity, String sqlQuery, Map<String, Object> paramt) {
        Object result = null;
        try (Session sess = HibernateDocumental.getSession()) {
            try {
                Query query = sess.createNativeQuery(sqlQuery, entity).setMaxResults(1);
                for (Map.Entry<String, Object> entry : paramt.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
                try {
                    result = query.getSingleResult();
                } catch (NoResultException ex) {
                    ex.toString();
                    return null;
                }
                Hibernate.initialize(result);
            } catch (HibernateException he) {
                LOG.log(Level.SEVERE, "", he);
            }
        }
        return (T) result;
    }

    public <T> T find(String sqlQuery, Object[] val) {
        Object result = null;
        try (Session sess = HibernateDocumental.getSession()) {
            try {
                NativeQuery sq = sess.createSQLQuery(sqlQuery);
                for (int i = 0; i < val.length; i++) {
                    sq.setParameter(i + 1, val[i]);
                }
                result = sq.getSingleResult();
                Hibernate.initialize(result);
            } catch (HibernateException he) {
                LOG.log(Level.SEVERE, "", he);
            }
        }
        return (T) result;
    }

    public <T> T find(String base, String sqlQuery, Map<String, Object> paramt) {
        Object result = null;
        try (Session sess = HibernateDocumental.getSessionData(base)) {
            try {
                NativeQuery sq = sess.createSQLQuery(sqlQuery);
                if (paramt != null) {
                    for (Map.Entry<String, Object> entry : paramt.entrySet()) {
                        sq.setParameter(Integer.valueOf(entry.getKey()), entry.getValue());
                    }
                }
                result = sq.getSingleResult();
                Hibernate.initialize(result);
            } catch (HibernateException he) {
                LOG.log(Level.SEVERE, "", he);
            }
        }
        return (T) result;
    }

    public Object persist(Object o) {
        Object ob = null;
        try {
            try (Session sess = HibernateDocumental.getSession()) {
                Transaction tx = sess.beginTransaction();
                if (ReflexionEntity.getIdFromEntity(o) == null) {
                    ob = sess.merge(o); // RETORNA EL OBJETO PERSISTIDO
                } else {
                    sess.update(o);
                    ob = o;
                }
                sess.flush();
                tx.commit();
            }
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public Object persist(String db, Object o) {
        Object ob = null;
        try {
            try (Session sess = HibernateDocumental.getSessionData(db)) {
                Transaction tx = sess.beginTransaction();
                if (ReflexionEntity.getIdFromEntity(o) == null) {
                    ob = sess.merge(o); // RETORNA EL OBJETO PERSISTIDO
                } else {
                    sess.update(o);
                    ob = o;
                }
                sess.flush();
                tx.commit();
            }
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    private TbTipoDocCab getDocCab(String base, String desCarpeta, String desTipoDoc) {
        Map<String, Object> paramt = new HashMap<>();
        paramt.put("tbCarpetas.desCarpeta", desCarpeta);
        paramt.put("desTipoDoc", desTipoDoc);
        return this.findP(TbTipoDocCab.class, paramt);
    }

    private List<TbTipoDocCab> getDocCabList(String desCarpeta, String desTipoDoc) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("desCarpeta", desCarpeta);
            paramt.put("desTipoDoc", "%" + desTipoDoc + "%");
            return this.findAll("SELECT dc FROM TbTipoDocCab dc INNER JOIN dc.tbCarpetas c WHERE c.desCarpeta = :desCarpeta "
                    + "AND dc.desTipoDoc LIKE :desTipoDoc", paramt);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public List<TbData> getDataFiles(String database, Date fechaInscripcion, Long id, Integer numInscripcion, Integer numRepertorio) {
        try {
            RegMovimiento mov = manager.find(RegMovimiento.class, id);
            List<TbTipoDocCab> docCab = this.getDocCabList(Utils.getAnio(fechaInscripcion).toString(), mov.getLibro().getNombre());
            TbTipoDocDet fieldInsc = null;
            TbTipoDocDet fieldfecha = null;
            if (fieldfecha == null) {
                fieldfecha = new TbTipoDocDet();
                fieldfecha.setIdRelacion("f11"); //fecha de inscripcion
            }
            if (fieldInsc == null) {
                fieldInsc = new TbTipoDocDet();
                fieldInsc.setIdRelacion("f02"); //numero de partida
            }
            List<TbData> result;
            try (Session sess = HibernateDocumental.getSessionData(database)) {
                CriteriaBuilder builder = sess.getCriteriaBuilder();
                CriteriaQuery query = builder.createQuery(TbData.class);
                Root entityRoot = query.from(TbData.class);
                query.select(entityRoot);
                if (Utils.isNotEmpty(docCab)) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(entityRoot.join("idCarpeta").join("idTipoDoc").in(docCab));
                    if (docCab.size() == 1) {
                        TbTipoDocCab doc = docCab.get(0);
                        predicates.add(builder.equal(entityRoot.get(doc.getFieldDocumental("numero de partida").getIdRelacion()), numInscripcion));
                        predicates.add(builder.like(entityRoot.get(doc.getFieldDocumental("fecha de inscripcion").getIdRelacion()), "%" + Utils.getAnio(fechaInscripcion) + "%"));
                    } else {
                        for (TbTipoDocCab doc : docCab) {
                            Predicate ins = builder.equal(entityRoot.get(doc.getFieldDocumental("numero de partida").getIdRelacion()), numInscripcion);
                            Predicate fec = builder.like(entityRoot.get(doc.getFieldDocumental("fecha de inscripcion").getIdRelacion()), "%" + Utils.getAnio(fechaInscripcion) + "%");
                            predicates.add(builder.and(ins, fec));
                        }
                    }
                    query.where(this.toArrayPredicates(predicates));
                } else {
                    query.where(
                            builder.equal(entityRoot.get(fieldInsc.getIdRelacion()), numInscripcion),
                            builder.like(entityRoot.get(fieldfecha.getIdRelacion()), "%" + Utils.getAnio(fechaInscripcion) + "%"));
                }
                query.distinct(true);
                javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
                result = q.getResultList();
                result.size();
                Hibernate.initialize(result);
            }
            return result;
        } catch (HibernateException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<TbData> getDataFiles(Map<String, Object> pms) {
        try {
            return this.findAll(TbData.class, pms);
        } catch (HibernateException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean saveDataDocument(TbLibrerias bases, TbData data) {
        try {
            AclUser userSgr = manager.find(AclUser.class, session.getUserId());
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("idUsuario", userSgr.getUserDocumental());
            TbUsuarios userDocuments = this.findP(TbUsuarios.class, paramt);
            String ipClient = session.getDatosEquipo();
            paramt = new HashMap<>();
            paramt.put("ip", ipClient);
            TbParametrosSgr sgr = this.findP(TbParametrosSgr.class, paramt);
            if (sgr == null) {
                sgr = new TbParametrosSgr();
            }
            sgr.setIdTransaccion(data.getIdTransaccion().intValue());
            sgr.setIdLibreria(bases.getIdLibreria());
            sgr.setIp(ipClient);
            sgr.setIdCarpeta(data.getIdCarpeta().getIdCarpeta());
            sgr.setIdUsuario(userDocuments.getCodUsuario());
            sgr.setPass(userSgr.getPassDocumental());
            sgr.setEstado(1);
            this.persist(sgr);
            return true;
        } catch (HibernateException he) {
            LOG.log(Level.SEVERE, "", he);
            return false;
        }
    }

    public List<TbData> getDataFiles(String bases, Date fechaInscripcion, Integer numInscripcion,
            Integer numRepertorio, String nombreLibro) {
        try {
            List<TbData> result;
            try (Session sess = HibernateDocumental.getSessionData(bases)) {
                List<TbTipoDocCab> docCab = this.getDocCabList(Utils.getAnio(fechaInscripcion) + "", nombreLibro);
                TbTipoDocDet fieldInsc = null;
                TbTipoDocDet fieldfecha = null;
                if (fieldfecha == null) {
                    fieldfecha = new TbTipoDocDet();
                    fieldfecha.setIdRelacion("f03");
                }
                if (fieldInsc == null) {
                    fieldInsc = new TbTipoDocDet();
                    fieldInsc.setIdRelacion("f02");
                }
                CriteriaBuilder builder = sess.getCriteriaBuilder();
                CriteriaQuery query = builder.createQuery(TbData.class);
                Root entityRoot = query.from(TbData.class);
                query.select(entityRoot);
                List<Predicate> predicates = new ArrayList<>();
                if (Utils.isNotEmpty(docCab)) {// ipoDocCab{idTipoDoc=23, desTipoDoc=PROPIEDAD HORIZONTAL-HISTORICO,
                    predicates.add(entityRoot.join("idCarpeta").join("idTipoDoc").in(docCab));
                    if (docCab.size() == 1) {
                        TbTipoDocCab doc = docCab.get(0);
                        predicates.add(builder.equal(entityRoot.get(doc.getFieldDocumental("Numero de Inscripcion").getIdRelacion()), numInscripcion + ""));
                        predicates.add(builder.like(entityRoot.get(doc.getFieldDocumental("Fecha de Inscripcion").getIdRelacion()), "%" + Utils.getAnio(fechaInscripcion) + "%"));
                    } else {
                        for (TbTipoDocCab doc : docCab) {
                            Predicate ins = builder.equal(entityRoot.get(doc.getFieldDocumental("Numero de Inscripcion").getIdRelacion()), numInscripcion + "");
                            Predicate fec = builder.like(entityRoot.get(doc.getFieldDocumental("Fecha de Inscripcion").getIdRelacion()), "%" + Utils.getAnio(fechaInscripcion) + "%");
                            predicates.add(builder.and(ins, fec));
                        }
                    }
//                    query.where(builder.or(this.toArrayPredicates(predicates)));
                } else {
                    query.where(//builder.equal(entityRoot.get("f01"), numRepertorio),
                            builder.equal(entityRoot.get(fieldInsc.getIdRelacion()), numInscripcion),
                            builder.like(entityRoot.get(fieldfecha.getIdRelacion()), "%" + Utils.getAnio(fechaInscripcion) + "%"));
                }
                query.where(this.toArrayPredicates(predicates));
                javax.persistence.Query q = this.getEntityManger(sess).createQuery(query);
                result = q.getResultList();
                result.size();
                Hibernate.initialize(result);
            }
            return result;
        } catch (HibernateException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
