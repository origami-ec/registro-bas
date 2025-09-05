/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ren_pago_detalle", schema = "financiero")
@NamedQueries({
    @NamedQuery(name = "RenPagoDetalle.findAll", query = "SELECT r FROM RenPagoDetalle r")})
public class RenPagoDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_pago", nullable = false)
    private long tipoPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;
    @JoinColumn(name = "tc_banco", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenEntidadBancaria tcBanco;
    @Size(max = 80)
    @Column(name = "tc_num_tarjeta", length = 80)
    private String tcNumTarjeta;
    @Column(name = "tc_fecha_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tcFechaCaducidad;
    @Size(max = 100)
    @Column(name = "tc_autorizacion", length = 100)
    private String tcAutorizacion;
    @Size(max = 60)
    @Column(name = "tc_baucher", length = 60)
    private String tcBaucher;
    @Size(max = 100)
    @Column(name = "tc_titular", length = 100)
    private String tcTitular;
    @Size(max = 50)
    @Column(name = "nc_num_credito", length = 50)
    private String ncNumCredito;
    @Column(name = "nc_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ncFecha;
    @JoinColumn(name = "ch_banco", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenEntidadBancaria chBanco;
    @Size(max = 50)
    @Column(name = "ch_num_cheque", length = 50)
    private String chNumCheque;
    @Size(max = 50)
    @Column(name = "ch_num_cuenta", length = 50)
    private String chNumCuenta;
    @JoinColumn(name = "tr_banco", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenEntidadBancaria trBanco;
    @Size(max = 50)
    @Column(name = "tr_num_transferencia", length = 50)
    private String trNumTransferencia;
    @Column(name = "tr_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trFecha;
    @JoinColumn(name = "banco", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenEntidadBancaria banco;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenPago pago;
    @Column(name = "ch_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chFecha;

    public RenPagoDetalle() {
    }

    public RenPagoDetalle(Long id) {
        this.id = id;
    }

    public RenPagoDetalle(Long id, long tipoPago) {
        this.id = id;
        this.tipoPago = tipoPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(long tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTcNumTarjeta() {
        return tcNumTarjeta;
    }

    public void setTcNumTarjeta(String tcNumTarjeta) {
        this.tcNumTarjeta = tcNumTarjeta;
    }

    public Date getTcFechaCaducidad() {
        return tcFechaCaducidad;
    }

    public void setTcFechaCaducidad(Date tcFechaCaducidad) {
        this.tcFechaCaducidad = tcFechaCaducidad;
    }

    public String getTcAutorizacion() {
        return tcAutorizacion;
    }

    public void setTcAutorizacion(String tcAutorizacion) {
        this.tcAutorizacion = tcAutorizacion;
    }

    public String getTcBaucher() {
        return tcBaucher;
    }

    public void setTcBaucher(String tcBaucher) {
        this.tcBaucher = tcBaucher;
    }

    public String getTcTitular() {
        return tcTitular;
    }

    public void setTcTitular(String tcTitular) {
        this.tcTitular = tcTitular;
    }

    public String getNcNumCredito() {
        return ncNumCredito;
    }

    public void setNcNumCredito(String ncNumCredito) {
        this.ncNumCredito = ncNumCredito;
    }

    public Date getNcFecha() {
        return ncFecha;
    }

    public void setNcFecha(Date ncFecha) {
        this.ncFecha = ncFecha;
    }
    
    public String getChNumCheque() {
        return chNumCheque;
    }

    public void setChNumCheque(String chNumCheque) {
        this.chNumCheque = chNumCheque;
    }

    public String getChNumCuenta() {
        return chNumCuenta;
    }

    public void setChNumCuenta(String chNumCuenta) {
        this.chNumCuenta = chNumCuenta;
    }

    public String getTrNumTransferencia() {
        return trNumTransferencia;
    }

    public void setTrNumTransferencia(String trNumTransferencia) {
        this.trNumTransferencia = trNumTransferencia;
    }

    public Date getTrFecha() {
        return trFecha;
    }

    public void setTrFecha(Date trFecha) {
        this.trFecha = trFecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public RenPago getPago() {
        return pago;
    }

    public void setPago(RenPago pago) {
        this.pago = pago;
    }

    public RenEntidadBancaria getTcBanco() {
        return tcBanco;
    }

    public void setTcBanco(RenEntidadBancaria tcBanco) {
        this.tcBanco = tcBanco;
    }

    public RenEntidadBancaria getChBanco() {
        return chBanco;
    }

    public void setChBanco(RenEntidadBancaria chBanco) {
        this.chBanco = chBanco;
    }

    public RenEntidadBancaria getTrBanco() {
        return trBanco;
    }

    public void setTrBanco(RenEntidadBancaria trBanco) {
        this.trBanco = trBanco;
    }

    public RenEntidadBancaria getBanco() {
        return banco;
    }

    public void setBanco(RenEntidadBancaria banco) {
        this.banco = banco;
    }

    public Date getChFecha() {
        return chFecha;
    }

    public void setChFecha(Date chFecha) {
        this.chFecha = chFecha;
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
        if (!(object instanceof RenPagoDetalle)) {
            return false;
        }
        RenPagoDetalle other = (RenPagoDetalle) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RenPagoDetalle{");
        sb.append("id=").append(id);
        sb.append(", tipoPago=").append(tipoPago);
        sb.append(", valor=").append(valor);
        sb.append(", tcBanco=").append(tcBanco);
        sb.append(", tcNumTarjeta=").append(tcNumTarjeta);
        sb.append(", tcFechaCaducidad=").append(tcFechaCaducidad);
        sb.append(", tcAutorizacion=").append(tcAutorizacion);
        sb.append(", tcBaucher=").append(tcBaucher);
        sb.append(", tcTitular=").append(tcTitular);
        sb.append(", ncNumCredito=").append(ncNumCredito);
        sb.append(", ncFecha=").append(ncFecha);
        sb.append(", chBanco=").append(chBanco);
        sb.append(", chNumCheque=").append(chNumCheque);
        sb.append(", chNumCuenta=").append(chNumCuenta);
        sb.append(", trBanco=").append(trBanco);
        sb.append(", trNumTransferencia=").append(trNumTransferencia);
        sb.append(", trFecha=").append(trFecha);
        sb.append(", banco=").append(banco);
        sb.append(", estado=").append(estado);
        sb.append(", pago=").append(pago);
        sb.append(", chFecha=").append(chFecha);
        sb.append('}');
        return sb.toString();
    }

}
