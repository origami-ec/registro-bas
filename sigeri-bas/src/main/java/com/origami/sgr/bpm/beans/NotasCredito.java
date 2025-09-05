/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenNotaCredito;
import com.origami.sgr.lazymodels.RenNotaCreditoLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Constantes;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.ebilling.interfaces.FacturacionElectronicaLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class NotasCredito implements Serializable {

    private static final Logger LOG = Logger.getLogger(NotasCredito.class.getName());

    @EJB
    protected Entitymanager em;
    @EJB(beanName = "ingreso")
    private IngresoTramiteLocal itl;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;

    protected RegpLiquidacion proforma;
    protected RenNotaCreditoLazy lazy;
    protected RenNotaCredito nc;
    protected RenCajero cajero;
    protected String motivo;
    protected Boolean render = false;
    protected Long tramite;
    protected Map map;
    protected BigDecimal valor;

    @PostConstruct
    protected void iniView() {
        try {
            lazy = new RenNotaCreditoLazy();
            nc = new RenNotaCredito();
            proforma = new RegpLiquidacion();
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            cajero = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
            valor = BigDecimal.ZERO;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarTramite() {
        try {
            if (tramite != null) {
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                proforma = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
                if (proforma == null) {
                    proforma = new RegpLiquidacion();
                    JsfUti.messageWarning(null, "No se encuentra el Tramite.", "");
                } else {
                    render = true;
                    nc.setClaveAccesoModifica(proforma.getClaveAcceso());
                    nc.setNumeroAutorizacionModifica(proforma.getNumeroAutorizacion());
                    nc.setFechaFactura(proforma.getFechaAutorizacion());
                    nc.setNumeroComprobanteModifica(proforma.getCodigoComprobante());
                    nc.setBeneficiario(proforma.getBeneficiario());
                    JsfUti.update("mainForm");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de Tramite.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void emitirNotaCredito() {
        try {

            if (motivo == null || motivo.isEmpty()) {
                JsfUti.messageWarning(null, "Debe ingresar el motivo.", "");
                return;
            }
            if (nc.getNumeroAutorizacionModifica() == null) {
                JsfUti.messageWarning(null, "La factura debe tener NUMERO DE AUTORIZACION.", "");
            } else {

                BigDecimal total = itl.getSumaValorNotaCredito(proforma.getNumTramiteRp());
                if (total == null || valor == null || (valor.compareTo(BigDecimal.ZERO) <= 0)) {
                    JsfUti.messageWarning(null, "Error en los valores de la nota de credito.", "");
                } else if (total.add(valor).compareTo(proforma.getTotalPagar()) > 0) {
                    JsfUti.messageWarning(null, "La suma de las notas de credito debe ser menor o igual al valor de la factura emitida.", "");
                } else if (cajero == null || motivo == null) {
                    JsfUti.messageWarning(null, "El usuario debe tener caja asignada y debe ingresar el motivo de la emision.", "");
                } else if (itl.emisionNotaCreditoFe(proforma.getId(), cajero, motivo, valor)) {
                    tramite = 0L;
                    valor = BigDecimal.ZERO;
                    proforma = new RegpLiquidacion();
                    nc = new RenNotaCredito();
                    lazy = new RenNotaCreditoLazy();
                    motivo = "";
                    JsfUti.update("mainForm");
                    JsfUti.messageInfo(null, "Se realizo la emision de la Nota de Credito.", "");
                } else {
                    JsfUti.messageWarning(null, "Problemas en la emision de la Nota de Credito.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reenvioNotaCredito(RenNotaCredito nc) {
        try {
            if (!nc.getEstado().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                if (itl.reenvioNotaCredito(nc)) {
                    lazy = new RenNotaCreditoLazy();
                    JsfUti.update("mainForm");
                    JsfUti.messageInfo(null, "Se realizo el reenvio de la Nota de Credito.", "");
                } else {
                    JsfUti.messageWarning(null, "Problemas en el reenvio de la Nota de Credito.", "");
                }
            } else {
                JsfUti.messageWarning(null, "El comprobante electronico ya tiene estado AUTORIZADO.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadFactura(RenNotaCredito re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() != null && re.getNumeroDocumento() != null) {
                ruta = Constantes.rutaFeOld + "/notacredito_" + re.getNumeroDocumento().replace("-", "").trim() + ".pdf";
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab("/sgr/DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "El comprobante electronico aun no esta autorizada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public RenNotaCreditoLazy getLazy() {
        return lazy;
    }

    public void setLazy(RenNotaCreditoLazy lazy) {
        this.lazy = lazy;
    }

    public RenNotaCredito getNc() {
        return nc;
    }

    public void setNc(RenNotaCredito nc) {
        this.nc = nc;
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

    public Boolean getRender() {
        return render;
    }

    public void setRender(Boolean render) {
        this.render = render;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
