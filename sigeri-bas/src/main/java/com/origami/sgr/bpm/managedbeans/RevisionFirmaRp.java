/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class RevisionFirmaRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(RevisionFirmaRp.class.getName());

    @Inject
    private AsynchronousService as;

    @Inject
    private FirmaDigitalLocal fd;

    protected HashMap<String, Object> par;
    protected String formatoArchivos;
    protected String observacion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpTareasTramite> tareas;
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected Boolean hasDoc, online = false;
    protected RegRegistrador registrador;
    protected AclUser user;
    protected RegpTareasTramite rtt;
    protected List<Observaciones> observacionesTramites;
    protected List<String> firmados = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    hasDoc = false;
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    if (liquidacion.getEstadoPago().getId() == 7L) {
                        online = true;
                    }
                    this.cargarArchivos(Constantes.indexacionHabilitantes, tramite);
                    observacionesTramites = reg.listarObservacionesPorTramite(ht);
                    tareas = reg.getTareasTramite(ht.getId());
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
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

    public void inactivarTareasRealizas() {
        for (RegpTareasTramite tt : tareas) {
            //if (tt.getDetalle().getActo().getSolvencia()) {
            tt.setRealizado(Boolean.FALSE);
            tt.setRevisado(Boolean.FALSE);
            manager.persist(tt);
            //}
        }
    }

    public boolean validaTareas() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRevisado()) {
                return false;
            }
        }
        return true;
    }

    public void completarTarea() {
        try {
            if (this.validaTareas()) {
                if (!firmados.isEmpty()) {
                    as.enviarCorreoFinTramite(liquidacion, firmados, session.getName_user());
                }
                reg.guardarObservaciones(ht, session.getName_user(), "TAREA REALIZADA", 
                        this.getTaskDataByTaskID().getName());
                par = new HashMap<>();
                par.put("aprobado", 1);
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Debe de realizar todas las tareas que le corresponden.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void regresarTramite() {
        try {
            observacion = "Se regresa el tramite al usuario anterior para correccion.";
            this.inactivarTareasRealizas();
            reg.guardarObservaciones(ht, session.getName_user(), observacion, 
                    this.getTaskDataByTaskID().getName());
            par = new HashMap<>();
            par.put("aprobado", 2);
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pasarDevolutiva() {
        try {
            par = new HashMap<>();
            par.put("catastrar", 3);
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                        + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
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

    public void showDlgDocumentos(RegpTareasTramite tt) {
        rtt = tt;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void actaInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("P_MOVIMIENTO", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.setNombreReporte("ActaInscripcion");
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void razonInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                //String juridico = itl.getUsuarioByRolName("asesor_juridico");
                //String jefe_inscripcion = itl.getUsuarioByRolName("jefe_inscripcion");
                ss.agregarParametro("ID_MOV", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
                //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
                //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                //ss.agregarParametro("REVISOR_LEGAL", juridico.toUpperCase());
                //ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.toUpperCase());
                ss.setNombreReporte("RazonInscripcion");
                ss.setNombreSubCarpeta("registro");
                ss.setTieneDatasource(true);
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificado() {
        try {
            RegCertificado ce = reg.getCertificadoFromTarea(rtt.getId());
            if (ce != null) {
                this.llenarParametros(ce);
                switch (ce.getTipoDocumento()) {
                    case "C01": //CERTIFICADO DE GRAVAMEN CON FICHA
                        ss.setNombreReporte("CertificadoGravamen");
                        break;
                    case "C02": //CERTIFICADO HISTORIADO CON FICHA
                        ss.setNombreReporte("CertificadoGravamenHistoriado");
                        break;
                    case "C03": //CERTIFICADO LINDERADO CON FICHA
                        ss.setNombreReporte("CertificadoGravamenLinderado");
                        break;
                    case "C04": //CERTIFICADO DE VENTAS CON FICHA
                        ss.setNombreReporte("CertificadoGravamenVentas");
                        break;
                    case "C05": //CERTIFICADO DE BIENES
                        ss.setNombreReporte("CertificadoBienes");
                        break;
                    case "C06": //CERTIFICADO GENERAL
                        ss.setNombreReporte("CertificadoGeneral");
                        break;
                    case "C07": //COPIA RAZON INSCRIPCION 
                        this.llenarParametrosRazon(ce);
                        break;
                    default:
                        JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
                        return;
                }
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el certificado para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            user = manager.find(AclUser.class, session.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            //ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosRazon(RegCertificado ce) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            //ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
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

    public void firmarDocumento(RegpTareasTramite tt) {
        try {
            File temp;
            RegCertificado ce = reg.getCertificadoFromTarea(tt.getId());
            if (ce != null && ce.getId() != null) {
                temp = fd.firmarCertificadoPath(ce);
                if (temp != null) {
                    tt.setRevisado(true);
                    tt.setRutaDocumento(temp.getAbsolutePath());
                    manager.update(tt);
                    firmados.add(temp.getAbsolutePath());
                    JsfUti.messageInfo(null, "Documento firmado con exito.", "");
                    return;
                }
            } else {
                temp = as.generarFirmaInscripciones(liquidacion.getNumTramiteRp());
                if (temp != null) {
                    firmados.add(temp.getAbsolutePath());
                    for (RegpTareasTramite ttt : tareas) {
                        if (!ttt.getDetalle().getActo().getSolvencia()) {
                            ttt.setRevisado(Boolean.TRUE);
                            ttt.setRutaDocumento(temp.getAbsolutePath());
                            manager.update(ttt);
                        }
                    }
                    JsfUti.messageInfo(null, "Documento firmado con exito.", "");
                    return;
                }
                /*RegMovimiento mov = reg.getMovimientoFromTarea(tt.getId());
                if (mov != null && mov.getId() != null) {
                    temp = fd.firmarInscripcionFile(mov);
                }*/
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        JsfUti.messageError(null, "No se pudo firmar documento.", "");
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

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

    /*public List<RegCertificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<RegCertificado> certificados) {
        this.certificados = certificados;
    }*/
    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public RegpTareasTramite getRtt() {
        return rtt;
    }

    public void setRtt(RegpTareasTramite rtt) {
        this.rtt = rtt;
    }

}
