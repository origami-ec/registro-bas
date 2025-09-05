/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Arturo
 */
public class Remanentes implements Serializable{
    public String tipo;
    public BigInteger num_tramite;
    public String num_repertorio;
    public BigInteger num_comprobante;
    public String fecha_ingreso;
    public BigDecimal valor_total;
    public String acto;
    public Integer cantidad;

    public Remanentes() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigInteger getNum_tramite() {
        return num_tramite;
    }

    public void setNum_tramite(BigInteger num_tramite) {
        this.num_tramite = num_tramite;
    }

    public String getNum_repertorio() {
        return num_repertorio;
    }

    public void setNum_repertorio(String num_repertorio) {
        this.num_repertorio = num_repertorio;
    }

    public BigInteger getNum_comprobante() {
        return num_comprobante;
    }

    public void setNum_comprobante(BigInteger num_comprobante) {
        this.num_comprobante = num_comprobante;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public BigDecimal getValor_total() {
        return valor_total;
    }

    public void setValor_total(BigDecimal valor_total) {
        this.valor_total = valor_total;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Remanentes{" + "tipo=" + tipo + ", num_tramite=" + num_tramite + ", num_repertorio=" + num_repertorio + ", num_comprobante=" + num_comprobante + ", fecha_ingreso=" + fecha_ingreso + ", valor_total=" + valor_total + ", acto=" + acto + ", cantidad=" + cantidad + '}';
    }
}
