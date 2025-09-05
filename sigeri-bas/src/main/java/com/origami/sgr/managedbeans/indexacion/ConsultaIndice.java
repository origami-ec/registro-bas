/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.indexacion;

import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.services.DocumentalService;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CtlgCargo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegEstadoMovimiento;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.lazymodels.RegEnteIntervinienteLazy;
import com.origami.sgr.lazymodels.RegFichaLazy;
import com.origami.sgr.lazymodels.RegMovimientoClienteLazy;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.MovimientoModel;
import com.origami.sgr.services.interfaces.ArchivosService;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.Constantes;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.CroppedImage;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ConsultaIndice implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConsultaIndice.class.getName());

    @Inject
    protected DocumentalService doc;
    @Inject
    protected ArchivosService arc;
    @Inject
    protected Entitymanager em;
    @Inject
    protected ServletSession ss;
    @Inject
    protected UserSession us;
    @Inject
    protected RegistroPropiedadServices reg;

    protected List<RegMovimiento> listMovs = new ArrayList<>();
    protected RegMovimiento movSelec = new RegMovimiento();
    protected Long numeroTramite;
    protected List<RegEnteInterviniente> propietarios = new ArrayList<>();
    protected List<RegMovimientoCliente> movimientosInterv = new ArrayList<>();
    protected List<RegFicha> fichasInterv = new ArrayList<>();
    protected RegEnteIntervinienteLazy intervinientesLazy;
    protected RegMovimiento movimiento;
    protected MovimientoModel mm;
    protected RegFicha ficha;
    protected RegMovimientoMarginacion marg;
    protected RegMovimientoCliente interv;

    protected List<RegMovimientoCliente> clientes = new ArrayList<>();
    protected List<RegFicha> fichas = new ArrayList<>();
    protected List<RegMovimientoFicha> movimientosFichas = new ArrayList<>();
    protected List<RegMovimientoMarginacion> marginaciones = new ArrayList<>();

    protected String linderos = "";
    protected RegMovimientosLazy movimientosLazy;
    protected RegFichaLazy fichasLazy;

    protected Integer tipoConsulta = 1;
    protected String valorConsulta = "";
    protected Boolean showMovs = false;
    protected Boolean showFichas = false;
    protected RegMovimientosLazy lazyMovs;
    protected RegFichaLazy lazyFichas;

    protected Boolean realizarTarea = false;
    protected Boolean showInterv = false;
    protected Boolean showBtn = false;
    protected Long idTarea;
    protected RegEnteInterviniente interviniente;

    protected RegEnteInterviniente select = new RegEnteInterviniente();
    protected Integer cantMovs = 0;
    protected Integer cantFich = 0;
    protected Integer tabIndex = 0;

    protected String cadena = "";
    protected Date desde;
    protected Date hasta;

    protected RegLibro libroConsEsp;
    protected RegActo actoConsEsp;
    protected Long inscripcionConsEsp;
    protected Long repertorioConsEsp;
    protected Date desdeConsEsp;
    protected Date hastaConsEsp;
    protected List<RegActo> listActos;
    protected List<RegMovimiento> movimientosSeleccionados;
    protected String urlDownload = "";
    protected Integer anio;
    protected Calendar cal = Calendar.getInstance();

    protected RegMovimientoCliente rmc;
    protected RegMovimientoClienteLazy mcl;
    protected String nombre, apellido, documento, contrato, contratante;
    protected Integer inscripcion, repertorio;
    protected List<File> tempFiles;
    protected CroppedImage croppedImage;
    protected Long idBLob;

    protected ArchivoDocs archivo;
    protected Integer pagina = 0;
    protected Integer indice = 0;
    protected String textAux;
    protected Boolean enableOCR = false;
    protected Map map;

    @PostConstruct
    protected void iniView() {
        try {
            interv = new RegMovimientoCliente();
            intervinientesLazy = new RegEnteIntervinienteLazy();
            archivo = new ArchivoDocs();
            movimiento = new RegMovimiento();
            movimiento.setEnteJudicial(new RegEnteJudiciales());
            if (ss.tieneParametro("movimientoId")) {
                Long movimientoId = (Long) ss.retornarValor("movimientoId");
                movimiento = em.find(RegMovimiento.class, movimientoId);
                if (movimiento != null) {
                    fichas = reg.getRegFichaByIdRegMov(movimiento.getId());
                    clientes = reg.getRegMovClienteByIdMov(movimiento.getId());
                    marginaciones = reg.getRegMovMargByIdMov(movimiento.getId());
                    mm = new MovimientoModel(movimiento, null);
                    if (movimiento.getApp() != null) {
                        archivo = doc.consultarArchivo(new ArchivoDocs(movimiento.getApp()));
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegActo> complete(String query) {//////////////mejorar consulta
        List<RegActo> listaActos = em.findAll(Querys.getRegActoList);
        listActos = new ArrayList<>();
        if (!query.equals("*")) {
            for (RegActo a : listaActos) {
                if (a.getAbreviatura().toUpperCase().contains(query.toUpperCase())) {
                    listActos.add(a);
                }
                if (a.getNombre().toUpperCase().contains(query.toUpperCase())) {
                    listActos.add(a);
                }
            }
            if (listActos.size() >= 10) {
                return listActos.subList(0, 10);
            }
        } else {
            listActos.addAll(listaActos.subList(0, 10));
        }
        return listActos;
    }

    public void imprimirInscripcionesPorFecha() {
        try {
            if (hasta.after(desde) || hasta.equals(desde)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String strDesde = sdf.format(desde);
                String strHasta = sdf.format(hasta);
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("RegConsultaEspecificaFechas");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("DESDE", sdf.parse(strDesde));
                ss.agregarParametro("HASTA", sdf.parse(strHasta));
                ss.agregarParametro("USERNAME", us.getName_user());
                JsfUti.redirectNewTab("/sgr/Documento");
            } else {
                JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
            }
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
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
            ss.setNombreReporte("RegConsultaEspecificaPersona");
            ss.setNombreSubCarpeta("registro");
            //servletSession.agregarParametro("ENTE", interv.getId());
            ss.agregarParametro("ENTE", interv.getCedRuc());
            ss.agregarParametro("NOMBRE", interv.getNombre());
            ss.agregarParametro("USERNAME", us.getName_user());
            ss.agregarParametro("TOTAL", "TOTAL   :   " + i + "  Movimientos.");
            JsfUti.redirectNewTab("/sgr/Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
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
            JsfUti.redirectNewTab("/sgr/Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacoraFicha() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", null);
            ss.agregarParametro("numFicha", ficha.getNumFicha());
            ss.agregarParametro("titulo", Messages.bitacoraFicha);
            JsfUti.redirectNewTab("/sgr/Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
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
            ss.setNombreReporte("RegConsultaEspecificaPersona");
            ss.setNombreSubCarpeta("registroPropiedad");
            ss.agregarParametro("ENTE", interviniente.getId());
            ss.agregarParametro("USERNAME", us.getName_user());
            ss.agregarParametro("TOTAL", "TOTAL   :   " + i + "  Movimientos.");
            JsfUti.redirectNewTab("/sgr/Documento");
            showBtn = true;
            JsfUti.update("mainForm:panelInterviniente");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirInscripcionesEspecificas() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("RegConsultaEspecifica");
            ss.setNombreSubCarpeta("registro");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date limite = sdf.parse("01-01-2017");
            Date hasta2016 = hastaConsEsp;
            //ESTA FECHA ESTA QUEMADA EN EL CODIGO POR QUE
            //DESDE AQUI LAS INSCRIPCIONES SE HICIERON EN RPP
            if (hastaConsEsp != null) {
                if (hastaConsEsp.before(limite)) {
                    limite = hastaConsEsp;
                } else {
                    limite = Utils.sumarRestarDiasFecha(hastaConsEsp, 1);
                }
            } else {
                limite = reg.getFechaInscripcionMayor();
                hasta2016 = limite;
            }
            ss.agregarParametro("LIBRO", libroConsEsp != null ? libroConsEsp.getId() : 0L);
            ss.agregarParametro("LIBRO_NAME", libroConsEsp != null ? libroConsEsp.getNombre() : "TODOS LOS LIBROS");
            ss.agregarParametro("ACTO", actoConsEsp != null ? actoConsEsp.getId() : 0L);
            ss.agregarParametro("ACTO_NAME", actoConsEsp != null ? actoConsEsp.getAbreviatura() + " | " + actoConsEsp.getNombre() : "TODOS LOS ACTOS");
            ss.agregarParametro("INSCRIPCION", inscripcionConsEsp != null ? inscripcionConsEsp : 0L);
            ss.agregarParametro("REPERTORIO", repertorioConsEsp != null ? repertorioConsEsp : 0L);
            ss.agregarParametro("DESDE", desdeConsEsp != null ? desdeConsEsp : reg.getFechaInscripcionMenor());
            ss.agregarParametro("HASTA", limite);
            ss.agregarParametro("HASTA2016", hasta2016);
            ss.agregarParametro("USERNAME", us.getName_user());
            JsfUti.redirectNewTab("/sgr/Documento");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultaInterviniente(RegEnteInterviniente interv) {
        try {
            select = interv;
            cantMovs = 0;
            cantFich = 0;
            //Collection col1 = reg.getListIdMovsByInterv(interv.getId());
            Collection col1 = reg.getListIdMovsByCedRucInterv(interv.getCedRuc());
            if (col1 != null) {
                if (!col1.isEmpty()) {
                    movimientosLazy = new RegMovimientosLazy(col1);
                    //Collection col2 = reg.getListIdFichasByInterv(interv.getId());
                    /*Collection col2 = reg.getListIdFichasByDocInterv(interv.getCedRuc());
                    if (col2.isEmpty()) {
                        fichasLazy = null;
                    } else {
                        fichasLazy = new RegFichaLazy(col2);
                    }*/
                    if (interv.getCedRuc() != null) {
                        fichasLazy = new RegFichaLazy(interv.getCedRuc(), 1);
                    } else {
                        fichasLazy = null;
                    }
                    cantMovs = col1.size();
                    //cantFich = col2.size();
                    JsfUti.update("formConsulta");
                    JsfUti.executeJS("PF('dlgConsultaInterv').show();");
                } else {
                    JsfUti.messageInfo(null, "El cliente no registra Movimientos.", "");
                }
            } else {
                JsfUti.messageInfo(null, "El cliente no registra Movimientos.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgBienes(RegEnteInterviniente interv) {
        try {
            select = interv;
            fichasInterv = reg.getBienesByPropietario(interv.getId());
            if (fichasInterv.isEmpty()) {
                JsfUti.messageInfo(null, "El cliente no registra propiedades.", "");
            } else {
                JsfUti.update("formFichasInterviniente");
                JsfUti.executeJS("PF('dlgFichasInterviniente').show();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
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
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void detalleMovimiento(RegMovimiento movi) {
        try {
            movSelec = movi;
            JsfUti.update("formIndicesDet");
            JsfUti.executeJS("PF('consultaIndicesDet').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultaIndiceByTramite() {
        try {
            if (Utils.isNum(valorConsulta)) {
                numeroTramite = new Long(valorConsulta);
                //numeroTramite = Long.getLong(valorConsulta);
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
            Logger.getLogger(ConsultaIndice.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            if (movimiento.getEnteJudicial() == null) {
                movimiento.setEnteJudicial(new RegEnteJudiciales());
            }
            if (movimiento.getDomicilio() == null) {
                movimiento.setDomicilio(new RegDomicilio());
            }
            movimiento.setRegMovimientoReferenciaCollection(em.findAll(Querys.getRegMovimientoReferenciaByIdMov,
                    new String[]{"idmov"}, new Object[]{movimiento.getId()}));
            fichas = reg.getRegFichaByIdRegMov(mov.getId());
            clientes = reg.getRegMovClienteByIdMov(mov.getId());
            marginaciones = reg.getRegMovMargByIdMov(mov.getId());
            mm = new MovimientoModel(movimiento, null);
            this.setTabIndex(1);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovCliSelect(RegMovimientoCliente cliente) {
        try {
            this.rmc = cliente;
            this.showDlgMovSelect(cliente.getMovimiento());
            if (rmc != null) {

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectInterv(SelectEvent event) {
        RegEnteInterviniente in = (RegEnteInterviniente) event.getObject();
        if (this.containsInterviniente(in.getCedRuc())) {
            JsfUti.messageError(null, Messages.elementoRepetido, "");
        } else {
            RegMovimientoCliente mc = new RegMovimientoCliente();
            mc.setEnteInterv(in);
            clientes.add(mc);
        }
    }

    public boolean containsInterviniente(String cedRuc) {
        for (RegMovimientoCliente mc : clientes) {
            if (mc.getEnteInterv().getCedRuc().equals(cedRuc)) {
                return true;
            }
        }
        return false;
    }

    public void guardarMovimiento() {
        try {
            if (movimiento.getEnteJudicial() == null || movimiento.getEnteJudicial().getId() == null) {
                JsfUti.messageError(null, Messages.camposObligatorios, "Not./Juz");
                return;
            }
            if (movimiento.getDomicilio() != null && movimiento.getDomicilio().getId() == null) {
                movimiento.setDomicilio(null);
            }
            if (movimiento.getFechaInscripcion() != null && movimiento.getLibro() != null && movimiento.getNumRepertorio() != null) {
                if (movimiento.getNumInscripcion() == null) {
                    movimiento.setNumInscripcion(0);
                }
                if (movimiento.getNumRepertorio() == null) {
                    movimiento.setNumRepertorio(0);
                }
                movimiento.setFechaMod(new Date());
                movimiento.setUsuarioMod(us.getUserId().intValue());
                movimiento.setEstado("AC");
                Collection<RegMovimientoReferencia> regMovimientoReferenciaCollection = movimiento.getRegMovimientoReferenciaCollection();
                movimiento.setRegMovimientoClienteCollection(null);
                movimiento.setRegMovimientoReferenciaCollection(null);
                movimiento = reg.guardarInscripcionEdicion(movimiento,
                        (List<RegMovimientoReferencia>) regMovimientoReferenciaCollection,
                        (List<RegMovimientoFicha>) movimiento.getRegMovimientoFichaCollection(),
                        clientes, marginaciones, mm);
                if (movimiento != null && movimiento.getId() != null) {
                    JsfUti.messageInfo(null, Messages.transaccionOK, "");
                } else {
                    JsfUti.messageInfo(null, Messages.errorTransaccion, "");
                }
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNewMarginacion() {
        marg = new RegMovimientoMarginacion();
        JsfUti.update("formMarginacion");
        JsfUti.executeJS("PF('dlgMarginacion').show();");
    }

    public void agregarMarginacion() {
        if (marg.getObservacion() != null) {
            marg.setMovimiento(movimiento);
            marg.setFechaIngreso(new Date());
            marg.setUserIngreso(us.getUserId());
            marginaciones.add(marg);
            JsfUti.update(":mainForm:tabConsultas:tbvDatosMov:dtMarginacion");
            JsfUti.executeJS("PF('dlgMarginacion').hide();");
        } else {
            JsfUti.messageWarning(null, "Debe ingresar contenido de texto a la marginacion.", "");
        }
    }

    public void visualizaScann() {
        try {
            if (movimiento.getId() != null) {
                //String ruta = reg.rutaDocumentoInscripcion(movimiento);
                String ruta = "";
                if (ruta != null) {
                    ss.instanciarParametros();
                    ss.setNombreDocumento(ruta);
                    JsfUti.redirectNewTab("/sgr/DownLoadFiles");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showLinderos(RegFicha f) {
        linderos = f.getLinderos();
    }

    public void showDlgFichaSelect(RegFicha f) {
        try {
            ficha = f;
            //ficha.setDescripcionTemp(f.getObsvEstado(f.getEstado()));
            movimientosFichas = reg.getRegMovByIdFicha(f.getId());
            propietarios = reg.getPropietariosByFicha(f.getId());
            JsfUti.update("formFichaSelect");
            JsfUti.executeJS("PF('dlgFichaSelect').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void otrasConsultas() {
        try {
            switch (tipoConsulta) {
                case 1:
                    if (this.validaCampo()) {
                        lazyFichas = new RegFichaLazy(valorConsulta, 1);
                        showMovs = false;
                        showFichas = true;
                        JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                    }
                    break;
                case 2:
                    if (this.validaCampo()) {
                        if (Utils.validateNumberPattern(valorConsulta)) {
                            lazyFichas = new RegFichaLazy(valorConsulta, 2);
                            showMovs = false;
                            showFichas = true;
                            JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                        } else {
                            JsfUti.messageError(null, "Solo debe ingresar numeros.", "");
                        }
                    }

                    break;
                case 3:
                    if (this.validaCampo()) {
                        if (Utils.validateNumberPattern(valorConsulta)) {
                            lazyMovs = new RegMovimientosLazy(valorConsulta, 1);
                            showMovs = true;
                            showFichas = false;
                            JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                        } else {
                            JsfUti.messageError(null, "Solo debe ingresar numeros.", "");
                        }
                    }
                    break;
                case 4:
                    if (this.validaCampo()) {
                        if (Utils.validateNumberPattern(valorConsulta)) {
                            lazyMovs = new RegMovimientosLazy(valorConsulta, 2);
                            showMovs = true;
                            showFichas = false;
                            JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                        } else {
                            JsfUti.messageError(null, "Solo debe ingresar numeros.", "");
                        }
                    }
                    break;
                case 5:
                    JsfUti.update("formFechas");
                    JsfUti.executeJS("PF('consultaFechas').show();");
                    break;
                case 6:
                    if (this.validaCampo()) {
                        //lazyFichas = new RegFichaLazy("numFicha", valorConsulta, 6);
                        lazyFichas = new RegFichaLazy();
                        showMovs = false;
                        showFichas = true;
                        JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                    }
                    break;
                case 7:
                    JsfUti.executeJS("PF('consultaEspecifica').show();");
                    break;
                case 8:
                    this.consultaIndiceByTramite();
                    break;
                default:
                    showMovs = false;
                    showFichas = false;
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaCampo() {
        if (valorConsulta == null) {
            JsfUti.messageError(null, Messages.campoVacio, "");
            return false;
        } else {
            return true;
        }
    }

    public void buscarInscripFechas() {
        try {
            if (hasta.after(desde) || hasta.equals(desde)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date limite = sdf.parse("01-01-2017");
                //ESTA FECHA ESTA QUEMADA EN EL CODIGO POR QUE
                //DESDE AQUI LAS INSCRIPCIONES SE HICIERON EN RPP
                if (hasta.after(limite) || hasta.equals(limite)) {
                    limite = Utils.sumarRestarDiasFecha(hasta, 1);
                } else {
                    limite = hasta;
                }
                lazyMovs = new RegMovimientosLazy(desde, limite);
                showMovs = true;
                showFichas = false;
                JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
                JsfUti.executeJS("PF('consultaFechas').hide();");
            } else {
                JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
            }
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarInterv() {
        try {
            if (cadena != null) {
                intervinientesLazy = new RegEnteIntervinienteLazy(cadena);
            } else {
                intervinientesLazy = new RegEnteIntervinienteLazy();
                JsfUti.messageError(null, Messages.campoVacio, "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultaEspecifica() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date limite = sdf.parse("01-01-2017");
            //ESTA FECHA ESTA QUEMADA EN EL CODIGO POR QUE
            //DESDE AQUI LAS INSCRIPCIONES SE HICIERON EN RPP
            if (hastaConsEsp != null) {
                if (hastaConsEsp.before(limite)) {
                    limite = hastaConsEsp;
                } else {
                    limite = Utils.sumarRestarDiasFecha(hastaConsEsp, 1);
                }
            } else {
                limite = reg.getFechaInscripcionMayor();
            }
            lazyMovs = new RegMovimientosLazy(libroConsEsp, actoConsEsp, inscripcionConsEsp, repertorioConsEsp, desdeConsEsp != null ? desdeConsEsp : reg.getFechaInscripcionMenor(), limite);
            showMovs = true;
            showFichas = false;
            JsfUti.update("mainForm:tabConsultas:pnlGrpTables");
            JsfUti.executeJS("PF('consultaEspecifica').hide();");
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarClientes() {
        try {
            this.rmc = new RegMovimientoCliente();
            if (documento == null && nombre == null && desde == null && hasta == null
                    && contrato == null && repertorio == null && anio == null && inscripcion == null) {
                JsfUti.messageWarning(null, "Debe ingresar un criterio de busqueda.", "");
            } else {
                Date temp;
                if (hasta != null) {
                    temp = Utils.sumarRestarDiasFecha(hasta, 1);
                } else {
                    temp = null;
                }
                mcl = new RegMovimientoClienteLazy(documento, nombre, desde, temp, contrato, repertorio, anio, inscripcion);
                JsfUti.update("mainForm:tabConsultas:dtIndices");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgIndice() {
        if (rmc != null) {

        }
    }

    public void reporteIndices() {
        try {
            if (mcl != null) {
                /*List<IndiceProp> list = reg.llenarModeloIndice(mcl.getList());
                if (!list.isEmpty()) {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(Boolean.FALSE);
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("Indices");
                    ss.agregarParametro("USER", us.getName_user());
                    ss.setDataSource(list);
                    JsfUti.redirectNewTab("/sgr/Documento");
                } else {
                    JsfUti.messageWarning(null, "La consulta no muestra resultados.", "");
                }*/
            } else {
                JsfUti.messageWarning(null, "La consulta no muestra resultados.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editInterviniente(RegMovimientoCliente interv) {
        ss.instanciarParametros();
        ss.agregarParametro("interv", interv);
        ss.agregarParametro("acto", movimiento.getActo());
        this.showDlg("/resources/dialog/dlgEditEnteInterviniente");
    }
    
    public void showDlgEditInterv(int index) {
        interv = clientes.get(index);
        JsfUti.update("formEditInterv");
        JsfUti.executeJS("PF('dlgEditInter').show();");
    }

    public String getPapelByMovimientoInterviniente(Long mov, Long inter) {
        return reg.getPapelByMovimientoInterviniente(mov, inter);
    }

    public String getPapelByMovAndCodInterv(Long mov, String doc) {
        return reg.getPapelByMovAndDocumentoInterv(mov, doc);
    }

    public List<RegLibro> getLibros() {
        return em.findAll(Querys.getRegLibroList);
    }

    public List<RegDomicilio> getDomicilios() {
        return em.findAllEntCopy(Querys.getRegDomicilioList);
    }

    public void selectObject(SelectEvent event) {
        try {
            if (event.getObject() instanceof RegEnteJudiciales) {
                movimiento.setEnteJudicial((RegEnteJudiciales) event.getObject());
            } else if (event.getObject() instanceof RegMovimiento) {
                if (movimiento.getRegMovimientoReferenciaCollection() == null) {
                    movimiento.setRegMovimientoReferenciaCollection(new ArrayList<>());
                }
                RegMovimiento m = (RegMovimiento) event.getObject();
                if (this.containsReferencia(m.getId())) {
                    JsfUti.messageError(null, Messages.elementoRepetido, "");
                } else {
                    RegMovimientoReferencia mr = new RegMovimientoReferencia();
                    mr.setMovimientoReferencia(m);
                    mr.setMovimiento(movimiento.getId());
                    movimiento.getRegMovimientoReferenciaCollection().add(mr);
                }
            } else if (event.getObject() instanceof RegMovimientoCliente) {
                RegMovimientoCliente interv = (RegMovimientoCliente) event.getObject();
                if (interv.getDomicilio() != null && interv.getDomicilio().getId() == null) {
                    interv.setDomicilio(null);
                }
                if (interv.getEnteInterv() != null && interv.getEnteInterv().getNacionalidad() != null || interv.getEnteInterv().getNacionalidad().getId() != null) {
                    interv.getEnteInterv().setNacionalidad(null);
                }
                int indexOf = clientes.indexOf(interv);
                if (indexOf > -1) {
                    clientes.set(indexOf, interv);
                } else {
                    clientes.add(interv);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean containsReferencia(Long idmov) {
        for (RegMovimientoReferencia mr : movimiento.getRegMovimientoReferenciaCollection()) {
            if (Objects.equals(mr.getMovimientoReferencia().getId(), idmov)) {
                return true;
            }
        }
        return false;
    }

    public void selectObjectM(SelectEvent event) {
        if (event.getObject() instanceof RegDomicilio) {
            movimiento.setDomicilio((RegDomicilio) event.getObject());
        }
    }

    public Collection<RegPapel> getPapeles() {
        return em.findAllEntCopy(Querys.getRegPapelesList);
    }

    public String getFihasIndice(Long mov) {
        String result = "";
        try {
            List<RegFicha> temp = reg.getRegFichaByIdRegMov(mov);
            for (RegFicha f : temp) {
                result = result + "," + f.getNumFicha();
            }
            if (result.isEmpty()) {
                return result;
            } else {
                return result.substring(1, result.length());
            }
        } catch (Exception e) {
            return "";
        }
    }

    public void selectPage(PageEvent event) {
        try {
            pagina = event.getPage();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        try {
            String url = archivo.getImagenes().get(pagina).getUrlWebService();
            this.textAux = arc.getTextOfImage(url, e.getX1(), e.getX2(),
                    e.getY1(), e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            System.out.println(e);
        }
    }

    public void showDlg(String urlFacelet) {
        try {
            if (!urlFacelet.startsWith("/")) {
                urlFacelet = "/" + urlFacelet;
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "60%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void agregarListaIntervEdit() {
        if (interv.getPapel() == null) {
            JsfUti.messageInfo(null, "Debe seleccionar el papel.", "");
            return;
        }
        if (interv.getItem() != null) {
            interv.setEstado(interv.getItem().getValor());
        }
        if (interv.getCargo() != null) {
            interv.setEstado(interv.getCargo().getNombre());
        }
        JsfUti.update("mainForm:tbvDatosMov:dtInterviniente");
        JsfUti.executeJS("PF('dlgEditInter').hide();");
    }

    public List<RegEstadoMovimiento> getEstadosInscripcion() {
        return em.findAllEntCopy(Querys.getRegEstadosInscripcion);
    }
    
    public List<CtlgItem> getEstadosCivil() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        return em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }
    
    public List<CtlgCargo> getCtlgCargos() {
        return em.findAllEntCopy(Querys.CtlgTipocargoOrderByNombre);
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

    public Boolean getShowMovs() {
        return showMovs;
    }

    public void setShowMovs(Boolean showMovs) {
        this.showMovs = showMovs;
    }

    public Boolean getShowFichas() {
        return showFichas;
    }

    public void setShowFichas(Boolean showFichas) {
        this.showFichas = showFichas;
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

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public RegMovimientoMarginacion getMarg() {
        return marg;
    }

    public void setMarg(RegMovimientoMarginacion marg) {
        this.marg = marg;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

    public Boolean getEnableOCR() {
        return enableOCR;
    }

    public void setEnableOCR(Boolean enableOCR) {
        this.enableOCR = enableOCR;
    }

    public RegMovimientoCliente getInterv() {
        return interv;
    }

    public void setInterv(RegMovimientoCliente interv) {
        this.interv = interv;
    }

}
