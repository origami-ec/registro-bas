/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Utils;
import com.origami.sql.ConsultasSqlLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.ibatis.logging.LogFactory;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class ReportesPersonales implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReportesPersonales.class.getName());

    @Inject
    protected UserSession us;
    @Inject
    protected Entitymanager em;
    @Inject
    protected ServletSession ss;
    @Inject
    protected ConsultasSqlLocal sql;

    protected List<TareaWF> taskWorkflow;
    protected SimpleDateFormat sdf;
    protected Integer reporte;
    protected AclUser usuario;
    protected Boolean excel;
    protected Date desde;
    protected Date hasta;

    @PostConstruct
    protected void iniView() {
        try {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            excel = false;
            reporte = 0;
            usuario = em.find(AclUser.class, us.getUserId());
            desde = new Date();
            hasta = new Date();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirReportes() {
        try {
            if (reporte == 0) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de reporte.", "");
                return;
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setIgnorarPagineo(true);
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
            ss.setNombreSubCarpeta("workflow");
            if (usuario != null && usuario.getId() != null) {
                ss.agregarParametro("USUARIO", usuario.getId());
            } else {
                ss.agregarParametro("USUARIO", null);
            }
            switch (reporte) {
                case 1:
                    if (desde == null || hasta == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("FECHA", sdf.format(desde));
                    ss.agregarParametro("HASTA", sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    ss.setNombreSubCarpeta("workflow");
                    ss.setNombreReporte("ReporteCertificados");
                    break;
                case 2:
                    if (desde == null || hasta == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("FECHA", sdf.format(desde));
                    ss.agregarParametro("HASTA", sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    ss.setNombreReporte("ReporteInscripciones");
                    break;
                case 3:
                    if (desde == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar la fecha del reporte.", "");
                        return;
                    }
                    ss.agregarParametro("FECHA", desde);
                    ss.setNombreReporte("ReporteTotalInscripciones");
                    break;
                case 4:
                    if (usuario != null && usuario.getId() != null) {
                        if (usuario.getEnte() != null) {
                            ss.agregarParametro("NOMBRES", usuario.getEnte().getNombresApellidos());
                        }
                        ss.agregarParametro("USER", usuario.getUsuario());
                        taskWorkflow = sql.getTasksWorkflow(usuario.getUsuario(), null, null);
                        ss.setTieneDatasource(false);
                        ss.setDataSource(taskWorkflow);
                        //ss.setNombreReporte("ReporteBandejaTareas");
                        ss.setNombreReporte("ReporteBandejaTareas2");
                    } else {
                        ss.agregarParametro("NOMBRES", "TODOS");
                        taskWorkflow = sql.getTasksWorkflow(null, null, null);
                        LogFactory.useNoLogging();
                        ss.setTieneDatasource(false);
                        ss.setDataSource(taskWorkflow);
                        ss.setNombreReporte("ReporteBandejaTareas2");
                    }
                    break;
                case 5:
                    if (desde == null || hasta == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                        return;
                    }
                    if (usuario == null || usuario.getId() == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar un usuario para el reporte.", "");
                        return;
                    }
                    ss.agregarParametro("NAME_USER", usuario.getUsuario());
                    ss.agregarParametro("FECHA", sdf.format(desde));
                    ss.agregarParametro("HASTA", sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    ss.setNombreReporte("ReporteTareasRealizadas");
                    break;
                case 6:
                    if (desde == null || hasta == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde);
                    ss.agregarParametro("HASTA", hasta);
                    ss.agregarParametro("USUARIO", us.getName_user());
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("informeNotasDevolutivas");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                    break;
            }
            if (excel) {
                JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
            setExcel(false);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Integer getReporte() {
        return reporte;
    }

    public void setReporte(Integer reporte) {
        this.reporte = reporte;
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

    public Boolean getExcel() {
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

}
