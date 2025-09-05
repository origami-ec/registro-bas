/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
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
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class FichasReutilizables implements Serializable {

    private static final Logger LOG = Logger.getLogger(FichasReutilizables.class.getName());

    @Inject
    private Entitymanager em;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;

    private RegFicha fichaSel;
    private LazyModel<RegFicha> fichasLazy;
    private Map<String, Object> filterss;
    protected List<CtlgItem> estadosInformacion = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            filterss = new HashMap<>();
            filterss.put("informacionFicha.codename", "DUPL");
            fichasLazy = new LazyModel<>(RegFicha.class, filterss);

            filterss = new HashMap<>();
            filterss.put("catalogo", "ficha.estado_informacion");
            estadosInformacion = em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, filterss);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void redirectEditarFicha(RegFicha ficha) {
        try {
            if (Utils.isNotEmpty(ficha.getRegMovimientoFichaCollection())) {
                JsfUti.messageError(null, "Ficha no puede ser reutilizada porque tiene movientos asociados.", "");
                return;
            }
            ss.instanciarParametros();
            ss.agregarParametro("idFicha", ficha.getId());
            ss.agregarParametro("fichaReutilizable", true);
            JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void imprimirFichaRegistral(RegFicha ficha) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("FichaRegistral");
            ss.agregarParametro("ID_FICHA", ficha.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
            ss.agregarParametro("USER_NAME", us.getName_user());
            ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab("/sgr/Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgFichaSelect(RegFicha rf) {
        try {
            fichaSel = rf;
            JsfUti.update("formFichaSelect");
            JsfUti.executeJS("PF('dlgFichaSelect').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public RegFicha getFichaSel() {
        return fichaSel;
    }

    public void setFichaSel(RegFicha fichaSel) {
        this.fichaSel = fichaSel;
    }

    public LazyModel<RegFicha> getFichasLazy() {
        return fichasLazy;
    }

    public void setFichasLazy(LazyModel<RegFicha> fichasLazy) {
        this.fichasLazy = fichasLazy;
    }

    public Map<String, Object> getFilterss() {
        return filterss;
    }

    public void setFilterss(Map<String, Object> filterss) {
        this.filterss = filterss;
    }

    public List<CtlgItem> getEstadosInformacion() {
        return estadosInformacion;
    }

    public void setEstadosInformacion(List<CtlgItem> estadosInformacion) {
        this.estadosInformacion = estadosInformacion;
    }

}
