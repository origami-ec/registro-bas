/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.google.gson.Gson;
import com.origami.config.SisVars;
import com.origami.documental.entities.TbData;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.bpm.models.CantidadesTramites;
import com.origami.sgr.bpm.models.CantidadesUsuarios;
import com.origami.sgr.bpm.models.DataModel;
import com.origami.sgr.bpm.models.ReporteTramitesRp;
import com.origami.sgr.bpm.models.TareasSinRealizar;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ConsultaRegistralView;
import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.GeTipoTramite;
import com.origami.sgr.entities.GeneracionDocs;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegCertificadoMovimientoIntervinientes;
import com.origami.sgr.entities.RegCertificadoMovimientoReferencias;
import com.origami.sgr.entities.RegCertificadoPropietario;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegEstadoMovimiento;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaMarginacion;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCapital;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegMovimientoParticipante;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.entities.RegMovimientoRepresentante;
import com.origami.sgr.entities.RegMovimientoSocios;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.entities.SecuenciaRepertorio;
import com.origami.sgr.entities.UsuariosApp;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.historico.entities.IndiceProp;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.CertificadoModel;
import com.origami.sgr.models.ConsultaMovimientoModel;
import com.origami.sgr.models.IndiceRegistro;
import com.origami.sgr.models.MovimientoModel;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.models.ReporteDatosUsuario;
import com.origami.sgr.models.Sms;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import com.origami.sql.ConexionActiviti;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.origami.sgr.models.UsuarioRegistro;
import com.origami.sgr.util.NroTomoEnum;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;

/**
 *
 * @author Anyelo
 */
@Singleton(name = "registroPropiedad")
@Interceptors(value = {HibernateEjbInterceptor.class})
@Lock(LockType.READ)
public class RegistroPropiedadEjb extends BpmManageBeanBaseRoot implements RegistroPropiedadServices {

    @Inject
    private SeqGenMan sec;

    @Inject
    private BitacoraServices bs;

    @Inject
    private UserSession us;

    @Inject
    private RegCertificadoService rcs;

    /**
     * Retorna un modelo de datos con la informacion del movimiento registral y
     * todas sus relaciones consultando por el id de la base de datos
     *
     * @param idMov Object
     * @return Object
     */
    @Override
    public ConsultaMovimientoModel getConsultaMovimiento(Long idMov) {
        ConsultaMovimientoModel modelo = new ConsultaMovimientoModel();
        List<RegMovimientoCliente> listMovsClientes;
        List<RegFicha> listFichas;
        List<RegMovimientoReferencia> listMovsRef;
        List<RegMovimientoMarginacion> listMovMarg;
        List<RegMovimientoCapital> listMovcap;
        List<RegMovimientoRepresentante> listMovRep;
        List<RegMovimientoSocios> listMovSoc;

        try {

            listFichas = this.getRegFichaByIdRegMov(idMov);
            if (listFichas != null) {
                modelo.setFichas(listFichas);
            }

            listMovsRef = this.getRegMovRefByIdRegMov(idMov);
            if (listMovsRef != null) {
                modelo.setListMovRef(listMovsRef);
            }

            listMovsClientes = this.getRegMovClienteByIdMov(idMov);
            if (listMovsClientes != null) {
                modelo.setListMovCli(listMovsClientes);
            }

            listMovMarg = this.getRegMovMargByIdMov(idMov);
            if (listMovMarg != null) {
                modelo.setMarginaciones(listMovMarg);
            }

            listMovcap = this.getRegMovCapByIdMov(idMov);
            if (listMovcap != null) {
                modelo.setListMovCap(listMovcap);
            }

            listMovRep = this.getRegMovRepreByIdMov(idMov);
            if (listMovRep != null) {
                modelo.setListMovRep(listMovRep);
            }

            listMovSoc = this.getRegMovSocioByIdMov(idMov);
            if (listMovSoc != null) {
                modelo.setListMovSoc(listMovSoc);
            }

        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return modelo;
    }

    /**
     * devuelve la lista de fichas registrales enlazadas a una inscripcion
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegFicha> getRegFichaByIdRegMov(Long id) {
        List<RegFicha> fichas;
        try {
            fichas = manager.findAll(Querys.getRegFichasByMovimientoId, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return fichas;
    }

    /**
     * devuelve la lista de fichas registrales enlazadas a un mismo propietario
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegFicha> getBienesByPropietario(Long id) {
        List<RegFicha> fichas;
        try {
            fichas = manager.findAll(Querys.getRegFichasByPropietarioId, new String[]{"prop"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return fichas;
    }

    /**
     * devuelve todos los propietarios que pertenecen a una misma ficha
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegEnteInterviniente> getPropietariosByFicha(Long id) {
        List<RegEnteInterviniente> props;
        try {
            props = manager.findAll(Querys.getPropietariosByFichaId, new String[]{"ficha"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return props;
    }

    /**
     * retorna la lista de movimientos de referencia enlazados a una misma
     * inscripcion
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegMovimientoReferencia> getRegMovRefByIdRegMov(Long id) {
        List<RegMovimientoReferencia> referencias;
        try {
            referencias = manager.findAll(Querys.getRegMovimientoReferenciaByIdMov, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return referencias;
    }

    /**
     * devuelve la lista de objetos regmovimientoclientes enlazados a un mismo
     * movimiento
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegMovimientoCliente> getRegMovClienteByIdMov(Long id) {
        List<RegMovimientoCliente> list;
        try {
            list = manager.findAll(Querys.getRegMovimientoClienteByMovimiento, new String[]{"movid"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * devuelve una lista de regenteintervinientes que pertenencen a un mismo
     * movimiento
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegEnteInterviniente> getRegIntervsByIdMov(Long id) {
        List<RegEnteInterviniente> list;
        try {
            list = manager.findAll(Querys.getIntervinientesByMov, new String[]{"idmovimiento"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * devuelve una lista de objetos regmovimientoficha enlazados al mismo
     * movimiento
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegMovimientoFicha> getRegMovFichaByIdMov(Long id) {
        List<RegMovimientoFicha> list;
        try {
            list = manager.findAll(Querys.getRegMovFichasByMovimientoId, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<RegMovimientoCapital> getRegMovCapByIdMov(Long id) {
        List<RegMovimientoCapital> list;
        try {
            list = manager.findAll(Querys.getRegMovCapitByMovimientoId, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<RegMovimientoRepresentante> getRegMovRepreByIdMov(Long id) {
        List<RegMovimientoRepresentante> list;
        try {
            list = manager.findAll(Querys.getRegMovRepreByMovimientoId, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<RegMovimientoSocios> getRegMovSocioByIdMov(Long id) {
        List<RegMovimientoSocios> list;
        try {
            list = manager.findAll(Querys.getRegMovSocioByMovimientoId, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * retorna la lista de observaciones de marginacion enlazadas a un mismo
     * movimiento
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegMovimientoMarginacion> getRegMovMargByIdMov(Long id) {
        List<RegMovimientoMarginacion> list;
        try {
            list = manager.findAll(Querys.getRegMovMargByMovimientoId, new String[]{"idmov"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * devuelve una lista de objetos regmovimiento a los que esta enlazada una
     * ficha
     *
     * @param idFicha Object
     * @return Object
     */
    @Override
    public List<RegMovimiento> getMovimientosByFicha(Long idFicha) {
        List<RegMovimiento> list;
        try {
            list = manager.findAll(Querys.getMovimientosByIdFicha, new String[]{"idFicha"}, new Object[]{idFicha});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public List<RegFichaMarginacion> getMarginacionesByFicha(Long idFicha) {
        List<RegFichaMarginacion> list;
        try {
            list = manager.findAll(Querys.getMarginacionesByIdFicha, new String[]{"idFicha"}, new Object[]{idFicha});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public List<RegCertificadoMovimiento> getMovsByCertificado(Long id) {
        List<RegCertificadoMovimiento> list;
        try {
//            Map<String, Object> paramt = new HashMap<>();
//            paramt.put("certificado.id", id);
//            list = manager.findObjectByParameterOrderList(RegCertificadoMovimiento.class, paramt, new String[]{"movimiento.fechaInscripcion", "movimiento.numRepertorio"}, Boolean.FALSE);
            list = manager.findAll(Querys.getMovsByCertificado, new String[]{"id"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public List<RegCertificadoPropietario> getPropsByCertificado(Long id) {
        List<RegCertificadoPropietario> list;
        try {
            list = manager.findAll(Querys.getPropsByCertificado, new String[]{"id"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public RegMovimientoParticipante getParticipantesMov(Long id) {
        try {
            RegMovimientoParticipante mp = (RegMovimientoParticipante) manager.find(Querys.getParticipanteByMov, new String[]{"idmovimiento"}, new Object[]{id});
            return mp;
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * registra en la base de datos una ficha registral nueva y todas sus
     * relaciones
     *
     * @param ficha Object
     * @param props Object
     * @param mfs Object
     * @param margs Object
     * @return Object
     */
    @Override
    public RegFicha guardarFichaRegistral(RegFicha ficha, List<RegFichaPropietarios> props, List<RegMovimientoFicha> mfs, List<RegFichaMarginacion> margs) {
        BigInteger periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        try {
            if (ficha.getNumFicha() == null) {
                BigInteger numero;
                switch (ficha.getTipoFicha().getCodigo()) {
                    case 0:
                        numero = sec.getSecuenciaGeneral(Constantes.secuenciaNumeroFicha);
                        break;
                    case 1:
                        numero = sec.getSecuenciaGeneral(Constantes.secuenciaFichaMueble);
                        break;
                    case 2:
                        numero = sec.getSecuenciaGeneral(Constantes.secuenciaInterviniente);
                        break;
                    case 3:
                        numero = sec.getSecuenciaGeneral(Constantes.secuenciaInterviniente);
                        break;
                    default:
                        numero = sec.getSecuenciaGeneral(Constantes.secuenciaNumeroFicha);
                        break;
                }
                ficha.setNumFicha(numero.longValue());
            }
            if (ficha.getId() == null) {
                ficha = (RegFicha) manager.merge(ficha);
                bs.registrarFicha(ficha, ActividadesTransaccionales.GENERACION_FICHA, periodo, null);
            } else {
                bs.registrarFicha(ficha, ActividadesTransaccionales.MODIFICAR_FICHA, periodo, null);
                ficha = (RegFicha) manager.merge(ficha);
            }
            for (RegMovimientoFicha m : mfs) {
                if (m.getId() == null) {
                    m.setFicha(ficha);
                    manager.persist(m);
                    bs.registrarFichaMov(ficha, m.getMovimiento(), ActividadesTransaccionales.AGREGAR_REFERENCIA, periodo);
                }
                //manager.update(m.getMovimiento());
            }
            for (RegFichaPropietarios p : props) {
                if (p.getId() == null) {
                    p.setFicha(ficha);
                    manager.persist(p);
                    bs.registrarFichaProp(ficha, p.getPropietario(), ActividadesTransaccionales.AGREGAR_PROPIETARIO, periodo);
                }
            }
            /*for (RegFichaMarginacion m : margs) {
                if (m.getId() == null) {
                    m.setFicha(ficha);
                    manager.persist(m);
                }
            }*/
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return ficha;
    }

    /**
     * actualiza los datos de la ficha registral y las relaciones
     *
     * @param ficha Object
     * @param props Object
     * @param mfs Object
     * @param margs Object
     * @return Object
     */
    @Override
    public RegFicha editarFichaRegistral(RegFicha ficha, List<RegFichaPropietarios> props, List<RegMovimientoFicha> mfs, List<RegFichaMarginacion> margs) {
        BigInteger periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        try {
            ficha = (RegFicha) manager.persist(ficha);
            bs.registrarFicha(ficha, ActividadesTransaccionales.MODIFICAR_FICHA, periodo, null);
            for (RegMovimientoFicha m : mfs) {
                if (m.getId() == null) {
                    m.setFicha(ficha);
                    manager.persist(m);
                    bs.registrarFichaMov(ficha, m.getMovimiento(), ActividadesTransaccionales.AGREGAR_REFERENCIA, periodo);
                }
            }
            for (RegFichaPropietarios p : props) {
                if (p.getId() == null) {
                    p.setFicha(ficha);
                    manager.persist(p);
                    bs.registrarFichaProp(ficha, p.getPropietario(), ActividadesTransaccionales.AGREGAR_PROPIETARIO, periodo);
                }
            }
            /*for (RegFichaMarginacion m : margs) {
                if (m.getId() == null) {
                    m.setFicha(ficha);
                    manager.persist(m);
                }
            }*/
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return ficha;
    }

    /**
     * devuelve los papeles enlazados al acto que se consulta por id
     *
     * @param idacto Object
     * @return Object
     */
    @Override
    public Collection<RegPapel> getRegCatPapelByActo(Long idacto) {
        Collection<RegPapel> papels = null;
        RegActo acto;
        try {
            acto = (RegActo) manager.find(Querys.getRegCatPapelByActo, new String[]{"idacto"}, new Object[]{idacto});
            if (acto != null) {
                acto.getRegPapelCollection().size();
                papels = (Collection<RegPapel>) EntityBeanCopy.clone(acto.getRegPapelCollection());
                Hibernate.initialize(papels);
            }
        } catch (HibernateException e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return papels;
    }

    /**
     * guarda un registro en la tabla reg_ente_interviniente
     *
     * @param en Object
     * @return Object
     */
    @Override
    public RegEnteInterviniente saveInterviniente(RegEnteInterviniente en) {
        try {
            BigInteger cedRuc = sec.getSecuenciaEntes(Constantes.secuenciaInterviniente);
            en.setCedRuc(cedRuc.toString());
            en = (RegEnteInterviniente) manager.persist(en);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return en;
    }

    /**
     * consulta un reg_ente_interviente por numero de cedula y nombres
     *
     * @param cedula Object
     * @param nombre Object
     * @return Object
     */
    @Override
    public RegEnteInterviniente getInterviniente(String cedula, String nombre) {
        RegEnteInterviniente en;
        try {
            en = (RegEnteInterviniente) manager.find(Querys.getRegIntervByCedRucByNombre, new String[]{"cedula", "nombre"}, new Object[]{cedula, nombre});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return en;
    }

    /**
     * guarda en la base de datos un registro de una inscripcion en la tabla
     * reg_movimiento
     *
     * @param movimiento Object
     * @param movimientoReferenciaList Object
     * @param movimientoFichas Object
     * @param movimientoClientes Object
     * @return Object
     */
    @Override
    public Boolean guardarInscripcion(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList,
            List<RegMovimientoFicha> movimientoFichas, List<RegMovimientoCliente> movimientoClientes) {
        try {
            movimiento = (RegMovimiento) manager.persist(movimiento);
            if (!movimientoClientes.isEmpty()) {
                for (RegMovimientoCliente cn : movimientoClientes) {
                    cn.setMovimiento(movimiento);
                    manager.persist(cn);
                }
            }
            if (!movimientoFichas.isEmpty()) {
                for (RegMovimientoFicha acc : movimientoFichas) {
                    acc.setMovimiento(movimiento);
                    manager.persist(acc);
                }
            }
            if (!movimientoReferenciaList.isEmpty()) {
                for (RegMovimientoReferencia acc : movimientoReferenciaList) {
                    acc.setMovimiento(movimiento.getId());
                    manager.persist(acc);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /**
     * actualiza los datos de una inscripcion y todas sus relaciones
     *
     * @param movimiento Object
     * @param movimientoReferenciaList Object
     * @param movimientoFichas Object
     * @param movimientoClientes Object
     * @param mma Object
     * @param mm Object
     * @return Object
     */
    @Override
    public RegMovimiento guardarInscripcionEdicion(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList,
            List<RegMovimientoFicha> movimientoFichas, List<RegMovimientoCliente> movimientoClientes, List<RegMovimientoMarginacion> mma, MovimientoModel mm) {
        BigInteger periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        Boolean flag = false;
        try {
            if (movimiento.getId() == null) {
                flag = true;
            }
            movimiento = (RegMovimiento) manager.persist(movimiento);
            if (mm == null && flag) {
                bs.registrarMovimiento(null, movimiento, ActividadesTransaccionales.INGRESO_INSCRIPCION, periodo);
            } else if (mm != null) {
                bs.registrarMovimiento(mm, movimiento, ActividadesTransaccionales.MODIFICACION_INSCRIPCION, periodo);
            }
            if (!movimientoClientes.isEmpty()) {
                for (RegMovimientoCliente cn : movimientoClientes) {
                    if (cn.getDomicilio() != null && cn.getDomicilio().getId() == null) {
                        cn.setDomicilio(null);
                    }
                    if (cn.getChangeEnte()) {
                        manager.update(cn.getEnteInterv());
                    }
                    if (cn.getId() == null) {
                        cn.setMovimiento(movimiento);
                        manager.persist(cn);
                        bs.registrarMovInterv(movimiento, cn.getEnteInterv(), ActividadesTransaccionales.AGREGAR_INTERVINIENTE, periodo);
                    } else {
                        manager.update(cn);
                    }
                }
            }
            if (!movimientoFichas.isEmpty()) {
                for (RegMovimientoFicha acc : movimientoFichas) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento);
                        manager.persist(acc);
                        bs.registrarFichaMov(acc.getFicha(), movimiento, ActividadesTransaccionales.AGREGAR_REFERENCIA, periodo);
                    }
                }
            }
            if (!movimientoReferenciaList.isEmpty()) {
                for (RegMovimientoReferencia acc : movimientoReferenciaList) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento.getId());
                        manager.persist(acc);
                        //bs.registrarMovMov(movimiento, acc, periodo, ActividadesTransaccionales.AGREGAR_REFERENCIA);
                    } else {
                        manager.persist(acc.getMovimientoReferencia());
                    }
                }
            }
            if (mma != null && !mma.isEmpty()) {
                for (RegMovimientoMarginacion rm : mma) {
                    if (rm.getId() == null) {
                        rm.setMovimiento(movimiento);
                        manager.persist(rm);
                        bs.registrarMovMarginacion(movimiento, rm.getObservacion(), periodo);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return movimiento;
    }

    @Override
    public RegMovimiento guardarInscripcionEdicion(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList,
            List<RegMovimientoFicha> movimientoFichas, List<RegMovimientoCliente> movimientoClientes, List<RegMovimientoCapital> movimientoCapitales,
            List<RegMovimientoRepresentante> movimientoRepresentantes, List<RegMovimientoSocios> movimientoSocios, MovimientoModel mm) {
        BigInteger periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        Boolean flag = false;
        try {
            if (movimiento.getId() == null) {
                flag = true;
            }
            movimiento = (RegMovimiento) manager.persist(movimiento);
            if (mm == null && flag) {
                bs.registrarMovimiento(null, movimiento, ActividadesTransaccionales.INGRESO_INSCRIPCION, periodo);
            } else if (mm != null) {
                bs.registrarMovimiento(mm, movimiento, ActividadesTransaccionales.MODIFICACION_INSCRIPCION, periodo);
            }
            if (!movimientoClientes.isEmpty()) {
                for (RegMovimientoCliente cn : movimientoClientes) {
                    if (cn.getDomicilio() != null && cn.getDomicilio().getId() == null) {
                        cn.setDomicilio(null);
                    }
                    if (cn.getChangeEnte()) {
                        manager.update(cn.getEnteInterv());
                    }
                    if (cn.getId() == null) {
                        cn.setMovimiento(movimiento);
                        manager.persist(cn);
                        bs.registrarMovInterv(movimiento, cn.getEnteInterv(), ActividadesTransaccionales.AGREGAR_INTERVINIENTE, periodo);
                    } else {
                        manager.update(cn);
                    }
                }
            }
            if (!movimientoFichas.isEmpty()) {
                for (RegMovimientoFicha acc : movimientoFichas) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento);
                        manager.persist(acc);
                        bs.registrarFichaMov(acc.getFicha(), movimiento, ActividadesTransaccionales.AGREGAR_REFERENCIA, periodo);
                    }
                }
            }
            if (!movimientoReferenciaList.isEmpty()) {
                for (RegMovimientoReferencia acc : movimientoReferenciaList) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento.getId());
                        manager.persist(acc);
                        bs.registrarMovMov(movimiento, acc, periodo, ActividadesTransaccionales.AGREGAR_REFERENCIA);
                    } else {
                        manager.persist(acc.getMovimientoReferencia());
                    }
                }
            }
            if (!movimientoCapitales.isEmpty()) {
                for (RegMovimientoCapital acc : movimientoCapitales) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento);
                        manager.persist(acc);
                    } else {
                        manager.update(acc);
                    }
                }
            }
            if (!movimientoRepresentantes.isEmpty()) {
                for (RegMovimientoRepresentante acc : movimientoRepresentantes) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento);
                        manager.persist(acc);
                    } else {
                        manager.update(acc);
                    }
                }
            }
            if (!movimientoSocios.isEmpty()) {
                for (RegMovimientoSocios acc : movimientoSocios) {
                    if (acc.getId() == null) {
                        acc.setMovimiento(movimiento);
                        manager.persist(acc);
                    } else {
                        manager.update(acc);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return movimiento;
    }

    /**
     * retorna una lista de objetos reg_movimiento enlazados a un interviniete,
     * consultando por cedula
     *
     * @param cedula Object
     * @return Object
     */
    @Override
    public List<RegMovimiento> getListMovimientosByCedInterv(String cedula) {
        List<RegMovimiento> movs;
        try {
            movs = manager.findAll(Querys.getListMovsByCedRucInterv, new String[]{"codigo"}, new Object[]{cedula});
            if (movs == null) {
                movs = new ArrayList<>();
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return movs;
    }

    /**
     * retorna una lista de objetos reg_movimiento consultando por el numero de
     * tramite
     *
     * @param tramite Object
     * @return Object
     */
    @Override
    public List<RegMovimientoCliente> getListMovimientosByTramite(Long tramite) {
        List<RegMovimientoCliente> movs;
        try {
            movs = manager.findAll(Querys.getListMovsByTramite, new String[]{"numero"}, new Object[]{tramite});
            if (movs == null) {
                movs = new ArrayList<>();
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return movs;
    }

    /**
     * devuelve un objeto collection que contiene una lista de pk de los
     * movimientos relacionados a un interviniente consultando por el numero de
     * cedula
     *
     * @param documento Object
     * @return Object
     */
    @Override
    public Collection getListIdMovsByCedRucInterv(String documento) {
        Collection values;
        try {
            values = manager.findAll(Querys.getListIdMovsByCedRucInterv, new String[]{"codigo"}, new Object[]{documento});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return values;
    }
    
    @Override
    public Collection getListIdMovsByCedRucIntervTipoLibro(String documento, Integer tipoLibro) {
        Collection values;
        try {
            values = manager.findAll(Querys.getListIdMovsByCedRucIntervTipoLibro, new String[]{"codigo", "tipoLibro"},
                    new Object[]{documento, tipoLibro});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return values;
    }

    /**
     * consulta la mayor fecha de inscripcion registrada
     *
     * @return Object
     */
    @Override
    public Date getFechaInscripcionMayor() {
        Date fecha;
        try {
            fecha = (Date) manager.find(Querys.getFechaInscripcionMayor);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return fecha;
    }

    /**
     * consulta la menor fecha de inscripcion ingresada
     *
     * @return Object
     */
    @Override
    public Date getFechaInscripcionMenor() {
        Date fecha;
        try {
            fecha = (Date) manager.find(Querys.getFechaInscripcionMenor);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return fecha;
    }

    /**
     * devuelve un objeto collection que contiene una lista de pk de las fichas
     * relacionadas a un interviniente consultando por el numero de cedula
     *
     * @param documento Object
     * @return Object
     */
    @Override
    public Collection getListIdFichasByDocInterv(String documento) {
        Collection<BigInteger> values;
        Collection list = new ArrayList();
        try {
            values = manager.getSqlQuery("select distinct mf.ficha from app.reg_movimiento_ficha mf "
                    + "inner join app.reg_movimiento_cliente mc on(mc.movimiento = mf.movimiento) "
                    + "inner join app.reg_ente_interviniente en on(en.id = mc.ente_interv) "
                    + "where en.ced_ruc = '" + documento + "'");
            if (values != null) {
                for (BigInteger temp : values) {
                    list.add(temp.longValue());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    /**
     * devuelve la lista de reg_movimiento_ficha consultando por el id de la
     * ficha
     *
     * @param id Object
     * @return Object
     */
    @Override
    public List<RegMovimientoFicha> getRegMovByIdFicha(Long id) {
        List<RegMovimientoFicha> list;
        try {
            list = manager.findAll(Querys.getRegMovimientoFichaByFicha, new String[]{"idFicha"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    /**
     * retorna el nombre del papel que tiene ingresado el interviniente en la
     * inscripcion que se encuentra registrado en la tabla
     * reg_ente_interviniente
     *
     * @param mov Object
     * @param inter Object
     * @return Object
     */
    @Override
    public String getPapelByMovimientoInterviniente(Long mov, Long inter) {
        String papel;
        try {
            papel = (String) manager.find(Querys.getPapelByMovimientoInterviniente, new String[]{"mov", "inter"}, new Object[]{mov, inter});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return papel;
    }

    /**
     * retorna el nombre del papel que tiene ingresado el interviniente en la
     * inscripcion que se encuentra registrado en la tabla
     * reg_ente_interviniente
     *
     * @param mov Object
     * @param doc Object
     * @return Object
     */
    @Override
    public String getPapelByMovAndDocumentoInterv(Long mov, String doc) {
        String papel;
        try {
            papel = (String) manager.find(Querys.getPapelByMovYDocInterv, new String[]{"mov", "doc"}, new Object[]{mov, doc});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return papel;
    }

    /**
     * encuentra el objeto libro consultando por codigo anterior
     *
     * @param codigo Object
     * @return Object
     */
    @Override
    public String getLibroByCodigo(Long codigo) {
        String libro;
        try {
            libro = (String) manager.find(Querys.getLibroByCodigoAnterior, new String[]{"codigo"}, new Object[]{codigo});
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return "";
        }
        return libro;
    }

    /**
     * devuelve el listado de tareas internas de cada tramite, se refire a cada
     * contrato ingresado en la proforma
     *
     * @param idTramite Object
     * @return Object
     */
    @Override
    public List<RegpTareasTramite> getTareasTramite(Long idTramite) {
        Map<String, Object> map;
        HistoricoTramites ht;
        RegpLiquidacion liquidacion;
        RegpTareasTramite ta;
        List<RegpTareasTramite> tareas = new ArrayList<>();
        try {
            ht = manager.find(HistoricoTramites.class, idTramite);
            if (!ht.getRegpTareasTramiteCollection().isEmpty()) {
                tareas = (List<RegpTareasTramite>) ht.getRegpTareasTramiteCollection();
                //System.out.println("// tareas: " + tareas);
            } else {
                map = new HashMap();
                map.put("numTramiteRp", ht.getNumTramite());
                liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                //System.out.println("// detalles: " + liquidacion.getRegpLiquidacionDetallesCollection());
                if (liquidacion != null) {
                    for (RegpLiquidacionDetalles de : liquidacion.getRegpLiquidacionDetallesCollection()) {
                        ta = new RegpTareasTramite();
                        ta.setFecha(de.getFechaIngreso());
                        ta.setDetalle(de);
                        ta.setTramite(ht);
                        ta.setEstado(Boolean.TRUE);
                        ta.setRealizado(Boolean.FALSE);
                        ta = (RegpTareasTramite) manager.persist(ta);
                        tareas.add(ta);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return tareas;
    }

    /**
     * busca la inscripcion que le corresponde a la subtarea de un tramite
     *
     * @param tarea Object
     * @return Object
     */
    @Override
    public RegMovimiento getMovimientoInscripcion(Long tarea) {
        Map<String, Object> map;
        RegMovimiento mov;
        RegpTareasTramite tt;
        //RegEnteInterviniente en;
        //RegMovimientoCliente mc;
        try {
            tt = manager.find(RegpTareasTramite.class, tarea);
            map = new HashMap();
            map.put("tramite", tt);
            mov = (RegMovimiento) manager.findObjectByParameter(RegMovimiento.class, map);
            if (mov == null) {
                mov = new RegMovimiento();
                mov.setActo(tt.getDetalle().getActo());
                mov.setLibro(mov.getActo().getLibro());
                mov.setEnteJudicial(tt.getDetalle().getLiquidacion().getEnteJudicial());
                mov.setNumRepertorio(tt.getDetalle().getLiquidacion().getRepertorio());
                mov.setFechaRepertorio(tt.getDetalle().getLiquidacion().getFechaRepertorio());
                mov.setFechaOto(tt.getDetalle().getLiquidacion().getFechaOto());
                mov.setDomicilio(tt.getDetalle().getLiquidacion().getDomicilio());
                mov.setOrdJud(tt.getDetalle().getLiquidacion().getOrdJud());
                mov.setFechaResolucion(tt.getDetalle().getLiquidacion().getFechaResolucion());
                mov.setEscritJuicProvResolucion(tt.getDetalle().getLiquidacion().getEscritJuicProvResolucion());
                mov.setUserCreador(tt.getDetalle().getLiquidacion().getInscriptor());
                mov.setValorUuid(Utils.getValorUuid());
                mov.setTramite(tt);
                mov = (RegMovimiento) manager.persist(mov);
                Hibernate.initialize(mov);
                /*for (RegpIntervinientes in : tt.getDetalle().getRegpIntervinientesCollection()) {
                    map = new HashMap();
                    map.put("ced_ruc", in.getEnte().getCiRuc());
                    en = (RegEnteInterviniente) manager.findObjectByParameter(RegEnteInterviniente.class, map);
                    if (en == null) {
                        en = new RegEnteInterviniente();
                        en.setCedRuc(in.getEnte().getCiRuc());
                        en.setNombre(in.getEnte().getNombreCompleto());
                    }
                    mc = new RegMovimientoCliente();
                    if (in.getPapel() != null) {
                        mc.setPapel(in.getPapel());
                    }
                    mc.setEnteInterv(en);
                    mc.setMovimiento(mov);
                    mc.setEnte(in.getEnte());
                }*/
            }
        } catch (HibernateException e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new RegMovimiento();
        }
        return mov;
    }

    /**
     * consulta la cantidad de movimientos por anio y libro ingresados en la
     * base de datos
     *
     * @param anio Object
     * @param libroId Object
     * @return Object
     */
    @Override
    public BigInteger cantidadMovimientosXanioYlibro(Integer anio, Long libroId) {
        BigInteger cantidad;
        try {
            cantidad = (BigInteger) manager.getNativeQuery("SELECT count(*) FROM app.reg_movimiento m WHERE m.estado = 'AC' and m.libro=" + libroId + " and to_char(m.fecha_inscripcion, 'yyyy')='" + anio + "'");
            if (cantidad == null) {
                cantidad = BigInteger.ZERO;
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return BigInteger.ZERO;
        }
        return cantidad;
    }

    /**
     * trae la lista de objetos reg_movimiento que corresponden a un mismo anio
     * y a un mismo libro
     *
     * @param anio Object
     * @param idLibro Object
     * @return Object
     */
    @Override
    public List<RegMovimiento> getRegMovimientosPorLibroAnio(Integer anio, Long idLibro) {
        List<RegMovimiento> regMovimientos;
        BigInteger cant = this.cantidadMovimientosXanioYlibro(anio, idLibro);
        int valor;
        if (cant.compareTo(BigInteger.TEN) > 0) {
            valor = cant.subtract(BigInteger.TEN).intValue();
        } else {
            valor = 0;
        }
        try {
            regMovimientos = manager.findFirstAndMaxResult(Querys.getRegMovimientosPorLibroAnio, new String[]{"anio", "libroid"}, new Object[]{anio.toString(), idLibro}, valor, 10);
            if (regMovimientos == null) {
                regMovimientos = new ArrayList<>();
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return regMovimientos;
    }

    /**
     * registra en la base de datos una inscripcion nueva
     *
     * @param movimiento Object
     * @param movimientoReferenciaList Object
     * @return Object
     */
    @Override
    public RegMovimiento guardarMovimientoNuevo(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList) {
        List<RegMovimientoFicha> movimientoFichas = (List<RegMovimientoFicha>) movimiento.getRegMovimientoFichaCollection();
        List<RegMovimientoCliente> movimientoCliente = (List<RegMovimientoCliente>) movimiento.getRegMovimientoClienteCollection();

        movimiento.setRegMovimientoFichaCollection(null);
        movimiento.setRegMovimientoClienteCollection(null);
        //RegMovimientoReferencia referencia;
        try {
            if (movimiento.getValorUuid() == null) {
                movimiento.setValorUuid(Utils.getValorUuid());
            }
            movimiento.setRazonImpresa(Boolean.FALSE);
            movimiento.setInscripcionImpresa(Boolean.FALSE);
            movimiento.setEstado("AC");
            //movimiento.setLibro(movimiento.getActo().getLibro());

            movimiento = (RegMovimiento) manager.merge(movimiento);

            if (!movimientoCliente.isEmpty()) {
                for (RegMovimientoCliente mc : movimientoCliente) {
                    if (mc.getId() == null) {
                        mc.setMovimiento(movimiento);
                        manager.persist(mc);
                    } else {
                        manager.update(mc);
                    }
                }
            }

            if (!movimientoFichas.isEmpty()) {
                for (RegMovimientoFicha mf : movimientoFichas) {
                    if (mf.getId() == null) {
                        mf.setMovimiento(movimiento);
                        manager.persist(mf);
                    }
                }
            }

            if (!movimientoReferenciaList.isEmpty()) {
                for (RegMovimientoReferencia mr : movimientoReferenciaList) {
                    manager.update(mr.getMovimientoReferencia());
                    if (mr.getId() == null) {
                        mr.setMovimiento(movimiento.getId());
                        manager.persist(mr);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movimiento;
    }

    @Override
    public RegMovimiento guardarMovimientoNuevoCompleto(RegMovimiento movimiento, List<RegMovimientoReferencia> mrfs) {
        List<RegMovimientoCliente> mcs = (List<RegMovimientoCliente>) movimiento.getRegMovimientoClienteCollection();
        List<RegMovimientoFicha> mfs = (List<RegMovimientoFicha>) movimiento.getRegMovimientoFichaCollection();
        List<RegMovimientoCapital> mcps = (List<RegMovimientoCapital>) movimiento.getRegMovimientoCapitalCollection();
        List<RegMovimientoRepresentante> mres = (List<RegMovimientoRepresentante>) movimiento.getRegMovimientoRepresentanteCollection();
        List<RegMovimientoSocios> msos = (List<RegMovimientoSocios>) movimiento.getRegMovimientoSociosCollection();

        movimiento.setRegMovimientoFichaCollection(null);
        movimiento.setRegMovimientoClienteCollection(null);
        movimiento.setRegMovimientoCapitalCollection(null);
        movimiento.setRegMovimientoRepresentanteCollection(null);
        movimiento.setRegMovimientoSociosCollection(null);

        try {
            if (movimiento.getValorUuid() == null) {
                movimiento.setValorUuid(Utils.getValorUuid());
            }
            if (movimiento.getCodVerificacion() == null) {
                movimiento.setCodVerificacion(rcs.genCodigoVerif());
            }
            movimiento.setRazonImpresa(Boolean.FALSE);
            movimiento.setInscripcionImpresa(Boolean.FALSE);
            movimiento.setEstado("AC");
            movimiento = (RegMovimiento) manager.merge(movimiento);

            if (!mcs.isEmpty()) {
                for (RegMovimientoCliente mc : mcs) {
                    if (mc.getId() == null) {
                        mc.setMovimiento(movimiento);
                        manager.persist(mc);
                    } else {
                        manager.update(mc);
                    }
                }
            }

            if (!mfs.isEmpty()) {
                for (RegMovimientoFicha mf : mfs) {
                    if (mf.getId() == null) {
                        mf.setMovimiento(movimiento);
                        manager.persist(mf);
                    }
                }
            }

            if (!mcps.isEmpty()) {
                for (RegMovimientoCapital mc : mcps) {
                    if (mc.getId() == null) {
                        mc.setMovimiento(movimiento);
                        manager.persist(mc);
                    }
                }
            }

            if (!mres.isEmpty()) {
                for (RegMovimientoRepresentante mr : mres) {
                    if (mr.getId() == null) {
                        mr.setMovimiento(movimiento);
                        manager.persist(mr);
                    } else {
                        manager.update(mr);
                    }
                }
            }

            if (!msos.isEmpty()) {
                for (RegMovimientoSocios ms : msos) {
                    if (ms.getId() == null) {
                        ms.setMovimiento(movimiento);
                        manager.persist(ms);
                    } else {
                        manager.update(ms);
                    }
                }
            }

            if (!mrfs.isEmpty()) {
                for (RegMovimientoReferencia mr : mrfs) {
                    manager.update(mr.getMovimientoReferencia());
                    if (mr.getId() == null) {
                        mr.setMovimiento(movimiento.getId());
                        manager.persist(mr);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movimiento;
    }

    /**
     * devuelve todos los usarios que tienen relacionado un rol que se busca por
     * el nombre
     *
     * @param nombre Object
     * @return Object
     */
    @Override
    public List<AclUser> getUsuariosByRolName(String nombre) {
        List<AclUser> list = new ArrayList<>();
        Map<String, Object> mapa = new HashMap<>();
        if (nombre != null) {
            mapa.put("nombre", nombre);
        }
        mapa.put("estado", Boolean.TRUE);
        try {
            AclRol rol = manager.findObjectByParameter(AclRol.class, mapa);
            if (rol != null && rol.getAclUserCollection() != null) {
                list = (List<AclUser>) rol.getAclUserCollection();
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public AclUser getByRolNameCajero(String username) {

        AclUser list;
        Map<String, Object> mapa = new HashMap<>();
        if (username != null) {
            mapa.put("usuario", username);
        }
//        mapa.put("estado", Boolean.TRUE);
        try {
            AclUser rol = manager.findObjectByParameter(AclUser.class, mapa);
            list = rol;
        } catch (Exception e) {
//            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new AclUser();
        }
        return list;
    }

    @Override
    public Long getIdByRolName(String nombre) {
        Long regreso = null;
        Map<String, Object> mapa = new HashMap<>();
        if (nombre != null) {
            mapa.put("nombre", nombre);
        }
        mapa.put("estado", Boolean.TRUE);
        try {
            AclRol rol = manager.findObjectByParameter(AclRol.class, mapa);
            regreso = rol.getId();
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return regreso;
        }
        return regreso;
    }

    /**
     * consultando por el nombre del rol retorna una cadena con los nombres de
     * usuarios que poseen ese rol concatenados con comas
     *
     * @param nombre Object
     * @return Object
     */
    @Override
    public String getCandidateUserByRolName(String nombre) {
        String cadena = "";
        List<AclUser> list;
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("estado", Boolean.TRUE);
        try {
            //AclRol rol = manager.find(AclRol.class, id);
            AclRol rol = manager.findObjectByParameter(AclRol.class, map);
            list = (List<AclUser>) rol.getAclUserCollection();
            for (AclUser user : list) {
                cadena = cadena + "," + user.getUsuario();
            }
            cadena = cadena.substring(1);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return cadena;
    }

    /**
     * registra en la base de datos las observaciones que se ingresan para los
     * tramites
     *
     * @param ht Object
     * @param nameUser Object
     * @param observaciones Object
     * @param taskDefinitionKey Object
     * @return Object
     */
    @Override
    public Observaciones guardarObservaciones(HistoricoTramites ht, String nameUser, String observaciones, String taskDefinitionKey) {
        try {
            Observaciones observ = new Observaciones();
            observ.setEstado(true);
            observ.setFecCre(new Date());
            observ.setTarea(taskDefinitionKey);
            observ.setIdTramite(ht);
            observ.setUserCre(nameUser);
            observ.setObservacion(observaciones);
            observ.setAclLogin(us.getIdAclLogin());
            return (Observaciones) manager.persist(observ);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * devuelve el nombre completo de la persona consultando por el nombre de
     * usuario registrado en la base
     *
     * @param user Object
     * @return Object
     */
    @Override
    public String getNombreByUserName(String user) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("usuario", user);
            AclUser u = manager.findObjectByParameter(AclUser.class, map);
            if (u != null && u.getEnte() != null) {
                return u.getEnte().getNombreCompleto();
            } else {
                return user;
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return user;
        }
    }

    /**
     * consulta los tramites por fechas de ingreso devolviendo una lista de
     * modelos de datos
     *
     * @param desde Object
     * @param hasta Object
     * @return Object
     */
    @Override
    public List<ReporteTramitesRp> getTareasByTramite(Date desde, Date hasta) {
        List<ReporteTramitesRp> list = new ArrayList<>();
        List<HistoricoTramites> listHt;
        List<HistoricTaskInstance> htis;
        ReporteTramitesRp rt;
        String query;
        try {
            listHt = manager.findAll(Querys.getHistoricoTramitesByFechasIngreso, new String[]{"inicio", "fin"}, new Object[]{desde, hasta});
            for (HistoricoTramites h : listHt) {
                query = "select ht.* from act_hi_taskinst ht where proc_inst_id_ = '" + h.getIdProceso() + "' order by start_time_";
                htis = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).list();
                for (HistoricTaskInstance hi : htis) {
                    rt = new ReporteTramitesRp();
                    rt.setAssigne(hi.getAssignee());
                    rt.setNombre(hi.getName());
                    rt.setFechainicio(hi.getStartTime());
                    rt.setFechafin(hi.getEndTime());
                    rt.setNumtramite(h.getNumTramite());
                    rt.setIdprocess(h.getIdProceso());
                    rt.setCliente(h.getSolicitante().getNombreCompleto());
                    if (hi.getAssignee() != null) {
                        rt.setUsuario(this.getNombreByUserName(hi.getAssignee()));
                    }
                    list.add(rt);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    /**
     * consulta al activiti las tareas asignadas a los usuarios por rangos de
     * fecha
     *
     * @param desde Object
     * @param hasta Object
     * @return Object
     */
    @Override
    public List<ReporteTramitesRp> getTareasByUsuarios(String desde, String hasta) {
        Map<String, Object> map = new HashMap<>();
        List<ReporteTramitesRp> list = new ArrayList<>();
        HistoricoTramites ht;
        List<HistoricTaskInstance> htis;
        ReporteTramitesRp rt;
        String query;
        try {
            query = "select ht.* from act_hi_taskinst ht where end_time_ between to_date('" + desde + "', 'dd/MM/yyyy') "
                    + "and to_date('" + hasta + "', 'dd/MM/yyyy') order by assignee_, end_time_";
            htis = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).list();
            for (HistoricTaskInstance hi : htis) {
                map.put("idProceso", hi.getExecutionId());
                ht = manager.findObjectByParameter(HistoricoTramites.class, map);
                if (ht != null) {
                    rt = new ReporteTramitesRp();
                    rt.setAssigne(hi.getAssignee());
                    rt.setNombre(hi.getName());
                    rt.setFechainicio(hi.getStartTime());
                    rt.setFechafin(hi.getEndTime());
                    rt.setNumtramite(ht.getNumTramite());
                    rt.setIdprocess(ht.getIdProceso());
                    rt.setCliente(ht.getSolicitante().getNombreCompleto());
                    rt.setFechaentrega(ht.getFechaEntrega());
                    if (hi.getAssignee() != null) {
                        rt.setUsuario(this.getNombreByUserName(hi.getAssignee()));
                    }
                    list.add(rt);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public List<ReporteTramitesRp> getTareasRealizadasByUsuario(String usuario, String desde, String hasta) {
        Map<String, Object> map = new HashMap<>();
        List<ReporteTramitesRp> list = new ArrayList<>();
        HistoricoTramites ht;
        List<HistoricTaskInstance> htis;
        ReporteTramitesRp rt;
        String query;
        try {
            query = "select ht.* from act_hi_taskinst ht where assignee_ = '" + usuario
                    + "' and end_time_ between to_date('" + desde + "', 'dd/MM/yyyy') "
                    + "and to_date('" + hasta + "', 'dd/MM/yyyy') order by assignee_, end_time_";
            htis = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).list();
            for (HistoricTaskInstance hi : htis) {
                map.put("idProceso", hi.getProcessInstanceId());
                ht = manager.findObjectByParameter(HistoricoTramites.class, map);
                if (ht != null) {
                    rt = new ReporteTramitesRp();
                    rt.setAssigne(hi.getAssignee());
                    rt.setNombre(hi.getName());
                    rt.setFechainicio(hi.getStartTime());
                    rt.setFechafin(hi.getEndTime());
                    rt.setNumtramite(ht.getNumTramite());
                    rt.setIdprocess(ht.getIdProceso());
                    rt.setCliente(ht.getSolicitante().getNombreCompleto());
                    rt.setFechaentrega(ht.getFechaEntrega());
                    if (hi.getAssignee() != null) {
                        rt.setUsuario(this.getNombreByUserName(hi.getAssignee()));
                    }
                    list.add(rt);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    /**
     * consulta desde el activiti la cantidad de tramites por cada usuario
     *
     * @param desde Object
     * @param hasta Object
     * @return Object
     */
    @Override
    public List<CantidadesUsuarios> getTotalTramitesByUsuarios(String desde, String hasta) {
        List<CantidadesUsuarios> list = new ArrayList<>();
        List<AclUser> users;
        CantidadesUsuarios cu;
        try {
            users = manager.findAllOrdered(AclUser.class, new String[]{"usuario"}, new Boolean[]{true});
            for (AclUser u : users) {
                Long l = engine.getProcessEngine().getTaskService().createTaskQuery().taskAssignee(u.getUsuario()).count();
                if (l.intValue() > 0) {
                    cu = new CantidadesUsuarios();
                    cu.setUser(u.getUsuario());
                    cu.setName(this.getNombreByUserName(u.getUsuario()));
                    cu.setTareas(l.intValue());
                    cu.setFichas(this.getTotalFichaByUser(u.getUsuario(), desde, hasta));
                    cu.setMovimientos(this.getTotalMovimientosByUser(u.getId(), desde, hasta));
                    list.add(cu);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public Integer getTotalFichaByUser(String user, String desde, String hasta) {
        try {
            BigInteger tmp = (BigInteger) manager.getNativeQuery(Querys.getTotalFichasByUser, new Object[]{user, desde, hasta});
            return tmp.intValue();
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    public Integer getTotalMovimientosByUser(Long user, String desde, String hasta) {
        try {
            BigInteger tmp = (BigInteger) manager.getNativeQuery(Querys.getTotalMovimientosByUser, new Object[]{user, desde, hasta});
            return tmp.intValue();
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    /**
     * consulta las cantidades de actos ingresadas por tramite entre un rango de
     * fechas
     *
     * @param desde Object
     * @param hasta Object
     * @return Object
     */
    @Override
    public List<DataModel> getCantidadesByActo(String desde, String hasta) {
        List<DataModel> list = new ArrayList<>();
        List<RegActo> actos;
        DataModel dm;
        try {
            actos = manager.findAll(Querys.getActosByFechaIngreso, new String[]{"desde", "hasta"}, new Object[]{desde, hasta});
            for (RegActo ac : actos) {
                dm = new DataModel();
                dm.setId(ac.getId());
                dm.setNombre(ac.getNombre());
                dm.setCantidad1(this.getTotalContratosIngresados(ac.getId(), desde, hasta));
                list.add(dm);
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    public Integer getTotalContratosIngresados(Long acto, String desde, String hasta) {
        try {
            BigInteger tmp = (BigInteger) manager.getNativeQuery(Querys.getTotalIngresadosByContrato, new Object[]{acto, desde, hasta});
            return tmp.intValue();
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    /**
     * consulta los tramites asignados a un usuario por rangos de fecha
     *
     * @param desde Object
     * @param hasta Object
     * @param user Object
     * @return Object
     */
    @Override
    public List<ReporteTramitesRp> getTramitesByUser(Date desde, Date hasta, String user) {
        List<ReporteTramitesRp> list = new ArrayList<>();
        HistoricoTramites ht;
        ReporteTramitesRp rt;
        try {
            ArrayList<Task> tareasAct = (ArrayList<Task>) engine.getUsertasksList(user, null);
            ArrayList<Task> temp = (ArrayList<Task>) engine.getCandidateUsertasksList(user);
            for (Task t : temp) {
                tareasAct.add(t);
            }
            if (tareasAct.size() > 0) {
                for (Task task : tareasAct) {
                    ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteByIdProcessFechaEntrega, new String[]{"idprocess", "desde", "hasta"}, new Object[]{task.getProcessInstanceId(), desde, hasta});
                    if (ht != null) {
                        rt = new ReporteTramitesRp();
                        rt.setNumtramite(ht.getNumTramite());
                        rt.setCliente(ht.getNombrePropietario());
                        rt.setFechainicio(ht.getFechaIngreso());
                        rt.setFechafin(ht.getFechaEntrega());
                        list.add(rt);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * guarda un registro nuevo en la tabla reg_certificado que corresponde a un
     * certificado generado en el sistema
     *
     * @param ce Object
     * @return Object
     */
    @Override
    public RegCertificado saveCertificadoHistoriaDominio(RegCertificado ce) {
        RegCertificadoMovimiento cm;
        RegCertificadoPropietario cp;
        RegCertificadoMovimientoIntervinientes rcmi;
        RegCertificadoMovimientoReferencias rcmr;
        try {
            if (ce.getFicha() != null) {
                ce.setLinderosRegistrales(ce.getFicha().getLinderos());
                if (ce.getFicha().getParroquia() != null) {
                    ce.setParroquia(ce.getFicha().getParroquia().getDescripcion());
                }
                if (ce.getFicha().getClaveCatastral() != null) {
                    ce.setClaveCatastral(ce.getFicha().getClaveCatastral());
                }
                if (ce.getFicha().getUrlFoto() != null) {
                    ce.setUrlFoto(ce.getFicha().getUrlFoto());
                }
            }
            ce.setCertificadoImpreso(Boolean.TRUE);
            ce = (RegCertificado) manager.merge(ce);
            if (ce.getFicha() != null) {
                for (RegMovimientoFicha mf : ce.getFicha().getRegMovimientoFichaCollection()) {
                    cm = this.llenarModeloMovimiento(mf.getMovimiento());
                    cm.setCertificado(ce);
                    cm.setMovimiento(mf.getMovimiento());
                    cm = (RegCertificadoMovimiento) manager.persist(cm);
                    for (RegMovimientoCliente mc : mf.getMovimiento().getRegMovimientoClienteCollection()) {
                        rcmi = this.llenarModeloCliente(mc);
                        rcmi.setMovimientoCertificado(cm);
                        manager.persist(rcmi);
                    }
                    for (RegMovimientoReferencia mr : this.getRegMovRefByIdRegMov(mf.getMovimiento().getId())) {
                        rcmr = this.llenarModeloReferencia(mr.getMovimientoReferencia());
                        rcmr.setMovimientoCertificado(cm);
                        manager.persist(rcmr);
                    }
                }
                for (RegFichaPropietarios fp : ce.getFicha().getRegFichaPropietariosCollection()) {
                    if (fp.getPropietario() != null) {
                        cp = new RegCertificadoPropietario();
                        cp.setCertificado(ce);
                        cp.setDocumento(fp.getPropietario().getCedRuc());
                        cp.setNombres(fp.getPropietario().getNombre());
                        cp.setPropietario(fp.getPropietario());
                        cp.setCalidad(fp.getCalidad());
                        manager.persist(cp);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return ce;
    }

    @Override
    public RegCertificado saveCertificadoRazon(RegCertificado ce, RegCertificadoMovimiento rcm) {
        try {
            ce = (RegCertificado) manager.merge(ce);
            rcm.setCertificado(ce);
            manager.merge(rcm);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return ce;
    }

    @Override
    public RegCertificado saveCertificadoRazon(RegCertificado ce) {
        RegCertificadoMovimientoIntervinientes rcmi;
        RegCertificadoMovimientoReferencias rcmr;
        try {
            List<RegCertificadoMovimiento> cmovs = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ce.setRegCertificadoMovimientoCollection(null);

            ce = (RegCertificado) manager.merge(ce);
            for (RegCertificadoMovimiento mov : cmovs) {
                if (mov.getId() == null) {
                    if (ce.getRegCertificadoMovimientoCollection() == null) {
                        ce.setRegCertificadoMovimientoCollection(new ArrayList<>());
                    }
                    mov = this.llenarModeloMovimiento(mov);
                    mov.setCertificado(ce);
                    mov = (RegCertificadoMovimiento) manager.merge(mov);
                    ce.getRegCertificadoMovimientoCollection().add(mov);
                    for (RegMovimientoCliente mc : mov.getMovimiento().getRegMovimientoClienteCollection()) {
                        if (mov.getRegCertificadoMovimientoIntervinientesCollection() == null) {
                            mov.setRegCertificadoMovimientoIntervinientesCollection(new ArrayList<>());
                        }
                        rcmi = this.llenarModeloCliente(mc);
                        rcmi.setMovimientoCertificado(mov);
                        mov.getRegCertificadoMovimientoIntervinientesCollection().add((RegCertificadoMovimientoIntervinientes) manager.merge(rcmi));
                    }
                    //EN ESTA TABLAS SE GUARDAN LAS FICHAS DEL MOVIMIENTO
                    for (RegMovimientoFicha mf : mov.getMovimiento().getRegMovimientoFichaCollection()) {
                        if (mov.getRegCertificadoMovimientoReferenciasCollection() == null) {
                            mov.setRegCertificadoMovimientoReferenciasCollection(new ArrayList<>());
                        }
                        rcmr = this.llenarModeloReferenciaRazon(mf.getFicha());
                        rcmr.setMovimientoCertificado(mov);
                        mov.getRegCertificadoMovimientoReferenciasCollection().add((RegCertificadoMovimientoReferencias) manager.merge(rcmr));
                    }
                }
            }
            return ce;
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public RegCertificado saveCertificadoFicha(RegCertificado ce) {
        RegCertificadoMovimientoIntervinientes rcmi;
        RegCertificadoMovimientoReferencias rcmr;
        List<RegCertificadoMovimiento> cmovs = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
        List<RegCertificadoPropietario> cprops = (List<RegCertificadoPropietario>) ce.getRegCertificadoPropietarioCollection();
        try {
            ce.setRegCertificadoMovimientoCollection(null);
            ce.setRegCertificadoPropietarioCollection(null);

            ce = (RegCertificado) manager.merge(ce);

            for (RegCertificadoPropietario cp : cprops) {
                if (cp.getId() == null) {
                    cp.setCertificado(ce);
                    manager.merge(cp);
                }
            }
            for (RegCertificadoMovimiento mov : cmovs) {
                if (mov.getId() == null) {
                    if (ce.getRegCertificadoMovimientoCollection() == null) {
                        ce.setRegCertificadoMovimientoCollection(new ArrayList<>());
                    }
                    mov = this.llenarModeloMovimiento(mov);
                    mov.setCertificado(ce);
                    mov = (RegCertificadoMovimiento) manager.merge(mov);
                    ce.getRegCertificadoMovimientoCollection().add(mov);
                    for (RegMovimientoCliente mc : mov.getMovimiento().getRegMovimientoClienteCollection()) {
                        if (mov.getRegCertificadoMovimientoIntervinientesCollection() == null) {
                            mov.setRegCertificadoMovimientoIntervinientesCollection(new ArrayList<>());
                        }
                        rcmi = this.llenarModeloCliente(mc);
                        rcmi.setMovimientoCertificado(mov);
                        mov.getRegCertificadoMovimientoIntervinientesCollection().add((RegCertificadoMovimientoIntervinientes) manager.merge(rcmi));

                    }
                    for (RegMovimientoReferencia mr : this.getRegMovRefByIdRegMov(mov.getMovimiento().getId())) {
                        if (mov.getRegCertificadoMovimientoReferenciasCollection() == null) {
                            mov.setRegCertificadoMovimientoReferenciasCollection(new ArrayList<>());
                        }
                        rcmr = this.llenarModeloReferencia(mr.getMovimientoReferencia());
                        rcmr.setMovimientoCertificado(mov);
                        mov.getRegCertificadoMovimientoReferenciasCollection().add((RegCertificadoMovimientoReferencias) manager.merge(rcmr));
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        Hibernate.initialize(ce);
        return ce;
    }

    public RegCertificadoMovimiento llenarModeloMovimiento(RegMovimiento m) {
        RegCertificadoMovimiento cm = new RegCertificadoMovimiento();
        try {
            if (m.getActo() != null) {
                cm.setActo(m.getActo().getNombre());
            }
            if (m.getDomicilio() != null) {
                cm.setCanton(m.getDomicilio().getNombre());
            }
            if (m.getFechaOto() != null) {
                cm.setFechaCelebracion(m.getFechaOto());
            }
            if (m.getFechaInscripcion() != null) {
                cm.setFechaInscripcion(m.getFechaInscripcion());
            }
            if (m.getFechaResolucion() != null) {
                cm.setFechaResolucion(m.getFechaResolucion());
            }
            if (m.getFolioFin() != null) {
                cm.setFolioFin(m.getFolioFin());
            }
            if (m.getFolioInicio() != null) {
                cm.setFolioInicio(m.getFolioInicio());
            }
            if (m.getNumTomo() != null) {
                cm.setTomo(m.getNumTomo());
            }
            if (m.getNumInscripcion() != null) {
                cm.setInscripcion(m.getNumInscripcion());
            }
            if (m.getEscritJuicProvResolucion() != null) {
                cm.setJuicioResolucion(m.getEscritJuicProvResolucion());
            }
            if (m.getLibro() != null) {
                cm.setLibro(m.getLibro().getNombre());
            }
            if (m.getEnteJudicial() != null) {
                cm.setNotaria(m.getEnteJudicial().getNombre());
            }
            if (m.getNumRepertorio() != null) {
                cm.setRepertorio(m.getNumRepertorio());
            }
            if (m.getObservacion() != null) {
                cm.setObservacion(m.getObservacion());
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return cm;
    }

    public RegCertificadoMovimiento llenarModeloMovimiento(RegCertificadoMovimiento cm) {
        try {
            if (cm.getMovimiento().getActo() != null) {
                cm.setActo(cm.getMovimiento().getActo().getNombre());
            }
            if (cm.getMovimiento().getDomicilio() != null) {
                cm.setCanton(cm.getMovimiento().getDomicilio().getNombre());
            }
            if (cm.getMovimiento().getFechaOto() != null) {
                cm.setFechaCelebracion(cm.getMovimiento().getFechaOto());
            }
            if (cm.getMovimiento().getFechaInscripcion() != null) {
                cm.setFechaInscripcion(cm.getMovimiento().getFechaInscripcion());
            }
            if (cm.getMovimiento().getFechaRepertorio() != null) {
                cm.setFechaRepertorio(cm.getMovimiento().getFechaRepertorio());
            }
            if (cm.getMovimiento().getFechaResolucion() != null) {
                cm.setFechaResolucion(cm.getMovimiento().getFechaResolucion());
            }
            if (cm.getMovimiento().getFolioFin() != null) {
                cm.setFolioFin(cm.getMovimiento().getFolioFin());
            }
            if (cm.getMovimiento().getFolioInicio() != null) {
                cm.setFolioInicio(cm.getMovimiento().getFolioInicio());
            }
            if (cm.getMovimiento().getNumTomo() != null) {
                cm.setTomo(cm.getMovimiento().getNumTomo());
            }
            if (cm.getMovimiento().getNumInscripcion() != null) {
                cm.setInscripcion(cm.getMovimiento().getNumInscripcion());
            }
            if (cm.getMovimiento().getEscritJuicProvResolucion() != null) {
                cm.setJuicioResolucion(cm.getMovimiento().getEscritJuicProvResolucion());
            }
            if (cm.getMovimiento().getLibro() != null) {
                cm.setLibro(cm.getMovimiento().getLibro().getNombre());
            }
            if (cm.getMovimiento().getEnteJudicial() != null) {
                cm.setNotaria(cm.getMovimiento().getEnteJudicial().getNombre());
            }
            if (cm.getMovimiento().getNumRepertorio() != null) {
                cm.setRepertorio(cm.getMovimiento().getNumRepertorio());
            }
            if (cm.getMovimiento().getObservacion() != null) {
                cm.setObservacion(cm.getMovimiento().getObservacion());
            }
            if (cm.getMovimiento().getAvaluoMunicipalCadena() != null) {
                cm.setAvaluo(cm.getMovimiento().getAvaluoMunicipalCadena());
            }
            if (cm.getMovimiento().getCuantiaCadena() != null) {
                cm.setCuantia(cm.getMovimiento().getCuantiaCadena());
            }
            if (cm.getMovimiento().getAvaluoMunicipal() != null
                    && cm.getMovimiento().getAvaluoMunicipal().compareTo(BigDecimal.ZERO) > 0) {
                cm.setAvaluo(cm.getMovimiento().getAvaluoMunicipal().toString());
            }
            if (cm.getMovimiento().getCuantia() != null
                    && cm.getMovimiento().getCuantia().compareTo(BigDecimal.ZERO) > 0) {
                cm.setCuantia(cm.getMovimiento().getCuantia().toString());
            }
            cm.setEsNegativa(cm.getMovimiento().getEsNegativa());
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return cm;
    }

    public RegCertificadoMovimientoIntervinientes llenarModeloCliente(RegMovimientoCliente mc) {
        RegCertificadoMovimientoIntervinientes rcmi = new RegCertificadoMovimientoIntervinientes();
        try {
            if (mc.getEnteInterv() != null) {
                rcmi.setDocumento(mc.getEnteInterv().getCedRuc());
                rcmi.setNombres(mc.getEnteInterv().getNombre());
            }
            if (mc.getDomicilio() != null) {
                rcmi.setDomicilio(mc.getDomicilio().getNombre());
            }
            if (mc.getPapel() != null) {
                rcmi.setPapel(mc.getPapel().getPapel());
            }
            if (mc.getEstado() != null) {
                rcmi.setEstadoCivil(mc.getEstado());
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return rcmi;
    }

    public RegCertificadoMovimientoReferencias llenarModeloReferencia(RegMovimiento mo) {
        RegCertificadoMovimientoReferencias rcmr = new RegCertificadoMovimientoReferencias();
        try {
            if (mo.getActo() != null) {
                rcmr.setActoReferencia(mo.getActo().getNombre());
            }
            if (mo.getFechaInscripcion() != null) {
                rcmr.setFechaInscripcion(mo.getFechaInscripcion());
            }
            if (mo.getFechaRepertorio() != null) {
                rcmr.setFechaRepertorio(mo.getFechaRepertorio());
            }
            if (mo.getNumInscripcion() != null) {
                rcmr.setInscripcionReferencia(mo.getNumInscripcion());
            }
            if (mo.getLibro() != null) {
                rcmr.setLibroReferencia(mo.getLibro().getNombre());
            }
            if (mo.getNumRepertorio() != null) {
                rcmr.setRepertorioReferencia(mo.getNumRepertorio());
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return rcmr;
    }

    public RegCertificadoMovimientoReferencias llenarModeloReferenciaRazon(RegFicha mo) {
        RegCertificadoMovimientoReferencias rcmr = new RegCertificadoMovimientoReferencias();
        try {
            if (mo.getClaveCatastral() != null) {
                rcmr.setLibroReferencia(mo.getClaveCatastral());
            }
            if (mo.getParroquia() != null) {
                rcmr.setActoReferencia(mo.getParroquia().getDescripcion());
            }
            if (mo.getNumFicha() != null) {
                rcmr.setRepertorioReferencia(mo.getNumFicha().intValue());
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return rcmr;
    }

    /**
     * actualiza los datos del objeto regcertificado
     *
     * @param ce Object
     * @param propietarios Object
     * @param movimientos Object
     * @return Object
     */
    @Override
    public RegCertificado updateCertificado(RegCertificado ce, List<RegCertificadoPropietario> propietarios,
            List<RegCertificadoMovimiento> movimientos) {
        ce.setRegCertificadoMovimientoCollection(null);
        ce.setRegCertificadoPropietarioCollection(null);
        RegCertificadoMovimientoIntervinientes rcmi;
        RegCertificadoMovimientoReferencias rcmr;
        try {
            ce = (RegCertificado) manager.persist(ce);
            for (RegCertificadoMovimiento cm : movimientos) {
                if (cm.getId() == null) {
                    cm.setCertificado(ce);
                    cm = (RegCertificadoMovimiento) manager.persist(cm);
                    for (RegMovimientoCliente mc : cm.getMovimiento().getRegMovimientoClienteCollection()) {
                        rcmi = this.llenarModeloCliente(mc);
                        rcmi.setMovimientoCertificado(cm);
                        manager.persist(rcmi);
                    }
                    for (RegMovimientoReferencia mr : this.getRegMovRefByIdRegMov(cm.getMovimiento().getId())) {
                        rcmr = this.llenarModeloReferencia(mr.getMovimientoReferencia());
                        rcmr.setMovimientoCertificado(cm);
                        manager.persist(rcmr);
                    }
                }
            }
            for (RegCertificadoPropietario cp : propietarios) {
                cp.setCertificado(ce);
                manager.persist(cp);
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return ce;
    }

    /**
     * devuelve un modelo de datos de tramites consultados al activiti
     *
     * @param desde Object
     * @return Object
     */
    @Override
    public List<CantidadesTramites> reporteCantidadesTramites(Calendar desde) {
        BigInteger tmp;
        String query, fecha;
        Long cant;
        CantidadesTramites ct;
        CantidadesTramites tr = new CantidadesTramites();
        Calendar hasta = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<CantidadesTramites> modelo = new ArrayList<>();
        try {
            hasta.set(desde.get(Calendar.YEAR), desde.get(Calendar.MONTH), desde.getActualMaximum(Calendar.DAY_OF_MONTH));
            while (desde.before(hasta)) {
                fecha = sdf.format(desde.getTime());
                //INICIALIZACION DE MODELO DE DATOS
                ct = new CantidadesTramites();
                ct.setFecha(desde.getTime());
                //CONSULTA TRAMITES ENTREGADOS
                query = "select count(*) from act_hi_taskinst where task_def_key_ = 'entregaDocs' and to_char(end_time_, 'dd/MM/yyyy') = '" + fecha + "'";
                cant = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();
                ct.setEntregados(cant.intValue());
                tr.setEntregados(tr.getEntregados() + ct.getEntregados());
                //CONSULTA TRAMITES INGRESADOS
                tmp = (BigInteger) manager.getNativeQuery(Querys.getCantidadTramites, new Object[]{2, fecha});
                if (tmp == null) {
                    ct.setRecibidos(0);
                } else {
                    ct.setRecibidos(tmp.intValue());
                }
                tr.setRecibidos(tr.getRecibidos() + ct.getRecibidos());
                //CONSULTA CERTIFICADOS INGRESADOS
                tmp = (BigInteger) manager.getNativeQuery(Querys.getCantIngCert, new Object[]{fecha, fecha});
                if (tmp == null) {
                    ct.setIncertificado(0);
                } else {
                    ct.setIncertificado(tmp.intValue());
                }
                tr.setIncertificado(tr.getIncertificado() + ct.getIncertificado());
                //CONSULTA INSCRIPCIONES INGRESADAS
                tmp = (BigInteger) manager.getNativeQuery(Querys.getCantIngInsc, new Object[]{fecha});
                if (tmp == null) {
                    ct.setIninscripcion(0);
                } else {
                    ct.setIninscripcion(tmp.intValue());
                }
                tr.setIninscripcion(tr.getIninscripcion() + ct.getIninscripcion());
                //CONSULTA TRAMITES VENCIDOS
                query = "select count(rt.*) from act_ru_task rt left join historico_tramites_view ht on "
                        + "ht.id_proceso = rt.proc_inst_id_ where to_char(ht.fecha_entrega, 'dd/MM/yyyy') "
                        + "= '" + fecha + "' and rt.task_def_key_ <> 'entregaDocs'";
                cant = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();
                ct.setVencidos(cant.intValue());
                tr.setVencidos(tr.getVencidos() + ct.getVencidos());
                //CONSULTA TRAMITES PENDIENTES
                query = "select count(distinct(t.proc_inst_id_)) from act_hi_taskinst t "
                        + "left join historico_tramites_view ht on ht.id_proceso = t.proc_inst_id_ "
                        + "where to_char(ht.fecha_entrega, 'dd/MM/yyyy') = '" + fecha + "' "
                        + "and t.task_def_key_ <> 'entregaDocs' and (t.end_time_ > "
                        + "to_date('" + fecha + "', 'dd/MM/yyyy') or t.end_time_ is null)";
                cant = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();
                ct.setPendientes(cant.intValue());
                tr.setPendientes(tr.getPendientes() + ct.getPendientes());
                //CONSULTA CERTIFICADOS POR ENTREGAR
                tmp = (BigInteger) manager.getNativeQuery(Querys.getCantOutCert, new Object[]{fecha, fecha});
                if (tmp == null) {
                    ct.setOutcertificado(0);
                } else {
                    ct.setOutcertificado(tmp.intValue());
                }
                tr.setOutcertificado(tr.getOutcertificado() + ct.getOutcertificado());
                //CONSULTA INSCRIPCIONES POR ENTREGAR
                tmp = (BigInteger) manager.getNativeQuery(Querys.getCantOutInsc, new Object[]{fecha});
                if (tmp == null) {
                    ct.setOutinscripcion(0);
                } else {
                    ct.setOutinscripcion(tmp.intValue());
                }
                tr.setOutinscripcion(tr.getOutinscripcion() + ct.getOutinscripcion());
                //INGRESO DEL MODELO DE DATOS EN LA LISTA
                modelo.add(ct);
                //SUMA DE UN DIA A LA FECHA
                desde.add(Calendar.DAY_OF_MONTH, 1);
            }
            modelo.add(0, tr);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return modelo;
    }

    /**
     * consulta los movimientos, las fichas y las inscripciones ingresadas por
     * cada usuario entre rangos de fechas
     *
     * @param user Object
     * @param desde Object
     * @param hasta Object
     * @return Object
     */
    @Override
    public List<ReporteDatosUsuario> getDatosIngresados(String user, Date desde, Date hasta) {
        Map<String, Object> map = new HashMap();
        List<ReporteDatosUsuario> list = new ArrayList<>();
        ReporteDatosUsuario rdu;
        try {
            List<RegFicha> fs = manager.findAll(Querys.getFichasByUserAndFecha, new String[]{"usuario", "desde", "hasta"}, new Object[]{user, desde, hasta});
            for (RegFicha rf : fs) {
                rdu = new ReporteDatosUsuario(1, rf);
                list.add(rdu);
            }
            List<RegMovimiento> ms = manager.findAll(Querys.getAntecedentesByUserAndFecha, new String[]{"usuario", "desde", "hasta"}, new Object[]{user, desde, hasta});
            for (RegMovimiento rm : ms) {
                rdu = new ReporteDatosUsuario(2, rm);
                list.add(rdu);
            }
            ms = manager.findAll(Querys.getInscripcionesByUserAndFecha, new String[]{"usuario", "desde", "hasta"}, new Object[]{user, desde, hasta});
            for (RegMovimiento rm : ms) {
                rdu = new ReporteDatosUsuario(3, rm);
                list.add(rdu);
            }
            map.put("usuario", user);
            AclUser u = manager.findObjectByParameter(AclUser.class, map);
            List<RegCertificado> cs = manager.findAll(Querys.getCertificadosByUserAndFecha, new String[]{"usuario", "desde", "hasta"}, new Object[]{u.getId(), desde, hasta});
            for (RegCertificado rc : cs) {
                rdu = new ReporteDatosUsuario(4, rc);
                list.add(rdu);
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * consulta la cantidad de tramites asignados a un usuario para calcular el
     * maximo numero de dias hacia atras que se pueden visualizar en las tareas
     *
     * @param user Object
     * @param fecha Object
     * @return Object
     */
    @Override
    public int getLimiteTramitesByUser(String user, String fecha) {
        Valores valor;
        String query;
        Long cant;
        Map map;
        try {
            query = "select count(htv.num_tramite) from act_hi_taskinst ht "
                    + "left join historico_tramites_view htv on htv.id_proceso = ht.proc_inst_id_ "
                    + "where assignee_ = '" + user + "' and end_time_ is null "
                    + "and htv.fecha_entrega < to_date('" + fecha + "', 'dd/MM/yyyy')";
            cant = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();
            if (cant.intValue() > 0) {
                map = new HashMap();
                map.put("code", Constantes.diasLimiteTramite);
                valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                return valor.getValorNumeric().intValue();
            } else {
                map = new HashMap();
                map.put("code", Constantes.diasMaxLimiteTramite);
                valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                return valor.getValorNumeric().intValue();
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    /**
     * calcula la cantidad de tramites que puede visualizar un usuario en la
     * bandeja de tareas dependiendo del rol asignado
     *
     * @param user Object
     * @return Object
     */
    @Override
    public int getCantidadTramitesByUser(Long user) {
        Map map = new HashMap();
        Valores valor;
        try {
            AclUser acl = manager.find(AclUser.class, user);
            if (acl != null) {
                if (!acl.getDashboard()) {
                    for (AclRol r : acl.getAclRolCollection()) {
                        if (r.getId().equals(8L)) { //ROL CERTIFICADOR
                            map.put("code", Constantes.cantidadTramitesCert);
                            valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                            return valor.getValorNumeric().intValue();
                        }
                        if (r.getId().equals(9L)) { //ROL INSCRIPTOR
                            map.put("code", Constantes.cantidadTramitesInsc);
                            valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                            return valor.getValorNumeric().intValue();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
        return 0;
    }

    /**
     * pregunta si el tramite tiene una inscripcion ingresada
     *
     * @param liquidacion Object
     * @return Object
     */
    @Override
    public boolean tramiteConInscripcion(Long liquidacion) {
        try {
            BigInteger ob = (BigInteger) manager.getNativeQuery(Querys.getLiquidacionInscripcion, new Object[]{liquidacion});
            //RegpLiquidacion ob = (RegpLiquidacion) manager.find(Querys.getLiquidacionInscripcion, new String[]{"liquidacion",}, new Object[]{liquidacion});
            return ob != null;
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * consulta si un tramite ha finalizado su proceso en el activiti
     *
     * @param numTramite Object
     * @return Object
     */
    @Override
    public boolean tramiteFinalizado(Long numTramite) {
        Long cant;

        try {
            String query = "SELECT count(htv.num_tramite) "
                    + "  FROM historico_tramites_view htv "
                    + "  LEFT join act_hi_taskinst ht ON (ht.proc_inst_id_ = htv.id_proceso) "
                    + " WHERE ht.task_def_key_ = 'entregaDocs' "
                    + "   AND ht.delete_reason_ LIKE 'completed' "
                    + "   AND end_time_ is not null "
                    + "   AND htv.num_tramite = " + numTramite;

            cant = engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(query).count();

            return cant.intValue() > 0;

        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * consulta las tareas que no se han realizado por cada usuario
     *
     * @param user Object
     * @return Object
     */
    @Override
    public List<TareasSinRealizar> getTareasSinRealizar(String user) {
        List<TareasSinRealizar> list = new ArrayList<>();
        TareasSinRealizar rt;
        String query = "SELECT htv.num_tramite, rt.name_, htv.fecha_ingreso, htv.fecha_entrega, rt.assignee_, rt.create_time_  "
                + "  FROM act_ru_task rt  "
                + "  INNER JOIN historico_tramites_view htv ON (htv.id_proceso = rt.proc_inst_id_) "
                + " WHERE ((rt.task_def_key_ = 'analisisProcesoRegistral') OR (rt.task_def_key_ = 'inscribirCertificar')) "
                + "   AND rt.assignee_ = '" + user + "' ORDER BY 5,2,3";

        Connection con;
        try {
            con = ConexionActiviti.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rt = new TareasSinRealizar();

                rt.setFuncionario(this.getNombreByUserName(rs.getString(5)));

                rt.setNumtramite(rs.getLong(1));
                rt.setNombreTarea(rs.getString(2));
                rt.setFechaingreso(rs.getTimestamp(3));
                rt.setFechaentrega(rs.getTimestamp(4));
                rt.setAssigne(rs.getString(5));
                rt.setFechainicio(rs.getTimestamp(6));
                list.add(rt);
            }
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * pregunta si un movimiento pasa del limite de la cuantia para agregarlo al
     * anexo de la uafe
     *
     * @param id Object
     * @return Object
     */
    @Override
    public Boolean movimientoUafe(Long id) {
        BigDecimal limite = new BigDecimal("10000");
        try {
            BigDecimal cuantia = (BigDecimal) manager.getNativeQuery(Querys.getCuantiaFromMov, new Object[]{id});
            BigInteger acto = (BigInteger) manager.getNativeQuery(Querys.getTramiteUafMov, new Object[]{id});
            if (cuantia.compareTo(limite) > 0 && acto != null) {
                return true;
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return false;
    }

    /**
     * consulta los certificados por nombres de propietarios
     *
     * @param cedula Object
     * @param nombres Object
     * @param apellidos Object
     * @return Object
     */
    @Override
    public List<CertificadoModel> getCertificadosByEnte(String cedula, String nombres, String apellidos) {
        List<CertificadoModel> list;
        String where = "";
        try {
            if (nombres == null && apellidos != null) {
                where = "where en.apellidos ilike '%" + apellidos + "%' order by ht.num_tramite desc";
            }
            if (nombres == null && apellidos != null) {
                where = "where en.nombres ilike '%" + apellidos + "%' order by ht.num_tramite desc";
            }
            if (nombres != null && apellidos != null) {
                where = "where en.nombres ilike '%" + nombres + "%' and en.apellidos ilike '%" + apellidos + "%' order by ht.num_tramite desc";
            }
            if (where.isEmpty()) {
                return new ArrayList<>();
            } else {
                list = manager.getSqlQueryParametros(CertificadoModel.class, Querys.getCertificadosByEnte + where);
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public RegpTareasTramite guardarRegpTareasTramite(RegpTareasTramite regpTareasTramite) {
        try {
            return (RegpTareasTramite) manager.merge(regpTareasTramite);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean saveBitacoraFicha(Long id) {
        try {
            BigInteger periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            RegMovimiento mov = manager.find(RegMovimiento.class, id);
            if (mov != null) {
                for (RegMovimientoFicha mf : mov.getRegMovimientoFichaCollection()) {
                    bs.registrarFichaMov(mf.getFicha(), mov, ActividadesTransaccionales.AGREGAR_REFERENCIA, periodo);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public List<RegFichaPropietarios> getPropietariosFichaByFicha(Long id) {
        try {
            return manager.findAll(Querys.getRegPropietariosFicha, new String[]{"ficha"}, new Object[]{id});
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public RegMovimiento generarInscripcion(RegMovimiento movimiento) {
        try {
            if (movimiento.getNumInscripcion() == null) {
                //movimiento.setNumInscripcion(sec.getSecuenciaInscripcion(movimiento.getLibro().getId(), movimiento.getFechaRepertorio()));
                movimiento.setNumInscripcion(sec.getSecuenciaInscripcion(movimiento.getLibro().getId()));
                movimiento.setFechaInscripcion(new Date());
                movimiento.setEstado("AC");
                if (movimiento.getCodVerificacion() == null) {
                    movimiento.setCodVerificacion(rcs.genCodigoVerif());
                }
                if (movimiento.getTramite() != null && movimiento.getTramite().getDetalle() != null) {
                    RegpLiquidacionDetalles ld = movimiento.getTramite().getDetalle();
                    ld.setInscripcion(movimiento.getNumInscripcion());
                    manager.persist(ld);
                }
            }
            movimiento = (RegMovimiento) manager.merge(movimiento);
            Calendar periodo = Calendar.getInstance();
            periodo.setTime(movimiento.getFechaInscripcion());
            Integer anio = periodo.get(Calendar.YEAR);
            bs.registrarMovimiento(null, movimiento, ActividadesTransaccionales.INGRESO_INSCRIPCION,
                    new BigInteger(anio.toString()));
            return movimiento;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void generacionDocumento(Long movimiento, String mensaje) {
        try {
            GeneracionDocs gen = new GeneracionDocs();
            gen.setAclLogin(us.getAclLogin().getId());
            gen.setFechaGeneracion(new Date());
            gen.setUsuario(us.getUserId());
            gen.setMovimiento(movimiento);
            gen.setObservacion(mensaje);
            manager.persist(gen);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Observaciones> listarObservacionesPorTramite(HistoricoTramites historicoTramites) {
        List<Observaciones> observacionesTramites = null;
        try {
            observacionesTramites = manager.findAll(Querys.getObservacionesByTramite, new String[]{"idTramite"}, new Object[]{historicoTramites.getId()});
            return observacionesTramites;
        } catch (Exception e) {
            System.out.println(e);
            return observacionesTramites;
        }
    }

    @Override
    public RegMovimiento getMovimientoFromTarea(Long tarea) {
        Map<String, Object> map;
        RegpTareasTramite tt;
        try {
            tt = manager.find(RegpTareasTramite.class, tarea);
            map = new HashMap();
            map.put("tramite", tt);
            return (RegMovimiento) manager.findObjectByParameter(RegMovimiento.class, map);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public RegCertificado getCertificadoFromTarea(Long tarea) {
        Map<String, Object> map;
        RegpTareasTramite tt;
        try {
            tt = manager.find(RegpTareasTramite.class, tarea);
            map = new HashMap();
            map.put("tareaTramite", tt);
            return (RegCertificado) manager.findObjectByParameter(RegCertificado.class, map);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<IndiceProp> llenarModeloIndice(List<RegMovimientoCliente> list) {
        List<IndiceProp> indices = new ArrayList<>();
        IndiceProp ind;
        try {
            for (RegMovimientoCliente mc : list) {
                ind = new IndiceProp();
                ind.setIndNombre(mc.getEnteInterv().getNombre());
                if (mc.getMovimiento().getActo() != null) {
                    ind.setIndContrato(mc.getMovimiento().getActo().getNombre());
                }
                if (mc.getPapel() != null) {
                    ind.setIndContratante(mc.getPapel().getPapel());
                }
                ind.setIndFecha(mc.getMovimiento().getFechaInscripcion());
                ind.setIndInscripcion(mc.getMovimiento().getNumInscripcion());
                ind.setIndRepertorio(mc.getMovimiento().getNumRepertorio());
                indices.add(ind);
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return indices;
    }

    @Override
    public List<IndiceRegistro> llenarModeloIndiceRegistro(List<RegMovimientoCliente> list) {
        List<IndiceRegistro> indices = new ArrayList<>();
        IndiceRegistro ind;
        try {
            if (list != null) {
                for (RegMovimientoCliente mc : list) {
                    ind = new IndiceRegistro();
                    ind.setNombres(mc.getEnteInterv().getNombre());
                    ind.setEstado(this.estadoMovimiento(mc.getMovimiento().getRegistro().getId().intValue()));
                    ind.setInscripcion(mc.getMovimiento().getNumInscripcion());
                    ind.setFechaInscripcion(mc.getMovimiento().getFechaInscripcion());
                    ind.setRepertorio(mc.getMovimiento().getNumRepertorio());
                    ind.setFechaRepertorio(mc.getMovimiento().getFechaRepertorio());
                    if (mc.getMovimiento().getActo() != null) {
                        ind.setContrato(mc.getMovimiento().getActo().getNombre());
                    }
                    if (mc.getPapel() != null) {
                        ind.setPapel(mc.getPapel().getPapel());
                    }
                    ind.setObservacion(mc.getMovimiento().getObservacion());
                    if (mc.getMovimiento().getTramite() != null) {
                        if (mc.getMovimiento().getTramite().getTramite() != null) {
                            ind.setTramite(mc.getMovimiento().getTramite().getTramite().getNumTramite());
                        }
                    }
                    indices.add(ind);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return indices;
    }

    @Override
    public List<IndiceRegistro> llenarModeloConsultaRegistro(List<ConsultaRegistralView> list) {
        List<IndiceRegistro> indices = new ArrayList<>();
        IndiceRegistro ind;
        try {
            if (list != null) {
                for (ConsultaRegistralView mc : list) {
                    ind = new IndiceRegistro();
                    ind.setNombres(mc.getNombre());
                    ind.setEstado(mc.getEstadoMovimiento());
                    ind.setInscripcion(mc.getInscripcion());
                    ind.setFechaInscripcion(mc.getFechaInscripcion());
                    ind.setRepertorio(mc.getRepertorio());
                    ind.setFechaRepertorio(mc.getFechaRepertorio());
                    ind.setContrato(mc.getContrato());
                    ind.setPapel(mc.getPapel());
                    ind.setObservacion(mc.getObservacion());
                    indices.add(ind);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return indices;
    }

    public String estadoMovimiento(Integer estado) {
        if (estado != null) {
            switch (estado) {
                case 1:
                    return "Vendido";
                case 2:
                    return "Vendido Parcial";
                case 3:
                    return "Cancelado";
                case 4:
                    return "Cancelado Parcial";
                case 5:
                    return "Vigente";
                case 6:
                    return "Propietario";
                default:
                    return "No especificado";
            }
        } else {
            return "No especificado";
        }
    }

    @Override
    public List<RegpTareasTramite> getTareasDinardapTramite(RegpTareasDinardap rtd) {
        Map map = new HashMap();
        try {
            map.put("estado", true);
            map.put("tareaDinardap", rtd);
            List<RegpTareasTramite> list = (List<RegpTareasTramite>) manager.findObjectByParameterList(RegpTareasTramite.class,
                    map);
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
    }

    @Override
    public CatEnte buscarGuardarEnteDinardap(String identificacion) {
        try {
            identificacion = identificacion.trim();
            Map mapa = new HashMap<>();
            mapa.put("ciRuc", identificacion);
            CatEnte ente = (CatEnte) manager.findObjectByParameter(CatEnte.class, mapa);
            if (ente == null) {
                String codigoPaquete;
                switch (identificacion.length()) {
                    case 10:
                        codigoPaquete = "C";
                        break;
                    case 13:
                        codigoPaquete = "R";
                        break;
                    default:
                        codigoPaquete = "";
                        break;
                }
                PubPersona p = this.buscarDinardap(identificacion);
                if (p != null) {
                    ente = new CatEnte();
                    ente.setCiRuc(identificacion);
                    ente.setEsPersona(Boolean.TRUE);
                    switch (codigoPaquete) {
                        case "R":
                            ente.setEsPersona(Boolean.FALSE);
                            ente.setRazonSocial(p.getRazonSocial());
                            ente.setTipoIdentificacion("R");
                            break;
                        case "C":
                            //ente.setNombres(p.getNombre());
                            ente.setApellidos(p.getApellidos());
                            ente.setTipoIdentificacion("C");
                            break;
                        default:
                            //ente.setNombres(p.getNombre());
                            ente.setApellidos(p.getApellidos());
                            ente.setTipoIdentificacion("P");
                            break;
                    }
                    //ente.setDireccion(p.getDireccion());
                    ente.setCorreo1(p.getCorreo1());
                    ente.setFechaCre(new Date());
                    if (p.getFechaNacimientoLong() != null) {
                        ente.setFechaNacimiento(new Date(p.getFechaNacimientoLong()));
                    }
                    ente.setUserCre(us.getName_user());
                    ente = (CatEnte) manager.persist(ente);
                } else {
                    return null;
                }
            }
            return ente;
        } catch (RestClientException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public PubPersona buscarDinardap(String identificacion) {
        try {
            RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(SisVars.userVentanillaSgr, SisVars.passVentanillaSgr));
            URI uri = new URI(SisVars.urlWsDinardap + "dinardap/aplicacion/SIGERI/identificacion/" + identificacion);
            ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
            if (contribuyente != null && contribuyente.getBody() != null) {
                return contribuyente.getBody();
            }
        } catch (URISyntaxException | RestClientException e) {
            System.out.println(e);
        }
        return null;
    }

    public PubPersona buscarDinardapOld(String identificacion) {
        try {
            String codigo;
            identificacion = identificacion.trim();
            RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(SisVars.userVentanillaSgr, SisVars.passVentanillaSgr));
            //URI uri = new URI(SisVars.urlWsDinardap + "dinardap/aplicacion/SIGERI/identificacion/" + identificacion);
            if (identificacion.length() == 10) {
                codigo = "CEDULA";
            } else {
                codigo = "RUC";
            }
            URI uri = new URI(SisVars.urlWsDinardap + "rp/persona/aplicacion/SIGERI/" + identificacion + "/" + codigo);
            ResponseEntity<PubPersona> contribuyente = restTemplate.getForEntity(uri, PubPersona.class);
            if (contribuyente != null && contribuyente.getBody() != null) {
                return contribuyente.getBody();
            }
        } catch (URISyntaxException | RestClientException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public boolean generarNuevoRepertorio(Long tramite) {
        List<RegMovimiento> movs;
        try {
            RegpLiquidacion liquidacion = (RegpLiquidacion) manager.find(Querys.getRegpLiquidacionByNumTramite,
                    new String[]{"numTramite"}, new Object[]{tramite});
            if (liquidacion != null) {
                movs = manager.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{liquidacion.getNumTramiteRp()});
                if (liquidacion.getEsRegistroPropiedad()) {
                    SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
                    liquidacion.setRepertorio(sr.getRepertorio());
                    liquidacion.setFechaRepertorio(sr.getFecha());
                }
                /*else {
                    SecuenciaRepertorioMercantil srm = sec.getSecuenciaRepertorioMercantil();
                    liquidacion.setRepertorio(srm.getRepertorio());
                    liquidacion.setFechaRepertorio(srm.getFecha());
                }*/
                manager.update(liquidacion);
                for (RegMovimiento rm : movs) {
                    //rm.setNumRepertorio(liquidacion.getRepertorio());
                    //rm.setFechaRepertorio(liquidacion.getFechaRepertorio());
                    rm.setTramite(null);
                    manager.update(rm);
                }
                this.generarIndice(liquidacion);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public boolean generarNuevoRepertorio(RegpLiquidacion liquidacion) {
        List<RegMovimiento> movs;
        try {
            movs = manager.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{liquidacion.getNumTramiteRp()});
            if (liquidacion.getEsRegistroPropiedad()) {
                SecuenciaRepertorio sr = sec.getSecuenciaRepertorio();
                liquidacion.setRepertorio(sr.getRepertorio());
                liquidacion.setFechaRepertorio(sr.getFecha());
            }
            /*else {
                SecuenciaRepertorioMercantil srm = sec.getSecuenciaRepertorioMercantil();
                liquidacion.setRepertorio(srm.getRepertorio());
                liquidacion.setFechaRepertorio(srm.getFecha());
            }*/
            manager.merge(liquidacion);
            for (RegMovimiento rm : movs) {
                rm.setTramite(null);
                manager.update(rm);
            }
            this.generarIndice(liquidacion);
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    public void generarIndice(RegpLiquidacion liquidacion) {
        RegMovimiento mov;
        RegMovimientoCliente mc;
        Calendar calendario = Calendar.getInstance();
        try {
            String verificacion = rcs.genCodigoVerif();
            for (RegpTareasTramite rtt : liquidacion.getTramite().getRegpTareasTramiteCollection()) {
                if (!rtt.getDetalle().getActo().getSolvencia()) {
                    mov = new RegMovimiento();
                    mov.setActo(rtt.getDetalle().getActo());
                    mov.setLibro(mov.getActo().getLibro());
                    mov.setNumRepertorio(liquidacion.getRepertorio());
                    mov.setFechaRepertorio(liquidacion.getFechaRepertorio());
                    mov.setEnteJudicial(liquidacion.getEnteJudicial());
                    mov.setFechaOto(liquidacion.getFechaOto());
                    mov.setOrdJud(liquidacion.getOrdJud());
                    mov.setFechaResolucion(liquidacion.getFechaResolucion());
                    //mov.setEscritJuicProvResolucion(liquidacion.getEscritJuicProvResolucion());
                    if (liquidacion.getNumFormulario() != null && !liquidacion.getNumFormulario().isEmpty()) {
                        mov.setEscritJuicProvResolucion(liquidacion.getNumFormulario());
                    }
                    mov.setUserCreador(liquidacion.getInscriptor());
                    mov.setValorUuid(Utils.getValorUuid());
                    mov.setCodVerificacion(verificacion);
                    mov.setTramite(rtt);
                    mov.setEstado("IN");
                    mov.setNumTomo(NroTomoEnum.fromUiParam(calendario.get(Calendar.YEAR)).name());
                    mov.setAvaluoMunicipal(rtt.getDetalle().getAvaluo());
                    mov.setCuantia(rtt.getDetalle().getCuantia());
                    mov.setBaseImponible(rtt.getDetalle().getBase());
                    mov.setFechaIngreso(calendario.getTime());
                    mov.setDomicilio(new RegDomicilio(281L)); //CANTON SANTO DOMINGO
                    //manager.persist(mov);
                    mov = (RegMovimiento) manager.persist(mov);
                    for (RegpIntervinientes in : rtt.getDetalle().getRegpIntervinientesCollection()) {
                        mc = new RegMovimientoCliente();
                        mc.setMovimiento(mov);
                        mc.setPapel(in.getPapel());
                        mc.setEnte(in.getEnte());
                        mc.setDomicilio(new RegDomicilio(281L)); //CANTON SANTO DOMINGO
                        if (in.getEnte().getEstadoCivil() != null) {
                            mc.setEstado(in.getEnte().getEstadoCivil().getValor());
                        }
                        mc.setEnteInterv(itl.getIntervinienteByEnte(in.getEnte()));
                        manager.persist(mc);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(RegistroPropiedadEjb.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public String getTaskIdFromNumTramite(Long tramite) {
        String taskId;
        try {
            String query = "SELECT task_id FROM flow.tareas_activas ta WHERE ta.num_tramite = " + tramite;
            taskId = (String) manager.getNativeQueryXpress(query);
            if (taskId == null) {
                taskId = "";
            }
        } catch (Exception e) {
            //Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            taskId = "";
        }
        return taskId;
    }

    @Override
    public String getNameTaskFromNumTramite(Long tramite) {
        String nameTask;
        try {
            String query = "SELECT name_ FROM flow.tareas_activas ta WHERE ta.num_tramite = " + tramite;
            nameTask = (String) manager.getNativeQueryXpress(query);
            if (nameTask == null) {
                nameTask = "";
            }
        } catch (Exception e) {
            //Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            nameTask = "";
        }
        return nameTask;
    }

    @Override
    public void enviarSms(Sms sms) {
        try {
            map = new HashMap();
            map.put("code", Constantes.smsUrlToken);
            Valores smsUrl = (Valores) manager.findObjectByParameter(Valores.class, map);
            map = new HashMap();
            map.put("code", Constantes.smsEstado);
            Valores smsEstado = (Valores) manager.findObjectByParameter(Valores.class, map);
            if (smsEstado.getValorString().equals("ACTIVO")) {
                String telf = sms.getDestinatario();
                if (telf.startsWith("0")) {
                    telf = sms.getDestinatario().substring(1);
                }
                telf = "593" + telf + "&text=" + sms.getMensaje();
                String url = smsUrl.getValorString() + telf;
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet httpPost = new HttpGet(url.replace(" ", "%20"));
                httpPost.setHeader("Content-type", "application/json; charset=utf-8");
                httpClient.execute(httpPost);
            }
        } catch (IOException ex) {
            System.out.println("no se envio el mensaje");
            //Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public PubSolicitud actualizarSolicitudVentanillaNumTramiteInscripcion(PubSolicitud pubSolicitud
    ) {

        HttpResponse httpResponse;
        Gson gson = new Gson();
//        String creds = String.format("%s:%s", SisVars.userVentanillaREST, SisVars.passVentanillaREST);
//        String auth = "Basic " + Base64.getEncoder()
//                .encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlWSVentanilla + "solicitud/actualizarTramiteInscripcion");
        System.out.println(SisVars.urlWSVentanilla + "solicitud/actualizarTramiteInscripcion");
        System.out.println(gson.toJson(pubSolicitud));
        httpPost.setEntity(new StringEntity(gson.toJson(pubSolicitud), ContentType.APPLICATION_JSON));
        //   httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
//        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8);
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                pubSolicitud = gson.fromJson(sb.toString(), PubSolicitud.class);
                return pubSolicitud;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public PubSolicitud linkPagoSolicitudVentanilla(PubSolicitud pubSolicitud
    ) {
        HttpResponse httpResponse;
        Gson gson = new Gson();
        String creds = String.format("%s:%s", SisVars.userVentanillaREST, SisVars.passVentanillaREST);
        String auth = "Basic " + Base64.getEncoder()
                .encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlWSPublico + "pagos/generarLinkPago");
        httpPost.setEntity(new StringEntity(gson.toJson(pubSolicitud), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                pubSolicitud = gson.fromJson(sb.toString(), PubSolicitud.class);
                return pubSolicitud;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public PubSolicitud actualizarSolicitudVentanilla(PubSolicitud pubSolicitud
    ) {

        HttpResponse httpResponse;
        Gson gson = new Gson();
        System.out.println(SisVars.urlWSVentanilla + "solicitud/actualizarSolicitudObservacion");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlWSVentanilla + "solicitud/actualizarSolicitudObservacion");
        httpPost.setEntity(new StringEntity(gson.toJson(pubSolicitud), ContentType.APPLICATION_JSON));
        //   httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                pubSolicitud = gson.fromJson(sb.toString(), PubSolicitud.class);
                return pubSolicitud;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Boolean iniciarTramiteActiviti(RegpLiquidacion liquidacion) {
        Map mapa = new HashMap();
        mapa.put("estado", Boolean.TRUE);
        String rolentregatramites = "entrega_tramites";
        try {
            HashMap<String, Object> pars = new HashMap<>();
            if (liquidacion.getCertificado()) {
                mapa.put("activitykey", Constantes.procesoCertificacion);
            } else {
                mapa.put("activitykey", Constantes.procesoInscripcion);
            }
            GeTipoTramite tipoTramite = (GeTipoTramite) manager.findObjectByParameter(GeTipoTramite.class, mapa);
            if (tipoTramite == null) {
                return false;
            }

            pars.put("prioridad", 50);
            pars.put("revision", 1);
            pars.put("aprobado", 1);
            pars.put("tramite", liquidacion.getNumTramiteRp());
            pars.put("proceso", tipoTramite.getDescripcion());
            pars.put("certificador", liquidacion.getInscriptor().getUsuario());
            pars.put("inscriptor", liquidacion.getInscriptor().getUsuario());
            pars.put("registrador", itl.getUsuarioByRolName("registrador"));
            pars.put("ventanilla", itl.getCandidateUserByRolName(rolentregatramites));
            Thread.sleep(5000);
            ProcessInstance p = this.startProcessByDefinitionKey(tipoTramite.getActivitykey(), pars);
            if (p != null) {
                HistoricoTramites ht = liquidacion.getTramite();
                ht.setIdProceso(p.getId());
                ht.setTipoTramite(tipoTramite);
                manager.update(ht);
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean actualizarUsuarioApp(UsuariosApp usuario, String obs
    ) {
        HttpResponse httpResponse;
        Gson gson = new Gson();
        String creds = String.format("%s:%s", SisVars.userVentanillaREST, SisVars.passVentanillaREST);
        String auth = "Basic " + Base64.getEncoder()
                .encodeToString(creds.getBytes());
        UsuarioRegistro usuarioRegistro = new UsuarioRegistro();
        usuarioRegistro.setIdentificacion(usuario.getUsuario());
        usuarioRegistro.setCreado(usuario.getAprueba());
        usuarioRegistro.setTipo(obs);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlWSPublico + "usuario/activarUsuarioEntidad");
        httpPost.setEntity(new StringEntity(gson.toJson(usuarioRegistro), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                UsuarioRegistro ur = gson.fromJson(sb.toString(), UsuarioRegistro.class);
                return ur != null ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long guardarArchivo(String archivo) {
        Long oid = 0L;
        File output = new File(archivo);
        try (FileInputStream fs = new FileInputStream(output)) {
            oid = ou.upFileDocEnable(fs, output.getName(), "application/pdf");
            //   File input = new File(filePdf);

        } catch (Exception e) {
            e.printStackTrace();
            oid = 0L;
        }
        if (output.exists()) {
            System.out.println("output.delete(): " + output.delete());
        }
        return oid;
    }

    @Override
    public Long guardarArchivo(byte[] archivo) {
        Long oid = 0L;
        String filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaTemporales) + new Date().getTime() + ".pdf";
        File output = new File(filePdf);
        try {
            FileUtils.writeByteArrayToFile(output, archivo);
        } catch (IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (FileInputStream fs = new FileInputStream(output)) {
            oid = ou.upFileDocEnable(fs, output.getName(), "application/pdf");
            output.delete();
        } catch (Exception e) {
            e.printStackTrace();
            oid = 0L;
        }
        return oid;
    }

    @Override
    public void finalizarTareaFirmaFuncionario(DocumentoFirma firmaDocumento) {
        HistoricoTramites ht1 = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteByNumTramite, new String[]{"tramite"}, new Object[]{firmaDocumento.getNumTramite()});
        String taskId = getTaskIdFromNumTramite(firmaDocumento.getNumTramite());

        if (!taskId.isEmpty()) {
            guardarObservaciones(ht1, firmaDocumento.getUsuario(), "Firma de documento " + firmaDocumento.getMotivo(), "Firma Documento Funcionario");
            try {
                HashMap<String, Object> par1 = new HashMap<>();
                reasignarTarea(taskId, firmaDocumento.getUsuario());
                completeTask(taskId, par1);
            } catch (Exception ex) {
                Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void finalizarTareaFirmaFuncionario(DocumentoFirma firmaDocumento, HashMap<String, Object> par) {
        HistoricoTramites ht1 = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteByNumTramite, new String[]{"tramite"}, new Object[]{firmaDocumento.getNumTramite()});
        String taskId = getTaskIdFromNumTramite(firmaDocumento.getNumTramite());

        if (!taskId.isEmpty()) {
            guardarObservaciones(ht1, firmaDocumento.getUsuario(), "Firma de documento " + firmaDocumento.getMotivo(), "Firma Documento Funcionario");
            try {
                reasignarTarea(taskId, firmaDocumento.getUsuario());
                completeTask(taskId, par);
            } catch (Exception ex) {
                Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public RegMovimiento guardarMigracion(RegMovimiento mov) {
        List<RegMovimientoCliente> mcs;
        try {
            if (mov.getId() == null) {
                mcs = (List<RegMovimientoCliente>) mov.getRegMovimientoClienteCollection();
                mov.setRegMovimientoClienteCollection(null);
                mov.setRegistro(new RegEstadoMovimiento(7L));
                mov.setEstado("AC");
                mov.setFechaRepertorio(mov.getFechaInscripcion());
                mov.setFechaIngreso(new Date());
                mov.setUserCreador(new AclUser(us.getUserId()));
                mov = (RegMovimiento) manager.persist(mov);
                if (!mcs.isEmpty()) {
                    for (RegMovimientoCliente mc : mcs) {
                        if (mc.getId() == null) {
                            mc.setMovimiento(mov);
                            manager.persist(mc);
                        }
                    }
                }
            }
            return mov;
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public RegMovimientoReferencia guardarMarginacion(RegMovimientoReferencia mr) {
        try {
            RegMovimiento mov = mr.getMovimientoReferencia();
            manager.update(mov);

            RegMovimientoMarginacion rmm = new RegMovimientoMarginacion();
            rmm.setFechaIngreso(new Date());
            rmm.setMovimiento(mov);
            rmm.setUsuario(mr.getUsuario());
            rmm.setUserIngreso(mr.getIdusuario());
            rmm.setObservacion(mr.getMarginacion());
            manager.persist(rmm);

            return (RegMovimientoReferencia) manager.persist(mr);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return mr;
        }
    }

    @Override
    public Long guardarTbData(RegMovimiento movimiento) {
        try {
            /*TbData padre = de.find(TbData.class, movimiento.getIdPadre().longValue());
            if (padre == null) {
                return -1L;
            }
            if (padre.getIdPadre().compareTo(BigInteger.ZERO) > 0) {
                return 0L;
            }
            TbData data = this.llenarTbData(movimiento, padre.getIdCarpeta().getIdTipoDoc().getIdTipoDoc());
            data.setFlgMedio(Short.valueOf("0"));
            data.setCanPaginas(Short.valueOf("0"));
            data.setUsrCreacion(Short.valueOf(us.getUserId().toString()));
            data.setFecCreacion(new Date());
            data.setIdLibreria(padre.getIdLibreria());
            data.setFlgOcr(Short.valueOf("0"));
            data.setFlgBloqueado(Short.valueOf("0"));
            data.setFlgEstado(Short.valueOf("8"));
            data.setIdPadre(BigInteger.valueOf(padre.getIdTransaccion()));
            //data.setIdBlobReg(null);
            data.setNumCaja(0);
            data.setIdCarpeta(padre.getIdCarpeta());
            data = (TbData) de.persist(data);
            return data.getIdTransaccion();*/
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        return -2L;
    }

    public TbData llenarTbData(RegMovimiento mov, Integer tipo) {
        SimpleDateFormat sdf;
        RegFicha ficha;
        try {
            TbData data = new TbData();
            switch (tipo) {
                case 1:
                    ficha = this.getFichaByIdMovimiento(mov.getId());
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF01(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF02(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    //data.setF04(mov.getNumTomo());                          //NRO TOMO LIBRO
                    data.setF07(mov.getActo().getNombre());                 //CONTRATO
                    data.setF10(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF11(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    data.setF15(mov.getLibro().getNombre());                //LIBRO
                    data.setF16(Constantes.provincia);                       //PROVINCIA
                    data.setF28(Constantes.canton);                          //CANTON
                    data.setF29(mov.getBaseImponible().toString());         //CUANTIA
                    data.setF30(mov.getValorUuid());                        //CODIGO UNICO
                    data.setF31(mov.getEscritJuicProvResolucion());         //ESCRITURA
                    data.setF33(Constantes.canton);                          //UBICACION DATO    
                    if (mov.getEnteJudicial() != null) {
                        data.setF35(mov.getEnteJudicial().getNombre());         //NOTARIA
                    }
                    data.setF36(mov.getDomicilio().getNombre());            //DOMICILIO NOTARIA
                    if (mov.getFechaOto() != null) {
                        data.setF37(sdf.format(mov.getFechaOto()));         //FECHA CELEBRACION
                    }
                    if (mov.getFechaResolucion() != null) {
                        data.setF39(sdf.format(mov.getFechaResolucion()));  //FECHA RESOLUCION JUICIO
                    }
                    data.setF40("$");                                       //MONEDA
                    if (ficha != null) {
                        data.setF12(ficha.getClaveCatastral());                 //CLAVE CATASTRAL
                        data.setF13(ficha.getUafTipoBien().getDescripcion());   //TIPO BIEN
                        data.setF14(ficha.getDescripcionBien());                //DESCRIPCION BIEN
                        data.setF17(ficha.getTipoPredio());                     //SECTOR/ZONA
                        data.setF19(ficha.getLinderos());                       //LINDEROS
                        data.setF27(ficha.getParroquia().getDescripcion());     //PARROQUIA
                    }
                    break;

                case 2:
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF01(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF02(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF07(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    if (mov.getEnteJudicial() != null) {
                        data.setF13(mov.getEnteJudicial().getNombre());         //AUTORIDAD
                    }
                    if (mov.getFechaResolucion() != null) {
                        data.setF14(sdf.format(mov.getFechaResolucion()));  //FECHA DISPOSICION
                    }
                    data.setF15(mov.getEscritJuicProvResolucion());         //NRO DISPOSICION
                    if (mov.getFechaOto() != null) {
                        data.setF16(sdf.format(mov.getFechaOto()));         //FECHA ESCRITURA
                    }
                    if (mov.getEnteJudicial() != null) {
                        data.setF17(mov.getEnteJudicial().getNombre());         //NOTARIA
                    }
                    data.setF18(mov.getDomicilio().getNombre());            //SUPERFICIE
                    data.setF19(mov.getActo().getNombre());                 //TIPO TRAMITE
                    data.setF20(Constantes.canton);                          //UBICACION DATO
                    break;

                case 5:
                case 7:
                    ficha = this.getFichaByIdMovimiento(mov.getId());
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF01(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF02(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    //data.setF04(mov.getNumTomo());                          //NRO TOMO LIBRO
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    data.setF07(mov.getActo().getNombre());                 //TIPO CONTRATO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF11(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION

                    data.setF15(mov.getLibro().getNombre());                //LIBRO
                    data.setF16(Constantes.provincia);                       //PROVINCIA

                    data.setF28(Constantes.canton);                          //CANTON
                    data.setF29(mov.getBaseImponible().toString());         //CUANTIA
                    data.setF30(mov.getValorUuid());                        //CODIGO UNICO
                    data.setF31(mov.getEscritJuicProvResolucion());         //NRO JUICIO
                    data.setF33(Constantes.canton);                          //UBICACION DATO    
                    if (mov.getEnteJudicial() != null) {
                        data.setF35(mov.getEnteJudicial().getNombre());         //NOTARIA
                    }
                    data.setF36(mov.getDomicilio().getNombre());            //DOMICILIO NOTARIA
                    if (mov.getFechaOto() != null) {
                        data.setF37(sdf.format(mov.getFechaOto()));         //FECHA CELEBRACION
                    }
                    if (mov.getFechaResolucion() != null) {
                        data.setF39(sdf.format(mov.getFechaResolucion()));  //FECHA RESOLUCION JUICIO
                    }
                    data.setF40("$");                                       //MONEDA
                    if (ficha != null) {
                        data.setF12(ficha.getClaveCatastral());                 //CLAVE CATASTRAL
                        data.setF13(ficha.getUafTipoBien().getDescripcion());   //TIPO BIEN
                        data.setF14(ficha.getDescripcionBien());                //DESCRIPCION BIEN
                        data.setF17(ficha.getTipoPredio());                     //SECTOR/ZONA
                        data.setF19(ficha.getLinderos());                       //LINDEROS
                        data.setF27(ficha.getParroquia().getDescripcion());     //PARROQUIA
                    }
                    break;

                case 6:
                    ficha = this.getFichaByIdMovimiento(mov.getId());
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF01(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF02(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    //data.setF04(mov.getNumTomo());                          //NRO TOMO LIBRO
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF10(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    data.setF14(mov.getLibro().getNombre());                //LIBRO
                    data.setF15(Constantes.provincia);                       //PROVINCIA
                    data.setF28(Constantes.canton);                          //CANTON
                    data.setF29(mov.getBaseImponible().toString());         //CUANTIA
                    data.setF30(mov.getActo().getNombre());                 //TIPO CONTRATO
                    data.setF31(mov.getValorUuid());                        //IDENTIFICADOR UNICO
                    data.setF32(mov.getEscritJuicProvResolucion());         //BRO JUICIO    
                    data.setF34(Constantes.canton);                          //UBICACION DATO
                    if (mov.getEnteJudicial() != null) {
                        data.setF36(mov.getEnteJudicial().getNombre());         //NOTARIA
                    }
                    data.setF37(mov.getDomicilio().getNombre());            //CANTON NOTARIA
                    if (mov.getFechaOto() != null) {
                        data.setF38(sdf.format(mov.getFechaOto()));         //FECHA CELEBRACION
                    }
                    if (mov.getFechaResolucion() != null) {
                        data.setF40(sdf.format(mov.getFechaResolucion()));  //FECHA RESOLUCION JUICIO
                    }
                    data.setF41("$");                                       //MONEDA
                    if (ficha != null) {
                        data.setF11(ficha.getClaveCatastral());                 //CLAVE CATASTRAL
                        data.setF12(ficha.getUafTipoBien().getDescripcion());   //TIPO BIEN
                        data.setF13(ficha.getDescripcionBien());                //DESCRIPCION BIEN
                        data.setF16(ficha.getTipoPredio());                     //SECTOR/ZONA
                        data.setF19(ficha.getLinderos());                       //LINDEROS
                        data.setF27(ficha.getParroquia().getDescripcion());     //PARROQUIA
                    }
                    break;

                case 8:
                    ficha = this.getFichaByIdMovimiento(mov.getId());
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF01(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF02(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    //data.setF04(mov.getNumTomo());                          //NRO TOMO LIBRO
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF10(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    data.setF14(mov.getLibro().getNombre());                //LIBRO
                    data.setF27(Constantes.canton);                          //CANTON
                    data.setF28(mov.getActo().getNombre());                 //TIPO CONTRATO
                    data.setF29(mov.getValorUuid());                        //IDENTIFICADOR UNICO
                    data.setF30(mov.getEscritJuicProvResolucion());         //BRO JUICIO     
                    data.setF32(Constantes.canton);                          //UBICACION DATO
                    if (mov.getEnteJudicial() != null) {
                        data.setF34(mov.getEnteJudicial().getNombre());         //NOTARIA
                    }
                    data.setF35(mov.getDomicilio().getNombre());            //CANTON NOTARIA
                    if (mov.getFechaOto() != null) {
                        data.setF36(sdf.format(mov.getFechaOto()));         //FECHA CELEBRACION
                    }
                    if (mov.getFechaResolucion() != null) {
                        data.setF38(sdf.format(mov.getFechaResolucion()));  //FECHA RESOLUCION JUICIO
                    }
                    data.setF39(Constantes.provincia);                       //PROVINCIA
                    data.setF40("$");                                       //MONEDA
                    data.setF41(mov.getBaseImponible().toString());         //CUANTIA   
                    if (ficha != null) {
                        data.setF11(ficha.getClaveCatastral());                 //CLAVE CATASTRAL
                        data.setF12(ficha.getUafTipoBien().getDescripcion());   //TIPO BIEN
                        data.setF13(ficha.getDescripcionBien());                //DESCRIPCION BIEN
                        data.setF15(ficha.getTipoPredio());                     //SECTOR/ZONA
                        data.setF18(ficha.getLinderos());                       //LINDEROS
                        data.setF26(ficha.getParroquia().getDescripcion());     //PARROQUIA
                    }
                    break;

                case 13:
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF01(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF02(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    //data.setF04(mov.getNumTomo());                          //NRO TOMO LIBRO
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    data.setF08(mov.getActo().getNombre());                 //TIPO CONTRATO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF11(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    data.setF15(mov.getLibro().getNombre());                //LIBRO
                    data.setF28(Constantes.canton);                          //CANTON
                    data.setF30(mov.getValorUuid());                        //IDENTIFICADOR UNICO
                    data.setF33(Constantes.canton);                          //UBICACION DATO
                    if (mov.getEnteJudicial() != null) {
                        data.setF35(mov.getEnteJudicial().getNombre());         //NOTARIA
                    }
                    data.setF36(mov.getDomicilio().getNombre());            //CANTON NOTARIA
                    if (mov.getFechaOto() != null) {
                        data.setF37(sdf.format(mov.getFechaOto()));         //FECHA CELEBRACION
                    }
                    data.setF38(mov.getEscritJuicProvResolucion());         //NRO JUICIO
                    if (mov.getFechaResolucion() != null) {
                        data.setF39(sdf.format(mov.getFechaResolucion()));  //FECHA RESOLUCION JUICIO
                    }
                    break;
                case 43:
                    //data.setF01(mov.getNumTomo());                          //NRO TOMO LIBRO
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF02(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF04(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF10(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    break;
                case 45:
                    //data.setF01(mov.getNumTomo());                          //NRO TOMO LIBRO
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF02(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF04(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    data.setF06(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    break;
                default:
                    //data.setF01(mov.getNumTomo());                          //NRO TOMO LIBRO
                    sdf = new SimpleDateFormat("yyyy");
                    data.setF02(sdf.format(mov.getFechaInscripcion()));     //ANIO INSCRIPCION
                    data.setF03(mov.getNumInscripcion().toString());        //NRO INSCRIPCION
                    data.setF05(mov.getNumRepertorio().toString());         //NRO REPERTORIO
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.setF06(sdf.format(mov.getFechaInscripcion()));     //FECHA INSCRIPCION
                    break;
            }
            return data;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public RegFicha getFichaByIdMovimiento(Long idmovimiento) {
        try {
            return (RegFicha) manager.find(Querys.getFichaFromMovimiento, new String[]{"idmovimiento"},
                    new Object[]{idmovimiento});
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Boolean updateEstadoTareas(Long tramite) {
        try {
            manager.updateNativeQuery(Querys.updateTareasTramite, new Object[]{tramite});
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
