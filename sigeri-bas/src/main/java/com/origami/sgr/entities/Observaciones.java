/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "observaciones", schema = "flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observaciones.findAll", query = "SELECT o FROM Observaciones o"),
    @NamedQuery(name = "Observaciones.findById", query = "SELECT o FROM Observaciones o WHERE o.id = :id"),
    @NamedQuery(name = "Observaciones.findByIdProceso", query = "SELECT o FROM Observaciones o WHERE o.idProceso = :idProceso"),
    @NamedQuery(name = "Observaciones.findByObservacion", query = "SELECT o FROM Observaciones o WHERE o.observacion = :observacion"),
    @NamedQuery(name = "Observaciones.findByFecCre", query = "SELECT o FROM Observaciones o WHERE o.fecCre = :fecCre"),
    @NamedQuery(name = "Observaciones.findByUserCre", query = "SELECT o FROM Observaciones o WHERE o.userCre = :userCre"),
    @NamedQuery(name = "Observaciones.findByEstado", query = "SELECT o FROM Observaciones o WHERE o.estado = :estado"),
    @NamedQuery(name = "Observaciones.findByTarea", query = "SELECT o FROM Observaciones o WHERE o.tarea = :tarea")})
public class Observaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_proceso")
    private BigInteger idProceso;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "fec_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;
    @Column(name = "user_cre")
    private String userCre;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 100)
    @Column(name = "tarea")
    private String tarea;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HistoricoTramites idTramite;
    @Column(name = "acl_login")
    private Long aclLogin;

    public Observaciones() {
    }

    public Observaciones(Long id) {
        this.id = id;
    }

    public Observaciones(Long id, String observacion, String userCre) {
        this.id = id;
        this.observacion = observacion;
        this.userCre = userCre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(BigInteger idProceso) {
        this.idProceso = idProceso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public HistoricoTramites getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
    }

    public Long getAclLogin() {
        return aclLogin;
    }

    public void setAclLogin(Long aclLogin) {
        this.aclLogin = aclLogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observaciones)) {
            return false;
        }
        Observaciones other = (Observaciones) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.Observaciones[ id=" + id + " ]";
    }
    
}
