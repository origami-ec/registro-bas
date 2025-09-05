/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author andysanchez
 */
@Entity
@Table(name = "pub_solicitud_juridico", schema = "flow")
public class PubSolicitudJuridico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte solicitante;
    @JoinColumn(name = "ente_judicial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteJudiciales enteJudicial;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "usuario")
    private Long usuario;
    @Transient
    private HistoricoTramites historicoTramites;

    public PubSolicitudJuridico() {
    }

    public PubSolicitudJuridico(CatEnte solicitante, RegEnteJudiciales enteJudicial, Date fechaIngreso, Long usuario, HistoricoTramites historicoTramites) {
        this.solicitante = solicitante;
        this.enteJudicial = enteJudicial;
        this.fechaIngreso = fechaIngreso;
        this.usuario = usuario;
        this.historicoTramites = historicoTramites;
    }
    
  
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public RegEnteJudiciales getEnteJudicial() {
        return enteJudicial;
    }

    public void setEnteJudicial(RegEnteJudiciales enteJudicial) {
        this.enteJudicial = enteJudicial;
    }

    public HistoricoTramites getHistoricoTramites() {
        return historicoTramites;
    }

    public void setHistoricoTramites(HistoricoTramites historicoTramites) {
        this.historicoTramites = historicoTramites;
    }

        public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
    
}
