/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Arturo
 */
public class AnexoSuperciaDetalleMovimiento implements Serializable{
    private Integer ROW_NUMBER;
    private BigInteger movimiento_id;
    private Integer num_inscripcion;
    private Date fecha_inscripcion;
    private Integer folio_inicio;
    private Integer folio_fin;
    private String num_tomo;
    private Integer num_repertorio;
    private Date fecha_oto;
    private String escrit_juic_prov_resolucion;
    private Date fecha_resolucion;
    private String observacion;
    private String nombre_acto;
    private String nombre_libro;
    private String ente_judicial;
    private String nombre_canton;
    private String avaluo_municipal_cadena;
    private String cuantia_cadena;
    private String intervinientesjs;
    private BigDecimal avaluo_municipal;
    private BigDecimal cuantia;
    
    private List<AnexoCotadDetalleMovimientoPartes>  intervinientes = new ArrayList<>();
    private List<AnexoSuperciaDetalleMovimientoRepresentantes>  representantes = new ArrayList<>();
    private List<AnexoSuperciaDetalleMovimientoSocios>  socios = new ArrayList<>();
    private List<AnexoSuperciaDetalleMovimientoCapitales>  capitales = new ArrayList<>();

    public AnexoSuperciaDetalleMovimiento() {
    }

    public Integer getROW_NUMBER() {
        return ROW_NUMBER;
    }

    public void setROW_NUMBER(Integer ROW_NUMBER) {
        this.ROW_NUMBER = ROW_NUMBER;
    }

    public BigInteger getMovimiento_id() {
        return movimiento_id;
    }

    public void setMovimiento_id(BigInteger movimiento_id) {
        this.movimiento_id = movimiento_id;
    }

    public Integer getNum_inscripcion() {
        return num_inscripcion;
    }

    public void setNum_inscripcion(Integer num_inscripcion) {
        this.num_inscripcion = num_inscripcion;
    }

    public Date getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(Date fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public Integer getFolio_inicio() {
        return folio_inicio;
    }

    public void setFolio_inicio(Integer folio_inicio) {
        this.folio_inicio = folio_inicio;
    }

    public Integer getFolio_fin() {
        return folio_fin;
    }

    public void setFolio_fin(Integer folio_fin) {
        this.folio_fin = folio_fin;
    }

    public String getNum_tomo() {
        return num_tomo;
    }

    public void setNum_tomo(String num_tomo) {
        this.num_tomo = num_tomo;
    }

    public Integer getNum_repertorio() {
        return num_repertorio;
    }

    public void setNum_repertorio(Integer num_repertorio) {
        this.num_repertorio = num_repertorio;
    }

    public Date getFecha_oto() {
        return fecha_oto;
    }

    public void setFecha_oto(Date fecha_oto) {
        this.fecha_oto = fecha_oto;
    }

    public String getEscrit_juic_prov_resolucion() {
        return escrit_juic_prov_resolucion;
    }

    public void setEscrit_juic_prov_resolucion(String escrit_juic_prov_resolucion) {
        this.escrit_juic_prov_resolucion = escrit_juic_prov_resolucion;
    }

    public Date getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(Date fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNombre_acto() {
        return nombre_acto;
    }

    public void setNombre_acto(String nombre_acto) {
        this.nombre_acto = nombre_acto;
    }

    public String getNombre_libro() {
        return nombre_libro;
    }

    public void setNombre_libro(String nombre_libro) {
        this.nombre_libro = nombre_libro;
    }

    public String getEnte_judicial() {
        return ente_judicial;
    }

    public void setEnte_judicial(String ente_judicial) {
        this.ente_judicial = ente_judicial;
    }

    public String getNombre_canton() {
        return nombre_canton;
    }

    public void setNombre_canton(String nombre_canton) {
        this.nombre_canton = nombre_canton;
    }

    public String getAvaluo_municipal_cadena() {
        return avaluo_municipal_cadena;
    }

    public void setAvaluo_municipal_cadena(String avaluo_municipal_cadena) {
        this.avaluo_municipal_cadena = avaluo_municipal_cadena;
    }

    public String getCuantia_cadena() {
        return cuantia_cadena;
    }

    public void setCuantia_cadena(String cuantia_cadena) {
        this.cuantia_cadena = cuantia_cadena;
    }

    public String getIntervinientesjs() {
        return intervinientesjs;
    }

    public void setIntervinientesjs(String intervinientesjs) {
        this.intervinientesjs = intervinientesjs;
    }

    public BigDecimal getAvaluo_municipal() {
        return avaluo_municipal;
    }

    public void setAvaluo_municipal(BigDecimal avaluo_municipal) {
        this.avaluo_municipal = avaluo_municipal;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public List<AnexoCotadDetalleMovimientoPartes> getIntervinientes() {
        return intervinientes;
    }    

    public void setIntervinientes(List<AnexoCotadDetalleMovimientoPartes> intervinientes) {
        this.intervinientes = intervinientes;
    }

    public List<AnexoSuperciaDetalleMovimientoRepresentantes> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<AnexoSuperciaDetalleMovimientoRepresentantes> representantes) {
        this.representantes = representantes;
    }

    public List<AnexoSuperciaDetalleMovimientoSocios> getSocios() {
        return socios;
    }

    public void setSocios(List<AnexoSuperciaDetalleMovimientoSocios> socios) {
        this.socios = socios;
    }

    public List<AnexoSuperciaDetalleMovimientoCapitales> getCapitales() {
        return capitales;
    }

    public void setCapitales(List<AnexoSuperciaDetalleMovimientoCapitales> capitales) {
        this.capitales = capitales;
    }

    @Override
    public String toString() {
        return "AnexoSuperciaDetalleMovimiento{" + "ROW_NUMBER=" + ROW_NUMBER + ", movimiento_id=" + movimiento_id + ", num_inscripcion=" + num_inscripcion + ", fecha_inscripcion=" + fecha_inscripcion + ", folio_inicio=" + folio_inicio + ", folio_fin=" + folio_fin + ", num_tomo=" + num_tomo + ", num_repertorio=" + num_repertorio + ", fecha_oto=" + fecha_oto + ", escrit_juic_prov_resolucion=" + escrit_juic_prov_resolucion + ", fecha_resolucion=" + fecha_resolucion + ", observacion=" + observacion + ", nombre_acto=" + nombre_acto + ", nombre_libro=" + nombre_libro + ", ente_judicial=" + ente_judicial + ", nombre_canton=" + nombre_canton + ", avaluo_municipal_cadena=" + avaluo_municipal_cadena + ", cuantia_cadena=" + cuantia_cadena + ", intervinientesjs=" + intervinientesjs + ", avaluo_municipal=" + avaluo_municipal + ", cuantia=" + cuantia + ", intervinientes=" + intervinientes + ", representantes=" + representantes + ", socios=" + socios + ", capitales=" + capitales + '}';
    }
    
}
