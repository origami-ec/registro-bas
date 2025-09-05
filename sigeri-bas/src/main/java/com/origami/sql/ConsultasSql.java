/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sql;

import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimientoFichas;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimientoPartes;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimiento;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimiento;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoCapitales;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoRepresentantes;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoSocios;
import com.origami.sgr.bpm.models.CantidadTramitesPorUsuario;
import com.origami.sgr.bpm.models.DataModel;
import com.origami.sgr.bpm.models.DatosTramitesFinalizados;
import com.origami.sgr.bpm.models.EvaluacionFuncionarios;
import com.origami.sgr.bpm.models.MovimientosModificadosIngresados;
import com.origami.sgr.bpm.models.Remanentes;
import com.origami.sgr.bpm.models.ReporteTramitesRp;
import com.origami.sgr.bpm.models.SolicitanteInterviniente;
import com.origami.sgr.bpm.models.TareaEntregaDocumento;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.bpm.models.TareasPorFechaIngresoEntrega;
import com.origami.sgr.bpm.models.TareasSinRealizar;
import com.origami.sgr.bpm.models.TramitesCorregidos;
import com.origami.sgr.bpm.models.TramitesParaRegistrador;
import com.origami.sgr.bpm.models.TramitesPorFechaIngresoAgrupadoRol;
import com.origami.sgr.bpm.models.TramitesReasignados;
import com.origami.sgr.bpm.models.TramitesVencidosElaborados;
import com.origami.sgr.bpm.models.TramitesVencidosPendientes;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.services.interfaces.BpmBaseEngine;
import com.origami.sgr.services.interfaces.DatabaseLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.activiti.engine.history.HistoricTaskInstance;

/**
 *
 * @author Anyelo
 */
@Stateless(name = "consultasSql")
public class ConsultasSql implements ConsultasSqlLocal {

    @EJB(beanName = "manager")
    private Entitymanager manager;

    @EJB(beanName = "bpmBaseEngine")
    protected BpmBaseEngine engine;

    @EJB(beanName = "dataSource")
    private DatabaseLocal ds;

    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public List<DataModel> getCantidadesByActoEntregado(String desde, String hasta) {
        Connection con = null;
        DataModel dm;
        List<DataModel> list = new ArrayList<>();
        String query = "select distinct(de.acto), de.nombre, count(de.nombre) from act_hi_taskinst ht "
                + "left join historico_tramites_view htv on (htv.id_proceso = ht.proc_inst_id_) "
                + "left join liquidacion_view li on (li.tramite = htv.id) "
                + "left join detalle_liquidacion_view de on (de.liquidacion = li.id) "
                + "where ht.task_def_key_ = 'entregaDocs' and ht.end_time_ between to_date('" + desde + "', 'dd/MM/yyyy') "
                + "and to_date('" + hasta + "', 'dd/MM/yyyy') group by de.acto, de.nombre order by de.nombre";
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dm = new DataModel();
                dm.setId(rs.getLong(1));
                dm.setNombre(rs.getString(2));
                dm.setCantidad1(rs.getInt(3));
                list.add(dm);
            }
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    @Override
    public List<ReporteTramitesRp> getTramitesPendientes(String desde, String hasta) {
        List<ReporteTramitesRp> list = new ArrayList<>();
        ReporteTramitesRp rt;
        String query = "select htv.num_tramite, rt.name_, rt.assignee_, htv.fecha_ingreso from act_ru_task rt "
                + "left join historico_tramites_view htv on (htv.id_proceso = rt.proc_inst_id_) "
                + "where rt.task_def_key_ <> 'entregaDocs' "
                + "and htv.fecha_entrega between to_date('" + desde + "', 'dd/MM/yyyy') and "
                + "to_date('" + hasta + "', 'dd/MM/yyyy') order by 1";
        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new ReporteTramitesRp();
                rt.setNumtramite(rs.getLong(1));
                rt.setNombre(rs.getString(2));
                rt.setAssigne(rs.getString(3));
                rt.setFechainicio(rs.getTimestamp(4));
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<TareasPorFechaIngresoEntrega> getTareasPorFechaIngresoEntrega(String desde, String hasta, Integer tipofecha) {
        List<TareasPorFechaIngresoEntrega> list = new ArrayList<>();
        TareasPorFechaIngresoEntrega rt;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();

        long nulo = 0;
        long diffDays;
        String query2;

        if (tipofecha == 1) {
            query2 = "      h.fecha_ingreso BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                    + "                          AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                    + "  AND li.estado_liquidacion = 2 "
                    + "ORDER BY 11,7";
        } else {
            query2 = "      h.fecha_entrega BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                    + "                          AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                    + "  AND li.estado_liquidacion = 2 "
                    + "ORDER BY 11,7";
        }

        String query = "SELECT li.codigo_comprobante, h.fecha_ingreso, h.num_tramite, li.user_ingreso, h.solicitante, "
                + "       ac.name_ , ac.start_time_, ac.end_time_, ac.assignee_,(case when h.entregado = true then 'SI' else 'NO' end) as retirado, "
                + "       h.fecha_entrega, h.id, to_char(h.fecha_entrega ,'dd/mm/yyyy')  "
                + "  FROM act_hi_taskinst ac "
                + " INNER JOIN (SELECT ac.execution_id_,MAX(cast(ac.id_ as INTEGER)) as idt FROM act_hi_taskinst ac GROUP BY 1 ) ab on ac.id_= CAST(ab.idt as VARCHAR) "
                + " INNER JOIN historico_tramites_view h on h.id_proceso=ac.proc_inst_id_"
                + " INNER JOIN liquidacion_view li on (li.tramite = h.id) "
                + " WHERE " + query2;
        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TareasPorFechaIngresoEntrega();
                rt.setGroupfechaentrega(rs.getString(13));
                rt.setFactura(rs.getString(1));
                rt.setFechaingreso(rs.getTimestamp(2));
                rt.setTramite(rs.getLong(3));

                CatEnte e = manager.find(CatEnte.class, Long.valueOf(rs.getInt(5)));

                rt.setCed_fact(e.getCiRuc());
                rt.setTarea_act(rs.getString(6));
                rt.setFechainicio(rs.getTimestamp(7));
                rt.setFechafin(rs.getTimestamp(8));
                rt.setFechaentrega(rs.getTimestamp(11));
                rt.setFuncionario(rs.getString(9));

                Observaciones obs = (Observaciones) manager.find(Querys.getObservacionesByIdTramite, new String[]{"idTramite"}, new Object[]{rs.getLong(12)});
                if (obs != null) {
                    rt.setObservacion(obs.getObservacion());
                }

                rt.setEntregado(rs.getString(10));

                cal1.setTime(rs.getTimestamp(7));     //Fecha de inicio  (rs.getTimestamp(7))
                cal2.setTime(rs.getTimestamp(11));    //Fecha de entrega (rs.getTimestamp(11))
                cal3.setTime(new java.util.Date());   //fecha actual
                cal4.setTime(rs.getTimestamp(2));
                //Fecha de fin  (rs.getTimestamp(8))
                long fechaIni = cal1.getTimeInMillis();//fecha inicio
                long fechaEnt = cal2.getTimeInMillis();//fecha entrega
                long fechaAct = cal3.getTimeInMillis();//fecha actual

                rt.setDiaspendiente(nulo);

                //Se evalua por tarea Entrega de Documentos
                if (rs.getString(6).equals("Entrega Documentos")// && rs.getTimestamp(7)!= null
                        ) {
                    if (fechaIni <= fechaEnt) { //fecha inicio se realiza antes de fecha de entrega
                        rt.setIndicador("Elaborado");
                        rt.setIndicadorled(1); //Verde
                    } else { //fecha inicio sobrepasa el limite de fecha de entrega, pero ya esta en flujo de entrega de documentos
                        rt.setIndicador("Vencido-Elaborado");
                        rt.setIndicadorled(4); //Naranja
                    }
                } else {
                    //Se evalua por las otras tareas anteriores a Entrega de Documentos que no tengan fecha fin
                    if (!rs.getString(6).isEmpty() && rs.getTimestamp(8) == null) {

                        if ((fechaIni <= fechaEnt) && (fechaEnt > fechaAct)) { //fecha inicio se realiza antes de fecha de entrega
                            //restarFechas(fechaMenor,fechaMayor) - fecha actual, fecha entrega
                            diffDays = Utils.restarFechas(new java.util.Date(), rs.getTimestamp(11));

                            if (diffDays >= 1) {
                                rt.setIndicador("Tiempo de ejecucion");
                                rt.setIndicadorled(2); //Celeste
                            } else {
                                if (diffDays == 0) {
                                    rt.setIndicador("Tiempo de ejecucion-Por vencer");
                                    rt.setIndicadorled(3); //Azul
                                }
                            }
                            rt.setDiaspendiente(diffDays);
                        } else {
                            diffDays = Utils.restarFechas(new java.util.Date(), rs.getTimestamp(11));
                            rt.setDiaspendiente(diffDays);
                            rt.setIndicador("Vencido-Pendiente");
                            rt.setIndicadorled(5); //Rojo
                        }
                    } else {
                        rt.setIndicador("Interrumpido");
                        rt.setIndicadorled(6); //Cafe
                    }
                }
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<TramitesParaRegistrador> getTramitesParaRegistrador(String desde, String hasta) {
        List<TramitesParaRegistrador> list = new ArrayList<>();
        TramitesParaRegistrador rt;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();

        String query = "SELECT h.num_tramite, h.fecha_ingreso, h.fecha_entrega, ac.start_time_, ac.end_time_, "
                + "       dl.nombre, li.repertorio, dl.inscripcion, acth.assignee_, h.id "
                + "  FROM act_hi_taskinst ac "
                + "  LEFT JOIN historico_tramites_view h ON h.id_proceso=ac.proc_inst_id_"
                + "  LEFT JOIN liquidacion_view li ON (li.tramite = h.id) "
                + "  LEFT JOIN detalle_liquidacion_view dl ON (dl.liquidacion = li.id) "
                + "  LEFT JOIN (SELECT act.assignee_, act.proc_inst_id_ "
                + "               FROM act_hi_taskinst act "
                + "              WHERE act.task_def_key_ = 'inscribirCertificar') AS acth ON (acth.proc_inst_id_ = ac.proc_inst_id_) "
                + " WHERE ac.task_def_key_ = 'revisionFirma'"
                + "   AND ac.start_time_ BETWEEN to_date('" + desde + "', 'dd/MM/yyyy') AND to_date('" + hasta + "', 'dd/MM/yyyy') "
                + " ORDER BY 2 ";
        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TramitesParaRegistrador();

                rt.setTramite(rs.getLong(1));
                rt.setFechaingreso(rs.getTimestamp(2));
                rt.setFechaentrega(rs.getTimestamp(3));
                rt.setFechainicio(rs.getTimestamp(4));
                rt.setFechafin(rs.getTimestamp(5));
                rt.setNombreActo(rs.getString(6));
                rt.setRepertorio(rs.getLong(7));
                rt.setInscripcion(rs.getLong(8));
                rt.setAsignado(rs.getString(9));

                Observaciones obs = (Observaciones) manager.find(Querys.getObservacionesByIdTramite, new String[]{"idTramite"}, new Object[]{rs.getLong(10)});
                if (obs != null) {
                    rt.setObservacion(obs.getObservacion());
                }

                cal1.setTime(rs.getTimestamp(3)); //fecha entrega
                cal2.setTime(rs.getTimestamp(4)); //fecha inicio

                long fechaEnt = cal1.getTimeInMillis();
                long fechaIni = cal2.getTimeInMillis();

                if (rs.getTimestamp(5) != null) { //fecha fin no es nula
                    cal3.setTime(rs.getTimestamp(5));
                    long fechaFin = cal3.getTimeInMillis();

                    if (fechaIni <= fechaEnt && fechaFin <= fechaEnt) { //fecha inicio se realiza antes de fecha de entrega
                        rt.setEstado("TAREA FINALIZADA ANTES DE LA FECHA DE ENTREGA");
                        rt.setLed(1); //Verde
                    } else { //fecha inicio sobrepasa el limite de fecha de entrega, pero ya esta en flujo de entrega de documentos
                        if (fechaIni > fechaEnt) { //fecha inicio se realiza antes de fecha de entrega
                            rt.setEstado("TAREA FINALIZADA DESPUES DE FECHA DE ENTREGA POR RETRASO EN BANDEJA DE ENTRADA");
                            rt.setLed(2); //Naranja Oscuro
                        }
                        if (fechaIni <= fechaEnt && fechaFin > fechaEnt) {
                            rt.setEstado("TAREA FINALIZADA DESPUES DE LA FECHA DE ENTREGA");
                            rt.setLed(3); //Rojo Claro
                        }
                    }
                } else {  //fecha fin es nula
                    cal4.setTime(new java.util.Date()); //fecha actual
                    long fechaAct = cal4.getTimeInMillis();

                    if (fechaIni <= fechaEnt && fechaEnt > fechaAct) {
                        rt.setEstado("TAREA EN PROCESO DE EJECUCION");
                        rt.setLed(4); //Celeste
                    } else {
                        if (fechaIni > fechaEnt && fechaEnt < fechaAct) {
                            rt.setEstado("TAREA NO FINALIZADA POR RETRASO EN BANDEJA DE ENTRADA");
                            rt.setLed(5); //Naranja Claro
                        }
                        if (fechaIni <= fechaEnt && fechaEnt < fechaAct) {
                            rt.setEstado("TAREA NO FINALIZADA - VENCIDA");
                            rt.setLed(6); //Rojo Oscuro
                        }
                    }
                }
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<CantidadTramitesPorUsuario> getCantidadTramitesPorUsuario(Calendar desde, AclUser user) {
        String query, fecha;
        Long cantElaborados;
        Long cantVencidoElab;
        CantidadTramitesPorUsuario ctu;
        Calendar hasta = Calendar.getInstance();
        List<CantidadTramitesPorUsuario> modelo = new ArrayList<>();

        try {
            hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));

            while (desde.before(hasta)) {
                fecha = sdf.format(desde.getTime());
                //INICIALIZACION DE MODELO DE DATOS
                ctu = new CantidadTramitesPorUsuario();
                ctu.setFecha(desde.getTime());

                //CONSULTA TRAMITES ENTREGADOS(ELABORADOS)
                query = "SELECT count(*) FROM act_hi_taskinst t "
                        + "  LEFT JOIN historico_tramites_view ht ON ht.id_proceso = t.proc_inst_id_ "
                        + " WHERE t.task_def_key_ = 'inscribirCertificar' "
                        + "   AND to_char(t.end_time_, 'dd/MM/yyyy') = '" + fecha + "'"
                        + "   AND t.end_time_ < ht.fecha_entrega"
                        + "   AND t.assignee_ = '" + user.getUsuario() + "'";

                cantElaborados = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();
                ctu.setElaborados(cantElaborados.intValue());

                //CONSULTA TRAMITES VENCIDOS(VENCIDOS ELABORADOS)
                query = "SELECT count(*) FROM act_hi_taskinst t "
                        + "  LEFT JOIN historico_tramites_view ht ON ht.id_proceso = t.proc_inst_id_ "
                        + " WHERE t.task_def_key_ = 'inscribirCertificar' "
                        + "   AND to_char(t.end_time_, 'dd/MM/yyyy') = '" + fecha + "'"
                        + "   AND t.end_time_ > ht.fecha_entrega"
                        + "   AND t.assignee_ = '" + user.getUsuario() + "'";

                cantVencidoElab = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();
                ctu.setVencidoElab(cantVencidoElab.intValue());

                ctu.setCantidad(ctu.getVencidoElab() + ctu.getElaborados());
                ctu.setFuncionario(user.getEnte().getNombreCompleto());
                //INGRESO DEL MODELO DE DATOS EN LA LISTA
                modelo.add(ctu);

                //SUMA DE UN DIA A LA FECHA
                desde.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
        }
        return modelo;
    }

    @Override
    public List<TramitesPorFechaIngresoAgrupadoRol> getTramitesPorFechaIngresoAgrupadoRol(String desde, String hasta) {
        List<TramitesPorFechaIngresoAgrupadoRol> list = new ArrayList<>();
        List<AclUser> lcertificador = new ArrayList<>();
        List<AclUser> linscriptor = new ArrayList<>();
        TramitesPorFechaIngresoAgrupadoRol rt;
        String query;
        Connection con;
        try {
            con = ds.getDataSource().getConnection();
            BigInteger actos;

            lcertificador = this.getUsuariosByRolName("certificador");
            for (AclUser u : lcertificador) {
                if (u.getUsuario() != null) {
                    query = "SELECT li.num_tramite_rp, li.fecha_ingreso, ht.fecha_entrega, us.usuario, li.id "
                            + "  FROM flow.regp_liquidacion li "
                            + "  LEFT JOIN flow.historico_tramites ht ON (ht.id = li.tramite) "
                            + "  LEFT JOIN app.acl_user us on (us.id = li.inscriptor) "
                            + " WHERE li.fecha_ingreso between to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                            + "                            AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                            + "   AND li.inscriptor = " + u.getId() + " ORDER BY 4,5,2";
                    if (con != null) {
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            rt = new TramitesPorFechaIngresoAgrupadoRol();

                            if (u.getCode() != null) {
                                rt.setFuncionario(u.getEnte().getNombreCompleto());
                                rt.setCodigouser(u.getCode());
                                rt.setNumtramite(rs.getLong(1));

                                actos = (BigInteger) manager.getNativeQuery(Querys.getCantActosByLiquidacion, new Object[]{rs.getLong(5)});

                                rt.setCant_actos(actos.longValue());
                                rt.setFechaingreso(rs.getTimestamp(2));
                                rt.setFechaentrega(rs.getTimestamp(3));
                                rt.setAssigne(rs.getString(4));
                                rt.setRol("CERTIFICADOR");
                                list.add(rt);
                            }
                        }
                    }
                }
            }

            linscriptor = this.getUsuariosByRolName("inscriptor");
            for (AclUser u : linscriptor) {
                if (u.getUsuario() != null) {
                    query = "SELECT li.num_tramite_rp, li.fecha_ingreso, ht.fecha_entrega, us.usuario, li.id "
                            + "  FROM flow.regp_liquidacion li "
                            + "  LEFT JOIN flow.historico_tramites ht ON (ht.id = li.tramite) "
                            + "  LEFT JOIN app.acl_user us on (us.id = li.inscriptor) "
                            + " WHERE li.fecha_ingreso BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                            + "                            AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                            + "   AND li.inscriptor = " + u.getId() + " ORDER BY 4,2";
                    if (con != null) {
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            rt = new TramitesPorFechaIngresoAgrupadoRol();

                            if (u.getCode() != null) {
                                rt.setFuncionario(u.getEnte().getNombreCompleto());
                                rt.setCodigouser(u.getCode());
                                rt.setNumtramite(rs.getLong(1));

                                actos = (BigInteger) manager.getNativeQuery(Querys.getCantActosByLiquidacion, new Object[]{rs.getLong(5)});

                                rt.setCant_actos(actos.longValue());
                                rt.setFechaingreso(rs.getTimestamp(2));
                                rt.setFechaentrega(rs.getTimestamp(3));
                                rt.setAssigne(rs.getString(4));
                                rt.setRol("INSCRIPTOR");
                                list.add(rt);
                            }
                        }
                    }
                }
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public List<TareasSinRealizar> getTareasSinRealizar() {
        List<TareasSinRealizar> list = new ArrayList<>();
        TareasSinRealizar rt;
        String query = "SELECT htv.num_tramite, rt.name_, htv.fecha_ingreso, htv.fecha_entrega, rt.assignee_, rt.create_time_  "
                + "  FROM act_ru_task rt  "
                + " INNER JOIN historico_tramites_view htv ON (htv.id_proceso = rt.proc_inst_id_) "
                + " WHERE (rt.task_def_key_ = 'analisisProcesoRegistral') OR (rt.task_def_key_ = 'inscribirCertificar') "
                + " ORDER BY 5,2,3";
        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TareasSinRealizar();

                rt.setFuncionario(this.getNombreByUserName(rs.getString(5)));

                rt.setNumtramite(rs.getLong(1));
                rt.setNombreTarea(rs.getString(2));
                rt.setFechaingreso(rs.getTimestamp(3));
                rt.setFechaentrega(rs.getTimestamp(4));
                rt.setAssigne(rs.getString(5));
                rt.setFechainicio(rs.getTimestamp(6));
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<TareasSinRealizar> getTodasTareasSinRealizar() {
        List<TareasSinRealizar> list = new ArrayList<>();
        TareasSinRealizar rt;
        String a;
        int intIndex;

        String query = "SELECT htv.num_tramite, rt.name_, htv.fecha_ingreso, htv.fecha_entrega, rt.assignee_, rt.create_time_  "
                + "  FROM act_ru_task rt  "
                + " INNER JOIN historico_tramites_view htv ON (htv.id_proceso = rt.proc_inst_id_) "
                + " ORDER BY 5,2,3";
        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TareasSinRealizar();

                a = rs.getString(5);

                if (a != null) {
                    intIndex = a.indexOf(",");

                    if (intIndex == - 1) {
                        rt.setFuncionario(this.getNombreByUserName(rs.getString(5)));
                    } else {
                        rt.setFuncionario("VARIOS");
                    }
                }

                rt.setNumtramite(rs.getLong(1));
                rt.setNombreTarea(rs.getString(2));
                rt.setFechaingreso(rs.getTimestamp(3));
                rt.setFechaentrega(rs.getTimestamp(4));
                rt.setAssigne(rs.getString(5));
                rt.setFechainicio(rs.getTimestamp(6));
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<SolicitanteInterviniente> getSolicitanteInterviniente(String desde, String hasta, CatEnte sol_int) {
        List<SolicitanteInterviniente> list = new ArrayList<>();

        SolicitanteInterviniente rt;
        String query;
        Connection con;

        try {
            con = ds.getDataSource().getConnection();

            if (sol_int.getId() != null) {

                query = "SELECT l.num_tramite_rp,l.codigo_comprobante,l.fecha_ingreso,it.valor,l.inf_adicional_prof,l.total_pagar, "
                        + "       CASE sol.es_persona when TRUE THEN "
                        + "            coalesce(sol.apellidos,'')||' '||coalesce(sol.nombres,'') "
                        + "       ELSE sol.razon_social end AS tramitador "
                        + "  FROM flow.regp_intervinientes i "
                        + "  LEFT JOIN flow.regp_liquidacion l on (l.id = i.liquidacion) "
                        + "  LEFT JOIN app.ctlg_item it on(it.id = l.estudio_juridico) "
                        + "  LEFT JOIN app.cat_ente sol on (sol.id = l.tramitador) "
                        + " WHERE l.estado_liquidacion = 2 "
                        + "   AND i.ente = " + sol_int.getId()
                        + "   AND l.fecha_ingreso BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                        + "                           AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                        + " ORDER BY 3,1";
                System.out.println(query);

                if (con != null) {
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        rt = new SolicitanteInterviniente();

                        rt.setTramite(rs.getLong(1));
                        rt.setFactura(rs.getString(2));
                        rt.setFechaingreso(rs.getTimestamp(3));
                        rt.setEstudioJuridico(rs.getString(4));
                        rt.setInfAdicional(rs.getString(5));
                        rt.setValor_fact(rs.getDouble(6));
                        rt.setTramitador(rs.getString(7));
                        rt.setSolicitanteInterviniente(sol_int.getNombreCompleto());
                        list.add(rt);
                    }
                }
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    /*JC INI*/
    @Override
    public List<TareaEntregaDocumento> getTareaEntregaDocumento(String desde, String hasta) {
        List<TareaEntregaDocumento> list = new ArrayList<>();
        TareaEntregaDocumento te;

        String query = "SELECT htv.num_tramite, htv.fecha_ingreso, htv.fecha_entrega, ht.assignee_, t.assignee_, t.start_time_ , t.end_time_ "
                + "  FROM act_hi_taskinst t "
                + " INNER JOIN historico_tramites_view htv ON (htv.id_proceso = t.proc_inst_id_) "
                + " INNER JOIN (SELECT a.proc_inst_id_, a.assignee_ FROM act_hi_taskinst a WHERE a.task_def_key_ = 'inscribirCertificar') ht ON (ht.proc_inst_id_ = t.proc_inst_id_) "
                + " WHERE t.task_def_key_ = 'entregaDocs' "
                + "   AND t.end_time_ BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + "                       AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + " ORDER BY 7,1,5";

        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                te = new TareaEntregaDocumento();

                te.setTramite(rs.getLong(1));
                te.setFechaingreso(rs.getTimestamp(2));
                te.setFechaEntrega(rs.getTimestamp(3));
                te.setAsignado(rs.getString(4));  //Inscriptor/Certificador
                te.setDespachado(rs.getString(5));//Despachado por
                te.setFechaInicioTarea(rs.getTimestamp(6));
                te.setFechaFinTarea(rs.getTimestamp(7));

                list.add(te);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<TramitesVencidosElaborados> getTramitesVencidosElaborados(String desde, String hasta) {
        List<TramitesVencidosElaborados> list = new ArrayList<>();
        TramitesVencidosElaborados te;

        String query = "SELECT htv.num_tramite, htv.fecha_ingreso, htv.fecha_entrega, ht.assignee_, ht2.name_, t.start_time_ , t.end_time_ ,ht2.id_ "
                + "  FROM act_hi_taskinst t "
                + " INNER JOIN historico_tramites_view htv ON (htv.id_proceso = t.proc_inst_id_) "
                + " INNER JOIN (SELECT a.proc_inst_id_, a.assignee_ FROM act_hi_taskinst a WHERE a.task_def_key_ = 'inscribirCertificar') ht ON (ht.proc_inst_id_ = t.proc_inst_id_) "
                + " INNER JOIN (SELECT b.proc_inst_id_, b.name_,b.start_time_,b.end_time_,b.id_ FROM act_hi_taskinst b WHERE b.task_def_key_ <> 'entregaDocs' )  ht2 ON (ht2.proc_inst_id_ = t.proc_inst_id_) "
                + " WHERE t.task_def_key_ = 'entregaDocs' "
                + "   AND htv.fecha_entrega BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + "                             AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + "   AND ht2.start_time_ > htv.fecha_entrega "
                + "   AND t.end_time_ is not null "
                + " ORDER BY 6,8,1,2";
        //System.out.println("hquery:"+query);
        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                te = new TramitesVencidosElaborados();

                te.setTramite(rs.getLong(1));
                te.setFechaingreso(rs.getTimestamp(2));
                te.setFechaEntrega(rs.getTimestamp(3));
                te.setAsignado(rs.getString(4));
                te.setTareaActual(rs.getString(5));
                te.setFechaInicioTarea(rs.getTimestamp(6));
                te.setFechaFinTarea(rs.getTimestamp(7));

                list.add(te);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /*JC FIN*/
    @Override
    public List<TramitesVencidosPendientes> getTramitesVencidosPendientes(String desde, String hasta) {
        List<TramitesVencidosPendientes> list = new ArrayList<>();
        TramitesVencidosPendientes rt;

        String query = "SELECT htv.num_tramite, htv.fecha_ingreso, htv.fecha_entrega, ht.assignee_, rt.name_, rt.create_time_ "
                + "  FROM act_ru_task rt  "
                + " INNER JOIN historico_tramites_view htv ON (htv.id_proceso = rt.proc_inst_id_) "
                + " INNER JOIN (SELECT a.proc_inst_id_, a.assignee_ FROM act_hi_taskinst a WHERE a.task_def_key_ = 'inscribirCertificar') ht ON (ht.proc_inst_id_ = rt.proc_inst_id_) "
                + " WHERE rt.task_def_key_ <> 'entregaDocs' "
                + "   AND htv.fecha_entrega BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + "                             AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + " ORDER BY 5,2,3";

        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TramitesVencidosPendientes();

                rt.setTramite(rs.getLong(1));
                rt.setFechaingreso(rs.getTimestamp(2));
                rt.setFechaEntrega(rs.getTimestamp(3));
                rt.setAsignado(rs.getString(4));
                rt.setTareaActual(rs.getString(5));
                rt.setFechaInicioTarea(rs.getTimestamp(6));
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<TramitesCorregidos> getTramitesCorregidos(String desde, String hasta) {
        List<TramitesCorregidos> list = new ArrayList<>();
        TramitesCorregidos rt;

        String query = "SELECT htv.num_tramite, htv.fecha_ingreso, htv.fecha_entrega, rt.assignee_, rt.create_time_ "
                + "  FROM act_ru_task rt  "
                + " INNER JOIN historico_tramites_view htv ON (htv.id_proceso = rt.proc_inst_id_) "
                + " WHERE rt.task_def_key_ <> 'entregaDocs' "
                + "   AND htv.fecha_entrega BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + "                             AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + " ORDER BY 5,2,3";

        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TramitesCorregidos();

                rt.setTramite(rs.getLong(1));
                rt.setFechaIngreso(rs.getTimestamp(2));
                rt.setFechaEntrega(rs.getTimestamp(3));
                rt.setAsignado(rs.getString(4));
                rt.setFechaElaboracion(rs.getTimestamp(5));
                rt.setFechaReIngreso(rs.getTimestamp(5));
                rt.setObservacion(rs.getString(4));
                rt.setFechaIngreso2(rs.getTimestamp(5));
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<TramitesReasignados> getTramitesReasignados(String desde, String hasta) {
        List<TramitesReasignados> list = new ArrayList<>();
        List<HistoricTaskInstance> htis;
        TramitesReasignados rt;
        String query, queryAct;

        query = "SELECT ht.num_tramite, ht.fecha_ingreso, ht.fecha_entrega, ob.observacion, ob.fec_cre, ob.user_cre "
                + "  FROM flow.historico_tramites ht "
                + " INNER JOIN flow.observaciones ob ON (ob.id_tramite = ht.id) "
                + " WHERE ob.tarea='REASIGNACION DE USUARIO' "
                + "   AND ht.fecha_ingreso BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + "                            AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                + " ORDER BY 1,5";

        Connection con;
        try {
            con = ds.getDataSource().getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rt = new TramitesReasignados();

                String observacion = rs.getString(4);
                String tarea = null;
                String asignado = null;
                String reasignado = null;

                int intIndex1 = observacion.indexOf(", USUARIO ANTERIOR: ");
                int intIndex2 = observacion.indexOf(", USUARIO ACTUAL: ");

                asignado = observacion.substring(intIndex1 + 19, intIndex2);
                reasignado = observacion.substring(intIndex2 + 17, observacion.length());

                rt.setTramite(rs.getLong(1));
                rt.setFechaIngreso(rs.getTimestamp(2));
                rt.setFechaEntrega(rs.getTimestamp(3));
                rt.setAsignado(asignado);
                rt.setReAsignado(reasignado);
                rt.setFechaReAsignacion(rs.getTimestamp(5));
                rt.setUserCreador(rs.getString(6));

                queryAct = "SELECT hi.name_, ht.num_tramite, hi.start_time_, hi.end_time_ "
                        + "  FROM act_hi_taskinst hi "
                        + "  LEFT JOIN historico_tramites_view ht ON ht.id_proceso = hi.proc_inst_id_ "
                        + " WHERE ht.num_tramite= " + rs.getLong(1)
                        + " ORDER BY 2,3 DESC LIMIT 1";

                htis = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(queryAct).list();

                for (HistoricTaskInstance hi : htis) {
                    rt.setTarea(hi.getName());
                    rt.setFechaFinTarea(hi.getEndTime());
                }

                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<MovimientosModificadosIngresados> getMovimientosModificadosIngresados(String desde, String hasta) {
        List<MovimientosModificadosIngresados> list = new ArrayList<>();

        MovimientosModificadosIngresados rt;
        String query;
        Connection con;

        try {
            con = ds.getDataSource().getConnection();

            query = "SELECT RG.NUM_INSCRIPCION AS NUM_INSCRIPCION, RG.NUM_REPERTORIO AS NUM_REPERTORIO, "
                    + "       RG.FECHA_INGRESO AS FECHA_INGRESO, RG.FECHA_MOD AS FECHA_MODIFICACION, AC.NOMBRE AS NOMBRE_ACTO, US.USUARIO"
                    + "  FROM APP.REG_MOVIMIENTO RG "
                    + "  LEFT JOIN APP.REG_ACTO  AC ON (AC.ID = RG.ACTO) "
                    + "  LEFT JOIN APP.ACL_USER  US ON (US.ID = RG.USER_CREADOR) "
                    + "  LEFT JOIN APP.CAT_ENTE  EN ON (EN.ID = US.ENTE) "
                    + " WHERE RG.FECHA_INGRESO BETWEEN to_timestamp('" + desde + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                    + "                            AND to_timestamp('" + hasta + "', 'DD/MM/YYYY HH24:MI')::timestamp without time zone "
                    + " ORDER BY 6";

            if (con != null) {
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    rt = new MovimientosModificadosIngresados();

                    rt.setNum_inscripcion(rs.getInt(1));
                    rt.setNum_repertorio(rs.getInt(2));
                    rt.setFecha_ingreso(rs.getTimestamp(3));
                    rt.setFecha_modificacion(rs.getTimestamp(4));
                    rt.setNombre_acto(rs.getString(5));
                    rt.setUsuario_creacion(rs.getString(6));

                    list.add(rt);
                }
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public List<EvaluacionFuncionarios> getEvaluacionFuncionarios(Calendar desde, AclUser user) {
        List<EvaluacionFuncionarios> list = new ArrayList<>();

        String query, fecha;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();

        BigInteger cantActos, canTramites, cantActosNeg, cantActosPos;

        EvaluacionFuncionarios eval;
        Calendar hasta = Calendar.getInstance();

        Connection conRpp, conAct;

        try {
            hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));

            conRpp = ds.getDataSource().getConnection();
            conAct = ConexionActiviti.getConnection();
            PreparedStatement psRpp, psAct;
            ResultSet rsRpp, rsAct;

            while (desde.before(hasta)) {
                eval = new EvaluacionFuncionarios();

                Integer actosContratosPos = 0, actosContratosNeg = 0;
                Integer tramitesReasigPos = 0, tramitesReasigNeg = 0;

                Integer cantElaboradosI = 0, cantElaboradosC = 0;
                Integer cantVencidoElabI = 0, cantVencidoElabC = 0;
                Integer cantPendientesI = 0, cantPendientesC = 0;
                Integer cantTiempoEjecI = 0, cantTiempoEjecC = 0;
                Integer cantInterrupI = 0, cantInterrupC = 0;

                eval.setFecha(desde.getTime());

                fecha = sdf.format(desde.getTime());

                //CONSULTA TRAMITES
                query = "SELECT count(li.id) FROM flow.regp_liquidacion li "
                        + " WHERE li.estado_liquidacion = 2 "
                        + "   AND to_char(li.fecha_ingreso, 'dd/MM/yyyy') = '" + fecha + "'"
                        + "   AND li.inscriptor = " + user.getId();

                canTramites = (BigInteger) manager.getNativeQuery(query);
                eval.setTramites(canTramites.longValue());

                //CONSULTA ACTOS
                query = "SELECT count(li.id) FROM flow.regp_liquidacion li "
                        + "  LEFT JOIN flow.regp_liquidacion_detalles de on de.liquidacion = li.id "
                        + "  LEFT JOIN app.reg_acto ac on ac.id = de.acto "
                        + " WHERE li.estado_liquidacion = 2 "
                        + "   AND to_char(li.fecha_ingreso, 'dd/MM/yyyy') = '" + fecha + "'"
                        + "   AND li.inscriptor = " + user.getId();

                cantActos = (BigInteger) manager.getNativeQuery(query);
                eval.setActosContratos(cantActos.longValue());

                //REASIGNACIONES POSITIVAS-NEGATIVAS
                query = "SELECT ob.id_tramite, ob.observacion, ob.fec_cre, li.id "
                        + "  FROM flow.observaciones ob "
                        + " INNER JOIN flow.historico_tramites h ON (h.id = ob.id_tramite) "
                        + " INNER JOIN flow.regp_liquidacion li ON (li.tramite = h.id) "
                        + " WHERE ob.tarea='REASIGNACION DE USUARIO' "
                        + "   AND ob.observacion like '%" + user.getUsuario() + "%'"
                        + "   AND to_char(li.fecha_ingreso, 'dd/MM/yyyy') = '" + fecha + "'"
                        + " ORDER BY 1";

                psRpp = conRpp.prepareStatement(query);
                rsRpp = psRpp.executeQuery();

                while (rsRpp.next()) {
                    String observacion = rsRpp.getString(2);
                    String asignado = null;
                    String reasignado = null;

                    int intIndex1 = observacion.indexOf(", USUARIO ANTERIOR: ");
                    int intIndex2 = observacion.indexOf(", USUARIO ACTUAL: ");

                    asignado = observacion.substring(intIndex1 + 19, intIndex2);
                    reasignado = observacion.substring(intIndex2 + 17, observacion.length());

                    query = "SELECT count(li.id) FROM flow.regp_liquidacion_detalles li "
                            + " WHERE li.liquidacion = " + rsRpp.getInt(4);

                    if (asignado.trim().equals(user.getUsuario())) {
                        tramitesReasigNeg++;

                        cantActosNeg = (BigInteger) manager.getNativeQuery(query);
                        actosContratosNeg = actosContratosNeg + cantActosNeg.intValue();
                    }

                    if (reasignado.trim().equals(user.getUsuario())) {
                        tramitesReasigPos++;

                        cantActosPos = (BigInteger) manager.getNativeQuery(query);
                        actosContratosPos = actosContratosPos + cantActosPos.intValue();
                    }
                }
                eval.setActosContratosPos(actosContratosPos);
                eval.setActosContratosNeg(actosContratosNeg);
                eval.setTramitesReasignadoPos(tramitesReasigPos);
                eval.setTramitesReasignadoNeg(tramitesReasigNeg);

                eval.setTotalTramites((canTramites.intValue() - tramitesReasigNeg) + tramitesReasigPos);
                eval.setTotalActos((cantActos.intValue() - actosContratosNeg) + actosContratosPos);

                //CONSULTA ESTADOS DE TRAMITES
                query = "SELECT distinct(h.num_tramite), h.fecha_ingreso, h.fecha_entrega, ac.start_time_, ac.end_time_, "
                        + "       ac.task_def_key_, de.solvencia  "
                        + "  FROM act_hi_taskinst ac "
                        + " INNER JOIN (SELECT ac.execution_id_,MAX(cast(ac.id_ as INTEGER)) as idt FROM act_hi_taskinst ac GROUP BY 1 ) ab on ac.id_= CAST(ab.idt as VARCHAR) "
                        + " INNER JOIN historico_tramites_view h on h.id_proceso=ac.proc_inst_id_"
                        + " INNER JOIN liquidacion_view li on (li.tramite = h.id) "
                        + " INNER JOIN detalle_liquidacion_view de on (de.liquidacion = li.id) "
                        + " WHERE to_char(li.fecha_ingreso, 'dd/MM/yyyy') = '" + fecha + "'"
                        + "   AND li.estado_liquidacion = 2 "
                        + "   AND li.inscriptor = '" + user.getId() + "'"
                        + " ORDER BY 2";

                psAct = conAct.prepareStatement(query);
                rsAct = psAct.executeQuery();

                while (rsAct.next()) {

                    cal1.setTime(rsAct.getTimestamp(4));  //Fecha de inicio
                    cal2.setTime(rsAct.getTimestamp(3));  //Fecha de entrega
                    cal3.setTime(new java.util.Date());   //fecha actual
                    cal4.setTime(rsAct.getTimestamp(2));   //fecha ingreso

                    long fechaIni = cal1.getTimeInMillis();//fecha inicio
                    long fechaEnt = cal2.getTimeInMillis();//fecha entrega
                    long fechaAct = cal3.getTimeInMillis();//fecha actual

                    //Se evalua por tarea Entrega de Documentos
                    if (rsAct.getString(6).equals("entregaDocs") && rsAct.getTimestamp(4) != null) {
                        if (fechaIni <= fechaEnt) { //fecha inicio se realiza antes de fecha de entrega
                            if (rsAct.getBoolean(7)) {
                                cantElaboradosI++;
                            } else {
                                cantElaboradosC++;
                            }
                        } else { //fecha inicio sobrepasa el limite de fecha de entrega, pero ya esta en flujo de entrega de documentos
                            if (rsAct.getBoolean(7)) {
                                cantVencidoElabI++;
                            } else {
                                cantVencidoElabC++;
                            }
                        }
                    } else {
                        //Se evalua por las otras tareas anteriores a Entrega de Documentos que no tengan fecha fin
                        if (!rsAct.getString(6).isEmpty() && rsAct.getTimestamp(5) == null) {
                            if ((fechaIni <= fechaEnt) && (fechaEnt > fechaAct)) { //fecha inicio se realiza antes de fecha de entrega
                                if (rsAct.getBoolean(7)) {
                                    cantTiempoEjecI++;
                                } else {
                                    cantTiempoEjecC++;
                                }
                            } else {
                                if (rsAct.getBoolean(7)) {
                                    cantPendientesI++;
                                } else {
                                    cantPendientesC++;
                                }
                            }
                        } else {
                            if (rsAct.getBoolean(7)) {
                                cantInterrupI++;
                            } else {
                                cantInterrupC++;
                            }
                        }
                    }
                }
                eval.setElaboradosI(cantElaboradosI);
                eval.setElaboradosC(cantElaboradosC);
                eval.setVencidosElabI(cantVencidoElabI);
                eval.setVencidosElabC(cantVencidoElabC);
                eval.setEjecucionI(cantTiempoEjecI);
                eval.setEjecucionC(cantTiempoEjecC);
                eval.setPendienteI(cantPendientesI);
                eval.setPendienteC(cantPendientesC);
                eval.setInterrumpidosI(cantInterrupI);
                eval.setInterrumpidosC(cantInterrupC);

                eval.setFuncionario(user.getEnte().getNombreCompleto());
                //INGRESO DEL MODELO DE DATOS EN LA LISTA
                list.add(eval);

                //SUMA DE UN DIA A LA FECHA
                desde.add(Calendar.DAY_OF_MONTH, 1);
            }
            conRpp.close();
            conAct.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public List<DatosTramitesFinalizados> getTramiteFinalizado(Long numTramite) {
        List<DatosTramitesFinalizados> list = new ArrayList<>();
        DatosTramitesFinalizados rt;
        Connection con;

        String query = "SELECT htv.num_tramite, htv.nombre_propietario, htv.fecha_ingreso, htv.fecha_entrega, ht.assignee_, ht.start_time_, ht.end_time_ "
                + "  FROM historico_tramites_view htv "
                + "  LEFT join act_hi_taskinst ht ON (ht.proc_inst_id_ = htv.id_proceso) "
                + " WHERE ht.task_def_key_ = 'entregaDocs' "
                + "   AND ht.delete_reason_ LIKE 'completed' "
                + "   AND end_time_ is not null "
                + "   AND htv.num_tramite = " + numTramite;
        //System.out.println(query);
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new DatosTramitesFinalizados();

                rt.setTramite(rs.getLong(1));
                rt.setCliente(rs.getString(2));
                rt.setFechaIngreso(rs.getTimestamp(3));
                rt.setFechaEntrega(rs.getTimestamp(4));
                rt.setUserResponsable(rs.getString(5));
                //rt.setFechaInicio(rs.getTimestamp(6));
                list.add(rt);
            }
            con.close();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public String getNombreByUserName(String user) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("usuario", user);
            AclUser u = manager.findObjectByParameter(AclUser.class, map);
            if (u.getEnte() != null) {
                return u.getEnte().getNombreCompleto();
            } else {
                return user;
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return user;
        }
    }

    @Override
    public List<AclUser> getUsuariosByRolName(String nombre) {
        List<AclUser> list;
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("estado", Boolean.TRUE);
        try {
            AclRol rol = manager.findObjectByParameter(AclRol.class, map);
            list = (List<AclUser>) rol.getAclUserCollection();
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public BigDecimal getValorNotasCredito(Date desde, Date hasta, Long caja) {
        Map<String, Object> map = new HashMap<>();
        map.put("desde", desde);
        map.put("hasta", hasta);
        BigDecimal temp;
        try {
            if (caja == 0L) {
                temp = (BigDecimal) manager.findObjectByParameter(Querys.getValorNotasCredito, map);
            } else {
                map.put("caja", caja);
                temp = (BigDecimal) manager.findObjectByParameter(Querys.getValorNotasCreditoByCaja, map);
            }
            //System.out.println("// " + temp);
            return temp;
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List<TareaWF> getTasksWorkflow(String usuario) {
        try {
            if (usuario != null) {
                return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja, new Object[]{usuario});
            } else {
                return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1, null);
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<TareaWF> getTasksWorkflow(String usuario, int case_) {
        try {
            switch (case_) {
                case 1:
                    if (usuario == null) {
                        return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja, new Object[]{usuario});
                    } else {
                        return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1, null);
                    }
                case 2:
                    if (usuario != null) {
                        return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1Group(usuario), new Object[]{usuario});
                    } else {
                        return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1Group(null), null);
                    }
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public List<TareaWF> getTasksWorkflow(int case_, Object[] values) {
        try {
            switch (case_) {
                case 1://SIN USUARIO
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2Group(0, 1), values);
                case 2://CON USUARIO
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2Group(1, 1), values);
                case 3://SIN USUARIO Y FECHA DE INGRESO
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2Group(2, 2), values);
                case 4://SIN USUARIO Y FECHA DE ENTREGA
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2Group(3, 3), values);
                case 5://CON USUARIO Y FECHA DE INGRESO
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2Group(4, 2), values);
                case 6://CON USUARIO Y FECHA DE ENTREGA
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2Group(5, 3), values);
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public List<TareaWF> getTasksWorkflow(String usuario, Date desde, Date hasta) {
        try {
            if (usuario != null) {
                if (desde != null && hasta != null) {
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandejaDesdeHasta, new Object[]{usuario, desde, hasta});
                } else {
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja2, new Object[]{usuario});
                }
            } else {
                if (desde != null && hasta != null) {
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1DesdeHasta, new Object[]{desde, hasta});
                } else {
                    return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1_1, null); 
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
    }

    public List<TareaWF> getTasksWorkflow(String usuario, int case_, Date desde6, Date hasta6) {
        try {
            switch (case_) {
                case 1:
                    if (usuario == null) {
                        if (desde6 != null && hasta6 != null) {
                            return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandejaDesdeHasta, new Object[]{usuario, desde6, hasta6});
                        } else {
                            return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja, new Object[]{usuario});
                        }
                    } else {
                        if (desde6 != null && hasta6 != null) {
                            return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1DesdeHasta, new Object[]{desde6, hasta6});
                        } else {
                            return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1, null);
                        }
                    }
                case 2:
                    if (usuario != null) {
                        return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1Group(usuario), new Object[]{usuario});
                    } else {
                        return manager.getSqlQueryValues(TareaWF.class, Querys.getTramitesBandeja1Group(null), null);
                    }
            }

        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    @Override
    public List<AnexoCotadDetalleMovimiento> getMovimientosAnexoCotad(String fecha_inscripcion) {
        try {
            if (fecha_inscripcion != null) {
                return manager.getSqlQueryValues(AnexoCotadDetalleMovimiento.class,Querys.getRegMovimientoAnexoCotad, new Object[]{fecha_inscripcion});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    @Override
    public List<AnexoCotadDetalleMovimientoPartes> getAnexoCotadIntervinientes(BigInteger movimiento_id) {
        try {
            if (movimiento_id != null) {
                return manager.getSqlQueryValues(AnexoCotadDetalleMovimientoPartes.class,Querys.getRegMovimientoAnexoCotadIntervinientes, new Object[]{movimiento_id});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    @Override
    public List<AnexoCotadDetalleMovimientoFichas> getAnexoCotadFichas(BigInteger movimiento_id) {
        try {
            if (movimiento_id != null) {
//                System.out.println("query:"+Querys.getRegMovimientoAnexoCotadFichas);
                return manager.getSqlQueryValues(AnexoCotadDetalleMovimientoFichas.class,Querys.getRegMovimientoAnexoCotadFichas, new Object[]{movimiento_id});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    @Override
    public List<AnexoSuperciaDetalleMovimiento> getMovimientosAnexoSupercia(String fecha_inscripcion) {
        try {
            if (fecha_inscripcion != null) {
                return manager.getSqlQueryValues(AnexoSuperciaDetalleMovimiento.class,Querys.getRegMovimientoAnexoSupercia, new Object[]{fecha_inscripcion});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    
    @Override
    public List<AnexoSuperciaDetalleMovimientoRepresentantes> getAnexoSuperciaRepresentantes(BigInteger movimiento_id) {
        try {
            if (movimiento_id != null) {
                return manager.getSqlQueryValues(AnexoSuperciaDetalleMovimientoRepresentantes.class,Querys.getRegMovimientoAnexoSuperciaRepresentantes, new Object[]{movimiento_id});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    
    @Override
    public List<AnexoSuperciaDetalleMovimientoCapitales> getAnexoSuperciaCapitales(BigInteger movimiento_id) {
        try {
            if (movimiento_id != null) {
                return manager.getSqlQueryValues(AnexoSuperciaDetalleMovimientoCapitales.class,Querys.getRegMovimientoAnexoSuperciaCapitales, new Object[]{movimiento_id});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    @Override
    public List<AnexoSuperciaDetalleMovimientoSocios> getAnexoSuperciaSocios(BigInteger movimiento_id) {
        try {
            if (movimiento_id != null) {
                return manager.getSqlQueryValues(AnexoSuperciaDetalleMovimientoSocios.class,Querys.getRegMovimientoAnexoSuperciaSocios, new Object[]{movimiento_id});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
    @Override
    public List <Remanentes> getRemanentesByFechaAndTipo(Long usuario, String desde, String hasta, Integer tipo){
        try {
            if (desde != null && hasta != null && tipo != null) {
                return manager.getSqlQueryValues(Remanentes.class,Querys.getRemanentes, new Object[]{usuario,usuario,desde,hasta,tipo});
            } 
        } catch (Exception e) {
            Logger.getLogger(ConsultasSql.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return null;
    }
    
}
