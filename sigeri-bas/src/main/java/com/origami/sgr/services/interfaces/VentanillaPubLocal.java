/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.models.Pago;
import com.origami.sgr.models.PagoReverso;
import java.io.File;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface VentanillaPubLocal {

    public RegpLiquidacion iniciarTramiteOnline(PubSolicitud solicitud);

    public RegpLiquidacion generarProformaOnline(PubSolicitud solicitud);

    public boolean iniciarTramiteActivitiOnline(RegpLiquidacion liquidacion);

    public RegpLiquidacion iniciarTramiteInscripcionOnline(PubSolicitud solicitud);

    public PubSolicitud generarSolicitudInscripcion(PubSolicitud solicitud);

    public HistoricoTramites actualizarRequisitosInscripcion(PubSolicitud solicitud);

    public void iniciarTramiteSolicitudInscripcion(PubSolicitud solicitud);

    public RenCajero getCajaVentanilla();

    public CtlgItem getUsoDocumentoById(Integer idUsoDocumento);

    public RegpLiquidacion iniciarTramiteBanca(Pago pago);

    public void iniciarProcesoActiviti(RegpLiquidacion liquidacion);

    public void iniciarTramiteActiviti(RegpLiquidacion liquidacion);

    public RegpLiquidacion reversarTramiteBanca(PagoReverso reverso);

    public File generarPDF(Long oid);

}
