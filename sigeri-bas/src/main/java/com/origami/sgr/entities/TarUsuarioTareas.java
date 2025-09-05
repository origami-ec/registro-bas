/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Where;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "tar_usuario_tareas", schema = "conf", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario", "rol"})})
@XmlRootElement
public class TarUsuarioTareas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser usuario;
    @JoinColumn(name = "rol", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclRol rol;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "peso")
    private BigInteger peso = BigInteger.ZERO;
    @Column(name = "cantidad")
    private BigInteger cantidad = BigInteger.ZERO;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Where(clause = "CAST(fecha AS DATE) = CAST(now() AS DATE)")
    @OneToMany(mappedBy = "usuarioTareas", fetch = FetchType.LAZY)
    private Collection<TarTareasAsignadas> tarTareasAsignadasCollection;
    @Column(name = "estado_ficha")
    private Boolean estadoFicha;
    @Column(name = "fecha_ficha")
    @Temporal(TemporalType.DATE)
    private Date fechaFicha;
    @Column(name = "dias")
    private Integer dias = 0;
    @Column(name = "cantidad_aux")
    private Integer cantidadAux = 1;

    public TarUsuarioTareas() {
    }

    public TarUsuarioTareas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public AclRol getRol() {
        return rol;
    }

    public void setRol(AclRol rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getDias() {
        if (dias == null) {
            dias = 0;
        }
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TarTareasAsignadas> getTarTareasAsignadasCollection() {
        return tarTareasAsignadasCollection;
    }

    public void setTarTareasAsignadasCollection(Collection<TarTareasAsignadas> tarTareasAsignadasCollection) {
        this.tarTareasAsignadasCollection = tarTareasAsignadasCollection;
    }

    public Boolean getEstadoFicha() {
        return estadoFicha;
    }

    public void setEstadoFicha(Boolean estadoFicha) {
        this.estadoFicha = estadoFicha;
    }

    public Date getFechaFicha() {
        return fechaFicha;
    }

    public void setFechaFicha(Date fechaFicha) {
        this.fechaFicha = fechaFicha;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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
        if (!(object instanceof TarUsuarioTareas)) {
            return false;
        }
        TarUsuarioTareas other = (TarUsuarioTareas) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "TarUsuarioTareas{" + "id=" + id + ", usuario=" + usuario + ", peso=" + peso + ", cantidad=" + cantidad + ", fecha=" + fecha + '}';
    }

    public Integer getCantidadAux() {
        if (cantidadAux == null) {
            cantidadAux = 0;
        }
        return cantidadAux;
    }

    public void setCantidadAux(Integer cantidadAux) {
        this.cantidadAux = cantidadAux;
    }

}
