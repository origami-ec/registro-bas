/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegpObservacionesIngreso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author Anyelo
 */
public class RegpObservacionesLazy extends LazyModel<RegpObservacionesIngreso> {

    public RegpObservacionesLazy() {
        super(RegpObservacionesIngreso.class, "fechaIngreso", "DESC");
    }

//    @Override
//    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
//        if (filters == null) {
//            filters = new HashMap<>();
//        }
//        List<Predicate> predicates = new ArrayList<>();
//        Join join = root.join("ente", JoinType.INNER);
//        ;
//        if (filters.containsKey("documento")) {
//            predicates.add(this.builder.like(join.get("documento"), "%" + filters.get("documento").toString().trim() + "%"));
//        }
//        if (filters.containsKey("nombres")) {
//            predicates.add(this.getOrPredicate(join, "nombres", "razonSocial", filters.get("nombres").toString().trim().replaceAll(" ", "%")));
//        }
//        if (filters.containsKey("apellidos")) {
//            predicates.add(this.getOrPredicate(join, "apellidos", "nombreComercial", filters.get("apellidos").toString().trim().replaceAll(" ", "%")));
//        }
//        predicates.addAll(this.findPropertyFilter(root, filters));
//        addWhere(predicates, crit);
//    }
}
