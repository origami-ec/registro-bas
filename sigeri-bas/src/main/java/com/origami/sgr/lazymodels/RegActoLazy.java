/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegTipoCobroActo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author Origami
 */
public class RegActoLazy extends LazyModel<RegActo> {

    private RegTipoCobroActo tipo;

    public RegActoLazy() {
        super(RegActo.class, "nombre", "ASC");
    }

    public RegActoLazy(RegTipoCobroActo cobro) {
        super(RegActo.class, "nombre", "ASC");
        this.tipo = cobro;
    }

//    @Override
//    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
//        if (filters == null) {
//            filters = new HashMap<>();
//        }
//        List<Predicate> predicates = new ArrayList<>();
//        if (filters.containsKey("nombre")) {
//            predicates.add(this.builder.like(this.builder.upper(root.get("nombre")),
//                    filters.get("nombre").toString().toUpperCase().trim() + "%"));
//            ignoreFilters.add("nombre");
//        }
//        if (this.tipo != null) {
//            predicates.add(this.builder.equal(root.get("tipoCobro"), this.tipo));
//        }
//        predicates.addAll(this.findPropertyFilter(root, filters));
//        addWhere(predicates, crit);
//    }

}
