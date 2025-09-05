/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author EDWIN
 */
@Entity
@Table(name = "pub_solicitud_requisito", schema = "flow")
public class PubSolicitudRequisito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud")
    private PubSolicitud solicitud;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acto")
    private RegActo acto;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisito")
    private RegRequisitos requisito;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisito_acto")
    private RegRequisitosActos requisitosActos;
    @Column(name = "fecha")
    private Date fecha;
    @Column(name = "documento")
    private Long documento;
    @Column(name = "tipo")
    private String tipo;

    public PubSolicitudRequisito() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PubSolicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(PubSolicitud solicitud) {
        this.solicitud = solicitud;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public RegRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(RegRequisitos requisito) {
        this.requisito = requisito;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public RegRequisitosActos getRequisitosActos() {
        return requisitosActos;
    }

    public void setRequisitosActos(RegRequisitosActos requisitosActos) {
        this.requisitosActos = requisitosActos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final PubSolicitudRequisito other = (PubSolicitudRequisito) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
