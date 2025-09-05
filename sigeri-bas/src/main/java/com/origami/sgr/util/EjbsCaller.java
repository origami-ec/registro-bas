/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.config.SisVars;
import com.origami.sgr.models.ProcessDef;
import com.origami.sgr.services.interfaces.BpmBaseEngine;
import com.origami.sgr.services.interfaces.Entitymanager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import com.origami.documental.ejbs.DocumentsEjb;
import javax.naming.NamingException;
import org.activiti.engine.repository.ProcessDefinition;

/**
 *
 * @author CarlosLoorVargas
 */
public class EjbsCaller {

    private static Entitymanager manager = null;
    private static BpmBaseEngine engine = null;

    public static Entitymanager getTransactionManager() {
        try {
            manager = (Entitymanager) new InitialContext().lookup(SisVars.entityManager);
        } catch (NamingException e) {
            manager = null;
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
        } 
        return manager;
    }

    public static BpmBaseEngine getEngine() {
        try {
            engine = (BpmBaseEngine) new InitialContext().lookup(SisVars.bpmBaseEngine);
        } catch (NamingException e) {
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
        }
        return engine;
    }

    public static ProcessDef getProcessDef(String key) {
        ProcessDef pd = null;
        try {
            ProcessDefinition p = getEngine().getProcessDefinitionByKey(key);
            if (p != null) {
                pd = new ProcessDef();
                pd.setId(p.getId());
                pd.setKey(p.getKey());
                pd.setName(p.getName());
                pd.setDescription(p.getDescription());
                pd.setDeploymentId(p.getDeploymentId());
                pd.setVersion(p.getVersion());
                pd.setDiagramResourceName(p.getDiagramResourceName());
                pd.setResourceName(p.getResourceName());
                pd.setSuspended(p.isSuspended());
                pd.setHasStartFormKey(p.hasStartFormKey());
            }
        } catch (Exception e) {
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
        }
        return pd;
    }

    public static DocumentsEjb getEjbDocuments() {
        try {
            return (DocumentsEjb) new InitialContext().lookup(SisVars.documents);
        } catch (NamingException e) {
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

}
