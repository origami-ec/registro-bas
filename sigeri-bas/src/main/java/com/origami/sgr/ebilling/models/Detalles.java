/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

import java.util.List;

/**
 *
 * @author andysanchez
 */
public class Detalles {

    private List<Detalle> detalle;

    public Detalles() {
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Detalles{" + "detalle=" + detalle.toString() + '}';
    }
    
    

}
