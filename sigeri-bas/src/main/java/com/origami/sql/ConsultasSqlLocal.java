/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sql;

import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimientoFichas;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimientoPartes;
import com.origami.sgr.bpm.models.AnexoCotadDetalleMovimiento;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimiento;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoCapitales;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoRepresentantes;
import com.origami.sgr.bpm.models.AnexoSuperciaDetalleMovimientoSocios;
import com.origami.sgr.bpm.models.CantidadTramitesPorUsuario;
import com.origami.sgr.bpm.models.DataModel;
import com.origami.sgr.bpm.models.DatosTramitesFinalizados;
import com.origami.sgr.bpm.models.EvaluacionFuncionarios;
import com.origami.sgr.bpm.models.MovimientosModificadosIngresados;
import com.origami.sgr.bpm.models.Remanentes;
import com.origami.sgr.bpm.models.ReporteTramitesRp;
import com.origami.sgr.bpm.models.SolicitanteInterviniente;
import com.origami.sgr.bpm.models.TareaEntregaDocumento;
import com.origami.sgr.bpm.models.TareaWF;
import com.origami.sgr.bpm.models.TareasPorFechaIngresoEntrega;
import com.origami.sgr.bpm.models.TareasSinRealizar;
import com.origami.sgr.bpm.models.TramitesCorregidos;
import com.origami.sgr.bpm.models.TramitesParaRegistrador;
import com.origami.sgr.bpm.models.TramitesPorFechaIngresoAgrupadoRol;
import com.origami.sgr.bpm.models.TramitesReasignados;
import com.origami.sgr.bpm.models.TramitesVencidosElaborados;
import com.origami.sgr.bpm.models.TramitesVencidosPendientes;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface ConsultasSqlLocal {

    public List<DataModel> getCantidadesByActoEntregado(String desde, String hasta);

    public List<ReporteTramitesRp> getTramitesPendientes(String desde, String hasta);

    public List<TareasPorFechaIngresoEntrega> getTareasPorFechaIngresoEntrega(String desde, String hasta, Integer tipofecha);

    public List<TramitesParaRegistrador> getTramitesParaRegistrador(String desde, String hasta);

    public List<CantidadTramitesPorUsuario> getCantidadTramitesPorUsuario(Calendar desde, AclUser user);

    public List<TramitesPorFechaIngresoAgrupadoRol> getTramitesPorFechaIngresoAgrupadoRol(String desde, String hasta);

    public List<TareasSinRealizar> getTareasSinRealizar();

    public List<TareasSinRealizar> getTodasTareasSinRealizar();

    public List<SolicitanteInterviniente> getSolicitanteInterviniente(String desde, String hasta, CatEnte sol_int);

    /*JC INI*/
    public List<TareaEntregaDocumento> getTareaEntregaDocumento(String desde, String hasta);

    public List<TramitesVencidosElaborados> getTramitesVencidosElaborados(String desde, String hasta);

    /*JC FIN*/
    public List<TramitesVencidosPendientes> getTramitesVencidosPendientes(String desde, String hasta);

    public List<TramitesCorregidos> getTramitesCorregidos(String desde, String hasta);

    public List<TramitesReasignados> getTramitesReasignados(String desde, String hasta);

    public List<MovimientosModificadosIngresados> getMovimientosModificadosIngresados(String desde, String hasta);

    public List<EvaluacionFuncionarios> getEvaluacionFuncionarios(Calendar desde, AclUser user);

    public List<DatosTramitesFinalizados> getTramiteFinalizado(Long numTramite);

    public String getNombreByUserName(String user);

    public List<AclUser> getUsuariosByRolName(String nombre);

    public BigDecimal getValorNotasCredito(Date desde, Date hasta, Long caja);

    public List<TareaWF> getTasksWorkflow(String usuario);

    public List<TareaWF> getTasksWorkflow(String usuario, int case_);
    
    public List<TareaWF> getTasksWorkflow(int case_, Object[] values);

    public List<TareaWF> getTasksWorkflow(String usuario, Date desde, Date hasta);
    
    public List<AnexoCotadDetalleMovimiento> getMovimientosAnexoCotad(String fecha_inscripcion);
    
    public List<AnexoCotadDetalleMovimientoPartes> getAnexoCotadIntervinientes(BigInteger movimiento_id);
    
    public List<AnexoCotadDetalleMovimientoFichas> getAnexoCotadFichas(BigInteger movimiento_id);
    
    public List<AnexoSuperciaDetalleMovimiento> getMovimientosAnexoSupercia(String fecha_inscripcion);
    
    public List<AnexoSuperciaDetalleMovimientoRepresentantes> getAnexoSuperciaRepresentantes(BigInteger movimiento_id);
    
    public List<AnexoSuperciaDetalleMovimientoCapitales> getAnexoSuperciaCapitales(BigInteger movimiento_id);
    
    public List<AnexoSuperciaDetalleMovimientoSocios> getAnexoSuperciaSocios(BigInteger movimiento_id);
    
    public List <Remanentes> getRemanentesByFechaAndTipo(Long usuario, String desde, String hasta, Integer tipo);
}
