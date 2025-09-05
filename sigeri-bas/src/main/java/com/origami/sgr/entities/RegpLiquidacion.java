/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Where;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_liquidacion", schema = "flow")
@XmlRootElement
public class RegpLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "inf_adicional")
    private String infAdicional;
    @Column(name = "observacion")
    private String observacionText;
    @JoinColumn(name = "estado_pago", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpEstadoPago estadoPago;
    @JoinColumn(name = "estado_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpEstadoLiquidacion estadoLiquidacion;
    @Column(name = "descuento_valor")
    private BigDecimal descuentoValor;
    @Column(name = "valor_actos")
    private BigDecimal valorActos;
    @Column(name = "num_tramite_rp")
    private Long numTramiteRp;
    @Column(name = "total_pagar")
    private BigDecimal totalPagar;
    @Column(name = "numero_comprobante")
    private BigInteger numeroComprobante = BigInteger.ZERO;
    @Column(name = "is_registro_propiedad")
    private Boolean esRegistroPropiedad = true;
    @Column(name = "reingreso")
    private Boolean reingreso = false;
    @Column(name = "ingresado")
    private Boolean ingresado = false;
    @Column(name = "cantidad_razones")
    private Integer cantidadRazones;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "inscripcion")
    private Boolean inscripcion = false;
    @Column(name = "certificado")
    private Boolean certificado = false;
    @Column(name = "user_anular")
    private Long userAnula;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;

    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @OrderBy("fecha_ingreso ASC")
    private Collection<RegpLiquidacionDetalles> regpLiquidacionDetallesCollection;

    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HistoricoTramites tramite;
    @JoinColumn(name = "uso_documento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem usoDocumento;
    @Column(name = "desc_limit_cobro")
    private BigDecimal DescLimitCobro;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte solicitante;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte beneficiario;
    @JoinColumn(name = "tramitador", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte tramitador;
    @JoinColumn(name = "estudio_juridico", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem estudioJuridico;
    @Column(name = "inf_adicional_prof")
    private String infAdicionalProf;

    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    @OrderBy("num_comprobante ASC")
    private Collection<RenPago> renPagoCollection;

    @Column(name = "user_creacion")
    private Long userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "user_edicion")
    private Long userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "user_ingreso")
    private Long userIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "clave_acceso")
    private String claveAcceso;
    @Column(name = "codigo_comprobante")
    private String codigoComprobante;

    @JoinColumn(name = "domicilio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegDomicilio domicilio;
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
    @JoinColumn(name = "ente_judicial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteJudiciales enteJudicial;
    @Column(name = "repertorio")
    private Integer repertorio;
    @Column(name = "fecha_repertorio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRepertorio;
    @JoinColumn(name = "inscriptor", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser inscriptor;
    @JoinColumn(name = "parroquia", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatParroquia parroquia;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Column(name = "ride_enviado")
    private Boolean rideEnviado = Boolean.FALSE;
    @Column(name = "numero_autorizacion")
    private String numeroAutorizacion;
    @Column(name = "estado_ws")
    private String estadoWs;
    @Column(name = "mensaje_ws")
    private String mensajeWs;

    @Column(name = "tasa_catastro")
    private BigDecimal tasaCatastro = BigDecimal.ZERO;
    @Column(name = "gastos_generales")
    private BigDecimal gastosGenerales = BigDecimal.ZERO;
    @Column(name = "descuento_porc")
    private BigDecimal descuentoPorc = BigDecimal.ZERO;

    @Size(max = 500)
    @Column(name = "oficio_memo_referencia")
    private String oficioMemoReferencia;
    @Column(name = "version_descuento")
    private Integer versionDescuento = 1;
    @Column(name = "tramite_referencia")
    private Long tramiteReferencia;

    @Transient
    private BigDecimal pagoFinal;
    @Transient
    private RegpObservacionesIngreso observacion;
    @Transient
    private RenCajero caja;
    @Transient
    private String emailTemp;
    @Column(name = "genera_factura")
    private Boolean generaFactura = Boolean.TRUE;
    @Column(name = "es_juridico")
    private Boolean esJuridico = Boolean.FALSE;

    @Column(name = "adicional")
    private BigDecimal adicional;

    @Column(name = "peso_tramite")
    private Integer pesoTramite = 1;

    @Column(name = "num_formulario")
    private String numFormulario;

    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    private Collection<RegpTareasDinardap> regpTareasDinardapCollection;

    @Column(name = "certificado_no_poseer_bien")
    private Boolean certificadoSinFlujo = Boolean.FALSE;

    @Transient
    private Boolean liquidacionSinTramite = Boolean.FALSE;

    /*@JoinColumn(name = "ficha", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegFicha ficha;*/
    @Column(name = "ficha")
    private Long numFicha = 0L;
    
    @Column(name = "num_inscripcion")
    private Integer numInscripcion = 0;
    @Column(name = "anio_inscripcion")
    private Integer anioInscripcion = 0;

    /*@JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegMovimiento movimiento;*/
    @Column(name = "movimiento")
    private Long movimiento = 0L;

    @Column(name = "tramite_online")
    private Boolean tramiteOnline = Boolean.FALSE;
    
    @Column(name = "tramite_corporativo")
    private Boolean tramiteCorporativo = Boolean.FALSE;
    @Column(name = "firma")
    private byte[] firma;
    
    @Column(name = "ingreso_direccion")
    private String ingresoDireccion;
    @Column(name = "ingreso_num_repertorio")
    private String ingresoNumRepertorio;
    @Column(name = "ingreso_fecha_repertorio")
    private String ingresoFechaRepertorio;
    @Column(name = "ingreso_fecha_inscripcion")
    private String ingresoFechaInscripcion;
    @Column(name = "ingreso_linderos")
    private Boolean ingresoLinderos = Boolean.FALSE;
    @Column(name = "ingreso_historiado")
    private Boolean ingresoHistoriado = Boolean.FALSE;
    @Column(name = "ingreso_ventas")
    private Boolean ingresoVentas = Boolean.FALSE;
    @Column(name = "titulo_credito")
    private Long tituloCredito = 0L;
    
    @Transient
    private Date fechainscripcioningreso;
    @Transient
    private Date fecharepertorioingreso;
    @Column(name = "correo_tramite")
    private String correoTramite;

    public RegpLiquidacion() {
    }

    public RegpLiquidacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfAdicional() {
        return infAdicional;
    }

    public void setInfAdicional(String infAdicional) {
        this.infAdicional = infAdicional;
    }

    public RegpEstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(RegpEstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public BigDecimal getDescuentoValor() {
        return descuentoValor;
    }

    public void setDescuentoValor(BigDecimal descuentoValor) {
        this.descuentoValor = descuentoValor;
    }

    public Long getNumTramiteRp() {
        return numTramiteRp;
    }

    public void setNumTramiteRp(Long numTramiteRp) {
        this.numTramiteRp = numTramiteRp;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Boolean getIngresado() {
        return ingresado;
    }

    public void setIngresado(Boolean ingresado) {
        this.ingresado = ingresado;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Long getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(Long userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    @XmlTransient
    public Collection<RegpLiquidacionDetalles> getRegpLiquidacionDetallesCollection() {
        return regpLiquidacionDetallesCollection;
    }

    public void setRegpLiquidacionDetallesCollection(Collection<RegpLiquidacionDetalles> regpLiquidacionDetallesCollection) {
        this.regpLiquidacionDetallesCollection = regpLiquidacionDetallesCollection;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public CtlgItem getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(CtlgItem usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    public BigDecimal getDescLimitCobro() {
        return DescLimitCobro;
    }

    public void setDescLimitCobro(BigDecimal DescLimitCobro) {
        this.DescLimitCobro = DescLimitCobro;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public Collection<RenPago> getRenPagoCollection() {
        return renPagoCollection;
    }

    public void setRenPagoCollection(Collection<RenPago> renPagoCollection) {
        this.renPagoCollection = renPagoCollection;
    }

    public BigDecimal getPagoFinal() {
        return pagoFinal;
    }

    public void setPagoFinal(BigDecimal pagoFinal) {
        this.pagoFinal = pagoFinal;
    }

    public Integer getCantidadRazones() {
        return cantidadRazones;
    }

    public void setCantidadRazones(Integer cantidadRazones) {
        this.cantidadRazones = cantidadRazones;
    }

    public RegpObservacionesIngreso getObservacion() {
        return observacion;
    }

    public void setObservacion(RegpObservacionesIngreso observacion) {
        this.observacion = observacion;
    }

    public RegpEstadoLiquidacion getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(RegpEstadoLiquidacion estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public Long getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(Long userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(Long userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public RegDomicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(RegDomicilio domicilio) {
        this.domicilio = domicilio;
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

    public RegEnteJudiciales getEnteJudicial() {
        return enteJudicial;
    }

    public void setEnteJudicial(RegEnteJudiciales enteJudicial) {
        this.enteJudicial = enteJudicial;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Date getFechaRepertorio() {
        return fechaRepertorio;
    }

    public void setFechaRepertorio(Date fechaRepertorio) {
        this.fechaRepertorio = fechaRepertorio;
    }

    public AclUser getInscriptor() {
        return inscriptor;
    }

    public void setInscriptor(AclUser inscriptor) {
        this.inscriptor = inscriptor;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public Boolean getRideEnviado() {
        return rideEnviado;
    }

    public void setRideEnviado(Boolean rideEnviado) {
        this.rideEnviado = rideEnviado;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Long getUserAnula() {
        return userAnula;
    }

    public void setUserAnula(Long userAnula) {
        this.userAnula = userAnula;
    }

    public Boolean getEsRegistroPropiedad() {
        return esRegistroPropiedad;
    }

    public void setEsRegistroPropiedad(Boolean esRegistroPropiedad) {
        this.esRegistroPropiedad = esRegistroPropiedad;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public BigDecimal getTasaCatastro() {
        return tasaCatastro;
    }

    public void setTasaCatastro(BigDecimal tasaCatastro) {
        this.tasaCatastro = tasaCatastro;
    }

    public BigDecimal getGastosGenerales() {
        return gastosGenerales;
    }

    public void setGastosGenerales(BigDecimal gastosGenerales) {
        this.gastosGenerales = gastosGenerales;
    }

    public BigDecimal getDescuentoPorc() {
        return descuentoPorc;
    }

    public void setDescuentoPorc(BigDecimal descuentoPorc) {
        this.descuentoPorc = descuentoPorc;
    }

    public Boolean getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Boolean inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Boolean getCertificado() {
        return certificado;
    }

    public void setCertificado(Boolean certificado) {
        this.certificado = certificado;
    }

    public CatEnte getTramitador() {
        return tramitador;
    }

    public void setTramitador(CatEnte tramitador) {
        this.tramitador = tramitador;
    }

    public CtlgItem getEstudioJuridico() {
        return estudioJuridico;
    }

    public void setEstudioJuridico(CtlgItem estudioJuridico) {
        this.estudioJuridico = estudioJuridico;
    }

    public String getInfAdicionalProf() {
        return infAdicionalProf;
    }

    public void setInfAdicionalProf(String infAdicionalProf) {
        this.infAdicionalProf = infAdicionalProf;
    }

    public String getEstadoWs() {
        return estadoWs;
    }

    public void setEstadoWs(String estadoWs) {
        this.estadoWs = estadoWs;
    }

    public String getMensajeWs() {
        return mensajeWs;
    }

    public void setMensajeWs(String mensajeWs) {
        this.mensajeWs = mensajeWs;
    }

    public String getOficioMemoReferencia() {
        return oficioMemoReferencia;
    }

    public void setOficioMemoReferencia(String oficioMemoReferencia) {
        this.oficioMemoReferencia = oficioMemoReferencia;
    }

    public Boolean getReingreso() {
        return reingreso;
    }

    public void setReingreso(Boolean reingreso) {
        this.reingreso = reingreso;
    }

    public Integer getVersionDescuento() {
        return versionDescuento;
    }

    public void setVersionDescuento(Integer versionDescuento) {
        this.versionDescuento = versionDescuento;
    }

    public CatEnte getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(CatEnte beneficiario) {
        this.beneficiario = beneficiario;
    }

    public BigDecimal getValorActos() {
        return valorActos;
    }

    public void setValorActos(BigDecimal valorActos) {
        this.valorActos = valorActos;
    }

    public RenCajero getCaja() {
        return caja;
    }

    public void setCaja(RenCajero caja) {
        this.caja = caja;
    }

    public Long getTramiteReferencia() {
        return tramiteReferencia;
    }

    public void setTramiteReferencia(Long tramiteReferencia) {
        this.tramiteReferencia = tramiteReferencia;
    }

    public String getEmailTemp() {
        return emailTemp;
    }

    public void setEmailTemp(String emailTemp) {
        this.emailTemp = emailTemp;
    }

    public BigDecimal getAdicional() {
        return adicional;
    }

    public void setAdicional(BigDecimal adicional) {
        this.adicional = adicional;
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
        if (!(object instanceof RegpLiquidacion)) {
            return false;
        }
        RegpLiquidacion other = (RegpLiquidacion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpLiquidacion[ id=" + id + " ]";
    }

    public Boolean getGeneraFactura() {
        return generaFactura;
    }

    public void setGeneraFactura(Boolean generaFactura) {
        this.generaFactura = generaFactura;
    }

    public Boolean getEsJuridico() {
        return esJuridico;
    }

    public void setEsJuridico(Boolean esJuridico) {
        this.esJuridico = esJuridico;
    }

    public Integer getPesoTramite() {
        return pesoTramite;
    }

    public void setPesoTramite(Integer pesoTramite) {
        this.pesoTramite = pesoTramite;
    }

    public String getNumFormulario() {
        return numFormulario;
    }

    public void setNumFormulario(String numFormulario) {
        this.numFormulario = numFormulario;
    }

    public Collection<RegpTareasDinardap> getRegpTareasDinardapCollection() {
        return regpTareasDinardapCollection;
    }

    public void setRegpTareasDinardapCollection(Collection<RegpTareasDinardap> regpTareasDinardapCollection) {
        this.regpTareasDinardapCollection = regpTareasDinardapCollection;
    }

    public Boolean getLiquidacionSinTramite() {
        return liquidacionSinTramite;
    }

    public void setLiquidacionSinTramite(Boolean liquidacionSinTramite) {
        this.liquidacionSinTramite = liquidacionSinTramite;
    }

    /*public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }*/

    public Boolean getCertificadoSinFlujo() {
        return certificadoSinFlujo;
    }

    public void setCertificadoSinFlujo(Boolean certificadoSinFlujo) {
        this.certificadoSinFlujo = certificadoSinFlujo;
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Integer getAnioInscripcion() {
        return anioInscripcion;
    }

    public void setAnioInscripcion(Integer anioInscripcion) {
        this.anioInscripcion = anioInscripcion;
    }

    /*public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }*/

    public Boolean getTramiteOnline() {
        return tramiteOnline;
    }

    public void setTramiteOnline(Boolean tramiteOnline) {
        this.tramiteOnline = tramiteOnline;
    }

    public Boolean getTramiteCorporativo() {
        return tramiteCorporativo;
    }

    public void setTramiteCorporativo(Boolean tramiteCorporativo) {
        this.tramiteCorporativo = tramiteCorporativo;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public Long getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Long movimiento) {
        this.movimiento = movimiento;
    }

    public String getObservacionText() {
        return observacionText;
    }

    public void setObservacionText(String observacionText) {
        this.observacionText = observacionText;
    }

    public String getIngresoDireccion() {
        return ingresoDireccion;
    }

    public void setIngresoDireccion(String ingresoDireccion) {
        this.ingresoDireccion = ingresoDireccion;
    }

    public String getIngresoNumRepertorio() {
        return ingresoNumRepertorio;
    }

    public void setIngresoNumRepertorio(String ingresoNumRepertorio) {
        this.ingresoNumRepertorio = ingresoNumRepertorio;
    }

    public String getIngresoFechaRepertorio() {
        return ingresoFechaRepertorio;
    }

    public void setIngresoFechaRepertorio(String ingresoFechaRepertorio) {
        this.ingresoFechaRepertorio = ingresoFechaRepertorio;
    }

    public String getIngresoFechaInscripcion() {
        return ingresoFechaInscripcion;
    }

    public void setIngresoFechaInscripcion(String ingresoFechaInscripcion) {
        this.ingresoFechaInscripcion = ingresoFechaInscripcion;
    }

    public Boolean getIngresoLinderos() {
        return ingresoLinderos;
    }

    public void setIngresoLinderos(Boolean ingresoLinderos) {
        this.ingresoLinderos = ingresoLinderos;
    }

    public Boolean getIngresoHistoriado() {
        return ingresoHistoriado;
    }

    public void setIngresoHistoriado(Boolean ingresoHistoriado) {
        this.ingresoHistoriado = ingresoHistoriado;
    }

    public Boolean getIngresoVentas() {
        return ingresoVentas;
    }

    public void setIngresoVentas(Boolean ingresoVentas) {
        this.ingresoVentas = ingresoVentas;
    }

    public Date getFechainscripcioningreso() {
        return fechainscripcioningreso;
    }

    public void setFechainscripcioningreso(Date fechainscripcioningreso) {
        this.fechainscripcioningreso = fechainscripcioningreso;
    }

    public Date getFecharepertorioingreso() {
        return fecharepertorioingreso;
    }

    public void setFecharepertorioingreso(Date fecharepertorioingreso) {
        this.fecharepertorioingreso = fecharepertorioingreso;
    }

    public Long getTituloCredito() {
        return tituloCredito;
    }

    public void setTituloCredito(Long tituloCredito) {
        this.tituloCredito = tituloCredito;
    }

    public String getCorreoTramite() {
        return correoTramite;
    }

    public void setCorreoTramite(String correoTramite) {
        this.correoTramite = correoTramite;
    }

}
