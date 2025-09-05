/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenPagoDetalle;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.EjbsCaller;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Henry Pilco
 */
public class PagoTransferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Entitymanager em;

    private String numTransferencia;
    private Date fecha;
    private Integer tipoPago;
    private RenEntidadBancaria banco;
    private BigDecimal valor = new BigDecimal("0.00");

    public PagoTransferencia() {
        em = EjbsCaller.getTransactionManager();
    }

    public Boolean noFindCoincidence(RenEntidadBancaria banco, String transferencia) {
        try {
            HashMap map = new HashMap();
            map.put("trBanco", banco);
            map.put("trNumTransferencia", transferencia);
            RenPagoDetalle pago = (RenPagoDetalle) em.findObjectByParameter(RenPagoDetalle.class, map);
            //System.out.println("// pago: " + pago);
            if (pago == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNumTransferencia() {
        return numTransferencia;
    }

    public void setNumTransferencia(String numTransferencia) {
        this.numTransferencia = numTransferencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagoTransferencia)) {
            return false;
        }
        PagoTransferencia other = (PagoTransferencia) object;
        if ((this.banco == null && other.banco != null) || (this.banco != null && !this.banco.equals(other.banco))
                || (this.numTransferencia == null && other.numTransferencia != null) || (this.numTransferencia != null && !this.numTransferencia.equals(other.numTransferencia))
                || (this.valor == null && other.valor != null) || (this.valor != null && !this.valor.equals(other.valor))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.numTransferencia);
        hash = 73 * hash + Objects.hashCode(this.banco);
        hash = 73 * hash + Objects.hashCode(this.valor);
        return hash;
    }
}
