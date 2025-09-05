/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.mail.entities.CorreoCargas;
import com.origami.mail.entities.CorreoUsuarios;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.SecuenciaComprobantes;
import com.origami.sgr.entities.SecuenciaGeneral;
import com.origami.sgr.entities.SecuenciaInscripcion;
import com.origami.sgr.entities.SecuenciaRepertorio;
//import com.origami.sgr.entities.SecuenciaRepertorioMercantil;
import com.origami.sgr.entities.SecuenciaTitulo;
import com.origami.sgr.entities.SecuenciaTramite;
import com.origami.sgr.entities.TarTareasAsignadas;
import com.origami.sgr.entities.TarUsuarioTareas;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.hibernate.HibernateException;
import com.origami.sql.ConsultasSQLService;

/**
 *
 * @author Anyelo
 */
@Singleton(name = "seqManager")
@Lock(LockType.READ)
@Interceptors(value = {HibernateEjbInterceptor.class})
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SeqGenManEjb implements SeqGenMan {

    @Inject
    private Entitymanager manager;

    @Inject
    protected ConsultasSQLService sql;

    /**
     * consulta a la tabla de secuencia general el numero siguiente de secuencia
     * correspondiente para el parametro enviado
     *
     * @param code Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public BigInteger getSecuenciaGeneral(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        SecuenciaGeneral sg;
        try {
            if (code != null && !code.isEmpty()) {
                map.put("code", code);
                sg = manager.findObjectByParameter(SecuenciaGeneral.class, map);
                if (sg == null) {
                    sg = new SecuenciaGeneral();
                    sg.setAnio(anio);
                    sg.setCode(code);
                    sg.setSecuencia(BigInteger.ZERO);
                }
                sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
                sg = (SecuenciaGeneral) manager.persist(sg);
                return sg.getSecuencia();
            }
        } catch (HibernateException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * para las secuencias que se reinician cada anio se consulta por el nombre
     * del parametro y el numero de anio actual
     *
     * @param code Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public BigInteger getSecuenciaGeneralByAnio(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        SecuenciaGeneral sg;
        try {
            if (code != null && !code.isEmpty()) {
                map.put("code", code);
                map.put("anio", anio);
                sg = manager.findObjectByParameter(SecuenciaGeneral.class, map);
                if (sg == null) {
                    sg = new SecuenciaGeneral();
                    sg.setAnio(anio);
                    sg.setCode(code);
                    sg.setSecuencia(BigInteger.ZERO);
                }
                sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
                sg = (SecuenciaGeneral) manager.persist(sg);
                return sg.getSecuencia();
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public BigInteger getSecuenciaTomoByAnio(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        SecuenciaGeneral sg, sec;
        try {
            if (code != null && !code.isEmpty()) {
                map.put("code", code);
                map.put("anio", anio);
                sg = manager.findObjectByParameter(SecuenciaGeneral.class, map);
                if (sg == null) {
                    map = new HashMap<>();
                    map.put("code", code);
                    map.put("anio", anio - 1);
                    sg = manager.findObjectByParameter(SecuenciaGeneral.class, map);
                    if (sg == null) {
                        sg = new SecuenciaGeneral();
                        sg.setAnio(anio);
                        sg.setCode(code);
                        sg.setSecuencia(BigInteger.ONE);
                        sg = (SecuenciaGeneral) manager.persist(sg);
                    } else {
                        sec = new SecuenciaGeneral();
                        sec.setAnio(anio);
                        sec.setCode(code);
                        sec.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
                        sec = (SecuenciaGeneral) manager.persist(sec);
                        return sec.getSecuencia();
                    }
                }
                return sg.getSecuencia();
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * devuelve el siguiente numero de secuencia que reemplaza al numero de
     * cedula en las personas que no se les ingresa el documento
     *
     * @param code Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public BigInteger getSecuenciaEntes(String code) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        SecuenciaGeneral sg;
        try {
            if (code != null && !code.isEmpty()) {
                map.put("code", code);
                sg = manager.findObjectByParameter(SecuenciaGeneral.class, map);
                if (sg == null) {
                    sg = new SecuenciaGeneral();
                    sg.setAnio(anio);
                    sg.setCode(code);
                    sg.setSecuencia(new BigInteger("900000000000000"));
                }
                sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
                sg = (SecuenciaGeneral) manager.persist(sg);
                return sg.getSecuencia();
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * retorna la siguiente secuencia de repertorio la cual se reinicia cada año
     *
     * @return Object
     */
    /*@Override
    @Lock(LockType.WRITE)
    synchronized public SecuenciaRepertorio getSecuenciaRepertorio() {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        SecuenciaRepertorio sr;
        Integer repertorio;
        try {
            repertorio = (Integer) manager.findObjectByParameter(Querys.getMinRepertorioDisponible, map);
            if (repertorio == null) {
                repertorio = (Integer) manager.findObjectByParameter(Querys.getMaxRepertorioNoDisponible, map);
                if (repertorio == null) {
                    repertorio = 0;
                }
                sr = new SecuenciaRepertorio();
                sr.setAnio(anio);
                sr.setDisponible(Boolean.FALSE);
                sr.setFecha(c.getTime());
                sr.setRepertorio(repertorio + 1);
            } else {
                map.put("repertorio", repertorio);
                map.put("disponible", Boolean.TRUE);
                sr = manager.findObjectByParameter(SecuenciaRepertorio.class, map);
                sr.setDisponible(Boolean.FALSE);
            }
            sr = (SecuenciaRepertorio) manager.persist(sr);
            return sr;
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }*/
    @Override
    @Lock(LockType.WRITE)
    public SecuenciaRepertorio getSecuenciaRepertorio() {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaRepertorio sr;
        Integer repertorio;
        try {
            //repertorio = (Integer) manager.getNativeQuery(Querys.getSecuenciaRepertorio, new Object[]{anio});
            repertorio = sql.getSecuenciaRepertorio(anio);
            System.out.println("// repertorio: " + repertorio);
            if (repertorio != null && repertorio > 0) {
                sr = new SecuenciaRepertorio();
                sr.setAnio(anio);
                sr.setDisponible(Boolean.FALSE);
                sr.setFecha(c.getTime());
                sr.setRepertorio(repertorio);
                sr = (SecuenciaRepertorio) manager.persist(sr);
                return sr;
            }
        } catch (SQLException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return new SecuenciaRepertorio();
    }

    /*@Override
    @Lock(LockType.WRITE)
    synchronized public SecuenciaRepertorioMercantil getSecuenciaRepertorioMercantil() {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        SecuenciaRepertorioMercantil sr;
        Integer repertorio;
        try {
            repertorio = (Integer) manager.findObjectByParameter(Querys.getMinRepMercDisponible, map);
            if (repertorio == null) {
                repertorio = (Integer) manager.findObjectByParameter(Querys.getMaxRepMercNoDisponible, map);
                if (repertorio == null) {
                    repertorio = 0;
                }
                sr = new SecuenciaRepertorioMercantil();
                sr.setAnio(anio);
                sr.setDisponible(Boolean.FALSE);
                sr.setFecha(c.getTime());
                sr.setRepertorio(repertorio + 1);
            } else {
                map.put("repertorio", repertorio);
                map.put("disponible", Boolean.TRUE);
                sr = manager.findObjectByParameter(SecuenciaRepertorioMercantil.class, map);
                sr.setDisponible(Boolean.FALSE);
            }
            sr = (SecuenciaRepertorioMercantil) manager.persist(sr);
            return sr;
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }*/

 /*@Override
    @Lock(LockType.WRITE)
    synchronized public SecuenciaRepertorio getSecuenciaRepertorio(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Integer anio = Integer.parseInt(sdf.format(fecha));
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        SecuenciaRepertorio sr;
        Integer repertorio;
        try {
            repertorio = (Integer) manager.findObjectByParameter(Querys.getMinRepertorioDisponible, map);
            if (repertorio == null) {
                repertorio = (Integer) manager.findObjectByParameter(Querys.getMaxRepertorioNoDisponible, map);
                if (repertorio == null) {
                    repertorio = 0;
                }
                sr = new SecuenciaRepertorio();
                sr.setAnio(anio);
                sr.setDisponible(Boolean.FALSE);
                sr.setFecha(fecha);
                sr.setRepertorio(repertorio + 1);
            } else {
                map.put("repertorio", repertorio);
                map.put("disponible", Boolean.TRUE);
                sr = manager.findObjectByParameter(SecuenciaRepertorio.class, map);
                sr.setDisponible(Boolean.FALSE);
            }
            sr = (SecuenciaRepertorio) manager.persist(sr);
            return sr;
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }*/

 /*@Override
    @Lock(LockType.WRITE)
    synchronized public SecuenciaRepertorioMercantil getSecuenciaRepertorioMercantil(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Integer anio = Integer.parseInt(sdf.format(fecha));
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        SecuenciaRepertorioMercantil sr;
        Integer repertorio;
        try {
            repertorio = (Integer) manager.findObjectByParameter(Querys.getMinRepMercDisponible, map);
            if (repertorio == null) {
                repertorio = (Integer) manager.findObjectByParameter(Querys.getMaxRepMercNoDisponible, map);
                if (repertorio == null) {
                    repertorio = 0;
                }
                sr = new SecuenciaRepertorioMercantil();
                sr.setAnio(anio);
                sr.setDisponible(Boolean.FALSE);
                sr.setFecha(fecha);
                sr.setRepertorio(repertorio + 1);
            } else {
                map.put("repertorio", repertorio);
                map.put("disponible", Boolean.TRUE);
                sr = manager.findObjectByParameter(SecuenciaRepertorioMercantil.class, map);
                sr.setDisponible(Boolean.FALSE);
            }
            sr = (SecuenciaRepertorioMercantil) manager.persist(sr);
            return sr;
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }*/
    /**
     * devuelve el numero de secuencia correspondiente para la inscripcion
     * dependiendo del id del libro al que se envia como parametro
     *
     * @param idLibro Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public Integer getSecuenciaInscripcion(Long idLibro) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        map.put("idLibro", BigInteger.valueOf(idLibro));
        SecuenciaInscripcion se;
        Integer inscripcion;
        try {
            inscripcion = (Integer) manager.findObjectByParameter(Querys.getMinInscripcionDisponible, map);
            if (inscripcion == null) {
                inscripcion = (Integer) manager.findObjectByParameter(Querys.getMaxInscripcionNoDisponible, map);
                if (inscripcion == null) {
                    inscripcion = 0;
                }
                se = new SecuenciaInscripcion();
                se.setAnio(anio);
                se.setLibro(BigInteger.valueOf(idLibro));
                se.setDisponible(Boolean.FALSE);
                se.setInscripcion(inscripcion + 1);
            } else {
                map.put("inscripcion", inscripcion);
                map.put("disponible", Boolean.TRUE);
                se = manager.findObjectByParameter(SecuenciaInscripcion.class, map);
                se.setDisponible(Boolean.FALSE);
            }
            se = (SecuenciaInscripcion) manager.persist(se);
            return se.getInscripcion();
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public SecuenciaInscripcion consultarSecuenciaInscripcion(Long idLibro) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        map.put("idLibro", BigInteger.valueOf(idLibro));
        SecuenciaInscripcion se;
        Integer inscripcion;
        try {
            inscripcion = (Integer) manager.findObjectByParameter(Querys.getMinInscripcionDisponible, map);
            if (inscripcion == null) {
                inscripcion = (Integer) manager.findObjectByParameter(Querys.getMaxInscripcionNoDisponible, map);
                if (inscripcion == null) {
                    inscripcion = 0;
                }
                se = new SecuenciaInscripcion();
                se.setAnio(anio);
                se.setLibro(BigInteger.valueOf(idLibro));
                se.setDisponible(Boolean.FALSE);
                se.setInscripcion(inscripcion + 1);
            } else {
                map.put("inscripcion", inscripcion);
                map.put("disponible", Boolean.TRUE);
                se = manager.findObjectByParameter(SecuenciaInscripcion.class, map);
                se.setDisponible(Boolean.FALSE);
            }
            return se;
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * devuelve el numero de secuencia correspondiente para la inscripcion
     * dependiendo del id del libro al que se envia como parametro
     *
     * @param idLibro Object
     * @param fecha Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public Integer getSecuenciaInscripcion(Long idLibro, Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        Integer anio = c.get(Calendar.YEAR);
        Map<String, Object> map = new HashMap<>();
        map.put("anio", anio);
        map.put("idLibro", BigInteger.valueOf(idLibro));
        SecuenciaInscripcion se;
        Integer inscripcion;
        try {
            inscripcion = (Integer) manager.findObjectByParameter(Querys.getMinInscripcionDisponible, map);
            if (inscripcion == null) {
                inscripcion = (Integer) manager.findObjectByParameter(Querys.getMaxInscripcionNoDisponible, map);
                if (inscripcion == null) {
                    inscripcion = 0;
                }
                se = new SecuenciaInscripcion();
                se.setAnio(anio);
                se.setLibro(BigInteger.valueOf(idLibro));
                se.setDisponible(Boolean.FALSE);
                se.setInscripcion(inscripcion + 1);
            } else {
                map.put("inscripcion", inscripcion);
                map.put("disponible", Boolean.TRUE);
                se = manager.findObjectByParameter(SecuenciaInscripcion.class, map);
                se.setDisponible(Boolean.FALSE);
            }
            se = (SecuenciaInscripcion) manager.persist(se);
            return se.getInscripcion();
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * consulta el usuario disponible para que se le asigne el tramite
     * correspondiente dependiendo de los criterios de busqueda
     *
     * @param idRol Object
     * @param cantidad Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public AclUser getUserForTramite(Long idRol, Integer cantidad) {
        Calendar cal = Calendar.getInstance();
        TarTareasAsignadas ta;
        if (cantidad == null) {
            cantidad = 1;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rolUsuario", idRol);
        map.put("fecha", cal.getTime());
        try {
            ta = (TarTareasAsignadas) manager.findObjectByParameter(Querys.getUsuarioTramite, map);
            if (ta == null) {
                this.encerarTareasUsuarios(cal.getTime());
                ta = (TarTareasAsignadas) manager.findObjectByParameter(Querys.getUsuarioTramite, map);
            }
            if (ta != null) {
                if (ta.getUsuarioTareas().getCantidadAux() > 0) {
                    cantidad = cantidad * ta.getUsuarioTareas().getCantidadAux();
                }
                ta.setCantidad(ta.getCantidad().add(BigInteger.valueOf(cantidad)));
                ta.setPeso(ta.getPeso().add(BigInteger.ONE));
                ta = (TarTareasAsignadas) manager.persist(ta);
                return ta.getUsuarioTareas().getUsuario();
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    synchronized public void encerarTareasUsuarios(Date fecha) {
        List<TarUsuarioTareas> list;
        TarTareasAsignadas ta;
        Map<String, Object> map = new HashMap<>();
        map.put("estado", Boolean.TRUE);
        try {
            list = manager.findObjectByParameterList(TarUsuarioTareas.class, map);
            for (TarUsuarioTareas ut : list) {
                map = new HashMap<>();
                map.put("usuarioTareas", ut);
                map.put("fecha", fecha);
                ta = manager.findObjectByParameter(TarTareasAsignadas.class, map);
                if (ta == null) {
                    ta = new TarTareasAsignadas();
                    ta.setCantidad(BigInteger.ZERO);
                    ta.setPeso(BigInteger.ZERO);
                    ta.setUsuarioTareas(ut);
                    ta.setFecha(fecha);
                    manager.persist(ta);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * consulta el usuario disponible para que se le asigne el tramite
     * correspondiente dependiendo de los criterios de busqueda y de la cantidad
     * de tareas para ese tramite
     *
     * @param idUser Object
     * @param cantidad Object
     * @return Object
     */
    @Override
    @Lock(LockType.WRITE)
    synchronized public AclUser getUserForTramiteById(Long idUser, Integer cantidad) {
        Calendar cal = Calendar.getInstance();
        TarTareasAsignadas ta;
        Map<String, Object> map = new HashMap<>();
        map.put("idUsuario", idUser);
        map.put("fecha", cal.getTime());
        try {
            ta = (TarTareasAsignadas) manager.findObjectByParameter(Querys.getUsuarioTareas, map);
            if (ta == null) {
                this.encerarTareasUsuarios(cal.getTime());
                ta = (TarTareasAsignadas) manager.findObjectByParameter(Querys.getUsuarioTareas, map);
            }
            if (ta != null) {
                ta.setCantidad(ta.getCantidad().add(BigInteger.valueOf(cantidad)));
                ta = (TarTareasAsignadas) manager.persist(ta);
                return ta.getUsuarioTareas().getUsuario();
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public boolean cobroDisponible(Long liq, Long user) {
        try {
            Integer estado = (Integer) manager.getNativeQuery(Querys.getEstadoByLiquidacion, new Object[]{liq});
            if (estado == null || estado == 0) {
                manager.updateNativeQuery(Querys.updateLiqudiacion, new Object[]{user, liq});
                return true;
            } else {
                return estado == user.intValue();
            }
        } catch (HibernateException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public TarUsuarioTareas getUserForTask(Long idRol, Integer cantidad) {
        TarUsuarioTareas user;
        Calendar cal = Calendar.getInstance();
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha, hoy;
        try {
            map.put("rol", idRol);
            fecha = (Date) manager.findObjectByParameter(Querys.getMinDateUsers, map);
            map = new HashMap<>();
            map.put("rol", idRol);
            map.put("fecha", fecha);
            System.out.println("getUserForTask fecha " + fecha);
            user = (TarUsuarioTareas) manager.findObjectByParameter(Querys.getMinUser, map);
            hoy = sdf.parse(sdf.format(cal.getTime()));
            if (user.getFecha().before(hoy)) {
                user.setFecha(new Date());
                user.setCantidad(BigInteger.valueOf(cantidad));
                manager.merge(user);
                return user;
            } else if (user.getPeso().compareTo(user.getCantidad()) > 0) {
                user.setCantidad(user.getCantidad().add(BigInteger.valueOf(cantidad)));
                manager.merge(user);
                user.setDias(Utils.restarFechas(hoy, user.getFecha()).intValue());
                return user;
            } else {
                user.setCantidad(BigInteger.valueOf(cantidad));
                user.setFecha(Utils.sumarDiasFechaSinWeekEnd(user.getFecha(), 1));
                manager.merge(user);
                user.setDias(Utils.restarFechas(hoy, user.getFecha()).intValue());
                return user;
            }
        } catch (ParseException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public TarUsuarioTareas getUserForTaskFicha(Long idRol, Integer cantidad) {
        TarUsuarioTareas user;
        Calendar cal = Calendar.getInstance();
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha, hoy;
        try {
            map.put("rol", idRol);
            fecha = (Date) manager.findObjectByParameter(Querys.getMinDateUsersFicha, map);
            System.out.println("getUserForTaskFicha fecha " + fecha);
            map = new HashMap<>();
            map.put("rol", idRol);
            map.put("fecha", fecha);
            user = (TarUsuarioTareas) manager.findObjectByParameter(Querys.getMinUserFicha, map);
            System.out.println("user " + user.toString());
            hoy = sdf.parse(sdf.format(cal.getTime()));
            if (user.getFechaFicha().before(hoy)) {
                user.setFechaFicha(new Date());
                user.setCantidad(BigInteger.valueOf(cantidad));
                manager.merge(user);
                return user;
            } else if (user.getPeso().compareTo(user.getCantidad()) > 0) {
                user.setCantidad(user.getCantidad().add(BigInteger.valueOf(cantidad)));
                manager.merge(user);
                user.setDias(Utils.restarFechas(hoy, user.getFechaFicha()).intValue());
                return user;
            } else {
                user.setCantidad(BigInteger.valueOf(cantidad));
                user.setFechaFicha(Utils.sumarDiasFechaSinWeekEnd(user.getFechaFicha(), 1));
                manager.merge(user);
                user.setDias(Utils.restarFechas(hoy, user.getFechaFicha()).intValue());
                return user;
            }
        } catch (ParseException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public TarUsuarioTareas getUserForTask(Long idRol, Integer cantidad, Date fechaEntrega) {
        TarUsuarioTareas user;
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = new Date();

        try {
            // BUSCAMOS UNO DE LOS USUARIOS QUE TENGAN MENOS TRAMITES Y ESTEN DENTRO DE LA FECHA DE ENTREGA.
            map.put("rol", idRol);
            map.put("fechaEntrega", fechaEntrega);
            map.put("fechaEntrega1", fecha);
            user = (TarUsuarioTareas) manager.findObjectByParameter(Querys.getUsersMinData, map);
            // PONEMOS LA CANTIDAD EN CERO CUANDO VIENE NULL
            if (user.getCantidad() == null) {
                user.setCantidad(BigInteger.ZERO);
            }
            // SI LA FECHA DE INGRESO ES DIFERENTE A LA DE HOY, LE SETEAMOS LA DEL DIA DE HOY
            if (!user.getFecha().equals(sdf.parse(sdf.format(fecha)))) {
                // ACTUALIZAMOS A LA FECHA DEL DIA DE HOY
                user.setFecha(fecha);
                // REINIAMOS TODOS LOS VALORES.
                user.setCantidad(BigInteger.ZERO);
                // SI LOS DIAS ES MAYOR A UNO PARA LA NUEVA ASIGNACION SE PONE LA CANTIDAD ANTERIOR A LA ACTUAL
                if (user.getDias() == 1) {
                    user.setCantidad(BigInteger.valueOf(user.getCantidadAux()));
                    user.setDias(0);
                    user.setCantidadAux(0);
                } else if (user.getDias() > 1) {
                    user.setDias(user.getDias() - 1);
                    user.setCantidad(user.getPeso());
                    user.setCantidadAux(user.getCantidadAux());
                } else {
                    user.setDias(0);
                    user.setCantidadAux(0);
                }
            }
            // SI EL PESO ES IGUAL A LA CANTIDAD USAMOS LOS CAMPOS AUXILIARES PARA ALMACENAR LOS DATOS
            if (user.getPeso().compareTo(user.getCantidad()) == 0) {
                // VERIFICAMOS SI LA CANTIDAD AUXILIAR ES IGUAL AL PESO QUERE DECIR QUE YA ECEDIO AL DIA ACTUAL MAS UNO
                if (user.getCantidadAux() == user.getPeso().intValue()) {
                    user.setDias(user.getDias() + 1);
                    user.setCantidadAux(0);
                }
                if (user.getDias().equals(0)) {
                    user.setDias(1);
                }
                user.setCantidadAux(user.getCantidadAux() + cantidad);
            } else {
                // ASIGNACIONES PARA EL DIA ACTUAL
                BigInteger sumTramites = user.getCantidad().add(BigInteger.valueOf(cantidad));
                // MODIFICAMOS E LVALOR DE TRAMITES ASIGNADOS.
                user.setCantidad(sumTramites);
            }
            // ACTUALIZAMOS EL REGISTRO.
            manager.merge(user);
            return user;
        } catch (ParseException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public Long getSecuenciaTramite() {
        try {
            SecuenciaTramite sec = new SecuenciaTramite();
            sec.setFecha(new Date());
            sec = (SecuenciaTramite) manager.persist(sec);
            return sec.getId();
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public Long getSecuenciaTitulo(Long tramite) {
        try {
            SecuenciaTitulo sec = new SecuenciaTitulo();
            sec.setNumeroTramite(tramite);
            sec.setFecha(new Date());
            sec = (SecuenciaTitulo) manager.persist(sec);
            return sec.getId();
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public BigInteger getSecuenciaComprobante(String code, String ambiente) {
        Map<String, Object> map = new HashMap<>();
        SecuenciaComprobantes sc;
        try {
            if (code != null && !code.isEmpty()) {
                map.put("code", code);
                map.put("ambiente", ambiente);
                sc = manager.findObjectByParameter(SecuenciaComprobantes.class, map);
                if (sc == null) {
                    sc = new SecuenciaComprobantes();
                    sc.setCode(code);
                    sc.setAmbiente(ambiente);
                    sc.setSecuencia(BigInteger.ZERO);
                }
                sc.setSecuencia(sc.getSecuencia().add(BigInteger.ONE));
                sc = (SecuenciaComprobantes) manager.persist(sc);
                return sc.getSecuencia();
            }
        } catch (HibernateException e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public CorreoUsuarios getMailDisponible() {
        Calendar cal = Calendar.getInstance();
        CorreoCargas cc;

        Map<String, Object> map = new HashMap<>();
        map.put("fecha", cal.getTime());

        try {
            cc = (CorreoCargas) manager.findObjectByParameter(Querys.getCorreoDisponible, map);
            if (cc == null) {
                this.inicializarCorreos(cal.getTime());
                cc = (CorreoCargas) manager.findObjectByParameter(Querys.getCorreoDisponible, map);
            }
            if (cc != null) {
                cc.setCantidad(cc.getCantidad() + 1);
                cc = (CorreoCargas) manager.persist(cc);
                return cc.getCorreoUsuario();
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    synchronized public void inicializarCorreos(Date fecha) {
        List<CorreoUsuarios> list;
        CorreoCargas ta;
        Map<String, Object> map = new HashMap<>();
        map.put("estado", Boolean.TRUE);
        try {
            list = manager.findObjectByParameterList(CorreoUsuarios.class, map);
            for (CorreoUsuarios ut : list) {
                ta = new CorreoCargas();
                ta.setCantidad(0);
                ta.setPeso(0);
                ta.setCorreoUsuario(ut);
                ta.setFecha(fecha);
                manager.persist(ta);
            }
        } catch (Exception e) {
            Logger.getLogger(SeqGenManEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
