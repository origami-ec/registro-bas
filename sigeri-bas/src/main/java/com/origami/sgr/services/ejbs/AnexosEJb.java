/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.config.SisVars;
import com.origami.jaxb.Estructura;
import com.origami.jaxb.ObjectFactory;
import com.origami.jaxb.SEGURIDAD;
import com.origami.sgr.entities.NprmSri;
import com.origami.sgr.entities.NprmSriCatalogo;
import com.origami.sgr.entities.NprmSriDetalle;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoParticipante;
import com.origami.sgr.entities.UafUsuarios;
import com.origami.sgr.models.DatoPublicoRegistroMC12;
import com.origami.sgr.models.DatoPublicoRegistroMS12;
import com.origami.sgr.models.DatoPublicoRegistroPropiedad12;
import com.origami.sgr.models.DetalleUaf;
import com.origami.sgr.models.Formulario2;
import com.origami.sgr.services.interfaces.AnexosLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.AnexosXml;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Anyelo
 */
@Stateless(name = "anexos")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class AnexosEJb implements AnexosLocal {

    @EJB(beanName = "manager")
    private Entitymanager em;

    @EJB(beanName = "registroPropiedad")
    private RegistroPropiedadServices reg;

    /**
     * Generacion de Anexo para el envio de informacion masiva con aplicativo
     * SEDI segun RESOLUCIÓN No.039-NG-DINARDAP-2015
     *
     * @param fechacorte Date
     * @param fechaCorteHasta
     * @return String
     * @throws java.io.IOException Exception
     */
    @Override
    public String getAnexoFormulario2Side(Date fechacorte, Date fechaCorteHasta) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Formulario2> list;
        BufferedWriter bw;
        File archivo;
        String line, tipopersona, nombres, razon, tipodocumento, documento, modificacion, repertorio, inicio, fin, celebracion;
        try {
            if (fechacorte != null) {
                if (fechaCorteHasta == null) {
                    fechaCorteHasta = fechacorte;
                }
                list = em.getSqlQueryValues(Formulario2.class, Querys.consultaAnexo2Side, new Object[]{fechacorte, fechaCorteHasta});
                if (list != null) {
                    sdf = new SimpleDateFormat("yyyy_MM_dd");
                    archivo = new File(SisVars.rutaAnexos + "RP_" + sdf.format(fechacorte) + ".txt");
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF8"));
                    bw.write(Constantes.cabeceraFormulario2Side);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("Data: " + list.size());
                    for (Formulario2 f2 : list) {
                        tipopersona = "";
                        nombres = "";
                        razon = "";
                        tipodocumento = "";
                        documento = "";
                        modificacion = "";
                        celebracion = "";
                        inicio = "";
                        fin = "";
                        if (f2.getDocumento() != null) {
                            switch (f2.getDocumento().length()) {
                                case 10:
                                    tipopersona = "NATURAL";
                                    tipodocumento = "CÉDULA";
                                    documento = Utils.isEmpty(f2.getDocumento());
                                    nombres = Utils.isEmpty(f2.getInterviniente());
                                    break;
                                case 13:
                                    tipopersona = "JURIDICA";
                                    tipodocumento = "RUC";
                                    documento = Utils.isEmpty(f2.getDocumento());
                                    razon = Utils.isEmpty(f2.getInterviniente());
                                    break;
                                default:
                                    nombres = Utils.isEmpty(f2.getInterviniente());
                                    razon = Utils.isEmpty(f2.getInterviniente());
                                    break;
                            }
                        }
                        if (f2.getFechamodificacion() != null) {
                            modificacion = sdf.format(f2.getFechamodificacion());
                        }
                        if (f2.getFecharepertorio() == null) {
                            repertorio = sdf.format(f2.getFechainscripcion());
                        } else {
                            repertorio = sdf.format(f2.getFecharepertorio());
                        }
                        if (f2.getFolioinicio() != null) {
                            inicio = f2.getFolioinicio().toString();
                        }
                        if (f2.getFoliofin() != null) {
                            fin = f2.getFoliofin().toString();
                        }
                        if (f2.getFechacelebracion() != null) {
                            celebracion = sdf.format(f2.getFechacelebracion());
                        }
                        line = Utils.isEmpty(f2.getContrato()) + ";" + Utils.isEmpty(f2.getTipoacto()) + ";" + Utils.isEmpty(f2.getTipotramite()) + ";" + Utils.isEmpty(f2.getTipolibro()) + ";"
                                + f2.getRepertorio() + ";" + repertorio + ";" + f2.getInscripcion() + ";" + sdf.format(f2.getFechainscripcion()) + ";" + tipopersona + ";" + razon.trim() + ";"
                                + nombres.trim() + ";" + nombres.trim() + ";;;" + tipodocumento + ";" + documento + ";;" + Utils.isEmpty(f2.getConyugue()) + ";" + Utils.isEmpty(f2.getDocconyugue()) + ";;"
                                + this.getDatosBienForm2(f2.getIdmovimiento().longValue())
                                + ";" + Utils.isEmpty(f2.getCuantia().toString()) + ";" + Constantes.moneda + ";"
                                + Utils.isEmpty(f2.getUuid()) + ";;;;;;;;" + modificacion + ";" + Constantes.descripcion + ";" + Utils.quitarTildes(f2.getEntidad()).toUpperCase() + ";"
                                + ";" + celebracion + ";;;;;;;;;;;;;;" + Utils.isEmpty(f2.getObservacion()).trim().replaceAll(";", ",") + ";"
                                + inicio + ";" + fin + ";";
                        line = Utils.quitarSaltos(line);
                        bw.write(Constantes.saltoLinea);
                        bw.write(line);
                    }
                    bw.flush();
                    bw.close();
                    return archivo.getAbsolutePath();
                } else {
                    System.out.println("NO hay nada");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    /**
     * Devuelve parte de la cadena de valores para armar el anexo 2 del SEDI,los
     * campos que forman parte de este resultado son desde: clave catastral,
     * hasta: cantón.
     *
     * @param id Long
     * @return String
     */
    @Override
    public String getPropiedadesForm2(Long id) {
        String result = "", seccion = "", provincia = "", canton = "";
        try {
            List<RegMovimientoFicha> mfs = reg.getRegMovFichaByIdMov(id);
            for (RegMovimientoFicha mf : mfs) {
                provincia = provincia + "|MANABI";
                canton = canton + "|PORTOVIEJO";
                result = result + "|";
                if (mf.getFicha().getClaveCatastral() != null && !mf.getFicha().getClaveCatastral().equalsIgnoreCase("0")) {
                    result = result + mf.getFicha().getClaveCatastral();
                }
            }
            if (!provincia.isEmpty()) {
                provincia = provincia.substring(1);
                canton = canton.substring(1);
            }
            if (!result.isEmpty()) {
                result = result.substring(1);
            }
            seccion = result + ";" + result + ";;" + provincia + ";;;;";
            result = "";
            for (RegMovimientoFicha mf : mfs) {
                result = result + "|" + mf.getFicha().getLinderos().trim().replaceAll(";", ",");
            }
            if (!result.isEmpty()) {
                result = result.substring(1);
            }
            seccion = seccion + result + ";";
            result = "";
            for (RegMovimientoFicha mf : mfs) {
                result = result + "|";
                if (mf.getFicha().getParroquia() != null && mf.getFicha().getParroquia().getSediParroquia() != null) {
                    result = result + mf.getFicha().getParroquia().getSediParroquia();
                }
            }
            if (!result.isEmpty()) {
                result = result.substring(1);
            }
            seccion = seccion + result + ";" + canton;
        } catch (Exception e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return seccion;
    }

    /**
     * Devuelve parte de la cadena de valores para armar el anexo 2 del SEDI,los
     * campos que forman parte de este resultado son desde: clave catastral,
     * hasta: cantón.
     *
     * @param id Long
     * @return String
     */
    @Override
    public String getDatosBienForm2(Long id) {
        String result = "", zona = "", ubicacion = "", parroquia = "";
        try {
            RegFicha ficha = (RegFicha) em.find(Querys.getMaxFichaFromMov, new String[]{"idFicha"}, new Object[]{id});
            if (ficha != null) {
                if (ficha.getClaveCatastral() != null && !ficha.getClaveCatastral().equalsIgnoreCase("0")) {
                    result = ficha.getClaveCatastral() + ";" + ficha.getClaveCatastral() + ";;LOJA;";
                } else {
                    result = ";;;LOJA;";
                }
                if (ficha.getTipoPredio() == null) {
                    zona = "RURALURBANO";
                } else if (ficha.getTipoPredio().equalsIgnoreCase("U")) {
                    zona = "URBANA";
                } else if (ficha.getTipoPredio().equalsIgnoreCase("R")) {
                    zona = "RURAL";
                }
                result = result + zona + ";;";
                if (ficha.getParroquia() != null) {
                    if (ficha.getParroquia().getSediParroquia() != null) {
                        parroquia = ficha.getParroquia().getSediParroquia();
                    }
                    if (ficha.getParroquia().getSediUbicacion() != null) {
                        ubicacion = ficha.getParroquia().getSediUbicacion();
                    }
                }
                if (!ubicacion.isEmpty()) {
                    result = result + ubicacion + ";";
                } else {
                    result = result + "OESTE;";
                }
                result = result + ficha.getLinderos().trim().replaceAll(";", ",") + ";";
                if (!parroquia.isEmpty()) {
                    result = result + parroquia + ";LOJA";
                } else {
                    result = result + "LOJA;LOJA";
                }
            } else {
                result = ";;;;;;;;;";
            }
        } catch (Exception e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * genera el archivo de la cabecera del anexo resu en formato xml
     *
     * @param desde Calendar
     * @param clave String
     * @param ua UafUsuarios
     * @return possible object is {@link File }
     */
    @Override
    public File archivoCabeceraResu(Calendar desde, String clave, UafUsuarios ua) {
        Calendar hasta = Calendar.getInstance();
        SimpleDateFormat sdf;
        BigInteger tramites, detalles;
        BigDecimal cuantias;
        try {
            sdf = new SimpleDateFormat("MM-yyyy");
            tramites = (BigInteger) em.getNativeQuery(Querys.getCantidadTramitesUaf, new Object[]{sdf.format(desde.getTime())});
            detalles = (BigInteger) em.getNativeQuery(Querys.getCantidadPersonasUaf, new Object[]{sdf.format(desde.getTime())});
            cuantias = (BigDecimal) em.getNativeQuery(Querys.getTotalCuantiUaf, new Object[]{sdf.format(desde.getTime())});
            sdf = new SimpleDateFormat("yyyyMMdd");
            hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));

            ObjectFactory ob = new ObjectFactory();
            SEGURIDAD.CABECERA ca = ob.createSEGURIDADCABECERA();
            ca.setCDR(Constantes.uafCodigoRegistro);         //CODIGO DE REGISTRO ASIGNADO A LA INSTITUCION
            ca.setPDR(sdf.format(hasta.getTime()));         //PERIODO CORRESPONDIENTE AL REPORTE
            ca.setFRE(sdf.format(new Date()));              //FECHA DEL REPORTE
            ca.setUSR(ua.getCodigoUser());                  //USUARIO QUE REALIZA EL REPORTE
            ca.setNRT(tramites);                            //TOTAL DE TRAMITES REGISTRADOS
            ca.setINT(detalles);                            //TOTAL DE INTERVININETES REGISTRADOS
            ca.setTVU(cuantias.toBigInteger());             //TOTAL DE LOS VALORES DE CUANTIA

            SEGURIDAD se = ob.createSEGURIDAD();
            se.setCLAVE(clave);
            se.setCABECERA(ca);

            JAXBContext jaxbContext = JAXBContext.newInstance(SEGURIDAD.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File temp = new File(SisVars.rutaAnexos + "CABECERA" + Constantes.uafCodigoRegistro + sdf.format(hasta.getTime()) + ".xml");
            //jaxbMarshaller.marshal(se, System.out);
            jaxbMarshaller.marshal(se, temp);
            return temp;
        } catch (JAXBException e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * genera el archivo del detalle del anexo resu en formato xml
     *
     * @param desde Calendar
     * @param clave String
     * @return possible object is {@link File }
     */
    @Override
    public File archivoDetalleResu(Calendar desde, String clave) {
        List<RegMovimientoCliente> mcs;
        Calendar hasta = Calendar.getInstance();
        List<DetalleUaf> list;
        SimpleDateFormat sdf;

        ObjectFactory ob = new ObjectFactory();
        Estructura strc = ob.createEstructura();
        Estructura.TRAMITE tramite;
        Estructura.TRAMITE.INTERVINIENTE interv;

        try {
            hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));
            sdf = new SimpleDateFormat("MM-yyyy");
            list = em.getSqlQueryValues(DetalleUaf.class, Querys.getMovsAnexoUaf, new Object[]{sdf.format(hasta.getTime())});
            sdf = new SimpleDateFormat("yyyyMMdd");
            for (DetalleUaf du : list) {
                tramite = ob.createEstructuraTRAMITE();
                tramite.setNIT(du.getRepertorio() + "-" + du.getInscripcion());          //NUMERO DE INGRESO DE TRAMITE
                tramite.setFCR(sdf.format(du.getFechainscripcion()));   //FECHA DE CELEBRACION DEL TRAMITE
                tramite.setTTM(du.getTramite());                        //TIPO DE TRAMITE
                tramite.setDTM(Utils.quitarTildes(Utils.quitarSaltos(du.getDescripcion())).toUpperCase());                    //DESCRIPCION DEL TRAMITE
                if (du.getCatastro() == null) {
                    du.setCatastro("N/A");
                } else {
                    du.setCatastro(du.getCatastro().replace(" ", ""));
                    if ("0".equals(du.getCatastro())) {
                        du.setCatastro("N/A");
                    }
                }
                tramite.setCCA(du.getCatastro());                       //CAMPO CODIGO CATASTRAL
                tramite.setVCC(du.getCuantia().toBigInteger());         //CAMPO VALOR CUANTIA
                if (du.getCodigo() == null) {
                    du.setCodigo("OTR");
                }
                tramite.setTTB(du.getCodigo());                         //CAMPO TIPO BIEN
                /*if (du.getCodigo().equalsIgnoreCase("OTR")) {
                    du.setDireccion("N/A");
                } else*/
                if (du.getDireccion() == null) {
                    du.setDireccion("PORTOVIEJO");
                }
                tramite.setDRB(Utils.quitarTildes(du.getDireccion().toUpperCase()));        //DIRECCION DEL BIEN
                tramite.setCBC(Constantes.uafCanton);                    //CANTON DEL BIEN
                tramite.setCDR(Constantes.uafCodigoRegistro);            //CODIGO DEL REGISTRO DE LA PROPIEDAD EMITIDO POR LA UAFE
                tramite.setFCT(sdf.format(hasta.getTime()));                      //FECHA DE CORTE DEL REPORTE

                mcs = reg.getRegMovClienteByIdMov(du.getId().longValue());
                for (RegMovimientoCliente mc : mcs) {
                    if (mc.getEnteInterv().getCedRuc().trim().length() == 10 || mc.getEnteInterv().getCedRuc().trim().length() == 13) {
                        interv = ob.createEstructuraTRAMITEINTERVINIENTE();
                        interv.setTII(mc.getEnteInterv().getTipoDocumento());
                        interv.setIDI(mc.getEnteInterv().getCedRuc().trim());
                        interv.setNRI(mc.getEnteInterv().getNombre().trim());
                        if (mc.getEnteInterv().getNacionalidad() == null) {
                            interv.setNAI("ECU");
                        } else {
                            interv.setNAI(mc.getEnteInterv().getNacionalidad().getCodigo());
                        }
                        if (mc.getUafRol() == null) {
                            interv.setRDI("N/A");
                        } else {
                            interv.setRDI(mc.getUafRol().getCode());
                        }
                        if (mc.getUafPapel() == null) {
                            interv.setPDI("N/A");
                        } else {
                            interv.setPDI(mc.getUafPapel().getCodigo());
                        }
                        tramite.getINTERVINIENTE().add(interv);
                    }
                }
                strc.getTRAMITE().add(tramite);
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(Estructura.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File temp = new File(SisVars.rutaAnexos + "DETALLE" + Constantes.uafCodigoRegistro + sdf.format(hasta.getTime()) + ".xml");
            //jaxbMarshaller.marshal(strc, System.out);
            jaxbMarshaller.marshal(strc, temp);
            return temp;
        } catch (JAXBException e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * genera el anexo que se envia la SRI en formato xml
     *
     * @param fecha Date
     * @param ruc String
     * @return String
     */
    @Override
    public String anexoNrpm(Date fecha, String ruc) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        List<RegMovimientoParticipante> list;
        try {
            list = em.findAll(Querys.getParticipantesNrpm, new String[]{"fechacorte"}, new Object[]{sdf.format(fecha)});
            File temp = AnexosXml.generarAnexoNrpm(ruc, fecha, list);
            return temp.getAbsolutePath();
        } catch (Exception e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public String anexoNrpm(NprmSri nprmSri, Date fecha, String ruc) {
        List<RegMovimientoParticipante> list = new ArrayList<>();
        try {
            for (NprmSriDetalle detalle : nprmSri.getNprmSriDetalles()) {
                list.add(detalle.getMovimientoParticipante());
            }
            File temp = AnexosXml.generarAnexoNrpm(ruc, fecha, list);
            return temp.getAbsolutePath();
        } catch (Exception e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public String anexoNrpm1(NprmSri nprmSri, Date fecha, String ruc) {
        try {
            File temp = AnexosXml.generarAnexoNrpm1(ruc, fecha, nprmSri);
            return temp.getAbsolutePath();
        } catch (Exception e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List<NprmSriCatalogo> getCatalogoSri(Integer tipoAnexo) {
        Map<String, Object> paramt = new HashMap<>();
        paramt.put("tipo", tipoAnexo);
        return this.em.findObjectByParameterList(NprmSriCatalogo.class, paramt);
    }

    @Override
    public NprmSri buscarAnexos(Integer tipoanexo, Integer tipoanexosri, Integer mesSri, Integer anioSri) {
        Map<String, Object> paramt = new HashMap<>();
        paramt.put("tipo", tipoanexo);
        paramt.put("tipoAnexo", tipoanexosri);
        paramt.put("mes", mesSri);
        paramt.put("anio", anioSri);
        NprmSri anexo = this.em.findObjectByParameter(NprmSri.class, paramt);
        if (anexo == null) {
            anexo = new NprmSri();
            anexo.setAnio(anioSri);
            anexo.setMes(mesSri);
            anexo.setTipo(tipoanexo);
            anexo.setTipoAnexo(tipoanexosri);
            anexo = (NprmSri) this.em.persist(anexo);
        }
        return anexo;
    }

    //METODO DE RESOLUCION 12-2014
    @Override
    public String getAnexo1Resolucion12(Date fechacorte) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<DatoPublicoRegistroPropiedad12> list;
        BufferedWriter bw;
        File archivo;
        String separador = Constantes.separador;
        String noCorresponde = "";
        String line;
        try {
            if (fechacorte != null) {
                list = em.getSqlQueryValues(DatoPublicoRegistroPropiedad12.class, Querys.consultaAnexo1Side12, new Object[]{sdf.format(fechacorte)});
                if (list != null) {
                    sdf = new SimpleDateFormat("yyyy_MM_dd");
                    archivo = new File(SisVars.rutaAnexos + "RP_" + sdf.format(fechacorte) + "_" + Constantes.canton + ".txt");
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    //bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF8"));       //CODIFICACION UTF-8
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "ISO-8859-1"));   //CODIFICACION ANSI
                    bw.write(Constantes.cabeceraAnexo1Side12);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (DatoPublicoRegistroPropiedad12 propiedad : list) {
                        if (propiedad.getNombres() != null) {
                            propiedad.setNombres(propiedad.getApellidos());
                        }
                        if (propiedad.getClavecatastral() == null) {
                            propiedad.setClavecatastral(noCorresponde);
                        }
                        if (propiedad.getTipopersona().equalsIgnoreCase("N")) {
                            propiedad.setRazonsocial(noCorresponde);
                        } else {
                            propiedad.setRazonsocial(propiedad.getApellidos());
                        }
                        if (propiedad.getZona() != null) {
                            if (propiedad.getZona().equalsIgnoreCase("U")) {
                                propiedad.setZona("Urbano");
                            } else if (propiedad.getZona().equalsIgnoreCase("R")) {
                                propiedad.setZona("Rural");
                            } else {
                                propiedad.setZona(noCorresponde);
                            }
                        } else {
                            propiedad.setZona(noCorresponde);
                        }
                        if (propiedad.getSuperficie() == null) {
                            propiedad.setSuperficie(noCorresponde);
                        }
                        if (propiedad.getParroquia() == null) {
                            propiedad.setParroquia(noCorresponde);
                        }
                        if (propiedad.getLindero() != null) {
                            propiedad.setLindero(Utils.quitarPleca(Utils.quitarSaltos(propiedad.getLindero())));
                            propiedad.setLinderodescrip(propiedad.getLindero());
                        } else {
                            propiedad.setLindero(noCorresponde);
                            propiedad.setLinderodescrip(noCorresponde);
                        }
                        if (propiedad.getValoruuid() == null) {
                            propiedad.setValoruuid(Utils.getValorUuid());
                        }
                        if (propiedad.getFechaescritura() == null) {
                            propiedad.setFechaescritura(propiedad.getFechainsripcion());
                        }
                        line = propiedad.getApellidos() + separador + propiedad.getNombres() + separador + propiedad.getCi() + separador + propiedad.getTipocompareciente() + separador
                                + propiedad.getRazonsocial() + separador + Utils.quitarTildes(propiedad.getTipocontrato()) + separador + propiedad.getNuminscripcion() + separador
                                + sdf.format(propiedad.getFechainsripcion()) + separador + propiedad.getClavecatastral() + separador + propiedad.getDescripcionbien() + separador
                                + Utils.quitarTildes(propiedad.getLibro()) + separador + propiedad.getProvincia() + separador + propiedad.getZona() + separador + propiedad.getSuperficie() + separador
                                + propiedad.getLindero() + separador + propiedad.getLinderodescrip() + separador + propiedad.getParroquia() + separador + propiedad.getCanton() + separador
                                + propiedad.getCuantia() + separador + propiedad.getUnidad() + separador + propiedad.getValoruuid() + separador + propiedad.getNumjuicio() + separador
                                + "VIGENTE" + separador + propiedad.getUbicaciondato() + separador + sdf.format(propiedad.getUltimamodificacion()) + separador
                                + propiedad.getNotaria() + separador + propiedad.getCantonnotaria() + separador + sdf.format(propiedad.getFechaescritura());
                        line = Utils.quitarSaltos(line);
                        bw.write(Constantes.saltoLinea);
                        bw.write(line);
                    }
                    bw.flush();
                    bw.close();
                    return archivo.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    public String getAnexo2Side12(Date fechacorte) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<DatoPublicoRegistroMC12> list;
        BufferedWriter bw;
        File archivo;
        String separador = Constantes.separador;
        String noCorresponde = "";
        String line;
        try {
            if (fechacorte != null) {
                list = em.getSqlQueryValues(DatoPublicoRegistroMC12.class, Querys.consultaAnexo2Side12, new Object[]{sdf.format(fechacorte)});
                if (list != null) {
                    sdf = new SimpleDateFormat("yyyy_MM_dd");
                    archivo = new File(SisVars.rutaAnexos + "RP_MC_" + sdf.format(fechacorte) + "_" + Constantes.canton + ".txt");
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "ISO-8859-1"));   //CODIFICACION ANSI
                    bw.write(Constantes.cabeceraAnexo2Side12);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (DatoPublicoRegistroMC12 mc : list) {
                        mc.procesarDatosBien();
                        if (mc.getCodigounico() == null) {
                            mc.setCodigounico(noCorresponde);
                        }
                        if (mc.getChasis() == null) {
                            mc.setChasis(noCorresponde);
                        }
                        line = mc.getApellidos() + separador + mc.getNombres() + separador + mc.getCi() + separador + mc.getTipocompareciente() + separador
                                + mc.getRazonsocial() + separador + Utils.quitarTildes(mc.getTipocontrato()) + separador + sdf.format(mc.getFechainsripcion()) + separador
                                + mc.getNuminscripcion() + separador + mc.getNombrerepresentante() + separador + sdf.format(mc.getFechacancelacion()) + separador
                                + mc.getTipobien() + separador + mc.getChasis() + separador + mc.getMotor() + separador + mc.getModelo() + separador
                                + mc.getMarca() + separador + mc.getAnio() + separador + mc.getPlaca() + separador + mc.getRegistrador() + separador
                                + sdf.format(mc.getUltimamodificacion()) + separador + mc.getCodigounico() + separador + mc.getEntidapublica() + separador
                                + mc.getCantonnombre() + separador + sdf.format(mc.getFechaescritura()) + separador + mc.getEstado();
                        line = Utils.quitarSaltos(line);
                        bw.write(Constantes.saltoLinea);
                        bw.write(line);
                    }
                    bw.flush();
                    bw.close();
                    return archivo.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    public String getAnexo3Side12(Date fechacorte) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<DatoPublicoRegistroMS12> list;
        BufferedWriter bw;
        File archivo;
        String separador = Constantes.separador;
        String noCorresponde = "";
        String line;
        try {
            if (fechacorte != null) {
                list = em.getSqlQueryValues(DatoPublicoRegistroMS12.class, Querys.consultaAnexo3Side12, new Object[]{sdf.format(fechacorte)});
                if (list != null) {
                    sdf = new SimpleDateFormat("yyyy_MM_dd");
                    archivo = new File(SisVars.rutaAnexos + "RP_MS_" + sdf.format(fechacorte) + "_" + Constantes.canton + ".txt");
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "ISO-8859-1"));   //CODIFICACION ANSI
                    bw.write(Constantes.cabeceraAnexo3Side12);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (DatoPublicoRegistroMS12 ms : list) {
                        if (ms.getCodigounico() == null) {
                            ms.setCodigounico(noCorresponde);
                        }
                        if (ms.getNumeroidentificacion() == null) {
                            ms.setNumeroidentificacion(noCorresponde);
                        }
                        if (ms.getCargonombre() == null) {
                            ms.setCargonombre(noCorresponde);
                        }
                        line = ms.getClienombre() + separador + ms.getRuc() + separador + ms.getTipocompania() + separador + sdf.format(ms.getFechainsripcion())
                                + separador + ms.getApellidos() + separador + ms.getNombres() + separador + ms.getNumeroidentificacion() + separador
                                + ms.getCargonombre() + separador + noCorresponde + separador + ms.getDisposicion() + separador
                                + ms.getAutoridaddisposicion() + separador + (ms.getFechadisposicion() == null ? noCorresponde : sdf.format(ms.getFechadisposicion()))
                                + separador + ms.getNumerodisposicion() + separador + ms.getFechaescritura() + separador + ms.getNotaria() + separador
                                + ms.getCantonnombre() + separador + Utils.quitarTildes(ms.getTipotramite()) + separador + ms.getUbicaciondato() + separador
                                + (ms.getUltimamodificacion() == null ? noCorresponde : sdf.format(ms.getUltimamodificacion())) + separador + ms.getCodigounico()
                                + separador + ms.getEstado();
                        line = Utils.quitarSaltos(line);
                        bw.write(Constantes.saltoLinea);
                        bw.write(line);
                    }
                    bw.flush();
                    bw.close();
                    return archivo.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(AnexosEJb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

}
