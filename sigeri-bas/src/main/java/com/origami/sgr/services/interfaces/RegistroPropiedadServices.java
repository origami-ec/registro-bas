/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.bpm.models.CantidadesTramites;
import com.origami.sgr.bpm.models.CantidadesUsuarios;
import com.origami.sgr.bpm.models.DataModel;
import com.origami.sgr.bpm.models.ReporteTramitesRp;
import com.origami.sgr.bpm.models.TareasSinRealizar;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ConsultaRegistralView;
import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegCertificadoPropietario;
import com.origami.sgr.entities.RegEnteInterviniente;
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
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.models.CertificadoModel;
import com.origami.sgr.models.ConsultaMovimientoModel;
import com.origami.sgr.models.IndiceRegistro;
import com.origami.sgr.models.MovimientoModel;
import com.origami.sgr.models.ReporteDatosUsuario;
import com.origami.sgr.models.Sms;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.origami.sgr.entities.UsuariosApp;
import com.origami.sgr.historico.entities.IndiceProp;
import com.origami.sgr.models.PubPersona;
import java.util.HashMap;

/**
 *
 * @author Anyelo
 */
@Local
public interface RegistroPropiedadServices {

    public ConsultaMovimientoModel getConsultaMovimiento(Long idMov);

    public List<RegFicha> getBienesByPropietario(Long id);

    public List<RegEnteInterviniente> getPropietariosByFicha(Long id);

    public List<RegFicha> getRegFichaByIdRegMov(Long id);

    public List<RegMovimientoReferencia> getRegMovRefByIdRegMov(Long id);

    public List<RegMovimientoCliente> getRegMovClienteByIdMov(Long id);

    public List<RegMovimientoCapital> getRegMovCapByIdMov(Long id);

    public List<RegMovimientoRepresentante> getRegMovRepreByIdMov(Long id);

    public List<RegMovimientoSocios> getRegMovSocioByIdMov(Long id);

    public List<RegMovimientoMarginacion> getRegMovMargByIdMov(Long id);

    public List<RegEnteInterviniente> getRegIntervsByIdMov(Long id);

    public List<RegMovimientoFicha> getRegMovFichaByIdMov(Long id);

    public List<RegMovimiento> getMovimientosByFicha(Long idFicha);

    public List<RegFichaMarginacion> getMarginacionesByFicha(Long idFicha);

    public List<RegCertificadoMovimiento> getMovsByCertificado(Long id);

    public List<RegCertificadoPropietario> getPropsByCertificado(Long id);

    public RegMovimientoParticipante getParticipantesMov(Long id);

    public RegFicha guardarFichaRegistral(RegFicha ficha, List<RegFichaPropietarios> props, List<RegMovimientoFicha> mfs, List<RegFichaMarginacion> margs);

    public RegFicha editarFichaRegistral(RegFicha ficha, List<RegFichaPropietarios> props, List<RegMovimientoFicha> mfs, List<RegFichaMarginacion> margs);

    public Collection<RegPapel> getRegCatPapelByActo(Long idacto);

    public RegEnteInterviniente saveInterviniente(RegEnteInterviniente en);

    public RegEnteInterviniente getInterviniente(String cedula, String nombre);

    public Boolean guardarInscripcion(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList,
            List<RegMovimientoFicha> movimientoFichas, List<RegMovimientoCliente> movimientoClientes);

    public RegMovimiento guardarInscripcionEdicion(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList,
            List<RegMovimientoFicha> movimientoFichas, List<RegMovimientoCliente> movimientoClientes, List<RegMovimientoMarginacion> mma, MovimientoModel mm);

    public RegMovimiento guardarInscripcionEdicion(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList,
            List<RegMovimientoFicha> movimientoFichas, List<RegMovimientoCliente> movimientoClientes, List<RegMovimientoCapital> movimientoCapitales,
            List<RegMovimientoRepresentante> movimientoRepresentantes, List<RegMovimientoSocios> movimientoSocios, MovimientoModel mm);

    public List<RegMovimiento> getListMovimientosByCedInterv(String cedula);

    public List<RegMovimientoCliente> getListMovimientosByTramite(Long tramite);

    public Collection getListIdMovsByCedRucInterv(String documento);
    
    public Collection getListIdMovsByCedRucIntervTipoLibro(String documento, Integer tipoLibro);

    public Date getFechaInscripcionMayor();

    public Date getFechaInscripcionMenor();

    public Collection getListIdFichasByDocInterv(String documento);

    public List<RegMovimientoFicha> getRegMovByIdFicha(Long id);

    public String getPapelByMovimientoInterviniente(Long mov, Long inter);

    public String getPapelByMovAndDocumentoInterv(Long mov, String doc);

    public String getLibroByCodigo(Long codigo);

    public List<RegpTareasTramite> getTareasTramite(Long idTramite);

    public RegMovimiento getMovimientoInscripcion(Long tarea);

    public BigInteger cantidadMovimientosXanioYlibro(Integer anio, Long libroId);

    public List<RegMovimiento> getRegMovimientosPorLibroAnio(Integer anio, Long idLibro);

    public RegMovimiento guardarMovimientoNuevo(RegMovimiento movimiento, List<RegMovimientoReferencia> movimientoReferenciaList);

    public RegMovimiento guardarMovimientoNuevoCompleto(RegMovimiento movimiento, List<RegMovimientoReferencia> mrfs);

    public List<AclUser> getUsuariosByRolName(String nombre);
    
    public Long getIdByRolName(String nombre);
    
    public AclUser getByRolNameCajero(String username);

    public String getCandidateUserByRolName(String nombre);

    public Observaciones guardarObservaciones(HistoricoTramites ht, String nameUser, String observaciones, String taskDefinitionKey);

    public String getNombreByUserName(String user);

    public List<ReporteTramitesRp> getTareasByTramite(Date desde, Date hasta);

    public List<ReporteTramitesRp> getTareasByUsuarios(String desde, String hasta);

    public List<ReporteTramitesRp> getTareasRealizadasByUsuario(String usuario, String desde, String hasta);
    
    public List<CantidadesUsuarios> getTotalTramitesByUsuarios(String desde, String hasta);

    public List<DataModel> getCantidadesByActo(String desde, String hasta);

    public List<ReporteTramitesRp> getTramitesByUser(Date desde, Date hasta, String user);

    public RegCertificado saveCertificadoHistoriaDominio(RegCertificado ce);

    public RegCertificado saveCertificadoRazon(RegCertificado ce, RegCertificadoMovimiento rcm);

    public RegCertificado saveCertificadoRazon(RegCertificado ce);

    public RegCertificado saveCertificadoFicha(RegCertificado ce);

    public RegCertificado updateCertificado(RegCertificado ce, List<RegCertificadoPropietario> propietarios, List<RegCertificadoMovimiento> movimientos);

    public List<CantidadesTramites> reporteCantidadesTramites(Calendar desde);

    public List<ReporteDatosUsuario> getDatosIngresados(String user, Date desde, Date hasta);

    public int getLimiteTramitesByUser(String user, String fecha);

    public int getCantidadTramitesByUser(Long user);

    public boolean tramiteConInscripcion(Long liquidacion);

    public boolean tramiteFinalizado(Long numTramite);

    public List<TareasSinRealizar> getTareasSinRealizar(String user);

    public Boolean movimientoUafe(Long id);

    public List<CertificadoModel> getCertificadosByEnte(String cedula, String nombres, String apellidos);

    public RegpTareasTramite guardarRegpTareasTramite(RegpTareasTramite regpTareasTramite);

    public boolean saveBitacoraFicha(Long id);

    public List<RegFichaPropietarios> getPropietariosFichaByFicha(Long id);

    public RegMovimiento generarInscripcion(RegMovimiento movimiento);

    public void generacionDocumento(Long movimiento, String mensaje);

    public List<Observaciones> listarObservacionesPorTramite(HistoricoTramites historicoTramites);

    public RegMovimiento getMovimientoFromTarea(Long tarea);

    public RegCertificado getCertificadoFromTarea(Long tarea);

    public List<IndiceProp> llenarModeloIndice(List<RegMovimientoCliente> list);

    public List<IndiceRegistro> llenarModeloIndiceRegistro(List<RegMovimientoCliente> list);
    
    public List<IndiceRegistro> llenarModeloConsultaRegistro(List<ConsultaRegistralView> list);

    public List<RegpTareasTramite> getTareasDinardapTramite(RegpTareasDinardap rtd);

    public CatEnte buscarGuardarEnteDinardap(String identificacion);
    
    public PubPersona buscarDinardap(String identificacion);

    public boolean generarNuevoRepertorio(Long tramite);

    public boolean generarNuevoRepertorio(RegpLiquidacion liquidacion);

    public String getTaskIdFromNumTramite(Long tramite);

    public String getNameTaskFromNumTramite(Long tramite);

    public void enviarSms(Sms sms);

    public PubSolicitud actualizarSolicitudVentanilla(PubSolicitud pubSolicitud);

    public PubSolicitud actualizarSolicitudVentanillaNumTramiteInscripcion(PubSolicitud pubSolicitud);

    public PubSolicitud linkPagoSolicitudVentanilla(PubSolicitud pubSolicitud);

    public Boolean iniciarTramiteActiviti(RegpLiquidacion liquidacion);

    public Boolean actualizarUsuarioApp(UsuariosApp usuario, String obs);

    public Long guardarArchivo(String archivo);

    public Long guardarArchivo(byte[] archivo);

    public void finalizarTareaFirmaFuncionario(DocumentoFirma firmaDocumento);

    public void finalizarTareaFirmaFuncionario(DocumentoFirma firmaDocumento, HashMap<String, Object> par);
    
    public RegMovimiento guardarMigracion(RegMovimiento mov);
    
    public RegMovimientoReferencia guardarMarginacion(RegMovimientoReferencia mr);
    
    public Long guardarTbData(RegMovimiento movimiento);
    
    public Boolean updateEstadoTareas(Long tramite);

}
