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
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegCertificadoPropietario;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.ejbs.RegCertificadoService;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.DateUtilRpp;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Certificar extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(Certificar.class.getName());

    @Inject
    private SeqGenMan sec;
    
    @Inject
    @Rpp
    private DateUtilRpp dateUtil;
    
    @Inject
    private RegCertificadoService certServ;

    protected Long numFicha;
    protected Long tarea;
    protected HashMap<String, Object> par;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected RegpTareasTramite tt;
    protected Integer render = 0;
    protected RegRegistrador registrador;
    protected RegCertificado certificado;
    protected RegFicha ficha = new RegFicha();
    protected String solvencia = "";
    protected Date fecha = new Date();
    protected AclUser user = new AclUser();
    protected Boolean razon = Boolean.FALSE;
    protected ContenidoReportes contenido;
    protected RegEnteInterviniente propietario = new RegEnteInterviniente();
    protected RegEnteInterviniente interviniente = new RegEnteInterviniente();
    protected List<RegMovimiento> movimientos = new ArrayList<>();
    protected List<RegMovimiento> temps = new ArrayList<>();
    protected List<RegFichaPropietarios> listProp = new ArrayList<>();
    protected List<RegCertificadoMovimiento> historia = new ArrayList<>();
    protected List<RegCertificadoPropietario> propietarios = new ArrayList<>();
    protected List<ContenidoReportes> contenidos = new ArrayList<>();

    //protected RegMovimiento movimiento = new RegMovimiento();
    protected RegCertificadoMovimiento rcm = new RegCertificadoMovimiento();
    protected boolean requiereCambio;
    protected RegTipoFicha tipoFicha;
    protected Boolean archivo = false;
    protected RegMovimiento movimiento = new RegMovimiento();
    protected RegMovimientoCliente cliente = new RegMovimientoCliente();
    protected List<RegMovimientoCliente> rmcs = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            requiereCambio = false;
            if (ss.getParametros() == null) {
                JsfUti.redirectFaces("/procesos/dashBoard.xhtml");
            } else if (ss.getParametros().get("tarea") == null || ss.getParametros().get("taskID") == null) {
                JsfUti.redirectFaces("/procesos/dashBoard.xhtml");
            } else {
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);

                String taskID = (String) ss.getParametros().get("taskID");
                this.setTaskId(taskID);
                if (ss.getParametros().get("archivo") != null) {
                    archivo = (Boolean) ss.getParametros().get("archivo");
                }
                tarea = (Long) ss.getParametros().get("tarea");
                tt = manager.find(RegpTareasTramite.class, tarea);
                map = new HashMap();
                map.put("tareaTramite", tt);
                certificado = (RegCertificado) manager.findObjectByParameter(RegCertificado.class, map);
                if (certificado == null) {
                    certificado = new RegCertificado();
                    // generar codigo verficación único
                    certificado.setCodVerificacion(certServ.genCodigoVerif());
                    certificado.setNombreSolicitante(liquidacion.getSolicitante().getNombresApellidos());
                    certificado.setUsoDocumento(liquidacion.getUsoDocumento().getValor());
                }
                if (certificado.getId() != null) {
                    historia = (List<RegCertificadoMovimiento>) certificado.getRegCertificadoMovimientoCollection();
                    propietarios = reg.getPropsByCertificado(certificado.getId());
                }
                if (certificado.getObservacion() != null) {
                    solvencia = certificado.getObservacion();
                }
                if (certificado.getFicha() != null) {
                    ficha = certificado.getFicha();
                    numFicha = ficha.getNumFicha();
                    manager.evict(ficha);
                } else if (liquidacion.getNumFicha() != null) {
                    numFicha = liquidacion.getNumFicha();
                    manager.evict(ficha);
                }
                if (certificado.getPropietario() != null) {
                    propietario = certificado.getPropietario();
                }
                if (certificado.getFechaEmision() != null) {
                    fecha = certificado.getFechaEmision();
                }
                if (certificado.getDescripcionBien() != null) {
                    if (!certificado.getDescripcionBien().isEmpty()) {
                        requiereCambio = true;
                    }
                }
                user = manager.find(AclUser.class, session.getUserId());
                map = new HashMap();
                map.put("actual", Boolean.TRUE);
                registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                map = new HashMap();
                map.put("code", "SOLVENCIA_CERTIFICADO");
                contenidos = manager.findObjectByParameterList(ContenidoReportes.class, map);
                if (ss.getParametros().get("tipo") == null) {
                    certificado.setTipoDocumento(tt.getDetalle().getActo().getAbreviatura());
                } else {
                    certificado.setTipoDocumento((String) ss.getParametros().get("tipo"));
                }
                tipoFicha = manager.find(RegTipoFicha.class, 1L);
                this.renderizarPaneles(certificado.getTipoDocumento());
                ss.instanciarParametros();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void renderizarPaneles(String code) {
        switch (code) {
            case "C01": //CERTIFICADO DE GRAVAMEN CON FICHA
                render = 1;
                certificado.setTipoCertificado(1L);
                break;
            case "C02": //CERTIFICADO HISTORIADO CON FICHA
                render = 1;
                certificado.setTipoCertificado(2L);
                break;
            case "C03": //CERTIFICADO LINDERADO CON FICHA
                render = 1;
                certificado.setTipoCertificado(3L);
                break;
            case "C04": //CERTIFICADO DE VENTAS CON FICHA
                render = 1;
                certificado.setTipoCertificado(4L);
                break;
            case "C05": //CERTIFICADO DE BIENES
                render = 2;
                certificado.setTipoCertificado(5L);
                break;
            case "C06": //CERTIFICADO GENERAL
                render = 3;
                certificado.setTipoCertificado(6L);
                certificado.setDescripcionBien(certificado.getClaseCertificado());
                break;
            case "C07": //COPIA DE RAZON DE INSCRIPCION
                render = 4;
                certificado.setTipoCertificado(7L);
                break;
            default:
                render = 0;
                break;
        }
    }

    public void nameReport(String code) {
        switch (code) {
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
            case "C07": //COPIA DE RAZON DE INSCRIPCION
                ss.setNombreReporte("CopiaRazonInscripcion");
                break;
        }
        JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        JsfUti.update("mainForm");
    }

    public void llenarParametros() {
        try {
            ss.instanciarParametros();
            ss.setFirmarCertificado(Boolean.FALSE);
            ss.setIdCertificado(null);
            //ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", certificado.getId());
            ss.agregarParametro("EMISION", certificado.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", certificado.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", certificado.getUsoDocumento());
            ss.agregarParametro("ADICIONAL", requiereCambio);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            this.nameReport(certificado.getTipoDocumento());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void llenarParametrosInforme() {
        try {
            ss.instanciarParametros();
            ss.setFirmarCertificado(Boolean.FALSE);
            ss.setIdCertificado(null);
            //ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("NOMBRE", this.certificado.getBeneficiario());
            ss.agregarParametro("BUSQUEDA", this.certificado.getLinderosRegistrales());
            ss.agregarParametro("ID_CERTIFICADO", certificado.getId());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CertificadoInformeBienes");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            JsfUti.update("mainForm");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void agregarHistoria(RegMovimiento mov) {
        if (this.validarMovs(mov.getId())) {
            RegCertificadoMovimiento cm = new RegCertificadoMovimiento();
            cm.setMovimiento(mov);
            historia.add(cm);
            JsfUti.update("mainForm");
        } else {
            JsfUti.messageWarning(null, "Ya se encuentra seleccionado el mismo movimiento.", "");
        }
    }

    public boolean validarMovs(Long id) {
        return historia.stream().noneMatch((cm) -> (cm.getMovimiento().getId().equals(id)));
    }

    public void agregarAllMovs() {
        if (!temps.isEmpty()) {
            this.cargarPropietarios();
            //this.llenarPropietarios();
            //historia = new ArrayList<>();
            RegCertificadoMovimiento cm;
            for (RegMovimiento m : temps) {
                cm = new RegCertificadoMovimiento();
                cm.setMovimiento(m);
                historia.add(cm);
            }
            temps = new ArrayList<>();
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgMovimientos').hide();");
            JsfUti.messageInfo(null, "Se cargaron los movimientos seleccionados.", "");
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar los movimientos para el certificado.", "");
        }
    }

    public void cargarPropietarios() {
        try {
            RegCertificadoPropietario cp;
            for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                cp = new RegCertificadoPropietario();
                cp.setPropietario(fp.getPropietario());
                cp.setDocumento(fp.getPropietario().getCedRuc());
                cp.setNombres(fp.getPropietario().getNombre());
                propietarios.add(cp);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void llenarPropietarios() {
        try {
            int flag;
            RegCertificadoPropietario cp;
            for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                flag = 0;
                for (RegCertificadoPropietario cps : propietarios) {
                    if (Objects.equals(cps.getPropietario().getId(), fp.getPropietario().getId())) {
                        cps.setDocumento(fp.getPropietario().getCedRuc());
                        cps.setNombres(fp.getPropietario().getNombre());
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    cp = new RegCertificadoPropietario();
                    cp.setPropietario(fp.getPropietario());
                    cp.setDocumento(fp.getPropietario().getCedRuc());
                    cp.setNombres(fp.getPropietario().getNombre());
                    propietarios.add(cp);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminarMovimiento(int indice) {
        RegCertificadoMovimiento cm = historia.remove(indice);
        if (cm.getId() != null) {
            cm = (RegCertificadoMovimiento) manager.merge(cm);
            manager.delete(cm);
        }
    }

    public void eliminarProp(int indice) {
        RegCertificadoPropietario cp = propietarios.remove(indice);
        if (cp.getId() != null) {
            manager.delete(cp);
        }
    }

    public void selectObjectMov(SelectEvent event) {
        try {
            /*movimiento = (RegMovimiento) event.getObject();
            rcm = new RegCertificadoMovimiento();
            rcm.setMovimiento(movimiento);
            historia.add(rcm);*/
            
            RegCertificadoMovimiento temp = new RegCertificadoMovimiento();
            temp.setMovimiento((RegMovimiento) event.getObject());
            historia.add(temp);
            
            //JsfUti.update("formAntecedente");
            //JsfUti.executeJS("PF('dlgAntecedente').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectObjectRazon(SelectEvent event) {
        try {
            RegMovimiento temp = (RegMovimiento) event.getObject();
            rcm = new RegCertificadoMovimiento();
            rcm.setMovimiento(temp);
            historia.add(rcm);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarHistoria() {
        try {
            rcm = new RegCertificadoMovimiento();
            rcm.setMovimiento(movimiento);
            historia.add(rcm);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarFichaRegistral() {
        try {
            if (numFicha != null) {
                if (propietarios.isEmpty() && historia.isEmpty()) {
                    map = new HashMap();
                    map.put("numFicha", numFicha);
                    map.put("tipoFicha", new RegTipoFicha(1L));
                    ficha = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
                    if (ficha != null) {
                        if (ficha.getEstado().getValor().equalsIgnoreCase("INACTIVO")) {
                            JsfUti.messageError(null, "Estado de la Ficha: " + ficha.getEstado().getValor(), "No se puede generar certificado.");
                            return;
                        }
                        StringBuffer validarInscripcion = itl.validarInscripcion(ficha);
                        if (validarInscripcion != null) {
                            advertencias = validarInscripcion.toString();
                        }
                        certificado.setDescripcionBien(ficha.getDescripcionBien());
                        certificado.setLinderosRegistrales(ficha.getLinderos());
                        movimientos = reg.getMovimientosByFicha(ficha.getId());
                        JsfUti.update("frmMovs");
                        JsfUti.executeJS("PF('dlgMovimientos').show();");
                        JsfUti.messageWarning(null, "Estado de la Ficha: " + ficha.getEstado().getValor(), "");
                    } else {
                        JsfUti.messageWarning(null, "No se encontro la ficha.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "Las listas de propietarios y/o movimientos no esta vacia.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de ficha.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarFichaMercantil() {
        try {
            if (numFicha != null) {
                if (propietarios.isEmpty() && historia.isEmpty()) {
                    map = new HashMap();
                    map.put("numFicha", numFicha);
                    map.put("tipoFicha", tipoFicha);
                    ficha = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
                    if (ficha != null) {
                        StringBuffer validarInscripcion = itl.validarInscripcion(ficha);
                        if (validarInscripcion != null) {
                            advertencias = validarInscripcion.toString();
                        }
                        certificado.setLinderosRegistrales(ficha.getLinderos());
                        movimientos = reg.getMovimientosByFicha(ficha.getId());
                        JsfUti.update("frmMovs");
                        JsfUti.executeJS("PF('dlgMovimientos').show();");
                    } else {
                        JsfUti.messageWarning(null, "No se encontro la ficha.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "Las listas de propietarios y/o movimientos no esta vacia.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de ficha.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateSolvencia() {
        if (contenido.getValor() != null) {
            solvencia = contenido.getValor();
        }
    }

    public void guardarCertificadoNoBienes() {
        try {
            /*if (!certificado.getNombreSolicitante().isEmpty() && !certificado.getUsoDocumento().isEmpty()
                    && !certificado.getLinderosRegistrales().isEmpty() && !certificado.getBeneficiario().isEmpty()) {*/
            if (!certificado.getLinderosRegistrales().isEmpty() && !certificado.getBeneficiario().isEmpty()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, Constantes.diasValidezCertificado));
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                if (requiereCambio) {
                    if (certificado.getDescripcionBien() != null) {
                        if (certificado.getDescripcionBien().isEmpty()) {
                            JsfUti.messageWarning(null, "Debe de especificar la observacion.", "");
                            return;
                        }
                    }
                } else {
                    certificado.setDescripcionBien(null);
                }
                certificado.setFechaCreacion(new Date());
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                certificado.setUserCreador(session.getUserId());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                //certificado.setTipoCertificado(1L);
                certificado.setRegistrador(registrador.getId());
                certificado = (RegCertificado) manager.merge(certificado);

                this.llenarParametros();
                //this.llenarParametrosFirmaElectronica();
            } else {
                JsfUti.messageWarning(null, "Todos los campos son obligatorios.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoGeneral() {
        try {
            if (!certificado.getObservacion().isEmpty() && !certificado.getNombreSolicitante().isEmpty()
                    && !certificado.getUsoDocumento().isEmpty()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, Constantes.diasValidezCertificado));
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setFechaCreacion(new Date());
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                certificado.setUserCreador(session.getUserId());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setRegistrador(registrador.getId());
                certificado = (RegCertificado) manager.merge(certificado);
                this.llenarParametros();
            } else {
                JsfUti.messageWarning(null, "Todos los campos son obligatorios para generar el certificado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoConficha() {
        try {
            if (this.validaciones()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, Constantes.diasValidezCertificado));
                certificado.setFicha(ficha);
                if (ficha.getDescripcionBien() != null) {
                    certificado.setDescripcionBien(ficha.getDescripcionBien());
                }
                if (ficha.getClaveCatastral() != null) {
                    certificado.setClaveCatastral(ficha.getClaveCatastral());
                }
                /*if (ficha.getParroquia() != null) {
                    if (HiberUtil.isProxy(ficha.getParroquia())) {
                        ficha.setParroquia(HiberUtil.unproxy(ficha.getParroquia()));
                    }
                    certificado.setCatParroquia(ficha.getParroquia());
                    certificado.setCatParroquia(new CatParroquia(ficha.getParroquia().getId()));
                }*/
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setFechaCreacion(new Date());
                certificado.setObservacion(solvencia);
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                if (archivo) {
                    int temp = historia.size();
                    certificado.setUserCreador(historia.get(temp - 1).getMovimiento().getUserCreador().getId());
                } else {
                    certificado.setUserCreador(session.getUserId());
                }
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setRegistrador(registrador.getId());
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado.setRegCertificadoPropietarioCollection(propietarios);
                certificado = reg.saveCertificadoFicha(certificado);
                if (certificado.getId() != null) {
                    //historia = reg.getMovsByCertificado(certificado.getId());
                    historia = (List<RegCertificadoMovimiento>) certificado.getRegCertificadoMovimientoCollection();
                    propietarios = reg.getPropsByCertificado(certificado.getId());
                    //agregarFichaTramite(liquidacion.getNumTramiteRp(), ficha, liquidacion.getTramite());
                }
                this.llenarParametros();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoMercantil() {
        try {
            if (this.validacionesMercantil()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, Constantes.diasValidezCertificado));
                certificado.setFicha(ficha);
                if (ficha.getDescripcionBien() != null) {
                    certificado.setDescripcionBien(ficha.getDescripcionBien());
                }
                if (ficha.getClaveCatastral() != null) {
                    certificado.setClaveCatastral(ficha.getClaveCatastral());
                }
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setFechaCreacion(new Date());
                certificado.setObservacion(solvencia);
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                certificado.setUserCreador(session.getUserId());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setRegistrador(registrador.getId());
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado.setRegCertificadoPropietarioCollection(propietarios);
                certificado = reg.saveCertificadoFicha(certificado);
                if (certificado.getId() != null) {
                    historia = (List<RegCertificadoMovimiento>) certificado.getRegCertificadoMovimientoCollection();
                }
                this.llenarParametros();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoInforme() {
        try {
            if (!certificado.getNombreSolicitante().isEmpty() && !certificado.getUsoDocumento().isEmpty()
                    && !certificado.getLinderosRegistrales().isEmpty() && !certificado.getBeneficiario().isEmpty()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, Constantes.diasValidezCertificado));
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setFechaCreacion(new Date());
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                certificado.setUserCreador(session.getUserId());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setRegistrador(registrador.getId());
                certificado = (RegCertificado) manager.merge(certificado);
                this.llenarParametrosInforme();
            } else {
                JsfUti.messageWarning(null, "Todos los campos son obligatorios.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaciones() {
        if (ficha.getId() == null) {
            JsfUti.messageWarning(null, "Debe buscar con el numero de ficha registral.", "");
            return false;
        }
        if (propietarios.isEmpty()) {
            JsfUti.messageWarning(null, "La ficha no tiene propietarios enlazados.", "");
            return false;
        }
        if (historia.isEmpty()) {
            JsfUti.messageWarning(null, "No ha seleccionado los movimientos para el certificado.", "");
            return false;
        }
        /*if (solvencia.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el contenido de la solvencia para el certificado.", "");
            return false;
        }*/
        return true;
    }

    public boolean validacionesMercantil() {
        if (ficha.getId() == null) {
            JsfUti.messageWarning(null, "Debe buscar con el numero de ficha registral.", "");
            return false;
        }
        if (historia.isEmpty()) {
            JsfUti.messageWarning(null, "No ha seleccionado los movimientos para el certificado.", "");
            return false;
        }
        return true;
    }

    public void guardarCertificadoRazon() {
        try {
            if (!historia.isEmpty()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, 60));
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setFechaCreacion(new Date());
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                certificado.setUserCreador(session.getUserId());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setRegistrador(registrador.getId());
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado = reg.saveCertificadoRazon(certificado);
                if (certificado.getId() != null) {
                    historia = (List<RegCertificadoMovimiento>) certificado.getRegCertificadoMovimientoCollection();
                }
                this.imprimirCopiaRazon();
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar una inscripcion para generar la copia de razon.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoGravamen() {
        try {
            if (!historia.isEmpty()) {
                certificado.setFechaEmision(fecha);
                certificado.setFechaVencimiento(dateUtil.addDays(fecha, 60));
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setFechaCreacion(new Date());
                certificado.setNumTramite(tt.getTramite().getNumTramite());
                certificado.setTareaTramite(tt);
                certificado.setTaskId(this.getTaskId());
                certificado.setUserCreador(session.getUserId());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setRegistrador(registrador.getId());
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado = reg.saveCertificadoRazon(certificado);
                if (certificado.getId() != null) {
                    historia = (List<RegCertificadoMovimiento>) certificado.getRegCertificadoMovimientoCollection();
                }
                this.llenarParametros();
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar una inscripcion para generar el documento.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirCopiaRazon() {
        try {
            ss.instanciarParametros();
            //this.llenarParametrosFirmaElectronica();
            ss.agregarParametro("ID_CERTIFICADO", certificado.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
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
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarSubTarea() {
        try {
            if (certificado.getId() != null) {
                if (archivo) {
                    tt.setRevisado(Boolean.TRUE);
                    tt.setFechaRevision(new Date());
                    manager.merge(tt);
                    session.setTaskID(this.getTaskId());
                    JsfUti.redirectFaces("/procesos/registro/revisarInscripcionRp.xhtml");
                } else {
                    tt.setRealizado(Boolean.TRUE);
                    tt.setFechaFin(new Date());
                    manager.merge(tt);
                    session.setTaskID(this.getTaskId());
                    JsfUti.redirectFaces("/procesos/registro/realizarProcesoRp.xhtml");
                }
            } else {
                JsfUti.messageWarning(null, "Primero debe guardar el certificado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void borrarListas() {
        try {
            for (RegCertificadoPropietario cp : propietarios) {
                if (cp.getId() != null) {
                    manager.delete(cp);
                }
            }
            if (Utils.isNotEmpty(certificado.getRegCertificadoMovimientoCollection())) {
                for (RegCertificadoMovimiento cm : certificado.getRegCertificadoMovimientoCollection()) {
                    if (cm.getId() != null) {
                        cm = (RegCertificadoMovimiento) manager.merge(cm);
                        manager.delete(cm);
                    }
                }
            } else {
                for (RegCertificadoMovimiento cm : historia) {
                    if (cm.getId() != null) {
                        cm = (RegCertificadoMovimiento) manager.merge(cm);
                        manager.delete(cm);
                    }
                }
            }
            propietarios = new ArrayList<>();
            historia = new ArrayList<>();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, "BorrarLista", e);
        }
    }

    public void guardarMigracion() {
        try {
            this.agregarHistoria();
            JsfUti.executeJS("PF('dlgAntecedente').hide();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validarMigracion() {
        if (movimiento.getNumInscripcion() == null) {
            return false;
        }
        if (movimiento.getNumRepertorio() == null) {
            return false;
        }
        if (movimiento.getFechaInscripcion() == null) {
            return false;
        }
        if (movimiento.getFechaOto() == null) {
            return false;
        }
        if (movimiento.getNumTomo() == null || movimiento.getNumTomo().isEmpty()) {
            return false;
        }
        if (movimiento.getObservacion() == null || movimiento.getObservacion().isEmpty()) {
            return false;
        }
        if (movimiento.getLibro() == null) {
            return false;
        }
        if (movimiento.getActo() == null) {
            return false;
        }
        if (movimiento.getEnteJudicial() == null) {
            return false;
        }
        if (movimiento.getDomicilio() == null) {
            return false;
        }
        return true;
    }

    public void selectInterviniente(SelectEvent event) {
        RegMovimientoCliente mc = new RegMovimientoCliente();
        mc.setEnteInterv((RegEnteInterviniente) event.getObject());
        movimiento.getRegMovimientoClienteCollection().add(mc);
        JsfUti.update("formAntecedente:tVdetalle:dtInterviniente");
    }

    public void agregarInterviniente() {
        RegMovimientoCliente mc = new RegMovimientoCliente();
        mc.setEnteInterv(interviniente);
        movimiento.getRegMovimientoClienteCollection().add(mc);
        JsfUti.update("formAntecedente:tVdetalle:dtInterviniente");
        JsfUti.executeJS("PF('dlgCrearInterviniente').hide();");
    }

    public void showDlgNewPersona() {
        interviniente = new RegEnteInterviniente();
        JsfUti.update("formCreaInterv");
        JsfUti.executeJS("PF('dlgCrearInterviniente').show();");
    }

    public void buscarIntervinienteDianardap() {
        try {
            if (interviniente.getCedRuc() != null && !interviniente.getCedRuc().isEmpty()) {
                PubPersona persona = reg.buscarDinardap(interviniente.getCedRuc());
                if (persona != null) {
                    interviniente.setNombre(persona.getApellidos());
                } else {
                    JsfUti.messageWarning(null, "No se encontro datos en la Dinardap.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar numero de identificacion.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardarInterviniente() {
        try {
            if (!interviniente.getNombre().isEmpty() && !interviniente.getCedRuc().isEmpty()) {
                if (!this.validarInterviniente()) {
                    interviniente.setUsuarioIngreso(session.getName_user());
                    interviniente.setFechaIngreso(new Date());
                    interviniente = (RegEnteInterviniente) manager.persist(interviniente);
                    this.agregarInterviniente();
                } else {
                    JsfUti.messageWarning(null, "Ya se existe el interviniente con el mismo nombre y la misma cedula.", "");
                }
            } else if (!interviniente.getNombre().isEmpty() && interviniente.getCedRuc().isEmpty()) {
                interviniente.setUsuarioIngreso(session.getName_user());
                interviniente.setFechaIngreso(new Date());
                interviniente = reg.saveInterviniente(interviniente);
                this.agregarInterviniente();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private Boolean validarInterviniente() {
        map = new HashMap();
        map.put("cedula", interviniente.getCedRuc());
        map.put("nombre", interviniente.getNombre());
        RegEnteInterviniente rei = (RegEnteInterviniente) manager.findObjectByParameter(Querys.getRegIntervByCedRucByNombre, map);
        return rei != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public void selectObjectPapel(SelectEvent event) {
        cliente.setPapel((RegPapel) event.getObject());
        movimiento.setRegMovimientoClienteCollection(rmcs);
    }

    public void showDlgPapel(String urlFacelet, int indice) {
        rmcs = (List<RegMovimientoCliente>) movimiento.getRegMovimientoClienteCollection();
        cliente = rmcs.get(indice);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "60%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public void selectObjectActo(SelectEvent event) {
        movimiento.setActo((RegActo) event.getObject());
    }

    public void selectObjectJudicial(SelectEvent event) {
        movimiento.setEnteJudicial((RegEnteJudiciales) event.getObject());
    }

    public List<RegTipoFicha> getTiposFichas() {
        return manager.findAllEntCopy(Querys.getRegTipoFicha);
    }

    public List<RegLibro> getLibros() {
        return manager.findAllEntCopy(Querys.getRegLibroListOrd);
    }

    public List<RegDomicilio> getDomicilios() {
        return manager.findAllEntCopy(Querys.getRegDomicilioList);
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

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public Integer getRender() {
        return render;
    }

    public void setRender(Integer render) {
        this.render = render;
    }

    public String getSolvencia() {
        return solvencia;
    }

    public void setSolvencia(String solvencia) {
        this.solvencia = solvencia;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public RegpTareasTramite getTt() {
        return tt;
    }

    public void setTt(RegpTareasTramite tt) {
        this.tt = tt;
    }

    public RegEnteInterviniente getPropietario() {
        return propietario;
    }

    public void setPropietario(RegEnteInterviniente propietario) {
        this.propietario = propietario;
    }

    public List<RegFichaPropietarios> getListProp() {
        return listProp;
    }

    public void setListProp(List<RegFichaPropietarios> listProp) {
        this.listProp = listProp;
    }

    public RegCertificado getCertificado() {
        return certificado;
    }

    public void setCertificado(RegCertificado certificado) {
        this.certificado = certificado;
    }

    public List<RegCertificadoMovimiento> getHistoria() {
        return historia;
    }

    public void setHistoria(List<RegCertificadoMovimiento> historia) {
        this.historia = historia;
    }

    public List<RegMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public List<RegMovimiento> getTemps() {
        return temps;
    }

    public void setTemps(List<RegMovimiento> temps) {
        this.temps = temps;
    }

    public ContenidoReportes getContenido() {
        return contenido;
    }

    public void setContenido(ContenidoReportes contenido) {
        this.contenido = contenido;
    }

    public List<ContenidoReportes> getContenidos() {
        return contenidos;
    }

    public void setContenidos(List<ContenidoReportes> contenidos) {
        this.contenidos = contenidos;
    }

    public List<RegCertificadoPropietario> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegCertificadoPropietario> propietarios) {
        this.propietarios = propietarios;
    }

    public RegRegistrador getRegistrador() {
        return registrador;
    }

    public void setRegistrador(RegRegistrador registrador) {
        this.registrador = registrador;
    }

    public boolean isRequiereCambio() {
        return requiereCambio;
    }

    public void setRequiereCambio(boolean requiereCambio) {
        this.requiereCambio = requiereCambio;
    }

    public void limpiarData() {
        if (!requiereCambio) {
            certificado.setDescripcionBien(null);
        } else {
            certificado.setDescripcionBien("Pero se encuentra inscrito unos Derechos hereditarios a nombre de ");
        }
    }

    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public RegEnteInterviniente getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(RegEnteInterviniente interviniente) {
        this.interviniente = interviniente;
    }

    public RegMovimientoCliente getCliente() {
        return cliente;
    }

    public void setCliente(RegMovimientoCliente cliente) {
        this.cliente = cliente;
    }

}
