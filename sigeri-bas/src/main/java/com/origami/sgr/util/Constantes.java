/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

/**
 *
 * @author Anyelo
 */
public class Constantes {

    public static String nombreRegistro = "EPMRP-SD Empresa Pública Municipal Registro de la Propiedad";
    public static String direcionPorDefectoAnexoSRI = "CANTON SANTO DOMINGO";
    public static String fechaInicioInscripciones = "01-09-2022";
    public static String nombreCiudad = "Santo Domingo";

    public static String salidaReportes = "<!DOCTYPE html>\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head id=\"j_id_3\">\n"
            + "     <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n"
            + "     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
            + "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" />\n"
            + "     <meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
            + "     <link type=\"text/css\" rel=\"stylesheet\" href=\"/sgr-std/javax.faces.resource/theme.css.xhtml?ln=primefaces-paradise-blue\" />"
            + "     <link type=\"text/css\" rel=\"stylesheet\" href=\"/sgr-std/javax.faces.resource/fa/font-awesome.css.xhtml?ln=primefaces&amp;v=11.0\" />"
            + "     <link rel=\"stylesheet\" type=\"text/css\" href=\"/sgr-std/javax.faces.resource/components.css.xhtml?ln=primefaces&amp;v=11.0\" />"
            + "     <script type=\"text/javascript\" src=\"/sgr-std/javax.faces.resource/jquery/jquery.js.xhtml?ln=primefaces&amp;v=11.0\">"
            + "     </script><script type=\"text/javascript\" src=\"/ssgr-std/javax.faces.resource/core.js.xhtml?ln=primefaces&amp;v=11.0\">"
            + "     </script><script type=\"text/javascript\" src=\"/sgr-std/javax.faces.resource/components.js.xhtml?ln=primefaces&amp;v=11.0\">"
            + "     </script><link rel=\"stylesheet\" type=\"text/css\" href=\"/sgr-std/javax.faces.resource/css/layout-blue.css.xhtml?ln=paradise-layout\" />"
            + "     <script type=\"text/javascript\">if(window.PrimeFaces){PrimeFaces.settings.locale='es_EC';PrimeFaces.settings.legacyWidgetNamespace=true;PrimeFaces.settings.projectStage='Development';}</script>\n"
            + "     <title>Documento</title>"
            + "</head>"
            + "<body class=\"exception-body\">\n"
            + "     <div class=\"exception-panel\"><img style=\"width: 256px !important;\" id=\"j_id_8\" src=\"/sgr-std/javax.faces.resource/images/pdf.svg.xhtml?ln=paradise-layout\" alt=\"\" />\n"
            + "            <div class=\"line\"></div>\n"
            + "            <h1>Documento no encontrado</h1>\n"
            + "            <p>Documento no se ecuentra disponible, contacte al administrador.</p>"
            + "            <button id=\"j_id_a\" name=\"j_id_a\" type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"window.open('\\/sgr-std\\/procesos\\/dashBoard.xhtml?dswid=6290','_self')\"><span class=\"ui-button-text ui-c\">INICIO</span></button><script id=\"j_id_a_s\" type=\"text/javascript\">$(function(){PrimeFaces.cw(\"Button\",\"widget_j_id_a\",{id:\"j_id_a\"});});</script>\n"
            + "     </div>"
            + "</body>\n"
            + "</html>";

    /* SECUENCIAS GENERALES*/
    public static String secuenciaObservacion = "OBSERVACIONES";
    public static String secuenciaNumeroFicha = "NUMERO_FICHA";
    public static String secuenciaFichaMueble = "NUMERO_FICHA_MUEBLE";
    public static String secuenciaFichaCompania = "NUMERO_FICHA_COMPANIA";
    public static String secuenciaFichaPersonal = "NUMERO_FICHA_PERSONAL";
    public static String secuenciaNumeroOrden = "NUMERO_ORDEN_FICHA";
    public static String secuenciaEnte = "CAT_ENTE";
    public static String secuenciaInterviniente = "REG_ENTE_INTERVINIENTE";
    public static String secuenciaTramite = "NUMERO_TRAMITE";
    public static String secuenciaFactura = "NUMERO_FACTURA";
    public static String secuenciaRepertorioSolvencia = "SECUENCIA_REPERTORIO_SOLVENCIA";
    public static String secuenciaPropiedad = "ORDEN_PAGO_PROPIEDAD";
    public static String secuenciaMercantil = "ORDEN_PAGO_MERCANTIL";
    public static String secuenciaTomoPropiedad = "TOMO_REPERTORIO_PROPIEDAD";
    public static String secuenciaTomoMercantil = "TOMO_REPERTORIO_MERCANTIL";

    public static String secuenciaNotaDevolutiva = "NUMERO_NOTA_DEVOLUTIVA";

    /* CONTENIDO DE REPORTES */
    public static String piePaginaProforma = "PIE_PAGINA_PROFORMA";
    public static String piePaginaComprobante = "PIE_PAGINA_COMPROBANTE_PAGO";
    public static String contenidoBaseNotaDev = "NOTA_DEVOLUTIVA";
    public static String contenidoBaseNotaDevCer = "NOTA_DEVOLUTIVA_DEVOLUTIVA";

    /* NOMBRES DE CATALOGOS */
    public static String estadoFicha = "ficha.estado";
    public static String estadosCivil = "registro.estado_civil";
    public static String cargosRepresenante = "registro.cargo_representante";
    public static String codigosTiempo = "registro.codigo_tiempo";
    public static String usosDocumento = "proforma.uso_documento";
    public static String estudiosJuridicos = "proforma.estudio_juridico";

    /*VARIABLES DE CONFIGURACION*/
    public static String limiteFactura = "LIMITE_VALOR_FACTURA";
    public static String datosFacturaElectronica = "FACTURA_ELECTRONICA";
    public static String diasValidezProforma = "DIAS_VALIDEZ_PROFORMA";
    public static String diasLimiteTramite = "DIAS_PERMITIDOS_TRAMITES";
    public static String diasMaxLimiteTramite = "DIAS_MAXIMO_PERMITIDOS_TRAMITES";
    public static String cantidadTramitesCert = "MAX_CANTIDAD_TRAMITES_CERT";
    public static String cantidadTramitesInsc = "MAX_CANTIDAD_TRAMITES_INSC";
    public static String diasTramiteJudicial = "DIAS_TRAMITE_ORDEN_JUDICIAL";
    public static String codigoRegistroUafe = "CODIGO_REGISTRO_UAFE";
    public static String diasTramiteReingreso = "DIAS_TRAMITE_REINGRESO";
    public static String diasCertificadoNoPoseerBienes = "CERTIFICADO_NO_POSEE_BIENES";
    public static String diasSolicitudInscripcionEnlinea = "SOLICITUD_INSCRIPCION_EN_LINEA";
    public static String valorExtraPorLote = "VALOR_EXTRA_POR_CADA_LOTE";
    public static String cantidadCertificadosPH = "CANTIDAD_CERTIFICADOS_CONTRATO_PH";
    public static String limiteCuantiaHipotecas = "LIMITE_CUANTIA_HIPOTECAS";
    public static String limiteFacturaHipotecas = "LIMITE_FACTURA_CON_LIMITE_HIPOTECA";
    public static String descuentoLimiteHipotecas = "DESCUENTO_VALOR_MAYOR_LIMITE_HIPOTECA";
    public static String diasTramitesFicha = "TRAMITES_FICHA";
    public static String diasCaducidadPass = "DIAS_CADUCIDAD_PASS";
    public static String diasValidezRepertorio = "DIAS_VALIDEZ_REPERTORIO";
    public static String fechaInicioActividades = "FECHA_INICIO_ACTIVIDADES";
    public static String limiteValorContrato = "LIMITE_VALOR_CONTRATO";
    public static String salarioBasicoUnificado = "SALARIO_BASICO_UNIFICADO";
    public static String gastosGenerales = "GASTOS_GENERALES";
    public static String adicionalCertificados = "VALOR_ADICIONAL_CERTIFICADOS";
    public static String smsUrlToken = "SMS_URL";
    public static String smsEstado = "SMS_ESTADO";
    public static String tiempoEsperaSolicitudLinea = "SOLICITUD_INSCRIPCION_EN_LINEA_PROFORMA";
    public static String rolReportesRecaudacion = "ROL_REPORTES_RECAUDACION";

    /*KEYS DE PROCESOS*/
    public static String procesoCertificacion = "RpCertificaciones";
    public static String procesoInscripcion = "RpInscripciones";
    public static String procesoSolicitudInscripcionOnline = "RpSolicitudInscripcionesOnline";

    /*CONTENIDO DE CORREOS*/
    public static String correoInicioTramite = "CORREO_INICIO_TRAMITE";
    public static String correoObservacionesTramite = "CORREO_OBSERVACIONES_TRAMITE";
    public static String correoTramiteFinalizado = "CORREO_FINALIZACION_TRAMITE";
    public static String correoTramiteNoIngresado = "CORREO_TRAMITE_NO_INGRESADO";
    
    public static String correoFactura = "CORREO_FACTURA_ELECTRONICA";
    public static String correoTramite = "CORREO_TRAMITE_FINALIZADO";
    public static String correoEnvioFactura = "CORREO_ENVIO_FACTURA_ELECTRONICA";
    public static String correoProformaTramite = "CORREO_PROFORMA_TRAMITE_ONLINE";
    public static String correoTramiteVacio = "CORREO_CONTENIDO_VACIO";
    public static String correoSolictiudInscripcionOnline = "CORREO_SOLICITUD_INSCRIPCION_EN_LINEA";
    public static String correoSolictiudInscripcionNotificacion = "CORREO_SOLICITUD_INSCRIPCION_NOTIFICACION";
    public static String correoSolictiudInscripcionPago = "CORREO_PAGO_INSCRIPCION_EN_LINEA";
    public static String correoFinTramiteCertificado = "CORREO_FIN_TRAMITE_CERTIFICADO";
    public static String correoFinTramiteInscripcion = "CORREO_FIN_TRAMITE_INSCRIPCION";

    /*SECUENCIA DE CERTIFICADOS*/
    public static String certificadoHistoria = "CERTIFICADO_HISTORIA_DOMINIO";
    public static String certificadoBienes = "CERTIFICADO_BIENES_RAICES";
    public static String certificadoGeneral = "CERTIFICADO_GENERAL";
    public static String secuenciaCertificados = "SECUENCIA_CERTIFICADOS";

    /*RUTAS DE FACTURAS AUTORIZADAS*/
    //public static String rutaFeOld = "C:/facturacion-electronica/sriComprobantes/comprobantes/autorizados/";
    public static String rutaFeOld = "/home/facturacion-electronica/sriComprobantes/comprobantes/autorizados/";
    
    public static String salto = "************************************************** NUEVO DOCUMENTO ***************************************************\n";

    /*VALORES DE ANEXOS*/
    public static String campoVacio = "";
    public static String saltoLinea = "\r\n";
    public static String moneda = "DÓLAR";
    public static String provincia = "SANTO DOMINGO";
    public static String canton = "santodomingo";
    public static String ubicacionDato = "RP.SANTODOMINGO";
    public static String descripcion = "REGISTRO DE LA PROPIEDAD DEL CANTON SANTO DOMINGO";
    public static String cabeceraFormulario2Side = "ACTO/CONTRATO;TIPO DE ACTO;TIPO DE TRÁMITE;TIPO DE LIBRO;REPERTORIO;"
            + "FECHA DE REPERTORIO;Nº INSCRIPCIÓN;FECHA DE INSCRIPCIÓN;TIPO DE PERSONA;RAZÓN SOCIAL;NOMBRES;APELLIDOS;"
            + "TIPO DE INTERVINIENTE;CALIDAD DE COMPARECIENTE;TIPO DE DOCUMENTO;Nº IDENTIFICACIÓN;ESTADO CIVIL;"
            + "NOMBRES Y APELLIDOS DEL CÓNYUGE;Nº IDENTIFICACIÓN DEL CÓNYUGE;SEPARACIÓN DE BIENES;Nº PREDIAL;CLAVE CATASTRAL;"
            + "DESCRIPCIÓN DEL BIEN;PROVINCIA;ZONA;SUPERFICIE O ÁREA;UBICACIÓN GEOGRÁFICA;DESCRIPCIÓN  DEL LINDERO;"
            + "PARROQUIA;CANTÓN;CUANTÍA;UNIDAD MONETARIA;IDENTIFICADOR SECUENCIAL;GRAVAMEN/LIMITACIÓN;TIPO DE GRAVAMEN O LIMITACIÓN;"
            + "GENERA GRAVAMEN O LIMITACIÓN;TIPO DE GRAVAMEN O LIMITACIÓN (GENERADO);FECHA DE CONSTITUCIÓN DE GRAVAMEN O LIMITACIÓN;"
            + "FECHA DE CANCELACIÓN DE GRAVAMEN O LIMITACIÓN;MARGINACIÓN DEL TRÁMITE ORIGINAL;ÚLTIMA MODIFICACIÓN DE LA INSCRIPCIÓN;"
            + "CANTÓN DEL REGISTRO DE LA PROPIEDAD;NOTARÍA/JUZGADO/ENTIDAD PÚBLICA;CANTÓN DE LA NOTARÍA/JUZGADO/ENTIDAD PÚBLICA;"
            + "FECHA DE ESCRITURA;PROPIEDAD HORIZONTAL;PORCENTAJE DE ALÍCUOTA;EXPENSAS;COMPARECIENTES MENORES;TUTOR;"
            + "FECHA DE ADJUDICACIÓN;FECHA DE INSINUACIÓN JUDICIAL O ACTA NOTARIAL;Nº ACUERDO MINISTERIAL;CAUSANTE;"
            + "FECHA DE DEFUNCIÓN;HEREDEROS;CÓNYUGE SOBREVIVIENTE;HISTÓRICO;OBSERVACION;FOLIO INICIAL;FOLIO FINAL;AVALUO CATASTRAL";
    public static String contenidoNotaDevolutiva = "</br>De la calificación realizada y en virtud de los artículos 11 numerales 1 y 5 "
            + "y el artículo 12 del Reglamento de la Ley Orgánica del Sistema Nacional del Registro Datos Públicos, se devuelve el "
            + "trámite para que se subsane los defectos de forma : <br/>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>1.- </b></div>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>2.- </b></div>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>3.- </b></div>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>4.- </b></div>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>5.- </b></div>"
            + " <br/> <br/>La presente nota no constituye Negativa Registral (Resolución 010-NG-DINARDAP-2017); el plazo"
            + " para subsanar los defectos de forma es de sesenta días, según artículo 13 de la ley de Registro.";
    public static String contenidoDevolutivaCertificado = "</br>De conformidad a lo establecido en el Art. 11 literal e) de la Ley "
            + "de Registro, se devuelve esta solicitud sin CERTIFICAR por el(los) siguiente(s) motivo(s): <br/>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>1.- </b></div>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>2.- </b></div>"
            + " <br/> <br/> <div><span style=\"white-space:pre\">	</span><b>3.- </b></div>"
            + " <br/>";
    public static String contenidoFichaInmueble = "Norte: \r\nSur: \r\nEste: \r\nOeste: \r\nÁrea Total: ";
    public static String contenidoFichaMueble = "Clase/Tipo: \r\nModelo: \r\nMotor/Fierro: \r\nMarca: \r\nColor: \r\nChasis: \r\nAño: \r\nPlaca: ";
    public static String uafCanton = "2301";            //CODIGO CANTON
    public static String uafCodigoRegistro = "21075";   //CODIGO DE REGISTRO ASIGNADO POR LA UAFE
    public static String urlDownDocsVentanilla = "https://servicios.registrodelapropiedadsd.gob.ec/ventanilla/validar.xhtml";
    public static String separador = "|";
    public static String separador2 = ";";
    /**
     * Formato RP-012-2014
     */
    public static String cabeceraAnexo1Side12 = "APELLIDOS|NOMBRES|NUMERODEIDENTIFICACION|TIPODECOMPARECIENTE|RAZONSOCIAL|"
            + "TIPODECONTRATO|NUMERODEINSCRIPCION|FECHADEINSCRIPCION|CLAVECATASTRAL|DESCRIPCIONDELBIEN|LIBRO|PROVINCIA|ZONA|"
            + "SUPERFICIE|ORIENTACION|LINDERO|PARROQUIA|CANTON|CUANTIA|UNIDADCUANTIA|CODIGOUNICO|NUMEROJUICIO|ESTADO|REGISTRADOR|"
            + "ULTIMAMODIFICACION|NOTARIA|CANTONNOTARIA|FECHADEESCRITURA";
    /**
     * Formato MC-012-2014
     */
    public static String cabeceraAnexo2Side12 = "APELLIDOS|NOMBRES|NUMERODEIDENTIFICACION|TIPODECOMPARECIENTE|RAZONSOCIAL|ACTONOMRES|"
            + "FECHADEINSCRIPCION|NUMERODEINSCRIPCION|NOMBREREPRESENTANTE|FECHADECANCELACION|TIPOBIEN|CHASIS|MOTOR|MODELO|MARCA|ANIO|"
            + "PLACA|REGISTRADOR|ULTIMAMODIFICACION|CODIGOUNICO|ENTIDADPUBLICA|CANTNOMBRE|FECHADEESCRITURA|ESTADO";
    /**
     * Formato MS-012-2014
     */
    public static String cabeceraAnexo3Side12 = "CLIENOMBRE|RUC|TIPO_COMPANIA|FECHAINSCRIPCION|APELLIDOS|NOMBRES|NUMERODEIDENTIFICACION|"
            + "CARGNOMBRE|TIPOCOMPARECIENTE|DISPOSICION|AUTORIDADDISPOSICION|FECHADISPOSICION|NUMERODISPOSICION|FECHAESCRITURA|NOTARIA|"
            + "CANTONNOTARIA|TIPOTRAMITE|UBICACIONDATO|ULTIMAMODIFICACION|CODIGOUNICO|ESTADO";
    public static String propiedadHorizontal = "tipo_bien_ph";
    public static String tipoCertificado = "tipo_certificado";
    public static String tipoCertificadoPara = "tipo_certificado_para";
    public static String tipoDocumento = "tipo_documento";
    public static String observacionCancelacionMercantil = "Mediante escritura pública otorgada el xx de xxxxx del año xxxx ante la "
            + "Notaria xxxxx del Cantón xxxxx, en donde XXXXXXXXXXXXX CANCELA EL CONTRATO DE XXXXXXXXXXXX que pesaba "
            + "sobre XXXXXXXXXXX detallada en la referida escritura. De propiedad de XXXXXX XXXXXX, además se tomó "
            + "nota al margen de la partida número xxx/xxxx quedando así sin ningún valor ni efecto.";
    public static int diasValidezCertificado = 30;
    public static String cajaVentanilla = "SECUENCIA_CAJA_5";
    public static String fromEmail = "REGISTRO DE LA PROPIEDAD CANTÓN SANTO DOMINGO";
    public static String tramiteEnLinea = "ONLINE";
    public static String tramiteDescargado = "DESCARGADO";
    public static String certificadoExpress = "CERTIFICADO XPRESS";
    public static String tituloCertificador = "ASISTENTE REGISTRAL";
    public static String indexacionHabilitantes = "HABILITANTES";
    public static String indexacionInscripciones = "INSCRIPCIONES";

    public static String DOC_NO_ENCONTRADO_PDF = "documento_no_encontrado.pdf";
    public static String TOPIC_SMS = "enviarSms";
    public static String SMS_TRAMITE = "EPMRP-SD usted tiene una nueva notificacion, revise su correo electronio. Este msj es solo informativo.";
    public static String SMS_FIN_TRAMITE = "EPMRP-SD su tramite ha finalizado correctamente revise su correo electronico";
    public static String TIPO_REQUISITO = "REQUISITO";
    public static String TIPO_REQUISITO_NOTIFICAR = "REQUISITO_NOTIFICA";
    public static String TIPO_REQUISITO_NOTIFICADO = "REQUISITO_NOTIFICADO";
    public static String downLoadRazonInscripcion = "https://servicios.registrodelapropiedadsd.gob.ec/ventanilla/validar.xhtml";

}
