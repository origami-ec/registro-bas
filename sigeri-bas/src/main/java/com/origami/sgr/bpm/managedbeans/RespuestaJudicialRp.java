/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpRespuestaJudicial;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class RespuestaJudicialRp extends BpmManageBeanBaseRoot implements Serializable {

    public static final Long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(DigitalizarDocsRp.class.getName());

    @EJB(beanName = "documentsManaged")
    private DocumentsManagedLocal doc;
    @EJB(beanName = "ingreso")
    private IngresoTramiteLocal itl;

    protected HashMap<String, Object> par;
    protected String formatoArchivos;
    protected String observacion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected RegpRespuestaJudicial rrj = new RegpRespuestaJudicial();

    @PostConstruct
    protected void iniView() {
        try {
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
                    docs = itl.getDocumentosTramite(ht.getId());
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            this.continuar();
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab("/sgr/OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo() 
                        + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocRespuesta() {
        if (rrj.getNumeroOficio() != null && observacion != null) {
            JsfUti.update("formDocs");
            JsfUti.executeJS("PF('dlgDocs').show();");
        } else {
            JsfUti.messageWarning(null, "Debe llenar los campos de numero de oficio y respuesta/observacion.", "");
        }
    }

    public void handleUpload(FileUploadEvent event) throws IOException {
        try {
            rrj.setRespuesta(observacion);
            rrj.setUsuario(session.getUserId());
            rrj.setTramite(ht);
            if (doc.saveDocRespuestaJudicial(event.getFile(), rrj)) {
                par = new HashMap<>();
                //par.put("entrega", 2);
                par.put("entrega", 1);
                this.completarTarea();
            } else {
                JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void pasarSinRespuesta() {
        try {
            if (observacion != null) {
                rrj.setRespuesta(observacion);
                rrj.setUsuario(session.getUserId());
                rrj.setFecha(new Date());
                rrj.setTramite(ht);
                manager.persist(rrj);
                par = new HashMap<>();
                par.put("entrega", 1);
                this.completarTarea();
            } else {
                JsfUti.messageWarning(null, "Debe llenar el campo de respuesta/observacion.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void completarTarea() {
        try {
            this.guardarObservacion();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }

    }

    public void guardarObservacion() {
        try {
            if (observacion != null) {
                Observaciones ob = new Observaciones();
                ob.setObservacion(observacion);
                ob.setEstado(true);
                ob.setFecCre(new Date());
                ob.setIdTramite(ht);
                ob.setTarea(this.getTaskDataByTaskID().getName());
                ob.setUserCre(session.getName_user());
                manager.persist(ob);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

    public RegpRespuestaJudicial getRrj() {
        return rrj;
    }

    public void setRrj(RegpRespuestaJudicial rrj) {
        this.rrj = rrj;
    }

}
