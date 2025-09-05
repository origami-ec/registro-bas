/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

//import com.sun.media.jai.codec.ImageCodec;
//import com.sun.media.jai.codec.ImageDecoder;
//import com.sun.media.jai.codec.TIFFDecodeParam;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
//import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
//import javax.media.jai.NullOpImage;
//import javax.media.jai.OpImage;
//import javax.media.jai.PlanarImage;
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_blob", catalog = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_blob"})})
@NamedQueries({
    @NamedQuery(name = "TbBlob.findAll", query = "SELECT t FROM TbBlob t")})
public class TbBlob implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id_transaccion", nullable = false)
    private Long idTransaccion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_blob", nullable = false)
    private Long id;
    @Column(name = "flg_medio")
    private Short flgMedio;
    @Lob
    @Column(name = "obj_blob")
    private byte[] objBlob;
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
    @Column(name = "size_blob")
    private Integer sizeBlob;
    @Column(name = "ext_blob", length = 3)
    private String extBlob;
    @Column(name = "width_blob")
    private Integer widthBlob;
    @Column(name = "height_blob")
    private Integer heightBlob;
    @Column(name = "file_name", length = 100)
    private String fileName;
    @Basic(optional = false)
    @Column(name = "ord_salida", nullable = false)
    private int ordSalida;
    @Column(name = "can_paginas")
    private Short canPaginas;
    @Column(name = "flg_ocr")
    private Short flgOcr;
    @OneToMany(mappedBy = "idBlob", fetch = FetchType.EAGER)
    private List<TbMarginacion> tbMarginaciones;
    @Transient
    private StreamedContent stream;
    @Transient
    private BufferedImage filter;

    public TbBlob() {
    }

    public TbBlob(Long idBlob) {
        this.id = idBlob;
    }

    public TbBlob(Long idBlob, long idTransaccion, int ordSalida) {
        this.id = idBlob;
        this.idTransaccion = idTransaccion;
        this.ordSalida = ordSalida;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getFlgMedio() {
        return flgMedio;
    }

    public void setFlgMedio(Short flgMedio) {
        this.flgMedio = flgMedio;
    }

    public byte[] getObjBlob() {
        return objBlob;
    }

    public void setObjBlob(byte[] objBlob) {
        this.objBlob = objBlob;
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

    public Integer getSizeBlob() {
        return sizeBlob;
    }

    public void setSizeBlob(Integer sizeBlob) {
        this.sizeBlob = sizeBlob;
    }

    public String getExtBlob() {
        if (extBlob == null || extBlob.isEmpty()) {
            extBlob = "tif";
        }
        return extBlob;
    }

    public void setExtBlob(String extBlob) {
        this.extBlob = extBlob;
    }

    public Integer getWidthBlob() {
        return widthBlob;
    }

    public void setWidthBlob(Integer widthBlob) {
        this.widthBlob = widthBlob;
    }

    public Integer getHeightBlob() {
        return heightBlob;
    }

    public void setHeightBlob(Integer heightBlob) {
        this.heightBlob = heightBlob;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getOrdSalida() {
        return ordSalida;
    }

    public void setOrdSalida(int ordSalida) {
        this.ordSalida = ordSalida;
    }

    public Short getCanPaginas() {
        return canPaginas;
    }

    public void setCanPaginas(Short canPaginas) {
        this.canPaginas = canPaginas;
    }

    public Short getFlgOcr() {
        return flgOcr;
    }

    public void setFlgOcr(Short flgOcr) {
        this.flgOcr = flgOcr;
    }

    public List<TbMarginacion> getTbMarginaciones() {
        return tbMarginaciones;
    }

    public void setTbMarginaciones(List<TbMarginacion> tbMarginaciones) {
        this.tbMarginaciones = tbMarginaciones;
    }

    public StreamedContent getStream() {
        if (stream == null) {
            if (objBlob == null) {
                stream = new DefaultStreamedContent();
            } else {
                /*if (this.getExtBlob().equals("tif")) {
                TIFFDecodeParam param = null;
                ImageDecoder dec = ImageCodec.createImageDecoder("TIFF", new ByteArrayInputStream(this.getObjBlob()), param);
                if (dec == null) {
                stream = new DefaultStreamedContent();
                System.out.println("Decode null");
                } else {
                RenderedImage op = dec.decodeAsRenderedImage(0);
                PlanarImage pi = new NullOpImage(op, null, null, OpImage.OP_IO_BOUND);
                filter = pi.getAsBufferedImage();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(filter, "png", os);
                stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> new ByteArrayInputStream(os.toByteArray())).build();
                }
                } else {
                stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> new ByteArrayInputStream(this.getObjBlob())).build();
                }*/ 
            }
        }
        return stream;
    }

    public BufferedImage getStreamBuffer() {
        if (objBlob != null) {
            /*if (filter == null) {
            TIFFDecodeParam param = null;
            ImageDecoder dec = ImageCodec.createImageDecoder("TIFF", new ByteArrayInputStream(this.getObjBlob()), param);
            if (dec != null) {
            RenderedImage op = dec.decodeAsRenderedImage(0);
            PlanarImage pi = new NullOpImage(op, null, null, OpImage.OP_IO_BOUND);
            return pi.getAsBufferedImage();
            }
            } else {
            return filter;
            }*/ 
        }
        return null;
    }

    /**
     * Permite suaviar la imagen
     *
     * @throws java.io.IOException
     */
    public void suavizarImagen() throws IOException {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
            ninth, ninth, ninth,
            ninth, ninth, ninth,
            ninth, ninth, ninth
        };
        ConvolveOp co = new ConvolveOp(new Kernel(3, 3, blurKernel));
        if (this.getExtBlob().equals("tif")) {
            filter = co.filter(this.getStreamBuffer(), null);
        } else {
            filter = co.filter(ImageIO.read(new ByteArrayInputStream(this.getObjBlob())), null);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(filter, "png", os);
        stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> new ByteArrayInputStream(os.toByteArray())).build();
    }

    public void intensificarImagen() throws IOException {
        float[] blurKernel = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
        };
        ConvolveOp co = new ConvolveOp(new Kernel(3, 3, blurKernel));
        if (this.getExtBlob().equals("tif")) {
            filter = co.filter(this.getStreamBuffer(), null);
        } else {
            filter = co.filter(ImageIO.read(new ByteArrayInputStream(this.getObjBlob())), null);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(filter, "png", os);
        stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> new ByteArrayInputStream(os.toByteArray())).build();
    }

    public String getStreamBase64() {
        if (getObjBlob() == null) {
            return Base64.getEncoder().encodeToString(objBlob);
        }
        return null;
    }

    public String getMarginaciones() {
        StringBuilder buffer = new StringBuilder();
        if (this.tbMarginaciones != null) {
            this.tbMarginaciones.forEach((tm) -> {
                buffer.append(tm.getDescripcion()).append("<br />");
            });
        }
        return buffer.toString();
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
        if (!(object instanceof TbBlob)) {
            return false;
        }
        TbBlob other = (TbBlob) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "TbBlob[ idBlob=" + id + " ]";
    }

}
