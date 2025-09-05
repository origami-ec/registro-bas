/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.models.Predio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author eduar
 */
@Local
public interface CatastroService {
    
    public List<Predio> buscarPredioCatastro(Integer tipo, String codigo);
    
}
