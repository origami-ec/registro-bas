/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans.noposeebien;

import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
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
 * @author gutya
 */
@Named
@ViewScoped
public class TareaCertificadoNoPoseeBienRp extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(TareaCertificadoNoPoseeBienRp.class.getName());

    @Inject
    private RegistroPropiedadServices rps;
    
    @Inject
    protected BitacoraServices bs;
    
    @Inject
    private UserSession us;

    private LazyModel tareas;
    protected AclUser user;
    protected RegpLiquidacion liquidacion;
    protected RegRegistrador registrador;
    private RegpTareasTramite regpTareasTramiteSeleccionada;
    protected BigInteger periodo;
    private Boolean habilitarEdicion;
    protected RegCertificado cert = new RegCertificado();

    protected String observacion = "";

    @PostConstruct
    public void init() {
        periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        Map<String, Object> filters = new HashMap<>();
        filters.put("detalle.liquidacion.certificadoSinFlujo", Boolean.TRUE);
        tareas = new LazyModel(RegpTareasTramite.class, filters, "fecha", "DESC");
        registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
        habilitarEdicion = us.getRoles().contains(35L) || us.getRoles().contains(1L);
    }

    public void showDlgEditar(RegCertificado c) {
        this.cert = c;
        JsfUti.update("formHabilitar");
        JsfUti.executeJS("PF('dlgHabilitarEdicion').show();");
    }

    public void aceptarEdicion(Boolean acepto) {
        if (acepto) {
            if (cert != null && cert.getId() != null && cert.getTareaTramite().getRealizado()) {
                cert.setEditable(Boolean.TRUE);
                manager.persist(cert);
                RegpTareasTramite regpTareasTramite = cert.getTareaTramite();
                regpTareasTramite.setRevisado(Boolean.FALSE);
                regpTareasTramite.setRealizado(Boolean.FALSE);
                manager.persist(regpTareasTramite);
                JsfUti.messageInfo(null, "Certificado " + cert.getNumCertificado().toString() + " Listo para Editar", "");
            } else {
                JsfUti.messageWarning(null, "Certificado " + cert.getNumCertificado().toString() + " aun no tiene la tarea completada", "");
            }
            guardarObservacion();
        }
        JsfUti.update("formHabilitar");
        JsfUti.executeJS("PF('dlgHabilitarEdicion').hide();");
    }

    public void guardarObservacion() {
        try {
            bs.registrarEdicionCertificado(cert, ActividadesTransaccionales.APROBAR_EDICION_CERTIFICADO, periodo);
            if (observacion == null || observacion.isEmpty()) {
                rps.guardarObservaciones(cert.getTareaTramite().getTramite(), us.getName_user(), cert.getClaseCertificado(), "HABILITAR EDICION " + cert.getClaseCertificado());
            } else {
                rps.guardarObservaciones(cert.getTareaTramite().getTramite(), us.getName_user(), observacion, "HABILITAR EDICION " + cert.getClaseCertificado());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void realizarTarea(RegpTareasTramite ta) {
        try {
            if (ta.getRealizado()) {
                JsfUti.messageWarning(null, "Tarea de Contrato ya fue concluida.", "");
            } else {
                ss.instanciarParametros();
                ss.agregarParametro("tarea", ta.getId());
                ss.agregarParametro("tramite", ta.getDetalle().getLiquidacion().getNumTramiteRp());
                JsfUti.redirectFaces("/procesos/registro/certificarNoPoseeBien.xhtml");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String usuario(Long id) {
        AclUser user = (AclUser) manager.find(Querys.getAclUserByID, new String[]{"id"}, new Object[]{id});
        if (user != null) {
            return user.getUsuario();
        } else {
            return "";
        }
    }

    public void entregaTramiteTarea(RegpTareasTramite ta) {
        try {
            map = new HashMap();
            map.put("numTramite", ta.getDetalle().getLiquidacion().getNumTramiteRp());
            HistoricoTramites ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);

            if (ta.getRealizado()) {
                if (Utils.isEmpty(rps.listarObservacionesPorTramite(ht))) {
                    ss.instanciarParametros();
                    ss.agregarParametro("tarea", ta.getId());
                    ss.agregarParametro("tramite", ta.getDetalle().getLiquidacion().getNumTramiteRp());
                    JsfUti.redirectFaces("/procesos/registro/entregaCertificadoNoPoseer.xhtml");
                } else {
                    JsfUti.messageWarning(null, "Trámite ha sido Entregado", "");
                }
            } else {
                JsfUti.messageWarning(null, "Tarea de no ha sido Concluida", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirCertificado(RegpTareasTramite regpTareasTramite) {
        if (regpTareasTramite.getRevisado()) {
            RegCertificado c = (RegCertificado) manager.find(Querys.getCertificadoByTarea, new String[]{"tarea"}, new Object[]{regpTareasTramite});
            if (c != null) {
                generarCertificado(c, regpTareasTramite);
            } else {
                JsfUti.messageWarning(null, "Certificado aun esta pendiente de elaboración", "");
            }
        } else {
            JsfUti.messageWarning(null, "Certificado aun no está Aprobado", "");
        }

    }

    public void generarCertificado(RegCertificado ce, RegpTareasTramite regpTareasTramite) {
        try {
            
            switch (ce.getTipoDocumento()) {
                case "C01"://MO POSEER BIEN
                    this.llenarParametros(ce);
                    ss.setNombreReporte("CertificadoNoBienes");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                case "C08"://COPIA RAZON DE INSCRIPCION
                    this.llenarParametrosRazon(ce);
                    ss.setNombreReporte("CopiaRazonInscripcion");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                case "C09": //UNICO BIEN
                    this.llenarParametros(ce);
                    ss.setNombreReporte("CertificadoSiPoseeBien");
                    JsfUti.redirectNewTab("/sgr/Documento");
                    break;
                default:
                    JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            user = manager.find(AclUser.class, us.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            //ss.setIdCertficado(ce.getId());
            //ss.setFirmarCertificado(registrador.isFirmaElectronica());
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header_loja.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark_loja.png"));
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer_loja.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }
    
    public void llenarParametrosRazon(RegCertificado ce) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            //ss.setIdCertficado(ce.getId());
            //ss.setFirmarCertificado(registrador.isFirmaElectronica());
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header_loja.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark_loja.png"));
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer_loja.png"));
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void showInfo(RegpTareasTramite regpTareasTramite) {
        try {
            this.regpTareasTramiteSeleccionada = regpTareasTramite;
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public LazyModel getTareas() {
        return tareas;
    }

    public void setTareas(LazyModel tareas) {
        this.tareas = tareas;
    }

    public RegpTareasTramite getRegpTareasTramiteSeleccionada() {
        return regpTareasTramiteSeleccionada;
    }

    public void setRegpTareasTramiteSeleccionada(RegpTareasTramite regpTareasTramiteSeleccionada) {
        this.regpTareasTramiteSeleccionada = regpTareasTramiteSeleccionada;
    }

    public Boolean getHabilitarEdicion() {
        return habilitarEdicion;
    }

    public void setHabilitarEdicion(Boolean habilitarEdicion) {
        this.habilitarEdicion = habilitarEdicion;
    }

    public RegCertificado getCert() {
        return cert;
    }

    public void setCert(RegCertificado cert) {
        this.cert = cert;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
