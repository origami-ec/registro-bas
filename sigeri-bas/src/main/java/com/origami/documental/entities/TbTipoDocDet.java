/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_tipo_doc_det")
@NamedQueries({
    @NamedQuery(name = "TbTipoDocDet.findAll", query = "SELECT t FROM TbTipoDocDet t")})
public class TbTipoDocDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_registro", nullable = false)
    private Integer idRegistro;
    @JoinColumn(name = "id_tipo_doc", referencedColumnName = "id_tipo_doc")
    @ManyToOne
    private TbTipoDocCab idTipoDoc;
    @Column(name = "des_registro", length = 60)
    private String desRegistro;
    @Column(name = "tipo_registro")
    private Short tipoRegistro;
    @Column(name = "size_registro")
    private Short sizeRegistro;
    @Column(name = "id_relacion", length = 3)
    private String idRelacion;
    @Column(name = "ord_salida")
    private Integer ordSalida;
    @Column(name = "usr_creacion")
    private Short usrCreacion;
    @Column(name = "fec_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "mar_coordl")
    private Integer marCoordl;
    @Column(name = "mar_coordt")
    private Integer marCoordt;
    @Column(name = "mar_coordw")
    private Integer marCoordw;
    @Column(name = "mar_coordh")
    private Integer marCoordh;
    @Column(name = "mar_scrollx")
    private Integer marScrollx;
    @Column(name = "mar_scrolly")
    private Integer marScrolly;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fac_zoom", precision = 15, scale = 3)
    private Double facZoom;
    @Column(name = "mar_coordl_dif")
    private Integer marCoordlDif;
    @Column(name = "mar_coordt_dif")
    private Integer marCoordtDif;
    @Column(name = "num_decimales")
    private Integer numDecimales;
    @Column(name = "tipo_ocricr")
    private Short tipoOcricr;
    @Column(name = "flg_cabecera")
    private Short flgCabecera;
    @Basic(optional = false)
    @Column(name = "flg_serie", nullable = false)
    private short flgSerie;
    @Basic(optional = false)
    @Column(name = "flg_size", nullable = false)
    private short flgSize;
    @Basic(optional = false)
    @Column(name = "flg_nulo", nullable = false)
    private short flgNulo;
    @Basic(optional = false)
    @Column(name = "flg_indexa", nullable = false)
    private short flgIndexa;
    @Column(name = "des_exp", length = 45)
    private String desExp;

    public TbTipoDocDet() {
    }

    public TbTipoDocDet(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public TbTipoDocDet(Integer idRegistro, short flgSerie, short flgSize, short flgNulo, short flgIndexa) {
        this.idRegistro = idRegistro;
        this.flgSerie = flgSerie;
        this.flgSize = flgSize;
        this.flgNulo = flgNulo;
        this.flgIndexa = flgIndexa;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public TbTipoDocCab getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(TbTipoDocCab idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getDesRegistro() {
        return desRegistro;
    }

    public void setDesRegistro(String desRegistro) {
        this.desRegistro = desRegistro;
    }

    public Short getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Short tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Short getSizeRegistro() {
        return sizeRegistro;
    }

    public void setSizeRegistro(Short sizeRegistro) {
        this.sizeRegistro = sizeRegistro;
    }

    public String getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(String idRelacion) {
        this.idRelacion = idRelacion;
    }

    public Integer getOrdSalida() {
        return ordSalida;
    }

    public void setOrdSalida(Integer ordSalida) {
        this.ordSalida = ordSalida;
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

    public Integer getMarCoordl() {
        return marCoordl;
    }

    public void setMarCoordl(Integer marCoordl) {
        this.marCoordl = marCoordl;
    }

    public Integer getMarCoordt() {
        return marCoordt;
    }

    public void setMarCoordt(Integer marCoordt) {
        this.marCoordt = marCoordt;
    }

    public Integer getMarCoordw() {
        return marCoordw;
    }

    public void setMarCoordw(Integer marCoordw) {
        this.marCoordw = marCoordw;
    }

    public Integer getMarCoordh() {
        return marCoordh;
    }

    public void setMarCoordh(Integer marCoordh) {
        this.marCoordh = marCoordh;
    }

    public Integer getMarScrollx() {
        return marScrollx;
    }

    public void setMarScrollx(Integer marScrollx) {
        this.marScrollx = marScrollx;
    }

    public Integer getMarScrolly() {
        return marScrolly;
    }

    public void setMarScrolly(Integer marScrolly) {
        this.marScrolly = marScrolly;
    }

    public Double getFacZoom() {
        return facZoom;
    }

    public void setFacZoom(Double facZoom) {
        this.facZoom = facZoom;
    }

    public Integer getMarCoordlDif() {
        return marCoordlDif;
    }

    public void setMarCoordlDif(Integer marCoordlDif) {
        this.marCoordlDif = marCoordlDif;
    }

    public Integer getMarCoordtDif() {
        return marCoordtDif;
    }

    public void setMarCoordtDif(Integer marCoordtDif) {
        this.marCoordtDif = marCoordtDif;
    }

    public Integer getNumDecimales() {
        return numDecimales;
    }

    public void setNumDecimales(Integer numDecimales) {
        this.numDecimales = numDecimales;
    }

    public Short getTipoOcricr() {
        return tipoOcricr;
    }

    public void setTipoOcricr(Short tipoOcricr) {
        this.tipoOcricr = tipoOcricr;
    }

    public Short getFlgCabecera() {
        return flgCabecera;
    }

    public void setFlgCabecera(Short flgCabecera) {
        this.flgCabecera = flgCabecera;
    }

    public short getFlgSerie() {
        return flgSerie;
    }

    public void setFlgSerie(short flgSerie) {
        this.flgSerie = flgSerie;
    }

    public short getFlgSize() {
        return flgSize;
    }

    public void setFlgSize(short flgSize) {
        this.flgSize = flgSize;
    }

    public short getFlgNulo() {
        return flgNulo;
    }

    public void setFlgNulo(short flgNulo) {
        this.flgNulo = flgNulo;
    }

    public short getFlgIndexa() {
        return flgIndexa;
    }

    public void setFlgIndexa(short flgIndexa) {
        this.flgIndexa = flgIndexa;
    }

    public String getDesExp() {
        return desExp;
    }

    public void setDesExp(String desExp) {
        this.desExp = desExp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegistro != null ? idRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbTipoDocDet)) {
            return false;
        }
        TbTipoDocDet other = (TbTipoDocDet) object;
        if ((this.idRegistro == null && other.idRegistro != null) || (this.idRegistro != null && !this.idRegistro.equals(other.idRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbTipoDocDet{" + "idRegistro=" + idRegistro + ", desRegistro=" + desRegistro + '}';
    }

}
