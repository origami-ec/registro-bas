/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.acl.service.AclCache;
import com.origami.sgr.services.interfaces.AclService;
import com.origami.sgr.util.EjbUtil;

/**
 *
 * @author Fernando
 */
public abstract class SisEjb {
    
    public static AclService aclService(){
        return (AclService) EjbUtil.getEjb("aclService");
    }
    
    public static AclCache aclCacheServ(){
        return (AclCache) EjbUtil.getEjb("AclCacheServ");
    }
    
}
