/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author asilva
 */
public class TareasPorFechaIngresoEntrega implements Serializable {

    public static final Long serialVersionUID = 1L;
    
    protected String groupfechaentrega = "";
    protected String factura;
    protected Long   tramite;

    protected String recaudador = "";
    protected String ced_fact   = "";
    protected String nomb_fact  = "";
    protected String contacto   = "";
    protected String tarea_act  = "";
            
    protected String funcionario = "";
    protected String observacion = "";
    
    protected String entregado   = "";
    protected String retirado    = "";
    protected String indicador   = "";
    
    protected Date fechaingreso;
    protected Date fechainicio;
    protected Date fechafin;
    protected Date fechaentrega;
        
    protected Integer indicadorled;
    
    protected Long diaspendiente;

    public String getGroupfechaentrega() {
        return groupfechaentrega;
    }

    public void setGroupfechaentrega(String groupfechaentrega) {
        this.groupfechaentrega = groupfechaentrega;
    }
    
    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getRecaudador() {
        return recaudador;
    }

    public void setRecaudador(String recaudador) {
        this.recaudador = recaudador;
    }

    public String getCed_fact() {
        return ced_fact;
    }

    public void setCed_fact(String ced_fact) {
        this.ced_fact = ced_fact;
    }

    public String getNomb_fact() {
        return nomb_fact;
    }

    public void setNomb_fact(String nomb_fact) {
        this.nomb_fact = nomb_fact;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTarea_act() {
        return tarea_act;
    }

    public void setTarea_act(String tarea_act) {
        this.tarea_act = tarea_act;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEntregado() {
        return entregado;
    }

    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }

    public String getRetirado() {
        return retirado;
    }

    public void setRetirado(String retirado) {
        this.retirado = retirado;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Date getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public Long getDiaspendiente() {
        return diaspendiente;
    }

    public void setDiaspendiente(Long diaspendiente) {
        this.diaspendiente = diaspendiente;
    }

    public Integer getIndicadorled() {
        return indicadorled;
    }

    public void setIndicadorled(Integer indicadorled) {
        this.indicadorled = indicadorled;
    }
    
}
