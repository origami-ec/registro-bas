/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.services;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jesus
 */
@Local
public interface OrigamiGTService {

    public Object methodGET(String url, Class resultClazz);

    public List methodListGET(String url, Class resultClazz);

    /**
     *
     * @param data el objecto a grabar o a consultar
     * @param url la url a donde e consultará
     * @param resultClass la clase que devuelve el servicio web
     * @return El objeto del webservice
     */
    public Object methodPOST(Object data, String url, Class resultClass);

    /**
     *
     * @param data el objecto a grabar o a consultar
     * @param url la url a donde e consultará
     * @param resultClass la clase que devuelve el servicio web
     * @return listado de objetos del webservice
     */
    public List methodListPOST(Object data, String url, Class resultClass);

    public List methodListPOST(List data, String url, Class resultClass);

    public Object methodPUT(Object data, String url, Class resultClass);

    public List methodListPUT(List data, String url, Class resultClass);

    public Object methodPOST(String url, Class resultClass);

    public List methodListPOST(String url, Class resultClass);

    public String autenticate(String user, String pass);

    public Object methodPOSTwithouAuth(Object data, String url, Class resultClass);

    public Object methodGETwithouAuth(String url, Class resultClazz);

    public Object methodPUTwithouAuth(Object data, String url, Class resultClass);

    /**
     *
     * @param data puede ser cualquier objeto pero en el lado del backend se
     * debe configurar que objeto se recibira
     * @param indice son los datos de la indexacion del documento
     * @param filename el nombre del archivo
     * @param formato el formato del archivo pdf - imagen - etc
     * @param file el archvo en bytes
     * @param url la url para guardar
     * @param resultClass que modelo devolverá
     * @return
     */
    public Object methodUploadFile(Object data, Object indice, String formato, String filename, byte[] file, String url, Class resultClass);
}
