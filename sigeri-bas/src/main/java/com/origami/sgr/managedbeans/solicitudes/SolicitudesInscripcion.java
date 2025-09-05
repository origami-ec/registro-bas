/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.solicitudes;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudPropietarios;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class SolicitudesInscripcion implements Serializable {

    private static final Logger LOG = Logger.getLogger(SolicitudesInscripcion.class.getName());

    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private Entitymanager manager;

    private PubSolicitud solicitud;
    private CtlgItem tipoInmueble;
    private CtlgItem estadoCivil;
    private CtlgItem tipoId;
    private List<CtlgItem> tipoCertificado;
    private List<CtlgItem> tipoCertificadoPara;
    private List<RegActo> actos;
    private List<String> provincias;
    private List<String> cantones;
    private RegPapel papel;
    private List<RegPapel> papeles;
    private RegActo acto;
    private RegEnteJudiciales notaria;
    private CtlgCatalogo inmuebles;
    private CtlgCatalogo estadosCivil;
    private CtlgCatalogo tipoIdCatalogo;
    private CtlgCatalogo certificados;
    private CtlgCatalogo certificadosPara;
    private RegDomicilio ciudad;
    private int tipo;
    private Boolean esPropiedadHorizontal = false;
    private Boolean copiaEscrituraPublica = false;
    private Boolean docAvaluoCatastro = false;
    private Boolean imprimirDocumento = false;

    @PostConstruct
    public void initView() {
        if (!JsfUti.isAjaxRequest()) {
            solicitud = new PubSolicitud();
            ciudad = new RegDomicilio();
            acto = new RegActo();
            notaria = new RegEnteJudiciales();
            papel = new RegPapel();
        }
    }

    /**
     *
     * @param url
     * @param tipo
     */
    public void openDialogF(String url, int tipo) {
        this.tipo = tipo;
        JsfUti.openDialogFrame(url);
    }

    public void selectObject(SelectEvent event) {
        try {
            if (event.getObject() instanceof CatEnte) {
                CatEnte interviniente = (CatEnte) event.getObject();
                switch (this.tipo) {
                    case 1:
                        solicitud.setSolNombres(interviniente.getNombreCompleto());
                        solicitud.setSolCedula(interviniente.getCiRuc());
                        solicitud.setSolCorreo(interviniente.getCorreo1());
                        solicitud.setSolCelular(interviniente.getTelefono1());
                        solicitud.setSolConvencional(interviniente.getTelefono2());
                        solicitud.setSolCalles(interviniente.getDireccion());
                        break;
                    case 2:
                        solicitud.setBenNombres(interviniente.getNombreCompleto());
                        solicitud.setBenDocumento(interviniente.getCiRuc());
                        solicitud.setBenDireccion(interviniente.getDireccion());
                        solicitud.setBenTelefono(interviniente.getTelefono1());
                        solicitud.setBenCorreo(interviniente.getCorreo1());
                        break;
                    case 3:
                        solicitud.setPropNombres(interviniente.getNombreCompleto());
                        solicitud.setPropCedula(interviniente.getCiRuc());
                        List<String> apellidos = Utils.obtenerApellidos(interviniente.getApellidos());
                        if (Utils.isNotEmpty(apellidos)) {
                            solicitud.setPropApellidos(apellidos.get(0));
                            if (apellidos.size() > 0) {
                                solicitud.setPropApellidoMaterno(apellidos.get(1));
                            }
                        }
                        break;
                    case 4:
                        solicitud.setPropConyugueNombres(interviniente.getNombreCompleto());
                        solicitud.setPropConyugueCedula(interviniente.getCiRuc());
                        break;
                }
            } else if (event.getObject() instanceof RegDomicilio) {
                ciudad = (RegDomicilio) event.getObject();
            } else if (event.getObject() instanceof RegActo) {
                acto = (RegActo) event.getObject();
            } else if (event.getObject() instanceof RegEnteJudiciales) {
                notaria = (RegEnteJudiciales) event.getObject();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Procesar Objecto " + event.getObject(), e);
        }
    }

    public void selectObject1(SelectEvent event) {
        try {
            if (event.getObject() instanceof CatEnte) {
                CatEnte interviniente = (CatEnte) event.getObject();
                solicitud.setSolNombres(interviniente.getNombreCompleto());
                solicitud.setSolCedula(interviniente.getCiRuc());
                solicitud.setSolCorreo(interviniente.getCorreo1());
                solicitud.setSolCelular(interviniente.getTelefono1());
                solicitud.setSolConvencional(interviniente.getTelefono2());
                solicitud.setSolCalles(interviniente.getDireccion());
                List<String> apellidos = Utils.obtenerApellidos(interviniente.getApellidos());
                if (Utils.isNotEmpty(apellidos)) {
                    solicitud.setPropApellidos(apellidos.get(0));
                    if (apellidos.size() > 0) {
                        solicitud.setPropApellidoMaterno(apellidos.get(1));
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Procesar Objecto " + event.getObject(), e);
        }
    }

    public void selectObject2(SelectEvent event) {
        try {

            if (event.getObject() instanceof CatEnte) {
                CatEnte interviniente = (CatEnte) event.getObject();
                if (Utils.isEmpty(solicitud.getPropietarios())) {
                    solicitud.setPropietarios(new ArrayList<>());
                }
                if (solicitud.getPropietarios().size() == 5) {
                    JsfUti.messageError(null, "Solo se permiten agregar 5 propietarios.", "");
                    return;
                }
                PubSolicitudPropietarios prop = new PubSolicitudPropietarios();
                prop.setNombres(interviniente.getNombres());
                prop.setApellidos(interviniente.getApellidos());
                prop.setSolicitud(solicitud);
                solicitud.getPropietarios().add(prop);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Procesar Objecto " + event.getObject(), e);
        }
    }

    public CtlgCatalogo getCatalogoPropiedadHorizontal() {
        if (inmuebles == null) {
            Map<String, Object> map = new HashMap();
            map.put("nombre", Constantes.propiedadHorizontal);
            inmuebles = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        }
        return inmuebles;
    }

    public CtlgCatalogo getTiposCertificado() {
        if (certificados == null) {
            Map<String, Object> map = new HashMap();
            map.put("nombre", Constantes.tipoCertificado);
            certificados = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        }
        return certificados;
    }

    public CtlgCatalogo getTiposCertificadoPara() {
        if (certificadosPara == null) {
            Map<String, Object> map = new HashMap();
            map.put("nombre", Constantes.tipoCertificadoPara);
            certificadosPara = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        }
        return certificadosPara;

    }

    public CtlgCatalogo getEstadosCivil() {
        if (estadosCivil == null) {
            Map<String, Object> map = new HashMap();
            map.put("nombre", Constantes.estadosCivil);
            estadosCivil = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        }
        return estadosCivil;

    }

    public CtlgCatalogo getTipoIdCatalogo() {
        if (tipoIdCatalogo == null) {
            Map<String, Object> map = new HashMap();
            map.put("nombre", Constantes.tipoDocumento);
            tipoIdCatalogo = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        }
        return tipoIdCatalogo;

    }

    public List<RegActo> getActos() {
        if (actos == null) {
            Map<String, Object> map = new HashMap();
            map.put("libro", new RegLibro(11L));
            actos = manager.findObjectByParameterList(RegActo.class, map);
        }
        return actos;

    }

    public List<String> getProvincias() {
        if (provincias == null) {
            provincias = manager.getSqlQuery(Querys.provincias);
        }
        return provincias;
    }

    public List<String> getCantones() {
        if (cantones == null && (solicitud.getSolProvincia() != null)) {
            if (!solicitud.getSolProvincia().isEmpty()) {
                cantones = manager.getSqlQueryValues(null, Querys.cantonesProvincias, new Object[]{solicitud.getSolProvincia()});
            }
        }
        return cantones;
    }

    public Boolean getOtros() {
        if (Utils.isNotEmpty(tipoCertificadoPara)) {
            for (CtlgItem ctlgItem : tipoCertificadoPara) {
                if (ctlgItem.getValor().equalsIgnoreCase("otros")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Guarda los datos de la solicitud
     */
    public void save() {
        if (solicitud.getSolCedula() == null) {
            JsfUti.messageError(null, "Debe buscar el solicitante", "");
            return;
        }
        if (solicitud.getSolCalles() == null) {
            JsfUti.messageError(null, "Debe Ingresar la dirección", "");
            return;
        }
        if (solicitud.getSolCorreo() == null) {
            JsfUti.messageError(null, "Debe ingresar un correo electronico", "");
            return;
        }
//        if (solicitud.getBen_documento() == null) {
//            JsfUti.messageError(null, "Debe buscar ", "");
//            return;
//        }
        solicitud.setTipoBien(tipoInmueble);
        if (Utils.isNotEmpty(tipoCertificado)) {
            solicitud.setTipoCertificado(tipoCertificado.get(0));
        }
        if (Utils.isNotEmpty(tipoCertificadoPara)) {
            solicitud.setTipoCertificadoPara(tipoCertificadoPara.get(0));
        }
        if (estadoCivil != null) {
            solicitud.setPropEstadoCivil(this.estadoCivil.getValor());
        }

        this.solicitud.setFechaSolicitud(new Date());
        solicitud = (PubSolicitud) manager.persist(solicitud);
        if (solicitud != null) {
            if (imprimirDocumento) {
                ss.instanciarParametros();
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/formularios/");
                ss.agregarParametro("FUNCIONARIO", session.getName_user());
                ss.setTieneDatasource(false);
                ss.setDataSource(Arrays.asList(solicitud));
                ss.setNombreReporte("formularios/report2");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        }
//        this.continuar();
    }

    /**
     * Toma la informacion que pertenece
     */
    public void saveLicitud() {
        try {
            if (solicitud.getSolCedula() == null) {
                JsfUti.messageError(null, "Debe buscar el solicitante", "");
                return;
            }
            if (solicitud.getSolNombres() == null) {
                JsfUti.messageError(null, "Debe ingresar los nombres", "");
                return;
            }
            this.solicitud.setFechaSolicitud(new Date());
            this.solicitud.setSolTipoDoc(this.getTipoId().getValor());
            this.solicitud = (PubSolicitud) manager.persist(solicitud);
            if (solicitud != null) {
                if (imprimirDocumento) {
                    ss.instanciarParametros();
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/formularios/");
                    ss.setTieneDatasource(false);
                    ss.setDataSource(Arrays.asList(solicitud));
                    ss.setNombreReporte("formularios/report1");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                }
            }
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "LICITUD DE FONDOS", e);
        }
    }

    /**
     * Toma la informacion que pertenece a la cancelaciones
     */
    public void saveCancelacion() {
        try {
            if (solicitud.getSolCedula() == null) {
                JsfUti.messageError(null, "Debe buscar el solicitante", "");
                return;
            }
            if (solicitud.getSolNombres() == null) {
                JsfUti.messageError(null, "Debe ingresar los nombres", "");
                return;
            }
            this.solicitud.setFechaSolicitud(new Date());
            if (Utils.isNotEmpty(papeles)) {
                String papeless = "";
                for (RegPapel papell : papeles) {
                    papeless += papell.getPapel() + ", ";
                }
                this.solicitud.setSolTipoDoc(papeless.substring(0, papeless.length() - 2));
            }
            if (acto != null) {
                this.solicitud.setPayframeUrl(acto.getNombre());
            }
            if (notaria != null) {
                this.solicitud.setBenTipoDoc(notaria.getNombre());
            }
            if (ciudad != null) {
                this.solicitud.setSolParroquia(ciudad.getNombre());
            }
            if (tipoInmueble != null) {
                this.solicitud.setTipoBien(tipoInmueble);
            }

            this.solicitud = (PubSolicitud) manager.persist(solicitud);
            if (solicitud != null) {
                if (imprimirDocumento) {
                    ss.instanciarParametros();
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/formularios/");
                    ss.setTieneDatasource(false);
                    ss.setDataSource(Arrays.asList(solicitud));
                    ss.setNombreReporte("formularios/report3");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                }
            }
//            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "CANCELACION", e);
        }
    }

    public void saveNoBienes() {
        try {
            if (solicitud.getSolCedula() == null) {
                JsfUti.messageError(null, "Debe buscar el solicitante", "");
                return;
            }
            if (solicitud.getSolNombres() == null) {
                JsfUti.messageError(null, "Debe ingresar los nombres", "");
                return;
            }
            this.solicitud.setFechaSolicitud(new Date());
            if (estadoCivil != null) {
                solicitud.setPropEstadoCivil(this.estadoCivil.getValor());
            }

            this.solicitud = (PubSolicitud) manager.persist(solicitud);
            if (solicitud != null) {
                if (imprimirDocumento) {
                    ss.instanciarParametros();
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/formularios/");
                    ss.setTieneDatasource(false);
                    ss.setDataSource(Arrays.asList(solicitud));
                    ss.setNombreReporte("formularios/report4");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                }
            }
//            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "CANCELACION", e);
        }
    }

    private void continuar() {
        JsfUti.messageInfo(null, "Datos almacenados correctamente", "");
        JsfUti.redirectCurrentPage();
    }

    public PubSolicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(PubSolicitud solicitud) {
        this.solicitud = solicitud;
    }

    public Boolean getEsPropiedadHorizontal() {
        return esPropiedadHorizontal;
    }

    public void setEsPropiedadHorizontal(Boolean esPropiedadHorizontal) {
        this.esPropiedadHorizontal = esPropiedadHorizontal;
    }

    public CtlgItem getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(CtlgItem tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public List<CtlgItem> getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(List<CtlgItem> tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public List<CtlgItem> getTipoCertificadoPara() {
        return tipoCertificadoPara;
    }

    public void setTipoCertificadoPara(List<CtlgItem> tipoCertificadoPara) {
        this.tipoCertificadoPara = tipoCertificadoPara;
    }

    public Boolean getCopiaEscrituraPublica() {
        return copiaEscrituraPublica;
    }

    public void setCopiaEscrituraPublica(Boolean copiaEscrituraPublica) {
        this.copiaEscrituraPublica = copiaEscrituraPublica;
    }

    public Boolean getDocAvaluoCatastro() {
        return docAvaluoCatastro;
    }

    public void setDocAvaluoCatastro(Boolean docAvaluoCatastro) {
        this.docAvaluoCatastro = docAvaluoCatastro;
    }

    public Boolean getImprimirDocumento() {
        return imprimirDocumento;
    }

    public void setImprimirDocumento(Boolean imprimirDocumento) {
        this.imprimirDocumento = imprimirDocumento;
    }

    public CtlgItem getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(CtlgItem estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public CtlgItem getTipoId() {
        return tipoId;
    }

    public void setTipoId(CtlgItem tipoId) {
        this.tipoId = tipoId;
    }

    public RegDomicilio getCiudad() {
        return ciudad;
    }

    public void setCiudad(RegDomicilio ciudad) {
        this.ciudad = ciudad;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public RegPapel getPapel() {
        return papel;
    }

    public void setPapel(RegPapel papel) {
        this.papel = papel;
    }

    public RegEnteJudiciales getNotaria() {
        return notaria;
    }

    public void setNotaria(RegEnteJudiciales notaria) {
        this.notaria = notaria;
    }

    public List<RegPapel> getPapeles() {
        return papeles;
    }

    public void setPapeles(List<RegPapel> papeles) {
        this.papeles = papeles;
    }

}
