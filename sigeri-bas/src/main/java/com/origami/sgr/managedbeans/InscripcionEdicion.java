/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
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
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegMovimientoCapital;
import com.origami.sgr.entities.RegMovimientoRepresentante;
import com.origami.sgr.entities.RegMovimientoSocios;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.MovimientoModel;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Logger;
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
public class InscripcionEdicion extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(InscripcionEdicion.class.getName());

    private static final long serialVersionUID = 1L;

    @Inject
    protected BitacoraServices bs;

    @Inject
    protected Entitymanager em;

    protected Boolean ingreso = false;
    protected Boolean flag = false;
    protected Long numFicha;
    protected Integer interviniente = 0;
    protected RegEnteInterviniente re = new RegEnteInterviniente();
    protected RegMovimientoCliente interv = new RegMovimientoCliente();
    protected MovimientoModel mm;
    protected BigInteger periodo;
    protected RegMovimientoMarginacion marg = new RegMovimientoMarginacion();

    //DATOS DE INSCRIPCION
    protected RegActo acto = new RegActo();
    protected RegEnteJudiciales enju = new RegEnteJudiciales();
    protected RegMovimiento movimiento = new RegMovimiento();
    protected RegpTareasTramite tareasTramite;
    protected List<RegMovimientoCliente> mcs = new ArrayList<>();
    protected List<RegMovimientoFicha> mfs = new ArrayList<>();
    protected List<RegMovimientoReferencia> mrs = new ArrayList<>();
    //protected List<RegMovimientoMarginacion> mma = new ArrayList<>();
    //protected RegDomicilio domicilio;

    //CATALOGOS
    //protected List<RegLibro> regLibroList = new ArrayList<>();
    //protected Collection<RegPapel> papeles = new ArrayList<>();
    protected RegMovimientoCapital movCap = new RegMovimientoCapital();
    protected RegMovimientoRepresentante movRep = new RegMovimientoRepresentante();
    protected RegMovimientoSocios movSoc = new RegMovimientoSocios();
    protected List<RegMovimientoCapital> mcps = new ArrayList<>();
    protected List<RegMovimientoRepresentante> mres = new ArrayList<>();
    protected List<RegMovimientoSocios> msos = new ArrayList<>();
    protected RegMovimientoReferencia movref = new RegMovimientoReferencia();
    protected RegFicha ficha;
    protected RegTipoFicha tipoFicha;

    @PostConstruct
    public void initView() {
        try {
            if (ss.getParametros() != null) {
                if (ss.getParametros().get("idMov") != null) {
                    Long idMov = (Long) ss.getParametros().get("idMov");
                    if (ss.getParametros().get("idFicha") != null) {
                        Long idFicha = (Long) ss.getParametros().get("idFicha");
                        if (idFicha != null) {
                            ficha = manager.find(RegFicha.class, idFicha);
                        }
                    }
                    movimiento = manager.find(RegMovimiento.class, idMov);
                    if (movimiento != null) {
                        periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                        if (movimiento.getActo() != null) {
                            acto = movimiento.getActo();
                            /*papeles = reg.getRegCatPapelByActo(acto.getId());
                            if (Utils.isEmpty(papeles)) {
                                papeles = manager.findAllEntCopy(Querys.getRegPapelesList);
                            }*/
                        }
                        /*if (movimiento.getDomicilio() == null) {
                            //movimiento.setDomicilio(new RegDomicilio(83L));
                            domicilio = manager.find(RegDomicilio.class, 83L); //CANTON LOJA
                            movimiento.setDomicilio(domicilio);
                        }*/
                        if (movimiento.getEnteJudicial() != null) {
                            enju = movimiento.getEnteJudicial();
                        }
                        mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                        mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                        mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                        //mma = reg.getRegMovMargByIdMov(movimiento.getId());
                        mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                        mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                        msos = reg.getRegMovSocioByIdMov(movimiento.getId());
                        mm = new MovimientoModel(movimiento, null);
                    } else {
                        JsfUti.redirectFaces("/procesos/manage/inscripciones.xhtml");
                    }
                } else {
                    JsfUti.redirectFaces("/procesos/manage/inscripciones.xhtml");
                }
            } else {
                JsfUti.redirectFaces("/procesos/manage/inscripciones.xhtml");
            }
            ss.instanciarParametros();
            movref.setMovimientoReferencia(new RegMovimiento());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarActoAbrev() {
        try {
            if (acto.getAbreviatura() != null) {
                map = new HashMap();
                map.put("abrev", acto.getAbreviatura());
                acto = (RegActo) manager.findObjectByParameter(Querys.getActobyAbreviatura, map);
                if (acto != null) {
                    movimiento.setActo(acto);
                    this.cargarPapelesByActo();
                } else {
                    acto = new RegActo();
                    JsfUti.messageInfo(null, Messages.sinCoincidencias, "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarNotariaAbrev() {
        try {
            if (enju.getAbreviatura() != null) {
                map = new HashMap();
                map.put("abrev", enju.getAbreviatura());
                enju = (RegEnteJudiciales) manager.findObjectByParameter(Querys.getRegEnteJudicialByAbrev, map);
                if (enju != null) {
                    movimiento.setEnteJudicial(enju);
                } else {
                    enju = new RegEnteJudiciales();
                    JsfUti.messageInfo(null, Messages.sinCoincidencias, "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarPapelesByActo() {
        /*papeles = reg.getRegCatPapelByActo(acto.getId());
        if (Utils.isEmpty(papeles)) {
            papeles = manager.findAllEntCopy(Querys.getRegPapelesList);
        }*/
        JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
    }

    public void selectObjectActo(SelectEvent event) {
        acto = (RegActo) event.getObject();
        movimiento.setActo(acto);
        //this.cargarPapelesByActo();
    }

    public void selectObjectJudicial(SelectEvent event) {
        enju = (RegEnteJudiciales) event.getObject();
        movimiento.setEnteJudicial(enju);
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
        ss.borrarDatos();
    }*/
    public void selectObjectAntecedente(SelectEvent event) {
        try {
            RegMovimiento m = (RegMovimiento) event.getObject();
            RegMovimientoReferencia mr = new RegMovimientoReferencia();
            mr.setMovimientoReferencia(m);
            mr.setActo(m.getActo());
            mr.setFechaInscripcion(m.getFechaInscripcion());
            mr.setInscripcion(m.getNumInscripcion());
            mr.setLibro(m.getLibro());
            mrs.add(mr);
            JsfUti.messageWarning(null, "Advertencia", "Esta marginacion no se guarda en el Documents.");
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

    public void deleteInterv(int index) {
        try {
            RegMovimientoCliente mc = mcs.remove(index);
            if (mc.getId() != null) {
                bs.registrarMovInterv(movimiento, mc.getEnteInterv(), ActividadesTransaccionales.ELIMINAR_INTERVINIENTE, periodo);
                manager.delete(mc);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void deleteMovsRef(int index) {
        try {
            RegMovimientoReferencia mr = mrs.remove(index);
            if (mr.getId() != null) {
                //bs.registrarMovMov(movimiento, mr, periodo, ActividadesTransaccionales.ELIMINAR_REFERENCIA);
                manager.delete(mr);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void deleteFicha(int index) {
        try {
            RegMovimientoFicha mf = mfs.remove(index);
            if (mf.getId() != null) {
                bs.registrarFichaMov(mf.getFicha(), movimiento, ActividadesTransaccionales.ELIMINAR_FICHA, periodo);
                manager.delete(mf);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarByNumFicha() {
        try {
            if (numFicha == null) {
                JsfUti.messageError(null, "Ingrese el Numero de Ficha para buscar.", "");
                return;
            }
            if (this.containsRegFicha(numFicha)) {
                JsfUti.messageError(null, "No se agrego la ficha", "Numero de ficha repetido o tipo de ficha es incorrecto.");
            } else {
                map = new HashMap();
                map.put("tipoFicha", tipoFicha);
                map.put("numFicha", numFicha);
                RegFicha rf = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
                if (rf == null) {
                    JsfUti.messageError(null, "No se encuentra Numero Ficha.", "");
                    return;
                }
                if (rf.getEstado().getValor().equalsIgnoreCase("INACTIVO")) {
                    JsfUti.messageError(null, "No se puede hacer referencia, estado de Ficha: INACTIVA.", "");
                    return;
                } else {
                    JsfUti.messageInfo(null, "El estado de la Ficha es: " + rf.getEstado().getValor(), "");
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

    public boolean containsInterviniente(String cedRuc) {
        for (RegMovimientoCliente mc : mcs) {
            if (mc.getEnteInterv().getCedRuc().equals(cedRuc)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsReferencia(Long idmov) {
        for (RegMovimientoReferencia mr : mrs) {
            if (Objects.equals(mr.getMovimientoReferencia().getId(), idmov)) {
                return true;
            }
        }
        return false;
    }

    public void showDlgNewInterv() {
        re = new RegEnteInterviniente();
        JsfUti.update("formCreaInterv");
        JsfUti.executeJS("PF('dlgCrearInterviniente').show();");
    }

    public void showDlgEditInterv(int index) {
        interv = mcs.get(index);
        JsfUti.update("formEditInterv");
        JsfUti.executeJS("PF('dlgEditInter').show();");
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
                        bs.registrarMovInterv(movimiento, re, ActividadesTransaccionales.MODIFICAR_INTERVINIENTE, periodo);
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

    /*public void agregarInterv() {
        RegMovimientoCliente mc = new RegMovimientoCliente();
        mc.setEnteInterv(re);
        mcs.add(mc);
        JsfUti.update("formNuevInsc:tVdetalle:dtInterviniente");
        JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
    }*/
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

    public void guardarMovimiento() {
        try {
            if (movimiento.getLibro() != null) {
                if (movimiento.getNumInscripcion() == null) {
                    movimiento.setNumInscripcion(0);
                }
                if (movimiento.getNumRepertorio() == null) {
                    movimiento.setNumRepertorio(0);
                }
                // mrs
                /*if (Utils.isNotEmpty(mrs)) {
                    for (RegMovimientoReferencia mr : mrs) {
                        if (mr.getMovimientoReferencia().getRegistro() == null) {
                            JsfUti.messageError(null, "Referencia", "Falta seleccionar el estado de la inscripcion " 
                                    + mr.getMovimientoReferencia().getNumInscripcion());
                            return;
                        }
                    }
                }*/
                movimiento.setFechaMod(new Date());
                movimiento.setUsuarioMod(session.getUserId().intValue());
                movimiento.setEditable(false);
                //movimiento = reg.guardarInscripcionEdicion(movimiento, mrs, mfs, mcs, mma, mm);
                movimiento = reg.guardarInscripcionEdicion(movimiento, mrs, mfs, mcs, mcps, mres, msos, mm);
                if (movimiento != null) {
                    mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                    mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                    mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                    mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                    mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                    msos = reg.getRegMovSocioByIdMov(movimiento.getId());
                    JsfUti.messageInfo(null, "Guardado Exitoso", "");
                    JsfUti.update("formNuevInsc");
                    //JsfUti.redirectFaces("/procesos/manage/inscripciones.xhtml");
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

    public void guardarMovimientoRedirigirFicha() {
        try {
            if (movimiento.getLibro() != null) {
                if (movimiento.getNumInscripcion() == null) {
                    movimiento.setNumInscripcion(0);
                }
                if (movimiento.getNumRepertorio() == null) {
                    movimiento.setNumRepertorio(0);
                }
                // mrs
                if (Utils.isNotEmpty(mrs)) {
                    for (RegMovimientoReferencia mr : mrs) {
                        if (mr.getMovimientoReferencia().getRegistro() == null) {
                            JsfUti.messageError(null, "Referencia", "Falta seleccionar el estado de la inscripcion " + mr.getMovimientoReferencia().getNumInscripcion());
                            return;
                        }
                    }
                }
                movimiento.setFechaMod(new Date());
                movimiento.setUsuarioMod(session.getUserId().intValue());
                movimiento.setEditable(false);
                //movimiento = reg.guardarInscripcionEdicion(movimiento, mrs, mfs, mcs, mma, mm);
                movimiento = reg.guardarInscripcionEdicion(movimiento, mrs, mfs, mcs, mcps, mres, msos, mm);
                if (movimiento != null) {
                    //mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                    //mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                    //mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                    //mma = reg.getRegMovMargByIdMov(movimiento.getId());
                    //mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                    //mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                    //msos = reg.getRegMovSocioByIdMov(movimiento.getId());
                    //JsfUti.messageInfo(null, Messages.transaccionOK, "");
                    if (ficha != null && ficha.getId() != null) {
                        ss.instanciarParametros();
                        ss.agregarParametro("idFicha", ficha.getId());
                        JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
                    } else {
                        JsfUti.redirectFaces("/procesos/manage/inscripciones.xhtml");
                    }
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

    public void corregirMovimiento() {
        try {
            if (movimiento.getFechaInscripcion() != null && movimiento.getLibro() != null && movimiento.getNumRepertorio() != null) {
                if (movimiento.getNumInscripcion() == null) {
                    movimiento.setNumInscripcion(0);
                }
                if (movimiento.getNumRepertorio() == null) {
                    movimiento.setNumRepertorio(0);
                }
                movimiento.setFechaMod(new Date());
                movimiento.setUsuarioMod(session.getUserId().intValue());
                //movimiento = reg.guardarInscripcionEdicion(movimiento, mrs, mfs, mcs, mma, mm);
                movimiento = reg.guardarInscripcionEdicion(movimiento, mrs, mfs, mcs, mcps, mres, msos, mm);
                if (movimiento != null) {
                    //mcs = reg.getRegMovClienteByIdMov(movimiento.getId());
                    //mfs = reg.getRegMovFichaByIdMov(movimiento.getId());
                    //mrs = reg.getRegMovRefByIdRegMov(movimiento.getId());
                    //mcps = reg.getRegMovCapByIdMov(movimiento.getId());
                    //mres = reg.getRegMovRepreByIdMov(movimiento.getId());
                    //msos = reg.getRegMovSocioByIdMov(movimiento.getId());
                    //JsfUti.messageInfo(null, Messages.transaccionOK, "");

                    map = new HashMap();
                    map.put("id", movimiento.getTramite().getId());
                    tareasTramite = (RegpTareasTramite) manager.findObjectByParameter(RegpTareasTramite.class, map);

                    tareasTramite.setCorregido(Boolean.TRUE);
                    em.persist(tareasTramite);

                    ss.borrarDatos();
                    JsfUti.redirectFaces("/admin/manage/solicitudCorreccion.xhtml");
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

    public void showDlgViewMarginacion(RegMovimientoMarginacion rmm) {
        marg = rmm;
        JsfUti.update("formViewMarginacion");
        JsfUti.executeJS("PF('dlgViewMarginacion').show();");
    }

    public void agregarMarginacion() {
        if (marg.getObservacion() != null) {
            marg.setFechaIngreso(new Date());
            marg.setUserIngreso(session.getUserId());
            //mma.add(marg);
            JsfUti.update("formNuevInsc:tVdetalle:dtMarginacion");
            JsfUti.executeJS("PF('dlgMarginacion').hide();");
        } else {
            JsfUti.messageWarning(null, "Debe ingresar contenido de texto a la marginacion.", "");
        }
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

    public void showDlgNewInterv(int tipo) {
        this.interviniente = tipo;
        re = new RegEnteInterviniente();
        JsfUti.update("formCreaInterv");
        JsfUti.executeJS("PF('dlgCrearInterviniente').show();");
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

    public List<RegDomicilio> getDomicilios() {
        return manager.findAllEntCopy(Querys.getRegDomicilioList);
    }

    public Collection<RegPapel> getPapeles() {
        return manager.findAllEntCopy(Querys.getRegPapelesList);
    }

    public List<RegLibro> getLibros() {
        //return manager.findAllEntCopy(Querys.getRegLibroListOrd);
        return manager.findAllEntCopy(Querys.getRegAllLibros);
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

    public List<CtlgItem> getEstadosCivil() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        return manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public List<CtlgItem> getCodigosTiempo() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.codigosTiempo);
        return manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public void guardarMarginacion() {
        try {
            if (this.validarMarginacion()) {
                movref.setMovimiento(movimiento.getId());
                movref = reg.guardarMarginacion(movref);
                mrs.add(movref);
                JsfUti.update("formNuevInsc:tVdetalle:dtMovimientosReff");
                JsfUti.executeJS("PF('dlgMarginacion').hide();");
                JsfUti.messageInfo(null, "Registro ingresado con exito.", "");
            } else {
                JsfUti.messageWarning(null, "Se debe ingresar todos los campos.", "");
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

    public void selectObjectActoRef(SelectEvent event) {
        RegActo acto = (RegActo) event.getObject();
        movref.getMovimientoReferencia().setActo(acto);
    }
    
    public List<RegEstadoMovimiento> getEstadosInscripcion() {
        return manager.findAllEntCopy(Querys.getRegEstadosInscripcion);
    }

    public Boolean getIngreso() {
        return ingreso;
    }

    public void setIngreso(Boolean ingreso) {
        this.ingreso = ingreso;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    /*public List<RegLibro> getRegLibroList() {
        return regLibroList;
    }

    public void setRegLibroList(List<RegLibro> regLibroList) {
        this.regLibroList = regLibroList;
    }*/
    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public List<RegMovimientoCliente> getMcs() {
        return mcs;
    }

    public void setMcs(List<RegMovimientoCliente> mcs) {
        this.mcs = mcs;
    }

    public RegEnteJudiciales getEnju() {
        return enju;
    }

    public void setEnju(RegEnteJudiciales enju) {
        this.enju = enju;
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

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    /*public void setPapeles(Collection<RegPapel> papeles) {
        this.papeles = papeles;
    }*/
    public RegEnteInterviniente getRe() {
        return re;
    }

    public void setRe(RegEnteInterviniente re) {
        this.re = re;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public RegMovimientoCliente getInterv() {
        return interv;
    }

    public void setInterv(RegMovimientoCliente interv) {
        this.interv = interv;
    }

    /*public List<RegMovimientoMarginacion> getMma() {
        return mma;
    }

    public void setMma(List<RegMovimientoMarginacion> mma) {
        this.mma = mma;
    }*/
    public RegMovimientoMarginacion getMarg() {
        return marg;
    }

    public void setMarg(RegMovimientoMarginacion marg) {
        this.marg = marg;
    }

    public BigInteger getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigInteger periodo) {
        this.periodo = periodo;
    }

    public RegpTareasTramite getTareasTramite() {
        return tareasTramite;
    }

    public void setTareasTramite(RegpTareasTramite tareasTramite) {
        this.tareasTramite = tareasTramite;
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

    public RegMovimientoReferencia getMovref() {
        return movref;
    }

    public void setMovref(RegMovimientoReferencia movref) {
        this.movref = movref;
    }

}
