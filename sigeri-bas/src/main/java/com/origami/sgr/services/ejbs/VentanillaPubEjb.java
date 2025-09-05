/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.google.gson.Gson;
import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ConvenioBanco;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.GeTipoTramite;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudActo;
import com.origami.sgr.entities.PubSolicitudRequisito;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegRequisitos;
import com.origami.sgr.entities.RegRequisitosActos;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.models.ActosRequisito;
import com.origami.sgr.models.Pago;
import com.origami.sgr.models.PagoReverso;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.BpmBaseEngine;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.services.interfaces.VentanillaPubLocal;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.activiti.engine.runtime.ProcessInstance;
import org.hibernate.Hibernate;

/**
 *
 * @author Anyelo
 */
@Stateless(name = "ventanillapub")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class VentanillaPubEjb implements VentanillaPubLocal {

    @Inject
    private OmegaUploader ou;
    @Inject
    private Entitymanager em;
    @Inject
    private SeqGenMan sec;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private BpmBaseEngine engine;
    @Inject
    private AsynchronousService as;
    @Inject
    private RegistroPropiedadServices rps;
    @Inject
    private UserSession session;

    /**
     * Guarda el tramite que se solicita por la aplicacion de la ventanilla,
     * equivale al registro de la proforma y guarda en las tablas de la
     * liquidacion, historico tramites y detalle de la liquidacion
     *
     * @param solicitud Object
     * @return Object
     */
    @Override
    public RegpLiquidacion iniciarTramiteOnline(PubSolicitud solicitud) {
        try {
            RegpLiquidacion liquidacion = this.generarProformaOnline(solicitud);

            if (liquidacion.getTituloCredito() == null || liquidacion.getTituloCredito() == 0L) {
                liquidacion.setTituloCredito(sec.getSecuenciaTitulo(liquidacion.getNumTramiteRp()));
                liquidacion.setCodigoComprobante(liquidacion.getTituloCredito().toString());
            }

            liquidacion.setFechaIngreso(new Date());
            liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L));    //ESTADO INGRESADO
            liquidacion.setEstadoPago(new RegpEstadoPago(7L));                  //PAGO ONLINE
            liquidacion.setUserIngreso(6L);                                     //USUARIO RECAUDADOR ONLINE

            this.asignarUsuarioFechaEntrega(liquidacion);

            return liquidacion;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public RegpLiquidacion generarProformaOnline(PubSolicitud solicitud) {
        RegpLiquidacion liquidacion = new RegpLiquidacion();
        HistoricoTramites ht = new HistoricoTramites();
        Date fecha = new Date();
        RegpLiquidacionDetalles det;
        BigDecimal valorunitario;
        RegActo acto;
        CatEnte ente;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println("// proforma online...");
            //caja = this.getCajaVentanilla();
            acto = this.getActoByTipo(solicitud.getTipoSolicitud());
            /*ente = this.findEnte(solicitud.getSolCedula(), solicitud.getSolTipoPersona(), solicitud.getSolTipoDoc(),
                    solicitud.getSolNombres(), solicitud.getSolApellidos(), solicitud.getSolProvincia(),
                    solicitud.getSolCorreo(), null, solicitud.getSolCelular(), solicitud.getSolConvencional());*/
            ente = this.findEnte(solicitud.getBenDocumento(), solicitud.getBenTipoPersona(), solicitud.getBenTipoDoc(),
                    solicitud.getBenNombres(), solicitud.getBenApellidos(), solicitud.getBenDireccion(),
                    solicitud.getBenCorreo(), null, solicitud.getBenTelefono(), null);

            /*LLENADO DE DATOS DE TRAMITE*/
            ht.setTipoTramite(new GeTipoTramite(1L)); //TRAMITE DE CERTIFICACIONES
            ht.setNombrePropietario(ente.getNombreCompleto());
            ht.setSolicitante(ente);
            ht.setFecha(fecha);
            ht.setIdProcesoTemp(Constantes.tramiteEnLinea);
            ht.setNumTramite(sec.getSecuenciaTramite());
            ht = (HistoricoTramites) em.persist(ht);

            /*LLENADO DE DATOS PARA LA PROFORMA*/
            //liquidacion.setFicha(findFicha(solicitud.getNumeroFicha()));
            if (solicitud.getNumeroFicha() != null) {
                liquidacion.setNumFicha(solicitud.getNumeroFicha().longValue());
            }
            liquidacion.setNumInscripcion(solicitud.getNumInscripcion() != null ? solicitud.getNumInscripcion() : null);
            liquidacion.setAnioInscripcion(solicitud.getAnioInscripcion() != null ? solicitud.getAnioInscripcion() : null);
            //liquidacion.setCertificadoSinFlujo(certificadoXpress(solicitud));
            liquidacion.setTramiteOnline(Boolean.TRUE);
            liquidacion.setTramite(ht);
            liquidacion.setNumTramiteRp(ht.getNumTramite());
            liquidacion.setSolicitante(ht.getSolicitante());
            liquidacion.setBeneficiario(ht.getSolicitante());
            liquidacion.setSubTotal(new BigDecimal(solicitud.getTotal()));
            liquidacion.setValorActos(new BigDecimal(solicitud.getTotal()));
            liquidacion.setDescuentoValor(BigDecimal.ZERO);
            liquidacion.setDescLimitCobro(BigDecimal.ZERO);
            liquidacion.setTotalPagar(new BigDecimal(solicitud.getTotal()));
            liquidacion.setFechaCreacion(fecha);
            liquidacion.setCorreoTramite(ente.getCorreo1());
            if (solicitud.getFechaRepertorio() != null) {
                liquidacion.setIngresoFechaRepertorio(sdf.format(solicitud.getFechaRepertorio()));
            }
            if (solicitud.getFechaInscripcion() != null) {
                liquidacion.setIngresoFechaInscripcion(sdf.format(solicitud.getFechaInscripcion()));
            }
            if (solicitud.getNumRepertorio() != null) {
                liquidacion.setIngresoNumRepertorio(solicitud.getNumRepertorio().toString());
            }
            if (solicitud.getTipoInmueble() != null) {
                liquidacion.setIngresoDireccion(" TIPO INMUEBLE: " + solicitud.getTipoInmueble() + ";");
            }
            if (solicitud.getSector() != null) {
                liquidacion.setIngresoDireccion(liquidacion.getIngresoDireccion() + " SECTOR: " + solicitud.getSector() + ";");
            }
            if (solicitud.getManzana() != null) {
                liquidacion.setIngresoDireccion(liquidacion.getIngresoDireccion() + " MANZANA: " + solicitud.getManzana() + ";");
            }
            if (solicitud.getLote() != null) {
                liquidacion.setIngresoDireccion(liquidacion.getIngresoDireccion() + " LOTE: " + solicitud.getLote() + ";");
            }
            if (solicitud.getSolNumeroCasa() != null) {
                liquidacion.setIngresoDireccion(liquidacion.getIngresoDireccion() + " DIRECCION: " + solicitud.getSolNumeroCasa() + ";");
            }
            if (solicitud.getNumInscripcion() != null) {
                liquidacion.setIngresoDireccion(liquidacion.getIngresoDireccion() + " INSCRIPCION: " + solicitud.getNumInscripcion() + ";");
            }
            if (solicitud.getObservacion() != null) {
                liquidacion.setInfAdicionalProf(solicitud.getObservacion());
            }
            //liquidacion.setFechaIngreso(fecha);
            //liquidacion.setUserCreacion(caja.getUsuario().getId());
            //liquidacion.setUserIngreso(caja.getUsuario().getId());
            liquidacion.setUserCreacion(3L); //USUARIO VETANILLA
            liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(1L));    //ESTADO APROBADO
            liquidacion.setEstadoPago(new RegpEstadoPago(1L));                  //PENDIENTE DE PAGO
            liquidacion.setCertificado(Boolean.TRUE);
            liquidacion.setRepertorio(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaRepertorioSolvencia).intValue());

            liquidacion.setUsoDocumento(this.getUsoDocumentoById(solicitud.getMotivoSolicitud()));
            /*if (solicitud.getMotivoSolicitud() != -1) {
                liquidacion.setUsoDocumento(getUsoDocumentoById(solicitud.getMotivoSolicitud()));
            } else {
                liquidacion.setUsoDocumento(saveUsoDocumentoById(solicitud.getOtroMotivo()));
            }*/

            liquidacion = (RegpLiquidacion) em.persist(liquidacion);

            /*DATOS DE DETALLE DE LA PROFORMA*/
            det = new RegpLiquidacionDetalles();
            det.setCantidad(solicitud.getCantidad());
            det.setActo(acto);
            det.setAvaluo(BigDecimal.ZERO);
            det.setCuantia(BigDecimal.ZERO);
            det.setBase(BigDecimal.ZERO);
            det.setFechaIngreso(fecha);
            //det.setValorUnitario(new BigDecimal(solicitud.getTotal()));
            det.setValorTotal(new BigDecimal(solicitud.getTotal()));
            valorunitario = det.getValorTotal().divide(new BigDecimal(solicitud.getCantidad()), 2, RoundingMode.HALF_UP);
            det.setValorUnitario(valorunitario);
            det.setRecargo(BigDecimal.ZERO);
            det.setDescuento(BigDecimal.ZERO);
            det.setLiquidacion(liquidacion);
            if (solicitud.getAnioAntecedenteSolicitado() != null) {
                det.setAnioAntecedenteSolicitado(solicitud.getAnioAntecedenteSolicitado());
                det.setNumPredio(det.getAnioAntecedenteSolicitado());
            }
            if (solicitud.getAnioUltimaTransferencia() != null) {
                det.setAnioUltimaTrasnferencia(solicitud.getAnioUltimaTransferencia());
            }

            det = (RegpLiquidacionDetalles) em.persist(det);

            /*FIN DATOS DE DETALLE DE LA PROFORMA*/
            //papel = this.getPapelByNombre("PROPIETARIO");
            /*ente = this.findEnte(solicitud.getPropCedula(), solicitud.getPropTipoPersona(), solicitud.getPropTipoDoc(),
                    solicitud.getPropNombres(), solicitud.getPropApellidos(), null, null, null, null, null);*/
            int cantidad = 0;
            RegpIntervinientes intv;
            String[] person = solicitud.getPropTipoPersona().split(";");
            for (String person1 : person) {
                //DATOS DEL INTERVINIENTE
                if (!person1.trim().isEmpty()) {
                    intv = new RegpIntervinientes();
                    intv.setNombres(person1);
                    intv.setLiquidacion(det);
                    em.persist(intv);
                    cantidad++;
                }
            }
            det.setCantidadIntervinientes(cantidad);
            /*GUARDA DATOS SOLICITUD*/
            solicitud.setTramite(ht);
            if (solicitud.getPayframeUrl() == null) {
                solicitud.setPayframeUrl("APP_MOVIL");
            }
            em.persist(solicitud);

            List<RegpLiquidacionDetalles> actosPorPagar = new ArrayList<>();
            actosPorPagar.add(det);
            liquidacion.setRegpLiquidacionDetallesCollection(actosPorPagar);
            return liquidacion;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public PubSolicitud generarSolicitudInscripcion(PubSolicitud solicitud) {
        HistoricoTramites ht = new HistoricoTramites();
        List<ActosRequisito> requisitos = solicitud.getRequisitos();
        solicitud.setTieneNotificacion(Boolean.FALSE);
        solicitud.setRequisitos(null);
        CatEnte ente;
        try {
            ente = this.findEnte(solicitud.getSolCedula(), solicitud.getSolTipoPersona(), solicitud.getSolTipoDoc(),
                    solicitud.getSolNombres(), solicitud.getSolApellidos(), solicitud.getSolProvincia(),
                    solicitud.getSolCorreo(), null, solicitud.getSolCelular(), solicitud.getSolConvencional());
            /*
                LLENADO DE DATOS DE TRAMITE
             */
            ht.setTipoTramite(new GeTipoTramite(3L));
            ht.setNombrePropietario(ente.getNombreCompleto());
            ht.setSolicitante(ente);
            ht.setFecha(new Date());
            ht.setFechaIngreso(new Date());
            ht.setIdProcesoTemp(SisVars.userVentanilla);
            ht.setNumTramite(sec.getSecuenciaTramite());
            ht = (HistoricoTramites) em.persist(ht);
            solicitud.setEstado("A");
            solicitud.setTramite(ht);
//            if (solicitud.getPayframeUrl() == null) {
//                solicitud.setPayframeUrl("APP_MOVIL");
//            }
            solicitud = (PubSolicitud) em.persist(solicitud);
            for (ActosRequisito ar : requisitos) {

                PubSolicitudRequisito solicitudRequisito = new PubSolicitudRequisito();
                solicitudRequisito.setActo(new RegActo(ar.getIdActo().longValue()));
                solicitudRequisito.setRequisito(new RegRequisitos(ar.getIdRequisito().longValue()));
                solicitudRequisito.setRequisitosActos(new RegRequisitosActos(ar.getRequisitoActo().longValue()));
                solicitudRequisito.setDocumento(ar.getDocumento());
                solicitudRequisito.setSolicitud(solicitud);
                solicitudRequisito.setFecha(new Date());
                solicitudRequisito.setTipo(Constantes.TIPO_REQUISITO);
                em.persist(solicitudRequisito);
            }
            List<ActosRequisito> actosFiltered = requisitos.stream()
                    .filter(Utils.distinctByKey(p -> p.getIdActo()))
                    .collect(Collectors.toList());

            for (ActosRequisito ar : actosFiltered) {
                PubSolicitudActo solicitudActo = new PubSolicitudActo();
                solicitudActo.setActo(new RegActo(ar.getIdActo().longValue()));
                solicitudActo.setSolicitud(solicitud);
                em.persist(solicitudActo);
            }
            solicitud = itl.asignarUsuarioInscripcionEnlinea(solicitud, ht);
            iniciarTramiteSolicitudInscripcion(solicitud);

            return solicitud;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public RegpLiquidacion iniciarTramiteInscripcionOnline(PubSolicitud solicitud) {
        Map map = new HashMap();
        map.put("numTramiteRp", solicitud.getNumeroTramiteInscripcion());
        RegpLiquidacion liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);

        HistoricoTramites ht = liquidacion.getTramite();

        try {
            RenCajero caja = this.getCajaVentanilla();

            ht.setFecha(new Date());
            ht.setIdProcesoTemp(SisVars.userVentanilla);
            ht = (HistoricoTramites) em.merge(ht);

            liquidacion.setTramiteOnline(Boolean.TRUE);
            liquidacion.setFechaIngreso(new Date());
            liquidacion.setFechaRepertorio(new Date());
            liquidacion.setUserCreacion(caja.getUsuario().getId());
            liquidacion.setUserIngreso(caja.getUsuario().getId());
            liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L));    //ESTADO INGRESADO
            liquidacion.setEstadoPago(new RegpEstadoPago(7L));                  //PAGO ONLINE
            liquidacion.setCertificado(Boolean.FALSE);
            liquidacion.setCaja(caja);
            liquidacion.setNumeroComprobante(sec.getSecuenciaGeneral(caja.getVariableSecuencia()));
            liquidacion.setCodigoComprobante("001-" + caja.getCodigoCaja() + "-" + Utils.completarCadenaConCeros(liquidacion.getNumeroComprobante().toString(), 9));
            /*
                FIN GENERA NUMERO DE COMPROBANTE
             */
            liquidacion = (RegpLiquidacion) em.merge(liquidacion);
            /*
                DATOS DE DETALLE DE LA PROFORMA
             */
            liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), caja);
            liquidacion = (RegpLiquidacion) em.merge(liquidacion);
            /*
                FIN DATOS DE DETALLE DE LA PROFORMA
             */
 /*
                GUARDA DATOS SOLICITUD
             */
            solicitud.setTieneNotificacion(Boolean.FALSE);
            solicitud.setTramite(ht);
            em.merge(solicitud);

            return liquidacion;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public void iniciarTramiteSolicitudInscripcion(PubSolicitud solicitud) {
        try {
            HistoricoTramites tramite = solicitud.getTramite();
            HashMap<String, Object> pars = new HashMap<>();
            Map map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("activitykey", Constantes.procesoSolicitudInscripcionOnline);
            GeTipoTramite tipoTramite = (GeTipoTramite) em.findObjectByParameter(GeTipoTramite.class, map);

            pars.put("tecnico", solicitud.getRevisor().getUsuario());
            pars.put("revision", 1);
            pars.put("tramite", tramite.getNumTramite());
            ProcessInstance p = engine.startProcessByDefinitionKey(tipoTramite.getActivitykey(), pars); //PROCESO PARA INICAR EN ACTIVITI
            if (p != null) {

                tramite.setIdProceso(p.getId());
                tramite.setTipoTramite(tipoTramite);
                em.update(tramite);
            }
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public HistoricoTramites actualizarRequisitosInscripcion(PubSolicitud solicitud) {
        Map map = new HashMap();
        map.put("numTramite", solicitud.getNumeroTramite());
        HistoricoTramites historicoTramites = (HistoricoTramites) em.findObjectByParameter(HistoricoTramites.class, map);
        if (historicoTramites != null) {
            map = new HashMap();
            map.put("tramite", historicoTramites);
            PubSolicitud ps
                    = (PubSolicitud) em.findObjectByParameter(PubSolicitud.class, map);
            if (ps != null) {
                ps.setTieneNotificacion(Boolean.FALSE);
                //rps.crearNotificacion(Variables.NOTIFICACION_SOLICITUD_INSCRIPCION, "Se actualizaron los requisitos del trámite " + historicoTramites.getNumTramite() + " del solicitante " + ps.getSolNombres() + " " + ps.getSolApellidos(), historicoTramites.getNumTramite(), ps.getRevisor());
                for (ActosRequisito ar : solicitud.getRequisitos()) {
                    PubSolicitudRequisito solicitudRequisito = new PubSolicitudRequisito();
                    solicitudRequisito.setActo(new RegActo(ar.getIdActo().longValue()));
                    solicitudRequisito.setRequisito(new RegRequisitos(ar.getIdRequisito().longValue()));
                    solicitudRequisito.setRequisitosActos(new RegRequisitosActos(ar.getRequisitoActo().longValue()));
                    solicitudRequisito.setDocumento(ar.getDocumento());
                    solicitudRequisito.setSolicitud(ps);
                    solicitudRequisito.setFecha(new Date());
                    solicitudRequisito.setTipo(Constantes.TIPO_REQUISITO);
                    em.persist(solicitudRequisito);
                }
                em.update(ps);

            }
        }
        return historicoTramites;
    }

    public RegActo getActoByTipo(Integer idActo) {
        try {
            RegActo acto = em.find(RegActo.class, idActo.longValue());
            return acto;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public CtlgItem getUsoDocumentoById(Integer idUsoDocumento) {
        try {
            CtlgItem item = em.find(CtlgItem.class, idUsoDocumento.longValue());
            if (item != null) {
                return item;
            } else {
                return new CtlgItem(4L);
            }
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return new CtlgItem(4L);
        }
    }

    public CtlgItem saveUsoDocumentoById(String usoDocumento) {
        try {
            HiberUtil.newTransaction();
            CtlgItem ctlgItem = null;
            Map map = new HashMap();
            try {
                //  map.put("catalogo.nombre", Variables.usosDocumento);
                map.put("valor", usoDocumento.trim());
                List<CtlgItem> items = em.findObjectByParameterList(CtlgItem.class, map);
                if (Utils.isNotEmpty(items)) {
                    ctlgItem = items.get(0);
                }
            } catch (Exception e) {
            }

            if (ctlgItem == null) {
                map = new HashMap();
                map.put("nombre", Constantes.usosDocumento);
                CtlgCatalogo catalogo = (CtlgCatalogo) em.findObjectByParameter(CtlgCatalogo.class, map);
                CtlgItem nuevoUsoDoc = new CtlgItem();
                if (usoDocumento.length() < 79) {
                    nuevoUsoDoc.setValor(usoDocumento.toUpperCase());
                } else {
                    nuevoUsoDoc.setValor(usoDocumento.toUpperCase().substring(0, 79));
                }
                if (usoDocumento.length() < 39) {
                    nuevoUsoDoc.setCodename(nuevoUsoDoc.getValor().trim().toLowerCase());
                } else {
                    nuevoUsoDoc.setCodename(nuevoUsoDoc.getValor().trim().toLowerCase().substring(0, 39));
                }
                nuevoUsoDoc.setEstado("A");
                nuevoUsoDoc.setCatalogo(catalogo);
                ctlgItem = (CtlgItem) em.merge(nuevoUsoDoc);
            }

            return ctlgItem;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public RegPapel getPapelByNombre(String nombre) {
        Map map = new HashMap();
        RegPapel papel;
        try {
            map.put("papel", nombre);
            map.put("estado", true);
            papel = (RegPapel) em.findObjectByParameter(RegPapel.class, map);
            return papel;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Metodo que busca los datos de la persona en la base local, de caso de no
     * estar registrado ingresado crea el registro; solamente para personas
     * naturales
     *
     * @param cedula Object
     * @param tipopersona Object
     * @param tipodoc Object
     * @param nombres Object
     * @param apellidos Object
     * @param direccion Object
     * @param correo1 Object
     * @param correo2 Object
     * @param tlfn1 Object
     * @param tlfn2 Object
     * @return Object
     */
    public CatEnte findEnte(String cedula, String tipopersona, String tipodoc, String nombres, String apellidos,
            String direccion, String correo1, String correo2, String tlfn1, String tlfn2) {
        Map map = new HashMap();
        CatEnte ente;
        HiberUtil.newTransaction();
        try {
            System.out.println("// cedula: " + cedula);
            map.put("ciRuc", cedula.trim());
            ente = (CatEnte) em.findObjectByParameter(CatEnte.class, map);
            if (ente == null) {
                ente = new CatEnte();
                ente.setCiRuc(cedula);
                ente.setEsPersona(Boolean.TRUE);
                if (tipodoc != null) {
                    switch (tipodoc) {
                        case "J":
                        case "R":
                            ente.setEsPersona(Boolean.FALSE);
                            ente.setRazonSocial(nombres);
                            ente.setNombreComercial(apellidos);
                            ente.setTipoIdentificacion("R");
                            break;
                        case "P":
                            ente.setNombres(nombres);
                            ente.setApellidos(apellidos);
                            ente.setTipoIdentificacion("P");
                            break;
                        default:
                            ente.setNombres(nombres);
                            ente.setApellidos(apellidos);
                            ente.setTipoIdentificacion("C");
                            break;
                    }
                } else {
                    ente.setNombres(nombres);
                    ente.setApellidos(apellidos);
                    ente.setTipoIdentificacion("C");
                }
                ente.setDireccion(direccion);
                ente.setCorreo1(correo1);
                ente.setCorreo2(correo2);
                ente.setTelefono1(tlfn1);
                ente.setTelefono2(tlfn2);
                ente.setFechaCre(new Date());
                ente.setUserCre(Constantes.tramiteEnLinea);
                ente = (CatEnte) em.persist(ente);
            } else {
                ente.setDireccion(direccion);
                ente.setCorreo1(correo1);
                ente.setCorreo2(correo2);
                ente.setTelefono1(tlfn1);
                ente.setTelefono2(tlfn2);
                ente.setFechaMod(new Date());
                ente.setUserMod(Constantes.tramiteEnLinea);
                em.update(ente);
            }
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return ente;
    }

    public boolean asignarUsuarioFechaEntrega(RegpLiquidacion liq) {
        try {
            HistoricoTramites ht = liq.getTramite();
            Map map = new HashMap<>();
            map.put("idLiquidacion", liq.getId());
            Integer dias = (Integer) em.findObjectByParameter(Querys.getMaxDiasByTramite, map);

            ht.setFechaIngreso(liq.getFechaIngreso());
            if (ht.getFechaEntrega() == null) {
                ht.setFechaEntrega(itl.diasEntregaTramite(ht.getFechaIngreso(), dias));
            }

            if (liq.getInscriptor() == null) {
                map = new HashMap<>();
                map.put("nombre", "certificador");
                AclRol rol = (AclRol) em.findObjectByParameter(AclRol.class, map);
                liq.setInscriptor(sec.getUserForTramite(rol.getId(), 1));
            }

            em.update(ht);
            em.update(liq);

        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    @Override
    public RenCajero getCajaVentanilla() {
        try {
            Map map = new HashMap();
            map.put("habilitado", true);
            map.put("variableSecuencia", Constantes.cajaVentanilla);
            RenCajero cajero = (RenCajero) em.findObjectByParameter(RenCajero.class, map);
            return cajero;
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * ejecuta una nueva instancia para los tramites que se solicitan por la
     * aplicacion de la ventanilla
     *
     * @param liquidacion Object
     * @return Object
     */
    @Override
    public boolean iniciarTramiteActivitiOnline(RegpLiquidacion liquidacion) {
        try {
            Map mapa = new HashMap();
            mapa.put("estado", Boolean.TRUE);
            mapa.put("activitykey", Constantes.procesoCertificacion);
            GeTipoTramite tipoTramite = (GeTipoTramite) em.findObjectByParameter(GeTipoTramite.class, mapa);
            if (tipoTramite == null) {
                return false;
            }

            HashMap<String, Object> pars = new HashMap<>();
            pars.put("prioridad", 50);
            pars.put("revision", 1);
            pars.put("aprobado", 1);
            pars.put("tramite", liquidacion.getNumTramiteRp());
            pars.put("proceso", tipoTramite.getDescripcion());
            pars.put("certificador", liquidacion.getInscriptor().getUsuario());
            pars.put("inscriptor", liquidacion.getInscriptor().getUsuario());
            pars.put("registrador", itl.getUsuarioByRolName("registrador"));
            pars.put("ventanilla", itl.getCandidateUserByRolName("entrega_tramites"));
            Thread.sleep(5000);
            ProcessInstance p = engine.startProcessByDefinitionKey(tipoTramite.getActivitykey(), pars);
            if (p != null) {
                HistoricoTramites ht = liquidacion.getTramite();
                ht.setIdProceso(p.getId());
                ht.setTipoTramite(tipoTramite);
                em.update(ht);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    private Boolean certificadoXpress(PubSolicitud solicitud) {
        Boolean xpress = Boolean.FALSE;
        switch (solicitud.getTipoSolicitud()) {
            case 114:
            case 121:
                xpress = Boolean.TRUE;
                break;
            default:
                xpress = Boolean.FALSE;
                break;
        }
        return xpress;
    }

    private RegFicha findFicha(Integer numFicha) {
        RegFicha regFicha = null;
        if (numFicha != null) {
            regFicha = (RegFicha) em.find(Querys.getRegFichaNumFicha, new String[]{"numFicha"},
                    new Object[]{Long.valueOf(numFicha)});
        }
        return regFicha != null ? regFicha : null;
    }

    @Override
    public RegpLiquidacion iniciarTramiteBanca(Pago pago) {
        try {
            RenCajero cajero = getCajaVentanilla();
            HashMap<String, Object> pars = new HashMap<>();
            pars.put("numTramiteRp", Long.parseLong(pago.getIdDeuda()));
            //pars.put("estadoLiquidacion", new RegpEstadoLiquidacion(1l));
            // pars.put("estadoPago", new RegpEstadoPago(1l));
            RegpLiquidacion liquidacion = em.findObjectByParameter(RegpLiquidacion.class, pars);
            Hibernate.initialize(liquidacion);
            if (liquidacion != null) {
                Hibernate.initialize(liquidacion.getTramite());
                if ((liquidacion.getEstadoLiquidacion().getId() == 2L && liquidacion.getEstadoPago().getId() == 7L)
                        || (liquidacion.getEstadoLiquidacion().getId() == 2L && liquidacion.getEstadoPago().getId() == 2L)) {
                    pago.setEstado("P003");
                    return null;
                }
                if (liquidacion.getEstadoLiquidacion().getId() == 3L) {
                    pago.setEstado("P003");
                    return null;
                }
                if (!(new Double(pago.getValorDeuda()).equals(liquidacion.getTotalPagar().doubleValue()))) {
                    pago.setEstado("P004");
                    pago.setObservacionPago("No se permite pago parcial");
                    return null;
                }
                liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(2L));
                liquidacion.setEstadoPago(new RegpEstadoPago(7L));
                /*
                GENERA NUMERO DE COMPROBANTE
                 */
                liquidacion.setCaja(cajero);
                liquidacion.setNumeroComprobante(sec.getSecuenciaGeneral(cajero.getVariableSecuencia()));
                liquidacion.setCodigoComprobante("001-" + cajero.getCodigoCaja() + "-" + Utils.completarCadenaConCeros(liquidacion.getNumeroComprobante().toString(), 9));
                /*
                FIN GENERA NUMERO DE COMPROBANTE
                 */
                liquidacion.setFechaIngreso(new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(pago.getFechaTransaccion()));
                em.merge(liquidacion);
                liquidacion.getTramite().setFechaIngreso(new Date());
                em.merge(liquidacion.getTramite());
                if (!liquidacion.getCertificadoSinFlujo()) {
                    liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                } else {
                    liquidacion = itl.asignarUsuarioSecuenciasCertifiacadoSinFlujo(liquidacion.getId(), cajero);
                }
                return liquidacion;
            } else {
                return null;
            }
        } catch (NumberFormatException | ParseException e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public void iniciarProcesoActiviti(RegpLiquidacion liquidacion) {
        try {
            if (liquidacion != null) {
                if (liquidacion.getNumTramiteRp() != null) {
                    if (liquidacion.getTramiteOnline()) {
                        iniciarTramiteActivitiOnline(liquidacion);
                    } else if (!liquidacion.getCertificadoSinFlujo()) {
                        iniciarTramiteActiviti(liquidacion);
                    } else {
                        rps.guardarObservaciones(liquidacion.getTramite(), session.getName_user(), "EMISIÓN DE CERTIFICADO", Constantes.certificadoExpress);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void iniciarTramiteActiviti(RegpLiquidacion liquidacion) {
        Boolean result = rps.iniciarTramiteActiviti(liquidacion);
        if (result) {
            try {
                as.enviarCorreoInicioTramite(liquidacion);
            } catch (Exception e) {
                System.out.println("No se puedo enviar correo de inicio de tramite..");
            }
        } else {
            System.out.println("Proceso no se pudo iniciar");
        }
    }

    @Override
    public RegpLiquidacion reversarTramiteBanca(PagoReverso reverso) {
        HashMap<String, Object> pars = new HashMap<>();
        pars.put("liquidacion.numTramiteRp", Long.parseLong(reverso.getIdDeuda()));
        ConvenioBanco convenio = em.findObjectByParameter(ConvenioBanco.class, pars);
        Hibernate.initialize(convenio);
        Gson gson = new Gson();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        if (convenio != null) {
            RegpLiquidacion liquidacion = convenio.getLiquidacion();
            try {
                System.out.println("format.parse(reverso.getFechaProcesoIr()) " + format.parse(reverso.getFechaProcesoIr()));
                System.out.println("convenio.getFechaProcesoIr() " + convenio.getFechaProcesoIr());

                if (reverso.getFechaProcesoIr().equals(format.format(convenio.getFechaProcesoIr())) && !convenio.getFacturaAnulada()) {
                    if (!(new Double(reverso.getValorDeuda()).equals(new Double(convenio.getValorDeuda())))) {
                        reverso.setEstado("R003");
                        return null;
                    }
                    format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    liquidacion.setEstadoLiquidacion(new RegpEstadoLiquidacion(3L));
                    liquidacion.setEstadoPago(new RegpEstadoPago(5L));
                    convenio.setFacturaAnulada(Boolean.TRUE);
                    this.em.merge(liquidacion);

                    List<RegpTareasTramite> tareas = rps.getTareasTramite(liquidacion.getTramite().getId());
                    for (RegpTareasTramite tt : tareas) {
                        tt.setEstado(Boolean.FALSE);
                        em.merge(tt);
                    }

                    convenio.setDataJsReverso(gson.toJson(reverso));
                    convenio.setFechaProcesoReverso(format.parse(reverso.getFechaTransaccion()));
                    this.em.merge(convenio);
                    rps.guardarObservaciones(liquidacion.getTramite(), reverso.getIdUsuario(), "Finalizacion de tramite", "End Task");
                    this.engine.deleteProcessInstance(liquidacion.getTramite().getIdProceso(), "Eliminado en proceso de reverso - " + reverso.getFechaTransaccion());
                    reverso.setEstado("R001");
                    return liquidacion;
                } else if (convenio.getFacturaAnulada()) {
                    reverso.setEstado("R005");
                    return null;
                } else {
                    reverso.setEstado("R004");
                    return null;
                }
            } catch (ParseException e) {
                Logger.getLogger(VentanillaPubEjb.class.getName()).log(Level.SEVERE, null, e);
                reverso.setEstado("S001");
            }
        } else {
            reverso.setEstado("R002");
            return null;
        }
        return null;
    }

    @Override
    public File generarPDF(Long oid) {
        String filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaTemporales) + "DocumentoFirma_" + new Date().getTime() + ".pdf";
        File file = new File(filePdf);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ou.streamFile(oid, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}
