/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.util.Utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
public class RegMovimientosLazy extends LazyModel<RegMovimiento> {

    private Date desde, hasta;
    private Collection list;
    private String valor, cedRuc;
    private int tipo = 0;
    private RegLibro libro;
    private RegActo acto;
    private Long inscripcion;
    private Long repertorio;
    private Integer folio;
    private String tomo;
    private BigDecimal baseImponible;

    public RegMovimientosLazy() {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "DESC");
        this.addSorted("numInscripcion", "DESC");
    }

    public RegMovimientosLazy(String valor, int tipo) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "DESC");
        this.addSorted("numInscripcion", "DESC");
        this.valor = valor;
        this.tipo = tipo;
    }

    public RegMovimientosLazy(Collection values) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "DESC");
        this.addSorted("numInscripcion", "DESC");
        list = values;
    }

    public RegMovimientosLazy(String cedula) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "DESC");
        this.addSorted("numInscripcion", "DESC");
        this.cedRuc = cedula;
    }

    public RegMovimientosLazy(Date desde, Date hasta) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "ASC");
        this.addSorted("libro", "ASC");
        this.addSorted("numInscripcion", "ASC");
        this.desde = desde;
        this.hasta = hasta;
    }
    
    public RegMovimientosLazy(Date desde, Date hasta, BigDecimal base) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "ASC");
        this.addSorted("libro", "ASC");
        this.addSorted("numInscripcion", "ASC");
        this.desde = desde;
        this.hasta = hasta;
        this.baseImponible = base;
    }

    public RegMovimientosLazy(RegLibro libro, Date desde, Date hasta) {
        super(RegMovimiento.class, "numInscripcion", "ASC");
        this.libro = libro;
        this.desde = desde;
        this.hasta = hasta;
    }

    public RegMovimientosLazy(RegLibro libro, RegActo acto, Long inscripcion,
            Long repertorio, Date desde, Date hasta) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "ASC");
        this.addSorted("libro", "ASC");
        this.addSorted("numInscripcion", "ASC");
        this.libro = libro;
        this.acto = acto;
        this.inscripcion = inscripcion;
        this.repertorio = repertorio;
        this.desde = desde;
        this.hasta = hasta;
    }

    public RegMovimientosLazy(RegLibro libro, RegActo acto, Long inscripcion,
            Long repertorio, Integer folio, String tomo, Date desde, Date hasta) {
        super(RegMovimiento.class);
        this.addSorted("fechaInscripcion", "ASC");
        this.addSorted("libro", "ASC");
        this.addSorted("numInscripcion", "ASC");
        this.libro = libro;
        this.acto = acto;
        this.inscripcion = inscripcion;
        this.repertorio = repertorio;
        this.folio = folio;
        this.tomo = tomo;
        this.desde = desde;
        this.hasta = hasta;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        try {
            if (filters == null) {
                filters = new HashMap<>();
            }
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get("estado").in(Arrays.asList("AC", "IN")));
            predicates.add(root.get("fechaInscripcion").isNotNull());
            if (tipo != 0) {
                if (tipo == 1) {
                    predicates.add(this.builder.equal(root.get("numInscripcion"), Integer.getInteger(valor.trim())));
                } else if (tipo == 2) {
                    predicates.add(this.builder.equal(root.get("numRepertorio"), Integer.getInteger(valor.trim())));
                }
            }
            if (Utils.isNotEmpty(list)) {
                predicates.add(root.get("id").in(list));
            }
            if (desde != null && hasta != null) {
                predicates.add(this.builder.between(root.get("fechaInscripcion"), desde, hasta));
            }
            if (this.libro != null && this.libro.getId() != null) {
                predicates.add(this.builder.equal(root.get("libro"), this.libro));
            }
            if (this.acto != null && this.acto.getId() != null) {
                predicates.add(this.builder.equal(root.get("acto"), this.acto));
            }
            if (this.inscripcion != null) {
                predicates.add(this.builder.equal(root.get("numInscripcion"), this.inscripcion.intValue()));
            }
            if (this.repertorio != null) {
                predicates.add(this.builder.equal(root.get("numRepertorio"), this.repertorio.intValue()));
            }
            if (this.folio != null && this.folio > 0) {
                predicates.add(this.builder.equal(root.get("folioInicio"), this.folio));
            }
            if (this.tomo != null && !this.tomo.isEmpty()) {
                predicates.add(this.builder.equal(root.get("numTomo"), this.tomo.trim()));
            }
            if (this.baseImponible != null) {
                predicates.add(this.builder.ge(root.get("baseImponible"), baseImponible));
            }
            if (filters.containsKey("baseImponible")) {
                predicates.add(this.builder.ge(root.get("baseImponible"), new BigDecimal(filters.get("baseImponible").toString())));
                addIgnoreFilters("baseImponible");
            }
            if (filters.containsKey("anioRep")) {
                predicates.add(this.builder.equal(this.builder.function("year", Integer.class, root.get("fechaRepertorio")),
                        Integer.valueOf(filters.get("anioRep").getFilterValue().toString())));
                addIgnoreFilters("anioRep");
            }
            if (filters.containsKey("anioIns")) {
                if (Utils.isNum(filters.get("anioIns").getFilterValue().toString())) {
                    predicates.add(this.builder.equal(this.builder.function("year", Integer.class, root.get("fechaInscripcion")),
                            Integer.valueOf(filters.get("anioIns").getFilterValue().toString())));
                    addIgnoreFilters("anioIns");
                }
            }
            if (cedRuc != null) {
                Join join1 = root.joinCollection("regMovimientoClienteCollection", JoinType.LEFT).join("enteInterv", JoinType.LEFT);
                Predicate cliente = this.builder.like(join1.get("cedRuc"), "%" + cedRuc.trim() + "%");

                Join join2 = root.joinCollection("regMovimientoRepresentanteCollection", JoinType.LEFT).join("enteInterv", JoinType.LEFT);
                Predicate representante = this.builder.like(join2.get("cedRuc"), "%" + cedRuc.trim() + "%");

                Join join3 = root.joinCollection("regMovimientoSociosCollection", JoinType.LEFT).join("enteInterv", JoinType.LEFT);
                Predicate socio = this.builder.like(join3.get("cedRuc"), "%" + cedRuc.trim() + "%");

                predicates.add(this.builder.or(cliente, representante, socio));
            }
            if (!filters.isEmpty()) {
                predicates.addAll(this.findPropertyFilter1(root, filters));
            }
            if (!getFilterss().isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, getFilterss()));
            }
            return predicates;
        } catch (NumberFormatException numberFormatException) {
            System.out.println(numberFormatException);
            return null;
        }
    }

}
