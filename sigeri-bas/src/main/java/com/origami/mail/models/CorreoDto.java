/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.mail.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CorreoDto implements Serializable {

    private Long id;
    private String destinatario; //SI SE ENVIA A MAS PERSONAS separar por punto y coma
    private String asunto;
    private String mensaje; //html
    private List<CorreoArchivoDto> archivos;
    private Boolean enviado;
    private Date FechaEnvio;
    private boolean tramite;
    private ConfiguracionDto configuracion;
    private Long numeroTramite;
    private String usuario;

    public CorreoDto(String destinatario, String asunto, String mensaje, List<CorreoArchivoDto> archivos) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.archivos = archivos;
    }

    public CorreoDto() {
    }

    /**
     * SI SE ENVIA A MAS PERSONAS separar por punto y coma
     *
     * @return
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * SI SE ENVIA A MAS PERSONAS separar por punto y coma
     *
     * @param destinatario
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<CorreoArchivoDto> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<CorreoArchivoDto> archivos) {
        this.archivos = archivos;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Date getFechaEnvio() {
        return FechaEnvio;
    }

    public void setFechaEnvio(Date FechaEnvio) {
        this.FechaEnvio = FechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTramite() {
        return tramite;
    }

    public void setTramite(boolean tramite) {
        this.tramite = tramite;
    }

    public ConfiguracionDto getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(ConfiguracionDto configuracion) {
        this.configuracion = configuracion;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Correo{" + "destinatario=" + destinatario + ", asunto=" + asunto + ", mensaje=" + mensaje + ", archivos=" + archivos + '}';
    }

}
