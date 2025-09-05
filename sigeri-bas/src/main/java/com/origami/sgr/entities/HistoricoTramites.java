/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "historico_tramites", schema = "flow")
@NamedQueries({
    @NamedQuery(name = "HistoricoTramites.findAll", query = "SELECT h FROM HistoricoTramites h"),
    @NamedQuery(name = "HistoricoTramites.findById", query = "SELECT h FROM HistoricoTramites h WHERE h.id = :id"),
    @NamedQuery(name = "HistoricoTramites.findByNumTramite", query = "SELECT h FROM HistoricoTramites h WHERE h.numTramite = :numTramite"),
    @NamedQuery(name = "HistoricoTramites.findByIdProceso", query = "SELECT h FROM HistoricoTramites h WHERE h.idProceso = :idProceso")})
public class HistoricoTramites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Size(max = 100)
    @Column(name = "id_proceso")
    private String idProceso;
    @Size(max = 100)
    @Column(name = "id_proceso_temp")
    private String idProcesoTemp;
    @Size(max = 250)
    @Column(name = "nombre_propietario")
    private String nombrePropietario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "carpeta_rep")
    private String carpetaRep;
    @Column(name = "entregado")
    private Boolean entregado = false;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte solicitante;
    @OneToMany(mappedBy = "idTramite", fetch = FetchType.LAZY)
    @OrderBy("fecCre DESC")
    private Collection<Observaciones> observacionesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tramite", fetch = FetchType.LAZY)
    private Collection<RegpLiquidacion> regpLiquidacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tramite", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    @OrderBy("fecha ASC")
    private Collection<RegpTareasTramite> regpTareasTramiteCollection;

    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "fecha_notificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNotificacion;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GeTipoTramite tipoTramite;

    @JoinColumn(name = "usuario_retiro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte usuarioRetiro;
    @Column(name = "fecha_retiro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRetiro;

    @Column(name = "blocked")
    private Boolean blocked = false;
    @Column(name = "user_desblock")
    private Long userDesblock;
    @Column(name = "fecha_desblock")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesblock;
    @Column(name = "documento")
    private Boolean documento = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tramite", fetch = FetchType.LAZY)
    private Collection<RegpNotaDevolutiva> regpNotaDevolutivaCollection;
    /*@Formula(value = "(SELECT CASE WHEN ta.id IS NOT NULL THEN ta.name_ WHEN UPPER(ob1.tarea) ~ 'ENTREGA' THEN 'Finalizado' WHEN ob1.tarea ~ 'CERTIFICADO' THEN ob1.observacion ELSE ob1.tarea END "
            + "FROM flow.observaciones ob1 LEFT JOIN flow.tareas_activas ta ON (ob1.id_tramite = ta.id) WHERE ob1.id_tramite = {alias}.id ORDER BY ob1.fec_cre DESC LIMIT 1) ")*/
    @Formula(value = "(select case when UPPER(rt.name_) ~ 'FIRMA' then 'EN-FIRMA' "
            + "when UPPER(rt.name_) ~ 'DEVOLUTIVA' then 'DEVOLUTIVA' else 'EN-PROCESO' end "
            + "from flow.reporte_tareas rt where rt.fin_time is null and rt.num_tramite = {alias}.num_tramite)")
    private String ultimaTarea;

    public HistoricoTramites() {
    }

    public HistoricoTramites(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCarpetaRep() {
        return carpetaRep;
    }

    public void setCarpetaRep(String carpetaRep) {
        this.carpetaRep = carpetaRep;
    }

    @XmlTransient
    public Collection<Observaciones> getObservacionesCollection() {
        return observacionesCollection;
    }

    public void setObservacionesCollection(Collection<Observaciones> observacionesCollection) {
        this.observacionesCollection = observacionesCollection;
    }

    @XmlTransient
    public Collection<RegpLiquidacion> getRegpLiquidacionCollection() {
        return regpLiquidacionCollection;
    }

    public void setRegpLiquidacionCollection(Collection<RegpLiquidacion> regpLiquidacionCollection) {
        this.regpLiquidacionCollection = regpLiquidacionCollection;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public Collection<RegpTareasTramite> getRegpTareasTramiteCollection() {
        return regpTareasTramiteCollection;
    }

    public void setRegpTareasTramiteCollection(Collection<RegpTareasTramite> regpTareasTramiteCollection) {
        this.regpTareasTramiteCollection = regpTareasTramiteCollection;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public GeTipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(GeTipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public CatEnte getUsuarioRetiro() {
        return usuarioRetiro;
    }

    public void setUsuarioRetiro(CatEnte usuarioRetiro) {
        this.usuarioRetiro = usuarioRetiro;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Long getUserDesblock() {
        return userDesblock;
    }

    public void setUserDesblock(Long userDesblock) {
        this.userDesblock = userDesblock;
    }

    public Date getFechaDesblock() {
        return fechaDesblock;
    }

    public void setFechaDesblock(Date fechaDesblock) {
        this.fechaDesblock = fechaDesblock;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
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
        if (!(object instanceof HistoricoTramites)) {
            return false;
        }
        HistoricoTramites other = (HistoricoTramites) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public Collection<RegpNotaDevolutiva> getRegpNotaDevolutivaCollection() {
        return regpNotaDevolutivaCollection;
    }

    public void setRegpNotaDevolutivaCollection(Collection<RegpNotaDevolutiva> regpNotaDevolutivaCollection) {
        this.regpNotaDevolutivaCollection = regpNotaDevolutivaCollection;
    }

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getUltimaTarea() {
        return ultimaTarea;
    }

    public void setUltimaTarea(String ultimaTarea) {
        this.ultimaTarea = ultimaTarea;
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.HistoricoTramites[ id=" + id + " ]";
    }

    /**
     *
     * @return 0 EN PROCESO 1 PARA ENTREGA 2 FINALIZADO
     */
    public Integer getEstadoTramite() {
        //System.out.println("// estado: >> " + ultimaTarea);
        // SI ES NULL EL TRAMITE ESTA FINALIZADO
        if (ultimaTarea == null) {
            return 0;
        }
        // EN PROCESO 
        if (ultimaTarea.equalsIgnoreCase("EN-PROCESO")) {
            return 1;
        }
        // PARA FIRMAR
        if (ultimaTarea.toUpperCase().contains("EN-FIRMA")) {
            return 2;
        }
        // CON DEVOLUTIVA
        if (ultimaTarea.toUpperCase().contains("DEVOLUTIVA")) {
            return 3;
        }
        return 0;
    }

}
