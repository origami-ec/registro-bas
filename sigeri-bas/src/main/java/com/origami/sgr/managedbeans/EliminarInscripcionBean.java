/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.SecuenciaInscripcion;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class EliminarInscripcionBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(EliminarInscripcionBean.class.getName());
    @Inject
    private Entitymanager manager;
    @Inject
    protected RegistroPropiedadServices reg;
    @Inject
    protected ServletSession ss;

    private Boolean inactivarMovimiento;
    private Integer numInscripcion;
    private Integer anio;
    private Date fechaInscripcion;
    private String observacion;
    private RegLibro libro;
    private RegMovimiento movimiento;
    private RegRegistrador registrador;
    private ContenidoReportes contenidoBaja;
    private SecuenciaInscripcion secuencia;

    private List<RegLibro> libros;

    @PostConstruct
    protected void initView() {
        try {
            if (!JsfUti.isAjaxRequest()) {
                movimiento = new RegMovimiento();
                libros = manager.findAll(RegLibro.class);
                inactivarMovimiento = false;
                fechaInscripcion = new Date();
                Map<String, Object> map = new HashMap<>();
                map.put("actual", Boolean.TRUE);
                registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                map = new HashMap<>();
                map.put("code", "CONTENIDO_BAJA_NUM_INSCRIPCION");
                contenidoBaja = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al iniciar vista.", e);
        }
    }

    public void buscarInscripcion() {
        try {
            if (numInscripcion == null) {
                JsfUti.messageError(null, "Debe ingresar el numero de inscripcion", "");
                return;
            }
            if (fechaInscripcion == null) {
                JsfUti.messageError(null, "Debe seleccionar la fecha de inscripcion.", "");
                return;
            }
            if (libro == null) {
                JsfUti.messageError(null, "Debe seleccionar el libro", "");
                return;
            }
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("inscripcion", numInscripcion);
            paramt.put("anio", Utils.getAnio(fechaInscripcion));
            paramt.put("libro", BigInteger.valueOf(libro.getId()));
            secuencia = (SecuenciaInscripcion) manager.findObjectByParameter(SecuenciaInscripcion.class, paramt);
            if (secuencia == null) {
                secuencia = new SecuenciaInscripcion();
                secuencia.setAnio(Utils.getAnio(fechaInscripcion));
                secuencia.setDisponible(false);
                secuencia.setInscripcion(numInscripcion);
                secuencia.setLibro(BigInteger.valueOf(libro.getId()));
            }

            paramt = new HashMap<>();
            paramt.put("inscripcion", numInscripcion);
            paramt.put("fechaInsc", Utils.dateFormatPattern("yyyy-MM-dd", fechaInscripcion));
            paramt.put("libro", libro.getId());
            movimiento = (RegMovimiento) manager.findObjectByParameter(Querys.getMovimientoByFecInscRep, paramt);
            if (movimiento != null) {
                observacion = String.format(contenidoBaja.getValor(), Utils.convertirFechaLetra(movimiento.getFechaInscripcion()), movimiento.getNumInscripcion(), getMovimiento().getActo().getLibro().getNombre());
            } else {
                observacion = String.format(contenidoBaja.getValor(), Utils.convertirFechaLetra(fechaInscripcion), numInscripcion, libro.getNombre());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar inscripcion", e);
        }
    }

    public void eliminarNumInscripcion() {
        try {
            if (secuencia == null) {
                JsfUti.messageError(null, "Error.", "Debe debe buscar la inscripcion.");
                return;
            }
            if (observacion == null || observacion.isEmpty()) {
                JsfUti.messageError(null, "Error.", "Debe ingresar el Motivo Eliminacion");
                return;
            }
            if (movimiento != null && movimiento.getId() != null) {
                if (movimiento.getNumInscripcion() == null) {
                    JsfUti.messageError(null, "Error.", "Inscripcion no tine numero de isncripcion generado.");
                    return;
                }
                movimiento.setFechaMod(new Date());
                if (inactivarMovimiento) {
                    movimiento.setEstado("DB");
                } else {
                    this.movimiento.setEditable(Boolean.TRUE);
                    this.movimiento.setNumInscripcion(null);
                    this.movimiento.setFechaInscripcion(null);
                }
                if (manager.update(this.movimiento)) {
                    JsfUti.messageInfo(null, "Info.", "Numero de inscripcion se ha eliminado correctamente.");
                    movimiento.setUserCreador(null);
                    movimiento = new RegMovimiento();
                } else {
                    JsfUti.messageError(null, "Error.", "Numero de inscripcion no se ha podido eliminado.");
                }
            }
            if (secuencia != null) {
                secuencia.setObservacion(observacion);
                manager.merge(secuencia);
            }
            generarRazon();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar inscripcion", e);
        }
    }

    private void generarRazon() {
        ss.instanciarParametros();
        ss.setTieneDatasource(false);
        ss.setNombreReporte("eliminacionNunInscripcion");
        ss.setNombreSubCarpeta("ingreso");

        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
        ss.agregarParametro("REGISTRADOR", registrador);

        ss.agregarParametro("OBSERVACION", observacion);
        JsfUti.redirectNewTab("/sgr/Documento");
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Boolean getInactivarMovimiento() {
        return inactivarMovimiento;
    }

    public void setInactivarMovimiento(Boolean inactivarMovimiento) {
        this.inactivarMovimiento = inactivarMovimiento;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<RegLibro> getLibros() {
        return libros;
    }

    public void setLibros(List<RegLibro> libros) {
        this.libros = libros;
    }

}
