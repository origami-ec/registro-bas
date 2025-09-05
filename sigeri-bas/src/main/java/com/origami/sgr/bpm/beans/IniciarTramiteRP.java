/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.CatParroquia;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.entities.RegpDetalleExoneracion;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpExoneracion;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpLiquidacionExoneracion;
import com.origami.sgr.entities.RegpObservacionesIngreso;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RegpTareasDinardapLazy;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import com.origami.sql.ConsultasSQLService;

/**
 * Controlador para la pagina iniciarTramiteRp.xhtml, con los metodos que se
 * ejecutan en cada uno de componentes que tengan un ajax asociado.
 *
 * @author Origami
 */
@Named
@ViewScoped
public class IniciarTramiteRP extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(IniciarTramiteRP.class.getName());

    @Inject
    protected ConsultasSQLService sam;

    protected CatEnte solicitante;
    protected CatEnte beneficiario;
    protected String cedula;
    protected RegpLiquidacionExoneracion exoneracion;
    protected List<RegpLiquidacionExoneracion> exoneraciones = new ArrayList<>();
    protected RegpLiquidacion liquidacion;
    protected HistoricoTramites ht;
    protected List<RegpIntervinientes> listInterv;
    protected List<RegpLiquidacionDetalles> actosPorPagar;
    protected List<RegpDetalleExoneracion> detalleExoneraciones;
    protected LazyModel<RegActo> actos;
    protected RegActo acto;
    protected CtlgCatalogo catalogo;
    protected CtlgItem usoDocumento;
    protected CtlgItem nuevoUsoDoc;
    protected RegpTareasDinardapLazy tareasLazy;
    protected RegpTareasDinardap regpTareasDinardap;
    protected String nombre = "";
    protected String obsAdicional = "";
    protected BigDecimal subTotal;
    protected BigDecimal subTotalDesc;
    protected BigDecimal totalPagar;
    protected BigDecimal descPorLey;
    protected BigDecimal recargoAplicado;
    protected BigDecimal descLimitCobro;
    protected BigDecimal gastosGenerales;
    protected BigDecimal avaluo = BigDecimal.ZERO;
    protected BigDecimal cuantia = BigDecimal.ZERO;
    protected BigDecimal porcPago = BigDecimal.ONE;
    protected Integer indice;
    protected Boolean propiedad = null;
    protected Boolean block = false;
    protected Boolean diferencia = false;
    protected Boolean solvencia = true;
    //protected ContenidoReportes contenido;
    protected RegRegistrador registrador;
    protected RegpObservacionesIngreso observacion;
    protected RegEnteJudiciales enju = new RegEnteJudiciales();
    protected RegpLiquidacionDetalles rld = new RegpLiquidacionDetalles();
    protected boolean editar = false;
    protected boolean certificado = false;
    protected int indiceActo = 0;
    protected RegpEstadoLiquidacion estadoLiquidacion, incompleta;
    protected RegpEstadoPago estadoPago;
    protected RegpIntervinientes interviniente;

    //VARIABLES PARA LA REFORMA DE TABLA DE ARANCELES
    protected Integer tipocalculo = 0;
    protected BigDecimal adicional = BigDecimal.ZERO;
    private Boolean beneficiarioEsSolicitante, agregaBeneficiario, agregaSolicitante;
    private Integer indexBeneficiario, indexSolicitante;
    private Integer aniosDiferencia;

    //FICHA INICIO
    private LazyModel fichasRegistrales;
    private Long numFicha;
    private Integer pesoAcum = 1;
    protected List<AclUser> users = new ArrayList<>();
    private AclUser userInscriptor;
    private StringBuffer coincidencias;
    protected Valores limiteActo;
    protected Valores salarioBasico;
    protected Valores limiteFactura;
    protected Valores gastosAdicionales;
    protected Valores adicionalCertificados;
    protected SimpleDateFormat sdf;

    //FICHA FIN
    /**
     * Inicializador de las variables en el momento que carga la vista.
     */
    @PostConstruct
    protected void iniView() {
        try {
            actos = new LazyModel(RegActo.class, "nombre", "ASC");
            actos.addFilter("tipoCobro", new RegTipoCobroActo(2L)); //ACTOS CON ARANCELES
            beneficiarioEsSolicitante = Boolean.FALSE;
            agregaBeneficiario = Boolean.FALSE;
            agregaSolicitante = Boolean.FALSE;
            solicitante = new CatEnte();
            beneficiario = new CatEnte();
            liquidacion = new RegpLiquidacion();
            ht = new HistoricoTramites();
            listInterv = new ArrayList<>();
            detalleExoneraciones = new ArrayList<>();
            actosPorPagar = new ArrayList<>();
            //actos = manager.findAllObjectOrder(RegActo.class, new String[]{"nombre"}, Boolean.TRUE);
            acto = new RegActo();
            subTotal = BigDecimal.ZERO;
            subTotalDesc = BigDecimal.ZERO;
            recargoAplicado = BigDecimal.ZERO;
            totalPagar = BigDecimal.ZERO;
            descPorLey = BigDecimal.ZERO;
            descLimitCobro = BigDecimal.ZERO;
            gastosGenerales = BigDecimal.ZERO;
            sdf = new SimpleDateFormat("dd-MM-yyyy");

            map = new HashMap();
            map.put("nombre", Constantes.usosDocumento);
            catalogo = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);

            map = new HashMap();
            map.put("code", Constantes.limiteValorContrato);
            limiteActo = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("code", Constantes.limiteFactura);
            limiteFactura = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("code", Constantes.salarioBasicoUnificado);
            salarioBasico = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("code", Constantes.gastosGenerales);
            gastosAdicionales = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("code", Constantes.adicionalCertificados);
            adicionalCertificados = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);

            usoDocumento = manager.find(CtlgItem.class, 4L);

            estadoLiquidacion = manager.find(RegpEstadoLiquidacion.class, 1L); //ESTADO LIQUIDACION ACEPTADA
            incompleta = manager.find(RegpEstadoLiquidacion.class, 4L); //ESTADO LIQUIDACION INCOMPLETA
            estadoPago = manager.find(RegpEstadoPago.class, 1L);
            if (ss.getParametros() != null) {
                if (ss.getParametros().get("idObservacion") != null) {
                    Long id = (Long) ss.getParametros().get("idObservacion");
                    observacion = manager.find(RegpObservacionesIngreso.class, id);
                    if (observacion != null) {
                        solicitante = observacion.getEnte();
                        liquidacion.setObservacion(observacion);
                    }
                    ss.instanciarParametros();
                }
            }
            numFicha = 0L;
            liquidacion.setPesoTramite(1);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /**
     * Trae el catalogo del uso de ducumento
     *
     * @return
     */
    public List<CtlgItem> getUsosDocumentos() {
        return manager.findAllEntCopy(Querys.getCtlgItemListUsosDocs);
    }

    public void buscarNotariaAbrev() {
        try {
            if (enju.getAbreviatura() != null) {
                map = new HashMap();
                map.put("abrev", enju.getAbreviatura());
                enju = (RegEnteJudiciales) manager.findObjectByParameter(Querys.getRegEnteJudicialByAbrev, map);
                if (enju != null) {
                    liquidacion.setEnteJudicial(enju);
                } else {
                    enju = new RegEnteJudiciales();
                    JsfUti.messageInfo(null, Messages.sinCoincidencias, "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /**
     * (SIN USAR EN iniciarTramiteRp) Se ejecuta una vez que el dialogo
     * framework invocado se cierra y devuelve el objecto RegEnteJudiciales
     * seleccionado
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectObjectJudicial(SelectEvent event) {
        enju = (RegEnteJudiciales) event.getObject();
        liquidacion.setEnteJudicial(enju);
    }

    /**
     * Busca los datos del solicitante si este esta registrado mas de una ves
     * muestra el dialog Framewrok con el listado de entes encontrados, caso
     * contrario obtiene el ente encontrado.
     */
    public void buscar() {
        if (solicitante.getCiRuc() != null || solicitante.getCiRuc().isEmpty()) {
            if (!solicitante.getCiRuc().isEmpty()) {
                cedula = solicitante.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    solicitante = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                    //solicitante = this.validarCIU(solicitante);
                } else {
                    solicitante = new CatEnte();
                }
                /*if (solicitante == null || solicitante.getId() == null) {
                    solicitante = reg.buscarGuardarEnteDinardap(cedula);
                }*/
                //buscarActosIngresados(cedula);
            }
            if (solicitante == null) {
                solicitante = new CatEnte();
            }
            if (solicitante.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            } else {
                //this.buscarActosIngresados(cedula);
                JsfUti.update("mainForm:accPanelRP:pnlSolicitante");
            }
        }
    }

    /**
     * Busca todos los trmites ingresados en el mismo numero de cedula.
     */
    /*private void buscarActosIngresados(String identificacion) {
        try {
            if (acto != null) {
                if (!acto.getSolvencia()) {
                    advertencias = itl.buscarActosIngresadosCedula(identificacion, acto);
                }
            } else {
                advertencias = itl.buscarActosIngresadosCedula(identificacion);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Buscar actos", e);
        }
    }*/
    /**
     * Busca los datos del solicitante si este esta registrado mas de una ves
     * muestra el dialog Framewrok con el listado de entes encontrados, caso
     * contrario obtiene el ente encontrado.
     */
    public void buscarBeneficiario() {
        if (beneficiario.getCiRuc() != null || beneficiario.getCiRuc().isEmpty()) {
            if (!beneficiario.getCiRuc().isEmpty()) {
                cedula = beneficiario.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    beneficiario = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                } else {
                    beneficiario = new CatEnte();
                }
            }
            if (beneficiario == null) {
                beneficiario = new CatEnte();
            }
            if (beneficiario.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            } else {
                liquidacion.setCorreoTramite(beneficiario.getCorreo1());
                JsfUti.update("mainForm:accPanelRP:pnlBeneficiario");
                JsfUti.update("mainForm:accPanelRP:pnlSolicitante");
            }
        }
    }

    public void limpiarDatosSolicitante() {
        solicitante = new CatEnte();
    }

    public void limpiarDatosBeneficiario() {
        beneficiario = new CatEnte();
    }

    /*public CatEnte validarCIU(CatEnte temp) {
        try {
            if (temp.getCodCiu() == null || temp.getCodCiu().compareTo(BigInteger.ONE) < 0) {
                BigInteger ciu = sam.validarCodigoCIU(temp.getCiRuc());
                //System.out.println("// ciu1: " + ciu);
                if (ciu.compareTo(BigInteger.ZERO) == 0) {
                    ciu = sam.registrarCodigoCIU(temp);
                }
                //System.out.println("// ciu2: " + ciu);
                if (ciu.compareTo(BigInteger.ZERO) != 0) {
                    temp.setCodCiu(ciu);
                    manager.merge(temp);
                } else {
                    JsfUti.messageWarning(null, "No se pudo registrar a la persona en el SAM.", "");
                }
            }
            return temp;
        } catch (SQLException e) {
            System.out.println(e);
            return temp;
        }
    }*/
    /**
     * Se ejecuta una vez que el dialog framework invocado se cierra y devuelve
     * el objecto CatEnte seleccionado y setea el ente como solicitante
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectSolicitante(SelectEvent event) {
        solicitante = (CatEnte) event.getObject();
        //solicitante = this.validarCIU(solicitante);
    }

    /**
     * (SIN USAR EN iniciarTramiteRp) Se ejecuta una vez que el dialogo
     * framework invocado se cierra y devuelve el objecto RegEnteJudiciales
     * seleccionado
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectNotaria(SelectEvent event) {
        enju = (RegEnteJudiciales) event.getObject();
        JsfUti.update("mainForm:pnlUnidadJudicial");
    }

    /**
     * Se ejecuta una vez que el dialog framework invocado se cierra y devuelve
     * el objecto CatEnte seleccionado, verifica si el solicitante es igual al
     * beneficiario para hacer validaciones posteriores, ademas lo setea como
     * beneficiario.
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectBeneficiario(SelectEvent event) {
        beneficiario = (CatEnte) event.getObject();
        //beneficiario = this.validarCIU(beneficiario);
        if (beneficiario.equals(solicitante)) {
            beneficiarioEsSolicitante = Boolean.TRUE;
        } else {
            beneficiarioEsSolicitante = Boolean.FALSE;
        }
        liquidacion.setCorreoTramite(beneficiario.getCorreo1());
    }

    /**
     * Cuando la variable {@code agregaBeneficiario} agrega al Beneficiario de
     * la proforma lo pone en la lista de los intervinientes del acto
     * seleccionado, caso contrario lo quita de la lista.
     */
    public void agregarBeneficiarioInterviniente() {
        if (agregaBeneficiario) {
            if (beneficiario != null && beneficiario.getId() != null) {
                indexBeneficiario = agregarIntereniente(beneficiario);
                if (indexBeneficiario == null) {
                    agregaBeneficiario = Boolean.FALSE;
                }
            } else {
                JsfUti.messageWarning(null, "Debe Agregar un Beneficiario", "");
                agregaBeneficiario = Boolean.FALSE;
            }
        } else {
            int postDelete = -1;
            for (int i = 0; i < listInterv.size(); i++) {
                if (listInterv.get(i).getEnte().getCiRuc().equalsIgnoreCase(beneficiario.getCiRuc())) {
                    postDelete = i;
                    break;
                }
            }
            if (postDelete != -1) {
                listInterv.remove(postDelete);
            }
//            if (indexBeneficiario != null) {
//                listInterv.remove(indexBeneficiario.intValue());
//            }
        }
    }

    /**
     * Cuando la variable {@code agregaSolicitante} agrega al Solicitante de la
     * proforma lo pone en la lista de los intervinientes del acto seleccionado,
     * caso contrario lo quita de la lista.
     */
    public void agregarSolicitanteInterviniente() {
        if (agregaSolicitante) {
            if (solicitante != null && solicitante.getId() != null) {
                indexSolicitante = agregarIntereniente(solicitante);
                if (indexSolicitante == null) {
                    agregaSolicitante = Boolean.FALSE;
                }
            } else {
                JsfUti.messageWarning(null, "Debe Agregar un Solicitante", "");
                agregaSolicitante = Boolean.FALSE;
            }
        } else {
            int postDelete = -1;
            for (int i = 0; i < listInterv.size(); i++) {
                if (listInterv.get(i).getEnte().getCiRuc().equalsIgnoreCase(solicitante.getCiRuc())) {
                    postDelete = i;
                    break;
                }
            }
            if (postDelete != -1) {
                listInterv.remove(postDelete);
            }
//            if (indexSolicitante != null) {
//                listInterv.remove(indexSolicitante.intValue());
//            }
        }
    }

    /**
     * Se ejecuta una vez que el dialog framework invocado se cierra y devuelve
     * el objecto CatEnte seleccionado, y lo agrega como interviniente del acto
     * seleccionado.
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectInterv(SelectEvent event) {
        CatEnte ente = (CatEnte) event.getObject();
        //System.out.println("el ente es -- " + ente.getId());
        agregarIntereniente(ente);
    }

    /**
     * Agrega el ente a la lista de intervinientes del acto seleccionado.
     *
     * @param e {@link CatEnte} a agregar a la lista deintervinientes del acto.
     * @return indice del interviniente agregado
     */
    private Integer agregarIntereniente(CatEnte e) {
        if (this.validaInterviniente(e.getCiRuc())) {
            RegpIntervinientes in = new RegpIntervinientes();
            in.setEnte(e);
            listInterv.add(in);
            //buscarActosIngresados(e.getCiRuc());
            return listInterv.size() - 1;
        } else {
            JsfUti.messageWarning(null, "Ya esta ingresado el interviniente.", "");
            return null;
        }
    }

    /**
     * Valida que el numero de identificacion no este en la lista de
     * intervinientes
     *
     * @param cedula Numero de identificacion a verificar si esta en la lista de
     * intervinientes.
     * @return true si no esta en la lista, caso contrario false.
     */
    public boolean validaInterviniente(String cedula) {

        return listInterv.stream().noneMatch((in) -> (in.getEnte().getCiRuc().equalsIgnoreCase(cedula)));
    }

    /**
     *
     * (SIN USAR EN iniciarTramiteRp) Busca todas las coincidencia que haya con
     * el nombre dado, ademas reemplaza los espacios en blanco con el comodin de
     * % para mayor coincidencia
     *
     * @param query Nombre del acto buscar
     * @return Listado de Actos encontrado.
     */
    public List<RegPapel> complete(String query) {
        List<RegPapel> results = manager.findMax(Querys.getRegCatPapelByPapel, new String[]{"papel"}, new Object[]{query.toLowerCase().trim().replaceAll(" ", "%") + "%"}, 10);
        return results;
    }

    /**
     * Se ejecuta una vez que el dialog framework invocado se cierra y devuelve
     * el objecto RegPapel seleccionado, y lo agrega como papel del
     * interviniente seleccionado.
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectObjectPapel(SelectEvent event) {
        RegPapel pa = (RegPapel) event.getObject();
        interviniente.setPapel(pa);
    }

    /**
     * Muestra un dialog framework de la paguina enviada como parametro
     *
     * @param urlFacelet Url de la paguina a embeber como dialog fremework
     * @param indice Indice del interviniente a editar
     */
    public void showDlgPapel(String urlFacelet, int indice) {
        interviniente = listInterv.get(indice);
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

    /**
     * Remueva el interviniente que tenga el indice pasado como parametro
     *
     * @param index Indice del interviniente a remover de la lista
     */
    public void eliminarInterviniente(int index) {
        try {
            RegpIntervinientes in = listInterv.get(index);
            listInterv.remove(index);
            if (in.getId() != null) {
                manager.delete(in);
            }
            /*if (in.getEnte().getCiRuc().equalsIgnoreCase(beneficiario.getCiRuc())) {
                agregaBeneficiario = Boolean.FALSE;
            }
            if (in.getEnte().getCiRuc().equalsIgnoreCase(solicitante.getCiRuc())) {
                agregaSolicitante = Boolean.FALSE;
            }*/
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void quitarDescuento(int index) {
        RegpIntervinientes in = listInterv.get(index);
        in.setExoneracion(null);
    }

    /**
     * (SIN USAR EN iniciarTramiteRp) Busca todas las coincidencia que haya con
     * el nombre dado, ademas reemplaza los espacios en blanco con el comodin de
     * % para mayor coincidencia
     */
    public void buscarActos() {
        try {
            /*if (nombre != null) {
                actos = manager.findAll(Querys.getActobyNombre, new String[]{"nombre"}, new Object[]{"%" + nombre.toLowerCase().trim().replaceAll(" ", "%") + "%"});
                if (actos == null) {
                    actos = new ArrayList<>();
                }
            } else {
                actos = manager.findAll(Querys.getActoOrdered);
            }*/
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void onRowSelect(RegActo regacto) {
        try {
            acto = regacto;
            certificado = false;
            if (acto.getArancel() == null) {
                JsfUti.messageWarning(null, "El acto seleccionado no tiene arancel asociado.", "");
                return;
            }
            if (propiedad == null) {
                propiedad = acto.getTipoActo().getId() == 1L;
            } else {
                Boolean temp = acto.getTipoActo().getId() == 1L;
                if (!Objects.equals(temp, propiedad)) {
                    JsfUti.messageWarning(null, "No se pueden seleccionar actos de propiedad con actos mercantil.", "");
                    return;
                }
            }
            aniosDiferencia = 0;
            editar = false;
            //certificado = false;
            //this.verTipoCalculo();
            rld = new RegpLiquidacionDetalles();
            rld.setCantidad(1);
            //rld.setCantidadIntervinientes(44);
            rld.setActo(acto);
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setRecargo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setBase(BigDecimal.ZERO);
            rld.setReingreso(liquidacion.getReingreso());
            rld.setExoneracion(new RegpExoneracion());
            JsfUti.update("formCuantia");
            JsfUti.executeJS("PF('dlgCuantia').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /**
     * Calcula los anios excedentes que tiene con referencia a la ultima
     * transferencia, si es mayor a 15 calcula el excedente de anios.
     */
    public void aniosCalculo() {
        aniosDiferencia = 0;
        if (rld.getAnioUltimaTrasnferencia() != null && rld.getAnioAntecedenteSolicitado() != null) {
            aniosDiferencia = rld.getAnioUltimaTrasnferencia() - rld.getAnioAntecedenteSolicitado();
            if (aniosDiferencia < 0) {
                JsfUti.messageWarning(null, "El año de la ultima transferencia no puede ser menor al del movimiento solicitado.", "");
                aniosDiferencia = 0;
                return;
            }
            if (aniosDiferencia - 15 >= 0) {
                aniosDiferencia = aniosDiferencia - 15;
            } else {
                aniosDiferencia = 0;
            }
        }

    }

    public void verTipoCalculo() {
        tipocalculo = 0;
        if (acto.getTipoCobro() != null) {
            tipocalculo = acto.getTipoCobro().getId().intValue();
        }
    }

    public void showDlgEditActo(int indice) {
        try {
            certificado = false;
            editar = true;
            indiceActo = indice;
            rld = actosPorPagar.remove(indice);

            /*if (rld.getActo().getTipoActo() != null) {
                String name = rld.getActo().getTipoActo().getNombre().toUpperCase();
                if (name.contains("HISTORIADO")) {
                    certificado = true;
                    aniosCalculo();
                }
            }*/
            acto = rld.getActo();
            avaluo = rld.getAvaluo();
            cuantia = rld.getCuantia();
            //this.verTipoCalculo();
            listInterv = (List<RegpIntervinientes>) rld.getRegpIntervinientesCollection();

            if (listInterv == null) {
                map = new HashMap();
                map.put("liquidacion", rld);
                listInterv = manager.findObjectByParameterList(RegpIntervinientes.class, map);
                if (listInterv == null) {
                    listInterv = new ArrayList<>();
                }
            }
            /*agregaBeneficiario = agregaSolicitante = Boolean.FALSE;
            for (RegpIntervinientes in : listInterv) {
                if (solicitante != null) {
                    if (solicitante.getId() != null) {
                        if (solicitante.getCiRuc().equalsIgnoreCase(in.getEnte().getCiRuc())) {
                            agregaSolicitante = Boolean.TRUE;
                        }
                    }
                }
                if (beneficiario != null) {
                    if (beneficiario.getId() != null) {
                        if (beneficiario.getCiRuc().equalsIgnoreCase(in.getEnte().getCiRuc())) {
                            agregaBeneficiario = Boolean.TRUE;
                        }
                    }
                }

            }
            if (solicitante != null && beneficiario != null) {
                if (solicitante.getId() != null && beneficiario.getId() != null) {
                    if (beneficiario.getCiRuc().equalsIgnoreCase(solicitante.getCiRuc())) {
                        if (agregaBeneficiario && agregaSolicitante) {
                            agregaBeneficiario = Boolean.FALSE;
                        }
                    }
                }
            }*/
            JsfUti.update("formEditCuantia");
            JsfUti.executeJS("PF('dglEditCuantia').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageWarning(null, "ERROR DE APLICACION.", "");
        }
    }

    public void agregarActo() {
        try {
            if (this.validaPapeles()) {
                if (acto.getArancel() != null) {
                    switch (acto.getArancel().getCodigo()) {
                        case 1:
                            this.calculonormal();
                            break;
                        case 2: //REBAJA DEL 50% DEL ARANCEL
                            this.calculoArancel(new BigDecimal("0.5"));
                            break;
                        case 3: //REBAJA DEL 30% DEL ARANCEL
                            this.calculoArancel(new BigDecimal("0.3"));
                            break;
                    }
                    this.setearvalores();
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void calculonormal() {
        if (acto.getArancel().getValor().compareTo(new BigDecimal(-1)) == 0) {
            if (this.validaCuantiaAvaluo()) {
                rld.setAvaluo(avaluo);
                rld.setCuantia(cuantia);
                if (avaluo.compareTo(cuantia) > 0) {
                    if (this.propiedad) {
                        //rld.setValorUnitario(itl.calculoCuantia(avaluo, salarioBasico.getValorNumeric()));
                        //rld.setValorUnitario(itl.calculoCuantia(avaluo)); //Anterior
                        rld.setValorUnitario(itl.calculoCuantiaSTD(avaluo)); //Nuevo
                    } else {
                        rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo));
                    }
                } else {
                    if (this.propiedad) {
                        //rld.setValorUnitario(itl.calculoCuantia(cuantia, salarioBasico.getValorNumeric()));
                        //rld.setValorUnitario(itl.calculoCuantia(cuantia)); //Anterior
                        rld.setValorUnitario(itl.calculoCuantiaSTD(cuantia)); //Nuevo
                    } else {
                        rld.setValorUnitario(itl.calculoCuantiaDeterminada(cuantia));
                    }
                }
                this.calculoContrato(gastosAdicionales.getValorNumeric(), new BigDecimal("0.00"));
            }
        } else {
            if (avaluo != null) {
                rld.setAvaluo(avaluo);
            }
            if (cuantia != null) {
                rld.setCuantia(cuantia);
            }
            rld.setValorUnitario(acto.getArancel().getValor());
            if (rld.getAplicaDescuento()) { // CERTIFICADOS CON AMPLIACION
                rld.setValorUnitario(rld.getValorUnitario().subtract(new BigDecimal("7.00")));
            }
            this.calculoContrato(new BigDecimal("0.00"), new BigDecimal("0.00"));
        }
    }

    public void calculoMediaCuantia() {
        if (this.validaCuantiaAvaluo()) {
            rld.setAvaluo(avaluo);
            rld.setCuantia(cuantia);
            if (avaluo.compareTo(cuantia) > 0) {
                if (this.propiedad) {
                    //rld.setValorUnitario(itl.calculoCuantia(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP), salarioBasico.getValorNumeric()));
                    //rld.setValorUnitario(itl.calculoCuantia(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                    rld.setValorUnitario(itl.calculoCuantiaSTD(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                }
            } else {
                if (this.propiedad) {
                    //rld.setValorUnitario(itl.calculoCuantia(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP), salarioBasico.getValorNumeric()));
                    //rld.setValorUnitario(itl.calculoCuantia(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                    rld.setValorUnitario(itl.calculoCuantiaSTD(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                }
            }
            this.calculoContrato(gastosAdicionales.getValorNumeric(), new BigDecimal("0.00"));
        }
    }

    public void calculoArancel(BigDecimal descuento) {
        if (this.validaCuantiaAvaluo()) {
            rld.setAvaluo(avaluo);
            rld.setCuantia(cuantia);
            if (avaluo.compareTo(cuantia) > 0) {
                if (this.propiedad) {
                    rld.setValorUnitario(itl.calculoCuantiaSTD(avaluo));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo));
                }
            } else {
                if (this.propiedad) {
                    rld.setValorUnitario(itl.calculoCuantiaSTD(cuantia));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(cuantia));
                }
            }
            //BigDecimal descuento = rld.getValorUnitario().multiply(factor);
            //rld.setValorUnitario(Utils.bigdecimalTo2Decimals(rld.getValorUnitario().subtract(descuento)));
            this.calculoContrato(gastosAdicionales.getValorNumeric(), descuento);
        }
    }

    public void setearvalores() {
        if (avaluo != null && cuantia != null) {
            if (avaluo.compareTo(cuantia) >= 0) {
                rld.setBase(avaluo);
            } else {
                rld.setBase(cuantia);
            }
        }
        if (avaluo == null && cuantia != null) {
            rld.setBase(cuantia);
        }
        if (avaluo != null && cuantia == null) {
            rld.setBase(avaluo);
        }
    }

    public boolean validarTramiteIngresado(RegpLiquidacion liquid) {
        try {
            if (liquid.getNumInscripcion() == null || liquid.getAnioInscripcion() == null) {
                JsfUti.messageWarning(null, "", "Debe ingresar el numero de inscripcion y el año de inscripcion.");
                return false;
            }
            StringBuffer result = this.itl.validarInscripcion(liquid);
            if (result != null) {
                JsfUti.messageWarning(null, "", result.toString());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean validaPapeles() {
        indice = 0;
        porcPago = BigDecimal.ZERO;
        if (rld.getCantidad() == null || rld.getCantidad() == 0) {
            JsfUti.messageWarning(null, "La cantidad no puede ser 0.", "");
            return false;
        }
        if (listInterv.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el/los intervinientes del contrato.", "");
            return false;
        }
        rld.setCantidadIntervinientes(listInterv.size());
        /*if (acto.getCodigoAnterior() != null && acto.getCodigoAnterior() > 0) {
            rld.setCantidadIntervinientes(acto.getCodigoAnterior()); // SE GUARDA EL CODIGO DEL RUBRO DEL SAM
        }*/
        for (RegpIntervinientes rei : listInterv) {
            if (!acto.getSolvencia() && rei.getPapel() == null) {
                JsfUti.messageWarning(null, "Debe ingresar el papel de el/los intervinientes del contrato.", "");
                return false;
            }
            /*if (rei.getExoneracion() != null) {
                indice++;
                porcPago = porcPago.add(rei.getExoneracion().getValor());
            }*/
        }
        /*if (indice > 0) {
            porcPago = porcPago.divide(new BigDecimal(indice), 2, RoundingMode.HALF_UP);
        }*/
        return true;
    }

    public Boolean validaCuantiaAvaluo() {
        Boolean flag = false;
        if (avaluo == null) {
            avaluo = BigDecimal.ZERO;
        }
        if (cuantia == null) {
            cuantia = BigDecimal.ZERO;
        }
        if (avaluo.compareTo(BigDecimal.ZERO) > 0 || cuantia.compareTo(BigDecimal.ZERO) > 0) {
            flag = true;
        } else {
            JsfUti.messageWarning(null, "El valor debe del avaluo o la cuantia debe ser mayor a 0.", "");
        }
        return flag;
    }

    public void calculoContrato() {
        if (rld.getReingreso()) {
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
        } else {
            if (propiedad) {
                if (rld.getValorUnitario().compareTo(limiteActo.getValorNumeric()) > 0) {
                    rld.setValorUnitario(limiteActo.getValorNumeric());
                    rld.setSubtotal(limiteActo.getValorNumeric());
                    rld.setRecargo(gastosAdicionales.getValorNumeric());
                } else {
                    rld.setRecargo(gastosAdicionales.getValorNumeric());
                    rld.setSubtotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
                    rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
                }
            } else {
                if (rld.getValorUnitario().compareTo(limiteFactura.getValorNumeric()) > 0) {
                    rld.setValorUnitario(limiteFactura.getValorNumeric());
                    rld.setSubtotal(limiteFactura.getValorNumeric());
                    rld.setRecargo(gastosAdicionales.getValorNumeric());
                } else {
                    rld.setRecargo(gastosAdicionales.getValorNumeric());
                    rld.setSubtotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
                    rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
                }
            }
        }
        BigDecimal descontar = BigDecimal.ZERO;
        rld.setDescuento(descontar.setScale(2, RoundingMode.HALF_UP));
        rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).subtract(rld.getDescuento()).setScale(2, RoundingMode.HALF_UP));
        List<RegpIntervinientes> temp = (List<RegpIntervinientes>) EntityBeanCopy.clone(listInterv);
        rld.setRegpIntervinientesCollection(temp);
        if (editar) {
            actosPorPagar.add(indiceActo, rld);
        } else {
            if (rld.getFechaIngreso() == null) {
                rld.setFechaIngreso(new Date());
            }
            actosPorPagar.add(rld);
        }

        this.calculoTotalPagar();
        JsfUti.update("mainForm:accPanelRP:pnlContratos");
        JsfUti.executeJS("PF('dlgCuantia').hide();");
        JsfUti.executeJS("PF('dglEditCuantia').hide();");
    }

    public void calculoContrato(BigDecimal recargo, BigDecimal descuento) {
        if (rld.getReingreso()) {
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
        } else {
            if (rld.getValorUnitario().compareTo(limiteActo.getValorNumeric()) > 0) {
                rld.setValorUnitario(limiteActo.getValorNumeric());
            }

            descuento = rld.getValorUnitario().multiply(descuento);
            rld.setValorUnitario(Utils.bigdecimalTo2Decimals(rld.getValorUnitario().subtract(descuento)));
            rld.setSubtotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));

            if (acto.getSolvencia()) {
                switch (acto.getTransaccion()) {
                    case "CV":
                        rld.setSubtotal(rld.getSubtotal().add(adicionalCertificados.getValorNumeric().multiply(new BigDecimal(rld.getNumPredio()))));
                        break;
                    case "CP":
                        rld.setSubtotal(rld.getSubtotal().add(adicionalCertificados.getValorNumeric().multiply(new BigDecimal(rld.getCantidadIntervinientes()))));
                        break;
                }
            }

            if (rld.getExoneracion().getId() != null) {
                rld.setDescuento(rld.getSubtotal().multiply(rld.getExoneracion().getValor()));
                rld.setDescuento(rld.getDescuento().setScale(2, RoundingMode.HALF_UP));
            } else {
                rld.setDescuento(new BigDecimal("0.00"));
            }

            rld.setSubtotal(rld.getSubtotal().subtract(rld.getDescuento()));
            rld.setRecargo(rld.getSubtotal().multiply(recargo).setScale(2, RoundingMode.HALF_UP));
            rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
        }

        List<RegpIntervinientes> temp = (List<RegpIntervinientes>) EntityBeanCopy.clone(listInterv);
        rld.setRegpIntervinientesCollection(temp);
        if (editar) {
            actosPorPagar.add(indiceActo, rld);
        } else {
            if (rld.getFechaIngreso() == null) {
                rld.setFechaIngreso(new Date());
            }
            actosPorPagar.add(rld);
        }

        this.calculoTotalPagar();
        JsfUti.update("mainForm:accPanelRP:pnlContratos");
        JsfUti.executeJS("PF('dlgCuantia').hide();");
        JsfUti.executeJS("PF('dglEditCuantia').hide();");
    }

    public void calculoTotalPagar() {
        totalPagar = BigDecimal.ZERO;
        descPorLey = BigDecimal.ZERO;
        recargoAplicado = BigDecimal.ZERO;
        subTotal = BigDecimal.ZERO;
        subTotalDesc = BigDecimal.ZERO;
        descLimitCobro = BigDecimal.ZERO;
        actosPorPagar.stream().map((det) -> {
            subTotal = subTotal.add(det.getSubtotal());
            return det;
        }).map((det) -> {
            descPorLey = descPorLey.add(det.getDescuento());
            recargoAplicado = recargoAplicado.add(det.getRecargo());
            return det;
        }).forEachOrdered((det) -> {
            subTotalDesc = subTotalDesc.add(det.getValorTotal());
        });
        gastosGenerales = subTotalDesc.multiply(adicional).setScale(2, RoundingMode.HALF_UP);
        //totalPagar = subTotal.subtract(descPorLey).subtract(gastosGenerales).add(recargoAplicado);
        totalPagar = subTotal.add(recargoAplicado);
    }

    public void eliminarDetalle(int rowIndex) {
        try {
            RegpLiquidacionDetalles de = actosPorPagar.remove(rowIndex);
            if (de.getId() != null) {
                manager.delete(de);
            }
            this.calculoTotalPagar();
            JsfUti.update("mainForm:accPanelRP:pnlContratos");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgUsoDoc() {
        nuevoUsoDoc = new CtlgItem();
        JsfUti.update("formUsoDoc");
        JsfUti.executeJS("PF('usoDocumento').show();");
    }

    public void showDlgSolicitudes(Boolean esJuridico) {
        tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, esJuridico);
        if (!esJuridico) {
            JsfUti.update("frmSolicitudes");
            JsfUti.executeJS("PF('dglSolicitudes').show();");
        } else {
            JsfUti.update("frmSolicitudesJuridico");
            JsfUti.executeJS("PF('dglSolicitudesJuridico').show();");
        }
    }

    public void seleccionarSolicitud(RegpTareasDinardap rtt, Boolean esJuridico) {
        this.regpTareasDinardap = rtt;
        if (!esJuridico) {
            liquidacion.setOficioMemoReferencia(regpTareasDinardap.getNumeroSolicitud());
            JsfUti.update("mainForm:accPanelRP:solicitud");
            JsfUti.executeJS("PF('dglSolicitudes').hide();");
        } else {
            liquidacion.setEscritJuicProvResolucion(regpTareasDinardap.getNumeroSolicitud());
            JsfUti.update("mainForm:accPanelRP:numero_juicio");
            JsfUti.executeJS("PF('dglSolicitudesJuridico').hide();");
        }
    }

    public void showDlgEditUsoDoc() {
        if (usoDocumento != null) {
            nuevoUsoDoc = usoDocumento;
            JsfUti.update("formUsoDoc");
            JsfUti.executeJS("PF('usoDocumento').show();");
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar el elemento para editar.", "");
        }
    }

    public void guardarUsoDoc() {
        try {
            if (nuevoUsoDoc.getValor() == null || nuevoUsoDoc.getValor().isEmpty()) {
                JsfUti.messageWarning(null, "El campo nombre esta vacio.", "");
            } else {
                nuevoUsoDoc.setValor(nuevoUsoDoc.getValor().toUpperCase());
                nuevoUsoDoc.setEstado("A");
                nuevoUsoDoc.setCatalogo(catalogo);
                nuevoUsoDoc.setCodename(nuevoUsoDoc.getValor().trim().toLowerCase());
                usoDocumento = (CtlgItem) manager.persist(nuevoUsoDoc);
                nuevoUsoDoc = new CtlgItem();
                JsfUti.update("mainForm:accPanelRP:pnlUsoDoc");
                JsfUti.executeJS("PF('usoDocumento').hide();");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /*public void guardadoParcialLiquidacion() {
        try {
            //if (solicitante.getId() != null && beneficiario.getId() != null) {
            if (beneficiario.getId() != null) {
                //ht.setTipoTramite(tipoTramite);
                ht.setFecha(new Date());
                //ht.setNombrePropietario(solicitante.getNombreCompleto());
                //ht.setSolicitante(solicitante);
                ht.setNombrePropietario(beneficiario.getNombreCompleto());
                ht.setSolicitante(beneficiario);

                if (usoDocumento != null) {
                    liquidacion.setUsoDocumento(usoDocumento);
                }
                liquidacion.setEstadoLiquidacion(incompleta);
                liquidacion.setEstadoPago(estadoPago);
                liquidacion.setSubTotal(subTotal);
                liquidacion.setValorActos(subTotalDesc);
                liquidacion.setDescLimitCobro(descLimitCobro);
                liquidacion.setDescuentoPorc(porcPago);
                liquidacion.setDescuentoValor(descPorLey);
                liquidacion.setGastosGenerales(gastosGenerales);
                liquidacion.setTotalPagar(totalPagar);
                //liquidacion.setSolicitante(solicitante);
                liquidacion.setSolicitante(beneficiario);
                liquidacion.setBeneficiario(beneficiario);
                liquidacion.setFechaCreacion(new Date());
                liquidacion.setUserCreacion(session.getUserId());
                liquidacion.setTramite(ht);
                liquidacion = itl.saveParcialLiquidacion(liquidacion, actosPorPagar);

                if (liquidacion != null) {
                    ht = liquidacion.getTramite();
                    map = new HashMap();
                    map.put("liquidacion", liquidacion);
                    actosPorPagar = manager.findObjectByParameterList(RegpLiquidacionDetalles.class, map);
                    JsfUti.update("mainForm:accPanelRP");
                    JsfUti.messageWarning(null, "Liquidacion guardada parcialmente!!!", "");
                } else {
                    JsfUti.messageWarning(null, "Error al guardar liquidacion.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Se debe ingresar al solicitante y al beneficiario.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }*/
    public void guardarLiquidacion() {
        try {
            if (this.validaciones()) {
                //ht.setTipoTramite(tipoTramite);
                ht.setFecha(new Date());
                //ht.setNombrePropietario(solicitante.getNombreCompleto());
                //ht.setSolicitante(solicitante);
                ht.setNombrePropietario(beneficiario.getNombreCompleto());
                ht.setSolicitante(beneficiario);

                liquidacion.setEsRegistroPropiedad(propiedad);
                liquidacion.setCertificado(solvencia);
                liquidacion.setInscripcion(!solvencia);
                liquidacion.setUsoDocumento(usoDocumento);
                liquidacion.setSubTotal(subTotal);
                liquidacion.setValorActos(subTotalDesc);
                liquidacion.setAdicional(recargoAplicado);
                liquidacion.setDescLimitCobro(descLimitCobro);
                liquidacion.setDescuentoValor(descPorLey);
                liquidacion.setDescuentoPorc(porcPago);
                liquidacion.setGastosGenerales(gastosGenerales);
                liquidacion.setTotalPagar(totalPagar);
                //liquidacion.setSolicitante(solicitante);
                liquidacion.setSolicitante(beneficiario);
                liquidacion.setBeneficiario(beneficiario);
                liquidacion.setCantidadRazones(0);
                liquidacion.setTramite(ht);
                liquidacion.setFechaCreacion(new Date());
                liquidacion.setUserCreacion(session.getUserId());
                liquidacion.setEstadoPago(estadoPago);
                liquidacion.setEstadoLiquidacion(estadoLiquidacion);

                if (liquidacion.getFecharepertorioingreso() != null) {
                    liquidacion.setIngresoFechaRepertorio(sdf.format(liquidacion.getFecharepertorioingreso()));
                }
                if (liquidacion.getFechainscripcioningreso() != null) {
                    liquidacion.setIngresoFechaInscripcion(sdf.format(liquidacion.getFechainscripcioningreso()));
                }
                if (totalPagar.compareTo(BigDecimal.ZERO) == 0) {
                    liquidacion.setGeneraFactura(Boolean.FALSE);
                } else {
                    liquidacion.setGeneraFactura(Boolean.TRUE);
                }
                if (liquidacion.getReingreso()) {
                    liquidacion.setGeneraFactura(Boolean.FALSE);
                }
                /*if (liquidacion.getNumeroComprobante() == null) {
                    liquidacion.setNumeroComprobante(BigInteger.ZERO);
                }*/
                if (liquidacion.getVersionDescuento() == 2 || liquidacion.getVersionDescuento() == 3) {
                    liquidacion.setEsJuridico(true);
                }

                liquidacion = itl.saveLiquidacion(liquidacion, actosPorPagar);

                if (liquidacion == null) {
                    block = true;
                    JsfUti.update("mainForm:accPanelRP:pnlUsoDoc");
                    JsfUti.messageError(null, Messages.error, "Error al guardar la Solicitud.");
                } else {
                    this.cargarDatosReporte();
                    List<String> urlList = new ArrayList<>();
                    String url = "/procesos/registro/iniciarTramiteRp.xhtml";
                    urlList.add(SisVars.urlbase + "Documento");
                    JsfUti.redirectMultipleConIP_V2(url, urlList);
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaciones() {
//        pesoAcum = 0;
        /*if (solicitante.getId() == null) {
            JsfUti.messageWarning(null, "Debe ingresar Cliente solicitante.", "");
            return false;
        }*/
        if (liquidacion.getVersionDescuento() == null) {
            JsfUti.messageWarning(null, "Debe seleccionar el tipo de trámite.", "");
            return false;
        }
        if (liquidacion.getVersionDescuento() < 1 || liquidacion.getVersionDescuento() > 3) {
            JsfUti.messageWarning(null, "Debe seleccionar el tipo de trámite.", "");
            return false;
        }
        if (beneficiario.getId() == null) {
            JsfUti.messageWarning(null, "Debe ingresar datos para el Titulo de Credito.", "");
            return false;
        }
        /*if (beneficiario.getCorreo1() == null || beneficiario.getCorreo1().isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar correo electronico de la persona.", "");
            return false;
        }*/
        if (liquidacion.getCorreoTramite() == null || liquidacion.getCorreoTramite().isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar correo electronico para el trámite.", "");
            return false;
        }
        if (actosPorPagar.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el/los contrato(s) del tramite.", "");
            return false;
        }
        for (RegpLiquidacionDetalles d : actosPorPagar) {
            if (d.getExoneracion().getId() == null) {
                d.setExoneracion(null);
            }
            if (!d.getActo().getSolvencia()) {
                solvencia = false;
            }
        }
        return true;
    }

    public void cargarDatosReporte() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            //ss.setNombreReporte("comprobante_proforma");
            ss.setNombreReporte("proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelarAplicarExo() {
        exoneraciones = new ArrayList<>();
        porcPago = BigDecimal.ONE;
        this.inicializarDesc();
        JsfUti.update("mainForm:accPanelRP:pnlContratos");
        JsfUti.executeJS("PF('dlgExoneracion').hide();");
    }

    public void inicializarDesc() {
        actosPorPagar.stream().map((de) -> {
            de.setDescuento(BigDecimal.ZERO);
            return de;
        }).forEachOrdered((de) -> {
            de.setValorTotal(de.getValorUnitario());
        });
        this.calculoTotalPagar();
    }

    public void showDlgAplicarExo() {
        if (this.cargarIntervsExos()) {
            JsfUti.update("formExo");
            JsfUti.executeJS("PF('dlgExoneracion').show();");
        } else {
            JsfUti.messageWarning(null, "No hay contratos marcados para aplicar descuento.", "");
        }
    }

    public boolean cargarIntervsExos() {
        exoneraciones = new ArrayList<>();
        RegpLiquidacionExoneracion temp;
        List<RegpIntervinientes> intervs;
        for (RegpLiquidacionDetalles de : actosPorPagar) {
            if (de.getAplicaDescuento()) {
                intervs = (List<RegpIntervinientes>) de.getRegpIntervinientesCollection();
                if (intervs == null) {
                    map = new HashMap();
                    map.put("liquidacion", de);
                    intervs = manager.findObjectByParameterList(RegpIntervinientes.class, map);
                    if (intervs == null) {
                        intervs = new ArrayList<>();
                    }
                }
                for (RegpIntervinientes rei : intervs) {
                    if (this.validaProps(rei.getEnte().getCiRuc())) {
                        temp = new RegpLiquidacionExoneracion();
                        temp.setEnte(rei.getEnte());
                        exoneraciones.add(temp);
                    }
                }
            }
        }
        return !exoneraciones.isEmpty();
    }

    public boolean validaProps(String cedula) {
        return exoneraciones.stream().noneMatch((r) -> (r.getEnte().getCiRuc().equalsIgnoreCase(cedula)));
    }

    public void calcularValorDescuento() {
        try {
            if (this.calcularPorcPago()) {
                this.aplicarValorDesc();
                JsfUti.update("mainForm:accPanelRP:pnlContratos");
                JsfUti.executeJS("PF('dlgExoneracion').hide();");
            } else {
                JsfUti.messageWarning(null, "No se ha seleccionado el tipo de exoneracion.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void aplicarValorDesc() {
        actosPorPagar.forEach((de) -> {
            if (de.getAplicaDescuento()) {
                de.setValorTotal(de.getValorUnitario().multiply(porcPago).setScale(2, RoundingMode.HALF_UP));
                de.setDescuento(de.getValorUnitario().subtract(de.getValorTotal()));
            } else {
                de.setDescuento(BigDecimal.ZERO);
                de.setValorTotal(de.getValorUnitario());
            }
        });
        this.calculoTotalPagar();
    }

    public boolean calcularPorcPago() {
        indice = 0;
        porcPago = BigDecimal.ONE;
        BigDecimal temp = BigDecimal.ZERO;
        boolean aplicado = false;
        for (RegpLiquidacionExoneracion ex : exoneraciones) {
            if (ex.getBeneficiario()) {
                indice++;
                if (ex.getExoneracion() == null) {
                    temp = temp.add(BigDecimal.ONE);
                } else {
                    aplicado = true;
                    temp = temp.add(BigDecimal.ONE.subtract(ex.getExoneracion().getValor()));
                }
            } else {
                ex.setExoneracion(null);
            }
        }
        if (indice > 0 && aplicado) {
            if (temp.compareTo(BigDecimal.ZERO) > 0) {
                porcPago = temp.divide(new BigDecimal(indice), 4, RoundingMode.HALF_UP);
            } else {
                porcPago = BigDecimal.ZERO;
            }
            return true;
        } else {
            return false;
        }
    }

    public void showDlgExon(String urlFacelet, int indice) {
        exoneracion = exoneraciones.get(indice);
        if (exoneracion.getBeneficiario()) {
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "60%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
        } else {
            JsfUti.messageWarning(null, "Para seleccionar exoneracion debe marcar si es beneficiario.", "");
        }
    }

    public void actualizarBeneficiarioEsSolicitante() {
        if (beneficiarioEsSolicitante) {
            if (solicitante != null) {
                if (solicitante.getId() != null) {
                    beneficiario = solicitante;
                } else {
                    JsfUti.messageWarning(null, "Na ha seleccionado el beneficiario.", "");
                    beneficiarioEsSolicitante = Boolean.FALSE;
                }
            } else {
                beneficiarioEsSolicitante = Boolean.FALSE;
                JsfUti.messageWarning(null, "Na ha seleccionado el beneficiario.", "");
            }

        } else {

            beneficiario = new CatEnte();
            beneficiarioEsSolicitante = Boolean.FALSE;
        }

    }

    public void actualizarDatosSolicitante() {
        try {
            manager.merge(solicitante);
            JsfUti.messageInfo(null, Messages.correcto, "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void actualizarDatosBeneficiario() {
        try {
            manager.merge(beneficiario);
            liquidacion.setCorreoTramite(beneficiario.getCorreo1());
            JsfUti.messageInfo(null, Messages.correcto, "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void selectObjectExo(SelectEvent event) {
        rld.setExoneracion((RegpExoneracion) event.getObject());
    }

    public void showDlgFichas() {
        Long count = 0l;
        Map<String, Object> fill = new HashMap<>();
        fill.put("numInscripcion", liquidacion.getNumInscripcion());
        if (liquidacion.getAnioInscripcion() != null) {
            fill.put("anioInscripcion", liquidacion.getAnioInscripcion().toString());
            count = ((Long) manager.findObjectByParameter(Querys.RegFichaCount, fill));
        }
        // MOSTRAMOS LAS FICHAS ASOCIADAS
        fill.clear();
        fill.put("regMovimientoFichaCollection.movimiento.numInscripcion", liquidacion.getNumInscripcion());
        fichasRegistrales = new LazyModel(RegFicha.class, "id", "DESC");
        if (count > 0) {
            fichasRegistrales.setFilterss(fill);
        }
        JsfUti.update("frmFichas");
        JsfUti.executeJS("PF('dglFichas').show();");
    }

    public void seleccionarFicha(RegFicha regFicha) {
        //this.liquidacion.setFicha(regFicha);
        numFicha = regFicha.getNumFicha();
        this.validarTramiteIngresado(liquidacion);
        //System.out.println("ficha" + this.liquidacion.getFicha().getClaveCatastral());
        JsfUti.update("mainForm:accPanelRP:ficha_registral");
        JsfUti.executeJS("PF('dglFichas').hide();");
    }

    public void limpiarFicha() {
        this.numFicha = 0L;
        // this.liquidacion.setFicha(null);
        this.liquidacion.setNumFicha(0L);
        JsfUti.update("mainForm:accPanelRP:ficha_registral");
    }

    public String valorArancel(RegActo ac) {
        String result = "SIN ARANCEL";
        if (ac.getArancel() != null) {
            if (ac.getArancel().getValor().compareTo(new BigDecimal(-1)) == 0) {
                result = "SEGUN CUANTIA";
            } else {
                result = ac.getArancel().getValor().toString();
            }
        }
        return result;
    }

    public void firmaSolicitante() {
        try {
            String imgBase64 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imgBase64");
            if (imgBase64 != null) {
                byte[] buffer = Base64.getDecoder().decode(imgBase64);
                if (buffer != null) {
                    liquidacion.setFirma(buffer);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(IniciarTramiteJuridicoRp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CtlgItem> getEstadosCiviles() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        return manager.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public List<RegDomicilio> getDomicilios() {
        return manager.findAllEntCopy(Querys.getRegDomicilioList);
    }

    public List<CatParroquia> getParroquias() {
        return manager.findAllEntCopy(Querys.getCatParroquiaList);
    }

    public void verificarTramite() {
        try {
            if (liquidacion.getEsJuridico()) {
                JsfUti.update("formJuridico");
                JsfUti.executeJS("PF('dlgIniciarJuridico').show();");
            } else {
                this.guardarLiquidacion();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public RegEnteJudiciales getEnju() {
        return enju;
    }

    public void setEnju(RegEnteJudiciales enju) {
        this.enju = enju;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public List<RegpIntervinientes> getListInterv() {
        return listInterv;
    }

    public void setListInterv(List<RegpIntervinientes> listInterv) {
        this.listInterv = listInterv;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    /*public List<RegActo> getActos() {
    return actos;
    }
    
    public void setActos(List<RegActo> actos) {
    this.actos = actos;
    }*/
    public LazyModel getActos() {
        return actos;
    }

    public void setActos(LazyModel actos) {
        this.actos = actos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RegpLiquidacionDetalles> getActosPorPagar() {
        return actosPorPagar;
    }

    public void setActosPorPagar(List<RegpLiquidacionDetalles> actosPorPagar) {
        this.actosPorPagar = actosPorPagar;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getObsAdicional() {
        return obsAdicional;
    }

    public void setObsAdicional(String obsAdicional) {
        this.obsAdicional = obsAdicional;
    }

    public CtlgItem getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(CtlgItem usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    public CtlgItem getNuevoUsoDoc() {
        return nuevoUsoDoc;
    }

    public void setNuevoUsoDoc(CtlgItem nuevoUsoDoc) {
        this.nuevoUsoDoc = nuevoUsoDoc;
    }

    public BigDecimal getDescPorLey() {
        return descPorLey;
    }

    public void setDescPorLey(BigDecimal descPorLey) {
        this.descPorLey = descPorLey;
    }

    public BigDecimal getDescLimitCobro() {
        return descLimitCobro;
    }

    public void setDescLimitCobro(BigDecimal descLimitCobro) {
        this.descLimitCobro = descLimitCobro;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public RegpLiquidacionDetalles getRld() {
        return rld;
    }

    public void setRld(RegpLiquidacionDetalles rld) {
        this.rld = rld;
    }

    public Boolean getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Boolean diferencia) {
        this.diferencia = diferencia;
    }

    public RegpIntervinientes getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(RegpIntervinientes interviniente) {
        this.interviniente = interviniente;
    }

    public BigDecimal getSubTotalDesc() {
        return subTotalDesc;
    }

    public void setSubTotalDesc(BigDecimal subTotalDesc) {
        this.subTotalDesc = subTotalDesc;
    }

    public CatEnte getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(CatEnte beneficiario) {
        this.beneficiario = beneficiario;
    }

    public RegpLiquidacionExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(RegpLiquidacionExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public List<RegpLiquidacionExoneracion> getExoneraciones() {
        return exoneraciones;
    }

    public void setExoneraciones(List<RegpLiquidacionExoneracion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }

    public BigDecimal getPorcPago() {
        return porcPago.multiply(new BigDecimal(100)).setScale(2);
    }

    public void setPorcPago(BigDecimal porcPago) {
        this.porcPago = porcPago;
    }

    public Integer getTipocalculo() {
        return tipocalculo;
    }

    public void setTipocalculo(Integer tipocalculo) {
        this.tipocalculo = tipocalculo;
    }

    public BigDecimal getGastosGenerales() {
        return gastosGenerales;
    }

    public void setGastosGenerales(BigDecimal gastosGenerales) {
        this.gastosGenerales = gastosGenerales;
    }

    public Boolean getBeneficiarioEsSolicitante() {
        return beneficiarioEsSolicitante;
    }

    public void setBeneficiarioEsSolicitante(Boolean beneficiarioEsSolicitante) {
        this.beneficiarioEsSolicitante = beneficiarioEsSolicitante;
    }

    public List<RegpDetalleExoneracion> getDetalleExoneraciones() {
        return detalleExoneraciones;
    }

    public void setDetalleExoneraciones(List<RegpDetalleExoneracion> detalleExoneraciones) {
        this.detalleExoneraciones = detalleExoneraciones;
    }

    public Boolean getAgregaBeneficiario() {
        return agregaBeneficiario;
    }

    public void setAgregaBeneficiario(Boolean agregaBeneficiario) {
        this.agregaBeneficiario = agregaBeneficiario;
    }

    public Boolean getAgregaSolicitante() {
        return agregaSolicitante;
    }

    public void setAgregaSolicitante(Boolean agregaSolicitante) {
        this.agregaSolicitante = agregaSolicitante;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public Integer getAniosDiferencia() {
        return aniosDiferencia;
    }

    public void setAniosDiferencia(Integer aniosDiferencia) {
        this.aniosDiferencia = aniosDiferencia;
    }

    public BigDecimal getRecargoAplicado() {
        return recargoAplicado;
    }

    public void setRecargoAplicado(BigDecimal recargoAplicado) {
        this.recargoAplicado = recargoAplicado;
    }

    public RegpTareasDinardapLazy getTareasLazy() {
        return tareasLazy;
    }

    public void setTareasLazy(RegpTareasDinardapLazy tareasLazy) {
        this.tareasLazy = tareasLazy;
    }

    public LazyModel getFichasRegistrales() {
        return fichasRegistrales;
    }

    public void setFichasRegistrales(LazyModel fichasRegistrales) {
        this.fichasRegistrales = fichasRegistrales;
    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public List<AclUser> getUsers() {
        return users;
    }

    public void setUsers(List<AclUser> users) {
        this.users = users;
    }

    public AclUser getUserInscriptor() {
        return userInscriptor;
    }

    public void setUserInscriptor(AclUser userInscriptor) {
        this.userInscriptor = userInscriptor;
    }

    public StringBuffer getCoincidencias() {
        return coincidencias;
    }

    public void setCoincidencias(StringBuffer coincidencias) {
        this.coincidencias = coincidencias;
    }

}
