/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_data", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_transaccion"})})
@NamedQueries({
    @NamedQuery(name = "TbData.findAll", query = "SELECT t FROM TbData t")})
public class TbData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_transaccion", nullable = false)
    private Long idTransaccion;
    @Basic(optional = false)
    @JoinColumn(name = "id_carpeta", referencedColumnName = "id_carpeta", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private TbCarpetas idCarpeta;
    @Lob
    @Column(length = 65535)
    private String f01;
    @Lob
    @Column(length = 65535)
    private String f13;
    @Lob
    @Column(length = 65535)
    private String f14;
    @Lob
    @Column(length = 65535)
    private String f02;
    @Lob
    @Column(length = 65535)
    private String f03;
    @Lob
    @Column(length = 65535)
    private String f04;
    @Lob
    @Column(length = 65535)
    private String f05;
    @Lob
    @Column(length = 65535)
    private String f06;
    @Lob
    @Column(length = 65535)
    private String f07;
    @Lob
    @Column(length = 65535)
    private String f08;
    @Lob
    @Column(length = 65535)
    private String f10;
    @Lob
    @Column(length = 65535)
    private String f11;
    @Lob
    @Column(length = 65535)
    private String f12;
    @Lob
    @Column(length = 65535)
    private String f15;
    @Lob
    @Column(length = 65535)
    private String f16;
    @Lob
    @Column(length = 65535)
    private String f17;
    @Lob
    @Column(length = 65535)
    private String f18;
    @Lob
    @Column(length = 65535)
    private String f19;
    @Lob
    @Column(length = 65535)
    private String f20;
    @Lob
    @Column(length = 65535)
    private String f21;
    @Lob
    @Column(length = 65535)
    private String f22;
    @Lob
    @Column(length = 65535)
    private String f23;
    @Lob
    @Column(length = 65535)
    private String f24;
    @Lob
    @Column(length = 65535)
    private String f25;
    @Lob
    @Column(length = 65535)
    private String f26;
    @Lob
    @Column(length = 65535)
    private String f27;
    @Lob
    @Column(length = 65535)
    private String f28;
    @Lob
    @Column(length = 65535)
    private String f29;
    @Lob
    @Column(length = 65535)
    private String f30;
    @Lob
    @Column(length = 65535)
    private String f31;
    @Lob
    @Column(length = 65535)
    private String f32;
    @Lob
    @Column(length = 65535)
    private String f33;
    @Lob
    @Column(length = 65535)
    private String f34;
    @Lob
    @Column(length = 65535)
    private String f35;
    @Lob
    @Column(length = 65535)
    private String f36;
    @Lob
    @Column(length = 65535)
    private String f37;
    @Lob
    @Column(length = 65535)
    private String f38;
    @Lob
    @Column(length = 65535)
    private String f39;
    @Lob
    @Column(length = 65535)
    private String f40;
    @Lob
    @Column(length = 65535)
    private String f41;
    @Column(name = "flg_medio")
    private Short flgMedio;
    @Column(name = "can_paginas")
    private Short canPaginas;
    @Column(name = "usr_creacion")
    private Short usrCreacion;
    @Column(name = "fec_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "usr_ultmod")
    private Integer usrUltmod;
    @Column(name = "fec_ultmod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecUltmod;
    @Basic(optional = false)
    @Column(name = "id_libreria", nullable = false)
    private int idLibreria;
    @Column(name = "flg_ocr")
    private Short flgOcr;
    @Column(name = "flg_bloqueado")
    private Short flgBloqueado;
    @Column(name = "flg_estado")
    private Short flgEstado;
    @Column(name = "id_padre")
    private BigInteger idPadre;
    @Column(name = "id_blob_reg")
    private BigInteger idBlobReg;
    @Column(name = "num_caja")
    private Integer numCaja;

    public TbData() {
    }

    public TbData(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public TbData(Long idTransaccion, TbCarpetas idCarpeta, int idLibreria) {
        this.idTransaccion = idTransaccion;
        this.idCarpeta = idCarpeta;
        this.idLibreria = idLibreria;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public TbCarpetas getIdCarpeta() {
        return idCarpeta;
    }

    public void setIdCarpeta(TbCarpetas idCarpeta) {
        this.idCarpeta = idCarpeta;
    }

    public String getF01() {
        return f01;
    }

    public void setF01(String f01) {
        this.f01 = f01;
    }

    public String getF13() {
        return f13;
    }

    public void setF13(String f13) {
        this.f13 = f13;
    }

    public String getF14() {
        return f14;
    }

    public void setF14(String f14) {
        this.f14 = f14;
    }

    public String getF02() {
        return f02;
    }

    public void setF02(String f02) {
        this.f02 = f02;
    }

    public String getF03() {
        return f03;
    }

    public void setF03(String f03) {
        this.f03 = f03;
    }

    public String getF04() {
        return f04;
    }

    public void setF04(String f04) {
        this.f04 = f04;
    }

    public String getF07() {
        return f07;
    }

    public void setF07(String f07) {
        this.f07 = f07;
    }

    public String getF10() {
        return f10;
    }

    public void setF10(String f10) {
        this.f10 = f10;
    }

    public String getF11() {
        return f11;
    }

    public void setF11(String f11) {
        this.f11 = f11;
    }

    public String getF12() {
        return f12;
    }

    public void setF12(String f12) {
        this.f12 = f12;
    }

    public Short getFlgMedio() {
        return flgMedio;
    }

    public void setFlgMedio(Short flgMedio) {
        this.flgMedio = flgMedio;
    }

    public Short getCanPaginas() {
        return canPaginas;
    }

    public void setCanPaginas(Short canPaginas) {
        this.canPaginas = canPaginas;
    }

    public Short getUsrCreacion() {
        return usrCreacion;
    }

    public void setUsrCreacion(Short usrCreacion) {
        this.usrCreacion = usrCreacion;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Integer getUsrUltmod() {
        return usrUltmod;
    }

    public void setUsrUltmod(Integer usrUltmod) {
        this.usrUltmod = usrUltmod;
    }

    public Date getFecUltmod() {
        return fecUltmod;
    }

    public void setFecUltmod(Date fecUltmod) {
        this.fecUltmod = fecUltmod;
    }

    public int getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(int idLibreria) {
        this.idLibreria = idLibreria;
    }

    public Short getFlgOcr() {
        return flgOcr;
    }

    public void setFlgOcr(Short flgOcr) {
        this.flgOcr = flgOcr;
    }

    public Short getFlgBloqueado() {
        return flgBloqueado;
    }

    public void setFlgBloqueado(Short flgBloqueado) {
        this.flgBloqueado = flgBloqueado;
    }

    public Short getFlgEstado() {
        return flgEstado;
    }

    public void setFlgEstado(Short flgEstado) {
        this.flgEstado = flgEstado;
    }

    public BigInteger getIdPadre() {
        if (idPadre == null) {
            idPadre = BigInteger.ZERO;
        }
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

    public Integer getNumCaja() {
        return numCaja;
    }

    public void setNumCaja(Integer numCaja) {
        this.numCaja = numCaja;
    }

    public String getF29() {
        return f29;
    }

    public void setF29(String f29) {
        this.f29 = f29;
    }

    public String getF37() {
        return f37;
    }

    public void setF37(String f37) {
        this.f37 = f37;
    }

    public String getF19() {
        return f19;
    }

    public void setF19(String f19) {
        this.f19 = f19;
    }

    public String getF21() {
        return f21;
    }

    public void setF21(String f21) {
        this.f21 = f21;
    }

    public String getF27() {
        return f27;
    }

    public void setF27(String f27) {
        this.f27 = f27;
    }

    public String getF22() {
        return f22;
    }

    public void setF22(String f22) {
        this.f22 = f22;
    }

    public String getF23() {
        return f23;
    }

    public void setF23(String f23) {
        this.f23 = f23;
    }

    public String getF24() {
        return f24;
    }

    public void setF24(String f24) {
        this.f24 = f24;
    }

    public String getF25() {
        return f25;
    }

    public void setF25(String f25) {
        this.f25 = f25;
    }

    public String getF35() {
        return f35;
    }

    public void setF35(String f35) {
        this.f35 = f35;
    }

    public String getF36() {
        return f36;
    }

    public void setF36(String f36) {
        this.f36 = f36;
    }

    public String getF15() {
        return f15;
    }

    public void setF15(String f15) {
        this.f15 = f15;
    }

    public String getF16() {
        return f16;
    }

    public void setF16(String f16) {
        this.f16 = f16;
    }

    public String getF28() {
        return f28;
    }

    public void setF28(String f28) {
        this.f28 = f28;
    }

    public String getF30() {
        return f30;
    }

    public void setF30(String f30) {
        this.f30 = f30;
    }

    public String getF31() {
        return f31;
    }

    public void setF31(String f31) {
        this.f31 = f31;
    }

    public String getF39() {
        return f39;
    }

    public void setF39(String f39) {
        this.f39 = f39;
    }

    public String getF40() {
        return f40;
    }

    public void setF40(String f40) {
        this.f40 = f40;
    }

    public String getF17() {
        return f17;
    }

    public void setF17(String f17) {
        this.f17 = f17;
    }

    public String getF18() {
        return f18;
    }

    public void setF18(String f18) {
        this.f18 = f18;
    }

    public String getF33() {
        return f33;
    }

    public void setF33(String f33) {
        this.f33 = f33;
    }

    public String getF20() {
        return f20;
    }

    public void setF20(String f20) {
        this.f20 = f20;
    }

    public String getF05() {
        return f05;
    }

    public void setF05(String f05) {
        this.f05 = f05;
    }

    public String getF06() {
        return f06;
    }

    public void setF06(String f06) {
        this.f06 = f06;
    }

    public String getF32() {
        return f32;
    }

    public void setF32(String f32) {
        this.f32 = f32;
    }

    public String getF34() {
        return f34;
    }

    public void setF34(String f34) {
        this.f34 = f34;
    }

    public String getF38() {
        return f38;
    }

    public void setF38(String f38) {
        this.f38 = f38;
    }

    public String getF41() {
        return f41;
    }

    public void setF41(String f41) {
        this.f41 = f41;
    }

    public String getF26() {
        return f26;
    }

    public void setF26(String f26) {
        this.f26 = f26;
    }

    public String getF08() {
        return f08;
    }

    public void setF08(String f08) {
        this.f08 = f08;
    }

    public String getParroquia() {
        if (f27 != null && !f27.isEmpty()) {
            return "Parroquia: " + f27 + "\n";
        } else {
            return "";
        }
    }

    public String getBarrio() {
        if (f21 != null && !f21.isEmpty()) {
            return "Barrio: " + f21 + "\n";
        } else {
            return "";
        }
    }

    public String getUbicacion() {
        return "Calle Principal: " + f22
                + "Calle Secundaria: " + f23
                + "Mz: " + f24
                + "Lote: " + f25 + "\n";
    }
    
    public String getClaveCatastral() {
        if (f12 != null && !f12.isEmpty()) {
            return "Clave Catastral: " + f12 + "\n";
        } else {
            return "";
        }
    }

    public String getLinderos() {
        if (f19 != null && !f19.isEmpty()) {
            return "Linderos: " + f19 + "\n";
        } else {
            return "";
        }
    }

    public Long getIdNotaria() {
        if (f35 != null && !f35.isEmpty()) {
            if (f35.contains("PRIMERA")) {
                return 209L;
            }
            if (f35.contains("SEGUNDA")) {
                return 210L;
            }
            if (f35.contains("TERCERA")) {
                return 211L;
            }
            if (f35.contains("CUARTA")) {
                return 212L;
            }
            if (f35.contains("QUINTA")) {
                return 213L;
            }
            if (f35.contains("SEXTA")) {
                return 214L;
            }
        }
        return 0L;
    }

    public Long getIdCanton() {
        if (f36 != null && !f36.isEmpty()) {
            if (f36.contains("LATACUNGA")) {
                return 39L; 
            }
            if (f36.contains("QUITO")) {
                return 73L; 
            }
            if (f36.contains("SAQUISIL")) {
                return 174L; 
            }
            if (f36.contains("GUAYAQUIL")) {
                return 34L; 
            }
            if (f36.contains("SALCEDO")) {
                return 77L; 
            }
            if (f36.contains("AMBATO")) {
                return 3L; 
            }
        }
        return 0L;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaccion != null ? idTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbData)) {
            return false;
        }
        TbData other = (TbData) object;
        return !((this.idTransaccion == null && other.idTransaccion != null) || (this.idTransaccion != null && !this.idTransaccion.equals(other.idTransaccion)));
    }

    @Override
    public String toString() {
        return "TbData{" + "idTransaccion=" + idTransaccion + ", idCarpeta=" + idCarpeta + ", canPaginas=" + canPaginas + ", idPadre=" + idPadre + ", idBlobReg=" + idBlobReg + '}';
    }

}
