/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgCargo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.CtlgTipoParticipacion;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegCapital;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegEstadoMovimiento;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCapital;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoParticipante;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegMovimientoRepresentante;
import com.origami.sgr.entities.RegMovimientoSocios;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.entities.SecuenciaInscripcion;
import com.origami.sgr.entities.UafNacionalidad;
import com.origami.sgr.entities.UafPapelInterv;
import com.origami.sgr.entities.UafRolInterv;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import java.util.logging.Logger;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Inscribir extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(Inscribir.class.getName());

    @Inject
    private BitacoraServices bs;

    @Inject
    private SeqGenMan sec;

    @Inject
    private FirmaDigitalLocal fd;

    protected Long tarea, ficha, uafrol;
    protected Integer tipocatastro = 0, consulta = 0, tipoparticipante = 0, interviniente = 0;
    protected int indice = -1;
    protected String tramite, taskID;
    protected Boolean uaf = false, caduco = false;
    protected RegpTareasTramite tt;
    protected UafNacionalidad nac;
    protected RegMovimiento movimiento = new RegMovimiento();
    protected List<RegEnteInterviniente> participantes = new ArrayList<>();
    protected List<RegMovimientoCliente> mcs = new ArrayList<>(), selects = new ArrayList<>();
    protected List<RegMovimientoFicha> mfs = new ArrayList<>();
    protected List<RegMovimientoReferencia> mrs = new ArrayList<>();
    protected RegEnteInterviniente re = new RegEnteInterviniente();
    protected RegMovimientoCliente interv = new RegMovimientoCliente();
    protected List<RegMovimiento> movimientosPorTomosList;
    //protected List<RegMovimientoCliente> movimientosRelacionadosTarea;
    protected RegMovimientoParticipante mp = new RegMovimientoParticipante();
    protected RegRegistrador registrador;
    /*protected TramiteModel tm = new TramiteModel();
    protected PredioModel pm = new PredioModel();
    protected List<PredioModel> list = new ArrayList<>();*/
    protected AclUser user;
    protected Boolean habilitar = true, archivo = false, esJuridico = false;
    protected BigInteger periodo;
    protected RegFichaPropietarios prop;
    protected RegDomicilio domicilio;
    protected RegTipoFicha tipoFicha;
    protected RegFicha regficha;
    protected RegMovimientoCapital movCap = new RegMovimientoCapital();
    protected RegMovimientoRepresentante movRep = new RegMovimientoRepresentante();
    protected RegMovimientoSocios movSoc = new RegMovimientoSocios();
    protected List<RegMovimientoCapital> mcps = new ArrayList<>();
    protected List<RegMovimientoRepresentante> mres = new ArrayList<>();
    protected List<RegMovimientoSocios> msos = new ArrayList<>();
    protected SecuenciaInscripcion secuencia;
    protected RegMovimientoReferencia movref = new RegMovimientoReferencia();

    @PostConstruct
    public void initView() {
        try {
            if (ss.getParametros() == null) {
                JsfUti.redirectFaces("/procesos/dashBoard.xhtml");
            } else if (ss.getParametros().get("tarea") == null || ss.getParametros().get("taskID") == null) {
                JsfUti.redirectFaces("/procesos/dashBoard.xhtml");
            } else {
                taskID = (String) ss.getParametros().get("taskID");
                habilitar = (Boolean) ss.getParametros().get("habilitar");
                esJuridico = (Boolean) ss.getParametros().get("esJuridico");
                if (esJuridico == null) {
                    esJuridico = Boolean.FALSE;
                }
                archivo = (Boolean) ss.getParametros().get("archivo");
                if (archivo == null) {
                    archivo = false;
                }
                this.setTaskId(taskID);
                tarea = (Long) ss.getParametros().get("tarea");
                movimiento = reg.getMovimientoInscripcion(tarea);

                mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                msos = reg.getRegMovSocioByIdMov(movimiento.getId());

                mp = reg.getParticipantesMov(movimiento.getId());
                if (mp == null) {
                    mp = new RegMovimientoParticipante();
                }
                this.renderTipoActo();
                uaf = reg.movimientoUafe(movimiento.getId());
                tt = movimiento.getTramite();

                map = new HashMap<>();
                map.put("codigo", "ECU");
                nac = (UafNacionalidad) manager.findObjectByParameter(UafNacionalidad.class, map); //NACIONALIDAD ECUATORIANA
                map = new HashMap();
                map.put("actual", Boolean.TRUE);
                registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                user = manager.find(AclUser.class, session.getUserId());
                periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                caduco = this.comprobarFechaRepertorio(movimiento.getFechaRepertorio());
                tipoFicha = manager.find(RegTipoFicha.class, 1L);
                movref.setMovimientoReferencia(new RegMovimiento());
            }
            ss.instanciarParametros();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean comprobarFechaRepertorio(Date fechaRepertorio) {
        Boolean temp = false;
        try {
            map = new HashMap();
            map.put("code", Constantes.diasValidezRepertorio);
            Valores dias = (Valores) manager.findObjectByParameter(Valores.class, map);
            Date temporal = Utils.sumarRestarDiasFecha(fechaRepertorio, dias.getValorNumeric().intValue());
            if (temporal.before(new Date()) && movimiento.getNumInscripcion() == null) {
                temp = true;
                JsfUti.messageError(null, "Repertorio Caducado", "Debe generar nuevo Repertorio.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return temp;
    }

    public List<RegLibro> getLibros() {
        return manager.findAllEntCopy(Querys.getRegLibroListOrd);
    }

    public void copiarIntervinientes(RegMovimiento mov) {
        mov.getRegMovimientoClienteCollection().stream().map(rmc -> {
            RegMovimientoCliente mc = new RegMovimientoCliente();
            mc.setEnteInterv(rmc.getEnteInterv());
            mc.setPapel(rmc.getPapel());
            mc.setItem(rmc.getItem());
            mc.setEstado(rmc.getEstado());
            return mc;
        }).forEachOrdered(mc -> {
            mcs.add(mc);
        });
        JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
    }

    public List<RegDomicilio> getDomicilios() {
        return manager.findAllEntCopy(Querys.getRegDomicilioList);
    }

    public List<RegEstadoMovimiento> getEstadosInscripcion() {
        return manager.findAllEntCopy(Querys.getRegEstadosInscripcion);
    }

    public List<RegPapel> getPapeles() {
        return manager.findAllEntCopy(Querys.getRegPapelesList);
    }

    public List<RegPapel> completePapel(String query) {
        List<RegPapel> results = manager.findMax(Querys.getRegCatPapelByPapel, new String[]{"papel"}, new Object[]{query.toLowerCase().trim().replaceAll(" ", "%") + "%"}, 10);
        return results;
    }

    public List<RegDomicilio> completeDomicilio(String query) {
        List<RegDomicilio> results = manager.findMax(Querys.getRegDomicilioByName, new String[]{"ciudad"}, new Object[]{query.toLowerCase().trim().replaceAll(" ", "%") + "%"}, 10);
        return results;
    }

    public List<CtlgItem> getEstadosCivil() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        return manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public List<CtlgItem> getCargosRep() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.cargosRepresenante);
        return manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public List<CtlgItem> getCodigosTiempo() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.codigosTiempo);
        return manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public void selectObjectActo(SelectEvent event) {
        RegActo acto = (RegActo) event.getObject();
        movimiento.setActo(acto);
        this.renderTipoActo();
    }

    public void selectObjectActoRef(SelectEvent event) {
        RegActo acto = (RegActo) event.getObject();
        movref.getMovimientoReferencia().setActo(acto);
    }

    /*public void selectObjectActo(SelectEvent event) {
        if (Objects.equals(movimiento.getActo().getFijo(), acto.getFijo())) {
            if (movimiento.getActo().getValor().compareTo(acto.getValor()) == 0) {
                movimiento.setActo(acto);
                movimiento.setLibro(acto.getLibro());
                this.renderTipoActo();
            } else {
                JsfUti.messageWarning(null, "El acto seleccionado no tiene el mismo valor que el acto proformado.", "");
            }
        } else {
            JsfUti.messageWarning(null, "El acto seleccionado no tiene el mismo tipo de cálculo que el acto proformado.", "");
        }
    }*/
    public void selectObjectJudicial(SelectEvent event) {
        RegEnteJudiciales enju = (RegEnteJudiciales) event.getObject();
        movimiento.setEnteJudicial(enju);
    }

    public void selectObjectJudicialRef(SelectEvent event) {
        RegEnteJudiciales enju = (RegEnteJudiciales) event.getObject();
        movref.getMovimientoReferencia().setEnteJudicial(enju);
    }

    public boolean containsInterviniente(String cedRuc) {
        return mcs.stream().anyMatch(mc -> (mc.getEnteInterv().getCedRuc().equals(cedRuc)));
    }

    public void selectInterv(SelectEvent event) {
        RegEnteInterviniente in = (RegEnteInterviniente) event.getObject();
        if (this.containsInterviniente(in.getCedRuc())) {
            JsfUti.messageError(null, Messages.elementoRepetido, "");
        } else {
            RegMovimientoCliente mc = new RegMovimientoCliente();
            mc.setEnteInterv(in);
            mcs.add(mc);
        }
    }

    public void selectNacUaf(SelectEvent event) {
        UafNacionalidad un = (UafNacionalidad) event.getObject();
        interv.getEnteInterv().setNacionalidad(un);
    }

    public void showDlgNewInterv(int tipo) {
        this.interviniente = tipo;
        re = new RegEnteInterviniente();
        JsfUti.update("formCreaInterv");
        JsfUti.executeJS("PF('dlgCrearInterviniente').show();");
    }

    public void buscarIntervinienteDinardap() {
        try {
            if (re.getCedRuc() != null && !re.getCedRuc().isEmpty()) {
                PubPersona persona = reg.buscarDinardap(re.getCedRuc());
                if (persona != null) {
                    //re.setNombre(persona.getApellidos() + " " + persona.getNombres());
                    re.setNombre(persona.getApellidos());
                } else {
                    JsfUti.messageWarning(null, "No se encontro datos en la Dinardap.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar numero de identificacion.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardarInterviniente() {
        try {
            if (re.getId() == null) {
                if (!re.getNombre().isEmpty() && !re.getCedRuc().isEmpty()) {
                    if (!validarInterviniente()) {
                        re.setUsuarioIngreso(session.getName_user());
                        re.setFechaIngreso(new Date());
                        re = (RegEnteInterviniente) manager.persist(re);
                        this.agregarInterv();
                    } else {
                        JsfUti.messageWarning(null, "Ya se existe el interviniente con el mismo nombre y la misma cedula.", "");
                    }
                } else if (!re.getNombre().isEmpty() && re.getCedRuc().isEmpty()) {
                    re.setUsuarioIngreso(session.getName_user());
                    re.setFechaIngreso(new Date());
                    re = reg.saveInterviniente(re);
                    this.agregarInterv();
                } else {
                    JsfUti.messageWarning(null, Messages.camposObligatorios, "");
                }
            } else {
                if (!re.getNombre().isEmpty() && !re.getCedRuc().isEmpty()) {
                    if (!validarInterviniente()) {
                        re.setUsuarioEdicion(session.getName_user());
                        re.setFechaEdicion(new Date());
                        re = (RegEnteInterviniente) manager.persist(re);
                        interv.setEnteInterv(re);
                        JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
                        JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
                    } else {
                        JsfUti.messageWarning(null, "Ya se existe el interviniente con el mismo nombre y la misma cedula.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, Messages.camposObligatorios, "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private Boolean validarInterviniente() {
        map = new HashMap();
        map.put("cedula", re.getCedRuc());
        map.put("nombre", re.getNombre());
        RegEnteInterviniente rei = (RegEnteInterviniente) manager.findObjectByParameter(Querys.getRegIntervByCedRucByNombre, map);
        return rei != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public void agregarInterv() {
        switch (interviniente) {
            case 1:
                RegMovimientoCliente mc = new RegMovimientoCliente();
                mc.setEnteInterv(re);
                mcs.add(mc);
                JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
                JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
                break;
            case 2:
                RegMovimientoRepresentante mr = new RegMovimientoRepresentante();
                mr.setEnteInterv(re);
                mres.add(mr);
                JsfUti.update("formNuevInsc:tVdetalle:tabPartes:dtrepresentantes");
                JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
                break;
            case 3:
                RegMovimientoSocios ms = new RegMovimientoSocios();
                ms.setEnteInterv(re);
                msos.add(ms);
                JsfUti.update("formNuevInsc:tVdetalle:tabPartes:dtsocios");
                JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
                break;
        }
    }

    public void showDlgEditInterv(int index, int tipo) {
        switch (tipo) {
            case 1:
                interv = mcs.get(index);
                JsfUti.update("formEditInterv");
                JsfUti.executeJS("PF('dlgEditInter').show();");
                break;
            case 2:
                movRep = mres.get(index);
                JsfUti.update("formEditRepr");
                JsfUti.executeJS("PF('dlgEditRepr').show();");
                break;
            case 3:
                movSoc = msos.get(index);
                JsfUti.update("formEditSocio");
                JsfUti.executeJS("PF('dlgEditSocio').show();");
                break;
        }
    }

    public void showDlgNombresInterv(int index) {
        try {
            interv = mcs.get(index);
            re = interv.getEnteInterv();
            JsfUti.update("formCreaInterv");
            JsfUti.executeJS("PF('dlgCrearInterviniente').show();");
        } catch (Exception e) {
            System.out.println(e);
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
        JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
        JsfUti.executeJS("PF('dlgEditInter').hide();");
    }

    public void deleteInterv(int index) {
        RegMovimientoCliente mc = mcs.remove(index);
        if (mc.getId() != null) {
            manager.delete(mc);
        }
    }

    public void limpiarDatos() {
        interv.setCedula(null);
        interv.setNombres(null);
        interv.setEstado(null);
    }

    public void buscarInterviniente() {
        RegEnteInterviniente rei;
        if (interv.getCedula() == null) {
            JsfUti.messageInfo(null, "Debe Ingresar un Numero de documento para buscar.", "");
            return;
        }
        if (!Utils.validateNumberPattern(interv.getCedula().trim())) {
            JsfUti.messageFatal(null, "Solo Debe Ingresar digitos.", "");
            return;
        }
        map = new HashMap();
        map.put("cedula", interv.getCedula());
        rei = (RegEnteInterviniente) manager.findObjectByParameter(Querys.getRegEnteIntervinienteByCedRuc, map);
        if (rei != null) {
            interv.setCedula(rei.getCedRuc());
            interv.setNombres(rei.getNombre());
        } else {
            JsfUti.messageWarning(null, "Persona no encontrada.", "");
        }
    }

    public void deleteMovsRef(int index) {
        RegMovimientoReferencia mr = mrs.remove(index);
        if (mr.getId() != null) {
            manager.delete(mr);
        }
    }

    public void deleteFicha(int index) {
        RegMovimientoFicha mf = mfs.remove(index);
        if (mf.getId() != null) {
            manager.delete(mf);
        }
    }

    public void buscarByNumFicha() {
        try {
            if (ficha == null) {
                JsfUti.messageInfo(null, "Ingrese el Numero de Ficha para buscar.", "");
                return;
            }
            if (this.containsRegFicha(ficha)) {
                JsfUti.messageError(null, "No se agrego la ficha", "Numero de ficha repetido o tipo de ficha es incorrecto.");
            } else {
                map = new HashMap();
                map.put("numFicha", ficha);
                map.put("tipoFicha", tipoFicha);
                RegFicha rf = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
                if (rf == null) {
                    JsfUti.messageError(null, "No se encuentra el numero de Ficha.", "");
                    return;
                }
                if (rf.getEstado().getValor().equalsIgnoreCase("ACTIVO")) {
                    if (movimiento.getLibro() != null && movimiento.getLibro().getEstadoFicha() != null
                            && movimiento.getLibro().getEstadoFicha() && movimiento.getLibro().getTipo() != null) {
                        CtlgItem temp = rf.getEstado();
                        rf.setEstado(new CtlgItem(movimiento.getLibro().getTipo().longValue()));
                        rf = (RegFicha) manager.merge(rf);
                        rf.setState(temp);
                    }
                    JsfUti.messageInfo(null, "El estado de la Ficha es: " + rf.getState().getValor(), "");
                } else {
                    JsfUti.messageError(null, "No se puede agregar referencia, estado de la Ficha: " + rf.getEstado().getValor(), "");
                    return;
                }
                RegMovimientoFicha mf = new RegMovimientoFicha();
                mf.setFicha(rf);
                mfs.add(mf);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean containsRegFicha(Long numFicha) {
        for (RegMovimientoFicha r : mfs) {
            if (!Objects.equals(r.getFicha().getTipoFicha().getId(), tipoFicha.getId())) {
                return true;
            }
            if (Objects.equals(r.getFicha().getNumFicha(), numFicha)) {
                return true;
            }
        }
        return false;
    }

    public void showDlgWithFicha(String url) {
        List<Long> ids;
        if (Utils.isNotEmpty(mfs)) {
            ids = new ArrayList<>();
            mfs.forEach((mf) -> {
                ids.add(mf.getFicha().getId());
            });
            ss.instanciarParametros();
            ss.agregarParametro("idFicha", ids);
        }
        showDlg(url);
    }

    public void showMovimientosFicha() {
        if (Utils.isEmpty(mfs)) {
            JsfUti.messageError(null, "Debe buscar la ficha ", "");
        }
    }

    /*public void selectObjectReferencia(SelectEvent event) {
            RegMovimiento m = (RegMovimiento) event.getObject();
        if (this.containsReferencia(m.getId())) {
            JsfUti.messageError(null, Messages.elementoRepetido, "");
        } else {
            RegMovimientoReferencia mr = new RegMovimientoReferencia();
            mr.setMovimientoReferencia(m);
            mrs.add(mr);
        }
    }*/
    public void selectObjectAntecedente(SelectEvent event) {
        try {
            RegMovimiento m = (RegMovimiento) event.getObject();
            movref = new RegMovimientoReferencia();
            movref.setMovimientoReferencia(m);
            movref.setActo(m.getActo());
            movref.setFechaInscripcion(m.getFechaInscripcion());
            movref.setInscripcion(m.getNumInscripcion());
            movref.setLibro(m.getLibro());

            JsfUti.update("formMarginacion");
            JsfUti.executeJS("PF('dlgMarginacion').show();");

            //mrs.add(mr);
            //JsfUti.messageWarning(null, "Advertencia", "Esta marginacion no se guarda en el Documents.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectObjectReferencia(SelectEvent event) {
        movref = (RegMovimientoReferencia) event.getObject();
        movref.setMovimiento(movimiento.getId());
        JsfUti.update("formMarginacion");
        JsfUti.executeJS("PF('dlgMarginacion').show();");
    }

    public boolean containsReferencia(Long idmov) {
        return mrs.stream().anyMatch(mr -> (Objects.equals(mr.getMovimientoReferencia().getId(), idmov)));
    }

    public void guardadoParcial() {
        try {

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean camposObligatorioInscripcion() {
        if (movimiento.getFechaOto() == null && movimiento.getFechaResolucion() == null) {
            JsfUti.messageError(null, "Fecha de celebracion o fecha de resolucion no debe ser vacia.", "");
            return false;
        }
        if (movimiento.getFechaOto() != null && movimiento.getFechaOto().compareTo(movimiento.getFechaRepertorio()) > 0) {
            JsfUti.messageError(null, "Fecha de celebracion no puede ser mayor a la fecha del repertorio.", "");
            return false;
        }
        if (movimiento.getFechaResolucion() != null && movimiento.getFechaResolucion().compareTo(movimiento.getFechaRepertorio()) > 0) {
            JsfUti.messageError(null, "Fecha de resolucion no puede ser mayor a la fecha del repertorio.", "");
            return false;
        }
        if (movimiento.getActo() == null) {
            JsfUti.messageError(null, "Debe Seleccionar un Acto.", "");
            return false;
        }
        if (movimiento.getLibro() == null) {
            JsfUti.messageError(null, "Se debe asociar un Libro al Acto seleccionado.", "");
            return false;
        }
        if (movimiento.getDomicilio() == null) {
            JsfUti.messageError(null, "Debe Seleccionar un Canton.", "");
            return false;
        }
        if (movimiento.getEnteJudicial() == null) {
            JsfUti.messageError(null, "Debe Seleccionar un Ente Judicial. Si no aplica, registrar como NINGUNO.", "");
            return false;
        }
        if (mcs == null || mcs.isEmpty()) {
            JsfUti.messageError(null, "Debe Ingresar los Intervinientes Involucrados en la Inscripcion", "");
            return false;
        }
        return true;
    }

    public boolean validacionNrpm() {
        if (movimiento.getVinculoFamiliar()) {
            if (mfs.isEmpty()) {
                return true;
            } else {
                if (mp.getEntrega() == null || mp.getTipoParticipanteEntrega() == null || mp.getTipoRelacionEntrega() == null) {
                    return true;
                }
                if (!mp.getTipoParticipanteEntrega().equalsIgnoreCase("2")) {
                    if (mp.getRecibe() == null || mp.getTipoParticipanteRecibe() == null || mp.getTipoRelacionRecibe() == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void guardarMovimiento() {
        try {
            if (this.camposObligatorioInscripcion()) {
                Calendar cl1 = Calendar.getInstance();
                Integer anio = cl1.get(Calendar.YEAR);
                movimientosPorTomosList = new ArrayList<>();
                movimientosPorTomosList = reg.getRegMovimientosPorLibroAnio(anio, movimiento.getLibro().getId());
                secuencia = sec.consultarSecuenciaInscripcion(movimiento.getLibro().getId());
                JsfUti.update("formAsignacionTomo");
                JsfUti.executeJS("PF('dlgAsignacionTomo').show();");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarTomo() {
        try {
            if (caduco) {
                JsfUti.messageError(null, "Repertorio Caducado", "Debe generar nuevo Repertorio.");
                return;
            }
            if (this.camposObligatorioInscripcion()) {
                movimiento.setUafe(uaf);
                if (movimiento.getFechaIngreso() == null) {
                    movimiento.setFechaIngreso(new Date());
                }
                movimiento.setRegistrador(registrador);
                movimiento.setEditable(Boolean.FALSE);
                movimiento.setTaskId(this.getTaskId());
                movimiento.setUserCreador(new AclUser(session.getUserId()));
                movimiento.setRegMovimientoClienteCollection(mcs);
                movimiento.setRegMovimientoFichaCollection(mfs);
                movimiento.setRegMovimientoCapitalCollection(mcps);
                movimiento.setRegMovimientoRepresentanteCollection(mres);
                movimiento.setRegMovimientoSociosCollection(msos);
                movimiento = reg.guardarMovimientoNuevoCompleto(movimiento, mrs);
                //movimiento = reg.guardarMovimientoNuevo(movimiento, mrs);
                if (movimiento != null) {
                    mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                    mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                    mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                    mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                    mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                    msos = reg.getRegMovSocioByIdMov(movimiento.getId());
                    //this.saveMovimientoParticipante();
                    /*for (RegMovimientoFicha f : mfs) {
                        this.agregarFichaTramite(movimiento.getTramite().getTramite().getNumTramite(), f.getFicha(), 
                                movimiento.getTramite().getTramite());
                    }*/
                    JsfUti.messageInfo(null, "Guardado Exitoso", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarIndexacion() {
        try {
            if (this.camposObligatorioInscripcion()) {
                movimiento.setResponseCatastro(session.getName_user());
                movimiento.setRegistrador(registrador);
                movimiento.setEditable(Boolean.FALSE);
                movimiento.setTaskId(this.getTaskId());
                movimiento.setRegMovimientoClienteCollection(mcs);
                movimiento.setRegMovimientoFichaCollection(mfs);
                movimiento.setRegMovimientoCapitalCollection(mcps);
                movimiento.setRegMovimientoRepresentanteCollection(mres);
                movimiento.setRegMovimientoSociosCollection(msos);
                movimiento = reg.guardarMovimientoNuevoCompleto(movimiento, mrs);
                if (movimiento != null) {
                    mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                    mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                    mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                    mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                    mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                    msos = reg.getRegMovSocioByIdMov(movimiento.getId());
                    JsfUti.messageInfo(null, "Guardado Exitoso", "");
                    JsfUti.executeJS("PF('dlgAsignacionTomo').hide();");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarMovimiento() {
        try {
            manager.merge(movimiento);
            JsfUti.messageInfo(null, Messages.correcto, "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarInscripcion(Boolean generaInscripcion) {
        try {
            if (caduco) {
                JsfUti.messageError(null, "Repertorio Caducado", "Debe generar nuevo Repertorio.");
                return;
            }
            /*if (generaInscripcion) {
                JsfUti.executeJS("PF('dlgAsignacionTomo').hide();");
                reg.generarInscripcion(movimiento);
                this.actaInscripcion();
            } else {
                //this.guardarTomo();
                if (movimiento.getNumInscripcion() == null && movimiento.getFechaInscripcion() == null) {
                    this.guardarMovimiento();
                } else {
                    this.actaInscripcion();
                }
            }*/
            if (movimiento.getNumInscripcion() == null && movimiento.getFechaInscripcion() == null) {
                this.guardarTomo();
                reg.generarInscripcion(movimiento);
                this.actaInscripcion();
            } else {
                this.actaInscripcion();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarPartida() {
        try {
            if (caduco) {
                JsfUti.messageError(null, "Repertorio Caducado", "Debe generar nuevo Repertorio.");
                return;
            }
            reg.generarInscripcion(movimiento);
            JsfUti.messageInfo(null, "Partida Generada", "Se generó la partida nro.: " + movimiento.getNumInscripcion());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actaInscripcion() {
        try {
            if (movimiento.getId() != null) {
                ss.instanciarParametros();
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("taskID", this.getTaskId());
                ss.agregarParametro("P_MOVIMIENTO", movimiento.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
                ss.setNombreReporte("ActaInscripcion");
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setEncuadernacion(Boolean.FALSE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void regresar() {
        if (this.camposObligatorioInscripcion()) {
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            session.setTaskID(this.getTaskId());
            if (habilitar) {
                if (tt.getFechaFin() == null) {
                    tt.setFechaFin(new Date());
                }

                tt.setRealizado(Boolean.TRUE);
                tt = reg.guardarRegpTareasTramite(tt);
                if (!esJuridico) {
                    JsfUti.redirectFaces("/procesos/registro/realizarProcesoRp.xhtml");
                } else {
                    JsfUti.redirectFaces("/procesos/registro/realizarProcesoJuridico.xhtml");
                }

            } else {
                if (archivo) {
                    JsfUti.redirectFaces("/procesos/registro/archivoRp.xhtml");
                } else {
                    JsfUti.redirectFaces("/procesos/registro/revisionRp.xhtml");
                }
            }
        }

    }

    public void realizarTarea() {
        if (caduco) {
            JsfUti.messageError(null, "Repertorio Caducado", "Debe generar nuevo Repertorio.");
            return;
        }
        if (this.camposObligatorioInscripcion()) {
            if (movimiento.getNumInscripcion() == null || movimiento.getNumInscripcion() == 0) {
                JsfUti.messageError(null, "Debe generar el numero de Inscripcion.", "");
                return;
            }
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("tramite", this.getTaskId());
            if (habilitar) {
                if (esJuridico) {
                    if (movimiento.getNumInscripcion() == null) {
                        JsfUti.messageError(null, "Debe generar los números de Inscripción.", "");
                        return;
                    } else {
                        tt.setRevisado(Boolean.TRUE);
                        tt.setFechaRevision(new Date());
                    }
                }
                tt.setFechaFin(new Date());
                tt.setRealizado(Boolean.TRUE);
                reg.saveBitacoraFicha(movimiento.getId());
                tt = reg.guardarRegpTareasTramite(tt);
                if (!esJuridico) {
                    JsfUti.redirectFaces("/procesos/registro/realizarProcesoRp.xhtml");
                } else {
                    JsfUti.redirectFaces("/procesos/registro/realizarProcesoJuridico.xhtml");
                }
            } else {
                if (archivo) {
                    JsfUti.redirectFaces("/procesos/registro/archivoRp.xhtml");
                } else {
                    JsfUti.redirectFaces("/procesos/registro/revisionRp.xhtml");
                }
            }
        }
    }

    public void revisarTarea() {
        if (this.camposObligatorioInscripcion()) {
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("tramite", this.getTaskId());

            tt.setRevisado(Boolean.TRUE);
            tt.setFechaRevision(new Date());
            tt = reg.guardarRegpTareasTramite(tt);

            JsfUti.redirectFaces("/procesos/registro/revisarInscripcionRp.xhtml");
        }
    }

    public void regresarCompletarTarea() {
        ss.instanciarParametros();
        ss.agregarParametro("taskID", this.getTaskId());
        ss.agregarParametro("tramite", this.getTaskId());
        JsfUti.redirectFaces("/procesos/registro/realizarProcesoRp.xhtml");
    }

    public void regresarRevisionTramite() {
        ss.instanciarParametros();
        ss.agregarParametro("taskID", this.getTaskId());
        ss.agregarParametro("tramite", this.getTaskId());
        JsfUti.redirectFaces("/procesos/registro/revisarInscripcionRp.xhtml");
    }

    public void completarTareaInscripcion() {
        if (this.camposObligatorioInscripcion()) {
            if (movimiento.getNumInscripcion() == null || movimiento.getNumInscripcion() == 0) {
                JsfUti.messageError(null, "Debe generar el numero de Inscripcion.", "");
                return;
            }
            this.guardarTomo();
            tt.setRealizado(Boolean.TRUE);
            tt.setFechaFin(new Date());
            manager.merge(tt);
            reg.saveBitacoraFicha(movimiento.getId());
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/revisionRp.xhtml");
        }
    }

    public void completarTareaRevisionInscripcion() {
        if (this.camposObligatorioInscripcion()) {
            if (movimiento.getNumInscripcion() == null || movimiento.getNumInscripcion() == 0) {
                JsfUti.messageError(null, "Debe generar el numero de Inscripcion.", "");
                return;
            }
            tt.setRealizado(Boolean.TRUE);
            tt.setFechaFin(new Date());
            tt.setRevisado(Boolean.TRUE);
            tt.setFechaRevision(new Date());
            manager.merge(tt);
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/revisionRp.xhtml");
        }

    }

    public void razonInscripcion() {
        try {
            if (movimiento.getId() != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("taskID", this.getTaskId());
                ss.agregarParametro("ID_MOV", movimiento.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
                ss.setNombreReporte("RazonInscripcion");
                ss.setNombreSubCarpeta("registro");
                ss.setTieneDatasource(true);
                ss.setEncuadernacion(Boolean.FALSE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void redirect() {
        session.setTaskID(this.getTaskId());
        JsfUti.redirectFaces("/procesos/registro/realizarProcesoRp.xhtml");
    }

    public void showDlgInfoUaf(int index) {
        interv = mcs.get(index);
        if (interv.getUafRol() != null) {
            uafrol = interv.getUafRol().getId();
        }
        if (interv.getEnteInterv().getNacionalidad() == null) {
            interv.getEnteInterv().setNacionalidad(nac);
        }
        JsfUti.update("formUaf");
        JsfUti.executeJS("PF('dlgUafe').show();");
    }

    public List<UafPapelInterv> getUafPapeles() {
        return manager.findAllEntCopy(Querys.getUafPapeles);
    }

    public void saveInfoUaf() {
        try {
            if (interv.getEnteInterv().getNacionalidad() == null) {
                JsfUti.messageWarning(null, "Debe seleccionar la nacionalidad del interviniente.", "");
            } else if (interv.getUafPapel() == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el papel del interviniente.", "");
            } else if (uafrol == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el rol del interviniente.", "");
            } else {
                interv.setUafRol(new UafRolInterv(uafrol));
                JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
                JsfUti.executeJS("PF('dlgUafe').hide();");
                uafrol = null;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgParticipantes(int a) {
        try {
            if (movimiento.getVinculoFamiliar() != null && movimiento.getVinculoFamiliar()) {
                tipoparticipante = a;
                participantes = reg.getRegIntervsByIdMov(movimiento.getId());
                JsfUti.update("formNrpm");
                JsfUti.executeJS("PF('dlgNrpm').show();");
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar si hay transferencia en vinculo familiar.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateFormParticipante() {
        if (mp.getTipoParticipanteEntrega() != null) {
            switch (mp.getTipoParticipanteEntrega()) {
                case "1":
                    mp.setTipoParticipanteRecibe("5");
                    break;
                case "2":
                    mp.setRecibe(null);
                    mp.setTipoParticipanteRecibe("6");
                    mp.setTipoRelacionRecibe(null);
                    break;
                case "3":
                    mp.setTipoParticipanteRecibe("1");
                    break;
                case "4":
                    mp.setTipoParticipanteRecibe("3");
                    break;
            }
        }
    }

    public void guardarParticipante(RegEnteInterviniente pa) {
        try {
            if (tipoparticipante == 1) {
                mp.setEntrega(pa);
            } else if (tipoparticipante == 2) {
                mp.setRecibe(pa);
            }
            JsfUti.update("formNuevInsc:tVdetalle:tvAnexos");
            JsfUti.executeJS("PF('dlgNrpm').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveMovimientoParticipante() {
        try {
            if (movimiento.getVinculoFamiliar() != null && movimiento.getVinculoFamiliar()) {
                mp.setMovimiento(movimiento);
                mp = (RegMovimientoParticipante) manager.persist(mp);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void renderTipoActo() {
        try {
            if (movimiento.getActo() != null) {
                if (movimiento.getActo().getTransaccion() == null) {
                    mp.setTipoParticipanteEntrega("1");    //OTORGANTE
                    mp.setTipoParticipanteRecibe("5");     //BENEFICIARIO
                } else {
                    switch (movimiento.getActo().getTransaccion()) {
                        case "2": //COMPRAVENTA
                            mp.setTipoParticipanteEntrega("3");     //VENDEDOR
                            mp.setTipoParticipanteRecibe("1");      //COMPRADOR
                            break;
                        case "6": //AUMENTO DE CAPITAL
                            mp.setTipoParticipanteEntrega("2");     //PROPIETARIO
                            mp.setTipoParticipanteRecibe("6");      //NO APLICA
                            break;
                        case "10": //USUFRUCTO
                            mp.setTipoParticipanteEntrega("1");     //OTORGANTE
                            mp.setTipoParticipanteRecibe("5");      //BENEFICIARIO
                            break;
                        case "12": //DERECHOS Y ACCIONES
                            mp.setTipoParticipanteEntrega("1");    //OTORGANTE
                            mp.setTipoParticipanteRecibe("5");     //BENEFICIARIO
                            break;
                        case "14": //PARTICION HEREDITARIA - TESTAMENTO
                            mp.setTipoParticipanteEntrega("4");     //CAUSANTE
                            mp.setTipoParticipanteRecibe("3");      //HEREDERO - LEGATARIO
                            break;
                    }
                }
                JsfUti.update("formNuevInsc:tVdetalle");
                //JsfUti.update("formNuevInsc:tVdetalle:tvAnexos");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void setDataWS() {
        if (tramite != null) {
            movimiento.setTramiteMuni(tramite);
        }
        switch (tipocatastro) {
            case 1:
                movimiento.setSendWs("TRASPASO DE DOMINIO");
                break;
            case 2:
                movimiento.setSendWs("DESMEMBRACION");
                break;
            case 3:
                movimiento.setSendWs("DECLARATORIA DE PROPIEDAD HORIZONTAL");
                break;
            case 4:
                movimiento.setSendWs("NUEVO CONTRATO");
                break;
        }
    }

    public void actualizarPropietariosFicha() {
        try {
            if (mfs != null && !mfs.isEmpty() && selects != null && !selects.isEmpty()) {
                List<RegFichaPropietarios> propietarios;
                for (RegMovimientoFicha rmf : mfs) {
                    if (rmf.getFicha() != null && rmf.getFicha().getId() != null) {
                        propietarios = reg.getPropietariosFichaByFicha(rmf.getFicha().getId());
                        if (propietarios != null && !propietarios.isEmpty()) {
                            for (RegFichaPropietarios rfp : propietarios) {
                                bs.registrarFichaProp(rmf.getFicha(), rfp.getPropietario(),
                                        ActividadesTransaccionales.ELIMINAR_PROPIETARIO, periodo);
                                manager.delete(rfp);
                            }
                        }
                        guadarPropietarioFicha(rmf.getFicha());
                    }
                }
                JsfUti.messageInfo(null, "Propietarios Actualizados", "");
            } else {
                JsfUti.messageError(null, "Debe seleccionar la ficha o los propietarios para continuar(s)", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void guadarPropietarioFicha(RegFicha regFicha) {
        for (RegMovimientoCliente mc : selects) {
            prop = new RegFichaPropietarios();
            prop.setPropietario(mc.getEnteInterv());
            prop.setEstado("A");
            prop.setUserIngreso(session.getName_user());
            prop.setFechaIngreso(new Date());
            prop.setFicha(regFicha);
            manager.persist(prop);
            bs.registrarFichaProp(regFicha, prop.getPropietario(), ActividadesTransaccionales.AGREGAR_PROPIETARIO, periodo);
        }
    }

    public void copiarDatosMov(SelectEvent event) {
        try {
            RegMovimiento m = (RegMovimiento) event.getObject();
            this.llenarInfo(m);
            JsfUti.messageInfo(null, "Datos copiados con exito!!!", "");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarInfo(RegMovimiento m) {
        RegMovimientoCliente mc;
        //RegMovimientoFicha mf;
        //movimiento.setCuantia(m.getCuantia());
        //movimiento.setAvaluoMunicipal(m.getAvaluoMunicipal());
        movimiento.setDomicilio(m.getDomicilio());
        movimiento.setOrdJud(m.getOrdJud());
        movimiento.setEscritJuicProvResolucion(m.getEscritJuicProvResolucion());
        movimiento.setFechaOto(m.getFechaOto());
        movimiento.setFechaResolucion(m.getFechaResolucion());
        movimiento.setEnteJudicial(m.getEnteJudicial());
        for (RegMovimientoCliente rmc : m.getRegMovimientoClienteCollection()) {
            if (!this.containsInterviniente(rmc.getEnteInterv().getCedRuc())) {
                mc = new RegMovimientoCliente();
                mc.setEnteInterv(rmc.getEnteInterv());
                mc.setPapel(rmc.getPapel());
                mc.setEstado(rmc.getEstado());
                mc.setDomicilio(rmc.getDomicilio());
                mc.setCedula(rmc.getCedula());
                mc.setNombres(rmc.getNombres());
                mcs.add(mc);
            }
        }
        /*for (RegMovimientoFicha rmf : m.getRegMovimientoFichaCollection()) {
            mf = new RegMovimientoFicha();
            mf.setFicha(rmf.getFicha());
            mfs.add(mf);
        }*/
    }

    public void generarRepertorio() {
        if (caduco) {
            String observacion = "Cambio de repertorio, repertorio caducado: " + movimiento.getNumRepertorio()
                    + ", fecha: " + movimiento.getFechaRepertorio();
            if (reg.generarNuevoRepertorio(movimiento.getNumeroTramite())) {
                reg.guardarObservaciones(tt.getTramite(), session.getName_user(), observacion, this.getTaskDataByTaskID().getName());
                this.regresarCompletarTarea();
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } else {
            JsfUti.messageWarning(null, "No se puede realizar esta accion.", "");
        }
    }

    public void addCapital() {
        if (movCap.getCapital() != null && (movCap.getValor().compareTo(BigDecimal.ZERO) > 0)) {
            mcps.add(movCap);
            movCap = new RegMovimientoCapital();
            JsfUti.update("formNuevInsc:tVdetalle:tabPartes:pnlCapital");
            JsfUti.update("formNuevInsc:tVdetalle:tabPartes:dtcapitales");
        } else {
            JsfUti.messageWarning(null, "Seleccione el tipo de capital y el valor debe ser mayor a cero.", "");
        }
    }

    public void deleteCapitales(int index) {
        RegMovimientoCapital mc = mcps.remove(index);
        if (mc.getId() != null) {
            manager.delete(mc);
        }
    }

    public void selectRepresentante(SelectEvent event) {
        RegEnteInterviniente in = (RegEnteInterviniente) event.getObject();
        RegMovimientoRepresentante mr = new RegMovimientoRepresentante();
        mr.setEnteInterv(in);
        mres.add(mr);
    }

    public void selectSocio(SelectEvent event) {
        RegEnteInterviniente in = (RegEnteInterviniente) event.getObject();
        RegMovimientoSocios ms = new RegMovimientoSocios();
        ms.setEnteInterv(in);
        msos.add(ms);
    }

    public void deleteRepresentante(int index) {
        RegMovimientoRepresentante mr = mres.remove(index);
        if (mr.getId() != null) {
            manager.delete(mr);
        }
    }

    public void deleteSocio(int index) {
        RegMovimientoSocios ms = msos.remove(index);
        if (ms.getId() != null) {
            manager.delete(ms);
        }
    }

    public void showDlgEditarFicha(int index) {
        indice = index;
        regficha = mfs.remove(indice).getFicha();
        JsfUti.update("formEditFicha");
        JsfUti.executeJS("PF('dlgEditFicha').show();");
    }

    public void editarFicha() {
        try {
            RegMovimientoFicha temp;
            if (regficha.getClaveCatastral() != null && !regficha.getClaveCatastral().isEmpty()) {
                regficha = (RegFicha) manager.merge(regficha);
                temp = new RegMovimientoFicha();
                temp.setFicha(regficha);
                mfs.add(indice, temp);
                JsfUti.executeJS("PF('dlgEditFicha').hide();");
                JsfUti.messageInfo(null, "Clave catastral actualizada.", "");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar la clave catastral de la ficha.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<RegTipoFicha> getRegTiposFicha() {
        return manager.findAllEntCopy(Querys.getRegTipoFicha);
    }

    public List<RegCapital> getRegCapitales() {
        return manager.findAll(RegCapital.class);
    }

    public List<CtlgCargo> getCtlgCargos() {
        return manager.findAllEntCopy(Querys.CtlgTipocargoOrderByNombre);
    }

    public List<CtlgTipoParticipacion> getCtlgParticipacion() {
        return manager.findAllEntCopy(Querys.CtlgTipoParticipacionOrderByNombre);
    }

    public void guardarMarginacion() {
        try {
            if (movref.getMarginacion() != null && !movref.getMarginacion().isEmpty()
                    && movref.getMovimientoReferencia().getRegistro() != null) {
                movref.setMovimiento(movimiento.getId());
                movref.setUsuario(session.getName_user());
                movref.setIdusuario(session.getUserId());
                movref = reg.guardarMarginacion(movref);
                mrs.add(movref);
                JsfUti.update("formNuevInsc:tVdetalle:dtMovimientosReff");
                JsfUti.executeJS("PF('dlgMarginacion').hide();");
                JsfUti.messageInfo(null, "Registro ingresado con exito.", "");
            } else {
                JsfUti.messageWarning(null, "Se debe ingresar el contenido de la marginacion.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, "Error al guardar la marginacion.", "");
        }
    }

    public boolean validarMarginacion() {
        if (movref.getMovimientoReferencia().getNumInscripcion() == null) {
            return false;
        }
        if (movref.getMovimientoReferencia().getFechaInscripcion() == null) {
            return false;
        }
        if (movref.getMovimientoReferencia().getNumTomo() == null) {
            return false;
        }
        if (movref.getMovimientoReferencia().getLibro() == null) {
            return false;
        }
        if (movref.getMovimientoReferencia().getActo() == null) {
            return false;
        }
        /*if (movref.getMovimientoReferencia().getEnteJudicial() == null) {
            return false;
        }
        if (movref.getMovimientoReferencia().getDomicilio() == null) {
            return false;
        }*/
        if (movref.getMarginacion() == null || movref.getMarginacion().isEmpty()) {
            return false;
        }
        return true;
    }

    public void showDlgIndice() {
        try {
            if (movimiento.getIdTransaccion() == null || movimiento.getIdTransaccion().compareTo(1L) < 0) {
                JsfUti.update("formTransaccion");
                JsfUti.executeJS("PF('dlgTransaccion').show();");
            } else {
                JsfUti.update("formBorrarIndice");
                JsfUti.executeJS("PF('dlgBorrarIndice').show();");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void crearIndiceDocuments() {
        try {
            if (movimiento.getIdPadre() != null && movimiento.getIdPadre().compareTo(BigInteger.ZERO) > 0) {
                movimiento.setIdTransaccion(reg.guardarTbData(movimiento));
                switch (movimiento.getIdTransaccion().intValue()) {
                    case -2:
                        JsfUti.messageError(null, "No se pudo registrar el indice en el Documents.", "");
                        break;
                    case -1:
                        JsfUti.messageWarning(null, "No se encontro el ID_TRANSACCION.", "");
                        break;
                    case 0:
                        JsfUti.messageWarning(null, "El ID_TRANSACCION no es un indice Padre.", "");
                        break;
                    default:
                        manager.update(movimiento);
                        JsfUti.update("formNuevInsc");
                        JsfUti.executeJS("PF('dlgTransaccion').hide();");
                        JsfUti.messageInfo(null, "Indice creado con exito.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe de ingresar el ID_TRANSACCION del Documents.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void limpiarIndiceDocuments() {
        try {
            movimiento.setIdTransaccion(null);
            movimiento.setIdPadre(null);
            movimiento.setIdBlobReg(null);
            manager.update(movimiento);
            this.regresarRevisionTramite();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void firmarActaInscripcion() {
        try {
            if (movimiento.getDocumentoActa() == null) {
                File file = fd.firmarActaInscripcion(movimiento);
                if (file != null) {
                    this.regresarRevisionTramite();
                } else {
                    JsfUti.messageWarning(null, "NO se puedo firmar el documento.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Ya exite el acta con firma electrónica.", "");
            }
        } catch (IOException e) {
            System.out.println(e);
            JsfUti.messageError(null, "Error", "Error al firmar el documento.");
        }
    }

    public void descargarActaInscripcion() {
        String ruta = SisVars.rutaFirmados;
        try {
            if (movimiento.getDocumentoActa() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + "ACTA_" + movimiento.getCodVerificacion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(movimiento.getDocumentoActa(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public RegMovimientoParticipante getMp() {
        return mp;
    }

    public void setMp(RegMovimientoParticipante mp) {
        this.mp = mp;
    }

    public List<RegEnteInterviniente> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<RegEnteInterviniente> participantes) {
        this.participantes = participantes;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public Integer getTipocatastro() {
        return tipocatastro;
    }

    public void setTipocatastro(Integer tipocatastro) {
        this.tipocatastro = tipocatastro;
    }

    public Integer getConsulta() {
        return consulta;
    }

    public void setConsulta(Integer consulta) {
        this.consulta = consulta;
    }

    public UafNacionalidad getNac() {
        return nac;
    }

    public void setNac(UafNacionalidad nac) {
        this.nac = nac;
    }

    public Long getUafrol() {
        return uafrol;
    }

    public void setUafrol(Long uafrol) {
        this.uafrol = uafrol;
    }

    public Long getTarea() {
        return tarea;
    }

    public void setTarea(Long tarea) {
        this.tarea = tarea;
    }

    public Long getFicha() {
        return ficha;
    }

    public void setFicha(Long ficha) {
        this.ficha = ficha;
    }

    public RegpTareasTramite getTt() {
        return tt;
    }

    public void setTt(RegpTareasTramite tt) {
        this.tt = tt;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<RegMovimientoCliente> getMcs() {
        return mcs;
    }

    public void setMcs(List<RegMovimientoCliente> mcs) {
        this.mcs = mcs;
    }

    public List<RegMovimientoFicha> getMfs() {
        return mfs;
    }

    public void setMfs(List<RegMovimientoFicha> mfs) {
        this.mfs = mfs;
    }

    public List<RegMovimientoReferencia> getMrs() {
        return mrs;
    }

    public void setMrs(List<RegMovimientoReferencia> mrs) {
        this.mrs = mrs;
    }

    public RegEnteInterviniente getRe() {
        return re;
    }

    public void setRe(RegEnteInterviniente re) {
        this.re = re;
    }

    public RegMovimientoCliente getInterv() {
        return interv;
    }

    public void setInterv(RegMovimientoCliente interv) {
        this.interv = interv;
    }

    public List<RegMovimiento> getMovimientosPorTomosList() {
        return movimientosPorTomosList;
    }

    public void setMovimientosPorTomosList(List<RegMovimiento> movimientosPorTomosList) {
        this.movimientosPorTomosList = movimientosPorTomosList;
    }

    public Boolean getUaf() {
        return uaf;
    }

    public void setUaf(Boolean uaf) {
        this.uaf = uaf;
    }

    /*public TramiteModel getTm() {
        return tm;
    }

    public void setTm(TramiteModel tm) {
        this.tm = tm;
    }*/
    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }

    public Boolean getArchivo() {
        return archivo;
    }

    public void setArchivo(Boolean archivo) {
        this.archivo = archivo;
    }

    public Boolean getEsJuridico() {
        return esJuridico;
    }

    public void setEsJuridico(Boolean esJuridico) {
        this.esJuridico = esJuridico;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public List<RegMovimientoCliente> getSelects() {
        return selects;
    }

    public void setSelects(List<RegMovimientoCliente> selects) {
        this.selects = selects;
    }

    public Boolean getCaduco() {
        return caduco;
    }

    public void setCaduco(Boolean caduco) {
        this.caduco = caduco;
    }

    public RegMovimientoCapital getMovCap() {
        return movCap;
    }

    public void setMovCap(RegMovimientoCapital movCap) {
        this.movCap = movCap;
    }

    public RegMovimientoRepresentante getMovRep() {
        return movRep;
    }

    public void setMovRep(RegMovimientoRepresentante movRep) {
        this.movRep = movRep;
    }

    public RegMovimientoSocios getMovSoc() {
        return movSoc;
    }

    public void setMovSoc(RegMovimientoSocios movSoc) {
        this.movSoc = movSoc;
    }

    public List<RegMovimientoCapital> getMcps() {
        return mcps;
    }

    public void setMcps(List<RegMovimientoCapital> mcps) {
        this.mcps = mcps;
    }

    public List<RegMovimientoRepresentante> getMres() {
        return mres;
    }

    public void setMres(List<RegMovimientoRepresentante> mres) {
        this.mres = mres;
    }

    public List<RegMovimientoSocios> getMsos() {
        return msos;
    }

    public void setMsos(List<RegMovimientoSocios> msos) {
        this.msos = msos;
    }

    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public SecuenciaInscripcion getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(SecuenciaInscripcion secuencia) {
        this.secuencia = secuencia;
    }

    public RegFicha getRegficha() {
        return regficha;
    }

    public void setRegficha(RegFicha regficha) {
        this.regficha = regficha;
    }

    public RegMovimientoReferencia getMovref() {
        return movref;
    }

    public void setMovref(RegMovimientoReferencia movref) {
        this.movref = movref;
    }

}
