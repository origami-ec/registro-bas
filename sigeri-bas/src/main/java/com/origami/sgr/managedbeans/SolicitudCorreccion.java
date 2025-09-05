/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpCorreccion;
import com.origami.sgr.entities.RegpCorreccionDetalles;
import com.origami.sgr.entities.RegpEstadoCorreccion;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.lazymodels.RegpCorreccionLazy;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.session.ServletSession;
import com.origami.sql.ConsultasSqlLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author asilva
 */
@Named
@ViewScoped
public class SolicitudCorreccion extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(SolicitudCorreccion.class.getName());

    @Inject
    private ServletSession ss;

    @EJB(beanName = "registroPropiedad")
    private RegistroPropiedadServices reg;

    @EJB(beanName = "consultasSql")
    private ConsultasSqlLocal sql;

    @EJB(beanName = "documentsManaged")
    private DocumentsManagedLocal doc;

    protected RegpCorreccionLazy lazyCorrs;
    protected RegpCorreccion sc;
    protected RegpCorreccionDetalles scd;
    protected RegpEstadoCorreccion estadoCorreccion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpTareasTramite> tareas;
    protected RegpTareasTramite tarea;
    protected CatEnte userRetira;
    protected AclUser funcionario;
    protected String motivo = null;
    protected String asignado = null;
    protected Boolean permitido = false;
    protected Boolean render = false;
    protected Boolean renderS = false;//Supervisor
    protected Boolean renderR = false;//Retira doc
    protected Boolean renderC = false;//Certificador-Inscriptor
    protected Boolean renderD = false;//Digitalizador
    protected Long tramite;
    protected List<RegCertificado> certificados;
    protected AclUser user;
    protected RegRegistrador registrador;

    @PostConstruct
    protected void iniView() {
        try {
            this.setTaskId(session.getTaskID());
            ht = new HistoricoTramites();
            liquidacion = new RegpLiquidacion();
            userRetira = new CatEnte();
            funcionario = new AclUser();
            lazyCorrs = new RegpCorreccionLazy();
            sc = new RegpCorreccion();
            scd = new RegpCorreccionDetalles();
            estadoCorreccion = manager.find(RegpEstadoCorreccion.class, 1L); //Ingresada
            certificados = new ArrayList<>();
            registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles(Long rol) {
        permitido = false;
        for (Long l : session.getRoles()) {
            if (Objects.equals(l, rol)) {
                permitido = true;
            }
        }
    }

    public Boolean validaRolFuncionario(String rol, String user) {
        List<AclUser> lstUsers = sql.getUsuariosByRolName(rol);
        for (AclUser u : lstUsers) {
            if (u.getUsuario().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void redirectFacelet(String urlFacelet) {
        JsfUti.redirectFaces(urlFacelet);
    }

    public void buscarTramite() {
        try {
            map = new HashMap();
            map.put("numTramite", tramite);
            ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
            if (ht != null) {
                if (reg.tramiteFinalizado(tramite)) {
                    map = new HashMap();
                    map.put("numTramiteRp", ht.getNumTramite());
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    if (liquidacion == null) {
                        liquidacion = new RegpLiquidacion();
                        JsfUti.messageWarning(null, "No se encontro el Tramite con el numero: " + tramite, "");
                    } else {
                        render = true;
                        JsfUti.update("mainForm");
                    }
                } else {
                    JsfUti.messageWarning(null, "El Tramite " + tramite + " aun no ha finalizado", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de Tramite.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarSolicitud() {
        Boolean ingresaSolicitud = false;
        if (motivo == null) {
            JsfUti.messageWarning(null, "Debe ingresar una observacion para solicitar la correccion.", "");
        } else {
            map = new HashMap();
            map.put("numTramite", tramite);
            ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
            if (ht != null) {
                map = new HashMap();
                map.put("idTramite", ht);
                RegpCorreccion c = (RegpCorreccion) manager.findObjectByParameter(RegpCorreccion.class, map);
                if (c != null) {
                    if (c.getFechaEntrega() == null) {
                        JsfUti.messageWarning(null, "Ya se ingreso una solicitud con el numero Tramite: " + tramite, "");
                    } else {
                        ingresaSolicitud = true;
                    }
                } else {
                    ingresaSolicitud = true;
                }

                if (ingresaSolicitud) {
                    sc = new RegpCorreccion();
                    scd = new RegpCorreccionDetalles();
                    sc.setIdTramite(ht);
                    sc.setFechaIngreso(new Date());
                    sc.setUserCreacion(session.getName_user());
                    sc.setEstadoCorreccion(manager.find(RegpEstadoCorreccion.class, 1L));
                    sc = (RegpCorreccion) manager.persist(sc);

                    scd.setCorreccion(sc);
                    scd.setUserIngreso(session.getName_user());
                    scd.setObservacion(motivo);
                    scd.setFechaElaboracion(new Date());
                    scd = (RegpCorreccionDetalles) manager.persist(scd);
                    
                    manager.updateNativeQuery(Querys.updateCertificadosCorreccion, new Object[]{ht.getNumTramite()});

                    JsfUti.messageInfo(null, "Solicitud de Correccion ingresada con exito", "");
                    JsfUti.update("mainForm");
                }
            }
        }
    }

    public void revisarSolicitud(RegpCorreccion sol) {
        Boolean openDlg = false;
        certificados =  new ArrayList<>();
        try {
            sc = sol;
            ht = sol.getIdTramite();
            motivo = null;
            
            certificados = manager.findAll(Querys.getCertificados, new String[]{"tramite"}, new Object[]{ht.getNumTramite()});

            map = new HashMap();
            map.put("correccion", sol);
            scd = (RegpCorreccionDetalles) manager.findObjectByParameter(RegpCorreccionDetalles.class, map);

            if (scd != null) {
                map = new HashMap();
                map.put("numTramiteRp", ht.getNumTramite());
                liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);

                switch (scd.getCorreccion().getEstadoCorreccion().getId().intValue()) {
                    case 1:
                        this.validaRoles(18L);//Supervisor registral
                        if (permitido) {
                            renderS = true;
                            openDlg = true;
                        } else {
                            JsfUti.messageWarning(null, "Usuario no autorizado para reasignar solicitudes", "");
                        }

                        break;
                    case 2:
                        if (scd.getCorreccion().getUserAsignado().equals(session.getName_user())) {
                            this.validaRoles(8L); //Certificador/Registrador
                            if (permitido) {
                                tareas = reg.getTareasTramite(ht.getId());
                                renderC = true;
                                openDlg = true;
                            } else {
                                this.validaRoles(9L); //Certificador/Registrador
                                if (permitido) {
                                    tareas = reg.getTareasTramite(ht.getId());
                                    renderC = true;
                                    openDlg = true;
                                } else {
                                    JsfUti.messageWarning(null, "Funcionario no autorizado para corregir la solicitud", "");
                                }
                            }
                        } else {
                            JsfUti.messageWarning(null, "Funcionario Inscriptor/Certificador no autorizado", "");
                        }

                        break;
                    case 3:
                        this.validaRoles(19L);//Registrador
                        if (permitido) {
                            openDlg = true;
                        } else {
                            JsfUti.messageWarning(null, "Usuario no autorizado para firmar la solicitud", "");
                        }

                        break;

                    case 4:
                        this.validaRoles(22L);//Digitalizacion final
                        if (permitido) {
                            tareas = reg.getTareasTramite(ht.getId());
                            renderD = true;
                            openDlg = true;
                        } else {
                            JsfUti.messageWarning(null, "Usuario no autorizado para digitalizar la solicitud", "");
                        }

                        break;

                    case 5:
                        this.validaRoles(13L);//Entrega de docs
                        if (permitido) {
                            renderR = true;
                            openDlg = true;
                        } else {
                            JsfUti.messageWarning(null, "Usuario no autorizado para entregar la solicitud", "");
                        }

                        break;
                    default:
                        JsfUti.redirectFaces("/admin/manage/solicitudCorreccion.xhtml");
                        break;
                }

                if (openDlg) {
                    JsfUti.update("formRevision");
                    JsfUti.executeJS("PF('dlgRevisionSolicitudRp').show();");
                }
            } else {
                JsfUti.messageWarning(null, "No se encontraron los datos en la solicitud", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void procesar(RegpCorreccionDetalles sol) {
        RegpCorreccionDetalles scds = new RegpCorreccionDetalles();
        Boolean registrar = false;
        scd = sol;
        sc = scd.getCorreccion();

        if (motivo == null) {
            JsfUti.messageWarning(null, "Debe ingresar una observacion para procesar la solicitud.", "");
        } else {
            scds.setCorreccion(scd.getCorreccion());
            scds.setUserIngreso(session.getName_user());
            scds.setFechaElaboracion(new Date());
            scds.setObservacion(motivo);

            switch (scd.getCorreccion().getEstadoCorreccion().getId().intValue()) {
                case 1: //Asignacion de Funcionario certificador/inscriptor
                    if (funcionario.getId() != null) {
                        if (this.validaRolFuncionario("certificador", funcionario.getUsuario()) || this.validaRolFuncionario("inscriptor", funcionario.getUsuario())) {
                            registrar = true;
                            sc.setUserAsignado(funcionario.getUsuario());
                            sc.setEstadoCorreccion(manager.find(RegpEstadoCorreccion.class, 2L));
                        } else {
                            JsfUti.messageWarning(null, "Funcionario asignado no tiene rol Certificador/Inscriptor", "");
                        }
                    } else {
                        JsfUti.messageWarning(null, "Debe asignar un funcionario continuar procesando la solicitud.", "");
                    }

                    break;
                case 2: //Edicion del certificador/inscriptor
                    registrar = true;
                    sc.setEstadoCorreccion(manager.find(RegpEstadoCorreccion.class, 3L));

                    break;
                case 3: //Firma del registrador
                    registrar = true;
                    sc.setEstadoCorreccion(manager.find(RegpEstadoCorreccion.class, 4L));
                    manager.updateNativeQuery(Querys.updateCertificadosRevisados, new Object[]{session.getUserId(), sc.getIdTramite().getNumTramite()});
                    break;
                case 4: //Digitalizacion del documento
                    registrar = true;
                    sc.setEstadoCorreccion(manager.find(RegpEstadoCorreccion.class, 5L));

                    break;

                case 5: //Entrega del documento
                    if (userRetira.getId() == null) {
                        JsfUti.messageWarning(null, "Debe escoger el cliente que retira la solicitud.", "");
                    } else {
                        registrar = true;
                        sc.setUserRetira(userRetira);
                        sc.setFechaEntrega(new Date());
                        sc.setEstadoCorreccion(manager.find(RegpEstadoCorreccion.class, 6L));
                    }

                    break;
                default:
                    JsfUti.redirectFaces("/admin/manage/solicitudCorreccion.xhtml");
                    break;
            }

            if (registrar) {
                sc = (RegpCorreccion) manager.persist(sc);
                manager.persist(scds);
                JsfUti.messageInfo(null, "Revision realizada con exito", "");

                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgRevisionSolicitudRp').hide();");
            }
        }
    }

    public void realizarTarea(RegpTareasTramite ta) {
        try {
            if (ta.getCorregido()) {
                JsfUti.messageWarning(null, "La solicitud ya fue corregida.", "");
            } else {
                if (ta.getDetalle().getActo().getSolvencia()) {
                    RegCertificado c = (RegCertificado) manager.find(Querys.getCertificadoByTarea, new String[]{"tarea"}, new Object[]{ta.getId()});
                    ss.borrarDatos();
                    ss.instanciarParametros();
                    if (c != null) {
                        ss.agregarParametro("certificado", c.getId());
                    } else {
                        ss.agregarParametro("tarea", ta.getId());
                    }
                    JsfUti.executeJS("PF('dlgRevisionSolicitudRp').hide();");
                    JsfUti.redirectFaces("/procesos/manage/correccionCertificado.xhtml");
                } else {
                    RegMovimiento mov = (RegMovimiento) manager.find(Querys.getRegMovimientoByTramite, new String[]{"tramite"}, new Object[]{ta.getId()});

                    ss.instanciarParametros();
                    ss.agregarParametro("idMov", mov.getId());
                    JsfUti.redirectFaces("/procesos/manage/correccionInscripcion.xhtml");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocument(RegpTareasTramite tt) {
        if (tt.getRealizado()) {
            tarea = tt;
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        } else {
            JsfUti.messageWarning(null, "Debe completarse primero las Tareas de Certificacion o Inscripcion del tramite.", "");
        }
    }

    public void uploadDocTareaTramite(FileUploadEvent event) throws IOException {
        try {
            if (tarea.getId() != null) {
                if (doc.saveDocumentoTarea(event.getFile(), tarea, session.getUserId())) {
                    tarea.setReemplazo(Boolean.TRUE);
                    JsfUti.update("formRevision:tabRevision:dtTareas");
                    JsfUti.executeJS("PF('dlgObsvs').hide();");
                    JsfUti.messageInfo(null, "Documento cargado con exito!!!", "");
                } else {
                    JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
                }
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }
    
    public void generarCertificado(RegCertificado ce) {
        try {
            user = manager.find(AclUser.class, ce.getUserCreador());
            switch (ce.getTipoCertificado().intValue()) {
                case 1:
                    this.imprimirFichaRegistral(ce);
                    break;
                case 2:
                    this.imprimirBienRaiz(ce);
                    break;
                case 3:
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

            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("FD_CHD_Validate");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
            ss.agregarParametro("USER_NAME", user.getUsuario());
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getTituloNombreCompleto().trim().toUpperCase());
            }
            JsfUti.redirectNewTab("/sgr/pdfjs/web/visor.html?file=/rpp/Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBienRaiz(RegCertificado ce) {
        try {
            if (ce.getPropietario() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setNombreReporte("FD_CBR_Validate");
                ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("USER_NAME", user.getUsuario());
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("EMISION", ce.getFechaEmision());
                JsfUti.redirectNewTab("/sgr/pdfjs/web/visor.html?file=/rpp/Documento");
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
                ss.setNombreReporte("FD_CG_Validate");
                ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("USER_NAME", user.getUsuario());
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("EMISION", ce.getFechaEmision());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                JsfUti.redirectNewTab("/sgr/pdfjs/web/visor.html?file=/rpp/Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectFuncionario(SelectEvent event) {
        funcionario = (AclUser) event.getObject();
    }

    public void selectCliente(SelectEvent event) {
        userRetira = (CatEnte) event.getObject();
    }

    public Boolean getRender() {
        return render;
    }

    public void setRender(Boolean render) {
        this.render = render;
    }

    public Boolean getRenderS() {
        return renderS;
    }

    public void setRenderS(Boolean renderS) {
        this.renderS = renderS;
    }

    public Boolean getRenderR() {
        return renderR;
    }

    public void setRenderR(Boolean renderR) {
        this.renderR = renderR;
    }

    public Boolean getRenderC() {
        return renderC;
    }

    public void setRenderC(Boolean renderC) {
        this.renderC = renderC;
    }

    public Boolean getRenderD() {
        return renderD;
    }

    public void setRenderD(Boolean renderD) {
        this.renderD = renderD;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getAsignado() {
        return asignado;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    public AclUser getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(AclUser funcionario) {
        this.funcionario = funcionario;
    }

    public CatEnte getUserRetira() {
        return userRetira;
    }

    public void setUserRetira(CatEnte userRetira) {
        this.userRetira = userRetira;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public RegpCorreccionLazy getLazyCorrs() {
        return lazyCorrs;
    }

    public void setLazyCorrs(RegpCorreccionLazy lazyCorrs) {
        this.lazyCorrs = lazyCorrs;
    }

    public RegpCorreccion getSc() {
        return sc;
    }

    public void setSc(RegpCorreccion sc) {
        this.sc = sc;
    }

    public RegpCorreccionDetalles getScd() {
        return scd;
    }

    public void setScd(RegpCorreccionDetalles scd) {
        this.scd = scd;
    }

    public RegpEstadoCorreccion getEstadoCorreccion() {
        return estadoCorreccion;
    }

    public void setEstadoCorreccion(RegpEstadoCorreccion estadoCorreccion) {
        this.estadoCorreccion = estadoCorreccion;
    }

    public RegpTareasTramite getTarea() {
        return tarea;
    }

    public void setTarea(RegpTareasTramite tarea) {
        this.tarea = tarea;
    }

    public List<RegCertificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<RegCertificado> certificados) {
        this.certificados = certificados;
    }

}
