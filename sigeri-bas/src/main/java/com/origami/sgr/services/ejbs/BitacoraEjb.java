/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.entities.AclLogin;
import com.origami.sgr.entities.RegBitacora;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.MovimientoModel;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.SessionServiceLocal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 *
 * @author CarlosLoorVargas
 */
@Stateless(name = "bitacora")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class BitacoraEjb implements BitacoraServices {

    @EJB(beanName = "manager")
    private Entitymanager em;

    @EJB(beanName = "sessionServices")
    private SessionServiceLocal ss;

    private RegBitacora rb;

    /**
     * metodo que registra en la bitacora un ingreso en la base de datos de una
     * ficha registral
     *
     * @param f Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @param orden Object
     * @return Object
     */
    @Override
    public Object registrarFicha(RegFicha f, ActividadesTransaccionales actividadTransaccional, BigInteger periodo, BigInteger orden) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        Object reg = null;
        String actividad = actividadTransaccion(actividadTransaccional, f, null, null, null);
        try {
            if (!actividad.equalsIgnoreCase(actividadTransaccional.getDescripcion() + "\n")) {
                rb = new RegBitacora();
                rb.setCodUsuario(codUsu);
                rb.setIdUsuario(codUsu);
                rb.setActividad(actividad);
                rb.setFecha(new Date());
                rb.setFechaHora(new Date());
                rb.setCodPrograma("SGReg");
                rb.setAnio(periodo);
                if (orden != null) {
                    rb.setRepertorioOrdenTramite(orden);
                }
                rb.setNumFicha(new BigInteger(f.getNumFicha() + ""));
                rb.setIdFicha(new BigInteger(f.getId().toString()));
                rb.setTipServicio(new BigInteger("2"));
                rb.setIpCliente(login.getIpUserSession());
                rb.setMacCliente(login.getMacClient());
                reg = em.persist(rb);
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return reg;
    }

    /**
     * metodo que registra en la bitacora un ingreso en la base de datos de un
     * movimiento registral
     *
     * @param movimientoModel Object
     * @param m Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public Object registrarMovimiento(MovimientoModel movimientoModel, RegMovimiento m, ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        Object reg = null;
        String actividad = this.actividadTransaccion(actividadTransaccional, null, movimientoModel, m, null);
        try {
            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setFechaInscripcion(m.getFechaInscripcion());
            rb.setIdMovimiento(new BigInteger(m.getId().toString()));
            rb.setTipServicio(new BigInteger("1"));
            rb.setIndice(new BigInteger(m.getIndice().toString()));
            if (m.getNumInscripcion() != null) {
                rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
            }
            if (m.getNumRepertorio() != null) {
                rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
            }
            rb.setIpCliente(login.getIpUserSession());
            rb.setMacCliente(login.getMacClient());
            reg = em.persist(rb);
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return reg;
    }

    /**
     * registra el ingreso en la base de datos de la relacion de una ficha
     * registral con un propietario
     *
     * @param f Object
     * @param rei Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarFichaProp(RegFicha f, RegEnteInterviniente rei, ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        String actividad = actividadTransaccional.getDescripcion() + "\n";
        actividad = actividad + "DOCUMENTO: " + rei.getCedRuc() + ", NOMBRES: " + rei.getNombre() + "\n";
        try {
            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setCedRuc(rei.getCedRuc());
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setNumFicha(new BigInteger(f.getNumFicha().toString()));
            rb.setIdFicha(new BigInteger(f.getId().toString()));
            rb.setTipServicio(new BigInteger("2"));
            rb.setIpCliente(login.getIpUserSession());
            rb.setMacCliente(login.getMacClient());
            em.persist(rb);
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre una ficha registral y un movimiento registral
     *
     * @param f Object
     * @param mov Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarFichaMov(RegFicha f, RegMovimiento mov, ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        boolean flag = false;
        String actividad;
        try {
            if (f != null) {
                actividad = actividadTransaccion(actividadTransaccional, f, null, mov, null);
                rb = new RegBitacora();
                rb.setCodUsuario(codUsu);
                rb.setIdUsuario(codUsu);
                rb.setActividad(actividad);
                rb.setFecha(new Date());
                rb.setFechaHora(new Date());
                rb.setCodPrograma("SGReg");
                rb.setAnio(periodo);
                rb.setNumFicha(new BigInteger(f.getNumFicha() + ""));
                rb.setIdFicha(new BigInteger(f.getId().toString()));
                rb.setFechaInscripcion(mov.getFechaInscripcion());
                rb.setIdMovimiento(new BigInteger(mov.getId().toString()));
                rb.setTipServicio(new BigInteger("1"));
                rb.setIndice(new BigInteger(mov.getIndice().toString()));
                rb.setNumInscripcion(new BigInteger(mov.getNumInscripcion().toString()));
                if (mov.getNumRepertorio() != null) {
                    rb.setRepertorioOrdenTramite(new BigInteger(mov.getNumRepertorio().toString()));
                }
                rb.setIpCliente(login.getIpUserSession());
                rb.setMacCliente(login.getMacClient());
                em.persist(rb);
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre una ficha registral y una lista de movimientos registrales
     *
     * @param f Object
     * @param movs Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarFichaMovs(RegFicha f, List<RegMovimiento> movs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        boolean flag = false;
        String actividad;
        try {
            if (f != null) {
                actividad = actividadTransaccion(actividadTransaccional, f, null, null, null);
                for (RegMovimiento m : movs) {
                    rb = new RegBitacora();
                    rb.setCodUsuario(codUsu);
                    rb.setIdUsuario(codUsu);
                    rb.setActividad(actividad);
                    rb.setFecha(new Date());
                    rb.setFechaHora(new Date());
                    rb.setCodPrograma("SGReg");
                    rb.setAnio(periodo);
                    rb.setNumFicha(new BigInteger(f.getNumFicha() + ""));
                    rb.setIdFicha(new BigInteger(f.getId().toString()));
                    rb.setFechaInscripcion(m.getFechaInscripcion());
                    rb.setIdMovimiento(new BigInteger(m.getId().toString()));
                    rb.setTipServicio(new BigInteger("1"));
                    if (m.getIndice() != null) {
                        rb.setIndice(new BigInteger(m.getIndice().toString()));
                    }
                    if (m.getNumInscripcion() != null) {
                        rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
                    }
                    if (m.getNumRepertorio() != null) {
                        rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
                    }
                    rb.setIpCliente(login.getIpUserSession());
                    rb.setMacCliente(login.getMacClient());
                    em.persist(rb);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre un movimiento registral y una lista de fichas registrales
     *
     * @param m Object
     * @param fs Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarMovFichas(RegMovimiento m, List<RegMovimientoFicha> fs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        boolean flag = false;
        String actividad;
        try {
            //actividad = this.actividadTransaccion(actividadTransaccional, null, null, null);
            //MODIFICACION: 02-12-2016
            actividad = this.actividadTransaccion(actividadTransaccional, null, null, m, null);
            for (RegMovimientoFicha f : fs) {
                rb = new RegBitacora();
                rb.setCodUsuario(codUsu);
                rb.setIdUsuario(codUsu);
                rb.setActividad(actividad);
                rb.setFecha(new Date());
                rb.setFechaHora(new Date());
                rb.setCodPrograma("SGReg");
                rb.setAnio(periodo);
                rb.setNumFicha(new BigInteger(f.getFicha().getNumFicha().toString()));
                rb.setIdFicha(new BigInteger(f.getFicha().getId().toString()));
                rb.setTipServicio(new BigInteger("1"));
                rb.setFechaInscripcion(m.getFechaInscripcion());
                rb.setIdMovimiento(new BigInteger(m.getId().toString()));
                rb.setIndice(new BigInteger(m.getIndice().toString()));
                if (m.getNumInscripcion() != null) {
                    rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
                }
                if (m.getNumRepertorio() != null) {
                    rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
                }
                rb.setIpCliente(login.getIpUserSession());
                rb.setMacCliente(login.getMacClient());
                em.persist(rb);
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre un movimiento registral y los intervientes de dicha inscripcion
     *
     * @param m Object
     * @param mcs Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarMovProps(RegMovimiento m, List<RegMovimientoCliente> mcs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        boolean flag = false;
        String actividad;
        try {
            for (RegMovimientoCliente mc : mcs) {
                actividad = actividadTransaccional.getDescripcion() + "\n";
                actividad = actividad + "DOCUMENTO: " + mc.getEnteInterv().getCedRuc() + ", NOMBRES: " + mc.getEnteInterv().getNombre() + "\n";
                rb = new RegBitacora();
                rb.setCodUsuario(codUsu);
                rb.setIdUsuario(codUsu);
                rb.setCedRuc(mc.getEnteInterv().getCedRuc());
                rb.setActividad(actividad);
                rb.setFecha(new Date());
                rb.setFechaHora(new Date());
                rb.setCodPrograma("SGReg");
                rb.setAnio(periodo);
                rb.setTipServicio(new BigInteger("1"));
                rb.setFechaInscripcion(m.getFechaInscripcion());
                rb.setIdMovimiento(new BigInteger(m.getId().toString()));
                rb.setIndice(new BigInteger(m.getIndice().toString()));
                if (m.getNumInscripcion() != null) {
                    rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
                }
                if (m.getNumRepertorio() != null) {
                    rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
                }
                rb.setIpCliente(login.getIpUserSession());
                rb.setMacCliente(login.getMacClient());
                em.persist(rb);
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre un movimiento registral y un movimiento de referencia
     *
     * @param m Object
     * @param mv Object
     * @param periodo Object
     * @param actividadTransaccional Object
     * @return Object
     */
    @Override
    public boolean registrarMovMov(RegMovimiento m, RegMovimientoReferencia mv, BigInteger periodo, ActividadesTransaccionales actividadTransaccional) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        String actividad;
        try {
            actividad = this.actividadTransaccion(actividadTransaccional, null, null, null, null);
            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setFechaInscripcion(m.getFechaInscripcion());
            rb.setIdMovimiento(new BigInteger(m.getId().toString()));
            rb.setTipServicio(new BigInteger("1"));
            rb.setIndice(new BigInteger(m.getIndice().toString()));
            if (m.getNumInscripcion() != null) {
                rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
            }
            if (m.getNumRepertorio() != null) {
                rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
            }
            em.persist(rb);

            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setFechaInscripcion(mv.getMovimientoReferencia().getFechaInscripcion());
            rb.setIdMovimiento(new BigInteger(mv.getId().toString()));
            rb.setTipServicio(new BigInteger("1"));
            rb.setIndice(new BigInteger(mv.getMovimientoReferencia().getIndice().toString()));
            rb.setNumInscripcion(new BigInteger(mv.getMovimientoReferencia().getNumInscripcion().toString()));
            rb.setRepertorioOrdenTramite(new BigInteger(mv.getMovimientoReferencia().getNumRepertorio().toString()));
            rb.setIpCliente(login.getIpUserSession());
            rb.setMacCliente(login.getMacClient());
            em.persist(rb);
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre un movimiento registral y una lista de movimientos de referencia
     *
     * @param m Object
     * @param movs Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarMovMovs(RegMovimiento m, List<RegMovimientoReferencia> movs, ActividadesTransaccionales actividadTransaccional,
            BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        String actividad;
        boolean flag = false;
        try {
            actividad = this.actividadTransaccion(actividadTransaccional, null, null, null, null);
            for (RegMovimientoReferencia mv : movs) {
                rb = new RegBitacora();
                rb.setCodUsuario(codUsu);
                rb.setIdUsuario(codUsu);
                rb.setActividad(actividad);
                rb.setFecha(new Date());
                rb.setFechaHora(new Date());
                rb.setCodPrograma("SGReg");
                rb.setAnio(periodo);
                rb.setFechaInscripcion(mv.getMovimientoReferencia().getFechaInscripcion());
                rb.setIdMovimiento(new BigInteger(m.getId().toString()));
                rb.setTipServicio(new BigInteger("1"));
                rb.setIndice(new BigInteger(mv.getMovimientoReferencia().getIndice().toString()));
                if (mv.getMovimientoReferencia().getNumInscripcion() != null) {
                    rb.setNumInscripcion(new BigInteger(mv.getMovimientoReferencia().getNumInscripcion().toString()));
                }
                if (mv.getMovimientoReferencia().getNumRepertorio() != null) {
                    rb.setRepertorioOrdenTramite(new BigInteger(mv.getMovimientoReferencia().getNumRepertorio().toString()));
                }
                rb.setIpCliente(login.getIpUserSession());
                rb.setMacCliente(login.getMacClient());
                em.persist(rb);
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre un movimiento registral y un interviniente
     *
     * @param m Object
     * @param rei Object
     * @param actividadTransaccional Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarMovInterv(RegMovimiento m, RegEnteInterviniente rei,
            ActividadesTransaccionales actividadTransaccional, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        String actividad = actividadTransaccional.getDescripcion() + "\n";
        actividad = actividad + "DOCUMENTO: " + rei.getCedRuc() + ", NOMBRES: " + rei.getNombre() + "\n";
        try {
            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setFechaInscripcion(m.getFechaInscripcion());
            rb.setIdMovimiento(new BigInteger(m.getId().toString()));
            rb.setTipServicio(new BigInteger("1"));
            rb.setIndice(new BigInteger(m.getIndice().toString()));
            if (m.getNumInscripcion() != null) {
                rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
            }
            if (m.getNumRepertorio() != null) {
                rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
            }
            rb.setIpCliente(login.getIpUserSession());
            rb.setMacCliente(login.getMacClient());
            em.persist(rb);
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * registra en la bitacora el ingreso en la base de datos de una relacion
     * entre un movimiento registral y la observacion que se ingresa como
     * marginacion
     *
     * @param m Object
     * @param observacion Object
     * @param periodo Object
     * @return Object
     */
    @Override
    public boolean registrarMovMarginacion(RegMovimiento m, String observacion, BigInteger periodo) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        String actividad = "AGREGAR MARGINACION\n" + observacion;
        try {
            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setFechaInscripcion(m.getFechaInscripcion());
            rb.setIdMovimiento(new BigInteger(m.getId().toString()));
            rb.setTipServicio(new BigInteger("1"));
            rb.setIndice(new BigInteger(m.getIndice().toString()));
            if (m.getNumInscripcion() != null) {
                rb.setNumInscripcion(new BigInteger(m.getNumInscripcion().toString()));
            }
            if (m.getNumRepertorio() != null) {
                rb.setRepertorioOrdenTramite(new BigInteger(m.getNumRepertorio().toString()));
            }
            rb.setIpCliente(login.getIpUserSession());
            rb.setMacCliente(login.getMacClient());
            em.persist(rb);
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    public String actividadTransaccion(ActividadesTransaccionales actividad, RegFicha ficha, MovimientoModel movimientoAnterior, RegMovimiento movimiento, RegEnteInterviniente rei) {
        String detalleActividad = actividad.getDescripcion() + "\n";
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
        try {
            switch (actividad) {
                case GENERACION_FICHA:
                    detalleActividad = detalleActividad
                            + (ficha.getClaveCatastral() == null ? "" : "Codigo: " + ficha.getClaveCatastral() + "\n")
                            + (ficha.getDescripcionBien() == null ? "" : "DESCRIPCION: " + ficha.getDescripcionBien() + "\n")
                            + (ficha.getLinderos() == null ? "" : "Lindero: " + ficha.getLinderos() + "\n");
                    break;

                case DATOS_REGISTRALES:
                    detalleActividad = detalleActividad
                            + (ficha.getLinderos() == null ? "" : "Lindero: " + ficha.getLinderos() + "\n")
                            + (ficha.getDescripcionBien() == null ? "" : "Descripcion: " + ficha.getDescripcionBien() + "\n");
                    break;

                case MODIFICAR_FICHA:
                    RegFicha fichaAnterior = em.find(RegFicha.class, ficha.getId());
                    if (!fichaAnterior.getEstado().equals(ficha.getEstado())) {
                        if (ficha.getEstado().getCodename().equalsIgnoreCase("activo")) {
                            detalleActividad = detalleActividad + ActividadesTransaccionales.ACTIVAR_FICHA.getDescripcion() + "\n";
                        } else {
                            detalleActividad = detalleActividad + ActividadesTransaccionales.INACTIVAR_FICHA.getDescripcion() + ficha.getEstado().getValor() + "\n";
                        }
                    }
                    if (fichaAnterior.getLinderos() != null && !fichaAnterior.getLinderos().equals(ficha.getLinderos())) {
                        detalleActividad = detalleActividad + "LINDERO ANTERIOR: " + fichaAnterior.getLinderos() + "\n";
                    }
                    if (fichaAnterior != null) {
                        if (fichaAnterior.getDescripcionBien() != null && !fichaAnterior.getDescripcionBien().equals(ficha.getDescripcionBien())) {
                            detalleActividad = detalleActividad + "DESCRIPCION ANTERIOR: " + fichaAnterior.getDescripcionBien() + "\n";
                        }
                    } else {
                        if (!ficha.getDescripcionBien().equals(fichaAnterior.getDescripcionBien())) {
                            detalleActividad = detalleActividad + "DESCRIPCION ANTERIOR: " + fichaAnterior.getDescripcionBien() + "\n";
                        }
                    }
                    if (fichaAnterior.getClaveCatastral() != null && !fichaAnterior.getClaveCatastral().equals(ficha.getClaveCatastral())) {
                        detalleActividad = detalleActividad + "CODIGO PREDIAL ANTERIOR: " + fichaAnterior.getClaveCatastral() + "\n";
                    }
                    if (fichaAnterior.getParroquia() != null && !fichaAnterior.getParroquia().equals(ficha.getParroquia())) {
                        detalleActividad = detalleActividad + "PARROQUIA ANTERIOR: " + fichaAnterior.getParroquia().getDescripcion() + "\n";
                    }
                    if (fichaAnterior.getTipoPredio() != null && !fichaAnterior.getTipoPredio().equals(ficha.getTipoPredio())) {
                        detalleActividad = detalleActividad + "TIPO ANTERIOR: " + fichaAnterior.getTipoPredio() + "\n";
                    }
                    /*if ((fichaAnterior.getCiudadela() != null && !fichaAnterior.getCiudadela().equals(ficha.getCiudadela())) || (fichaAnterior.getCiudadela() == null && ficha.getCiudadela() != null)) {
                        detalleActividad = detalleActividad + "CIUDADELA ANTERIOR: " + (fichaAnterior.getCiudadela() == null ? "" : fichaAnterior.getCiudadela().getNombre()) + "\n";
                    }*/
                    break;

                case INGRESO_INSCRIPCION:
                    if (movimiento.getActo() != null) {
                        detalleActividad = detalleActividad + "ACTO: " + movimiento.getActo().getNombre() + "\n";
                    }
                    if (movimiento.getEnteJudicial() != null) {
                        detalleActividad = detalleActividad + "NOTARIA/JUZG: " + movimiento.getEnteJudicial().getNombre() + "\n";
                    }
                    if (movimiento.getFechaInscripcion() != null) {
                        detalleActividad = detalleActividad + "FECHA INSCRIPCION: " + fecha.format(movimiento.getFechaInscripcion()) + "\n";
                    }
                    if (movimiento.getFechaOto() != null) {
                        detalleActividad = detalleActividad + "FECHA OTORGAMIENTO: " + fecha.format(movimiento.getFechaOto()) + "\n";
                    }
                    if (movimiento.getObservacion() != null && !movimiento.getObservacion().isEmpty()) {
                        detalleActividad = detalleActividad + "OBSERVACION: " + movimiento.getObservacion() + "\n";
                    }
                    break;

                case MODIFICACION_INSCRIPCION:
                    detalleActividad = detalleActividad + "DATOS ANTERIORES: \n";
                    if (movimientoAnterior.getCodigoCan() != null && !movimientoAnterior.getCodigoCan().equals(movimiento.getDomicilio())) {
                        detalleActividad = detalleActividad + "CANTON: " + movimientoAnterior.getCodigoCan().getNombre() + "\n";
                    }
                    if (movimientoAnterior.getNumTomo() != null && !movimientoAnterior.getNumTomo().equals(movimiento.getNumTomo())) {
                        detalleActividad = detalleActividad + "TOMO: " + movimientoAnterior.getNumTomo() + "\n";
                    }
                    if (movimientoAnterior.getActo() != null && movimiento.getActo() != null && !movimientoAnterior.getActo().getId().equals(movimiento.getActo().getId())) {
                        detalleActividad = detalleActividad + "ACTO: " + movimientoAnterior.getActo().getNombre() + "\n";
                    }
                    if (movimientoAnterior.getEnteJudicial() != null && movimiento.getEnteJudicial() != null && !movimiento.getEnteJudicial().equals(movimientoAnterior.getEnteJudicial())) {
                        detalleActividad = detalleActividad + "NOTARIA/JUZG: " + movimientoAnterior.getEnteJudicial().getNombre() + "\n";
                    }
                    if (movimientoAnterior.getFechaInscripcion() != null && movimiento.getFechaInscripcion() != null && movimientoAnterior.getFechaInscripcion().getTime() != movimiento.getFechaInscripcion().getTime()) {
                        detalleActividad = detalleActividad + "FECHA INSCRIPCION: " + fecha.format(movimientoAnterior.getFechaInscripcion()) + "\n";
                    }
                    if (movimientoAnterior.getFechaOto() != null && movimiento.getFechaOto() != null && movimientoAnterior.getFechaOto().getTime() != movimiento.getFechaOto().getTime()) {
                        detalleActividad = detalleActividad + "FECHA OTORGAMIENTO: " + fecha.format(movimientoAnterior.getFechaOto()) + "\n";
                    }
//                    if (movimientoAnterior.getFolioInicio() != null && !movimientoAnterior.getFolioInicio().equals(movimiento.getFolioInicio()) || !movimientoAnterior.getFolioFin().equals(movimiento.getFolioFin())) {
                    if (movimiento.getFolioInicio() != null) {
                        if (movimientoAnterior.getFolioInicio() != null && !movimiento.getFolioInicio().equals(movimientoAnterior.getFolioInicio()) || !movimiento.getFolioFin().equals(movimientoAnterior.getFolioFin())) {
                            detalleActividad = detalleActividad + "FOLIO INICIAL: " + movimientoAnterior.getFolioInicio() + " \tFOLIO FINAL: " + movimientoAnterior.getFolioFin() + "\n";
                        }
                    }
                    if (movimiento.getOrdJud() != null) {
//                    if (!movimientoAnterior.getOrdJud().equals(movimiento.getOrdJud())) {
                        if (!movimiento.getOrdJud().equals(movimientoAnterior.getOrdJud())) {
                            detalleActividad = detalleActividad + "ORDEN JUDICIAL: " + movimientoAnterior.getOrdJud() + "\n";
                        }
                    }
//                    if (movimientoAnterior.getEscritJuicProvResolucion() != null && !movimientoAnterior.getEscritJuicProvResolucion().equals(movimiento.getEscritJuicProvResolucion())) {
                    if (movimientoAnterior.getEscritJuicProvResolucion() != null && !movimiento.getEscritJuicProvResolucion().equals(movimientoAnterior.getEscritJuicProvResolucion())) {
                        detalleActividad = detalleActividad + "Escrit/Juic/Provi/Resolucion: " + movimientoAnterior.getEscritJuicProvResolucion() + "\n";
                    }
                    if (movimientoAnterior.getObservacion() != null && !movimientoAnterior.getObservacion().equals(movimiento.getObservacion())) {
                        detalleActividad = detalleActividad + "OBSERVACION: " + movimientoAnterior.getObservacion() + "\n";
                    }
                    if (!movimientoAnterior.getMovRefList().isEmpty()) {
                        for (RegMovimientoReferencia mr : movimientoAnterior.getMovRefList()) {
                            if (mr.getId() == null) {
                                detalleActividad = detalleActividad + "- Se Agrega Referencia - Libro: " + mr.getMovimientoReferencia().getLibro().getNombre() + ", Num.Inscripcion: " + mr.getMovimientoReferencia().getNumInscripcion()
                                        + ", F.Inscrip.: " + fecha.format(mr.getMovimientoReferencia().getFechaInscripcion()) + ", Repertorio: " + mr.getMovimientoReferencia().getNumRepertorio() + "\n";
                            }
                        }
                    }
                    if (!movimientoAnterior.getMovRefListDel().isEmpty()) {
                        for (RegMovimientoReferencia mr : movimientoAnterior.getMovRefListDel()) {
                            detalleActividad = detalleActividad + "- Se Elimina Referencia - Libro: " + mr.getMovimientoReferencia().getLibro().getNombre() + ", Num.Inscripcion: " + mr.getMovimientoReferencia().getNumInscripcion()
                                    + ", F.Inscrip.: " + fecha.format(mr.getMovimientoReferencia().getFechaInscripcion()) + ", Repertorio: " + mr.getMovimientoReferencia().getNumRepertorio() + "\n";
                        }
                    }
                    if (movimientoAnterior.getMovCliList().isEmpty() && !movimientoAnterior.getMovCliListOld().isEmpty()) {
                        for (RegMovimientoCliente mco : movimientoAnterior.getMovCliListOld()) {
                            detalleActividad = detalleActividad + "Se Elimina Cliente: " + mco.getEnteInterv().getNombre() + (mco.getPapel() == null ? "\n" : ", Papel: " + mco.getPapel().getPapel() + "\n");
                        }
                    } else if (!movimientoAnterior.getMovCliList().isEmpty() && movimientoAnterior.getMovCliListOld().isEmpty()) {
                        for (RegMovimientoCliente mcn : movimientoAnterior.getMovCliList()) {
                            detalleActividad = detalleActividad + "Se Agrega Cliente: " + mcn.getEnteInterv().getNombre() + ", Papel: " + mcn.getPapel().getPapel() + "\n";
                        }
                    } else if (!movimientoAnterior.getMovCliList().isEmpty() && !movimientoAnterior.getMovCliListOld().isEmpty()) {
                        for (RegMovimientoCliente mco : movimientoAnterior.getMovCliListOld()) {
                            Boolean borrado = true;
                            for (RegMovimientoCliente mcn : movimientoAnterior.getMovCliList()) {
                                if (mcn.getId() == null) {
                                    detalleActividad = detalleActividad + "Se Agrega Cliente: " + mcn.getEnteInterv().getNombre() + ", Papel: " + mcn.getPapel().getPapel() + "\n";
                                } else if (mco.getId().equals(mcn.getId())) {
                                    borrado = false;
                                    if (!mco.getPapel().getId().equals(mcn.getPapel().getId())) {
                                        detalleActividad = detalleActividad + "Se Modifica Cliente: " + mcn.getEnteInterv().getNombre() + ", Papel anterior: " + mco.getPapel().getPapel() + "\n";
                                    }
                                    if (mco.getEstado() != null && !mco.getEstado().equals(mcn.getEstado())) {
                                        detalleActividad = detalleActividad + "Se Modifica Cliente: " + mcn.getEnteInterv().getNombre() + ", Estado anterior: " + mco.getEstado() + "\n";
                                    }
                                    if (mco.getCedula() != null && !mco.getCedula().equals(mcn.getCedula())) {
                                        detalleActividad = detalleActividad + "Se Modifica Cliente: " + mcn.getEnteInterv().getNombre() + ", Inf. Anterior: " + mco.getEstado() + " - " + mco.getNombres() + " - " + mco.getCedula() + "\n";
                                    }
                                    if (mco.getNombres() != null && !mco.getNombres().equals(mcn.getNombres())) {
                                        detalleActividad = detalleActividad + "Se Modifica Cliente: " + mcn.getEnteInterv().getNombre() + ", Inf. Anterior: " + mco.getEstado() + " - " + mco.getNombres() + " - " + mco.getCedula() + "\n";
                                    }
                                }
                            }
                            if (borrado) {
                                detalleActividad = detalleActividad + "Se Elimina Cliente: " + mco.getEnteInterv().getNombre() + ", Papel: " + mco.getPapel().getPapel() + "\n";
                            }
                        }
                    }
                    break;
                case ELIMINAR_REFERENCIA:
                    if (movimiento.getObservacionEliminacion() != null) {
                        detalleActividad = detalleActividad + "Observación: \n" + movimiento.getObservacionEliminacion() + "\n";
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return detalleActividad;
    }

    @Override
    public boolean registrarEdicionCertificado(RegCertificado certificado, ActividadesTransaccionales actividadTransaccional,
            BigInteger periodo
    ) {
        BigInteger codUsu = BigInteger.valueOf(ss.getSessionContext().getUserId());
        AclLogin login = ss.getSessionContext().getAclLogin();
        String actividad = actividadTransaccional.getDescripcion();
        try {
            rb = new RegBitacora();
            rb.setCodUsuario(codUsu);
            rb.setIdUsuario(codUsu);
            rb.setActividad(actividad);
            rb.setFecha(new Date());
            rb.setFechaHora(new Date());
            rb.setCodPrograma("SGReg");
            rb.setAnio(periodo);
            rb.setIdCertificado(new BigInteger(certificado.getId().toString()));
            rb.setTipServicio(new BigInteger("1"));
            rb.setIpCliente(login.getIpUserSession());
            rb.setMacCliente(login.getMacClient());
            em.persist(rb);
        } catch (Exception e) {
            Logger.getLogger(BitacoraEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

}
