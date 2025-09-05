/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.models.CantidadTramitesPorUsuario;
import com.origami.sgr.bpm.models.CantidadesTramites;
import com.origami.sgr.bpm.models.CantidadesUsuarios;
import com.origami.sgr.bpm.models.DataModel;
import com.origami.sgr.bpm.models.EvaluacionFuncionarios;
import com.origami.sgr.bpm.models.MovimientosModificadosIngresados;
import com.origami.sgr.bpm.models.ReporteTramitesRp;
import com.origami.sgr.bpm.models.TareaEntregaDocumento;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.bpm.models.TareasPorFechaIngresoEntrega;
import com.origami.sgr.bpm.models.TareasSinRealizar;
import com.origami.sgr.bpm.models.TramitesParaRegistrador;
import com.origami.sgr.bpm.models.TramitesPorFechaIngresoAgrupadoRol;
import com.origami.sgr.bpm.models.TramitesReasignados;
import com.origami.sgr.bpm.models.TramitesVencidosElaborados;
import com.origami.sgr.bpm.models.TramitesVencidosPendientes;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sql.ConsultasSqlLocal;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.text.WordUtils;
import org.apache.ibatis.logging.LogFactory;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ReportesDiarios implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReportesDiarios.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;

    @Inject
    protected RegistroPropiedadServices reg;

    @Inject
    private ConsultasSqlLocal sql;

    @Inject
    protected Entitymanager em;

    protected Integer tipo = 0;
    protected Integer reporte = 0, reporte1 = 0, reporte2 = 0, reporte3 = 0;
    protected Integer tipofecha = 0;
    protected Date desde = new Date();
    protected Date hasta;
    protected Date desde2;
    protected Date hasta2;
    protected Date desde3;
    protected Date hasta3;
    protected Date desde4;
    protected Date hasta4;
    protected Date desde5;
    protected Date hasta5;
    protected Date desde6;
    protected Date hasta6;
    protected Date desde7;
    protected Date hasta7;
    protected Date desde8, hasta8;
    /*COMBO DE REPORTES DE TAREA CON EL ACTIVITI*/
    protected Integer tipotarea = 0;
    private Boolean excel = false;

    protected List<ReporteTramitesRp> list = new ArrayList<>();
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    protected List<CantidadesTramites> cantidades = new ArrayList<>();
    protected List<TareasPorFechaIngresoEntrega> tareas = new ArrayList<>();
    protected List<TramitesParaRegistrador> lregistrador = new ArrayList<>();
    protected List<CantidadTramitesPorUsuario> tramitesUser = new ArrayList<>();
    protected List<TramitesPorFechaIngresoAgrupadoRol> tramitesRol = new ArrayList<>();
    protected List<TareasSinRealizar> tareasNoRealizadas = new ArrayList<>();
    protected List<TareasSinRealizar> todasTareasSinRealizar = new ArrayList<>();
    protected List<TareaEntregaDocumento> entregaDocumento = new ArrayList<>();
    protected List<TramitesVencidosElaborados> vencidosElaborados = new ArrayList<>();
    protected List<TramitesVencidosPendientes> vencidosPendientes = new ArrayList<>();
    protected List<TramitesReasignados> tramitesReasignados = new ArrayList<>();
    protected List<MovimientosModificadosIngresados> movimientos = new ArrayList<>();
    protected List<EvaluacionFuncionarios> evaluacion = new ArrayList<>();
    protected List<AclUser> inscriptores = new ArrayList<>();
    protected List<AclUser> certificadores = new ArrayList<>();
    protected List<AclUser> funcionarios = new ArrayList<>();
    protected List<AclUser> revisores = new ArrayList<>();
    protected AclUser user;
    protected AclUser revisor;
    protected CantidadesTramites ct = new CantidadesTramites();
    protected Calendar ca;
    protected int mes;
    protected int anio;

    protected Map map;
    private AclUser usuario, usuario1;
    private List<TareaWF> taskWorkflow;
    protected RegRegistrador registrador;
    protected String nombre = "", busqueda = "";

    @PostConstruct
    protected void iniView() {
        try {
            ca = Calendar.getInstance();
            mes = ca.get(Calendar.MONTH);
            anio = ca.get(Calendar.YEAR);

            /*certificadores = sql.getUsuariosByRolName("certificador");
            inscriptores = sql.getUsuariosByRolName("inscriptor");
            for (int i = 0; i <= certificadores.size() - 1; i++) {
                funcionarios.add(certificadores.get(i));
            }
            for (int i = 0; i <= inscriptores.size() - 1; i++) {
                funcionarios.add(inscriptores.get(i));
            }*/
            /*funcionarios = reg.getUsuariosByRolName("certificador");
            funcionarios.addAll(reg.getUsuariosByRolName("inscriptor"));
            funcionarios.addAll(reg.getUsuariosByRolName("inscriptor_juridico"));
            funcionarios.addAll(reg.getUsuariosByRolName("inscriptor_mercantil"));*/
            funcionarios = em.findAll(Querys.getUsuariosActivos);
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) em.findObjectByParameter(RegRegistrador.class, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void limpiarListas() {
        list = new ArrayList<>();
        tareas = new ArrayList<>();
        cantidades = new ArrayList<>();
        lregistrador = new ArrayList<>();
        tramitesUser = new ArrayList<>();
        tramitesRol = new ArrayList<>();
        tareasNoRealizadas = new ArrayList<>();
        entregaDocumento = new ArrayList<>();
        vencidosElaborados = new ArrayList<>();
        vencidosPendientes = new ArrayList<>();
        tramitesReasignados = new ArrayList<>();
        movimientos = new ArrayList<>();
    }

    public void consultarReporte() {
        switch (reporte) {
            case 1:
                this.cantidadesTramites();
                break;
            case 2:
                if (tipofecha == 0) {
                    JsfUti.messageWarning(null, "Seleccione el tipo de consulta.", "");
                } else {
                    this.tareasPorFechaIngresoEntrega();
                }
                break;
            case 3:
                this.tramitesParaRegistrador();
                break;
            case 4:
                if (user == null) {
                    JsfUti.messageWarning(null, "Seleccione el funcionario.", "");
                } else {
                    this.cantidadTramitesPorUsuario(user);
                }
                break;
            case 5:
                this.tareasSinRealizar();
                break;

            default:
                JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                break;
        }
    }

    public void generarReporte(boolean excel) {
        switch (reporte) {
            case 1:
                this.reporteCantidadesTramites(excel);
                break;
            case 2:
                if (tipofecha == 0) {
                    JsfUti.messageWarning(null, "Seleccione el tipo de consulta.", "");
                } else {
                    this.reporteTareasPorFechaIngresoEntrega(excel);
                }
                break;
            case 3:
                this.reporteTramitesParaRegistrador(excel);
                break;
            case 4:
                this.reporteCantidadTramitesPorUsuario(excel);
                break;
            case 5:
                this.reporteTareasSinRealizar(excel);
                break;

            default:
                JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                break;
        }
    }

    public void selectTipoReporte(boolean excel) {
        switch (tipo) {
            case 1:
                this.reporteTareasTramite(excel);
                break;
            case 2:
                this.reporteTareasUsuarios(excel);
                break;
            case 3:
                this.reporteTotalesUsuarios(excel);
                break;
            case 4:
                this.cantidadesPorContrato(excel);
                break;
            case 5:
                this.cantidadesPorContratoEntregados(excel);
                break;
            case 6:
                this.tramitesPendientes(excel);
                break;
            case 7:
                if (tipofecha == 0) {
                    JsfUti.messageWarning(null, "Seleccione el tipo de consulta.", "");
                } else {
                    this.reporteTareasPorFechaIngresoEntrega(excel);
                }
                break;
            case 8:
                this.reporteTramitesParaRegistrador(excel);
                break;
            case 9:
                this.reporteCantidadTramitesPorUsuario(excel);
                break;
            case 10:
                this.reportePorFechaIngresoAgrupadoRol(excel);
                break;
            case 11:
                this.reporteTramitesAutorizados(excel);
                break;
            case 12:
                this.reporteTramitesAceptados(excel);
                break;
            case 13:
                this.reporteTodasTareasSinRealizar(excel);
                break;
            case 14:
                this.generarReporteMarginacion(excel);
                break;
            default:
                JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                break;
        }
    }

    /*METODO PARA REPORTE DE TARAEAS*/
    public void selectTipoReporteTarea(boolean excel) {
        switch (tipotarea) {
            case 1:
                this.reporteTareasEntregaDocumento(excel);
                break;
            case 2:
                this.reporteTramitesVencidosElaborados(excel);
                break;
            case 3:
                this.reporteTramitesVencidosPendientes(excel);
                break;
            case 4:
                this.reporteTramitesCorregidos(excel);
                break;
            case 5:
                this.reporteTramitesReasignados(excel);
                break;
            case 6:
                this.reporteMovimientosModificadosIngresados(excel);
                break;
            case 7:
                this.reporteEvaluacionFuncionarios(excel);
                break;
            case 8:
                this.generarObservacionesRevision(excel);
                break;
            default:
                JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                break;
        }
    }

    /*Metodos Por Repoortes*/
    public void reporteTareasEntregaDocumento(boolean excel) {
        try {
            if (desde4 != null && hasta4 != null) {
                if (!desde4.after(hasta4)) {
                    entregaDocumento = sql.getTareaEntregaDocumento(sdf2.format(desde4), sdf2.format(hasta4));

                    if (!entregaDocumento.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reporteTareaEntregaDocumentos");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf2.format(desde4));
                        ss.agregarParametro("HASTA", sdf2.format(hasta4));
                        ss.setDataSource(entregaDocumento);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteTramitesVencidosElaborados(boolean excel) {
        try {
            if (desde4 != null && hasta4 != null) {
                if (!desde4.after(hasta4)) {
                    vencidosElaborados = sql.getTramitesVencidosElaborados(sdf2.format(desde4), sdf2.format(hasta4));

                    if (!vencidosElaborados.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reporteTramitesVencidosElaborados");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf2.format(desde4));
                        ss.agregarParametro("HASTA", sdf2.format(hasta4));
                        ss.setDataSource(vencidosElaborados);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteTramitesVencidosPendientes(boolean excel) {
        try {
            if (desde4 != null && hasta4 != null) {
                if (!desde4.after(hasta4)) {
                    vencidosPendientes = sql.getTramitesVencidosPendientes(sdf2.format(desde4), sdf2.format(hasta4));
                    if (!vencidosPendientes.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reporteTramitesVencidosPendientes");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf2.format(desde4));
                        ss.agregarParametro("HASTA", sdf2.format(hasta4));
                        ss.setDataSource(vencidosPendientes);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteTramitesCorregidos(boolean excel) {
        try {
            if (desde4 != null && hasta4 != null) {
                if (!desde4.after(hasta4)) {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(Boolean.TRUE);
                    ss.setNombreSubCarpeta("workflow");
                    ss.setNombreReporte("reporteTramitesCorregidos");
                    ss.agregarParametro("USER", us.getName_user());
                    ss.agregarParametro("DESDE", sdf2.format(desde4));
                    ss.agregarParametro("HASTA", sdf2.format(hasta4));
                    this.redirectReport(excel);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteTramitesReasignados(boolean excel) {
        try {
            if (desde4 != null && hasta4 != null) {
                if (!desde4.after(hasta4)) {
                    tramitesReasignados = sql.getTramitesReasignados(sdf2.format(desde4), sdf2.format(hasta4));

                    if (!tramitesReasignados.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reporteTramitesReasignados");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf2.format(desde4));
                        ss.agregarParametro("HASTA", sdf2.format(hasta4));
                        ss.setDataSource(tramitesReasignados);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteMovimientosModificadosIngresados(boolean excel) {
        try {
            if (desde4 != null && hasta4 != null) {
                if (!desde4.after(hasta4)) {
                    movimientos = sql.getMovimientosModificadosIngresados(sdf2.format(desde4), sdf2.format(hasta4));

                    if (!movimientos.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reporteDeMovimientos");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf2.format(desde4));
                        ss.agregarParametro("HASTA", sdf2.format(hasta4));
                        ss.setDataSource(movimientos);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteEvaluacionFuncionarios(boolean excel) {
        try {
            ca.clear();
            ca.set(anio, mes, 1);

            evaluacion = sql.getEvaluacionFuncionarios(ca, user);

            if (!evaluacion.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("reporteEvaluacionFuncionarios");
                ss.agregarParametro("USUARIO", us.getName_user());
                ss.setDataSource(evaluacion);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void redirectReport(boolean excel) {
        if (excel) {
            JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
        } else {
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        }
    }

    public void cantidadesTramites() {
        this.limpiarListas();
        try {
            ca.clear();
            ca.set(anio, mes, 1);
            cantidades = reg.reporteCantidadesTramites(ca);
            ct = cantidades.remove(0);
            JsfUti.update("mainForm:mainTab");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteCantidadesTramites(boolean excel) {
        try {
            if (!cantidades.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("CantidadesTramites");
                ss.agregarParametro("USER", us.getName_user());
                ss.setDataSource(cantidades);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTareasTramite(boolean excel) {
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    list = reg.getTareasByTramite(desde, Utils.sumarRestarDiasFecha(hasta, 1));
                    if (!list.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("TareasPorTramite");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(list);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTareasUsuarios(boolean excel) {
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    list = reg.getTareasByUsuarios(sdf.format(desde), sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    if (!list.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("TareasPorUsuario");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(list);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTotalesUsuarios(boolean excel) {
        List<CantidadesUsuarios> can;
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    can = reg.getTotalTramitesByUsuarios(sdf.format(desde), sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    if (!can.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("totalesConsultas");
                        ss.agregarParametro("USUARIO", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(can);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cantidadesPorContrato(boolean excel) {
        List<DataModel> can;
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    can = reg.getCantidadesByActo(sdf.format(desde), sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    if (!can.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("totalesContratos");
                        ss.agregarParametro("REPORTE", "CONTRATOS INGRESADOS POR RANGOS DE FECHA");
                        ss.agregarParametro("USUARIO", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(can);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cantidadesPorContratoEntregados(boolean excel) {
        List<DataModel> can;
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    can = sql.getCantidadesByActoEntregado(sdf.format(desde), sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    if (!can.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("totalesContratos");
                        ss.agregarParametro("REPORTE", "CONTRATOS ENTREGADOS POR RANGOS DE FECHA");
                        ss.agregarParametro("USUARIO", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(can);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void tramitesPendientes(boolean excel) {
        List<ReporteTramitesRp> can;
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    can = sql.getTramitesPendientes(sdf.format(desde), sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));
                    if (!can.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("tramitesPendientes");
                        ss.agregarParametro("USUARIO", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(can);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void tareasPorFechaIngresoEntrega() {
        this.limpiarListas();
        try {
            if (desde2 != null && hasta2 != null) {
                if (!desde2.after(hasta2)) {
                    if (Utils.restarFechas(desde2, hasta2) > 31) {
                        JsfUti.messageWarning(null, "Advertencia", "La consulta no puede ser mayor a 31 dias.");
                    } else {
                        tareas = sql.getTareasPorFechaIngresoEntrega(sdf2.format(desde2), sdf2.format(hasta2), tipofecha);
                        JsfUti.update("mainForm:mainTab");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTareasPorFechaIngresoEntrega(boolean excel) {
        try {
            if (!tareas.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("reportePorFechaIngresoEntrega");
                ss.agregarParametro("USUARIO", us.getName_user());
                ss.agregarParametro("DESDE", sdf2.format(desde2));
                ss.agregarParametro("HASTA", sdf2.format(hasta2));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                ss.setDataSource(tareas);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void tramitesParaRegistrador() {
        this.limpiarListas();
        try {
            if (desde3 != null && hasta3 != null) {
                if (!desde3.after(hasta3)) {
                    if (Utils.restarFechas(desde3, hasta3) > 31) {
                        JsfUti.messageWarning(null, "Advertencia", "La consulta no puede ser mayor a 31 dias.");
                    } else {
                        lregistrador = sql.getTramitesParaRegistrador(sdf.format(desde3), sdf.format(Utils.sumarRestarDiasFecha(hasta3, 1)));
                        JsfUti.update("mainForm:mainTab");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTramitesParaRegistrador(boolean excel) {
        try {
            if (!lregistrador.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("reporteParaRegistrador");
                ss.agregarParametro("USUARIO", us.getName_user());
                ss.agregarParametro("DESDE", sdf.format(desde3));
                ss.agregarParametro("HASTA", sdf.format(hasta3));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                ss.setDataSource(lregistrador);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cantidadTramitesPorUsuario(AclUser user) {
        this.limpiarListas();
        try {
            ca.clear();
            ca.set(anio, mes, 1);
            tramitesUser = sql.getCantidadTramitesPorUsuario(ca, user);
            JsfUti.update("mainForm:mainTab");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteCantidadTramitesPorUsuario(boolean excel) {
        try {
            if (!tramitesUser.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("CantidadTramitesPorUsuario");
                ss.agregarParametro("USUARIO", us.getName_user());
                ss.setDataSource(tramitesUser);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reportePorFechaIngresoAgrupadoRol(boolean excel) {
        try {
            if (desde != null && hasta != null) {
                if (!desde.after(hasta)) {
                    tramitesRol = sql.getTramitesPorFechaIngresoAgrupadoRol(sdf2.format(desde), sdf2.format(hasta));

                    if (!tramitesRol.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reportePorFechaIngresoAgrupadoRol");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.setDataSource(tramitesRol);
                        this.redirectReport(excel);
                    } else {
                        JsfUti.messageWarning(null, "Error", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void tareasSinRealizar() {
        this.limpiarListas();
        try {
            tareasNoRealizadas = sql.getTareasSinRealizar();
            JsfUti.update("mainForm:mainTab");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTareasSinRealizar(boolean excel) {
        try {
            if (!tareasNoRealizadas.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("reporteTareasSinRealizar");
                ss.agregarParametro("USER", us.getName_user());
                ss.setOnePagePerSheet(Boolean.TRUE);
                ss.setDataSource(tareasNoRealizadas);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTodasTareasSinRealizar(boolean excel) {
        try {
            todasTareasSinRealizar = sql.getTodasTareasSinRealizar();

            if (!todasTareasSinRealizar.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreSubCarpeta("workflow");
                ss.setNombreReporte("reporteTodasTareasSinRealizar");
                ss.agregarParametro("USER", us.getName_user());
                ss.setOnePagePerSheet(Boolean.TRUE);
                ss.setDataSource(todasTareasSinRealizar);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTramitesAutorizados(boolean excel) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(Boolean.TRUE);
            ss.setNombreSubCarpeta("workflow");
            ss.setNombreReporte("reporteTramitesAutorizados");
            ss.agregarParametro("USER", us.getName_user());
            ss.agregarParametro("DESDE", desde);
            ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta, 1));
            ss.agregarParametro("HASTA_STRING", new SimpleDateFormat("dd/MM/yyyy").format(hasta));
            this.redirectReport(excel);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void reporteTramitesAceptados(boolean excel) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(Boolean.TRUE);
            ss.setNombreSubCarpeta("workflow");
            ss.setNombreReporte("reporteTramitesAceptados");
            ss.agregarParametro("USER", us.getName_user());
            ss.agregarParametro("DESDE", sdf2.format(desde));
            ss.agregarParametro("HASTA", sdf2.format(hasta));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/workflow/");
            this.redirectReport(excel);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void generarObservacionesRevision(boolean excel) {
        try {
            if (desde5 != null && hasta5 != null) {
                if (desde5.before(hasta5) || desde5.equals(hasta5)) {
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDesde = sdf.format(desde5);
                    String strHasta = sdf.format(hasta5);
                    ss.instanciarParametros();
                    ss.setNombreReporte("observacionesIngresadas");
                    ss.setTieneDatasource(true);
                    ss.setNombreSubCarpeta("diarios");
                    ss.agregarParametro("USUARIO", us.getName_user());
                    if (revisor == null) {
                        ss.agregarParametro("ID_USER", 0L);
                    } else {
                        ss.agregarParametro("ID_USER", revisor.getId());
                    }
                    ss.agregarParametro("DESDE", sdf.parse(strDesde));
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta5, 1));
                    ss.agregarParametro("HASTA_STRING", strHasta);
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                    this.redirectReport(excel);
                }
            }
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporteMarginacion(boolean excel) {
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDesde = sdf.format(desde);
                    String strHasta = sdf.format(hasta);
                    ss.instanciarParametros();
                    ss.setNombreReporte("marginacionArchivo");
                    ss.setTieneDatasource(true);
                    ss.setNombreSubCarpeta("diarios");
                    ss.agregarParametro("USUARIO", us.getName_user());
                    ss.agregarParametro("DESDE", sdf.parse(strDesde));
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta, 1));
                    ss.agregarParametro("HASTA_STRING", strHasta);
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                    this.redirectReport(excel);
                }
            }
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirReportes() {
        try {
            if (reporte1 == 0) {
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
            switch (reporte1) {
                case 1:
                    if (desde6 == null || hasta6 == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("FECHA", sdf.format(desde6));
                    ss.agregarParametro("HASTA", sdf.format(Utils.sumarRestarDiasFecha(hasta6, 1)));
                    ss.setNombreSubCarpeta("workflow");
                    ss.setNombreReporte("ReporteCertificados");
                    break;
                case 2:
                    if (desde6 == null || hasta6 == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("FECHA", sdf.format(desde6));
                    ss.agregarParametro("HASTA", sdf.format(Utils.sumarRestarDiasFecha(hasta6, 1)));
                    ss.setNombreReporte("ReporteInscripciones");
                    break;
                case 3:
                    if (desde6 == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar la fecha del reporte.", "");
                        return;
                    }
                    ss.agregarParametro("FECHA", desde6);
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
                    if (desde6 == null || hasta6 == null) {
                        JsfUti.messageWarning(null, "Debe seleccionar las fechas desde y hasta.", "");
                        return;
                    }
                    if(usuario == null || usuario.getId() == null){
                        JsfUti.messageWarning(null, "Debe seleccionar un usuario para el reporte.", "");
                        return;
                    }
                    ss.agregarParametro("NAME_USER", usuario.getUsuario());
                    ss.agregarParametro("FECHA", sdf.format(desde6));
                    ss.agregarParametro("HASTA", sdf.format(Utils.sumarRestarDiasFecha(hasta6, 1)));
                    ss.setNombreReporte("ReporteTareasRealizadas");
                    break;
                case 6:
                    if (desde6 == null || hasta6 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde6);
                    ss.agregarParametro("HASTA", hasta6);
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

    public void imprimirReportes2() {
        try {
            if (reporte3 == 0) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de reporte e ingresar la fecha desde y hasta.", "");
                return;
            }
            AclUser acluser;
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            switch (reporte3) {
                case 1:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta7, 1));
                    ss.agregarParametro("HASTA_STRING", sdf.format(hasta7));
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("ReporteTransferenciasMunicipio");
                    ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    acluser = em.find(AclUser.class, us.getUserId());
                    if (acluser != null && acluser.getEnte() != null) {
                        ss.agregarParametro("NOMBRES", acluser.getEnte().getNombresTitulo().toUpperCase());
                    }
                    if (excel) {
                        JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    } else {
                        JsfUti.redirectNewTab("/sgr/Documento");
                    }
                    break;
                case 2:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta7, 1));
                    ss.agregarParametro("HASTA_STRING", sdf.format(hasta7));
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("ReporteTransferenciasMunicipioExcel");
                    ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    break;
                case 7:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta7, 1));
                    ss.agregarParametro("HASTA_STRING", sdf.format(hasta7));
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("ReporteHipotecasMunicipio");
                    ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    acluser = em.find(AclUser.class, us.getUserId());
                    if (acluser != null && acluser.getEnte() != null) {
                        ss.agregarParametro("NOMBRES", acluser.getEnte().getNombresTitulo().toUpperCase());
                    }
                    if (excel) {
                        JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    } else {
                        JsfUti.redirectNewTab("/sgr/Documento");
                    }
                    break;
                case 8:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta7, 1));
                    ss.agregarParametro("HASTA_STRING", sdf.format(hasta7));
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("ReporteHipotecasMunicipioExcel");
                    ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    break;
                case 3:
                    if (!busqueda.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("InformeDeBienes");
                        ss.setNombreSubCarpeta("registro");
                        ss.agregarParametro("NOMBRE", nombre);
                        ss.agregarParametro("BUSQUEDA", busqueda);
                        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                        ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header_loja.png"));
                        ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark_loja.png"));
                        ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer_loja.png"));
                        acluser = em.find(AclUser.class, us.getUserId());
                        if (acluser != null && acluser.getEnte() != null) {
                            ss.agregarParametro("USUARIO", acluser.getEnte().getNombresTitulo().toUpperCase());
                        }
                        if (excel) {
                            JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                        } else {
                            JsfUti.redirectNewTab("/sgr/Documento");
                        }
                    }
                    break;
                case 4:
                    if (!busqueda.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("InformeDeBienesVentas");
                        ss.setNombreSubCarpeta("registro");
                        ss.agregarParametro("NOMBRE", nombre);
                        ss.agregarParametro("BUSQUEDA", busqueda);
                        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                        ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header_loja.png"));
                        ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark_loja.png"));
                        ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer_loja.png"));
                        acluser = em.find(AclUser.class, us.getUserId());
                        if (acluser != null && acluser.getEnte() != null) {
                            ss.agregarParametro("USUARIO", acluser.getEnte().getNombresTitulo().toUpperCase());
                        }
                        if (excel) {
                            JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                        } else {
                            JsfUti.redirectNewTab("/sgr/Documento");
                        }
                    }
                    break;
                case 5:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", hasta7);
                    ss.agregarParametro("USUARIO", us.getName_user());
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("informeNotasDevolutivas");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    if (excel) {
                        JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    } else {
                        JsfUti.redirectNewTab("/sgr/Documento");
                    }
                    break;
                case 6:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", hasta7);
                    ss.agregarParametro("USUARIO", WordUtils.capitalizeFully(us.getNombrePersonaLogeada()));
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("informeTramiteOnline");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    if (excel) {
                        JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    } else {
                        JsfUti.redirectNewTab("/sgr/Documento");
                    }
                    break;
                //POR FAVOR REVISAR QUE YA EXITE UN CASE 7 MAS ARRIBA
                /*case 7:
                    if (desde7 == null || hasta7 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar la fecha desde y hasta.", "");
                        return;
                    }
                    ss.agregarParametro("DESDE", desde7);
                    ss.agregarParametro("HASTA", hasta7);
                    ss.agregarParametro("USUARIO", WordUtils.capitalizeFully(us.getNombrePersonaLogeada()));
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("informeTotalesCertificados");
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                    if (excel) {
                        JsfUti.redirectNewTab("/sgr/DocumentoExcel");
                    } else {
                        JsfUti.redirectNewTab("/sgr/Documento");
                    }
                    break;*/

            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirReportes3() {
        try {
            if (reporte2 == 0) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de reporte.", "");
                return;
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("workflow");
            if (usuario1 != null && usuario1.getId() != null) {
                ss.agregarParametro("USUARIO", usuario1.getId());
            } else {
                ss.agregarParametro("USUARIO", us.getUserId());
            }
            switch (reporte2) {
                case 1: //Bandeja de Tareas Agrup. x Usuario
                    if ((us.getRoles().contains(1l) || us.getRoles().contains(18l))) {
                        if (usuario1 == null) {
                            usuario1 = null;
                        }
                    } else {
                        usuario1 = this.em.find(AclUser.class, us.getUserId());
                    }
                    if (usuario1 != null && usuario1.getId() != null) {
                        if (usuario1.getEnte() != null) {
                            ss.agregarParametro("NOMBRES", usuario1.getEnte().getNombresApellidos());
                        }
                        ss.agregarParametro("USER", usuario1.getUsuario());
                        //taskWorkflow = sql.getTasksWorkflow(usuario1.getUsuario(), 2);
                        taskWorkflow = sql.getTasksWorkflow(2, new Object[]{usuario1.getUsuario()});
                    } else {
                        ss.agregarParametro("NOMBRES", "TODOS");
                        //taskWorkflow = sql.getTasksWorkflow(null, 2);
                        taskWorkflow = sql.getTasksWorkflow(1, null);
                    }
                    ss.setTieneDatasource(false);
                    ss.setDataSource(taskWorkflow);
                    ss.setNombreReporte("ReporteBandejaTareas2Group");
                    break;
                case 2: //Bandeja de Tareas Agrup. x Fec.Ing
                    if (desde8 == null || hasta8 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar fecha desde y hasta.", "");
                        return;
                    }
                    if ((us.getRoles().contains(1l) || us.getRoles().contains(18l))) {
                        if (usuario1 == null) {
                            usuario1 = null;
                        }
                    } else {
                        usuario1 = this.em.find(AclUser.class, us.getUserId());
                    }
                    if (usuario1 != null && usuario1.getId() != null) {
                        if (usuario1.getEnte() != null) {
                            ss.agregarParametro("NOMBRES", usuario1.getEnte().getNombresApellidos());
                        }
                        ss.agregarParametro("USER", usuario1.getUsuario());
                        //taskWorkflow = sql.getTasksWorkflow(usuario1.getUsuario(), 3);
                        taskWorkflow = sql.getTasksWorkflow(5, new Object[]{usuario1.getUsuario(), desde8, Utils.sumarRestarDiasFecha(hasta8, 1)});
                    } else {
                        ss.agregarParametro("NOMBRES", "TODOS");
                        //taskWorkflow = sql.getTasksWorkflow(null, 3);
                        taskWorkflow = sql.getTasksWorkflow(3, new Object[]{desde8, Utils.sumarRestarDiasFecha(hasta8, 1)});
                    }
                    ss.setTieneDatasource(false);
                    ss.setDataSource(taskWorkflow);
                    ss.setNombreReporte("ReporteBandejaTareas3Group");
                    break;
                case 3: //Bandeja de Tareas Agrup. x Fec.Ent
                    if (desde8 == null || hasta8 == null) {
                        JsfUti.messageWarning(null, "Debe ingresar fecha desde y hasta.", "");
                        return;
                    }
                    if ((us.getRoles().contains(1l) || us.getRoles().contains(18l))) {
                        if (usuario1 == null) {
                            usuario1 = null;
                        }
                    } else {
                        usuario1 = this.em.find(AclUser.class, us.getUserId());
                    }
                    if (usuario1 != null && usuario1.getId() != null) {
                        if (usuario1.getEnte() != null) {
                            ss.agregarParametro("NOMBRES", usuario1.getEnte().getNombresApellidos());
                        }
                        ss.agregarParametro("USER", usuario1.getUsuario());
                        //taskWorkflow = sql.getTasksWorkflow(usuario1.getUsuario(), 4);
                        taskWorkflow = sql.getTasksWorkflow(6, new Object[]{usuario1.getUsuario(), desde8, Utils.sumarRestarDiasFecha(hasta8, 1)});
                    } else {
                        ss.agregarParametro("NOMBRES", "TODOS");
                        //taskWorkflow = sql.getTasksWorkflow(null, 4);
                        taskWorkflow = sql.getTasksWorkflow(4, new Object[]{desde8, Utils.sumarRestarDiasFecha(hasta8, 1)});
                    }
                    ss.setTieneDatasource(false);
                    ss.setDataSource(taskWorkflow);
                    ss.setNombreReporte("ReporteBandejaTareas4Group");
                    break;
            }

            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
            if (excel) {
                ss.setOnePagePerSheet(false);
                JsfUti.redirectNewTab("/sgr/DocumentoExcel");
            } else {
                JsfUti.redirectNewTab("/sgr/Documento");
            }
            setExcel(false);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
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

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getReporte() {
        return reporte;
    }

    public void setReporte(Integer reporte) {
        this.reporte = reporte;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public List<CantidadesTramites> getCantidades() {
        return cantidades;
    }

    public void setCantidades(List<CantidadesTramites> cantidades) {
        this.cantidades = cantidades;
    }

    public CantidadesTramites getCt() {
        return ct;
    }

    public void setCt(CantidadesTramites ct) {
        this.ct = ct;
    }

    public List<TareasPorFechaIngresoEntrega> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareasPorFechaIngresoEntrega> tareas) {
        this.tareas = tareas;
    }

    public List<TramitesParaRegistrador> getLregistrador() {
        return lregistrador;
    }

    public void setLregistrador(List<TramitesParaRegistrador> lregistrador) {
        this.lregistrador = lregistrador;
    }

    public List<CantidadTramitesPorUsuario> getTramitesUser() {
        return tramitesUser;
    }

    public void setTramitesUser(List<CantidadTramitesPorUsuario> tramitesUser) {
        this.tramitesUser = tramitesUser;
    }

    public List<TramitesPorFechaIngresoAgrupadoRol> getTramitesRol() {
        return tramitesRol;
    }

    public void setTramitesRol(List<TramitesPorFechaIngresoAgrupadoRol> tramitesRol) {
        this.tramitesRol = tramitesRol;
    }

    public List<TareasSinRealizar> getTramitesGer() {
        return tareasNoRealizadas;
    }

    public void setTramitesGer(List<TareasSinRealizar> tramitesGer) {
        this.tareasNoRealizadas = tramitesGer;
    }

    public List<TareaEntregaDocumento> getEntregaDocumento() {
        return entregaDocumento;
    }

    public void setEntregaDocumento(List<TareaEntregaDocumento> entregaDocumento) {
        this.entregaDocumento = entregaDocumento;
    }

    public List<TramitesVencidosElaborados> getVencidosElaborados() {
        return vencidosElaborados;
    }

    public void setVencidosElaborados(List<TramitesVencidosElaborados> vencidosElaborados) {
        this.vencidosElaborados = vencidosElaborados;
    }

    public List<TramitesVencidosPendientes> getVencidosPendientes() {
        return vencidosPendientes;
    }

    public void setVencidosPendientes(List<TramitesVencidosPendientes> vencidosPendientes) {
        this.vencidosPendientes = vencidosPendientes;
    }

    public List<TramitesReasignados> getTramitesReasignados() {
        return tramitesReasignados;
    }

    public void setTramitesReasignados(List<TramitesReasignados> tramitesReasignados) {
        this.tramitesReasignados = tramitesReasignados;
    }

    public List<AclUser> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<AclUser> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public Integer getTipofecha() {
        return tipofecha;
    }

    public void setTipofecha(Integer tipofecha) {
        this.tipofecha = tipofecha;
    }

    public Date getDesde2() {
        return desde2;
    }

    public void setDesde2(Date desde2) {
        this.desde2 = desde2;
    }

    public Date getHasta2() {
        return hasta2;
    }

    public void setHasta2(Date hasta2) {
        this.hasta2 = hasta2;
    }

    public Date getDesde3() {
        return desde3;
    }

    public void setDesde3(Date desde3) {
        this.desde3 = desde3;
    }

    public Date getHasta3() {
        return hasta3;
    }

    public void setHasta3(Date hasta3) {
        this.hasta3 = hasta3;
    }

    public Date getDesde4() {
        return desde4;
    }

    public void setDesde4(Date desde4) {
        this.desde4 = desde4;
    }

    public Date getHasta4() {
        return hasta4;
    }

    public void setHasta4(Date hasta4) {
        this.hasta4 = hasta4;
    }

    public Integer getTipotarea() {
        return tipotarea;
    }

    public void setTipotarea(Integer tipotarea) {
        this.tipotarea = tipotarea;
    }

    public List<AclUser> getRevisores() {
        return revisores;
    }

    public void setRevisores(List<AclUser> revisores) {
        this.revisores = revisores;
    }

    public AclUser getRevisor() {
        return revisor;
    }

    public void setRevisor(AclUser revisor) {
        this.revisor = revisor;
    }

    public Date getDesde5() {
        return desde5;
    }

    public void setDesde5(Date desde5) {
        this.desde5 = desde5;
    }

    public Date getHasta5() {
        return hasta5;
    }

    public void setHasta5(Date hasta5) {
        this.hasta5 = hasta5;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public AclUser getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(AclUser usuario1) {
        this.usuario1 = usuario1;
    }

    public List<AclUser> getInscriptores() {
        return inscriptores;
    }

    public void setInscriptores(List<AclUser> inscriptores) {
        this.inscriptores = inscriptores;
    }

    public List<AclUser> getCertificadores() {
        return certificadores;
    }

    public void setCertificadores(List<AclUser> certificadores) {
        this.certificadores = certificadores;
    }

    public List<TareaWF> getTaskWorkflow() {
        return taskWorkflow;
    }

    public void setTaskWorkflow(List<TareaWF> taskWorkflow) {
        this.taskWorkflow = taskWorkflow;
    }

    public Integer getReporte1() {
        return reporte1;
    }

    public void setReporte1(Integer reporte1) {
        this.reporte1 = reporte1;
    }

    public Integer getReporte2() {
        return reporte2;
    }

    public void setReporte2(Integer reporte2) {
        this.reporte2 = reporte2;
    }

    public Integer getReporte3() {
        return reporte3;
    }

    public void setReporte3(Integer reporte3) {
        this.reporte3 = reporte3;
    }

    public Date getDesde6() {
        return desde6;
    }

    public void setDesde6(Date desde6) {
        this.desde6 = desde6;
    }

    public Date getHasta6() {
        return hasta6;
    }

    public void setHasta6(Date hasta6) {
        this.hasta6 = hasta6;
    }

    public Date getDesde7() {
        return desde7;
    }

    public void setDesde7(Date desde7) {
        this.desde7 = desde7;
    }

    public Date getHasta7() {
        return hasta7;
    }

    public void setHasta7(Date hasta7) {
        this.hasta7 = hasta7;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public Boolean getExcel() {
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

    public Date getDesde8() {
        return desde8;
    }

    public void setDesde8(Date desde8) {
        this.desde8 = desde8;
    }

    public Date getHasta8() {
        return hasta8;
    }

    public void setHasta8(Date hasta8) {
        this.hasta8 = hasta8;
    }

}
