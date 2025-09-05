/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.documental.entities.TbData;
import com.origami.documental.lazy.LazyModelDocs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import org.primefaces.model.FilterMeta;

/**
 *
 * @author eduar
 */
public class TbDataLazy extends LazyModelDocs<TbData> {

    public TbDataLazy() {
        super("doc_data_lata", TbData.class);
    }
    
    @Override
    public List<Predicate> criteriaFilterSetup1(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        List<Predicate> predicates = new ArrayList<>();
        try {
            //predicates.add(root.get("f02").isNotNull());
            //predicates.add(root.get("f11").isNotNull());
            if (this.filterss != null) {
                predicates.addAll(this.findPropertyFilter(root, this.filterss));
            }
            // LLAMAMOS EL ME  PARA REALIZAR LOS FILTER
            predicates.addAll(this.findPropertyFilter1(root, filters));
            return predicates;
        } catch (Exception e) {
            Logger.getLogger(LazyModelDocs.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
