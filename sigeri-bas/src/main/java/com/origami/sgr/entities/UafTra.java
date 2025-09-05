/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "uaf_tra", schema = "ctlg")
@NamedQueries({
    @NamedQuery(name = "UafTra.findAll", query = "SELECT u FROM UafTra u")
    , @NamedQuery(name = "UafTra.findById", query = "SELECT u FROM UafTra u WHERE u.id = :id")
    , @NamedQuery(name = "UafTra.findByMovimiento", query = "SELECT u FROM UafTra u WHERE u.movimiento.id = :idMovimiento AND u.periodo = :periodo")
    , @NamedQuery(name = "UafTra.findByNumRepertorio", query = "SELECT u FROM UafTra u WHERE u.numRepertorio = :numRepertorio")
    , @NamedQuery(name = "UafTra.findByNumInscripcion", query = "SELECT u FROM UafTra u WHERE u.numInscripcion = :numInscripcion")
    , @NamedQuery(name = "UafTra.findByFechaInscripcion", query = "SELECT u FROM UafTra u WHERE u.fechaInscripcion = :fechaInscripcion")
    , @NamedQuery(name = "UafTra.findByPeriodo", query = "SELECT u FROM UafTra u WHERE u.periodo = :periodo AND u.registro = :tipo")
    , @NamedQuery(name = "UafTra.findByNit", query = "SELECT u FROM UafTra u WHERE u.nit = :nit")
    , @NamedQuery(name = "UafTra.findByFcr", query = "SELECT u FROM UafTra u WHERE u.fcr = :fcr")
    , @NamedQuery(name = "UafTra.findByDtm", query = "SELECT u FROM UafTra u WHERE u.dtm = :dtm")
    , @NamedQuery(name = "UafTra.findByCca", query = "SELECT u FROM UafTra u WHERE u.cca = :cca")
    , @NamedQuery(name = "UafTra.findByVcc", query = "SELECT u FROM UafTra u WHERE u.vcc = :vcc")
    , @NamedQuery(name = "UafTra.findByTtb", query = "SELECT u FROM UafTra u WHERE u.ttb = :ttb")
    , @NamedQuery(name = "UafTra.findByDrb", query = "SELECT u FROM UafTra u WHERE u.drb = :drb")})
public class UafTra implements Serializable {

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
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "libro")
    private String libro;
    @Column(name = "acto")
    private String acto;
    @Column(name = "nit")
    private String nit;
    @Column(name = "fcr")
    private String fcr;
    @Column(name = "dtm")
    private String dtm;
    @Column(name = "cca")
    private String cca;
    @Column(name = "vcc")
    private String vcc;
    @Column(name = "ttb")
    private String ttb = "OTR";
    @Column(name = "drb")
    private String drb;
    @Column(name = "seleccionado")
    private boolean seleccionado;
    @Column(name = "registro")
    private boolean registro;
    @JoinColumn(name = "uaf_tramite", referencedColumnName = "id")
    @ManyToOne
    private UafTramite uafTramite;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegMovimiento movimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uafTra")
    private List<UafInt> uafNitList;

    @Transient
    private String codigo;

    public UafTra() {
        this.seleccionado = true;
    }

    public UafTra(Long id) {
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

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getFcr() {
        return fcr;
    }

    public void setFcr(String fcr) {
        this.fcr = fcr;
    }

    public String getDtm() {
        return dtm;
    }

    public void setDtm(String dtm) {
        this.dtm = dtm;
    }

    public String getCca() {
        return cca;
    }

    public void setCca(String cca) {
        this.cca = cca;
    }

    public String getVcc() {
        return vcc;
    }

    public void setVcc(String vcc) {
        this.vcc = vcc;
    }

    public String getTtb() {
        return ttb;
    }

    public void setTtb(String ttb) {
        this.ttb = ttb;
    }

    public String getDrb() {
        return drb;
    }

    public void setDrb(String drb) {
        this.drb = drb;
    }

    public List<UafInt> getUafNitList() {
        return uafNitList;
    }

    public void setUafNitList(List<UafInt> uafNitList) {
        this.uafNitList = uafNitList;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public UafTramite getUafTramite() {
        return uafTramite;
    }

    public void setUafTramite(UafTramite uafTramite) {
        this.uafTramite = uafTramite;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public boolean isRegistro() {
        return registro;
    }

    public void setRegistro(boolean registro) {
        this.registro = registro;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
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
        if (!(object instanceof UafTra)) {
            return false;
        }
        UafTra other = (UafTra) object;
        if (this.movimiento != null && other.getMovimiento() != null) {
            return this.movimiento.getId().equals(other.getMovimiento().getId());

        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UafTra{id=").append(id);
        sb.append(", numRepertorio=").append(numRepertorio);
        sb.append(", numInscripcion=").append(numInscripcion);
        sb.append(", fechaInscripcion=").append(fechaInscripcion);
        sb.append(", periodo=").append(periodo);
        sb.append(", libro=").append(libro);
        sb.append(", acto=").append(acto);
        sb.append(", nit=").append(nit);
        sb.append(", fcr=").append(fcr);
        sb.append(", dtm=").append(dtm);
        sb.append(", cca=").append(cca);
        sb.append(", vcc=").append(vcc);
        sb.append(", ttb=").append(ttb);
        sb.append(", drb=").append(drb);
        sb.append(", seleccionado=").append(seleccionado);
        sb.append(", registro=").append(registro);
        sb.append(", uafTramite=").append(uafTramite);
        sb.append(", movimiento=").append(movimiento);
        sb.append(", uafNitList=").append(uafNitList);
        sb.append(", codigo=").append(codigo);
        sb.append('}');
        return sb.toString();
    }
    
}
