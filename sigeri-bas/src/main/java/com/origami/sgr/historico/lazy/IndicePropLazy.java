/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.lazy;

import com.origami.sgr.historico.entities.IndiceProp;
import com.origami.sgr.lazymodels.LazyModel;
import java.util.ArrayList;
import java.util.Date;
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
public class IndicePropLazy extends LazyModel<IndiceProp> {

    protected List<IndiceProp> list = new ArrayList<>();
    protected String cedula, nombre, nombre2, contrato, contratante;
    protected Integer mi, inscripcion, repertorio, desde, hasta, certificado;
    protected Boolean solvencia;
    protected Date fecha, fmi;

    public IndicePropLazy() {
        super(IndiceProp.class);
        this.addSorted("indFecha", "ASC");
        this.addSorted("indRepertorio", "ASC");
    }

    public IndicePropLazy(String cedula, String nombre, String nombre2, String contrato, String contratante, Integer mi,
            Integer inscripcion, Integer repertorio, Integer desde, Integer hasta, Integer certificado, Boolean solvencia,
            Date fecha, Date fmi) {
        super(IndiceProp.class);
        this.addSorted("indFecha", "ASC");
        this.addSorted("indRepertorio", "ASC");
        this.cedula = cedula;
        this.nombre = nombre;
        this.nombre2 = nombre2;
        this.contrato = contrato;
        this.contratante = contratante;
        this.mi = mi;
        this.inscripcion = inscripcion;
        this.repertorio = repertorio;
        this.desde = desde;
        this.hasta = hasta;
        this.certificado = certificado;
        this.solvencia = solvencia;
        this.fecha = fecha;
        this.fmi = fmi;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        try {
            if (filters == null) {
                filters = new HashMap<>();
            }
            List<Predicate> predicates = new ArrayList<>();
            
            if (!cedula.isEmpty()) {
                predicates.add(this.builder.like(root.get("indCedula"), "%" + cedula.trim() + "%"));
            }
            /*if (!nombre.isEmpty()) {
                predicates.add(this.builder.like(root.get("indNombre"), "%" + nombre.trim().toUpperCase() + "%"));
            }*/
            if (!nombre.isEmpty() && nombre2.isEmpty()) {
                predicates.add(this.builder.like(root.get("indNombre"), "%" + nombre.trim().toUpperCase() + "%"));
            } else if (!nombre2.isEmpty() && nombre.isEmpty()) {
                predicates.add(this.builder.like(root.get("indNombre"), "%" + nombre2.trim().toUpperCase() + "%"));
            } else if (!nombre.isEmpty() && !nombre2.isEmpty()) {
                predicates.add(this.builder.or(this.builder.like(root.get("indNombre"), "%" + nombre.trim() + "%"), 
                        this.builder.like(root.get("indNombre"), "%" + nombre2.trim() + "%")));
            }
            /*if (!solvencia) {
                Criterion c1 = Restrictions.not(Restrictions.in("indLibro", new Object[]{50, 52, 53, 54}));
                Criterion c2 = Restrictions.isNull("indLibro");
                crit.add(Restrictions.or(c1, c2));
            }*/
            if (!contrato.isEmpty()) {
                predicates.add(this.builder.like(root.get("indContrato"), "%" + contrato.trim() + "%"));
            }
            if (desde != null && hasta != null) {
                predicates.add(this.builder.between(root.get("indAnio"), desde, hasta));
            }
            if (repertorio != null) {
                predicates.add(this.builder.equal(root.get("indRepertorio"), repertorio));
            }
            if (inscripcion != null) {
                predicates.add(this.builder.equal(root.get("indInscripcion"), inscripcion));
            }
            if (mi != null) {
                predicates.add(this.builder.equal(root.get("indMi"), mi));
            }
            if (certificado != null) {
                predicates.add(this.builder.equal(root.get("indNlinea"), certificado));
            }
            if (!contratante.isEmpty()) {
                predicates.add(this.builder.like(root.get("indContratante"), "%" + contratante.trim() + "%"));
            }
            if (fecha != null) {
                predicates.add(this.builder.equal(root.get("indFecha"), fecha));
            }
            /*if (fmi != null) {
                crit.add(Restrictions.eq("indFmi", fmi));
            }*/
            if (!filters.isEmpty()) {
                predicates.addAll(this.findPropertyFilter1(root, filters));
            }
            if (!getFilterss().isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, getFilterss()));
            }
            return predicates;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
