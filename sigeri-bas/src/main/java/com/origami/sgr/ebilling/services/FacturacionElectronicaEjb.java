/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.services;

import com.google.gson.Gson;
import com.origami.config.SisVars;
import com.origami.sgr.ebilling.interfaces.FacturacionElectronicaLocal;
import com.origami.sgr.ebilling.models.Cabecera;
import com.origami.sgr.ebilling.models.ComprobanteElectronico;
import com.origami.sgr.ebilling.models.ComprobanteSRI;
import com.origami.sgr.ebilling.models.Detalle;
import com.origami.sgr.ebilling.models.DetallePago;
import com.origami.sgr.ebilling.models.Detalles;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenDatosFacturaElectronica;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenNotaCredito;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.Constantes;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.UnsupportedCharsetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author andysanchez
 */
@Singleton(name = "facturacion")
@Lock(LockType.READ)
@ApplicationScoped
public class FacturacionElectronicaEjb implements FacturacionElectronicaLocal {

    @Inject
    private Entitymanager em;

    @Inject
    private IngresoTramiteLocal itl;

    protected static final String FACTURA = "01";
    protected static final String NOTACREDITO = "04";
    protected static final String NOTADEBITO = "05";
    protected static final String GUIAREMISION = "06";
    protected static final String COMPPROBANTERETENCION = "07";

    private SimpleDateFormat formatComprobante;
    private ComprobanteElectronico comprobanteElectronico;

    private final Client client = ClientBuilder.newClient();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(FacturacionElectronicaEjb.class.getName());

    protected static String consultaFacturasContribuyente = "consultaFacturasContribuyente/";
    protected static String enviarCorreoFacturacionElectronica = "enviarCorreoFacturacionElectronica/";
    protected static String enviarFacturacionElectronica = "enviarFacturacionElectronica/";
    protected static String reenviarFacturacionElectronica = "reenviarFacturacionElectronica/";
    protected static String enviarNotaCreditoElectronica = "enviarNotaCreditoElectronica/";
    protected static String enviarRenFacturacionElectronica = "enviarRenFacturacionElectronica/";
    protected static String reenviarRenFacturacionElectronica = "reenviarRenFacturacionElectronica/";

    @Override
    public List<ComprobanteSRI> getAllComprobanteByCedula(String cedula) {
        try {
            List<ComprobanteSRI> comprobanteSRIList = client.target(SisVars.urlWsFacturacion + consultaFacturasContribuyente + cedula)
                    .request(MediaType.APPLICATION_JSON).get(new GenericType<List<ComprobanteSRI>>() {
            });
            return comprobanteSRIList;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    @Override
    public void reenviarCorreoFacturaElectronicaSRI(ComprobanteSRI comprobanteSRI) {
        try {

            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            // HttpPost httpPost = new HttpPost("http://127.0.0.1:8780/api/facturacionElectronica/enviarFacturacionElectronica/");
            HttpPost httpPost = new HttpPost(SisVars.urlWsFacturacion + enviarCorreoFacturacionElectronica);
            httpPost.setEntity(new StringEntity(gson.toJson(comprobanteSRI), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpClient.execute(httpPost);

        } catch (IOException | UnsupportedCharsetException ex) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Boolean emitirFacturaElectronica(RegpLiquidacion liquidacion, RenCajero cajero) {

        /*Map<String, Object> map = new HashMap();
        map.put("code", Variables.datosFacturaElectronica);
        RenDatosFacturaElectronica dfe = em.findObjectByParameter(RenDatosFacturaElectronica.class, map);*/
        //LLENADO DEL MODELO DE DATOS PARA LA FACTURACION
        comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setIdLiquidacion(liquidacion.getId());
        comprobanteElectronico.setTipoLiquidacionSGR("RL");
        comprobanteElectronico.setTramite(liquidacion.getNumTramiteRp());
        comprobanteElectronico.setIsOnline(SisVars.isOnline);
        comprobanteElectronico.setAmbiente(SisVars.ambienteFacturacion);
        comprobanteElectronico.setPuntoEmision(cajero.getCajero().getPuntoEmision());
        //comprobanteElectronico.setRucEntidad(dfe.getRuc());
        comprobanteElectronico.setRucEntidad(cajero.getCajero().getEntidad().getRucEntidad());
        comprobanteElectronico.setComprobanteCodigo(SisVars.FACTURA); //Factura

        //para los reenvios =o =/
        if (liquidacion.getClaveAcceso() != null) {
            comprobanteElectronico.setClaveAcceso(liquidacion.getClaveAcceso());
        }
        if (liquidacion.getNumeroComprobante() != null) {
            if (liquidacion.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                comprobanteElectronico.setNumComprobante(liquidacion.getNumeroComprobante().toString());
            }
        }

        comprobanteElectronico = this.llenarInfoFactura(comprobanteElectronico, liquidacion, itl.getFormasPagoFe(liquidacion));

        try {
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsFacturacion + enviarFacturacionElectronica);
            httpPost.setEntity(new StringEntity(gson.toJson(comprobanteElectronico), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpClient.execute(httpPost);
            return true;
        } catch (IOException | UnsupportedCharsetException ex) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    @Override
    public Boolean reenviarFacturaElectronica(RegpLiquidacion liquidacion, RenCajero cajero, Boolean reenvioVerificacion) {
        //LLENADO DEL MODELO DE DATOS PARA LA FACTURACION
        comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setIdLiquidacion(liquidacion.getId());
        comprobanteElectronico.setTipoLiquidacionSGR("RL");
        comprobanteElectronico.setTramite(liquidacion.getNumTramiteRp());
        comprobanteElectronico.setIsOnline(SisVars.isOnline);
        comprobanteElectronico.setAmbiente(SisVars.ambienteFacturacion);
        comprobanteElectronico.setPuntoEmision(cajero.getCajero().getPuntoEmision());
        //comprobanteElectronico.setRucEntidad(dfe.getRuc());
        comprobanteElectronico.setRucEntidad(cajero.getCajero().getEntidad().getRucEntidad());
        comprobanteElectronico.setComprobanteCodigo(SisVars.FACTURA); //Factura
        comprobanteElectronico.setReenvioVerificacion(reenvioVerificacion);

        //para los reenvios =o =/
        if (liquidacion.getClaveAcceso() != null) {
            comprobanteElectronico.setClaveAcceso(liquidacion.getClaveAcceso());
        }
        if (liquidacion.getNumeroComprobante() != null) {
            if (liquidacion.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                comprobanteElectronico.setNumComprobante(liquidacion.getNumeroComprobante().toString());
            }
        }

        comprobanteElectronico = this.llenarInfoFactura(comprobanteElectronico, liquidacion, itl.getFormasPagoFe(liquidacion));

        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsFacturacion + reenviarFacturacionElectronica);
            httpPost.setEntity(new StringEntity(gson.toJson(comprobanteElectronico), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");

            Future<HttpResponse> futureResponse = executorService.submit(() -> {
                return httpClient.execute(httpPost);
            });
            futureResponse.get(30, TimeUnit.SECONDS);

            return true;
        } catch (InterruptedException | UnsupportedCharsetException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    @Override
    public Boolean emitirNotaCredito(RenNotaCredito notaCredito) {

        Map<String, Object> map = new HashMap();
        map.put("code", Constantes.datosFacturaElectronica);
        RenDatosFacturaElectronica dfe = em.findObjectByParameter(RenDatosFacturaElectronica.class, map);

        comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setIsOnline(SisVars.isOnline);
        comprobanteElectronico.setAmbiente(SisVars.ambienteFacturacion);
        comprobanteElectronico.setPuntoEmision(notaCredito.getCajaEmision().getCodigoCaja());
        comprobanteElectronico.setRucEntidad(dfe.getRuc());
        comprobanteElectronico.setComprobanteCodigo(NOTACREDITO); //Factura
        comprobanteElectronico.setNumComprobanteModifica(notaCredito.getFactura().getCodigoComprobante().replace("-", "").trim());
        comprobanteElectronico.setMotivoNotaCredito(notaCredito.getMotivo());
        comprobanteElectronico.setTipoDocumentoModifica(FACTURA);

        comprobanteElectronico.setIdLiquidacion(notaCredito.getId());
        comprobanteElectronico.setTipoLiquidacionSGR("RN");
        comprobanteElectronico.setTramite(notaCredito.getFactura().getNumTramiteRp());

        if (notaCredito.getClaveAcceso() != null) {
            comprobanteElectronico.setClaveAcceso(notaCredito.getClaveAcceso());
        }
        if (notaCredito.getCodigoDocumento() != null) {
            comprobanteElectronico.setNumComprobante(notaCredito.getCodigoDocumento().toString());
        }

        comprobanteElectronico = this.llenarInfoNotaCredito(comprobanteElectronico, notaCredito);
        try {
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsFacturacion + enviarNotaCreditoElectronica);
            httpPost.setEntity(new StringEntity(gson.toJson(comprobanteElectronico), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpClient.execute(httpPost);

        } catch (IOException ex) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public Boolean emitirFacturaElectronicaSinTramite(RenFactura renFactura, RenCajero cajero) {
        Map<String, Object> map = new HashMap();
        map.put("code", Constantes.datosFacturaElectronica);
        RenDatosFacturaElectronica dfe = em.findObjectByParameter(RenDatosFacturaElectronica.class, map);

        //LLENADO DEL MODELO DE DATOS PARA LA FACTURACION
        comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setTramite(renFactura.getNumTramite());
        comprobanteElectronico.setIsOnline(SisVars.isOnline);
        comprobanteElectronico.setAmbiente(SisVars.ambienteFacturacion);
        comprobanteElectronico.setPuntoEmision(cajero.getCajero().getPuntoEmision());
        comprobanteElectronico.setRucEntidad(dfe.getRuc());
        comprobanteElectronico.setComprobanteCodigo(SisVars.FACTURA); //Factura

        comprobanteElectronico.setIdLiquidacion(renFactura.getId());
        comprobanteElectronico.setTipoLiquidacionSGR("RF");

        //para los reenvios =o =/
        if (renFactura.getClaveAcceso() != null) {
            comprobanteElectronico.setClaveAcceso(renFactura.getClaveAcceso());
        }
        if (renFactura.getNumeroComprobante() != null) {
            comprobanteElectronico.setNumComprobante(renFactura.getNumeroComprobante().toString());
        }

        comprobanteElectronico = this.llenarInfoRenFactura(comprobanteElectronico, renFactura, itl.getFormasPagoFe(renFactura.getPago()));
        try {
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsFacturacion + enviarRenFacturacionElectronica);
            httpPost.setEntity(new StringEntity(gson.toJson(comprobanteElectronico), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpClient.execute(httpPost);
        } catch (IOException ex) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public Boolean reenviarFacturaElectronicaSinTramite(RenFactura renFactura, RenCajero cajero) {
        Map<String, Object> map = new HashMap();
        map.put("code", Constantes.datosFacturaElectronica);
        RenDatosFacturaElectronica dfe = em.findObjectByParameter(RenDatosFacturaElectronica.class, map);

        //LLENADO DEL MODELO DE DATOS PARA LA FACTURACION
        comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setTramite(renFactura.getNumTramite());
        comprobanteElectronico.setIsOnline(SisVars.isOnline);
        comprobanteElectronico.setAmbiente(SisVars.ambienteFacturacion);
        comprobanteElectronico.setPuntoEmision(cajero.getCajero().getPuntoEmision());
        comprobanteElectronico.setRucEntidad(dfe.getRuc());
        comprobanteElectronico.setComprobanteCodigo(SisVars.FACTURA); //Factura

        comprobanteElectronico.setIdLiquidacion(renFactura.getId());
        comprobanteElectronico.setTipoLiquidacionSGR("RF");

        //para los reenvios =o =/
        if (renFactura.getClaveAcceso() != null) {
            comprobanteElectronico.setClaveAcceso(renFactura.getClaveAcceso());
        }
        if (renFactura.getNumeroComprobante() != null) {
            comprobanteElectronico.setNumComprobante(renFactura.getNumeroComprobante().toString());
        }

        comprobanteElectronico = this.llenarInfoRenFactura(comprobanteElectronico, renFactura, itl.getFormasPagoFe(renFactura.getPago()));

        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlWsFacturacion + reenviarRenFacturacionElectronica);
            httpPost.setEntity(new StringEntity(gson.toJson(comprobanteElectronico), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            Future<HttpResponse> futureResponse = executorService.submit(() -> {
                return httpClient.execute(httpPost);
            });
            futureResponse.get(30, TimeUnit.SECONDS);

            return true;
        } catch (InterruptedException | UnsupportedCharsetException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    public ComprobanteElectronico llenarInfoFactura(ComprobanteElectronico ce, RegpLiquidacion liq,
            HashMap<String, Object> formasPago) {
        formatComprobante = new SimpleDateFormat("yyyy-MM-dd");
        Detalle detalle;
        List<Detalle> detalles = new ArrayList<>();
        DetallePago pago;
        List<DetallePago> pagos = new ArrayList<>();
        Cabecera cab = new Cabecera();
        try {
            cab.setFechaEmision(formatComprobante.format(liq.getFechaIngreso()));

            if (liq.getBeneficiario() == null) {
                cab.setCedulaRuc(liq.getSolicitante().getCiRuc());
                cab.setEsPasaporte(!liq.getSolicitante().getTipoIdentificacion().equals("P") ? Boolean.FALSE : Boolean.TRUE);
                cab.setPropietario(liq.getSolicitante().getNombreCompleto().toUpperCase().replaceAll("&", "Y"));
                if (liq.getSolicitante().getDireccion() != null) {
                    cab.setDireccion(liq.getSolicitante().getDireccion().toUpperCase());
                } else {
                    cab.setDireccion("SIN INFORMACIÓN");
                }
                if (liq.getSolicitante().getTelefono1() != null) {
                    cab.setTelefono(liq.getSolicitante().getTelefono1());
                } else {
                    cab.setTelefono("SIN INFORMACIÓN");
                }
                if (liq.getSolicitante().getCorreo1() != null) {
                    cab.setCorreo(liq.getEmailTemp());
                } else {
                    cab.setCorreo("SIN INFORMACIÓN");
                }
            } else {
                cab.setCedulaRuc(liq.getBeneficiario().getCiRuc());
                cab.setEsPasaporte(!liq.getBeneficiario().getTipoIdentificacion().equals("P") ? Boolean.FALSE : Boolean.TRUE);
                cab.setPropietario(liq.getBeneficiario().getNombreCompleto().toUpperCase().replaceAll("&", "Y"));
                if (liq.getBeneficiario().getDireccion() != null) {
                    cab.setDireccion(liq.getBeneficiario().getDireccion().toUpperCase());
                } else {
                    cab.setDireccion("SIN INFORMACIÓN");
                }
                if (liq.getBeneficiario().getTelefono1() != null) {
                    cab.setTelefono(liq.getBeneficiario().getTelefono1());
                } else {
                    cab.setTelefono("SIN INFORMACIÓN");
                }
                if (liq.getBeneficiario().getCorreo1() != null) {
                    cab.setCorreo(liq.getBeneficiario().getCorreo1());
                } else {
                    cab.setCorreo("SIN INFORMACIÓN");
                }
            }

            for (Map.Entry<String, Object> entrySet : formasPago.entrySet()) {
                pago = new DetallePago();
                pago.setFormaPago(entrySet.getKey());
                BigDecimal val = (BigDecimal) entrySet.getValue();
                pago.setTotal(val.doubleValue());
                pagos.add(pago);
            }

            for (RegpLiquidacionDetalles det : liq.getRegpLiquidacionDetallesCollection()) {
                detalle = new Detalle();
                detalle.setCodigoPrincipal(det.getActo().getId().toString());
                detalle.setDescripcion(det.getActo().getNombre());
                detalle.setValorTotal(det.getValorTotal().doubleValue());
                detalle.setValorUnitario(det.getValorUnitario().doubleValue());
                if (det.getRecargo() != null) {
                    detalle.setRecargo(det.getRecargo().doubleValue());
                } else {
                    detalle.setRecargo(0.0);
                }
                detalle.setDescuento(det.getDescuento().doubleValue());
                detalle.setCantidad(det.getCantidad());
                detalle.setIva(0.00);
                //detalle.setCodigoTarifa("7");
                //detalle.setCodigoTarifa("6");
                detalle.setCodigoTarifa("0");
                detalles.add(detalle);
            }

            ce.setCabecera(cab);
            ce.setDetallePagos(pagos);
            ce.setDetalles(new Detalles());
            ce.getDetalles().setDetalle(detalles);

            BigDecimal temp = liq.getDescLimitCobro().add(liq.getGastosGenerales());
            ce.setDescuentoAdicional(temp);

            return ce;
        } catch (Exception e) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public ComprobanteElectronico llenarInfoRenFactura(ComprobanteElectronico ce, RenFactura renFactura,
            HashMap<String, Object> formasPago) {
        formatComprobante = new SimpleDateFormat("yyyy-MM-dd");
        Detalle detalle;
        List<Detalle> detalles = new ArrayList<>();
        DetallePago pago;
        List<DetallePago> pagos = new ArrayList<>();
        Cabecera cab = new Cabecera();
        try {
            cab.setFechaEmision(formatComprobante.format(renFactura.getFecha()));
            cab.setCedulaRuc(renFactura.getSolicitante().getCiRuc());
            cab.setEsPasaporte(!renFactura.getSolicitante().getTipoIdentificacion().equals("P") ? Boolean.FALSE : Boolean.TRUE);
            cab.setPropietario(renFactura.getSolicitante().getNombreCompleto().toUpperCase().replaceAll("&", "Y"));
            if (renFactura.getSolicitante().getDireccion() != null) {
                cab.setDireccion(renFactura.getSolicitante().getDireccion().toUpperCase());
            } else {
                cab.setDireccion("SIN INFORMACIÓN");
            }
            if (renFactura.getSolicitante().getTelefono1() != null) {
                cab.setTelefono(renFactura.getSolicitante().getTelefono1());
            } else {
                cab.setTelefono("SIN INFORMACIÓN");
            }
            if (renFactura.getSolicitante().getCorreo1() != null) {
                cab.setCorreo(renFactura.getSolicitante().getCorreo1());
            } else {
                cab.setCorreo("SIN INFORMACIÓN");
            }

            for (Map.Entry<String, Object> entrySet : formasPago.entrySet()) {
                pago = new DetallePago();
                pago.setFormaPago(entrySet.getKey());
                BigDecimal val = (BigDecimal) entrySet.getValue();
                pago.setTotal(val.doubleValue());
                pagos.add(pago);
            }

            for (RegpLiquidacionDetalles det : renFactura.getLiquidacionDetalles()) {
                detalle = new Detalle();
                detalle.setCodigoPrincipal(det.getActo().getId().toString());
                detalle.setDescripcion(det.getActo().getNombre());
                detalle.setValorTotal(det.getValorTotal().doubleValue());
                detalle.setValorUnitario(det.getValorUnitario().doubleValue());
                detalle.setRecargo(det.getRecargo().doubleValue());
                detalle.setDescuento(det.getDescuento().doubleValue());
                detalle.setCantidad(det.getCantidad());
                detalle.setIva(0.00);
                //detalle.setCodigoTarifa("7");
                //detalle.setCodigoTarifa("6");
                detalle.setCodigoTarifa("0");
                detalles.add(detalle);
            }

            ce.setCabecera(cab);
            ce.setDetallePagos(pagos);
            ce.setDetalles(new Detalles());
            ce.getDetalles().setDetalle(detalles);

            ce.setDescuentoAdicional(BigDecimal.ZERO);

            return ce;
        } catch (Exception e) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public ComprobanteElectronico llenarInfoNotaCredito(ComprobanteElectronico ce, RenNotaCredito notaCredito) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RegpLiquidacion liq = notaCredito.getFactura();
        ce.setFechaEmisionDocumentoModifica(sdf.format(notaCredito.getFactura().getFechaAutorizacion()));
        Detalle detalle;
        List<Detalle> detalles = new ArrayList<>();
        List<DetallePago> pagos = new ArrayList<>();
        Cabecera cab = new Cabecera();
        try {
            cab.setFechaEmision(sdf.format(notaCredito.getFechaEmision()));
            if (liq.getBeneficiario() == null) {
                cab.setCedulaRuc(liq.getSolicitante().getCiRuc());
                cab.setEsPasaporte(!liq.getSolicitante().getTipoIdentificacion().equals("P") ? Boolean.FALSE : Boolean.TRUE);
                cab.setPropietario(liq.getSolicitante().getNombreCompleto().toUpperCase().replaceAll("&", "Y"));
                if (liq.getSolicitante().getDireccion() != null) {
                    cab.setDireccion(liq.getSolicitante().getDireccion().toUpperCase());
                } else {
                    cab.setDireccion("SIN INFORMACIÓN");
                }
                if (liq.getSolicitante().getTelefono1() != null) {
                    cab.setTelefono(liq.getSolicitante().getTelefono1());
                } else {
                    cab.setTelefono("SIN INFORMACIÓN");
                }
                if (liq.getSolicitante().getCorreo1() != null) {
                    cab.setCorreo(liq.getSolicitante().getCorreo1());
                } else {
                    cab.setCorreo("SIN INFORMACIÓN");
                }
            } else {
                cab.setCedulaRuc(liq.getBeneficiario().getCiRuc());
                cab.setEsPasaporte(!liq.getBeneficiario().getTipoIdentificacion().equals("P") ? Boolean.FALSE : Boolean.TRUE);
                cab.setPropietario(liq.getBeneficiario().getNombreCompleto().toUpperCase().replaceAll("&", "Y"));
                if (liq.getBeneficiario().getDireccion() != null) {
                    cab.setDireccion(liq.getBeneficiario().getDireccion().toUpperCase());
                } else {
                    cab.setDireccion("SIN INFORMACIÓN");
                }
                if (liq.getBeneficiario().getTelefono1() != null) {
                    cab.setTelefono(liq.getBeneficiario().getTelefono1());
                } else {
                    cab.setTelefono("SIN INFORMACIÓN");
                }
                if (liq.getBeneficiario().getCorreo1() != null) {
                    cab.setCorreo(liq.getBeneficiario().getCorreo1());
                } else {
                    cab.setCorreo("SIN INFORMACIÓN");
                }
            }

            detalle = new Detalle();
            detalle.setDescripcion("FACTURA " + liq.getCodigoComprobante());
            detalle.setValorTotal(notaCredito.getValorTotal().doubleValue());
            detalle.setValorUnitario(notaCredito.getValorTotal().doubleValue());
            detalle.setRecargo(0.0);
            detalle.setDescuento(0.0);
            detalle.setCantidad(1);
            detalle.setIva(0.00);
            //detalle.setCodigoTarifa("7");
            //detalle.setCodigoTarifa("6");
            detalle.setCodigoTarifa("0");
            detalles.add(detalle);

            ce.setCabecera(cab);
            ce.setDetallePagos(pagos);
            ce.setDetalles(new Detalles());
            ce.getDetalles().setDetalle(detalles);

            return ce;
        } catch (Exception e) {
            Logger.getLogger(FacturacionElectronicaEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

}
