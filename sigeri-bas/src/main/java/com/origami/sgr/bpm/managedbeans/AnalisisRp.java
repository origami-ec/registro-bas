/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
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
 * @author Anyelo
 */
@Named
@ViewScoped
public class AnalisisRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(AnalisisRp.class.getName());

    @Inject
    private RegistroPropiedadServices rps;

    @Inject
    private SeqGenMan sec;

    @Inject
    private AsynchronousService as;

    protected HashMap<String, Object> par;
    protected String observacion = "";
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    //protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected Boolean hasDoc, online = false, tieneNotaDevolutiva = false;
    protected Boolean solicitudAprobada, caduco = false;
    protected RegpNotaDevolutiva notaDevolutiva;
    protected List<RegpNotaDevolutiva> notaDevolutivaAnalisis;
    protected List<Observaciones> observacionesTramites;
    protected Calendar cal;
    protected List<AclUser> asesorJuridico;
    protected AclUser us;
    protected String negativa;
    protected RegRegistrador registrador;
    protected ContenidoReportes contenidoDevolutiva;

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    hasDoc = true;
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    //docs = itl.getDocumentosTramite(ht.getId());
                    if (this.getProcessInstanceAllAttachmentsFiles().isEmpty()) {
                        hasDoc = false;
                    }
                    if (liquidacion.getEstadoPago().getId() == 7L) {
                        online = true;
                    }
                    solicitudAprobada = Boolean.TRUE;

                    //nota devolutiva
                    notaDevolutivaAnalisis = manager.findAll(Querys.getNotaDevolutivaByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});

                    if (Utils.isNotEmpty(notaDevolutivaAnalisis)) {
                        tieneNotaDevolutiva = Boolean.TRUE;
                    }

                    observacionesTramites = rps.listarObservacionesPorTramite(ht);

                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    StringBuffer validarInscripcion = itl.validarInscripcion(liquidacion);
                    if (validarInscripcion != null) {
                        advertencias = validarInscripcion.toString();
                    }
                    cal = Calendar.getInstance();
                    this.cargarArchivos(Constantes.indexacionHabilitantes, tramite);
                    registrador = (RegRegistrador) manager.find(Querys.getJuridico);
                    caduco = this.validarRepertorio(liquidacion.getFechaRepertorio());
                    map = new HashMap();
                    map.put("code", Constantes.contenidoBaseNotaDev);
                    contenidoDevolutiva = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgAprobado(Boolean estaAprobada) {
        this.solicitudAprobada = estaAprobada;
        if (solicitudAprobada) {
            observacion = "TAREA REALIZADA";
            completarTarea();
        } else {
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        }

    }

    public void rechazar() {
        try {
            this.solicitudAprobada = false;
            notaDevolutiva = new RegpNotaDevolutiva();
            notaDevolutiva.setTramite(ht);
            notaDevolutiva.setFechaIngreso(new Date());
            notaDevolutiva.setFecha(Constantes.nombreCiudad + ", " + Utils.convertirFechaLetra(new Date()));
            notaDevolutiva.setRealizado(session.getName_user());
            notaDevolutiva.setElaborado(session.getName_user());
            notaDevolutiva.setPara(ht.getSolicitante());
            notaDevolutiva.setAsunto("Devolución de Trámite # " + ht.getNumTramite().toString());
            //notaDevolutiva.setDetalle(Variables.contenidoNotaDevolutiva);
            notaDevolutiva.setDetalle(contenidoDevolutiva.getValor());
            notaDevolutiva.setFirma(registrador.getNombreReportes());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelar() {
        this.solicitudAprobada = true;
        this.notaDevolutiva = null;
    }

    public void guardarObservacion() {
        try {
            rps.guardarObservaciones(ht, session.getName_user(), observacion, this.getTaskDataByTaskID().getName());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            if (observacion != null) {
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                par = new HashMap<>();
                par.put("aprobado", solicitudAprobada ? 1 : 2);
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observacion.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaDevolutiva() {
        try {
            if (!solicitudAprobada && notaDevolutiva.getId() != null) {
                observacion = "NOTA DEVOLUTIVA: " + notaDevolutiva.getAsunto();
                as.enviarCorreoNotaDevolutiva(liquidacion, SisVars.rutaDevolutivas + liquidacion.getNumTramiteRp()
                        + "-" + notaDevolutiva.getId().toString() + ".pdf", session.getName_user());

                String rolVentanila = "entrega_tramites";
                par = new HashMap<>();
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                par.put("ventanilla", itl.getCandidateUserByRolName(rolVentanila));
                par.put("aprobado", solicitudAprobada ? 1 : 2);
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "No se pudo completar este proceso.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarNotaDevolutiva() {
        try {
            String anio = String.valueOf(cal.get(Calendar.YEAR));
            notaDevolutiva.setNumNotaDevolutiva(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaNotaDevolutiva).toString() + "-" + anio);
            notaDevolutiva = (RegpNotaDevolutiva) manager.persist(notaDevolutiva);
            if (notaDevolutiva != null && notaDevolutiva.getId() != null) {
                this.imprimirNotaDvolutiva(notaDevolutiva.getId());
            } else {
                JsfUti.messageWarning(null, "No se pudo guardar los datos.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void imprimirNotaDvolutiva(Long idNotaDevolutiva) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();

            ss.setGeneraFile(true);
            ss.setRutaDocumento(SisVars.rutaDevolutivas + liquidacion.getNumTramiteRp() + "-" + idNotaDevolutiva.toString() + ".pdf");

            ss.setTieneDatasource(true);
            ss.setNombreReporte("NotaDevolutiva");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", idNotaDevolutiva);
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cropDocHabilitante(RegpDocsTramite rdt) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?id=" + rdt.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showFormularioOnline() {
        try {
            if (ht.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("FormularioOnline");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_TRAMITE", ht.getId());
                ss.agregarParametro("FOTO1", this.getImage(Querys.getOid1Solicitud));
                ss.agregarParametro("FOTO2", this.getImage(Querys.getOid2Solicitud));
                ss.agregarParametro("FOTO3", this.getImage(Querys.getOid3Solicitud));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadDocument() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{ht.getId()});
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public InputStream getImage(String sql) {
        BigInteger oid = (BigInteger) manager.getNativeQuery(sql, new Object[]{ht.getId()});
        if (oid != null) {
            return ou.streamFile(oid.longValue());
        }
        return null;
    }

    public void showDlgNegativa() {
        try {
            JsfUti.update("formNegativa");
            JsfUti.executeJS("PF('dlgNegativa').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarNegativa() {
        try {
            if (negativa != null && !negativa.isEmpty()) {
                liquidacion.setInfAdicional(negativa);
                liquidacion.setIngresado(Boolean.TRUE);
                manager.merge(liquidacion);
                observacion = "Pasa a inscribir negativa.";
                solicitudAprobada = true;
                this.completarTarea();
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el motivo de la NEGATIVA.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion=" + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void nuevaObservacion() {
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");
    }

    public void mantenerEstado() {
        this.guardarObservacion();
        this.continuar();
    }

    public void generarNuevoRepertorio() {
        try {
            observacion = "Cambio de repertorio, repertorio caducado: " + liquidacion.getRepertorio()
                    + ", fecha: " + liquidacion.getFechaRepertorio();
            if (rps.generarNuevoRepertorio(liquidacion)) {
                this.mantenerEstado();
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRepertorio() {
        if (caduco) {
            observacion = "Cambio de secuencia, repertorio caducado: " + liquidacion.getRepertorio()
                    + ", fecha de repertorio: " + liquidacion.getFechaRepertorio();
            if (rps.generarNuevoRepertorio(liquidacion)) {
                this.mantenerEstado();
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } else {
            JsfUti.messageWarning(null, "No se puede realizar esta accion.", "");
        }
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /*public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }*/
    public Boolean getHasDoc() {
        return hasDoc;
    }

    public void setHasDoc(Boolean hasDoc) {
        this.hasDoc = hasDoc;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getSolicitudAprobada() {
        return solicitudAprobada;
    }

    public void setSolicitudAprobada(Boolean solicitudAprobada) {
        this.solicitudAprobada = solicitudAprobada;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public Boolean getTieneNotaDevolutiva() {
        return tieneNotaDevolutiva;
    }

    public void setTieneNotaDevolutiva(Boolean tieneNotaDevolutiva) {
        this.tieneNotaDevolutiva = tieneNotaDevolutiva;
    }

    public List<RegpNotaDevolutiva> getNotaDevolutivaAnalisis() {
        return notaDevolutivaAnalisis;
    }

    public void setNotaDevolutivaAnalisis(List<RegpNotaDevolutiva> notaDevolutivaAnalisis) {
        this.notaDevolutivaAnalisis = notaDevolutivaAnalisis;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public String getNegativa() {
        return negativa;
    }

    public void setNegativa(String negativa) {
        this.negativa = negativa;
    }

    public Boolean getCaduco() {
        return caduco;
    }

    public void setCaduco(Boolean caduco) {
        this.caduco = caduco;
    }

}
