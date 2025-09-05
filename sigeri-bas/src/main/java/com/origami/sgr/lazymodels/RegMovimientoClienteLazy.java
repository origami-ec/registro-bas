/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegMovimientoCliente;
import java.util.ArrayList;
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
public class RegMovimientoClienteLazy extends LazyModel<RegMovimientoCliente> {

    protected Join mov;
    protected Join pro;
    protected Join acto;
    protected Join libro;
    protected String nombre;
    protected Date desde;
    protected Date hasta;
    protected String documento, contrato, contratante;
    protected Integer repertorio, inscripcion, anio, anioRepertorio, tipo;

    public RegMovimientoClienteLazy(String documento, String nombre, Date desde, Date hasta,
            String contrato, Integer repertorio, Integer anio, Integer inscripcion) {
        super(RegMovimientoCliente.class);
        //this.addSorted("movimiento.fechaRepertorio", "DESC");
        //this.addSorted("movimiento.numRepertorio", "DESC");
        this.documento = documento;
        this.nombre = nombre;
        this.desde = desde;
        this.hasta = hasta;
        this.contrato = contrato;
        this.repertorio = repertorio;
        this.anio = anio;
        this.inscripcion = inscripcion;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        // LLAMAMOS AL ME  QUE REALIZA LA BUSQUEDA DE CUALQUIER PROPIEDAD
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        mov = root.join("movimiento", JoinType.INNER);
        pro = root.join("enteInterv", JoinType.INNER);
        acto = mov.join("acto", JoinType.INNER);
        libro = mov.join("libro", JoinType.INNER);
        if (documento != null && documento.trim().length() > 0) {
            predicates.add(this.builder.like(builder.upper(pro.get("cedRuc")), documento.trim().toUpperCase() + "%"));
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            predicates.add(this.builder.like(builder.upper(pro.get("nombre")), nombre.trim().toUpperCase() + "%"));
        }
        if (anio != null) {
            predicates.add(this.builder.equal(this.builder.function("year", Integer.class, mov.get("fechaRepertorio")), anio));
            //predicates.add(this.builder.equal(this.builder.function("year", Integer.class, mov.get("fechaInscripcion")), anio));
        }
        if (anioRepertorio != null) {
            predicates.add(this.builder.equal(this.builder.function("year", Integer.class, mov.get("fechaRepertorio")), anioRepertorio));
        }
        if (desde != null && hasta != null) {
            //predicates.add(this.builder.between(mov.get("fechaInscripcion"), desde, hasta));
            predicates.add(this.builder.between(mov.get("fechaRepertorio"), desde, hasta));
        } else if (desde != null) {
            //predicates.add(this.builder.equal(mov.get("fechaInscripcion"), desde));
            predicates.add(this.builder.equal(mov.get("fechaRepertorio"), desde));
        } else if (hasta != null) {
            //predicates.add(this.builder.equal(mov.get("fechaInscripcion"), hasta));
            predicates.add(this.builder.equal(mov.get("fechaRepertorio"), hasta));
        }
        if (inscripcion != null) {
            predicates.add(this.builder.equal(mov.get("numInscripcion"), inscripcion));
        }
        if (repertorio != null) {
            predicates.add(this.builder.equal(mov.get("numRepertorio"), repertorio));
        }
        if (contrato != null && !contrato.isEmpty()) {
            predicates.add(this.builder.like(builder.upper(acto.get("nombre")), "%" + contrato.trim().toUpperCase() + "%"));
        }
        /*if (contratante != null && !contratante.isEmpty()) {
            filters.put("papel.papel", contratante.trim());
        }*/
        if (tipo != null && tipo > 0) {
            //filters.put("movimiento.libro.tipo", tipo);
            predicates.add(this.builder.equal(libro.get("tipo"), tipo));
        }
        if (!filters.isEmpty()) {
            predicates.addAll(this.findPropertyFilter1(root, filters));
        }
        if (!getFilterss().isEmpty()) {
            predicates.addAll(this.findPropertyFilter(root, getFilterss()));
        }
        addWhere(predicates, crit);
        //criteriaSorted(crit);
        return predicates;
    }

    public void criteriaSorted(CriteriaQuery crit) {
        crit.orderBy(builder.desc(mov.get("fechaRepertorio")),
                builder.desc(mov.get("numRepertorio")),
                builder.desc(mov.get("numInscripcion")));
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
