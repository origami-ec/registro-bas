/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

/**
 *
 * @author gutya
 */

import java.math.BigDecimal;

public class MotivoNotaDebito {

    protected String razon;
    protected BigDecimal valor;

    public String getRazon() {
        return this.razon;
    }

    public void setRazon(String value) {
        this.razon = value;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public void setValor(BigDecimal value) {
        this.valor = value;
    }

}
