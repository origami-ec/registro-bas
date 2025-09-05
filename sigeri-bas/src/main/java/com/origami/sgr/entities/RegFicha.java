/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_ficha", schema = "app")
@XmlRootElement
public class RegFicha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_ficha")
    private Long numFicha;
    @Column(name = "fecha_ape")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaApe;
    @Column(name = "linderos")
    private String linderos;
    @Size(max = 155)
    @Column(name = "direccion_bien")
    private String direccionBien;
    @Size(max = 1)
    @Column(name = "tipo_predio")
    private String tipoPredio;
    @JoinColumn(name = "tipo_ficha", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegTipoFicha tipoFicha;
    @Column(name = "descripcion_bien")
    private String descripcionBien;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem estado;
    @Column(name = "ficha_matriz")
    private BigInteger fichaMatriz;
    @Size(max = 100)
    @Column(name = "user_edicion")
    private String userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area_escritura")
    private BigDecimal areaEscritura;
    @Column(name = "alicuota_escritura")
    private BigDecimal alicuotaEscritura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ficha", fetch = FetchType.LAZY)
    private Collection<RegMovimientoFicha> regMovimientoFichaCollection;
    @JoinColumn(name = "parroquia", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatParroquia parroquia;
    @Size(max = 100)
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Size(max = 100)
    @Column(name = "clave_catastral_old")
    private String claveCatastralOld;
    @Column(name = "user_ingreso")
    private String userIngreso;
    @Column(name = "sector")
    private String sector;
    @Column(name = "id_foto")
    private Long idFoto;
    @Column(name = "url_foto")
    private String urlFoto;
    @JoinColumn(name = "uaf_tipo_bien", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UafTipoBien uafTipoBien;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ficha", fetch = FetchType.LAZY)
    private Collection<RegFichaPropietarios> regFichaPropietariosCollection;
    @OneToMany(mappedBy = "ficha", fetch = FetchType.LAZY)
    private Collection<RegFichaMarginacion> regFichaMarginacionCollection;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HistoricoTramites tramite;
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "area_terreno")
    private BigDecimal areaTerreno;
    @Column(name = "area_construccion")
    private BigDecimal areaConstruccion;
    @JoinColumn(name = "unidad_medida", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem unidadMedida;
    @Column(name = "alicuota_construccion")
    private BigDecimal alicuotaConstruccion;
    @JoinColumn(name = "informacion_ficha", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem informacionFicha;
    @Size(max = 100)
    @Column(name = "user_revision")
    private String userRevision;
    @Column(name = "fecha_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRevision;
    @Column(name = "manazana")
    private String manzana;
    @Column(name = "lote")
    private String lote;
    @Column(name = "division")
    private String division;
    @Column(name = "departamento")
    private String departamento;
    @Column(name = "nombre_predio")
    private String nombrePredio;
    @JoinColumn(name = "barrio", referencedColumnName = "id")
    @ManyToOne
    private Barrios barrio;

    @Transient
    private CatEnte persona;
    @Transient
    private CtlgItem state;

    public RegFicha() {
    }

    public RegFicha(Long id) {
        this.id = id;
    }

    public RegFicha(Long id, long numFicha) {
        this.id = id;
        this.numFicha = numFicha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public Date getFechaApe() {
        return fechaApe;
    }

    public void setFechaApe(Date fechaApe) {
        this.fechaApe = fechaApe;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public String getDescripcionBien() {
        return descripcionBien;
    }

    public void setDescripcionBien(String descripcionBien) {
        this.descripcionBien = descripcionBien;
    }

    public CtlgItem getEstado() {
        return estado;
    }

    public void setEstado(CtlgItem estado) {
        this.estado = estado;
    }

    public BigInteger getFichaMatriz() {
        return fichaMatriz;
    }

    public void setFichaMatriz(BigInteger fichaMatriz) {
        this.fichaMatriz = fichaMatriz;
    }

    public String getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(String userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public BigDecimal getAreaEscritura() {
        return areaEscritura;
    }

    public void setAreaEscritura(BigDecimal areaEscritura) {
        this.areaEscritura = areaEscritura;
    }

    public BigDecimal getAlicuotaEscritura() {
        return alicuotaEscritura;
    }

    public void setAlicuotaEscritura(BigDecimal alicuotaEscritura) {
        this.alicuotaEscritura = alicuotaEscritura;
    }

    @XmlTransient
    public Collection<RegMovimientoFicha> getRegMovimientoFichaCollection() {
        return regMovimientoFichaCollection;
    }

    public void setRegMovimientoFichaCollection(Collection<RegMovimientoFicha> regMovimientoFichaCollection) {
        this.regMovimientoFichaCollection = regMovimientoFichaCollection;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public String getTipoPredioTemp() {
        if (this.tipoPredio == null) {
            return "";
        }
        switch (this.tipoPredio) {
            case "U":
                return "URBANO";
            case "R":
                return "RURAL";
            default:
                return "";
        }
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getClaveCatastralOld() {
        return claveCatastralOld;
    }

    public void setClaveCatastralOld(String claveCatastralOld) {
        this.claveCatastralOld = claveCatastralOld;
    }

    public String getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(String userIngreso) {
        this.userIngreso = userIngreso;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public UafTipoBien getUafTipoBien() {
        return uafTipoBien;
    }

    public void setUafTipoBien(UafTipoBien uafTipoBien) {
        this.uafTipoBien = uafTipoBien;
    }

    public String getDireccionBien() {
        return direccionBien;
    }

    public void setDireccionBien(String direccionBien) {
        this.direccionBien = direccionBien;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Collection<RegFichaPropietarios> getRegFichaPropietariosCollection() {
        return regFichaPropietariosCollection;
    }

    public void setRegFichaPropietariosCollection(Collection<RegFichaPropietarios> regFichaPropietariosCollection) {
        this.regFichaPropietariosCollection = regFichaPropietariosCollection;
    }

    public Collection<RegFichaMarginacion> getRegFichaMarginacionCollection() {
        return regFichaMarginacionCollection;
    }

    public void setRegFichaMarginacionCollection(Collection<RegFichaMarginacion> regFichaMarginacionCollection) {
        this.regFichaMarginacionCollection = regFichaMarginacionCollection;
    }

    public BigDecimal getAreaTerreno() {
        return areaTerreno;
    }

    public void setAreaTerreno(BigDecimal areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public CtlgItem getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(CtlgItem unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getAlicuotaConstruccion() {
        return alicuotaConstruccion;
    }

    public void setAlicuotaConstruccion(BigDecimal alicuotaConstruccion) {
        this.alicuotaConstruccion = alicuotaConstruccion;
    }

    public CtlgItem getInformacionFicha() {
        return informacionFicha;
    }

    public void setInformacionFicha(CtlgItem informacionFicha) {
        this.informacionFicha = informacionFicha;
    }

    public String getUserRevision() {
        return userRevision;
    }

    public void setUserRevision(String userRevision) {
        this.userRevision = userRevision;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNombrePredio() {
        return nombrePredio;
    }

    public void setNombrePredio(String nombrePredio) {
        this.nombrePredio = nombrePredio;
    }

    public Barrios getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrios barrio) {
        this.barrio = barrio;
    }

    public CatEnte getPersona() {
        return persona;
    }

    public void setPersona(CatEnte persona) {
        this.persona = persona;
    }

    public CtlgItem getState() {
        if (state == null) {
            state = estado;
        }
        return state;
    }

    public void setState(CtlgItem state) {
        this.state = state;
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
        if (!(object instanceof RegFicha)) {
            return false;
        }
        RegFicha other = (RegFicha) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegFicha[ id=" + id + " ]";
    }

}
