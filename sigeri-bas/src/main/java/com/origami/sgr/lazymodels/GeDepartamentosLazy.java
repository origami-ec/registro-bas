/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.GeDepartamento;

/**
 *
 * @author Anyelo
 */
public class GeDepartamentosLazy extends LazyModel<GeDepartamento> {

    public GeDepartamentosLazy() {
        super(GeDepartamento.class, "nombre", "ASC");
    }

}
