/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegpCorreccion;

/**
 *
 * @author asilva
 */
public class RegpCorreccionLazy extends LazyModel<RegpCorreccion> {

    public RegpCorreccionLazy() {
        super(RegpCorreccion.class, "id", "DESC");
    }

//    @Override
//    public void criteriaFilterSetup(Criteria crit, Map<String, Object> filters) throws Exception {
//        Criteria c = crit.createCriteria("idTramite");
//        if (filters.containsKey("numTramite")) {
//            c.add(Restrictions.eq("numTramite", new Long(filters.get("numTramite").toString().trim())));
//        }
//        if (filters.containsKey("propietario")) {
//            c.add(Restrictions.ilike("nombrePropietario", "%"+filters.get("propietario").toString().trim()+"%"));
//        }
//    }
}
