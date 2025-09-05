/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.acl.service;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Fernando
 */
@Local
public interface AclCache {
 
    Boolean checkAccess(String url, Long idUser, List<Long> rolesIds);

    void clear();
    
    public Boolean getEnabled();

    public void setEnabled(Boolean enabled);
    
}
