/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sql.ConsultasSqlLocal;
import java.io.Serializable;
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
 * @author origami
 */
@Named
@ViewScoped
public class ReporteMarginacion implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ReporteMarginacion.class.getName());

    @Inject
    private Entitymanager em;
    @Inject
    private UserSession sess;
    @Inject
    private RegistroPropiedadServices services;
    @Inject
    private ServletSession ss;
    @Inject
    private Entitymanager manager;
    @Inject
    private ConsultasSqlLocal sql;
    private LazyModel<RegMovimientoReferencia> lazyReferencia;
    private Date inscripcionDesde;
    private Date inscripcionHasta;
    private AclUser inscriptor;
    private List<AclUser> inscriptores = new ArrayList<>();
    private Map<String, Object> filterss;

    @PostConstruct
    public void initView() {
        try {
            if (!JsfUti.isAjaxRequest()) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -1);
                inscripcionDesde = cal.getTime();
                inscripcionHasta = cal.getTime();
                inscriptor = manager.find(AclUser.class, this.sess.getUserId());
                inscriptores = sql.getUsuariosByRolName("inscriptor");

                filterss = new HashMap<>();
                filterss.put("user", inscriptor);
                filterss.put("fechaInscripcionDesde", inscripcionDesde);
                filterss.put("fechaInscripcionHasta", inscripcionHasta);
                List<Long> movimientosId = manager.findNamedQuery(Querys.getMovimientoIds, filterss);
                if (Utils.isNotEmpty(movimientosId)) {
                    lazyReferencia = new LazyModel<>(RegMovimientoReferencia.class);
                    lazyReferencia.getFilterss().put("movimiento", movimientosId);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultar() {
        try {
            System.out.println("Desde " + inscripcionDesde);
            if (inscripcionDesde.compareTo(inscripcionHasta) > 0) {
                JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
                return;
            }
            if (inscripcionDesde.equals(inscripcionHasta)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(inscripcionHasta);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                inscripcionHasta = cal.getTime();
            }
            filterss = new HashMap<>();
            filterss.put("user", inscriptor);
            filterss.put("fechaInscripcionDesde", inscripcionDesde);
            filterss.put("fechaInscripcionHasta", inscripcionHasta);
            List<Long> movimientosId = manager.findNamedQuery(Querys.getMovimientoIds, filterss);
            if (Utils.isNotEmpty(movimientosId)) {
                lazyReferencia = new LazyModel<>(RegMovimientoReferencia.class);
                lazyReferencia.getFilterss().put("movimiento", movimientosId);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporte() {
        try {
            if (inscripcionDesde == null) {
                JsfUti.messageError(null, "Debe seleccionar la fecha desde.", "");
                return;
            }
            ss.instanciarParametros();
            if (inscriptor != null) {
                ss.agregarParametro("USUARIO", inscriptor.getId());
            }
            if (inscripcionDesde != null) {
                ss.agregarParametro("FECHA", inscripcionDesde);
            }

            ss.setNombreReporte("ReporteInscripcionesMarginacion");
            ss.setNombreSubCarpeta("workflow");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
            ss.setTieneDatasource(true);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegMovimientoReferencia> getReferencias(RegMovimiento mov) {
        if (mov != null) {
            List<RegMovimientoReferencia> regMovRefByIdRegMov = services.getRegMovRefByIdRegMov(mov.getId());
            return regMovRefByIdRegMov;
        }
        return null;
    }

    public List<RegMovimientoReferencia> getReferencias(Long mov) {
        if (mov != null) {
            List<RegMovimientoReferencia> regMovRefByIdRegMov = services.getRegMovRefByIdRegMov(mov);
            return regMovRefByIdRegMov;
        }
        return null;
    }

    public Date getInscripcionDesde() {
        return inscripcionDesde;
    }

    public void setInscripcionDesde(Date inscripcionDesde) {
        this.inscripcionDesde = inscripcionDesde;
    }

    public Date getInscripcionHasta() {
        return inscripcionHasta;
    }

    public void setInscripcionHasta(Date inscripcionHasta) {
        this.inscripcionHasta = inscripcionHasta;
    }

    public AclUser getInscriptor() {
        return inscriptor;
    }

    public void setInscriptor(AclUser inscriptor) {
        this.inscriptor = inscriptor;
    }

    public List<AclUser> getInscriptores() {
        return inscriptores;
    }

    public void setInscriptores(List<AclUser> inscriptores) {
        this.inscriptores = inscriptores;
    }

    public LazyModel<RegMovimientoReferencia> getLazyReferencia() {
        return lazyReferencia;
    }

    public void setLazyReferencia(LazyModel<RegMovimientoReferencia> lazyReferencia) {
        this.lazyReferencia = lazyReferencia;
    }

}
