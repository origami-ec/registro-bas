/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.RegpTareasDinardapDocs;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.lazymodels.RegpTareasDinardapLazy;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author gutya
 */
@Named
@ViewScoped
public class TareasDinardapJuridico extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(TareasDinardap.class.getName());
//    public static final long serialVersionUID = 1L;

    @Inject
    protected RegistroPropiedadServices reg;

    @Inject
    private DocumentsManagedLocal doc;

    @Inject
    private ServletSession ss;

    protected Boolean showCer = false, showIns = false, realizado = false;
    protected Integer opcion = 0;
    protected String formatoArchivos;
    protected RegpTareasDinardapLazy tareasLazy;
    protected RegpTareasDinardap tareaDinardap = new RegpTareasDinardap();
    protected RegpTareasTramite tareaRegistro = new RegpTareasTramite();
    protected List<RegpTareasTramite> tareas = new ArrayList<>();
    protected RegTipoCobroActo tipo;
    protected RegRegistrador registrador;
    protected AclUser user;

    @PostConstruct
    protected void iniView() {
        try {
            formatoArchivos = SisVars.formatoArchivos;
            tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, Boolean.TRUE);
            registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
            user = manager.find(AclUser.class, session.getUserId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDetalleTarea(RegpTareasDinardap t) {
        tareaDinardap = t;
        JsfUti.update("formDetalleTarea");
        JsfUti.executeJS("PF('dlgDetalle').show();");
    }

    public void showDocsTarea(RegpTareasDinardap t) {
        tareaDinardap = t;
        JsfUti.update("formDocumentos");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void showDlgIngreso() {
        tareaDinardap = new RegpTareasDinardap();
        JsfUti.update("formInfoTarea");
        JsfUti.executeJS("PF('dlgIngresoTarea').show();");
    }

    public void descargarDocumento(RegpTareasDinardapDocs doc) {
        try {
            if (doc.getDocumento() != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "/DescargarDocsRepositorio?id=" + doc.getDocumento());
            } else {
                JsfUti.messageError(null, "Error, no se encuentra el documento.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgTareas(RegpTareasDinardap t) {
        try {
            tareaRegistro = new RegpTareasTramite();
            tareaDinardap = t;
            tareas = reg.getTareasDinardapTramite(t);
            JsfUti.update("formTareas");
            JsfUti.executeJS("PF('dlgTareas').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaCampos() {
        if (tareaDinardap.getNumeroSolicitud() != null) {
            if (tareaDinardap.getInstitucion() != null) {
                if (tareaDinardap.getSolicitante() != null) {
                    if (tareaDinardap.getObservacion() != null) {
                        return true;
                    }
                }
            }
        }
        JsfUti.messageWarning(null, "Todos los campos son obligatorios.", "");
        return false;
    }

    public void aceptarSolicitud() {
        realizado = false;
        tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, Boolean.TRUE);
        JsfUti.update("mainForm:dtTareasDinardap");
        JsfUti.executeJS("PF('dlgIngresoTarea').hide();");
    }

    public void crearTarea() {
        if (tareaRegistro.getId() != null || (this.validaListas() && tareaRegistro.getId() == null)) {
            if (tipo == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de tarea.", "");
                return;
            }
            this.llenarDatos();
            ss.instanciarParametros();
            ss.agregarParametro("tarea", tareaRegistro.getId());
            if (tipo.getId() == 1L) {
                JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
            } else {
                ss.agregarParametro("tipo", tipo.getId());
                JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");
            }
        } else {
            JsfUti.messageError(null, "Falta de completar Tareas.", "");
        }
    }

    public void llenarDatos() {
        try {
            if (tareaDinardap.getId() != null) {
                tareaRegistro.setRealizado(false);
                tareaRegistro.setEstado(true);
                tareaRegistro.setFecha(new Date());
                //  tareaRegistro.setObservacion(tipo.getNombre());
                //    tareaRegistro.setTareaDinardap(tareaDinardap);
                tareaRegistro = (RegpTareasTramite) manager.persist(tareaRegistro);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void redirecTarea(RegpTareasTramite temp) {
        try {
            tareaRegistro = new RegpTareasTramite();
//            if (temp.getRealizado()) {
//                JsfUti.messageWarning(null, "Tarea Completada", "Ya fue realizada esta tarea.");
//            } else {
//                if (temp.getRegCertificado() != null) {
//                    //JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");
//                    if (tipo == null) {
//                        JsfUti.messageWarning(null, "Debe seleccionar el tipo de certificado.", "");
//                        return;
//                    }
//                    ss.instanciarParametros();
//                    ss.agregarParametro("tarea", temp.getId());
//                    ss.agregarParametro("tipo", tipo.getId());
//                    JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");
//                } else if (reg.getMovimientoByTarea(temp) != null) {
//                    ss.instanciarParametros();
//                    ss.agregarParametro("tarea", temp.getId());
//                    JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
//                } else {
//                    tareaRegistro = temp;
//                    JsfUti.messageError(null, "Ingrese nueva tarea de solicitud.", "No se encuentra certificado ni inscripcion asociada a la tarea.");
//                }
//            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void finalizarSolicitud() {
        try {
            if (tareas.isEmpty()) {
                JsfUti.messageError(null, "No se puede finalizar solicitud", "No se han realizado tareas.");
            } else {
                if (this.validaListas()) {
                    if (tareaDinardap.getId() != null) {
                        tareaDinardap.setRealizado(Boolean.TRUE);
                        tareaDinardap.setFechaFin(new Date());
                        Boolean b = manager.update(tareaDinardap);
                        if (b) {
                            tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, Boolean.TRUE);
                            JsfUti.update("mainForm");
                            JsfUti.executeJS("PF('dlgTareas').hide();");
                        } else {
                            JsfUti.messageError(null, Messages.error, "");
                        }
                    } else {
                        JsfUti.messageError(null, Messages.error, "");
                    }
                } else {
                    JsfUti.messageError(null, "Falta de completar Tareas.", "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean validaListas() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRealizado()) {
                return false;
            }
        }
        return true;
    }

    public void guardarSolicitud() {
        if (this.validaCampos()) {
            tareaDinardap.setEstado(true);
            tareaDinardap.setEsJuridico(true);
            tareaDinardap.setUsuario(session.getName_user());
            tareaDinardap = (RegpTareasDinardap) manager.persist(tareaDinardap);
            tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, Boolean.TRUE);
            JsfUti.executeJS("PF('dlgIngresoTarea').hide();");
            JsfUti.executeJS("PF('dlgDetalle').hide();");
            JsfUti.update("mainForm");
            //JsfUti.update("formObs");
            //JsfUti.executeJS("PF('dlgObsvs').show();");
        }
    }

    public void handleUpload(FileUploadEvent event) throws IOException {
        try {
            if (tareaDinardap.getId() == null) {
                JsfUti.messageError(null, "Se debe guardar la solicitud primero!!!", "");
                return;
            }
            if (doc.saveDocsTareaDinardap(event.getFile(), tareaDinardap, session.getUserId())) {
                tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, Boolean.TRUE);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgObsvs').hide();");
            } else {
                JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void downloadDocTareas(RegpTareasDinardapDocs tdd) {
        try {
            if (tdd.getId() != null) {
                JsfUti.redirectNewTab("/rpm/OmegaDownDocs?code=" + tdd.getDoc() + "&name=" + tdd.getNombreArchivo() + "&tipo=2&content=" + tdd.getContentType());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

//    public void visualizarDoc(RegpTareasTramite temp) {
//        RegMovimiento mo = reg.getMovimientoByTarea(temp);
//        if (temp.getRegCertificado() != null) {
//            this.generarCertificado(temp.getRegCertificado());
//        } else if (mo != null) {
//            this.imprimirInscripcion(mo);
//        } else {
//            JsfUti.messageError(null, "No se encuentra el documento.", "");
//        }
//    }
    public void imprimirInscripcion(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("P_MOVIMIENTO", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.setNombreReporte("ActaInscripcion");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab("/rpm/Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificado(RegCertificado ce) {
        try {
            switch (ce.getTipoCertificado().intValue()) {
                case 1:
                    this.imprimirFichaRegistral(ce);
                    break;
                case 2:
                    this.imprimirNoBienes(ce);
                    break;
                case 3:
                    this.imprimirNoGravamen(ce);
                    break;
                case 4:
                    this.imprimirCertificadoGeneral(ce);
                    break;
                default:
                    JsfUti.messageInfo(null, Messages.error, "");
                    break;
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirFichaRegistral(RegCertificado ce) {
        try {
            if (ce.getFicha() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("USER_NAME", session.getName_user());
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                if (ce.getCertificadoImpreso()) {
                    ss.setNombreReporte("CerHDCab");
                } else {
                    ss.setNombreReporte("CerHDFichaCab");
                }
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("BACKGROUND", JsfUti.getRealPath("/resources/image/background.jpeg"));
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("NOMBRES", user.getEnte().getTituloNombreCompleto().trim().toUpperCase());
                }
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab("/rpm/Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirNoBienes(RegCertificado ce) {
        try {
            if (ce.getPropietario() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setNombreReporte("CertificadoNoBienesCab");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("BACKGROUND", JsfUti.getRealPath("/resources/image/background.jpeg"));
                ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("USER_NAME", session.getName_user());
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("EMISION", ce.getFechaEmision());
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab("/rpm/Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirNoGravamen(RegCertificado ce) {
        try {
            if (ce.getPropietario() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setNombreReporte("CerMerNoGravamenCab");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("BACKGROUND", JsfUti.getRealPath("/resources/image/background.jpeg"));
                ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("USER_NAME", session.getName_user());
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("EMISION", ce.getFechaEmision());
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab("/rpm/Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirCertificadoGeneral(RegCertificado ce) {
        try {
            if (ce.getId() != null && ce.getFechaEmision() != null && ce.getObservacion() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setNombreReporte("CertificadoGeneral");
                ss.setNombreReporte("CertificadoGeneralCab");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("BACKGROUND", JsfUti.getRealPath("/resources/image/background.jpeg"));
                ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("USER_NAME", session.getName_user());
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("EMISION", ce.getFechaEmision());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab("/rpm/Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegTipoCobroActo> getTiposCertificado() {
        return new ArrayList();
    }

    public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }

    public RegpTareasDinardap getTareaDinardap() {
        return tareaDinardap;
    }

    public void setTareaDinardap(RegpTareasDinardap tareaDinardap) {
        this.tareaDinardap = tareaDinardap;
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public RegpTareasDinardapLazy getTareasLazy() {
        return tareasLazy;
    }

    public void setTareasLazy(RegpTareasDinardapLazy tareasLazy) {
        this.tareasLazy = tareasLazy;
    }

    public Boolean getShowCer() {
        return showCer;
    }

    public void setShowCer(Boolean showCer) {
        this.showCer = showCer;
    }

    public Boolean getShowIns() {
        return showIns;
    }

    public void setShowIns(Boolean showIns) {
        this.showIns = showIns;
    }

    public Boolean getRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public RegTipoCobroActo getTipo() {
        return tipo;
    }

    public void setTipo(RegTipoCobroActo tipo) {
        this.tipo = tipo;
    }

    public Collection<RegActo> getActos() {
        return manager.findAll(Querys.getRegActoList);
    }

}
