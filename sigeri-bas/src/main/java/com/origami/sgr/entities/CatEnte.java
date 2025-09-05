/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "cat_ente", schema = "app")
@XmlRootElement
public class CatEnte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "ci_ruc")
    private String ciRuc;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "es_persona")
    private Boolean esPersona = true;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "representante_legal")
    private BigInteger representanteLegal;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Column(name = "estado")
    private String estado = "A";
    @Column(name = "user_cre")
    private String userCre;
    @Column(name = "fecha_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCre;
    @Column(name = "user_mod")
    private String userMod;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Column(name = "nombre_comercial")
    private String nombreComercial;
    @Column(name = "razon_social")
    private String razonSocial;
    @Column(name = "titulo_prof")
    private String tituloProf;
    @OneToMany(mappedBy = "ente", fetch = FetchType.LAZY)
    private Collection<AclUser> aclUserCollection;

    @Column(name = "telefono1")
    private String telefono1;

    @Column(name = "telefono2")
    private String telefono2;

    @Column(name = "correo1")
    private String correo1;

    @Column(name = "correo2")
    private String correo2;

    @Column(name = "excepcional")
    private Boolean excepcional = false;

    @OneToMany(mappedBy = "ente", fetch = FetchType.LAZY)
    private Collection<RegMovimientoCliente> regMovimientoClienteCollection;

    @Column(name = "tipo_identificacion")
    private String tipoIdentificacion = "C";
    
    @Column(name = "cod_ciu")
    private BigInteger codCiu;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "para", fetch = FetchType.LAZY)
    private Collection<RegpNotaDevolutiva> regpNotaDevolutivaCollection;

    @JoinColumn(name = "estado_civil", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem estadoCivil;
    
    @Transient
    private String nombreCompleto;

    public CatEnte() {
    }

    public CatEnte(Long id) {
        this.id = id;
    }

    public CatEnte(Long id, Boolean esPersona, String estado) {
        this.id = id;
        this.esPersona = esPersona;
        this.estado = estado;
    }

    public String getNombreCompleto() {
        if (esPersona) {
            return Utils.isEmpty(apellidos) + " " + Utils.isEmpty(nombres);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombresApellidos() {
        if (esPersona) {
            return Utils.isEmpty(nombres) + " " + Utils.isEmpty(apellidos);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public String getTituloNombreCompleto() {
        if (esPersona) {
            return Utils.isEmpty(tituloProf) + " " + Utils.isEmpty(apellidos) + " " + Utils.isEmpty(nombres);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public String getNombresTitulo() {
        if (esPersona) {
            return Utils.isEmpty(nombres) + " " + Utils.isEmpty(apellidos) + "\n" + Utils.isEmpty(tituloProf);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCiRuc() {
        return ciRuc;
    }

    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigInteger getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(BigInteger representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public Date getFechaCre() {
        return fechaCre;
    }

    public void setFechaCre(Date fechaCre) {
        this.fechaCre = fechaCre;
    }

    public String getUserMod() {
        return userMod;
    }

    public void setUserMod(String userMod) {
        this.userMod = userMod;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTituloProf() {
        return tituloProf;
    }

    public void setTituloProf(String tituloProf) {
        this.tituloProf = tituloProf;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<AclUser> getAclUserCollection() {
        return aclUserCollection;
    }

    public void setAclUserCollection(Collection<AclUser> aclUserCollection) {
        this.aclUserCollection = aclUserCollection;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    public Boolean getExcepcional() {
        return excepcional;
    }

    public void setExcepcional(Boolean excepcional) {
        this.excepcional = excepcional;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<RegMovimientoCliente> getRegMovimientoClienteCollection() {
        return regMovimientoClienteCollection;
    }

    public void setRegMovimientoClienteCollection(Collection<RegMovimientoCliente> regMovimientoClienteCollection) {
        this.regMovimientoClienteCollection = regMovimientoClienteCollection;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
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
        if (!(object instanceof CatEnte)) {
            return false;
        }
        CatEnte other = (CatEnte) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public Collection<RegpNotaDevolutiva> getRegpNotaDevolutivaCollection() {
        return regpNotaDevolutivaCollection;
    }

    public void setRegpNotaDevolutivaCollection(Collection<RegpNotaDevolutiva> regpNotaDevolutivaCollection) {
        this.regpNotaDevolutivaCollection = regpNotaDevolutivaCollection;
    }

    public CtlgItem getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(CtlgItem estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public BigInteger getCodCiu() {
        return codCiu;
    }

    public void setCodCiu(BigInteger codCiu) {
        this.codCiu = codCiu;
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CatEnte[ id=" + id + " ]";
    }

}
