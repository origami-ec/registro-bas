/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.bpm.models.SolicitanteInterviniente;
import com.origami.sgr.bpm.models.TramitesPorFechaIngresoAgrupadoRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.models.Remanentes;
import com.origami.sql.ConsultasSqlLocal;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class ReportesVentanilla extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(ReportesVentanilla.class.getName());

    @Inject
    private UserSession us;
    
    @Inject
    private ConsultasSqlLocal sql;

    protected int tipoConsulta = 0;
    protected int tipoReporte = 0;
    protected int tipoReporteHist = 0;
    protected Date desde = new Date();
    protected Date hasta = new Date();
    protected Date desde2 = new Date();
    protected Date hasta2 = new Date();
    protected Date desde3 = new Date();
    protected Date hasta3 = new Date();
    protected Calendar limite = Calendar.getInstance();
    protected AclUser user;
    protected Long usuario = 0L;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    protected List<AclUser> cajeros = new ArrayList<>();
    protected List<TramitesPorFechaIngresoAgrupadoRol> tramitesRol = new ArrayList<>();
    protected List<SolicitanteInterviniente> solic_interv = new ArrayList<>();
    protected CatEnte solicitanteInterviniente;
    protected String nombreCaja;
    protected List<Remanentes> remanentes;
    protected BigInteger cantidad;

    @PostConstruct
    protected void iniView() {
        try {
            cajeros = reg.getUsuariosByRolName("ventanilla");
            //cajeros.addAll(reg.getUsuariosByRolName("ventanilla_mercantil"));
            //cajeros.addAll(reg.getUsuariosByRolName("ventanilla_juridico"));
            limite.set(2022, 05, 01, 0, 0, 0);
            solicitanteInterviniente = new CatEnte();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void limpiarListas() {
        solic_interv = new ArrayList<>();
        tramitesRol = new ArrayList<>();
    }

    public void generarConsulta() {
        try {
            switch (tipoConsulta) {
                case 1:
                    if (solicitanteInterviniente.getId() != null) {
                        this.consultaSolicitanteInterviniente();
                    } else {
                        JsfUti.messageWarning(null, "Seleccione el Solicitante / Interviniente.", "");
                    }
                    break;
                default:
                    JsfUti.messageWarning(null, "Seleccione el tipo de consulta.", "");
                    break;
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarDocumento(boolean excel) {
        switch (tipoConsulta) {
            case 1:
                this.reporteSolicitanteInterviniente(excel);
                break;
        }
    }

    public void generarReportes(boolean excel) {
        try {
            AclUser temp = manager.find(AclUser.class, us.getUserId());
            switch (tipoReporte) {
                case 0:
                    JsfUti.messageWarning(null, "Debe Seleccionar el tipo de Reporte.", "");
                    break;
                case 1: // TRAMITES INGRESADOS
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("todoslostramites");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                case 2: // CERTIFICADOS INGRESADOS
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("TramitesCertificados");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                case 3: // TRAMITES SINE
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        //ss.setNombreReporte("TramitesRazones");
                        ss.setNombreReporte("TramitesJudicialesSine");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                case 4: // INSCRIPCIONES INGRESADAS
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("TramitesInscripciones");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                case 5: // TRAMITES JUDICIALES
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("TramitesJudiciales");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                case 6: // OBSERVACIONES CAC
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("reporteObservacionesCac");
                        ss.setNombreSubCarpeta("ingreso");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                case 7: // TRAMITES NO INGRESADOS
                    if (this.validarFechas(desde, hasta)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("tramitesNoIngresados");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("TESORERA", temp == null ? "" : temp.getEnte().getNombreCompleto());
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", user == null ? 0L : user.getId());
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_reporte.png"));
                        this.redirectReport(excel);
                    }
                    break;
                default:
                    JsfUti.messageWarning(null, "Problemas al generar el reporte seleccionado.", "");
                    break;
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReportesHist(boolean excel) {
        try {

            switch (tipoReporteHist) {
                case 0:
                    JsfUti.messageWarning(null, "Debe Seleccionar el tipo de Reporte.", "");
                    break;
                case 1: // CUADRE DE CAJA
                    if (this.validarFechas(desde3, hasta3)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("cuadreCajaHist");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("DESDE", sdf2.format(desde3));
                        ss.agregarParametro("HASTA", sdf2.format(hasta3));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta3));
                        ss.agregarParametro("USUARIO", usuario);
                        ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/diarios/");
                        this.redirectReport(excel);
                    }
                    break;
                case 2: // DESCUENTOS POR LEY
                    if (this.validarFechas(desde3, hasta3)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("descuentosHist");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("DESDE", sdf2.format(desde3));
                        ss.agregarParametro("HASTA", sdf2.format(hasta3));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta3));
                        ss.agregarParametro("USUARIO", usuario);
                        this.redirectReport(excel);
                    }
                    break;
                case 3: // DESCUENTOS POR LIMITE
                    if (this.validarFechas(desde3, hasta3)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("descuentosLimiteHist");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("DESDE", sdf2.format(desde3));
                        ss.agregarParametro("HASTA", sdf2.format(hasta3));
                        ss.agregarParametro("USUARIO", usuario);
                        this.redirectReport(excel);
                    }
                    break;
                case 4: // FACTURAS ANULADAS
                    if (this.validarFechas(desde3, hasta3)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("facturasAnuladasHist");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("DESDE", sdf2.format(desde3));
                        ss.agregarParametro("HASTA", sdf2.format(hasta3));
                        ss.agregarParametro("USUARIO", usuario);
                        this.redirectReport(excel);
                    }
                    break;
                case 5: // FACTURAS INGRESADAS POR EL BIESS
                    if (this.validarFechas(desde3, hasta3)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("facturasInBiessHist");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("DESDE", sdf2.format(desde3));
                        ss.agregarParametro("HASTA", sdf2.format(hasta3));
                        ss.agregarParametro("USUARIO", usuario);
                        this.redirectReport(excel);
                    }
                    break;
                case 6: // AGRUPADO POR ACTOS
                    if (this.validarFechas(desde3, hasta3)) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("detalleContratosHist");
                        ss.setNombreSubCarpeta("diarios");
                        ss.agregarParametro("DESDE", sdf2.format(desde3));
                        ss.agregarParametro("HASTA", sdf2.format(hasta3));
                        ss.agregarParametro("USUARIO", usuario);
                        this.redirectReport(excel);
                    }
                    break;
                default:
                    JsfUti.messageWarning(null, "Problemas al generar el reporte seleccionado.", "");
                    break;
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultaSolicitanteInterviniente() {
        this.limpiarListas();
        try {
            if (desde2 != null && hasta2 != null) {
                if (!desde2.after(hasta2)) {
                    solic_interv = sql.getSolicitanteInterviniente(sdf2.format(desde2), sdf2.format(hasta2), solicitanteInterviniente);
                    JsfUti.update("mainForm:mainTab");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteSolicitanteInterviniente(boolean excel) {
        try {
            if (!solic_interv.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(Boolean.FALSE);
                ss.setNombreReporte("reporteSolicitanteInterviniente");
                ss.setNombreSubCarpeta("diarios");
                ss.agregarParametro("DESDE", sdf2.format(desde2));
                ss.agregarParametro("HASTA", sdf2.format(hasta2));
                ss.agregarParametro("USER", us.getName_user());
                ss.setDataSource(solic_interv);
                this.redirectReport(excel);
            } else {
                JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validarFechas(Date desde, Date hasta) {
        if (user == null) {
            usuario = 0L;
            nombreCaja = "TODOS";
        } else {
            usuario = user.getId();
            nombreCaja = user.getUsuario();
        }
        if (desde.before(limite.getTime()) || hasta.equals(limite.getTime())) {
            JsfUti.messageWarning(null, "La fecha de consulta debe ser mayor a la fecha limite: "
                    + sdf.format(limite.getTime()), "");
            return false;
        }
        if (desde.before(hasta) || desde.equals(hasta)) {
            return true;
        } else {
            JsfUti.messageWarning(null, "Fecha Desde debe ser menos a fecha Hasta.", "");
            return false;
        }
    }

    public void cargaVariables() {
        BigInteger cantidadLiq, cantidadFactura;
        String q;
        String fd = sdf.format(desde);
        String fh = sdf.format(Utils.sumarRestarDiasFecha(hasta, 1));
        if (user == null) {
            q = "select count(*) from flow.regp_liquidacion where estado_liquidacion = 2 "
                    + "and numero_comprobante > 0 and "
                    + "fecha_ingreso between to_date('" + fd + "', 'dd/MM/yyyy') and to_date('" + fh + "', 'dd/MM/yyyy')";
            cantidadLiq = (BigInteger) manager.getNativeQuery(q);
            q = "select count(*) from financiero.ren_factura "
                    + "where estado = true and numero_comprobante > 0 "
                    + "and   fecha between to_date('" + fd + "', 'dd/MM/yyyy') and to_date('" + fh + "', 'dd/MM/yyyy')";
            cantidadFactura = (BigInteger) manager.getNativeQuery(q);
        } else {
            q = "select count(*) from flow.regp_liquidacion where estado_liquidacion = 2 "
                    + "and numero_comprobante > 0 and user_ingreso = " + user.getId() + " "
                    + "and fecha_ingreso between to_date('" + fd + "', 'dd/MM/yyyy') and to_date('" + fh + "', 'dd/MM/yyyy')";
            cantidadLiq = (BigInteger) manager.getNativeQuery(q);
            q = "select count(*) from financiero.ren_factura left outer join financiero.ren_cajero on financiero.ren_cajero.id =  financiero.ren_factura.caja "
                    + "where estado = true and numero_comprobante > 0 "
                    + " and usuario = " + user.getId() + " and  fecha between to_date('" + fd + "', 'dd/MM/yyyy') and to_date('" + fh + "', 'dd/MM/yyyy')";
            cantidadFactura = (BigInteger) manager.getNativeQuery(q);
        }
        if (cantidadFactura == null) {
            cantidadFactura = BigInteger.ZERO;
        }
        cantidad = cantidadLiq.add(cantidadFactura);
    }

    public void redirectReport(boolean excel) {
        if (excel) {
            JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
        } else {
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        }
    }

    public void selectObject(SelectEvent event) {
        solicitanteInterviniente = (CatEnte) event.getObject();
    }

    public int getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(int tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public int getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(int tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public int getTipoReporteHist() {
        return tipoReporteHist;
    }

    public void setTipoReporteHist(int tipoReporteHist) {
        this.tipoReporteHist = tipoReporteHist;
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

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public List<AclUser> getCajeros() {
        return cajeros;
    }

    public SimpleDateFormat getSdf2() {
        return sdf2;
    }

    public void setSdf2(SimpleDateFormat sdf2) {
        this.sdf2 = sdf2;
    }

    public List<TramitesPorFechaIngresoAgrupadoRol> getTramitesRol() {
        return tramitesRol;
    }

    public void setTramitesRol(List<TramitesPorFechaIngresoAgrupadoRol> tramitesRol) {
        this.tramitesRol = tramitesRol;
    }

    public void setCajeros(List<AclUser> cajeros) {
        this.cajeros = cajeros;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public List<SolicitanteInterviniente> getSolic_interv() {
        return solic_interv;
    }

    public void setSolic_interv(List<SolicitanteInterviniente> solic_interv) {
        this.solic_interv = solic_interv;
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

    public CatEnte getSolicitanteInterviniente() {
        return solicitanteInterviniente;
    }

    public void setSolicitanteInterviniente(CatEnte solicitanteInterviniente) {
        this.solicitanteInterviniente = solicitanteInterviniente;
    }

}
