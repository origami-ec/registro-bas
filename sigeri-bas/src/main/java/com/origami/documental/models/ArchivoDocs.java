/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author eduar
 */
public class ArchivoDocs implements Serializable {
    
    private String id;
    private String nombre;
    private String tramite;
    private String numTramite;
    private String ruta;
    private String rutaNotas;
    private String fecha;
    private Integer numeroPaginas;
    private String peso;
    private String tipo;
    private String formato;
    private UsuarioDocs usuario;
    private UsuarioDocs usuarioModifica;
    private List<Imagen> imagenes;
    private String rutaConvertido;

    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private List<IndexacionCampo> detalles;
    private String convertir;

    public ArchivoDocs() {
    }
    
    public ArchivoDocs(String id) {
        this.id = id;
    }

    public ArchivoDocs(String id, String convertir) {
        this.id = id;
        this.convertir = convertir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRutaNotas() {
        return rutaNotas;
    }

    public void setRutaNotas(String rutaNotas) {
        this.rutaNotas = rutaNotas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getRutaConvertido() {
        return rutaConvertido;
    }

    public void setRutaConvertido(String rutaConvertido) {
        this.rutaConvertido = rutaConvertido;
    }

    public String getTipoIndexacion() {
        return tipoIndexacion;
    }

    public void setTipoIndexacion(String tipoIndexacion) {
        this.tipoIndexacion = tipoIndexacion;
    }

    public String getDetalleDocumento() {
        return detalleDocumento;
    }

    public void setDetalleDocumento(String detalleDocumento) {
        this.detalleDocumento = detalleDocumento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getConvertir() {
        return convertir;
    }

    public void setConvertir(String convertir) {
        this.convertir = convertir;
    }

    public UsuarioDocs getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDocs usuario) {
        this.usuario = usuario;
    }

    public UsuarioDocs getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(UsuarioDocs usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public List<IndexacionCampo> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<IndexacionCampo> detalles) {
        this.detalles = detalles;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
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
        final ArchivoDocs other = (ArchivoDocs) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArchivoDocs{");
        sb.append("id=").append(id);
        sb.append(", nombre=").append(nombre);
        sb.append(", tramite=").append(tramite);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", ruta=").append(ruta);
        sb.append(", rutaNotas=").append(rutaNotas);
        sb.append(", fecha=").append(fecha);
        sb.append(", numeroPaginas=").append(numeroPaginas);
        sb.append(", peso=").append(peso);
        sb.append(", tipo=").append(tipo);
        sb.append(", formato=").append(formato);
        sb.append(", usuario=").append(usuario);
        sb.append(", usuarioModifica=").append(usuarioModifica);
        sb.append(", imagenes=").append(imagenes);
        sb.append(", rutaConvertido=").append(rutaConvertido);
        sb.append(", tipoIndexacion=").append(tipoIndexacion);
        sb.append(", detalleDocumento=").append(detalleDocumento);
        sb.append(", estado=").append(estado);
        sb.append(", detalles=").append(detalles);
        sb.append(", convertir=").append(convertir);
        sb.append('}');
        return sb.toString();
    }

}
