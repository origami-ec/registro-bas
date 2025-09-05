/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Juan Carlos
 */
@Local
public interface FichaIngresoService {

    public Long saveListFichasPredial(List<RegFicha> list, List<RegMovimiento> movs);

    public String saveFichasDuplicadas(RegFicha matriz, Integer cantidad, String usuario, String linderos,
            List<RegMovimiento> movimientos);
}
