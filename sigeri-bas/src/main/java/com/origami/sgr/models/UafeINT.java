/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;

/**
 *
 * @author Origami
 */
public class UafeINT implements Serializable {
    
    public static final Long serialVersionUID = 1L;
    
    protected String nit;       //NUMERO INGRESO TRAMITE
    //TIPO DE DOCUMENTO
    protected String idi;       //IDENTIFICACION INTERVINIENTE
    protected String nri;       //NOMBRES COMPLETOS
    //NACIONALIDAD INTERVINIENTE
    protected String rdi;       //ROL INTERVINIENTE
    protected String pdi;       //PAPEL INTERVINIENTE
    protected String cdr;       //CODIGO REGISTRO

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getIdi() {
        return idi;
    }

    public void setIdi(String idi) {
        this.idi = idi;
    }

    public String getNri() {
        return nri;
    }

    public void setNri(String nri) {
        this.nri = nri;
    }

    public String getRdi() {
        return rdi;
    }

    public void setRdi(String rdi) {
        this.rdi = rdi;
    }

    public String getPdi() {
        return pdi;
    }

    public void setPdi(String pdi) {
        this.pdi = pdi;
    }

    public String getCdr() {
        return cdr;
    }

    public void setCdr(String cdr) {
        this.cdr = cdr;
    }

}
