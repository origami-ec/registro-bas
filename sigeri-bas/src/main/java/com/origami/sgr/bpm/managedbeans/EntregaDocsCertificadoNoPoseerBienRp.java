/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class EntregaDocsCertificadoNoPoseerBienRp extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private ServletSession ss;

    private static final Long serialVersionUID = 2L;

    private static final Logger LOG = Logger.getLogger(EntregaDocsRp.class.getName());

    @Inject
    protected RegistroPropiedadServices reg;

    protected String cedula;
    protected CatEnte solicitante = new CatEnte();
    protected HashMap<String, Object> par;
    protected String formatoArchivos;
    protected String observacion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    private Boolean retiraDocumentoEsSolicitante;
    private Observaciones ob;

    @Inject
    private RegistroPropiedadServices rps;
    private List<Observaciones> observacionesTramites;

    @PostConstruct
    protected void iniView() {
        try {
            retiraDocumentoEsSolicitante = Boolean.FALSE;
            formatoArchivos = SisVars.formatoArchivos;
            Long tramite = (Long) ss.getParametros().get("tramite");
            if (tramite != null) {
                map = new HashMap();
                map.put("numTramite", tramite);
                ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                observacionesTramites = rps.listarObservacionesPorTramite(ht);
                if (Utils.isEmpty(observacionesTramites)) {
                    observacionesTramites = new ArrayList();
                }
            } else {
                JsfUti.redirectFaces("/procesos/manage/entregaDocumentos.xhtml");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscar() {
        if (solicitante.getCiRuc() != null || solicitante.getCiRuc().isEmpty()) {
            if (!solicitante.getCiRuc().isEmpty()) {
                cedula = solicitante.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    solicitante = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                }
                if (solicitante == null || solicitante.getId() == null) {
                    solicitante = reg.buscarGuardarEnteDinardap(cedula);
                }
            }
            if (solicitante == null) {
                solicitante = new CatEnte();
            }
            if (solicitante.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            }
            JsfUti.update("mainForm:mainTab:panelSolicitante");
        }
    }

    public void limpiarDatosSolicitante() {
        solicitante = new CatEnte();
        cedula = "";
    }

    public void selectObject(SelectEvent event) {
        solicitante = (CatEnte) event.getObject();
    }

    public void actualizarBeneficiarioEsSolicitante() {
        if (retiraDocumentoEsSolicitante) {
            solicitante = liquidacion.getSolicitante();
        } else {
            solicitante = new CatEnte();
            retiraDocumentoEsSolicitante = Boolean.FALSE;
        }
    }

    public void guardarObservacion() {
        try {
            String temp = "Persona que retira los documentos, CI/RUC: " + solicitante.getCiRuc() + ", Nombre: " + solicitante.getNombreCompleto();
            reg.guardarObservaciones(ht, session.getName_user(), temp, Constantes.certificadoExpress);
            ht.setEntregado(Boolean.TRUE);
            manager.update(ht);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            if (solicitante.getId() != null) {
                this.guardarObservacion();
                JsfUti.redirectFaces("/procesos/manage/entregaDocumentos.xhtml");
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar la persona que retira los Documentos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
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

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public Boolean getRetiraDocumentoEsSolicitante() {
        return retiraDocumentoEsSolicitante;
    }

    public void setRetiraDocumentoEsSolicitante(Boolean retiraDocumentoEsSolicitante) {
        this.retiraDocumentoEsSolicitante = retiraDocumentoEsSolicitante;
    }

}
