/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.OrdenFichas;

/**
 *
 * @author Origami
 */
public class OrdenFichasLazy extends LazyModel<OrdenFichas> {

    public OrdenFichasLazy() {
        super(OrdenFichas.class, "fechaOrden", "DESC");
    }

}
