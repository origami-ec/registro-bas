/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful;

import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.restful.models.DatosCertificado;
import com.origami.sgr.restful.models.DatosDetalleProforma;
import com.origami.sgr.restful.models.DatosFicha;
import com.origami.sgr.restful.models.DatosProforma;
import com.origami.sgr.restful.services.RestServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author asilva
 */
@Path(value = "consultas/")
@Produces({"application/Json", "text/xml"})
@ApplicationScoped
public class ConsultasRest implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConsultasRest.class.getName());
    private static final long serialVersionUID = 1L;

    @EJB(beanName = "restservices")
    private RestServices rs;

    @GET
    @Path(value = "datosProforma/tramite/{numTramite}")
    public DatosProforma getProforma(@PathParam(value = "numTramite") Long numTramite) {
        DatosProforma datos;
        try {
            RegpLiquidacion liq = rs.getRegpLiquidacionByNumTramite(numTramite);
            if (liq == null) {
                System.out.println("No existen datos para el tramite ingresado");
                return null;
            } else {
                datos = this.getDatosProforma(liq);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Consulta de Datos de Proforma WS", e);
            return null;
        }
        return datos;
    }

    private DatosProforma getDatosProforma(RegpLiquidacion liq) {
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            DatosProforma proforma = new DatosProforma();
            proforma.setSubtotal(liq.getSubTotal());
            proforma.setDescuento(liq.getDescuentoValor());
            proforma.setDescuento_porc(liq.getDescuentoPorc());
            proforma.setTotalPagar(liq.getTotalPagar());

            List<RegpLiquidacionDetalles> listLiqDet = rs.getRegpLiquidacionDetalles(liq.getId());
            if (!listLiqDet.isEmpty()) {
                proforma.setDetalle(this.getDetalleProforma(listLiqDet));
            }

            AclUser usr = rs.getAclUserByID(liq.getUserCreacion());
            if (usr != null) {
                if (usr.getEnte().getTituloProf() != null) {
                    proforma.setRevisor(usr.getEnte().getTituloProf() + " " + usr.getEnte().getNombres() + " " + usr.getEnte().getApellidos());
                } else {
                    proforma.setRevisor(usr.getEnte().getNombres() + " " + usr.getEnte().getApellidos());
                }
            }
            return proforma;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return new DatosProforma();
        }
    }

    private List<DatosDetalleProforma> getDetalleProforma(List<RegpLiquidacionDetalles> listLiqDet) {
        List<DatosDetalleProforma> listaDetalle = new ArrayList<>();
        DatosDetalleProforma detalle;
        try {
            for (RegpLiquidacionDetalles ht : listLiqDet) {
                detalle = new DatosDetalleProforma();
                detalle.setCantidad(ht.getCantidad());
                detalle.setActo(ht.getActo().getNombre());
                detalle.setAvaluo(ht.getAvaluo());
                detalle.setCuantia(ht.getCuantia());
                detalle.setValorUnitario(ht.getValorUnitario());
                detalle.setValorTotal(ht.getValorTotal());
                listaDetalle.add(detalle);
            }
            return listaDetalle;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
    }

    @GET
    @Path(value = "datosCertificado/certificado/{numerocertificado}")
    public DatosCertificado getCertificado(@PathParam(value = "numerocertificado") Long numero) {
        try {
            if (numero != null) {
                return rs.getDatosByCertificado(numero);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Consulta Datos de Certificado WS", e);
            return null;
        }
        return null;
    }

    @GET
    @Path(value = "datosFicha/ficha/{numFicha}")
    public DatosFicha getFicha(@PathParam(value = "numFicha") Long numFicha) {
        try {
            if (numFicha != null) {
                return rs.getDatosByNumFicha(numFicha);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Consulta Datos de Ficha WS", e);
            return null;
        }
        return null;
    }

    @GET
    @Path(value = "datosBien/codcatastral/{clavepredial}")
    public DatosFicha getEstadoBien(@PathParam(value = "clavepredial") String catastral) {
        try {
            if (catastral != null) {
                return rs.getDatosFichaByCodPredial(catastral);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Consulta Datos de Ficha WS", e);
            return null;
        }
        return null;
    }

}
