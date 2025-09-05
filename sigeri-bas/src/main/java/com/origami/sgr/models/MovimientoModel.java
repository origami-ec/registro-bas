/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoReferencia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author origami
 */
public class MovimientoModel implements Serializable {

    public static final long serialVersionUID = 1L;

    private RegDomicilio codigoCan;
    private String numTomo;
    private RegActo acto;
    private RegEnteJudiciales enteJudicial;
    private Date fechaOto;
    private Date fechaInscripcion;
    private Integer folioInicio;
    private Integer folioFin;
    private Boolean ordJud;
    private String escritJuicProvResolucion;
    private String observacion;

    /*Listas nuevas que se van a agregar al movimiento editado*/
    private List<RegMovimientoCliente> movCliList = new ArrayList<>();

    /*Listas iniciales que se obtienen antes de editar el movimiento*/
    private List<RegMovimientoCliente> movCliListOld = new ArrayList<>();

    private List<RegMovimientoReferencia> movRefList = new ArrayList<>();
    private List<RegMovimientoReferencia> movRefListDel = new ArrayList<>();

    public MovimientoModel(RegMovimiento movimiento, List<RegMovimientoCliente> movCliList) {
        this.codigoCan = movimiento.getDomicilio();
        this.numTomo = movimiento.getNumTomo();
        this.acto = movimiento.getActo();
        this.enteJudicial = movimiento.getEnteJudicial();
        this.fechaOto = movimiento.getFechaOto();
        this.fechaInscripcion = movimiento.getFechaInscripcion();
        this.folioInicio = movimiento.getFolioInicio();
        this.folioFin = movimiento.getFolioFin();
        this.ordJud = movimiento.getOrdJud();
        this.escritJuicProvResolucion = movimiento.getEscritJuicProvResolucion();
        this.observacion = movimiento.getObservacion();

        if (movCliList != null && !movCliList.isEmpty()) {
            RegMovimientoCliente cliente;
            for (RegMovimientoCliente movCliList1 : movCliList) {
                cliente = new RegMovimientoCliente();
                cliente.setId(movCliList1.getId());
                cliente.setCedula(movCliList1.getCedula());
                cliente.setEstado(movCliList1.getEstado());
                cliente.setNombres(movCliList1.getNombres());
                cliente.setPapel(movCliList1.getPapel());
                cliente.setEnteInterv(movCliList1.getEnteInterv());
                this.movCliListOld.add(cliente);
            }
        }
    }

    public RegDomicilio getCodigoCan() {
        return codigoCan;
    }

    public void setCodigoCan(RegDomicilio codigoCan) {
        this.codigoCan = codigoCan;
    }

    public String getNumTomo() {
        return numTomo;
    }

    public void setNumTomo(String numTomo) {
        this.numTomo = numTomo;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public RegEnteJudiciales getEnteJudicial() {
        return enteJudicial;
    }

    public void setEnteJudicial(RegEnteJudiciales enteJudicial) {
        this.enteJudicial = enteJudicial;
    }

    public Date getFechaOto() {
        return fechaOto;
    }

    public void setFechaOto(Date fechaOto) {
        this.fechaOto = fechaOto;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getFolioInicio() {
        return folioInicio;
    }

    public void setFolioInicio(Integer folioInicio) {
        this.folioInicio = folioInicio;
    }

    public Integer getFolioFin() {
        return folioFin;
    }

    public void setFolioFin(Integer folioFin) {
        this.folioFin = folioFin;
    }

    public Boolean getOrdJud() {
        return ordJud;
    }

    public void setOrdJud(Boolean ordJud) {
        this.ordJud = ordJud;
    }

    public String getEscritJuicProvResolucion() {
        return escritJuicProvResolucion;
    }

    public void setEscritJuicProvResolucion(String escritJuicProvResolucion) {
        this.escritJuicProvResolucion = escritJuicProvResolucion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<RegMovimientoCliente> getMovCliListOld() {
        return movCliListOld;
    }

    public void setMovCliListOld(List<RegMovimientoCliente> movCliListOld) {
        this.movCliListOld = movCliListOld;
    }

    public List<RegMovimientoCliente> getMovCliList() {
        return movCliList;
    }

    public void setMovCliList(List<RegMovimientoCliente> movCliList) {
        this.movCliList = movCliList;
    }

    public List<RegMovimientoReferencia> getMovRefList() {
        return movRefList;
    }

    public void setMovRefList(List<RegMovimientoReferencia> movRefList) {
        this.movRefList = movRefList;
    }

    public List<RegMovimientoReferencia> getMovRefListDel() {
        return movRefListDel;
    }

    public void setMovRefListDel(List<RegMovimientoReferencia> movRefListDel) {
        this.movRefListDel = movRefListDel;
    }

}
