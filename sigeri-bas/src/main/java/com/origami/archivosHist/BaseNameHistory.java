/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.archivosHist;

/**
 *
 * @author dfcalderio
 */
public enum BaseNameHistory {

    DEFAULT(0, 0, 0, "DEFAULT"),
    /*DESARROLLO
    ARCHIVOS1(1, 1871, 2002, "1871-2002", "jdbc:postgresql://localhost:5432/"),
    ARCHIVOS2(2, 2003, 2014, "2003-2015", "jdbc:postgresql://localhost:5432/"),
    ARCHIVOS3(3, 2015, 2018, "2015-2018", "jdbc:postgresql://localhost:5432/"),
    ARCHIVOS4(4, 2019, 2100, "2019", "jdbc:postgresql://localhost:5432/");
    protected String user = "sisapp";
    protected String pass = "sisapp98";*/
    /*PRODUCCION*/
    ARCHIVOS1(1, 1871, 2002, "1871-2002", "jdbc:postgresql://192.168.200.245:5432/"),
    ARCHIVOS2(2, 2003, 2014, "LojaFinal", "jdbc:postgresql://192.168.200.245:5432/"),
    ARCHIVOS3(3, 2015, 2018, "db_Sand", "jdbc:postgresql://192.168.200.245:5432/"),
    ARCHIVOS4(4, 2019, 2100, "db_Sand1", "jdbc:postgresql://192.168.200.245:5432/");
    protected String user = "postgres";
    protected String pass = "regproloja";

    private final int code;
    private final int anio_incio;
    private final int anio_fin;
    private final String descripcion;
    protected String url;

    private BaseNameHistory(int code, int anio_incio, int anio_fin, String descripcion) {
        this.code = code;
        this.anio_incio = anio_incio;
        this.anio_fin = anio_fin;
        this.descripcion = descripcion;
    }

    /**
     *
     * @param code
     * @param anio_incio
     * @param anio_fin
     * @param descripcion nombre de la base
     * @param url Ejemplo: jdbc:postgresql://192.168.200.130:5432/
     */
    private BaseNameHistory(int code, int anio_incio, int anio_fin, String descripcion, String url) {
        this.code = code;
        this.anio_incio = anio_incio;
        this.anio_fin = anio_fin;
        this.descripcion = descripcion;
        this.url = url;
    }

    public int getAnio_incio() {
        return anio_incio;
    }

    public int getAnio_fin() {
        return anio_fin;
    }

    public int getCode() {
        return code;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url + descripcion;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    /**
     * Devuelve el nombre de la base a cual debe conectarse
     *
     * @param anio
     * @return
     */
    public BaseNameHistory byCode(int anio) {
        for (BaseNameHistory value : BaseNameHistory.values()) {
            if (anio >= value.getAnio_incio()) {
                if (anio <= value.getAnio_fin()) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BaseNameHistory{" + "code=" + code + ", anio_incio=" + anio_incio + ", anio_fin=" + anio_fin + ", descripcion=" + descripcion + '}';
    }

}
