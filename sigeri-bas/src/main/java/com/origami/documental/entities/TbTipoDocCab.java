/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_tipo_doc_cab")
@NamedQueries({
    @NamedQuery(name = "TbTipoDocCab.findAll", query = "SELECT t FROM TbTipoDocCab t")})
public class TbTipoDocCab implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_doc", nullable = false)
    private Integer idTipoDoc;
    @Column(name = "des_tipo_doc", length = 60)
    private String desTipoDoc;
    @Column(name = "usr_creacion")
    private Short usrCreacion;
    @Column(name = "fec_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    private Short tipo;
    @Column(name = "flg_plantilla")
    private Integer flgPlantilla;
    @Lob
    @Column(name = "obj_plantilla")
    private byte[] objPlantilla;
    @Column(name = "mar_coordl")
    private Integer marCoordl;
    @Column(name = "mar_coordt")
    private Integer marCoordt;
    @Column(name = "mar_coordw")
    private Integer marCoordw;
    @Column(name = "mar_coordh")
    private Integer marCoordh;
    @Column(name = "mar_texto", length = 30)
    private String marTexto;
    @Column(name = "mar_icrocr_opc")
    private Integer marIcrocrOpc;
    @Column(name = "mar_coordl_guia")
    private Integer marCoordlGuia;
    @Column(name = "mar_coordt_guia")
    private Integer marCoordtGuia;
    @Column(name = "mar_scrollx")
    private Integer marScrollx;
    @Column(name = "mar_scrolly")
    private Integer marScrolly;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fac_zoom", precision = 15, scale = 3)
    private Double facZoom;
    private Integer dpi;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "idTipoDoc")
    private List<TbTipoDocDet> tbTipoDocDets = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idTipoDoc")
    private List<TbCarpetas> tbCarpetas = new ArrayList<>();

    public TbTipoDocCab() {
    }

    public TbTipoDocCab(Integer idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public Integer getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(Integer idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getDesTipoDoc() {
        return desTipoDoc;
    }

    public void setDesTipoDoc(String desTipoDoc) {
        this.desTipoDoc = desTipoDoc;
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

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public Integer getFlgPlantilla() {
        return flgPlantilla;
    }

    public void setFlgPlantilla(Integer flgPlantilla) {
        this.flgPlantilla = flgPlantilla;
    }

    public byte[] getObjPlantilla() {
        return objPlantilla;
    }

    public void setObjPlantilla(byte[] objPlantilla) {
        this.objPlantilla = objPlantilla;
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

    public String getMarTexto() {
        return marTexto;
    }

    public void setMarTexto(String marTexto) {
        this.marTexto = marTexto;
    }

    public Integer getMarIcrocrOpc() {
        return marIcrocrOpc;
    }

    public void setMarIcrocrOpc(Integer marIcrocrOpc) {
        this.marIcrocrOpc = marIcrocrOpc;
    }

    public Integer getMarCoordlGuia() {
        return marCoordlGuia;
    }

    public void setMarCoordlGuia(Integer marCoordlGuia) {
        this.marCoordlGuia = marCoordlGuia;
    }

    public Integer getMarCoordtGuia() {
        return marCoordtGuia;
    }

    public void setMarCoordtGuia(Integer marCoordtGuia) {
        this.marCoordtGuia = marCoordtGuia;
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

    public Integer getDpi() {
        return dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public List<TbTipoDocDet> getTbTipoDocDets() {
        return tbTipoDocDets;
    }

    public void setTbTipoDocDets(List<TbTipoDocDet> tbTipoDocDets) {
        this.tbTipoDocDets = tbTipoDocDets;
    }

    public List<TbCarpetas> getTbCarpetas() {
        return tbCarpetas;
    }

    public void setTbCarpetas(List<TbCarpetas> tbCarpetas) {
        this.tbCarpetas = tbCarpetas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoDoc != null ? idTipoDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbTipoDocCab)) {
            return false;
        }
        TbTipoDocCab other = (TbTipoDocCab) object;
        if ((this.idTipoDoc == null && other.idTipoDoc != null) || (this.idTipoDoc != null && !this.idTipoDoc.equals(other.idTipoDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbTipoDocCab{" + "idTipoDoc=" + idTipoDoc + ", desTipoDoc=" + desTipoDoc + ", tbTipoDocDets=" + tbTipoDocDets + '}';
    }

    public TbTipoDocDet getFieldDocumental(String namefield) {
        if (Utils.isNotEmpty(tbTipoDocDets)) {
            for (TbTipoDocDet doc : tbTipoDocDets) {
                if (doc.getDesRegistro().equalsIgnoreCase(namefield)) {
                    return doc;
                }
            }
        }
        return null;
    }

    public Long getIdLibro() {
        switch (idTipoDoc) {
            case 1:             //PROPIEDADES
                return 1L;
            case 5:             //HIPOTECAS
                return 2L;
            case 6:             //EMBARGOS
                return 6L;
            case 7:             //SENTENCIAS
                return 3L;
            case 8:             //PROHIBICIONES
                return 5L;
            case 13:             //DEMANDAS
                return 25L;
            case 14:            //SUJETOS MERCANTILES
                return 14L;
            case 15:            //CANCELACION MERCANTIL
                return 15L;
            case 17:            //DISPOSICIONES JUDICIALES
                return 18L;
            case 18:            //PRENDAS INDUSTRIALES
                return 13L;
            case 19:            //COMPRAVENTA CON RESERVA DE DOMINIO
                return 19L;
            case 21:            //PROPIEDAD HORIZONTAL
                return 4L;
            case 23:            //RESOLUCIONES MUNICIPALES
                return 7L;
            case 26:            //ACLARATORIAS
                return 8L;
            case 27:            //CONSTITUCION DE SERVIDUMBRE
                return 10L;
            case 28:            //EXPROPIACIONES MUNICIPALES
                return 12L;
            case 31:            //UTILIDAD PUBLICA
                return 9L;
            case 33:            //CONGREGACIONES RELIGIOSAS
                return 11L;
            case 46:            //REGLAMENTO INTERNO
                return 27L;
            case 47:            //RESCILIACIONES
                return 29L;
            case 50:            //UTILIDAD PUBLICA
                return 9L;
            case 51:            //CESION DE DERECHOS
                return 31L;
            case 52:            //ACTOS Y OBJETOS MERCANTILES
                return 17L;
            case 53:            //DISPOSICIONES JUDICIALES Y ADMINISTRATIVAS
                return 18L;
            default:
                return 0L;
        }
    }

}
