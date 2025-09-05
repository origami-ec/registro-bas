/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author XndySxnchez
 */
@Entity
@Table(name = "nprm_sri", schema = "ctlg")
public class NprmSri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "tipo_anexo")
    private Integer tipoAnexo = 1;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "feccha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecchaIngreso;
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "nprmSri", fetch = FetchType.LAZY)
    private List<NprmSriDetalle> nprmSriDetalles;

    public NprmSri() {
    }

    public NprmSri(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoAnexo() {
        return tipoAnexo;
    }

    public void setTipoAnexo(Integer tipoAnexo) {
        this.tipoAnexo = tipoAnexo;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Date getFecchaIngreso() {
        return fecchaIngreso;
    }

    public void setFecchaIngreso(Date fecchaIngreso) {
        this.fecchaIngreso = fecchaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public List<NprmSriDetalle> getNprmSriDetalles() {
        return nprmSriDetalles;
    }

    public void setNprmSriDetalles(List<NprmSriDetalle> nprmSriDetalles) {
        this.nprmSriDetalles = nprmSriDetalles;
    }

    public Boolean getAnexoUnoRegPropiedad() {
        return this.tipoAnexo == 1;
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
        if (!(object instanceof NprmSri)) {
            return false;
        }
        NprmSri other = (NprmSri) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NprmSri{" + "id=" + id + ", tipo=" + tipo + ", tipoAnexo=" + tipoAnexo + ", mes=" + mes + ", anio=" + anio + ", fecchaIngreso=" + fecchaIngreso + ", usuarioIngreso=" + usuarioIngreso + '}';
    }

    public boolean existeMovimiento(RegMovimiento movimiento) {
        if (this.nprmSriDetalles != null) {
            if (!this.nprmSriDetalles.isEmpty()) {
                for (NprmSriDetalle det : this.nprmSriDetalles) {
                    if (det.getMovimiento() != null && Long.valueOf(det.getMovimiento().toString()).equals(movimiento.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public NprmSriDetalle getDetalleMovimiento(RegMovimiento movimiento) {
        if (this.nprmSriDetalles != null) {
            for (NprmSriDetalle det : this.nprmSriDetalles) {
                if (det.getMovimiento() != null && Long.valueOf(det.getMovimiento().toString()).equals(movimiento.getId())) {
                    return det;
                }
            }
        }
        return null;
    }

    public List<NprmSriDetalle> getDetallesMovimiento(RegMovimiento movimiento) {
        List<NprmSriDetalle> detalles = null;
        if (this.nprmSriDetalles != null) {
            for (NprmSriDetalle det : this.nprmSriDetalles) {
                if (det.getMovimiento() != null && Long.valueOf(det.getMovimiento().toString()).equals(movimiento.getId())) {
                    if (detalles == null) {
                        detalles = new ArrayList<>();
                    }
                    detalles.add(det);
                }
            }
        }
        return detalles;
    }

    public boolean removeDetalle(RegMovimiento movimiento) {
        NprmSriDetalle detalleMovimiento = getDetalleMovimiento(movimiento);
        if (detalleMovimiento != null) {
            return this.nprmSriDetalles.remove(detalleMovimiento);
        }
        return false;
    }

}
