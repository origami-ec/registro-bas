/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_movimiento", schema = "app")
@XmlRootElement
public class RegMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_repertorio")
    private Integer numRepertorio;
    @Column(name = "num_inscripcion")
    private Integer numInscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "indice")
    private Integer indice = 0;
    @Column(name = "folio_inicio")
    private Integer folioInicio = 0;
    @Column(name = "folio_fin")
    private Integer folioFin = 0;
    @Column(name = "fecha_cort")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCort;
    /**
     * ESTADO DEL MOVIMIENTOS
     */
    /*@Column(name = "registro")
    private Integer registro;*/
    @JoinColumn(name = "registro", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private RegEstadoMovimiento registro;
    @Size(max = 3)
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Column(name = "usuario_mod")
    private Integer usuarioMod;
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 10)
    @Column(name = "num_tomo")
    private String numTomo;
    @Column(name = "fecha_oto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOto;
    @Column(name = "ord_jud")
    private Boolean ordJud = false;
    @Column(name = "fecha_resolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResolucion;
    @Column(name = "escrit_juic_prov_resolucion")
    private String escritJuicProvResolucion;
    @Column(name = "fecha_repertorio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRepertorio;
    @JoinColumn(name = "num_tramite", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private RegpTareasTramite tramite;
    @Size(max = 64)
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "razon_impresa")
    private Boolean razonImpresa;
    @Column(name = "inscripcion_impresa")
    private Boolean inscripcionImpresa;
    @Column(name = "num_pagina_razon")
    private Integer numPaginaRazon;
    @Column(name = "num_pagina_inscripcion")
    private Integer numPaginaInscripcion;
    @Column(name = "num_paginas_contabilizada")
    private Integer numPaginasContabilizada;
    @Column(name = "folio_anterior")
    private Integer folioAnterior;
    @Column(name = "es_negativa")
    private Boolean esNegativa = false;
    @Column(name = "vinculo_familiar")
    private Boolean vinculoFamiliar = false;
    @Column(name = "valor_uuid")
    private String valorUuid;
    @Column(name = "anexo_negativa")
    private String anexoNegativa;
    @Column(name = "obs_cancela")
    private String obsCancela;
    @Column(name = "transferencia_dominio")
    private Boolean transferenciaDominio = false;
    @Column(name = "escrituras")
    private Boolean escrituras = false;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private Collection<RegMovimientoFicha> regMovimientoFichaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private Collection<RegCertificadoMovimiento> regCertificadoMovimientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimientoReferencia", fetch = FetchType.LAZY)
    private Collection<RegMovimientoReferencia> regMovimientoReferenciaCollection;

    @JoinColumn(name = "registrador", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegRegistrador registrador;
    @JoinColumn(name = "libro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegLibro libro;
//    @NotNull(message = "Not./Juz es obligatorio.")
    @JoinColumn(name = "ente_judicial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteJudiciales enteJudicial;
    @JoinColumn(name = "domicilio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegDomicilio domicilio;
    @JoinColumn(name = "acto", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegActo acto;
    @JoinColumn(name = "user_creador", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser userCreador;
    @OneToMany(mappedBy = "movimiento", fetch = FetchType.LAZY)
    private Collection<RegMovimientoCliente> regMovimientoClienteCollection;
    @OneToMany(mappedBy = "movimiento", fetch = FetchType.LAZY)
    @OrderBy("fechaIngreso")
    private Collection<RegMovimientoMarginacion> regMovimientoMarginacionCollection;
    @OneToMany(mappedBy = "movimiento", fetch = FetchType.LAZY)
    private Collection<RegMovimientoCapital> regMovimientoCapitalCollection;
    @OneToMany(mappedBy = "movimiento", fetch = FetchType.LAZY)
    private Collection<RegMovimientoRepresentante> regMovimientoRepresentanteCollection;
    @OneToMany(mappedBy = "movimiento", fetch = FetchType.LAZY)
    private Collection<RegMovimientoSocios> regMovimientoSociosCollection;

    @Column(name = "editable")
    private Boolean editable = true;
    @Column(name = "response_catastro")
    private String responseCatastro;
    @Column(name = "uafe")
    private Boolean uafe;

    @Size(max = 100)
    @Column(name = "tomo_registro")
    private String tomoRegistro;
    @Size(max = 10)
    @Column(name = "secuencia_repertorio")
    private String secuenciaRepertorio;
    @Size(max = 10)
    @Column(name = "secuencia_inscripcion")
    private String secuenciaInscripcion;
    @Size(max = 100)
    @Column(name = "send_ws")
    private String sendWs;
    @Size(max = 50)
    @Column(name = "tramite_muni")
    private String tramiteMuni;

    @Column(name = "avaluo_municipal")
    private BigDecimal avaluoMunicipal;

    @Column(name = "cuantia")
    private BigDecimal cuantia;

    @Column(name = "base_imponible")
    private BigDecimal baseImponible;

    @Column(name = "tipo_juicio") //1: CIVIL - 2: PENAL
    private Integer tipoJuicio;

    @Column(name = "cuantia_cadena")
    private String cuantiaCadena;
    @Column(name = "avaluo_municipal_cadena")
    private String avaluoMunicipalCadena;
    @Column(name = "cod_verificacion")
    private String codVerificacion;
    @Column(name = "documento")
    private Long documento;
    @Column(name = "documento_acta")
    private Long documentoActa;
    @Column(name = "id_transaccion")
    private Long idTransaccion;
    @Column(name = "id_padre")
    private BigInteger idPadre;
    @Column(name = "id_blob_reg")
    private BigInteger idBlobReg;
    @Transient
    private String observacionEliminacion;
    @Transient
    private String descripcionMarginacion;
    @Transient
    private Boolean marginados;
    @Formula(value = "(SELECT ht.num_tramite FROM flow.historico_tramites ht "
            + "left join flow.regp_tareas_tramite tt on tt.tramite = ht.id "
            + "where tt.id = {alias}.num_tramite)")
    private Long numeroTramite;
    @Column(name = "app")
    private String app;

    public RegMovimiento() {

    }

    public RegMovimiento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public Integer getFolioInicio() {
        return folioInicio;
    }

    public void setFolioInicio(Integer folioInicio) {
        this.folioInicio = folioInicio;
    }

    public Integer getFolioFin() {
        return folioFin;
    }

    public void setFolioFin(Integer folioFin) {
        this.folioFin = folioFin;
    }

    public Date getFechaCort() {
        return fechaCort;
    }

    public void setFechaCort(Date fechaCort) {
        this.fechaCort = fechaCort;
    }

    public RegEstadoMovimiento getRegistro() {
        return registro;
    }

    public void setRegistro(RegEstadoMovimiento registro) {
        this.registro = registro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public Integer getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(Integer usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNumTomo() {
        return numTomo;
    }

    public void setNumTomo(String numTomo) {
        this.numTomo = numTomo;
    }

    public Date getFechaOto() {
        return fechaOto;
    }

    public void setFechaOto(Date fechaOto) {
        this.fechaOto = fechaOto;
    }

    public Boolean getOrdJud() {
        return ordJud;
    }

    public void setOrdJud(Boolean ordJud) {
        this.ordJud = ordJud;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getEscritJuicProvResolucion() {
        return escritJuicProvResolucion;
    }

    public void setEscritJuicProvResolucion(String escritJuicProvResolucion) {
        this.escritJuicProvResolucion = escritJuicProvResolucion;
    }

    public Date getFechaRepertorio() {
        return fechaRepertorio;
    }

    public void setFechaRepertorio(Date fechaRepertorio) {
        this.fechaRepertorio = fechaRepertorio;
    }

    public RegpTareasTramite getTramite() {
        return tramite;
    }

    public void setTramite(RegpTareasTramite tramite) {
        this.tramite = tramite;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Boolean getRazonImpresa() {
        return razonImpresa;
    }

    public void setRazonImpresa(Boolean razonImpresa) {
        this.razonImpresa = razonImpresa;
    }

    public Boolean getInscripcionImpresa() {
        return inscripcionImpresa;
    }

    public void setInscripcionImpresa(Boolean inscripcionImpresa) {
        this.inscripcionImpresa = inscripcionImpresa;
    }

    public Integer getNumPaginaRazon() {
        return numPaginaRazon;
    }

    public void setNumPaginaRazon(Integer numPaginaRazon) {
        this.numPaginaRazon = numPaginaRazon;
    }

    public Integer getNumPaginaInscripcion() {
        return numPaginaInscripcion;
    }

    public void setNumPaginaInscripcion(Integer numPaginaInscripcion) {
        this.numPaginaInscripcion = numPaginaInscripcion;
    }

    public Integer getNumPaginasContabilizada() {
        return numPaginasContabilizada;
    }

    public void setNumPaginasContabilizada(Integer numPaginasContabilizada) {
        this.numPaginasContabilizada = numPaginasContabilizada;
    }

    public Integer getFolioAnterior() {
        return folioAnterior;
    }

    public void setFolioAnterior(Integer folioAnterior) {
        this.folioAnterior = folioAnterior;
    }

    public Boolean getEsNegativa() {
        return esNegativa;
    }

    public void setEsNegativa(Boolean esNegativa) {
        this.esNegativa = esNegativa;
    }

    public String getValorUuid() {
        return valorUuid;
    }

    public void setValorUuid(String valorUuid) {
        this.valorUuid = valorUuid;
    }

    public String getAnexoNegativa() {
        return anexoNegativa;
    }

    public void setAnexoNegativa(String anexoNegativa) {
        this.anexoNegativa = anexoNegativa;
    }

    public String getObsCancela() {
        return obsCancela;
    }

    public void setObsCancela(String obsCancela) {
        this.obsCancela = obsCancela;
    }

    public Boolean getTransferenciaDominio() {
        return transferenciaDominio;
    }

    public void setTransferenciaDominio(Boolean transferenciaDominio) {
        this.transferenciaDominio = transferenciaDominio;
    }

    @XmlTransient
    public Collection<RegMovimientoFicha> getRegMovimientoFichaCollection() {
        return regMovimientoFichaCollection;
    }

    public void setRegMovimientoFichaCollection(Collection<RegMovimientoFicha> regMovimientoFichaCollection) {
        this.regMovimientoFichaCollection = regMovimientoFichaCollection;
    }

    @XmlTransient
    public Collection<RegMovimientoReferencia> getRegMovimientoReferenciaCollection() {
        return regMovimientoReferenciaCollection;
    }

    public void setRegMovimientoReferenciaCollection(Collection<RegMovimientoReferencia> regMovimientoReferenciaCollection) {
        this.regMovimientoReferenciaCollection = regMovimientoReferenciaCollection;
    }

    public RegRegistrador getRegistrador() {
        return registrador;
    }

    public void setRegistrador(RegRegistrador registrador) {
        this.registrador = registrador;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public RegEnteJudiciales getEnteJudicial() {
        return enteJudicial;
    }

    public void setEnteJudicial(RegEnteJudiciales enteJudicial) {
        this.enteJudicial = enteJudicial;
    }

    public RegDomicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(RegDomicilio domicilio) {
        this.domicilio = domicilio;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public AclUser getUserCreador() {
        return userCreador;
    }

    public void setUserCreador(AclUser userCreador) {
        this.userCreador = userCreador;
    }

    public Boolean getEscrituras() {
        return escrituras;
    }

    public void setEscrituras(Boolean escrituras) {
        this.escrituras = escrituras;
    }

    public Boolean getUafe() {
        return uafe;
    }

    public void setUafe(Boolean uafe) {
        this.uafe = uafe;
    }

    @XmlTransient
    public Collection<RegMovimientoCliente> getRegMovimientoClienteCollection() {
        return regMovimientoClienteCollection;
    }

    public void setRegMovimientoClienteCollection(Collection<RegMovimientoCliente> regMovimientoClienteCollection) {
        this.regMovimientoClienteCollection = regMovimientoClienteCollection;
    }

    public Boolean getVinculoFamiliar() {
        return vinculoFamiliar;
    }

    public void setVinculoFamiliar(Boolean vinculoFamiliar) {
        this.vinculoFamiliar = vinculoFamiliar;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getObservacionEliminacion() {
        return observacionEliminacion;
    }

    public void setObservacionEliminacion(String observacionEliminacion) {
        this.observacionEliminacion = observacionEliminacion;
    }

    public String getResponseCatastro() {
        return responseCatastro;
    }

    public void setResponseCatastro(String responseCatastro) {
        this.responseCatastro = responseCatastro;
    }

    public String getTomoRegistro() {
        return tomoRegistro;
    }

    public void setTomoRegistro(String tomoRegistro) {
        this.tomoRegistro = tomoRegistro;
    }

    public String getSecuenciaRepertorio() {
        return secuenciaRepertorio;
    }

    public void setSecuenciaRepertorio(String secuenciaRepertorio) {
        this.secuenciaRepertorio = secuenciaRepertorio;
    }

    public String getSecuenciaInscripcion() {
        return secuenciaInscripcion;
    }

    public void setSecuenciaInscripcion(String secuenciaInscripcion) {
        this.secuenciaInscripcion = secuenciaInscripcion;
    }

    public String getSendWs() {
        return sendWs;
    }

    public void setSendWs(String sendWs) {
        this.sendWs = sendWs;
    }

    public String getTramiteMuni() {
        return tramiteMuni;
    }

    public void setTramiteMuni(String tramiteMuni) {
        this.tramiteMuni = tramiteMuni;
    }

    public Collection<RegCertificadoMovimiento> getRegCertificadoMovimientoCollection() {
        return regCertificadoMovimientoCollection;
    }

    public void setRegCertificadoMovimientoCollection(Collection<RegCertificadoMovimiento> regCertificadoMovimientoCollection) {
        this.regCertificadoMovimientoCollection = regCertificadoMovimientoCollection;
    }

    public Collection<RegMovimientoMarginacion> getRegMovimientoMarginacionCollection() {
        return regMovimientoMarginacionCollection;
    }

    public void setRegMovimientoMarginacionCollection(Collection<RegMovimientoMarginacion> regMovimientoMarginacionCollection) {
        this.regMovimientoMarginacionCollection = regMovimientoMarginacionCollection;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Integer getTipoJuicio() {
        return tipoJuicio;
    }

    public void setTipoJuicio(Integer tipoJuicio) {
        this.tipoJuicio = tipoJuicio;
    }

    public String getDescripcionMarginacion() {
        return descripcionMarginacion;
    }

    public void setDescripcionMarginacion(String descripcionMarginacion) {
        this.descripcionMarginacion = descripcionMarginacion;
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
        if (!(object instanceof RegMovimiento)) {
            return false;
        }
        RegMovimiento other = (RegMovimiento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimiento[ id=" + id + " ]";
    }

    public String getCuantiaCadena() {
        return cuantiaCadena;
    }

    public void setCuantiaCadena(String cuantiaCadena) {
        this.cuantiaCadena = cuantiaCadena;
    }

    public String getAvaluoMunicipalCadena() {
        return avaluoMunicipalCadena;
    }

    public void setAvaluoMunicipalCadena(String avaluoMunicipalCadena) {
        this.avaluoMunicipalCadena = avaluoMunicipalCadena;
    }

    public String getDetalle() {
        if (this.getFechaInscripcion() == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("<b>INSCRIPCION: </b>");
        buffer.append(new SimpleDateFormat("dd/MM/yyyy").format(this.getFechaInscripcion()));
        buffer.append("-");
        buffer.append(Utils.completarCadenaConCeros(this.getNumInscripcion() + "", 6));
        buffer.append("<br/>");
        buffer.append("<b>LIBRO: </b>");
        buffer.append(this.getLibro() == null ? "" : this.getLibro().getNombre());
        buffer.append("<br/>");
        buffer.append("<b>TIPO: </b>");
        buffer.append(this.getActo().getNombre());
        buffer.append("<br/>");
        buffer.append("<b>CUANTIA: </b>");
        buffer.append((this.getCuantiaCadena() == null || this.getCuantiaCadena().isEmpty()) ? this.getCuantia() + "" : (this.getCuantiaCadena() == null ? "" : this.getCuantiaCadena()));
        buffer.append("<br/>");
        buffer.append("<b>NOTARIA: </b>");
        buffer.append(this.getEnteJudicial() == null ? "" : this.getEnteJudicial().getNombre());
        buffer.append("<br/>");
        buffer.append("<b>OBSERVACION: </b>");
        buffer.append(this.getObservacion());
        return buffer.toString();
    }

    public HashMap<RegPapel, List<RegMovimientoCliente>> getGroupByPapel() {
        HashMap<RegPapel, List<RegMovimientoCliente>> group = null;
        if (regMovimientoClienteCollection != null && !regMovimientoClienteCollection.isEmpty()) {
            group = new HashMap<>();
            for (RegMovimientoCliente rmc : regMovimientoClienteCollection) {
                List<RegMovimientoCliente> get = group.get(rmc.getPapel());
                if (get == null) {
                    get = new ArrayList<>(regMovimientoClienteCollection.size());
                }
                get.add(rmc);
                group.put(rmc.getPapel(), get);
            }
        }
        return group;
    }

    public String getDescripcionMarginacion(RegMovimiento movimiento) {
        if (descripcionMarginacion == null) {
            if (movimiento.getLibro().getNombre().toUpperCase().contains("CANCELACION")) {
                descripcionMarginacion = "SE CANCELA " + ((movimiento.getActo() == null) ? "" : movimiento.getActo().getNombre());
            } else {
                descripcionMarginacion = "SE INSCRIBE " + ((movimiento.getActo() == null) ? "" : movimiento.getActo().getNombre());
            }
            String nombres = null;
            if (movimiento.getUserCreador().getEnte() != null) {
                nombres = movimiento.getUserCreador().getEnte().getNombres().split(" ")[0] + " " + movimiento.getUserCreador().getEnte().getApellidos().split(" ")[0];
            } else {
                nombres = "MIGRACION ";
            }
            descripcionMarginacion += " (" + Utils.dateFormatPattern("dd/MM/YYYY", movimiento.getFechaInscripcion()) + " " + nombres + ")";
        }
        return descripcionMarginacion;
    }

    public Boolean getMarginado() {
        return Utils.isNotEmpty(regMovimientoMarginacionCollection);
    }

    public Boolean getMarginados() {
        return marginados;
    }

    public void setMarginados(Boolean marginados) {
        this.marginados = marginados;
    }

    public Collection<RegMovimientoCapital> getRegMovimientoCapitalCollection() {
        return regMovimientoCapitalCollection;
    }

    public void setRegMovimientoCapitalCollection(Collection<RegMovimientoCapital> regMovimientoCapitalCollection) {
        this.regMovimientoCapitalCollection = regMovimientoCapitalCollection;
    }

    public Collection<RegMovimientoRepresentante> getRegMovimientoRepresentanteCollection() {
        return regMovimientoRepresentanteCollection;
    }

    public void setRegMovimientoRepresentanteCollection(Collection<RegMovimientoRepresentante> regMovimientoRepresentanteCollection) {
        this.regMovimientoRepresentanteCollection = regMovimientoRepresentanteCollection;
    }

    public Collection<RegMovimientoSocios> getRegMovimientoSociosCollection() {
        return regMovimientoSociosCollection;
    }

    public void setRegMovimientoSociosCollection(Collection<RegMovimientoSocios> regMovimientoSociosCollection) {
        this.regMovimientoSociosCollection = regMovimientoSociosCollection;
    }

    public String getCodVerificacion() {
        return codVerificacion;
    }

    public void setCodVerificacion(String codVerificacion) {
        this.codVerificacion = codVerificacion;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public Long getDocumentoActa() {
        return documentoActa;
    }

    public void setDocumentoActa(Long documentoActa) {
        this.documentoActa = documentoActa;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public BigInteger getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(BigInteger idPadre) {
        this.idPadre = idPadre;
    }

    public BigInteger getIdBlobReg() {
        return idBlobReg;
    }

    public void setIdBlobReg(BigInteger idBlobReg) {
        this.idBlobReg = idBlobReg;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

}
