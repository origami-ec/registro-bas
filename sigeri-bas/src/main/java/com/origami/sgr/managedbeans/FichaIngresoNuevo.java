/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.Barrios;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaMarginacion;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.entities.UafTipoBien;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.ConsultaMovimientoModel;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.FichaIngresoService;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import com.origami.sgr.util.WhereClause;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.origami.sgr.entities.CatParroquia;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class FichaIngresoNuevo extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(FichaIngresoNuevo.class.getName());

    @Inject
    private BitacoraServices bs;

    @Inject
    private FichaIngresoService fis;

    //protected PredioModel predioModel = new PredioModel();
    protected Boolean disable = false;
    protected RegFicha ficha;
    protected List<CtlgItem> estados = new ArrayList<>();
    protected List<CtlgItem> estadosInformacion = new ArrayList<>();
    protected List<RegFichaPropietarios> propietarios = new ArrayList<>();
    protected List<RegMovimientoFicha> movimientos = new ArrayList<>();
    //protected List<RegFichaMarginacion> marginaciones = new ArrayList<>();
    protected RegFichaMarginacion marg = new RegFichaMarginacion();
    protected ConsultaMovimientoModel modelo = new ConsultaMovimientoModel();
    //protected RegEnteInterviniente re = new RegEnteInterviniente();
    protected RegMovimiento movimiento = new RegMovimiento();
    protected List<RegMovimientoCliente> selects = new ArrayList<>();
    protected BigInteger periodo;
    protected Long tramite;
    protected List<UafTipoBien> tiposBien = new ArrayList<>();
    protected HistoricoTramites ht = new HistoricoTramites();
    protected String taskId;
    protected RegTipoFicha tipoFicha;
    protected RegEnteInterviniente persona = new RegEnteInterviniente();

    //VARIALBES DE DUPLICAR FICHA
    protected Long numeroFicha;
    protected RegFicha fichaMain;
    protected String linderos;
    protected String response;
    protected List<RegMovimiento> movsficha;
    protected Integer cantidad = 0;
    //private Boolean fichaReutilizable = false;
    //private Boolean duplicada = false;

    @PostConstruct
    public void initView() {
        try {
            fichaMain = new RegFicha();
            movsficha = new ArrayList<>();
            periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            ficha = new RegFicha();
            ficha.setTipoPredio("U");
            ficha.setEstado(new CtlgItem(11L)); //ESTADO POR DEFECTO ACTIVO
            //ficha.setParroquia(new CatParroquia(4L)); 
            map = new HashMap<>();
            map.put("catalogo", Constantes.estadoFicha);
            estados = manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
            map = new HashMap<>();
            tiposBien = manager.findNamedQuery(Querys.getTiposBienUaf, map);
            tipoFicha = manager.find(RegTipoFicha.class, 1L);
            if (ss.getParametros() != null) {
                /*DATOS DE EDICION DE FICHA*/
                if (ss.getParametros().get("idFicha") != null) {
                    Long idFicha = (Long) ss.getParametros().get("idFicha");
                    ficha = manager.find(RegFicha.class, idFicha);
                    this.cargarListasPersistidas();
                }
                if (ss.tieneParametro("tipoFicha")) {
                    Long tipo = (Long) ss.getParametros().get("tipoFicha");
                    tipoFicha = manager.find(RegTipoFicha.class, tipo);
                }
                if (ficha.getLinderos() == null || ficha.getLinderos().isEmpty()) {
                    if (tipoFicha.getId() == 1L) {
                        ficha.setLinderos(Constantes.contenidoFichaInmueble);
                    }
                    if (tipoFicha.getId() == 2L) {
                        ficha.setLinderos(Constantes.contenidoFichaMueble);
                    }
                }
            }
            ss.instanciarParametros();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void eliminarMovimiento(RegMovimiento m, int indice) {
        try {
            //if (m.getEditable()) {
            RegMovimientoFicha mf = movimientos.remove(indice);
            if (mf.getId() != null) {
                manager.delete(mf);
                bs.registrarFichaMov(ficha, mf.getMovimiento(), ActividadesTransaccionales.ELIMINAR_REFERENCIA, periodo);
            }
            /*} else {
                JsfUti.messageWarning(null, "NO puede eliminar este Movimiento.", "");
            }*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editarMovimiento(RegMovimiento m) {
        //if (m.getEditable()) {
        if (m.getTramite() == null) {
            ss.instanciarParametros();
            ss.agregarParametro("idMov", m.getId());
            if (ficha != null && ficha.getId() != null) {
                //manager.merge(ficha);
                //ficha = reg.guardarFichaRegistral(ficha, propietarios, movimientos, marginaciones);
                ficha = reg.guardarFichaRegistral(ficha, propietarios, movimientos, null);
                ss.agregarParametro("idFicha", ficha.getId());
                JsfUti.redirectFaces("/procesos/manage/inscripcionEdicionFicha.xhtml");
            } else {
                JsfUti.redirectFaces("/procesos/manage/inscripcionEdicion.xhtml");
            }
        } else {
            JsfUti.messageWarning(null, "NO puede editar esta Inscripcion.", "");
        }
    }

    public void verDetalleMovimiento(RegMovimiento mov) {
        try {
            modelo = reg.getConsultaMovimiento(mov.getId());
            if (modelo != null) {
                JsfUti.update("formMovRegSelec");
                JsfUti.executeJS("PF('dlgMovRegSelec').show();");
            } else {
                JsfUti.messageError(null, "No se pudo hacer la consulta.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectObject(SelectEvent event) {
        Boolean flag = true;
        //CatEnte ente = (CatEnte) event.getObject();
        RegEnteInterviniente ente = (RegEnteInterviniente) event.getObject();
        for (RegFichaPropietarios p : propietarios) {
            if (p.getPropietario().getCedRuc().equalsIgnoreCase(ente.getCedRuc())) {
                flag = false;
                JsfUti.messageInfo(null, Messages.elementoRepetido, "");
                break;
            }
        }
        if (flag) {
            RegFichaPropietarios prop = new RegFichaPropietarios();
            //prop.setEnte(ente);
            prop.setPropietario(ente);
            prop.setEstado("A");
            prop.setUserIngreso(session.getName_user());
            prop.setFechaIngreso(new Date());
            propietarios.add(prop);
        }
    }

    public void selectObjectMov(SelectEvent event) {
        Boolean flag = true;
        try {
            RegMovimiento mov = (RegMovimiento) event.getObject();
            for (RegMovimientoFicha m : movimientos) {
                if (Objects.equals(m.getMovimiento().getId(), mov.getId())) {
                    flag = false;
                    JsfUti.messageWarning(null, Messages.elementoRepetido, "");
                    break;
                }
            }
            if (flag) {
                /*RegMovimientoFicha mf = new RegMovimientoFicha();
                    mf.setMovimiento(mov);
                    movimientos.add(mf);*/
                if (mov.getRegMovimientoFichaCollection().isEmpty()) {
                    RegMovimientoFicha mf = new RegMovimientoFicha();
                    mf.setMovimiento(mov);
                    movimientos.add(mf);
                } else {
                    movimiento = mov;
                    JsfUti.update("formFichasMovs");
                    JsfUti.executeJS("PF('dlgFichasMovs').show();");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void addMovimientoFicha() {
        RegMovimientoFicha mf = new RegMovimientoFicha();
        mf.setMovimiento(movimiento);
        movimientos.add(mf);
        JsfUti.update("mainForm:tabFicha:dtMov");
        JsfUti.executeJS("PF('dlgFichasMovs').hide();");
    }

    public void selectObHTramite(SelectEvent event) {
        ht = (HistoricoTramites) event.getObject();
    }

    public void vaciarTramite() {
        ht = new HistoricoTramites();
    }

    public void deletePropietario(int indice) {
        try {
            RegFichaPropietarios fp = propietarios.remove(indice);
            if (fp.getId() != null) {
                manager.delete(fp);
                bs.registrarFichaProp(ficha, fp.getPropietario(), ActividadesTransaccionales.ELIMINAR_PROPIETARIO, periodo);
            }
            //propietarios.remove(indice);
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /*public void deleteMarginacion(int indice) {
        try {
            RegFichaMarginacion fp = marginaciones.remove(indice);
            if (fp.getId() != null) {
                manager.delete(fp);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }*/

 /*public void findTramite() {
        Boolean flag = true;
        try {
            if (tramite != null) {
                for (RegFichaMarginacion m : marginaciones) {
                    if (Objects.equals(m.getTramite().getNumTramite(), tramite)) {
                        flag = false;
                        JsfUti.messageWarning(null, Messages.elementoRepetido, "");
                        break;
                    }
                }
                if (flag) {
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    if (ht != null) {
                        marg = new RegFichaMarginacion();
                        marg.setTramite(ht);
                        marg.setFechaIngreso(new Date());
                        marg.setUserIngreso(session.getUserId());
                        marginaciones.add(marg);
                        JsfUti.update("mainForm:tabFicha:dtMarginacion");
                    } else {
                        JsfUti.messageWarning(null, "No se encuentra tramite.", "");
                    }
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de tramite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }*/
    public void showDlgNewInterv() {
        persona = new RegEnteInterviniente();
        JsfUti.update("formCreaInterv");
        JsfUti.executeJS("PF('dlgCrearInterviniente').show();");
    }

    public void guardarInterviniente() {
        try {
            if (!persona.getNombre().isEmpty() && !persona.getCedRuc().isEmpty()) {
                map = new HashMap();
                map.put("cedula", persona.getCedRuc());
                map.put("nombre", persona.getNombre());
                RegEnteInterviniente rei = (RegEnteInterviniente) manager.findObjectByParameter(Querys.getRegIntervByCedRucByNombre, map);
                if (rei == null) {
                    persona.setUsuarioIngreso(session.getName_user());
                    persona.setFechaIngreso(new Date());
                    persona = (RegEnteInterviniente) manager.persist(persona);
                    this.agregarInterv();
                } else {
                    JsfUti.messageWarning(null, "Ya se existe el interviniente con el mismo nombre y la misma cedula.", "");
                }
            } else if (!persona.getNombre().isEmpty() && persona.getCedRuc().isEmpty()) {
                persona.setUsuarioIngreso(session.getName_user());
                persona.setFechaIngreso(new Date());
                persona = reg.saveInterviniente(persona);
                this.agregarInterv();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarInterv() {
        /*RegFichaPropietarios prop = new RegFichaPropietarios();
        prop.setPropietario(persona);
        prop.setEstado("A");
        prop.setUserIngreso(session.getName_user());
        prop.setFechaIngreso(new Date());
        propietarios.add(prop);*/
        ficha.setClaveCatastral(persona.getCedRuc());
        ficha.setLinderos(persona.getNombre());
        JsfUti.update("mainForm");
        JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
    }

    public void saveFicha() {
        try {
            if (this.validaCampos()) {
                /*if (ficha.getBarrio() != null) {
                    ficha.setSector(ficha.getBarrio().getNombre());
                }*/
 /*if (ficha.getParroquia() != null) {
                    if (ficha.getParroquia().getTipo().equals("RURAL")) {
                        ficha.setTipoPredio("R");
                    }
                }*/
                if (ficha.getId() == null) {
                    ficha.setFechaApe(new Date());
                    ficha.setUserIngreso(session.getName_user());
                } else {
                    ficha.setFechaEdicion(new Date());
                    ficha.setUserEdicion(session.getName_user());
                }
                ficha = reg.guardarFichaRegistral(ficha, propietarios, movimientos, null);
                if (ficha == null) {
                    JsfUti.messageError(null, Messages.error, "");
                } else {
                    JsfUti.messageInfo(null, Messages.transaccionOK, "Numero de Ficha: " + ficha.getNumFicha());
                    this.cargarListasPersistidas();
                    JsfUti.update("mainForm");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaCampos() {
        if (ficha.getTipoFicha() == null) {
            ficha.setTipoFicha(tipoFicha);
        }
        if (ficha.getClaveCatastral() == null || ficha.getClaveCatastral().isEmpty()) {
            ficha.setClaveCatastral("NO REGISTRA");
        } else if (ficha.getTipoFicha().getCodigo() == 2 || ficha.getTipoFicha().getCodigo() == 3) {
            if (ficha.getNumFicha() == null && Utils.isNum(ficha.getClaveCatastral())) {
                ficha.setUafTipoBien(new UafTipoBien(8L));
                ficha.setNumFicha(Long.parseLong(ficha.getClaveCatastral()));
            }
        }
        if (ficha.getEstado() == null) {
            JsfUti.messageWarning(null, Messages.faltanCampos, "Falta seleccionar el estado de la ficha.");
            return false;
        }
        /*if (ficha.getInformacionFicha() == null) {
            JsfUti.messageWarning(null, Messages.faltanCampos, "Falta seleccionar si la ficha está completa o no.");
            return false;
        }*/
        if (ficha.getUafTipoBien() == null) {
            JsfUti.messageWarning(null, Messages.faltanCampos, "Falta seleccionar el tipo de bien de la ficha.");
            return false;
        }
        if (ficha.getLinderos() == null || ficha.getLinderos().isEmpty()) {
            JsfUti.messageWarning(null, Messages.faltanCampos, "Debe ingresar los linderos/descripcion de la ficha.");
            return false;
        }
        /*if (ficha.getDescripcionBien() == null || ficha.getDescripcionBien().isEmpty()) {
            JsfUti.messageWarning(null, Messages.faltanCampos, "Debe ingresar la descripcion de la propiedad.");
            return false;
        }*/
        if (ficha.getTipoFicha().getCodigo() == 0 && ficha.getParroquia() == null) {
            JsfUti.messageWarning(null, Messages.faltanCampos, "Debe ingresar la parroquia para este tipo de ficha.");
            return false;
        }
        return true;
    }

    public void validarDatosAdicionales(Integer tipo) {
        List<RegFicha> fichasTemp = null;
        String numFichas = null;
        if (tipo.equals(1)) { // LOTE
            map = new HashMap<>();
            if (ficha.getParroquia() != null) {
                map.put("parroquia", ficha.getParroquia());
            }
            if (ficha.getBarrio() != null) {
                map.put("barrio", ficha.getBarrio());
            }
            if (ficha.getManzana() != null && !ficha.getManzana().isEmpty()) {
                map.put("manzana", ficha.getManzana());
            }
            if (ficha.getLote() != null && !ficha.getLote().isEmpty()) {
                map.put("lote", ficha.getLote());
            }
            if (ficha.getNombrePredio() != null && !ficha.getNombrePredio().isEmpty()) {
                map.put("lote", ficha.getNombrePredio());
            }
            if (ficha.getDepartamento() != null && !ficha.getDepartamento().isEmpty()) {
                map.put("departamento", ficha.getDepartamento());
            }
            if (ficha.getDivision() != null && !ficha.getDivision().isEmpty()) {
                map.put("division", ficha.getDivision());
            }
            if (!map.isEmpty()) {
                if (ficha.getNumFicha() != null) {
                    map.put("numFicha", new WhereClause(ficha.getNumFicha(), "notEqual"));
                }
                fichasTemp = manager.findObjectByParameterList(RegFicha.class, map);
            }
        } else if (tipo.equals(2)) { // CLAVE CATASTRAL
            if (Utils.isEmpty(ficha.getClaveCatastral()).isEmpty() && Utils.isEmpty(ficha.getClaveCatastralOld()).isEmpty()) {
                return;
            }
            numFichas = (String) manager.getNativeQuery(Querys.getSqlFicha(ficha.getClaveCatastral(), ficha.getClaveCatastralOld(), ficha.getNumFicha()));
        }
        if (Utils.isNotEmpty(fichasTemp)) {
            StringBuilder builder = new StringBuilder();
            for (RegFicha rf : fichasTemp) {
                if (!rf.getNumFicha().equals(this.ficha.getNumFicha())) {
                    builder.append(rf.getNumFicha());
                    builder.append(", ");
                }
            }
            JsfUti.messageWarning(null, "Advertencia", "Se encontraron las siguientes fichas con las mismas identificaciones: " + builder);
        } else if (numFichas != null) {
            JsfUti.messageWarning(null, "Advertencia", "Se encontraron las siguientes fichas con las mismas identificaciones: " + numFichas);
        } else {
            JsfUti.messageInfo(null, "No se encontraron concidencias.", "");
        }
        map = new HashMap<>();
    }

    public void cargarListasPersistidas() {
        try {
            propietarios = reg.getPropietariosFichaByFicha(ficha.getId());
            movimientos = reg.getRegMovByIdFicha(ficha.getId());
            //marginaciones = reg.getMarginacionesByFicha(ficha.getId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void onDtCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            if (newValue != null && !newValue.equals(oldValue)) {
                JsfUti.messageInfo(null, "Celda editada.", "Valor anterior: " + oldValue + ", Nuevo valor: " + newValue);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            modelo = reg.getConsultaMovimiento(mov.getId());
            if (modelo != null) {
                JsfUti.update("formMovRegSelec");
                JsfUti.executeJS("PF('dlgMovRegSelec').show();");
            } else {
                modelo = new ConsultaMovimientoModel();
                JsfUti.messageError(null, "No se pudo hacer la consulta.", "");
            }
            selects = new ArrayList<>();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarPropietarios() {
        RegFichaPropietarios prop;
        if (!selects.isEmpty()) {
            for (RegMovimientoCliente mc : selects) {
                prop = new RegFichaPropietarios();
                prop.setPropietario(mc.getEnteInterv());
                prop.setEstado("A");
                prop.setUserIngreso(session.getName_user());
                prop.setFechaIngreso(new Date());
                propietarios.add(prop);
            }
            JsfUti.update("mainForm:tabFicha:dtPropietarios");
            JsfUti.messageInfo(null, "Propietario(s) cargados con exito.", "");
        }
    }

    public void findFichaPrincipal() {
        try {
            if (numeroFicha == null) {
                JsfUti.messageInfo(null, "Ingrese el Numero de Ficha para buscar.", "");
                return;
            }
            map = new HashMap();
            map.put("numFicha", numeroFicha);
            map.put("tipoFicha", new RegTipoFicha(1L));
            fichaMain = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
            if (fichaMain == null) {
                linderos = null;
                fichaMain = new RegFicha();
                movsficha = new ArrayList<>();
                JsfUti.messageError(null, "No se encuentra el numero de ficha.", "");
            } else {
                linderos = fichaMain.getLinderos();
                movsficha = reg.getMovimientosByFicha(fichaMain.getId());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
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
        }
        return null;
        //}
    }

    public void guardarFichasDuplicadas() {
        try {
            if (fichaMain.getId() == null) {
                JsfUti.messageError(null, "Ficha Registral principal no encontrada.", "");
                return;
            }
            if (linderos == null) {
                JsfUti.messageError(null, "Debe ingresar los linderos para las fichas nuevas.", "");
                return;
            }
            if (cantidad != null && cantidad > 0) {
                response = fis.saveFichasDuplicadas(fichaMain, cantidad, session.getName_user(), linderos, movsficha);
                if (response != null) {
                    JsfUti.update("formMensaje");
                    JsfUti.executeJS("PF('dlgMensajes').show()");
                } else {
                    JsfUti.messageError(null, Messages.error, "");
                }
            } else {
                JsfUti.messageError(null, "Debe Ingresar una cantidad mayor a Cero.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirFichaRegistral() {
        if (ficha.getId() != null) {
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
            ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } else {
            JsfUti.messageWarning(null, "No se puede generar el Reporte de la Ficha", "");
        }
    }

    public void selectPersona(SelectEvent event) {
        persona = (RegEnteInterviniente) event.getObject();
        ficha.setClaveCatastral(persona.getCedRuc());
        if (this.validarClaveCatastral()) {
            ficha.setLinderos(persona.getNombre());
        }
    }

    public boolean validarClaveCatastral() {
        try {
            if (ficha.getClaveCatastral() != null && !ficha.getClaveCatastral().isEmpty()) {
                map = new HashMap<>();
                map.put("claveCatastral", ficha.getClaveCatastral());
                Object ob = manager.findObjectByParameter(RegFicha.class, map);
                if (ob == null) {
                    JsfUti.messageInfo(null, "DISPONIBLE", "El valor de codigo/clave/chasis esta disponible.");
                    return true;
                } else {
                    JsfUti.messageError(null, "NO DISPONIBLE", "El valor de codigo/clave/chasis ya se encuentra registrado.");
                }
            } else {
                JsfUti.messageError(null, "INGRESAR DATOS", "Debe ingresar codigo/clave/chasis para poder validar.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return false;
    }

    public List<CtlgItem> getUnidades() {
        return manager.findAllEntCopy(Querys.getCtlgItemUnidades);
    }

    public List<RegTipoFicha> getTiposFichas() {
        return manager.findAllEntCopy(Querys.getRegTipoFicha);
    }

    public List<Barrios> getBarrios() {
        return manager.findAllEntCopy(Querys.getBarriosList);
    }

    public List<CatParroquia> getListParroqia() {
        return manager.findAllEntCopy(Querys.getCatParroquiaList);
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public List<RegMovimientoFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimientoFicha> movimientos) {
        this.movimientos = movimientos;
    }

    public ConsultaMovimientoModel getModelo() {
        return modelo;
    }

    public void setModelo(ConsultaMovimientoModel modelo) {
        this.modelo = modelo;
    }

    public List<RegFichaPropietarios> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegFichaPropietarios> propietarios) {
        this.propietarios = propietarios;
    }

    public List<CtlgItem> getEstados() {
        return estados;
    }

    public void setEstados(List<CtlgItem> estados) {
        this.estados = estados;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    /*public RegEnteInterviniente getRe() {
        return re;
    }

    public void setRei(RegEnteInterviniente re) {
        this.re = re;
    }*/
    public List<UafTipoBien> getTiposBien() {
        return tiposBien;
    }

    public void setTiposBien(List<UafTipoBien> tiposBien) {
        this.tiposBien = tiposBien;
    }

    public RegFichaMarginacion getMarg() {
        return marg;
    }

    public void setMarg(RegFichaMarginacion marg) {
        this.marg = marg;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<RegMovimientoCliente> getSelects() {
        return selects;
    }

    public void setSelects(List<RegMovimientoCliente> selects) {
        this.selects = selects;
    }

    public Long getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Long numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public List<RegMovimiento> getMovsficha() {
        return movsficha;
    }

    public void setMovsficha(List<RegMovimiento> movsficha) {
        this.movsficha = movsficha;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<CtlgItem> getEstadosInformacion() {
        return estadosInformacion;
    }

    public void setEstadosInformacion(List<CtlgItem> estadosInformacion) {
        this.estadosInformacion = estadosInformacion;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    /*public Boolean getFichaReutilizable() {
        return fichaReutilizable;
    }

    public void setFichaReutilizable(Boolean fichaReutilizable) {
        this.fichaReutilizable = fichaReutilizable;
    }*/
    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    /*public PredioModel getPredioModel() {
        return predioModel;
    }
    public void setPredioModel(PredioModel predioModel) {
        this.predioModel = predioModel;
    }*/
 /*public List<RegFichaMarginacion> getMarginaciones() {
        return marginaciones;
    }
    public void setMarginacion(List<RegFichaMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }*/
    public RegEnteInterviniente getPersona() {
        return persona;
    }

    public void setPersona(RegEnteInterviniente persona) {
        this.persona = persona;
    }
}
