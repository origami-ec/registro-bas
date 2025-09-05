/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.solicitudes;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudActo;
import com.origami.sgr.entities.PubSolicitudRequisito;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.lazymodels.LazyModel;
//import com.origami.sgr.lazymodels.LazyModelWS;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
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
public class SolicitudVentanillaPublica implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(SolicitudVentanillaPublica.class.getName());

    @Inject
    private UserSession us;
    
    @Inject
    private Entitymanager manager;
    
    @Inject
    protected ServletSession ss;

    protected RegCertificado certificado;
    private LazyModel<PubSolicitud> pubSolicitudVentanillas;
    private PubSolicitud solicitud;
    private List<PubSolicitudActo> actos;
    private List<PubSolicitudRequisito> requisitos;
    private List<PubPersona> propietarios;
    private List<RegCertificado> certificados;
    private AclUser user;
    private RegRegistrador registrador;
    private RegpLiquidacion liquidacion;
    private List<RegMovimiento> movimientos;
    protected Boolean imprimirBorradorCertificado = false;

    @PostConstruct
    protected void iniView() {
        pubSolicitudVentanillas = new LazyModel<>(PubSolicitud.class, "id", "DESC");
    }

    public void infoSolicitud(PubSolicitud solicitud) {
        this.solicitud = solicitud;
        if (solicitud.getTipoSolicitud() != 0) {
            propietarios = new ArrayList();
            /*     if (solicitud.getPropTipoPersona() != null) {
                List<String> props = Arrays.asList(solicitud.getPropTipoPersona().split(";"));

                for (String s : props) {
                    PubPersona p = new PubPersona();
                    if (s.contains(":::")) {
                        List<String> prop = Arrays.asList(s.split(":::"));
                        String max = Collections.max(prop, Comparator.comparing(String::length));
                        try {
                            p.setNombres(max);
                            prop.remove(max);
                            String ced = "";
                            for (String t : prop) {
                                if (NumberUtils.isParsable(t)) {
                                    ced = t;
                                    break;
                                }
                            }
                            if (!ced.isEmpty()) {
                                prop.remove(ced);
                            }
                            if (Utils.isNotEmpty(prop)) {
                                for (String t : prop) {
                                    if (!ced.isEmpty()) {
                                        p.setCedRuc(ced);
                                    }
                                    p.setTipoDocumento(t + "\n" + p.getTipoDocumento());
                                }
                            }

                        } catch (Exception e) {
                            p.setNombres(s);
                        }
                    } else {
                        p.setNombres(s);
                    }
                    propietarios.add(p);
                }
                //propietarios
            }*/
            Map map = new HashMap();
            map.put("numTramiteRp", solicitud.getTramite().getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            certificados = new ArrayList<>();
            if (solicitud.getNumeroFicha() != null) {
                certificados = manager.findAll(Querys.getCertificadosXficha, new String[]{"numFicha"}, new Object[]{Long.valueOf(solicitud.getNumeroFicha())});
                if (certificados != null) {
                    certificados.sort(Comparator.comparing(RegCertificado::getId).reversed());
                }
            }
            movimientos = manager.findAll(Querys.getMovimientoByAnioInscRep, new String[]{"libro", "inscripcion", "anio"}, new Object[]{11L, solicitud.getNumInscripcion(), solicitud.getAnioInscripcion()});
            if (movimientos == null) {
                movimientos = new ArrayList<>();
            }
            JsfUti.executeJS("PF('dlgSolicitud').show();");
            JsfUti.update("frmFormulario");

        } else {
            Map map = new HashMap();
            map.put("solicitud.id", solicitud.getId());
            actos = manager.findObjectByParameterList(PubSolicitudActo.class, map);

            map = new HashMap();
            map.put("solicitud.id", solicitud.getId());
            map.put("tipo", SisVars.TIPO_REQUISITO);
            requisitos = manager.findObjectByParameterOrderList(PubSolicitudRequisito.class, map, new String[]{"fecha"}, Boolean.FALSE);
            JsfUti.executeJS("PF('dlgSolicitudInscripcion').show();");
            JsfUti.update("frmSolicitudInscripcion");
        }

    }

    public void imprimirFichaRegistral() {
        try {
            if (solicitud.getNumeroFicha() != null) {
                Map map = new HashMap();
                map.put("numFicha", solicitud.getNumeroFicha().longValue());
                RegFicha ficha = (RegFicha) manager.findObjectByParameter(RegFicha.class,
                        map);
                if (ficha.getEstado().getValor().equalsIgnoreCase("INACTIVO")) {
                    JsfUti.messageError(null, "No se imprime Ficha Registral, estado de Ficha: INACTIVA.", "");
                } else {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(true);
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("FichaRegistral");
                    ss.agregarParametro("ID_FICHA", ficha.getId());
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                    ss.agregarParametro("USER_NAME", us.getName_user());
                    ss.setEncuadernacion(Boolean.TRUE);
                    JsfUti.redirectNewTab("/sigeri-playas/Documento");
                }
            } else {
                JsfUti.messageError(null, "Solicitud no tiene una ficha registral asociada", "");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getActo(Integer tipoSolicitud) {
        RegActo acto = (RegActo) manager.find(Querys.getRegCatPapelByActo, new String[]{"idacto"}, new Object[]{tipoSolicitud.longValue()});
        if (acto != null) {
            return acto.getNombre();
        } else {
            return "";
        }
    }

    public String getMotivo(Integer idUsoDocumento) {
        CtlgItem item = manager.find(CtlgItem.class,
                idUsoDocumento.longValue());
        if (item != null) {
            return item.getValor();
        } else {
            return "";
        }
    }

    public Boolean tieneDocumento() {
        if (solicitud != null) {
            if (solicitud.getOidDocument() != null || solicitud.getOidDocument2() != null || solicitud.getOidDocument3() != null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    public void documentoSolicitud() {
        if (solicitud.getOidDocument() != null || solicitud.getOidDocument2() != null || solicitud.getOidDocument3() != null) {
            if (solicitud.getPayframeUrl().equals("WEB_PAGE")) {
                formularioWeb();
            } else {
                formularioMovil();
            }
        } else {
            JsfUti.messageWarning(null, "Solicitud no posee documentos asociados", "");
        }
    }

    public void formularioMovil() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("FormularioOnline");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_TRAMITE", solicitud.getTramite().getId());
            //ss.agregarParametro("FOTO1", this.getImage(Querys.getOidSolicitud));
            //ss.agregarParametro("FOTO2", this.getImage(Querys.getOid2Solicitud));
            // ss.agregarParametro("FOTO3", this.getImage(Querys.getOid3Solicitud));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_comprobante.jpg"));
            JsfUti.redirectNewTab("/sigeri-playas/Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void formularioWeb() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{solicitud.getTramite().getId()});
            if (oid != null) {
                JsfUti.redirectNewTab("/sigeri-playas/OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /*public InputStream getImage(String sql) {
        BigInteger oid = (BigInteger) manager.getNativeQuery(sql, new Object[]{solicitud.getTramite().getId()});
        if (oid != null) {
            return ou.streamFile(oid.longValue(), 3);
        }
        return null;
    }*/
    public void downLoadDocument(Long oid) {
        try {
            if (oid != null) {
                JsfUti.redirectNewTab("/sigeri-playas/OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificadoBorrador(RegCertificado ce) {
        try {
            imprimirBorradorCertificado = true;
            this.llenarParametros(ce);
            switch (ce.getTipoDocumento()) {
                case "C01": //NO POSEER BIENES
                    ss.setNombreReporte("CertificadoNoBienes");
                    break;
                /*case "C02": //CERTIFICADO DE SOLVENCIA
                    ss.setNombreReporte("CertificadoSolvencia");
                    break;*/
                case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                    ss.setNombreReporte("CertificadoHistoriaDominio");
                    break;
                /*case "C04": //CERTIFICADO DE FICHA PERSONAL (MERCANTIL)
                    ss.setNombreReporte("CertificadoFichaPersonal");
                    break;*/
                case "C05": //CERTIFICADO MERCANTIL 
                    ss.setNombreReporte("CertificadoMercantil");
                    break;
                case "C06": //CERTIFICADO DE RAZON DE INSCRIPCION
                    this.llenarParametrosRazon(ce);
                    break;
                case "C07": //CERTIFICADO GENERAL 
                    ss.setNombreReporte("CertificadoGeneral");
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
            user = manager.find(AclUser.class, us.getUserId());
            Map map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
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
            ss.setEncuadernacion(Boolean.TRUE);
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
            ss.setEncuadernacion(true);
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

    public void visualizaDocuments(RegMovimiento movimiento) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropImages.xhtml?id="
                    + movimiento.getId() + "&numRepertorio=" + movimiento.getNumRepertorio() + "&numInscripcion="
                    + movimiento.getNumInscripcion() + "&fechaIns=" + movimiento.getFechaInscripcion().getTime());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegCertificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<RegCertificado> certificados) {
        this.certificados = certificados;
    }

    public LazyModel<PubSolicitud> getPubSolicitudVentanillas() {
        return pubSolicitudVentanillas;
    }

    public void setPubSolicitudVentanillas(LazyModel<PubSolicitud> pubSolicitudVentanillas) {
        this.pubSolicitudVentanillas = pubSolicitudVentanillas;
    }

    public PubSolicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(PubSolicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<PubSolicitudActo> getActos() {
        return actos;
    }

    public void setActos(List<PubSolicitudActo> actos) {
        this.actos = actos;
    }

    public List<PubSolicitudRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<PubSolicitudRequisito> requisitos) {
        this.requisitos = requisitos;
    }

    public List<RegMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

}
