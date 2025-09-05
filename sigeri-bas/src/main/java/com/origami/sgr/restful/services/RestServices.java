/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.services;

import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.restful.models.DatosCertificado;
import com.origami.sgr.restful.models.DatosFicha;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author asilva
 */
@Local
public interface RestServices {
    
    public AclUser getAclUserByID(Long id);
    
    public RegpLiquidacion getRegpLiquidacionByNumTramite(Long numTramite);
    
    public List<RegpLiquidacionDetalles> getRegpLiquidacionDetalles(Long id);
    
    public RegCertificado getRegCertificadoByNumCertif(Long numCertif);
    
    public RegpDocsTarea getRegpDocsTareaByTareaTramite(Long tareaTramite);
    
    public RegFicha getRegFichaByNumFicha(Long numFicha);
    
    public RegFicha getRegFichaByCodPredial(String codigo);
    
    public List<RegMovimiento> getMovimientosByFicha(Long idFicha);
    
    public List<RegEnteInterviniente> getPropietariosByFicha(Long id);
    
    public DatosCertificado getDatosByCertificado(Long numero);
    
    public DatosFicha getDatosByNumFicha(Long numero);
    
    public DatosFicha getDatosFichaByCodPredial(String codigo);
    
}
