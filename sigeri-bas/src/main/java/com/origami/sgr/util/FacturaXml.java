/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.config.SisVars;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenDatosFacturaElectronica;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenNotaCredito;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Anyelo
 */
public class FacturaXml implements Serializable {

    /**
     * Retorna texto en formato xml que se envia como parametro al webservice
     * que se encarga de la facturacion electronica en un modulo complementario
     *
     * @param liquidacion Object
     * @param ruc Object
     * @param formasPago Object
     * @return Object
     * @throws TransformerConfigurationException Exception
     * @throws TransformerException Exception
     * @throws IOException Exception
     */
    public static String generarXmlWs(RegpLiquidacion liquidacion, String ruc, HashMap<String, Object> formasPago)
            throws TransformerConfigurationException, TransformerException, IOException {
        StreamResult result = new StreamResult(new StringWriter());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            //ELEMENTO XML
            Element xml = doc.createElement("xml");
            doc.appendChild(xml);

            //ELEMENTO FACTURAS
            Element facturas = doc.createElement("facturas");
            xml.appendChild(facturas);

            //ELEMENTO CABECERA
            Element cabecera = doc.createElement("cabecera");
            facturas.appendChild(cabecera);

            // ELEMENTO ENTIDAD_TRIBUTARIA
            Element entidad = doc.createElement("ENTIDAD_TRIBUTARIA");
            entidad.appendChild(doc.createTextNode(ruc));
            cabecera.appendChild(entidad);

            // ELEMENTO FECHA_EMISION
            Element fecha = doc.createElement("FECHA_EMISION");
            fecha.appendChild(doc.createTextNode(sdf.format(liquidacion.getFechaIngreso())));
            cabecera.appendChild(fecha);

            if (liquidacion.getBeneficiario() == null) {
                // ELEMENTO CEDULA_RUC
                Element cedRuc = doc.createElement("CEDULA_O_RUC");
                cedRuc.appendChild(doc.createTextNode(liquidacion.getSolicitante().getCiRuc()));
                cabecera.appendChild(cedRuc);
                // ELEMENTO PROPIETARIO
                Element propietario = doc.createElement("PROPIETARIO");
                propietario.appendChild(doc.createTextNode(liquidacion.getSolicitante().getNombreCompleto().toUpperCase().replaceAll("&", "Y")));
                cabecera.appendChild(propietario);
                // ELEMENTO DIRECCION
                Element direccion = doc.createElement("DIRECCION");
                if (liquidacion.getSolicitante().getDireccion() != null) {
                    direccion.appendChild(doc.createTextNode(liquidacion.getSolicitante().getDireccion().toUpperCase()));
                } else {
                    direccion.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(direccion);
                // ELEMENTO TELEFONO
                Element telefono = doc.createElement("TELEFONO");
                if (liquidacion.getSolicitante().getTelefono1() != null) {
                    telefono.appendChild(doc.createTextNode(liquidacion.getSolicitante().getTelefono1()));
                } else {
                    telefono.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(telefono);
                // ELEMENTO CORREO
                Element correo = doc.createElement("CORREO");
                if (liquidacion.getEmailTemp() != null) {
                    correo.appendChild(doc.createTextNode(liquidacion.getEmailTemp()));
                } else if (liquidacion.getSolicitante().getCorreo1() != null) {
                    correo.appendChild(doc.createTextNode(liquidacion.getSolicitante().getCorreo1()));
                } else {
                    correo.appendChild(doc.createTextNode(SisVars.correo));
                }
                cabecera.appendChild(correo);
            } else {
                // ELEMENTO CEDULA_RUC
                Element cedRuc = doc.createElement("CEDULA_O_RUC");
                cedRuc.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getCiRuc()));
                cabecera.appendChild(cedRuc);
                // ELEMENTO PROPIETARIO
                Element propietario = doc.createElement("PROPIETARIO");
                propietario.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getNombreCompleto().toUpperCase().replaceAll("&", "Y")));
                cabecera.appendChild(propietario);
                // ELEMENTO DIRECCION
                Element direccion = doc.createElement("DIRECCION");
                if (liquidacion.getBeneficiario().getDireccion() != null) {
                    direccion.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getDireccion().toUpperCase()));
                } else {
                    direccion.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(direccion);
                // ELEMENTO TELEFONO
                Element telefono = doc.createElement("TELEFONO");
                if (liquidacion.getBeneficiario().getTelefono1() != null) {
                    telefono.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getTelefono1()));
                } else {
                    telefono.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(telefono);
                // ELEMENTO CORREO
                Element correo = doc.createElement("CORREO");
                if (liquidacion.getBeneficiario().getCorreo1() != null) {
                    correo.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getCorreo1()));
                } else {
                    correo.appendChild(doc.createTextNode(SisVars.correo));
                }
                cabecera.appendChild(correo);
            }

            // ELEMENTO CLAVE_ACCESO
            Element claveAcceso = doc.createElement("CLAVE_ACCESO");
            if (liquidacion.getClaveAcceso() == null || liquidacion.getClaveAcceso().isEmpty()) {
                claveAcceso.appendChild(doc.createTextNode(""));
            } else {
                claveAcceso.appendChild(doc.createTextNode(liquidacion.getClaveAcceso()));
            }
            cabecera.appendChild(claveAcceso);

            // ELEMENTO DESCUENTO_LIM_COBRO
            BigDecimal temp = liquidacion.getDescLimitCobro().add(liquidacion.getGastosGenerales());
            Element descLimCobro = doc.createElement("DESCUENTO_LIM_COBRO");
            //descLimCobro.appendChild(doc.createTextNode(liquidacion.getDescLimitCobro().toString()));
            descLimCobro.appendChild(doc.createTextNode(temp.toString()));
            cabecera.appendChild(descLimCobro);

            // ELEMENTO PAGOS
            Element pagos = doc.createElement("pagos");
            cabecera.appendChild(pagos);
            // AGREGA CADA ELEMENTO PAGO
            for (Map.Entry<String, Object> entrySet : formasPago.entrySet()) {
                // ELEMENTO PAGO
                Element pago = doc.createElement("pago");
                pagos.appendChild(pago);
                // ELEMENTO FORMAPAGO
                Element fo = doc.createElement("FORMAPAGO");
                fo.appendChild(doc.createTextNode(entrySet.getKey()));
                pago.appendChild(fo);
                // ELEMENTO TOTAL
                Element to = doc.createElement("TOTAL");
                to.appendChild(doc.createTextNode(entrySet.getValue().toString()));
                pago.appendChild(to);
            }

            // ELEMENTO DETALLES
            Element detalles = doc.createElement("detalles");
            facturas.appendChild(detalles);
            // AGREGA CADA ELEMENTO DETALLE 
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                // ELEMENTO DETALLE
                Element detalle = doc.createElement("detalle");
                detalles.appendChild(detalle);
                // ELEMENTO CODIGO_PRINCIPAL
                Element cp = doc.createElement("CODIGO_PRINCIPAL");
                cp.appendChild(doc.createTextNode(""));
                detalle.appendChild(cp);
                // ELEMENTO CODIGO_AUXILIAR
                Element ca = doc.createElement("CODIGO_AUXILIAR");
                ca.appendChild(doc.createTextNode(""));
                detalle.appendChild(ca);
                // ELEMENTO DESCRIPCION
                Element descripcion = doc.createElement("DESCRIPCION");
                descripcion.appendChild(doc.createTextNode(det.getActo().getNombre()));
                detalle.appendChild(descripcion);
                // ELEMENTO VALOR
                Element valor = doc.createElement("VALOR");
                valor.appendChild(doc.createTextNode(det.getValorUnitario().toString()));
                detalle.appendChild(valor);
                // ELEMENTO DESCUENTO
                Element descuento = doc.createElement("DESCUENTO");
                descuento.appendChild(doc.createTextNode(det.getDescuento().toString()));
                detalle.appendChild(descuento);
                // ELEMENTO CANTIDAD
                Element cantidad = doc.createElement("CANTIDAD");
                cantidad.appendChild(doc.createTextNode(String.valueOf(det.getCantidad())));
                detalle.appendChild(cantidad);
                // ELEMENTO IVA
                Element iva = doc.createElement("IVA");
                iva.appendChild(doc.createTextNode("0"));
                detalle.appendChild(iva);
            }
            doc.normalize();
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            return result.getWriter().toString();
        } catch (ParserConfigurationException | DOMException e) {
            Logger.getLogger(FacturaXml.class.getName()).log(Level.SEVERE, null, e);
            return "";
        } finally {
            result.getWriter().close();
        }
    }

    /**
     * Retorna texto en formato xml que se envia como parametro al webservice
     * que se encarga de la facturacion electronica en un modulo complementario
     *
     * @param liquidacion Object
     * @param ruc Object
     * @param formasPago Object
     * @param sol Object
     * @return Object
     * @throws TransformerConfigurationException Exception
     * @throws TransformerException Exception
     * @throws IOException Exception
     */
    public static String generarXmlWs(RegpLiquidacion liquidacion, String ruc, HashMap<String, Object> formasPago, PubSolicitud sol)
            throws TransformerConfigurationException, TransformerException, IOException {
        StreamResult result = new StreamResult(new StringWriter());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            //ELEMENTO XML
            Element xml = doc.createElement("xml");
            doc.appendChild(xml);

            //ELEMENTO FACTURAS
            Element facturas = doc.createElement("facturas");
            xml.appendChild(facturas);

            //ELEMENTO CABECERA
            Element cabecera = doc.createElement("cabecera");
            facturas.appendChild(cabecera);

            // ELEMENTO ENTIDAD_TRIBUTARIA
            Element entidad = doc.createElement("ENTIDAD_TRIBUTARIA");
            entidad.appendChild(doc.createTextNode(ruc));
            cabecera.appendChild(entidad);

            // ELEMENTO FECHA_EMISION
            Element fecha = doc.createElement("FECHA_EMISION");
            fecha.appendChild(doc.createTextNode(sdf.format(liquidacion.getFechaIngreso())));
            cabecera.appendChild(fecha);

            Element cedRuc = doc.createElement("CEDULA_O_RUC");
            cedRuc.appendChild(doc.createTextNode(sol.getBenDocumento()));
            cabecera.appendChild(cedRuc);

            Element propietario = doc.createElement("PROPIETARIO");
            propietario.appendChild(doc.createTextNode(sol.getBenNombres().trim().toUpperCase()));
            cabecera.appendChild(propietario);
            
            Element direccion = doc.createElement("DIRECCION");
            direccion.appendChild(doc.createTextNode(sol.getBenDireccion()));
            cabecera.appendChild(direccion);
            
            Element telefono = doc.createElement("TELEFONO");
            telefono.appendChild(doc.createTextNode(sol.getBenTelefono()));
            cabecera.appendChild(telefono);
            
            Element correo = doc.createElement("CORREO");
            correo.appendChild(doc.createTextNode(sol.getBenCorreo()));
            cabecera.appendChild(correo);

            // ELEMENTO CLAVE_ACCESO
            Element claveAcceso = doc.createElement("CLAVE_ACCESO");
            if (liquidacion.getClaveAcceso() == null || liquidacion.getClaveAcceso().isEmpty()) {
                claveAcceso.appendChild(doc.createTextNode(""));
            } else {
                claveAcceso.appendChild(doc.createTextNode(liquidacion.getClaveAcceso()));
            }
            cabecera.appendChild(claveAcceso);

            // ELEMENTO DESCUENTO_LIM_COBRO
            BigDecimal temp = liquidacion.getDescLimitCobro().add(liquidacion.getGastosGenerales());
            Element descLimCobro = doc.createElement("DESCUENTO_LIM_COBRO");
            //descLimCobro.appendChild(doc.createTextNode(liquidacion.getDescLimitCobro().toString()));
            descLimCobro.appendChild(doc.createTextNode(temp.toString()));
            cabecera.appendChild(descLimCobro);

            // ELEMENTO PAGOS
            Element pagos = doc.createElement("pagos");
            cabecera.appendChild(pagos);
            // AGREGA CADA ELEMENTO PAGO
            for (Map.Entry<String, Object> entrySet : formasPago.entrySet()) {
                // ELEMENTO PAGO
                Element pago = doc.createElement("pago");
                pagos.appendChild(pago);
                // ELEMENTO FORMAPAGO
                Element fo = doc.createElement("FORMAPAGO");
                fo.appendChild(doc.createTextNode(entrySet.getKey()));
                pago.appendChild(fo);
                // ELEMENTO TOTAL
                Element to = doc.createElement("TOTAL");
                to.appendChild(doc.createTextNode(entrySet.getValue().toString()));
                pago.appendChild(to);
            }

            // ELEMENTO DETALLES
            Element detalles = doc.createElement("detalles");
            facturas.appendChild(detalles);
            // AGREGA CADA ELEMENTO DETALLE 
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                // ELEMENTO DETALLE
                Element detalle = doc.createElement("detalle");
                detalles.appendChild(detalle);
                // ELEMENTO CODIGO_PRINCIPAL
                Element cp = doc.createElement("CODIGO_PRINCIPAL");
                cp.appendChild(doc.createTextNode(""));
                detalle.appendChild(cp);
                // ELEMENTO CODIGO_AUXILIAR
                Element ca = doc.createElement("CODIGO_AUXILIAR");
                ca.appendChild(doc.createTextNode(""));
                detalle.appendChild(ca);
                // ELEMENTO DESCRIPCION
                Element descripcion = doc.createElement("DESCRIPCION");
                descripcion.appendChild(doc.createTextNode(det.getActo().getNombre()));
                detalle.appendChild(descripcion);
                // ELEMENTO VALOR
                Element valor = doc.createElement("VALOR");
                valor.appendChild(doc.createTextNode(det.getValorUnitario().toString()));
                detalle.appendChild(valor);
                // ELEMENTO DESCUENTO
                Element descuento = doc.createElement("DESCUENTO");
                descuento.appendChild(doc.createTextNode(det.getDescuento().toString()));
                detalle.appendChild(descuento);
                // ELEMENTO CANTIDAD
                Element cantidad = doc.createElement("CANTIDAD");
                cantidad.appendChild(doc.createTextNode(String.valueOf(det.getCantidad())));
                detalle.appendChild(cantidad);
                // ELEMENTO IVA
                Element iva = doc.createElement("IVA");
                iva.appendChild(doc.createTextNode("0"));
                detalle.appendChild(iva);
            }
            doc.normalize();
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            return result.getWriter().toString();
        } catch (ParserConfigurationException | DOMException e) {
            Logger.getLogger(FacturaXml.class.getName()).log(Level.SEVERE, null, e);
            return "";
        } finally {
            result.getWriter().close();
        }
    }

    public static String generarXmlWs(RenFactura factura)
            throws TransformerConfigurationException, TransformerException, IOException {
        StreamResult result = new StreamResult(new StringWriter());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            //ELEMENTO XML
            Element xml = doc.createElement("xml");
            doc.appendChild(xml);

            //ELEMENTO FACTURAS
            Element facturas = doc.createElement("facturas");
            xml.appendChild(facturas);

            //ELEMENTO CABECERA
            Element cabecera = doc.createElement("cabecera");
            facturas.appendChild(cabecera);

            // ELEMENTO ENTIDAD_TRIBUTARIA
            Element entidad = doc.createElement("ENTIDAD_TRIBUTARIA");
            entidad.appendChild(doc.createTextNode(factura.getRuc()));
            cabecera.appendChild(entidad);

            // ELEMENTO FECHA_EMISION
            Element fecha = doc.createElement("FECHA_EMISION");
            fecha.appendChild(doc.createTextNode(sdf.format(factura.getFecha())));
            cabecera.appendChild(fecha);

            // ELEMENTO CEDULA_RUC
            Element cedRuc = doc.createElement("CEDULA_O_RUC");
            cedRuc.appendChild(doc.createTextNode(factura.getSolicitante().getCiRuc()));
            cabecera.appendChild(cedRuc);

            // ELEMENTO PROPIETARIO
            Element propietario = doc.createElement("PROPIETARIO");
            propietario.appendChild(doc.createTextNode(factura.getSolicitante().getNombreCompleto().toUpperCase().replaceAll("&", "Y")));
            cabecera.appendChild(propietario);

            // ELEMENTO DIRECCION
            Element direccion = doc.createElement("DIRECCION");
            if (factura.getSolicitante().getDireccion() != null) {
                direccion.appendChild(doc.createTextNode(factura.getSolicitante().getDireccion().toUpperCase()));
            } else {
                direccion.appendChild(doc.createTextNode("NINGUNO"));
            }
            cabecera.appendChild(direccion);

            // ELEMENTO TELEFONO
            Element telefono = doc.createElement("TELEFONO");
            if (factura.getSolicitante().getTelefono1() != null) {
                telefono.appendChild(doc.createTextNode(factura.getSolicitante().getTelefono1()));
            } else {
                telefono.appendChild(doc.createTextNode("NINGUNO"));
            }
            cabecera.appendChild(telefono);

            // ELEMENTO CORREO
            Element correo = doc.createElement("CORREO");
            if (factura.getSolicitante().getCorreo1() != null) {
                correo.appendChild(doc.createTextNode(factura.getSolicitante().getCorreo1()));
            } else {
                correo.appendChild(doc.createTextNode(SisVars.correo));
            }
            cabecera.appendChild(correo);

            // ELEMENTO CLAVE_ACCESO
            Element claveAcceso = doc.createElement("CLAVE_ACCESO");
            if (factura.getClaveAcceso() == null || factura.getClaveAcceso().isEmpty()) {
                claveAcceso.appendChild(doc.createTextNode(""));
            } else {
                claveAcceso.appendChild(doc.createTextNode(factura.getClaveAcceso()));
            }
            cabecera.appendChild(claveAcceso);

            // ELEMENTO DESCUENTO_LIM_COBRO
            Element descLimCobro = doc.createElement("DESCUENTO_LIM_COBRO");
            descLimCobro.appendChild(doc.createTextNode(factura.getLiquidacion().getDescLimitCobro().toString()));
            cabecera.appendChild(descLimCobro);

            // ELEMENTO PAGOS
            Element pagos = doc.createElement("pagos");
            cabecera.appendChild(pagos);
            // ELEMENTO PAGO
            Element pago = doc.createElement("pago");
            pagos.appendChild(pago);
            // ELEMENTO FORMAPAGO
            Element fo = doc.createElement("FORMAPAGO");
            fo.appendChild(doc.createTextNode(factura.getFormaPago()));
            pago.appendChild(fo);
            // ELEMENTO TOTAL
            Element to = doc.createElement("TOTAL");
            to.appendChild(doc.createTextNode(factura.getLiquidacion().getTotalPagar().toString()));
            pago.appendChild(to);

            // ELEMENTO DETALLES
            Element detalles = doc.createElement("detalles");
            facturas.appendChild(detalles);
            // AGREGA CADA ELEMENTO DETALLE 
            for (RegpLiquidacionDetalles det : factura.getLiquidacion().getRegpLiquidacionDetallesCollection()) {
                // ELEMENTO DETALLE
                Element detalle = doc.createElement("detalle");
                detalles.appendChild(detalle);
                // ELEMENTO CODIGO_PRINCIPAL
                Element cp = doc.createElement("CODIGO_PRINCIPAL");
                cp.appendChild(doc.createTextNode(""));
                detalle.appendChild(cp);
                // ELEMENTO CODIGO_AUXILIAR
                Element ca = doc.createElement("CODIGO_AUXILIAR");
                ca.appendChild(doc.createTextNode(""));
                detalle.appendChild(ca);
                // ELEMENTO DESCRIPCION
                Element descripcion = doc.createElement("DESCRIPCION");
                descripcion.appendChild(doc.createTextNode(det.getActo().getNombre()));
                detalle.appendChild(descripcion);
                // ELEMENTO VALOR
                Element valor = doc.createElement("VALOR");
                valor.appendChild(doc.createTextNode(det.getValorUnitario().toString()));
                detalle.appendChild(valor);
                // ELEMENTO DESCUENTO
                Element descuento = doc.createElement("DESCUENTO");
                descuento.appendChild(doc.createTextNode(det.getDescuento().toString()));
                detalle.appendChild(descuento);
                // ELEMENTO CANTIDAD
                Element cantidad = doc.createElement("CANTIDAD");
                cantidad.appendChild(doc.createTextNode(String.valueOf(det.getCantidad())));
                detalle.appendChild(cantidad);
                // ELEMENTO IVA
                Element iva = doc.createElement("IVA");
                iva.appendChild(doc.createTextNode("0"));
                detalle.appendChild(iva);
            }
            doc.normalize();
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            return result.getWriter().toString();
        } catch (ParserConfigurationException | DOMException e) {
            Logger.getLogger(FacturaXml.class.getName()).log(Level.SEVERE, null, e);
            return "";
        } finally {
            result.getWriter().close();
        }
    }

    /**
     * Retorna texto en formato xml que se envia como parametro al webservice
     * que se encarga de la generacion de notas de credito en un modulo
     * complementario
     *
     * @param liquidacion Object
     * @param ruc Object
     * @param nc Object
     * @return Object
     * @throws TransformerConfigurationException Exception
     * @throws TransformerException Exception
     * @throws IOException Exception
     */
    public static String generarXmlNCWs(RegpLiquidacion liquidacion, String ruc, RenNotaCredito nc)
            throws TransformerConfigurationException, TransformerException, IOException {
        StreamResult result = new StreamResult(new StringWriter());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            //ELEMENTO XML
            Element xml = doc.createElement("xml");
            doc.appendChild(xml);

            //ELEMENTO FACTURAS
            Element facturas = doc.createElement("facturas");
            xml.appendChild(facturas);

            //ELEMENTO CABECERA
            Element cabecera = doc.createElement("cabecera");
            facturas.appendChild(cabecera);

            // ELEMENTO ENTIDAD_TRIBUTARIA
            Element entidad = doc.createElement("ENTIDAD_TRIBUTARIA");
            entidad.appendChild(doc.createTextNode(ruc));
            cabecera.appendChild(entidad);

            // ELEMENTO FECHA_EMISION
            Element fecha = doc.createElement("FECHA_EMISION");
            fecha.appendChild(doc.createTextNode(sdf.format(new Date())));
            cabecera.appendChild(fecha);

            if (liquidacion.getBeneficiario() == null) {
                // ELEMENTO CEDULA_RUC
                Element cedRuc = doc.createElement("CEDULA_O_RUC");
                cedRuc.appendChild(doc.createTextNode(liquidacion.getSolicitante().getCiRuc()));
                cabecera.appendChild(cedRuc);
                // ELEMENTO PROPIETARIO
                Element propietario = doc.createElement("PROPIETARIO");
                propietario.appendChild(doc.createTextNode(liquidacion.getSolicitante().getNombreCompleto().toUpperCase().replaceAll("&", "Y")));
                cabecera.appendChild(propietario);
                // ELEMENTO DIRECCION
                Element direccion = doc.createElement("DIRECCION");
                if (liquidacion.getSolicitante().getDireccion() != null) {
                    direccion.appendChild(doc.createTextNode(liquidacion.getSolicitante().getDireccion().toUpperCase()));
                } else {
                    direccion.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(direccion);
                // ELEMENTO TELEFONO
                Element telefono = doc.createElement("TELEFONO");
                if (liquidacion.getSolicitante().getTelefono1() != null) {
                    telefono.appendChild(doc.createTextNode(liquidacion.getSolicitante().getTelefono1()));
                } else {
                    telefono.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(telefono);
                // ELEMENTO CORREO
                Element correo = doc.createElement("CORREO");
                if (liquidacion.getSolicitante().getCorreo1() != null) {
                    correo.appendChild(doc.createTextNode(liquidacion.getSolicitante().getCorreo1()));
                } else {
                    correo.appendChild(doc.createTextNode(SisVars.correo));
                }
                cabecera.appendChild(correo);
            } else {
                // ELEMENTO CEDULA_RUC
                Element cedRuc = doc.createElement("CEDULA_O_RUC");
                cedRuc.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getCiRuc()));
                cabecera.appendChild(cedRuc);
                // ELEMENTO PROPIETARIO
                Element propietario = doc.createElement("PROPIETARIO");
                propietario.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getNombreCompleto().toUpperCase().replaceAll("&", "Y")));
                cabecera.appendChild(propietario);
                // ELEMENTO DIRECCION
                Element direccion = doc.createElement("DIRECCION");
                if (liquidacion.getBeneficiario().getDireccion() != null) {
                    direccion.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getDireccion().toUpperCase()));
                } else {
                    direccion.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(direccion);
                // ELEMENTO TELEFONO
                Element telefono = doc.createElement("TELEFONO");
                if (liquidacion.getBeneficiario().getTelefono1() != null) {
                    telefono.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getTelefono1()));
                } else {
                    telefono.appendChild(doc.createTextNode("NINGUNO"));
                }
                cabecera.appendChild(telefono);
                // ELEMENTO CORREO
                Element correo = doc.createElement("CORREO");
                if (liquidacion.getBeneficiario().getCorreo1() != null) {
                    correo.appendChild(doc.createTextNode(liquidacion.getBeneficiario().getCorreo1()));
                } else {
                    correo.appendChild(doc.createTextNode(SisVars.correo));
                }
                cabecera.appendChild(correo);
            }

            // ELEMENTO CODIGO_MODIFICADO
            Element codigo = doc.createElement("CODIGO_MODIFICADO");
            codigo.appendChild(doc.createTextNode("01")); //CODIGO DE TIPO DE DOCUMENTO FACTURA
            cabecera.appendChild(codigo);

            String factura;
            // ELEMENTO NUMERO_DOCUMENTO_SUSTENTO
            Element numero = doc.createElement("NUMERO_DOCUMENTO_SUSTENTO");
            if (liquidacion.getCodigoComprobante().length() == 15) {
                factura = liquidacion.getCodigoComprobante().substring(6);
                factura = liquidacion.getCodigoComprobante().substring(3, 6) + "-" + factura;
                factura = liquidacion.getCodigoComprobante().substring(0, 3) + "-" + factura;
            } else {
                factura = liquidacion.getCodigoComprobante().substring(3);
                factura = "001-" + liquidacion.getCodigoComprobante().substring(0, 3) + "-" + factura;
            }
            numero.appendChild(doc.createTextNode(factura));
            //numero.appendChild(doc.createTextNode(liquidacion.getCodigoComprobante()));
            cabecera.appendChild(numero);

            // ELEMENTO FECHA_EMISION_SUSTENTO
            Element emision = doc.createElement("FECHA_EMISION_SUSTENTO");
            emision.appendChild(doc.createTextNode(sdf.format(liquidacion.getFechaIngreso())));
            cabecera.appendChild(emision);

            // ELEMENTO MOTIVO
            Element motivo = doc.createElement("MOTIVO");
            if (nc.getMotivo() == null) {
                motivo.appendChild(doc.createTextNode("ANULACION DE FACTURA POR ERROR DE EMISION"));
            } else {
                motivo.appendChild(doc.createTextNode(nc.getMotivo()));
            }
            cabecera.appendChild(motivo);

            // ELEMENTO CLAVE_ACCESO
            Element claveAcceso = doc.createElement("CLAVE_ACCESO");
            if (nc.getClaveAcceso() == null) {
                claveAcceso.appendChild(doc.createTextNode(""));
            } else {
                claveAcceso.appendChild(doc.createTextNode(nc.getClaveAcceso()));
            }
            cabecera.appendChild(claveAcceso);

            // ELEMENTO DETALLES
            Element detalles = doc.createElement("detalles");
            facturas.appendChild(detalles);

            // ELEMENTO DETALLE
            Element detalle = doc.createElement("detalle");
            detalles.appendChild(detalle);
            // ELEMENTO CODIGO_PRINCIPAL
            Element cp = doc.createElement("CODIGO_PRINCIPAL");
            cp.appendChild(doc.createTextNode(""));
            detalle.appendChild(cp);
            // ELEMENTO CODIGO_AUXILIAR
            Element ca = doc.createElement("CODIGO_AUXILIAR");
            ca.appendChild(doc.createTextNode(""));
            detalle.appendChild(ca);
            // ELEMENTO DESCRIPCION
            Element descripcion = doc.createElement("DESCRIPCION");
            descripcion.appendChild(doc.createTextNode("FACTURA No " + liquidacion.getCodigoComprobante()));
            detalle.appendChild(descripcion);
            // ELEMENTO VALOR
            Element valor = doc.createElement("VALOR");
            valor.appendChild(doc.createTextNode(nc.getValorTotal().toString()));
            detalle.appendChild(valor);
            // ELEMENTO DESCUENTO
            Element descuento = doc.createElement("DESCUENTO");
            descuento.appendChild(doc.createTextNode("0.00"));
            detalle.appendChild(descuento);
            // ELEMENTO CANTIDAD
            Element cantidad = doc.createElement("CANTIDAD");
            cantidad.appendChild(doc.createTextNode("1"));
            detalle.appendChild(cantidad);
            // ELEMENTO IVA
            Element iva = doc.createElement("IVA");
            iva.appendChild(doc.createTextNode("0"));
            detalle.appendChild(iva);

            doc.normalize();
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            return result.getWriter().toString();
        } catch (ParserConfigurationException | DOMException e) {
            Logger.getLogger(FacturaXml.class.getName()).log(Level.SEVERE, null, e);
            return "";
        } finally {
            result.getWriter().close();
        }
    }

    /**
     * Devuelve el contenido de una etiqueta dentro de un texto en formato xml
     *
     * @param cadena Object
     * @param valor Object
     * @return Object
     */
    public static String getValoresFromXmlWS(String cadena, String valor) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(cadena.getBytes()));
            NodeList list = doc.getElementsByTagName(valor);
            if (list.item(0) != null) {
                return list.item(0).getTextContent();
            } else {
                return "";
            }
        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
            Logger.getLogger(FacturaXml.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Genera un archivo xml con que representa a la factura electronica recien
     * generada, es decir antes de ser firmada y autorizada por el SRI
     *
     * @param liquidacion Object
     * @param caja Object
     * @param dfe Object
     * @throws TransformerConfigurationException Exception
     * @throws TransformerException Exception
     */
    public static void generarArchivoXml(RegpLiquidacion liquidacion, RenCajero caja, RenDatosFacturaElectronica dfe)
            throws TransformerConfigurationException, TransformerException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // CREACION DE ESTRUCTURA DEL DOCUMENTO XML EN MEMORIA
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            // ELEMENTO FACTURA Y ATRIBUTOS ID, VERSION
            Element factura = doc.createElement("factura");
            Attr attr1 = doc.createAttribute("id");
            attr1.setValue("comprobante");
            factura.setAttributeNode(attr1);
            Attr attr2 = doc.createAttribute("version");
            attr2.setValue("1.1.0");
            factura.setAttributeNode(attr2);
            doc.appendChild(factura);

            // ELEMENTO INFOTRIBUTARIA
            Element infoTributaria = doc.createElement("infoTributaria");
            factura.appendChild(infoTributaria);
            // ELEMENTO AMBIENTE
            Element ambiente = doc.createElement("ambiente");
            ambiente.appendChild(doc.createTextNode(dfe.getAmbiente()));
            infoTributaria.appendChild(ambiente);
            // ELEMENTO TIPOEMISION
            Element tipoEmision = doc.createElement("tipoEmision");
            tipoEmision.appendChild(doc.createTextNode(dfe.getTipoEmision()));
            infoTributaria.appendChild(tipoEmision);
            // ELEMENTO RAZONSOCIAL
            Element razonSocial = doc.createElement("razonSocial");
            razonSocial.appendChild(doc.createTextNode(dfe.getRazonSocial()));
            infoTributaria.appendChild(razonSocial);
            // ELEMENTO RUC
            Element ruc = doc.createElement("ruc");
            ruc.appendChild(doc.createTextNode(dfe.getRuc()));
            infoTributaria.appendChild(ruc);
            // ELEMENTO CLAVEACCESO
            Element claveAcceso = doc.createElement("claveAcceso");
            claveAcceso.appendChild(doc.createTextNode(liquidacion.getClaveAcceso()));
            infoTributaria.appendChild(claveAcceso);
            // ELEMENTO CODDOC
            Element codDoc = doc.createElement("codDoc");
            codDoc.appendChild(doc.createTextNode(dfe.getTipoComprobante()));
            infoTributaria.appendChild(codDoc);
            // ELEMENTO ESTAB
            Element estab = doc.createElement("estab");
            estab.appendChild(doc.createTextNode(dfe.getSerie()));
            infoTributaria.appendChild(estab);
            // ELEMENTO PTOEMI
            Element ptoEmi = doc.createElement("ptoEmi");
            ptoEmi.appendChild(doc.createTextNode(caja.getCodigoCaja()));
            infoTributaria.appendChild(ptoEmi);
            // ELEMENTO SECUENCIAL
            Element secuencial = doc.createElement("secuencial");
            secuencial.appendChild(doc.createTextNode(Utils.completarCadenaConCeros(liquidacion.getNumeroComprobante().toString(), 9)));
            infoTributaria.appendChild(secuencial);
            // ELEMENTO DIRMATRIZ
            Element dirMatriz = doc.createElement("dirMatriz");
            dirMatriz.appendChild(doc.createTextNode(dfe.getDireccionMatriz()));
            infoTributaria.appendChild(dirMatriz);

            // ELEMENTO INFOFACTURA
            Element infoFactura = doc.createElement("infoFactura");
            factura.appendChild(infoFactura);
            // ELEMENTO FECHAEMISION
            Element fechaEmision = doc.createElement("fechaEmision");
            fechaEmision.appendChild(doc.createTextNode(sdf.format(liquidacion.getFechaIngreso())));
            infoFactura.appendChild(fechaEmision);
            // ELEMENTO OBLIGADOCONTABILIDAD
            Element obligadoContabilidad = doc.createElement("obligadoContabilidad");
            obligadoContabilidad.appendChild(doc.createTextNode("SI"));
            infoFactura.appendChild(obligadoContabilidad);
            // ELEMENTO TIPOIDENTIFICACIONCOMPRADOR
            Element tipoIdentificacionComprador = doc.createElement("tipoIdentificacionComprador");
            if (liquidacion.getSolicitante().getTipoIdentificacion().equalsIgnoreCase("P")) {
                tipoIdentificacionComprador.appendChild(doc.createTextNode("06"));
            } else if (liquidacion.getSolicitante().getTipoIdentificacion().equalsIgnoreCase("R")) {
                tipoIdentificacionComprador.appendChild(doc.createTextNode("04"));
            } else {
                tipoIdentificacionComprador.appendChild(doc.createTextNode("05"));
            }
            infoFactura.appendChild(tipoIdentificacionComprador);
            // ELEMENTO RAZONSOCIALCOMPRADOR
            Element razonSocialComprador = doc.createElement("razonSocialComprador");
            razonSocialComprador.appendChild(doc.createTextNode(liquidacion.getSolicitante().getNombreCompleto().toUpperCase()));
            infoFactura.appendChild(razonSocialComprador);
            // ELEMENTO IDENTIFICACIONCOMPRADOR
            Element identificacionComprador = doc.createElement("identificacionComprador");
            identificacionComprador.appendChild(doc.createTextNode(liquidacion.getSolicitante().getCiRuc()));
            infoFactura.appendChild(identificacionComprador);
            // ELEMENTO DIRECCIONCOMPRADOR
            Element direccionComprador = doc.createElement("direccionComprador");
            if (liquidacion.getSolicitante().getDireccion() != null) {
                direccionComprador.appendChild(doc.createTextNode(liquidacion.getSolicitante().getDireccion().toUpperCase()));
            } else {
                direccionComprador.appendChild(doc.createTextNode("PORTOVIEJO"));
            }
            infoFactura.appendChild(direccionComprador);
            // ELEMENTO TOTALSINIMPUESTOS
            Element totalSinImpuestos = doc.createElement("totalSinImpuestos");
            BigDecimal subTotal = liquidacion.getTotalPagar().add(liquidacion.getDescLimitCobro());
            totalSinImpuestos.appendChild(doc.createTextNode(subTotal.setScale(2).toString()));
            infoFactura.appendChild(totalSinImpuestos);
            // ELEMENTO TOTALDESCUENTO
            Element totalDescuento = doc.createElement("totalDescuento");
            //totalDescuento.appendChild(doc.createTextNode(liquidacion.getDescLimitCobro().toString()));
            BigDecimal descuentos = liquidacion.getDescuentoValor().add(liquidacion.getDescLimitCobro());
            totalDescuento.appendChild(doc.createTextNode(descuentos.setScale(2).toString()));
            infoFactura.appendChild(totalDescuento);
            // ELEMENTO TOTALCONIMPUESTOS
            Element totalConImpuestos = doc.createElement("totalConImpuestos");
            infoFactura.appendChild(totalConImpuestos);
            // ELEMENTO TOTALIMPUESTOS
            Element totalImpuesto = doc.createElement("totalImpuesto");
            totalConImpuestos.appendChild(totalImpuesto);
            // ELEMENTO CODIGO
            Element codigo = doc.createElement("codigo");
            codigo.appendChild(doc.createTextNode(dfe.getTipoImpuesto()));
            totalImpuesto.appendChild(codigo);
            // ELEMENTO CODIGOPORCENTAJE
            Element codigoPorcentaje = doc.createElement("codigoPorcentaje");
            codigoPorcentaje.appendChild(doc.createTextNode(dfe.getTipoPorcentajeImpuesto()));
            totalImpuesto.appendChild(codigoPorcentaje);
            // ELEMENTO DESCUENTOADICIONAL
            Element descuentoAdicional = doc.createElement("descuentoAdicional");
            descuentoAdicional.appendChild(doc.createTextNode(liquidacion.getDescLimitCobro().toString()));
            totalImpuesto.appendChild(descuentoAdicional);
            // ELEMENTO BASEIMPONIBLE
            Element baseImponible = doc.createElement("baseImponible");
            baseImponible.appendChild(doc.createTextNode(subTotal.setScale(2).toString()));
            totalImpuesto.appendChild(baseImponible);
            // ELEMENTO VALOR
            Element valor = doc.createElement("valor");
            valor.appendChild(doc.createTextNode("0.00"));
            totalImpuesto.appendChild(valor);
            // ELEMENTO PROPINA
            Element propina = doc.createElement("propina");
            propina.appendChild(doc.createTextNode("0.00"));
            infoFactura.appendChild(propina);
            // ELEMENTO IMPORTETOTAL
            Element importeTotal = doc.createElement("importeTotal");
            importeTotal.appendChild(doc.createTextNode(liquidacion.getTotalPagar().toString()));
            infoFactura.appendChild(importeTotal);
            // ELEMENTO MONEDA
            Element moneda = doc.createElement("moneda");
            moneda.appendChild(doc.createTextNode(dfe.getMoneda()));
            infoFactura.appendChild(moneda);
            // ELEMENTO PAGOS
            Element pagos = doc.createElement("pagos");
            infoFactura.appendChild(pagos);
            // ELEMENTO PAGO
            Element pago = doc.createElement("pago");
            pagos.appendChild(pago);
            // ELEMENTO FORMAPAGO
            Element formaPago = doc.createElement("formaPago");
            formaPago.appendChild(doc.createTextNode(dfe.getFormaPago()));
            pago.appendChild(formaPago);
            // ELEMENTO TOTAL
            Element total = doc.createElement("total");
            total.appendChild(doc.createTextNode(liquidacion.getTotalPagar().toString()));
            pago.appendChild(total);
            // SI LA LIQUIDACION ES CTA POR COBRAR SE AGREGAN DOS ELEMENTOS
            if (liquidacion.getEstadoPago().getId() == 3L) {
                // ELEMENTO PLAZO
                Element plazo = doc.createElement("plazo");
                plazo.appendChild(doc.createTextNode(dfe.getDiasPlazo()));
                pago.appendChild(plazo);
                // ELEMENTO UNIDADTIEMPO
                Element unidadTiempo = doc.createElement("unidadTiempo");
                unidadTiempo.appendChild(doc.createTextNode(dfe.getUnidadTiempo()));
                pago.appendChild(unidadTiempo);
            }
            // ELEMENTO DETALLES
            Element detalles = doc.createElement("detalles");
            factura.appendChild(detalles);
            // AGREGA CADA ELEMENTO DETALLE 
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                // ELEMENTO DETALLE
                Element detalle = doc.createElement("detalle");
                detalles.appendChild(detalle);
                // ELEMENTO DESCRIPCION
                Element descripcion = doc.createElement("descripcion");
                descripcion.appendChild(doc.createTextNode(det.getActo().getNombre()));
                detalle.appendChild(descripcion);
                // ELEMENTO CANTIDAD
                Element cantidad = doc.createElement("cantidad");
                BigDecimal cant = BigDecimal.valueOf(det.getCantidad()).setScale(6);
                cantidad.appendChild(doc.createTextNode(cant.toString()));
                detalle.appendChild(cantidad);
                // ELEMENTO PRECIOUNITARIO
                Element precioUnitario = doc.createElement("precioUnitario");
                BigDecimal precio = det.getValorUnitario().setScale(6);
                precioUnitario.appendChild(doc.createTextNode(precio.toString()));
                detalle.appendChild(precioUnitario);
                // ELEMENTO DESCUENTO
                Element descuento = doc.createElement("descuento");
                descuento.appendChild(doc.createTextNode(det.getDescuento().toString()));
                detalle.appendChild(descuento);
                // ELEMENTO PRECIO
                Element precioTotalSinImpuesto = doc.createElement("precioTotalSinImpuesto");
                precioTotalSinImpuesto.appendChild(doc.createTextNode(det.getValorTotal().toString()));
                detalle.appendChild(precioTotalSinImpuesto);
                // ELEMENTO IMPUESTOS
                Element impuestos = doc.createElement("impuestos");
                detalle.appendChild(impuestos);
                // ELEMENTO IMPUESTO
                Element impuesto = doc.createElement("impuesto");
                impuestos.appendChild(impuesto);
                // ELEMENT CODIGO
                Element codigoDet = doc.createElement("codigo");
                codigoDet.appendChild(doc.createTextNode(dfe.getTipoImpuesto()));
                impuesto.appendChild(codigoDet);
                // ELEMENT CODIGOPORCENTAJE
                Element codigoPorcentajeDet = doc.createElement("codigoPorcentaje");
                codigoPorcentajeDet.appendChild(doc.createTextNode(dfe.getTipoPorcentajeImpuesto()));
                impuesto.appendChild(codigoPorcentajeDet);
                // ELEMENT TARIFA
                Element tarifa = doc.createElement("tarifa");
                tarifa.appendChild(doc.createTextNode("0"));
                impuesto.appendChild(tarifa);
                // ELEMENT BASEIMPONIBLE
                Element baseImponibleDet = doc.createElement("baseImponible");
                baseImponibleDet.appendChild(doc.createTextNode(det.getValorTotal().toString()));
                impuesto.appendChild(baseImponibleDet);
                // ELEMENT VALOR
                Element valorDet = doc.createElement("valor");
                valorDet.appendChild(doc.createTextNode("0.00"));
                impuesto.appendChild(valorDet);
            }
            // CREACION DEL ARCHIVO XML
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            //transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            DOMSource source = new DOMSource(doc);
            File archivo = new File(caja.getRutaComprobantesGenerados() + liquidacion.getClaveAcceso() + ".xml");
            StreamResult result = new StreamResult(archivo.getPath());
            transformer.transform(source, result);
        } catch (ParserConfigurationException | DOMException e) {
            Logger.getLogger(FacturaXml.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
