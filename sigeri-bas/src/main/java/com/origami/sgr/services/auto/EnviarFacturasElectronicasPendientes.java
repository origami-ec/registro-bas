/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.auto;

import com.origami.config.SisVars;
import com.origami.sgr.ebilling.interfaces.FacturacionElectronicaLocal;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.MsgFormatoNotificacion;
import com.origami.sgr.entities.MsgTipoFormatoNotificacion;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.Email;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named
@Stateless
@javax.enterprise.context.Dependent
public class EnviarFacturasElectronicasPendientes {

    private static final Logger LOG = Logger.getLogger(EnviarFacturasElectronicasPendientes.class.getName());

    @Inject
    private Entitymanager manager;
    @Inject
    private FacturacionElectronicaLocal fac;
    @Inject
    private IngresoTramiteLocal itl;
    private SimpleDateFormat sdf;

    /**
     * A LAS 22:00 SE ENVIAN LAS FACTURAS PENDIENTE DE ENVIAR =O
     */
    //@Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "22", dayOfMonth = "*", year = "*", minute = "*/20", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTaskFacturas() {
        if (SisVars.enviar) {
            //liquidacionesSinEnviar();
        }
    }

    /**
     * A LAS 23:10 SE VERIFICAN QUE LAS FACTURAS EMITIDAS =O
     */
    //@Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*", year = "*", minute = "10", second = "00", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTaskComprobacionFacturas() {
        if (SisVars.enviar) {
            //facturasAutorizacion();
        }
    }

    /**
     * A LAS 23:50 SE ENVIA UN CORREO A JASSMINA DE LAS FACTURAS NO ENVIADAS =O
     */
    //@Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*", year = "*", minute = "50", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTaskCorreo() {
        if (SisVars.enviar) {
            //enviarCorreoFacturasNoAutorizadas();
            //manager.executeFunction("flow.update_estado_tramite");
        }
    }

    /**
     *
     */
    //@Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "13", dayOfMonth = "*", year = "*", minute = "50", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTaskActualizarTramitesEstadistica() {
        if (SisVars.enviar) {
            //manager.executeFunction("flow.update_estado_tramite");
        }
    }

    private void facturasAutorizacion() {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Map map = new HashMap();
        map.put("fecha", sdf.format(new Date()));
        List<RegpLiquidacion> facturas = manager.findNamedQuery(Querys.getFacturasAutorizadasComprobacion, map);
        if (Utils.isNotEmpty(facturas)) {
            AclUser temp;
            RenCajero cajero;
            for (RegpLiquidacion re : facturas) {
                temp = manager.find(AclUser.class, re.getUserIngreso());
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", temp);
                cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
                if (cajero != null) {
                    fac.reenviarFacturaElectronica(re, cajero, Boolean.TRUE);
                }
            }
        }
    }

    private void liquidacionesSinEnviar() {
        List<RegpLiquidacion> facturas = manager.findAll(Querys.getFacturasNoAutorizadasReenvio);
        if (Utils.isNotEmpty(facturas)) {
            AclUser temp;
            Map map;
            RenCajero cajero;
            for (RegpLiquidacion re : facturas) {
                temp = manager.find(AclUser.class, re.getUserIngreso());
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", temp);
                cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
                if (cajero != null) {
                    fac.reenviarFacturaElectronica(re, cajero, Boolean.FALSE);
                }
            }
        }
    }

    private void enviarCorreoFacturasNoAutorizadas() {
        List<RegpLiquidacion> facturas = manager.findAll(Querys.getFacturasNoAutorizadasReenvio);
        if (Utils.isNotEmpty(facturas)) {
            String numTramiteFacturas = "<br/>";
            for (RegpLiquidacion re : facturas) {
                numTramiteFacturas = re.getNumTramiteRp().toString() + " - " + re.getCodigoComprobante() + "<br/>" + numTramiteFacturas;
            }
            List<AclUser> usuariosTesoreros = itl.getUsuariosByRolName("tesorero");
            if (Utils.isNotEmpty(usuariosTesoreros)) {
                if (usuariosTesoreros.get(0).getEnte().getCorreo1() != null) {
                    CatEnte tesorero = usuariosTesoreros.get(0).getEnte();
                    Map<String, Object> map = new HashMap();
                    map.put("descripcion", "CORREO_ENVIO_FACTURA_ELECTRONICA");
                    MsgTipoFormatoNotificacion tfn = (MsgTipoFormatoNotificacion) manager.findObjectByParameter(MsgTipoFormatoNotificacion.class, map);
                    map = new HashMap(); //
                    map.put("estado", 1);
                    map.put("tipo", tfn);
                    MsgFormatoNotificacion fn = (MsgFormatoNotificacion) manager.findObjectByParameter(MsgFormatoNotificacion.class, map);
                    String contenido = fn.getHeader()
                            + "<br/>Estimada(o) " + tesorero.getNombreCompleto() + ", las siguientes facturas no han podido ser ingresadas: <br/>"
                            + "<bold>" + numTramiteFacturas
                            + "</bold> Por favor revisarlas e intentar reenviarlas manualmente<br/><br/>"
                            + "Agradecemos su comprención.<br/>";
                    enviarCorreo(tesorero.getCorreo1(), fn.getAsunto() + ": REVISIÓN", contenido);
                }
            }
        }
    }

    private void enviarCorreo(String correo, String asunto, String contenido) {
        Email send = new Email(correo, asunto, contenido, null);
        send.sendMail();
    }

}
