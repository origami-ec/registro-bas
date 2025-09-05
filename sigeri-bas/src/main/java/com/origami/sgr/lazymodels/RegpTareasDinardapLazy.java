/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegpTareasDinardap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate; 
import org.primefaces.model.FilterMeta;
/**
 *
 * @author gutya
 */
public class RegpTareasDinardapLazy extends LazyModel<RegpTareasDinardap> {

    private Boolean estado;
    private Boolean esJuridico;

    public RegpTareasDinardapLazy() {
        super(RegpTareasDinardap.class, "fecha", "DESC");
    }

    public RegpTareasDinardapLazy(Boolean estado, Boolean esJuridico) {
        super(RegpTareasDinardap.class, "fecha", "DESC");
        this.estado = estado;
        this.esJuridico = esJuridico;
    }

    public RegpTareasDinardapLazy(Boolean todoTrue) {
        super(RegpTareasDinardap.class, "fecha", "DESC");
        this.estado = Boolean.TRUE;
        //    this.esJuridico = Boolean.TRUE;
    }

//    @Override
//    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        if (filters == null) {
//            filters = new HashMap<>();
//        }
//
//        if (filters.containsKey("id")) {
//            predicates.add(this.builder.equal(root.get("numRepertorio"), new Long(filters.get("id").toString().trim())));
//        }
//        if (filters.containsKey("ticket")) {
//            predicates.add(this.builder.like(root.get("ticket"), "%" + filters.get("ticket").toString().trim() + "%"));
//        }
//        if (filters.containsKey("institucion")) {
//            predicates.add(this.builder.like(root.get("institucion"), "%" + filters.get("institucion").toString().trim() + "%"));
//        }
//        if (filters.containsKey("solicitante")) {
//            predicates.add(this.builder.like(root.get("solicitante"), "%" + filters.get("solicitante").toString().trim() + "%"));
//        }
//        if (filters.containsKey("numeroSolicitud")) {
//            predicates.add(this.builder.like(root.get("numeroSolicitud"), "%" + filters.get("numeroSolicitud").toString().trim() + "%"));
//
//        }
//        predicates.add(this.builder.equal(root.get("estado"), estado));
//        if (esJuridico != null) {
//            predicates.add(this.builder.equal(root.get("esJuridico"), esJuridico));
//        }
//        //predicates.addAll(this.findPropertyFilter(root, filters));
//        addWhere(predicates, crit);
//    }

}
