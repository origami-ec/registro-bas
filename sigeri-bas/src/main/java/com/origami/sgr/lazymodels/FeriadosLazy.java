/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.Feriados;

/**
 *
 * @author Anyelo
 */
public class FeriadosLazy extends LazyModel<Feriados> {

    public FeriadosLazy() {
        super(Feriados.class, "fecha", "DESC");
    }

}
