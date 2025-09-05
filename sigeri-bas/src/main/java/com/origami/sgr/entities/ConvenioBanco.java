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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author XndySxnchez
 */
@Entity
@Table(name = "convenio_banco", schema = "flow")
public class ConvenioBanco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institucion", referencedColumnName = "id")
    private RenEntidadBancaria institucion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canal", referencedColumnName = "id")
    private CtlgItem canal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    private RegpLiquidacion liquidacion;
    @Column(name = "id_usuario")
    private String idUsuario;
    @Column(name = "secuencial")
    private String secuencial;
    @Column(name = "valor_deuda")
    private String valorDeuda;
    @Column(name = "id_pago_ins_rec")
    private String idPagoInsRec;
    @Column(name = "fecha_proceso_Ir")
    @Temporal(TemporalType.DATE)
    private Date fechaProcesoIr;
    @Column(name = "fecha_transaccion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTransaccion;
    @Column(name = "id_servicio")
    private String idServicio;
    @Column(name = "data_js")
    private String dataJs;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "factura_anulada")
    private Boolean facturaAnulada;
    @Column(name = "fecha_proceso_reverso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProcesoReverso;
    @Column(name = "data_js_reverso")
    private String dataJsReverso;

    public ConvenioBanco() {
    }

    public ConvenioBanco(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RenEntidadBancaria getInstitucion() {
        return institucion;
    }

    public void setInstitucion(RenEntidadBancaria institucion) {
        this.institucion = institucion;
    }

    public CtlgItem getCanal() {
        return canal;
    }

    public void setCanal(CtlgItem canal) {
        this.canal = canal;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(String valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getIdPagoInsRec() {
        return idPagoInsRec;
    }

    public void setIdPagoInsRec(String idPagoInsRec) {
        this.idPagoInsRec = idPagoInsRec;
    }

    public Date getFechaProcesoIr() {
        return fechaProcesoIr;
    }

    public void setFechaProcesoIr(Date fechaProcesoIr) {
        this.fechaProcesoIr = fechaProcesoIr;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getDataJs() {
        return dataJs;
    }

    public void setDataJs(String dataJs) {
        this.dataJs = dataJs;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getFacturaAnulada() {
        if (facturaAnulada == null) {
            facturaAnulada = false;
        }
        return facturaAnulada;
    }

    public void setFacturaAnulada(Boolean facturaAnulada) {
        this.facturaAnulada = facturaAnulada;
    }

    public Date getFechaProcesoReverso() {
        return fechaProcesoReverso;
    }

    public void setFechaProcesoReverso(Date fechaProcesoReverso) {
        this.fechaProcesoReverso = fechaProcesoReverso;
    }

    public String getDataJsReverso() {
        return dataJsReverso;
    }

    public void setDataJsReverso(String dataJsReverso) {
        this.dataJsReverso = dataJsReverso;
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
        if (!(object instanceof ConvenioBanco)) {
            return false;
        }
        ConvenioBanco other = (ConvenioBanco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConvenioBanco{" + "id=" + id + ", institucion=" + institucion + ", canal=" + canal + ", liquidacion=" + liquidacion + ", facturaAnulada=" + facturaAnulada + ", fechaProcesoReverso=" + fechaProcesoReverso + ", dataJsReverso=" + dataJsReverso + '}';
    }

}
