/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.config.SisVars;
import com.origami.documental.ejbs.DocumentsEjb;
import com.origami.documental.entities.TbCarpetas;
import com.origami.documental.entities.TbData;
import com.origami.documental.entities.TbDataIntervinientes;
import com.origami.documental.entities.TbTipoDocCab;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.lazymodels.TbDataLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class DlgLazyTbData implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;
    @Inject
    private Entitymanager em;
    @Inject
    private DocumentsEjb doc;

    protected Integer estado = 0;
    protected String dbData = "doc_data_lata";
    protected TbDataLazy dataLazy;
    protected RegMovimiento movimiento;
    protected RegMovimientoReferencia marginacion;
    protected SimpleDateFormat sdf;
    protected Map map;
    protected TbTipoDocCab tipoDocumento;
    protected TbCarpetas tipoCarpeta;
    protected List<TbTipoDocCab> libros;
    protected List<TbCarpetas> anios;
    protected String partida = "";
    protected List<TbDataIntervinientes> partes;

    @PostConstruct
    protected void initView() {
        try {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            libros = doc.findAll(TbTipoDocCab.class, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateCarpetas() {
        try {
            if (tipoDocumento != null && tipoDocumento.getIdTipoDoc() != null) {
                map = new HashMap<>();
                map.put("idTipoDoc", tipoDocumento.getIdTipoDoc());
                map.put("flgEstado", estado.shortValue());
                anios = doc.findAll(TbCarpetas.class, map);
            }
        } catch (Exception e) {
            System.out.println(e);
            anios = new ArrayList<>();
        }
    }

    public void buscarData() {
        try {
            if (!partida.isEmpty() && tipoCarpeta != null) {
                dataLazy = new TbDataLazy();
                dataLazy.addFilter("f02:equals", partida);
                dataLazy.addFilter("idCarpeta", tipoCarpeta);
            } else {
                JsfUti.messageWarning(null, "Debe ingresar los parámetros de consulta.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void visualizarBlob(TbData data) {
        try {
            if (data.getIdPadre().compareTo(BigInteger.ZERO) > 0) {
                ss.agregarParametro("id_transaccion", data.getIdTransaccion());
                ss.agregarParametro("id_padre", data.getIdPadre());
                ss.agregarParametro("id_blob_reg", data.getIdBlobReg());
                JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/visorDocuments.xhtml");
            } else {
                JsfUti.messageWarning(null, "No se encuentra la imagen digitalizada.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void seleccionarData(TbData data) {
        try {
            map = new HashMap();
            map.put("idTransaccion", data.getIdTransaccion());
            movimiento = (RegMovimiento) em.findObjectByParameter(RegMovimiento.class, map);
            if (movimiento == null || movimiento.getId() == null) {
                movimiento = new RegMovimiento();
                if (data.getIdCarpeta().getIdTipoDoc().getIdLibro() > 0) {
                    movimiento = this.cargarTbData(data);
                    marginacion = new RegMovimientoReferencia();
                    marginacion.setMovimientoReferencia(movimiento);
                    PrimeFaces.current().dialog().closeDynamic(marginacion);
                } else {
                    JsfUti.messageWarning(null, "No esta configurado el id del libro.", "");
                }
            } else {
                movimiento.setIdPadre(data.getIdPadre());
                movimiento.setIdBlobReg(data.getIdBlobReg());
                marginacion = new RegMovimientoReferencia();
                marginacion.setMovimientoReferencia(movimiento);
                PrimeFaces.current().dialog().closeDynamic(marginacion);
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, "Error al insertar el registro como antecendente.", "");
        }
    }

    public void seleccionarMovimiento(TbData data) {
        try {
            map = new HashMap();
            map.put("idTransaccion", data.getIdTransaccion());
            movimiento = (RegMovimiento) em.findObjectByParameter(RegMovimiento.class, map);
            if (movimiento == null || movimiento.getId() == null) {
                movimiento = new RegMovimiento();
                if (data.getIdCarpeta().getIdTipoDoc().getIdLibro() > 0) {
                    movimiento = this.cargarTbData(data);
                    this.llenarIntervinientes(data.getIdTransaccion());
                    PrimeFaces.current().dialog().closeDynamic(movimiento);
                } else {
                    JsfUti.messageWarning(null, "No esta configurado el id del libro.", "");
                }
            } else {
                movimiento.setIdPadre(data.getIdPadre());
                movimiento.setIdBlobReg(data.getIdBlobReg());
                PrimeFaces.current().dialog().closeDynamic(movimiento);
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, "Error al insertar el registro como antecendente.", "");
        }
    }

    public void llenarIntervinientes(Long transaccion) {
        List<RegMovimientoCliente> rmcs = new ArrayList<>();
        RegMovimientoCliente rmc;
        RegEnteInterviniente temp;
        try {
            map = new HashMap();
            map.put("idTransaccion", transaccion);
            partes = doc.findAll(TbDataIntervinientes.class, map);
            if (!partes.isEmpty()) {
                for (TbDataIntervinientes tb : partes) {
                    temp = this.findNombres(tb);
                    if (temp != null) {
                        rmc = new RegMovimientoCliente();
                        rmc.setEnteInterv(temp);
                        rmc.setPapel(this.findPapel(tb.getTipo()));
                        rmcs.add(rmc);
                    }
                }
            }
            movimiento.setRegMovimientoClienteCollection(rmcs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RegEnteInterviniente findNombres(TbDataIntervinientes tb) {
        RegEnteInterviniente ente = null;
        try {
            if (tb.getIdentificacion().length() == 10 || tb.getIdentificacion().length() == 13) {
                map = new HashMap<>();
                map.put("cedRuc", tb.getIdentificacion());
                ente = (RegEnteInterviniente) em.findObjectByParameter(RegEnteInterviniente.class, map);
            } else if (tb.getClase().equals("CEDULA")) {
                map = new HashMap<>();
                map.put("nombre", tb.getApellidos().trim() + " " + tb.getNombres().trim());
                ente = (RegEnteInterviniente) em.findObjectByParameter(RegEnteInterviniente.class, map);
            } else if (tb.getClase().equals("RUC")) {
                map = new HashMap<>();
                map.put("nombre", tb.getRasonSocial().trim());
                ente = (RegEnteInterviniente) em.findObjectByParameter(RegEnteInterviniente.class, map);
            }
            if (ente == null) {
                map = new HashMap<>();
                map.put("cedRuc", Utils.completarCadenaConCeros(tb.getId().toString(), 15));
                ente = (RegEnteInterviniente) em.findObjectByParameter(RegEnteInterviniente.class, map);
            }
            if (ente == null) {
                ente = new RegEnteInterviniente();
                if (tb.getClase().equals("CEDULA")) {
                    ente.setNombre(tb.getApellidos().trim() + " " + tb.getNombres().trim());
                    ente.setTipoInterv("N");
                } else {
                    ente.setNombre(tb.getRasonSocial().trim());
                    ente.setTipoInterv("J");
                }
                ente.setProcedencia("D");
                ente.setUsuarioIngreso(us.getName_user());
                ente.setFechaIngreso(new Date());
                ente.setCedRuc(Utils.completarCadenaConCeros(tb.getId().toString(), 15));
                ente = (RegEnteInterviniente) em.persist(ente);
            }
            if (ente != null) {
                return ente;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public RegPapel findPapel(String tipo) {
        try {
            if (tipo.equals("1") || tipo.equals("COMPRADOR")) {
                return em.find(RegPapel.class, 58L);
            } else if (tipo.equals("2") || tipo.equals("VENDEDOR")) {
                return em.find(RegPapel.class, 208L);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public RegMovimiento cargarTbData(TbData data) {
        try {
            Long idNotaria;
            Long idCanton;
            RegMovimiento mov = new RegMovimiento();
            switch (data.getIdCarpeta().getIdTipoDoc().getIdTipoDoc()) {
                case 1: //PROPIEDADES
                    mov.setNumInscripcion(Integer.parseInt(data.getF02()));
                    mov.setTomoRegistro(data.getF03());
                    mov.setNumTomo(data.getF04());
                    mov.setNumRepertorio(0);
                    if (data.getF10() != null && !data.getF10().isEmpty()) {
                        if (Utils.isNum(data.getF10())) {
                            mov.setNumRepertorio(Integer.parseInt(data.getF10()));
                        }
                    }
                    if (data.getF11() != null && !data.getF11().isEmpty()) {
                        mov.setFechaInscripcion(sdf.parse(data.getF11()));
                    }
                    if (data.getF30() != null && !data.getF30().isEmpty()) {
                        mov.setValorUuid(data.getF30());
                    }
                    if (data.getF37() != null && !data.getF37().isEmpty()) {
                        mov.setFechaOto(sdf.parse(data.getF37()));
                    }
                    mov.setLibro(new RegLibro(data.getIdCarpeta().getIdTipoDoc().getIdLibro()));
                    mov.setObservacion(data.getParroquia() + data.getBarrio()
                            + data.getClaveCatastral() + data.getLinderos());
                    mov.setCuantiaCadena(data.getF29());
                    idNotaria = data.getIdNotaria();
                    if (idNotaria > 0) {
                        RegEnteJudiciales temp = em.find(RegEnteJudiciales.class, idNotaria);
                        if (temp != null) {
                            mov.setEnteJudicial(temp);
                        }
                    }
                    idCanton = data.getIdCanton();
                    if (idCanton > 0) {
                        RegDomicilio temp = em.find(RegDomicilio.class, idCanton);
                        if (temp != null) {
                            mov.setDomicilio(temp);
                        }
                    }
                    mov.setIdTransaccion(data.getIdTransaccion());
                    mov.setIdPadre(data.getIdPadre());
                    mov.setIdBlobReg(data.getIdBlobReg());
                    break;
                    
                case 5:
                case 7:
                case 13:
                    mov.setNumInscripcion(Integer.parseInt(data.getF02()));
                    mov.setTomoRegistro(data.getF03());
                    mov.setNumTomo(data.getF04());
                    mov.setNumRepertorio(0);
                    if (data.getF05() != null && !data.getF05().isEmpty()) {
                        if (Utils.isNum(data.getF05())) {
                            mov.setNumRepertorio(Integer.parseInt(data.getF05()));
                        }
                    }
                    if (data.getF11() != null && !data.getF11().isEmpty()) {
                        mov.setFechaInscripcion(sdf.parse(data.getF11()));
                    }
                    if (data.getF30() != null && !data.getF30().isEmpty()) {
                        mov.setValorUuid(data.getF30());
                    }
                    if (data.getF37() != null && !data.getF37().isEmpty()) {
                        mov.setFechaOto(sdf.parse(data.getF37()));
                    }
                    mov.setLibro(new RegLibro(data.getIdCarpeta().getIdTipoDoc().getIdLibro()));
                    mov.setObservacion(data.getParroquia() + data.getBarrio()
                            + data.getClaveCatastral() + data.getLinderos());
                    
                    idNotaria = data.getIdNotaria();
                    if (idNotaria > 0) {
                        RegEnteJudiciales temp = em.find(RegEnteJudiciales.class, idNotaria);
                        if (temp != null) {
                            mov.setEnteJudicial(temp);
                        }
                    }
                    idCanton = data.getIdCanton();
                    if (idCanton > 0) {
                        RegDomicilio temp = em.find(RegDomicilio.class, idCanton);
                        if (temp != null) {
                            mov.setDomicilio(temp);
                        }
                    }
                    mov.setIdTransaccion(data.getIdTransaccion());
                    mov.setIdPadre(data.getIdPadre());
                    mov.setIdBlobReg(data.getIdBlobReg());
                    break;

                case 6:
                    mov.setNumInscripcion(Integer.parseInt(data.getF02()));
                    mov.setTomoRegistro(data.getF03());
                    mov.setNumTomo(data.getF04());
                    mov.setNumRepertorio(0);
                    if (data.getF05() != null && !data.getF05().isEmpty()) {
                        if (Utils.isNum(data.getF05())) {
                            mov.setNumRepertorio(Integer.parseInt(data.getF05()));
                        }
                    }
                    if (data.getF10() != null && !data.getF10().isEmpty()) {
                        mov.setFechaInscripcion(sdf.parse(data.getF10()));
                    }
                    if (data.getF31() != null && !data.getF31().isEmpty()) {
                        mov.setValorUuid(data.getF31());
                    }
                    if (data.getF40() != null && !data.getF40().isEmpty()) {
                        mov.setFechaOto(sdf.parse(data.getF40()));
                    }
                    mov.setLibro(new RegLibro(data.getIdCarpeta().getIdTipoDoc().getIdLibro()));
                    idNotaria = this.getIdNotaria(data.getF36());
                    if (idNotaria > 0) {
                        RegEnteJudiciales temp = em.find(RegEnteJudiciales.class, idNotaria);
                        if (temp != null) {
                            mov.setEnteJudicial(temp);
                        }
                    }
                    idCanton = this.getIdCanton(data.getF37());
                    if (idCanton > 0) {
                        RegDomicilio temp = em.find(RegDomicilio.class, idCanton);
                        if (temp != null) {
                            mov.setDomicilio(temp);
                        }
                    }
                    mov.setIdTransaccion(data.getIdTransaccion());
                    mov.setIdPadre(data.getIdPadre());
                    mov.setIdBlobReg(data.getIdBlobReg());
                    break;

                case 8:
                    mov.setNumInscripcion(Integer.parseInt(data.getF02()));
                    mov.setTomoRegistro(data.getF03());
                    mov.setNumTomo(data.getF04());
                    mov.setNumRepertorio(0);
                    if (data.getF05() != null && !data.getF05().isEmpty()) {
                        if (Utils.isNum(data.getF05())) {
                            mov.setNumRepertorio(Integer.parseInt(data.getF05()));
                        }
                    }
                    if (data.getF10() != null && !data.getF10().isEmpty()) {
                        mov.setFechaInscripcion(sdf.parse(data.getF10()));
                    }
                    if (data.getF29() != null && !data.getF29().isEmpty()) {
                        mov.setValorUuid(data.getF29());
                    }
                    if (data.getF36() != null && !data.getF36().isEmpty()) {
                        mov.setFechaOto(sdf.parse(data.getF36()));
                    }
                    if (data.getF38() != null && !data.getF38().isEmpty()) {
                        mov.setFechaResolucion(sdf.parse(data.getF38()));
                    }
                    mov.setLibro(new RegLibro(data.getIdCarpeta().getIdTipoDoc().getIdLibro()));
                    idNotaria = this.getIdNotaria(data.getF34());
                    if (idNotaria > 0) {
                        RegEnteJudiciales temp = em.find(RegEnteJudiciales.class, idNotaria);
                        if (temp != null) {
                            mov.setEnteJudicial(temp);
                        }
                    }
                    idCanton = this.getIdCanton(data.getF35());
                    if (idCanton > 0) {
                        RegDomicilio temp = em.find(RegDomicilio.class, idCanton);
                        if (temp != null) {
                            mov.setDomicilio(temp);
                        }
                    }
                    mov.setIdTransaccion(data.getIdTransaccion());
                    mov.setIdPadre(data.getIdPadre());
                    mov.setIdBlobReg(data.getIdBlobReg());
                    break;
                    
                case 21: //PROPIEDAD HORIZONTAL
                    mov.setNumInscripcion(Integer.parseInt(data.getF03()));
                    mov.setTomoRegistro(data.getF04());
                    mov.setNumTomo(data.getF01());
                    mov.setNumRepertorio(0);
                    if (data.getF05() != null && !data.getF05().isEmpty()) {
                        if (Utils.isNum(data.getF05())) {
                            mov.setNumRepertorio(Integer.parseInt(data.getF05()));
                        }
                    }
                    if (data.getF10() != null && !data.getF10().isEmpty()) {
                        mov.setFechaInscripcion(sdf.parse(data.getF10()));
                    }
                    if (data.getF31() != null && !data.getF31().isEmpty()) {
                        mov.setValorUuid(data.getF31());
                    }
                    if (data.getF38() != null && !data.getF38().isEmpty()) {
                        mov.setFechaOto(sdf.parse(data.getF38()));
                    }
                    mov.setObservacion(data.getParroquia() + data.getBarrio()
                            + data.getClaveCatastral() + data.getLinderos());
                    mov.setLibro(new RegLibro(data.getIdCarpeta().getIdTipoDoc().getIdLibro()));
                    idNotaria = this.getIdNotaria(data.getF36());
                    if (idNotaria > 0) {
                        RegEnteJudiciales temp = em.find(RegEnteJudiciales.class, idNotaria);
                        if (temp != null) {
                            mov.setEnteJudicial(temp);
                        }
                    }
                    idCanton = this.getIdCanton(data.getF37());
                    if (idCanton > 0) {
                        RegDomicilio temp = em.find(RegDomicilio.class, idCanton);
                        if (temp != null) {
                            mov.setDomicilio(temp);
                        }
                    }
                    mov.setIdTransaccion(data.getIdTransaccion());
                    mov.setIdPadre(data.getIdPadre());
                    mov.setIdBlobReg(data.getIdBlobReg());
                    break;

                default:
                    mov.setNumInscripcion(Integer.parseInt(data.getF04()));
                    mov.setTomoRegistro(data.getF03());
                    mov.setNumTomo(data.getF01());
                    mov.setNumRepertorio(0);
                    if (data.getF05() != null && !data.getF05().isEmpty()) {
                        if (Utils.isNum(data.getF05())) {
                            mov.setNumRepertorio(Integer.parseInt(data.getF05()));
                        }
                    }
                    if (data.getF10() != null && !data.getF10().isEmpty()) {
                        mov.setFechaInscripcion(sdf.parse(data.getF10()));
                    }
                    if (data.getF31() != null && !data.getF31().isEmpty()) {
                        mov.setValorUuid(data.getF31());
                    }
                    if (data.getF38() != null && !data.getF38().isEmpty()) {
                        mov.setFechaOto(sdf.parse(data.getF38()));
                    }
                    mov.setLibro(new RegLibro(data.getIdCarpeta().getIdTipoDoc().getIdLibro()));
                    idNotaria = this.getIdNotaria(data.getF36());
                    if (idNotaria > 0) {
                        RegEnteJudiciales temp = em.find(RegEnteJudiciales.class, idNotaria);
                        if (temp != null) {
                            mov.setEnteJudicial(temp);
                        }
                    }
                    idCanton = this.getIdCanton(data.getF37());
                    if (idCanton > 0) {
                        RegDomicilio temp = em.find(RegDomicilio.class, idCanton);
                        if (temp != null) {
                            mov.setDomicilio(temp);
                        }
                    }
                    mov.setIdTransaccion(data.getIdTransaccion());
                    mov.setIdPadre(data.getIdPadre());
                    mov.setIdBlobReg(data.getIdBlobReg());
                    break;
            }
            return mov;
        } catch (NumberFormatException | ParseException e) {
            System.out.println(e);
            return null;
        }
    }

    public Long getIdNotaria(String code) {
        if (code != null && !code.isEmpty()) {
            if (code.contains("PRIMERA")) {
                return 209L;
            }
            if (code.contains("SEGUNDA")) {
                return 210L;
            }
            if (code.contains("TERCERA")) {
                return 211L;
            }
            if (code.contains("CUARTA")) {
                return 212L;
            }
            if (code.contains("QUINTA")) {
                return 213L;
            }
            if (code.contains("SEXTA")) {
                return 214L;
            }
        }
        return 0L;
    }

    public Long getIdCanton(String code) {
        if (code != null && !code.isEmpty()) {
            if (code.contains("LATACUNGA")) {
                return 39L; 
            }
            if (code.contains("QUITO")) {
                return 73L; 
            }
            if (code.contains("SAQUISIL")) {
                return 174L; 
            }
            if (code.contains("GUAYAQUIL")) {
                return 34L; 
            }
            if (code.contains("SALCEDO")) {
                return 77L; 
            }
            if (code.contains("AMBATO")) {
                return 3L; 
            }
        }
        return 0L;
    }
    
    public TbDataLazy getDataLazy() {
        return dataLazy;
    }

    public void setDataLazy(TbDataLazy dataLazy) {
        this.dataLazy = dataLazy;
    }

    public List<TbTipoDocCab> getLibros() {
        return libros;
    }

    public void setLibros(List<TbTipoDocCab> libros) {
        this.libros = libros;
    }

    public List<TbCarpetas> getAnios() {
        return anios;
    }

    public void setAnios(List<TbCarpetas> anios) {
        this.anios = anios;
    }

    public TbTipoDocCab getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TbTipoDocCab tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public TbCarpetas getTipoCarpeta() {
        return tipoCarpeta;
    }

    public void setTipoCarpeta(TbCarpetas tipoCarpeta) {
        this.tipoCarpeta = tipoCarpeta;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

}
