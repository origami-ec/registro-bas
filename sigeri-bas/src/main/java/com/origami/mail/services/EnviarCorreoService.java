/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.mail.services;

import com.origami.mail.models.CorreoDto;
import javax.ejb.Local;

/**
 *
 * @author eduar
 */
@Local
public interface EnviarCorreoService {
    
    public Boolean enviarCorreo(CorreoDto correo);
    
    public Boolean reenviarCorreo(CorreoDto correo);
    
}
