/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegEnteInterviniente;
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
public class RegEnteIntervinienteLazy extends LazyModel<RegEnteInterviniente> {

    private String cadena;
    private String consulta;
    private int tipoConsulta = 0;

    public RegEnteIntervinienteLazy() {
        super(RegEnteInterviniente.class);
    }

    public RegEnteIntervinienteLazy(String cadena) {
        super(RegEnteInterviniente.class);
        this.cadena = cadena;
    }

    public RegEnteIntervinienteLazy(String consulta, int tipoConsulta) {
        super(RegEnteInterviniente.class);
        this.consulta = consulta;
        this.tipoConsulta = tipoConsulta;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();

        if (tipoConsulta == 1) {
            predicates.add(this.builder.like(root.get("nombre"), "%" + consulta.trim() + "%"));
        } else if (tipoConsulta == 2) {
            predicates.add(this.builder.like(root.get("cedRuc"), "%" + consulta.trim() + "%"));
        }
        if (cadena != null) {
            predicates.add(builder.or(builder.like(builder.upper(root.get("cedRuc")), "%".concat(cadena.trim().toUpperCase()).concat("%")),
                    builder.like(builder.upper(root.get("nombre")), "%".concat(cadena.trim().toUpperCase()).concat("%"))));
        }

        if (!filters.isEmpty()) {
            predicates.addAll(this.findPropertyFilter1(root, filters));
        }
        if (!getFilterss().isEmpty()) {
            predicates.addAll(this.findPropertyFilter(root, getFilterss()));
        }
        addWhere(predicates, crit);
        return predicates;
    }

}
