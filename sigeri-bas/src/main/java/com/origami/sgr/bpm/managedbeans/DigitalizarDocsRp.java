/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DigitalizarDocsRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(DigitalizarDocsRp.class.getName());

    @Inject
    private IngresoTramiteLocal itl;

    @Inject
    private DocumentsManagedLocal doc;

    @Inject
    private RegistroPropiedadServices reg;

    protected HashMap<String, Object> par;
    protected String formatoArchivos;
    protected String observacion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected Boolean tareaInicio = true;
    protected List<RegpTareasTramite> tareas;
    protected RegpTareasTramite tarea = new RegpTareasTramite();
    private String userName;

    @PostConstruct
    protected void iniView() {
        try {
            tareaInicio = true;
            formatoArchivos = SisVars.formatoArchivos;
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    tareas = reg.getTareasTramite(ht.getId());
                } else {
                    this.continuar();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            this.continuar();
        }
    }

    public void handleUpload(FileUploadEvent event) throws IOException {
        try {
            if (doc.saveDocumentoHabilitante(event.getFile(), ht, session.getUserId())) {
                this.completarTareaDigitalizacion();
                JsfUti.messageInfo(null, "Archivo subido correctamente!!!", "");
            } else {
                JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void completarTareaDigitalizacion() {
        try {
            userName = "";
            if (!liquidacion.getEsJuridico()) {
                par = new HashMap<>();
                if (liquidacion.getInscriptor() != null) {
                    par.put("secretaria", itl.getCandidateUserByRolName("secretario_registral"));
                    par.put("entregaDocumentos", itl.getCandidateUserByRolName("entrega_documento"));
                    par.put("inscriptor", liquidacion.getInscriptor().getUsuario());
                    par.put("certificador", liquidacion.getInscriptor().getUsuario());
                    userName =  liquidacion.getInscriptor().getUsuario();
                } else {
                    JsfUti.messageError(null, Messages.error, "No tiene usuario asignado.");
                    return;
                }
            } else {
                par = new HashMap<>();
                par.put("secretariaJuridico", itl.getCandidateUserByRolName("secretaria_juridico"));
                userName = itl.getCandidateUserByRolName("secretaria_juridico");
            }
            //System.out.println("Data: " + userName);
            this.guardarObservacion();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            JsfUti.update("formDlgResult");
            JsfUti.executeJS("PF('dlgResult').show()");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    private void guardarObservacion() {
        try {
            reg.guardarObservaciones(ht, session.getName_user(), this.getTaskDataByTaskID().getName(), 
                    this.getTaskDataByTaskID().getName());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void uploadDocTareaTramite(FileUploadEvent event) throws IOException {
        try {
            if (tarea.getId() != null) {
                if (doc.saveDocumentoTarea(event.getFile(), tarea, session.getUserId())) {
                    tareas = reg.getTareasTramite(ht.getId());
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgObsvs').hide();");
                    JsfUti.messageInfo(null, "Documento cargado con exito!!!", "");
                    tarea = new RegpTareasTramite();
                } else {
                    JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
                }
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Boolean getTareaInicio() {
        return tareaInicio;
    }

    public void setTareaInicio(Boolean tareaInicio) {
        this.tareaInicio = tareaInicio;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public RegpTareasTramite getTarea() {
        return tarea;
    }

    public void setTarea(RegpTareasTramite tarea) {
        this.tarea = tarea;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
