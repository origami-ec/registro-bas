/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RenNotaCredito;

/**
 *
 * @author Anyelo
 */
public class RenNotaCreditoLazy extends LazyModel<RenNotaCredito> {

    public RenNotaCreditoLazy() {
        super(RenNotaCredito.class, "id", "DESC");
    }

}
