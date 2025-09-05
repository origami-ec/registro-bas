/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCapital;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegMovimientoRepresentante;
import com.origami.sgr.entities.RegMovimientoSocios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anyelo
 */
public class ConsultaMovimientoModel implements Serializable{

    public static final Long serialVersionUID = 1L; 
    
    private RegMovimiento movimiento;
    private RegFicha ficha;
    private List<RegMovimiento> movimientos;
    private List<RegFicha> fichas;
    private List<RegMovimientoCliente> listMovCli,listMovCliSelect;
    private List<RegMovimientoFicha> listMovFic;
    private List<RegMovimientoReferencia> listMovRef;
    private List<RegMovimientoMarginacion> marginaciones;
    private List<RegMovimientoCapital> listMovCap = new ArrayList<>();
    private List<RegMovimientoRepresentante> listMovRep =  new ArrayList<>();
    private List<RegMovimientoSocios> listMovSoc =  new ArrayList<>();
    
    public ConsultaMovimientoModel() {
        this.movimiento = new RegMovimiento();
        this.ficha = new RegFicha();
        this.movimientos = new ArrayList<>();
        this.fichas = new ArrayList<>();
        this.listMovCli = new ArrayList<>();
        this.listMovCliSelect = new ArrayList<>();
        this.listMovFic = new ArrayList<>();
        this.listMovRef = new ArrayList<>();
        marginaciones = new ArrayList<>();
        this.listMovCap = new ArrayList<>();
        this.listMovRep = new ArrayList<>();
        this.listMovSoc = new ArrayList<>();

    }

    public ConsultaMovimientoModel(RegMovimiento movimiento, List<RegMovimientoCliente> listMovCli, 
            List<RegMovimientoFicha> listMovFic, List<RegMovimientoReferencia> listMovRef,
            List<RegMovimientoMarginacion> listMovMarg,List<RegMovimientoCapital> listMovCap,
            List<RegMovimientoRepresentante> listMovRep,List<RegMovimientoSocios> listMovSoc) {
        this.movimiento = movimiento;
        this.listMovCli = listMovCli;
        this.listMovFic = listMovFic;
        this.listMovRef = listMovRef;
        this.marginaciones = listMovMarg;
        this.listMovCap = listMovCap;
        this.listMovRep = listMovRep;
        this.listMovSoc = listMovSoc;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public List<RegMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public List<RegFicha> getFichas() {
        return fichas;
    }

    public void setFichas(List<RegFicha> fichas) {
        this.fichas = fichas;
    }

    public List<RegMovimientoCliente> getListMovCli() {
        return listMovCli;
    }

    public void setListMovCli(List<RegMovimientoCliente> listMovCli) {
        this.listMovCli = listMovCli;
    }

    public List<RegMovimientoCliente> getListMovCliSelect() {
        return listMovCliSelect;
    }

    public void setListMovCliSelect(List<RegMovimientoCliente> listMovCliSelect) {
        this.listMovCliSelect = listMovCliSelect;
    }
    
    public List<RegMovimientoFicha> getListMovFic() {
        return listMovFic;
    }

    public void setListMovFic(List<RegMovimientoFicha> listMovFic) {
        this.listMovFic = listMovFic;
    }

    public List<RegMovimientoReferencia> getListMovRef() {
        return listMovRef;
    }

    public void setListMovRef(List<RegMovimientoReferencia> listMovRef) {
        this.listMovRef = listMovRef;
    }

    public List<RegMovimientoMarginacion> getMarginaciones() {
        return marginaciones;
    }

    public void setMarginaciones(List<RegMovimientoMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }

    public List<RegMovimientoCapital> getListMovCap() {
        return listMovCap;
    }

    public void setListMovCap(List<RegMovimientoCapital> listMovCap) {
        this.listMovCap = listMovCap;
    }

    public List<RegMovimientoRepresentante> getListMovRep() {
        return listMovRep;
    }

    public void setListMovRep(List<RegMovimientoRepresentante> listMovRep) {
        this.listMovRep = listMovRep;
    }

    public List<RegMovimientoSocios> getListMovSoc() {
        return listMovSoc;
    }

    public void setListMovSoc(List<RegMovimientoSocios> listMovSoc) {
        this.listMovSoc = listMovSoc;
    }
    
    
}
