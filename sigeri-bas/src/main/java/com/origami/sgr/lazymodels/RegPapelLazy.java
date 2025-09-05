/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegPapel;

/**
 *
 * @author Anyelo
 */
public class RegPapelLazy extends LazyModel<RegPapel> {

    public RegPapelLazy() {
        super(RegPapel.class, "papel", "ASC");
    }
}
