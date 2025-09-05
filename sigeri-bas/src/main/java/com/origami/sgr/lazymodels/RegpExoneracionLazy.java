/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegpExoneracion;
import java.util.ArrayList;
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
public class RegpExoneracionLazy extends LazyModel<RegpExoneracion> {

    private Boolean estate;

    public RegpExoneracionLazy() {
        super(RegpExoneracion.class, "id", "ASC");
    }

    public RegpExoneracionLazy(Boolean estado) {
        super(RegpExoneracion.class, "id", "ASC");
        estate = estado;
    }
/*
    @Override
    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        if (estate != null) {
            predicates.add(builder.equal(root.get("estado"), estate));
        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        addWhere(predicates, crit);
    }*/

}
