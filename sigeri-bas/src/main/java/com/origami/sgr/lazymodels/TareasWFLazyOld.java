/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.EjbsCaller;
import com.origami.sgr.util.Utils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author Anyelo
 */
public class TareasWFLazyOld extends LazyDataModel<TareaWF> {

    private static final Logger LOG = Logger.getLogger(TareasWFLazyOld.class.getName());

    private Entitymanager manager;
    private String taskDefKey;
    private String usuario = "admin";
    private List<TareaWF> tareas;

    private void init() {
        manager = EjbsCaller.getTransactionManager();
    }

    public TareasWFLazyOld() {
        this.init();
    }

    public TareasWFLazyOld(String user) {
        this.usuario = user;
        this.init();
    }

    public TareasWFLazyOld(String user, String taskDefKey) {
        this.usuario = user;
        this.taskDefKey = taskDefKey;
        this.init();
    }

    @Override
    public List<TareaWF> load(int first, int pageSize, Map<String, SortMeta> sorter, Map<String, FilterMeta> filters) {
        try {
            Object[] parametros = new Object[]{usuario, usuario};
            String order = "ORDER BY fecha_entrega ASC ";
            StringBuilder queryCount = new StringBuilder("SELECT CAST(COUNT(*) AS INTEGER) FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
            StringBuilder query = new StringBuilder("SELECT id, num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
            query.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
            query.append("blocked, task_id \"taskId\", proc_inst_id \"procInstId\", form_key \"formKey\", priority, name_ \"name\", candidate ");
            query.append("FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
            tareas = new ArrayList<>();
            if (filters.isEmpty()) {
                if (taskDefKey == null) {
                    parametros = new Object[]{usuario, usuario};
                } else {
                    query.append("AND task_def_key = ? ");
                    queryCount.append("AND task_def_key = ? ");
                    parametros = new Object[]{usuario, usuario, taskDefKey};
                }
                Integer temp = (Integer) manager.getNativeQuery(queryCount.toString(), parametros);
                this.setRowCount(temp);
            } else {
                if (filters.containsKey("idTramite") && Utils.isNum(filters.get("idTramite").getFilterValue().toString().trim())) {
                    query.append("AND num_tramite = ? ");
                    parametros = new Object[]{usuario, usuario, new BigInteger(filters.get("idTramite").getFilterValue().toString().trim())};
                }
            }
            query.append(order);
            tareas = manager.findNativeQueryFirstAndMaxResult(TareaWF.class, query.toString(), parametros, first, pageSize);
            if (!filters.isEmpty()) {
                this.setRowCount(tareas == null ? 0 : tareas.size());
            }
            return tareas;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return this.getRowCount();
    }

}
