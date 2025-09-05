/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpLiquidacion;
import java.math.BigDecimal;
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
public class RegpLiquidacionLazy extends LazyModel<RegpLiquidacion> {

    private Long estadoPago;
    private Long estadoPago2;
    private Boolean ingresado;
    private BigDecimal limite;

    public RegpLiquidacionLazy() {
        super(RegpLiquidacion.class, "numTramiteRp", "DESC");
    }

    public RegpLiquidacionLazy(Long estadoPago) {
        super(RegpLiquidacion.class, "fechaCreacion", "DESC");
        this.estadoPago = estadoPago;
    }

    public RegpLiquidacionLazy(Long estadoPago, Long estadoPago2) {
        super(RegpLiquidacion.class, "fechaCreacion", "DESC");
        this.estadoPago = estadoPago;
        this.estadoPago2 = estadoPago2;
    }

    public RegpLiquidacionLazy(Boolean ingresado) {
        super(RegpLiquidacion.class, "fechaCreacion", "DESC");
        this.ingresado = ingresado;
    }

    public RegpLiquidacionLazy(BigDecimal valor) {
        super(RegpLiquidacion.class, "numTramiteRp", "DESC");
        limite = valor;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        try {
            if (filters == null) {
                filters = new HashMap<>();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (estadoPago != null && estadoPago2 == null) {
                predicates.add(builder.equal(root.get("estadoPago"), new RegpEstadoPago(estadoPago)));
            }
            if (estadoPago != null && estadoPago2 != null) {
                predicates.add(root.get("estadoPago").in(Arrays.asList(new RegpEstadoPago(estadoPago), new RegpEstadoPago(estadoPago2))));
            }
            if (ingresado != null) {
                predicates.add(builder.equal(root.get("ingresado"), ingresado));
            }
            if (limite != null) {
                predicates.add(builder.greaterThan(root.get("totalPagar"), limite));
            }
            if (!filters.isEmpty()) {
                predicates.addAll(this.findPropertyFilter1(root, filters));
            }
            if (!getFilterss().isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, getFilterss()));
            }
            addWhere(predicates, crit);
            return predicates;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
