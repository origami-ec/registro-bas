/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.bpm.models.DetalleProceso;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.lazymodels.ProcessInstanceLazy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class TramitesRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TramitesRp.class.getName());

    protected List<TareaWF> tareasWF = new ArrayList<>();
    protected Boolean esAdmin = false;
    protected String usuario, usuario2, usuario3;
    protected String taskDefKey, taskDefKey2, taskDefKey3;
    protected String observacion, idProcess, candidatos;
    protected Long tramite;
    protected Integer prioridad = 0, cantidad = 0, cantidad2 = 0, cantidad3 = 0;
    protected AclUser user = new AclUser();
    protected ProcessInstanceLazy details;
    protected DetalleProceso proceso;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<AclUser> users = new ArrayList<>();
    protected HistoricTaskInstance tareaActual;
    protected List<Attachment> listAttach = new ArrayList<>();
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected HashMap<String, Object> pars;
    private StreamedContent stream;

    @PostConstruct
    protected void iniView() {
        try {
            proceso = new DetalleProceso();
            details = new ProcessInstanceLazy(false);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarTramitePendiente() {
        try {
            if (usuario != null) {
                tareasWF = this.getListaTareasPersonales(usuario, null);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean validateAdmin(List<Long> list) {
        for (Long id : list) {
            if (id.equals(1L)) {
                return true;
            }
        }
        return false;
    }

    public void reasignarTramitesByUsuario(SelectEvent event) {
        try {
            user = (AclUser) event.getObject();
            if (user != null && user.getUsuario() != null) {
                if (!tareasWF.isEmpty()) {
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
                    tareasWF = new ArrayList<>();
                } else {
                    JsfUti.messageWarning(null, "No se encontraron tramites.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se encontro usuario de sistema.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDelete(DetalleProceso dp) {
        try {
            proceso = dp;
            JsfUti.update("formEdit");
            JsfUti.executeJS("PF('dlgEditCert').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void deleteTramite() {
        try {
            if (proceso.getId() != null) {
                if (observacion != null) {
                    if (this.saveObservacion()) {
                        this.deleteProcessInstance(proceso.getInstancia());
                        JsfUti.messageInfo(null, "Tramite eliminado con exito.", "");
                        JsfUti.update("formMain:tbViewTramites:dtDetail");
                        JsfUti.executeJS("PF('dlgEditCert').hide();");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR", "");
        }
    }

    public boolean saveObservacion() {
        try {
            Observaciones ob = new Observaciones();
            ob.setObservacion(observacion);
            ob.setEstado(true);
            ob.setFecCre(new Date());
            ob.setIdTramite(new HistoricoTramites(proceso.getId()));
            ob.setTarea("ELIMINAR TRAMITE");
            ob.setUserCre(session.getName_user());
            manager.persist(ob);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void showDetalis(DetalleProceso dp) {
        try {
            proceso = dp;
            tareas = this.getTaskByProcessInstanceIdMain(proceso.getInstancia());
            listAttach = this.getProcessInstanceAllAttachmentsFiles(proceso.getInstancia());
            map = new HashMap();
            map.put("id", proceso.getId());
            ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
            //this.initDocEsc(this.ht.getNumTramite().toString());
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgPrioridad(DetalleProceso dp) {
        try {
            proceso = dp;
            prioridad = (int) this.getVariableByPorcessIntance(dp.getInstancia(), "prioridad");
            JsfUti.update("formEditPrioridadTra");
            JsfUti.executeJS("PF('editPrioridadTraDlg').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarPrioridad() {
        try {
            if (proceso.getInstancia() != null && prioridad > 0) {
                this.setVariableByProcessInstance(proceso.getInstancia(), "prioridad", prioridad);
                List<Task> tareasActivas = this.obtenerTareasActivasProcessInstance(proceso.getInstancia());
                this.asignarTareaPriority(tareasActivas, prioridad);
                JsfUti.messageInfo(null, "Prioridad de tramite actualizada.", "");
                JsfUti.update("formMain:tbViewTramites:dtDetail");
                JsfUti.executeJS("PF('editPrioridadTraDlg').hide();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void showDlgReasigancion(DetalleProceso dp) {
        try {
            proceso = dp;
            map = new HashMap();
            map.put("id", proceso.getId());
            ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
            map = new HashMap();
            map.put("numTramiteRp", ht.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            map = new HashMap();
            map.put("sisEnabled", true);
            users = manager.findObjectByParameterList(AclUser.class, map);
            tareaActual = this.getTaskByProcessInstance(dp.getInstancia());
            if (tareaActual.getEndTime() == null) {
                /*if (tareaActual.getAssignee() == null) {
                    JsfUti.messageError(null, "Esta tarea no se puede re-asignar. Tiene usuarios candidatos.", "");
                    return;
                }*/
                JsfUti.update("formreasignar");
                JsfUti.executeJS("PF('dlgReasignar').show();");
            } else {
                JsfUti.messageError(null, "No se puede re asignar Tarea. Tarea Finalizada.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void reasignarTarea(AclUser user) {
        try {
            if (liquidacion != null && liquidacion.getId() != null) {
                liquidacion.setInscriptor(user);
                manager.merge(liquidacion);
            }
            String obs = "TAREA: " + tareaActual.getName() + ", USUARIO ANTERIOR: " + tareaActual.getAssignee() + ", USUARIO ACTUAL: " + user.getUsuario();
            reg.guardarObservaciones(ht, session.getName_user(), obs, "REASIGNACION DE USUARIO");
            this.reasignarTarea(tareaActual.getId(), user.getUsuario());
            Map<String, Object> v = this.engine.getvariables(tareaActual.getProcessInstanceId());
            for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                if (entrySet.getValue() != null && entrySet.getValue().equals(tareaActual.getAssignee())) {
                    this.setVariableByProcessInstance(tareaActual.getProcessInstanceId(), entrySet.getKey(), user.getUsuario());
                }
            }
            JsfUti.executeJS("PF('dlgReasignar').hide();");
            JsfUti.update("formMain:tbViewTramites:dtDetail");
            JsfUti.messageInfo(null, "Tarea Re-Asignada con exito.", "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void asignarCanditeUsers() {
        try {
            if (usuario2 != null && candidatos != null) {
                if (!tareasWF.isEmpty()) {
                    for (TareaWF tw : tareasWF) {
                        this.setCandidateUsers(tw.getTarea().getId(), candidatos);
                    }
                    JsfUti.messageInfo(null, "Tareas con candidatos con exito.", "");
                    tareasWF = new ArrayList<>();
                } else {
                    JsfUti.messageInfo(null, "No se encontraron tareas.", "");
                }
            } else {
                JsfUti.messageInfo(null, "Debe ingresar los usuarios candidatos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void deleteCanditeUsers() {
        try {
            if (usuario3 != null) {
                if (!tareasWF.isEmpty()) {
                    for (TareaWF tw : tareasWF) {
                        this.eliminarCandidateUsers(tw.getTarea().getId(), usuario3);
                    }
                    JsfUti.messageInfo(null, "Tareas con candidatos con exito.", "");
                    tareasWF = new ArrayList<>();
                } else {
                    JsfUti.messageInfo(null, "No se encontraron tareas.", "");
                }
            } else {
                JsfUti.messageInfo(null, "Debe ingresar el usuario para eliminar tareas.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void executeJobManagementService() {
        try {
            if (idProcess != null) {
                this.getProcessEngine().getManagementService().executeJob(idProcess);
                System.out.println("Execute job: " + idProcess);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void iniciarTramiteEspecifico() {
        try {
            if (tramite == null) {
                JsfUti.messageError(null, "Debe ingresar el numero de tramite.", "");
                return;
            }
            map = new HashMap();
            map.put("numTramiteRp", tramite);
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            if (liquidacion != null) {
                if (reg.iniciarTramiteActiviti(liquidacion)) {
                    reg.updateEstadoTareas(tramite);
                    JsfUti.messageInfo(null, "El tramite se inicializo con exito.", "");
                } else {
                    JsfUti.messageError(null, "No se pudo inicilizar el tramite.", "");
                }
            } else {
                JsfUti.messageError(null, "Numero de tramite no encontrado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cantidadTareas() {
        if (usuario != null) {
            cantidad = this.getCantidadTareasUser(usuario, taskDefKey);
            tareasWF = this.getListaTareasPersonales(usuario, taskDefKey);
        }
    }

    public void cantidadTareas2() {
        if (usuario2 != null) {
            cantidad2 = this.getCantidadTareasUser(usuario2, taskDefKey2);
            tareasWF = this.getListaTareasPersonales(usuario2, taskDefKey2);
        }
    }

    public void cantidadTareas3() {
        if (usuario3 != null) {
            cantidad3 = this.getCantidadTareasUser(usuario3, taskDefKey3);
            tareasWF = this.getListaTareasPersonales(usuario3, taskDefKey3);
        }
    }

    public void viewProcess(DetalleProceso dp) {
        System.out.println("// id proceso: " + dp.getInstancia());
        stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> engine.getProcessInstanceDiagram(dp.getInstancia())).build();
        JsfUti.executeJS("PF('dlgDiagrama').show()");
        JsfUti.update("frmDiagrama");
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

    public ProcessInstanceLazy getDetails() {
        return details;
    }

    public void setDetails(ProcessInstanceLazy details) {
        this.details = details;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<Attachment> getListAttach() {
        return listAttach;
    }

    public void setListAttach(List<Attachment> listAttach) {
        this.listAttach = listAttach;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public DetalleProceso getProceso() {
        return proceso;
    }

    public void setProceso(DetalleProceso proceso) {
        this.proceso = proceso;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public List<AclUser> getUsers() {
        return users;
    }

    public void setUsers(List<AclUser> users) {
        this.users = users;
    }

    public HistoricTaskInstance getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(HistoricTaskInstance tareaActual) {
        this.tareaActual = tareaActual;
    }

    public String getIdProcess() {
        return idProcess;
    }

    public void setIdProcess(String idProcess) {
        this.idProcess = idProcess;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getCandidatos() {
        return candidatos;
    }

    public void setCandidatos(String candidatos) {
        this.candidatos = candidatos;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(Integer cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskDefKey2() {
        return taskDefKey2;
    }

    public void setTaskDefKey2(String taskDefKey2) {
        this.taskDefKey2 = taskDefKey2;
    }

    public String getUsuario3() {
        return usuario3;
    }

    public void setUsuario3(String usuario3) {
        this.usuario3 = usuario3;
    }

    public String getTaskDefKey3() {
        return taskDefKey3;
    }

    public void setTaskDefKey3(String taskDefKey3) {
        this.taskDefKey3 = taskDefKey3;
    }

    public Integer getCantidad3() {
        return cantidad3;
    }

    public void setCantidad3(Integer cantidad3) {
        this.cantidad3 = cantidad3;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

}
