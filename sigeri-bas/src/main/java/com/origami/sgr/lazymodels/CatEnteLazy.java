/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.CatEnte;
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
public class CatEnteLazy extends LazyModel<CatEnte> {

    public CatEnteLazy() {
        super(CatEnte.class, "id", "DESC");
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        try {
            if (filters == null) {
                filters = new HashMap<>();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (filters.containsKey("nombrerazonsocial")) {
                predicates.add(this.getOrPredicate(root, Arrays.asList("nombres", "razonSocial"),
                        Arrays.asList(filters.get("nombrerazonsocial").getFilterValue().toString().trim().replaceAll(" ", "%"))));
                addIgnoreFilters("nombrerazonsocial");
            }
            if (filters.containsKey("apellidonombrecomercial")) {
                predicates.add(this.getOrPredicate(root, Arrays.asList("apellidos", "nombreComercial"),
                        Arrays.asList(filters.get("apellidonombrecomercial").getFilterValue().toString().trim().replaceAll(" ", "%"))));
                addIgnoreFilters("apellidonombrecomercial");
            }
            if (!filters.isEmpty()) {
                predicates.addAll(this.findPropertyFilter1(root, filters));
            }
            if (!getFilterss().isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, getFilterss()));
            }
            return predicates;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
