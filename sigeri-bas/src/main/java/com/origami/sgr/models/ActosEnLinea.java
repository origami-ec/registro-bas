/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.math.BigInteger;
import java.util.Objects;

/**
 *
 * @author EDWIN
 */
public class ActosEnLinea {

    private BigInteger id;
    private String acto;
    private String descripcionBreve;
    private String descripcion;
    private String urlImage;
    private Integer orden;
    private Double valor;
    private String tiempoEntrega;
    private String requisito;
    private String urlRp;
    private Boolean certificado;
    private Boolean tramiteBanca;

    public ActosEnLinea() {
    }

    public ActosEnLinea(BigInteger id, String acto, Boolean certificado, Boolean tramiteBanca) {
        this.id = id;
        this.acto = acto;
        this.certificado = certificado;
        this.tramiteBanca = tramiteBanca;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTiempoEntrega() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(String tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }

    public String getRequisito() {
        return requisito;
    }

    public void setRequisito(String requisito) {
        this.requisito = requisito;
    }

    public String getUrlRp() {
        return urlRp;
    }

    public void setUrlRp(String urlRp) {
        this.urlRp = urlRp;
    }

    public Boolean getCertificado() {
        return certificado;
    }

    public void setCertificado(Boolean certificado) {
        this.certificado = certificado;
    }

    public Boolean getTramiteBanca() {
        return tramiteBanca;
    }

    public void setTramiteBanca(Boolean tramiteBanca) {
        this.tramiteBanca = tramiteBanca;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActosEnLinea other = (ActosEnLinea) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
