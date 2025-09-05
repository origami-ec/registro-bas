/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.entities.Barrios;
import com.origami.sgr.entities.CatParroquia;
import com.origami.sgr.entities.CtlgCargo;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegArancel;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoActo;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RegEnteIntervinienteLazy;
import com.origami.sgr.lazymodels.RegPapelLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Mantenimientos implements Serializable {

    private static final Logger LOG = Logger.getLogger(Mantenimientos.class.getName());
    private static final long serialVersionUID = 1L;

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;

    protected Map map;
    protected Map<String, Object> params;
    protected LazyModel<RegActo> actos;
    protected LazyModel<RegActo> contratos;
    protected LazyModel<RegDomicilio> domicilios;
    protected RegEnteIntervinienteLazy intervinientes;
    protected LazyModel<RegEnteJudiciales> juzgados;
    protected RegPapelLazy papeles;
    protected LazyModel<RegRegistrador> registradores;
    protected List<RegLibro> libros;
    protected List<CtlgCargo> cargos;
    protected List<RegLibro> libroByActo;
    protected List<RegPapel> papelByActo;
    protected CtlgCargo cargo;
    protected RegActo acto;
    protected RegDomicilio domicilio;
    protected RegEnteInterviniente ente;
    protected RegPapel papel;
    protected RegEnteJudiciales juzgado;
    protected RegLibro libro;
    protected RegLibro libroSeleccionado;
    protected RegRegistrador registrador;
    protected Boolean permitido = false;
    protected Boolean nuevo = false;
    protected RegArancel arancel;
    protected RegArancel arancelSeleccionado;
    protected List<RegArancel> aranceles;
    protected LazyModel<RegArancel> arancelesLazy;
    protected boolean soloCuantia = false;
    protected LazyModel<Barrios> lazyBarrios;
    protected Barrios barrio;
    protected List<CatParroquia> parroquias;
    protected Long cobro = 0L;

    @PostConstruct
    protected void iniView() {
        try {
            contratos = new LazyModel(RegActo.class, "nombre", "ASC");
            contratos.addFilter("tipoCobro", new RegTipoCobroActo(1L)); //ACTOS REGISTRALES

            actos = new LazyModel(RegActo.class, "nombre", "ASC");
            actos.addFilter("tipoCobro", new RegTipoCobroActo(2L)); //ACTOS CON ARANCELES

            arancelesLazy = new LazyModel<>(RegArancel.class, "denominacion", "ASC");
            domicilios = new LazyModel(RegDomicilio.class, "nombre", "ASC");
            intervinientes = new RegEnteIntervinienteLazy();
            juzgados = new LazyModel<>(RegEnteJudiciales.class);
            papeles = new RegPapelLazy();
            registradores = new LazyModel<>(RegRegistrador.class);
            libros = em.findAllEntCopy(Querys.getRegLibroListall);
            cargos = em.findAllEntCopy(Querys.CtlgTipocargoOrderByNombre);
            aranceles = em.findAllEntCopy(Querys.getAranceles);

            cargo = new CtlgCargo();
            acto = new RegActo();
            domicilio = new RegDomicilio();
            ente = new RegEnteInterviniente();
            papel = new RegPapel();
            juzgado = new RegEnteJudiciales();
            libro = new RegLibro();
            registrador = new RegRegistrador();

            libroByActo = new ArrayList<>();
            papelByActo = new ArrayList<>();
            arancel = new RegArancel();

            lazyBarrios = new LazyModel<>(Barrios.class, "nombre", "ASC");
            barrio = new Barrios();
            parroquias = em.findAllOrdEntCopy(CatParroquia.class, new String[]{"descripcion"}, new Boolean[]{true});
            this.validaRoles();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        for (Long l : us.getRoles()) {
            if (l == 1) {
                permitido = true;
                break;
            }
        }
    }

    public void selectActo(RegActo ac) {
        try {
            acto = ac;
            cobro = acto.getTipoActo().getId();
            arancelSeleccionado = acto.getArancel();
            libroSeleccionado = acto.getLibro();
            JsfUti.update("formContrato");
            JsfUti.executeJS("PF('dlgNewActo').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectObjectPapel(SelectEvent event) {
        RegPapel p = (RegPapel) event.getObject();
        papelByActo.add(p);
    }

    public void selectDomicilio(RegDomicilio d) {
        domicilio = d;
    }

    public void selectEnte(RegEnteInterviniente r) {
        ente = r;
    }

    public void selectLibro(RegLibro l) {
        libro = l;
    }

    public void selectPapel(RegPapel p) {
        papel = p;
    }

    public void selectJuzgado(RegEnteJudiciales e) {
        juzgado = e;
    }

    public void selectRegistrador(RegRegistrador re) {
        registrador = re;
    }

    public void selectCargo(CtlgCargo item) {
        cargo = item;
    }

    public void selectArancel(RegArancel a) {
        arancel = a;
        if (arancel.getValor() != null) {
            if (arancel.getValor().compareTo(new BigDecimal("-1")) == 0) {
                soloCuantia = true;
                arancel.setValor(null);
            } else {
                soloCuantia = false;
            }
        } else {
            soloCuantia = false;
        }
    }

    public void selectSoloCuantia() {
        if (soloCuantia) {
            arancel.setValor(null);
        } else {

        }

    }

    public void borrarPapel(int index) {
        papelByActo.remove(index);
    }

    public void showDlgActo() {
        JsfUti.update("formContrato");
        JsfUti.executeJS("PF('dlgNewActo').show();");
    }

    public void saveActo() {
        try {
            if (acto.getNombre() != null && !acto.getNombre().isEmpty()) {
                if (arancelSeleccionado != null) {
                    if (arancelSeleccionado.getId() != null) {
                        acto.setArancel(arancelSeleccionado);
                    }
                }
                if (libroSeleccionado != null) {
                    acto.setLibro(libroSeleccionado);
                }
                if (acto.getId() == null) {
                    if (acto.getSolvencia() == null) {
                        acto.setSolvencia(Boolean.FALSE);
                    }
                    acto.setFijo(Boolean.TRUE);
                    acto.setValor(BigDecimal.ZERO);
                    acto.setEstado(Boolean.TRUE);
                    acto.setUserCre(us.getName_user());
                    acto.setFechaCre(new Date());
                    acto = (RegActo) em.persist(acto);
                } else {
                    acto.setUserEdicion(us.getName_user());
                    acto.setFechaEdicion(new Date());
                    em.update(acto);
                }
                actos = new LazyModel<>(RegActo.class, "nombre", "ASC");
                acto = new RegActo();
                papelByActo = new ArrayList<>();
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
                arancelSeleccionado = null;
                libroSeleccionado = null;
                JsfUti.update("mainForm:accMantenimiento:dtActos");
                JsfUti.executeJS("PF('dlgNewActo').hide();");
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectActoDirecto(RegActo ac) {
        try {
            acto = ac;
            JsfUti.update("formContrato");
            JsfUti.executeJS("PF('dlgNewActo').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveActoDirecto() {
        try {
            if (acto.getNombre() == null || acto.getNombre().isEmpty()) {
                JsfUti.messageWarning(null, "Debe ingresar el nombre del contrato y el libro.", "");
            } else {
                //acto.setLibro(libroSeleccionado);
                acto.setSolvencia(Boolean.FALSE);
                acto.setFijo(Boolean.TRUE);
                acto.setValor(BigDecimal.ZERO);
                acto.setEstado(Boolean.TRUE);
                acto.setTipoCobro(new RegTipoCobroActo(1L));
                acto.setUserCre(us.getName_user());
                acto.setFechaCre(new Date());
                em.persist(acto);
                contratos = new LazyModel(RegActo.class, "nombre", "ASC");
                contratos.addFilter("tipoCobro", new RegTipoCobroActo(1L));
                acto = new RegActo();
                libroSeleccionado = new RegLibro();
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveDomicilio() {
        try {
            if (domicilio.getNombre() != null && !domicilio.getNombre().isEmpty()) {
                if (domicilio.getId() == null) {
                    domicilio.setUsuarioIngreso(us.getName_user());
                    domicilio.setFechaIngreso(new Date());
                    em.persist(domicilio);
                } else {
                    domicilio.setUsuarioEdicion(us.getName_user());
                    domicilio.setFechaEdicion(new Date());
                    em.update(domicilio);
                }
                domicilios = new LazyModel(RegDomicilio.class, "nombre", "ASC");
                domicilio = new RegDomicilio();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveCargo() {
        try {
            if (cargo.getNombre() != null && !cargo.getNombre().isEmpty()) {
                if (cargo.getId() == null) {
                    cargo.setEstado(true);
                    em.persist(cargo);
                } else {
                    em.update(cargo);
                }
                cargos = em.findAllEntCopy(Querys.CtlgTipocargoOrderByNombre);
                cargo = new CtlgCargo();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveEnte() {
        try {
            if (ente.getId() != null) {
                if (ente.getNombre() != null && !ente.getNombre().isEmpty()) {
                    ente.setUsuarioEdicion(us.getName_user());
                    ente.setFechaEdicion(new Date());
                    em.update(ente);
                    intervinientes = new RegEnteIntervinienteLazy();
                    ente = new RegEnteInterviniente();
                } else {
                    JsfUti.messageWarning(null, Messages.camposObligatorios, "Nombre vacío.");
                }
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar el interviniente.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveLibro() {
        try {
            if (libro.getNombre() != null && !libro.getNombre().isEmpty()) {
                if (libro.getId() == null) {
                    libro.setUserCre(us.getName_user());
                    libro.setFechaCre(new Date());
                    em.persist(libro);
                } else {
                    libro.setUserEdicion(us.getName_user());
                    libro.setFechaEdicion(new Date());
                    em.update(libro);
                }
                libros = em.findAllEntCopy(Querys.getRegLibros);
                libro = new RegLibro();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "Nombre vacío.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void savePapel() {
        try {
            if (papel.getPapel() != null && !papel.getPapel().isEmpty()) {
                if (papel.getId() == null) {
                    papel.setUserCre(us.getName_user());
                    papel.setFechaCre(new Date());
                    em.persist(papel);
                } else {
                    papel.setUserEdicion(us.getName_user());
                    papel.setFechaEdicion(new Date());
                    em.update(papel);
                }
                papeles = new RegPapelLazy();
                papel = new RegPapel();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveJuzgado() {
        try {
            if (juzgado.getNombre() != null && !juzgado.getNombre().isEmpty()) {
                juzgado.setAbreviatura(juzgado.getAbreviatura().toUpperCase());
                juzgado.setNombre(juzgado.getNombre().toUpperCase());
                if (juzgado.getId() == null) {
                    juzgado.setUserCreacion(us.getName_user());
                    juzgado.setFechaCreacion(new Date());
                    em.persist(juzgado);
                } else if (juzgado.getSedi()) {
                    JsfUti.messageWarning(null, "No se puede editar este registro.", "");
                    return;
                } else {
                    juzgado.setUserEdicion(us.getName_user());
                    juzgado.setFechaEdicion(new Date());
                    em.update(juzgado);
                }
                juzgados = new LazyModel<>(RegEnteJudiciales.class);
                juzgado = new RegEnteJudiciales();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveRegistrador() {
        try {
            if (registrador.getNombreCompleto() != null && registrador.getTituloCompleto() != null) {
                if (registrador.getId() == null) {
                    registrador.setUserIngreso(us.getName_user());
                    registrador.setFechaIngreso(new Date());
                    em.persist(registrador);
                } else {
                    registrador.setUserEdicion(us.getName_user());
                    registrador.setFechaEdicion(new Date());
                    em.update(registrador);
                }
                registradores = new LazyModel<>(RegRegistrador.class);
                registrador = new RegRegistrador();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveBarrio() {
        try {
            if (barrio.getNombre() != null && !barrio.getNombre().isEmpty()) {
                if (barrio.getId() == null) {
                    barrio.setEstado(true);
                }
                em.persist(barrio);
                barrio = new Barrios();
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "Nombre vacío.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlg(String urlFacelet) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "50%");
        options.put("closable", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public void showDlgNewReg() {
        nuevo = true;
        registrador = new RegRegistrador();
        JsfUti.update("frmReg");
        JsfUti.executeJS("PF('dlgRegistrador').show();");
    }

    public void showDlgEditReg(RegRegistrador r) {
        nuevo = false;
        registrador = r;
        JsfUti.update("frmReg");
        JsfUti.executeJS("PF('dlgRegistrador').show();");
    }

    public void cambiarRegistrador() {
        try {
            if (registrador.getNombreCompleto() == null || registrador.getTituloCompleto() == null) {
                JsfUti.messageError(null, "Faltan Campos", "Todos los campos son obligatorios.");
            } else {
                /*if (registrador.isFirmaJuridico()) {
                    em.updateNativeQuery(Querys.updateRegistradoresJuridico, new Object[]{});
                }*/
                em.updateNativeQuery(Querys.updateRegistradores, new Object[]{});
                registrador.setActual(true);
                registrador.setFirmaJuridico(true);
                registrador.setFirmaElectronica(true);
                if (registrador.getId() == null) {
                    registrador.setFechaIngreso(new Date());
                    registrador.setUserIngreso(us.getName_user());
                } else {
                    registrador.setFechaEdicion(new Date());
                    registrador.setUserEdicion(us.getName_user());
                }
                em.persist(registrador);
                registradores = new LazyModel<>(RegRegistrador.class);
                JsfUti.update("mainForm:dtRegistradores");
                JsfUti.messageInfo(null, "Se realizaron los cambios con éxito...", "");
                JsfUti.executeJS("PF('dlgRegistrador').hide();");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveArancel() {
        try {
            if (arancel.getDenominacion() != null && !arancel.getDenominacion().isEmpty()) {
                if (!soloCuantia && arancel.getValor() == null) {
                    JsfUti.messageInfo(null, "Debe poner el valor de Arancel.", "");
                    return;
                }
                if (soloCuantia) {
                    arancel.setValor(new BigDecimal("-1"));
                }
                arancel.setDenominacion(arancel.getDenominacion().toUpperCase());
                arancel.setValorFijo(!soloCuantia);
                if (arancel.getId() == null) {
                    arancel.setUserCre(us.getName_user());
                    arancel.setFechaCre(new Date());
                    em.persist(arancel);
                    JsfUti.messageInfo(null, "Arancel registrado con exito.", "");
                } else {
                    arancel.setUserEdicion(us.getName_user());
                    arancel.setFechaEdicion(new Date());
                    em.update(arancel);
                    JsfUti.messageInfo(null, "Arancel actualizado con exito.", "");
                }
                arancelesLazy = new LazyModel<>(RegArancel.class, "denominacion", "ASC");
                arancel = new RegArancel();
                soloCuantia = false;
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void saveActosPago() {
        try {
            if (acto.getNombre() == null || acto.getNombre().isEmpty()) {
                JsfUti.messageWarning(null, "Debe ingresar el nombre del acto.", "");
                return;
            }
            if (!acto.getSolvencia()) {
                if (libroSeleccionado == null) {
                    JsfUti.messageWarning(null, "Debe seleccionar el libro.", "");
                    return;
                } else {
                    acto.setLibro(libroSeleccionado);
                }
            }
            if (arancelSeleccionado == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el arancel.", "");
                return;
            }
            if (cobro.intValue() < 1) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de acto.", "");
                return;
            }
            acto.setPeso(1);
            //acto.setCodigoAnterior(0);
            acto.setArancel(arancelSeleccionado);
            acto.setTipoActo(new RegTipoActo(cobro));
            if (acto.getSolvencia()) {
                acto.setDias(2);
                if (cobro == 1) { //CERTIFICADOS PROPIEDAD
                    acto.setLibro(new RegLibro(51L));
                } else { //CERTIFICADOS MERCANTIL
                    acto.setLibro(new RegLibro(52L));
                }
            } else {
                acto.setDias(5);
            }
            acto.setNombre(acto.getNombre().toUpperCase());
            if (acto.getId() == null) {
                acto.setFijo(Boolean.TRUE);
                acto.setValor(BigDecimal.ZERO);
                acto.setEstado(Boolean.TRUE);
                acto.setTipoCobro(new RegTipoCobroActo(2L)); //ACTOS CON ARANCELES
                acto.setUserCre(us.getName_user());
                acto.setFechaCre(new Date());
                acto = (RegActo) em.persist(acto);
            } else {
                acto.setUserEdicion(us.getName_user());
                acto.setFechaEdicion(new Date());
                em.update(acto);
            }
            actos = new LazyModel(RegActo.class, "nombre", "ASC");
            actos.addFilter("tipoCobro", new RegTipoCobroActo(2L)); //ACTOS CON ARANCELES
            acto = new RegActo();
            arancelSeleccionado = null;
            libroSeleccionado = null;
            JsfUti.update("mainForm:accMantenimiento:dtActos");
            JsfUti.executeJS("PF('dlgNewActo').hide();");
            JsfUti.messageInfo(null, Messages.transaccionOK, "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgUpload() {
        JsfUti.update("formUploadKey");
        JsfUti.executeJS("PF('dlgUploadKey').show();");
    }

    public void handleUploadKey(FileUploadEvent event) throws IOException {
        try {
            UploadedFile key = event.getFile();
            String ruta = SisVars.rutaFirmaEC + key.getFileName();
            try (FileOutputStream fos = new FileOutputStream(ruta)) {
                fos.write(key.getContent());
            }
            registrador.setFileSign(ruta);
            JsfUti.messageInfo(null, "Archivo de firma cargada con exito.", "");
            JsfUti.update("frmReg");
            JsfUti.executeJS("PF('dlgUploadKey').hide();");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public boolean isSoloCuantia() {
        return soloCuantia;
    }

    public void setSoloCuantia(boolean soloCuantia) {
        this.soloCuantia = soloCuantia;
    }

    public RegDomicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(RegDomicilio domicilio) {
        this.domicilio = domicilio;
    }

    public RegEnteInterviniente getEnte() {
        return ente;
    }

    public void setEnte(RegEnteInterviniente ente) {
        this.ente = ente;
    }

    public RegPapel getPapel() {
        return papel;
    }

    public void setPapel(RegPapel papel) {
        this.papel = papel;
    }

    public RegEnteJudiciales getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(RegEnteJudiciales juzgado) {
        this.juzgado = juzgado;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public LazyModel<RegRegistrador> getRegistradores() {
        return registradores;
    }

    public void setRegistradores(LazyModel<RegRegistrador> registradores) {
        this.registradores = registradores;
    }

    public RegRegistrador getRegistrador() {
        return registrador;
    }

    public void setRegistrador(RegRegistrador registrador) {
        this.registrador = registrador;
    }

    public List<RegLibro> getLibroByActo() {
        return libroByActo;
    }

    public void setLibroByActo(List<RegLibro> libroByActo) {
        this.libroByActo = libroByActo;
    }

    public List<RegPapel> getPapelByActo() {
        return papelByActo;
    }

    public void setPapelByActo(List<RegPapel> papelByActo) {
        this.papelByActo = papelByActo;
    }

    public LazyModel<RegActo> getActos() {
        return actos;
    }

    public void setActos(LazyModel<RegActo> actos) {
        this.actos = actos;
    }

    public LazyModel<RegDomicilio> getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(LazyModel<RegDomicilio> domicilios) {
        this.domicilios = domicilios;
    }

    public RegEnteIntervinienteLazy getIntervinientes() {
        return intervinientes;
    }

    public void setIntervinientes(RegEnteIntervinienteLazy intervinientes) {
        this.intervinientes = intervinientes;
    }

    public LazyModel<RegEnteJudiciales> getJuzgados() {
        return juzgados;
    }

    public void setJuzgados(LazyModel<RegEnteJudiciales> juzgados) {
        this.juzgados = juzgados;
    }

    public RegPapelLazy getPapeles() {
        return papeles;
    }

    public void setPapeles(RegPapelLazy papeles) {
        this.papeles = papeles;
    }

    public List<RegLibro> getLibros() {
        return libros;
    }

    public void setLibros(List<RegLibro> libros) {
        this.libros = libros;
    }

    public List<CtlgCargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<CtlgCargo> cargos) {
        this.cargos = cargos;
    }

    public CtlgCargo getCargo() {
        return cargo;
    }

    public void setCargo(CtlgCargo cargo) {
        this.cargo = cargo;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public RegArancel getArancel() {
        return arancel;
    }

    public void setArancel(RegArancel arancel) {
        this.arancel = arancel;
    }

    public List<RegArancel> getAranceles() {
        return aranceles;
    }

    public void setAranceles(List<RegArancel> aranceles) {
        this.aranceles = aranceles;
    }

    public LazyModel<RegArancel> getArancelesLazy() {
        return arancelesLazy;
    }

    public void setArancelesLazy(LazyModel<RegArancel> arancelesLazy) {
        this.arancelesLazy = arancelesLazy;
    }

    public RegArancel getArancelSeleccionado() {
        return arancelSeleccionado;
    }

    public void setArancelSeleccionado(RegArancel arancelSeleccionado) {
        this.arancelSeleccionado = arancelSeleccionado;
    }

//</editor-fold>
    public RegLibro getLibroSeleccionado() {
        return libroSeleccionado;
    }

    public void setLibroSeleccionado(RegLibro libroSeleccionado) {
        this.libroSeleccionado = libroSeleccionado;
    }

    public LazyModel<Barrios> getLazyBarrios() {
        return lazyBarrios;
    }

    public void setLazyBarrios(LazyModel<Barrios> lazyBarrios) {
        this.lazyBarrios = lazyBarrios;
    }

    public Barrios getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrios barrio) {
        this.barrio = barrio;
    }

    public List<CatParroquia> getParroquias() {
        return parroquias;
    }

    public void setParroquias(List<CatParroquia> parroquias) {
        this.parroquias = parroquias;
    }

    public Long getCobro() {
        return cobro;
    }

    public void setCobro(Long cobro) {
        this.cobro = cobro;
    }

    public LazyModel<RegActo> getContratos() {
        return contratos;
    }

    public void setContratos(LazyModel<RegActo> contratos) {
        this.contratos = contratos;
    }

}
