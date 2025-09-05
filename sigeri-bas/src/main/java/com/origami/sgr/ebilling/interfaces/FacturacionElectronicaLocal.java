/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.interfaces;

import com.origami.sgr.ebilling.models.ComprobanteSRI;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenNotaCredito;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author andysanchez
 */
@Local
public interface FacturacionElectronicaLocal {

    public Boolean emitirFacturaElectronica(RegpLiquidacion liquidacion, RenCajero cajero);

    public Boolean emitirFacturaElectronicaSinTramite(RenFactura renFactura, RenCajero cajero);

    public Boolean emitirNotaCredito(RenNotaCredito notaCredito);

    public List<ComprobanteSRI> getAllComprobanteByCedula(String cedula);
    
    public void reenviarCorreoFacturaElectronicaSRI(ComprobanteSRI comprobanteSRI);
    
    public Boolean reenviarFacturaElectronica(RegpLiquidacion liquidacion, RenCajero cajero, Boolean reenvioVerificacion);
    
    public Boolean reenviarFacturaElectronicaSinTramite(RenFactura renFactura, RenCajero cajero);

}
