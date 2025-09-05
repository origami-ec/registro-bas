/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.config;

/**
 *
 * @author Origami
 */
public class SisVars {

    public static String urlbase;
    public static String facesUrl = "/faces";
    public static String urlbaseFaces;
    public static String ejbRuta;
    public static String formatoArchivos;// ="/(\\.|\\/)(gif|jpe?g|png|pdf|xlsx|docx|xlsm|dwg|shp|doc|xls|ppt|pptx|tif|txt)$/";
    public static String rutaAnexos;
    public static String rutaTemporales;
    public static String rutaFirmados;
    public static String rutaFirmaEC; //C:\facturacion-electronica\firmas-electronicas
    public static String rutaTitulos;
    public static String rutaDevolutivas;
    
    public static String smtp_Host;
    public static String smtp_Port;
    public static String correo;
    public static String pass;
    public static boolean ssl;

    //CONEXION BASE
    public static String driverClass;
    public static String url;
    public static String userName;
    public static String password;
    public static int minPoolSize = 1;
    public static int maxPoolSize = 1;
    public static int maxIdleTime = 1;

    //CONEXION BASE DOCUMENTAL
    public static String docDriverClass;
    public static String docUrl;
    public static String docUserName;
    public static String docPassword;
    public static int docMinPoolSize = 1;
    public static int docMaxPoolSize = 1;
    public static int docMaxIdleTime = 1;

    //CONEXION BASE ACTIVITI
    public static String actdriverClass;
    public static String acturl;
    public static String actuserName;
    public static String actpassword;
    public static int actminPoolSize = 1;
    public static int actmaxPoolSize = 1;
    public static int actmaxIdleTime = 1;

    //JNDI EJB
    public static String entityManager;
    public static String bpmBaseEngine;
    public static String bpmProcessEngine;
    public static String documents;

    //zoning
    public static String region;

    //webservices
    public static String dominioVentanilla;
    public static String urlWSVentanilla;
    public static String wsdlMunicipio;
    public static String urlWsFacturacion;
    public static String urlFirmaEC;
    public static String urlSms;
    public static String urlWSPublico;
    public static String urlWsDinardap;
    public static String urlWsCatastro;
    public static String urlWsEmail;
    public static String urlOrigamiZuul;
    public static String urlOrigamiDocs;
    public static String urlOrigamiMedia;
    public static String ambienteFacturacion;
    public static boolean isOnline;

    //COMPROBANTES DE SRI
    public static String FACTURA = "01";
    public static String NOTACREDITO = "04";
    public static String NOTADEBITO = "05";
    public static String GUIAREMISION = "06";
    public static String COMPPROBANTERETENCION = "07";
    public static boolean enviar;

    //VENTANILLA PUBLICA
    public static String userVentanillaSgr = "ventanilla";
    public static String passVentanillaSgr = "12345678";
    public static String userVentanillaREST = "v3nt2n1ll4";
    public static String passVentanillaREST = "4s7dSePw7J0BLFPjgHl";
    public static String userVentanilla = "ONLINE";

    public static String TIPO_REQUISITO = "REQUISITO";
    public static String TIPO_REQUISITO_NOTIFICAR = "REQUISITO_NOTIFICA";
    public static String TIPO_REQUISITO_NOTIFICADO = "REQUISITO_NOTIFICADO";
    
    //GESTOR DOCUMENTAL
    public static String notaAgregar = "A";
    public static String notaEliminar = "E";
    public static String VIEW_DOC = "VIEW";
    public static String DOWNLOAD_DOC = "DOWNLOAD";
    public final static String NOTA = "NOTA";
    public final static String LINEA = "LINEA";
    public final static String REMARCADO = "REMARCADO";
    public final static String SELLO = "SELLOS";
    public static Integer maxAnchoNota = 200;
    public static Integer maxAltoNota = 100;
    public static Integer maxAltoLinea = 10;
    public static Integer maxAltoRemarcado = 50;
    
}
