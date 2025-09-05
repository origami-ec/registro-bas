/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimientoFichas;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimientoPartes;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimiento;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimiento;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoCapitales;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoRepresentantes;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoSocios;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.NprmSri;
import com.origami.sgr.entities.NprmSriCatalogo;
import com.origami.sgr.entities.NprmSriDetalle;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoParticipante;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RenDatosFacturaElectronica;
import com.origami.sgr.entities.UafInt;
import com.origami.sgr.entities.UafNacionalidad;
import com.origami.sgr.entities.UafPapelInterv;
import com.origami.sgr.entities.UafTra;
import com.origami.sgr.entities.UafTramite;
import com.origami.sgr.entities.UafUsuarios;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.ClaveValorModel;
import com.origami.sgr.services.interfaces.AnexosLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import com.origami.sql.ConsultasSqlLocal;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ReportesExternos implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReportesExternos.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    protected UserSession us;

    @EJB
    protected Entitymanager em;

    @EJB(beanName = "anexos")
    private AnexosLocal an;

    @Inject
    private ConsultasSqlLocal sql;

    protected Map map;
    protected Integer tipoanexo, mes, anio, mesSri, anioSri, mesUaf, anioUaf, tipoanexosedi = 0;
    protected int tipoanexoUAFE;
    protected Date corte, corteHasta, fechaModificacion;
    protected String cadena, codigoResu;
    protected Calendar desde, hasta;
    protected UafUsuarios uaf;
    protected SimpleDateFormat sdf;

    private List<UafTra> uafTras;
    private List<UafTra> uafTrasRevisar;
    private UafTra tra;
    private List<UafInt> uafNits;
    private UafInt nit;
    private List<RegMovimiento> movimientosSeleccionados;
    private RegMovimientosLazy movimientos;
    private RegMovimiento movimiento;
    private NprmSriDetalle dp = new NprmSriDetalle();
    private Integer index;
    private List<NprmSriDetalle> detallesMovimiento = new ArrayList<>();
    private NprmSri anexo;
    private Integer tipoparticipante = 0;
    private List<RegEnteInterviniente> participantes = new ArrayList<>();
    private List<RegMovimientoParticipante> participantesAdd = new ArrayList<>();
    private List<RegMovimientoParticipante> participantesTemp = new ArrayList<>();
    private List<RegMovimiento> listMovCap;
    private List<NprmSriCatalogo> sriCatalogoTransaccion;
    private RegMovimientosLazy lazy;
    private List<ClaveValorModel> anios;
    private List<ClaveValorModel> meses;
    private String archivoanexosedi;
    private Boolean umbral = true;

    @PostConstruct
    protected void iniView() {
        try {
            desde = Calendar.getInstance();
            anexo = new NprmSri();
            anexo.setAnio(desde.get(Calendar.YEAR));
            anexo.setMes(desde.get(Calendar.MONTH));
            //anexo.setTipoAnexo(1);
            uafTras = new ArrayList<>();
            uafTrasRevisar = new ArrayList<>();
            uafNits = new ArrayList<>();
            tipoanexoUAFE = 0;
            tipoanexo = 0;
            corte = new Date();
            movimiento = new RegMovimiento();
            mes = desde.get(Calendar.MONTH);
            anio = desde.get(Calendar.YEAR);
            mesSri = desde.get(Calendar.MONTH);
            anioSri = desde.get(Calendar.YEAR);
            mesUaf = desde.get(Calendar.MONTH);
            anioUaf = desde.get(Calendar.YEAR);
            tra = new UafTra();
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("aclUser", new AclUser(us.getUserId()));
            uaf = (UafUsuarios) em.findObjectByParameter(UafUsuarios.class, map);
            tipoanexoUAFE = 1;
            buscarMovientos();
            generarAnios(2023);
            generarMeses();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void generarAnios(Integer desde) {
        if (Utils.isEmpty(anios)) {
            anios = new ArrayList<>();
        }
        Integer anioActual = Utils.getAnio(new Date());
        anioActual = anioActual + 30;
        for (int i = desde; i <= anioActual; i++) {
            anios.add(new ClaveValorModel(i + "", i));
        }
    }

    private void generarMeses() {
        if (Utils.isEmpty(meses)) {
            meses = new ArrayList<>();
        }
        meses.add(new ClaveValorModel("Enero", 0));
        meses.add(new ClaveValorModel("Febrero", 1));
        meses.add(new ClaveValorModel("Marzo", 2));
        meses.add(new ClaveValorModel("Abril", 3));
        meses.add(new ClaveValorModel("Mayo", 4));
        meses.add(new ClaveValorModel("Junio", 5));
        meses.add(new ClaveValorModel("Julio", 6));
        meses.add(new ClaveValorModel("Agosto", 7));
        meses.add(new ClaveValorModel("Septiembre", 8));
        meses.add(new ClaveValorModel("Octubre", 9));
        meses.add(new ClaveValorModel("Noviembre", 10));
        meses.add(new ClaveValorModel("Diciembre", 11));
    }

    public void generarSedi() {
        try {
            if (corte != null) {
                switch (tipoanexosedi) {
                    case 1:
                        archivoanexosedi = an.getAnexo1Resolucion12(corte);
                        break;
                    case 2:
                        archivoanexosedi = an.getAnexo2Side12(corte);
                        break;
                    case 3:
                        archivoanexosedi = an.getAnexo3Side12(corte);
                        break;
                }
                if (archivoanexosedi != null) {
                    JsfUti.messageInfo(null, "Anexo generado con Exito!!!", "");
                    ss.setContentType("application/txt");
                    ss.setNombreDocumento(archivoanexosedi);
                    JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
                } else {
                    JsfUti.messageError(null, "No se pudo generar el anexo seleccionado.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Seleccione la fecha de corte del reporte.", "");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarAnexoCotad() {
        try {
            sdf = new SimpleDateFormat("MM-yyyy");
            desde.set(anexo.getAnio(), anexo.getMes(), 1);

            String fecha_inscripcion = sdf.format(desde.getTime());
            List<AnexoCotadDetalleMovimiento> movimientos = sql.getMovimientosAnexoCotad(fecha_inscripcion);
            System.out.println("Tamaño lista movimiento:" + movimientos.size());

            if (movimientos.size() > 0) {
                for (Integer i = 0; i < movimientos.size(); i++) {
                    List<AnexoCotadDetalleMovimientoPartes> listIntervinientes = sql.getAnexoCotadIntervinientes(movimientos.get(i).getMovimiento_id());
                    List<AnexoCotadDetalleMovimientoFichas> listFichas = sql.getAnexoCotadFichas(movimientos.get(i).getMovimiento_id());
                    movimientos.get(i).setIntervinientes(listIntervinientes);
                    movimientos.get(i).setFichas(listFichas);
                    movimientos.get(i).setROW_NUMBER(i + 1);
                }

                sdf = new SimpleDateFormat("MMMM");
                String mes = Utils.convertirMesALetra(anexo.getMes());

                ss.instanciarParametros();
                ss.setTieneDatasource(false);
                ss.setDataSource(movimientos);
                ss.setNombreSubCarpeta("anexos");
                ss.setNombreReporte("anexoCotad");
                ss.agregarParametro("ANIO", anexo.getAnio().toString());
                ss.agregarParametro("MES", mes);
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                ss.agregarParametro("FECHA_INSCRIPCION", fecha_inscripcion);
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("USER_NAME", us.getName_user());
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "No hay registros en el periodo seleccionado.", "");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarAnexoSupercia() {
        try {
            sdf = new SimpleDateFormat("MM-yyyy");
            desde.set(anexo.getAnio(), anexo.getMes(), 1);

            String fecha_inscripcion = sdf.format(desde.getTime());
            List<AnexoSuperciaDetalleMovimiento> movimientos = sql.getMovimientosAnexoSupercia(fecha_inscripcion);
            System.out.println("Tamaño lista movimiento:" + movimientos.size());

            if (movimientos.size() > 0) {
                for (Integer i = 0; i < movimientos.size(); i++) {
                    List<AnexoCotadDetalleMovimientoPartes> listIntervinientes = sql.getAnexoCotadIntervinientes(movimientos.get(i).getMovimiento_id());
                    List<AnexoSuperciaDetalleMovimientoRepresentantes> listRepresentantes = sql.getAnexoSuperciaRepresentantes(movimientos.get(i).getMovimiento_id());
                    List<AnexoSuperciaDetalleMovimientoCapitales> listCapitales = sql.getAnexoSuperciaCapitales(movimientos.get(i).getMovimiento_id());
                    List<AnexoSuperciaDetalleMovimientoSocios> listSocios = sql.getAnexoSuperciaSocios(movimientos.get(i).getMovimiento_id());
                    movimientos.get(i).setIntervinientes(listIntervinientes);
                    movimientos.get(i).setRepresentantes(listRepresentantes);
                    movimientos.get(i).setCapitales(listCapitales);
                    movimientos.get(i).setSocios(listSocios);
                    movimientos.get(i).setROW_NUMBER(i + 1);
                }

                String mes = Utils.convertirMesALetra(anexo.getMes());
                ss.instanciarParametros();
                ss.setTieneDatasource(false);
                ss.setDataSource(movimientos);
                ss.setNombreSubCarpeta("anexos");
                ss.setNombreReporte("anexoSupercia");
                ss.agregarParametro("ANIO", anexo.getAnio().toString());
                ss.agregarParametro("MES", mes);
                ss.agregarParametro("FECHA_INSCRIPCION", fecha_inscripcion);
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("USER_NAME", us.getName_user());
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "No hay registros en el periodo seleccionado.", "");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarDetalleResu() {
        try {
            if (uaf != null) {
                desde.clear();
                desde.set(anioUaf, mesUaf, 1);
                File temp = an.archivoDetalleResu(desde, codigoResu);
                if (temp != null) {
                    JsfUti.messageInfo(null, "Anexo generado con Exito!!!", "");
                    fechaModificacion = new Date();
                    fechaModificacion.setTime(temp.lastModified());
                    ss.setContentType("application/xml");
                    ss.setNombreDocumento(temp.getAbsolutePath());
                    JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
                } else {
                    JsfUti.messageError(null, "No se pudo generar el anexo seleccionado.", "");
                }
            } else {
                JsfUti.messageWarning(null, "El usuario debe estar autorizado para generar este anexo.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarCabeceraResu() {
        try {
            if (uaf != null && codigoResu != null) {
                desde.clear();
                desde.set(anioUaf, mesUaf, 1);
                File temp = an.archivoCabeceraResu(desde, codigoResu, uaf);
                if (temp != null) {
                    JsfUti.messageInfo(null, "Anexo generado con Exito!!!", "");
                    fechaModificacion = new Date();
                    fechaModificacion.setTime(temp.lastModified());
                    ss.setContentType("application/xml");
                    ss.setNombreDocumento(temp.getAbsolutePath());
                    JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
                } else {
                    JsfUti.messageError(null, "No se pudo generar el anexo seleccionado.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Falta que ingrese un dato obligatorio", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarAnexoNrpm() {
        try {
            if (anexo == null) {
                JsfUti.messageError(null, "Debe Buscar los movimientos para poder generar el archivo", "");
                return;
            }
            if (anexo.getId() == null) {
                JsfUti.messageError(null, "Debe Buscar los movimientos para poder generar el archivo", "");
                return;
            }
            desde.clear();
            desde.set(anexo.getAnio(), anexo.getMes(), 1);
            map = new HashMap();
            map.put("code", Constantes.datosFacturaElectronica);
            RenDatosFacturaElectronica dfe = (RenDatosFacturaElectronica) em.findObjectByParameter(RenDatosFacturaElectronica.class, map);
            String documento = an.anexoNrpm1(anexo, desde.getTime(), dfe.getRuc());
            if (documento != null) {
                JsfUti.messageInfo(null, "Anexo generado con Exito!!!", "");
                ss.setContentType("application/xml");
                ss.setNombreDocumento(documento);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarMovientos() {
        try {
            sdf = new SimpleDateFormat("MM-yyyy");
            desde.clear();
            desde.set(anio, mes, 1);
            Date desde1 = desde.getTime();
            desde.add(Calendar.MONTH, 1);
            desde.add(Calendar.SECOND, -1);
            Date hasta1 = desde.getTime();
            String query = "SELECT u FROM UafTra u WHERE u.periodo = :periodo";
            uafTras = em.findAll(query, new String[]{"periodo"}, new Object[]{sdf.format(desde1)});
            if (uafTras == null) {
                uafTras = new ArrayList<>();
            }
            List<RegMovimiento> movs = new ArrayList<>();
            for (UafTra u : uafTras) {
                if (u.isSeleccionado()) {
                    if (!movs.contains(u.getMovimiento())) {
                        movs.add(u.getMovimiento());
                    }
                }
            }
            if (!movs.isEmpty()) {
                movimientosSeleccionados = movs;
            }
            if (this.umbral) {
                movimientos = new RegMovimientosLazy(desde1, hasta1, new BigDecimal("9999.99"));
            } else {
                movimientos = new RegMovimientosLazy(desde1, hasta1, new BigDecimal("0.01"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generarUafINT() {
        try {
            if (uaf != null) {
                desde.clear();
                desde.set(anio, mes, 1);
                hasta = Calendar.getInstance();
                hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("anexoUafINT");
                ss.setNombreSubCarpeta("anexos");
                sdf = new SimpleDateFormat("yyyyMMdd");
                ss.setNombreDocumento("INT" + Constantes.uafCodigoRegistro + sdf.format(hasta.getTime()));
                sdf = new SimpleDateFormat("MM-yyyy");
                ss.agregarParametro("PERIODO", sdf.format(hasta.getTime()));
                int cant = 0;
                for (UafTra u : uafTras) {
                    for (UafInt n : u.getUafNitList()) {
                        if (n.isSeleccionado()) {
                            cant++;
                        }
                    }
                }
                ss.agregarParametro("TOTAL_INT", cant);
                sdf = new SimpleDateFormat("yyyyMMdd");
                ss.agregarParametro("DATE_STRING", sdf.format(hasta.getTime()));
                ss.agregarParametro("CODIGO_REGISTRO", Constantes.uafCodigoRegistro);
                JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
            } else {
                JsfUti.messageWarning(null, "El usuario debe estar autorizado para generar este anexo.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "Error al generar el archivo del anexo.", "");
        }
    }

    public void generarUafTRA() {
        try {
            if (uaf != null) {
                desde.clear();
                desde.set(anio, mes, 1);
                hasta = Calendar.getInstance();
                hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("anexoUafTRA");
                ss.setNombreSubCarpeta("anexos");
                sdf = new SimpleDateFormat("yyyyMMdd");
                ss.setNombreDocumento("TRA" + Constantes.uafCodigoRegistro + sdf.format(hasta.getTime()));
                sdf = new SimpleDateFormat("MM-yyyy");
                ss.agregarParametro("PERIODO", sdf.format(hasta.getTime()));
                ss.agregarParametro("TOTAL_TRA", movimientosSeleccionados.size());
                sdf = new SimpleDateFormat("yyyyMMdd");
                ss.agregarParametro("DATE_STRING", sdf.format(hasta.getTime()));
                ss.agregarParametro("CODIGO_REGISTRO", Constantes.uafCodigoRegistro);
                ss.agregarParametro("CODIGO_CANTON", Constantes.uafCanton);
                JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
            } else {
                JsfUti.messageWarning(null, "El usuario debe estar autorizado para generar este anexo.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "Error al generar el archivo del anexo.", "");
        }
    }

    public void onRowSelectUAF(SelectEvent event) {
        movimiento = (RegMovimiento) event.getObject();
        listarParticipantes(movimiento);
    }

    public void onRowUnselectUAF(UnselectEvent event) {
        movimiento = (RegMovimiento) event.getObject();

        for (UafTra u : uafTras) {
            if (u.getMovimiento().getId().equals(movimiento.getId())) {
                tra = u;
                tra.setSeleccionado(false);
                break;
            }
        }

        tra = (UafTra) em.persist(tra);

        int indexOf = uafTras.indexOf(tra);
        if (indexOf != -1) {
            uafTras.remove(indexOf);
        }

        JsfUti.update("mainForm");
    }

    public void listarParticipantes(RegMovimiento movimiento) {
        if (movimiento.getCuantiaCadena() != null && (movimiento.getCuantia() == null || movimiento.getCuantia().compareTo(BigDecimal.ZERO) == 0)) {
            String aux = replaceCharactes(movimiento.getCuantiaCadena());
            if (aux != null) {
                movimiento.setCuantia(new BigDecimal(aux));
            }
        } else if (movimiento.getBaseImponible() != null && movimiento.getBaseImponible().compareTo(BigDecimal.ZERO) > 0) {
            movimiento.setCuantia(movimiento.getBaseImponible());
        }
        this.movimiento = movimiento;
        try {
            String query = "SELECT u FROM UafTra u WHERE u.movimiento.id = :idMovimiento AND u.periodo = :periodo";
            sdf = new SimpleDateFormat("MM-yyyy");
            List<UafTra> allTras = em.findAll(query, new String[]{"idMovimiento", "periodo"},
                    new Object[]{this.movimiento.getId(), sdf.format(this.movimiento.getFechaInscripcion())});
            if (allTras != null) {
                if (!allTras.isEmpty()) {
                    tra = allTras.get(0);
                    if (!uafTras.contains(tra)) {
                        uafTras.add(tra);
                    }
                } else {
                    tra = new UafTra();
                    tra.setMovimiento(this.movimiento);
                }
            } else {
                tra = new UafTra();
                tra.setMovimiento(this.movimiento);
            }
            if (!uafTras.contains(tra)) {
                sdf = new SimpleDateFormat("yyyyMMdd");
                tra.setNit(this.movimiento.getNumInscripcion() + "");
                tra.setFcr(sdf.format(this.movimiento.getFechaInscripcion()));
                tra.setDtm(Utils.quitarCaracteresEspeciales(Utils.quitarTildes(Utils.quitarSaltos(this.movimiento.getObservacion()))));
                tra.setLibro(this.movimiento.getLibro().getNombre());
                tra.setActo(this.movimiento.getActo().getNombre());
                if (tra.getActo().equalsIgnoreCase("COMPRAVENTA")) {
                    tra.setUafTramite(new UafTramite(2L));
                }
                tra.setRegistro(this.movimiento.getLibro().getAnexoUnoRegPropiedad());
                String vcc;
                if (this.movimiento.getCuantia() != null) {
                    vcc = this.movimiento.getCuantia().intValue() + "";
                } else {
                    vcc = replaceCharactes(this.movimiento.getCuantiaCadena());
                }
                tra.setVcc(vcc);
                BigInteger idFicha = (BigInteger) em.getNativeQuery("select max(m.ficha) from app.reg_movimiento_ficha m where m.movimiento = " + this.movimiento.getId());
                if (idFicha != null) {
                    RegFicha ficha = em.find(RegFicha.class, idFicha.longValue());
                    if (ficha != null) {
                        if (ficha.getDescripcionBien() != null) {
                            tra.setDtm(Utils.quitarCaracteresEspeciales(Utils.quitarTildes(Utils.quitarSaltos(ficha.getDescripcionBien()))));
                        }
                        tra.setCca(ficha.getClaveCatastral().replaceAll("\\s+", " ").replaceAll(" ", "-").trim());
                        if (ficha.getDireccionBien() != null && !ficha.getDireccionBien().isEmpty()) {
                            tra.setDrb(Utils.quitarCaracteresEspeciales(Utils.quitarTildes(Utils.quitarSaltos(ficha.getDireccionBien()))));
                        } else if (ficha.getDescripcionBien() != null) {
                            tra.setDrb(Utils.quitarCaracteresEspeciales(Utils.quitarTildes(Utils.quitarSaltos(ficha.getDescripcionBien()))));
                        }
                        if (ficha.getUafTipoBien() != null) {
                            tra.setTtb(ficha.getUafTipoBien().getCodigo());
                        }
                    }
                }
                sdf = new SimpleDateFormat("MM-yyyy");
                tra.setPeriodo(sdf.format(this.movimiento.getFechaInscripcion()));
                uafNits.clear();
                List<RegMovimientoCliente> clientes = em.findAll("SELECT r FROM RegMovimientoCliente r  WHERE r.movimiento.id = :id",
                        new String[]{"id"}, new Object[]{this.movimiento.getId()});
                if (clientes == null || clientes.isEmpty()) {
                    clientes = new ArrayList<>();
                }
                clientes.forEach((mc) -> {
                    nit = new UafInt();
                    nit.setNit(this.movimiento.getNumInscripcion() + "");
                    nit.setIdi(mc.getEnteInterv().getCedRuc());
                    nit.setNri(mc.getEnteInterv().getNombre());
                    nit.setPapel(mc.getPapel().getNombre());
                    if (nit.getPapel().equalsIgnoreCase("comprador")) {
                        nit.setRdi("02");
                        nit.setPdi("20");
                    }
                    if (nit.getPapel().equalsIgnoreCase("vendedor")) {
                        nit.setRdi("01");
                        nit.setPdi("63");
                    }
                    nit.setUafTra(tra);
                    nit.setPeriodo(sdf.format(this.movimiento.getFechaInscripcion()));
                    nit.setSeleccionado(Boolean.TRUE);
                    uafNits.add(nit);
                });
                tra.setUafNitList(uafNits);
            } else {
                int indexOf = uafTras.indexOf(tra);
                tra = uafTras.get(indexOf);
                tra.setDtm(Utils.quitarCaracteresEspeciales(Utils.quitarTildes(Utils.quitarSaltos(tra.getDtm()))));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        JsfUti.update("formUAF");
        JsfUti.update("formUAF:uafTable");
        JsfUti.executeJS("PF('dlgUAF').show();");
    }

    public String replaceCharactes(String text) {
        if (text == null) {
            return null;
        }
        if (text.toUpperCase().contains("INDETERMIN")) {
            return null;
        }
        String aux = text.substring(0, text.length() - 3);
        String aux1 = text.substring(text.length() - 3);
        if (aux1.contains(",")) {
            aux1 = aux1.replace(",", ".");
        }
        text = aux.replaceAll("[^a-zA-Z0-9]", "");
        return text + aux1;
    }

    public boolean checkMovimiento(UafTra mov) {

        if (mov.getUafTramite() == null) {
            return false;
        }
        int cont = 0, contOk = 0, contMal = 0;

        for (UafInt n : mov.getUafNitList()) {
            if (n.isSeleccionado()) {
                cont++;
                if (n.getNai() == null || n.getRdi() == null || n.getPdi() == null) {
                    contMal++;
                } else {
                    contOk++;
                }

            }
        }
        if (contMal != 0) {
            return false;
        }

        return (contOk == cont && cont != 0);

    }

    public void guardarEditMovTraInt() {
        // GUARDADO DE UAFTRA
        if (checkMovimiento(tra)) {
            tra.setSeleccionado(true);
            tra = (UafTra) em.persist(tra);

            int indexOf = uafTras.indexOf(tra);
            if (indexOf != -1) {
                uafTras.set(indexOf, tra);
            } else {
                uafTras.add(tra);
            }
            List<RegMovimiento> movs = new ArrayList<>();
            for (UafTra u : uafTras) {
                if (u.isSeleccionado()) {
                    if (!movs.contains(u.getMovimiento())) {
                        movs.add(u.getMovimiento());
                    }
                }
            }

            if (!movs.isEmpty()) {
                movimientosSeleccionados = movs;
            }
            JsfUti.update("formUAF");
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgUAF').hide();");
            JsfUti.messageInfo(null, "Moviento actualizado con exito.", "");
        } else {
            JsfUti.messageWarning(null, "Debe de completar la informacion para poder procesar el Movimiento.", "");
        }
    }

    public void guardarEditMovTraIntCheck() {

        for (UafTra u : uafTrasRevisar) {
            tra = (UafTra) em.persist(u);
            int indexOf = uafTras.indexOf(tra);
            if (indexOf != -1) {
                uafTras.set(indexOf, tra);
            } else {
                uafTras.add(tra);
            }
        }
        JsfUti.update("formUAFCheck");
        JsfUti.executeJS("PF('dlgUAFCheck').hide();");
    }

    public List<UafPapelInterv> getUafPapeles() {
        return em.findAllEntCopy(Querys.getUafPapeles);
    }

    public List<UafTramite> getUafTramites() {
        return em.findAllEntCopy("SELECT u FROM UafTramite u");
    }

    public List<UafNacionalidad> getUafNacionalidades() {
        return em.findAllEntCopy("SELECT u FROM UafNacionalidad u");
    }

    public void renderTipoActo() {
        try {
            dp.getMovimientoParticipante().setTipotransaccion(dp.getTipoTransaccion().getCodigo());
            switch (dp.getTipoTransaccion().getCodigo()) {
                case "12": //DERECHOS Y ACCIONES
                    dp.getMovimientoParticipante().setTipoParticipanteEntrega(dp.getTipoTransaccion().getEntrega());
                    dp.getMovimientoParticipante().setTipoParticipanteRecibe(dp.getTipoTransaccion().getRecibe());
                    break;
                default: //OTROS
                    dp.getMovimientoParticipante().setTipoParticipanteEntrega(dp.getTipoTransaccion().getEntrega());
                    dp.getMovimientoParticipante().setTipoParticipanteRecibe(dp.getTipoTransaccion().getRecibe());
                    break;
            }
            JsfUti.update("frmAnexoSri");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgParticipantes(int a) {
        try {
            if (this.anexo.getAnexoUnoRegPropiedad()) {
//                if (movimiento.getVinculoFamiliar() != null && movimiento.getVinculoFamiliar()) {
                tipoparticipante = a;
                participantes = new ArrayList<>();
                movimiento.getRegMovimientoClienteCollection().forEach((mc) -> {
                    participantes.add(mc.getEnteInterv());
                });
                JsfUti.update("formNrpm");
                JsfUti.executeJS("PF('dlgNrpm').show();");
//                } else {
//                    JsfUti.messageWarning(null, "Debe seleccionar si hay transferencia en vinculo familiar.", "");
//                }
            } else {
                tipoparticipante = a;
                participantes = new ArrayList<>();
                movimiento.getRegMovimientoClienteCollection().forEach((mc) -> {
                    participantes.add(mc.getEnteInterv());
                });
                JsfUti.update("formNrpm");
                JsfUti.executeJS("PF('dlgNrpm').show();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarParticipante(RegEnteInterviniente pa) {
        try {

            if (tipoparticipante == 1) {
//                mp.setEntrega(pa);
                dp.getMovimientoParticipante().setEntrega(pa);
                JsfUti.update("frmAnexoSri1:pngTipoEntrega1");
            } else if (tipoparticipante == 2) {
//                mp.setRecibe(pa);
                dp.getMovimientoParticipante().setRecibe(pa);
                JsfUti.update("frmAnexoSri1:pnlPart2");
            }
            JsfUti.update("frmAnexoSri");
            JsfUti.update("frmAnexoSriMc");
            JsfUti.executeJS("PF('dlgNrpm').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarParticipantes(RegMovimiento movimiento) {
        this.movimiento = movimiento;
        try {
            if (!anexo.existeMovimiento(movimiento)) {
                anexo.setFecchaIngreso(new Date());
                anexo.setUsuarioIngreso(this.us.getName_user());
                Map<String, Object> pm = new HashMap<>();
                pm.put("movimiento", this.movimiento);
                List<RegMovimientoParticipante> all = this.em.findObjectByParameterList(RegMovimientoParticipante.class, pm);
                dp = new NprmSriDetalle();
                dp.setMovimiento(BigInteger.valueOf(this.movimiento.getId()));
                dp.setMovimientoParticipante(new RegMovimientoParticipante());
                if (movimiento.getActo() != null && movimiento.getActo().getTransaccion() != null) {
                    pm = new HashMap<>();
                    pm.put("codigo", movimiento.getActo().getTransaccion());
                    dp.setTipoTransaccion(this.em.findObjectByParameter(NprmSriCatalogo.class, pm));
                }
                if (Utils.isEmpty(all)) {
                    movimiento.getRegMovimientoClienteCollection().forEach((mc) -> {
                        RegMovimientoParticipante rmp = new RegMovimientoParticipante();
//                        rmp.set
                        participantesTemp.add(rmp);
                    });
                    if (this.anexo.getAnexoUnoRegPropiedad()) {
                        JsfUti.update("frmAnexoSri");
                        JsfUti.executeJS("PF('dlgParticipantes').show();");
                    } else {
                        JsfUti.update("frmAnexoSriMc");
                        JsfUti.executeJS("PF('dlgParticipantesMC').show();");
                    }
                } else {
                    participantesTemp.addAll(all);
                    dp.setMovimientoParticipante(all.get(0));
                    if (this.anexo.getAnexoUnoRegPropiedad()) {
                        JsfUti.update("frmAnexoSri");
                        JsfUti.executeJS("PF('dlgParticipantes').show();");
                    } else {
                        JsfUti.update("frmAnexoSriMc");
                        JsfUti.executeJS("PF('dlgParticipantesMC').show();");
                    }
                }
            } else {
                dp = anexo.getDetalleMovimiento(movimiento);
                if (dp.getMovimientoParticipante() == null) {
                    dp.setMovimientoParticipante(new RegMovimientoParticipante());
                }
                if (this.anexo.getAnexoUnoRegPropiedad()) {
                    JsfUti.update("frmAnexoSri");
                    JsfUti.executeJS("PF('dlgParticipantes').show();");
                } else {
                    JsfUti.update("frmAnexoSriMc");
                    JsfUti.executeJS("PF('dlgParticipantesMC').show();");
                }
            }
        } catch (HibernateException hibernateException) {
            LOG.log(Level.SEVERE, null, hibernateException);
        }
    }

    public void agregarParticipantesAll(RegMovimiento movimiento) {
        this.movimiento = movimiento;
        try {
            Map<String, Object> pm = null;
            NprmSriCatalogo catalogo = null;
            detallesMovimiento = new ArrayList<>();
            if (!anexo.existeMovimiento(movimiento)) {
                if (movimiento.getActo() != null) {
                    pm = new HashMap<>();
                    if (!Utils.isEmpty(movimiento.getActo().getTransaccion()).isEmpty()) {
                        pm.put("codigo", movimiento.getActo().getTransaccion());
                    } else {
                        pm.put("codigo", "11");
                    }
                    catalogo = this.em.findObjectByParameter(NprmSriCatalogo.class, pm);
                }
                HashMap<RegPapel, List<RegMovimientoCliente>> groupByPapel = movimiento.getGroupByPapel();
                if (groupByPapel != null) {
                    RegPapel papel = null;
                    for (Map.Entry<RegPapel, List<RegMovimientoCliente>> entry : groupByPapel.entrySet()) {
                        if (entry.getKey() != null && entry.getKey().getCodigoNrpm() != null) {
                            if (entry.getKey().getCodigoNrpm().trim().equals(catalogo.getEntrega())) {
                                papel = entry.getKey();
                                break;
                            }
                        } else {
                            papel = entry.getKey();
                        }
                    }
                    groupByPapel.remove(papel);
                    for (RegMovimientoCliente rmc : movimiento.getGroupByPapel().get(papel)) {
                        Collection<List<RegMovimientoCliente>> values = groupByPapel.values();
                        for (List<RegMovimientoCliente> recibeList : values) {
                            for (RegMovimientoCliente recibe : recibeList) {
                                dp = new NprmSriDetalle();
                                dp.setMovimiento(BigInteger.valueOf(this.movimiento.getId()));
                                RegMovimientoParticipante rmp = new RegMovimientoParticipante();
                                rmp.setEntrega(rmc.getEnteInterv());
                                if (Utils.isEmpty(papel.getCodigoNrpm()).isEmpty()) {
                                    rmp.setTipoParticipanteEntrega(catalogo.getEntrega());
                                } else {
                                    rmp.setTipoParticipanteEntrega(papel.getCodigoNrpm());
                                }
                                rmp.setRecibe(recibe.getEnteInterv());
                                if (Utils.isEmpty(recibe.getPapel().getCodigoNrpm()).isEmpty()) {
                                    rmp.setTipoParticipanteRecibe(catalogo.getRecibe());
                                } else {
                                    rmp.setTipoParticipanteRecibe(recibe.getPapel().getCodigoNrpm());
                                }
                                dp.setMovimientoParticipante(rmp);
                                dp.setTipoTransaccion(catalogo);
                                detallesMovimiento.add(dp);
                            }
                        }
                    }
                }
            } else {
                detallesMovimiento = anexo.getDetallesMovimiento(movimiento);
            }
            if (Utils.isNotEmpty(detallesMovimiento)) {
                dp = detallesMovimiento.get(0);
                index = 0;
            }
            if (this.anexo.getAnexoUnoRegPropiedad()) {
                JsfUti.update("frmAnexoSri1");
                JsfUti.executeJS("PF('dlgAnexoSRI1').show();");
            } else {
                JsfUti.update("frmAnexoSriMc");
                JsfUti.executeJS("PF('dlgParticipantesMC').show();");
            }
        } catch (HibernateException hibernateException) {
            LOG.log(Level.SEVERE, null, hibernateException);
        }
    }

    public void saveMovimientoParticipante() {
        try {
            if (dp.getMovimientoParticipante().getEntrega() == null) {
                JsfUti.messageWarning(null, "Seleccione quien entrega.", "");
                return;
            }
            if (dp.getMovimientoParticipante().getTipoParticipanteEntrega() == null) {
                JsfUti.messageWarning(null, "Seleccione quien tipo participante entrega.", "");
                return;
            }
            if (anexo.getTipoAnexo() != 2) {
                if (dp.getMovimientoParticipante().getRecibe() == null) {
                    JsfUti.messageWarning(null, "Seleccione quien recibe.", "");
                    return;
                }
            }
            if (anexo.getTipoAnexo() != 2) {
                if (!dp.getTipoTransaccion().getCodigo().equals("6")) {
                    if (dp.getMovimientoParticipante().getTipoParticipanteRecibe() == null) {
                        JsfUti.messageWarning(null, "Seleccione quien tipo participante recibe.", "");
                        return;
                    }
                }
            }
            if (dp.getId() == null) {
                Hibernate.initialize(movimiento);
                dp.getMovimientoParticipante().setMovimiento(movimiento);
                dp.setMovimientoParticipante((RegMovimientoParticipante) em.merge(dp.getMovimientoParticipante()));
                Hibernate.initialize(dp.getMovimientoParticipante());
                if (!listMovCap.contains(movimiento)) {
                    listMovCap.add(movimiento);
                }
                dp.setNprmSri(anexo);
                dp = (NprmSriDetalle) em.persist(dp);
                anexo.getNprmSriDetalles().add(dp);
                detallesMovimiento.set(index, dp);
            } else {
                this.em.update(dp.getMovimientoParticipante());
                this.em.update(dp);
//                participantesAdd.set(participantesAdd.indexOf(dp.getMovimientoParticipante()), dp.getMovimientoParticipante());
                anexo.getNprmSriDetalles().set(anexo.getNprmSriDetalles().indexOf(dp), dp);
            }
//            this.movimiento = new RegMovimiento();
            this.em.update(anexo);
            dp = new NprmSriDetalle();
            setIndex(-1);
            dp.setMovimientoParticipante(new RegMovimientoParticipante());
            JsfUti.update("frmAnexoSri1:selectRecibe1");
            JsfUti.update("frmAnexoSri1:selectEntrega1");
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void deleteMovimientoParticipante(RegMovimiento movimiento) {
        try {
            NprmSriDetalle detalle = anexo.getDetalleMovimiento(movimiento);
            participantesAdd.remove(detalle.getMovimientoParticipante());
            listMovCap.remove(movimiento);
            this.em.delete(detalle);
            anexo.getNprmSriDetalles().remove(detalle);
            this.movimiento = new RegMovimiento();
            this.em.persist(anexo);
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<NprmSriCatalogo> getSriCatalogoTransaccion() {
        if (Utils.isEmpty(sriCatalogoTransaccion)) {
            sriCatalogoTransaccion = this.an.getCatalogoSri(this.anexo.getTipoAnexo());
        }
        return sriCatalogoTransaccion;
    }

    public void buscarMovientosCapital() {
        try {
            /*if (anexo.getTipoAnexo() == 0) {
                JsfUti.messageWarning(null, "Seleccione el tipo de formulario para el anexo.", "");
                return;
            }*/
            desde.clear();
            desde.set(anexo.getAnio(), anexo.getMes(), 1);
            Date desde1 = desde.getTime();
            desde.add(Calendar.MONTH, 1);
            Date hasta1 = desde.getTime();
            anexo = this.an.buscarAnexos(tipoanexo, anexo.getTipoAnexo(), anexo.getMes(), anexo.getAnio());
            if (anexo.getFecchaIngreso() == null) {
                anexo.setFecchaIngreso(new Date());
            }
            if (anexo.getUsuarioIngreso() == null) {
                anexo.setUsuarioIngreso(us.getName_user());
            }
            lazy = new RegMovimientosLazy(desde1, hasta1);
            Map<String, Object> pm = new HashMap<>();
            pm.put("acto.anexoUnoRegPropiedad", true);
            lazy.setFilterss(pm);
            if (Utils.isNotEmpty(anexo.getNprmSriDetalles())) {
                for (NprmSriDetalle det : anexo.getNprmSriDetalles()) {
                    listMovCap.add(det.getMovimientoParticipante().getMovimiento());
                    det.getMovimientoParticipante().setTipotransaccion(det.getTipoTransaccion().getCodigo());
                    participantesAdd.add(det.getMovimientoParticipante());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void buscarMovientosSriMercantil() {
        try {
            desde.clear();
            desde.set(anexo.getAnio(), 0, 1);
            Date desde1 = desde.getTime();
            System.out.println("// fecha desde: " + desde1);
            //desde.add(Calendar.MONTH, 1);
            desde.set(anexo.getAnio(), 11, 31);
            Date hasta1 = desde.getTime();
            System.out.println("// fecha hasta: " + hasta1);
            anexo = this.an.buscarAnexos(tipoanexo, anexo.getTipoAnexo(), 0, anexo.getAnio());
            if (anexo.getFecchaIngreso() == null) {
                anexo.setFecchaIngreso(new Date());
            }
            if (anexo.getUsuarioIngreso() == null) {
                anexo.setUsuarioIngreso(us.getName_user());
            }
            lazy = new RegMovimientosLazy(desde1, hasta1);
            Map<String, Object> pm = new HashMap<>();
            pm.put("libro.anexoTresMercantil", true);
            lazy.setFilterss(pm);
            if (Utils.isNotEmpty(anexo.getNprmSriDetalles())) {
                for (NprmSriDetalle det : anexo.getNprmSriDetalles()) {
                    listMovCap.add(det.getMovimientoParticipante().getMovimiento());
                    det.getMovimientoParticipante().setTipotransaccion(det.getTipoTransaccion().getCodigo());
                    participantesAdd.add(det.getMovimientoParticipante());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void clear() {
        participantesAdd.clear();
    }

    public boolean filterByCuantia(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }
        System.out.println("filter : " + filterText);
        BigDecimal val = new BigDecimal(value.toString());

        return val.compareTo(new BigDecimal(filterText)) > 0;

    }

    public Integer getTipoanexo() {
        return tipoanexo;
    }

    public void setTipoanexo(Integer tipoanexo) {
        this.tipoanexo = tipoanexo;
    }

    public Date getCorte() {
        return corte;
    }

    public void setCorte(Date corte) {
        this.corte = corte;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMesSri() {
        return mesSri;
    }

    public void setMesSri(Integer mesSri) {
        this.mesSri = mesSri;
    }

    public Integer getAnioSri() {
        return anioSri;
    }

    public void setAnioSri(Integer anioSri) {
        this.anioSri = anioSri;
    }

    public Calendar getDesde() {
        return desde;
    }

    public void setDesde(Calendar desde) {
        this.desde = desde;
    }

    public String getCodigoResu() {
        return codigoResu;
    }

    public void setCodigoResu(String codigoResu) {
        this.codigoResu = codigoResu;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getMesUaf() {
        return mesUaf;
    }

    public void setMesUaf(Integer mesUaf) {
        this.mesUaf = mesUaf;
    }

    public Integer getAnioUaf() {
        return anioUaf;
    }

    public void setAnioUaf(Integer anioUaf) {
        this.anioUaf = anioUaf;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Integer getTipoanexosedi() {
        return tipoanexosedi;
    }

    public void setTipoanexosedi(Integer tipoanexosedi) {
        this.tipoanexosedi = tipoanexosedi;
    }

    public int getTipoanexoUAFE() {
        return tipoanexoUAFE;
    }

    public void setTipoanexoUAFE(int tipoanexoUAFE) {
        this.tipoanexoUAFE = tipoanexoUAFE;
    }

    public Calendar getHasta() {
        return hasta;
    }

    public void setHasta(Calendar hasta) {
        this.hasta = hasta;
    }

    public UafUsuarios getUaf() {
        return uaf;
    }

    public void setUaf(UafUsuarios uaf) {
        this.uaf = uaf;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public List<UafTra> getUafTras() {
        return uafTras;
    }

    public void setUafTras(List<UafTra> uafTras) {
        this.uafTras = uafTras;
    }

    public List<UafTra> getUafTrasRevisar() {
        return uafTrasRevisar;
    }

    public void setUafTrasRevisar(List<UafTra> uafTrasRevisar) {
        this.uafTrasRevisar = uafTrasRevisar;
    }

    public UafTra getTra() {
        return tra;
    }

    public void setTra(UafTra tra) {
        this.tra = tra;
    }

    public List<UafInt> getUafNits() {
        return uafNits;
    }

    public void setUafNits(List<UafInt> uafNits) {
        this.uafNits = uafNits;
    }

    public UafInt getNit() {
        return nit;
    }

    public void setNit(UafInt nit) {
        this.nit = nit;
    }

    public RegMovimientosLazy getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(RegMovimientosLazy movimientos) {
        this.movimientos = movimientos;
    }

    public List<RegMovimiento> getMovimientosSeleccionados() {
        return movimientosSeleccionados;
    }

    public void setMovimientosSeleccionados(List<RegMovimiento> movimientosSeleccionados) {
        this.movimientosSeleccionados = movimientosSeleccionados;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public AnexosLocal getAn() {
        return an;
    }

    public void setAn(AnexosLocal an) {
        this.an = an;
    }

    public NprmSriDetalle getDp() {
        return dp;
    }

    public void setDp(NprmSriDetalle dp) {
        this.dp = dp;
    }

    public NprmSri getAnexo() {
        return anexo;
    }

    public void setAnexo(NprmSri anexo) {
        this.anexo = anexo;
    }

    public Integer getTipoparticipante() {
        return tipoparticipante;
    }

    public void setTipoparticipante(Integer tipoparticipante) {
        this.tipoparticipante = tipoparticipante;
    }

    public List<RegEnteInterviniente> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<RegEnteInterviniente> participantes) {
        this.participantes = participantes;
    }

    public List<RegMovimientoParticipante> getParticipantesAdd() {
        return participantesAdd;
    }

    public void setParticipantesAdd(List<RegMovimientoParticipante> participantesAdd) {
        this.participantesAdd = participantesAdd;
    }

    public List<RegMovimiento> getListMovCap() {
        return listMovCap;
    }

    public void setListMovCap(List<RegMovimiento> listMovCap) {
        this.listMovCap = listMovCap;
    }

    public RegMovimientosLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegMovimientosLazy lazy) {
        this.lazy = lazy;
    }

    public List<ClaveValorModel> getAnios() {
        return anios;
    }

    public void setAnios(List<ClaveValorModel> anios) {
        this.anios = anios;
    }

    public List<ClaveValorModel> getMeses() {
        return meses;
    }

    public void setMeses(List<ClaveValorModel> meses) {
        this.meses = meses;
    }

    public List<NprmSriDetalle> getDetallesMovimiento() {
        return detallesMovimiento;
    }

    public void setDetallesMovimiento(List<NprmSriDetalle> detallesMovimiento) {
        this.detallesMovimiento = detallesMovimiento;
    }

    public Date getCorteHasta() {
        return corteHasta;
    }

    public void setCorteHasta(Date corteHasta) {
        this.corteHasta = corteHasta;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<RegMovimientoParticipante> getParticipantesTemp() {
        return participantesTemp;
    }

    public void setParticipantesTemp(List<RegMovimientoParticipante> participantesTemp) {
        this.participantesTemp = participantesTemp;
    }

    public Boolean getUmbral() {
        return umbral;
    }

    public void setUmbral(Boolean umbral) {
        this.umbral = umbral;
    }

}
