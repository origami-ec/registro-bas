/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.entities.RegMovimientoFile;
import com.origami.sgr.historico.model.Acto;
import com.origami.sgr.historico.model.Cliente;
import com.origami.sgr.historico.model.ClienteActo;
import com.origami.sgr.historico.model.OrionActo;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author dfcalderio
 */
@Named(value = "historicoView")
@ViewScoped
public class ConsultaHistorico implements Serializable {

    @EJB(beanName = "manager")
    private Entitymanager manager;

    @Inject
    private UserSession userSession;

    private Integer tipoBusqueda;
    private List<ClienteActo> traspasos;
    private List<ClienteActo> gravamenes;
    private List<ClienteActo> gravamenesRelacionados;
    private ClienteActo gravamenenSeleccionado;
    private List<ClienteActo> gravamenesByActo;
    private List<ClienteActo> advertenciasByActo;
    private ClienteActo clienteActo;
    private ClienteActo clienteActoSeleccionado;
    private ClienteActo gravamenesByActoSeleccionado;
    private List<Acto> anteriores;
    private List<Acto> anterioresSeleccionados;
    private Acto anterioreSeleccionado;
    private List<Acto> posteriores;
    private List<Acto> posterioresSeleccionados;
    private Acto posteriorSeleccionado;
    private List<Cliente> vendedores;
    private List<Cliente> compradores;
    private List<Cliente> vendedoresSeleccionados;
    private List<Cliente> compradoresSeleccionados;
    private List<Cliente> deudores;
    private Date desde;
    private Date hasta;
    private String cliente;
    private String inscripcion;
    private String repertorio;
    private String anio;
    private Connection conn;
    private SimpleDateFormat format;
    private String observaciones;
    private String observGravamen;

    private List<RegMovimientoFile> certificadosVisual;

    // INFO ORION
    @Inject
    private ArchivosService arch;

    private int tabSelect;

    private OrionActo actoSeleccionado;

    private List<OrionActo> compras;
    private List<OrionActo> ventas;

    private List<OrionActo> hipotecas;
    private List<OrionActo> prohibiciones;

    private List<OrionActo> embargos;
    private List<OrionActo> patrimonios;

    private List<OrionActo> certificados;

    private List<OrionActo> demandas;
    private List<OrionActo> interdicciones;

    private List<OrionActo> cauciones;
    private List<OrionActo> nombramientos;

    private List<OrionActo> poderes;
    private List<OrionActo> prendas;

    private String certificado;

    @PostConstruct
    public void init() {
        tipoBusqueda = 1;
        format = new SimpleDateFormat("yyyy-MM-dd");
        clearDataOrion();
    }

    @PreDestroy
    public void preDestroy() {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void buscar() {

        if (inscripcion != null) {
            if (inscripcion.length() > 0 && inscripcion.length() < 6) {
                inscripcion = StringUtils.leftPad(inscripcion, 6, "0");
            }
        }
        clearData();
        if (tipoBusqueda == 1) {
            buscarTraspasos(null);
            if (!traspasos.isEmpty()) {
                clienteActo = traspasos.get(0);
                buscarInfoTraspasos(clienteActo);
            }
        } else {
            buscarGravamenes();
            if (!gravamenes.isEmpty()) {
                gravamenenSeleccionado = gravamenes.get(0);
                deudoresAsociados(gravamenenSeleccionado);
                gravamenesRelacionados = new ArrayList<>();
                observGravamen = gravamenenSeleccionado.getObservaciones();
                if (gravamenenSeleccionado.getTipoGravamen().equals("R")) {
                    actoByGravamen();
                    gravamenesRelacionadosByGravamen();
                } else {
                    observaciones = "";
                    gravamenesRelacionados.add(gravamenenSeleccionado);
                }
            }
        }
    }

    private void clearData() {
        observGravamen = "";
        observaciones = "";
        gravamenes = new ArrayList<>();
        gravamenesByActo = new ArrayList<>();
        traspasos = new ArrayList<>();
        gravamenes = new ArrayList<>();
        gravamenesRelacionados = new ArrayList<>();
        anteriores = new ArrayList<>();
        posteriores = new ArrayList<>();
        compradores = new ArrayList<>();
        vendedores = new ArrayList<>();
        clienteActo = new ClienteActo();
        gravamenenSeleccionado = new ClienteActo();
        gravamenesByActoSeleccionado = new ClienteActo();
        advertenciasByActo = new ArrayList<>();
        certificadosVisual = new ArrayList<>();
    }

    public void buscarInfoTraspasos(ClienteActo c) {

        observaciones = observacionesByActo(c);
        Map<String, List<ClienteActo>> findGravamenesByActo = buscarGravamenesByActo(c);
        advertenciasByActo = findGravamenesByActo.get("advertencias");
        gravamenesByActo = findGravamenesByActo.get("gravamenes");

        Map<String, List<Acto>> antecedentes = buscarAntecedentes(c);
        anteriores = antecedentes.get("anteriores");
        posteriores = antecedentes.get("posteriores");
        Map<String, List<Cliente>> intervinientes = buscarIntervinientes(c);
        compradores = intervinientes.get("compradores");
        vendedores = intervinientes.get("vendedores");
        certificadosVisual = buscarCertificados(c);
        observGravamen = null;
    }

    public void verInfoMovimiento(ClienteActo ac) {

        if (tipoBusqueda == 1) {
            clienteActo = ac;
            buscarInfoTraspasos(ac);
            JsfUti.update("frmInfoTraspaso");
            JsfUti.executeJS("PF('dlgInfoTraspaso').show()");
        } else {
            gravamenenSeleccionado = ac;
            observGravamen = gravamenenSeleccionado.getObservaciones();
            if (gravamenenSeleccionado.getTipoGravamen().equals("R")) {
                actoByGravamen();
                gravamenesRelacionadosByGravamen();
            } else {
                observaciones = "";
                gravamenesRelacionados.add(gravamenenSeleccionado);
            }
        }
    }

    private String observacionesByActo(ClienteActo ac) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String result = "<b>INSCRIPCION:</b> " + (ac.getAnio() != null ? ac.getAnio() : "") + (ac.getInscripcion() != null ? ac.getInscripcion() : "") + " de " + (ac.getFechaInscripcion() != null ? df.format(ac.getFechaInscripcion()) : " ") + " </br>"
                    + "<b>TIPO:</b> " + (ac.getActo() != null ? ac.getActo() : " ") + "</br>"
                    + "<b>CUANTIA:</b> " + (ac.getCuantia() != null ? ac.getCuantia() : " ") + "</br>"
                    + "<b>CLAVE CATASTRAL:</b> " + (ac.getCatastro() != null ? ac.getCatastro() : "") + "</br>"
                    + "<b>LUGAR:</b> " + (ac.getParroquia() != null ? ac.getParroquia() : " ") + "</br>"
                    + "<b>UBICACION:</b> " + (ac.getUbicacionPropiedad() != null ? ac.getUbicacionPropiedad() : " ") + "</br>"
                    + "<b>NOTARIA:</b> " + (ac.getNotaria() != null ? ac.getNotaria() : " ") + "</br>"
                    + "<b>FECHA:</b> " + (ac.getFechaNotaria() != null ? df.format(ac.getFechaNotaria()) : " ") + "</br>"
                    + "<b>OBSERVACIONES:</b> " + (ac.getObservaciones() != null ? ac.getObservaciones() : " ");

            return result;
        } catch (Exception ex) {
            return "";
        }

    }

    public void buscarTraspasos(Acto ac) {

        traspasos = new ArrayList<>();
        if (ac == null) {
            if (desde != null && hasta != null) {
                if (desde.after(hasta)) {
                    JsfUti.messageWarning(null, "La fecha desde es mayor a la fecha hasta", "");
                    return;
                }
            }

            if (cliente.isEmpty() && anio.isEmpty() && inscripcion.isEmpty() && desde == null && hasta == null) {
                JsfUti.messageWarning(null, "Debe seleccionar al menos un parametro en la busqueda", "");
                return;
            }

            if (!anio.isEmpty()) {
                if (anio.length() < 4) {
                    JsfUti.messageWarning(null, "El año debe tener 4 digitos", "");
                    return;
                }
            }
        }
        try {
            Long pk = 0L;

            String query = crearQueryTraspasos(ac);

            conn = getConnection();

            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        pk++;
                        ClienteActo ca = new ClienteActo();
                        ca.setPk(pk);
                        ca.setId(rs.getString("id"));
                        ca.setCliente(rs.getString("comprador"));
                        ca.setVendedor(rs.getString("vendedor"));
                        ca.setActo(rs.getString("acto"));
                        ca.setInscripcion(rs.getString("num_inscripcion"));
                        ca.setRepertorio(rs.getString("repertorio"));
                        ca.setAnio(rs.getString("anio"));
                        ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                        ca.setCuantia(rs.getString("cuantia"));
                        ca.setCatastro(rs.getString("catastro"));
                        ca.setParroquia(rs.getString("parroquia"));
                        ca.setUbicacionPropiedad(rs.getString("ubicacion_propiedad"));
                        ca.setNotaria(rs.getString("notaria"));
                        ca.setFechaNotaria(rs.getDate("fecha_notaria"));
                        ca.setVendido(rs.getInt("vendido"));
                        ca.setGravado(rs.getInt("gravado"));
                        ca.setLinderos(rs.getString("linderos"));
                        ca.setObservaciones(rs.getString("observaciones"));

                        traspasos.add(ca);
                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //         }

        } catch (SQLException ex) {
            try {
                conn.close();
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public void onRowSelectClienteActoAnterior(SelectEvent event) {
        clearData();
        buscarTraspasos(anterioreSeleccionado);
        if (!traspasos.isEmpty()) {
            clienteActo = traspasos.get(0);
            buscarInfoTraspasos(clienteActo);
        }
        posteriorSeleccionado = null;
    }

    public void onRowSelectClienteActoPosterior(SelectEvent event) {
        clearData();
        buscarTraspasos(posteriorSeleccionado);
        if (!traspasos.isEmpty()) {
            clienteActo = traspasos.get(0);
            buscarInfoTraspasos(clienteActo);
        }
        anterioreSeleccionado = null;
    }

    public void buscarGravamenes() {

        gravamenes = new ArrayList<>();
        if (desde != null && hasta != null) {
            if (desde.after(hasta)) {
                JsfUti.messageWarning(null, "La fecha desde es mayor a la fecha hasta", "");
                return;
            }
        }

        if (cliente.isEmpty() && anio.isEmpty() && inscripcion.isEmpty() && desde == null && hasta == null) {
            JsfUti.messageWarning(null, "Debe seleccionar al menos un parametro en la busqueda", "");
            return;
        }

        if (!anio.isEmpty()) {
            if (anio.length() < 4) {
                JsfUti.messageWarning(null, "El año debe tener 4 digitos", "");
                return;
            }
        }
        conn = getConnection();
        try {

            String query = createQueryGravamenes();

            PreparedStatement ps = conn.prepareStatement(query);
            Long pk = 0L;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClienteActo ca = new ClienteActo();

                    ca.setId(UUID.randomUUID().toString());
                    ca.setIdgrp(rs.getString("idgrp"));
                    ca.setIdTras(rs.getString("idtras"));
                    ca.setCliente(rs.getString("cliente"));
                    ca.setActo(rs.getString("acto"));
                    ca.setInscripcion(rs.getString("num_inscripcion"));
                    ca.setRepertorio(rs.getString("repertorio"));
                    ca.setAnio(rs.getString("anio"));
                    ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                    ca.setCuantia(rs.getString("cuantia"));
                    ca.setCatastro(rs.getString("catastro"));
                    ca.setTipoCliente(rs.getString("tipo"));
                    ca.setParroquia(rs.getString("parroquia"));
                    ca.setUbicacionPropiedad(rs.getString("ubicacion_propiedad"));
                    ca.setNotaria(rs.getString("notaria"));
                    ca.setFechaNotaria(rs.getDate("fecha_notaria"));
                    ca.setVendido(rs.getInt("vendido"));
                    ca.setGravado(rs.getInt("gravado"));
                    ca.setLinderos(rs.getString("linderos"));
                    ca.setObservaciones(rs.getString("observaciones"));
                    ca.setEstadoGravamen(rs.getInt("estado"));
                    ca.setTipoGravamen(rs.getString("tipo_gravamen"));
                    ca.setInstitucion(rs.getString("institucion"));
                    ca.setNumero(rs.getString("numero"));
                    ca.setNumeroGravamen(rs.getString("numero_gravamen"));
                    gravamenes.add(ca);
                }
                conn.close();
            } catch (SQLException ex) {
                conn.close();
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            try {
                conn.close();
            } catch (SQLException ex1) {
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }

    }

    String crearQueryTraspasos(Acto ac) {
        String query;
        String whereCliente = null;
        String whereDate = null;
        String whereAnio = null;
        String whereInscripcion = null;
        String where = null;

        if (ac != null) {
            where = "t.void = 0 AND t.repertor <> '0' AND t.id = '" + ac.getId() + "'";
        } else {
            if (!cliente.isEmpty()) {
                whereCliente = " t.comprador ILIKE '" + cliente + "%' OR t.vendedor ILIKE '" + cliente + "%' OR c.nombre ILIKE '"
                        + cliente + "%' OR v.nombre ILIKE '" + cliente + "%'  AND t.void = 0 AND t.repertor <> '0'";
            }

            if (desde != null && hasta != null) {
                whereDate = "t.fecins >= '" + format.format(desde) + "' and t.fecins <= '" + format.format(hasta) + "'";
            }
            if (desde != null && hasta == null) {
                whereDate = "t.fecins >= '" + format.format(desde) + "'";
            }
            if (desde == null && hasta != null) {
                whereDate = "t.fecins <= '" + format.format(hasta) + "'";
            }

            if (!inscripcion.isEmpty()) {
                whereInscripcion = "t.numero ILIKE '%" + inscripcion + "%'";
            }
            if (!anio.isEmpty()) {
                whereAnio = "t.anio ILIKE '%" + anio + "%'";
            }

            if (whereCliente != null && whereAnio != null && whereInscripcion != null) {
                where = whereCliente + " AND " + whereAnio + " AND " + whereInscripcion;
            }
            if (whereCliente != null && whereAnio != null && whereInscripcion == null) {
                where = whereCliente + " AND " + whereAnio;
            }
            if (whereCliente != null && whereAnio == null && whereInscripcion != null) {
                where = whereCliente + " AND " + whereInscripcion;
            }
            if (whereCliente == null && whereAnio != null && whereInscripcion != null) {
                where = whereAnio + " AND " + whereInscripcion;
            }
            if (whereCliente != null && whereAnio == null && whereInscripcion == null) {
                where = whereCliente;
            }
            if (whereCliente == null && whereAnio == null && whereInscripcion != null) {
                where = whereInscripcion;
            }
            if (whereCliente == null && whereAnio != null && whereInscripcion == null) {
                where = whereAnio;
            }

            if (where != null && whereDate != null) {
                where += " AND " + whereDate;
            }
            if (where == null && whereDate != null) {
                where = whereDate;
            }
        }
        query = "SELECT t.id, t.idtras, CASE WHEN c IS NULL THEN t.comprador ELSE c.nombre END as comprador, t.repertor as repertorio,\n"
                + "		CASE WHEN v IS NULL THEN t.vendedor ELSE v.nombre END as vendedor, tt.nombre as acto, t.numero as num_inscripcion,\n"
                + "		t.anio, t.fecins as fecha_inscripcion, t.valor as cuantia, t.catastro, p.nombre as parroquia,t.observa as observaciones,\n"
                + "		t.ubiprop as ubicacion_propiedad, t.notaria, t.fecnot as fecha_notaria, t.vendido, t.gravado, t.linderos\n"
                + "         FROM historico.ttraspas t  \n"
                + "         LEFT OUTER JOIN historico.ttrasdes c ON t.id = c.idtras\n"
                + "         LEFT OUTER JOIN historico.ttrasori v ON t.id = v.idtras\n"
                + "         LEFT OUTER JOIN historico.ttiptras tt ON tt.id = t.idtiptras\n"
                + "         LEFT OUTER JOIN historico.tparroqu p ON p.id = t.idparr\n"
                + "         WHERE " + where + " ORDER BY t.fecins DESC ";

        return query;
    }

    public String createQueryGravamenes() {

        String whereDeudorTGP = null;
        String whereDeudorTGG = null;
        String whereDate = null;
        String whereAnio = null;
        String whereInscripcion = null;
        String whereDeudor1 = null;
        String whereDeudor2 = null;

        if (!cliente.isEmpty()) {
            whereDeudorTGP = "tgrp.void = 0 AND (tgp.deudor ILIKE '" + cliente + "%' OR tinst.nombre ILIKE '" + cliente + "%' ) \n";
            whereDeudorTGG = "tgrp.void = 0 AND (tggd.deudor ILIKE '" + cliente + "%' OR tinst.nombre ILIKE '" + cliente + "%' )";
        }

        if (desde != null && hasta != null) {
            whereDate = "tgrp.fecins >= '" + format.format(desde) + "' and tgrp.fecins <= '" + format.format(hasta) + "'";
        }
        if (desde != null && hasta == null) {
            whereDate = "tgrp.fecins >= '" + format.format(desde) + "'";
        }
        if (desde == null && hasta != null) {
            whereDate = "tgrp.fecins <= '" + format.format(hasta) + "'";
        }
        if (!inscripcion.isEmpty()) {
            whereInscripcion = "tgg.numero ILIKE '%" + inscripcion + "%'";
        }
        if (!anio.isEmpty()) {
            whereAnio = "tgg.anio ILIKE '%" + anio + "%'";
        }
        if (whereDeudorTGP != null && whereAnio != null && whereInscripcion != null) {
            whereDeudor1 = whereDeudorTGP + " AND " + whereAnio + " AND " + whereInscripcion;
            whereDeudor2 = whereDeudorTGG + " AND " + whereAnio + " AND " + whereInscripcion;
        }
        if (whereDeudorTGP == null && whereAnio != null && whereInscripcion != null) {
            whereDeudor1 = whereAnio + " AND " + whereInscripcion;
            whereDeudor2 = whereAnio + " AND " + whereInscripcion;
        }
        if (whereDeudorTGP != null && whereAnio == null && whereInscripcion != null) {
            whereDeudor1 = whereDeudorTGP + " AND " + whereInscripcion;
            whereDeudor2 = whereDeudorTGG + " AND " + whereInscripcion;
        }
        if (whereDeudorTGP != null && whereAnio != null && whereInscripcion == null) {
            whereDeudor1 = whereDeudorTGP + " AND " + whereAnio;
            whereDeudor2 = whereDeudorTGG + " AND " + whereAnio;
        }
        if (whereDeudorTGP != null && whereAnio == null && whereInscripcion == null) {
            whereDeudor1 = whereDeudorTGP;
            whereDeudor2 = whereDeudorTGG;
        }
        if (whereDeudorTGP == null && whereAnio != null && whereInscripcion == null) {
            whereDeudor1 = whereAnio;
            whereDeudor2 = whereAnio;
        }
        if (whereDeudorTGP == null && whereAnio == null && whereInscripcion != null) {
            whereDeudor1 = whereInscripcion;
            whereDeudor2 = whereInscripcion;
        }

        if (whereDeudor1 != null && whereDate != null) {
            whereDeudor1 += " AND " + whereDate;
            whereDeudor2 += " AND " + whereDate;
        }
        if (whereDeudor1 == null && whereDate != null) {
            whereDeudor1 = whereDate;
            whereDeudor2 = whereDate;
        }

        //  LAS ADVERTENCIAS QUE ESTAN REGISTRADAS EN LOS GRAVAMENES
        //  + "WHERE " + whereDeudor1 + " AND tinst.id NOT IN('128 ','0354')\n"
        //  + "WHERE " + whereDeudor2 + " AND tinst.id NOT IN('128 ','0354')\n";
        //  '128 ' NO DESPACHAR NADA
        //  '0354' CODIFICACION DEL SISTEMA
        String query = "WITH consulta as (SELECT tgg.id, tgg.idgrp, null as idtras, tgp.deudor as cliente, ttip.nombre as acto, null as num_inscripcion, tgg.repertor as repertorio, \n"
                + "tgg.anio||tgg.numero as numero_gravamen,tgg.anio as anio, tgg.numero, tgrp.fecins as fecha_inscripcion, tgrp.valor as cuantia, \n"
                + "null as catastro, 'DEUDOR' as tipo, null as parroquia, null as ubicacion_propiedad, null as notaria, null as fecha_notaria, \n"
                + "null as vendido, null as gravado, null as linderos, tgrp.observa as observaciones, tinst.nombre as institucion, tinst.id as id_institucion,\n"
                + "tgg.letra as letra, tgg.cancel as estado, tgrp.tipgrav as tipo_gravamen \n"
                + "FROM historico.tgggrav tgg \n"
                + "INNER JOIN  historico.tgrpgrav tgrp ON tgg.idgrp = tgrp.id\n"
                + "INNER JOIN  historico.ttipgrav ttip ON tgg.idtipgrav = ttip.\"idtipGrav\"\n"
                + "INNER JOIN  historico.tgpdeudo tgp ON tgg.idgrp = tgp.idgrp\n"
                + "LEFT JOIN  historico.tinstcre tinst ON tgrp.idinst=tinst.id\n"
                + "WHERE " + whereDeudor1
                + "\n UNION ALL\n"
                + "SELECT tgg.id, tggd.idgrp, ttras.id as idtras, tggd.deudor as cliente, ttip.nombre as acto, ttras.anio||ttras.numero as num_inscripcion, tgg.repertor as repertorio, \n"
                + "tgg.anio||tgg.numero as numero_gravamen, tgg.anio as anio, tgg.numero,tgrp.fecins as fecha_inscripcion, tgrp.valor as cuantia, \n"
                + "ttras.catastro, 'DEUDOR' as tipo, parr.nombre as parroquia, ttras.ubiprop as ubicacion_propiedad, ttras.notaria, \n"
                + "ttras.fecnot as fecha_notaria,ttras.vendido, ttras.gravado, ttras.linderos, tgrp.observa as observaciones, \n"
                + "tinst.nombre as institucion, tinst.id as id_institucion,tgg.letra as letra, tgg.cancel as estado, tgrp.tipgrav as tipo_gravamen \n"
                + "FROM  historico.tgggrav tgg\n"
                + "INNER JOIN  historico.tgrpgrav tgrp ON tgg.idgrp=tgrp.id\n"
                + "INNER JOIN  historico.ttipgrav ttip ON tgg.idtipgrav = ttip.\"idtipGrav\"\n"
                + "INNER JOIN  historico.tggdeudo tggd ON tgg.idgrp=tggd.idgrp\n"
                + "LEFT JOIN historico.tinstcre tinst ON tgrp.idinst=tinst.id\n"
                + "INNER JOIN  historico.ttraspas ttras ON tggd.idtras = ttras.id \n"
                + "LEFT JOIN historico.tparroqu parr ON parr.id = ttras.idparr\n"
                + "WHERE " + whereDeudor2 + " ) \n"
                + "SELECT * FROM consulta ORDER BY 11 DESC;";

        System.out.println("Qury gravamenes: " + query);

        return query;
    }

    public void onRowSelectClienteActo(SelectEvent event) {

        observaciones = observacionesByActo(clienteActo);
        Map<String, List<ClienteActo>> findGravamenesByActo = buscarGravamenesByActo(clienteActo);
        advertenciasByActo = findGravamenesByActo.get("advertencias");
        gravamenesByActo = findGravamenesByActo.get("gravamenes");

        Map<String, List<Acto>> antecedentes = buscarAntecedentes(clienteActo);
        anteriores = antecedentes.get("anteriores");
        posteriores = antecedentes.get("posteriores");
        Map<String, List<Cliente>> intervinientes = buscarIntervinientes(clienteActo);
        compradores = intervinientes.get("compradores");
        vendedores = intervinientes.get("vendedores");
        certificadosVisual = buscarCertificados(clienteActo);
        observGravamen = null;

        anterioreSeleccionado = null;
        posteriorSeleccionado = null;
    }

    public Map<String, List<ClienteActo>> buscarGravamenesByActo(ClienteActo c) {

        Map<String, List<ClienteActo>> result = new HashMap<>();

        String query = "SELECT ttip.nombre as acto, tinst.nombre as institucion, tinst.id as id_institucion, tgg.numero, tgg.repertor as repertorio, tgg.anio,\n"
                + "tgrp.fecins as fecha_inscripcion, tgg.cancel as estado, tgras.idtras, tgras.idgrp,\n"
                + "tgrp.tipgrav as tipo_gravamen, tgrp.observa as observaciones, tgg.letra as letra, tgg.anio||tgg.numero as numero_gravamen\n"
                + "FROM  historico.tgggrav tgg\n"
                + "INNER JOIN  historico.tggtras  tgras ON tgras.idgrp = tgg.idgrp\n"
                + "INNER JOIN  historico.tgrpgrav tgrp ON tgg.idgrp=tgrp.id\n"
                + "INNER JOIN  historico.ttipgrav ttip ON tgg.idtipgrav = ttip.\"idtipGrav\"\n"
                + "INNER JOIN  historico.tinstcre tinst ON tgrp.idinst=tinst.id\n"
                + "WHERE tgg.repertor <> '0' and tgras.idtras = '" + c.getId() + "' AND tgg.repertor NOT  LIKE '000000' AND tgrp.void = 0 \n"
                + "ORDER BY tgrp.fecins DESC;";

        conn = getConnection();
        List<ClienteActo> gravs = new ArrayList<>();
        List<ClienteActo> advs = new ArrayList<>();

        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        ClienteActo ca = new ClienteActo();
                        ca.setId(UUID.randomUUID().toString());
                        ca.setId(rs.getString("idtras"));
                        ca.setIdgrp(rs.getString("idgrp"));
                        ca.setActo(rs.getString("acto"));
                        ca.setInscripcion(rs.getString("numero"));
                        ca.setRepertorio(rs.getString("repertorio"));
                        ca.setAnio(rs.getString("anio"));
                        ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                        ca.setObservaciones(rs.getString("observaciones"));
                        ca.setEstadoGravamen(rs.getInt("estado"));
                        ca.setTipoGravamen(rs.getString("tipo_gravamen"));
                        ca.setInstitucion(rs.getString("institucion"));
                        ca.setIdInstitucion(rs.getString("id_institucion"));
                        ca.setNumeroGravamen(rs.getString("numero_gravamen"));

                        if (ca.getIdInstitucion().equals("128 ") || ca.getIdInstitucion().equals("0354")) {
                            advs.add(ca);
                        } else {
                            gravs.add(ca);
                        }

                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                try {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }

        result.put("gravamenes", gravs);
        result.put("advertencias", advs);

        return result;
    }

    public Map<String, List<Acto>> buscarAntecedentes(ClienteActo c) {

        Map<String, List<Acto>> result = new HashMap<>();

        List<Acto> actosAnteriores = new ArrayList<>();
        List<Acto> actosPosteriores = new ArrayList<>();

        String queryAnteriores = "SELECT t.id, tt.nombre as acto, t.anio, t.fecins as fecha_inscripcion, t.numero as num_inscripcion, t.repertor as repertorio, \n"
                + "t.ubiprop as ubicacion_propiedad FROM historico.ttraspas t\n"
                + "LEFT JOIN historico.ttiptras tt ON tt.id = t.idtiptras\n"
                + "WHERE t.id IN( SELECT e.anterior FROM historico.tenlace e WHERE e.posterior = '" + c.getId() + "')\n"
                + "ORDER BY t.fecins DESC";

        String queryPosteriores = "SELECT t.id, tt.nombre as acto, t.anio, t.fecins as fecha_inscripcion, t.numero as num_inscripcion, t.repertor as repertorio, \n"
                + "t.ubiprop as ubicacion_propiedad FROM historico.ttraspas t\n"
                + "LEFT JOIN historico.ttiptras tt ON tt.id = t.idtiptras\n"
                + "WHERE t.id IN( SELECT e.posterior FROM historico.tenlace e WHERE e.anterior = '" + c.getId() + "')\n"
                + "ORDER BY t.fecins;";

        conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement psA = conn.prepareStatement(queryAnteriores);
                try (ResultSet rs = psA.executeQuery()) {
                    while (rs.next()) {
                        Acto ca = new Acto();
                        ca.setId(rs.getString("id"));
                        ca.setActo(rs.getString("acto"));
                        ca.setAnio(rs.getString("anio"));
                        ca.setRepertorio(rs.getString("repertorio"));
                        ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                        ca.setInscripcion(rs.getString("num_inscripcion"));
                        ca.setUbicacionPropiedad(rs.getString("ubicacion_propiedad"));
                        actosAnteriores.add(ca);
                    }

                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

                PreparedStatement psP = conn.prepareStatement(queryPosteriores);
                try (ResultSet rs = psP.executeQuery()) {
                    while (rs.next()) {
                        Acto ca = new Acto();
                        ca.setId(rs.getString("id"));
                        ca.setActo(rs.getString("acto"));
                        ca.setAnio(rs.getString("anio"));
                        ca.setRepertorio(rs.getString("repertorio"));
                        ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                        ca.setInscripcion(rs.getString("num_inscripcion"));
                        ca.setUbicacionPropiedad(rs.getString("ubicacion_propiedad"));
                        actosPosteriores.add(ca);
                    }
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }
                conn.close();

            } catch (SQLException ex) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }

        result.put("anteriores", actosAnteriores);
        result.put("posteriores", actosPosteriores);

        return result;
    }

    public Map<String, List<Cliente>> buscarIntervinientes(ClienteActo c) {

        Map<String, List<Cliente>> result = new HashMap<>();

        List<Cliente> cVendedores = new ArrayList<>();
        List<Cliente> cCompradores = new ArrayList<>();

        String query = "SELECT CASE WHEN c IS NULL THEN t.comprador ELSE c.nombre END as cliente, 'ADQUIRIENTE'  as tipo \n"
                + "FROM historico.ttraspas t  \n"
                + "LEFT OUTER JOIN historico.ttrasdes c ON t.id = c.idtras\n"
                + "WHERE t.id = '" + c.getId() + "'\n"
                + "UNION ALL\n"
                + "SELECT CASE WHEN c IS NULL THEN t.vendedor ELSE c.nombre END as cliente, 'TRANSFIRIENTE'  as tipo \n"
                + "FROM historico.ttraspas t  \n"
                + "LEFT OUTER JOIN historico.ttrasori c ON t.id = c.idtras\n"
                + "WHERE t.id = '" + c.getId() + "';";

        conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Cliente cc = new Cliente();
                        cc.setNombre(rs.getString("cliente"));
                        cc.setTipo(rs.getString("tipo"));
                        if (cc.getTipo().equals("TRANSFIRIENTE")) {
                            cVendedores.add(cc);
                        } else {
                            cCompradores.add(cc);
                        }

                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                try {
                    conn.close();
                } catch (SQLException ex1) {

                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
        result.put("compradores", cCompradores);
        result.put("vendedores", cVendedores);

        return result;
    }

    public List<RegMovimientoFile> buscarCertificados(ClienteActo c) {
        List<RegMovimientoFile> certif = manager.findAll("SELECT r FROM RegMovimientoFile r WHERE r.idtras = :idTras", new String[]{"idTras"}, new Object[]{c.getId()});
        return certif;
    }

    public void verCertificadoVisualFAC(RegMovimientoFile cert) {
        try {
            if (cert != null) {
                JsfUti.redirectNewTab("/sgr/OmegaDownDocs?code=" + cert.getOidFile() + "&name=" + cert.getNombreArchivo()
                        + "&tipo=1&content=" + cert.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void onRowSelectClienteActoGravamen(SelectEvent event) {
        observGravamen = gravamenesByActoSeleccionado.getObservaciones();
    }

    public void onRowUnselectClienteActoGravamen(UnselectEvent event) {
        observGravamen = gravamenesByActoSeleccionado.getObservaciones();
    }

    public void onRowSelectGravamen(SelectEvent event) {
        gravamenenSeleccionado = (ClienteActo) event.getObject();
        gravamenesRelacionados = new ArrayList<>();
        observGravamen = gravamenenSeleccionado.getObservaciones();
        if (gravamenenSeleccionado.getTipoGravamen().equals("R")) {
            actoByGravamen();
            gravamenesRelacionadosByGravamen();
        } else {
            observaciones = "";
            gravamenesRelacionados.add(gravamenenSeleccionado);
        }
        deudoresAsociados(gravamenenSeleccionado);
    }

    public void onRowUnselectGravamen(UnselectEvent event) {
        gravamenenSeleccionado = (ClienteActo) event.getObject();
        gravamenesRelacionados = new ArrayList<>();
        observGravamen = gravamenenSeleccionado.getObservaciones();

        if (gravamenenSeleccionado.getTipoGravamen().equals("R")) {
            gravamenesRelacionadosByGravamen();
            actoByGravamen();
        } else {
            observaciones = "";
            gravamenesRelacionados.add(gravamenenSeleccionado);
        }
    }

    private void deudoresAsociados(ClienteActo ac) {

        String query = "SELECT tgp.deudor\n"
                + "FROM historico.tgpdeudo tgp\n"
                + "INNER JOIN historico.tgggrav tgg ON tgg.idgrp = tgp.idgrp\n"
                + "INNER JOIN  historico.ttipgrav ttip ON tgg.idtipgrav = ttip.\"idtipGrav\"\n"
                + "WHERE tgg.anio ILIKE '%" + ac.getAnio() + "%' AND tgg.numero ILIKE '%" + ac.getNumero() + "%' AND ttip.nombre ILIKE '%" + ac.getActo() + "%'\n"
                + "GROUP BY 1 ORDER BY 1;";
        if (ac.getTipoGravamen().equals("R")) {
            query = "SELECT tgp.deudor\n"
                    + "FROM historico.tggdeudo tgp\n"
                    + "INNER JOIN historico.tgggrav tgg ON tgg.idgrp = tgp.idgrp\n"
                    + "INNER JOIN  historico.ttipgrav ttip ON tgg.idtipgrav = ttip.\"idtipGrav\"\n"
                    + "WHERE tgg.anio ILIKE '%" + ac.getAnio() + "%' AND tgg.numero ILIKE '%" + ac.getNumero() + "%' AND ttip.nombre ILIKE '%" + ac.getActo() + "%'\n"
                    + "GROUP BY 1 ORDER BY 1;";
        }
        deudores = new ArrayList<>();
        conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Cliente cc = new Cliente();
                        cc.setNombre(rs.getString("deudor"));
                        deudores.add(cc);
                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                try {
                    conn.close();
                } catch (SQLException ex1) {

                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public void actoByGravamen() {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query = "SELECT t.id, tt.nombre as acto, t.numero as num_inscripcion, t.repertor as repertorio,\n"
                + "t.anio, t.fecins as fecha_inscripcion, t.valor as cuantia, t.catastro, p.nombre as parroquia,\n"
                + "t.ubiprop as ubicacion_propiedad, t.notaria, t.fecnot as fecha_notaria, t.vendido, t.gravado, t.linderos, t.observa as observaciones\n"
                + "FROM historico.ttraspas t \n"
                + "LEFT JOIN historico.ttiptras tt ON tt.id = t.idtiptras\n"
                + "LEFT JOIN historico.tparroqu p ON p.id = t.idparr\n"
                + "WHERE t.id = '" + gravamenenSeleccionado.getIdTras() + "' AND t.void = 0";

        conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ClienteActo ca = new ClienteActo();
                        ca.setId(rs.getString("id"));
                        ca.setActo(rs.getString("acto"));
                        ca.setInscripcion(rs.getString("num_inscripcion"));
                        ca.setAnio(rs.getString("anio"));
                        ca.setCuantia(rs.getString("cuantia"));
                        ca.setCatastro(rs.getString("catastro"));
                        ca.setParroquia(rs.getString("parroquia"));
                        ca.setNotaria(rs.getString("notaria"));
                        ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                        ca.setFechaNotaria(rs.getDate("fecha_notaria"));
                        ca.setObservaciones(rs.getString("observaciones"));
                        ca.setUbicacionPropiedad(rs.getString("ubicacion_propiedad"));

                        observaciones = observacionesByActo(ca);
                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public void gravamenesRelacionadosByGravamen() {
        String query = "SELECT tgg.id, ttip.nombre as acto, ttras.numero as num_inscripcion, tgg.repertor as repertorio, tgg.anio||tgg.numero as numero_gravamen,\n"
                + "tgg.anio as anio, tgrp.fecins as fecha_inscripcion, tgrp.valor as cuantia, ttras.catastro, 'DEUDOR' as tipo, parr.nombre as parroquia,\n"
                + "ttras.ubiprop as ubicacion_propiedad, ttras.notaria, ttras.fecnot as fecha_notaria, ttras.vendido, ttras.gravado, ttras.linderos,\n"
                + "tgrp.observa as observaciones, tinst.nombre as institucion,tgg.letra as letra, tgg.cancel as estado, tgrp.tipgrav as tipo_gravamen \n"
                + "FROM  historico.tgggrav tgg\n"
                + "INNER JOIN  historico.tgrpgrav tgrp ON tgg.idgrp=tgrp.id\n"
                + "INNER JOIN  historico.ttipgrav ttip ON tgg.idtipgrav = ttip.\"idtipGrav\"\n"
                + "INNER JOIN  historico.tggtras tgtra ON tgtra.idgrp=tgrp.id\n"
                + "INNER JOIN historico.tinstcre tinst ON tgrp.idinst=tinst.id\n"
                + "INNER JOIN  historico.ttraspas ttras ON tgtra.idtras = ttras.id \n"
                + "LEFT JOIN historico.tparroqu parr ON parr.id = ttras.idparr\n"
                + "WHERE  tgg.repertor <> '0' and ttras.id = '" + gravamenenSeleccionado.getIdTras() + "' AND tgg.repertor NOT  LIKE '000000' AND ttras.void = 0 \n"
                + "ORDER BY tgg.repertor, tgrp.fecins;";
        conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ClienteActo ca = new ClienteActo();
                        //ca.setIdgrp(rs.getString("idgrp"));
                        ca.setActo(rs.getString("acto"));
                        ca.setInscripcion(rs.getString("num_inscripcion"));
                        ca.setAnio(rs.getString("anio"));
                        ca.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                        ca.setEstadoGravamen(rs.getInt("estado"));
                        ca.setTipoGravamen(rs.getString("tipo_gravamen"));
                        ca.setInstitucion(rs.getString("institucion"));
                        ca.setNumeroGravamen(rs.getString("numero_gravamen"));
                        gravamenesRelacionados.add(ca);
                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public void buscarOrion() {

        clearDataOrion();

        if (repertorio != null) {
            if (repertorio.length() > 0 && repertorio.length() < 4) {
                repertorio = StringUtils.leftPad(repertorio, 4, "0");
            }
        }
        String whereCliente = null;
        String whereAnio = null;
        String whereRepertorio = null;
        String where = null;

        if (cliente != null) {
            if (!cliente.isEmpty()) {
                whereCliente = "(acto.\"Nombre\" ILIKE '" + cliente + "%' OR acto.\"Descripcion\" ILIKE '" + cliente + "%')";
            }
        }
        if (repertorio != null) {
            if (!repertorio.isEmpty()) {
                whereRepertorio = "acto.\"Numero\" = '" + repertorio + "'";
            }
        }
        if (anio != null) {
            if (!anio.isEmpty()) {
                whereAnio = "acto.\"Anio\" = '" + anio + "'";
            }
        }

        if (whereCliente != null && whereAnio != null && whereRepertorio != null) {
            where = "WHERE " + whereCliente + " AND " + whereAnio + " AND " + whereRepertorio + "\n";
        }
        if (whereCliente != null && whereAnio != null && whereRepertorio == null) {
            where = "WHERE " + whereCliente + " AND " + whereAnio + "\n";
        }
        if (whereCliente != null && whereAnio == null && whereRepertorio != null) {
            where = "WHERE " + whereCliente + " AND " + whereRepertorio + "\n";
        }
        if (whereCliente == null && whereAnio != null && whereRepertorio != null) {
            where = "WHERE " + whereAnio + " AND " + whereRepertorio + "\n";
        }
        if (whereCliente != null && whereAnio == null && whereRepertorio == null) {
            where = "WHERE " + whereCliente + "\n";
        }
        if (whereCliente == null && whereAnio == null && whereRepertorio != null) {
            where = "WHERE " + whereRepertorio + "\n";
        }
        if (whereCliente == null && whereAnio != null && whereRepertorio == null) {
            where = "WHERE " + whereAnio + "\n";
        }

        if (where == null) {
            JsfUti.messageWarning(null, "Debe llenar la menos un parametro para la busqueda", "");
            return;
        }

        String query = "SELECT  acto.\"Id\" as id, tipo.\"Id\" as id_tipo, tipo.\"Nombre\" as acto, acto.\"Anio\" as anio, acto.\"Numero\" as repertorio, acto.\"Mes\" as estado,\n"
                + "acto.\"Nombre\" as cliente, acto.\"Descripcion\" as descripcion, acto.\"Valor\" as cuantia, acto.\"Predio\" as predio,\n"
                + "acto.\"Parroquia\" as parroquia, acto.\"Integrado\" as integrado, acto.\"Certificado\" as certificado, \n"
                + "acto.\"CedUno\" as ced_uno, acto.\"CedDos\" as ced_dos, acto.\"Detalle\" as detalle, acto.\"idCedUno\" as id_ceduno,\n"
                + "acto.\"idCedDos\" as id_ceddos\n"
                + "FROM historico.\"NEGRO\" acto \n"
                + "INNER JOIN historico.\"TIPOSNEGRO\" tipo ON acto.\"Tipo\" = tipo.\"Id\"\n"
                + where
                + "ORDER BY 7;";

        conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement ps = conn.prepareStatement(query);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        OrionActo ac = new OrionActo();
                        ac.setId(rs.getBigDecimal("id"));
                        ac.setIdActo(rs.getInt("id_tipo"));
                        ac.setActo(rs.getString("acto"));
                        ac.setAnio(rs.getString("anio"));
                        ac.setRepertorio(rs.getString("repertorio"));
                        ac.setMes(rs.getString("estado"));
                        ac.setCliente(rs.getString("cliente"));
                        ac.setDescripcion(rs.getString("descripcion"));
                        ac.setCuantia(rs.getString("cuantia"));
                        ac.setPredio(rs.getString("predio"));
                        ac.setParroquia(rs.getString("parroquia"));
                        ac.setIntegrado(rs.getString("integrado"));
                        ac.setCertificado(rs.getString("certificado"));
                        adicionarActoOrion(ac);
                    }
                    conn.close();
                } catch (SQLException ex) {
                    conn.close();
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                try {
                    conn.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private void adicionarActoOrion(OrionActo ac) {
        switch (ac.getIdActo()) {
            case 2: { // CERTIFICADOS
                certificados.add(ac);
                break;
            }
            case 3: { // COMPRAS
                compras.add(ac);
                break;
            }
            case 5: { // DEMANDAS
                demandas.add(ac);
                break;
            }
            case 6: { // EMBARGOS
                embargos.add(ac);
                break;
            }
            case 7: { // HIPOTECAS
                hipotecas.add(ac);
                break;
            }
            case 8: { // INTERDICCIONES
                interdicciones.add(ac);
                break;
            }
            case 9: { // NOMBRAMIENTOS
                nombramientos.add(ac);
                break;
            }
            case 11: { // PATRIMONIO FAMILIAR
                patrimonios.add(ac);
                break;
            }
            case 12: { // PODER ESPECIAL
                poderes.add(ac);
                break;
            }
            case 13: { // PRENDAS
                prendas.add(ac);
                break;
            }
            case 14: { // PROHIBICIONES DE ENAJENAR
                prohibiciones.add(ac);
                break;
            }
            case 15: { // VENTAS
                ventas.add(ac);
                break;
            }
            case 1953: { // CAUCIONES
                cauciones.add(ac);
                break;
            }
        }
    }

    private void clearDataOrion() {
        compras = new ArrayList<>();
        ventas = new ArrayList<>();
        hipotecas = new ArrayList<>();
        prohibiciones = new ArrayList<>();
        embargos = new ArrayList<>();
        patrimonios = new ArrayList<>();
        certificados = new ArrayList<>();
        demandas = new ArrayList<>();
        interdicciones = new ArrayList<>();
        cauciones = new ArrayList<>();
        nombramientos = new ArrayList<>();
        poderes = new ArrayList<>();
        prendas = new ArrayList<>();
        certificado = "";
    }

    public void onRowSelectCertificado(SelectEvent event) {
        certificado = "";
        if (actoSeleccionado.getCertificado() != null) {
            try {
                certificado = arch.rtfToHtml(actoSeleccionado.getCertificado());
            } catch (IOException ex) {
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onRowUnselectCertificado(UnselectEvent event) {
        certificado = "";
        if (actoSeleccionado.getCertificado() != null) {
            try {
                certificado = arch.rtfToHtml(actoSeleccionado.getCertificado());
            } catch (IOException ex) {
                Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean filterByInscripcion(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        filterText = StringUtils.leftPad(filterText, 6, "0");

        return value.toString().equals(filterText);
    }

    public void test() {
        System.out.println("Entro al test del ajax");
    }

    public Connection getConnection() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties props = new Properties();
        props.setProperty("user", SisVars.userName);
        props.setProperty("password", SisVars.password);
        conn = null;
        try {
            conn = DriverManager.getConnection(SisVars.url, props);
        } catch (SQLException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
//        try {
//            Session sess = HiberUtil.getSession();
//            SessionImplementor sessImpl = (SessionImplementor) sess;
//            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
//            return conn;
//        } catch (SQLException e) {
//            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "", e);
//            //throw new SqlRuntimeException(e);
//            return null;
//        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public List<ClienteActo> getTraspasos() {
        return traspasos;
    }

    public void setTraspasos(List<ClienteActo> traspasos) {
        this.traspasos = traspasos;
    }

    public List<ClienteActo> getGravamenes() {
        return gravamenes;
    }

    public void setGravamenes(List<ClienteActo> gravamenes) {
        this.gravamenes = gravamenes;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public List<Acto> getAnteriores() {
        return anteriores;
    }

    public void setAnteriores(List<Acto> anteriores) {
        this.anteriores = anteriores;
    }

    public List<Acto> getPosteriores() {
        return posteriores;
    }

    public void setPosteriores(List<Acto> posteriores) {
        this.posteriores = posteriores;
    }

    public List<Cliente> getVendedores() {
        return vendedores;
    }

    public void setVendedores(List<Cliente> vendedores) {
        this.vendedores = vendedores;
    }

    public List<Cliente> getCompradores() {
        return compradores;
    }

    public void setCompradores(List<Cliente> compradores) {
        this.compradores = compradores;
    }

    public List<Cliente> getDeudores() {
        return deudores;
    }

    public void setDeudores(List<Cliente> deudores) {
        this.deudores = deudores;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(String inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(String repertorio) {
        this.repertorio = repertorio;
    }

    public List<ClienteActo> getGravamenesByActo() {
        return gravamenesByActo;
    }

    public void setGravamenesByActo(List<ClienteActo> gravamenesByActo) {
        this.gravamenesByActo = gravamenesByActo;
    }

    public ClienteActo getClienteActo() {
        return clienteActo;
    }

    public void setClienteActo(ClienteActo clienteActo) {
        this.clienteActo = clienteActo;
    }

    public ClienteActo getGravamenesByActoSeleccionado() {
        return gravamenesByActoSeleccionado;
    }

    public void setGravamenesByActoSeleccionado(ClienteActo gravamenesByActoSeleccionado) {
        this.gravamenesByActoSeleccionado = gravamenesByActoSeleccionado;
    }

    public Acto getAnterioreSeleccionado() {
        return anterioreSeleccionado;
    }

    public void setAnterioreSeleccionado(Acto anterioreSeleccionado) {
        this.anterioreSeleccionado = anterioreSeleccionado;
    }

    public Acto getPosteriorSeleccionado() {
        return posteriorSeleccionado;
    }

    public void setPosteriorSeleccionado(Acto posteriorSeleccionado) {
        this.posteriorSeleccionado = posteriorSeleccionado;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservGravamen() {
        return observGravamen;
    }

    public void setObservGravamen(String observGravamen) {
        this.observGravamen = observGravamen;
    }

    public List<ClienteActo> getAdvertenciasByActo() {
        return advertenciasByActo;
    }

    public void setAdvertenciasByActo(List<ClienteActo> advertenciasByActo) {
        this.advertenciasByActo = advertenciasByActo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public List<ClienteActo> getGravamenesRelacionados() {
        return gravamenesRelacionados;
    }

    public void setGravamenesRelacionados(List<ClienteActo> gravamenesRelacionados) {
        this.gravamenesRelacionados = gravamenesRelacionados;
    }

    public ClienteActo getGravamenenSeleccionado() {
        return gravamenenSeleccionado;
    }

    public void setGravamenenSeleccionado(ClienteActo gravamenenSeleccionado) {
        this.gravamenenSeleccionado = gravamenenSeleccionado;
    }

    public int getTabSelect() {
        return tabSelect;
    }

    public void setTabSelect(int tabSelect) {
        this.tabSelect = tabSelect;
    }

    public OrionActo getActoSeleccionado() {
        return actoSeleccionado;
    }

    public void setActoSeleccionado(OrionActo actoSeleccionado) {
        this.actoSeleccionado = actoSeleccionado;
    }

    public List<OrionActo> getCompras() {
        return compras;
    }

    public void setCompras(List<OrionActo> compras) {
        this.compras = compras;
    }

    public List<OrionActo> getVentas() {
        return ventas;
    }

    public void setVentas(List<OrionActo> ventas) {
        this.ventas = ventas;
    }

    public List<OrionActo> getHipotecas() {
        return hipotecas;
    }

    public void setHipotecas(List<OrionActo> hipotecas) {
        this.hipotecas = hipotecas;
    }

    public List<OrionActo> getProhibiciones() {
        return prohibiciones;
    }

    public void setProhibiciones(List<OrionActo> prohibiciones) {
        this.prohibiciones = prohibiciones;
    }

    public List<OrionActo> getEmbargos() {
        return embargos;
    }

    public void setEmbargos(List<OrionActo> embargos) {
        this.embargos = embargos;
    }

    public List<OrionActo> getPatrimonios() {
        return patrimonios;
    }

    public void setPatrimonios(List<OrionActo> patrimonios) {
        this.patrimonios = patrimonios;
    }

    public List<OrionActo> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<OrionActo> certificados) {
        this.certificados = certificados;
    }

    public List<OrionActo> getDemandas() {
        return demandas;
    }

    public void setDemandas(List<OrionActo> demandas) {
        this.demandas = demandas;
    }

    public List<OrionActo> getInterdicciones() {
        return interdicciones;
    }

    public void setInterdicciones(List<OrionActo> interdicciones) {
        this.interdicciones = interdicciones;
    }

    public List<OrionActo> getCauciones() {
        return cauciones;
    }

    public void setCauciones(List<OrionActo> cauciones) {
        this.cauciones = cauciones;
    }

    public List<OrionActo> getNombramientos() {
        return nombramientos;
    }

    public void setNombramientos(List<OrionActo> nombramientos) {
        this.nombramientos = nombramientos;
    }

    public List<OrionActo> getPoderes() {
        return poderes;
    }

    public void setPoderes(List<OrionActo> poderes) {
        this.poderes = poderes;
    }

    public List<OrionActo> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<OrionActo> prendas) {
        this.prendas = prendas;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public List<RegMovimientoFile> getCertificadosVisual() {
        return certificadosVisual;
    }

    public void setCertificadosVisual(List<RegMovimientoFile> certificadosVisual) {
        this.certificadosVisual = certificadosVisual;
    }

//</editor-fold>
}
