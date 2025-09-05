/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCapital;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegMovimientoRepresentante;
import com.origami.sgr.entities.RegMovimientoSocios;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.lazymodels.RegCertificadosLazy;
import com.origami.sgr.lazymodels.RegEnteIntervinienteLazy;
import com.origami.sgr.lazymodels.RegFichaLazy;
import com.origami.sgr.lazymodels.RegMovimientoClienteLazy;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.IndiceRegistro;
import com.origami.sgr.models.index.TbBlob;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ConsultasRp extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private RegistroPropiedadServices reg;

    protected List<RegMovimiento> listMovs = new ArrayList<>();
    protected RegMovimiento movSelec = new RegMovimiento();
    protected Long numeroTramite;
    protected List<RegEnteInterviniente> propietarios = new ArrayList<>();
    protected List<RegMovimientoCliente> movimientosInterv = new ArrayList<>();
    protected List<RegFicha> fichasInterv = new ArrayList<>();
    protected RegEnteIntervinienteLazy intervinientesLazy;
    protected RegMovimiento movimiento;
    protected RegMovimiento movimiento1;
    protected RegFicha ficha;

    protected List<RegMovimientoCliente> clientes = new ArrayList<>();
    protected List<RegFicha> fichas = new ArrayList<>();
    protected List<RegMovimientoFicha> movimientosFichas = new ArrayList<>();
    protected List<RegMovimientoMarginacion> marginaciones = new ArrayList<>();
    protected List<RegMovimientoRepresentante> listMovRep = new ArrayList<>();
    protected List<RegMovimientoSocios> listMovSoc = new ArrayList<>();
    protected List<RegMovimientoCapital> listMovcap = new ArrayList<>();

    protected String linderos;
    protected RegCertificadosLazy certificadosLazy;
    protected RegMovimientosLazy movimientosLazy;
    protected RegFichaLazy fichasLazy;

    protected Integer tipoConsulta = 0;
    protected String valorConsulta;
    protected RegMovimientosLazy lazyMovs;
    protected RegFichaLazy lazyFichas;
    protected Integer showDataTable = 0;

    protected Boolean realizarTarea = false;
    protected Boolean showInterv = false;
    protected Boolean showBtn = false;
    protected Long idTarea;
    protected RegEnteInterviniente interviniente;

    protected RegEnteInterviniente select = new RegEnteInterviniente();
    protected Integer cantMovs = 0;
    protected Integer cantFich = 0;

    protected String cadena = "";
    protected Date desde;
    protected Date hasta;

    protected List<RegLibro> libros;
    protected RegLibro libroConsEsp;
    protected RegActo actoConsEsp;
    protected Long inscripcionConsEsp;
    protected Long repertorioConsEsp;
    protected Date desdeConsEsp;
    protected Date hastaConsEsp;
    protected List<RegActo> listActos;
    protected List<RegMovimiento> movimientosSeleccionados;
    protected String urlDownload = "";
    protected Calendar cal = Calendar.getInstance();
    protected Integer folioConsEsp;
    protected String tomoConsEsp;

    protected RegMovimientoCliente rmc;
    protected RegMovimientoClienteLazy mcl;
    protected String nombre, apellido, documento, contrato, contratante, nombre1;
    protected Integer inscripcion, repertorio, anio, tipo;
    protected Integer inscripcion1, repertorio1, anio1, tipo1;

    protected List<TbBlob> blobs;
    protected RegRegistrador registrador;
    protected AclUser user;

    @PostConstruct
    protected void iniView() {
        try {
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
            user = manager.find(AclUser.class, session.getUserId());
            libros = manager.findAll(RegLibro.class);
            actoConsEsp = new RegActo();
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirInscripcionesPorFecha() {
        try {
            if (hasta.after(desde) || hasta.equals(desde)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String strDesde = sdf.format(desde);
                String strHasta = sdf.format(hasta);
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("ConsultaEspecificaFechas");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("DESDE", sdf.parse(strDesde));
                ss.agregarParametro("HASTA", sdf.parse(strHasta));
                ss.agregarParametro("USERNAME", session.getName_user());
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
            }
        } catch (ParseException e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirReporte(RegEnteInterviniente interv) {
        try {
            Integer i = 0;
            Collection col1 = reg.getListIdMovsByCedRucInterv(interv.getCedRuc());
            if (col1 != null) {
                i = col1.size();
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("ConsultaEspecificaPersona");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ENTE", interv.getCedRuc());
            ss.agregarParametro("NOMBRE", interv.getNombre());
            ss.agregarParametro("USERNAME", session.getName_user());
            ss.agregarParametro("TOTAL", "TOTAL   :   " + i + "  Movimientos.");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirReporte(RegEnteInterviniente interv, Integer tipoLibro) {
        try {
            Integer i = 0;
            Collection col1 = reg.getListIdMovsByCedRucIntervTipoLibro(interv.getCedRuc(), tipoLibro);
            if (col1 != null) {
                i = col1.size();
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("ConsultaEspecificaPersona");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ENTE", interv.getCedRuc());
            ss.agregarParametro("NOMBRE", interv.getNombre());
            ss.agregarParametro("TIPO_LIBRO", tipoLibro);
            ss.agregarParametro("USERNAME", session.getName_user());
            ss.agregarParametro("TOTAL", "TOTAL   :   " + i + "  Movimientos.");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirInformeBienes(RegEnteInterviniente interv) {
        try {
            Integer i = 0;
            Collection col1 = reg.getListIdMovsByCedRucInterv(interv.getCedRuc());
            if (col1 != null) {
                i = col1.size();
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("CertificadoInformeBienes");
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ENTE", interv.getCedRuc());
            ss.agregarParametro("NOMBRE", interv.getNombre());
            ss.agregarParametro("USERNAME", session.getName_user());
            ss.agregarParametro("TOTAL", "TOTAL   :   " + i + "  Movimientos.");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void reporteInformeBienes() {
        try {
            if (!cadena.isEmpty()) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("InformeDeBienes");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("NOMBRE", cadena);
                ss.agregarParametro("USERNAME", session.getName_user());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar un valor para el reporte.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacoraMov() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", movimiento.getId());
            ss.agregarParametro("numFicha", null);
            ss.agregarParametro("titulo", Messages.bitacoraMovimiento);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacoraMov(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", mov.getId());
            ss.agregarParametro("numFicha", null);
            ss.agregarParametro("titulo", Messages.bitacoraMovimiento);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacoraFicha() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", null);
            //ss.agregarParametro("numFicha", ficha.getNumFicha());
            ss.agregarParametro("numFicha", ficha.getId());
            ss.agregarParametro("titulo", Messages.bitacoraFicha);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirCompletandoTarea() {
        try {
            Integer i = 0;
            if (interviniente.getRegMovimientoClienteCollection() != null) {
                i = interviniente.getRegMovimientoClienteCollection().size();
            }
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("ConsultaEspecificaPersona");
            ss.setNombreSubCarpeta("registroPropiedad");
            ss.agregarParametro("ENTE", interviniente.getId());
            ss.agregarParametro("USERNAME", session.getName_user());
            ss.agregarParametro("TOTAL", "TOTAL   :   " + i + "  Movimientos.");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            showBtn = true;
            JsfUti.update("mainForm:panelInterviniente");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirInscripcionesEspecificas() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("ConsultaEspecifica");
            ss.setNombreSubCarpeta("registro");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date limite = sdf.parse(Constantes.fechaInicioInscripciones);
            if (hastaConsEsp != null) {
                if (hastaConsEsp.before(limite)) {
                    limite = hastaConsEsp;
                } else {
                    limite = Utils.sumarRestarDiasFecha(hastaConsEsp, 1);
                }
            } else {
                limite = reg.getFechaInscripcionMayor();
            }
            ss.agregarParametro("LIBRO", libroConsEsp != null ? libroConsEsp.getId() : 0L);
            ss.agregarParametro("LIBRO_NAME", libroConsEsp != null ? libroConsEsp.getNombre() : "TODOS LOS LIBROS");
            ss.agregarParametro("ACTO", actoConsEsp.getId() != null ? actoConsEsp.getId() : 0L);
            ss.agregarParametro("ACTO_NAME", actoConsEsp.getId() != null ? actoConsEsp.getAbreviatura() + " | " + actoConsEsp.getNombre() : "TODOS LOS ACTOS");
            ss.agregarParametro("INSCRIPCION", inscripcionConsEsp != null ? inscripcionConsEsp : 0L);
            ss.agregarParametro("REPERTORIO", repertorioConsEsp != null ? repertorioConsEsp : 0L);
            ss.agregarParametro("DESDE", desdeConsEsp != null ? desdeConsEsp : reg.getFechaInscripcionMenor());
            ss.agregarParametro("HASTA", limite);
            ss.agregarParametro("USERNAME", session.getName_user());
            ss.agregarParametro("FOLIO", folioConsEsp != null ? folioConsEsp : 0);
            ss.agregarParametro("TOMO", tomoConsEsp != null && !tomoConsEsp.isEmpty() ? tomoConsEsp : null);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (ParseException e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "No se pudo generar el reporte.");
        }
    }

    public void consultaInterviniente(RegEnteInterviniente interv) {
        try {
            select = interv;
            movimientosLazy = new RegMovimientosLazy(interv.getCedRuc());
            fichasLazy = new RegFichaLazy(interv.getCedRuc(), 1);
            JsfUti.update("formConsulta");
            JsfUti.executeJS("PF('dlgConsultaInterv').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultaPropiedades(RegEnteInterviniente interv) {
        try {
            select = interv;
            movimientosLazy = new RegMovimientosLazy(interv.getCedRuc());
            movimientosLazy.addFilter("libro.tipo", 1);
            fichasLazy = new RegFichaLazy(interv.getCedRuc(), 1L);
            JsfUti.update("formConsulta");
            JsfUti.executeJS("PF('dlgConsultaInterv').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultaGravamenes(RegEnteInterviniente interv) {
        try {
            select = interv;
            movimientosLazy = new RegMovimientosLazy(interv.getCedRuc());
            movimientosLazy.addFilter("libro.tipo", 2);
            fichasLazy = new RegFichaLazy(interv.getCedRuc(), 2L);
            JsfUti.update("formConsulta");
            JsfUti.executeJS("PF('dlgConsultaInterv').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgBienes(RegEnteInterviniente interv) {
        try {
            select = interv;
            /*fichasInterv = reg.getBienesByPropietario(interv.getId());
            if (fichasInterv.isEmpty()) {
                JsfUti.messageInfo(null, "El cliente no tiene enlazada ninguna ficha.", "");
            } else {
                JsfUti.update("formFichasInterviniente");
                JsfUti.executeJS("PF('dlgFichasInterviniente').show();");
            }*/
            fichasLazy = new RegFichaLazy(interv.getCedRuc(), 1);
            JsfUti.update("formFichasInterviniente");
            JsfUti.executeJS("PF('dlgFichasInterviniente').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultaIndices(RegEnteInterviniente interv) {
        try {
            select = interv;
            listMovs = reg.getListMovimientosByCedInterv(interv.getCedRuc());
            if (listMovs.isEmpty()) {
                JsfUti.messageInfo(null, "El cliente no registra ingresos de indices.", "");
            } else {
                JsfUti.update("formIndices");
                JsfUti.executeJS("PF('consultaIndices').show();");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void detalleMovimiento(RegMovimiento movi) {
        try {
            movSelec = movi;
            JsfUti.update("formIndicesDet");
            JsfUti.executeJS("PF('consultaIndicesDet').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultaIndiceByTramite() {
        try {
            if (Utils.isNum(valorConsulta)) {
                numeroTramite = Long.getLong(valorConsulta);
                clientes = reg.getListMovimientosByTramite(numeroTramite);
                if (clientes.isEmpty()) {
                    JsfUti.messageInfo(null, "No hay datos.", "");
                } else {
                    JsfUti.update("formIndicesTramite");
                    JsfUti.executeJS("PF('consultaIndicesTramite').show();");
                }
            } else {
                JsfUti.messageInfo(null, "Numero de tramite es incorrecto.", "");
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            movimiento.setRegMovimientoReferenciaCollection(manager.findAll(Querys.getRegMovimientoReferenciaByIdMov,
                    new String[]{"idmov"}, new Object[]{movimiento.getId()}));
            fichas = reg.getRegFichaByIdRegMov(mov.getId());
            clientes = reg.getRegMovClienteByIdMov(mov.getId());
            marginaciones = reg.getRegMovMargByIdMov(mov.getId());
            listMovRep = reg.getRegMovRepreByIdMov(mov.getId());
            listMovSoc = reg.getRegMovSocioByIdMov(mov.getId());
            listMovcap = reg.getRegMovCapByIdMov(mov.getId());
            JsfUti.update("formMovRegSelec");
            JsfUti.executeJS("PF('dlgMovRegSelec').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgFichaSelect(RegFicha f) {
        try {
            ficha = f;
            //ficha.setDescripcionTemp(f.getObsvEstado(f.getEstado()));
            movimientosFichas = reg.getRegMovByIdFicha(f.getId());
            propietarios = reg.getPropietariosByFicha(f.getId());
            //JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
            JsfUti.update("formFichaSelect");
            JsfUti.executeJS("PF('dlgFichaSelect').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void otrasConsultas() {
        try {
            lazyFichas = null;
            lazyMovs = null;
            switch (tipoConsulta) {
                case 0:
                    JsfUti.messageWarning(null, "Debe seleccionar el tipo de consulta.", "");
                    break;
                case 1: //NRO DE FICHA O CLAVE CATASTRAL
                    JsfUti.executeJS("PF('dtFichasConsultas').clearFilters();");
                    lazyFichas = new RegFichaLazy(new RegTipoFicha(1L));
                    showDataTable = 2;
                    JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                    break;
                case 2: //LINDEROS REGISTRALES
                    JsfUti.update("frmLinderos");
                    JsfUti.executeJS("PF('dlgLinderos').show();");
                    break;
                case 3: //VISUALIZAR TODAS LAS INSCRIPCIONES
                    JsfUti.executeJS("PF('dtMovsConsultas').clearFilters();");
                    lazyMovs = new RegMovimientosLazy();
                    showDataTable = 1;
                    JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                    break;
                case 4: //CONSULTA DE INSCRIPCIONES POR RANGO DE FECHA
                    JsfUti.update("formFechas");
                    JsfUti.executeJS("PF('consultaFechas').show();");
                    break;
                case 5: //CONSULTA ESPECIFICA DE INSCRIPCIONES
                    JsfUti.update("formInsEspecificas");
                    JsfUti.executeJS("PF('consultaEspecifica').show();");
                    break;
                case 6: //FICHAS MERCANTILES
                    JsfUti.executeJS("PF('dtFichasConsultas').clearFilters();");
                    lazyFichas = new RegFichaLazy(new RegTipoFicha(2L));
                    showDataTable = 2;
                    JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                case 7: //CERTIFICADOS WORD
                    JsfUti.executeJS("PF('dtCertificadosConsulta').clearFilters();");
                    certificadosLazy = new RegCertificadosLazy();
                    certificadosLazy.addFilter("tipoCertificado", 7L);
                    certificadosLazy.addSorted("numCertificado", "DESC");
                    showDataTable = 3;
                    JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                    break;
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscarFichasPorLinderos() {
        try {
            if (linderos != null && !linderos.isEmpty()) {
                JsfUti.executeJS("PF('dtFichasConsultas').clearFilters();");
                lazyFichas = new RegFichaLazy(linderos, 2);
                showDataTable = 2;
                JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                JsfUti.executeJS("PF('dlgLinderos').hide();");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscarInscripFechas() {
        try {
            if (hasta.after(desde) || hasta.equals(desde)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date limite = sdf.parse(Constantes.fechaInicioInscripciones);
                if (hasta.after(limite) || hasta.equals(limite)) {
                    limite = Utils.sumarRestarDiasFecha(hasta, 1);
                } else {
                    limite = hasta;
                }
                JsfUti.executeJS("PF('dtMovsConsultas').clearFilters();");
                lazyMovs = new RegMovimientosLazy(desde, limite);
                showDataTable = 1;
                JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                JsfUti.executeJS("PF('consultaFechas').hide();");
            } else {
                JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
            }
        } catch (ParseException e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscarInterv() {
        try {
            if (cadena != null && !cadena.isEmpty()) {
                intervinientesLazy = new RegEnteIntervinienteLazy(cadena);
                JsfUti.update("mainForm:tabConsultas:dtInterviniente");
            } else {
                //intervinientesLazy = new RegEnteIntervinienteLazy();
                JsfUti.messageError(null, Messages.campoVacio, "");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultaEspecifica() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date limite = sdf.parse(Constantes.fechaInicioInscripciones);
            if (hastaConsEsp != null) {
                if (hastaConsEsp.before(limite)) {
                    limite = hastaConsEsp;
                } else {
                    limite = Utils.sumarRestarDiasFecha(hastaConsEsp, 1);
                }
            } else {
                limite = reg.getFechaInscripcionMayor();
            }
            JsfUti.executeJS("PF('dtMovsConsultas').clearFilters();");
            lazyMovs = new RegMovimientosLazy(libroConsEsp, actoConsEsp, inscripcionConsEsp,
                    repertorioConsEsp, folioConsEsp, tomoConsEsp,
                    desdeConsEsp != null ? desdeConsEsp : reg.getFechaInscripcionMenor(), limite);
            showDataTable = 1;
            JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
            JsfUti.executeJS("PF('consultaEspecifica').hide();");
        } catch (ParseException e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscarClientes() {
        try {
            if (documento.isEmpty() && nombre.isEmpty() && desde == null && hasta == null
                    && contrato.isEmpty() && repertorio == null && anio == null && inscripcion == null) {
                JsfUti.messageWarning(null, "Debe ingresar un criterio de busqueda.", "");
            } else {
                mcl = new RegMovimientoClienteLazy(documento, nombre, desde, hasta, contrato, repertorio, anio, inscripcion);
                mcl.setTipo(tipo);
                JsfUti.update("mainForm:tabConsultas:dtIndices");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscarClientes1() {
        try {
            if (nombre1.isEmpty() && repertorio1 == null && anio1 == null && inscripcion1 == null) {
                JsfUti.messageWarning(null, "Debe ingresar un criterio de busqueda.", "");
            } else {
                movimiento1 = new RegMovimiento();
                movimientosLazy = new RegMovimientosLazy();
                //movimientosLazy.setTipo1(tipo1);
                Map<String, Object> fill = new HashMap<>();
                if (nombre1 != null) {
                    fill.put("nombre", nombre1);
                }
                if (anio1 != null) {
                    fill.put("anioIns", anio1);
                }
                if (inscripcion1 != null) {
                    fill.put("numInscripcion", inscripcion1);
                }
                if (repertorio1 != null) {
                    fill.put("numRepertorio", repertorio1);
                }
                movimientosLazy.setFilterss(fill);
                JsfUti.update("mainForm:tabConsultas:dtBusIndices");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<RegMovimientoReferencia> listReferencia(Long idMov) {
        return this.reg.getRegMovRefByIdRegMov(idMov);
    }

    public void showDlgDocumentos(RegMovimiento mov) {
        movimiento = mov;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void imprimirInscripcion(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.agregarParametro("P_MOVIMIENTO", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.setNombreReporte("ActaInscripcion");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirRazon(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.agregarParametro("ID_MOV", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.setNombreReporte("RazonInscripcion");
            ss.setNombreSubCarpeta("registro");
            ss.setTieneDatasource(true);
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void generarRazonGrupal(RegMovimiento mov) {
        Calendar cl = Calendar.getInstance();
        if (mov.getFechaRepertorio() != null) {
            cl.setTime(mov.getFechaRepertorio());
        } else {
            cl.setTime(mov.getFechaInscripcion());
        }
        Integer year = cl.get(Calendar.YEAR);
        try {
            ss.instanciarParametros();
            ss.agregarParametro("REPERTORIO", mov.getNumRepertorio());
            ss.agregarParametro("ANIO", year.toString());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("PROPIEDAD", mov.getLibro().getPropiedad());
            ss.setNombreReporte("RazonInscripcion_v1");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgIndice() {
        if (rmc != null) {
            this.showDlgMovSelect(rmc.getMovimiento());
        }
    }

    public void reporteIndices() {
        try {
            if (mcl != null) {
                //List<IndiceProp> list = reg.llenarModeloIndice(mcl.getList());
                List<IndiceRegistro> list = reg.llenarModeloIndiceRegistro(mcl.getWrappedData());
                if (!list.isEmpty()) {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(Boolean.FALSE);
                    ss.setNombreSubCarpeta("registro");
                    //ss.setNombreReporte("Indices");
                    ss.setNombreReporte("Indicesv2");
                    ss.agregarParametro("USER", session.getName_user());
                    ss.setDataSource(list);
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                } else {
                    JsfUti.messageWarning(null, "La consulta no muestra resultados.", "");
                }
            } else {
                JsfUti.messageWarning(null, "La consulta no muestra resultados.", "");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String getPapelByMovimientoInterviniente(Long mov, Long inter) {
        return reg.getPapelByMovimientoInterviniente(mov, inter);
    }

    public String getPapelByMovAndCodInterv(Long mov, String doc) {
        return reg.getPapelByMovAndDocumentoInterv(mov, doc);
    }

    public List<RegLibro> getLibros() {
        return manager.findAll(Querys.getRegLibroList);
    }

    public String referencia(RegMovimiento mov) {
        /*if (mov.getLibro().getTipo() == 1) {
            return mov.getObservacion();
        } else {*/
        List<RegMovimientoReferencia> regMovRefByIdRegMov = this.reg.getRegMovRefByIdRegMov(mov.getId());
        if (Utils.isNotEmpty(regMovRefByIdRegMov)) {
            StringBuilder buffer = new StringBuilder();
            for (RegMovimientoReferencia refM : regMovRefByIdRegMov) {
                buffer.append(new SimpleDateFormat("dd/MM/yyyy").format(refM.getMovimientoReferencia().getFechaInscripcion()))
                        .append("-")
                        .append(Utils.completarCadenaConCeros(refM.getMovimientoReferencia().getNumInscripcion() + "", 6));
                buffer.append(" : ");
            }
            return buffer.substring(0, buffer.length() - 2);
//                return buffer.toString();
        }
        return null;
        //}
    }

    public String getFihasIndice(Long mov) {
        String result = "";
        try {
            List<RegFicha> temp = reg.getRegFichaByIdRegMov(mov);
            for (RegFicha f : temp) {
                result = result + ", " + f.getNumFicha();
            }
            if (result.isEmpty()) {
                return result;
            } else {
                return result.substring(1, result.length());
            }
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    public void visualizaScann() {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropImages.xhtml?id=" + movimiento.getId() + "&propiedad=" + movimiento.getLibro().getPropiedad()
                    + "&repertorio=" + movimiento.getNumRepertorio() + "&inscripcion=" + movimiento.getNumInscripcion()
                    + "&libro=" + movimiento.getLibro().getCodigoAnterior() + "&fecha=" + movimiento.getFechaInscripcion().getTime());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void integracionYura() {
        try {
            /*if (movimiento.getDigitalizacion() == null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/consultaDocs.xhtml?repertorio=" + movimiento.getNumRepertorio()
                        + "&fecha=" + movimiento.getFechaInscripcion().getTime());
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + movimiento.getDigitalizacion()
                        + "&name=" + movimiento.getDigitalizacion() + ".pdf&tipo=1&content=application/pdf");
            }*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann(RegMovimiento mov) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropImages.xhtml?id=" + mov.getId() + "&propiedad=" + mov.getLibro().getPropiedad()
                    + "&repertorio=" + mov.getNumRepertorio() + "&inscripcion=" + mov.getNumInscripcion()
                    + "&libro=" + mov.getLibro().getCodigoAnterior() + "&fecha=" + mov.getFechaInscripcion().getTime());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void integracionYura(RegMovimiento mov) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        try {
            /*if (mov.getDigitalizacion() == null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/consultaDocs.xhtml?repertorio=" + mov.getNumRepertorio()
                        + "&fecha=" + mov.getFechaInscripcion().getTime());
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + mov.getDigitalizacion()
                        + "&name=" + movimiento.getDigitalizacion() + ".pdf&tipo=1&content=application/pdf");
            }*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirFichaRegistral(RegFicha ficha) {
        try {
            if (ficha.getEstado().getValor().equalsIgnoreCase("INACTIVO")) {
                JsfUti.messageError(null, "No se imprime Ficha Registral, estado de Ficha: INACTIVA.", "");
            } else {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                if (ficha.getTipoFicha().getCodigo() < 1) {
                    ss.setNombreReporte("FichaRegistral");
                } else {
                    ss.setNombreReporte("FichaMercantil");
                }
                ss.agregarParametro("ID_FICHA", ficha.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("USER_NAME", session.getName_user());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_verificacion.png"));
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificadoBorrador(RegCertificado ce) {
        try {
            this.llenarParametros(ce);
            switch (ce.getTipoDocumento()) {
                case "C01": //NO POSEER BIENES
                    ss.setNombreReporte("CertificadoNoBienes");
                    break;
                /*case "C02": //CERTIFICADO DE SOLVENCIA
                    ss.setNombreReporte("CertificadoSolvencia");
                    break;*/
                case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                    ss.setNombreReporte("CertificadoHistoriaDominio");
                    break;
                /*case "C04": //CERTIFICADO DE FICHA PERSONAL (MERCANTIL)
                    ss.setNombreReporte("CertificadoFichaPersonal");
                    break;*/
                case "C05": //CERTIFICADO MERCANTIL 
                    ss.setNombreReporte("CertificadoMercantil");
                    break;
                case "C07": //CERTIFICADO GENERAL 
                    ss.setNombreReporte("CertificadoGeneral");
                    break;
                default:
                    JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
                    return;
            }
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGger.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null && id > 0L) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOGger.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
    }

    public void selectObjectActo(SelectEvent event) {
        this.actoConsEsp = (RegActo) event.getObject();
    }

    public void initActo() {
        this.actoConsEsp = new RegActo();
    }

    public RegEnteInterviniente getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(RegEnteInterviniente interviniente) {
        this.interviniente = interviniente;
    }

    public Integer getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(Integer tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public String getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(String valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public RegEnteIntervinienteLazy getIntervinientesLazy() {
        return intervinientesLazy;
    }

    public void setIntervinientesLazy(RegEnteIntervinienteLazy intervinientesLazy) {
        this.intervinientesLazy = intervinientesLazy;
    }

    public List<RegFicha> getFichas() {
        return fichas;
    }

    public void setFichas(List<RegFicha> fichas) {
        this.fichas = fichas;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<RegMovimientoFicha> getMovimientosFichas() {
        return movimientosFichas;
    }

    public void setMovimientosFichas(List<RegMovimientoFicha> movimientosFichas) {
        this.movimientosFichas = movimientosFichas;
    }

    public List<RegMovimientoCliente> getMovimientosInterv() {
        return movimientosInterv;
    }

    public void setMovimientosInterv(List<RegMovimientoCliente> movimientosInterv) {
        this.movimientosInterv = movimientosInterv;
    }

    public List<RegFicha> getFichasInterv() {
        return fichasInterv;
    }

    public void setFichasInterv(List<RegFicha> fichasInterv) {
        this.fichasInterv = fichasInterv;
    }

    public List<RegMovimientoCliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<RegMovimientoCliente> clientes) {
        this.clientes = clientes;
    }

    public List<RegMovimientoCapital> getListMovcap() {
        return listMovcap;
    }

    public void setListMovcap(List<RegMovimientoCapital> listMovcap) {
        this.listMovcap = listMovcap;
    }

    public List<RegMovimientoRepresentante> getListMovRep() {
        return listMovRep;
    }

    public void setListMovRep(List<RegMovimientoRepresentante> listMovRep) {
        this.listMovRep = listMovRep;
    }

    public List<RegMovimientoSocios> getListMovSoc() {
        return listMovSoc;
    }

    public void setListMovSoc(List<RegMovimientoSocios> listMovSoc) {
        this.listMovSoc = listMovSoc;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public RegMovimientosLazy getMovimientosLazy() {
        return movimientosLazy;
    }

    public void setMovimientosLazy(RegMovimientosLazy movimientosLazy) {
        this.movimientosLazy = movimientosLazy;
    }

    public RegFichaLazy getFichasLazy() {
        return fichasLazy;
    }

    public void setFichasLazy(RegFichaLazy fichasLazy) {
        this.fichasLazy = fichasLazy;
    }

    public RegMovimientosLazy getLazyMovs() {
        return lazyMovs;
    }

    public void setLazyMovs(RegMovimientosLazy lazyMovs) {
        this.lazyMovs = lazyMovs;
    }

    public RegFichaLazy getLazyFichas() {
        return lazyFichas;
    }

    public void setLazyFichas(RegFichaLazy lazyFichas) {
        this.lazyFichas = lazyFichas;
    }

    public Boolean getRealizarTarea() {
        return realizarTarea;
    }

    public void setRealizarTarea(Boolean realizarTarea) {
        this.realizarTarea = realizarTarea;
    }

    public Boolean getShowInterv() {
        return showInterv;
    }

    public void setShowInterv(Boolean showInterv) {
        this.showInterv = showInterv;
    }

    public Boolean getShowBtn() {
        return showBtn;
    }

    public void setShowBtn(Boolean showBtn) {
        this.showBtn = showBtn;
    }

    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public Integer getCantMovs() {
        return cantMovs;
    }

    public void setCantMovs(Integer cantMovs) {
        this.cantMovs = cantMovs;
    }

    public Integer getCantFich() {
        return cantFich;
    }

    public void setCantFich(Integer cantFich) {
        this.cantFich = cantFich;
    }

    public RegEnteInterviniente getSelect() {
        return select;
    }

    public void setSelect(RegEnteInterviniente select) {
        this.select = select;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
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

    public RegLibro getLibroConsEsp() {
        return libroConsEsp;
    }

    public void setLibroConsEsp(RegLibro libroConsEsp) {
        this.libroConsEsp = libroConsEsp;
    }

    public Long getInscripcionConsEsp() {
        return inscripcionConsEsp;
    }

    public void setInscripcionConsEsp(Long inscripcionConsEsp) {
        this.inscripcionConsEsp = inscripcionConsEsp;
    }

    public Long getRepertorioConsEsp() {
        return repertorioConsEsp;
    }

    public void setRepertorioConsEsp(Long repertorioConsEsp) {
        this.repertorioConsEsp = repertorioConsEsp;
    }

    public Date getDesdeConsEsp() {
        return desdeConsEsp;
    }

    public void setDesdeConsEsp(Date desdeConsEsp) {
        this.desdeConsEsp = desdeConsEsp;
    }

    public Date getHastaConsEsp() {
        return hastaConsEsp;
    }

    public void setHastaConsEsp(Date hastaConsEsp) {
        this.hastaConsEsp = hastaConsEsp;
    }

    public RegActo getActoConsEsp() {
        return actoConsEsp;
    }

    public void setActoConsEsp(RegActo actoConsEsp) {
        this.actoConsEsp = actoConsEsp;
    }

    public List<RegMovimiento> getMovimientosSeleccionados() {
        return movimientosSeleccionados;
    }

    public void setMovimientosSeleccionados(List<RegMovimiento> movimientosSeleccionados) {
        this.movimientosSeleccionados = movimientosSeleccionados;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<RegMovimiento> getListMovs() {
        return listMovs;
    }

    public void setListMovs(List<RegMovimiento> listMovs) {
        this.listMovs = listMovs;
    }

    public RegMovimiento getMovSelec() {
        return movSelec;
    }

    public void setMovSelec(RegMovimiento movSelec) {
        this.movSelec = movSelec;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public List<RegEnteInterviniente> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegEnteInterviniente> propietarios) {
        this.propietarios = propietarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public RegMovimientoClienteLazy getMcl() {
        return mcl;
    }

    public void setMcl(RegMovimientoClienteLazy mcl) {
        this.mcl = mcl;
    }

    public RegMovimientoCliente getRmc() {
        return rmc;
    }

    public void setRmc(RegMovimientoCliente rmc) {
        this.rmc = rmc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public String getContratante() {
        return contratante;
    }

    public void setContratante(String contratante) {
        this.contratante = contratante;
    }

    public List<RegMovimientoMarginacion> getMarginaciones() {
        return marginaciones;
    }

    public void setMarginaciones(List<RegMovimientoMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }

    public List<TbBlob> getBlobs() {
        return blobs;
    }

    public void setBlobs(List<TbBlob> blobs) {
        this.blobs = blobs;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getInscripcion1() {
        return inscripcion1;
    }

    public void setInscripcion1(Integer inscripcion1) {
        this.inscripcion1 = inscripcion1;
    }

    public Integer getRepertorio1() {
        return repertorio1;
    }

    public void setRepertorio1(Integer repertorio1) {
        this.repertorio1 = repertorio1;
    }

    public Integer getAnio1() {
        return anio1;
    }

    public void setAnio1(Integer anio1) {
        this.anio1 = anio1;
    }

    public Integer getTipo1() {
        return tipo1;
    }

    public void setTipo1(Integer tipo1) {
        this.tipo1 = tipo1;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public RegMovimiento getMovimiento1() {
        return movimiento1;
    }

    public void setMovimiento1(RegMovimiento movimiento1) {
        this.movimiento1 = movimiento1;
    }

    public Integer getFolioConsEsp() {
        return folioConsEsp;
    }

    public void setFolioConsEsp(Integer folioConsEsp) {
        this.folioConsEsp = folioConsEsp;
    }

    public String getTomoConsEsp() {
        return tomoConsEsp;
    }

    public void setTomoConsEsp(String tomoConsEsp) {
        this.tomoConsEsp = tomoConsEsp;
    }

    public Integer getShowDataTable() {
        return showDataTable;
    }

    public void setShowDataTable(Integer showDataTable) {
        this.showDataTable = showDataTable;
    }

    public RegCertificadosLazy getCertificadosLazy() {
        return certificadosLazy;
    }

    public void setCertificadosLazy(RegCertificadosLazy certificadosLazy) {
        this.certificadosLazy = certificadosLazy;
    }

}
