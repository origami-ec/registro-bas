/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.ebilling.interfaces.FacturacionElectronicaLocal;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.Feriados;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.MsgFormatoNotificacion;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudJuridico;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegTablaCuantia;
import com.origami.sgr.entities.RegTablaCuantiaDeterminada;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpLiquidacionExoneracion;
import com.origami.sgr.entities.RegpObservacionesIngreso;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenDatosFacturaElectronica;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenFacturaAnulada;
import com.origami.sgr.entities.RenNotaCredito;
import com.origami.sgr.entities.RenPago;
import com.origami.sgr.entities.RenPagoDetalle;
import com.origami.sgr.entities.RenPagoRubro;
import com.origami.sgr.entities.SecuenciaRepertorio;
import com.origami.sgr.entities.TarUsuarioTareas;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.models.PagoModel;
import com.origami.sgr.models.SamLiquidacionDetalles;
import com.origami.sgr.restful.services.RestServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.Email;
import com.origami.sgr.util.FacturaXml;
import com.origami.sgr.util.LeerFacturaXml;
import com.origami.sgr.util.Modulo11;
import com.origami.sgr.util.NroTomoEnum;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import com.origami.sgr.util.WhereClause;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.xml.transform.TransformerException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import com.origami.sql.ConsultasSQLService;

/**
 *
 * @author Anyelo
 */
@Stateless(name = "ingreso")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class IngresoTramiteEjb implements IngresoTramiteLocal {

    @Inject
    private Entitymanager manager;
    @Inject
    private SeqGenMan sec;
    @Inject
    private FacturacionElectronicaLocal fac;
    @Inject
    protected RegistroPropiedadServices reg;
    @Inject
    private RestServices restServices;
    @Inject
    private RegCertificadoService rcs;
    @Inject
    protected ConsultasSQLService sam;

    /**
     * Calculo del valor a pagar de un contrato que se calcule con el valor del
     * avaluo o la cuantia, parametrizado en una tabla de la BD
     *
     * @param valor Object
     * @return Retorna el valor a pagar del contrato
     */
    @Override
    public BigDecimal calculoCuantia(BigDecimal valor) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            //List<RegTablaCuantia> list = manager.findAll(RegTablaCuantia.class);
            List<RegTablaCuantia> list = manager.findAllObjectOrder(RegTablaCuantia.class, new String[]{"valor1"}, true);
            if (list != null) {
                for (RegTablaCuantia t : list) {
                    if (t.getValor2() != null) {
                        if ((valor.compareTo(t.getValor1()) >= 0) && (valor.compareTo(t.getValor2()) <= 0)) {
                            total = t.getCancelar();
                            break;
                        }
                    }
                }
                if (total.compareTo(BigDecimal.ZERO) <= 0) {
                    for (RegTablaCuantia t : list) {
                        if (t.getCancelar() == null) {
                            total = t.getValorBase();
                            valor = valor.subtract(t.getCantidadBase());
                            valor = valor.multiply(t.getExceso());
                            total = total.add(valor);
                            total = Utils.bigdecimalTo2Decimals(total);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    @Override
    public BigDecimal calculoCuantiaDeterminada(BigDecimal valor) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            List<RegTablaCuantiaDeterminada> list = manager.findAll(RegTablaCuantiaDeterminada.class);
            if (list != null) {
                RegTablaCuantiaDeterminada t = list.get(0);
                total = t.getValorBase();
                valor = valor.multiply(t.getExcesoValor());
                total = total.add(valor);
                total = Utils.bigdecimalTo2Decimals(total);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    @Override
    public BigDecimal calculoCuantia(BigDecimal valor, BigDecimal sbu) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            RegTablaCuantia rtc = (RegTablaCuantia) manager.find(Querys.getRegTablaCuantia,
                    new String[]{"cantidad1", "cantidad2"}, new Object[]{valor, valor});
            if (rtc == null) {
                rtc = (RegTablaCuantia) manager.find(Querys.getRegTablaCuantiaLimite);
                if (rtc != null) {
                    total = sbu.multiply(rtc.getExceso());
                }
            } else {
                total = sbu.multiply(rtc.getExceso());
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculoCuantiaSTD(BigDecimal valor) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            //List<RegTablaCuantia> list = manager.findAll(RegTablaCuantia.class);
            List<RegTablaCuantia> list = manager.findAllObjectOrder(RegTablaCuantia.class, new String[]{"valor1"}, true);
            if (list != null) {
                for (RegTablaCuantia t : list) {
                    if (t.getValor2() != null) {
                        if ((valor.compareTo(t.getValor1()) >= 0) && (valor.compareTo(t.getValor2()) <= 0)) {
                            valor = valor.subtract(t.getValor1());
                            total = t.getCancelar().add(valor.multiply(t.getExceso()));
                            total = Utils.bigdecimalTo2Decimals(total);
                            break;
                        }
                    }
                }
                if (total.compareTo(BigDecimal.ZERO) <= 0) {
                    for (RegTablaCuantia t : list) {
                        if (t.getCancelar() == null) {
                            valor = valor.subtract(t.getCantidadBase());
                            total = t.getValorBase().add(valor.multiply(t.getExceso()));
                            total = Utils.bigdecimalTo2Decimals(total);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return total;
    }

    /**
     * Generacion de la clave de acceso mediante los parametros especificados
     * por el SRI para los formatos de la facturacion electronica
     *
     * @param liquidacion Object
     * @param caja Object
     * @param fe Object
     * @return True si la generacion tuvo exito, false por lo contrario
     */
    @Override
    public String generarClaveAcceso(RegpLiquidacion liquidacion, RenCajero caja, RenDatosFacturaElectronica fe) {
        Modulo11 mo = new Modulo11();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String claveAcceso = "";
        try {
            if (liquidacion.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                if (fe != null) {
                    Random r = new Random();
                    int numero = r.nextInt(fe.getValorMax()) + fe.getValorMin();
                    System.out.println("sdf.format(liquidacion.getFechaIngreso()) " + sdf.format(liquidacion.getFechaIngreso()));
                    System.out.println("fe.getTipoComprobante() " + fe.getTipoComprobante());
                    System.out.println("fe.getRuc() " + fe.getRuc());
                    System.out.println("fe.getAmbiente() " + fe.getAmbiente());
                    System.out.println("fe.getSerie() " + fe.getSerie());
                    System.out.println("caja.getCodigoCaja() " + caja.getCodigoCaja());
                    System.out.println("liquidacion.getCodigoComprobante.replace.trim " + liquidacion.getCodigoComprobante().replace("-", "").trim());
                    System.out.println("numero " + numero);
                    System.out.println("fe.getTipoEmision()");
                    claveAcceso = sdf.format(liquidacion.getFechaIngreso())
                            + fe.getTipoComprobante()
                            + fe.getRuc()
                            + fe.getAmbiente()
                            + fe.getSerie() //ESTABLECIMIENTO
                            + caja.getCodigoCaja()
                            + liquidacion.getCodigoComprobante().trim()
                            + numero
                            + fe.getTipoEmision();
                    System.out.println("claveAcceso " + claveAcceso);
                    String verificador = mo.digitoVerificador(claveAcceso).toString();
                    System.out.println("verificador " + verificador);
                    claveAcceso = claveAcceso + verificador;
                    System.out.println("claveAcceso + verificador" + claveAcceso);
//                    if (claveAcceso.length() == 49) {
//                        liquidacion.setClaveAcceso(claveAcceso);
//                    }
                }
            }

        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return claveAcceso;
    }

    /**
     * Guarda un registro de una persona que no se le ingreso un numero de
     * cedula, en su lugar va un numero de secuencia
     *
     * @param ente Object
     * @return Object
     */
    @Override
    public CatEnte saveCatEnteSinCedRuc(CatEnte ente) {
        try {
            BigInteger cedRuc = sec.getSecuenciaEntes(Constantes.secuenciaEnte);
            ente.setCiRuc(cedRuc.toString());
            ente = (CatEnte) manager.persist(ente);
            System.out.println("el id persistido es---- " + ente.getId());
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return ente;
    }

    /**
     * Guarda un registro en la tabla regp_observaciones_ingreso, que se utiliza
     * para registrar las observaciones de los revisores antes de generar la
     * proforma para el ingreso del tramite
     *
     * @param ob Object
     * @return Object
     */
    @Override
    public RegpObservacionesIngreso saveObservacionIngreso(RegpObservacionesIngreso ob) {
        try {
            if (ob.getNumero() == null) {
                ob.setNumero(sec.getSecuenciaGeneral(Constantes.secuenciaObservacion));
            }
            ob = (RegpObservacionesIngreso) manager.persist(ob);
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return ob;
    }

    /**
     * Guarda la proforma con estado pendiente pago para que sea ingresada
     * despues de cancelar el pago, la liquidacion esta compuesta de las tablas
     * historico_tramites, regp_liquidacion y sus respectivos detalles
     *
     * @param liq Object
     * @param actosPorPagar Object
     * @return Object
     */
    @Override
    public RegpLiquidacion saveLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar) {

        try {
            liq.setRegpLiquidacionDetallesCollection(null);
            HistoricoTramites ht = liq.getTramite();
            if (liq.getNumTramiteRp() != null) {
                ht.setNumTramite(liq.getNumTramiteRp());
            }
            if (ht.getNumTramite() == null) {
                ht.setNumTramite(sec.getSecuenciaTramite());
            }
            ht = (HistoricoTramites) manager.merge(ht);
            liq.setNumTramiteRp(ht.getNumTramite());
            liq.setTramite(ht);
            if (liq.getId() == null) {
                liq = (RegpLiquidacion) manager.persist(liq);
            } else {
                liq = (RegpLiquidacion) manager.merge(liq);
            }

            for (RegpLiquidacionDetalles d : actosPorPagar) {
                List<RegpIntervinientes> listInterv = (List<RegpIntervinientes>) d.getRegpIntervinientesCollection();
                for (RegpIntervinientes x : listInterv) {

                    System.out.println(" x.getEnte().getApellidos()---- " + x.getEnte().getApellidos());
                }
                d.setRegpIntervinientesCollection(null);
                d.setLiquidacion(liq);
                if (d.getId() == null) {
                    d = (RegpLiquidacionDetalles) manager.persist(d);
                } else {
                    d = (RegpLiquidacionDetalles) manager.merge(d);
                }
                if (listInterv != null) {
                    for (RegpIntervinientes i : listInterv) {
                        i.setLiquidacion(d);
//                        if (i.getEnte() != null && i.getEnte().getId() == null) {
//                            manager.merge(i.getEnte());
//                        }
                        if (i.getId() == null) {
                            manager.persist(i);
                        } else {
                            manager.merge(i);
                        }
                    }
                }
            }

        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liq;
    }

    /**
     * Guarda la proforma en partes y va actualizando los datos ingresados, en
     * este estado la proforma esta incompleta y no puede ser ingresada
     *
     * @param liq Object
     * @param actosPorPagar Object
     * @return Object
     */
    @Override
    public RegpLiquidacion saveParcialLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar) {
        try {
            HistoricoTramites ht = liq.getTramite();
            if (ht.getNumTramite() == null) {
                //ht.setNumTramite(sec.getSecuenciaGeneral(Variables.secuenciaTramite).longValue());
                ht.setNumTramite(sec.getSecuenciaTramite());
                liq.setNumTramiteRp(ht.getNumTramite());
            }
            ht = (HistoricoTramites) manager.persist(ht);
            liq.setTramite(ht);
            liq = (RegpLiquidacion) manager.persist(liq);
            if (Utils.isNotEmpty(actosPorPagar)) {
                for (RegpLiquidacionDetalles d : actosPorPagar) {
                    List<RegpIntervinientes> listInterv = (List<RegpIntervinientes>) d.getRegpIntervinientesCollection();
                    d.setRegpIntervinientesCollection(null);
                    d.setLiquidacion(liq);
                    d = (RegpLiquidacionDetalles) manager.persist(d);
                    if (listInterv != null) {
                        for (RegpIntervinientes i : listInterv) {
                            i.setLiquidacion(d);
                            manager.persist(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liq;
    }

    /**
     * Actualiza los datos de una proforma, se puede editar y corregir los datos
     * cuando aun no ha sido ingresado el tramite
     *
     * @param liq Object
     * @param actosPorPagar Object
     * @return Object
     */
    @Override
    public RegpLiquidacion editLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar) {
        System.out.println("la liquidacion es  ---- " + liq);
        try {
            for (RegpLiquidacionDetalles d : actosPorPagar) {
                List<RegpIntervinientes> listInterv = (List<RegpIntervinientes>) d.getRegpIntervinientesCollection();
                d.setRegpIntervinientesCollection(null);
                d.setLiquidacion(liq);
                RegpLiquidacionDetalles x = new RegpLiquidacionDetalles();
                x = d;
                x.setLiquidacion(liq);

                if (d.getExoneracion() != null) {
                    if (d.getExoneracion().getId() == null) {
                        x.setExoneracion(null);
                    }
                }
                if (x.getId() == null) {
                    x = (RegpLiquidacionDetalles) manager.persist(x);
                } else {
                    x = (RegpLiquidacionDetalles) manager.merge(x);
                }

                if (listInterv != null) {
                    System.out.println("// listInterv: " + listInterv);
                    for (RegpIntervinientes i : listInterv) {
                        i.setLiquidacion(x);
                        if (i.getId() != null) {
                            manager.merge(i);
                        } else {
                            manager.persist(i);
                        }
                    }
                }
            }
            manager.persist(liq.getTramite());
            manager.merge(liq);
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liq;
    }

    /**
     * Se duplica una proforma cuando aun no ha sido ingresado el tramite, pero
     * ya paso el tiempo limite de validez de una profroma, se crea un nuevo
     * registro con los mismos datos pero con el numero de tramite actualizado
     *
     * @param liq Object
     * @param actosPorPagar Object
     * @return Object
     */
    @Override
    public RegpLiquidacion duplicarLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar) {
        try {
            HistoricoTramites ht = liq.getTramite();
            //ht.setNumTramite(sec.getSecuenciaGeneral(Variables.secuenciaTramite).longValue());
            ht.setNumTramite(sec.getSecuenciaTramite());
            ht.setId(null);
            ht = (HistoricoTramites) manager.persist(ht);

            //List<RegpLiquidacionDetalles> actosPorPagar = (List<RegpLiquidacionDetalles>) liq.getRegpLiquidacionDetallesCollection();
            //liq.setRegpLiquidacionDetallesCollection(null);
            liq.setId(null);
            liq.setNumTramiteRp(ht.getNumTramite());
            liq.setTramite(ht);
            liq = (RegpLiquidacion) manager.persist(liq);

            for (RegpLiquidacionDetalles d : actosPorPagar) {
                List<RegpIntervinientes> listInterv = (List<RegpIntervinientes>) d.getRegpIntervinientesCollection();
                d.setRegpIntervinientesCollection(null);
                d.setFechaIngreso(new Date());
                d.setLiquidacion(liq);
                d.setId(null);
                d = (RegpLiquidacionDetalles) manager.persist(d);
                if (listInterv != null) {
                    for (RegpIntervinientes i : listInterv) {
                        i.setId(null);
                        i.setLiquidacion(d);
                        manager.persist(i);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liq;
    }

    /**
     * Se registra los datos del pago de una proforma, la forma de pago, los
     * valores por contrato, usuario cajero, etc.
     *
     * @param liquidacion Object
     * @param pago Object
     * @param cajero Object
     * @return Object
     */
    @Override
    public RegpLiquidacion cancelarLiquidacion(RegpLiquidacion liquidacion, RenPago pago, RenCajero cajero) {
        List<RenPagoDetalle> detallePago;
        RenPagoRubro rubro;
        try {
            detallePago = (List<RenPagoDetalle>) pago.getRenPagoDetalleCollection();
            pago.setRenPagoDetalleCollection(null);

            /*if (liquidacion.getNumeroComprobante() != null
                    && liquidacion.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                pago.setNumComprobante(liquidacion.getNumeroComprobante());
                liquidacion.setCodigoComprobante(liquidacion.getNumeroComprobante().toString());
            }*/
            if (liquidacion.getTituloCredito() == null || liquidacion.getTituloCredito() == 0L) {
                liquidacion.setTituloCredito(sec.getSecuenciaTitulo(liquidacion.getNumTramiteRp()));
                liquidacion.setCodigoComprobante(liquidacion.getTituloCredito().toString());
                pago.setNumComprobante(BigInteger.valueOf(liquidacion.getTituloCredito()));
            }

            pago.setFechaPago(new Date());
            pago.setEstado(true);
            pago.setLiquidacion(liquidacion);
            pago.setCajero(cajero.getUsuario());
            //pago.setCajero(new AclUser(liquidacion.getUserIngreso()));
            pago.setDescuento(liquidacion.getDescuentoValor());
            pago.setRecargo(BigDecimal.ZERO);
            pago.setInteres(BigDecimal.ZERO);
            pago = (RenPago) manager.persist(pago);
            for (RenPagoDetalle det : detallePago) {
                det.setPago(pago);
                manager.persist(det);
            }
            liquidacion.setEstadoPago(new RegpEstadoPago(2L)); //ESTADO PAGO CANCELADO
            liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L)); // ESTADO LIQUIDACION: INGRESADO
            manager.update(liquidacion);
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                rubro = new RenPagoRubro();
                rubro.setPago(pago);
                rubro.setRubro(det.getActo());
                rubro.setValor(det.getValorTotal());
                manager.persist(rubro);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liquidacion;
    }

    /**
     * Despues de cancelar el valor de la proforma al tramite se le asignan las
     * secuencias de repertorio, numero de inscripcion, usuario que se encarga
     * del tramite y la generacion de la factura electronica
     *
     * @param idLiquidacion Object
     * @param cajero Object
     * @return Object
     */
    @Override
    public RegpLiquidacion asignarUsuarioSecuencias(Long idLiquidacion, RenCajero cajero) {
        Map<String, Object> map;
        RegpLiquidacion liquidacion;
        RegpEstadoLiquidacion estadoLiquidacion;
        String nombre = "certificador";
        Integer peso = 0;
        Integer dias;
        try {
            estadoLiquidacion = manager.find(RegpEstadoLiquidacion.class, 2L);
            liquidacion = manager.find(RegpLiquidacion.class, idLiquidacion);
            liquidacion.setEstadoLiquidacion(estadoLiquidacion);
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                peso = peso + det.getCantidad();
                if (det.getActo().getSolvencia()) {
                    det.setInscripcion(sec.getSecuenciaInscripcion(det.getActo().getLibro().getId()));
                } else {
                    nombre = "inscriptor";
                }
                if (det.getFechaIngreso() == null) {
                    det.setFechaIngreso(new Date());
                }
                manager.update(det);
            }
            if (nombre.equals("certificador")) {
                liquidacion.setCertificado(Boolean.TRUE);
                if (peso > 0) {
                    liquidacion.setPesoTramite(peso);
                } else {
                    liquidacion.setPesoTramite(liquidacion.getRegpLiquidacionDetallesCollection().size());
                }
                liquidacion.setRepertorio(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaRepertorioSolvencia).intValue());
            } else {
                liquidacion.setInscripcion(Boolean.TRUE);
                if (liquidacion.getRepertorio() == null) {
                    SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
                    liquidacion.setRepertorio(sr.getRepertorio());
                    liquidacion.setFechaRepertorio(sr.getFecha());
                }
            }

            if (liquidacion.getVersionDescuento() != null) {
                switch (liquidacion.getVersionDescuento()) {
                    case 2:
                        nombre = "tramites_judiciales";
                        break;
                    case 3:
                        nombre = "tramites_sine";
                        break;
                }
            } else if (liquidacion.getEsJuridico()) {
                nombre = "tramites_judiciales";
            }

            map = new HashMap<>();
            map.put("idLiquidacion", idLiquidacion);
            dias = (Integer) manager.findObjectByParameter(Querys.getMaxDiasByTramite, map);

            HistoricoTramites ht = liquidacion.getTramite();
            ht.setFechaIngreso(liquidacion.getFechaIngreso());
            if (ht.getFechaEntrega() == null) {
                ht.setFechaEntrega(this.diasEntregaTramite(ht.getFechaIngreso(), dias));
            }

            if (liquidacion.getInscriptor() == null) {
                map = new HashMap<>();
                map.put("nombre", nombre);
                AclRol rol = manager.findObjectByParameter(AclRol.class, map);

                liquidacion.setInscriptor(sec.getUserForTramite(rol.getId(), liquidacion.getPesoTramite()));
            }
            manager.update(ht);
            liquidacion.setTramite(ht);
            this.generarIndices(liquidacion);
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liquidacion;
    }

    /**
     * guarda las observaciones enlazadas al tramite
     *
     * @param ht Object
     * @param nameUser Object
     * @param observaciones Object
     * @param taskDefinitionKey Object
     * @return Object
     */
    @Override
    public Observaciones guardarObservaciones(HistoricoTramites ht, String nameUser, String observaciones, String taskDefinitionKey) {
        try {
            Observaciones observ = new Observaciones();
            observ.setEstado(true);
            observ.setFecCre(new Date());
            observ.setTarea(taskDefinitionKey);
            observ.setIdTramite(ht);
            observ.setUserCre(nameUser);
            observ.setObservacion(observaciones);
            return (Observaciones) manager.persist(observ);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Registra en la BD los indices despues de ser ingresados, los indices son
     * los movimientos registrales recien ingresados y sirven para consultas
     *
     * @param liquidacion Object
     */
    @Override
    public void generarIndices(RegpLiquidacion liquidacion) {
        RegpTareasTramite ta;
        RegMovimiento mov;
        RegMovimientoCliente mc;
        Calendar calendario = Calendar.getInstance();
        try {
            String verificacion = rcs.genCodigoVerif();
            ArrayList<RegpLiquidacionDetalles> liquidacionDetalles
                    = new ArrayList(liquidacion.getRegpLiquidacionDetallesCollection());
            for (RegpLiquidacionDetalles det : liquidacionDetalles) {

                for (int i = 0; i < det.getCantidad(); i++) {
                    ta = new RegpTareasTramite();
                    ta.setFecha(det.getFechaIngreso());
                    ta.setDetalle(det);
                    ta.setTramite(liquidacion.getTramite());
                    ta.setEstado(Boolean.TRUE);
                    ta.setRealizado(Boolean.FALSE);
                    ta = (RegpTareasTramite) manager.persist(ta);
                    if (!det.getActo().getSolvencia()) {
                        mov = new RegMovimiento();
                        mov.setActo(det.getActo());
                        mov.setLibro(mov.getActo().getLibro());
                        mov.setNumRepertorio(liquidacion.getRepertorio());
                        mov.setFechaRepertorio(liquidacion.getFechaRepertorio());
                        mov.setEnteJudicial(liquidacion.getEnteJudicial());
                        mov.setFechaOto(liquidacion.getFechaOto());
                        mov.setOrdJud(liquidacion.getOrdJud());
                        mov.setFechaResolucion(liquidacion.getFechaResolucion());
                        //mov.setEscritJuicProvResolucion(liquidacion.getEscritJuicProvResolucion());
                        if (liquidacion.getNumFormulario() != null && !liquidacion.getNumFormulario().isEmpty()) {
                            mov.setEscritJuicProvResolucion(liquidacion.getNumFormulario());
                        }
                        mov.setUserCreador(liquidacion.getInscriptor());
                        mov.setValorUuid(Utils.getValorUuid());
                        mov.setCodVerificacion(verificacion);
                        mov.setTramite(ta);
                        mov.setEstado("IN");
                        mov.setNumTomo(NroTomoEnum.fromUiParam(calendario.get(Calendar.YEAR)).name());
                        mov.setAvaluoMunicipal(det.getAvaluo());
                        mov.setCuantia(det.getCuantia());
                        mov.setBaseImponible(det.getBase());
                        mov.setFechaIngreso(calendario.getTime());
                        mov.setDomicilio(new RegDomicilio(281L)); //CANTON SANTO DOMINGO
                        //manager.persist(mov);
                        mov = (RegMovimiento) manager.persist(mov);
                        for (RegpIntervinientes in : det.getRegpIntervinientesCollection()) {
                            mc = new RegMovimientoCliente();
                            mc.setMovimiento(mov);
                            mc.setPapel(in.getPapel());
                            mc.setEnte(in.getEnte());
                            mc.setDomicilio(new RegDomicilio(281L)); //CANTON SANTO DOMINGO
                            if (in.getEnte().getEstadoCivil() != null) {
                                mc.setEstado(in.getEnte().getEstadoCivil().getValor());
                            }
                            mc.setEnteInterv(this.getIntervinienteByEnte(in.getEnte()));
                            manager.persist(mc);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Consulta y retorna el registro de la tabla reg_ente_interviniente que le
     * corresponde a la tabla cat_ente
     *
     * @param ente Object
     * @return Object
     */
    @Override
    public RegEnteInterviniente getIntervinienteByEnte(CatEnte ente) {
        Map<String, Object> map;
        RegEnteInterviniente en = new RegEnteInterviniente();
        try {
            map = new HashMap<>();
            map.put("cedRuc", ente.getCiRuc());
            map.put("nombre", ente.getNombreCompleto());
            en = manager.findObjectByParameter(RegEnteInterviniente.class, map);
            if (en == null) {
                en = new RegEnteInterviniente();
                en.setCedRuc(ente.getCiRuc());
                en.setNombre(ente.getNombreCompleto());
                if (ente.getEsPersona()) {
                    en.setTipoInterv("N");
                } else {
                    en.setTipoInterv("J");
                }
                en.setProcedencia("R");
                en = (RegEnteInterviniente) manager.persist(en);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return en;
    }

    /**
     * Calcula los dias de entrega de tramite, sin contar fines de seman ni dias
     * de feriado ingresados en la base del sistema
     *
     * @param fecha Object
     * @param dias Object
     * @return Object
     */
    @Override
    public Date diasEntregaTramite(Date fecha, int dias) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        Feriados fe;
        Map map;
        try {
            cal.setTime(fecha);
            int hoy;
            do {
                cal.add(Calendar.DAY_OF_YEAR, 1);
                hoy = cal.get(Calendar.DAY_OF_WEEK);
                if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                    map = new HashMap<>();
                    map.put("fecha", sdf.parse(sdf.format(cal.getTime())));
                    fe = (Feriados) manager.findObjectByParameter(Feriados.class, map);
                    if (fe == null) {
                        dias--;
                    }
                }
            } while (dias > 0);
            return cal.getTime();
        } catch (ParseException e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return Utils.sumarDiasFechaSinWeekEnd(fecha, dias);
        }
    }

    /**
     * Retorna el nombre de usuario de la tabla acl_user consultado por el pk de
     * dicha tabla
     *
     * @param id Object
     * @return Object
     */
    @Override
    public String getNameUserByAclUserId(Long id) {
        try {
            AclUser user = manager.find(AclUser.class,
                    id);
            if (user != null) {
                return user.getUsuario();
            } else {
                return "";
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return "";
        }
    }

    /**
     * Retorna el nombre de usuario de la tabla acl_user consultado por el
     * nombre del rol que tiene asociado el usuario
     *
     * @param name Object
     * @return Object
     */
    @Override
    public String getUsuarioByRolName(String name) {
        List<AclUser> list;
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", name);
        map.put("estado", Boolean.TRUE);
        try {
            //AclRol rol = manager.find(AclRol.class, id);
            AclRol rol = manager.findObjectByParameter(AclRol.class,
                    map);
            if (rol != null) {
                list = (List<AclUser>) rol.getAclUserCollection();
                return list.get(0).getUsuario();
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    /**
     * Genera un archivo en disco en formato XML que representa la factura
     * electronica, antes de ser firmada y autorizada
     *
     * @param idLiquidacion Object
     * @return Object
     */
    @Override
    public Boolean generarXml(Long idLiquidacion) {
        Map<String, Object> map;
        RegpLiquidacion liquidacion;
        try {
            liquidacion = manager.find(RegpLiquidacion.class,
                    idLiquidacion);
            if (liquidacion.getClaveAcceso() != null) {
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", new AclUser(liquidacion.getUserIngreso()));
                RenCajero cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class,
                        map);
                map = new HashMap();
                map.put("code", Constantes.datosFacturaElectronica);
                RenDatosFacturaElectronica dfe = manager.findObjectByParameter(RenDatosFacturaElectronica.class,
                        map);
                FacturaXml.generarArchivoXml(liquidacion, cajero, dfe);
            } else {
                return false;
            }
        } catch (TransformerException e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * Devuelve un HashMap con todos las diferentes formas de pago de una misma
     * factura
     *
     * @param liq Object
     * @return Object
     */
    @Override
    public HashMap<String, Object> getFormasPagoFe(RegpLiquidacion liq) {
        HashMap<String, Object> map = new HashMap<>();
        Map<String, Object> pars;
        //List<RenPagoDetalle> list;
        try {
            //SIN UTILIZACION DEL SISTEMA FINANCIERO: 01
            //COMPENSACIÓN DE DEUDAS: 15
            //TARJETA DE DÉBITO: 16
            //DINERO ELECTRÓNICO: 17
            //TARJETA PREPAGO: 18
            //TARJETA DE CRÉDITO: 19
            //OTROS CON UTILIZACION DEL SISTEMA FINANCIERO: 20
            //ENDOSO DE TÍTULOS: 21

            Long estado = liq.getEstadoPago().getId();
            if (estado == 3L || estado == 4L) {
                map.put("15", liq.getTotalPagar());
                return map;
            } else if (estado == 7L) {
                map.put("20", liq.getTotalPagar());
                return map;
            } else {
                List<RenPagoDetalle> pagoDetalles;
                for (RenPago pa : liq.getRenPagoCollection()) {
                    pars = new HashMap<>();
                    pars.put("pago", pa);
                    pagoDetalles = (List<RenPagoDetalle>) manager.findAll(Querys.getPagoDetalles,
                            new String[]{"idPago"}, new Object[]{pa.getId()});

                    if (pagoDetalles != null) {
                        for (RenPagoDetalle de : pagoDetalles) {
                            switch (Long.valueOf(de.getTipoPago()).intValue()) {
                                case 1:
                                    map.put("01", de.getValor());
                                    break;
                                case 2:
                                    map.put("19", de.getValor());
                                    break;
                                case 3:
                                    map.put("15", de.getValor());
                                    break;
                                case 4:
                                    map.put("20", de.getValor());
                                    break;
                                case 5:
                                    map.put("20", de.getValor());
                                    break;
                                case 6:
                                    map.put("01", de.getValor());
                                    break;
                                case 7:
                                    map.put("16", de.getValor());
                                    break;
                                default:
                                    map.put("01", de.getValor());
                                    break;
                            }
                        }
                    } else {
                        map.put("01", liq.getTotalPagar());
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            map = new HashMap<>();
            System.out.println("holi try");
            map.put("01", liq.getTotalPagar());
            return map;
        }
        return map;
    }

    @Override
    public HashMap<String, Object> getFormasPagoFe(RenPago pago) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (pago != null && pago.getRenPagoDetalleCollection() != null) {
                for (RenPagoDetalle de : pago.getRenPagoDetalleCollection()) {
                    switch (Long.valueOf(de.getTipoPago()).intValue()) {
                        case 1:
                            map.put("01", de.getValor());
                            break;
                        case 2:
                            map.put("19", de.getValor());
                            break;
                        case 3:
                            map.put("15", de.getValor());
                            break;
                        case 4:
                            map.put("20", de.getValor());
                            break;
                        case 5:
                            map.put("20", de.getValor());
                            break;
                        case 6:
                            map.put("01", de.getValor());
                            break;
                        case 7:
                            map.put("16", de.getValor());
                            break;
                        default:
                            map.put("01", de.getValor());
                            break;
                    }
                }
            } else {
                map.put("01", pago.getValor());
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            map = new HashMap<>();
            map.put("01", pago.getValor());
            return map;
        }
        return map;
    }

    /**
     * Retorna una lista de liquidaciones que representan las facturas
     * electronicas que no se enviaron por correo
     *
     * @param fecha Object
     * @param usuario Object
     * @return Object
     */
    @Override
    public List<RegpLiquidacion> cargarFacturasNoEnviadas(String fecha, Long usuario) {
        Map<String, Object> map;
        List<RegpLiquidacion> liquidaciones;
        RenCajero cajero;
        try {
            map = new HashMap();
            map.put("fecha", fecha);
            map.put("idUsuario", usuario);
            liquidaciones = manager.findNamedQuery(Querys.getFacturasNoEnviadas, map);
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(usuario));
            cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class,
                    map);
            for (RegpLiquidacion re : liquidaciones) {
                map = LeerFacturaXml.leerFacturaXmlAutorizada(cajero, re.getClaveAcceso());
                String nu = (String) map.get("numeroAutorizacion");
                Date fe = (Date) map.get("fechaAutorizacion");
                if (nu != null && fe != null) {
                    re.setNumeroAutorizacion(nu);
                    re.setFechaAutorizacion(fe);
                    manager.update(re);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liquidaciones;
    }

    /**
     * Lee el archivo XML que devuelve el SRI con el numero y fecha de
     * autoriacion, para actualizar esos datos en el registro de la liquidacion
     *
     * @param re Object
     * @param cajero Object
     * @return Object
     */
    @Override
    public RegpLiquidacion cargarAutorizacionFactura(RegpLiquidacion re, RenCajero cajero) {
        Map<String, Object> map;
        try {
            map = LeerFacturaXml.leerFacturaXmlAutorizada(cajero, re.getClaveAcceso());
            String nu = (String) map.get("numeroAutorizacion");
            Date fe = (Date) map.get("fechaAutorizacion");
            if (nu != null && fe != null) {
                re.setNumeroAutorizacion(nu);
                re.setFechaAutorizacion(fe);
                manager.update(re);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return re;
    }

    /**
     * Genera el archivo PDF que se envia al usuario por correo electronico como
     * RIDE, lo guarda en una ruta específica para despues leerlo
     *
     * @param liquidaciones Object
     * @param cajero Object
     * @param reporte Object
     * @param map Object
     * @return Object
     */
    @Override
    public Boolean generarRIDE(List<RegpLiquidacion> liquidaciones, RenCajero cajero, String reporte, Map map) {
        Connection conn = null;
        JasperPrint jp;
        try {
            //Session sess = HiberUtil.getSession();
            Session sess = manager.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();

            for (RegpLiquidacion re : liquidaciones) {
                if (re.getNumeroAutorizacion() != null && re.getFechaAutorizacion() != null) {
                    map.put("LIQUIDACION", re.getId());
                    map.put("FORMA_PAGO", this.getEstadoPagoLiquidacion(re));
                    jp = JasperFillManager.fillReport(reporte, map, conn);
                    JasperExportManager.exportReportToPdfFile(jp, cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".pdf");
                }
            }

        } catch (SQLException | JRException e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(IngresoTramiteEjb.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

    /**
     * Retorna el codigo del tipo de pago principal en la recaudacion
     *
     * @param re Object
     * @return Object
     */
    @Override
    public Integer getEstadoPagoLiquidacion(RegpLiquidacion re) {
        try {
            if (re.getEstadoPago().getId() == 3L) {
                return 0;
            } else {
                for (RenPago pa : re.getRenPagoCollection()) {
                    for (RenPagoDetalle de : pa.getRenPagoDetalleCollection()) {
                        Long tipo = de.getTipoPago();
                        return tipo.intValue();
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return -1;
        }
        return -1;
    }

    /**
     * Envía de manera individual el correo electronico de la facturacion
     * electronica para los usuarios, junto con los archivos adjuntos de los
     * XMLs y los RIDEs
     *
     * @param re Object
     * @param cajero Object
     * @return Object
     */
    @Override
    public Boolean envioCorreoFacturaElectronica(RegpLiquidacion re, RenCajero cajero) {
        MsgFormatoNotificacion fn;
        List<File> files = new ArrayList<>();
        try {
            if (re.getSolicitante().getCorreo1() == null) {
                return false;
            }
            fn = (MsgFormatoNotificacion) manager.find(Querys.getMsgNotificacion, new String[]{"tipo"}, new Object[]{Constantes.correoEnvioFactura});
            if (fn == null) {
                return false;
            }
            File xml = new File(cajero.getRutaComprobantesAutorizados() + re.getClaveAcceso() + ".xml");
            File ride = new File(cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".pdf");
            if (xml.exists() && ride.exists()) {
                String contenido = fn.getHeader() + fn.getFooter();
                files.add(xml);
                files.add(ride);
                Email correo = new Email(re.getSolicitante().getCorreo1(), fn.getAsunto(), contenido, files);
                correo.sendMail();
                File nuevo = new File(cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".xml");
                xml.renameTo(nuevo);
                re.setRideEnviado(Boolean.TRUE);
                manager.update(re);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * Retorna los nombres de los usuarios que estan relacionados a un rol, los
     * nombres de usuarios retornan en una sola cadena separados por comas
     *
     * @param nombre Object
     * @return Object
     */
    @Override
    public String getCandidateUserByRolName(String nombre) {
        String cadena = "";
        List<AclUser> list;
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("estado", Boolean.TRUE);
        try {
            //AclRol rol = manager.find(AclRol.class, id);
            AclRol rol = manager.findObjectByParameter(AclRol.class,
                    map);
            list = (List<AclUser>) rol.getAclUserCollection();
            for (AclUser user : list) {
                cadena = cadena + "," + user.getUsuario();
            }
            cadena = cadena.substring(1);
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return cadena;
    }

    /**
     * Devuelve la lista de AclUser asociados a un rol que se consulta por
     * nombre de rol
     *
     * @param nombre Object
     * @return Object
     */
    @Override
    public List<AclUser> getUsuariosByRolName(String nombre) {
        List<AclUser> list;
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("estado", Boolean.TRUE);
        try {
            //AclRol rol = manager.find(AclRol.class, id);
            AclRol rol = manager.findObjectByParameter(AclRol.class,
                    map);
            list = (List<AclUser>) rol.getAclUserCollection();
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    /**
     * Genera una nueva factura con nuevas secuencias para un tramite que se
     * anulo la factura anterior
     *
     * @param li Object
     * @return Object
     */
    @Override
    public Boolean nuevaFacturaTramiteExistente(RegpLiquidacion li) {
        Map<String, Object> map;
        try {
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(li.getUserIngreso()));
            RenCajero cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class,
                    map);
            if (cajero != null) {
                RenFacturaAnulada fa = new RenFacturaAnulada();
                fa.setClaveAcceso(li.getClaveAcceso());
                fa.setCodigoComprobante(li.getCodigoComprobante());
                fa.setFecha(new Date());
                fa.setFechaAnulacion(li.getFechaAnulacion());
                fa.setFechaAutorizacion(li.getFechaAutorizacion());
                fa.setFechaEmision(li.getFechaIngreso());
                fa.setNumTramiteRp(li.getNumTramiteRp());
                fa.setNumeroAutorizacion(li.getNumeroAutorizacion());
                fa.setNumeroComprobante(li.getNumeroComprobante());
                fa.setSolicitante(li.getSolicitante());
                fa.setUserAnulacion(li.getUserAnula());
                fa.setSubTotal(li.getSubTotal());
                fa.setTotalPagar(li.getTotalPagar());
                fa.setObservacion(li.getInfAdicional());
                fa.setLiquidacion(li);
                manager.persist(fa);

                //li.setNumeroComprobante(sec.getSecuenciaGeneral(cajero.getVariableSecuencia()));
                li.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L));
                String temp = Utils.completarCadenaConCeros(li.getNumeroComprobante().toString(), 9);
                li.setCodigoComprobante(cajero.getCodigoCaja() + temp);
                li.setFechaIngreso(new Date());
                li.setFechaAutorizacion(null);
                li.setNumeroAutorizacion(null);

                map = new HashMap<>();
                map.put("code", Constantes.datosFacturaElectronica);
                RenDatosFacturaElectronica dfe = manager.findObjectByParameter(RenDatosFacturaElectronica.class,
                        map);
                this.generarClaveAcceso(li, cajero, dfe);
                if (li.getClaveAcceso() != null) {
                    FacturaXml.generarArchivoXml(li, cajero, dfe);
                }
            }
        } catch (TransformerException e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * Retorna la lista de documentos enlazados a cada tarea dentro de un mismo
     * tramite
     *
     * @param tramite Object
     * @return Object
     */
    @Override
    public List<RegpDocsTarea> getDocumentosTareas(Long tramite) {
        List<RegpDocsTarea> docs;
        try {
            docs = manager.findAll(Querys.getDocsTareasByTramite, new String[]{"idTramite"}, new Object[]{tramite});
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return docs;
    }

    /**
     * Retorna la lista de documentos enlazados a un tramite que se este
     * consultando
     *
     * @param tramite Object
     * @return Object
     */
    @Override
    public List<RegpDocsTramite> getDocumentosTramite(Long tramite) {
        List<RegpDocsTramite> docs;
        try {
            docs = manager.findAll(Querys.getDocsTramiteByTramite, new String[]{"idTramite"}, new Object[]{tramite});
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return docs;
    }

    /**
     * Metodo para realizar pruebas con el desarrollo de comprobantes
     * electronicos para notas de credito
     *
     * @param liquidacion Object
     * @param cajero Object
     * @return Object
     */
    @Override
    public Boolean pruebasNotaCredito(RegpLiquidacion liquidacion, RenCajero cajero) {
        Map<String, Object> map;
        try {
            map = new HashMap();
            map.put("code", Constantes.datosFacturaElectronica);
            RenDatosFacturaElectronica dfe = manager.findObjectByParameter(RenDatosFacturaElectronica.class,
                    map);
            String xml = FacturaXml.generarXmlNCWs(liquidacion, dfe.getRuc(), null);
            System.out.println("// TEXTO XML");
            System.out.println(xml);
            //FEWSDL_Impl fe = new FEWSDL_Impl();
            //String response = fe.getFEWSDLPort().serviceGenerarXML(xml, "notaCredito", new BigInteger(SisVars.codigoAmbiente), new BigInteger(cajero.getCodigoCaja()));
            System.out.println("//");
            System.out.println("// TEXTO RESPUESTA WS");
            //System.out.println(response);
        } catch (IOException | TransformerException e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return true;
    }

    /**
     * Metodo para realizar pruebas con el desarrollo de comprobantes
     * electronicos para emision de facturas
     *
     * @param idLiquidacion Object
     * @return Object
     */
    @Override
    public Boolean pruebasWsFacturacion(Long idLiquidacion) {
        Map<String, Object> map;
        RegpLiquidacion liquidacion;
        try {
            liquidacion = manager.find(RegpLiquidacion.class,
                    idLiquidacion);
            /*map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(liquidacion.getUserIngreso()));
            RenCajero cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);*/
            map = new HashMap();
            map.put("code", Constantes.datosFacturaElectronica);
            RenDatosFacturaElectronica dfe = manager.findObjectByParameter(RenDatosFacturaElectronica.class,
                    map);
            String xml = FacturaXml.generarXmlWs(liquidacion, dfe.getRuc(), this.getFormasPagoFe(liquidacion));
            System.out.println("// TEXTO XML");
            System.out.println(xml);
            /*FEWSDL_Impl fe = new FEWSDL_Impl();
            String response = fe.getFEWSDLPort().serviceGenerarXML(xml, "factura", new BigInteger(SisVars.codigoAmbiente), new BigInteger(cajero.getCodigoCaja()));
            System.out.println("//");
            System.out.println("// TEXTO RESPUESTA WS");
            System.out.println(response);*/
        } catch (IOException | TransformerException e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * envia una peticion al webservice de facturacion para la emision de una
     * nueva factura sin el ingreso de un tramite nuevo
     *
     * @param tramite Object
     * @param solicitante Object
     * @param formapago Object
     * @param caja Object
     * @return Object
     */
    @Override
    public Boolean emitirFacturaSinTramite(Long tramite, CatEnte solicitante, String formapago, RenCajero caja) {
        Map<String, Object> map = new HashMap();
        String documento, autorizacion, acceso;
        RenFactura factura;
        map.put("numTramiteRp", tramite);
        RegpLiquidacion liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class,
                map);
        if (liquidacion != null) {
            factura = new RenFactura();
            factura.setFecha(new Date());
            factura.setLiquidacion(liquidacion);
            factura.setTotalPagar(liquidacion.getTotalPagar());
            factura.setNumTramite(tramite);
            factura.setSolicitante(solicitante);
            factura.setCaja(caja);
            factura.setFormaPago(formapago);
            factura = (RenFactura) manager.persist(factura);
            fac.emitirFacturaElectronicaSinTramite(factura, caja);
            manager.update(factura);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Metodo de conexion con WebService para la emision de comprobantes
     * electronicos de tipo facturas
     *
     * @param liq Object
     * @param cajero Object
     * @return Object
     */
    @Override
    public Boolean emisionFacturaElectronica(Long liq, RenCajero cajero) {
        RegpLiquidacion liquidacion;
        try {
            liquidacion = manager.find(RegpLiquidacion.class,
                    liq);
            return fac.emitirFacturaElectronica(liquidacion, cajero);

        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Generacion comprobantes electronicos de tipo notas de credito
     *
     * @param liq Object
     * @param cajero Object
     * @param motivo Object
     * @param valor Object
     * @return Object
     */
    @Override
    public Boolean emisionNotaCreditoFe(Long liq, RenCajero cajero, String motivo, BigDecimal valor) {
        Map<String, Object> map;
        RegpLiquidacion liquidacion;
        BigInteger numComprobante;
        RenNotaCredito notaCredito = new RenNotaCredito();
        try {
//            numComprobante = sec.getSecuenciaGeneral(cajero.getVariableSecuenciaNotaCredito());
            numComprobante = sec.getSecuenciaComprobante(cajero.getCajero().getVariableSecuenciaNotaCredito(), SisVars.ambienteFacturacion);
            notaCredito.setMotivo(motivo);
            notaCredito.setValorTotal(valor);
            notaCredito.setValorPendiente(valor);
            liquidacion = manager.find(RegpLiquidacion.class, liq);
            notaCredito.setFechaEmision(new Date());
            notaCredito.setFactura(liquidacion);
            notaCredito.setClaveAccesoModifica(liquidacion.getClaveAcceso());
            notaCredito.setNumeroAutorizacionModifica(liquidacion.getNumeroAutorizacion());
            notaCredito.setNumeroComprobanteModifica(liquidacion.getCodigoComprobante());
            notaCredito.setCajaEmision(cajero);
            String temp = Utils.completarCadenaConCeros(numComprobante.toString(), 9);
            notaCredito.setNumeroDocumento("001-" + cajero.getCodigoCaja() + "-" + temp);
            notaCredito.setCodigoDocumento(numComprobante.longValue());
            notaCredito = (RenNotaCredito) manager.persist(notaCredito);
            fac.emitirNotaCredito(notaCredito);
            return true;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * devuelve el objeto de la nota de credito enlazada al tramite
     *
     * @param tramite Object
     * @return Object
     */
    @Override
    public Boolean findNotaCreditoByTramite(Long tramite) {
        try {
            Object nc = manager.find(Querys.getNotaCreditoByTramite, new String[]{"tramite"}, new Object[]{tramite});
            return nc != null;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * suma el valor total de las notas de credito que estan enlazadas al mismo
     * tramite
     *
     * @param tramite Object
     * @return Object
     */
    @Override
    public BigDecimal getSumaValorNotaCredito(Long tramite) {
        try {
            BigDecimal suma = (BigDecimal) manager.find(Querys.getSumNCbyTramite, new String[]{"tramite"}, new Object[]{tramite});
            if (suma == null) {
                return BigDecimal.ZERO;
            } else {
                return suma;
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * se realiza el envio de una nota de credito en caso de que el sri no la
     * devuelva autorizada
     *
     * @param nc Object
     * @return Object
     */
    @Override
    public Boolean reenvioNotaCredito(RenNotaCredito nc) {
        Map<String, Object> map;
        String documento, autorizacion, acceso;
        try {
            //CONSULTA DE DATOS DEL RUC DE LA EMPRESA
            map = new HashMap();
            map.put("code", Constantes.datosFacturaElectronica);
            RenDatosFacturaElectronica dfe = manager.findObjectByParameter(RenDatosFacturaElectronica.class,
                    map);
            //LLENADO DE TEXTO EN FORMATO XML PARA ENVIO COMO PARAMETRO DE XML
            String xml = FacturaXml.generarXmlNCWs(nc.getFactura(), dfe.getRuc(), nc);
            //System.out.println("//TEXTO XML");
            //System.out.println(xml);
            //INSTANCIA DE OBJETO QUE LLAMA AL WEBSERVICE
            //FEWSDL_Impl fe = new FEWSDL_Impl();
            //AQUI SE OBTIENE LA RESPUETA DEL WEBSERVICE
            String response = "";
            //String response = fe.getFEWSDLPort().serviceGenerarXML(xml, "notaCredito", new BigInteger(SisVars.codigoAmbiente), new BigInteger(nc.getCajaEmision().getCodigoCaja()));
            //System.out.println("//RESPUESTA WS");
            //System.out.println(response);
            //SE OBTIENEN LOS DATOS DE LA RESPUESTA PARA ACTUALIZAR LOS DATOS DE LA LIQUIDACION
            String estado = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "estado");
            String mensaje = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "mensaje");
            nc.setEstado(estado);
            nc.setMensaje(mensaje);
            switch (estado) {
                case "AUTORIZADO":
                    acceso = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "clave_acceso");
                    documento = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "nro_documento");
                    autorizacion = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "numeroAutorizacion");
                    nc.setClaveAcceso(acceso);
                    nc.setNumeroDocumento(documento);
                    nc.setNumeroAutorizacion(autorizacion);
                    break;
                case "PENDIENTE":
                    acceso = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "clave_acceso");
                    documento = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "nro_documento");
                    nc.setClaveAcceso(acceso);
                    nc.setNumeroDocumento(documento);
                case "DEVUELTA":
                    acceso = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "clave_acceso");
                    documento = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "nro_documento");
                    nc.setClaveAcceso(acceso);
                    nc.setNumeroDocumento(documento);
                    break;
                case "":
                    acceso = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "clave_acceso");
                    documento = FacturaXml.getValoresFromXmlWS("<respuesta>" + response + "</respuesta>", "nro_documento");
                    nc.setClaveAcceso(acceso);
                    nc.setNumeroDocumento(documento);
                    break;
            }
            manager.persist(nc);
        } catch (IOException | TransformerException e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * Consulta los tramites donde el id del solicitante sea el consultado y por
     * rangos de fecha
     *
     * @param id Object
     * @param desde Object
     * @param hasta Object
     * @return devuelve un listado de liquidaciones correspondientes a las
     * factura generadas
     */
    @Override
    public List<RegpLiquidacion> getComprobantesBySolicitante(Long id, Date desde, Date hasta) {
        List<RegpLiquidacion> list;
        try {
            list = (List<RegpLiquidacion>) manager.findAll(Querys.getComprobantesBySolicitante, new String[]{"codigosolicitante", "desde", "hasta"}, new Object[]{id, desde, hasta});
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public File mergeFilesPdf(List<InputStream> list) {
        try {
            //crear el documento receptor de los otros archivos
            File file = new File(Constantes.rutaFeOld + new Date().getTime() + ".pdf");
            /*OutputStream out = new FileOutputStream(file);
            //crear un nuevo documento PDF
            Document document = new Document();
            //crear un escritor del PDF
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            //para cada PDF en la lista
            //leer su contenido por página e ir agregando
            //cada página en el PDF de la variable document
            for (InputStream in : list) {
                PdfReader reader = new PdfReader(in);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    document.newPage();
                    //import the page from source pdf
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    //add the page to the destination pdf
                    cb.addTemplate(page, 0, 0);
                }
            }
            //cerrar streams para liberar recursos
            //y cualquier bloqueo de archivo
            out.flush();
            document.close();
            out.close();*/
            return file;
            //} catch (DocumentException | IOException e) {
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * consulta las exoneraciones aplicadas sobre la liquidacion
     *
     * @param liquidacion Object
     * @return Object
     */
    @Override
    public List<RegpLiquidacionExoneracion> getExoneraciones(RegpLiquidacion liquidacion) {
        Map<String, Object> map;
        List<CatEnte> entes;
        RegpLiquidacionExoneracion exo;
        List<RegpLiquidacionExoneracion> list = new ArrayList<>();
        try {
            manager.updateNativeQuery(Querys.inactivarExoneraciones, new Object[]{liquidacion.getId()});
            entes = (List<CatEnte>) manager.findAll(Querys.beneficiariosLiquidacion, new String[]{"proforma"}, new Object[]{liquidacion});
            for (CatEnte en : entes) {
                map = new HashMap();
                map.put("ente", en);
                map.put("liquidacion", liquidacion);
                exo = manager.findObjectByParameter(RegpLiquidacionExoneracion.class,
                        map);
                if (exo == null) {
                    exo = new RegpLiquidacionExoneracion();
                    exo.setEnte(en);
                    exo.setLiquidacion(liquidacion);
                    exo = (RegpLiquidacionExoneracion) manager.persist(exo);
                } else {
                    exo.setEstado(Boolean.TRUE);
                }
                list.add(exo);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * guarda los detalles de las exoneraciones aplicadas sobre la liquidacion
     *
     * @param liquidacion Object
     * @param exoneraciones Object
     * @return Object
     */
    @Override
    public Boolean saveExoneracion(RegpLiquidacion liquidacion, List<RegpLiquidacionExoneracion> exoneraciones) {
        Map<String, Object> map;
        RegpLiquidacionExoneracion temp;
        try {
            manager.updateNativeQuery(Querys.inactivarExoneraciones, new Object[]{liquidacion.getId()});
            for (RegpLiquidacionExoneracion exo : exoneraciones) {
                map = new HashMap();
                map.put("ente", exo.getEnte());
                map.put("liquidacion", liquidacion);
                temp = manager.findObjectByParameter(RegpLiquidacionExoneracion.class,
                        map);
                if (temp == null) {
                    exo.setLiquidacion(liquidacion);
                    manager.persist(exo);
                } else {
                    temp.setEstado(Boolean.TRUE);
                    temp.setBeneficiario(exo.getBeneficiario());
                    temp.setExoneracion(exo.getExoneracion());
                    manager.persist(temp);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * retorna el detalle de la liquidacion que contiene los actos ingresados
     *
     * @param liquidacion Object
     * @return Object
     */
    @Override
    public List<RegpLiquidacionDetalles> getActosPorPagar(RegpLiquidacion liquidacion) {
        List<RegpLiquidacionDetalles> detalles;
        Map<String, Object> map;
        try {
            map = new HashMap();
            map.put("liquidacion", liquidacion);
            detalles = manager.findObjectByParameterList(RegpLiquidacionDetalles.class,
                    map);
            detalles.forEach((de) -> {
                de.getRegpIntervinientesCollection().size();
            });
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return detalles;
    }

    @Override
    public PubSolicitudJuridico guardarSolicitudJudicial(PubSolicitudJuridico pubSolicitudJuridico) {
        PubSolicitudJuridico psj = null;
        try {
            psj = (PubSolicitudJuridico) manager.persist(pubSolicitudJuridico);
            HistoricoTramites historicoTramites = pubSolicitudJuridico.getHistoricoTramites();
            historicoTramites.setFechaIngreso(psj.getFechaIngreso());
            //historicoTramites.setNumTramite(sec.getSecuenciaGeneral(Variables.secuenciaTramite).longValue());
            historicoTramites.setNumTramite(sec.getSecuenciaTramite());
            historicoTramites = (HistoricoTramites) manager.persist(historicoTramites);
            psj.setHistoricoTramites(historicoTramites);
            return psj;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    @Override
    public RenFactura emitirFacturaSinTramiteSinGuardarRenLiquidacionSoloRenPago(RegpLiquidacion liquidacion, RenPago pago, RenCajero cajero, String observacion) {
        BigInteger numComprobante;
        List<RenPagoDetalle> detallePago;
        RenPagoRubro rubro;
        RegpLiquidacion liquidacionOriginal = null, liquidacionTareas = null;
        if (!liquidacion.getLiquidacionSinTramite()) {
            Map map = new HashMap();
            map.put("numTramiteRp", liquidacion.getNumTramiteRp());
            liquidacionOriginal = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            liquidacionTareas = liquidacionOriginal;
        }

        RenFactura factura;
        List<RegpLiquidacionDetalles> actosPorPagar = new ArrayList();
        List<RegpLiquidacionDetalles> actosRenFactura = new ArrayList();
        try {
            numComprobante = sec.getSecuenciaComprobante(cajero.getCajero().getVariableSecuenciaFacturas(), SisVars.ambienteFacturacion);
            //numComprobante = sec.getSecuenciaGeneral(cajero.getVariableSecuencia());
            detallePago = (List<RenPagoDetalle>) pago.getRenPagoDetalleCollection();
            pago.setRenPagoDetalleCollection(null);
            pago.setFechaPago(new Date());
            pago.setEstado(true);
            pago.setNumComprobante(numComprobante);
            if (!liquidacion.getLiquidacionSinTramite()) {
                pago.setLiquidacion(liquidacion);
            }
            pago.setCajero(cajero.getUsuario());
            pago.setDescuento(liquidacion.getDescuentoValor());
            pago.setRecargo(BigDecimal.ZERO);
            pago.setInteres(BigDecimal.ZERO);
            pago = (RenPago) manager.persist(pago);
            for (RenPagoDetalle det : detallePago) {
                det.setPago(pago);
                manager.persist(det);
            }
            BigDecimal valorTotal = BigDecimal.ZERO;
            BigDecimal valorUni = BigDecimal.ZERO;
            if (liquidacionTareas != null) {
                liquidacionTareas.getRegpLiquidacionDetallesCollection().clear();
            }
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {

                rubro = new RenPagoRubro();
                rubro.setPago(pago);
                rubro.setRubro(det.getActo());
                rubro.setValor(det.getValorTotal());
                manager.persist(rubro);

                valorTotal = det.getValorTotal();
                valorUni = det.getValorUnitario();

                det.setValorTotal(BigDecimal.ZERO);
                det.setValorUnitario(BigDecimal.ZERO);
                det.setReingreso(Boolean.TRUE);
                if (liquidacionOriginal != null && !liquidacion.getLiquidacionSinTramite()) {
                    det.setLiquidacion(liquidacionOriginal);
                    det = (RegpLiquidacionDetalles) manager.persist(det);
                }
                det.setValorTotal(valorTotal);
                det.setValorUnitario(valorUni);
                if (!det.getActo().getNombre().equalsIgnoreCase("ALCANCE DE PAGOS")) {
                    actosPorPagar.add(det);
                }
                actosRenFactura.add(det);

            }

            factura = new RenFactura();
            factura.setFecha(new Date());
            if (!liquidacion.getLiquidacionSinTramite()) {
                factura.setLiquidacion(liquidacion);
                factura.setFacturaSinTramite(Boolean.FALSE);
            } else {
                factura.setFacturaSinTramite(Boolean.TRUE);
            }

            factura.setTotalPagar(liquidacion.getTotalPagar());
            factura.setSubTotal(liquidacion.getSubTotal());
            factura.setNumTramite(liquidacion.getNumTramiteRp());
            factura.setSolicitante(liquidacion.getSolicitante());
            factura.setCaja(cajero);
            factura.setFormaPago("");
            factura.setPago(pago);
            factura.setObservacion(observacion);
            factura.setNumeroComprobante(numComprobante);
            /*String temp = Utils.completarCadenaConCeros(factura.getNumeroComprobante().toString(), 9);
            factura.setCodigoComprobante("001-" + cajero.getCodigoCaja() + "-" + temp);*/
            factura.setCodigoComprobante(cajero.getCajero().getEntidad().getEstablecimiento() + "-"
                    + cajero.getCajero().getPuntoEmision() + "-"
                    + String.format("%09d", numComprobante));
            factura.setFechaEmision(new Date());
            factura = (RenFactura) manager.persist(factura);
            factura.setLiquidacionDetalles(actosRenFactura);
            fac.emitirFacturaElectronicaSinTramite(factura, cajero);

            if (liquidacionTareas != null && !liquidacion.getLiquidacionSinTramite()) {
                liquidacionTareas.setRegpLiquidacionDetallesCollection(actosPorPagar);
                this.generarIndices(liquidacionTareas);
            }

            return factura;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Valida que actos deben pasar a generar tareas para inscripcion
     *
     * @param liquidacion
     * @return
     */
    @Override
    public ArrayList<RegpLiquidacionDetalles> validarTareasActos(RegpLiquidacion liquidacion) {
        try {
            Boolean ph = false;
            ArrayList<RegpLiquidacionDetalles> regpLiquidacionDetalles = new ArrayList();
            for (RegpLiquidacionDetalles rld : liquidacion.getRegpLiquidacionDetallesCollection()) {
                if (rld.getActo().getTipoActo() != null) {
                    if (rld.getActo().getTipoActo().getId() == 44L) {
                        ph = true;
                        regpLiquidacionDetalles.add(rld);
                        break;
                    }
                }
            }
            if (ph) {
                return regpLiquidacionDetalles;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList(liquidacion.getRegpLiquidacionDetallesCollection());
    }

    @Override
    public RegpLiquidacion asignarUsuarioSecuenciasSinTramite(RegpLiquidacion liquidacion) {
        Map<String, Object> map;
        RegpEstadoLiquidacion estadoLiquidacion;
        String nombre = "certificador";
        Integer dias, suma = 0;
        try {
            estadoLiquidacion = manager.find(RegpEstadoLiquidacion.class, 2L);
            liquidacion.setEstadoLiquidacion(estadoLiquidacion);
            List<RegpLiquidacionDetalles> liquidacionDetalles = (List<RegpLiquidacionDetalles>) manager.findAll(Querys.getDetallesByLiquidacion, new String[]{"parametro"}, new Object[]{liquidacion.getId()});
            for (RegpLiquidacionDetalles det : liquidacionDetalles) {
                if (det.getActo().getSolvencia()) {
                    det.setInscripcion(sec.getSecuenciaInscripcion(det.getActo().getLibro().getId()));
                } else {
                    nombre = "inscriptor";
                }
                if (det.getReingreso() && det.getFechaIngreso() != null) {
                    det.setFechaIngreso(det.getFechaIngreso());
                } else {
                    det.setFechaIngreso(new Date());
                }
                manager.update(det);
                if (det.getRegpIntervinientesCollection() != null) {
                    det.getRegpIntervinientesCollection().clear();
                } else {
                    det.setRegpIntervinientesCollection(new ArrayList<>());
                }

                List<RegpIntervinientes> interv = (List<RegpIntervinientes>) manager.findAll(Querys.getRegpIntervinientes, new String[]{"idRegpIntervinientes"}, new Object[]{det.getId()});
                det.setRegpIntervinientesCollection(interv);
            }
            liquidacion.setRegpLiquidacionDetallesCollection(new ArrayList<>());
            liquidacion.setRegpLiquidacionDetallesCollection(liquidacionDetalles);
            map = new HashMap<>();
            map.put("nombre", nombre);
            AclRol rol = manager.findObjectByParameter(AclRol.class, map);
            if (nombre.equals("certificador")) {
                liquidacion.setCertificado(Boolean.TRUE);
                liquidacion.setRepertorio(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaRepertorioSolvencia).intValue());
            } else {
                liquidacion.setInscripcion(Boolean.TRUE);
                if (liquidacion.getRepertorio() == null) {
                    if (liquidacion.getEsRegistroPropiedad()) {
                        SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
                        liquidacion.setRepertorio(sr.getRepertorio());
                        liquidacion.setFechaRepertorio(sr.getFecha());
                    }
                    /*else {
                        SecuenciaRepertorioMercantil srm = sec.getSecuenciaRepertorioMercantil();
                        liquidacion.setRepertorio(srm.getRepertorio());
                        liquidacion.setFechaRepertorio(srm.getFecha());
                    }*/
                }
            }
            if (liquidacion.getIngresado()) {
                map = new HashMap();
                map.put("code", Constantes.diasTramiteJudicial);
                Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                dias = valor.getValorNumeric().intValue();
            } else if (liquidacion.getReingreso()) {
                map = new HashMap();
                map.put("code", Constantes.diasTramiteReingreso);
                Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                dias = valor.getValorNumeric().intValue();
            } else if (liquidacion.getCertificadoSinFlujo()) {
                map = new HashMap();
                map.put("code", Constantes.diasCertificadoNoPoseerBienes);
                Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                dias = valor.getValorNumeric().intValue();
            } /*else if (liquidacion.getFicha() != null && nombre.equals("certificador")) {
                map = new HashMap();
                map.put("code", Variables.diasTramitesFicha);
                Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                dias = valor.getValorNumeric().intValue();
                liquidacion.setInscriptor(reg.getUsuariosByRolName("certificador_ficha").get(0));
            }*/ else {
                map = new HashMap<>();
                map.put("idLiquidacion", liquidacion.getId());
                dias = (Integer) manager.findObjectByParameter(Querys.getMaxDiasByTramite, map);
            }
            HistoricoTramites ht = liquidacion.getTramite();
            ht.setFechaIngreso(liquidacion.getFechaIngreso());
            if (ht.getFechaEntrega() == null) {
                dias = dias + suma;
                ht.setFechaEntrega(this.diasEntregaTramite(ht.getFechaIngreso(), dias));
            }

            /*SI ALGUIEN LEE ESTO Y VA A CAMBIAR EL TEMA DE LA FECHA DE ENTREGA
            PORFI ver la clase: VentanillaPubEjb ->> asignarUsuarioFechaEntrega(RegpLiquidacion liq, HistoricoTramites ht)*/
            if (liquidacion.getInscriptor() == null) {
                if (liquidacion.getEsJuridico()) {
                    liquidacion.setInscriptor(reg.getUsuariosByRolName("secretaria_juridico").get(0));
                } else if (liquidacion.getTramiteCorporativo()) {
                    liquidacion.setInscriptor(reg.getUsuariosByRolName("inscriptor_corporativo").get(0));
                } else {
                    TarUsuarioTareas tut;
                    map = new HashMap();
                    map.put("rol", rol.getId());
                    tut = sec.getUserForTask(rol.getId(), 1, ht.getFechaEntrega());
                    dias = dias + tut.getDias();
                    //System.out.println(ht.getFechaEntrega() + " Dias add fecha entrega " + dias);
                    ht.setFechaEntrega(this.diasEntregaTramite(ht.getFechaIngreso(), dias));
                    liquidacion.setInscriptor(tut.getUsuario());
                }
            }
            manager.update(ht);
            liquidacion.setTramite(ht);
            if (!liquidacion.getEsJuridico()) {
                this.generarIndices(liquidacion);
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liquidacion;
    }

    @Override
    public RegpLiquidacion asignarUsuarioSecuenciasCertifiacadoSinFlujo(Long idLiquidacion, RenCajero cajero) {
        Map<String, Object> map;
        RegpLiquidacion liquidacion;
        RegpEstadoLiquidacion estadoLiquidacion;
        String nombre = "certificador";
        Integer dias;
        try {
            estadoLiquidacion = manager.find(RegpEstadoLiquidacion.class, 2L);
            liquidacion = manager.find(RegpLiquidacion.class, idLiquidacion);
            liquidacion.setEstadoLiquidacion(estadoLiquidacion);
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                if (det.getActo().getSolvencia()) {
                    det.setInscripcion(sec.getSecuenciaInscripcion(det.getActo().getLibro().getId()));
                } else {
                    nombre = "inscriptor";
                }
                if (det.getReingreso() && det.getFechaIngreso() != null) {
                    det.setFechaIngreso(det.getFechaIngreso());
                } else {
                    det.setFechaIngreso(new Date());
                }
                manager.update(det);
            }

            if (nombre.equals("certificador")) {
                liquidacion.setCertificado(Boolean.TRUE);
                liquidacion.setRepertorio(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaRepertorioSolvencia).intValue());
            }
            map = new HashMap();
            map.put("code", Constantes.diasCertificadoNoPoseerBienes);
            Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
            dias = valor.getValorNumeric().intValue();

            HistoricoTramites ht = liquidacion.getTramite();
            ht.setFechaIngreso(liquidacion.getFechaIngreso());
            if (ht.getFechaEntrega() == null) {
                ht.setFechaEntrega(this.diasEntregaTramite(ht.getFechaIngreso(), dias));
            }

            manager.update(ht);
            liquidacion.setTramite(ht);
            this.generarIndices(liquidacion);
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return liquidacion;
    }

    @Override
    public StringBuffer validarInscripcion(RegpLiquidacion liquid) {
        try {
            StringBuffer buffer = null;
            List<RegpLiquidacion> liquidacionesExistente = null;
            Map<String, Object> map = new HashMap();
            if (liquid.getId() != null) {
                map.put("id", new WhereClause(liquid.getId(), "noEquals"));
            }
            if (liquid.getNumInscripcion() != null && liquid.getAnioInscripcion() != null
                    && liquid.getNumInscripcion() > 0 && liquid.getAnioInscripcion() > 0) {
                map.put("numInscripcion", liquid.getNumInscripcion());
                map.put("anioInscripcion", liquid.getAnioInscripcion());
            } else if (liquid.getNumFicha() != null && liquid.getNumFicha() > 0) {
                map.put("numFicha", liquid.getNumFicha());
            } else {
                return null;
            }
            liquidacionesExistente = manager.findObjectByParameterList(RegpLiquidacion.class, map);
            //System.out.println("Map " + map);
            if (Utils.isNotEmpty(liquidacionesExistente)) {
                buffer = new StringBuffer();
                if (liquidacionesExistente.size() > 1) {
                    buffer.append("Existen Tramites ingresados: ");
                } else {
                    buffer.append("Existe el Tramite: ");
                }
                for (RegpLiquidacion liquidacion : liquidacionesExistente) {
                    //System.out.println("Liquidacion verificando: " + liquidacion);
                    StringBuilder sqlActiveTask = new StringBuilder("SELECT id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
                    sqlActiveTask.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
                    sqlActiveTask.append("blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate ");
                    sqlActiveTask.append("FROM flow.tareas_activas WHERE num_tramite = :numTramite");
                    //String sqlActiveTask = "SELECT * FROM flow.tareas_activas WHERE num_tramite = :numTramite";
                    List<TareaWF> acticeTasks = manager.getSqlQueryParametros(TareaWF.class, sqlActiveTask.toString(),
                            new String[]{"numTramite"}, new Object[]{liquidacion.getNumTramiteRp()});
                    if (acticeTasks != null) {
                        buffer.append("<br/>#: ");
                        buffer.append(liquidacion.getNumTramiteRp())
                                .append(" con fecha: ");
                        if (liquidacion.getFechaIngreso() == null) {
                            buffer.append(Utils.dateFormatPattern("dd/MM/yyyy HH:mm", liquidacion.getFechaCreacion()));
                        } else {
                            buffer.append(Utils.dateFormatPattern("dd/MM/yyyy HH:mm", liquidacion.getFechaIngreso()));
                        }
                        /*if (liquidacion.getFicha() != null) {
                            buffer.append(" con ficha # ").append(liquidacion.getFicha().getNumFicha());
                        }*/
                        if (liquidacion.getNumInscripcion() != null && liquidacion.getAnioInscripcion() != null) {
                            buffer.append(" y inscripcion # ").append(liquidacion.getNumInscripcion())
                                    .append(" del año ").append(liquidacion.getAnioInscripcion());
                        }
                    }
                }
                return buffer;
            }
            return null;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public StringBuffer validarInscripcion(RegFicha ficha) {
        try {
            if (ficha == null) {
                return null;
            }
            StringBuffer buffer = null;
            List<RegpLiquidacion> liquidacionesExistente = null;
            Map<String, Object> map = new HashMap();
            map.put("numFicha", ficha.getNumFicha());
            liquidacionesExistente = manager.findObjectByParameterList(RegpLiquidacion.class, map);
            //System.out.println("Map " + map);
            if (Utils.isNotEmpty(liquidacionesExistente)) {
                buffer = new StringBuffer();
                if (liquidacionesExistente.size() > 1) {
                    buffer.append("Existen Tramites ingresados: ");
                } else {
                    buffer.append("Existe el Tramite: ");
                }
                for (RegpLiquidacion liquidacion : liquidacionesExistente) {
                    //System.out.println("Liquidacion verificando: " + liquidacion);
                    StringBuilder sqlActiveTask = new StringBuilder("SELECT id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
                    sqlActiveTask.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
                    sqlActiveTask.append("blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate ");
                    sqlActiveTask.append("FROM flow.tareas_activas WHERE num_tramite = :numTramite");
                    //String sqlActiveTask = "SELECT * FROM flow.tareas_activas WHERE num_tramite = :numTramite";
                    List<TareaWF> acticeTasks = manager.getSqlQueryParametros(TareaWF.class, sqlActiveTask.toString(),
                            new String[]{"numTramite"}, new Object[]{liquidacion.getNumTramiteRp()});
                    if (acticeTasks != null) {
                        buffer.append("<br/>#: ");
                        buffer.append(liquidacion.getNumTramiteRp())
                                .append(" con fecha: ");
                        if (liquidacion.getFechaIngreso() == null) {
                            buffer.append(Utils.dateFormatPattern("dd/MM/yyyy HH:mm", liquidacion.getFechaCreacion()));
                        } else {
                            buffer.append(Utils.dateFormatPattern("dd/MM/yyyy HH:mm", liquidacion.getFechaIngreso()));
                        }
                    }
                }
                return buffer;
            }
            return null;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public RegpLiquidacion generarRepertorioIndiceJuridico(RegpLiquidacion liquidacion) {
        if (liquidacion.getEsJuridico() && liquidacion.getRepertorio() == null) {
            if (liquidacion.getEsRegistroPropiedad()) {
                SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
                liquidacion.setRepertorio(sr.getRepertorio());
                liquidacion.setFechaRepertorio(sr.getFecha());
            }
            /*else {
                SecuenciaRepertorioMercantil srm = sec.getSecuenciaRepertorioMercantil();
                liquidacion.setRepertorio(srm.getRepertorio());
                liquidacion.setFechaRepertorio(srm.getFecha());
            }*/
            liquidacion = (RegpLiquidacion) manager.persist(liquidacion);

            List<RegpLiquidacionDetalles> liquidacionDetalles = restServices.getRegpLiquidacionDetalles(liquidacion.getId());

            if (Utils.isNotEmpty(liquidacion.getRegpLiquidacionDetallesCollection())) {
                liquidacion.getRegpLiquidacionDetallesCollection().clear();
            } else {
                liquidacion.setRegpLiquidacionDetallesCollection(new ArrayList());
            }

            for (RegpLiquidacionDetalles det : liquidacionDetalles) {
                if (det.getRegpIntervinientesCollection() != null) {
                    det.getRegpIntervinientesCollection().clear();
                } else {
                    det.setRegpIntervinientesCollection(new ArrayList<>());
                }

                List<RegpIntervinientes> interv = (List<RegpIntervinientes>) manager.findAll(Querys.getRegpIntervinientes, new String[]{"idRegpIntervinientes"}, new Object[]{det.getId()});
                det.setRegpIntervinientesCollection(interv);
            }

            liquidacion.setRegpLiquidacionDetallesCollection(liquidacionDetalles);
            generarIndices(liquidacion);
        }
        return liquidacion;
    }

    @Override
    public StringBuffer buscarActosIngresadosCedula(String identificacion) {
        try {
            StringBuffer buffer = null;
            List<Object[]> list = manager.getSqlQueryValues(null, Querys.enteTieneTramite(false), new Object[]{identificacion});
            if (list != null) {
                buffer = new StringBuffer();
                if (list.size() > 1) {
                    buffer.append("Existen Tramites asociados al numero de cedula ").append(identificacion).append(":");
                } else {
                    buffer.append("Existe el Tramite asociados al numero de cedula ").append(identificacion).append(":");
                }
                for (Object[] data : list) {
                    buffer.append("<br/>");
                    buffer.append("Tramite # ").append(data[1])
                            .append(" Contrato: ").append(data[0]);
                    buffer.append(" con fecha de ingreso: ").append(data[2]);
                    // System.out.println("Data " + Arrays.toString(data));
                }
                return buffer;
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    public StringBuffer buscarActosIngresadosCedula(String identificacion, RegActo acto) {
        try {
            StringBuffer buffer = null;
            List<Object[]> list = null;
            if (acto.getLibro() != null) {
                //list = manager.getSqlQueryValues(null, Querys.enteTieneTramite(true), new Object[]{identificacion, (acto.getLibro().getTipo() == 1 ? 2 : 1)});
                list = manager.getSqlQueryValues(null, Querys.enteTieneTramite(true), new Object[]{identificacion});
            } else {
                list = manager.getSqlQueryValues(null, Querys.enteTieneTramite(true), new Object[]{identificacion});
            }
            if (list != null) {
                buffer = new StringBuffer();
                if (list.size() > 1) {
                    buffer.append("Existen Tramites asociados al numero de cedula ").append(identificacion).append(":");
                } else {
                    buffer.append("Existe el Tramite asociados al numero de cedula ").append(identificacion).append(":");
                }
                for (Object[] data : list) {
                    buffer.append("<br/>");
                    buffer.append("Tramite # ").append(data[1])
                            .append(" Contrato: ").append(data[0]);
                    buffer.append(" con fecha de ingreso: ").append(data[2]);
                    // System.out.println("Data " + Arrays.toString(data));
                }
                return buffer;
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    public PubSolicitud asignarUsuarioInscripcionEnlinea(PubSolicitud solicitud, HistoricoTramites ht) {

        try {
            Map<String, Object> map;

            String nombre = "ventanilla_solicitud_inscripcion";
            Integer dias;
            map = new HashMap<>();
            map.put("nombre", nombre);
            //PARA OBTENER NOMBRE DEL ROL EN ACLROL
            AclRol rol = manager.findObjectByParameter(AclRol.class, map);

            map = new HashMap();
            map.put("code", Constantes.diasSolicitudInscripcionEnlinea);
            Valores valor = (Valores) manager.findObjectByParameter(Valores.class, map);
            dias = valor.getValorNumeric().intValue();

            TarUsuarioTareas tut;
            map = new HashMap();
            map.put("rol", rol.getId());
            tut = sec.getUserForTask(rol.getId(), 1, ht.getFechaEntrega());
            dias = dias + tut.getDias();
            ht.setFechaEntrega(this.diasEntregaTramite(ht.getFechaIngreso(), dias));
            solicitud.setRevisor(tut.getUsuario());//USUARIO ASIGNADO
            manager.update(ht);
            manager.update(solicitud);
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return solicitud;
    }

    @Override
    public Boolean registrarLiquidacionSAM(Long idliquidacion) {
        try {
            RegpLiquidacion liq = manager.find(RegpLiquidacion.class, idliquidacion);
            if (liq != null) {
                List<RegpLiquidacionDetalles> detalles = manager.findAll(Querys.getDetallesByLiquidacion,
                        new String[]{"parametro"}, new Object[]{liq.getId()});
                if (!detalles.isEmpty()) {
                    //return sam.registrarLiquidacion(liq, detalles);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public SamLiquidacionDetalles verificarPagoSAM(Long tramite) {
        try {
            //return sam.verificarPago(tramite);
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Boolean ingresarLiquidacionAutomatico(RegpLiquidacion liq, SamLiquidacionDetalles det) {
        try {
            PagoModel modelPago = new PagoModel();
            modelPago.setValorLimite(liq.getTotalPagar());
            modelPago.setValorRecibido(liq.getTotalPagar());
            modelPago.setValorCobrar(liq.getTotalPagar());
            modelPago.setValorTotalEfectivo(liq.getTotalPagar());
            modelPago.calcularTotalPago();
            liq.setFechaIngreso(det.getFechaPago());
            liq.setPagoFinal(liq.getTotalPagar());
            liq.setNumeroComprobante(BigInteger.valueOf(det.getNumEmision()));
            liq.setUserIngreso(liq.getUserCreacion());
            liq.setEstado(liq.getUserCreacion().intValue());
            liq = this.cancelarLiquidacion(liq, modelPago.realizarPago(liq), null);
            if (liq != null) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public Boolean asignarUsuarioSecuencias(RegpLiquidacion liquidacion) {
        Map<String, Object> map;
        String nombre = "certificador";
        Integer dias;
        try {
            for (RegpLiquidacionDetalles det : liquidacion.getRegpLiquidacionDetallesCollection()) {
                if (det.getActo().getSolvencia()) {
                    det.setInscripcion(sec.getSecuenciaInscripcion(det.getActo().getLibro().getId()));
                } else {
                    nombre = "inscriptor";
                }
                if (det.getFechaIngreso() == null) {
                    det.setFechaIngreso(new Date());
                }
                manager.update(det);
            }
            if (nombre.equals("certificador")) {
                liquidacion.setCertificado(Boolean.TRUE);
                liquidacion.setPesoTramite(liquidacion.getRegpLiquidacionDetallesCollection().size());
                liquidacion.setRepertorio(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaRepertorioSolvencia).intValue());
            } else {
                liquidacion.setInscripcion(Boolean.TRUE);
                if (liquidacion.getRepertorio() == null) {
                    SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
                    liquidacion.setRepertorio(sr.getRepertorio());
                    liquidacion.setFechaRepertorio(sr.getFecha());
                }
            }

            if (liquidacion.getVersionDescuento() != null) {
                switch (liquidacion.getVersionDescuento()) {
                    case 2:
                        nombre = "tramites_judiciales";
                        break;
                    case 3:
                        nombre = "tramites_sine";
                        break;
                }
            } else if (liquidacion.getEsJuridico()) {
                nombre = "tramites_judiciales";
            }

            HistoricoTramites ht = liquidacion.getTramite();
            ht.setFechaIngreso(liquidacion.getFechaIngreso());
            if (ht.getFechaEntrega() == null) {
                map = new HashMap<>();
                map.put("idLiquidacion", liquidacion.getId());
                dias = (Integer) manager.findObjectByParameter(Querys.getMaxDiasByTramite, map);

                ht.setFechaEntrega(this.diasEntregaTramite(ht.getFechaIngreso(), dias));
            }
            manager.update(ht);
            if (liquidacion.getInscriptor() == null) {
                map = new HashMap<>();
                map.put("nombre", nombre);
                AclRol rol = manager.findObjectByParameter(AclRol.class, map);
                liquidacion.setInscriptor(sec.getUserForTramite(rol.getId(), 1));
            }
            liquidacion.setTramite(ht);
            manager.update(liquidacion);
            this.generarIndices(liquidacion);
            return true;
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

}
