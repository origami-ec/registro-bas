/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.ebilling.interfaces.FacturacionElectronicaLocal;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.GeTipoTramite;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenTipoEntidadBancaria;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.models.PagoModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.services.interfaces.VentanillaPubLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class IngresoExonerados extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(IngresoExonerados.class.getName());

    @Inject
    private FacturacionElectronicaLocal facturacion;

    @Inject
    private UserSession us;

    @Inject
    protected VentanillaPubLocal vp;

    @Inject
    protected AsynchronousService serv;
    
    @Inject
    private SeqGenMan sec;

    protected LazyModel<RegpLiquidacion> liquidaciones;
    protected RegpLiquidacion liquidacion;
    protected ContenidoReportes proforma;
    protected ContenidoReportes comprobante;
    protected RegRegistrador registrador;
    protected PagoModel modelPago;
    protected HashMap<String, Object> pars;
    protected Integer tipoIngreso = 0;
    protected GeTipoTramite tipoTramite;
    protected RenCajero cajero;
    protected Long referencia;
    protected List<RenEntidadBancaria> bancos;
    protected List<RenEntidadBancaria> tarjetas;
    protected Valores valor;
    protected CtlgCatalogo catalogo;
    protected CtlgItem estudioJuridico;
    protected CtlgItem estudioJuridicoNuevo;
    protected CatEnte solicitanteInterviniente;
    protected String infoAdicional;
    protected AclUser asignado;

    protected HistoricoTramites historico = new HistoricoTramites();

    @PostConstruct
    protected void iniView() {
        try {

            liquidaciones = new LazyModel(RegpLiquidacion.class, "numTramiteRp", "DESC");
            liquidaciones.addFilter("totalPagar", BigDecimal.ZERO);

            liquidacion = new RegpLiquidacion();
            modelPago = new PagoModel();
            map = new HashMap();
            map.put("code", Constantes.piePaginaProforma);
            proforma = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            map = new HashMap();
            map.put("code", Constantes.piePaginaComprobante);
            comprobante = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("tipo", new RenTipoEntidadBancaria(1L));
            bancos = manager.findObjectByParameterList(RenEntidadBancaria.class, map);
            map.put("tipo", new RenTipoEntidadBancaria(2L));
            tarjetas = manager.findObjectByParameterList(RenEntidadBancaria.class, map);
            map = new HashMap();
            map.put("code", Constantes.diasValidezProforma);
            valor = (Valores) manager.findObjectByParameter(Valores.class, map);

            solicitanteInterviniente = new CatEnte();
            map = new HashMap();
            map.put("nombre", Constantes.estudiosJuridicos);
            catalogo = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showProforma(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showTituloCredito(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("titulo_credito");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
            ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showTicket(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            ss.setNombreReporte("ticket");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID", re.getId());
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/header.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showFormularioUafe(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("formularioUafe");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showComprobanteIngreso(RegpLiquidacion re) {
        try {
            if (re.getEstadoLiquidacion().getId() == 2L) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setImprimir(Boolean.TRUE);
                if (re.getGeneraFactura()) {
                    ss.setNombreReporte("comprobante_factura");
                } else {
                    ss.setNombreReporte("comprobante_factura_exonerada");
                }
                //ss.setNombreReporte("comprobante_factura");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", re.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "La proforma no ha sido ingresada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgTipoIngreso(RegpLiquidacion re) {
        try {
            tipoIngreso = 0;
            if (cajero != null) {
                if (this.comprararFechas(re.getFechaCreacion())) {
                    // if (sec.cobroDisponible(re.getId(), us.getUserId())) {
                    liquidacion = re;
                    // LIQUIDACION CON ESTADO ACEPTADO Y VALOR DE PAGO 0.00
                    if (re.getEstadoLiquidacion().getId() == 1L && (re.getTotalPagar().compareTo(BigDecimal.ZERO) == 0)) {
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                    } else if (re.getEstadoLiquidacion().getId() == 1L && re.getEstadoPago().getId() == 1L) { // PENDIENTE DE PAGO
                        tipoIngreso = 1;
                        procesarLiquidacion();
                        /*
                                SE COMENTA ESTO XK ACA NO EXISTEN LAS COMPENSACIONES NI LAS CUENTAS POR COBRAR
                                LOS TRAMITES DE VALOR 0 PASAN DIRECTAMENTE
                         */
                        //JsfUti.update("formIngreso");
                        //JsfUti.executeJS("PF('dlgIngresoTramite').show();");

                    } else if (re.getEstadoPago().getId() == 3L) { // CTA POR COBRAR
                        JsfUti.messageWarning(null, "Proforma ya esta Ingresada como Cta. por Cobrar.", "");
                        /*liquidacion.setPagoFinal(liquidacion.getTotalPagar());
                         modelPago = new PagoModel(liquidacion.getTotalPagar());
                         JsfUti.update("formProcesar");
                         JsfUti.executeJS("PF('dlgProcesar').show();");*/
                    } else if (re.getEstadoPago().getId() == 2L) { // CANCELADO
                        JsfUti.messageWarning(null, "Proforma ya esta Cancelada e Ingresada.", "");
                    } else {
                        JsfUti.messageWarning(null, "Estado de Pago ANULADO o INCOMPLETO.", "");
                    }
//                    } else {
//                        JsfUti.messageError(null, "La liquidación fue ingresada por otro cajero.", "");
//                    }
                } else {
                    JsfUti.messageWarning(null, "La proforma paso fecha limite de validez.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Usuario no tiene Cajero asignado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarTramite(RegpLiquidacion re) {
        try {
            tipoIngreso = 0;
            if (this.comprararFechas(re.getFechaCreacion())) {
                liquidacion = re;
                // LIQUIDACION CON ESTADO ACEPTADO Y VALOR DE PAGO 0.00
                if (re.getEstadoLiquidacion().getId() == 1L && (re.getTotalPagar().compareTo(BigDecimal.ZERO) == 0)) {
                    JsfUti.update("formIngresoExo");
                    JsfUti.executeJS("PF('dlgIngresoExo').show();");
                } else {
                    JsfUti.messageWarning(null, "La proforma NO esta en estado aceptado o no tiene valor cero.", "");
                }
            } else {
                JsfUti.messageWarning(null, "La proforma paso fecha limite de validez.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean comprararFechas(Date in) {
        try {
            Date fecha = Utils.sumarDiasFechaSinWeekEnd(in, valor.getValorNumeric().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date hoy = sdf.parse(sdf.format(new Date()));
            fecha = sdf.parse(sdf.format(fecha));
            return !fecha.before(hoy);
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void showDlgEditProforma(RegpLiquidacion re) {
        try {
            if (re.getEstadoLiquidacion().getId() == 1L || re.getEstadoLiquidacion().getId() == 4L) { // ESTADO LIQUIDACION INGRESADA
                if (Objects.equals(re.getUserCreacion(), us.getUserId())) {
                    ss.instanciarParametros();
                    ss.agregarParametro("proforma", re.getId());
                    JsfUti.redirectFaces("/procesos/registro/editarProforma.xhtml");
                } else {
                    JsfUti.messageWarning(null, "Proforma solo se puede editar por el usuario que la creo.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Proforma no se puede Editar. Ya esta ingresada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void procesarLiquidacion() {
        try {
            if (null != tipoIngreso) {
                switch (tipoIngreso) {
                    case 0:
                        JsfUti.messageWarning(null, "Seleccionar el tipo de Ingreso.", "");
                        break;
                    case 1:
                        if (liquidacion.getEsJuridico()) {
                            System.out.println("liquidacion.getFechaCreacion() : " + liquidacion.getFechaCreacion());
                            if (liquidacion.getRepertorio() != null) {
                                if (validarRepertorio(liquidacion.getFechaCreacion())) {
                                    return;
                                }
                            }
                        }
                        liquidacion.setPagoFinal(liquidacion.getTotalPagar());
                        modelPago = new PagoModel();
                        modelPago.setValorLimite(liquidacion.getTotalPagar());
                        modelPago.setValorRecibido(liquidacion.getTotalPagar());
                        modelPago.setValorCobrar(liquidacion.getTotalPagar());
                        modelPago.setValorTotalEfectivo(liquidacion.getTotalPagar());
                        modelPago.calcularTotalPago();
                        JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                        JsfUti.update("formProcesar");
                        JsfUti.executeJS("PF('dlgProcesar').show();");
                        break;
                    case 2:
                        JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                        break;
                    case 3:
                        JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelarLiquidacion() {
        try {
            if (modelPago.getValorTotal().compareTo(liquidacion.getTotalPagar()) == 0) {
                if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                    liquidacion.setFechaIngreso(new Date());
                    liquidacion.setUserIngreso(us.getUserId());
                    if (solicitanteInterviniente.getId() != null) {
                        liquidacion.setTramitador(solicitanteInterviniente);
                    }
                    liquidacion.setEstudioJuridico(estudioJuridico);
                    liquidacion.setInfAdicionalProf(infoAdicional);
                    liquidacion.setEstado(us.getUserId().intValue());
                    liquidacion = itl.cancelarLiquidacion(liquidacion, modelPago.realizarPago(liquidacion), cajero);
                    if (liquidacion != null) {
                        liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                        if (liquidacion.getTramiteOnline()) {
                            this.initTramiteOnline();
                        } else {
                            this.initTramite();
                        }
                    }
                } else {
                    JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                }
            } else {
                JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor ni menor al de la proforma.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void initTramite() {
        this.iniciarTramiteActiviti();
        this.generarComprobante();
    }

    private void initTramiteOnline() {
        vp.iniciarTramiteActivitiOnline(liquidacion);
        this.generarComprobante();
    }

    public void initTramiteFactura(RegpLiquidacion liq) {
        liquidacion = liq;
        this.iniciarTramiteActiviti();
        JsfUti.messageInfo(null, "Tramite", "Iniciado correctamente");

    }

    public void updateCorreoBeneceificiario() {
        try {
            //System.out.println("Email: " + liquidacion.getBeneficiario().getCorreo1());
            manager.update(liquidacion.getBeneficiario());
            JsfUti.messageInfo(null, "Correo", "Actualizado correctamente.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectInterv(SelectEvent event) {
        try {
            CatEnte in = (CatEnte) event.getObject();
            liquidacion.setBeneficiario(in);
            liquidacion.setSolicitante(in);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generarComprobante() {
        try {
            if (liquidacion.getId() != null) {
                ss.instanciarParametros();

                ss.setGeneraFile(true);
                ss.setRutaDocumento(SisVars.rutaTitulos + liquidacion.getNumTramiteRp() + ".pdf");

                ss.setTieneDatasource(true);
                ss.setNombreReporte("titulo_credito");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
                ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");

                JsfUti.executeJS("PF('dlgProcesar').hide();");
                JsfUti.executeJS("PF('dlgIngresoExo').hide();");
                JsfUti.update("formUserAsignado");
                JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");

                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarTramiteSinValor() {
        try {
            if (liquidacion.getId() != null) {
                liquidacion.setFechaIngreso(new Date());
                liquidacion.setUserIngreso(us.getUserId());
                liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L)); //ESTADO LIQUIDACION: INGRESADA
                liquidacion.setEstadoPago(new RegpEstadoPago(2L)); //ESTADO PAGO: CANCELADA
                if (liquidacion.getTituloCredito() == null || liquidacion.getTituloCredito() == 0L) {
                    liquidacion.setTituloCredito(sec.getSecuenciaTitulo(liquidacion.getNumTramiteRp()));
                    liquidacion.setCodigoComprobante(liquidacion.getTituloCredito().toString());
                }
                manager.update(liquidacion);
                liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                if (liquidacion != null) {
                    this.iniciarTramiteActiviti();
                    JsfUti.executeJS("PF('dlgIngresoExo').hide();");
                    JsfUti.update("formUserAsignado");
                    JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void asignarTramitePruebas(RegpLiquidacion temp) {
        try {
            liquidacion = temp;
            if (liquidacion.getId() != null) {
                liquidacion.setFechaIngreso(new Date());
                liquidacion.setUserIngreso(us.getUserId());
                liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L)); //ESTADO LIQUIDACION: INGRESADA
                liquidacion.setEstadoPago(new RegpEstadoPago(2L)); //ESTADO PAGO: CANCELADA
                manager.update(liquidacion);
                liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                if (liquidacion != null) {
                    this.iniciarTramiteActiviti();
                    JsfUti.update("formUserAsignado");
                    JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean tramiteReferencia() {
        try {
            map = new HashMap();
            map.put("numTramiteRp", referencia);
            map.put("estadoLiquidacion", new RegpEstadoLiquidacion(2L));
            RegpLiquidacion temp = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            if (temp != null) {
                /*if (!liquidacion.getReingreso()) { //SI LA LIQUIDACION ACTUAL ES REINGRESO NO COPIA LA MISMA FECHA DE ENTREGA DE LA REFERENCIA
                    HistoricoTramites ht = liquidacion.getTramite();
                    ht.setFechaEntrega(temp.getTramite().getFechaEntrega());
                    manager.update(ht);
                }*/
                if (temp.getTramite().getFechaEntrega().after(new Date())) { //SI LA FECHA DE ENTREGA YA PASO, NO SE ASIGNA PARA QUE SE CALCULE UNA NUEVA
                    HistoricoTramites ht = liquidacion.getTramite();
                    ht.setFechaEntrega(temp.getTramite().getFechaEntrega());
                    manager.update(ht);
                }
                liquidacion.setTramiteReferencia(referencia);
                liquidacion.setInscriptor(temp.getInscriptor());
                manager.update(liquidacion);
                return true;
            } else {
                JsfUti.messageError(null, "No se encontró tramite ingresado con el número: " + referencia + ", revisar el número o el estado INGRESADO del trámite.", "");
                return false;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void iniciarTramiteActiviti() {
        Boolean result = reg.iniciarTramiteActiviti(liquidacion);
        if (!result) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void iniciarTramiteActivitiOnline() {
        Boolean result = vp.iniciarTramiteActivitiOnline(liquidacion);
        if (!result) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
    }

    public void generarXML(RegpLiquidacion re) {
        try {
            if (itl.generarXml(re.getId())) {
                JsfUti.messageInfo(null, "XML generado con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "No se puedo generar XMl.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarXMLWsTest(RegpLiquidacion re) {
        try {
            if (itl.pruebasWsFacturacion(re.getId())) {
                JsfUti.messageInfo(null, "XML generado con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "No se puedo generar XMl.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadFactura(RegpLiquidacion re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() == null) {
                JsfUti.messageWarning(null, "La factura electronica aun no esta autorizada.", "");
            } else if (re.getEstadoWs() == null) {
                ruta = Constantes.rutaFeOld + "/" + re.getClaveAcceso() + ".pdf";
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab("/sgr/DownLoadFiles");
            } else if (re.getEstadoWs().equalsIgnoreCase("AUTORIZADO")) {
                ruta = Constantes.rutaFeOld + "/factura_" + re.getCodigoComprobante() + ".pdf";
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab("/sgr/DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo. Revisar documento autorizado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadFacturaXml(RegpLiquidacion re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() == null) {
                JsfUti.messageWarning(null, "La factura electronica aun no esta autorizada.", "");
            } else if (re.getEstadoWs() == null) {
                ruta = Constantes.rutaFeOld + "/" + re.getClaveAcceso() + ".xml";
                ss.instanciarParametros();
                ss.setContentType("application/xml");
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab("/sgr/DownLoadFiles");
            } else if (re.getEstadoWs().equalsIgnoreCase("AUTORIZADO")) {
                ruta = Constantes.rutaFeOld + "/factura_" + re.getCodigoComprobante() + ".xml";
                ss.instanciarParametros();
                ss.setContentType("application/xml");
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab("/sgr/DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo. Revisar documento autorizado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRIDE(RegpLiquidacion re) {
        try {
            if (re.getFechaAutorizacion() != null && re.getNumeroAutorizacion() != null) {
                this.showRIDE(re);
            } else {
                re = itl.cargarAutorizacionFactura(re, cajero);
                if (re.getFechaAutorizacion() != null && re.getNumeroAutorizacion() != null) {
                    this.showRIDE(re);
                } else {
                    JsfUti.messageWarning(null, "No se encontro el archivo autorizado.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showRIDE(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("ride");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("LIQUIDACION", re.getId());
            ss.agregarParametro("FORMA_PAGO", itl.getEstadoPagoLiquidacion(re));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/ingreso/");
            ss.agregarParametro("LOGO_URL", JsfUti.getRealPath("/resources/icons/logorp.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgCorrDoc(RegpLiquidacion re) {
        if (re.getEstadoLiquidacion().getId() == 2L) { //TRAMITE INGRESADO
            historico = re.getTramite();
            JsfUti.update("formDocs");
            JsfUti.executeJS("PF('dlgDocs').show();");
        } else {
            JsfUti.messageWarning(null, "El tramite debe estar ingresado para corregir documento.", "");
        }
    }

    public List<CtlgItem> getEstudiosJuridicos() {
        return manager.findAllEntCopy(Querys.getCtlgItemListEstudiosJuridicoa);
    }

    public void showDlgEstudioJuridico() {
        estudioJuridicoNuevo = new CtlgItem();
        JsfUti.update("formEstudioJuridico");
        JsfUti.executeJS("PF('estudioJuridico').show();");
    }

    public void showDlgEditEstudioJuridico() {
        if (estudioJuridico != null) {
            estudioJuridicoNuevo = estudioJuridico;
            //JsfUti.update("formProcesar:tabDetalle:tabInfoPro");.
            JsfUti.update("formEstudioJuridico");
            JsfUti.executeJS("PF('estudioJuridico').show();");
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar el elemento para editar.", "");
        }
    }

    public void guardarEstudioJuridico() {
        try {
            if (estudioJuridicoNuevo.getValor() != null) {
                estudioJuridicoNuevo.setEstado("A");
                estudioJuridicoNuevo.setCatalogo(catalogo);
                estudioJuridicoNuevo.setCodename(estudioJuridicoNuevo.getValor().trim().toLowerCase());
                estudioJuridico = (CtlgItem) manager.persist(estudioJuridicoNuevo);
                estudioJuridicoNuevo = new CtlgItem();
                if (tipoIngreso == 1) {
                    JsfUti.update("formProcesar:tabDetalle:panelEstudio");
                } else {
                    JsfUti.update("formIngresoExo:tabDetalleIng:pnlEstudio");
                }
                JsfUti.executeJS("PF('estudioJuridico').hide();");
            } else {
                JsfUti.messageWarning(null, "El campo nombre esta vacio.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectObject(SelectEvent event) {
        solicitanteInterviniente = (CatEnte) event.getObject();
    }

    public void handleUpload(FileUploadEvent event) throws IOException {
        try {
            if (historico.getId() != null) {
                /*if (doc.saveDocumentoHabilitante(event.getFile(), historico, session.getUserId())) {
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgDocs').hide();");
                    JsfUti.messageInfo(null, "Reemplazo de Documento con exito!!!", "");
                } else {
                    JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
                }*/
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void generarNuevaFactura(RegpLiquidacion re) {
        try {

            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            RenCajero temp = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
            facturacion.emitirFacturaElectronica(re, temp);
            JsfUti.messageInfo(null, "Se confirma la emision del comprobante elecronico.", "");

            /*if (re.getNumeroComprobante().compareTo(BigInteger.ZERO) == 0
                    && re.getTotalPagar().compareTo(BigDecimal.ZERO) > 0
                    && re.getEstadoLiquidacion().getId() == 2L) {
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                //map.put("usuario", new AclUser(re.getUserIngreso()));
                map.put("usuario", new AclUser(us.getUserId()));
                RenCajero temp = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
                facturacion.emitirFacturaElectronica(re, temp);
                JsfUti.messageInfo(null, "Se confirma la emision del comprobante elecronico.", "");
            } else {
                JsfUti.messageWarning(null, "Revisar los datos de la proforma.", "");
            }*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pruebasFacturacion(RegpLiquidacion re) {
        try {
            itl.pruebasWsFacturacion(re.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void desblockLiquidacion(RegpLiquidacion re) {
        try {
            re.setEstado(0);
            manager.update(re);
            JsfUti.messageInfo(null, "OK.", "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void procesarTitulo() {
        liquidacion.setFechaIngreso(new Date());
        liquidacion.setPagoFinal(liquidacion.getTotalPagar());
        modelPago = new PagoModel();
        modelPago.setValorLimite(liquidacion.getTotalPagar());
        modelPago.setValorRecibido(liquidacion.getTotalPagar());
        modelPago.setValorCobrar(liquidacion.getTotalPagar());
        modelPago.setValorTotalEfectivo(liquidacion.getTotalPagar());
        modelPago.calcularTotalPago();
        JsfUti.update("formIngresoTitulo");
        JsfUti.executeJS("PF('dlgIngresoTitulo').show();");
    }

    public void showDlgTituloCredito(RegpLiquidacion re) {
        try {
            if (cajero != null) {
                if (this.comprararFechas(re.getFechaCreacion())) {
                    liquidacion = re;
                    // LIQUIDACION CON ESTADO ACEPTADO Y VALOR DE PAGO 0.00
                    if (re.getEstadoLiquidacion().getId() == 1L && (re.getTotalPagar().compareTo(BigDecimal.ZERO) == 0)) {
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                    } else if (re.getEstadoLiquidacion().getId() == 1L && re.getEstadoPago().getId() == 1L) { // PENDIENTE DE PAGO
                        this.procesarTitulo();
                    } else if (re.getEstadoPago().getId() == 3L) { // CTA POR COBRAR
                        JsfUti.messageWarning(null, "Proforma ya esta Ingresada como Cta. por Cobrar.", "");
                    } else if (re.getEstadoPago().getId() == 2L) { // CANCELADO
                        JsfUti.messageWarning(null, "Proforma ya esta Cancelada e Ingresada.", "");
                    } else {
                        JsfUti.messageWarning(null, "Estado de Pago ANULADO o INCOMPLETO.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "La solicitud pasa de fecha de validez.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Usuario no tiene permisos asignados.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarTitulo() {
        try {
            if (liquidacion.getNumeroComprobante() == null
                    || liquidacion.getNumeroComprobante().compareTo(BigInteger.ZERO) <= 0) {
                JsfUti.messageWarning(null, "Faltan campos.", "Debe ingresar el número de título de crédito.");
                return;
            }
            if (liquidacion.getFechaIngreso() == null) {
                JsfUti.messageWarning(null, "Faltan campos.", "Debe ingresar fecha y hora de pago del título.");
                return;
            }
            if (modelPago.getValorTotal().compareTo(liquidacion.getTotalPagar()) == 0) {
                if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                    liquidacion.setUserIngreso(us.getUserId());
                    liquidacion.setEstudioJuridico(estudioJuridico);
                    liquidacion.setInfAdicionalProf(infoAdicional);
                    liquidacion.setEstado(us.getUserId().intValue());
                    liquidacion = itl.cancelarLiquidacion(liquidacion, modelPago.realizarPago(liquidacion), cajero);
                    if (liquidacion != null) {
                        liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                        if (liquidacion.getTramiteOnline()) {
                            this.iniciarTramiteActivitiOnline();
                            this.showDlgUserIngreso();
                        } else {
                            this.iniciarTramiteActiviti();
                            this.showDlgUserIngreso();
                        }
                    }
                } else {
                    JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                }
            } else {
                JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor ni menor al de la proforma.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgUserIngreso() {
        JsfUti.executeJS("PF('dlgProcesar').hide();");
        JsfUti.executeJS("PF('dlgIngresoExo').hide();");
        JsfUti.update("formUserAsignado");
        JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");
    }

    public void continuarCobro() {
        try {
            if (liquidacion.getId() != null) {
                //serv.enviarCorreoTituloCredito(liquidacion, SisVars.rutaTitulos + liquidacion.getNumTramiteRp() + ".pdf");
                JsfUti.redirectFaces("/procesos/tesoreria/ingresoExonerados.xhtml");
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }

    }

    public List<AclUser> getDisponibles() {
        return manager.findAllEntCopy(Querys.getUsuarios);
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public LazyModel<RegpLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(LazyModel<RegpLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public PagoModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoModel modelPago) {
        this.modelPago = modelPago;
    }

    public Integer getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(Integer tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public List<RenEntidadBancaria> getBancos() {
        return bancos;
    }

    public void setBancos(List<RenEntidadBancaria> bancos) {
        this.bancos = bancos;
    }

    public List<RenEntidadBancaria> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<RenEntidadBancaria> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public HistoricoTramites getHistorico() {
        return historico;
    }

    public void setHistorico(HistoricoTramites historico) {
        this.historico = historico;
    }

    public CtlgItem getEstudioJuridico() {
        return estudioJuridico;
    }

    public void setEstudioJuridico(CtlgItem estudioJuridico) {
        this.estudioJuridico = estudioJuridico;
    }

    public CtlgItem getEstudioJuridicoNuevo() {
        return estudioJuridicoNuevo;
    }

    public void setEstudioJuridicoNuevo(CtlgItem estudioJuridicoNuevo) {
        this.estudioJuridicoNuevo = estudioJuridicoNuevo;
    }

    public CatEnte getSolicitanteInterviniente() {
        return solicitanteInterviniente;
    }

    public void setSolicitanteInterviniente(CatEnte solicitanteInterviniente) {
        this.solicitanteInterviniente = solicitanteInterviniente;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public AclUser getAsignado() {
        return asignado;
    }

    public void setAsignado(AclUser asignado) {
        this.asignado = asignado;
    }

}
