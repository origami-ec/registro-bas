/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.indexacion;

import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.OrdenFichas;
import com.origami.sgr.entities.OrdenFichasDetalle;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.OrdenFichasLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
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

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class ManageTramitesFicha implements Serializable {

    private static final Logger LOG = Logger.getLogger(ManageTramitesFicha.class.getName());

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;

    @Inject
    private SeqGenMan sec;

    @Inject
    protected RegistroPropiedadServices reg;

    protected Map map;
    protected Date desde = new Date();
    protected Date hasta = new Date();
    protected Date periodo = new Date();
    protected OrdenFichas orden;
    protected OrdenFichasDetalle detalle;
    protected OrdenFichasLazy ordenes;
    protected AclUser user;
    protected Calendar cal;
    protected List<RegMovimiento> movimientos;
    protected List<RegMovimiento> movs2;
    protected List<OrdenFichasDetalle> detalles;
    protected Boolean admin = false, revisor = false;
    protected List<AclUser> usuarios = new ArrayList<>();
    protected SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

    @PostConstruct
    protected void iniView() {
        cal = Calendar.getInstance();
        orden = new OrdenFichas();
        movimientos = new ArrayList<>();
        ordenes = new OrdenFichasLazy();
        usuarios = reg.getUsuariosByRolName("digitador_ficha");
        this.validaRoles();
    }

    public void buscarMovimientos() {
        try {
            if (desde != null && hasta != null) {
                movimientos = em.findAll(Querys.getMovsSinFicha, new String[]{"desde", "hasta"}, new Object[]{desde, hasta});
            } else {
                JsfUti.messageWarning(null, "Debe ingresar las fechas desde y hasta.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        admin = us.getRoles().contains(1L);
        revisor = us.getRoles().contains(44L) || us.getRoles().contains(1L);
    }

    public void showDlgNewOrder() {
        JsfUti.update("frmOrder");
        JsfUti.executeJS("PF('dlgOrder').show();");
    }

    public void saveOrder() {
        try {
            if (periodo != null && user != null) {
                cal.setTime(periodo);
                map = new HashMap();
                map.put("mesOrden", cal.get(Calendar.MONTH) + 1);
                map.put("anioOrden", cal.get(Calendar.YEAR));
                orden = (OrdenFichas) em.findObjectByParameter(OrdenFichas.class, map);
                if (orden == null) {
                    orden = new OrdenFichas();
                    orden.setAnioOrden(cal.get(Calendar.YEAR));
                    orden.setEstado(1L);
                    orden.setFechaOrden(new Date());
                    orden.setMesOrden(cal.get(Calendar.MONTH) + 1);
                    orden.setTipoOrden(1);
                    orden.setUsuario(user);
                    orden.setNumOrden(sec.getSecuenciaGeneral(Constantes.secuenciaNumeroOrden).intValue());
                    orden = (OrdenFichas) em.merge(orden);
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgOrder').hide();");
                    JsfUti.messageInfo(null, "Nueva orden generada con el numero: " + orden.getNumOrden(), "");
                } else {
                    JsfUti.messageWarning(null, "Ya esta registrado la orden con el periodo: " + orden.getPeriodo(), "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el periodo y el usuario.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void realizarOrden(OrdenFichas of) {
        try {
            orden = of;
            cal.set(orden.getAnioOrden(), orden.getMesOrden() - 1, 1);
            movs2 = em.findAll(Querys.getMovsSinFichaPeriodo, new String[]{"periodo"}, new Object[]{sdf.format(cal.getTime())});
            //detalles = (List<OrdenFichasDetalle>) orden.getOrdenFichasDetallesCollection();
            detalles = em.findAll(Querys.getDetallesOrden, new String[]{"idOrder"}, new Object[]{orden.getId()});
            JsfUti.update("formRealizarOrden");
            JsfUti.executeJS("PF('dlgRealizarOrden').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void registrarOrden(RegMovimiento mov) {
        try {
            map = new HashMap();
            map.put("movimiento", mov);
            detalle = (OrdenFichasDetalle) em.findObjectByParameter(OrdenFichasDetalle.class, map);
            if (detalle == null) {
                detalle = new OrdenFichasDetalle();
                detalle.setFechaInicio(new Date());
                detalle.setMovimiento(mov);
                detalle.setOrdenFichas(orden);
                detalle.setNameUser(us.getName_user());
                em.merge(detalle);
                detalles = em.findAll(Querys.getDetallesOrden, new String[]{"idOrder"}, new Object[]{orden.getId()});
                JsfUti.update("formRealizarOrden");
                JsfUti.messageInfo(null, "Detalle ingresado para la orden numero: " + orden.getNumOrden(), "");
            } else {
                JsfUti.messageWarning(null, "El movimiento ya esta registrado para la orden numero: " + detalle.getOrdenFicha().getNumOrden(), "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void marcarFinalizada(OrdenFichasDetalle ofd) {
        try {
            List temp = reg.getRegMovFichaByIdMov(ofd.getMovimiento().getId());
            if (temp.isEmpty()) {
                JsfUti.messageWarning(null, "No se puede finalizar, el movimiento no tiene ficha enlazada.", "");
            } else {
                ofd.setFechaFin(new Date());
                em.update(ofd);
                detalles = em.findAll(Querys.getDetallesOrden, new String[]{"idOrder"}, new Object[]{orden.getId()});
                JsfUti.update("formRealizarOrden");
                JsfUti.messageInfo(null, "Detalle marcado como finalizado.", "");
            }
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

    public List<RegMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public OrdenFichasLazy getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(OrdenFichasLazy ordenes) {
        this.ordenes = ordenes;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public List<AclUser> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<AclUser> usuarios) {
        this.usuarios = usuarios;
    }

    public OrdenFichas getOrden() {
        return orden;
    }

    public void setOrden(OrdenFichas orden) {
        this.orden = orden;
    }

    public List<RegMovimiento> getMovs2() {
        return movs2;
    }

    public void setMovs2(List<RegMovimiento> movs2) {
        this.movs2 = movs2;
    }

    public List<OrdenFichasDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenFichasDetalle> detalles) {
        this.detalles = detalles;
    }

}
