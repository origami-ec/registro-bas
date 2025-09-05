/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
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
@Table(name = "pub_solicitud_acto", schema = "flow")
public class PubSolicitudActo implements Serializable {

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

    public PubSolicitudActo() {
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

}
