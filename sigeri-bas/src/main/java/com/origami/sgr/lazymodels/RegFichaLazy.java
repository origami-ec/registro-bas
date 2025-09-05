/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
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
public class RegFichaLazy extends LazyModel<RegFicha> {

    private Collection list;
    private String valor;
    private int tipo = 0;
    private String ciRuc;
    private String linderos;
    private Long tipoLibro;
    private RegTipoFicha tipoficha;

    public RegFichaLazy() {
        super(RegFicha.class);
        this.addSorted("numFicha", "DESC");
    }

    public RegFichaLazy(String valor, int tipo) {
        super(RegFicha.class);
        this.addSorted("numFicha", "DESC");
        this.valor = valor;
        this.tipo = tipo;
    }

    public RegFichaLazy(Collection collection) {
        super(RegFicha.class);
        this.addSorted("numFicha", "ASC");
        list = collection;
    }

    public RegFichaLazy(RegTipoFicha tipoficha) {
        super(RegFicha.class);
        this.addSorted("numFicha", "DESC");
        this.tipoficha = tipoficha;
    }
    
    public RegFichaLazy(String cedula, Long tipoLibro) {
        super(RegFicha.class);
        this.addSorted("numFicha", "DESC");
        this.ciRuc = cedula;
        this.tipoLibro = tipoLibro;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();

        if (tipo != 0) {
            switch (tipo) {
                case 1: //FILTRO POR CEDULA-RUC-DOCUMENTO
                    ciRuc = valor;
                    break;
                case 2: //FILTRO POR LINDEROS
                    linderos = valor;
                    break;
            }
        }
        if (Utils.isNotEmpty(list)) {
            predicates.add(root.get("id").in(list));
        }
        if (ciRuc != null && !ciRuc.isEmpty()) {
            Join join = root.joinCollection("regMovimientoFichaCollection", JoinType.INNER)
                    .join("movimiento", JoinType.INNER)
                    .joinCollection("regMovimientoClienteCollection", JoinType.INNER)
                    .join("enteInterv");
            predicates.add(this.builder.like(join.get("cedRuc"), "%" + ciRuc.trim() + "%"));
        }
        if (this.tipoLibro != null) {
            Join join = root.joinCollection("regMovimientoFichaCollection", JoinType.INNER)
                    .join("movimiento", JoinType.INNER).join("libro", JoinType.INNER);
            predicates.add(this.builder.equal(join.get("tipo"), tipoLibro.intValue()));
        }
        if (linderos != null && !linderos.isEmpty()) {
            predicates.add(builder.or(builder.like(builder.upper(root.get("linderos")), "%".concat(linderos.trim().toUpperCase()).concat("%")),
                    builder.like(builder.upper(root.get("descripcionBien")), "%".concat(linderos.trim().toUpperCase()).concat("%"))));
        }
        if (this.tipoficha != null) {
            predicates.add(this.builder.equal(root.get("tipoFicha"), this.tipoficha));
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
