/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.session.UserSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class CorreccionDocs implements Serializable {

    private static final Logger LOG = Logger.getLogger(CorreccionDocs.class.getName());

    @EJB(beanName = "documentsManaged")
    private DocumentsManagedLocal doc;

    @Inject
    private UserSession us;

    @EJB(beanName = "manager")
    private Entitymanager em;

    @EJB(beanName = "registroPropiedad")
    private RegistroPropiedadServices reg;

    protected Map map;
    protected Long tramite;
    protected HistoricoTramites ht;
    protected RegpTareasTramite tarea;
    protected List<RegpTareasTramite> tareas;

    @PostConstruct
    protected void iniView() {
        try {
            ht = new HistoricoTramites();
            tarea = new RegpTareasTramite();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findTramite() {
        try {
            if (tramite != null) {
                map = new HashMap();
                map.put("numTramite", tramite);
                ht = (HistoricoTramites) em.findObjectByParameter(HistoricoTramites.class, map);
                if (ht != null) {
                    tareas = reg.getTareasTramite(ht.getId());
                } else {
                    ht = new HistoricoTramites();
                    JsfUti.messageWarning(null, "No se encontro el tramite.", "");
                }
                JsfUti.update("mainForm");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de tramite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocument(RegpTareasTramite tt) {
        if (tt.getRealizado()) {
            tarea = tt;
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        } else {
            JsfUti.messageWarning(null, "Debe completarse primero las Tareas de Certificacion o Inscripcion del tramite.", "");
        }
    }

    public void uploadDocTareaTramite(FileUploadEvent event) throws IOException {
        try {
            if (tarea.getId() != null) {
                if (doc.saveDocumentoTarea(event.getFile(), tarea, us.getUserId())) {
                    tarea.setReemplazo(Boolean.TRUE);
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgObsvs').hide();");
                    JsfUti.messageInfo(null, "Documento cargado con exito!!!", "");
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
    
    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
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

}
