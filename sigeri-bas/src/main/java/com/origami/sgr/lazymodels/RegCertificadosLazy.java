/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegCertificado;
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
public class RegCertificadosLazy extends LazyModel<RegCertificado> {

    private Boolean prop = Boolean.FALSE;
    private String cedula;
    private String nombre;
    private Long numeroTramite;

    public RegCertificadosLazy() {
        super(RegCertificado.class, "id", "DESC");
    }

    public RegCertificadosLazy(Boolean propietarios, String cedula, String nombre) {
        super(RegCertificado.class, "id", "DESC");
        this.prop = propietarios;
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public RegCertificadosLazy(Long numeroTramite) {
        super(RegCertificado.class, "id", "DESC");
        this.numeroTramite = numeroTramite;
    }
/*
    @Override
    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        if (prop) {
            if (cedula != null) {
                //filters.put("propietario.cedRuc", cedula.trim());
            }
            if (nombre != null) {
                //filters.put("propietario.nombre", nombre.trim());
            }
        }
        if (numeroTramite != null) {
//            filters.put("numTramite", numeroTramite);
        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        addWhere(predicates, crit);

    }
*/
}
