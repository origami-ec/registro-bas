/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
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
@Table(name = "regp_tareas_dinardap_docs", schema = "flow")
public class RegpTareasDinardapDocs implements Serializable {
    private static final long serialVersionUID = 1L;
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "documento")
    private String documento;
    @JoinColumn(name = "tarea_dinardap", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegpTareasDinardap tareaDinardap;
    @Column(name = "doc")
    private Long doc;
    @Column(name = "usuario")
    private Long usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Size(max = 255)
    @Column(name = "content_type")
    private String contentType;
    

    public RegpTareasDinardapDocs() {
    }

    public RegpTareasDinardapDocs(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public RegpTareasDinardap getTareaDinardap() {
        return tareaDinardap;
    }

    public void setTareaDinardap(RegpTareasDinardap tareaDinardap) {
        this.tareaDinardap = tareaDinardap;
    }

    public Long getDoc() {
        return doc;
    }

    public void setDoc(Long doc) {
        this.doc = doc;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
        if (!(object instanceof RegpTareasDinardapDocs)) {
            return false;
        }
        RegpTareasDinardapDocs other = (RegpTareasDinardapDocs) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.rpp.entities.RegpTareasDinardapDocs[ id=" + id + " ]";
    }
      
}
