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
public class ActosRequisito {

    private BigInteger requisitoActo;
    private BigInteger idActo;
    private String acto;
    private BigInteger idRequisito;
    private String requisito;
    private Boolean requerido;
    private Long documento;

    public ActosRequisito() {
    }

    public BigInteger getRequisitoActo() {
        return requisitoActo;
    }

    public void setRequisitoActo(BigInteger requisitoActo) {
        this.requisitoActo = requisitoActo;
    }

    public BigInteger getIdActo() {
        return idActo;
    }

    public void setIdActo(BigInteger idActo) {
        this.idActo = idActo;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public BigInteger getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(BigInteger idRequisito) {
        this.idRequisito = idRequisito;
    }

    public String getRequisito() {
        return requisito;
    }

    public void setRequisito(String requisito) {
        this.requisito = requisito;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.requisitoActo);
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
        final ActosRequisito other = (ActosRequisito) obj;
        if (!Objects.equals(this.requisitoActo, other.requisitoActo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return requisito;
    }

}
