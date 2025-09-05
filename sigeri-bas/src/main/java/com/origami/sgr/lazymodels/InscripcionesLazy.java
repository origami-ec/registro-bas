/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author Anyelo
 */
public class InscripcionesLazy extends LazyModel<RegMovimiento> {

    private int sizeLoad = 0;
    private RegLibro libro;
    private Integer numInscripcion;
    private Integer numRepertorio;
    private String secuenciaIns;
    private String secuenciaRep;
    private Integer anioInsc;

    public InscripcionesLazy() {
        super(RegMovimiento.class);
        this.Sorteds.put("fechaInscripcion", "DESC");
        this.Sorteds.put("numRepertorio", "DESC");
    }

    public InscripcionesLazy(RegLibro libro, Integer inscripcion, Integer repertorio, String secuenciaIns,
            String secuenciaRep, Integer anioInsc) {
        super(RegMovimiento.class);
        this.libro = libro;
        this.numInscripcion = inscripcion;
        this.numRepertorio = repertorio;
        this.secuenciaIns = secuenciaIns;
        this.secuenciaRep = secuenciaRep;
        this.anioInsc = anioInsc;
    }

//    @Override
//    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
//        if (filters == null) {
//            filters = new HashMap<>();
//        }
//        List<Predicate> predicates = new ArrayList<>();
//        predicates.add(root.get("estado").in(Arrays.asList("AC", "AN", "IN")));
//
//        if (this.libro != null) {
//            System.out.println("Libro: " + this.libro.getNombre());
//            predicates.add(this.builder.equal(root.get("libro"), this.libro));
//        }
//        if (this.numInscripcion != null) {
//            System.out.println("Nro Inscripcion: " + this.numInscripcion);
//            predicates.add(this.builder.equal(root.get("numInscripcion"), this.numInscripcion));
//        }
//        if (this.numRepertorio != null) {
//            predicates.add(this.builder.equal(root.get("numRepertorio"), this.numRepertorio));
//        }
//        if (this.anioInsc != null) {
//            System.out.println("Anio: " + this.anioInsc);
////            crit.add(Restrictions.sqlRestriction("cast(to_char(fecha_inscripcion, 'yyyy') as int) = (?)", anioInsc, StandardBasicTypes.INTEGER));
//            predicates.add(this.builder.equal(this.builder.function("year", Integer.class, root.get("fechaInscripcion")), anioInsc));
//        }
//        if (this.secuenciaIns != null) {
//            this.secuenciaIns = this.secuenciaIns.replace(" ", "");
//            if (!this.secuenciaIns.equals("")) {
//                predicates.add(this.builder.equal(root.get("secuenciaInscripcion"), this.secuenciaIns));
//            }
//        }
//        if (this.secuenciaRep != null) {
//            this.secuenciaRep = this.secuenciaRep.replace(" ", "");
//            if (!this.secuenciaRep.equals("")) {
//                predicates.add(this.builder.equal(root.get("secuenciaRepertorio"), this.secuenciaRep));
//            }
//
//        }
//        predicates.addAll(this.findPropertyFilter(root, filters));
//        addWhere(predicates, crit);
//    }

//    @Override
//    public List<RegMovimiento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//        List result = null;
//        Criteria cq, dcq;
//        try {
//            cq = manager.getSession().createCriteria(this.getEntity(), "entity");
//            this.criteriaFilterSetup(cq, filters);
//            cq.setProjection(Projections.projectionList().add(Projections.rowCount()));
//            dcq = manager.getSession().createCriteria(this.getEntity(), "entity1");
//
//            this.criteriaFilterSetup(dcq, filters);
//            if (orderCrit != null) {
//                this.criteriaSortSetup(orderCrit, orderField, sortOrder);
//            } else {
//                this.criteriaSortSetup(dcq, sortField, sortOrder);
//            }
//            this.loadAllSize(dcq);
//            this.criteriaPageSetup(dcq, first, pageSize);
//            rowCount = 0;
//            rowCount = ((Long) cq.uniqueResult()).intValue();
//            this.setRowCount(rowCount);
//            result = dcq.list();
//            Hibernate.initialize(result);
//        } catch (Exception ex) {
//            Logger.getLogger(BaseLazyDataModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return result;
//    }
    public int getSizeData() {
        return sizeLoad;
    }

//    public void loadAllSize(Criteria crit) {
//        try {
//            sizeLoad = crit.list().size();
//        } catch (Exception e) {
//            Logger.getLogger(BaseLazyDataModel.class.getName()).log(Level.SEVERE, null, e);
//        }
//    }
}
