package com.origami.documental.models;

import java.io.Serializable;

public class UsuarioDocs implements Serializable {

    private Long referenceId;
    private String nombres;
    private String usuario;

    public UsuarioDocs() {
    }

    public UsuarioDocs(Long referenceId, String nombres, String usuario) {
        this.referenceId = referenceId;
        this.nombres = nombres;
        this.usuario = usuario;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
