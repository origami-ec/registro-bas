/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.bpm.models.DetalleProceso;
import com.origami.sgr.ebilling.interfaces.FacturacionElectronicaLocal;
import com.origami.sgr.ebilling.models.ComprobanteSRI;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpExoneracion;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenNotaCredito;
import com.origami.sgr.entities.RenPago;
import com.origami.sgr.entities.RenPagoDetalle;
import com.origami.sgr.entities.RenPagoRubro;
import com.origami.sgr.entities.RenTipoEntidadBancaria;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.ProcessInstanceLazy;
import com.origami.sgr.lazymodels.RegActoLazy;
import com.origami.sgr.lazymodels.RenFacturaLazy;
import com.origami.sgr.lazymodels.RenNotaCreditoLazy;
import com.origami.sgr.models.PagoModel;
import com.origami.sgr.models.SamLiquidacionDetalles;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.ZoneId;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class FacturacionElectronica extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(FacturacionElectronica.class.getName());

    @Inject
    protected Entitymanager em;

    @Inject
    private AsynchronousService as;

    @Inject
    private UserSession us;

    @Inject
    private FacturacionElectronicaLocal fac;

    @Inject
    private SeqGenMan sec;

    protected RenNotaCreditoLazy lazy;
    protected int dias = 0;
    protected RenCajero cajero;
    protected Date fecha = new Date();
    protected Date ingreso = new Date();
    protected Date desde = new Date();
    protected Date hasta = new Date();
    protected List<RegpLiquidacion> liquidaciones, facturas;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected List<AclUser> cajeros = new ArrayList<>();
    protected AclUser user, caja;
    protected Boolean flag = Boolean.FALSE;
    protected Boolean block = Boolean.FALSE;
    protected CatEnte solicitante = new CatEnte();
    protected Integer tipoTarea = 0, estado = 1;
    protected Long tramite, titulocredito = 0L;
    protected String observacion;
    protected RegpLiquidacion proforma = new RegpLiquidacion();
    protected RenPago renPago;
    protected CatEnte ente;
    protected Long numerotramite;
    protected String formaPago;
    protected String cedula = "";
    protected RenFacturaLazy emisiones;
    protected Integer tipoPago;

    //VARIABLLEES PARA AGRREGAR ACTOS A LA FACTURA =O
    protected RegActoLazy actos;
    protected List<RegpIntervinientes> listInterv;
    protected RegpIntervinientes interviniente;
    private Integer aniosDiferencia;
    protected RegActo acto;
    protected RegpLiquidacionDetalles rld = new RegpLiquidacionDetalles();
    protected boolean editar = false;
    protected boolean certificado = false;
    protected boolean diferenciaPagos = false;
    protected CtlgCatalogo catalogo;
    protected CtlgItem usoDocumento;
    protected CtlgItem nuevoUsoDoc;
    protected String nombre = "";
    protected String obsAdicional = "";
    protected BigDecimal subTotal;
    protected BigDecimal subTotalDesc;
    protected BigDecimal totalPagar;
    protected BigDecimal descPorLey;
    protected BigDecimal recargoAplicado;
    protected BigDecimal descLimitCobro;
    protected BigDecimal gastosGenerales;
    protected BigDecimal avaluo = BigDecimal.ZERO;
    protected BigDecimal cuantia = BigDecimal.ZERO;
    protected BigDecimal porcPago = BigDecimal.ONE;
    protected BigDecimal adicional = BigDecimal.ZERO;
    protected BigDecimal valorDiferenciaActo = BigDecimal.ZERO;
    protected Integer indice;
    protected List<RegpLiquidacionDetalles> actosPorPagar;
    protected int indiceActo = 0;
    private Boolean beneficiarioEsSolicitante, agregaBeneficiario, agregaSolicitante, facturaSinTramite = false;
    private Integer indexBeneficiario, indexSolicitante;
    //VARIABLES PARA LA REFORMA DE TABLA DE ARANCELES
    protected Integer tipocalculo = 0;
    protected RegpLiquidacion liquidacion;
    protected ProcessInstanceLazy details;
    protected HistoricoTramites procesoBorrar;
    protected List<RenPagoDetalle> pagodetalle;
    protected Valores valor;
    protected List<RenEntidadBancaria> tarjetas;
    protected PagoModel modelPago;
    protected List<RenEntidadBancaria> bancos;

    private String obs;
    private RenFactura factura;
    protected DetalleProceso proceso;

    private List<ComprobanteSRI> comprobantesElectronicos;
    private ComprobanteSRI comprobanteSRI;

    //VARIABLES CAMBIO DE TIPO DE PAGO
    protected String numeroNotaCredito;
    protected String chNumeroCheque;
    protected String chNumeroCuenta;
    protected String trNumTransferencia;
    protected RenEntidadBancaria bancoSelect;
    protected RenEntidadBancaria tarjeta;
    protected String titular;
    protected String baucher;
    protected String autorizacion;
    protected RenPagoDetalle detalleNew;
    protected Integer tipocobro;
    private List<RenPagoDetalle> listaPagosNew;
    protected BigDecimal valorDetaleNew = new BigDecimal("0.00");
    protected BigDecimal valorTotalListTipoPagos = new BigDecimal("0.00");
    protected CatEnte enteCambio;

    @PostConstruct
    protected void iniView() {
        try {
            listaPagosNew = new ArrayList<>();
            bancoSelect = new RenEntidadBancaria();
//            details = new ProcessInstanceLazy(false);
            factura = new RenFactura();
            proceso = new DetalleProceso();
            lazy = new RenNotaCreditoLazy();
            actos = new RegActoLazy(new RegTipoCobroActo(2L));
            liquidaciones = new ArrayList<>();
            facturas = new ArrayList<>();
            cajeros = itl.getUsuariosByRolName("cajero");
            emisiones = new RenFacturaLazy();
            ente = new CatEnte();
            listInterv = new ArrayList<>();
            acto = new RegActo();
            actosPorPagar = new ArrayList<>();
            map = new HashMap();
            map.put("code", Constantes.diasValidezProforma);
            valor = (Valores) em.findObjectByParameter(Valores.class, map);
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            cajero = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("tipo", new RenTipoEntidadBancaria(1L));
            bancos = em.findObjectByParameterList(RenEntidadBancaria.class, map);
            map.put("tipo", new RenTipoEntidadBancaria(2L));
            tarjetas = em.findObjectByParameterList(RenEntidadBancaria.class, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarComprobantes() {
        if (solicitante == null || solicitante.getId() == null) {
            JsfUti.messageWarning(null, "Debe seleccionar un Solicitante.", "");
        } else {
            comprobantesElectronicos = fac.getAllComprobanteByCedula(solicitante.getCiRuc());
        }
    }

    public Boolean verificarTramites() {
        boolean retorno = false;
        map = new HashMap();
        map.put("realizado", true);
        map.put("tramite", proforma.getTramite());
        List<RegpTareasTramite> listaTramites;
        listaTramites = (List<RegpTareasTramite>) manager.findObjectByParameterList(RegpTareasTramite.class, map);
        if (!listaTramites.isEmpty()) {
            retorno = true;
        }
        System.out.println("listaTramites.size() es --- " + listaTramites.size());
        return retorno;
    }

    public void onRowSelect() {
        try {
            if (acto.getArancel() == null) {
                JsfUti.messageWarning(null, "El acto seleccionado no tiene arancel asociado.", "");
                return;
            }
            aniosDiferencia = 0;
            editar = false;
            certificado = false;
            diferenciaPagos = false;
            this.verTipoCalculo();
            rld = new RegpLiquidacionDetalles();
            rld.setCantidad(1);
            rld.setActo(acto);
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setRecargo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setCantidadIntervinientes(1);
            if (acto.getTipoActo() != null) {
                String name = acto.getTipoActo().getNombre().toUpperCase();
                if (name.contains("HISTORIADO")) {
                    certificado = true;
                }
                if (name.contains("DIFERENCIA")) {
                    diferenciaPagos = true;
                }
            }
            listInterv.stream().map((re) -> {
                re.setId(null);
                return re;
            }).forEachOrdered((re) -> {
                re.setExoneracion(null);
            });
            JsfUti.update("formCuantia");
            JsfUti.executeJS("PF('dlgCuantia').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void verTipoCalculo() {
        tipocalculo = 0;
        if (acto.getTipoCobro() != null) {
            tipocalculo = acto.getTipoCobro().getId().intValue();
        }
    }

    public void cargarFacturasAutorizadas() {
        try {
            if (user != null && fecha != null) {
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", user);
                cajero = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
                if (cajero != null) {
                    liquidaciones = itl.cargarFacturasNoEnviadas(sdf.format(fecha), user.getId());
                    if (liquidaciones.isEmpty()) {
                        JsfUti.messageWarning(null, "No se han cargado facturas para generar RIDE.", "");
                    }
                    JsfUti.update("mainForm");
                } else {
                    JsfUti.messageWarning(null, "Usuario no tiene Caja asignada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar el cajero y fecha de ingreso.", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRIDE() {
        try {
            if (liquidaciones.isEmpty()) {
                JsfUti.messageWarning(null, "No se han cargado facturas para generar RIDE.", "");
            } else if (cajero != null) {
                map = new HashMap();
                map.put("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/ingreso/");
                map.put("LOGO_URL", JsfUti.getRealPath("/resources/icons/logorp.png"));
                flag = itl.generarRIDE(liquidaciones, cajero, JsfUti.getRealPath("/reportes/ingreso/ride.jasper"), map);
                if (flag) {
                    JsfUti.messageInfo(null, "Se generaron los RIDEs con exito.", "");
                } else {
                    JsfUti.messageWarning(null, "Problemas al generar RIDE.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarNotificacion(RegpLiquidacion re) {
        try {
            if (re.getFechaAutorizacion() != null && re.getNumeroAutorizacion() != null) {
                if (itl.envioCorreoFacturaElectronica(re, cajero)) {
                    JsfUti.messageInfo(null, "Se envio el correo con EXITO.", "");
                } else {
                    JsfUti.messageError(null, "NO se envio el correo electronico.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se pudo enviar el correo electronico.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarTodosLosCorreos() {
        try {
            if (liquidaciones.isEmpty()) {
                JsfUti.messageWarning(null, "No hay facturas para enviar.", "");
            } else if (flag) {
                as.enviarCorreosRIDE(liquidaciones, cajero);
                block = true;
                JsfUti.update("mainForm:tabFacturacion:pnlComandos");
                JsfUti.messageInfo(null, "Se estan enviando los correos electronicos.", "");
            } else {
                JsfUti.messageWarning(null, "Debe generar RIDEs para enviar los correos electronicos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findLiquidacion() {
        try {
            if (tramite != null) {
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                map.put("estadoLiquidacion", 2L);
                map.put("estadoPago", 2L);
                proforma = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
                map = new HashMap();
                map.put("liquidacion", proforma);
                renPago = (RenPago) em.findObjectByParameter(RenPago.class, map);
                map = new HashMap();
                map.put("pago", renPago);
                pagodetalle = (List<RenPagoDetalle>) em.findObjectByParameterList(RenPagoDetalle.class, map);
                for (RenPagoDetalle x : pagodetalle) {
                    tipocobro = Integer.valueOf(x.getTipoPago() + "");
                }
//                tipocobro = new Integer(pagodetalle.get(0).getTipoPago()+"") ;
//                System.out.println("el tipo pago es  ----- " + tipocobro);
                if (proforma == null) {
                    proforma = new RegpLiquidacion();
                    JsfUti.messageWarning(null, "No se encuentra el Tramite o ya fué anulado", "");
                } else {
                    ente = proforma.getBeneficiario();
                    System.out.println("se encontro el tramite con codigo--- " + proforma.getNumTramiteRp());
                    JsfUti.update("mainForm");
                    JsfUti.messageInfo(null, "TRÁMITE ENCONTRADO", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de Tramite.", "");
            }
        } catch (NumberFormatException e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void registrarLiquidacionSAM() {
        try {
            if (proforma != null && proforma.getId() != null) {
                SamLiquidacionDetalles sld = itl.verificarPagoSAM(proforma.getNumTramiteRp());
                if (sld == null) {
                    if (itl.registrarLiquidacionSAM(proforma.getId())) {
                        JsfUti.messageInfo(null, "La solicitud se puedo registar con exito en el SAM.", "");
                    } else {
                        JsfUti.messageError(null, "NO se pudo registrar la solicitud en el SAM. Error de sistema.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "NO se pudo registrar la solicitud en el SAM. El tramite ya existia.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se encuentra el numero de tramite ingresado.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void tareasFacturas() {
        if (proforma.getId() != null) {
            switch (tipoTarea) {
                case 1:
                    this.showDlgAnulacion();
                    break;
                case 2:
                    this.generarNuevaFactura();
                    break;
                default:
                    JsfUti.messageWarning(null, "Debe seleccionar la tarea para la factura.", "");
                    break;
            }
        } else {
            JsfUti.messageWarning(null, "Debe buscar el tramite.", "");
        }
    }

    public void showDlgAnulacion() {
        if (proforma.getEstadoLiquidacion().getId() == 3L) {
            JsfUti.messageWarning(null, "La factura ya fue anulada.", "");
        } else if (proforma.getNumeroComprobante().compareTo(BigInteger.ZERO) <= 0) {
            JsfUti.messageWarning(null, "La proforma no tiene factura asignada.", "");
        } else {
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        }
    }

    public void anularTramiteFactura() {
        try {
            if (!observacion.isEmpty()) {
                map = new HashMap();
                map.put("numTramite", proforma.getNumTramiteRp());
                procesoBorrar = new HistoricoTramites();
                procesoBorrar = (HistoricoTramites) em.findObjectByParameter(HistoricoTramites.class, map);

                renPago = new RenPago();
                map = new HashMap();
                map.put("liquidacion", proforma);
                renPago = (RenPago) em.findObjectByParameter(RenPago.class, map);
                renPago.setEstado(false);
                renPago.setObservacion(observacion);
                em.merge(renPago);
                proforma.setEstadoLiquidacion(new RegpEstadoLiquidacion(3L));
                proforma.setEstadoPago(new RegpEstadoPago(5L));
                em.merge(proforma);
                renPago = new RenPago();
                proforma = new RegpLiquidacion();
                this.deleteTramite();
                JsfUti.messageWarning(null, "Tramite ANULADO.", "");
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgObsvs').hide();");
                observacion = "";
            } else {
                JsfUti.messageWarning(null, "Debe ingresar la observacion de la anulacion.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarNuevaFactura() {
        try {
            if (proforma.getEstadoLiquidacion().getId() == 3L
                    && proforma.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                if (itl.nuevaFacturaTramiteExistente(proforma)) {
                    block = Boolean.TRUE;
                    JsfUti.update("mainForm");
                    JsfUti.messageInfo(null, "Ya hay nueva factura para el tramite.", "");
                } else {
                    JsfUti.messageWarning(null, Messages.error, "");
                }
            } else {
                JsfUti.messageWarning(null, "La factura debe estar anulada para generar una nueva.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarFacturasElectronicas() {
        try {
            map = new HashMap();
            map.put("ingreso", sdf.format(ingreso));
            if (caja != null && ingreso != null) {
                map.put("usuario", caja.getId());
                if (estado == 1) {
                    facturas = em.findNamedQuery(Querys.getFacturasAutorizadas, map);
                } else {
                    facturas = em.findNamedQuery(Querys.getFacturasNoAutorizadas, map);
                }
            } else {
                if (estado == 1) {
                    facturas = em.findNamedQuery(Querys.getFacturasAutorizadasAllUser, map);
                } else {
                    facturas = em.findNamedQuery(Querys.getFacturasNoAutorizadasAllUser, map);
                }
            }
            JsfUti.update("mainForm");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarCorreoFacturaElectronicaSRI(ComprobanteSRI comprobanteSRI) {
        this.comprobanteSRI = comprobanteSRI;
        JsfUti.update("formCorreo");
        JsfUti.executeJS("PF('dlgReenvioCorreo').show();");
    }

    public void reenviarCorreoFacturaElectronicaSRI() {
        //System.out.println("comprobanteSRI " + comprobanteSRI.getContribuyente().getEmail());
        fac.reenviarCorreoFacturaElectronicaSRI(comprobanteSRI);
        JsfUti.messageWarning(null, "Correo enviado Correctamente.", "");
        JsfUti.update("mainForm");
        JsfUti.executeJS("PF('dlgReenvioCorreo').hide();");
    }

    public void reenvioFacturas(RegpLiquidacion re) {
        try {
            /*if (re.getEstadoWs() == null) {
                JsfUti.messageWarning(null, "Se debe revisar los datos del comprobante antes del reenvio.", "");
            } else */
            if (re.getEstadoWs() != null && re.getEstadoWs().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                JsfUti.messageWarning(null, "Factura con estado: AUTORIZADO, no se necesita comprobar el reenvio.", "");
            } else {
                AclUser temp = em.find(AclUser.class, re.getUserIngreso());
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", temp);
                cajero = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
                if (cajero != null) {
                    fac.reenviarFacturaElectronica(re, cajero, Boolean.FALSE);
                    this.consultarFacturasElectronicas();
                    JsfUti.messageWarning(null, "Factura reenviada, consulte nuevamente", "");
                } else {
                    JsfUti.messageWarning(null, "No se encuentra caja de ingreso de Tramite.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlg() {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "60%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic("/resources/dialog/dlglazyente", options, null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgPapel(String urlFacelet, int indice) {
        interviniente = listInterv.get(indice);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "60%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public void selectObjectExo(SelectEvent event) {
        RegpExoneracion ex = (RegpExoneracion) event.getObject();
        //exoneracion.setExoneracion(ex);
        interviniente.setExoneracion(ex);
    }

    public void eliminarInterviniente(int index) {
        try {
            RegpIntervinientes in = listInterv.get(index);
            listInterv.remove(index);
            if (in.getId() != null) {
                em.delete(in);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarActo() {
        try {
            if (this.validaPapeles()) {

                if (certificado) {
                    if (rld.getAnioUltimaTrasnferencia() == null) {
                        JsfUti.messageWarning(null, "Debe especificar el anio de la ultima trasferencia.", "");
                        return;
                    }
                    if (rld.getAnioAntecedenteSolicitado() == null) {
                        JsfUti.messageWarning(null, "Debe especificar el anio del antecedente solicitado.", "");
                        return;
                    }
                }
                if (indice > 0 && (rld.getCantidadIntervinientes() != null && rld.getCantidadIntervinientes() < indice)) {
                    JsfUti.messageWarning(null, "La cantidad de intervinientes es menor a la cantidad de intervinientes exonerados.", "");
                    return;
                }
                if (rld.getCantidadIntervinientes() == null && indice > 0) {
                    JsfUti.messageWarning(null, "Debe de especificar la cantidad de intervinientes comparecientes al pago para aplicar el descuento.", "");
                    return;
                }
                if (rld.getCantidad() == null) {
                    JsfUti.messageWarning(null, "Debe de ingresar la cantidad.", "");
                    return;
                }
                if (rld.getCantidad() == 0) {
                    JsfUti.messageWarning(null, "La cantidad no puede ser 0.", "");
                    return;
                }
                if (acto.getArancel() != null) {
                    if (acto.getArancel().getValor().compareTo(new BigDecimal(-1)) == 0) {
                        if (this.validaCuantiaAvaluo()) {
                            if (avaluo != null) {
                                rld.setAvaluo(avaluo);
                            }
                            if (cuantia != null) {
                                rld.setCuantia(cuantia);
                            }
                            if (avaluo.compareTo(cuantia) > 0) {
                                rld.setValorUnitario(itl.calculoCuantia(avaluo));
                            } else {
                                rld.setValorUnitario(itl.calculoCuantia(cuantia));
                            }
                            this.calculoContrato();
                        }
                    } else {
                        if (avaluo != null) {
                            rld.setAvaluo(avaluo);
                        }
                        if (cuantia != null) {
                            rld.setCuantia(cuantia);
                        }
                        if (diferenciaPagos) {
                            if (valorDiferenciaActo != null && valorDiferenciaActo.compareTo(BigDecimal.ZERO) > 0) {
                                rld.setValorUnitario(valorDiferenciaActo);
                            } else {
                                JsfUti.messageWarning(null, "La cantidad no puede ser 0 en Valor de Diferencia de Pago.", "");
                                return;
                            }
                        } else {
                            rld.setValorUnitario(acto.getArancel().getValor());
                        }
                        this.calculoContrato();
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaPapeles() {
        indice = 0;
        porcPago = BigDecimal.ZERO;
        if (listInterv.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el/los interviniente(s) y su tipo para el contrato.", "");
            return false;
        } else {
            if (!listInterv.stream().map((re) -> {
                if (re.getExoneracion() != null) {
                    indice++;
                    porcPago = porcPago.add(re.getExoneracion().getValor());
                }
                return re;
            }).noneMatch((re) -> (!acto.getSolvencia() && re.getPapel() == null))) {
                //JsfUti.messageWarning(null, "Existen intervinientes sin especificar el Papel o la Calidad.", "");
                //return false;
            }
        }
        if (indice > 0) {
            porcPago = porcPago.divide(new BigDecimal(indice), 2, RoundingMode.HALF_UP);
        }
        return true;
    }

    public Boolean validaCuantiaAvaluo() {
        Boolean temp = false;
        if (avaluo == null) {
            avaluo = BigDecimal.ZERO;
        }
        if (cuantia == null) {
            cuantia = BigDecimal.ZERO;
        }
        if (avaluo.compareTo(BigDecimal.ZERO) > 0 || cuantia.compareTo(BigDecimal.ZERO) > 0) {
            temp = true;
        } else {
            JsfUti.messageWarning(null, "El valor debe del avaluo o la cuantia debe ser mayor a 0.", "");
        }
        return temp;
    }

    public void calculoContrato() {
        Boolean certificados = false;
        if (rld.getReingreso()) {
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
        } else {
            if (rld.getActo().getSolvencia()) {
                rld.setValorTotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
                rld.setSubtotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
                rld.setRecargo(new BigDecimal(aniosDiferencia).multiply(new BigDecimal(rld.getCantidad())));
            } else {
                rld.setValorTotal(rld.getValorUnitario());
                rld.setSubtotal(rld.getValorUnitario());
                rld.setRecargo(new BigDecimal(aniosDiferencia));
            }

        }

        rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
        BigDecimal descontar = BigDecimal.ZERO;

        if (indice > 0) {
            BigDecimal valReferencia = rld.getValorTotal().divide(new BigDecimal(rld.getCantidadIntervinientes()), 2, RoundingMode.HALF_UP);
            boolean compartida = true;
            BigDecimal porciento = BigDecimal.ZERO;
            for (RegpIntervinientes i : listInterv) {
                if (i.getExoneracion() != null) {
                    if (i.getExoneracion().getCompartida()) {
                        descontar = descontar.add(valReferencia.multiply(i.getExoneracion().getValor()));
                    } else {
                        porciento = i.getExoneracion().getValor();
                        compartida = Boolean.FALSE;
                        break;
                    }
                }
            }
            if (!compartida) {
                descontar = rld.getValorTotal().multiply(porciento);
            }
        }
        rld.setDescuento(descontar.setScale(2, RoundingMode.HALF_UP));

        rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).subtract(rld.getDescuento()).setScale(2, RoundingMode.HALF_UP));

        List<RegpIntervinientes> temp = (List<RegpIntervinientes>) EntityBeanCopy.clone(listInterv);
        rld.setRegpIntervinientesCollection(temp);
        if (editar) {
            actosPorPagar.add(indiceActo, rld);
        } else {
            rld.setFechaIngreso(new Date());
            actosPorPagar.add(rld);
        }
        if (certificados) {
            this.agregarCertificados();
        }
        this.calculoTotalPagar();
        JsfUti.update("mainForm:tabViewAnulacion:pnlContratos");
        JsfUti.executeJS("PF('dlgCuantia').hide();");
        //JsfUti.executeJS("PF('dglEditCuantia').hide();");
    }

    public void deleteTramite() {
        try {
            if (procesoBorrar.getId() != null) {
                if (this.saveObservacion()) {
                    this.deleteProcessInstance(procesoBorrar.getIdProceso());
                    JsfUti.messageInfo(null, "Tramite eliminado con exito.", "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR", "");
        }
    }

    public boolean saveObservacion() {
        try {
            Observaciones ob = new Observaciones();
            ob.setObservacion("Por baja de titulo");
            ob.setEstado(true);
            ob.setFecCre(new Date());
            ob.setIdTramite(new HistoricoTramites(procesoBorrar.getId()));
            ob.setTarea("ELIMINAR TRAMITE");
            ob.setUserCre(session.getName_user());
            ob.setAclLogin(session.getAclLogin().getId());
            manager.persist(ob);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void cargarEntidadesBancarias(Integer tipo) {
        bancos = new ArrayList<>();
        map = new HashMap();
        map.put("tipo", tipoPago);
        map.put("estado", true);
        bancos = em.findObjectByParameterList(RenEntidadBancaria.class, map);
    }

    public void verTipoPago() {
        System.out.println("el tipo pago es--- " + tipoPago);
    }

    public void generarComprobanteReenvio() {
        try {
            if (proforma.getId() != null) {
                ss.instanciarParametros();
                ss.setGeneraFile(true);
                ss.setRutaDocumento(SisVars.rutaTitulos + proforma.getNumTramiteRp() + ".pdf");
                ss.setTieneDatasource(true);
                ss.setNombreReporte("titulo_credito");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", proforma.getId());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(proforma.getTotalPagar().toString()));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
                ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
//                JsfUti.executeJS("PF('dlgProcesar').hide();");
//                JsfUti.executeJS("PF('dlgIngresoExo').hide();");
//                JsfUti.update("formUserAsignado");
//                JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
//                JsfUti.redirectFaces("/procesos/tesoreria/editarFacturas.xhtml");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reenviarCorreo() {
        this.generarComprobanteReenvio();
        //System.out.println("el correo del entes es--- " + ente.getCorreo1());
        JsfUti.update("dlgEnviarCorreo");
        JsfUti.executeJS("PF('dlgEnviarCorreo').show();");
//        try {
//            Thread.sleep(3000); // 1000 milisegundos = 2 segundo
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void enviarCorreoProforma() {
        if (proforma.getId() != null) {
            as.enviarCorreoTituloCredito(proforma, SisVars.rutaTitulos + proforma.getNumTramiteRp() + ".pdf",
                    session.getName_user());
            JsfUti.redirectFaces("/procesos/tesoreria/editarFacturas.xhtml");
        } else {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void eliminarTipoCobro(RenPagoDetalle item) {
        this.setValorTotalListTipoPagos(this.getValorTotalListTipoPagos().subtract(item.getValor()));
        listaPagosNew.remove(item);
        JsfUti.update("mainForm");
    }

    public void addTipoPago() {
        try {

            detalleNew = new RenPagoDetalle();
            switch (tipoPago) {
                case 1://efectivo
                    if (valorDetaleNew.intValue() > 0) {
//                        detalleNew.setPago(renPago);
                        detalleNew.setTipoPago(1L);
                        detalleNew.setValor(valorDetaleNew);
                        this.setValorTotalListTipoPagos(this.getValorTotalListTipoPagos().add(valorDetaleNew));
                        valorDetaleNew = new BigDecimal("0.00");
                        listaPagosNew.add(detalleNew);
                        System.out.println("el tamanio es--- " + listaPagosNew.size());
                    } else {
                        JsfUti.messageError(null, "EL VALOR DEBE SER MAYOR A 0", "");
                        return;
                    }
                    break;
                case 3://nota credito
                    if (numeroNotaCredito != null && !numeroNotaCredito.isEmpty() && valorDetaleNew.intValue() > 0) {
//                        detalleNew.setPago(renPago);
                        detalleNew.setTipoPago(3L);
                        detalleNew.setValor(valorDetaleNew);
                        detalleNew.setNcNumCredito(numeroNotaCredito);
                        detalleNew.setNcFecha(new Date());

                        this.setValorTotalListTipoPagos(this.getValorTotalListTipoPagos().add(valorDetaleNew));
                        numeroNotaCredito = "";
                        valorDetaleNew = new BigDecimal("0.00");
                        listaPagosNew.add(detalleNew);
                    } else {
                        JsfUti.messageError(null, "DEBE LLENAR TODOS LOS CAMPOS CORRECTAMENTE", "");
                        return;
                    }
                    break;
                case 4://cheque
                    System.out.println("bancoSelect es --- " + bancoSelect);
                    if (bancoSelect != null && !chNumeroCheque.isEmpty() && !chNumeroCuenta.isEmpty() && valorDetaleNew.intValue() > 0) {
//                        detalleNew.setPago(renPago);
                        detalleNew.setTipoPago(4L);
                        detalleNew.setValor(valorDetaleNew);
                        detalleNew.setChBanco(bancoSelect);
                        detalleNew.setChNumCheque(chNumeroCheque);
                        detalleNew.setChNumCuenta(chNumeroCuenta);
                        this.setValorTotalListTipoPagos(this.getValorTotalListTipoPagos().add(valorDetaleNew));
                        bancoSelect = null;
                        chNumeroCuenta = "";
                        chNumeroCheque = "";
                        valorDetaleNew = new BigDecimal("0.00");
                        listaPagosNew.add(detalleNew);
                    } else {
                        JsfUti.messageError(null, "DEBE LLENAR TODOS LOS CAMPOS CORRECTAMENTE", "");
                        return;
                    }
                    break;
                case 5://transferencia
                    if (tarjeta != null && !trNumTransferencia.isEmpty() && valorDetaleNew.intValue() > 0) {
//                        detalleNew.setPago(renPago);
                        detalleNew.setTipoPago(5L);
                        detalleNew.setValor(valorDetaleNew);
                        detalleNew.setTrBanco(tarjeta);
                        detalleNew.setTrNumTransferencia(trNumTransferencia);
                        detalleNew.setTrFecha(new Date());
                        this.setValorTotalListTipoPagos(this.getValorTotalListTipoPagos().add(valorDetaleNew));

                        tarjeta = null;
                        trNumTransferencia = "";
                        valorDetaleNew = new BigDecimal("0.00");
                        listaPagosNew.add(detalleNew);
                    } else {
                        JsfUti.messageError(null, "DEBE LLENAR TODOS LOS CAMPOS CORRECTAMENTE", "");
                        return;
                    }
                    break;
                case 7://tarjeta
                    if (bancoSelect != null & !autorizacion.isEmpty() && !baucher.isEmpty() && !titular.isEmpty() && valorDetaleNew.intValue() > 0) {
//                        detalleNew.setPago(renPago);
                        detalleNew.setTipoPago(7L);
                        detalleNew.setValor(valorDetaleNew);
                        detalleNew.setTcBanco(bancoSelect);
                        detalleNew.setTcAutorizacion(autorizacion);
                        detalleNew.setTcBaucher(baucher);
                        detalleNew.setTcTitular(titular);
                        this.setValorTotalListTipoPagos(this.getValorTotalListTipoPagos().add(valorDetaleNew));

                        valorDetaleNew = new BigDecimal("0.00");
                        bancoSelect = null;
                        autorizacion = "";
                        baucher = "";
                        titular = "";
                        listaPagosNew.add(detalleNew);
                    } else {
                        JsfUti.messageError(null, "DEBE LLENAR TODOS LOS CAMPOS CORRECTAMENTE", "");
                        return;
                    }
                    break;
            }
        } catch (Exception e) {
        }
        System.out.println("valor total es--- " + valorTotalListTipoPagos);
    }

    public void cambiarTipoCobro() {
        try {
            if (diferenciaFechas() == 0) {
                renPago = new RenPago();
                map = new HashMap();
                map.put("liquidacion", proforma);
                renPago = (RenPago) em.findObjectByParameter(RenPago.class, map);
                pagodetalle = new ArrayList<>();
                map = new HashMap();
                map.put("pago", renPago);
                pagodetalle = (List<RenPagoDetalle>) em.findObjectByParameterList(RenPagoDetalle.class, map);
                if (!pagodetalle.isEmpty()) {
                    if (valorTotalListTipoPagos.compareTo(renPago.getValor()) == 0) {
                        for (RenPagoDetalle det : listaPagosNew) {
                            det.setPago(renPago);
                            em.persist(det);
                        }
                        for (RenPagoDetalle detdel : pagodetalle) {
                            detdel.setPago(null);
                            detdel.setBanco(null);
                            detdel.setTrBanco(null);
                            detdel.setChBanco(null);
                            em.delete(detdel);
                        }
                        JsfUti.messageInfo(null, "SE CAMBIÓ EL TIPO DE PAGO", "");
                        JsfUti.redirect("/sgr-std/procesos/tesoreria/editarFacturas.xhtml");
                    } else {
                        JsfUti.messageError(null, "El valor del nuevo detalle debe ser igual al anterior", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "No se encuentran detalles", "");
                }
            } else {
                JsfUti.messageWarning(null, "Excedio la fecha limite", "");
            }
        } catch (Exception e) {
        }

    }

    public void darDeBajaTitulo() {
        try {
            if (diferenciaFechas() == 0) {
                if (verificarTramites().equals(Boolean.TRUE)) {
                    JsfUti.messageWarning(null, "Imposible Cambiar, tramite terminado", "");
                } else {
                    JsfUti.update("formObs");
                    JsfUti.executeJS("PF('dlgObsvs').show();");
                }
            } else {
                JsfUti.update("mainForm");
                JsfUti.messageWarning(null, "No se puede cambiar, supero el día de validez", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminarDetalle(int rowIndex) {
        try {
            RegpLiquidacionDetalles de = actosPorPagar.remove(rowIndex);
            if (de.getId() != null) {
                em.delete(de);
            }
            this.calculoTotalPagar();
            JsfUti.update("mainForm:tabViewAnulacion:pnlContratos");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarCertificados() {
        RegActo regacto = em.find(RegActo.class, 11L);
        map = new HashMap();
        map.put("code", Constantes.cantidadCertificadosPH);
        Valores temp = (Valores) em.findObjectByParameter(Valores.class, map);
        for (int i = 0; i < temp.getValorNumeric().intValue(); i++) {
            //System.out.println("// cada ingreso...");
            rld = new RegpLiquidacionDetalles();
            rld.setActo(regacto);
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setDescuento(BigDecimal.ZERO);
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
            rld.setFechaIngreso(new Date());
            rld.setRegpIntervinientesCollection((List<RegpIntervinientes>) EntityBeanCopy.clone(listInterv));
            actosPorPagar.add(rld);
        }
    }

    public void calculoTotalPagar() {
        totalPagar = BigDecimal.ZERO;
        descPorLey = BigDecimal.ZERO;
        recargoAplicado = BigDecimal.ZERO;
        subTotal = BigDecimal.ZERO;
        subTotalDesc = BigDecimal.ZERO;
        descLimitCobro = BigDecimal.ZERO;
        actosPorPagar.stream().map((det) -> {
            subTotal = subTotal.add(det.getSubtotal());
            return det;
        }).map((det) -> {
            descPorLey = descPorLey.add(det.getDescuento());
            recargoAplicado = recargoAplicado.add(det.getRecargo());
            return det;
        }).forEachOrdered((det) -> {
            subTotalDesc = subTotalDesc.add(det.getValorTotal());
        });
        gastosGenerales = subTotalDesc.multiply(adicional).setScale(2, RoundingMode.HALF_UP);
        totalPagar = subTotal.subtract(descPorLey).subtract(gastosGenerales).add(recargoAplicado);
    }

    public void selectObject(SelectEvent event) {
        solicitante = (CatEnte) event.getObject();
        consultarComprobantes();
    }

    public void downLoadFacturas() {
        File file, temp = null;
        FileInputStream fi;
        List<InputStream> fis = new ArrayList<>();
        try {
            if (solicitante.getId() == null || desde == null || hasta == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el solicitante y escoger las fechas correctamente.", "");
            } else if (desde.after(hasta)) {
                JsfUti.messageWarning(null, "Debe seleccionar las fechas correctamente.", "");
            } else {
                List<RegpLiquidacion> list = itl.getComprobantesBySolicitante(solicitante.getId(), desde, Utils.sumarRestarDiasFecha(hasta, 1));
                System.out.println("// " + list);
                /*List<RegpLiquidacion> list = itl.getComprobantesBySolicitante(solicitante.getId(), sdf.format(desde),
                        sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));*/
                if (list != null && !list.isEmpty()) {
                    for (RegpLiquidacion cod : list) {
                        if (cod.getEstadoWs() == null) {
                            temp = new File(Constantes.rutaFeOld + "/" + cod.getClaveAcceso() + ".pdf");
                        } else if (cod.getEstadoWs().equalsIgnoreCase("AUTORIZADO")) {
                            temp = new File(Constantes.rutaFeOld + "/factura_" + cod.getCodigoComprobante() + ".pdf");
                        }
                        if (temp != null && temp.exists()) {
                            fi = new FileInputStream(temp);
                            fis.add(fi);
                        }
                    }
                    file = itl.mergeFilesPdf(fis);
                    if (file != null) {
                        ss.instanciarParametros();
                        ss.setNombreDocumento(file.getAbsolutePath());
                        JsfUti.redirectNewTab("/sgr/DownLoadFiles");
                    } else {
                        JsfUti.messageWarning(null, "No se pudo generar el Archivo.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "No se encontraron facturas a nombre del solicitante.", "");
                }
            }
        } catch (FileNotFoundException e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Long diferenciaFechas() {//para verificar que las fechas no pasen de 1 dia
        Instant instant = (new Date()).toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaActual = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
        instant = proforma.getFechaIngreso().toInstant();
        LocalDate proformaDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaProforma = LocalDate.of(proformaDate.getYear(), proformaDate.getMonth(), proformaDate.getDayOfMonth());
        long diferenciaDias = ChronoUnit.DAYS.between(fechaActual, fechaProforma);
        System.out.println("la diferencia de fechas es--- " + diferenciaDias);
        //return diferenciaDias;
        return 0L;
    }

    public void selectSolicitanteAlcance(SelectEvent event) {
        ente = (CatEnte) event.getObject();
    }

    public void selectSolicitante(SelectEvent event) {
        try {
            if (diferenciaFechas() == 0) {
                if (verificarTramites().equals(Boolean.TRUE)) {
                    JsfUti.messageWarning(null, "Imposible Cambiar, tramite terminado", "");
                } else {//aqui agregue oara cambiar el contribuyente (verifico que no pase 1 dia y que nni este realizado algo del tramite)
                    System.out.println("el diferenciaFechas es --- " + diferenciaFechas());
                    ente = (CatEnte) event.getObject();
                    proforma.setBeneficiario(ente);
                    proforma.setSolicitante(ente);
                    em.merge(proforma);
                    System.out.println("verificarTramites es--- " + verificarTramites());
                    System.out.println("se guardiii");
                    this.cargarDatosReporteTituloCredito();
//                    List<String> urlList = new ArrayList<>();
                    JsfUti.messageWarning(null, "Cambiado correctamente", "");
                    JsfUti.redirectFaces("/procesos/tesoreria/editarFacturas.xhtml");

//                    String url = "/procesos/tesoreria/editarFacturas.xhtml";
//                    urlList.add(SisVars.urlbase + "Documento");
//                    JsfUti.redirectMultipleConIP_V2(url, urlList);
                }
            } else {
                JsfUti.update("mainForm");
                JsfUti.messageWarning(null, "No se puede cambiar, supero el día de validez", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarDatosReporteTituloCredito() {
        try {
            ss.instanciarParametros();

            ss.setGeneraFile(true);
            ss.setRutaDocumento(SisVars.rutaTitulos + proforma.getNumTramiteRp() + ".pdf");

            ss.setTieneDatasource(true);
            ss.setNombreReporte("titulo_credito");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", proforma.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(proforma.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
            ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarDatosReporte() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            //ss.setNombreReporte("comprobante_proforma");
            ss.setNombreReporte("proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", proforma.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(proforma.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void limpiarDatosBeneficiario() {
        ente = new CatEnte();
    }

    public void generarComprobanteAlcance() {
        System.out.println("llega aqui");
//        try {
        if (proforma.getId() != null) {
            System.out.println("entra despues del if");
            ss.instanciarParametros();
            ss.setGeneraFile(true);
            ss.setRutaDocumento(SisVars.rutaTitulos + proforma.getNumTramiteRp() + ".pdf");
            ss.setTieneDatasource(true);
            ss.setNombreReporte("titulo_credito_alcance");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", proforma.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(factura.getTotalPagar().toString()));
            ss.agregarParametro("NUMERO_TITULO", factura.getNumeroComprobante());

            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
            ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
//
//            JsfUti.executeJS("PF('dlgProcesar').hide();");
//            JsfUti.executeJS("PF('dlgIngresoExo').hide();");
//            JsfUti.update("formUserAsignado");
//            JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");

            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } else {
            System.out.println("parece que salio null");
        }
//        } catch (Exception e) {
//            JsfUti.messageError(null, Messages.error, "");
//            LOG.log(Level.SEVERE, null, e);
//        }
    }

    public void guardarAlcancePago() {
        try {
            if (proforma != null && ente != null) {
                if (totalPagar.intValue() > 0 && obs != null) {
                    AclUser temp = em.find(AclUser.class, proforma.getUserIngreso());
                    map = new HashMap();
                    map.put("habilitado", Boolean.TRUE);
                    map.put("usuario", temp);
                    cajero = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
                    factura.setTotalPagar(totalPagar);
                    factura.setLiquidacion(proforma);
                    factura.setSolicitante(ente);
                    factura.setFechaEmision(new Date());
                    factura.setObservacion(obs);
                    factura.setNumTramite(proforma.getNumTramiteRp());
                    factura.setCaja(cajero);
                    factura.setCodigoComprobante(sec.getSecuenciaTitulo(factura.getNumTramite()).toString());
                    factura.setNumeroComprobante(new BigInteger(factura.getCodigoComprobante()));
                    renPago = new RenPago();
                    renPago.setFechaPago(factura.getFechaEmision());
                    renPago.setValor(factura.getTotalPagar());
                    renPago.setInteres(BigDecimal.ZERO);
                    renPago.setDescuento(BigDecimal.ZERO);
                    renPago.setRecargo(BigDecimal.ZERO);
                    renPago.setEstado(true);
                    renPago.setNumComprobante(factura.getNumeroComprobante());
//                    em.persist(renPago);
                    renPago = (RenPago) em.persist(renPago);
                    factura.setPago(renPago);
                    em.persist(factura);
                    JsfUti.messageInfo(null, "Se guardó correctamente el alcance de tramite ", proforma.getNumTramiteRp().toString());
                    this.generarComprobanteAlcance();

                    JsfUti.update("mainForm");
                } else {
                    JsfUti.messageError(null, "La observacion no puede estar vacia y el valor debe ser mayoir a 0.00", "");
                }
            } else {
                JsfUti.messageError(null, "NO HAY DATOS POR GUARDAR", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, "DEBE LLENAR LA INFORMACION NECESARIA", "");
        }
    }

    public void facturaElectronica() {
        try {
            if (ente.getId() != null) {
                if (!facturaSinTramite) {
                    if (numerotramite == null) {
                        JsfUti.messageWarning(null, "Debe Ingresar un Número de Trámite", "");
                        return;
                    }
                }
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", new AclUser(us.getUserId()));
                RenCajero temp = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
                if (temp != null) {
                    if (!facturaSinTramite) {
                        map = new HashMap();
                        map.put("numTramiteRp", numerotramite);
                        liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
                    } else {
                        liquidacion = new RegpLiquidacion();
                        liquidacion.setLiquidacionSinTramite(Boolean.TRUE);
                    }
                    liquidacionTemp();

                } else {
                    JsfUti.messageWarning(null, "El usuario debe tener caja asignada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe llenar todos los datos para generar la factura.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarBeneficiario() {
//        if (ente.getCiRuc().isEmpty()) {            
        if (enteCambio == null) {
            enteCambio = new CatEnte();
        }
        if (enteCambio.getId() == null) {
            ss.instanciarParametros();
            if (cedula != null && !cedula.isEmpty()) {
                ss.agregarParametro("ciRuc_", cedula);
            }
            showDlg("/resources/dialog/dlglazyente");

        } else {
            JsfUti.update("mainForm:pnlSolicitante");
        }
//        }
    }

    public void selectObjectPapel(SelectEvent event) {
        RegPapel pa = (RegPapel) event.getObject();
        interviniente.setPapel(pa);
    }

    public void liquidacionTemp() {
        try {
            liquidacion.setSolicitante(ente);
            liquidacion.setFechaCreacion(new Date());
            liquidacion.setUserCreacion(us.getUserId());
            liquidacion.setSubTotal(subTotal);
            liquidacion.setValorActos(subTotalDesc);
            liquidacion.setAdicional(recargoAplicado);
            liquidacion.setDescLimitCobro(descLimitCobro);
            liquidacion.setDescuentoValor(descPorLey);
            liquidacion.setDescuentoPorc(porcPago);
            liquidacion.setGastosGenerales(gastosGenerales);
            liquidacion.setTotalPagar(totalPagar);

            if (totalPagar.compareTo(BigDecimal.ZERO) == 0) {
                liquidacion.setGeneraFactura(Boolean.FALSE);
            } else {
                liquidacion.setGeneraFactura(Boolean.TRUE);
            }
            if (liquidacion.getRegpLiquidacionDetallesCollection() != null) {
                liquidacion.getRegpLiquidacionDetallesCollection().clear();
            } else {
                liquidacion.setRegpLiquidacionDetallesCollection(new ArrayList());
            }
            liquidacion.setRegpLiquidacionDetallesCollection(actosPorPagar);

            liquidacion.setPagoFinal(liquidacion.getTotalPagar());
            modelPago = new PagoModel();
            modelPago.setValorLimite(liquidacion.getTotalPagar());
            modelPago.setValorRecibido(liquidacion.getTotalPagar());
            modelPago.setValorCobrar(liquidacion.getTotalPagar());
            modelPago.setValorTotalEfectivo(liquidacion.getTotalPagar());
            modelPago.calcularTotalPago();
            JsfUti.update("formProcesar");
            JsfUti.executeJS("PF('dlgProcesar').show();");

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelarLiquidacion() {
        try {
            if (obs != null && !obs.isEmpty()) {
                if (modelPago.getValorTotal().compareTo(liquidacion.getTotalPagar()) == 0) {
                    if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                        factura = itl.emitirFacturaSinTramiteSinGuardarRenLiquidacionSoloRenPago(liquidacion, modelPago.realizarPago(liquidacion), cajero, obs);
                        if (factura != null) {
                            this.generarComprobante();
                        } else {
                            JsfUti.messageWarning(null, "Ocurrio un error al generar la Factura", "");
                        }
                    } else {
                        JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                    }
                } else {
                    JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor ni menor al de la proforma.");
                }
            } else {
                JsfUti.messageWarning(null, "Ingrese una Observación", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobante() {
        try {
            if (factura.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                if (liquidacion.getGeneraFactura()) {
                    ss.setNombreReporte("comprobante_factura_sin_tramite");
                } else {
                    ss.setNombreReporte("comprobante_factura_exonerada_sin_tramite");
                }

                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", factura.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("LOGO_URL", JsfUti.getRealPath("/resources/image/logo_comprobante.jpg"));
                //ss.agregarParametro("FOOTER", comprobante.getValor());
                //ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
                //JsfUti.redirectNewTab(SisVars.urlServidorPublica + "/Documento");
                List<String> urlList = new ArrayList<>();
                String url = "/procesos/tesoreria/alcancePagos.xhtml";
                urlList.add(SisVars.urlbase + "Documento");
                JsfUti.redirectMultipleConIP_V2(url, urlList);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobante(Long idfactura) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("ingreso");
            ss.setNombreReporte("comprobante_factura_sin_tramite");
            ss.agregarParametro("ID_LIQUIDACION", idfactura);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadFactura(RenFactura re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() != null && re.getCodigoComprobante() != null) {
                ruta = Constantes.rutaFeOld + "factura_" + re.getCodigoComprobante().replace("-", "").trim() + ".pdf";
                System.out.println("ruta " + ruta);
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "El comprobante electronico aun no esta autorizada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reenvioEmision(RenFactura nc) {
        try {
            if (nc.getEstadoWs() != null) {
                if (!nc.getEstadoWs().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                    reenvio(nc);
                } else {
                    JsfUti.messageWarning(null, "El comprobante electronico ya tiene estado AUTORIZADO.", "");
                }
            } else {
                reenvio(nc);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void reenvio(RenFactura renFactura) {
        List<RegpLiquidacionDetalles> actos = new ArrayList();
        RegpLiquidacionDetalles detalle;

        map = new HashMap();
        map.put("pago", renFactura.getPago());

        List<RenPagoRubro> pagoRubros = (List<RenPagoRubro>) em.findObjectByParameterList(RenPagoRubro.class, map);
        System.out.println("pagoRubros " + pagoRubros.size());

        for (RenPagoRubro rdr : pagoRubros) {
            detalle = new RegpLiquidacionDetalles();
            detalle.setActo(rdr.getRubro());
            detalle.setValorTotal(rdr.getValor());
            detalle.setValorUnitario(rdr.getValor());
            detalle.setRecargo(BigDecimal.ZERO);
            detalle.setDescuento(BigDecimal.ZERO);
            detalle.setCantidad(1);
            actos.add(detalle);
        }

        renFactura.setLiquidacionDetalles(actos);
        if (fac.reenviarFacturaElectronicaSinTramite(renFactura, renFactura.getCaja())) {
            emisiones = new RenFacturaLazy();
            JsfUti.messageInfo(null, "Se realizo el reenvio de la Factura.", "");
        } else {
            JsfUti.messageWarning(null, "Problemas en el reenvio de la Factura.", "");
        }
    }

    public void aniosCalculo() {
        aniosDiferencia = 0;
        if (rld.getAnioUltimaTrasnferencia() != null && rld.getAnioAntecedenteSolicitado() != null) {
            aniosDiferencia = rld.getAnioUltimaTrasnferencia() - rld.getAnioAntecedenteSolicitado();
            if (aniosDiferencia < 0) {
                JsfUti.messageWarning(null, "El año de la ultima transferencia no puede ser menor al del movimiento solicitado.", "");
                aniosDiferencia = 0;
                return;
            }
            if (aniosDiferencia - 15 >= 0) {
                aniosDiferencia = aniosDiferencia - 15;
            } else {
                aniosDiferencia = 0;
            }
        }

    }

    public void agregarSolicitanteInterviniente() {
        if (agregaSolicitante) {
            if (ente != null && ente.getId() != null) {
                indexSolicitante = agregarIntereniente(ente);
                if (indexSolicitante == null) {
                    agregaSolicitante = Boolean.FALSE;
                }
            } else {
                JsfUti.messageWarning(null, "Debe Agregar un Solicitante", "");
                agregaSolicitante = Boolean.FALSE;
            }
        } else {
            if (indexSolicitante != null) {
                listInterv.remove(indexSolicitante.intValue());
            }
        }
    }

    public void selectInterv(SelectEvent event) {
        CatEnte ente = (CatEnte) event.getObject();
        agregarIntereniente(ente);
    }

    private Integer agregarIntereniente(CatEnte e) {
        if (this.validaInterviniente(e.getCiRuc())) {
            RegpIntervinientes in = new RegpIntervinientes();
            in.setEnte(e);
            listInterv.add(in);
            return listInterv.size() - 1;
        } else {
            JsfUti.messageWarning(null, "Ya esta ingresado el interviniente.", "");
            return null;
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

    public void showDlgTipoIngreso() {
        try {
            if (cajero != null) {

            } else {
                JsfUti.messageWarning(null, "Usuario no tiene Cajero asignado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaInterviniente(String cedula) {
        return listInterv.stream().noneMatch((r) -> (r.getEnte().getCiRuc().equalsIgnoreCase(cedula)));
    }

    public void generarReporteFacturacionElectronica(String ruta) {
        try {
            ss.instanciarParametros();
            ss.setContentType("application/pdf");
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab("/sgr/DownLoadFiles");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void xmlComprobanteSRI(String ruta) {
        ss.instanciarParametros();
        ss.setContentType("application/xml");
        ss.setNombreDocumento(ruta);
        JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
    }

    public void downloadNotaCredito(RenNotaCredito nc) {
        String ruta;
        try {
            if (nc.getNumeroAutorizacionModifica() != null && nc.getNumeroDocumento() != null) {
                ruta = Constantes.rutaFeOld + "notacredito_" + nc.getNumeroDocumento().replace("-", "").trim() + ".pdf";
                System.out.println("ruta " + ruta);
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "El comprobante electronico aun no esta autorizada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reenvioNotaCredito(RenNotaCredito nc) {
        try {
            if (nc.getEstado() != null) {
                if (!nc.getEstado().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                    reenviarNC(nc);
                } else {
                    JsfUti.messageWarning(null, "El comprobante electronico ya tiene estado AUTORIZADO.", "");
                }
            } else {
                reenviarNC(nc);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void reenviarNC(RenNotaCredito nc) {
        if (fac.emitirNotaCredito(nc)) {
            lazy = new RenNotaCreditoLazy();
            JsfUti.update("mainForm");
            JsfUti.messageInfo(null, "Se realizo el reenvio de la Nota de Credito.", "");
        } else {
            JsfUti.messageWarning(null, "Problemas en el reenvio de la Nota de Credito.", "");
        }
    }

    public void printTituloCredito() {
        try {
            if (titulocredito == null || titulocredito <= 0) {
                JsfUti.messageWarning(null, "Nro de título debe ser mayor a cero.", "");
                return;
            }

            map = new HashMap();
            map.put("tituloCredito", titulocredito);
            RegpLiquidacion liq = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            if (liq != null) {
                JsfUti.messageWarning(null, "Nro de título ya fue generado para el trámite " + liq.getNumTramiteRp(), "");
                return;
            }

            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("titulo_credito_sin_generar");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("NRO_TITULO", titulocredito);
            ss.agregarParametro("FECHA_EMISION", ingreso);
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
            ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void revisarTramite() {
        try {

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public List<RegpLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<RegpLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<RegpLiquidacion> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<RegpLiquidacion> facturas) {
        this.facturas = facturas;
    }

    public List<AclUser> getCajeros() {
        return cajeros;
    }

    public void setCajeros(List<AclUser> cajeros) {
        this.cajeros = cajeros;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public AclUser getCaja() {
        return caja;
    }

    public void setCaja(AclUser caja) {
        this.caja = caja;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Integer getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(Integer tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public RegpLiquidacion getProforma() {
        return proforma;
    }

    public void setProforma(RegpLiquidacion proforma) {
        this.proforma = proforma;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public Long getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(Long numerotramite) {
        this.numerotramite = numerotramite;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public RenFacturaLazy getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(RenFacturaLazy emisiones) {
        this.emisiones = emisiones;
    }

    public RegActoLazy getActos() {
        return actos;
    }

    public void setActos(RegActoLazy actos) {
        this.actos = actos;
    }

    public RenCajero getCajero() {
        return cajero;
    }

    public void setCajero(RenCajero cajero) {
        this.cajero = cajero;
    }

    public List<RegpIntervinientes> getListInterv() {
        return listInterv;
    }

    public void setListInterv(List<RegpIntervinientes> listInterv) {
        this.listInterv = listInterv;
    }

    public Integer getAniosDiferencia() {
        return aniosDiferencia;
    }

    public void setAniosDiferencia(Integer aniosDiferencia) {
        this.aniosDiferencia = aniosDiferencia;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public RegpLiquidacionDetalles getRld() {
        return rld;
    }

    public void setRld(RegpLiquidacionDetalles rld) {
        this.rld = rld;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public Integer getTipocalculo() {
        return tipocalculo;
    }

    public void setTipocalculo(Integer tipocalculo) {
        this.tipocalculo = tipocalculo;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
    }

    public CtlgItem getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(CtlgItem usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    public CtlgItem getNuevoUsoDoc() {
        return nuevoUsoDoc;
    }

    public void setNuevoUsoDoc(CtlgItem nuevoUsoDoc) {
        this.nuevoUsoDoc = nuevoUsoDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObsAdicional() {
        return obsAdicional;
    }

    public void setObsAdicional(String obsAdicional) {
        this.obsAdicional = obsAdicional;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getSubTotalDesc() {
        return subTotalDesc;
    }

    public void setSubTotalDesc(BigDecimal subTotalDesc) {
        this.subTotalDesc = subTotalDesc;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getDescPorLey() {
        return descPorLey;
    }

    public void setDescPorLey(BigDecimal descPorLey) {
        this.descPorLey = descPorLey;
    }

    public BigDecimal getRecargoAplicado() {
        return recargoAplicado;
    }

    public void setRecargoAplicado(BigDecimal recargoAplicado) {
        this.recargoAplicado = recargoAplicado;
    }

    public BigDecimal getDescLimitCobro() {
        return descLimitCobro;
    }

    public void setDescLimitCobro(BigDecimal descLimitCobro) {
        this.descLimitCobro = descLimitCobro;
    }

    public BigDecimal getGastosGenerales() {
        return gastosGenerales;
    }

    public void setGastosGenerales(BigDecimal gastosGenerales) {
        this.gastosGenerales = gastosGenerales;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public BigDecimal getPorcPago() {
        return porcPago;
    }

    public void setPorcPago(BigDecimal porcPago) {
        this.porcPago = porcPago;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public Boolean getAgregaBeneficiario() {
        return agregaBeneficiario;
    }

    public void setAgregaBeneficiario(Boolean agregaBeneficiario) {
        this.agregaBeneficiario = agregaBeneficiario;
    }

    public Boolean getAgregaSolicitante() {
        return agregaSolicitante;
    }

    public void setAgregaSolicitante(Boolean agregaSolicitante) {
        this.agregaSolicitante = agregaSolicitante;
    }

    public RegpIntervinientes getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(RegpIntervinientes interviniente) {
        this.interviniente = interviniente;
    }

    public Boolean getBeneficiarioEsSolicitante() {
        return beneficiarioEsSolicitante;
    }

    public void setBeneficiarioEsSolicitante(Boolean beneficiarioEsSolicitante) {
        this.beneficiarioEsSolicitante = beneficiarioEsSolicitante;
    }

    public Integer getIndexBeneficiario() {
        return indexBeneficiario;
    }

    public void setIndexBeneficiario(Integer indexBeneficiario) {
        this.indexBeneficiario = indexBeneficiario;
    }

    public Integer getIndexSolicitante() {
        return indexSolicitante;
    }

    public void setIndexSolicitante(Integer indexSolicitante) {
        this.indexSolicitante = indexSolicitante;
    }

    public List<RegpLiquidacionDetalles> getActosPorPagar() {
        return actosPorPagar;
    }

    public void setActosPorPagar(List<RegpLiquidacionDetalles> actosPorPagar) {
        this.actosPorPagar = actosPorPagar;
    }

    public int getIndiceActo() {
        return indiceActo;
    }

    public void setIndiceActo(int indiceActo) {
        this.indiceActo = indiceActo;
    }

    public BigDecimal getAdicional() {
        return adicional;
    }

    public void setAdicional(BigDecimal adicional) {
        this.adicional = adicional;
    }

    public List<CtlgItem> getUsosDocumentos() {
        return em.findAllEntCopy(Querys.getCtlgItemListUsosDocs);
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public PagoModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoModel modelPago) {
        this.modelPago = modelPago;
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

    public boolean isDiferenciaPagos() {
        return diferenciaPagos;
    }

    public void setDiferenciaPagos(boolean diferenciaPagos) {
        this.diferenciaPagos = diferenciaPagos;
    }

    public BigDecimal getValorDiferenciaActo() {
        return valorDiferenciaActo;
    }

    public void setValorDiferenciaActo(BigDecimal valorDiferenciaActo) {
        this.valorDiferenciaActo = valorDiferenciaActo;
    }

    public List<ComprobanteSRI> getComprobantesElectronicos() {
        return comprobantesElectronicos;
    }

    public void setComprobantesElectronicos(List<ComprobanteSRI> comprobantesElectronicos) {
        this.comprobantesElectronicos = comprobantesElectronicos;
    }

    public ComprobanteSRI getComprobanteSRI() {
        return comprobanteSRI;
    }

    public void setComprobanteSRI(ComprobanteSRI comprobanteSRI) {
        this.comprobanteSRI = comprobanteSRI;
    }

    public RenNotaCreditoLazy getLazy() {
        return lazy;
    }

    public void setLazy(RenNotaCreditoLazy lazy) {
        this.lazy = lazy;
    }

    public Boolean getFacturaSinTramite() {
        return facturaSinTramite;
    }

    public void setFacturaSinTramite(Boolean facturaSinTramite) {
        this.facturaSinTramite = facturaSinTramite;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public RenPago getRenPago() {
        return renPago;
    }

    public void setRenPago(RenPago renPago) {
        this.renPago = renPago;
    }

    public DetalleProceso getProceso() {
        return proceso;
    }

    public void setProceso(DetalleProceso proceso) {
        this.proceso = proceso;
    }

    public ProcessInstanceLazy getDetails() {
        return details;
    }

    public void setDetails(ProcessInstanceLazy details) {
        this.details = details;
    }

    public HistoricoTramites getProcesoBorrar() {
        return procesoBorrar;
    }

    public void setProcesoBorrar(HistoricoTramites procesoBorrar) {
        this.procesoBorrar = procesoBorrar;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

    public List<RenPagoDetalle> getPagodetalle() {
        return pagodetalle;
    }

    public void setPagodetalle(List<RenPagoDetalle> pagodetalle) {
        this.pagodetalle = pagodetalle;
    }

    public String getNumeroNotaCredito() {
        return numeroNotaCredito;
    }

    public void setNumeroNotaCredito(String numeroNotaCredito) {
        this.numeroNotaCredito = numeroNotaCredito;
    }

    public RenEntidadBancaria getBancoSelect() {
        return bancoSelect;
    }

    public void setBancoSelect(RenEntidadBancaria bancoSelect) {
        this.bancoSelect = bancoSelect;
    }

    public String getChNumeroCheque() {
        return chNumeroCheque;
    }

    public void setChNumeroCheque(String chNumeroCheque) {
        this.chNumeroCheque = chNumeroCheque;
    }

    public String getChNumeroCuenta() {
        return chNumeroCuenta;
    }

    public void setChNumeroCuenta(String chNumeroCuenta) {
        this.chNumeroCuenta = chNumeroCuenta;
    }

    public RenEntidadBancaria getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(RenEntidadBancaria tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getTrNumTransferencia() {
        return trNumTransferencia;
    }

    public void setTrNumTransferencia(String trNumTransferencia) {
        this.trNumTransferencia = trNumTransferencia;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBaucher() {
        return baucher;
    }

    public void setBaucher(String baucher) {
        this.baucher = baucher;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public RenPagoDetalle getDetalleNew() {
        return detalleNew;
    }

    public void setDetalleNew(RenPagoDetalle detalleNew) {
        this.detalleNew = detalleNew;
    }

    public Integer getTipocobro() {
        return tipocobro;
    }

    public void setTipocobro(Integer tipocobro) {
        this.tipocobro = tipocobro;
    }

    public List<RenPagoDetalle> getListaPagosNew() {
        return listaPagosNew;
    }

    public void setListaPagosNew(List<RenPagoDetalle> listaPagosNew) {
        this.listaPagosNew = listaPagosNew;
    }

    public BigDecimal getValorDetaleNew() {
        return valorDetaleNew;
    }

    public void setValorDetaleNew(BigDecimal valorDetaleNew) {
        this.valorDetaleNew = valorDetaleNew;
    }

    public BigDecimal getValorTotalListTipoPagos() {
        return valorTotalListTipoPagos;
    }

    public void setValorTotalListTipoPagos(BigDecimal valorTotalListTipoPagos) {
        this.valorTotalListTipoPagos = valorTotalListTipoPagos;
    }

    public CatEnte getEnteCambio() {
        return enteCambio;
    }

    public void setEnteCambio(CatEnte enteCambio) {
        this.enteCambio = enteCambio;
    }

    public Long getTitulocredito() {
        return titulocredito;
    }

    public void setTitulocredito(Long titulocredito) {
        this.titulocredito = titulocredito;
    }

}
