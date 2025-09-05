/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.lazymodels.LazyModel;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @author Anyelo
 */
@Named
@ViewScoped
public class FichasIngresadas implements Serializable {

    private static final Logger LOG = Logger.getLogger(FichasIngresadas.class.getName());

    @Inject
    protected RegistroPropiedadServices reg;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;
    @Inject
    private Entitymanager em;

    protected Map map;
    protected RegRegistrador registrador;
    protected LazyModel<RegFicha> listadoFichas;
    protected RegFicha fichaSel = new RegFicha();
    protected List<RegMovimientoFicha> movimientos;
    protected Boolean permitido = false, admin = false;
    protected List<RegEnteInterviniente> propietarios = new ArrayList<>();
    protected List<CtlgItem> estadosInformacion = new ArrayList<>();
    protected RegTipoFicha tipoFicha;
    protected RegTipoFicha newTipoFicha;

    @PostConstruct
    protected void iniView() {
        try {
            tipoFicha = em.find(RegTipoFicha.class, 1L);
            listadoFichas = new LazyModel(RegFicha.class, "numFicha", "DESC");
            listadoFichas.addFilter("tipoFicha", tipoFicha);

            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            map = new HashMap<>();
            map.put("catalogo", "ficha.estado_informacion");
            estadosInformacion = em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
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
                System.out.println("Ficha id:" + ficha.getId());
                ss.agregarParametro("ID_FICHA", ficha.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("USER_NAME", us.getName_user());
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgFichaSelect(RegFicha rf) {
        try {
            fichaSel = rf;
            movimientos = reg.getRegMovByIdFicha(rf.getId());
            propietarios = reg.getPropietariosByFicha(rf.getId());
            JsfUti.update("formFichaSelect");
            JsfUti.executeJS("PF('dlgFichaSelect').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacora() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", null);
            ss.agregarParametro("numFicha", fichaSel.getNumFicha());
            ss.agregarParametro("titulo", Messages.bitacoraFicha);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNewFicha() {
        JsfUti.update("formTipoFicha");
        JsfUti.executeJS("PF('dlgTipoFicha').show();");
    }

    public void redirectFichaNueva() {
        if (newTipoFicha != null) {
            ss.instanciarParametros();
            ss.agregarParametro("tipoFicha", newTipoFicha.getId());
            JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
        } else {
            JsfUti.messageWarning("Nueva Ficha", "Debe seleccionar el tipo ficha", "");
        }
    }

    public void redirectEditarFicha(RegFicha ficha) {
        ss.instanciarParametros();
        ss.agregarParametro("idFicha", ficha.getId());
        ss.agregarParametro("tipoFicha", ficha.getTipoFicha().getId());
        JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
    }

    public void habilitarEdicion() {
        try {
            if (permitido) {
                em.update(fichaSel);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgFichaSelect').hide();");
                JsfUti.messageInfo(null, "Ficha Registral habilitada para edicion.", "");
            } else {
                JsfUti.messageWarning(null, "Usuario no permitido.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegTipoFicha> getTiposFichas() {
        return em.findAllEntCopy(Querys.getRegTipoFicha);
    }

    public void updateLazy() {
        listadoFichas = new LazyModel(RegFicha.class, "numFicha", "DESC");
        listadoFichas.addFilter("tipoFicha", tipoFicha);
    }

    public LazyModel<RegFicha> getListadoFichas() {
        return listadoFichas;
    }

    public void setListadoFichas(LazyModel<RegFicha> listadoFichas) {
        this.listadoFichas = listadoFichas;
    }

    public RegFicha getFichaSel() {
        return fichaSel;
    }

    public void setFichaSel(RegFicha fichaSel) {
        this.fichaSel = fichaSel;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<RegEnteInterviniente> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegEnteInterviniente> propietarios) {
        this.propietarios = propietarios;
    }

    public List<RegMovimientoFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimientoFicha> movimientos) {
        this.movimientos = movimientos;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<CtlgItem> getEstadosInformacion() {
        return estadosInformacion;
    }

    public void setEstadosInformacion(List<CtlgItem> estadosInformacion) {
        this.estadosInformacion = estadosInformacion;
    }

    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public RegTipoFicha getNewTipoFicha() {
        return newTipoFicha;
    }

    public void setNewTipoFicha(RegTipoFicha newTipoFicha) {
        this.newTipoFicha = newTipoFicha;
    }

}
