//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.09.22 a las 09:40:37 AM COT 
//


package com.origami.jaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CLAVE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CABECERA"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CDR"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="4"/&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="PDR"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;pattern value="[0-9]*"/&gt;
 *                         &lt;minLength value="8"/&gt;
 *                         &lt;maxLength value="8"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="FRE"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;pattern value="[0-9]*"/&gt;
 *                         &lt;minLength value="8"/&gt;
 *                         &lt;maxLength value="8"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="USR"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;minLength value="5"/&gt;
 *                         &lt;maxLength value="20"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="NRT" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *                   &lt;element name="INT" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *                   &lt;element name="TVU" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
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
    "clave",
    "cabecera"
})
@XmlRootElement(name = "SEGURIDAD")
public class SEGURIDAD {

    @XmlElement(name = "CLAVE", required = true)
    protected String clave;
    @XmlElement(name = "CABECERA", required = true)
    protected SEGURIDAD.CABECERA cabecera;

    /**
     * Obtiene el valor de la propiedad clave.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLAVE() {
        return clave;
    }

    /**
     * Define el valor de la propiedad clave.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLAVE(String value) {
        this.clave = value;
    }

    /**
     * Obtiene el valor de la propiedad cabecera.
     * 
     * @return
     *     possible object is
     *     {@link SEGURIDAD.CABECERA }
     *     
     */
    public SEGURIDAD.CABECERA getCABECERA() {
        return cabecera;
    }

    /**
     * Define el valor de la propiedad cabecera.
     * 
     * @param value
     *     allowed object is
     *     {@link SEGURIDAD.CABECERA }
     *     
     */
    public void setCABECERA(SEGURIDAD.CABECERA value) {
        this.cabecera = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="CDR"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="4"/&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="PDR"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="[0-9]*"/&gt;
     *               &lt;minLength value="8"/&gt;
     *               &lt;maxLength value="8"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="FRE"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="[0-9]*"/&gt;
     *               &lt;minLength value="8"/&gt;
     *               &lt;maxLength value="8"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="USR"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;minLength value="5"/&gt;
     *               &lt;maxLength value="20"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="NRT" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
     *         &lt;element name="INT" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
     *         &lt;element name="TVU" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
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
        "cdr",
        "pdr",
        "fre",
        "usr",
        "nrt",
        "_int",
        "tvu"
    })
    public static class CABECERA {

        @XmlElement(name = "CDR", required = true)
        protected String cdr;
        @XmlElement(name = "PDR", required = true)
        protected String pdr;
        @XmlElement(name = "FRE", required = true)
        protected String fre;
        @XmlElement(name = "USR", required = true)
        protected String usr;
        @XmlElement(name = "NRT", required = true)
        protected BigInteger nrt;
        @XmlElement(name = "INT", required = true)
        protected BigInteger _int;
        @XmlElement(name = "TVU", required = true)
        protected BigInteger tvu;

        /**
         * Obtiene el valor de la propiedad cdr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCDR() {
            return cdr;
        }

        /**
         * Define el valor de la propiedad cdr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCDR(String value) {
            this.cdr = value;
        }

        /**
         * Obtiene el valor de la propiedad pdr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDR() {
            return pdr;
        }

        /**
         * Define el valor de la propiedad pdr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDR(String value) {
            this.pdr = value;
        }

        /**
         * Obtiene el valor de la propiedad fre.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFRE() {
            return fre;
        }

        /**
         * Define el valor de la propiedad fre.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFRE(String value) {
            this.fre = value;
        }

        /**
         * Obtiene el valor de la propiedad usr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUSR() {
            return usr;
        }

        /**
         * Define el valor de la propiedad usr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUSR(String value) {
            this.usr = value;
        }

        /**
         * Obtiene el valor de la propiedad nrt.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getNRT() {
            return nrt;
        }

        /**
         * Define el valor de la propiedad nrt.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setNRT(BigInteger value) {
            this.nrt = value;
        }

        /**
         * Obtiene el valor de la propiedad int.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getINT() {
            return _int;
        }

        /**
         * Define el valor de la propiedad int.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setINT(BigInteger value) {
            this._int = value;
        }

        /**
         * Obtiene el valor de la propiedad tvu.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getTVU() {
            return tvu;
        }

        /**
         * Define el valor de la propiedad tvu.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setTVU(BigInteger value) {
            this.tvu = value;
        }

    }

}
