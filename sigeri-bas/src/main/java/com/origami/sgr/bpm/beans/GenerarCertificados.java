/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.GeneracionDocs;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class GenerarCertificados implements Serializable {

    private static final Logger LOG = Logger.getLogger(GenerarCertificados.class.getName());
    private static final long serialVersionUID = 1L;

    @EJB(beanName = "registroPropiedad")
    protected RegistroPropiedadServices reg;

    @Inject
    protected FirmaDigitalLocal fd;

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;

    @Inject
    private ServletSession ss;

    @Inject
    private OmegaUploader ou;

    @Inject
    private AsynchronousService as;

    protected Map map;
    protected AclUser user;
    protected BigInteger numeroCertificado;
    protected Long numeroTramite;
    protected Long tipo = 0L;
    protected String correo;
    protected RegpLiquidacion liquidacion;
    protected RegRegistrador registrador;
    protected GeneracionDocs gen;
    protected List<RegCertificado> certificados = new ArrayList<>();
    protected List<RegMovimiento> movimientos = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            user = em.find(AclUser.class, us.getUserId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findCertificados() {
        try {
            if (Utils.isNotEmpty(movimientos)) {
                movimientos = new ArrayList<>();
            }
            if (numeroTramite != null) {
                certificados = em.findAll(Querys.getCertificadosToDownload, new String[]{"tramite"}, new Object[]{numeroTramite});
                if (certificados == null) {
                    certificados = new ArrayList<>();
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el número del trámite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findRegMovimientos() {
        try {
            if (Utils.isNotEmpty(certificados)) {
                certificados = new ArrayList<>();
            }
            if (numeroTramite != null) {
                movimientos = em.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{numeroTramite});
                if (movimientos == null) {
                    movimientos = new ArrayList<>();
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el número del trámite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findAllDocumentos() {
        try {
            if (numeroTramite == null) {
                JsfUti.messageWarning(null, "Debe ingresar el número del trámite.", "");
                return;
            }
            certificados = new ArrayList<>();
            movimientos = new ArrayList<>();
            certificados = em.findAll(Querys.getCertificadosToDownload, new String[]{"tramite"}, new Object[]{numeroTramite});
            movimientos = em.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{numeroTramite});
            if (certificados == null) {
                certificados = new ArrayList<>();
            }
            if (movimientos == null) {
                movimientos = new ArrayList<>();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void descargarCertificado(RegCertificado ce) {
        String ruta = SisVars.rutaFirmados;
        try {
            if (ce.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Código de verificación No encontrado.", "");
                return;
            }
            if (ce.getDocumento() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + ce.getCodVerificacion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(ce.getDocumento(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            this.generacionDocumento(ce);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void descargarRazon() {
        if (Utils.isEmpty(movimientos)) {
            JsfUti.messageWarning(null, "No se encontraron inscripciones con el numero de tramite.", "");
            return;
        }
        for (RegMovimiento mov : movimientos) {
            if (mov.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Verifique que la inscripcion numero " + mov.getNumInscripcion() + " tenga generada el codigo de verificacion.", "");
                return;
            }
        }
        RegMovimiento ce = movimientos.get(0);
        String ruta = SisVars.rutaFirmados;
        try {
            if (ce.getDocumento() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + "RAZON_" + ce.getCodVerificacion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(ce.getDocumento(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            this.generacionDocumento(ce);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void descargarActa(RegMovimiento ce) {
        if (Utils.isEmpty(movimientos)) {
            JsfUti.messageWarning(null, "No se encontraron inscripciones con el numero de tramite.", "");
            return;
        }
        for (RegMovimiento mov : movimientos) {
            if (mov.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Verifique que la inscripcion numero " + mov.getNumInscripcion() + " tenga generada el codigo de verificacion.", "");
                return;
            }
        }

        String ruta = SisVars.rutaFirmados;
        try {
            if (ce.getDocumentoActa() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + "ACTA_" + ce.getCodVerificacion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(ce.getDocumentoActa(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            this.generacionDocumento(ce);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generacionDocumento(RegMovimiento ce) {
        try {
            gen = new GeneracionDocs();
            gen.setAclLogin(us.getAclLogin().getId());
            gen.setFechaGeneracion(new Date());
            gen.setUsuario(us.getUserId());
            gen.setMovimiento(ce.getId());
            gen.setObservacion("DESCARGA DE RAZON CON FIRMA DIGITAL");
            em.persist(gen);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generacionDocumento(RegCertificado ce) {
        try {
            ce.setPrintOnline(ce.getPrintOnline() + 1);
            ce.setDatePrintOnline(new Date());
            em.update(ce);

            gen = new GeneracionDocs();
            gen.setAclLogin(us.getAclLogin().getId());
            gen.setFechaGeneracion(new Date());
            gen.setUsuario(us.getUserId());
            gen.setCertificado(ce.getId());
            gen.setObservacion("DESCARGA DE CERTIFICADO CON FIRMA DIGITAL");
            em.persist(gen);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgCorreo() {
        try {
            liquidacion = new RegpLiquidacion();
            if (movimientos.isEmpty() && certificados.isEmpty()) {
                JsfUti.messageWarning(null, "No se encontraron documentos con el numero de tramite.", "");
            } else {
                map = new HashMap();
                map.put("numTramiteRp", numeroTramite);
                liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
                if (liquidacion != null) {
                    if (liquidacion.getCorreoTramite() != null && !liquidacion.getCorreoTramite().isEmpty()) {
                        correo = liquidacion.getCorreoTramite();
                    } else {
                        correo = liquidacion.getBeneficiario().getCorreo1();
                    }
                    JsfUti.update("formEnvioMail");
                    JsfUti.executeJS("PF('dlgReenvioCorreo').show();");
                } else {
                    JsfUti.messageWarning(null, "No se encontro el numero de tramite.", "");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reenviarDocsEmail() {
        try {
            if (correo == null || correo.isEmpty()) {
                JsfUti.messageError(null, "Debe Ingresar un email válido.", "");
            } else if (liquidacion != null && liquidacion.getNumTramiteRp() != null) {
                if (as.reenviarCorreoDocumentos(liquidacion.getNumTramiteRp(), correo, us.getName_user())) {
                    numeroTramite = null;
                    certificados = new ArrayList<>();
                    movimientos = new ArrayList<>();
                    JsfUti.messageInfo(null, "Correo enviado.", "Puede demorar unos minutos que le llegue al usuario.");
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgReenvioCorreo').hide();");
                } else {
                    JsfUti.messageWarning(null, "No se pudo enviar el correo.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se encontro el numero de tramite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public BigInteger getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(BigInteger numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public List<RegCertificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<RegCertificado> certificados) {
        this.certificados = certificados;
    }

    public List<RegMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
