/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

/**
 *
 * @author andysanchez
 */
@Named
@ViewScoped
public class NotaDevolutivaRp extends BpmManageBeanBaseRoot implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(NotaDevolutivaRp.class.getName());

    protected RegpNotaDevolutiva notaDevolutiva;
    protected HashMap<String, Object> par;
    protected String observacion = "", nombreArchivo = "";
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected Boolean hasDoc, online = false, quienRetiraSolicita, quienReingresaSolicita;
    protected Valores valor;
    protected List<RegpNotaDevolutiva> notaDevolutivaAnalisis;
    protected LazyModel<RegpNotaDevolutiva> notaDevolutivas;
    protected Date fecha;
    protected RegRegistrador registrador;

    @PostConstruct
    protected void init() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    quienReingresaSolicita = false;
                    quienRetiraSolicita = false;
                    hasDoc = true;
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    this.cargarArchivos(Constantes.indexacionHabilitantes, tramite);
                    if (liquidacion.getEstadoPago().getId() == 7L) {
                        online = true;
                    }
                    notaDevolutiva = (RegpNotaDevolutiva) manager.find(Querys.getNotaDevolutivaByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});

                    notaDevolutivaAnalisis = manager.findAll(Querys.getNotaDevolutivaByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});

                    if (Utils.isNotEmpty(notaDevolutivaAnalisis)) {
                        notaDevolutiva = notaDevolutivaAnalisis.get(0);
                        notaDevolutivaAnalisis.remove(0);
                    }
                    if (notaDevolutiva.getQuienReingresa() == null) {
                        notaDevolutiva.setQuienReingresa(new CatEnte());
                    }
                    if (notaDevolutiva.getQuienRetira() == null) {
                        notaDevolutiva.setQuienRetira(new CatEnte());
                    }
                    map = new HashMap();
                    map.put("code", Constantes.diasTramiteReingreso);
                    valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                    registrador = (RegRegistrador) manager.find(Querys.getJuridico);
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {

        }
    }

    public void mantenerEstado(Boolean mantieneEstado) {
        if (notaDevolutiva.getQuienRetira() != null && notaDevolutiva.getQuienRetira().getId() != null) {
            notaDevolutiva.setEstaRetirado(Boolean.TRUE);
        }
        if (notaDevolutiva.getQuienReingresa() != null && notaDevolutiva.getQuienReingresa().getId() != null) {
            notaDevolutiva.setEstaReingresado(Boolean.TRUE);
        }
        if (notaDevolutiva.getEstaRetirado()) {
            if (notaDevolutiva.getFechaRetiro() == null) {
                notaDevolutiva.setFechaRetiro(new Date());
            }
        }
        if (notaDevolutiva.getEstaReingresado()) {
            if (notaDevolutiva.getFechaReingreso() == null) {
                notaDevolutiva.setFechaReingreso(new Date());

            }
        }
        if (!mantieneEstado) {
            fecha = new Date();
            Date temp = itl.diasEntregaTramite(fecha, valor.getValorNumeric().intValue());
            notaDevolutiva.setFechaEntregaTramite(temp);
            notaDevolutiva.setUsuarioReingresa(session.getName_user());
            ht.setFechaEntrega(temp);
            manager.update(ht);
        } else {
            notaDevolutiva.setUsuarioMantieneEstado(session.getName_user());
            if (notaDevolutiva.getObservacion().isEmpty()) {
                notaDevolutiva.setObservacion(observacion);
            } else {
                notaDevolutiva.setObservacion(notaDevolutiva.getObservacion() + " " + observacion);
            }
        }
        try {
            Hibernate.initialize(notaDevolutiva);
            if (notaDevolutiva.getQuienRetira() != null && notaDevolutiva.getQuienRetira().getId() == null) {
                notaDevolutiva.setQuienRetira(null);
            }
            if (notaDevolutiva.getQuienReingresa() != null && notaDevolutiva.getQuienReingresa().getId() == null) {
                notaDevolutiva.setQuienReingresa(null);
            }
            if (manager.persist(notaDevolutiva) != null) {
                JsfUti.messageInfo(null, Messages.correcto, "");
                if (mantieneEstado) {
                    JsfUti.redirectFaces("/procesos/manage/entregaDocumentos.xhtml");
                }
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (HibernateException e) {
            JsfUti.messageError(null, Messages.error, "");
            System.out.println(e);
        }
    }

    public void completarTarea() throws Exception {
        //System.out.println("completarTarea");
        if (notaDevolutiva.getEstaReingresado()) {
            this.mantenerEstado(false);
            map = new HashMap();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
            this.cargarDatosReporte();
        } else {
            JsfUti.messageError(null, "Debe Seleccionar la opcion de que el tramite esta Reingresado", "");
        }
    }

    public void completarTareaDevolutiva() throws Exception {
        if (notaDevolutiva.getEstaReingresado()) {
            this.mantenerEstado(false);
            map = new HashMap();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            //this.cargarDatosReporte();
            this.continuar();
        } else {
            JsfUti.messageError(null, "Debe Seleccionar la opcion de que el tramite esta Reingresado", "");
        }
    }

    public void descargarNotaDevolutiva(Long idNotaDevolutiva) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("NotaDevolutiva");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", idNotaDevolutiva);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab("/sgr/OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                        + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void showFormularioOnline() {
        try {
            if (ht.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("FormularioOnline");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_TRAMITE", ht.getId());
                ss.agregarParametro("FOTO1", this.getImage(Querys.getOid1Solicitud));
                ss.agregarParametro("FOTO2", this.getImage(Querys.getOid2Solicitud));
                ss.agregarParametro("FOTO3", this.getImage(Querys.getOid3Solicitud));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                //JsfUti.redirectNewTab("/sgr/Documento");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public InputStream getImage(String sql) {
        BigInteger oid = (BigInteger) manager.getNativeQuery(sql, new Object[]{ht.getId()});
        if (oid != null) {
            return ou.streamFile(oid.longValue());
        }
        return null;
    }

    public void downLoadDocument() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{ht.getId()});
            if (oid != null) {
                JsfUti.redirectNewTab("/sgr/OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void cargarDatosReporte() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            ss.setNombreReporte("comprobante_proforma_reingreso");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");

            List<String> urlList = new ArrayList<>();
            String url = "/procesos/dashBoard.xhtml";
            urlList.add(SisVars.urlbase + "Documento");
            JsfUti.redirectMultipleConIP_V2(url, urlList);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarQuienRetira() {
        try {
            if (!notaDevolutiva.getQuienRetira().getCiRuc().isEmpty()) {
                CatEnte catEnte = reg.buscarGuardarEnteDinardap(notaDevolutiva.getQuienRetira().getCiRuc());
                if (catEnte != null) {
                    notaDevolutiva.setQuienRetira(catEnte);
                }
                JsfUti.update("mainForm:mainTab:datosRetiroRetira");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarQuienReingresa() {
        if (!notaDevolutiva.getQuienReingresa().getCiRuc().isEmpty()) {
            CatEnte catEnte = reg.buscarGuardarEnteDinardap(notaDevolutiva.getQuienReingresa().getCiRuc());
            if (catEnte != null) {
                notaDevolutiva.setQuienReingresa(null);
                notaDevolutiva.setQuienReingresa(catEnte);
                notaDevolutiva.setEstaReingresado(Boolean.TRUE);
            }
            JsfUti.update("mainForm:mainTab:datosRetiroReingreso");
        }
    }

    public void limpiarQuienRetira() {
        notaDevolutiva.setQuienRetira(new CatEnte());
    }

    public void limpiarQuienReingresa() {
        notaDevolutiva.setQuienReingresa(new CatEnte());
    }

    public void quienRetiraEsSolicitante() {
        if (quienRetiraSolicita) {
            notaDevolutiva.setQuienRetira(liquidacion.getSolicitante());
            notaDevolutiva.setEstaRetirado(Boolean.TRUE);
        } else {
            notaDevolutiva.setQuienRetira(new CatEnte());
            notaDevolutiva.setEstaRetirado(Boolean.FALSE);
        }

    }

    public void quienReingresaEsSolicitante() {
        if (quienReingresaSolicita) {
            notaDevolutiva.setQuienReingresa(liquidacion.getSolicitante());
            notaDevolutiva.setEstaReingresado(Boolean.TRUE);
        } else {
            notaDevolutiva.setQuienReingresa(new CatEnte());
            notaDevolutiva.setEstaReingresado(Boolean.FALSE);
        }
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getHasDoc() {
        return hasDoc;
    }

    public void setHasDoc(Boolean hasDoc) {
        this.hasDoc = hasDoc;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public List<RegpNotaDevolutiva> getNotaDevolutivaAnalisis() {
        return notaDevolutivaAnalisis;
    }

    public void setNotaDevolutivaAnalisis(List<RegpNotaDevolutiva> notaDevolutivaAnalisis) {
        this.notaDevolutivaAnalisis = notaDevolutivaAnalisis;
    }

    public LazyModel<RegpNotaDevolutiva> getNotaDevolutivas() {
        return notaDevolutivas;
    }

    public void setNotaDevolutivas(LazyModel<RegpNotaDevolutiva> notaDevolutivas) {
        this.notaDevolutivas = notaDevolutivas;
    }

    public Boolean getQuienRetiraSolicita() {
        return quienRetiraSolicita;
    }

    public void setQuienRetiraSolicita(Boolean quienRetiraSolicita) {
        this.quienRetiraSolicita = quienRetiraSolicita;
    }

    public Boolean getQuienReingresaSolicita() {
        return quienReingresaSolicita;
    }

    public void setQuienReingresaSolicita(Boolean quienReingresaSolicita) {
        this.quienReingresaSolicita = quienReingresaSolicita;
    }

}
