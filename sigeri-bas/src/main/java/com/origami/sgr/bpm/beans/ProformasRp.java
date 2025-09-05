/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.lazymodels.RegpLiquidacionLazy;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Constantes;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ProformasRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ProformasRp.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    private IngresoTramiteLocal itl;

    protected RegpLiquidacionLazy liquidaciones;
    protected RegpLiquidacion liquidacion;
    protected ContenidoReportes proforma;
    protected RegRegistrador registrador;
    protected Valores validez;

    protected List<HistoricTaskInstance> tareas = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            liquidacion = new RegpLiquidacion();
            liquidaciones = new RegpLiquidacionLazy(1L);
            map = new HashMap();
            map.put("code", Constantes.piePaginaProforma);
            proforma = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
            map = new HashMap();
            map.put("code", Constantes.diasValidezProforma);
            validez = (Valores) manager.findObjectByParameter(Valores.class, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
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
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showInfo(RegpLiquidacion re) {
        try {
            liquidacion = re;
            if (liquidacion.getTramite().getIdProceso() == null) {
                tareas = new ArrayList<>();
            } else {
                tareas = this.getTaskByProcessInstanceIdMain(liquidacion.getTramite().getIdProceso());
                if (!tareas.isEmpty()) {
                    if (tareas.get(0).getEndTime() != null) {
                        JsfUti.messageInfo(null, "TRAMITE FINALIZADO", "El tramite " + liquidacion.getNumTramiteRp() + " ha Finaliado su proceso.");
                    }
                }
            }
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editProforma(RegpLiquidacion re) {
        try {
            if (re.getUserCreacion() != session.getUserId()) {
                JsfUti.messageWarning(null, "Liquidacion no se puede Editar", 
                        "Solo puede editar el mismo usuario que creó la proforma.");
                return;
            }
            if (re.getEstadoLiquidacion().getId() == 1L || re.getEstadoLiquidacion().getId() == 4L) { // ESTADO LIQUIDACION INGRESADA
                if (this.comprararFechas(re.getFechaCreacion())) {
                    ss.instanciarParametros();
                    ss.agregarParametro("proforma", re.getId());
                    JsfUti.redirectFaces("/procesos/registro/editarProforma.xhtml");
                } else {
                    JsfUti.messageWarning(null, "Liquidacion no se puede Editar.", "Fecha limite de validez de liquidacion es 7 dias.");
                }
            } else {
                JsfUti.messageWarning(null, "Liquidacion no se puede Editar", "Ya esta la liquidacion ingresada.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean comprararFechas(Date in) {
        try {
            Date fecha = Utils.sumarDiasFechaSinWeekEnd(in, validez.getValorNumeric().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date hoy = sdf.parse(sdf.format(new Date()));
            fecha = sdf.parse(sdf.format(fecha));
            if (fecha.before(hoy)) {
                return false; // FUERA DE FECHA DE VALIDEZ DE PROFORMA
            } else {
                return true; // DENTRO DE FECHA DE VALIDEZ DE PROFORMA
            }
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
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

}
