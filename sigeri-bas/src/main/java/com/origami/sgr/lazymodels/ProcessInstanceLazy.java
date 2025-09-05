/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.bpm.models.DetalleProceso;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.services.interfaces.BpmBaseEngine;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.EjbsCaller;
import com.origami.sgr.util.Querys;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author CarlosLoorVargas
 */
public class ProcessInstanceLazy extends LazyDataModel<DetalleProceso> {

    private static final long serialVersionUID = 1L;
    protected BpmBaseEngine engine;
    private DetalleProceso det;
    private List<DetalleProceso> ldet;
    private List<HistoricProcessInstance> lhpi;
    private boolean state = false;
    private ProcessDefinition p;
    private ProcessInstance pi;
    private HistoricoTramites ht;
    protected Entitymanager manager;
    private int maxRows = 0;
    private HistoryService hs;

    private void init() {
        ldet = new ArrayList<>();
        engine = EjbsCaller.getEngine();
        manager = EjbsCaller.getTransactionManager();
        maxRows = engine.getCountProcessInstanceHistoric(state).intValue();
    }

    public ProcessInstanceLazy() {
        this.init();
    }

    public ProcessInstanceLazy(boolean state) {
        this.state = state;
        this.init();
    }

    @Override
    public List<DetalleProceso> load(int first, int pageSize, Map<String, SortMeta> sortOrder, Map<String, FilterMeta> filters) {
        try {
            return getDefaultData(first, pageSize, filters);
        } catch (Exception e) {
            Logger.getLogger(ProcessInstanceLazy.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private List getDefaultData(int first, int pageSize, Map<String, FilterMeta> filters) {
        try {
            ldet = new ArrayList<>();
            hs = engine.getProcessEngine().getHistoryService();
            if (filters.isEmpty()) {
                this.setRowCount(maxRows);
                lhpi = engine.getProcessInstanceHistoric(first, pageSize, state);
            } else {
                if (filters.containsKey("idProceso")) {
                    lhpi = hs.createHistoricProcessInstanceQuery().processInstanceId(
                            this.getProceso(Long.parseLong(filters.get("idProceso").getFilterValue().toString().trim())))
                            .unfinished().listPage(first, pageSize);
                }
                if (filters.containsKey("instancia")) {
                    lhpi = hs.createHistoricProcessInstanceQuery().processInstanceId(
                            filters.get("instancia").getFilterValue().toString().trim())
                            .unfinished().listPage(first, pageSize);
                }
                this.setRowCount(lhpi.size());
            }
            for (HistoricProcessInstance hpi : lhpi) {
                p = engine.getProcessDataByDefID(hpi.getProcessDefinitionId());
                pi = engine.getProcessInstanceById(hpi.getId());
                det = new DetalleProceso();
                ht = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{hpi.getId()});
                if (ht == null) {
                    ht = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, new String[]{"idprocess"}, new Object[]{hpi.getId()});
                }
                if (ht != null) {
                    det.setId(ht.getId());
                    if (p.getName() != null) {
                        det.setNombreProceso(p.getName() + " (" + ht.getId() + ")");
                    } else {
                        det.setNombreProceso(p.getKey() + " (" + ht.getId() + ")");
                    }
                    if (pi != null) {
                        det.setInstancia(pi.getId());
                    }
                    if (ht.getId() != null) {
                        det.setIdProceso(ht.getNumTramite().toString());
                    }
                    det.setFechaInicio(hpi.getStartTime());
                    det.setFechaFin(hpi.getEndTime());
                    det.setTasks(engine.getTaskByProcessInstanceId(hpi.getId()));
                    ldet.add(det);
                }
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(ProcessInstanceLazy.class.getName()).log(Level.SEVERE, null, e);
        }
        return ldet;
    }

    public String getProceso(Long id) {
        return (String) manager.find(Querys.getIdProcesoByNumTramite, new String[]{"tramite"}, new Object[]{id});
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return this.getRowCount();
    }

    public Object stream() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
