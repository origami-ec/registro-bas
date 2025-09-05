/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Fernando
 */
@Named
@ApplicationScoped
public class AppConfig {

    public AppConfig() {
    }

    public String getUrlbase() {
        return SisVars.urlbase;
    }

    public String getFacesUrl() {
        return SisVars.facesUrl;
    }

    public String getUrlbaseFaces() {
        return SisVars.urlbaseFaces;
    }

    public String getFormatoArchivos() {
        return SisVars.formatoArchivos;
    }

    /**
     *
     * @param updates
     * @return
     */
    public String update(String updates) {
        if (updates != null && !updates.isEmpty()) {
            return ", " + updates;
        }
        return "";
    }

    /**
     * True
     *
     * @param updates Elementos a actualizar
     * @return Verdadero si los update no esta vacio.
     */
    public Boolean isUpdate(String updates) {
        if (updates != null && !updates.isEmpty()) {
            return true;
        }
        return false;
    }

    public String estadoMovimiento(Integer estado) {
        switch (estado) {
            case 1:
                return "Vendido";
            case 2:
                return "Vendido Parcial";
            case 3:
                return "Cancelado";
            case 4:
                return "Cancelado Parcial";
            case 5:
                return "Vigente";
            case 6:
                return "Propietario";
            default:
                return "No especificado";
        }
    }

    public String estadoColor(Integer estado) {
        switch (estado) {
            case 1:
                return "#EAEDED";
            case 2:
                return "#F9E79F";
            case 3:
                return "#D0ECE7";
            case 4:
                return "#F5CBA7";
            case 5:
                return "#D5F5E3";
            default:
                return "";
        }
    }
}
