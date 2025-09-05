/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.models.ReporteTramitesRp;
import com.origami.sgr.bpm.models.TareasSinRealizar;
import com.origami.sgr.entities.AclLogin;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.FirmaElectronica;
import com.origami.sgr.models.FirmaElectronicaModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DatosUsuario implements Serializable {

    private static final Logger LOG = Logger.getLogger(DatosUsuario.class.getName());

    @Inject
    protected Entitymanager em;
    @Inject
    protected UserSession us;
    @Inject
    protected ServletSession ss;
    @Inject
    private FirmaDigitalLocal firmaDigitalLocal;
    @EJB(beanName = "registroPropiedad")
    protected RegistroPropiedadServices reg;

    protected AclUser user;
    private String username = "";
    private String pass;
    private String passOne;
    private String passTwo;
    protected Boolean permitido = false;
    protected Integer tipo = 0;
    protected Date desde;
    protected Date hasta;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected List<TareasSinRealizar> tareasNoRealizadas = new ArrayList<>();

    private FirmaElectronica firmaElectronica;
    private String claveFE;
    private Boolean firmaValidada;

    @PostConstruct
    protected void iniView() {
        try {
            user = em.find(AclUser.class, us.getUserId());

            if (user == null) {
                JsfUti.redirectFaces("/");
            } else {
                username = user.getUsuario();
                this.validaRoles();
                desde = sdf.parse(sdf.format(new Date()));
                hasta = sdf.parse(sdf.format(new Date()));
                Map map = new HashMap();
                map.put("funcionario", user.getUsuario());
                firmaElectronica = (FirmaElectronica) em.findObjectByParameter(FirmaElectronica.class, map);
                if (firmaElectronica == null) {
                    firmaElectronica = new FirmaElectronica();
                    firmaElectronica.setFuncionario(user.getUsuario());
                    firmaElectronica.setFechaCreacion(new Date());
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        for (Long l : us.getRoles()) {
            if (l == 1) {
                permitido = true;
            }
        }
    }

    public void showDlgClave() {
        JsfUti.update("formChangePass");
        JsfUti.executeJS("PF('dlgChangePass').show();");
    }

    public void cambioClave() {
        try {
            if (passOne != null && passTwo != null && pass != null) {
                pass = Utils.encriptaEnMD5(pass);
                if (pass.equals(user.getClave())) {
                    if (passOne.length() > 7) {
                        if (passOne.equals(passTwo)) {
                            //user.setPass(passOne);
                            passOne = Utils.encriptaEnMD5(passOne);
                            user.setClave(passOne);
                            user.setFechaActPass(new Date());
                            user.setCaducadaPass(false);
                            em.update(user);
                            boolean flag = em.update(user);
                            if (flag) {
                                AclLogin aclLogin = new AclLogin();
                                aclLogin.setEvento("Cambio de clave");
                                aclLogin.setIpUserSession(us.getAclLogin().getIpUserSession());
                                aclLogin.setUserSessionId(BigInteger.valueOf(user.getId()));
                                aclLogin.setUserSessionName(us.getName_user());
                                aclLogin.setFechaDoLogin(new Date());
                                aclLogin.setMacClient(us.getAclLogin().getMacClient());
                                em.persist(aclLogin);
                                JsfUti.messageInfo(null, "Datos actualizados con exito. Debe salir e iniciar sesion nuevamente para comprobar los cambios.", "");
                                JsfUti.executeJS("PF('dlgChangePass').hide();");
                                JsfUti.update("mainForm");
                            } else {
                                JsfUti.messageError(null, Messages.problemaConexion, "");
                            }
                        } else {
                            JsfUti.messageError(null, Messages.noCoincidenClaves, "");
                        }
                    } else {
                        JsfUti.messageError(null, "La clave debe tener minimo 8 caracteres.", "");
                    }
                } else {
                    JsfUti.messageError(null, "La clave actual ingresada no coincide.", "");
                }
            } else {
                JsfUti.messageError(null, Messages.faltanCampos, "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void upload(FileUploadEvent file) {
        try {
            String name = Utils.getValorUuid();
            Utils.copyFileServerHidden(file.getFile(), SisVars.rutaFirmaEC, name);
            firmaElectronica.setUid(name);
            JsfUti.messageInfo(null, "Firma cargada correctamente", "");
        } catch (IOException e) {
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo de la Firma Electrónica", "");
        }
    }

    public void validarFirmaElectronica() {
        if (firmaElectronica.getUid() != null && claveFE != null && !claveFE.isEmpty()) {
            try {

                FirmaElectronicaModel firmaElectronicaValidar = new FirmaElectronicaModel();
                firmaElectronicaValidar.setArchivo(SisVars.rutaFirmaEC + File.separator + firmaElectronica.getUid());
                firmaElectronicaValidar.setClave(claveFE);
                firmaElectronicaValidar.setValidarFecha(Boolean.TRUE);
                firmaElectronicaValidar = firmaDigitalLocal.validarFirmaElectronica(firmaElectronicaValidar);
                System.out.println(firmaElectronicaValidar.toString());
                if (firmaElectronicaValidar.getUid() != null) {
                    firmaValidada = Boolean.TRUE;
                    firmaElectronica.setEstadoFirma(firmaElectronicaValidar.getEstadoFirma());
                    firmaElectronica.setFechaEmision(firmaElectronicaValidar.getFechaEmision());
                    firmaElectronica.setFechaExpiracion(firmaElectronicaValidar.getFechaExpiracion());
                    firmaElectronica.setIsuser(firmaElectronicaValidar.getIsuser());
                    JsfUti.messageInfo(null, "Firma electrónica validada", "");
                } else {
                    firmaValidada = Boolean.FALSE;
                    JsfUti.messageError(null, "No se pudo validar su firma electrónica", "Intente nuevamente");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsfUti.messageError(null, "Debe subir el archivo P12 y escribir su clave", "");
        }
    }

    public void guardarFirmaElectronica() {
        firmaElectronica = (FirmaElectronica) em.persist(firmaElectronica);
        JsfUti.messageInfo(null, "Firma electrónica grabada correctamente", "");
    }

    public void selectTipoReporte() {
        switch (tipo) {
            case 1:
                this.reporteTramitesPendientes();
                break;
            case 2:
                this.reporteDatosIngresados();
                break;
            case 3:
                this.reporteTareasSinRealizar();
                break;
            default:
                JsfUti.messageWarning(null, "Seleccione el tipo de reporte que necesita.", "");
                break;
        }
    }

    public void reporteTramitesPendientes() {
        List<ReporteTramitesRp> list;
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    list = reg.getTramitesByUser(desde, Utils.sumarRestarDiasFecha(hasta, 1), us.getName_user());
                    if (!list.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("tramitesPendientesByUser");
                        ss.agregarParametro("USUARIO", us.getName_user());
                        ss.agregarParametro("DESDE", sdf.format(desde));
                        ss.agregarParametro("HASTA", sdf.format(hasta));
                        ss.setDataSource(list);
                        JsfUti.redirectNewTab("/sgr/Documento");
                    } else {
                        JsfUti.messageWarning(null, "La consulta no tiene valores.", "");
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteDatosIngresados() {
        try {
            if (desde != null && hasta != null) {
                if (!desde.after(hasta)) {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(Boolean.TRUE);
                    ss.setNombreSubCarpeta("workflow");
                    ss.setNombreReporte("DetalleDatosUsuario");
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/workflow/");
                    ss.agregarParametro("USER", username);
                    ss.agregarParametro("DESDE", desde);
                    ss.agregarParametro("HASTA", Utils.sumarRestarDiasFecha(hasta, 1));
                    ss.agregarParametro("HASTA_STRING", sdf.format(hasta));
                    ss.agregarParametro("NAME_USER", us.getName_user());
                    JsfUti.redirectNewTab("/sgr/Documento");
                } else {
                    JsfUti.messageWarning(null, "Error al seleccionar fechas.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Error al seleccionar fechas.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteTareasSinRealizar() {
        try {
            if (desde != null && hasta != null) {
                if (desde.before(hasta) || desde.equals(hasta)) {
                    tareasNoRealizadas = reg.getTareasSinRealizar(us.getName_user());

                    if (!tareasNoRealizadas.isEmpty()) {
                        ss.instanciarParametros();
                        ss.setTieneDatasource(Boolean.FALSE);
                        ss.setNombreSubCarpeta("workflow");
                        ss.setNombreReporte("reporteTareasSinRealizar");
                        ss.agregarParametro("USER", us.getName_user());
                        ss.setOnePagePerSheet(Boolean.TRUE);
                        ss.setDataSource(tareasNoRealizadas);
                        JsfUti.redirectNewTab("/sgr/Documento");
                    } else {
                        JsfUti.messageWarning(null, "Advertencia", "La consulta no tiene valores.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassOne() {
        return passOne;
    }

    public void setPassOne(String passOne) {
        this.passOne = passOne;
    }

    public String getPassTwo() {
        return passTwo;
    }

    public void setPassTwo(String passTwo) {
        this.passTwo = passTwo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public String getClaveFE() {
        return claveFE;
    }

    public void setClaveFE(String claveFE) {
        this.claveFE = claveFE;
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public Boolean getFirmaValidada() {
        return firmaValidada;
    }

    public void setFirmaValidada(Boolean firmaValidada) {
        this.firmaValidada = firmaValidada;
    }

}
