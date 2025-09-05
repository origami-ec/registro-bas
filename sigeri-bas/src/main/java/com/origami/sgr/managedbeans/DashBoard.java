/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.lazymodels.TareasWFLazy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.views.TareasActivas;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DashBoard extends BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<TareaWF> tareasWF = new ArrayList<>();
    protected Boolean esAdmin = false;
    protected String usuario, taskDefKey;
    protected AclUser user = new AclUser();
    protected TareasWFLazy lazy;
    protected Integer cantidad = 0;
    protected Date hoy = new Date();
    protected int data = 0;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected StringBuilder queryCount = null;
    protected List<RegFicha> fichas = new ArrayList<>();
    protected StreamedContent stream;

    @PostConstruct
    protected void iniView() {
        try {
            usuario = session.getName_user();
            if (usuario != null) {
                queryCount = new StringBuilder("SELECT CAST(COUNT(*) AS INTEGER) FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
                this.validateRoles(session.getRoles());
                cantidad = (Integer) manager.getNativeQuery(queryCount.toString(), new Object[]{usuario, usuario});
                lazy = new TareasWFLazy(usuario);
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void actualizarTramitePendiente() {
        try {
            if (usuario != null && !usuario.isEmpty()) {
                lazy = new TareasWFLazy(usuario);
                cantidad = (Integer) manager.getNativeQuery(queryCount.toString(), new Object[]{usuario, usuario});
                JsfUti.update("formMain");
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void validateRoles(List<Long> list) {
        for (Long id : list) {
            if (id.equals(1L)) {
                esAdmin = true;
            }
        }
    }

    public void reasignarTramitesByUsuario(SelectEvent event) {
        try {
            user = (AclUser) event.getObject();
            if (user != null && user.getUsuario() != null) {
                for (TareaWF tw : tareasWF) {
                    this.reasignarTarea(tw.getTarea().getId(), user.getUsuario());
                    Map<String, Object> v = this.engine.getvariables(tw.getTarea().getProcessInstanceId());
                    for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                        if (entrySet.getValue() != null && entrySet.getValue().equals(tw.getTarea().getAssignee())) {
                            this.setVariableByProcessInstance(tw.getTarea().getProcessInstanceId(), entrySet.getKey(), user.getUsuario());
                        }
                    }
                }
                JsfUti.messageInfo(null, "Al parecer reasignacion con exito.", "");
            } else {
                JsfUti.messageWarning(null, "No se pudo reasignar los tramites.", "");
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public BigInteger getEstadoPagoByTramite(Long tramite) {
        return (BigInteger) manager.getNativeQuery(Querys.getEstadoPagoLiquidacion, new Object[]{tramite});
    }

    public void viewProcess(TareaWF dp) {
        try {
            stream = DefaultStreamedContent.builder().contentType("image/png").stream(()
                    -> engine.getProcessInstanceDiagram(dp.getProcInstId())).build();
            System.out.println("engine.getProcessInstanceDiagram(dp.getProcInstId())");
            JsfUti.executeJS("PF('dlgDiagrama').show()");
            JsfUti.update("frmDiagrama");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void viewProcess(TareasActivas ta) {
        try {
            stream = DefaultStreamedContent.builder().contentType("image/png").stream(()
                    -> engine.getProcessInstanceDiagram(ta.getProcInstId())).build();
            System.out.println("engine.getProcessInstanceDiagram(dp.getProcInstId())");
            JsfUti.executeJS("PF('dlgDiagrama').show()");
            JsfUti.update("frmDiagrama");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void redirecTask(TareasActivas tarea) {
        session.setTaskID(tarea.getTaskId());
        session.setTitlePage(tarea.getName());
        JsfUti.redirectFaces(tarea.getFormKey());
    }

    public List<TareaWF> getTareasWF() {
        return tareasWF;
    }

    public void setTareasWF(List<TareaWF> tareasWF) {
        this.tareasWF = tareasWF;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Boolean getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public TareasWFLazy getLazy() {
        return lazy;
    }

    public void setLazy(TareasWFLazy lazy) {
        this.lazy = lazy;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public List<RegFicha> getFichas() {
        return fichas;
    }

    public void setFichas(List<RegFicha> fichas) {
        this.fichas = fichas;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    /*public LazyModel<TareasActivas> getTareasLazy() {
        return tareasLazy;
    }

    public void setTareasLazy(LazyModel<TareasActivas> tareasLazy) {
        this.tareasLazy = tareasLazy;
    }*/

}
