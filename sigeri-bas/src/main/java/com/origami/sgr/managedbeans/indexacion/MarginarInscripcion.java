/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.indexacion;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.ConsultaMovimientoModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
public class MarginarInscripcion implements Serializable {

    private static final Logger LOG = Logger.getLogger(MarginarInscripcion.class.getName());

    @Inject
    protected RegistroPropiedadServices reg;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private Entitymanager em;
    @Inject
    private BitacoraServices bs;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;
    @Inject
    private FirmaDigitalLocal fd;
    @Inject
    private AsynchronousService as;

    protected RegMovimiento movimiento;
    protected ConsultaMovimientoModel modelo = new ConsultaMovimientoModel();
    protected RegMovimientosLazy movimientosLazy;
    protected Date fechaIngreso = new Date();
    protected Date fechaActa = new Date();
    protected Date fechaDesde = new Date();
    protected Date fechaHasta = new Date();
    protected Calendar cal = Calendar.getInstance();
    protected String urlDownload = "";
    protected Integer anio, paginas = 0;
    protected BigInteger periodo;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected RegMovimientoMarginacion marg = new RegMovimientoMarginacion();
    protected Integer tomo = 0, folio = 0, pagina = 1;
    protected Map map;
    protected RegRegistrador registrador;
    protected AclUser user;
    protected Boolean administrador = false;
    protected Boolean habilitarEdicion = false;
    protected Boolean habilitarFirma = false;

    @PostConstruct
    protected void iniView() {
        try {
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) em.findObjectByParameter(RegRegistrador.class, map);
            periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            movimientosLazy = new RegMovimientosLazy();
            movimientosLazy.addSorted("fechaInscripcion", "DESC");
            this.validaRoles();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void validaRoles() {
        administrador = us.getRoles().contains(1L);     //ROL ADMINISTRADOR
        habilitarEdicion = us.getRoles().contains(6L);  //JEFE_INSCRIPCIONES
        habilitarFirma = us.getRoles().contains(25L);   //FIRMA_DOCUMENTOS
    }

    public void showDlgRepertorios() {
        JsfUti.update("frmConsultarRepertorio");
        JsfUti.executeJS("PF('dlgRepertorio').show();");
    }

    public void showDlgInscripciones() {
        JsfUti.update("frmConsultarInscripciones");
        JsfUti.executeJS("PF('dlgInscripciones').show();");
    }

    public void showDlgDocumentos(RegMovimiento mov) {
        movimiento = mov;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void redirectFacelet(String cadena) {
        JsfUti.redirectFaces(cadena);
    }

    public void imprimirInscripciones(Boolean encuadernacion, Integer margen) {
        try {
            if (fechaIngreso != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setEncuadernacion(encuadernacion);
                ss.setMargen(margen);
                ss.setNombreReporte("reporteInscripcionesIngresadas");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("FECHA_INSCRIPCION", fechaIngreso);
                ss.agregarParametro("USUARIO", us.getUserId());
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirActaInicio(Boolean encuadernacion, Integer margen) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(encuadernacion);
            ss.setMargen(margen);
            ss.setNombreReporte("Repertorio_apertura");
            ss.setNombreSubCarpeta("registro");
            sdf = new SimpleDateFormat("EEEEE dd 'de' MMMMM 'de' yyyy");
            ss.agregarParametro("FECHA_STRING", sdf.format(fechaActa));
            ss.agregarParametro("FECHA", fechaActa);
            ss.agregarParametro("TOMO", tomo);
            ss.agregarParametro("FOLIO", folio);
            ss.agregarParametro("PAGINA", pagina);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            cal.setTime(fechaIngreso);
            ss.agregarParametro("ANIO", cal.get(Calendar.YEAR));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editarInscripcion(RegMovimiento mov) {
        try {
            if (mov.getEditable()) {
                if (mov.getTramite() == null) {
                    ss.instanciarParametros();
                    ss.agregarParametro("idMov", mov.getId());
                    JsfUti.redirectFaces("/procesos/manage/inscripcionEdicion.xhtml");
                } else if (mov.getTramite().getRealizado()) {
                    ss.instanciarParametros();
                    ss.agregarParametro("idMov", mov.getId());
                    JsfUti.redirectFaces("/procesos/manage/inscripcionEdicion.xhtml");
                } else {
                    JsfUti.messageWarning(null, "Debe de terminar la tarea para poder editar este movimiento.", "");
                }
            } else {
                JsfUti.messageWarning(null, "NO se puede editar.", "Debe pedir permisos para editar esta inscripción.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void imprimirInscripcion(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("P_MOVIMIENTO", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
            ss.setNombreReporte("ActaInscripcion");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            //ss.setEncuadernacion(Boolean.FALSE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            //reg.generacionDocumento(mov.getId(), "ACTA DE INSCRIPCION");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirRazon(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
            ss.setNombreReporte("RazonInscripcion");
            ss.setNombreSubCarpeta("registro");
            ss.setTieneDatasource(true);
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirCopiaRazon(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRazonGrupal(RegMovimiento mov) {
        Calendar cl = Calendar.getInstance();
        if (mov.getFechaRepertorio() != null) {
            cl.setTime(mov.getFechaRepertorio());
        } else {
            cl.setTime(mov.getFechaInscripcion());
        }
        Integer year = cl.get(Calendar.YEAR);
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            ss.instanciarParametros();
            ss.agregarParametro("INSCRIPTOR", mov.getUserCreador().getUsuario());
            ss.agregarParametro("REPERTORIO", mov.getNumRepertorio());
            ss.agregarParametro("INDEXADOR", mov.getResponseCatastro());
            ss.agregarParametro("ANIO", year.toString());
            ss.agregarParametro("FECHA_REP", mov.getFechaRepertorio());
            ss.agregarParametro("FECHA_REPERTORIO", sdf.format(mov.getFechaRepertorio()));
            if (mov.getTramite() != null) {
                ss.agregarParametro("TRAMITE", mov.getTramite().getTramite().getNumTramite());
                ss.agregarParametro("COMPROBANTE", mov.getTramite().getDetalle().getLiquidacion().getCodigoComprobante());
            }
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("COD_VERIFICACION", mov.getCodVerificacion());
            ss.agregarParametro("PROPIEDAD", mov.getLibro().getPropiedad());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
            ss.setNombreReporte("RazonInscripcion_v1");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            modelo = reg.getConsultaMovimiento(mov.getId());
            if (modelo != null) {
                cal.setTime(mov.getFechaInscripcion());
                anio = cal.get(Calendar.YEAR);
                /*urlDownload = "/pages/rpp/descarga.jsf?nombreLibro=" + movimiento.getLibro().getNombreCarpeta()
                 + "&anioInscripcion=" + anio + "&numeroTomo=" + movimiento.getNumTomo() + "&numeroInscripcion="
                 + movimiento.getNumInscripcion() + "&folioInicial=" + movimiento.getFolioInicio()
                 + "&folioFinal=" + movimiento.getFolioFin();*/
                JsfUti.update("formMovRegSelec");
                JsfUti.executeJS("PF('dlgMovRegSelec').show();");
            } else {
                JsfUti.messageError(null, "No se pudo hacer la consulta.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacora() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", movimiento.getId());
            ss.agregarParametro("numFicha", null);
            ss.agregarParametro("titulo", Messages.bitacoraMovimiento);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann() {
        try {
            /*if (movimiento.getIdTransaccion() != null && movimiento.getIdPadre() != null) {
                ss.instanciarParametros();
                ss.agregarParametro("id_transaccion", movimiento.getIdTransaccion());
                ss.agregarParametro("id_padre", movimiento.getIdPadre());
                JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/visorDocuments.xhtml");
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropImages.xhtml?id="
                        + movimiento.getId() + "&numRepertorio=" + movimiento.getNumRepertorio() + "&numInscripcion="
                        + movimiento.getNumInscripcion() + "&fechaIns=" + movimiento.getFechaInscripcion().getTime());
            }*/
            if (movimiento.getValorUuid() != null && !movimiento.getValorUuid().isEmpty()) {
                JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion="
                        + Constantes.indexacionInscripciones + "&tramite=" + movimiento.getValorUuid());
            } else {
                JsfUti.messageWarning(null, "No se puede visualizar el documento.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNewMarginacion() {
        marg = new RegMovimientoMarginacion();
        JsfUti.update("formMarginacion");
        JsfUti.executeJS("PF('dlgMarginacion').show();");
    }

    public void agregarMarginacion() {
        try {
            if (marg.getObservacion() != null && movimiento.getId() != null) {
                marg.setFechaIngreso(new Date());
                marg.setUserIngreso(us.getUserId());
                marg.setMovimiento(movimiento);
                em.persist(marg);
                bs.registrarMovMarginacion(movimiento, marg.getObservacion(), periodo);
                modelo.setMarginaciones(reg.getRegMovMargByIdMov(movimiento.getId()));
                JsfUti.update("formMovRegSelec:tvMovimiento:dtMarginacion");
                JsfUti.executeJS("PF('dlgMarginacion').hide();");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar contenido de texto a la marginacion.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void habilitarEdicionMov() {
        try {
            if (habilitarEdicion) {
                movimiento.setEditable(true);
                em.update(movimiento);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
                JsfUti.messageInfo(null, "Movimiento habilitado para edicion.", "");
            } else {
                JsfUti.messageWarning(null, "Usuario no permitido.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarInscripcion(RegMovimiento mo, boolean flag) {
        try {
            File file;
            if (flag) {
                file = fd.firmarActaInscripcion(mo);
                JsfUti.messageInfo(null, "Documento firmado con exito.", "");
            } else {
                System.out.println("// nro tramite: " + mo.getNumeroTramite());
                file = as.generarFirmaInscripciones(mo.getNumeroTramite());
                //fd.firmarInscripcionFile(mo);
            }
            if (file != null) {
                JsfUti.messageInfo(null, "Documento firmado con exito.", "");
            } else {
                JsfUti.messageWarning(null, "NO se pudo generar movimiento.", "");
            }
        } catch (java.io.IOException e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, e.getMessage());
        }
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
    }
    
    public void showDlgMarginaciones(RegMovimiento mov) {
        try {
            movimiento = mov;
            modelo = reg.getConsultaMovimiento(mov.getId());
            if (modelo != null) {
                JsfUti.update("formVerMarginaciones");
                JsfUti.executeJS("PF('dlgVerMarginaciones').show();");
            } else {
                JsfUti.messageError(null, "No se pudo hacer la consulta.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public RegMovimientosLazy getMovimientosLazy() {
        return movimientosLazy;
    }

    public void setMovimientosLazy(RegMovimientosLazy movimientosLazy) {
        this.movimientosLazy = movimientosLazy;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public ConsultaMovimientoModel getModelo() {
        return modelo;
    }

    public void setModelo(ConsultaMovimientoModel modelo) {
        this.modelo = modelo;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public RegMovimientoMarginacion getMarg() {
        return marg;
    }

    public void setMarg(RegMovimientoMarginacion marg) {
        this.marg = marg;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public Integer getTomo() {
        return tomo;
    }

    public void setTomo(Integer tomo) {
        this.tomo = tomo;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Date getFechaActa() {
        return fechaActa;
    }

    public void setFechaActa(Date fechaActa) {
        this.fechaActa = fechaActa;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Boolean getHabilitarEdicion() {
        return habilitarEdicion;
    }

    public void setHabilitarEdicion(Boolean habilitarEdicion) {
        this.habilitarEdicion = habilitarEdicion;
    }

    public Boolean getHabilitarFirma() {
        return habilitarFirma;
    }

    public void setHabilitarFirma(Boolean habilitarFirma) {
        this.habilitarFirma = habilitarFirma;
    }

}
