/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.HashMap;
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
public class NotasDevolutivasRpIngresadas extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private AsynchronousService as;

    private RegpLiquidacion liquidacion;
    private RegRegistrador registrador;
    private RegpNotaDevolutiva notaDevolutiva;
    private LazyModel<RegpNotaDevolutiva> notaDevolutivas;

    @PostConstruct
    protected void init() {
        try {
            registrador = (RegRegistrador) manager.find(Querys.getJuridico);
            notaDevolutivas = new LazyModel<>(RegpNotaDevolutiva.class, "id", "DESC");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void descargarNotaDevolutiva(Long idNotaDevolutiva) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("NotaDevolutiva");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", idNotaDevolutiva);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void editarNotaDevolutiva(RegpNotaDevolutiva devolutiva) {
        try {
            if (devolutiva.getElaborado().equals(session.getName_user())) {
                notaDevolutiva = devolutiva;
                JsfUti.update("frmNota");
                JsfUti.executeJS("PF('dlgNotaDevolutiva').show();");
            } else {
                JsfUti.messageWarning(null, "Solo puede editar el mismo usuario que registró la nota devolutiva.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actualizarDetalleNotaDevolutiva() {
        manager.merge(notaDevolutiva);
        JsfUti.messageInfo(null, "Nota Devolutiva Actualizada: " + notaDevolutiva.getNumNotaDevolutiva(), "");
        JsfUti.update("mainForm");
        JsfUti.executeJS("PF('dlgNotaDevolutiva').hide();");
        notaDevolutiva = null;
    }

    public void visualizarTicket(RegpNotaDevolutiva rnd) {
        try {
            map = new HashMap();
            map.put("numTramiteRp", rnd.getTramite().getNumTramite());
            RegpLiquidacion liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            if (liquidacion != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setImprimir(true);
                ss.setNombreReporte("comprobante_proforma_reingreso");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgEnviar(RegpNotaDevolutiva devolutiva) {
        try {
            if (devolutiva.getElaborado().equals(session.getName_user())) {
                notaDevolutiva = devolutiva;
                this.generarDevolutiva();
                JsfUti.update("frmMailNota");
                JsfUti.executeJS("PF('dlgSendDevolutiva').show();");
            } else {
                JsfUti.messageWarning(null, "Solo puede reenviar correo el mismo usuario que registró la nota devolutiva.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generarDevolutiva() {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();

            ss.setGeneraFile(true);
            ss.setRutaDocumento(SisVars.rutaDevolutivas + notaDevolutiva.getTramite().getNumTramite()
                    + "-" + notaDevolutiva.getId().toString() + ".pdf");

            ss.setTieneDatasource(true);
            ss.setNombreReporte("NotaDevolutiva");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", notaDevolutiva.getId());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void sendMailDevolutiva() {
        try {
            map = new HashMap();
            map.put("numTramiteRp", notaDevolutiva.getTramite().getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            if (liquidacion != null) {
                as.enviarCorreoNotaDevolutiva(liquidacion, SisVars.rutaDevolutivas + liquidacion.getNumTramiteRp()
                        + "-" + notaDevolutiva.getId().toString() + ".pdf", session.getName_user());
                JsfUti.executeJS("PF('dlgSendDevolutiva').hide();");
                JsfUti.messageInfo(null, "Correo reenviado con exito.", "");
                return;
            }
            JsfUti.messageWarning(null, "No se pudo reenviar el correo.", "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, "No se pudo reenviar el correo.", "");
        }
    }

    public LazyModel<RegpNotaDevolutiva> getNotaDevolutivas() {
        return notaDevolutivas;
    }

    public void setNotaDevolutivas(LazyModel<RegpNotaDevolutiva> notaDevolutivas) {
        this.notaDevolutivas = notaDevolutivas;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

}
