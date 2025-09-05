/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.acl.AccessLevelRequest;
import com.origami.sgr.acl.RespuestaAcceso;
import com.origami.sgr.acl.entity.AclUrl;
import com.origami.sgr.entities.AclRol;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface AclService {
    
    public List<AclRol> getRoles_url(AclUrl urlEnt);
    
    public List<AclRol> getRoles_url(Long idUrl);
    
    public RespuestaAcceso checkAccessLevel(AccessLevelRequest alreq);
    
}
