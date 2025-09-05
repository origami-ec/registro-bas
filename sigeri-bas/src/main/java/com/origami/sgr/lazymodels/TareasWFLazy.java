/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.views.TareasActivas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author User
 */
public class TareasWFLazy extends LazyModel<TareasActivas> {

    protected String taskDefKey;
    protected String usuario = "admin";

    public TareasWFLazy() {
        super(TareasActivas.class, "fechaEntrega", "ASC");
        this.setDistinct(false);
    }

    public TareasWFLazy(String user) {
        super(TareasActivas.class, "fechaEntrega", "ASC");
        this.usuario = user;
        this.setDistinct(false);
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        try {
            if (filters == null) {
                filters = new HashMap<>();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (usuario != null) {
                Predicate equal = this.builder.equal(this.builder.upper(root.get("assignee")), usuario.toUpperCase());
                Predicate like = this.builder.like(this.builder.upper(root.get("candidate")), "%".concat(usuario.toUpperCase()).concat("%"));
                predicates.add(this.builder.or(equal, like));
            }
            if (!filters.isEmpty()) {
                predicates.addAll(this.findPropertyFilter1(root, filters));
            }
            if (!getFilterss().isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, getFilterss()));
            }
            return predicates;
        } catch (Exception e) {
            Logger.getLogger(TareasWFLazy.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
