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
public class EvaluacionFuncionarios implements Serializable {

    public static final Long serialVersionUID = 1L;
    
    protected String funcionario;
    protected Date fecha;

    protected Long tramites;
    protected Integer tramitesReasignadoPos;
    protected Integer tramitesReasignadoNeg;
    protected Integer totalTramites;
    
    protected Long actosContratos;
    protected Integer actosContratosPos;
    protected Integer actosContratosNeg;
    protected Integer totalActos;
    
    protected Integer elaboradosI;
    protected Integer elaboradosC;
    protected Integer vencidosElabI;
    protected Integer vencidosElabC;
    protected Integer pendienteI;
    protected Integer pendienteC;
    protected Integer ejecucionI;
    protected Integer ejecucionC;
    protected Integer interrumpidosI;
    protected Integer interrumpidosC;

    protected String observacion = "";

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getTramites() {
        return tramites;
    }

    public void setTramites(Long tramites) {
        this.tramites = tramites;
    }

    public Integer getTramitesReasignadoPos() {
        return tramitesReasignadoPos;
    }

    public void setTramitesReasignadoPos(Integer tramitesReasignadoPos) {
        this.tramitesReasignadoPos = tramitesReasignadoPos;
    }

    public Integer getTramitesReasignadoNeg() {
        return tramitesReasignadoNeg;
    }

    public void setTramitesReasignadoNeg(Integer tramitesReasignadoNeg) {
        this.tramitesReasignadoNeg = tramitesReasignadoNeg;
    }

    public Integer getTotalTramites() {
        return totalTramites;
    }

    public void setTotalTramites(Integer totalTramites) {
        this.totalTramites = totalTramites;
    }

    public Long getActosContratos() {
        return actosContratos;
    }

    public void setActosContratos(Long actosContratos) {
        this.actosContratos = actosContratos;
    }

    public Integer getActosContratosPos() {
        return actosContratosPos;
    }

    public void setActosContratosPos(Integer actosContratosPos) {
        this.actosContratosPos = actosContratosPos;
    }

    public Integer getActosContratosNeg() {
        return actosContratosNeg;
    }

    public void setActosContratosNeg(Integer actosContratosNeg) {
        this.actosContratosNeg = actosContratosNeg;
    }

    public Integer getTotalActos() {
        return totalActos;
    }

    public void setTotalActos(Integer totalActos) {
        this.totalActos = totalActos;
    }

    public Integer getElaboradosI() {
        return elaboradosI;
    }

    public void setElaboradosI(Integer elaboradosI) {
        this.elaboradosI = elaboradosI;
    }

    public Integer getElaboradosC() {
        return elaboradosC;
    }

    public void setElaboradosC(Integer elaboradosC) {
        this.elaboradosC = elaboradosC;
    }

    public Integer getVencidosElabI() {
        return vencidosElabI;
    }

    public void setVencidosElabI(Integer vencidosElabI) {
        this.vencidosElabI = vencidosElabI;
    }

    public Integer getVencidosElabC() {
        return vencidosElabC;
    }

    public void setVencidosElabC(Integer vencidosElabC) {
        this.vencidosElabC = vencidosElabC;
    }

    public Integer getPendienteI() {
        return pendienteI;
    }

    public void setPendienteI(Integer pendienteI) {
        this.pendienteI = pendienteI;
    }

    public Integer getPendienteC() {
        return pendienteC;
    }

    public void setPendienteC(Integer pendienteC) {
        this.pendienteC = pendienteC;
    }

    public Integer getEjecucionI() {
        return ejecucionI;
    }

    public void setEjecucionI(Integer ejecucionI) {
        this.ejecucionI = ejecucionI;
    }

    public Integer getEjecucionC() {
        return ejecucionC;
    }

    public void setEjecucionC(Integer ejecucionC) {
        this.ejecucionC = ejecucionC;
    }

    public Integer getInterrumpidosI() {
        return interrumpidosI;
    }

    public void setInterrumpidosI(Integer interrumpidosI) {
        this.interrumpidosI = interrumpidosI;
    }

    public Integer getInterrumpidosC() {
        return interrumpidosC;
    }

    public void setInterrumpidosC(Integer interrumpidosC) {
        this.interrumpidosC = interrumpidosC;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


}
