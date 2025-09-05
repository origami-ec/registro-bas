/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.services.DocumentalService;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.models.DetalleProceso;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaMarginacion;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.managedbeans.DashBoard;
import com.origami.sgr.services.interfaces.BpmBaseEngine;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.ApplicationContextUtils;
import com.origami.sgr.util.Archivo;
import com.origami.sgr.util.CmisUtil;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.NumeroLetra;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.chemistry.opencmis.client.api.Document;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Fernando
 */
public abstract class BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 5L;
    public static final Logger LOGger = Logger.getLogger(BpmManageBeanBaseRoot.class.getName());

    @Inject
    protected BpmBaseEngine engine;

    @EJB
    protected Entitymanager manager;

    @Inject
    protected UserSession session;

    @Inject
    protected DocumentalService doc;

    @Inject
    protected ServletSession ss;

    @Inject
    protected RegistroPropiedadServices reg;
    
    @Inject
    protected IngresoTramiteLocal itl;
    
    @Inject
    protected OmegaUploader ou;

    protected Map map;
    protected ArrayList<Archivo> files = new ArrayList<>();
    protected String taskId;
    protected HistoricoTramites bpmTramite;
    protected List<Attachment> listAttachment = new ArrayList<>();
    protected String advertencias = "";
    protected List<ArchivoDocs> archivos;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAdvertencias() {
        return advertencias;
    }

    public void setAdvertencias(String advertencias) {
        this.advertencias = advertencias;
    }

    public ProcessEngine getProcessEngine() {
        return engine.getProcessEngine();
    }

    public String obtenerFormKey(String processId) {
        return engine.getProcessEngine().getFormService().getStartFormData(processId).getFormKey();
    }

    public String obtenerKeyProceso(String processId) {
        return engine.getProcessKey(processId);
    }

    public Integer obtenerVersionProceso(String processDefinitionId) {
        return engine.getProcessDefinitionVersion(processDefinitionId);
    }

    public void loadProcessByClassPath(String path) {
        engine.loadSingleProcessByClassPath(path);
    }

    public List<ProcessDefinition> getProcesosDesplegados() {
        return engine.getProcessDesployedList();
    }

    public List<ProcessDefinition> getAllProcesosDesplegados() {
        return engine.getAllProcessDesployedList();
    }

    public List<ArchivoDocs> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoDocs> archivos) {
        this.archivos = archivos;
    }

    public Map<String, String> getProcesos() {
        Map<String, String> lproc = new HashMap<>();
        getProcesosDesplegados().forEach(p -> {
            lproc.put(p.getName(), p.getName());
        });
        return lproc;
    }

    public String generadorCeroALaIzquierda(Long n) {
        int cont = 0;
        Long num = n;
        String salida = "";
        while (num > 0) {
            num = num / 10;
            cont++;
        }
        for (int i = 0; i < 8 - cont; i++) {
            salida = salida + "0";
        }
        salida = salida + n;
        return salida;
    }

    public List<DetalleProceso> getInstanciaProcesos() {
        List<DetalleProceso> ldet = new ArrayList<>();
        DetalleProceso det;
        HistoricoTramites ht;
        List<HistoricProcessInstance> h = engine.getProcessInstanceHistoric();
        try {
            for (HistoricProcessInstance hpi : h) {
                ProcessDefinition p = engine.getProcessDataByDefID(hpi.getProcessDefinitionId());
                ProcessInstance pi = engine.getProcessInstanceById(hpi.getId());
                det = new DetalleProceso();
                ht = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{hpi.getId()});
                if (ht == null) {
                    ht = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, new String[]{"idprocess"}, new Object[]{hpi.getId()});
                }
                if (ht != null) {
                    if (p.getName() != null) {
                        det.setNombreProceso(p.getName() + " (" + ht.getId() + ")");
                    } else {
                        det.setNombreProceso(p.getKey() + " (" + ht.getId() + ")");
                    }
                    if (pi != null) {
                        det.setInstancia(pi.getId());
                    }
                    if (ht.getId() != null) {
                        det.setIdProceso(ht.getId().toString());
                    }
                    det.setFechaInicio(hpi.getStartTime());
                    det.setFechaFin(hpi.getEndTime());
                    det.setTasks(engine.getTaskByProcessInstanceId(hpi.getId()));
                    ldet.add(det);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return ldet;
    }

    public String getProcessInstanceByTareaID(String tareaID) {
        if (tareaID != null) {
            Task tarea = engine.getTaskDataByTaskID(tareaID);
            if (tarea != null) {
                return tarea.getProcessInstanceId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public List<TareaWF> getListaTareasPersonales(String usuario, String keyTask) {
        ArrayList<Task> tareasAct = (ArrayList<Task>) engine.getUsertasksList(usuario, keyTask);
        ArrayList<Task> temp = (ArrayList<Task>) engine.getCandidateUsertasksList(usuario);
        temp.forEach(t -> {
            tareasAct.add(t);
        });
        List<TareaWF> tareasWF = new ArrayList<>();
        HistoricProcessInstance p;
        HistoricoTramites historicoTramites1;
        try {
            if (tareasAct.size() > 0) {
                for (Task task : tareasAct) {
                    p = engine.getHistoricProcessInstanceByInstanceID(task.getProcessInstanceId());
                    if (p != null && p.getSuperProcessInstanceId() != null) {
                        historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, 
                                new String[]{"idprocess"}, new Object[]{p.getSuperProcessInstanceId()});
                        if (historicoTramites1 == null) {
                            historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, 
                                    new String[]{"idprocess"}, new Object[]{p.getSuperProcessInstanceId()});
                            /*if (historicoTramites1 != null) {
                                historicoTramites1.setIdProceso(p.getId());
                                historicoTramites1.setCarpetaRep(historicoTramites1.getId() + "-" + p.getId());
                                acl.updateAndPersistEntity(historicoTramites1);
                            }*/
                        }
                    } else {
                        historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, 
                                new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                        if (historicoTramites1 == null) {
                            historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, 
                                    new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                        }
                    }
                    if (historicoTramites1 != null) {
                        TareaWF tareawf = new TareaWF();
                        tareawf.setIdTramite(historicoTramites1.getNumTramite());
                        tareawf.setTarea(task);
                        /*if (task.getDescription() != null) {
                            if (task.getDescription().length() > 50) {
                                tareawf.setDescripcionTareaMayor50char(true);
                            } else {
                                tareawf.setDescripcionTareaMayor50char(false);
                            }
                        }*/
                        tareawf.setTramite(historicoTramites1);
                        /*tareawf.setTramiteRp(Long.parseLong(serv.getEntityByParameters(Querys.getProcedureNumberById, 
                                new String[]{"tramite"}, new Object[]{historicoTramites1.getId()}).toString()));*/
                        tareasWF.add(tareawf);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return tareasWF;
    }

    public List<TareaWF> getListaTareasPersonales(String usuario, int limite, String taskDefKey) {
        List<TareaWF> tareasWF = new ArrayList<>();
        //HistoricProcessInstance p;
        HistoricoTramites historicoTramites1;
        TareaWF tareawf;
        List<Task> tareasAct;
        try {
            if (taskDefKey == null) {
                tareasAct = engine.getAllTasksUser(usuario, limite);
            } else {
                tareasAct = engine.getTasksUserByNameTask(usuario, limite, taskDefKey);
            }
            if (tareasAct.size() > 0) {
                for (Task task : tareasAct) {
                    /*p = engine.getHistoricProcessInstanceByInstanceID(task.getProcessInstanceId());
                    if (p != null) {
                        historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, 
                            new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                    }*/
                    historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, 
                            new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                    if (historicoTramites1 != null) {
                        tareawf = new TareaWF();
                        tareawf.setIdTramite(historicoTramites1.getNumTramite());
                        tareawf.setTarea(task);
                        tareawf.setTramite(historicoTramites1);
                        tareasWF.add(tareawf);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return tareasWF;
    }

    public List<Task> obtenerTareasUsuarios(String usuario) {
        try {
            return this.engine.getUsertasksList(usuario, null);
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public List<HistoricTaskInstance> obtenerTareasUsuariosCompletadas() {
        return this.engine.getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().finished().list();
    }

    public Boolean reasignarTarea(String id, String usuario) throws Exception {
        return this.engine.setAssigneeTask(id, usuario);
    }

    public Boolean setCandidateUsers(String id, String usuario) throws Exception {
        return this.engine.setCandidateUser(id, usuario);
    }

    public Boolean eliminarCandidateUsers(String id, String usuario) throws Exception {
        return this.engine.deleteCandidateUser(id, usuario);
    }

    public void asignarTareaPriority(List<Task> tareas, Integer prioridad) throws Exception {
        tareas.forEach(t -> {
            this.engine.setTaskPriority(t.getId(), prioridad);
        });
    }

    public Task obtenerTarea(String id) {
        return engine.getTaskDataByTaskID(id);
    }

    /**
     * Lista los Archivo que a tenido el flujo durante lo que va del proceso.
     *
     * @param id_proceso String
     * @return possible object is {@link List }
     */
    public List<Archivo> documentosAdjuntados(String id_proceso) {
        List<Archivo> archi = (List<Archivo>) engine.getProcessEngine().getRuntimeService().getVariable(id_proceso, "listaArchivosFinal");
        return archi;
    }

    /**
     * Lista los Archivo que a tenido el flujo, cuando este esta finalizado.
     *
     * @param id_proceso String
     * @return possible object is {@link List }
     */
    public List<Archivo> documentosAdjuntadosTramitesFinalizados(String id_proceso) {
        List<Archivo> archi = new ArrayList<>();
        List<HistoricVariableInstance> variableInstanceQuerys = engine.getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(id_proceso).variableName("listaArchivosFinal").list();
        for (HistoricVariableInstance acc : variableInstanceQuerys) {
            archi = (List<Archivo>) acc.getValue();
        }
        return archi;
    }

    public boolean validarEntradaDeRequisitos() {
        if (files == null) {
            files = new ArrayList<>();
        }
        if (!files.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", "Se ingresaron los requisitos con éxito!");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return true;
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje", "No ingresó correctamente los requisitos.");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return false;
        }
    }

    public Boolean validaFiles() {
        if (files == null) {
            files = new ArrayList<>();
        }
        if (files.isEmpty()) {
            JsfUti.messageWarning(null, "Debe cargar los Documentos.", "");
            return false;
        } else {
            return true;
        }
    }

    public Boolean validaUnArchivo() {
        if (files == null) {
            files = new ArrayList<>();
        }
        if (files.size() != 1) {
            JsfUti.messageWarning(null, "Debe cargar solo un Documento.", "");
            return false;
        } else {
            return true;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            Date d = new Date();
            String rutaArchivo = SisVars.rutaTemporales + d.getTime() + event.getFile().getFileName();
            File file = new File(rutaArchivo);
            InputStream is;
            is = event.getFile().getInputStream();
            try (OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                is.close();
            }
            Archivo documento = new Archivo();
            documento.setNombre(d.getTime() + "_" + event.getFile().getFileName());
            documento.setTipo(event.getFile().getContentType());
            documento.setRuta(rutaArchivo);
            this.files.add(documento);

        } catch (IOException e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public ProcessInstance startProcessByDefinitionKey(String processDefinitionKey, HashMap<String, Object> parameters) {
        ProcessInstance f;
        try {
            f = engine.startProcessByDefinitionKey(processDefinitionKey, parameters);
        } catch (Exception e) {
            f = null;
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return f;
    }

    public Task getTaskDataByTaskID(String taskId) {
        return engine.getTaskDataByTaskID(taskId);
    }

    public Task getTaskDataByTaskID() {
        if (this.taskId != null) {
            return engine.getTaskDataByTaskID(this.taskId);
        }
        return null;
    }

    public void completeTask(String taskid, HashMap<String, Object> parameters) {
        try {
            engine.completeTask(taskid, parameters);
        } catch (Exception ex) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirecPag(TareaWF tarea) {
        session.setTaskID(tarea.getTarea().getId());
        session.setTitlePage(tarea.getTarea().getName());
        JsfUti.redirectFaces(tarea.getTarea().getFormKey());
    }

    public void redirecTask(TareaWF tarea) {
        session.setTaskID(tarea.getTaskId());
        session.setTitlePage(tarea.getName());
        JsfUti.redirectFaces(tarea.getFormKey());
    }

    public void redirectLogin() {
        JsfUti.redirectFaces2(SisVars.urlbase + "faces/login.xhtml");
    }

    public void continuar() {
        JsfUti.redirectFaces("/procesos/dashBoard.xhtml");
    }

    public void eliminarFile(Archivo doc) {
        if (Utils.isNotEmpty(files)) {
            files.remove(doc);
        }
    }

    public ArrayList<Archivo> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<Archivo> files) {
        this.files = files;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public List<Attachment> getAttachmentsFiles() {
        return this.engine.getAttachmentsFiles(this.getTaskId());
    }

    public List<Attachment> getAttachmentsFiles(String taskId) {
        return this.engine.getAttachmentsFiles(taskId);
    }

    public void showDocument(String url) {
        JsfUti.redirectNewTab(url);
    }

    public Object getVariable(String taskid, String varName) {
        return this.engine.getvariable(taskid, varName);
    }

    public Object getVariableByPorcessIntance(String processInstanceId, String varName) {
        return this.engine.getVariableByProcessInstance(processInstanceId, varName);
    }

    public void setVariableByProcessInstance(String processInstanceId, String varName, Object value) {
        this.engine.setVariableProcessInstance(processInstanceId, varName, value);
    }

    public List<HistoricTaskInstance> getEndedUsertasksList(String asignee) {
        return this.engine.getEndedUsertasksList(asignee);
    }

    /*public ResTareasUsuarios getResumenTareasUsuarios(String usuario, String mail) {
        ResTareasUsuarios res = new ResTareasUsuarios();
        try {
            res.setUser(usuario);
            res.setMailUser(mail);
            res.setCompletedTasks(this.getEndedUsertasksList(usuario).size());
            res.setCurrentTasks(this.obtenerTareasUsuarios(usuario).size());
            res.setTasks(this.getListaTareasPersonales(usuario, null));
        } catch (Exception ex) {
            res = null;
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }*/
    public List<HistoricTaskInstance> getTaskByProcessInstanceId(String processInstanceId) {
        return engine.getTaskByProcessInstanceId(processInstanceId);
    }

    public List<HistoricTaskInstance> getTaskByProcessInstanceIdMain(String processInstanceId) {
        return engine.getTaskByProcessInstanceIdMain(processInstanceId);
    }

    public HistoricTaskInstance getTaskByProcessInstance(String processInstanceId) {
        return engine.getLastTaskByProcessInstance(processInstanceId);
    }

    public List<Attachment> getProcessInstanceAttachmentsFiles(String processInstanceId) {
        return this.engine.getProcessInstanceAttachmentsFiles(processInstanceId);
    }

    public List<Attachment> getProcessInstanceAllAttachmentsFiles(String processInstanceId) {
        return this.engine.getAttachmentsFilesByProcessInstanceIdMain(processInstanceId);
    }

    public List<Attachment> getProcessInstanceAttachmentsFiles() {
        if (this.getTaskDataByTaskID() != null) {
            return this.engine.getProcessInstanceAttachmentsFiles(this.getTaskDataByTaskID().getProcessInstanceId());
        } else {
            return null;
        }
    }

    public List<Attachment> getProcessInstanceAllAttachmentsFiles() {
        if (this.getTaskDataByTaskID() != null) {
            Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
            //HistoricoTramites ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteById, new String[]{"id"}, new Object[]{idHistTram});
            map = new HashMap();
            map.put("numTramite", tramite);
            HistoricoTramites ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
            return this.engine.getAttachmentsFilesByProcessInstanceIdMain(ht.getIdProceso());
        } else {
            return null;
        }
    }

    public List<Attachment> getProcessInstanceAllAttachments() {
        if (this.getTaskDataByTaskID() != null) {
            Long idHistTram = (Long) this.getVariable(session.getTaskID(), "tramite");
            HistoricoTramites ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteById, new String[]{"id"}, new Object[]{idHistTram});
            return this.engine.getAttachmentsFilesByProcessInstanceIdMain(ht.getIdProceso());
        } else {
            return null;
        }
    }

    public List<Attachment> getProcessInstanceAllAttachments(Long id) {
        HistoricoTramites ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteById, new String[]{"id"}, new Object[]{id});
        return this.engine.getAttachmentsFilesByProcessInstanceIdMain(ht.getIdProceso());
    }

    public Object getvariableByExecutionId(String taskId, String varName) {
        return engine.getvariableByExecutionId(taskId, varName);
    }

    public Document attachDocument(String carpeta, String fileName, String mimetype, byte[] content) {
        CmisUtil cmisu;
        try {
            cmisu = (CmisUtil) ApplicationContextUtils.getBean("cmisUtil");
            if (cmisu != null) {
                return cmisu.createDocument(cmisu.getFolder(carpeta), fileName, mimetype, content);
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public void descargarDocumento(String url) {
        String s[] = url.split("nodeRef=");
        JsfUti.redirectNewTab(SisVars.urlbase + "DescargarDocsRepositorio?id=" + s[1]);
    }

    public void mostrarDocumento(String url) {
        String s[] = url.split("nodeRef=");
        JsfUti.redirectNewTab(SisVars.urlbase + "AbrirDocsRepositorio?id=" + s[1]);
    }

    public List<Task> obtenerTareasActivasProcessInstance(String processInstanceId) {
        return this.engine.getListTaskActiveByProcessInstance(processInstanceId);
    }

    public List<IdentityLink> obtenerIdentityLinkByIdTask(String taskId) {
        return this.engine.identityLinkPorTareaId(taskId);
    }

    public List<HistoricIdentityLink> obtenerHistoricIdentityLinkByIdTask(String taskId) {
        return this.engine.HistoricidentityLinkPorTareaId(taskId);
    }

    public List<String> obtenerProcessInstanceByProcessInstaceIdMain(String processInstaceId) {
        return this.engine.getListProcessInstanceIdsByProcessInstanceIdMain(processInstaceId);
    }

    public List<Attachment> getListAttachment() {
        return listAttachment;
    }

    public void setListAttachment(List<Attachment> listAttachment) {
        this.listAttachment = listAttachment;
    }

    public List<Attachment> getListAttAttachmentByProcessInstance() {
        String processInstance = this.getProcessInstanceByTareaID(taskId);
        return this.getProcessInstanceAllAttachmentsFiles(processInstance);
    }

    public String usuariosCandidatos(String idTask) {
        try {
            String candidatosAsignados = "";
            if (idTask != null) {
                for (IdentityLink identityLink : this.obtenerIdentityLinkByIdTask(idTask)) {
                    candidatosAsignados = candidatosAsignados + ", " + identityLink.getUserId();
                }
                if (!candidatosAsignados.isEmpty()) {
                    return candidatosAsignados.substring(2);
                }
            }
            return "";
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, ("Error en tarea " + idTask), e);
            return "";
        }
    }

    public Object getVariables(String taskId, String variable) {
        if (taskId != null) {
            Map<String, Object> v = engine.getVar(taskId);
            for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                if (entrySet.getKey().compareTo(variable) == 0) {
                    return entrySet.getValue();
                }
            }
        }

        return null;
    }

    public Integer getCantidadTareasUser(String usuario) {
        return engine.getNumberTasksUser(usuario, null);
    }

    public Integer getCantidadTareasUser(String usuario, String taskDefKey) {
        return engine.getNumberTasksUser(usuario, taskDefKey);
    }

    public void deleteProcessInstance(String instance) {
        this.engine.deleteProcessInstance(instance, "Eliminado por usuario - " + (new Date()).getTime());
    }

    public Boolean deleteProcessInstance(String instance, String observacion) {
        try {
            Thread.sleep(1000);
            this.engine.deleteProcessInstance(instance, observacion);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public byte[] getByteArray(String ruta) {
        File file = new File(ruta);
        return file.getAbsolutePath().getBytes();
    }

    public Task getTaskByProcessId(String processId) {
        return engine.getTaskDataByProcessID(processId);
    }

    public Object getExecutionVariable(String taskid, String instanceId, String varName) {
        return this.engine.getvariableByExecutionId(taskid, instanceId, varName);
    }

    public HistoricoTramites getBpmTramite() {
        return bpmTramite;
    }

    public void setBpmTramite(HistoricoTramites bpmTramite) {
        this.bpmTramite = bpmTramite;
    }

    public void removerContenido(Object valor, Collection listado) {
        listado.remove(valor);
    }

    public byte[] leerArchivo(String ruta) throws Exception {
        Path path = Paths.get(ruta);
        byte[] data = Files.readAllBytes(path);
        return data;
    }

    protected void cargarArchivos(String indexacion, Long tramite) {
        try {
            archivos = doc.buscarArchivos(Constantes.indexacionHabilitantes, tramite.toString());
        } catch (Exception e) {
            e.toString();
        }
    }

    public void visualizarDocsTramite(Long numTramite) {
        try {
            if (numTramite != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion="
                        + Constantes.indexacionHabilitantes + "&tramite=" + numTramite.toString());
            } else {
                JsfUti.messageWarning(null, "No se puede visualizar documentos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void visualizarDocsTramite(Long numTramite, Integer posicion) {
        try {
            if (numTramite != null) {
                /*JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion="
                        + Constantes.indexacionHabilitantes + "&tramite=" + numTramite.toString() + "&indice=" + posicion.toString());*/
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/digitalizacion/reviewDocs.xhtml?transaccion="
                        + Constantes.indexacionHabilitantes + "&tramite=" + numTramite.toString() + "&indice=" + posicion.toString());
            } else {
                JsfUti.messageWarning(null, "No se puede visualizar documentos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void showDlg(String urlFacelet) {
        try {
            if (!urlFacelet.startsWith("/")) {
                urlFacelet = "/" + urlFacelet;
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "60%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, taskId, e);
        }
    }

    public void showDlgWith(String urlFacelet) {
        if (!urlFacelet.startsWith("/")) {
            urlFacelet = "/" + urlFacelet;
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "75%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
//        RequestContext.getCurrentInstance().openDialog(urlFacelet, options, null);
    }

    public void showDlgWithParams(String urlFacelet, Map<String, List<String>> params) {
        if (!urlFacelet.startsWith("/")) {
            urlFacelet = "/" + urlFacelet;
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "75%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, params);
    }

    public void showDlgDocuments(String urlFacelet) {
        try {
            if (!urlFacelet.startsWith("/")) {
                urlFacelet = "/" + urlFacelet;
            }
            java.util.Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "900");
            options.put("height", "500");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            options.put("contentHeight", "100%");
            org.primefaces.PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    protected List<String> addParam(Object b) {
        if (b == null) {
            return null;
        }
        List<String> p = new ArrayList<>(1);
        p.add(b.toString());
        return p;
    }

    public String cantidadstring(String cantidad) {
        NumeroLetra n = new NumeroLetra();
        return n.Convertir(cantidad, true) + " CON " + cantidad.substring(cantidad.indexOf('.') + 1) + "/100";
    }

    public StreamedContent imagenProceso(String id, String name) {
        try {
            return DefaultStreamedContent.builder().contentType("image/png").stream(() -> engine.getProcessDiagram(id, name)).build();
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public List<RegFicha> getFichasHT(Long tramite) {
        List<RegFicha> fichas;
        try {
            fichas = manager.findAll(Querys.getFichaFromTramite, new String[]{"numero"}, new Object[]{tramite});
            RegFicha temp = (RegFicha) manager.find(Querys.getFichaFromLiquidacion, new String[]{"numero"}, new Object[]{tramite});
            if (temp != null) {
                fichas.add(temp);
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return fichas;
    }

    public void agregarFichaTramite(Long numTramite, RegFicha ficha, HistoricoTramites historicoTramites) {
        if (Utils.isEmpty(getFichasHT(numTramite))) {
            RegFichaMarginacion fichaMarginacion = new RegFichaMarginacion();
            fichaMarginacion.setFicha(ficha);
            fichaMarginacion.setTramite(historicoTramites);
            fichaMarginacion.setFechaIngreso(new Date());
            fichaMarginacion.setUserIngreso(session.getUserId());
            manager.persist(fichaMarginacion);
        }
    }

    public boolean validarRepertorio(Date fechaRepertorio) {
        Boolean caduco = false;
        try {
            map = new HashMap();
            map.put("code", Constantes.diasValidezRepertorio);
            Valores dias = (Valores) manager.findObjectByParameter(Valores.class, map);
            Date temporal = Utils.sumarRestarDiasFecha(fechaRepertorio, dias.getValorNumeric().intValue());
            //System.out.println("dias " + dias.toString());
            //System.out.println("temporal " + temporal);
            if (temporal.before(new Date())) {
                caduco = true;
                JsfUti.messageError(null, "Repertorio Caducado", "Debe generar nuevo Repertorio.");
            }
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
        }
        return caduco;
    }

    public void downLoadDocument(Long oid) {
        try {
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El documento aún no ha sido adjuntado", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                        + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void showProforma(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGger.log(Level.SEVERE, null, e);
        }
    }
    
    public void showTituloCredito(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("titulo_credito");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
            ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
    }
    
    public void descargarAdjuntos(Long numTramite) {
        try {
            map = new HashMap();
            map.put("tramite.numTramite", numTramite);
            PubSolicitud solicitud = (PubSolicitud) manager.findObjectByParameter(PubSolicitud.class, map);
            if (solicitud != null && solicitud.getOidDocument() != null) {
                String ruta = SisVars.rutaTemporales;
                ruta = ruta + numTramite.toString() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(solicitud.getOidDocument(), fos);
                    fos.close();
                }
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "No se encuentra el documento adjunto del usuario.", "");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
