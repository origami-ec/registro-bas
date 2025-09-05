//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.09.22 a las 09:40:37 AM COT 
//
package com.origami.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Clase Java para anonymous complex type.
 *
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que
 * haya en esta clase.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TRAMITE" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="NIT"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="1"/&gt;
 *                         &lt;maxLength value="20"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="FCR"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;pattern value="[0-9]*"/&gt;
 *                         &lt;minLength value="8"/&gt;
 *                         &lt;maxLength value="8"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="TTM"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="2"/&gt;
 *                         &lt;maxLength value="2"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="DTM"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="10"/&gt;
 *                         &lt;maxLength value="250"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CCA"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="1"/&gt;
 *                         &lt;maxLength value="50"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="VCC" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *                   &lt;element name="TTB"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="3"/&gt;
 *                         &lt;maxLength value="3"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="DRB"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="3"/&gt;
 *                         &lt;maxLength value="150"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CBC"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="4"/&gt;
 *                         &lt;maxLength value="4"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CDR"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="4"/&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="FCT"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;pattern value="[0-9]*"/&gt;
 *                         &lt;minLength value="8"/&gt;
 *                         &lt;maxLength value="8"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="INTERVINIENTE" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="TII"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="IDI"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="3"/&gt;
 *                                   &lt;maxLength value="20"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NRI"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="5"/&gt;
 *                                   &lt;maxLength value="150"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAI"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="3"/&gt;
 *                                   &lt;maxLength value="3"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="RDI"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="2"/&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PDI"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="2"/&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tramite"
})
@XmlRootElement(name = "estructura")
public class Estructura {

    @XmlElement(name = "TRAMITE", required = true)
    protected List<Estructura.TRAMITE> tramite;

    /**
     * Gets the value of the tramite property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the tramite property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTRAMITE().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Estructura.TRAMITE }
     *
     *
     * @return possible object is {@link List }
     */
    public List<Estructura.TRAMITE> getTRAMITE() {
        if (tramite == null) {
            tramite = new ArrayList<Estructura.TRAMITE>();
        }
        return this.tramite;
    }

    /**
     * <p>
     * Clase Java para anonymous complex type.
     *
     * <p>
     * El siguiente fragmento de esquema especifica el contenido que se espera
     * que haya en esta clase.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="NIT"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="1"/&gt;
     *               &lt;maxLength value="20"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="FCR"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="[0-9]*"/&gt;
     *               &lt;minLength value="8"/&gt;
     *               &lt;maxLength value="8"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="TTM"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="2"/&gt;
     *               &lt;maxLength value="2"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="DTM"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="10"/&gt;
     *               &lt;maxLength value="250"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CCA"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="1"/&gt;
     *               &lt;maxLength value="50"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="VCC" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
     *         &lt;element name="TTB"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="3"/&gt;
     *               &lt;maxLength value="3"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="DRB"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="3"/&gt;
     *               &lt;maxLength value="150"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CBC"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="4"/&gt;
     *               &lt;maxLength value="4"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CDR"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="4"/&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="FCT"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="[0-9]*"/&gt;
     *               &lt;minLength value="8"/&gt;
     *               &lt;maxLength value="8"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="INTERVINIENTE" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="TII"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="IDI"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="3"/&gt;
     *                         &lt;maxLength value="20"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NRI"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="5"/&gt;
     *                         &lt;maxLength value="150"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAI"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="3"/&gt;
     *                         &lt;maxLength value="3"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="RDI"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="2"/&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PDI"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="2"/&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "nit",
        "fcr",
        "ttm",
        "dtm",
        "cca",
        "vcc",
        "ttb",
        "drb",
        "cbc",
        "cdr",
        "fct",
        "interviniente"
    })
    public static class TRAMITE {

        @XmlElement(name = "NIT", required = true)
        protected String nit;
        @XmlElement(name = "FCR", required = true)
        protected String fcr;
        @XmlElement(name = "TTM", required = true)
        protected String ttm;
        @XmlElement(name = "DTM", required = true)
        protected String dtm;
        @XmlElement(name = "CCA", required = true)
        protected String cca;
        @XmlElement(name = "VCC", required = true)
        protected BigInteger vcc;
        @XmlElement(name = "TTB", required = true)
        protected String ttb;
        @XmlElement(name = "DRB", required = true)
        protected String drb;
        @XmlElement(name = "CBC", required = true)
        protected String cbc;
        @XmlElement(name = "CDR", required = true)
        protected String cdr;
        @XmlElement(name = "FCT", required = true)
        protected String fct;
        @XmlElement(name = "INTERVINIENTE", required = true)
        protected List<Estructura.TRAMITE.INTERVINIENTE> interviniente;

        /**
         * Obtiene el valor de la propiedad nit.
         *
         * @return possible object is {@link String }
         *
         */
        public String getNIT() {
            return nit;
        }

        /**
         * Define el valor de la propiedad nit.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setNIT(String value) {
            this.nit = value;
        }

        /**
         * Obtiene el valor de la propiedad fcr.
         *
         * @return possible object is {@link String }
         *
         */
        public String getFCR() {
            return fcr;
        }

        /**
         * Define el valor de la propiedad fcr.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setFCR(String value) {
            this.fcr = value;
        }

        /**
         * Obtiene el valor de la propiedad ttm.
         *
         * @return possible object is {@link String }
         *
         */
        public String getTTM() {
            return ttm;
        }

        /**
         * Define el valor de la propiedad ttm.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setTTM(String value) {
            this.ttm = value;
        }

        /**
         * Obtiene el valor de la propiedad dtm.
         *
         * @return possible object is {@link String }
         *
         */
        public String getDTM() {
            return dtm;
        }

        /**
         * Define el valor de la propiedad dtm.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setDTM(String value) {
            this.dtm = value;
        }

        /**
         * Obtiene el valor de la propiedad cca.
         *
         * @return possible object is {@link String }
         *
         */
        public String getCCA() {
            return cca;
        }

        /**
         * Define el valor de la propiedad cca.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setCCA(String value) {
            this.cca = value;
        }

        /**
         * Obtiene el valor de la propiedad vcc.
         *
         * @return possible object is {@link BigInteger }
         *
         */
        public BigInteger getVCC() {
            return vcc;
        }

        /**
         * Define el valor de la propiedad vcc.
         *
         * @param value allowed object is {@link BigInteger }
         *
         */
        public void setVCC(BigInteger value) {
            this.vcc = value;
        }

        /**
         * Obtiene el valor de la propiedad ttb.
         *
         * @return possible object is {@link String }
         *
         */
        public String getTTB() {
            return ttb;
        }

        /**
         * Define el valor de la propiedad ttb.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setTTB(String value) {
            this.ttb = value;
        }

        /**
         * Obtiene el valor de la propiedad drb.
         *
         * @return possible object is {@link String }
         *
         */
        public String getDRB() {
            return drb;
        }

        /**
         * Define el valor de la propiedad drb.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setDRB(String value) {
            this.drb = value;
        }

        /**
         * Obtiene el valor de la propiedad cbc.
         *
         * @return possible object is {@link String }
         *
         */
        public String getCBC() {
            return cbc;
        }

        /**
         * Define el valor de la propiedad cbc.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setCBC(String value) {
            this.cbc = value;
        }

        /**
         * Obtiene el valor de la propiedad cdr.
         *
         * @return possible object is {@link String }
         *
         */
        public String getCDR() {
            return cdr;
        }

        /**
         * Define el valor de la propiedad cdr.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setCDR(String value) {
            this.cdr = value;
        }

        /**
         * Obtiene el valor de la propiedad fct.
         *
         * @return possible object is {@link String }
         *
         */
        public String getFCT() {
            return fct;
        }

        /**
         * Define el valor de la propiedad fct.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setFCT(String value) {
            this.fct = value;
        }

        /**
         * Gets the value of the interviniente property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the interviniente property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getINTERVINIENTE().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Estructura.TRAMITE.INTERVINIENTE }
         *
         *
         * @return possible object is {@link List }
         */
        public List<Estructura.TRAMITE.INTERVINIENTE> getINTERVINIENTE() {
            if (interviniente == null) {
                interviniente = new ArrayList<>();
            }
            return this.interviniente;
        }

        /**
         * <p>
         * Clase Java para anonymous complex type.
         *
         * <p>
         * El siguiente fragmento de esquema especifica el contenido que se
         * espera que haya en esta clase.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="TII"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="1"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="IDI"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="3"/&gt;
         *               &lt;maxLength value="20"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NRI"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="5"/&gt;
         *               &lt;maxLength value="150"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAI"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="3"/&gt;
         *               &lt;maxLength value="3"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="RDI"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="2"/&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PDI"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="2"/&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "tii",
            "idi",
            "nri",
            "nai",
            "rdi",
            "pdi"
        })
        public static class INTERVINIENTE {

            @XmlElement(name = "TII", required = true)
            protected String tii;
            @XmlElement(name = "IDI", required = true)
            protected String idi;
            @XmlElement(name = "NRI", required = true)
            protected String nri;
            @XmlElement(name = "NAI", required = true)
            protected String nai;
            @XmlElement(name = "RDI", required = true)
            protected String rdi;
            @XmlElement(name = "PDI", required = true)
            protected String pdi;

            /**
             * Obtiene el valor de la propiedad tii.
             *
             * @return possible object is {@link String }
             *
             */
            public String getTII() {
                return tii;
            }

            /**
             * Define el valor de la propiedad tii.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setTII(String value) {
                this.tii = value;
            }

            /**
             * Obtiene el valor de la propiedad idi.
             *
             * @return possible object is {@link String }
             *
             */
            public String getIDI() {
                return idi;
            }

            /**
             * Define el valor de la propiedad idi.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setIDI(String value) {
                this.idi = value;
            }

            /**
             * Obtiene el valor de la propiedad nri.
             *
             * @return possible object is {@link String }
             *
             */
            public String getNRI() {
                return nri;
            }

            /**
             * Define el valor de la propiedad nri.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setNRI(String value) {
                this.nri = value;
            }

            /**
             * Obtiene el valor de la propiedad nai.
             *
             * @return possible object is {@link String }
             *
             */
            public String getNAI() {
                return nai;
            }

            /**
             * Define el valor de la propiedad nai.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setNAI(String value) {
                this.nai = value;
            }

            /**
             * Obtiene el valor de la propiedad rdi.
             *
             * @return possible object is {@link String }
             *
             */
            public String getRDI() {
                return rdi;
            }

            /**
             * Define el valor de la propiedad rdi.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setRDI(String value) {
                this.rdi = value;
            }

            /**
             * Obtiene el valor de la propiedad pdi.
             *
             * @return possible object is {@link String }
             *
             */
            public String getPDI() {
                return pdi;
            }

            /**
             * Define el valor de la propiedad pdi.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setPDI(String value) {
                this.pdi = value;
            }

        }

    }

}
