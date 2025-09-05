/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.PubSolicitudJuridico;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpLiquidacionExoneracion;
import com.origami.sgr.entities.RegpObservacionesIngreso;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenDatosFacturaElectronica;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenNotaCredito;
import com.origami.sgr.entities.RenPago;
import com.origami.sgr.models.SamLiquidacionDetalles;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface IngresoTramiteLocal {

    public BigDecimal calculoCuantia(BigDecimal valor);

    public BigDecimal calculoCuantiaDeterminada(BigDecimal valor);

    public BigDecimal calculoCuantia(BigDecimal valor, BigDecimal sbu);

    public BigDecimal calculoCuantiaSTD(BigDecimal valor);

    public String generarClaveAcceso(RegpLiquidacion liquidacion, RenCajero caja, RenDatosFacturaElectronica fe);

    public CatEnte saveCatEnteSinCedRuc(CatEnte ente);

    public RegpObservacionesIngreso saveObservacionIngreso(RegpObservacionesIngreso ob);

    public RegpLiquidacion saveLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar);

    public RegpLiquidacion saveParcialLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar);

    public RegpLiquidacion editLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar);

    public RegpLiquidacion duplicarLiquidacion(RegpLiquidacion liq, List<RegpLiquidacionDetalles> actosPorPagar);

    public RegpLiquidacion cancelarLiquidacion(RegpLiquidacion liquidacion, RenPago pago, RenCajero cajero);

    public RegpLiquidacion asignarUsuarioSecuencias(Long idLiquidacion, RenCajero cajero);

    public Observaciones guardarObservaciones(HistoricoTramites ht, String nameUser, String observaciones, String taskDefinitionKey);

    public void generarIndices(RegpLiquidacion liquidacion);
    
    public RegEnteInterviniente getIntervinienteByEnte(CatEnte ente);

    public Date diasEntregaTramite(Date fecha, int dias);

    public String getNameUserByAclUserId(Long id);

    public String getUsuarioByRolName(String name);

    public Boolean generarXml(Long idLiquidacion);

    public List<RegpLiquidacion> cargarFacturasNoEnviadas(String fecha, Long usuario);

    public RegpLiquidacion cargarAutorizacionFactura(RegpLiquidacion re, RenCajero cajero);

    public Boolean generarRIDE(List<RegpLiquidacion> liquidaciones, RenCajero cajero, String reporte, Map map);

    public Integer getEstadoPagoLiquidacion(RegpLiquidacion re);

    public Boolean envioCorreoFacturaElectronica(RegpLiquidacion re, RenCajero cajero);

    public String getCandidateUserByRolName(String nombre);

    public List<AclUser> getUsuariosByRolName(String nombre);

    public Boolean nuevaFacturaTramiteExistente(RegpLiquidacion li);

    public List<RegpDocsTarea> getDocumentosTareas(Long tramite);

    public List<RegpDocsTramite> getDocumentosTramite(Long tramite);

    public Boolean pruebasNotaCredito(RegpLiquidacion liquidacion, RenCajero cajero);

    public Boolean pruebasWsFacturacion(Long idLiquidacion);

    public Boolean emitirFacturaSinTramite(Long tramite, CatEnte solicitante, String formapago, RenCajero caja);

    //public Boolean reenvioFacturaSinTramite(RenFactura factura, RenCajero cajero);
    public Boolean emisionFacturaElectronica(Long liq, RenCajero cajero);

    public Boolean emisionNotaCreditoFe(Long liq, RenCajero cajero, String motivo, BigDecimal valor);

    public Boolean findNotaCreditoByTramite(Long tramite);

    public BigDecimal getSumaValorNotaCredito(Long tramite);

    public Boolean reenvioNotaCredito(RenNotaCredito nc);

    public List<RegpLiquidacion> getComprobantesBySolicitante(Long id, Date desde, Date hasta);

    public File mergeFilesPdf(List<InputStream> list);

    public List<RegpLiquidacionExoneracion> getExoneraciones(RegpLiquidacion liquidacion);

    public Boolean saveExoneracion(RegpLiquidacion liquidacion, List<RegpLiquidacionExoneracion> exoneraciones);

    public List<RegpLiquidacionDetalles> getActosPorPagar(RegpLiquidacion liquidacion);

    public HashMap<String, Object> getFormasPagoFe(RegpLiquidacion liq);

    public HashMap<String, Object> getFormasPagoFe(RenPago pago);

    public PubSolicitudJuridico guardarSolicitudJudicial(PubSolicitudJuridico pubSolicitudJuridico);

    public RenFactura emitirFacturaSinTramiteSinGuardarRenLiquidacionSoloRenPago(RegpLiquidacion liquidacion, RenPago pago, RenCajero cajero, String observacion);

    public ArrayList<RegpLiquidacionDetalles> validarTareasActos(RegpLiquidacion liquidacion);

    public RegpLiquidacion asignarUsuarioSecuenciasSinTramite(RegpLiquidacion liquidacion);

    public RegpLiquidacion asignarUsuarioSecuenciasCertifiacadoSinFlujo(Long idLiquidacion, RenCajero cajero);

    public StringBuffer validarInscripcion(RegpLiquidacion liquid);

    public StringBuffer validarInscripcion(RegFicha ficha);

    public RegpLiquidacion generarRepertorioIndiceJuridico(RegpLiquidacion liquidacion);

    public StringBuffer buscarActosIngresadosCedula(String identificacion);

    public StringBuffer buscarActosIngresadosCedula(String identificacion, RegActo acto);

    public PubSolicitud asignarUsuarioInscripcionEnlinea(PubSolicitud solicitud, HistoricoTramites ht);

    public Boolean registrarLiquidacionSAM(Long idliquidacion);

    public SamLiquidacionDetalles verificarPagoSAM(Long tramite);

    public Boolean ingresarLiquidacionAutomatico(RegpLiquidacion liq, SamLiquidacionDetalles det);

    public Boolean asignarUsuarioSecuencias(RegpLiquidacion liquidacion);

}
