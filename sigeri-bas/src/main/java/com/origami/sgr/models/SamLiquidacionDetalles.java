/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Anyelo
 */
public class SamLiquidacionDetalles implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger id;
    private BigInteger acto;
    private Integer numPredio;
    private String nomUrb;
    private String mzUrb;
    private String slUrb;
    private BigDecimal avaluo;
    private BigDecimal cuantia;
    private Integer cantidad;
    private BigDecimal valorUnitario;
    private BigDecimal descuento;
    private BigDecimal valorTotal;
    private String observacion;
    private BigInteger liquidacion;
    private BigInteger exoneracion;
    private Boolean aplicaDescuento;
    private Boolean reingreso;
    private Integer inscripcion;
    private Date fechaIngreso;
    private BigDecimal diferenciaPago;
    private BigDecimal descuentoPromedio;
    private Integer cantidadIntervinientes;
    private Integer anioUltimaTrasnferencia;
    private Integer anioAntecedenteSolicitado;
    private BigDecimal recargo;
    private BigDecimal base;
    private Date fechaPago;
    private Long numEmision;
    private String loginCobro;

    public SamLiquidacionDetalles() {

    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getActo() {
        return acto;
    }

    public void setActo(BigInteger acto) {
        this.acto = acto;
    }

    public Integer getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(Integer numPredio) {
        this.numPredio = numPredio;
    }

    public String getNomUrb() {
        return nomUrb;
    }

    public void setNomUrb(String nomUrb) {
        this.nomUrb = nomUrb;
    }

    public String getMzUrb() {
        return mzUrb;
    }

    public void setMzUrb(String mzUrb) {
        this.mzUrb = mzUrb;
    }

    public String getSlUrb() {
        return slUrb;
    }

    public void setSlUrb(String slUrb) {
        this.slUrb = slUrb;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigInteger getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(BigInteger liquidacion) {
        this.liquidacion = liquidacion;
    }

    public BigInteger getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(BigInteger exoneracion) {
        this.exoneracion = exoneracion;
    }

    public Boolean getAplicaDescuento() {
        return aplicaDescuento;
    }

    public void setAplicaDescuento(Boolean aplicaDescuento) {
        this.aplicaDescuento = aplicaDescuento;
    }

    public Boolean getReingreso() {
        return reingreso;
    }

    public void setReingreso(Boolean reingreso) {
        this.reingreso = reingreso;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getDiferenciaPago() {
        return diferenciaPago;
    }

    public void setDiferenciaPago(BigDecimal diferenciaPago) {
        this.diferenciaPago = diferenciaPago;
    }

    public BigDecimal getDescuentoPromedio() {
        return descuentoPromedio;
    }

    public void setDescuentoPromedio(BigDecimal descuentoPromedio) {
        this.descuentoPromedio = descuentoPromedio;
    }

    public Integer getCantidadIntervinientes() {
        return cantidadIntervinientes;
    }

    public void setCantidadIntervinientes(Integer cantidadIntervinientes) {
        this.cantidadIntervinientes = cantidadIntervinientes;
    }

    public Integer getAnioUltimaTrasnferencia() {
        return anioUltimaTrasnferencia;
    }

    public void setAnioUltimaTrasnferencia(Integer anioUltimaTrasnferencia) {
        this.anioUltimaTrasnferencia = anioUltimaTrasnferencia;
    }

    public Integer getAnioAntecedenteSolicitado() {
        return anioAntecedenteSolicitado;
    }

    public void setAnioAntecedenteSolicitado(Integer anioAntecedenteSolicitado) {
        this.anioAntecedenteSolicitado = anioAntecedenteSolicitado;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Long getNumEmision() {
        return numEmision;
    }

    public void setNumEmision(Long numEmision) {
        this.numEmision = numEmision;
    }

    public String getLoginCobro() {
        return loginCobro;
    }

    public void setLoginCobro(String loginCobro) {
        this.loginCobro = loginCobro;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SamLiquidacionDetalles{id=").append(id);
//        sb.append(", acto=").append(acto);
//        sb.append(", numPredio=").append(numPredio);
//        sb.append(", nomUrb=").append(nomUrb);
//        sb.append(", mzUrb=").append(mzUrb);
//        sb.append(", slUrb=").append(slUrb);
//        sb.append(", avaluo=").append(avaluo);
//        sb.append(", cuantia=").append(cuantia);
//        sb.append(", cantidad=").append(cantidad);
//        sb.append(", valorUnitario=").append(valorUnitario);
//        sb.append(", descuento=").append(descuento);
//        sb.append(", valorTotal=").append(valorTotal);
//        sb.append(", observacion=").append(observacion);
//        sb.append(", liquidacion=").append(liquidacion);
//        sb.append(", exoneracion=").append(exoneracion);
//        sb.append(", aplicaDescuento=").append(aplicaDescuento);
//        sb.append(", reingreso=").append(reingreso);
//        sb.append(", inscripcion=").append(inscripcion);
//        sb.append(", fechaIngreso=").append(fechaIngreso);
//        sb.append(", diferenciaPago=").append(diferenciaPago);
//        sb.append(", descuentoPromedio=").append(descuentoPromedio);
//        sb.append(", cantidadIntervinientes=").append(cantidadIntervinientes);
//        sb.append(", anioUltimaTrasnferencia=").append(anioUltimaTrasnferencia);
//        sb.append(", anioAntecedenteSolicitado=").append(anioAntecedenteSolicitado);
//        sb.append(", recargo=").append(recargo);
//        sb.append(", base=").append(base);
        sb.append(", fechaPago=").append(fechaPago);
        sb.append(", numEmision=").append(numEmision);
        sb.append(", loginCobro=").append(loginCobro);
        sb.append('}');
        return sb.toString();
    }
    
}
