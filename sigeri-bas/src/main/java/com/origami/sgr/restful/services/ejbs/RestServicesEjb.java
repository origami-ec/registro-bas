/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.services.ejbs;

import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegCertificadoMovimientoIntervinientes;
import com.origami.sgr.entities.RegCertificadoPropietario;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.restful.models.DatosCertificado;
import com.origami.sgr.restful.models.DatosFicha;
import com.origami.sgr.restful.models.DatosIntervinientes;
import com.origami.sgr.restful.models.DatosMovimientosFicha;
import com.origami.sgr.restful.models.DatosPropietariosFicha;
import com.origami.sgr.restful.services.RestServices;
import com.origami.sgr.services.ejbs.HibernateEjbInterceptor;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 *
 * @author asilva
 */
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Stateless(name = "restservices")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class RestServicesEjb implements RestServices {

    @EJB
    private Entitymanager services;

    @Override
    public AclUser getAclUserByID(Long id) {
        return (AclUser) services.find(Querys.getAclUserByID, new String[]{"id"}, new Object[]{id});
    }

    @Override
    public RegpLiquidacion getRegpLiquidacionByNumTramite(Long numTramite) {
        return (RegpLiquidacion) services.find(Querys.getRegpLiquidacionByNumTramite, new String[]{"numTramite"}, new Object[]{numTramite});
    }

    @Override
    public List<RegpLiquidacionDetalles> getRegpLiquidacionDetalles(Long id) {
        return (List<RegpLiquidacionDetalles>) services.findAll(Querys.getDetallesByLiquidacion, new String[]{"parametro"}, new Object[]{id});
    }

    @Override
    public RegCertificado getRegCertificadoByNumCertif(Long numCertif) {
        return (RegCertificado) services.find(Querys.getCertificadoByNumCertif, new String[]{"certificado"}, new Object[]{numCertif});
    }

    @Override
    public RegpDocsTarea getRegpDocsTareaByTareaTramite(Long tareaTramite) {
        return (RegpDocsTarea) services.find(Querys.getDocsTareasByTareaTramite, new String[]{"idTarea"}, new Object[]{tareaTramite});
    }

    @Override
    public RegFicha getRegFichaByNumFicha(Long numFicha) {
        return (RegFicha) services.find(Querys.getRegFichaNumFicha, new String[]{"numFicha"}, new Object[]{numFicha});
    }

    @Override
    public RegFicha getRegFichaByCodPredial(String codigo) {
        return (RegFicha) services.find(Querys.getRegFichaCodPredial, new String[]{"codigo"}, new Object[]{codigo});
    }

    @Override
    public List<RegMovimiento> getMovimientosByFicha(Long idFicha) {
        List<RegMovimiento> list;
        try {
            list = services.findAll(Querys.getMovimientosByIdFicha, new String[]{"idFicha"}, new Object[]{idFicha});
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public List<RegEnteInterviniente> getPropietariosByFicha(Long id) {
        List<RegEnteInterviniente> props;
        try {
            props = services.findAll(Querys.getPropietariosByFichaId, new String[]{"ficha"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return props;
    }

    @Override
    public DatosCertificado getDatosByCertificado(Long numero) {
        DatosCertificado datos = new DatosCertificado();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosPropietariosFicha dpf;
        DatosMovimientosFicha dmf;
        DatosIntervinientes dci;
        List<DatosPropietariosFicha> props = new ArrayList<>();
        List<DatosMovimientosFicha> movs = new ArrayList<>();
        List<DatosIntervinientes> intrvs;
        try {
            RegCertificado certificado = this.getRegCertificadoByNumCertif(numero);
            if (certificado != null) {
                datos.setNumCertificado(certificado.getNumCertificado());
                datos.setTipoCertificado(certificado.getClaseCertificado());
                if (certificado.getFicha() != null) {
                    datos.setNumFichaRegistral(certificado.getFicha().getNumFicha());
                }
                datos.setFechaEmision(sdf.format(certificado.getFechaEmision()));
                /*RegpDocsTarea docTar = this.getRegpDocsTareaByTareaTramite(certificado.getTareaTramite().getId());
                if (docTar != null) {
                    datos.setUrl(SisVars.urlPublica + "/OmegaDownDocs?code=" + docTar.getDoc() + "&name=" + docTar.getNombreArchivo()
                            + "&tipo=2&content=" + docTar.getContentType());
                }*/
                if (certificado.getCodVerificacion() != null) {
                    datos.setUrl(Constantes.urlDownDocsVentanilla + certificado.getCodVerificacion());
                }
                if (certificado.getNumTramite() != null) {
                    datos.setNumTramite(certificado.getNumTramite());
                }
                if (certificado.getObservacion() != null) {
                    datos.setSolvencia(certificado.getObservacion());
                }
                if (certificado.getLinderosRegistrales() != null) {
                    datos.setLinderosRegistrales(certificado.getLinderosRegistrales());
                }
                if (certificado.getParroquia() != null) {
                    datos.setParroquia(certificado.getParroquia());
                }
                if (certificado.getClaveCatastral() != null) {
                    datos.setClaveCatastral(certificado.getClaveCatastral());
                }
                for (RegCertificadoPropietario cp : certificado.getRegCertificadoPropietarioCollection()) {
                    dpf = new DatosPropietariosFicha();
                    dpf.setCi(cp.getDocumento());
                    dpf.setNombre(cp.getNombres());
                    props.add(dpf);
                }
                datos.setPropietarios(props);
                for (RegCertificadoMovimiento cm : certificado.getRegCertificadoMovimientoCollection()) {
                    dmf = new DatosMovimientosFicha();
                    intrvs = new ArrayList<>();
                    dmf.setActo(cm.getActo());
                    dmf.setFechaInscripcion(sdf.format(cm.getFechaInscripcion()));
                    dmf.setInscripcion(cm.getInscripcion());
                    dmf.setLibro(cm.getLibro());
                    dmf.setObservacion(cm.getObservacion());
                    dmf.setRepertorio(cm.getRepertorio());
                    for (RegCertificadoMovimientoIntervinientes cmi : cm.getRegCertificadoMovimientoIntervinientesCollection()) {
                        dci = new DatosIntervinientes();
                        dci.setCi(cmi.getDocumento());
                        dci.setDomicilio(cmi.getDomicilio());
                        dci.setEstadocivil(cmi.getEstadoCivil());
                        dci.setNombre(cmi.getNombres());
                        dci.setPapel(cmi.getPapel());
                        intrvs.add(dci);
                    }
                    dmf.setIntervinientes(intrvs);
                    movs.add(dmf);
                }
                datos.setMovimientos(movs);
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return datos;
    }

    @Override
    public DatosFicha getDatosByNumFicha(Long numero) {
        DatosFicha datos = new DatosFicha();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosPropietariosFicha dpf;
        DatosMovimientosFicha dmf;
        DatosIntervinientes dfi;
        List<DatosPropietariosFicha> props = new ArrayList<>();
        List<DatosMovimientosFicha> movs = new ArrayList<>();
        List<DatosIntervinientes> intrvs;
        try {
            RegFicha ficha = this.getRegFichaByNumFicha(numero);
            if (ficha != null) {
                if (ficha.getClaveCatastral() != null) {
                    datos.setCodigoPredial(ficha.getClaveCatastral());
                }
                datos.setFechaApertura(sdf.format(ficha.getFechaApe()));
                datos.setLinderos(ficha.getLinderos());
                datos.setNumFicha(ficha.getNumFicha());
                if (ficha.getParroquia() != null) {
                    datos.setParroquia(ficha.getParroquia().getDescripcion());
                }
                datos.setTipoPredio(ficha.getTipoPredio());
                for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                    dpf = new DatosPropietariosFicha();
                    dpf.setCi(fp.getPropietario().getCedRuc());
                    dpf.setNombre(fp.getPropietario().getNombre());
                    props.add(dpf);
                }
                datos.setPropietarios(props);
                for (RegMovimientoFicha mf : ficha.getRegMovimientoFichaCollection()) {
                    dmf = new DatosMovimientosFicha();
                    intrvs = new ArrayList<>();
                    dmf.setActo(mf.getMovimiento().getActo().getNombre());
                    dmf.setFechaInscripcion(sdf.format(mf.getMovimiento().getFechaInscripcion()));
                    dmf.setInscripcion(mf.getMovimiento().getNumInscripcion());
                    dmf.setLibro(mf.getMovimiento().getLibro().getNombre());
                    if (mf.getMovimiento().getObservacion() != null) {
                        dmf.setObservacion(mf.getMovimiento().getObservacion());
                    }
                    dmf.setRepertorio(mf.getMovimiento().getNumRepertorio());
                    for (RegMovimientoCliente mc : mf.getMovimiento().getRegMovimientoClienteCollection()) {
                        dfi = new DatosIntervinientes();
                        dfi.setCi(mc.getEnteInterv().getCedRuc());
                        if (mc.getDomicilio() != null) {
                            dfi.setDomicilio(mc.getDomicilio().getNombre());
                        }
                        if (mc.getEstado() != null) {
                            dfi.setEstadocivil(mc.getEstado());
                        }
                        dfi.setNombre(mc.getEnteInterv().getNombre());
                        if (mc.getPapel() != null) {
                            dfi.setPapel(mc.getPapel().getNombre());
                        }
                        intrvs.add(dfi);
                    }
                    dmf.setIntervinientes(intrvs);
                    movs.add(dmf);
                }
                datos.setMovimientos(movs);
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return datos;
    }

    @Override
    public DatosFicha getDatosFichaByCodPredial(String codigo) {
        DatosFicha datos = new DatosFicha();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosPropietariosFicha dpf;
        DatosMovimientosFicha dmf;
        DatosIntervinientes dfi;
        List<DatosPropietariosFicha> props = new ArrayList<>();
        List<DatosMovimientosFicha> movs = new ArrayList<>();
        List<DatosIntervinientes> intrvs;
        try {
            RegFicha ficha = this.getRegFichaByCodPredial(codigo);
            if (ficha != null) {
                if (ficha.getClaveCatastral() != null) {
                    datos.setCodigoPredial(ficha.getClaveCatastral());
                }
                datos.setFechaApertura(sdf.format(ficha.getFechaApe()));
                datos.setLinderos(ficha.getLinderos());
                datos.setNumFicha(ficha.getNumFicha());
                if (ficha.getParroquia() != null) {
                    datos.setParroquia(ficha.getParroquia().getDescripcion());
                }
                datos.setTipoPredio(ficha.getTipoPredio());
                for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                    dpf = new DatosPropietariosFicha();
                    dpf.setCi(fp.getPropietario().getCedRuc());
                    dpf.setNombre(fp.getPropietario().getNombre());
                    props.add(dpf);
                }
                datos.setPropietarios(props);
                for (RegMovimientoFicha mf : ficha.getRegMovimientoFichaCollection()) {
                    dmf = new DatosMovimientosFicha();
                    intrvs = new ArrayList<>();
                    dmf.setActo(mf.getMovimiento().getActo().getNombre());
                    dmf.setFechaInscripcion(sdf.format(mf.getMovimiento().getFechaInscripcion()));
                    dmf.setInscripcion(mf.getMovimiento().getNumInscripcion());
                    dmf.setLibro(mf.getMovimiento().getLibro().getNombre());
                    if (mf.getMovimiento().getObservacion() != null) {
                        dmf.setObservacion(mf.getMovimiento().getObservacion());
                    }
                    dmf.setRepertorio(mf.getMovimiento().getNumRepertorio());
                    for (RegMovimientoCliente mc : mf.getMovimiento().getRegMovimientoClienteCollection()) {
                        dfi = new DatosIntervinientes();
                        dfi.setCi(mc.getEnteInterv().getCedRuc());
                        if (mc.getDomicilio() != null) {
                            dfi.setDomicilio(mc.getDomicilio().getNombre());
                        }
                        if (mc.getEstado() != null) {
                            dfi.setEstadocivil(mc.getEstado());
                        }
                        dfi.setNombre(mc.getEnteInterv().getNombre());
                        if (mc.getPapel() != null) {
                            dfi.setPapel(mc.getPapel().getNombre());
                        }
                        intrvs.add(dfi);
                    }
                    dmf.setIntervinientes(intrvs);
                    movs.add(dmf);
                }
                datos.setMovimientos(movs);
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return datos;
    }

}
