/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named
@ViewScoped
public class ReporteFichaRegistralRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReportesTesoreria.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;

    protected Date desde = new Date();
    protected Date hasta = new Date();
    protected Date desde2 = new Date();
    protected Date hasta2 = new Date();
    protected Date desde3 = new Date();
    protected Date hasta3 = new Date();
    protected Calendar limite = Calendar.getInstance();
    protected AclUser user;
    protected Long usuario = 0L;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    protected List<AclUser> digitadores = new ArrayList<>();

    protected int tipoReporte = 0;

    @PostConstruct
    protected void iniView() {
        try {
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("nombre", "digitador_ficha");
            AclRol rol = (AclRol) manager.findObjectByParameter(AclRol.class, map);
            if (us.getRoles().contains(44L)) {
                if (rol != null) {
                    digitadores = (List<AclUser>) rol.getAclUserCollection();
                }
            } else {
                digitadores = new ArrayList<>();
                user = manager.find(AclUser.class, us.getUserId());
                digitadores.add(user);
            }

            limite.set(2014, 9, 17, 0, 0, 0);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReportes(boolean excel) {
        try {

            switch (tipoReporte) {
                case 0:
                    JsfUti.messageWarning(null, "Debe Seleccionar el tipo de Reporte.", "");
                    break;
                case 1: // INFORME DE FICHAS
                    if (this.validarFechas(desde, hasta)) {
                        ss.borrarDatos();
                        ss.borrarParametros();
                        ss.instanciarParametros();
                        ss.setTieneDatasource(true);
                        ss.setNombreReporte("informeFichaIngresadas");
                        ss.setNombreSubCarpeta("registro");
                        ss.agregarParametro("DESDE", sdf2.format(desde));
                        ss.agregarParametro("HASTA", sdf2.format(hasta));
                        ss.agregarParametro("HASTA_STRING", sdf2.format(hasta));
                        ss.agregarParametro("USUARIO", usuario);
                        ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_comprobante.jpg"));
                        this.redirectReport(excel);
                    }
                    break;
                default:
                    JsfUti.messageWarning(null, "Problemas al generar el reporte seleccionado.", "");
                    break;
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validarFechas(Date desde, Date hasta) {
        if (user == null) {
            usuario = 0L;
        } else {
            usuario = user.getId();
        }
        if (desde.before(limite.getTime()) || hasta.equals(limite.getTime())) {
            JsfUti.messageWarning(null, "La fecha de consulta debe ser mayor a la fecha limite: " + limite.getTime(), "");
            return false;
        }
        if (desde.before(hasta) || desde.equals(hasta)) {
            return true;
        } else {
            JsfUti.messageWarning(null, "Fecha Desde debe ser menos a fecha Hasta.", "");
            return false;
        }
    }

    public void redirectReport(boolean excel) {
        if (excel) {
            JsfUti.redirectNewTab("/sgr/DocumentoExcel");
        } else {
            JsfUti.redirectNewTab("/sgr/Documento");
        }
    }

    public int getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(int tipoReporte) {
        this.tipoReporte = tipoReporte;
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

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public SimpleDateFormat getSdf2() {
        return sdf2;
    }

    public void setSdf2(SimpleDateFormat sdf2) {
        this.sdf2 = sdf2;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public Date getDesde2() {
        return desde2;
    }

    public void setDesde2(Date desde2) {
        this.desde2 = desde2;
    }

    public Date getHasta2() {
        return hasta2;
    }

    public void setHasta2(Date hasta2) {
        this.hasta2 = hasta2;
    }

    public Date getDesde3() {
        return desde3;
    }

    public void setDesde3(Date desde3) {
        this.desde3 = desde3;
    }

    public Date getHasta3() {
        return hasta3;
    }

    public void setHasta3(Date hasta3) {
        this.hasta3 = hasta3;
    }

    public List<AclUser> getDigitadores() {
        return digitadores;
    }

}
