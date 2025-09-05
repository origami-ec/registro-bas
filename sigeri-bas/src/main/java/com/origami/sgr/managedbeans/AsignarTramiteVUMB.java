/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.DepartamentoUsuario;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.SolicitudServicios;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class AsignarTramiteVUMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(AsignarTramiteVUMB.class.getName());
    
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    private List<HistoricoTramites> historicoTramite;
    private HistoricoTramites ht;
    protected String observaciones;
    private SolicitudServicios solicitudServicios;
    private List<DepartamentoUsuario> usuariosRol;
    private DepartamentoUsuario usuarioSeleccionado;
    private List<GeDepartamento> departamentos;
    private GeDepartamento departamento;
    private Observaciones observacion;
    protected HashMap<String, Object> par;

    @PostConstruct
    public void initView() {
        if (this.session.getTaskID() != null) {
            if (!JsfUti.isAjaxRequest()) {
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();

                map = new HashMap();
                map.put("numTramite", tramite);
                ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);

                map = new HashMap();
                map.put("tramite.id", ht.getId());
                solicitudServicios = (SolicitudServicios) manager.findObjectByParameter(SolicitudServicios.class, map);

                observacion.setIdTramite(ht);
                loadModel();
                docs = itl.getDocumentosTramite(ht.getId());
            }
        }
    }

    private void loadModel() {
        departamentos = manager.findAllEntCopy(Querys.getGeDepartamentos);
        departamento = new GeDepartamento();
        usuarioSeleccionado = new DepartamentoUsuario();
        if (!Utils.isEmpty(session.getRoles())) {
            //usuariosRol = ventanillaService.findAllUsuariosRolByDepartamento(departamento, null, null, null, null);
        }
    }

    public void loadUsuariosDepartamento() {
        if (departamento != null && departamento.getId() != null) {
            usuarioSeleccionado = new DepartamentoUsuario();
            map = new HashMap();
            map.put("departamento.id", departamento.getId());
            usuariosRol = manager.findObjectByParameterList(DepartamentoUsuario.class, map);
        }
    }

    /*Método para abrir el dialogo de observación */
    public void abriDialog() {
        if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
            JsfUti.messageError(null, "", "Debe seleccionar un usuario");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(this.getTaskDataByTaskID().getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    /*Método para abrir el dialogo de detalles */
    public void openDialogDetalles(HistoricoTramites item) {
        PrimeFaces.current().executeScript("PF('dlgDet').show()");
        PrimeFaces.current().ajax().update("frmDet");
    }

    public void completarTarea() {
        try {
            if (validar()) {
//                getParamts().put("usuario_2", usuarioSeleccionado.getUsuario().getUsuario());
//                observacion.setObservacion(observaciones);
//                if (saveTramite() == null) {
//                    return;
//                }
//                usuarioSeleccionado = new UsuarioRol();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    private Boolean validar() {
        if (Utils.isEmptyString(observaciones)) {
            JsfUti.messageError(null, "", "Ingrese una observación");
            return Boolean.FALSE;
        }
        if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
            JsfUti.messageError(null, "", "Escoja un usuario");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public DepartamentoUsuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(DepartamentoUsuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

    public List<DepartamentoUsuario> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<DepartamentoUsuario> usuariosRol) {
        this.usuariosRol = usuariosRol;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public List<HistoricoTramites> getHistoricoTramite() {
        return historicoTramite;
    }

    public void setHistoricoTramite(List<HistoricoTramites> historicoTramite) {
        this.historicoTramite = historicoTramite;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//</editor-fold>

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

}
