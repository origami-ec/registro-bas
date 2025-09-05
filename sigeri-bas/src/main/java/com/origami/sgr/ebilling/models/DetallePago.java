/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

/**
 *
 * @author andysanchez
 */
public class DetallePago {

    private String formaPago;
    private Double total;

    public DetallePago() {
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DetallePago{" + "formaPago=" + formaPago + ", total=" + total + '}';
    }

    
    
}
