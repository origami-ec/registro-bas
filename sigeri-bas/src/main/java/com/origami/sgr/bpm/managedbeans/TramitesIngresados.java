/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.lazymodels.RegpLiquidacionLazy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.math.BigInteger;
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

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class TramitesIngresados extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(TramitesIngresados.class.getName());

    protected HistoricoTramites ht;
    protected RegpLiquidacionLazy liquidaciones;
    protected RegpLiquidacion liquidacion;
    protected RegRegistrador registrador;
    protected Boolean permitido = false;
    protected Boolean prioridad = false;
    protected List<Attachment> listAttach = new ArrayList<>();
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected HistoricTaskInstance tareaActual;
    protected List<AclUser> users = new ArrayList<>();
    protected Boolean showUsers = false;
    protected Long usuario = 0L;
    protected String observaciones;
    protected String formatoArchivos;
    protected int priority = 0;
    protected String obsDesblock;
    protected String obsPriority;

    protected List<RegpDocsTarea> docs = new ArrayList<>();
    protected Boolean hasDoc = false, online = false;

    @PostConstruct
    protected void iniView() {
        try {
            formatoArchivos = SisVars.formatoArchivos;
            usuario = session.getUserId();
            ht = new HistoricoTramites();
            liquidacion = new RegpLiquidacion();
            liquidaciones = new RegpLiquidacionLazy();
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
            this.cargarUsuariosRegistro();
            this.validaRoles();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        //administrador
        session.getRoles().stream().filter(l -> (l == 1)).forEachOrdered(_item -> {
            permitido = true;
        });
    }

    public void showComprobanteIngreso(RegpLiquidacion re) {
        try {
            if (re.getEstadoLiquidacion().getId() == 2L) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setImprimir(Boolean.TRUE);
                if (re.getGeneraFactura()) {
                    ss.setNombreReporte("comprobante_factura");
                } else {
                    ss.setNombreReporte("comprobante_factura_exonerada");
                }
                //ss.setNombreReporte("comprobante_factura");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", re.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "La proforma no ha sido ingresada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showInfo(RegpLiquidacion re) {
        try {
            //hasDoc = true;
            online = false;
            ht = re.getTramite();
            liquidacion = re;
            this.cargarArchivos(Constantes.indexacionHabilitantes, re.getNumTramiteRp());
            if (liquidacion.getTramite().getIdProceso() == null) {
                tareas = new ArrayList<>();
                listAttach = new ArrayList<>();
            } else {
                tareas = this.getTaskByProcessInstanceIdMain(liquidacion.getTramite().getIdProceso());
                if (!tareas.isEmpty()) {
                    tareaActual = tareas.get(0);
                    priority = tareaActual.getPriority();
                    /*if (tareas.get(0).getEndTime() != null) {
                        JsfUti.messageInfo(null, "TRAMITE FINALIZADO", "El tramite " + liquidacion.getNumTramiteRp() + " ha Finaliado su proceso.");
                    }*/
                }
                if (liquidacion.getEstadoPago().getId() == 7L) {
                    online = true;
                }
            }
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocHabilitante() {
        try {
            if (ht.getId() != null) {
                RegpDocsTramite rdt = (RegpDocsTramite) manager.find(Querys.getDocsTramiteByTramite, new String[]{"idTramite"}, new Object[]{ht.getId()});
                if (rdt != null) {
                    JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                            + "&tipo=3&content=" + rdt.getContentType());
                } else {
                    JsfUti.messageWarning(null, "NO se encuentra documento habilitante!!!", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocTareas(RegpDocsTarea rdt) {
        try {
            if (rdt.getId() != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                        + "&tipo=2&content=" + rdt.getContentType());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarPrioridad() {
        try {
            if (tareaActual.getProcessInstanceId() != null && priority > 0) {
                this.setVariableByProcessInstance(tareaActual.getProcessInstanceId(), "prioridad", prioridad);
                List<Task> tareasActivas = this.obtenerTareasActivasProcessInstance(tareaActual.getProcessInstanceId());
                this.asignarTareaPriority(tareasActivas, priority);
                reg.guardarObservaciones(ht, session.getName_user(), obsPriority == null ? "PRIORIDAD: " + priority : obsPriority, "PRIORIDAD TRAMITE");
                JsfUti.messageInfo(null, "Prioridad de tramite actualizada.", "");
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void updateBlockHt() {
        try {
            if (ht.getId() != null) {
                ht.setUserDesblock(session.getUserId());
                ht.setFechaDesblock(new Date());
                manager.update(ht);
                reg.guardarObservaciones(ht, session.getName_user(), obsDesblock == null ? "DESBLOQUEAR TRAMITE" : obsDesblock, "DESBLOQUEAR TRAMITE");
                JsfUti.messageInfo(null, "Prioridad de tramite actualizada.", "");
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void editProforma(RegpLiquidacion re) {
        if (re.getEstadoLiquidacion().getId() == 1L) { // ESTADO LIQUIDACION INGRESADA
            ss.instanciarParametros();
            ss.agregarParametro("proforma", re.getId());
            JsfUti.redirectFaces("/procesos/registro/editarProforma.xhtml");
        } else {
            JsfUti.messageWarning(null, "Liquidacion no se puede Editar.", "");
        }
    }

    public void cargarUsuariosRegistro() {
        try {
            users = reg.getUsuariosByRolName("certificador");
            List<AclUser> temp = reg.getUsuariosByRolName("inscriptor");
            temp.forEach(u -> {
                users.add(u);
            });
            users.size();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgReAsignarUser(RegpLiquidacion re) {
        try {
            showUsers = true;
            ht = re.getTramite();
            if (ht.getIdProceso() == null) {
                JsfUti.messageError(null, "El tramite no ha sido Ingresado. No hay tarea para re-asignar.", "");
            } else {
                tareas = this.getTaskByProcessInstanceIdMain(ht.getIdProceso());
                if (tareas.size() > 0) {
                    tareaActual = tareas.get(0);
                    if (tareaActual.getTaskDefinitionKey().equalsIgnoreCase("analisisProcesoRegistral")
                            || tareaActual.getTaskDefinitionKey().equalsIgnoreCase("inscribirCertificar")) {
                        if (tareaActual.getEndTime() == null) {
                            if (tareaActual.getAssignee() == null) {
                                showUsers = false;
                                JsfUti.messageError(null, "Esta tarea no se puede re-asignar. Tiene usuarios candidatos.", "");
                            }
                            JsfUti.update("formreasignar");
                            JsfUti.executeJS("PF('dlgReasignar').show();");
                        } else {
                            JsfUti.messageError(null, "No se puede re asignar Tarea. Tarea Finalizada.", "");
                        }
                    } else {
                        JsfUti.messageWarning(null, "No se puede re asignar Tarea.", "Tarea actual: " + tareaActual.getName());
                    }
                } else {
                    JsfUti.messageError(null, "No se encontro tarea para reasignar.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reasignarTarea(AclUser user) {
        try {
            String obs = "TAREA: " + tareaActual.getName() + ", USUARIO ANTERIOR: " + tareaActual.getAssignee() + ", USUARIO ACTUAL: " + user.getUsuario();
            reg.guardarObservaciones(ht, session.getName_user(), obs, "REASIGNACION DE USUARIO");
            this.reasignarTarea(tareaActual.getId(), user.getUsuario());
            Map<String, Object> v = this.engine.getvariables(tareaActual.getProcessInstanceId());
            v.entrySet().stream().filter(entrySet -> (entrySet.getValue() != null && entrySet.getValue().equals(tareaActual.getAssignee()))).forEachOrdered(entrySet -> {
                this.setVariableByProcessInstance(tareaActual.getProcessInstanceId(), entrySet.getKey(), user.getUsuario());
            });
            JsfUti.executeJS("PF('dlgReasignar').hide();");
            JsfUti.update("mainForm:dtTramites");
            JsfUti.messageInfo(null, "Tarea Re-Asignada con exito.", "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showFormularioOnline() {
        try {
            if (ht.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("FormularioOnline");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_TRAMITE", ht.getId());
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadDocument() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{ht.getId()});
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    //Anula una factura: En el la tabla de regLiquidaciones en el estado de la factura le pone False
    public void inactivateInvoice(String observacion) {
        try {//valida que la observacion haya sido escrita
            if (observacion == null || observacion.equals("")) {
                JsfUti.messageError(null, "Debe Ingresar Observaciones", "");
                JsfUti.update("mainFormAnulada:tableAnulada");
            } else {
                RegpEstadoLiquidacion estadoLiquidacion = new RegpEstadoLiquidacion();
                estadoLiquidacion.setId(Long.parseLong("3"));
                estadoLiquidacion.setCode("ANULADA");
                liquidacion.setEstadoLiquidacion(estadoLiquidacion);
                liquidacion.setUserAnula(session.getUserId());
                liquidacion.setInfAdicional(observaciones);
                manager.update(liquidacion);
                JsfUti.messageInfo(null, Messages.liquidacionAnulada, "");
                JsfUti.update("mainFormAnulada:tableAnulada");
                observaciones = "";
            }
        } catch (NumberFormatException e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }

    }

    //busca liquidacion
    public void selectInvoice(RegpLiquidacion rl) {
        try {
            liquidacion = manager.find(RegpLiquidacion.class, rl.getId());

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion=" + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public RegpLiquidacionLazy getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(RegpLiquidacionLazy liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public List<AclUser> getUsers() {
        return users;
    }

    public void setUsers(List<AclUser> users) {
        this.users = users;
    }

    public Boolean getShowUsers() {
        return showUsers;
    }

    public void setShowUsers(Boolean showUsers) {
        this.showUsers = showUsers;
    }

    public HistoricTaskInstance getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(HistoricTaskInstance tareaActual) {
        this.tareaActual = tareaActual;
    }

    public List<Attachment> getListAttach() {
        return listAttach;
    }

    public void setListAttach(List<Attachment> listAttach) {
        this.listAttach = listAttach;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public Boolean getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Boolean prioridad) {
        this.prioridad = prioridad;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getObsDesblock() {
        return obsDesblock;
    }

    public void setObsDesblock(String obsDesblock) {
        this.obsDesblock = obsDesblock;
    }

    public String getObsPriority() {
        return obsPriority;
    }

    public void setObsPriority(String obsPriority) {
        this.obsPriority = obsPriority;
    }

    public List<RegpDocsTarea> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTarea> docs) {
        this.docs = docs;
    }

    public Boolean getHasDoc() {
        return hasDoc;
    }

    public void setHasDoc(Boolean hasDoc) {
        this.hasDoc = hasDoc;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

}
