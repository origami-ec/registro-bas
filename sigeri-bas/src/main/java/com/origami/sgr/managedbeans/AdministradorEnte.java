/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.lazymodels.CatEnteLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.VerCedulaUtils;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
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
public class AdministradorEnte implements Serializable {

    private static final Logger LOG = Logger.getLogger(AdministradorEnte.class.getName());

    @Inject
    private IngresoTramiteLocal itl;

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;

    @Inject
    protected RegistroPropiedadServices reg;

    protected VerCedulaUtils vcu;
    protected CatEnteLazy cel;
    protected CatEnte ente, enteDinardap;
    protected Boolean persona = true;
    protected String cedula = "";
    protected String nombre = "";
    protected String razonsocial = "";
    protected String tipoDocumento = "C";
    protected Boolean consultaDinardap = Boolean.FALSE;
    protected Map map;

    @PostConstruct
    protected void iniView() {
        try {
            cel = new CatEnteLazy();
            ente = new CatEnte();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNewEnte() {
        ente = new CatEnte();
        JsfUti.executeJS("PF('dlgNewCliente').show();");
        JsfUti.update("formNewEnte");
    }

    public void showDlgEditEnte(CatEnte catente) {
        ente = catente;
        tipoDocumento = ente.getTipoIdentificacion();
        JsfUti.executeJS("PF('dlgEditEnte').show();");
        JsfUti.update("formEditEnte");
    }

    public void updateTipoDoc() {
        if (ente.getEsPersona()) {
            tipoDocumento = "C";
        } else {
            tipoDocumento = "R";
        }
    }

    public void editEnte() {
        try {
            if (this.validateNames()) {
                if (!this.validacionCorreo()) {
                    return;
                }
                this.namesToUpper();
                ente.setTipoIdentificacion(tipoDocumento);
                ente.setUserMod(us.getName_user());
                ente.setFechaMod(new Date());
                em.update(ente);
                cel = new CatEnteLazy();
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgEditEnte').hide();");
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
            } else {
                JsfUti.messageWarning(null, "Faltan de ingresar lo campos nombres, apellidos o razon social.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validacionCedula() {
        try {
            if (ente.getCiRuc() != null) {
                if (this.existeEnte(ente.getCiRuc())) {
                    JsfUti.messageWarning(null, Messages.ciRucExiste, "");
                } else {
                    if (ente.getEsPersona()) {
                        //this.buscarDatoSeguro();
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validacionCorreo() {
        if (ente.getCorreo1() != null && !ente.getCorreo1().isEmpty() && !Utils.validarEmailConExpresion(ente.getCorreo1())) {
            JsfUti.messageWarning(null, "Dato del campo Correo1 no es valido.", "");
            return false;
        }
        if (ente.getCorreo2() != null && !ente.getCorreo2().isEmpty() && !Utils.validarEmailConExpresion(ente.getCorreo2())) {
            JsfUti.messageWarning(null, "Dato del campo Correo2 no es valido.", "");
            return false;
        }
        return true;
    }

    public Boolean existeEnte(String ciruc) {
        if (ente != null && ente.getId() != null) {
            return Boolean.FALSE;
        }
        CatEnte temp = (CatEnte) em.find(Querys.getEnteByIdent, new String[]{"ciRuc"}, new Object[]{ciruc});
        return temp != null;
    }

    public boolean validateNames() {
        if (ente.getEsPersona()) {
            if (ente.getNombres().isEmpty() || ente.getApellidos().isEmpty()) {
                return false;
            }
        } else {
            if (ente.getRazonSocial().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void namesToUpper() {
        if (ente.getNombres() != null) {
            ente.setNombres(ente.getNombres().toUpperCase());
        }
        if (ente.getApellidos() != null) {
            ente.setApellidos(ente.getApellidos().toUpperCase());
        }
        if (ente.getRazonSocial() != null) {
            ente.setRazonSocial(ente.getRazonSocial().toUpperCase());
        }
        if (ente.getNombreComercial() != null) {
            ente.setNombreComercial(ente.getNombreComercial().toUpperCase());
        }
    }

    /*
     * SOLO FUNCIONA PARA PERSONAS NATURALES y JURIDICAS =( </3
     */
    public void buscarDatoSeguro() {
        try {
            if (ente.getCiRuc() != null && !ente.getCiRuc().isEmpty()) {
                enteDinardap = reg.buscarGuardarEnteDinardap(ente.getCiRuc());
                if (enteDinardap != null) {
                    ente = enteDinardap;
                    ente.setTipoIdentificacion(tipoDocumento);
                    consultaDinardap = Boolean.TRUE;
                } else {
                    cedula = ente.getCiRuc();
                    ente = new CatEnte();
                    ente.setCiRuc(cedula);
                    JsfUti.messageError(null, "No se encontraron Resultados para: " + cedula, "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, "MENSAJE DE ERROR", "ERROR DE APLICACION!!!");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveEnte() {
        try {
            if (this.validateNames()) {
                if (!this.validacionCorreo()) {
                    return;
                }
                this.namesToUpper();
                ente.setUserCre(us.getName_user());
                ente.setFechaCre(new Date());
                ente.setTipoIdentificacion(tipoDocumento);
                if (ente.getCiRuc().isEmpty()) {
                    ente.setExcepcional(true);
                    itl.saveCatEnteSinCedRuc(ente);
                    this.confirmacion();
                } else if (this.existeEnte(ente.getCiRuc())) {
                    JsfUti.messageWarning(null, Messages.ciRucExiste, "");
                } else {
                    vcu = new VerCedulaUtils();
                    switch (tipoDocumento) {
                        case "P":
                            updateEnte();
                            break;
                        case "C":
                            System.out.println("entro a cedula aqunque es ruc");
                            if (consultaDinardap) {
                                updateEnte();
                            } else if (vcu.isCIValida(ente.getCiRuc())) {
                                if (ente.getId() == null) {
                                    em.persist(ente);
                                } else {
                                    em.merge(ente);
                                }
                                this.confirmacion();
                            } else {
                                JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                            }
                            break;
                        case "R":
                            if (consultaDinardap) {
                                updateEnte();
                            } else if (vcu.isRucValido(ente.getCiRuc())) {
                                updateEnte();
                            } else {
                                JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                            }
                            break;
                    }
                }
            } else {
                JsfUti.messageWarning(null, "Faltan de ingresar lo campos nombres, apellidos o razon social.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void updateEnte() {
        if (ente.getId() == null) {
            ente=(CatEnte) em.persist(ente);
        } else {
            ente= (CatEnte) em.merge(ente);
        }
        this.confirmacion();
    }

    public void confirmacion() {
        tipoDocumento = "C";
        cel = new CatEnteLazy();
        JsfUti.update("mainForm");
        JsfUti.executeJS("PF('dlgNewCliente').hide();");
        JsfUti.messageInfo(null, Messages.transaccionOK, "");
    }

    public void buscarDinardap() {
        try {
            PubPersona pubPersona;
            if (!ente.getCiRuc().isEmpty()) {
                pubPersona = reg.buscarDinardap(ente.getCiRuc());
                if (pubPersona != null && !pubPersona.getCiRuc().isEmpty()) {
                    if (pubPersona.getCiRuc().length() == 13) {
                        ente.setRazonSocial(pubPersona.getRazonSocial());
                        ente.setDireccion(pubPersona.getDireccion());
                        ente.setCorreo1(pubPersona.getCorreo1());
                    } else {
                        String[] name = pubPersona.getApellidos().split(" ");
                        switch (name.length) {
                            case 4:
                                ente.setApellidos(name[0].concat(" ").concat(name[1]));
                                ente.setNombres(name[2].concat(" ").concat(name[3]));
                                break;
                            case 3:
                                ente.setApellidos(name[0].concat(" ").concat(name[1]));
                                ente.setNombres(name[2]);
                                break;
                            default:
                                ente.setApellidos(name[0]);
                                ente.setNombres(name[1]);
                                break;
                        }
                        ente.setDireccion(pubPersona.getDireccion());
                        ente.setFechaNacimiento(new Date(pubPersona.getFechaNacimientoLong()));
                        ente.setEstadoCivil(this.getCatalogo(pubPersona.getEstadoCivilText()));
                    }
                } else {
                    JsfUti.messageError(null, "NO se encontró información con la identificación ingresada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar campo CI/RUC.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public CtlgItem getCatalogo(String value) {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        map.put("codename", value.toLowerCase().trim());
        return (CtlgItem) em.findObjectByParameter(Querys.getCtlgItemByCatalogoCodeName, map);
    }

    public List<CtlgItem> getEstadosCiviles() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        return em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public CatEnteLazy getCel() {
        return cel;
    }

    public void setCel(CatEnteLazy cel) {
        this.cel = cel;
    }

    public UserSession getUs() {
        return us;
    }

    public void setUs(UserSession us) {
        this.us = us;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public Boolean getPersona() {
        return persona;
    }

    public void setPersona(Boolean persona) {
        this.persona = persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

}
