/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.mail.entities.CorreoUsuarios;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.SecuenciaInscripcion;
import com.origami.sgr.entities.SecuenciaRepertorio;
//import com.origami.sgr.entities.SecuenciaRepertorioMercantil;
import com.origami.sgr.entities.TarUsuarioTareas;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface SeqGenMan {

    public BigInteger getSecuenciaGeneral(String code);

    public BigInteger getSecuenciaGeneralByAnio(String code);

    public BigInteger getSecuenciaTomoByAnio(String code);

    public BigInteger getSecuenciaEntes(String code);

    public SecuenciaRepertorio getSecuenciaRepertorio();

    //public SecuenciaRepertorioMercantil getSecuenciaRepertorioMercantil();
    
    //public SecuenciaRepertorio getSecuenciaRepertorio(Date fecha);
    
    //public SecuenciaRepertorioMercantil getSecuenciaRepertorioMercantil(Date fecha);

    public Integer getSecuenciaInscripcion(Long idLibro);
    
    public SecuenciaInscripcion consultarSecuenciaInscripcion(Long idLibro);

    public Integer getSecuenciaInscripcion(Long idLibro, Date fecha);

    public AclUser getUserForTramite(Long idRol, Integer cantidad);

    public AclUser getUserForTramiteById(Long idUser, Integer cantidad);

    public boolean cobroDisponible(Long liq, Long user);

    public TarUsuarioTareas getUserForTask(Long idRol, Integer cantidad);

    public TarUsuarioTareas getUserForTaskFicha(Long idRol, Integer cantidad);

    public TarUsuarioTareas getUserForTask(Long idRol, Integer cantidad, Date fechaEntrega);

    public Long getSecuenciaTramite();
    
    public Long getSecuenciaTitulo(Long tramite);
    
    public BigInteger getSecuenciaComprobante(String code, String ambiente);
    
    public CorreoUsuarios getMailDisponible();

}
