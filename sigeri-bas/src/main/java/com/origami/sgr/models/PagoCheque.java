/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.entities.RenEntidadBancaria;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Henry Pilco
 */
public class PagoCheque implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String numCheque;
    private String numCuenta;
    private Integer tipoPago;
    private Date fechaCheque;
    private RenEntidadBancaria banco;
    private BigDecimal valor= new BigDecimal("0.00");

    public PagoCheque() {
    }

    public String getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

    public RenEntidadBancaria getBanco() {
        return banco;
    }

    public void setBanco(RenEntidadBancaria banco) {
        this.banco = banco;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechaCheque() {
        return fechaCheque;
    }

    public void setFechaCheque(Date fechaCheque) {
        this.fechaCheque = fechaCheque;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagoCheque)) {
            return false;
        }
        PagoCheque other = (PagoCheque) object;
        if (
                (this.banco == null && other.banco != null) || (this.banco != null && !this.banco.equals(other.banco))
                || (this.numCheque == null && other.numCheque != null) || (this.numCheque != null && !this.numCheque.equals(other.numCheque))
                || (this.numCuenta == null && other.numCuenta != null) || (this.numCuenta != null && !this.numCuenta.equals(other.numCuenta))
                || (this.valor == null && other.valor != null) || (this.valor != null && !this.valor.equals(other.valor))
                ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.numCheque);
        hash = 59 * hash + Objects.hashCode(this.numCuenta);
        hash = 59 * hash + Objects.hashCode(this.banco);
        hash = 59 * hash + Objects.hashCode(this.valor);
        return hash;
    }
}
