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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_liquidacion_detalles", schema = "flow")
@XmlRootElement
public class RegpLiquidacionDetalles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "acto", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegActo acto;
    @Column(name = "num_predio")
    private Integer numPredio;
    @Column(name = "nom_urb")
    private String nomUrb;
    @Column(name = "mz_urb")
    private String mzUrb;
    @Column(name = "sl_urb")
    private String slUrb;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaluo")
    private BigDecimal avaluo;
    @Column(name = "cuantia")
    private BigDecimal cuantia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private Integer cantidad = 1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuento")
    private BigDecimal descuento = BigDecimal.ZERO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacion liquidacion;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpExoneracion exoneracion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "liquidacion", fetch = FetchType.LAZY)
    private Collection<RegpIntervinientes> regpIntervinientesCollection;
    @Column(name = "aplica_descuento")
    private Boolean aplicaDescuento = false;
    @Column(name = "reingreso")
    private Boolean reingreso = false;
    @Column(name = "inscripcion")
    private Integer inscripcion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @Column(name = "diferencia_pago")
    private BigDecimal diferenciaPago = BigDecimal.ZERO;
    @Column(name = "descuento_promedio")
    private BigDecimal descuentoPromedio = BigDecimal.ZERO;

    @Column(name = "cantidad_intervinientes")
    private Integer cantidadIntervinientes;
    @Column(name = "anio_ultima_trasnferencia")
    private Integer anioUltimaTrasnferencia;
    @Column(name = "anio_antecedente_solicitado")
    private Integer anioAntecedenteSolicitado;
    @Column(name = "recargo")
    private BigDecimal recargo = BigDecimal.ZERO;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalle", fetch = FetchType.LAZY)
    private Collection<RegpTareasTramite> regpTareasTramiteCollection;

    @Transient
    private BigDecimal subtotal = BigDecimal.ZERO;
    @Column(name = "base_imponible")
    private BigDecimal base;
    @Transient ///CAMPO UTILIZADO PARA LA EDICION DE LA PROFORMA
    private List<RegpIntervinientes> intervinientes;

    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Column(name = "num_emision")
    private BigInteger numEmision;
    @Column(name = "login_cobro")
    private String loginCobro;

    public RegpLiquidacionDetalles() {
        this.cuantia = BigDecimal.ZERO;
        this.avaluo = BigDecimal.ZERO;
        this.cantidad = 1;

    }

    public RegpLiquidacionDetalles(Long id) {
        this.id = id;
    }

    /*public void calculoTotalConDescuento() {
        if (valorTotal != null && exoneracion != null) {
            //descuento = valorTotal.multiply(exoneracion.getValor());
            //descuento = descuento.setScale(2, RoundingMode.HALF_DOWN); //SE REDONDEA HALF_DOWN PARA DESCONTAR MENOS ;)
            descuento = Utils.bigdecimalTo2Decimals(valorTotal.multiply(exoneracion.getValor()));
            valorTotal = valorTotal.subtract(descuento);
        } else {
            descuento = BigDecimal.ZERO;
        }
    }*/
    public BigDecimal getBaseImponible() {
        if (this.avaluo != null && this.cuantia != null) {
            if (this.avaluo.compareTo(this.cuantia) > 0) {
                return this.avaluo;
            } else {
                return this.cuantia;
            }
        }
        if (this.avaluo == null && this.cuantia != null) {
            return this.cuantia;

        }
        if (this.avaluo != null && this.cuantia == null) {
            return this.avaluo;

        }

        return BigDecimal.ZERO;
    }

    public Integer getPeso() {
        if (this.acto != null) {
            if (this.acto.getPeso() != null) {
                return acto.getPeso() * this.cantidad;
            }
        }
        return 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public Integer getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(Integer numPredio) {
        this.numPredio = numPredio;
    }

    public String getNomUrb() {
        return nomUrb;
    }

    public void setNomUrb(String nomUrb) {
        this.nomUrb = nomUrb;
    }

    public String getMzUrb() {
        return mzUrb;
    }

    public void setMzUrb(String mzUrb) {
        this.mzUrb = mzUrb;
    }

    public String getSlUrb() {
        return slUrb;
    }

    public void setSlUrb(String slUrb) {
        this.slUrb = slUrb;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public RegpExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(RegpExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public Collection<RegpIntervinientes> getRegpIntervinientesCollection() {
        return regpIntervinientesCollection;
    }

    public void setRegpIntervinientesCollection(Collection<RegpIntervinientes> regpIntervinientesCollection) {
        this.regpIntervinientesCollection = regpIntervinientesCollection;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getDiferenciaPago() {
        return diferenciaPago;
    }

    public void setDiferenciaPago(BigDecimal diferenciaPago) {
        this.diferenciaPago = diferenciaPago;
    }

    public Boolean getAplicaDescuento() {
        return aplicaDescuento;
    }

    public void setAplicaDescuento(Boolean aplicaDescuento) {
        this.aplicaDescuento = aplicaDescuento;
    }

    public Boolean getReingreso() {
        return reingreso;
    }

    public void setReingreso(Boolean reingreso) {
        this.reingreso = reingreso;
    }

    public BigDecimal getDescuentoPromedio() {
        return descuentoPromedio;
    }

    public void setDescuentoPromedio(BigDecimal descuentoPromedio) {
        this.descuentoPromedio = descuentoPromedio;
    }

    public Collection<RegpTareasTramite> getRegpTareasTramiteCollection() {
        return regpTareasTramiteCollection;
    }

    public void setRegpTareasTramiteCollection(Collection<RegpTareasTramite> regpTareasTramiteCollection) {
        this.regpTareasTramiteCollection = regpTareasTramiteCollection;
    }

    public Integer getCantidadIntervinientes() {
        return cantidadIntervinientes;
    }

    public void setCantidadIntervinientes(Integer cantidadIntervinientes) {
        this.cantidadIntervinientes = cantidadIntervinientes;
    }

    public Integer getAnioUltimaTrasnferencia() {
        return anioUltimaTrasnferencia;
    }

    public void setAnioUltimaTrasnferencia(Integer anioUltimaTrasnferencia) {
        this.anioUltimaTrasnferencia = anioUltimaTrasnferencia;
    }

    public Integer getAnioAntecedenteSolicitado() {
        return anioAntecedenteSolicitado;
    }

    public void setAnioAntecedenteSolicitado(Integer anioAntecedenteSolicitado) {
        this.anioAntecedenteSolicitado = anioAntecedenteSolicitado;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getBase() {
        base = BigDecimal.ZERO;
        if (avaluo != null && cuantia != null) {
            if (avaluo.compareTo(cuantia) >= 0) {
                base = avaluo;
            } else {
                base = cuantia;
            }
        }
        if (avaluo == null && cuantia != null) {
            base = cuantia;
        }
        if (avaluo != null && cuantia == null) {
            base = avaluo;
        }
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigInteger getNumEmision() {
        return numEmision;
    }

    public void setNumEmision(BigInteger numEmision) {
        this.numEmision = numEmision;
    }

    public String getLoginCobro() {
        return loginCobro;
    }

    public void setLoginCobro(String loginCobro) {
        this.loginCobro = loginCobro;
    }

    public List<RegpIntervinientes> getIntervinientes() {
        return intervinientes;
    }

    public void setIntervinientes(List<RegpIntervinientes> intervinientes) {
        this.intervinientes = intervinientes;
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
        if (!(object instanceof RegpLiquidacionDetalles)) {
            return false;
        }
        RegpLiquidacionDetalles other = (RegpLiquidacionDetalles) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpLiquidacionDetalles[ id=" + id + " ]";
    }

}
