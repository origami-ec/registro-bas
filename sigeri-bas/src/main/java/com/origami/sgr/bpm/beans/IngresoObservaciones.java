/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.RegpObservacionesIngreso;
import com.origami.sgr.lazymodels.RegpObservacionesLazy;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class IngresoObservaciones extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(IngresoObservaciones.class.getName());

    @EJB(beanName = "ingreso")
    private IngresoTramiteLocal itl;

    @Inject
    private UserSession us;

    @Inject
    private ServletSession ss;

    protected RegpObservacionesLazy lazyObvs;
    protected RegpObservacionesIngreso observacion;
    protected Date desde = new Date();
    protected Date hasta = new Date();
    protected List<AclUser> revisores = new ArrayList<>();
    protected AclUser user;
    protected Integer motivo = 1, reporte = 1;
    private Boolean guardar = false;

    @PostConstruct
    protected void iniView() {
        try {
            lazyObvs = new RegpObservacionesLazy();
            observacion = new RegpObservacionesIngreso();
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("nombre", "revisor");
            AclRol rol = (AclRol) manager.findObjectByParameter(AclRol.class, map);
            if (rol != null) {
                revisores = (List<AclUser>) rol.getAclUserCollection();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporteFecha() {
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDesde = sdf.format(desde);
                    String strHasta = sdf.format(hasta);
                    ss.instanciarParametros();
                    if (reporte == 1) {
                        ss.setNombreReporte("observacionesIngresadas");
                    }
                    if (reporte == 2) {
                        ss.setNombreReporte("proformasAceptadas");
                    }
                    ss.setTieneDatasource(true);
                    ss.setNombreSubCarpeta("diarios");
                    ss.agregarParametro("USUARIO", us.getName_user());
                    if (user == null) {
                        ss.agregarParametro("ID_USER", 0L);
                    } else {
                        ss.agregarParametro("ID_USER", user.getId());
                    }
                    ss.agregarParametro("DESDE", sdf.parse(strDesde));
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta, 1));
                    ss.agregarParametro("HASTA_STRING", strHasta);
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                    JsfUti.redirectNewTab("/sgr/Documento");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgObs() {
        guardar = true;
        observacion = new RegpObservacionesIngreso();
        JsfUti.executeJS("PF('dlgObvs').show();");
        JsfUti.update("frmObvs");
    }

    public void selectObject(SelectEvent event) {
        CatEnte en = (CatEnte) event.getObject();
        observacion.setEnte(en);
    }

    public void mostrarObservacion(RegpObservacionesIngreso obs) {
        observacion = new RegpObservacionesIngreso();
        observacion.setEnte(obs.getEnte());
        observacion.setObservacion1(obs.getObservacion1());
        observacion.setObservacion2(obs.getObservacion2());
        observacion.setObservacion3(obs.getObservacion3());
//        JsfUti.update("dlgObvs");
        JsfUti.update("frmObvs");

        JsfUti.executeJS("PF('dlgObvs').show();");

    }

    public void saveObservacion() {
        try {
            if (motivo == 1 && observacion.getEnte() == null) {
                JsfUti.messageWarning(null, Messages.solicitante, "");
            } else if (observacion.getObservacion1() == null) {
                JsfUti.messageWarning(null, "Debe ingresar datos en el campo Observacion.", "");
            } else {
                if (observacion.getId() == null) {
                    observacion.setFechaIngreso(new Date());
                    observacion.setUserIngreso(new AclUser(us.getUserId()));
                } else {
                    observacion.setFechaEdicion(new Date());
                    observacion.setUserEdicion(new AclUser(us.getUserId()));
                }
                if (motivo == 1) {
                    observacion.setEstado("C");
                } else if (motivo == 2) {
                    observacion.setEstado("O");
                }
                observacion = itl.saveObservacionIngreso(observacion);
                if (observacion != null) {
                    this.generarReporte(observacion);
                    lazyObvs = new RegpObservacionesLazy();
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgObvs').hide();");
                    //JsfUti.messageInfo(null, Messages.transaccionOK, "");
                } else {
                    JsfUti.messageError(null, Messages.error, "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporte(RegpObservacionesIngreso ob) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("ID_OBSERVACION", ob.getId());
            ss.setNombreReporte("reporteObservaciones");
            ss.setTieneDatasource(true);
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
            ss.setNombreSubCarpeta("ingreso");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editarObservacion(RegpObservacionesIngreso ob) {
        observacion = ob;
        JsfUti.executeJS("PF('dlgObvs').show();");
        JsfUti.update("frmObvs");
    }

    public void ingresarProforma(RegpObservacionesIngreso ob) {
        ss.instanciarParametros();
        ss.agregarParametro("idObservacion", ob.getId());
        JsfUti.redirectFaces("/procesos/registro/iniciarTramiteRp.xhtml");
    }

    public UserSession getUs() {
        return us;
    }

    public void setUs(UserSession us) {
        this.us = us;
    }

    public RegpObservacionesIngreso getObservacion() {
        return observacion;
    }

    public void setObservacion(RegpObservacionesIngreso observacion) {
        this.observacion = observacion;
    }

    public RegpObservacionesLazy getLazyObvs() {
        return lazyObvs;
    }

    public void setLazyObvs(RegpObservacionesLazy lazyObvs) {
        this.lazyObvs = lazyObvs;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public List<AclUser> getRevisores() {
        return revisores;
    }

    public void setRevisores(List<AclUser> revisores) {
        this.revisores = revisores;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public Integer getMotivo() {
        return motivo;
    }

    public void setMotivo(Integer motivo) {
        this.motivo = motivo;
    }

    public Integer getReporte() {
        return reporte;
    }

    public void setReporte(Integer reporte) {
        this.reporte = reporte;
    }

    public Boolean getGuardar() {
        return guardar;
    }

    public void setGuardar(Boolean guardar) {
        this.guardar = guardar;
    }
}
