/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.config.SisVars;
import com.origami.sgr.entities.NprmSri;
import com.origami.sgr.entities.NprmSriDetalle;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimientoParticipante;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.models.Nrpm;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.hibernate.Hibernate;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Anyelo
 */
public class AnexosXml implements Serializable {

    private static String COD_PROV = null;
    private static String COD_CANTON = null;
    private static String COD_PARRO = null;
    private static String NOMBRE_PRO = null;

    public static File generarAnexoNrpm(String reg, Date fecha, List<RegMovimientoParticipante> detalles) {
        SimpleDateFormat sdf;
        String nombre = "REP";
        RegFicha ficha;
        List<Valores> configs = EjbsCaller.getTransactionManager().findAll("SELECT v FROM Valores v WHERE v.code LIKE :code", new String[]{"code"}, new Object[]{"%ANEXO_SRI%"});
        if (Utils.isEmpty(configs)) {
            System.out.println("Sebe llenar los parametros: ANEXO_SRI_PROVINCIA, ANEXO_SRI_CANTON, ANEXO_SRI_PARROQUIA y ANEXO_SRI_NOMBRE_PROVINCIA_DEF en la tabla valores.");
            return null;
        }
        for (Valores config : configs) {
            switch (config.getCode()) {
                case "ANEXO_SRI_PROVINCIA":
                    COD_PROV = config.getValorString();
                case "ANEXO_SRI_CANTON":
                    COD_CANTON = config.getValorString();
                case "ANEXO_SRI_PARROQUIA":
                    COD_PARRO = config.getValorString();
                case "ANEXO_SRI_NOMBRE_PROVINCIA_DEF":
                    NOMBRE_PRO = config.getValorString();
            }
        }
        try {
            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            // ELEMENTO ANR
            Element anr = doc.createElement("anr");
            doc.appendChild(anr);

            // ELEMENTO DATOSINFORMANTE
            Element datosInformante = doc.createElement("datosInformante");
            anr.appendChild(datosInformante);

            // ELEMENTO RUC
            Element ruc = doc.createElement("ruc");
            ruc.appendChild(doc.createTextNode(reg));
            datosInformante.appendChild(ruc);

            // ELEMENTO TIPOANEXO
            Element tipoAnexo = doc.createElement("tipoAnexo");
            tipoAnexo.appendChild(doc.createTextNode(nombre));
            datosInformante.appendChild(tipoAnexo);

            // ELEMENTO ANIO
            Element anio = doc.createElement("anio");
            sdf = new SimpleDateFormat("yyyy");
            anio.appendChild(doc.createTextNode(sdf.format(fecha)));
            datosInformante.appendChild(anio);
            nombre = nombre + sdf.format(fecha);

            // ELEMENTO MES
            Element mes = doc.createElement("mes");
            sdf = new SimpleDateFormat("MM");
            mes.appendChild(doc.createTextNode(sdf.format(fecha)));
            datosInformante.appendChild(mes);
            nombre = nombre + sdf.format(fecha);

            // ELEMENTO TRANSACCIONES
            Element transacciones = doc.createElement("transacciones");
            anr.appendChild(transacciones);
            sdf = new SimpleDateFormat("dd/MM/yyyy");

            // AGREGA CADA ELEMENTO TRANSACCION
            for (RegMovimientoParticipante mp : detalles) {
                // ELEMENTO TRANSACCION
                Element transaccion = doc.createElement("transaccion");
                transacciones.appendChild(transaccion);

                // ELEMENTO DATOSPARTICIPANTE
                Element datosParticipante = doc.createElement("datosParticipante");
                transaccion.appendChild(datosParticipante);

                // ELEMENTO TIPOPARTICIPANTEENTREGA
                Element tipoParticipanteEntrega = doc.createElement("tipoParticipanteEntrega");
                tipoParticipanteEntrega.appendChild(doc.createTextNode(mp.getTipoParticipanteEntrega()));
                datosParticipante.appendChild(tipoParticipanteEntrega);

                // ELEMENTO TIPOIDENTIFICACIONENTREGA
                Element tipoIdentificacionEntrega = doc.createElement("tipoIdentificacionEntrega");
                tipoIdentificacionEntrega.appendChild(doc.createTextNode(mp.getEntrega() == null ? "" : mp.getEntrega().getTipoDocumentoNrpm()));
                datosParticipante.appendChild(tipoIdentificacionEntrega);

                // ELEMENTO NUMEROIDENTIFICACIONENTREGA
                Element numeroIdentificacionEntrega = doc.createElement("numeroIdentificacionEntrega");
                numeroIdentificacionEntrega.appendChild(doc.createTextNode(mp.getEntrega() == null ? "" : mp.getEntrega().getCedRuc()));
                datosParticipante.appendChild(numeroIdentificacionEntrega);

                // ELEMENTO TIPORELACIONENTREGA
                Element tipoRelacionEntrega = doc.createElement("tipoRelacionEntrega");
                tipoRelacionEntrega.appendChild(doc.createTextNode(mp.getTipoRelacionEntrega()));
                datosParticipante.appendChild(tipoRelacionEntrega);

                if (mp.getRecibe() == null) {
                    // ELEMENTO TIPOPARTICIPANTERECIBE
                    Element tipoParticipanteRecibe = doc.createElement("tipoParticipanteRecibe");
                    tipoParticipanteRecibe.appendChild(doc.createTextNode("6"));
                    datosParticipante.appendChild(tipoParticipanteRecibe);

                } else {
                    // ELEMENTO TIPOPARTICIPANTERECIBE
                    Element tipoParticipanteRecibe = doc.createElement("tipoParticipanteRecibe");
                    tipoParticipanteRecibe.appendChild(doc.createTextNode(mp.getTipoParticipanteRecibe()));
                    datosParticipante.appendChild(tipoParticipanteRecibe);

                    // ELEMENTO TIPOIDENTIFICACIONRECIBE
                    Element tipoIdentificacionRecibe = doc.createElement("tipoIdentificacionRecibe");
                    tipoIdentificacionRecibe.appendChild(doc.createTextNode(mp.getRecibe().getTipoDocumentoNrpm()));
                    datosParticipante.appendChild(tipoIdentificacionRecibe);

                    // ELEMENTO NUMEROIDENTIFICACIONRECIBE
                    Element numeroIdentificacionRecibe = doc.createElement("numeroIdentificacionRecibe");
                    numeroIdentificacionRecibe.appendChild(doc.createTextNode(mp.getRecibe().getCedRuc()));
                    datosParticipante.appendChild(numeroIdentificacionRecibe);

                    // ELEMENTO TIPORELACIONRECIBE
                    Element tipoRelacionRecibe = doc.createElement("tipoRelacionRecibe");
                    tipoRelacionRecibe.appendChild(doc.createTextNode(mp.getTipoRelacionRecibe()));
                    datosParticipante.appendChild(tipoRelacionRecibe);
                }

                // ELEMENTO DETALLETRANSACCION
                Element detalleTransaccion = doc.createElement("detalleTransaccion");
                transaccion.appendChild(detalleTransaccion);

                // ELEMENTO TIPOTRANSACCION
                Element tipoTransaccion = doc.createElement("tipoTransaccion");
                if (mp.getMovimiento().getActo().getTransaccion() == null) {
                    tipoTransaccion.appendChild(doc.createTextNode("11"));
                } else {
                    tipoTransaccion.appendChild(doc.createTextNode(mp.getMovimiento().getActo().getTransaccion()));
                }
                detalleTransaccion.appendChild(tipoTransaccion);

                // ELEMENTO FECHAINSCRIPCION
                Element fechaInscripcion = doc.createElement("fechaInscripcion");
                fechaInscripcion.appendChild(doc.createTextNode(sdf.format(mp.getMovimiento().getFechaInscripcion())));
                detalleTransaccion.appendChild(fechaInscripcion);

                // ELEMENTO FECHAESCRITURA
                Element fechaEscritura = doc.createElement("fechaEscritura");
                if (mp.getMovimiento().getFechaOto() == null) {
                    fechaEscritura.appendChild(doc.createTextNode(sdf.format(mp.getMovimiento().getFechaResolucion())));
                } else {
                    fechaEscritura.appendChild(doc.createTextNode(sdf.format(mp.getMovimiento().getFechaOto())));
                }
                detalleTransaccion.appendChild(fechaEscritura);

                // ELEMENTO PROVINCIA
                Element provincia = doc.createElement("provincia");
                provincia.appendChild(doc.createTextNode(COD_PROV));
                detalleTransaccion.appendChild(provincia);

                // ELEMENTO CANTON
                Element canton = doc.createElement("canton");
                canton.appendChild(doc.createTextNode(COD_CANTON));
                detalleTransaccion.appendChild(canton);

                // ELEMENTO MONTO
                Element monto = doc.createElement("monto");
                monto.appendChild(doc.createTextNode(mp.getMovimiento().getCuantia().toString()));
                detalleTransaccion.appendChild(monto);

                if (mp.getMovimiento().getActo().getTransaccion() != null
                        && (mp.getMovimiento().getActo().getTransaccion().equalsIgnoreCase("2")
                        || mp.getMovimiento().getActo().getTransaccion().equalsIgnoreCase("10")
                        || mp.getMovimiento().getActo().getTransaccion().equalsIgnoreCase("14"))) {
                    // ELEMENTO AVALUOCATASTRAL
                    Element avaluoCatastral = doc.createElement("avaluoCatastral");
                    avaluoCatastral.appendChild(doc.createTextNode(mp.getMovimiento().getTramite().getDetalle().getAvaluo().toString()));
                    detalleTransaccion.appendChild(avaluoCatastral);
                    ficha = (RegFicha) EjbsCaller.getTransactionManager().find(Querys.getMaxFichaFromMov, new String[]{"idFicha"}, new Object[]{mp.getMovimiento().getId()});
                    if (ficha != null) {
                        //ELEMENTO PARROQUIA
                        Element parroquia = doc.createElement("parroquia");
                        if (ficha.getParroquia() != null && ficha.getParroquia().getNrpmCodigo() != null) {
                            parroquia.appendChild(doc.createTextNode(ficha.getParroquia().getNrpmCodigo()));
                        } else {
                            parroquia.appendChild(doc.createTextNode(COD_PARRO));
                        }
                        detalleTransaccion.appendChild(parroquia);
                        // ELEMENTO DIRECCION
                        Element direccion = doc.createElement("direccion");
                        if (ficha.getDireccionBien() == null) {
                            direccion.appendChild(doc.createTextNode(NOMBRE_PRO));
                        } else if (ficha.getDireccionBien().length() < 10) {
                            direccion.appendChild(doc.createTextNode(NOMBRE_PRO));
                        } else {
                            direccion.appendChild(doc.createTextNode(Utils.quitarTildes(ficha.getDireccionBien().replace("  ", " ")).replaceAll("[^\\dA-Za-zñÑ\\s]", "")));
                        }
                        detalleTransaccion.appendChild(direccion);
                        // ELEMENTO NUMEROPREDIO
                        Element numeroPredio = doc.createElement("numeroPredio");

                        // ELEMENTO CLAVECATASTRAL
                        Element claveCatastral = doc.createElement("claveCatastral");

                        if (ficha.getClaveCatastral() != null && ficha.getClaveCatastral().length() > 3) {
                            numeroPredio.appendChild(doc.createTextNode(ficha.getClaveCatastral()));
                            claveCatastral.appendChild(doc.createTextNode(ficha.getClaveCatastral()));
                        } else {
                            numeroPredio.appendChild(doc.createTextNode(ficha.getNumFicha().toString()));
                            claveCatastral.appendChild(doc.createTextNode(ficha.getNumFicha().toString()));
                        }
                        detalleTransaccion.appendChild(numeroPredio);
                        detalleTransaccion.appendChild(claveCatastral);
                    }
                }
            }

            // CREACION DEL ARCHIVO XML
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            DOMSource source = new DOMSource(doc);
            File archivo = new File(SisVars.rutaAnexos + nombre + ".xml");
            System.out.println("archivo: " + archivo.getPath());
            StreamResult result = new StreamResult(archivo.getPath());
            transformer.transform(source, result);
            return archivo;
        } catch (IllegalArgumentException | ParserConfigurationException | TransformerException | DOMException e) {
            Logger.getLogger(AnexosXml.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public static File generarAnexoNrpm1(String reg, Date fecha, NprmSri nprmSri) {
        SimpleDateFormat sdf;
        RegFicha ficha;
        String nombre = "REP";
        if (nprmSri.getTipoAnexo() == 2) {
            nombre = "REM";
        }
        List<Valores> configs = EjbsCaller.getTransactionManager().findAll("SELECT v FROM Valores v WHERE v.code LIKE :code", new String[]{"code"}, new Object[]{"%ANEXO_SRI%"});
        if (Utils.isEmpty(configs)) {
            System.out.println("Sebe llenar los parametros: ANEXO_SRI_PROVINCIA, ANEXO_SRI_CANTON, ANEXO_SRI_PARROQUIA y ANEXO_SRI_NOMBRE_PROVINCIA_DEF en la tabla valores.");
            return null;
        }
        configs.forEach((config) -> {
            switch (config.getCode()) {
                case "ANEXO_SRI_PROVINCIA":
                    COD_PROV = config.getValorString();
                case "ANEXO_SRI_CANTON":
                    COD_CANTON = config.getValorString();
                case "ANEXO_SRI_PARROQUIA":
                    COD_PARRO = config.getValorString();
                case "ANEXO_SRI_NOMBRE_PROVINCIA_DEF":
                    NOMBRE_PRO = config.getValorString();
            }
        });
        try {
            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            // ELEMENTO ANR
            Element anr = doc.createElement("anr");
            doc.appendChild(anr);

            // ELEMENTO DATOSINFORMANTE
            Element datosInformante = createElement(doc, anr, "datosInformante");
            // ELEMENTO RUC
            createNode(doc, datosInformante, "ruc", reg);
            // ELEMENTO TIPOANEXO
            createNode(doc, datosInformante, "tipoAnexo", nombre);
            // ELEMENTO ANIO
            sdf = new SimpleDateFormat("yyyy");
            createNode(doc, datosInformante, "anio", sdf.format(fecha));
            nombre = nombre + sdf.format(fecha);
            // ELEMENTO MES
            if (nprmSri.getTipoAnexo() == 1) {
                sdf = new SimpleDateFormat("MM");
                createNode(doc, datosInformante, "mes", sdf.format(fecha));
                nombre = nombre + sdf.format(fecha);
            }
            // ELEMENTO TRANSACCIONES
            Element transacciones = createElement(doc, anr, "transacciones");
            sdf = new SimpleDateFormat("dd/MM/yyyy");

            // AGREGA CADA ELEMENTO TRANSACCION
            if (nprmSri.getNprmSriDetalles() != null) {
                for (NprmSriDetalle sriDet : nprmSri.getNprmSriDetalles()) {
                    RegMovimientoParticipante mp = sriDet.getMovimientoParticipante();
                    // ELEMENTO TRANSACCION
                    Element transaccion = createElement(doc, transacciones, "transaccion");
                    // ELEMENTO DATOSPARTICIPANTE
                    Element datosParticipante = createElement(doc, transaccion, "datosParticipante");
                    // ELEMENTO TIPOPARTICIPANTEENTREGA
                    createNode(doc, datosParticipante, "tipoParticipanteEntrega", mp.getTipoParticipanteEntrega());
                    // ELEMENTO TIPOIDENTIFICACIONENTREGA
                    if (HiberUtil.isProxy(mp.getEntrega())) {
                        Hibernate.isInitialized(mp.getEntrega());
                    }
                    createNode(doc, datosParticipante, "tipoIdentificacionEntrega", mp.getEntrega() == null ? "" : mp.getEntrega().getTipoDocumentoNrpm());
                    // ELEMENTO NUMEROIDENTIFICACIONENTREGA
                    createNode(doc, datosParticipante, "numeroIdentificacionEntrega", mp.getEntrega() == null ? "" : mp.getEntrega().getCedRuc());
                    // ELEMENTO TIPORELACIONENTREGA
                    createNode(doc, datosParticipante, "tipoRelacionEntrega", mp.getTipoRelacionEntrega());
                    if (mp.getRecibe() == null) {
                        // ELEMENTO TIPOPARTICIPANTERECIBE
                        createNode(doc, datosParticipante, "tipoParticipanteRecibe", "6");
                    } else {
                        // ELEMENTO TIPOPARTICIPANTERECIBE
                        createNode(doc, datosParticipante, "tipoParticipanteRecibe", mp.getTipoParticipanteRecibe());
                        // ELEMENTO TIPOIDENTIFICACIONRECIBE
                        if (HiberUtil.isProxy(mp.getRecibe())) {
                            Hibernate.isInitialized(mp.getRecibe());
                        }
                        createNode(doc, datosParticipante, "tipoIdentificacionRecibe", mp.getRecibe().getTipoDocumentoNrpm());
                        // ELEMENTO NUMEROIDENTIFICACIONRECIBE
                        createNode(doc, datosParticipante, "numeroIdentificacionRecibe", mp.getRecibe().getCedRuc());
                        // ELEMENTO TIPORELACIONRECIBE
                        createNode(doc, datosParticipante, "tipoRelacionRecibe", mp.getTipoRelacionRecibe());
                    }

                    // ELEMENTO DETALLETRANSACCION
                    Element detalleTransaccion = createElement(doc, transaccion, "detalleTransaccion");
                    // ELEMENTO TIPOTRANSACCION
                    if (sriDet.getTipoTransaccion() == null) {
                        createNode(doc, detalleTransaccion, "tipoTransaccion", "11");
                    } else {
                        createNode(doc, detalleTransaccion, "tipoTransaccion", sriDet.getTipoTransaccion().getCodigo());
                    }
                    // ELEMENTO FECHAINSCRIPCION
                    createNode(doc, detalleTransaccion, "fechaInscripcion", sdf.format(mp.getMovimiento().getFechaInscripcion()));
                    // ELEMENTO FECHAESCRITURA
                    if (mp.getMovimiento().getFechaOto() == null) {
                        createNode(doc, detalleTransaccion, "fechaEscritura", sdf.format(mp.getMovimiento().getFechaResolucion()));
                    } else {
                        createNode(doc, detalleTransaccion, "fechaEscritura", sdf.format(mp.getMovimiento().getFechaOto()));
                    }
                    // ELEMENTO PROVINCIA
                    createNode(doc, detalleTransaccion, "provincia", COD_PROV);
                    // ELEMENTO CANTON
                    createNode(doc, detalleTransaccion, "canton", COD_CANTON);
                    // ELEMENTO MONTO
                    if (mp.getMovimiento().getCuantia() != null
                            && (mp.getMovimiento().getCuantia().compareTo(BigDecimal.ZERO) > 0)) {
                        createNode(doc, detalleTransaccion, "monto", mp.getMovimiento().getCuantia().toString());
                    } else if (mp.getMovimiento().getAvaluoMunicipal() != null
                            && (mp.getMovimiento().getAvaluoMunicipal().compareTo(BigDecimal.ZERO) > 0)) {
                        createNode(doc, detalleTransaccion, "monto", mp.getMovimiento().getAvaluoMunicipal().toString());
                    } else {
                        createNode(doc, detalleTransaccion, "monto", BigDecimal.ONE.toString());
                    }

                    if (sriDet.getTipoTransaccion() != null
                            && (sriDet.getTipoTransaccion().getCodigo().equalsIgnoreCase("2")
                            || sriDet.getTipoTransaccion().getCodigo().equalsIgnoreCase("10")
                            || sriDet.getTipoTransaccion().getCodigo().equalsIgnoreCase("14"))) {
                        // ELEMENTO AVALUOCATASTRAL
                        BigDecimal temp = mp.getMovimiento().getTramite().getDetalle().getAvaluo();
                        if (temp != null && (temp.compareTo(BigDecimal.ZERO) > 0)) {
                            createNode(doc, detalleTransaccion, "avaluoCatastral", temp.toString());
                        } else if (mp.getMovimiento().getAvaluoMunicipal() != null
                                && (mp.getMovimiento().getAvaluoMunicipal().compareTo(BigDecimal.ZERO) > 0)) {
                            createNode(doc, detalleTransaccion, "avaluoCatastral", mp.getMovimiento().getAvaluoMunicipal().toString());
                        } else if (mp.getMovimiento().getCuantia() != null
                                && (mp.getMovimiento().getCuantia().compareTo(BigDecimal.ZERO) > 0)) {
                            createNode(doc, detalleTransaccion, "avaluoCatastral", mp.getMovimiento().getCuantia().toString());
                        } else {
                            createNode(doc, detalleTransaccion, "avaluoCatastral", BigDecimal.ONE.toString());
                        }

                        // DATOS DE LA FICHA
                        ficha = (RegFicha) EjbsCaller.getTransactionManager().find(Querys.getMaxFichaFromMov, new String[]{"idFicha"}, new Object[]{mp.getMovimiento().getId()});
                        if (ficha != null) {
                            //ELEMENTO PARROQUIA
                            if (ficha.getParroquia() != null && ficha.getParroquia().getNrpmCodigo() != null) {
//                            System.out.println("ficha.getParroquia().getNrpmCodigo() " + ficha.getParroquia().getNrpmCodigo());
                                createNode(doc, detalleTransaccion, "parroquia", ficha.getParroquia().getNrpmCodigo());
                            } else {
//                            System.out.println("COD_PARRO " + COD_PARRO);
                                createNode(doc, detalleTransaccion, "parroquia", COD_PARRO);
                            }
                            // ELEMENTO DIRECCION
                            try {
                                if (ficha.getDescripcionBien() == null) {
                                    //createNode(doc, detalleTransaccion, "direccion", Utils.completarCadenaR(NOMBRE_PRO, 10, " "));
                                    createNode(doc, detalleTransaccion, "direccion", Constantes.direcionPorDefectoAnexoSRI);
                                } else if (ficha.getDescripcionBien().length() < 10) {
                                    //createNode(doc, detalleTransaccion, "direccion", Utils.completarCadenaR(Utils.quitarTildes(ficha.getDireccionBien()), 10, " "));
                                    createNode(doc, detalleTransaccion, "direccion", Constantes.direcionPorDefectoAnexoSRI);
                                } else {
                                    String direccion = ficha.getDescripcionBien().substring(0, 80);
                                    createNode(doc, detalleTransaccion, "direccion", Utils.quitarTildes(direccion.replace("  ", " ")).replaceAll("[^\\dA-Za-zñÑ\\s]", ""));
                                }
                            } catch (Exception e) {
                                Logger.getLogger(AnexosXml.class.getName()).log(Level.SEVERE, "direccion", e);
                            }

                            if (ficha.getClaveCatastral() != null && ficha.getClaveCatastral().length() > 3) {
                                createNode(doc, detalleTransaccion, "numeroPredio", ficha.getClaveCatastral()); // ELEMENTO NUMEROPREDIO
                                createNode(doc, detalleTransaccion, "claveCatastral", ficha.getClaveCatastral()); // ELEMENTO CLAVECATASTRAL
                            } else {
                                createNode(doc, detalleTransaccion, "numeroPredio", ficha.getNumFicha().toString()); // ELEMENTO NUMEROPREDIO
                                createNode(doc, detalleTransaccion, "claveCatastral", ficha.getNumFicha().toString()); // ELEMENTO CLAVECATASTRAL
                            }
                        }
                    }
                }
            }
            // CREACION DEL ARCHIVO XML
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            DOMSource source = new DOMSource(doc);
            File archivo = new File(SisVars.rutaAnexos + nombre + ".xml");
            //System.out.println("archivo: " + archivo.getPath());
            StreamResult result = new StreamResult(archivo.getPath());
            transformer.transform(source, result);
            return archivo;
        } catch (IllegalArgumentException | ParserConfigurationException | TransformerException | DOMException e) {
            Logger.getLogger(AnexosXml.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public static boolean generarAnexoXml(String reg, Date fecha, List<Nrpm> detalles) {
        SimpleDateFormat sdf;
        String nombre = "REP";
        try {
            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            // ELEMENTO ANR
            Element anr = doc.createElement("anr");
            doc.appendChild(anr);

            // ELEMENTO DATOSINFORMANTE
            Element datosInformante = doc.createElement("datosInformante");
            anr.appendChild(datosInformante);

            // ELEMENTO RUC
            Element ruc = doc.createElement("ruc");
            ruc.appendChild(doc.createTextNode(reg));
            datosInformante.appendChild(ruc);

            // ELEMENTO TIPOANEXO
            Element tipoAnexo = doc.createElement("tipoAnexo");
            tipoAnexo.appendChild(doc.createTextNode(nombre));
            datosInformante.appendChild(tipoAnexo);

            // ELEMENTO ANIO
            Element anio = doc.createElement("anio");
            sdf = new SimpleDateFormat("yyyy");
            anio.appendChild(doc.createTextNode(sdf.format(fecha)));
            datosInformante.appendChild(anio);
            nombre = nombre + sdf.format(fecha);

            // ELEMENTO MES
            Element mes = doc.createElement("mes");
            sdf = new SimpleDateFormat("MM");
            mes.appendChild(doc.createTextNode(sdf.format(fecha)));
            datosInformante.appendChild(mes);
            nombre = nombre + sdf.format(fecha);

            // ELEMENTO TRANSACCIONES
            Element transacciones = doc.createElement("transacciones");
            anr.appendChild(transacciones);
            sdf = new SimpleDateFormat("dd/MM/yyyy");

            // AGREGA CADA ELEMENTO TRANSACCION
            for (Nrpm nr : detalles) {
                // ELEMENTO TRANSACCION
                Element transaccion = doc.createElement("transaccion");
                transacciones.appendChild(transaccion);

                // ELEMENTO DATOSPARTICIPANTE
                Element datosParticipante = doc.createElement("datosParticipante");
                transaccion.appendChild(datosParticipante);

                // ELEMENTO TIPOPARTICIPANTEENTREGA
                if (nr.getTipoparticipanteentrega() != null) {
                    Element tipoParticipanteEntrega = doc.createElement("tipoParticipanteEntrega");
                    tipoParticipanteEntrega.appendChild(doc.createTextNode(nr.getTipoparticipanteentrega().toString()));
                    datosParticipante.appendChild(tipoParticipanteEntrega);
                }

                // ELEMENTO TIPOIDENTIFICACIONENTREGA
                if (nr.getTipoidentificacionentrega() != null) {
                    Element tipoIdentificacionEntrega = doc.createElement("tipoIdentificacionEntrega");
                    tipoIdentificacionEntrega.appendChild(doc.createTextNode(nr.getTipoidentificacionentrega().toString()));
                    datosParticipante.appendChild(tipoIdentificacionEntrega);
                }

                // ELEMENTO NUMEROIDENTIFICACIONENTREGA
                if (nr.getNumeroidentificacionentrega() != null) {
                    Element numeroIdentificacionEntrega = doc.createElement("numeroIdentificacionEntrega");
                    numeroIdentificacionEntrega.appendChild(doc.createTextNode(nr.getNumeroidentificacionentrega()));
                    datosParticipante.appendChild(numeroIdentificacionEntrega);
                }

                // ELEMENTO TIPORELACIONENTREGA
                if (nr.getTiporelacionentrega() != null) {
                    Element tipoRelacionEntrega = doc.createElement("tipoRelacionEntrega");
                    tipoRelacionEntrega.appendChild(doc.createTextNode(nr.getTiporelacionentrega().toString()));
                    datosParticipante.appendChild(tipoRelacionEntrega);
                }

                // ELEMENTO TIPOPARTICIPANTERECIBE
                if (nr.getTipoparticipanterecibe() != null) {
                    Element tipoParticipanteRecibe = doc.createElement("tipoParticipanteRecibe");
                    tipoParticipanteRecibe.appendChild(doc.createTextNode(nr.getTipoparticipanterecibe().toString()));
                    datosParticipante.appendChild(tipoParticipanteRecibe);
                }

                // ELEMENTO TIPOIDENTIFICACIONRECIBE
                if (nr.getTipoidentificacionrecibe() != null) {
                    Element tipoIdentificacionRecibe = doc.createElement("tipoIdentificacionRecibe");
                    tipoIdentificacionRecibe.appendChild(doc.createTextNode(nr.getTipoidentificacionrecibe().toString()));
                    datosParticipante.appendChild(tipoIdentificacionRecibe);
                }

                // ELEMENTO NUMEROIDENTIFICACIONRECIBE
                if (nr.getNumeroidentificacionrecibe() != null) {
                    Element numeroIdentificacionRecibe = doc.createElement("numeroIdentificacionRecibe");
                    numeroIdentificacionRecibe.appendChild(doc.createTextNode(nr.getNumeroidentificacionrecibe()));
                    datosParticipante.appendChild(numeroIdentificacionRecibe);
                }

                // ELEMENTO TIPORELACIONRECIBE
                if (nr.getTiporelacionrecibe() != null) {
                    Element tipoRelacionRecibe = doc.createElement("tipoRelacionRecibe");
                    tipoRelacionRecibe.appendChild(doc.createTextNode(nr.getTiporelacionrecibe().toString()));
                    datosParticipante.appendChild(tipoRelacionRecibe);
                }

                // ELEMENTO DETALLETRANSACCION
                Element detalleTransaccion = doc.createElement("detalleTransaccion");
                transaccion.appendChild(detalleTransaccion);

                // ELEMENTO TIPOTRANSACCION
                if (nr.getTipotransaccion() != null) {
                    Element tipoTransaccion = doc.createElement("tipoTransaccion");
                    tipoTransaccion.appendChild(doc.createTextNode(nr.getTipotransaccion().toString()));
                    detalleTransaccion.appendChild(tipoTransaccion);
                }

                // ELEMENTO FECHAINSCRIPCION
                if (nr.getFechainscripcion() != null) {
                    Element fechaInscripcion = doc.createElement("fechaInscripcion");
                    fechaInscripcion.appendChild(doc.createTextNode(sdf.format(nr.getFechainscripcion())));
                    detalleTransaccion.appendChild(fechaInscripcion);
                }

                // ELEMENTO FECHAESCRITURA
                if (nr.getFechaescritura() != null) {
                    Element fechaEscritura = doc.createElement("fechaEscritura");
                    fechaEscritura.appendChild(doc.createTextNode(sdf.format(nr.getFechaescritura())));
                    detalleTransaccion.appendChild(fechaEscritura);
                }

                // ELEMENTO PROVINCIA
                if (nr.getProvincia() != null) {
                    Element provincia = doc.createElement("provincia");
                    provincia.appendChild(doc.createTextNode(nr.getProvincia().toString()));
                    detalleTransaccion.appendChild(provincia);
                }

                // ELEMENTO CANTON
                if (nr.getCanton() != null) {
                    Element canton = doc.createElement("canton");
                    canton.appendChild(doc.createTextNode(nr.getCanton().toString()));
                    detalleTransaccion.appendChild(canton);
                }

                // ELEMENTO MONTO
                if (nr.getMonto() != null) {
                    Element monto = doc.createElement("monto");
                    monto.appendChild(doc.createTextNode(nr.getMonto().toString()));
                    detalleTransaccion.appendChild(monto);
                }

                // ELEMENTO AVALUOCATASTRAL
                if (nr.getAvaluo() != null) {
                    Element avaluoCatastral = doc.createElement("avaluoCatastral");
                    avaluoCatastral.appendChild(doc.createTextNode(nr.getAvaluo().toString()));
                    detalleTransaccion.appendChild(avaluoCatastral);
                }
            }

            // CREACION DEL ARCHIVO XML
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            DOMSource source = new DOMSource(doc);
            File archivo = new File(SisVars.rutaAnexos + nombre + ".xml");
            StreamResult result = new StreamResult(archivo.getPath());
            transformer.transform(source, result);

        } catch (Exception e) {
            Logger.getLogger(AnexosXml.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    private static void createNode(Document doc, Element elementAppendChild, String nameElement, String textNode) {
        Element elementNode = doc.createElement(nameElement);
        elementNode.appendChild(doc.createTextNode(textNode));
        elementAppendChild.appendChild(elementNode);
    }

    private static Element createElement(Document doc, Element anr, String nameElement) {
        Element element = doc.createElement(nameElement);
        anr.appendChild(element);
        return element;
    }

}
