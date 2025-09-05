/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.GeneracionDocs;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.lazymodels.RegCertificadosLazy;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.CertificadoModel;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * @author Anyelo
 */
@Named
@ViewScoped
public class CertificadosRp implements Serializable {

    private static final Logger LOG = Logger.getLogger(CertificadosRp.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;

    @Inject
    private Entitymanager em;

    @Inject
    protected RegistroPropiedadServices reg;

    @Inject
    protected BitacoraServices bs;

    @Inject
    private FirmaDigitalLocal fd;

    protected Map map;
    protected RegpLiquidacion liquidacion;
    protected RegRegistrador registrador;
    protected RegFicha fichaSel = new RegFicha();
    protected List<RegMovimiento> movimientosFicha = new ArrayList<>();
    protected Boolean administrador = false;
    protected Boolean habilitarEdicion = false;
    protected Boolean habilitarFirma = false;
    protected Boolean imprimirBorradorCertificado = false;
    protected AclUser user;
    protected RegCertificadosLazy certificados;
    protected RegCertificado cert = new RegCertificado();
    protected List<CertificadoModel> lista = new ArrayList<>();
    protected String cedula2, nombres2, apellidos2;
    protected RegCertificadosLazy certificados2;
    protected String cedula, nombres;
    private Date fechaEmision;
    private AclUser usuario;
    protected BigInteger periodo;
    protected String observacion = "";
    protected CtlgItem tipo;

    @PostConstruct
    protected void iniView() {
        try {
            periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            certificados = new RegCertificadosLazy();
            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            user = em.find(AclUser.class, us.getUserId());
            fechaEmision = new Date();
            usuario = new AclUser();
            this.validaRoles();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        administrador = us.getRoles().contains(1L);     //ROL ADMINISTRADOR
        habilitarEdicion = us.getRoles().contains(5L);  //JEFE_CERTIFICACION
        habilitarFirma = us.getRoles().contains(25L);   //FIRMA_DOCUMENTOS
    }

    public void generarCertificado2(Long id) {
        try {
            RegCertificado c = em.find(RegCertificado.class, id);
            this.generarCertificado(c);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificado(RegCertificado ce) {
        if (ce.getTareaTramite().getRealizado() && ce.getEditable()) {
            imprimirBorradorCertificado = false;
            ce.setEditable(Boolean.FALSE);
            em.persist(ce);
            try {
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
            } catch (Exception e) {
                JsfUti.messageError(null, Messages.error, "");
                LOG.log(Level.SEVERE, null, e);
            }
        } else {
            JsfUti.messageWarning(null, "Certificado Nro. " + ce.getNumCertificado().toString() + " no se puede Imprimir, debe solicitar autorización", "");
        }
    }

    public void generarCertificadoBorrador(RegCertificado ce) {
        try {
            imprimirBorradorCertificado = true;
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
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            user = em.find(AclUser.class, us.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
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
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            if (imprimirBorradorCertificado) {
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            } else {
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            }
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
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            if (imprimirBorradorCertificado) {
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            } else {
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            }
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
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void llenarParametrosInforme(RegCertificado ce) {
        try {
            ss.instanciarParametros();
            ss.setFirmarCertificado(Boolean.FALSE);
            ss.setIdCertificado(null);
            //ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("NOMBRE", ce.getBeneficiario());
            ss.agregarParametro("BUSQUEDA", ce.getLinderosRegistrales());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            if (imprimirBorradorCertificado) {
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            } else {
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            }
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CertificadoInformeBienes");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void editCertificado(RegCertificado c) {
        if (c.getTareaTramite().getRealizado() && c.getEditable()) {
            /*ss.borrarDatos();
            ss.instanciarParametros();
            ss.agregarParametro("tipo", tipo.getCodename());
            ss.agregarParametro("tarea", c.getTareaTramite().getId());
            ss.agregarParametro("tramite", c.getNumTramite());
            JsfUti.redirectFaces("/procesos/manage/editarCertificadoRp.xhtml");*/

            cert = c;
            JsfUti.update("formCertf");
            JsfUti.executeJS("PF('dlgCertificado').show();");
        } else {
            JsfUti.messageWarning(null, "Certificado Nro " + c.getNumCertificado().toString() + " no se puede Editar, debe solicitar autorización", "");
        }
    }

    public void showDlgEditar(RegCertificado c) {
        this.cert = c;
        JsfUti.update("formHabilitar");
        JsfUti.executeJS("PF('dlgHabilitarEdicion').show();");
    }

    public void aceptarEdicion() {
        try {
            //if (cert != null && cert.getId() != null && cert.getTareaTramite().getRealizado()) {
            if (cert != null && cert.getId() != null && cert.getTareaTramite().getRevisado()) {
                cert.setEditable(Boolean.TRUE);
                em.persist(cert);
                this.guardarObservacion();
                JsfUti.messageInfo(null, "Certificado " + cert.getNumCertificado().toString() + " Listo para Editar", "");
                JsfUti.update("formHabilitar");
                JsfUti.executeJS("PF('dlgHabilitarEdicion').hide();");
            } else {
                JsfUti.messageError(null, "Certificado " + cert.getNumCertificado().toString() + ", el trámite debe finalizar para autorizar edicion.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardarObservacion() {
        try {
            bs.registrarEdicionCertificado(cert, ActividadesTransaccionales.APROBAR_EDICION_CERTIFICADO, periodo);
            if (observacion == null || observacion.isEmpty()) {
                reg.guardarObservaciones(cert.getTareaTramite().getTramite(), us.getName_user(), cert.getClaseCertificado(), "HABILITAR EDICION CERTIFICADO NRO " + cert.getNumCertificado());
            } else {
                reg.guardarObservaciones(cert.getTareaTramite().getTramite(), us.getName_user(), observacion, "HABILITAR EDICION CERTIFICADO NRO " + cert.getNumCertificado());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generacionDocumento(Long ficha, Long certificado, String mensaje) {
        try {
            GeneracionDocs gen = new GeneracionDocs();
            gen.setAclLogin(us.getAclLogin().getId());
            gen.setFechaGeneracion(new Date());
            gen.setUsuario(us.getUserId());
            gen.setFicha(ficha);
            gen.setCertificado(certificado);
            gen.setObservacion(mensaje);
            em.persist(gen);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarPropietarios() {
        try {
            if (cedula != null || nombres != null) {
                certificados2 = new RegCertificadosLazy(true, cedula, nombres);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarCertificados() {
        try {
            lista = reg.getCertificadosByEnte(cedula2, nombres2, apellidos2);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarDocumentoCertificado(RegCertificado ce) {
        try {
            //if (ce.getCertificadoImpreso() && ce.getDocumento() != null) {
            if (fd.firmarCertificado(ce)) {
                JsfUti.messageInfo(null, "Documento del certificado firmado con exito.", "");
            } else {
                JsfUti.messageWarning(null, "Hubo un error al momento de firmar el certificado.", "");
            }
            /* } else {
                JsfUti.messageWarning(null, "Se debe completar la tarea de firma del certificado.", "");
            }*/
        } catch (java.io.IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void desbloquearCertificado(RegCertificado ce) {
        try {
            if (ce.getId() != null) {
                ce.setPrintOnline(0);
                em.update(ce);
                JsfUti.messageInfo(null, "Certificado desbloqueado con éxito.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporteCertificados() {
        try {
            if (fechaEmision == null) {
                JsfUti.messageInfo(null, "Debe ingresar la fecha", "");
                return;
            }
            ss.instanciarParametros();
            //ss.setEncuadernacion(false);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.setNombreReporte("certificadosRealizadas");
            ss.agregarParametro("FECHA_EMISION", fechaEmision);
            if (usuario != null && usuario.getId() != null) {
                ss.agregarParametro("USUARIO", usuario);
            }
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/loja.jpg"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reditectCertificado() {
        try {
            if (tipo == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el modelo de certificacion.", "");
                return;
            }
            if (cert.getTareaTramite().getRealizado() && cert.getEditable()) {
                ss.borrarDatos();
                ss.instanciarParametros();
                ss.agregarParametro("tipo", tipo.getCodename());
                ss.agregarParametro("tarea", cert.getTareaTramite().getId());
                ss.agregarParametro("tramite", cert.getNumTramite());
                JsfUti.redirectFaces("/procesos/manage/editarCertificadoRp.xhtml");
            } else {
                JsfUti.messageWarning(null, "Certificado Nro " + cert.getNumCertificado().toString() + " no se puede Editar, debe solicitar autorización", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public BigInteger getEstadoPagoByTramite(Long tramite) {
        return (BigInteger) em.getNativeQuery(Querys.getEstadoPagoLiquidacion, new Object[]{tramite});
    }

    public List<CtlgItem> getTiposCertificados() {
        return em.findAllEntCopy(Querys.getCtlgItemCertificados);
    }

    public RegCertificadosLazy getCertificados() {
        return certificados;
    }

    public void setCertificados(RegCertificadosLazy certificados) {
        this.certificados = certificados;
    }

    public RegCertificado getCert() {
        return cert;
    }

    public void setCert(RegCertificado cert) {
        this.cert = cert;
    }

    public RegCertificadosLazy getCertificados2() {
        return certificados2;
    }

    public void setCertificados2(RegCertificadosLazy certificados2) {
        this.certificados2 = certificados2;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public List<CertificadoModel> getLista() {
        return lista;
    }

    public void setLista(List<CertificadoModel> lista) {
        this.lista = lista;
    }

    public String getCedula2() {
        return cedula2;
    }

    public void setCedula2(String cedula2) {
        this.cedula2 = cedula2;
    }

    public String getNombres2() {
        return nombres2;
    }

    public void setNombres2(String nombres2) {
        this.nombres2 = nombres2;
    }

    public String getApellidos2() {
        return apellidos2;
    }

    public void setApellidos2(String apellidos2) {
        this.apellidos2 = apellidos2;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
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

    public CtlgItem getTipo() {
        return tipo;
    }

    public void setTipo(CtlgItem tipo) {
        this.tipo = tipo;
    }

}
