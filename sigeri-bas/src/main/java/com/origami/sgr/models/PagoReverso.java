/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;

/**
 *
 * @author ANGEL NAVARRO
 */
public class PagoReverso implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String idServicio;
    private String idInstitucion;
    private String idCanal;
    private String idUsuario;
    private String idDeuda;
    private String valorDeuda;
    private String fechaProcesoIr;
    private String fechaTransaccion;
    private String estado;
    private String observacionPago;

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(String idDeuda) {
        this.idDeuda = idDeuda;
    }

    public String getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(String valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getFechaProcesoIr() {
        return fechaProcesoIr;
    }

    public void setFechaProcesoIr(String fechaProcesoIr) {
        this.fechaProcesoIr = fechaProcesoIr;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionPago() {
        return observacionPago;
    }

    public void setObservacionPago(String observacionPago) {
        this.observacionPago = observacionPago;
    }

    @Override
    public String toString() {
        return "PagoReverso [idServicio=" + idServicio + ", idInstitucion=" + idInstitucion + ", idCanal=" + idCanal
                + ", idUsuario=" + idUsuario + ", idDeuda=" + idDeuda + ", valorDeuda=" + valorDeuda
                + ", fechaProcesoIr=" + fechaProcesoIr + ", fechaTransaccion=" + fechaTransaccion + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCanal == null) ? 0 : idCanal.hashCode());
        result = prime * result + ((idDeuda == null) ? 0 : idDeuda.hashCode());
        result = prime * result + ((idInstitucion == null) ? 0 : idInstitucion.hashCode());
        result = prime * result + ((idServicio == null) ? 0 : idServicio.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PagoReverso other = (PagoReverso) obj;
        if (idCanal == null) {
            if (other.idCanal != null) {
                return false;
            }
        } else if (!idCanal.equals(other.idCanal)) {
            return false;
        }
        if (idDeuda == null) {
            if (other.idDeuda != null) {
                return false;
            }
        } else if (!idDeuda.equals(other.idDeuda)) {
            return false;
        }
        if (idInstitucion == null) {
            if (other.idInstitucion != null) {
                return false;
            }
        } else if (!idInstitucion.equals(other.idInstitucion)) {
            return false;
        }
        if (idServicio == null) {
            if (other.idServicio != null) {
                return false;
            }
        } else if (!idServicio.equals(other.idServicio)) {
            return false;
        }
        return true;
    }

}
