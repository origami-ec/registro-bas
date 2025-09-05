/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.services.interfaces.BpmProcessEngine;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import org.activiti.engine.ProcessEngine;
import com.origami.sgr.util.ApplicationContextUtils;

/**
 *
 * @author CarlosLoorVargas
 */
@Singleton(name = "bpmProcessEngine")
public class BpmProcessEngineEjb implements BpmProcessEngine {

    private ProcessEngine processEngine;

    @PostConstruct
    private void init() {
        processEngine = (ProcessEngine) ApplicationContextUtils.getBean("processEngine");
        //processEngine.getProcessEngineConfiguration()
    }

    @Override
    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

}
