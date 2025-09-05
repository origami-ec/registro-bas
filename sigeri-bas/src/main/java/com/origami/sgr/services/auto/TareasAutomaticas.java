/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.auto;

import com.origami.config.SisVars;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.util.List;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@Stateless
@javax.enterprise.context.Dependent
public class TareasAutomaticas {

    @Inject
    private Entitymanager em;
    @Inject
    private FirmaDigitalLocal fd;
    @Inject
    private AsynchronousService as;

    /**
     * CADA HORA SE VERIFICA LOS PENDIENTES DE ENVIO
     */
    @Schedule(year = "*", month = "*", dayOfMonth = "*", dayOfWeek = "*", hour = "9-18", minute = "0", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void enviarCorreoInicioTramiteOnline() {
        List<RegpLiquidacion> tramites;
        HistoricoTramites ht;
        String rutaPdf;
        try {
            tramites = em.findAll(Querys.getLiquidacionesOrdenadas);
            if (!tramites.isEmpty()) {
                for (RegpLiquidacion liquidacion : tramites) {
                    rutaPdf = fd.generarTituloCredito(liquidacion.getId(), liquidacion.getNumTramiteRp(),
                            Utils.cantidadAletras(liquidacion.getTotalPagar()));
                    if (rutaPdf != null) {
                        as.enviarCorreoTituloCredito(liquidacion, rutaPdf, SisVars.userVentanillaSgr);
                        ht = liquidacion.getTramite();
                        ht.setEntregado(Boolean.TRUE);
                        em.update(ht);
                    }
                    Thread.sleep(5000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("//ERROR EN METODO: enviarCorreoInicioTramiteOnline()");
            System.out.println(e);
        }
    }

}
