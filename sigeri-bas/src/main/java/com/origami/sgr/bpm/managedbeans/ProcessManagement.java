package com.origami.sgr.bpm.managedbeans;

import com.origami.sgr.entities.GeTipoTramite;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.repository.ProcessDefinition;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Origami
 *
 */
@Named
@ViewScoped
public class ProcessManagement extends BpmManageBeanBaseRoot implements Serializable {

    private List<GeTipoTramite> procedures = null, selProcedures;
    private List<ProcessDefinition> process = null;
    private StreamedContent stream;
    private GeTipoTramite tipoTramite;

    @PostConstruct
    public void iniView() {
        tipoTramite = new GeTipoTramite();
        procedures = manager.findAll(Querys.getGeTipoTramites);
    }

    public List<ProcessDefinition> getProcessDeployments() {
        try {
            if (procedures != null) {
                process = new ArrayList<>();
                List<ProcessDefinition> pro = this.getProcesosDesplegados();
                pro.forEach((p) -> {
                    for (GeTipoTramite t : procedures) {
                        if (p.getKey().equals(t.getActivitykey().trim())) {
                            process.add(p);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
        return process;
    }

    public void loadProcess() {
        try {
            if (selProcedures != null) {
                if (!selProcedures.isEmpty()) {
                    selProcedures.forEach((p) -> {
                        this.loadProcessByClassPath(p.getArchivoBpmn());
                    });
                    JsfUti.update("formMain:tdatos:dtprocessDep");
                    JsfUti.messageInfo(null, "Nota", "Se desplegaron " + selProcedures.size() + " proceso(s) satisfactoriamente.");
                } else {
                    JsfUti.messageWarning(null, "Advertencia", "Debe elegir al menos un proceso del listado");
                }
            } else {
                JsfUti.messageWarning(null, "Advertencia", "Debe elegir al menos un proceso del listado");
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void cargarProceso(GeTipoTramite tipo) {
        try {
            if (!tipo.getEstado()) {
                JsfUti.messageWarning(null, "Advertencia", "No se puede cargar proceso Inactivo.");
                return;
            }
            if (tipo.getArchivoBpmn() != null) {
                this.loadProcessByClassPath(tipo.getArchivoBpmn());
                JsfUti.update("formMain:tdatos:dtprocessDep");
                JsfUti.messageInfo(null, "Nota", "Se desplego el proceso (" + tipo.getActivitykey() + ") satisfactoriamente.");
            } else {
                JsfUti.messageWarning(null, "Advertencia", "No se encuentra ruta de archivo bpmn.");
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDiagram(String id, String name) {
        try {
            stream = this.imagenProceso(id, name);
            JsfUti.update("frmDiagrama");
            JsfUti.executeJS("PF('dlgDiagrama').show();");
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgProcess() {
        JsfUti.update("formProcess");
        JsfUti.executeJS("PF('dlgProcess').show();");
    }

    public void editProcess(GeTipoTramite gtp) {
        tipoTramite = gtp;
        this.showDlgProcess();
    }

    public void newProcess() {
        tipoTramite = new GeTipoTramite();
        this.showDlgProcess();
    }

    public void saveProcess() {
        try {
            if (this.validar()) {
                manager.persist(tipoTramite);
                procedures = manager.findAll(Querys.getGeTipoTramites);
                JsfUti.update("formMain");
                JsfUti.executeJS("PF('dlgProcess').hide();");
                JsfUti.messageInfo(null, "Mensaje:", "Transaccion con exito.");
            } else {
                JsfUti.messageWarning(null, "Advertencia", "Todos lo campos deben ser ingresados.");
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean validar() {
        if (tipoTramite.getDescripcion().isEmpty()) {
            return false;
        }
        if (tipoTramite.getActivitykey().isEmpty()) {
            return false;
        }
        if (tipoTramite.getArchivoBpmn().isEmpty()) {
            return false;
        }
        if (tipoTramite.getAbreviatura().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<GeTipoTramite> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<GeTipoTramite> procedures) {
        this.procedures = procedures;
    }

    public List<GeTipoTramite> getSelProcedures() {
        return selProcedures;
    }

    public void setSelProcedures(List<GeTipoTramite> selProcedures) {
        this.selProcedures = selProcedures;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    public GeTipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(GeTipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

}
