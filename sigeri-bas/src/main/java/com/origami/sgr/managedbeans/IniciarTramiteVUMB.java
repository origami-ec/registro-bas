/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.DepartamentoUsuario;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegistroSolicitudRequisitos;
import com.origami.sgr.entities.Servicio;
import com.origami.sgr.entities.ServicioRequisito;
import com.origami.sgr.entities.ServicioTipo;
import com.origami.sgr.entities.SolicitudDepartamento;
import com.origami.sgr.entities.SolicitudServicios;
import com.origami.sgr.entities.TipoContribuyentes;
import com.origami.sgr.services.ejbs.VentanillaPubEjb;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
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
import org.activiti.engine.runtime.ProcessInstance;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Andy
 */
@Named
@ViewScoped
public class IniciarTramiteVUMB extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(IniciarTramiteVUMB.class.getName());

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;

    @Inject
    private SeqGenMan sec;

    @Inject
    private DocumentsManagedLocal docu;
    
    private List<Servicio> servicios;
    private Servicio servicio;
    private SolicitudServicios solicitudServicios;
    private List<GeDepartamento> departamentos;
    private GeDepartamento departamento;

    private String identificacion;

    private List<ServicioRequisito> requisitosSeleccionados, requisitos;
    private HistoricoTramites tramite;
    private Boolean loading, requiereAutorizacion;
    private List<DepartamentoUsuario> userList;
    private DepartamentoUsuario userSelect;
    private AclUser usuario;
    private Boolean eleccionUsuarios, existeResponsable;
    private List<TipoContribuyentes> tiposContribuyentes;
    private TipoContribuyentes tipoContribuyente;
    private CatEnte solicitante;
    //Para la subida de archivos
    private UploadedFile file;
    private File FILE;

    @PostConstruct()
    public void initView() {
        loadModel();
    }

    public void loadModel() {
        departamentos = em.findAllEntCopy(Querys.getGeDepartamentos);

        servicios = em.findAllEntCopy(Querys.getServiciosActivos);
        loadDataTramite();
    }

    private void loadDataTramite() {
        identificacion = "";
        solicitudServicios = new SolicitudServicios();
        requisitosSeleccionados = new ArrayList<>();
        tramite = new HistoricoTramites();
        tramite.setSolicitante(new CatEnte());
        tramite.getSolicitante().setCiRuc("");
        loading = Boolean.FALSE;
        departamento = new GeDepartamento();
        requiereAutorizacion = Boolean.FALSE;
        userList = new ArrayList<>();
        usuario = new AclUser();

        eleccionUsuarios = Boolean.TRUE;
        existeResponsable = Boolean.FALSE;
        userSelect = new DepartamentoUsuario();

        tipoContribuyente = new TipoContribuyentes();
        //listaRequisitosGlobal = new ArrayList<>(); 
    }

    public void getServicioByDpto() {
        try {
            loading = Boolean.FALSE;
            if (departamento != null && departamento.getId() != null) {
                servicios = em.findAll(Querys.getServiciosDep, new String[]{"departamento"}, new Object[]{departamento.getId()});
            } else {
                userList = new ArrayList<>();
                servicios = new ArrayList<>();
            }
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, "Error: al traer los servicios por departamento", e);
            e.printStackTrace();
            userList = new ArrayList<>();
        }
    }

    public void cargarTipoContribuyentes() {
        tipoContribuyente = null;
        tiposContribuyentes = new ArrayList<>();
        if (servicio != null) {
            if (departamento.getId() == null) {
                departamento = servicio.getDepartamento();
            } else if (!departamento.getId().equals(servicio.getDepartamento().getId())) {
                departamento = servicio.getDepartamento();
            }
            tiposContribuyentes = em.findAll(TipoContribuyentes.class); //cambiar x filtro d los activos
        }
    }

    public void getRequisitosByServicio() {
        if (servicio.getId() != null && tipoContribuyente != null && tipoContribuyente.getId() != null) {
            requisitos = new ArrayList<>();

            initListUsuarios();
            ServicioTipo st = (ServicioTipo) em.find(Querys.getServiciosTipo,
                    new String[]{"servicio", "tc"},
                    new Object[]{servicio.getId(), tipoContribuyente.getId()});
            if (st != null && st.getId() != null) {
                Map<String, Object> paramsReq = new HashMap<>();
                paramsReq.put("servicio", st.getId());
                requisitos = em.findAll(Querys.getRequisitoBySer, new String[]{"servicio"}, new Object[]{st.getId()});
            }
        }
    }

    /*Obtener los usuarios cuando tengan a un responsable según el dpto asignado*/
    public void initListUsuarios() {
        try {
            eleccionUsuarios = Boolean.FALSE;
            userList = new ArrayList<>();
            userSelect = new DepartamentoUsuario();
            List<DepartamentoUsuario> users = em.findAll(Querys.getResponsableDepartamentoUs, new String[]{"departamento"}, new Object[]{departamento.getId()});
            if (!users.isEmpty()) {
                //existeResponsable = Boolean.TRUE;
                userList.addAll(users);
                System.out.println("userList: " + userList.size());
                userSelect = userList.get(0);
                JsfUti.update("dtUsuarios");
            }
            if (Utils.isEmpty(userList)) {
                JsfUti.messageError(null, "", "No existen usuarios al departamento por favor comuníquese con el administrador del sistema");
            }
        } catch (Exception e) {
            System.out.println("//Exception List Usuario " + e.getMessage());
        }
    }

    public void searchBeneficiario(Boolean parameter) {
        System.out.println("tramite.getSolicitante().getIdentificacion() " + tramite.getSolicitante().getCiRuc());
        if (!Utils.isEmptyString(tramite.getSolicitante().getCiRuc())) {

            identificacion = tramite.getSolicitante().getCiRuc();
            Map map = new HashMap<>();
            map.put("ciRuc", identificacion);
            Long count = ((Long) em.findObjectByParameter(Querys.CatEnteCount, map));
            if (count == 1) {
                solicitante = (CatEnte) em.findObjectByParameter(CatEnte.class, map);
            } else {
                solicitante = new CatEnte();
            }
            if (solicitante == null || solicitante.getId() == null) {
                solicitante = reg.buscarGuardarEnteDinardap(identificacion);
            }

            if (solicitante != null && solicitante.getId() != null) {
                tramite.setSolicitante(solicitante);
                PrimeFaces.current().ajax().update("idBeneficiacio");
            } else if (solicitante != null && solicitante.getId() == null) {
//                c = clienteService.create(c);
//                tramite.setSolicitante(c);
                PrimeFaces.current().ajax().update("idBeneficiacio");
            } else {
                ss.instanciarParametros();
                if (identificacion != null && !identificacion.isEmpty()) {
                    ss.agregarParametro("ciRuc_", identificacion);
                }
                showDlg("/resources/dialog/dlglazyente");
            }
        } else {
            ss.instanciarParametros();
            if (identificacion != null && !identificacion.isEmpty()) {
                ss.agregarParametro("ciRuc_", identificacion);
            }
            showDlg("/resources/dialog/dlglazyente");
        }
    }

    public void selectBeneficiario(SelectEvent evt) {
        try {
            tramite.setSolicitante((CatEnte) evt.getObject());
            PrimeFaces.current().ajax().update("@(.ui-panelgrid)");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar beneficiario", e);
        }
    }

    public void showDlg(String urlFacelet) {
        try {
            if (!urlFacelet.startsWith("/")) {
                urlFacelet = "/" + urlFacelet;
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "60%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
//            RequestContext.getCurrentInstance().openDialog(urlFacelet, options, null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void limpiarDatosSolicitante() {
        tramite.setSolicitante(new CatEnte());
        identificacion = "";
    }

    public void actualizarDatosSolicitante() {
        try {
            if (tramite.getSolicitante() != null && tramite.getSolicitante().getId() != null) {
                em.merge(tramite.getSolicitante());
                JsfUti.messageInfo(null, Messages.correcto, "");
            }

        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void initTramite() {
        try {
            if (validarInitTramite()) {

                Boolean ok = false;
                String usuariosAsignados = userSelect.getId() != null ? userSelect.getUsuario().getUsuario() : us.getName_user();
                /*Asignamos el tipo de tramite Ventanilla unica*/
                tramite.setTipoTramite(servicio.getTipoTramite());
                String[] codes = {"usuario", us.getName_user()};
                if (tramite.getTipoTramite().getUserDireccion() != null && tramite.getTipoTramite().getUserDireccion().trim().length() > 0) {
                    // Si solo existe un solo valor envia como variable
                    String[] temp = tramite.getTipoTramite().getUserDireccion().split(":");
                    if (temp.length == 1) {
                        codes[0] = temp[0];
                    } else {
                        codes = temp;
                        codes[1] = userSelect.getUsuario().getUsuario();
                    }
                }
//para resoluciones editado por David Borbor
//hay un caso especial, el tramite pasa directo a resoluciones si la exoneracion es por tercera edad o discapacdad
// caso contrario para a financiero

                tramite.setFechaIngreso(new Date());
                tramite.setFecha(new Date());
                tramite.setNumTramite(sec.getSecuenciaTramite());
                tramite.setNombrePropietario(tramite.getSolicitante().getNombreCompleto());
                tramite.setFechaEntrega(itl.diasEntregaTramite(tramite.getFechaIngreso(), servicio.getDiasRespuesta() != null ? servicio.getDiasRespuesta() : 2));
                tramite = (HistoricoTramites) em.merge(tramite);
                if (tramite != null) {
                    ok = true;
                    crearSolicitudesTramite(usuariosAsignados);
                    iniciarTramiteBPM();
                    reg.guardarObservaciones(tramite, us.getName_user(), "Inicio de Trámite", "Ventanilla Unica");
                }
                if (ok) {
                    if (file != null) {
                        File f = new File(SisVars.rutaTemporales + "/" + file.getFileName());
                        docu.saveDocumentoHabilitante(new FileInputStream(f), file.getFileName(), file.getContentType(), tramite, us.getUserId());
                    }
                    JsfUti.executeJS("PF('continuarDlg').show()");
                    PrimeFaces.current().ajax().update("frmContinuar");
                } else {
                    JsfUti.messageError(null, "Error", "No se pudo generar el trámite");
                    //JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void iniciarTramiteBPM() {
        try {

            HashMap<String, Object> pars = new HashMap<>();

            pars.put("usuario_1", us.getName_user());
            pars.put("usuario_2", us.getName_user());
            pars.put("usuario_3", us.getName_user());
            pars.put("usuario_4", us.getName_user());
            pars.put("usuario_5", us.getName_user());
            pars.put("usuario_6", us.getName_user());
            pars.put("usuario_7", us.getName_user());
            pars.put("usuario_8", us.getName_user());
            pars.put("revision", 1);
            pars.put("tramite", tramite.getNumTramite());
            pars.put("prioridad", 50);
            ProcessInstance p = engine.startProcessByDefinitionKey(tramite.getTipoTramite().getActivitykey(), pars); //PROCESO PARA INICAR EN ACTIVITI
            if (p != null) {
                tramite.setIdProceso(p.getId());
                em.update(tramite);
            }
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Boolean validarInitTramite() {
        if (departamento == null || departamento.getId() == null) {
            JsfUti.messageError(null, "", "Debe escoger el departamento");
            return Boolean.FALSE;
        }
        if (servicio == null || servicio.getId() == null) {
            JsfUti.messageError(null, "", "Debe escoger el servicio");
            return Boolean.FALSE;
        }
        if (tipoContribuyente == null || tipoContribuyente.getId() == null) {
            JsfUti.messageError(null, "", "Debe escoger el tipo de contribuyente");
            return Boolean.FALSE;
        }
        if (tramite.getSolicitante() == null || tramite.getSolicitante().getId() == null) {
            JsfUti.messageError(null, "", "Debe ingresar los datos el solicitante");
            return Boolean.FALSE;
        }
        if (servicio.getDescripcion() == null || servicio.getDescripcion().equals("")) {
            JsfUti.messageError(null, "", "Ingrese una observación");
            return Boolean.FALSE;
        }

        if (Utils.isEmpty(requisitosSeleccionados)) {
            JsfUti.messageError(null, "", "Debe seleccionar los requisitos obligatorios");
            return Boolean.FALSE;
        }
        for (ServicioRequisito req : requisitos) {
            if (!req.getOpcional() && !requisitosSeleccionados.contains(req)) {
                JsfUti.messageError(null, "", "Verifique que todos los requisitos obligatorios estén ingresados");
                return Boolean.FALSE;
            }
        }
//        if (file == null || FILE == null) {
//            JsfUti.messageError(null, "", "Por favor adjunte un documento");
//            return Boolean.FALSE;
//        }

        if (eleccionUsuarios) {
            if (Utils.isEmpty(userList)) {
                JsfUti.messageError(null, "", "No existen usuarios al departamento por favor comuníquese con el administrador del sistema");
                return Boolean.FALSE;
            }
            if (userSelect == null || userSelect.getId() == null) {
                JsfUti.messageError(null, "", "Debe escoger un usuario para el trámite");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private void crearSolicitudesTramite(String usuariosAsignados) {
        SolicitudDepartamento solicitudDepartamento = new SolicitudDepartamento();
        solicitudDepartamento.setEstado(Boolean.TRUE);
        solicitudDepartamento.setDepartamento(departamento);
        //CREANDO LA SOLICITUD SERVICIO
        solicitudDepartamento.setSolicitud(crearSolicitudServicio());
        //CREANDO LOS REQUISITOS QUE ESTAN Y NO EN LA SOLICITUD
        crearRegistroSolicitudRequisitos(solicitudDepartamento.getSolicitud());
        solicitudDepartamento.setResponsables(usuariosAsignados);
        solicitudDepartamento.setFecha(new Date());
        em.persist(solicitudDepartamento);
    }

    private void crearRegistroSolicitudRequisitos(SolicitudServicios solicitudServicios) {
        if (!Utils.isEmpty(requisitosSeleccionados)) {
            for (ServicioRequisito lm : requisitosSeleccionados) {
                System.out.println("solicitud " + solicitudServicios.getId());
                RegistroSolicitudRequisitos rs = new RegistroSolicitudRequisitos(new SolicitudServicios(solicitudServicios.getId()),
                        new ServicioRequisito(lm.getId()), Boolean.FALSE,
                        Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, "", new Date(), us.getName_user());
                em.persist(rs);
            }
        }
    }

    public SolicitudServicios crearSolicitudServicio() {
        solicitudServicios = new SolicitudServicios();
        solicitudServicios.setSolicitudInterna(Boolean.TRUE);
        solicitudServicios.setDescripcion(servicio.getDescripcion());
        solicitudServicios.setFechaCreacion(new Date());
        solicitudServicios.setEstado("A");
        solicitudServicios.setServicio(servicio);
        solicitudServicios.setTramite(tramite);
        solicitudServicios.setSolicitante(tramite.getSolicitante());
        solicitudServicios.setUsuarioIngreso(new AclUser(us.getUserId()));
        solicitudServicios.setTipoContribuyente(tipoContribuyente.getNombre());
        solicitudServicios.setFinalizado(Boolean.FALSE);
        solicitudServicios.setEnObservacion(Boolean.FALSE);
        solicitudServicios = (SolicitudServicios) em.merge(solicitudServicios);
        return solicitudServicios;
    }

    @Override
    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            FILE = Utils.copyFileServer(file, SisVars.rutaTemporales);
            System.out.println("FILE: " + FILE.getCanonicalPath());
            JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUti.messageError(null, null, "Ocurrió un error al subir el archivo");
        }
    }

    public void continuarTarea() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(Boolean.TRUE);
            ss.setNombreReporte("SolicitudInicioTramite");
            ss.setNombreSubCarpeta("administrativo");
            ss.agregarParametro("TRAMITE", tramite.getNumTramite());
            ss.agregarParametro("LOGO_URL", JsfUti.getRealPath("/resources/image/origami.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/origami.png"));
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/origami.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            this.continuar();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<ServicioRequisito> getRequisitosSeleccionados() {
        return requisitosSeleccionados;
    }

    public void setRequisitosSeleccionados(List<ServicioRequisito> requisitosSeleccionados) {
        this.requisitosSeleccionados = requisitosSeleccionados;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Boolean getLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading = loading;
    }

    public Boolean getRequiereAutorizacion() {
        return requiereAutorizacion;
    }

    public void setRequiereAutorizacion(Boolean requiereAutorizacion) {
        this.requiereAutorizacion = requiereAutorizacion;
    }

    public List<DepartamentoUsuario> getUserList() {
        return userList;
    }

    public void setUserList(List<DepartamentoUsuario> userList) {
        this.userList = userList;
    }

    public DepartamentoUsuario getUserSelect() {
        return userSelect;
    }

    public void setUserSelect(DepartamentoUsuario userSelect) {
        this.userSelect = userSelect;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public Boolean getEleccionUsuarios() {
        return eleccionUsuarios;
    }

    public void setEleccionUsuarios(Boolean eleccionUsuarios) {
        this.eleccionUsuarios = eleccionUsuarios;
    }

    public Boolean getExisteResponsable() {
        return existeResponsable;
    }

    public void setExisteResponsable(Boolean existeResponsable) {
        this.existeResponsable = existeResponsable;
    }

    public List<TipoContribuyentes> getTiposContribuyentes() {
        return tiposContribuyentes;
    }

    public void setTiposContribuyentes(List<TipoContribuyentes> tiposContribuyentes) {
        this.tiposContribuyentes = tiposContribuyentes;
    }

    public TipoContribuyentes getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(TipoContribuyentes tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public List<ServicioRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<ServicioRequisito> requisitos) {
        this.requisitos = requisitos;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public File getFILE() {
        return FILE;
    }

    public void setFILE(File FILE) {
        this.FILE = FILE;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
