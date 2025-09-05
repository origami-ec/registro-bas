/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.TarUsuarioTareas;
import com.origami.sgr.models.DetalleStatistics;
import com.origami.sgr.models.NamedItem;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Attachment;
import org.apache.commons.text.WordUtils;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DashBoardStatistics extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(DashBoardStatistics.class.getName());

    private static final long serialVersionUID = 1L;

    private List<NamedItem> actos, libros, usuariosDetalles;
    private NamedItem libroSeleccionado;
    private List<DetalleStatistics> processDetalle1;
    private String usuario, valuesJS, libro = "", colorsJS = "", estadoJS, estado, fechaJS, actoJS, days;
    private HorizontalBarChartModel horizontalBarModel;
    private BigInteger tCertificadosRecibidos, tCertificadosIngresados, tCertificadosRealizados, tCertificadosPendientes, tCertificadosEliminados,
            tInscripcionesRecibidas, tInscripcionesIngresadas, tInscripcionesRealizadas, tInscripcionesPendiente, tInscripcionesEliminados,
            tTotal, tTotalLibro;

    private Date desde;
    private Date hasta;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"), format;

    private String js;

    private String fechaJSProcess, actoJSProcess, mesJS;
    private String[] partsfechaJS, partsActoJS;

    private Boolean activarTotales, activarInscripcion, activarInscripcionUsuarios, detalleUsuario,
            activarCertificado, activarCertificadoUsuario, activarVerTodos, certificadores;

    private List<RegpLiquidacion> liquidacionesTemp, liquidaciones;
    private RegpLiquidacion liquidacion;
    private HistoricoTramites ht;
    private List<HistoricTaskInstance> tareas = new ArrayList<>();
    private HistoricTaskInstance tareaActual;
    private List<Attachment> listAttach = new ArrayList<>();
    private List<RegpDocsTarea> docs = new ArrayList<>();
    private int priority = 0;
    protected Boolean hasDoc = false, online = false;

    //VER TIEMPOS
    private List<AclRol> roles;
    private AclRol rolFilter;

    private Map<String, Object> filterss;
    private List<TarUsuarioTareas> usuariosTareas;
    private TarUsuarioTareas usuarioTarea;

    private BigInteger totalTramites, totalTramitesPendientes, totalTramitesFinalizados, totalTramitesEliminados, pendienteFecha;
    private String tiempoPromedio, tiempoEstimado;

    @PostConstruct
    protected void iniView() {
        try {
            //manager.executeFunction("flow.update_estado_tramite");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -1);
             desde = c.getTime();
            desde = new SimpleDateFormat("yyyy-MM-dd").parse("2021-11-15");
            //hasta = new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30");
            hasta = new Date();
            loadTramites();
            usuario = session.getName_user();
            activarTotales = Boolean.TRUE;
            activarInscripcion = Boolean.FALSE;
            activarInscripcionUsuarios = Boolean.FALSE;

            activarCertificado = Boolean.FALSE;
            detalleUsuario = Boolean.FALSE;

            activarVerTodos = Boolean.TRUE;
            //inscripcionesXusuarios();
        } catch (Exception e) {
            Logger.getLogger(DashBoardStatistics.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loadTramites() {
        tInscripcionesRecibidas = (BigInteger) manager.getNativeQuery(Querys.getTramitesRecibidos, new Object[]{Boolean.TRUE, sdf.format(desde), sdf.format(hasta)});
        tInscripcionesIngresadas = (BigInteger) manager.getNativeQuery(Querys.getTramitesIngresados, new Object[]{Boolean.TRUE, sdf.format(desde), sdf.format(hasta)});
        tInscripcionesRealizadas = (BigInteger) manager.getNativeQuery(Querys.getTramitesByEstado, new Object[]{Boolean.TRUE, "FINALIZADO", sdf.format(desde), sdf.format(hasta)});
        tInscripcionesPendiente = (BigInteger) manager.getNativeQuery(Querys.getTramitesByEstado, new Object[]{Boolean.TRUE, "PENDIENTE", sdf.format(desde), sdf.format(hasta)});
        tInscripcionesEliminados = (BigInteger) manager.getNativeQuery(Querys.getTramitesByEstado, new Object[]{Boolean.TRUE, "ELIMINADO", sdf.format(desde), sdf.format(hasta)});

        tCertificadosRecibidos = (BigInteger) manager.getNativeQuery(Querys.getTramitesRecibidos, new Object[]{Boolean.FALSE, sdf.format(desde), sdf.format(hasta)});
        tCertificadosIngresados = (BigInteger) manager.getNativeQuery(Querys.getTramitesIngresados, new Object[]{Boolean.FALSE, sdf.format(desde), sdf.format(hasta)});
        tCertificadosRealizados = (BigInteger) manager.getNativeQuery(Querys.getTramitesByEstado, new Object[]{Boolean.FALSE, "FINALIZADO", sdf.format(desde), sdf.format(hasta)});
        tCertificadosPendientes = (BigInteger) manager.getNativeQuery(Querys.getTramitesByEstado, new Object[]{Boolean.FALSE, "PENDIENTE", sdf.format(desde), sdf.format(hasta)});
        tCertificadosEliminados = (BigInteger) manager.getNativeQuery(Querys.getTramitesByEstado, new Object[]{Boolean.FALSE, "ELIMINADO", sdf.format(desde), sdf.format(hasta)});

        tTotal = tInscripcionesIngresadas.add(tCertificadosIngresados);

        js = "cargarPolarInscripciones(" + tInscripcionesIngresadas.toString() + ", "
                + tInscripcionesRealizadas.toString() + "," + tInscripcionesPendiente.toString() + "," + tInscripcionesEliminados.toString() + ")";
        JsfUti.executeJS(js);

        js = "cargarPolarCertificados(" + tCertificadosIngresados.toString() + ", "
                + tCertificadosRealizados.toString() + "," + tCertificadosPendientes.toString() + "," + tCertificadosEliminados.toString() + ")";
        JsfUti.executeJS(js);

    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void totalesInscripciones() {
        libro = "";
        valuesJS = "";
        colorsJS = "";
        tTotalLibro = BigInteger.ZERO;
        estadoJS = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("estadoJS");
        estado = estadoJS;
        estadoJS = estadoJS.substring(0, estadoJS.length() - 1).toUpperCase();
        activarTotales = Boolean.FALSE;
        activarInscripcionUsuarios = Boolean.FALSE;
        activarCertificado = Boolean.FALSE;
        activarInscripcion = Boolean.TRUE;
        if (estadoJS.equals("INGRESADO")) {
            libros = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class, Querys.getLibrosPorActoTotales,
                    new Object[]{sdf.format(desde), sdf.format(hasta)});
        } else {
            libros = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class, Querys.getLibrosPorActo,
                    new Object[]{estadoJS, sdf.format(desde), sdf.format(hasta)});
        }

        for (NamedItem i : libros) {
            i.setColor(Utils.colorRandom());
            i.setNombre(WordUtils.capitalizeFully(i.getNombre()));
            tTotalLibro = tTotalLibro.add(i.getCantidad());
            libro = libro + ", '" + i.getNombre() + "'";
            valuesJS = valuesJS + ", " + i.getCantidad();
            colorsJS = colorsJS + ", '" + i.getColor() + "'";

        }
        libroSeleccionado = libros.get(0);
        JsfUti.executeJS("cargarBarLibro(  [" + libro.substring(1, libro.length()) + "] ,"
                + " [ " + valuesJS.substring(1, valuesJS.length()) + " ] ,"
                + " [ " + colorsJS.substring(1, colorsJS.length()) + " ]  )");
        loadActos();
    }

    public void loadActos() {
        days = "";
        String mes = "", fecha = "", day;
        Integer diaValor, mesValor;
        actos = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class,
                Querys.getActosPorLibro, new Object[]{libroSeleccionado.getId(), sdf.format(desde), sdf.format(hasta)});

        LocalDate start = LocalDate.parse(sdf.format(desde)),
                end = LocalDate.parse(sdf.format(hasta));

        LocalDate next = start.minusDays(1);
        while ((next = next.plusDays(1)).isBefore(end.plusDays(1))) {
            if (!(next.getDayOfWeek() == DayOfWeek.SATURDAY
                    || next.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                mesValor = next.getMonthValue();
                diaValor = next.getDayOfMonth();
                mes = WordUtils.capitalizeFully(Utils.convertirMesALetra(mesValor).substring(0, 3));
                day = diaValor + "-" + mes + "-" + Integer.toString(next.getYear()).substring(2, 4);
                days = days + ", '" + day + "'";
                fecha = next.getYear() + "/" + mesValor + "/" + diaValor;
                for (NamedItem n : actos) {
                    if (estadoJS.equals("INGRESADO")) {
                        n.getCantidades().add((BigInteger) manager.getNativeQuery(
                                Querys.getTotalActosIngresadosPorDia,
                                new Object[]{Boolean.TRUE, n.getId(), fecha}));
                    } else {
                        n.getCantidades().add((BigInteger) manager.getNativeQuery(
                                Querys.getTotalActosPorDia,
                                new Object[]{Boolean.TRUE, n.getId(), estadoJS, fecha}));
                    }

                }
            }
        }
        valuesJS = "";
        for (NamedItem n : actos) {
            n.setColor(Utils.colorRandom());
            n.setNombre(n.getId().toString() + ".- " + WordUtils.capitalizeFully(n.getNombre()));
            valuesJS = valuesJS + ", " + n.toString();
        }
        JsfUti.executeJS("cargarLinearInscripciones( 'line-chart-" + libroSeleccionado.getId().toString()
                + "', [" + days.substring(1, days.length()) + "] , "
                + "[ " + valuesJS.substring(1, valuesJS.length()) + " ])");
    }

    public void returnInit() {
        activarTotales = Boolean.TRUE;
        activarInscripcion = Boolean.FALSE;
        activarInscripcionUsuarios = Boolean.FALSE;
        activarCertificadoUsuario = Boolean.FALSE;
        activarCertificado = Boolean.FALSE;
        detalleUsuario = Boolean.FALSE;
        loadTramites();
    }

    public void returnComparativaActos() {
        activarTotales = Boolean.FALSE;
        activarInscripcionUsuarios = Boolean.FALSE;
        activarCertificadoUsuario = Boolean.FALSE;
        detalleUsuario = Boolean.FALSE;
        activarInscripcion = Boolean.TRUE;
    }

    public void tramitesAsociados() throws ParseException {
        format = new SimpleDateFormat("dd-MM-yyyy");
        fechaJS = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fechaJS");
        actoJS = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("actoJS");

        partsActoJS = actoJS.split("-");
        actoJSProcess = partsActoJS[0].substring(0, partsActoJS[0].length() - 1);

        partsfechaJS = fechaJS.split("-");
        mesJS = Utils.convertirLetraAMes(partsfechaJS[1]).toString();
        fechaJSProcess = (partsfechaJS[0].length() <= 1 ? ("0" + partsfechaJS[0]) : partsfechaJS[0]) + "-"
                + (mesJS.length() <= 1 ? ("0" + mesJS) : mesJS) + "-20" + partsfechaJS[2];

        liquidacionesTemp = manager.findAll(Querys.getTramitesEstadistica,
                new String[]{"actoID", "fechaJS"}, new Object[]{Long.valueOf(actoJSProcess), format.parse(fechaJSProcess)});
        System.out.println("liquidacionesTemp: " + liquidacionesTemp.size());
        System.out.println("fechaJSProcess: " + fechaJSProcess);
        System.out.println("actoJSProcess: " + actoJSProcess);
        System.out.println("estadoJS: " + estadoJS);

        if (!estadoJS.equals("INGRESADO")) {
            liquidaciones = new ArrayList<>();
            for (RegpLiquidacion liq : liquidacionesTemp) {
                if (((BigInteger) manager.getNativeQuery(
                        Querys.getRenLiquidacionTramitesByEstado,
                        new Object[]{liq.getNumTramiteRp(), estadoJS})).intValue() > 0) {
                    liquidaciones.add(liq);
                };
            }
        } else {
            liquidaciones = liquidacionesTemp;
        }

        JsfUti.executeJS("PF('dlgTramiteAsociados').show();");
        JsfUti.update("frmTramites");

    }

    public void informacionTramite(RegpLiquidacion re) {
        try {

            ht = re.getTramite();
            liquidacion = re;
            if (liquidacion.getTramite().getIdProceso() == null) {
                tareas = new ArrayList<>();
                listAttach = new ArrayList<>();
            } else {
                tareas = this.getTaskByProcessInstanceIdMain(liquidacion.getTramite().getIdProceso());
                if (!tareas.isEmpty()) {
                    tareaActual = tareas.get(0);
                    priority = tareaActual.getPriority();
                }
                docs = itl.getDocumentosTareas(ht.getId());
                //this.initDocEsc(ht.getNumTramite().toString());
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

    public void showProforma(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("comprobante_proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab("/sgr/Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void inscripcionesXusuarios() {
        activarTotales = Boolean.FALSE;
        activarCertificado = Boolean.FALSE;
        activarInscripcion = Boolean.FALSE;
        activarInscripcionUsuarios = Boolean.TRUE;
        activarCertificadoUsuario = Boolean.TRUE;
        activarVerTodos = Boolean.TRUE;
        rolFilter = (AclRol) manager.find(AclRol.class, 9L);
        filterss = new HashMap<>();
        filterss.put("estado", Boolean.TRUE);
        filterss.put("rol", rolFilter);
        usuariosTareas = manager.findObjectByParameterList(TarUsuarioTareas.class, filterss);

        detalleUsuario = Boolean.TRUE;
        loadDetalleTramiteXTodosUsuario();

    }

    public void certificadosXusuarios() {
        activarTotales = Boolean.FALSE;
        activarCertificado = Boolean.FALSE;
        activarInscripcion = Boolean.FALSE;
        activarInscripcionUsuarios = Boolean.TRUE;
        activarCertificadoUsuario = Boolean.TRUE;
        activarVerTodos = Boolean.TRUE;
        rolFilter = (AclRol) manager.find(AclRol.class, 8L);

        filterss = new HashMap<>();
        filterss.put("estado", Boolean.TRUE);
        filterss.put("rol", rolFilter);
        usuariosTareas = manager.findObjectByParameterList(TarUsuarioTareas.class, filterss);

        detalleUsuario = Boolean.TRUE;
        loadDetalleTramiteXTodosUsuario();

    }

    public void loadDetalleTramiteXusuario(TarUsuarioTareas tt) {
        activarVerTodos = Boolean.FALSE;
        usuariosDetalles = new ArrayList<>();
        NamedItem item;
        detalleUsuario = Boolean.TRUE;
        usuarioTarea = tt;
        valuesJS = "";
        days = "";
        String valuesPendientesJS = "";
        String mes = "", fecha = "", day, us = usuarioTarea.getUsuario().getUsuario();
        Integer diaValor, mesValor;

        LocalDate start = LocalDate.parse(sdf.format(desde)),
                end = LocalDate.parse(sdf.format(hasta));

        LocalDate next = start.minusDays(1);
        while ((next = next.plusDays(1)).isBefore(end.plusDays(1))) {
            if (!(next.getDayOfWeek() == DayOfWeek.SATURDAY
                    || next.getDayOfWeek() == DayOfWeek.SUNDAY)) {

                mesValor = next.getMonthValue();
                diaValor = next.getDayOfMonth();
                mes = WordUtils.capitalizeFully(Utils.convertirMesALetra(mesValor).substring(0, 3));
                day = diaValor + "-" + mes + "-" + Integer.toString(next.getYear()).substring(2, 4);
                days = days + ", '" + day + "'";
                fecha = next.getYear() + "-" + mesValor + "-" + diaValor;

                item = new NamedItem();

                item.setFinalizados((BigInteger) manager.getNativeQuery(
                        Querys.getTotalTramitesEstadoXUSusario,
                        new Object[]{us, "FINALIZADO", fecha}));

                item.setPendientes((BigInteger) manager.getNativeQuery(
                        Querys.getTotalTramitesPendienteXUSusario,
                        new Object[]{us, fecha}));

                item.setEliminados((BigInteger) manager.getNativeQuery(
                        Querys.getTotalTramitesEstadoXUSusario,
                        new Object[]{us, "ELIMINADO", fecha}));

                valuesJS = valuesJS + ", " + item.getFinalizados();
                valuesPendientesJS = valuesPendientesJS + ", " + item.getPendientes();
                usuariosDetalles.add(item);
            }
        }
        totalTramitesPendientes = BigInteger.ZERO;
        totalTramitesEliminados = BigInteger.ZERO;
        totalTramitesFinalizados = BigInteger.ZERO;
        for (NamedItem i : usuariosDetalles) {
            totalTramitesPendientes = totalTramitesPendientes.add(i.getPendientes());
            totalTramitesEliminados = totalTramitesEliminados.add(i.getEliminados());
            totalTramitesFinalizados = totalTramitesFinalizados.add(i.getFinalizados());
        }

        tiempoEstimado = ((BigDecimal) manager.getNativeQuery(
                Querys.getTiempoEstimadoUsuario,
                new Object[]{us, sdf.format(desde), sdf.format(hasta)})).toString() + " días";

        tiempoPromedio = ((BigDecimal) manager.getNativeQuery(
                Querys.getTiempoPromedioUsuario,
                new Object[]{us, sdf.format(desde), sdf.format(hasta)})).toString() + " días";

        totalTramites = totalTramitesPendientes.add(totalTramitesEliminados).add(totalTramitesFinalizados);

        if (totalTramitesPendientes.compareTo(totalTramitesFinalizados) == 1) {
            pendienteFecha = totalTramitesPendientes.subtract(totalTramitesFinalizados);
        } else {
            pendienteFecha = totalTramitesFinalizados.subtract(totalTramitesPendientes);
        }
        System.out.println("'line-chart-" + us + "'");
        JsfUti.executeJS("cargarLinearTramitesUsuario( 'line-chart-" + us + "', "
                //JsfUti.executeJS("cargarLinearTramitesUsuario( 'line-chart-usuarioTarea', "
                + "[" + days.substring(1, days.length()) + "] , "
                + "[ " + valuesJS.substring(1, valuesJS.length()) + " ], '" + Utils.colorRandom()
                + "', [ " + valuesPendientesJS.substring(1, valuesPendientesJS.length()) + " ] ,"
                + " '" + Utils.colorRandom() + "', '" + us + "')");

    }

    public void tramitesAsociadosUsuario() throws ParseException {
        format = new SimpleDateFormat("dd-MM-yyyy");
        fechaJS = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fechaJS");
        String estadoTramiteAsociadoUsuario
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("estadoTramiteAsociadoUsuario");

        String tramiteAsociadoUsuario
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tramiteAsociadoUsuario");

        actoJS = "Trámites " + estadoTramiteAsociadoUsuario;

        partsfechaJS = fechaJS.split("-");
        mesJS = Utils.convertirLetraAMes(partsfechaJS[1]).toString();
        fechaJSProcess = (partsfechaJS[0].length() <= 1 ? ("0" + partsfechaJS[0]) : partsfechaJS[0]) + "-"
                + (mesJS.length() <= 1 ? ("0" + mesJS) : mesJS) + "-20" + partsfechaJS[2];

        List<NamedItem> numTramites = new ArrayList<>();
        if (estadoTramiteAsociadoUsuario.equals("FINALIZADO")) {
            numTramites = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class,
                    Querys.getTramitesEstadisticaUsuario,
                    new Object[]{tramiteAsociadoUsuario, estadoTramiteAsociadoUsuario,
                        format.parse(fechaJSProcess)});

        } else if (estadoTramiteAsociadoUsuario.equals("PENDIENTE")) {
            numTramites = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class,
                    Querys.getTramitesEstadisticaPendientesUsuario,
                    new Object[]{tramiteAsociadoUsuario,
                        format.parse(fechaJSProcess)});
        }

        liquidaciones = new ArrayList<>();
        for (NamedItem i : numTramites) {
            map = new HashMap<>();
            map.put("numTramiteRp", i.getNum_tramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            liquidaciones.add(liquidacion);
        }

        JsfUti.executeJS("PF('dlgTramiteAsociados').show();");
        JsfUti.update("frmTramites");

    }

    public void totalesCertificaciones() {
        libro = "";
        valuesJS = "";
        colorsJS = "";
        tTotalLibro = BigInteger.ZERO;
        estadoJS = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("estadoJS");
        estado = estadoJS;
        estadoJS = estadoJS.substring(0, estadoJS.length() - 1).toUpperCase();
        activarTotales = Boolean.FALSE;
        activarInscripcionUsuarios = Boolean.FALSE;
        activarInscripcion = Boolean.FALSE;
        activarCertificado = Boolean.TRUE;
        if (estadoJS.equals("INGRESADO")) {
            libros = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class, Querys.getCertificadosStadisticaTotales,
                    new Object[]{sdf.format(desde), sdf.format(hasta)});
        } else {
            libros = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class, Querys.getCertificadosStadistica,
                    new Object[]{estadoJS, sdf.format(desde), sdf.format(hasta)});
        }

        for (NamedItem i : libros) {
            i.setColor(Utils.colorRandom());
            if (i.getNombre().contains("CERTIFICADO DE")) {
                i.setNombre(i.getNombre().replace("CERTIFICADO DE", ""));
            } else if (i.getNombre().contains("CERTIFICADO")) {
                i.setNombre(i.getNombre().replace("CERTIFICADO", ""));
            }
            i.setNombre(WordUtils.capitalizeFully(i.getNombre()));
            tTotalLibro = tTotalLibro.add(i.getCantidad());
            libro = libro + ", '" + i.getNombre() + "'";
            valuesJS = valuesJS + ", " + i.getCantidad();
            colorsJS = colorsJS + ", '" + i.getColor() + "'";

        }
        libroSeleccionado = libros.get(0);
        JsfUti.executeJS("cargarBarLibro(  [" + libro.substring(1, libro.length()) + "] ,"
                + " [ " + valuesJS.substring(1, valuesJS.length()) + " ] ,"
                + " [ " + colorsJS.substring(1, colorsJS.length()) + " ]  )");
        loadCertificados();
    }

    public void loadCertificados() {
        days = "";
        String mes = "", fecha = "", day;
        Integer diaValor, mesValor;
        actos = (List<NamedItem>) manager.getNativeQueryParameter(NamedItem.class,
                Querys.getActosPorLibro, new Object[]{libroSeleccionado.getId(), sdf.format(desde), sdf.format(hasta)});

        LocalDate start = LocalDate.parse(sdf.format(desde)),
                end = LocalDate.parse(sdf.format(hasta));

        LocalDate next = start.minusDays(1);
        while ((next = next.plusDays(1)).isBefore(end.plusDays(1))) {
            if (!(next.getDayOfWeek() == DayOfWeek.SATURDAY
                    || next.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                mesValor = next.getMonthValue();
                diaValor = next.getDayOfMonth();
                mes = WordUtils.capitalizeFully(Utils.convertirMesALetra(mesValor).substring(0, 3));
                day = diaValor + "-" + mes + "-" + Integer.toString(next.getYear()).substring(2, 4);
                days = days + ", '" + day + "'";
                fecha = next.getYear() + "/" + mesValor + "/" + diaValor;
                for (NamedItem n : libros) {
                    if (estadoJS.equals("INGRESADO")) {
                        n.getCantidades().add((BigInteger) manager.getNativeQuery(
                                Querys.getTotalActosIngresadosPorDia,
                                new Object[]{Boolean.FALSE, n.getId(), fecha}));
                    } else {
                        n.getCantidades().add((BigInteger) manager.getNativeQuery(
                                Querys.getTotalActosPorDia,
                                new Object[]{Boolean.FALSE, n.getId(), estadoJS, fecha}));
                    }

                }
            }
        }
        valuesJS = "";
        for (NamedItem n : libros) {
            n.setColor(Utils.colorRandom());
            n.setNombre(n.getId().toString() + ".- " + WordUtils.capitalizeFully(n.getNombre()));
            valuesJS = valuesJS + ", " + n.toString();
        }
        JsfUti.executeJS("cargarLinearInscripciones( 'line-chart-certificado', "
                + "[" + days.substring(1, days.length()) + "] , [ " + valuesJS.substring(1, valuesJS.length()) + " ])");
    }

    public void loadDetalleTramiteXTodosUsuario() {
        //detalleUsuario = Boolean.FALSE;
        usuariosDetalles = new ArrayList<>();
        NamedItem item;
        //detalleUsuario = Boolean.TRUE;
        usuarioTarea = null;
        valuesJS = "";
        days = "";
        String mes = "", fecha = "", day;
        Integer diaValor, mesValor;

        LocalDate start = LocalDate.parse(sdf.format(desde)),
                end = LocalDate.parse(sdf.format(hasta));

        LocalDate next = start.minusDays(1);
        for (TarUsuarioTareas tt : usuariosTareas) {
            item = new NamedItem();
            item.setId(new BigInteger(tt.getId().toString()));
            item.setNombre(tt.getUsuario().getEnte().getNombres());
            item.setUsuario(tt.getUsuario().getUsuario());
            usuariosDetalles.add(item);
        }

        while ((next = next.plusDays(1)).isBefore(end.plusDays(1))) {
            if (!(next.getDayOfWeek() == DayOfWeek.SATURDAY
                    || next.getDayOfWeek() == DayOfWeek.SUNDAY)) {

                mesValor = next.getMonthValue();
                diaValor = next.getDayOfMonth();
                mes = WordUtils.capitalizeFully(Utils.convertirMesALetra(mesValor).substring(0, 3));
                day = diaValor + "-" + mes + "-" + Integer.toString(next.getYear()).substring(2, 4);
                days = days + ", '" + day + "'";
                fecha = next.getYear() + "-" + mesValor + "-" + diaValor;

                for (NamedItem n : usuariosDetalles) {
                    n.getCantidades().add((BigInteger) manager.getNativeQuery(
                            Querys.getTotalTramitesEstadoXUSusario,
                            new Object[]{n.getUsuario(), "FINALIZADO", fecha}));
                }

            }
        }

        for (NamedItem n : usuariosDetalles) {
            n.setColor(Utils.colorRandom());
            n.setNombre(WordUtils.capitalizeFully(n.getNombre()));
            valuesJS = valuesJS + ", " + n.toString();
            n.setTiempoEstimado(((BigDecimal) manager.getNativeQuery(
                    Querys.getTiempoEstimadoUsuario,
                    new Object[]{n.getUsuario(), sdf.format(desde), sdf.format(hasta)})).toString() + " días");
            n.setTiempoPromedioReal(((BigDecimal) manager.getNativeQuery(
                    Querys.getTiempoPromedioUsuario,
                    new Object[]{n.getUsuario(), sdf.format(desde), sdf.format(hasta)})).toString() + " días");

            for (BigInteger i : n.getCantidades()) {
                n.setCantidad(i.add(n.getCantidad()));
            }

        }

//        totalTramites = totalTramitesPendientes.add(totalTramitesEliminados).add(totalTramitesFinalizados);
//
//        if (totalTramitesPendientes.compareTo(totalTramitesFinalizados) == 1) {
//            pendienteFecha = totalTramitesPendientes.subtract(totalTramitesFinalizados);
//        } else {
//            pendienteFecha = totalTramitesFinalizados.subtract(totalTramitesPendientes);
//        }
        JsfUti.executeJS("cargarLinearTodosUsuarios( 'line-chart-todos-usuario', [" + days.substring(1, days.length()) + "] , "
                + "[ " + valuesJS.substring(1, valuesJS.length()) + " ])");

    }

    public TarUsuarioTareas getUsuarioTarea() {
        return usuarioTarea;
    }

    public void setUsuarioTarea(TarUsuarioTareas usuarioTarea) {
        this.usuarioTarea = usuarioTarea;
    }

    public String nombresUsuario(String nombre) {
        return WordUtils.capitalizeFully(nombre);
    }

    public List<TarUsuarioTareas> getUsuariosTareas() {
        return usuariosTareas;
    }

    public void setUsuariosTareas(List<TarUsuarioTareas> usuariosTareas) {
        this.usuariosTareas = usuariosTareas;
    }

    public List<AclRol> getRoles() {
        return roles;
    }

    public void setRoles(List<AclRol> roles) {
        this.roles = roles;
    }

    public AclRol getRolFilter() {
        return rolFilter;
    }

    public void setRolFilter(AclRol rolFilter) {
        this.rolFilter = rolFilter;
    }

    public List<NamedItem> getActos() {
        return actos;
    }

    public void setActos(List<NamedItem> actos) {
        this.actos = actos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<DetalleStatistics> getProcessDetalle1() {
        return processDetalle1;
    }

    public void setProcessDetalle1(List<DetalleStatistics> processDetalle1) {
        this.processDetalle1 = processDetalle1;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
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

    public BigInteger gettCertificadosRecibidos() {
        return tCertificadosRecibidos;
    }

    public void settCertificadosRecibidos(BigInteger tCertificadosRecibidos) {
        this.tCertificadosRecibidos = tCertificadosRecibidos;
    }

    public BigInteger gettInscripcionesRecibidas() {
        return tInscripcionesRecibidas;
    }

    public void settInscripcionesRecibidas(BigInteger tInscripcionesRecibidas) {
        this.tInscripcionesRecibidas = tInscripcionesRecibidas;
    }

    public BigInteger gettInscripcionesIngresadas() {
        return tInscripcionesIngresadas;
    }

    public void settInscripcionesIngresadas(BigInteger tInscripcionesIngresadas) {
        this.tInscripcionesIngresadas = tInscripcionesIngresadas;
    }

    public BigInteger gettInscripcionesRealizadas() {
        return tInscripcionesRealizadas;
    }

    public void settInscripcionesRealizadas(BigInteger tInscripcionesRealizadas) {
        this.tInscripcionesRealizadas = tInscripcionesRealizadas;
    }

    public BigInteger gettInscripcionesPendiente() {
        return tInscripcionesPendiente;
    }

    public void settInscripcionesPendiente(BigInteger tInscripcionesPendiente) {
        this.tInscripcionesPendiente = tInscripcionesPendiente;
    }

    public BigInteger gettCertificadosIngresados() {
        return tCertificadosIngresados;
    }

    public void settCertificadosIngresados(BigInteger tCertificadosIngresados) {
        this.tCertificadosIngresados = tCertificadosIngresados;
    }

    public BigInteger gettCertificadosRealizados() {
        return tCertificadosRealizados;
    }

    public void settCertificadosRealizados(BigInteger tCertificadosRealizados) {
        this.tCertificadosRealizados = tCertificadosRealizados;
    }

    public BigInteger gettCertificadosPendientes() {
        return tCertificadosPendientes;
    }

    public void settCertificadosPendientes(BigInteger tCertificadosPendientes) {
        this.tCertificadosPendientes = tCertificadosPendientes;
    }

    public BigInteger gettTotal() {
        return tTotal;
    }

    public void settTotal(BigInteger tTotal) {
        this.tTotal = tTotal;
    }

    public Boolean getActivarTotales() {
        return activarTotales;
    }

    public void setActivarTotales(Boolean activarTotales) {
        this.activarTotales = activarTotales;
    }

    public BigInteger gettCertificadosEliminados() {
        return tCertificadosEliminados;
    }

    public void settCertificadosEliminados(BigInteger tCertificadosEliminados) {
        this.tCertificadosEliminados = tCertificadosEliminados;
    }

    public BigInteger gettInscripcionesEliminados() {
        return tInscripcionesEliminados;
    }

    public void settInscripcionesEliminados(BigInteger tInscripcionesEliminados) {
        this.tInscripcionesEliminados = tInscripcionesEliminados;
    }

    public Boolean getActivarInscripcion() {
        return activarInscripcion;
    }

    public void setActivarInscripcion(Boolean activarInscripcion) {
        this.activarInscripcion = activarInscripcion;
    }

    public Boolean getActivarInscripcionUsuarios() {
        return activarInscripcionUsuarios;
    }

    public void setActivarInscripcionUsuarios(Boolean activarInscripcionUsuarios) {
        this.activarInscripcionUsuarios = activarInscripcionUsuarios;
    }

    public Boolean getActivarCertificado() {
        return activarCertificado;
    }

    public void setActivarCertificado(Boolean activarCertificado) {
        this.activarCertificado = activarCertificado;
    }

    public Boolean getActivarCertificadoUsuario() {
        return activarCertificadoUsuario;
    }

    public void setActivarCertificadoUsuario(Boolean activarCertificadoUsuario) {
        this.activarCertificadoUsuario = activarCertificadoUsuario;
    }

    public List<NamedItem> getLibros() {
        return libros;
    }

    public void setLibros(List<NamedItem> libros) {
        this.libros = libros;
    }

    public BigInteger gettTotalLibro() {
        return tTotalLibro;
    }

    public void settTotalLibro(BigInteger tTotalLibro) {
        this.tTotalLibro = tTotalLibro;
    }

    public NamedItem getLibroSeleccionado() {
        return libroSeleccionado;
    }

    public void setLibroSeleccionado(NamedItem libroSeleccionado) {
        this.libroSeleccionado = libroSeleccionado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<RegpLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<RegpLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public String getFechaJSProcess() {
        return fechaJSProcess;
    }

    public void setFechaJSProcess(String fechaJSProcess) {
        this.fechaJSProcess = fechaJSProcess;
    }

    public String getActoJSProcess() {
        return actoJSProcess;
    }

    public void setActoJSProcess(String actoJSProcess) {
        this.actoJSProcess = actoJSProcess;
    }

    public String getActoJS() {
        return actoJS;
    }

    public void setActoJS(String actoJS) {
        this.actoJS = actoJS;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public List<Attachment> getListAttach() {
        return listAttach;
    }

    public void setListAttach(List<Attachment> listAttach) {
        this.listAttach = listAttach;
    }

    public List<RegpDocsTarea> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTarea> docs) {
        this.docs = docs;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public HistoricTaskInstance getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(HistoricTaskInstance tareaActual) {
        this.tareaActual = tareaActual;
    }

    public BigInteger getTotalTramites() {
        return totalTramites;
    }

    public void setTotalTramites(BigInteger totalTramites) {
        this.totalTramites = totalTramites;
    }

    public BigInteger getTotalTramitesPendientes() {
        return totalTramitesPendientes;
    }

    public void setTotalTramitesPendientes(BigInteger totalTramitesPendientes) {
        this.totalTramitesPendientes = totalTramitesPendientes;
    }

    public BigInteger getTotalTramitesFinalizados() {
        return totalTramitesFinalizados;
    }

    public void setTotalTramitesFinalizados(BigInteger totalTramitesFinalizados) {
        this.totalTramitesFinalizados = totalTramitesFinalizados;
    }

    public BigInteger getTotalTramitesEliminados() {
        return totalTramitesEliminados;
    }

    public void setTotalTramitesEliminados(BigInteger totalTramitesEliminados) {
        this.totalTramitesEliminados = totalTramitesEliminados;
    }

    public Boolean getDetalleUsuario() {
        return detalleUsuario;
    }

    public void setDetalleUsuario(Boolean detalleUsuario) {
        this.detalleUsuario = detalleUsuario;
    }

    public String getTiempoPromedio() {
        return tiempoPromedio;
    }

    public void setTiempoPromedio(String tiempoPromedio) {
        this.tiempoPromedio = tiempoPromedio;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public BigInteger getPendienteFecha() {
        return pendienteFecha;
    }

    public void setPendienteFecha(BigInteger pendienteFecha) {
        this.pendienteFecha = pendienteFecha;
    }

    public Boolean getActivarVerTodos() {
        return activarVerTodos;
    }

    public void setActivarVerTodos(Boolean activarVerTodos) {
        this.activarVerTodos = activarVerTodos;
    }

    public List<NamedItem> getUsuariosDetalles() {
        return usuariosDetalles;
    }

    public void setUsuariosDetalles(List<NamedItem> usuariosDetalles) {
        this.usuariosDetalles = usuariosDetalles;
    }

}
