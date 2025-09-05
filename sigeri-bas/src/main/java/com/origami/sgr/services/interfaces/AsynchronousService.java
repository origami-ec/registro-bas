/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.models.ActosRequisito;
import com.origami.sgr.models.Sms;
import java.io.File;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface AsynchronousService {

    public void testSendEmail(String destinatario, Long tipo);
    
    public void enviarCorreosRIDEbasico(List<RegpLiquidacion> liquidaciones, RenCajero cajero);

    public void enviarCorreosRIDE(List<RegpLiquidacion> liquidaciones, RenCajero cajero);

    public void enviarCorreoTramiteFinalizadoV1(RegpLiquidacion re);

    public void enviarCorreoInicioTramite(RegpLiquidacion re);

    public void enviarCorreoProformaTramite(RegpLiquidacion re);

    public void sendNotificationFirebaseUserAndorid(CatEnte solicitante, Long numTramiteRp);

    public void generarFirmaDigital(Long numTramite);

    public void generarFirmaDigital(RegCertificado certificado);

    public List<File> generarFirmaDigitalArchivos(Long tramite);

    public void enviarCorreoTramiteFinalizado(RegpLiquidacion re, List<File> archivos);

    public void enviarCorreoProformaInscripcionSmsTramite(RegpLiquidacion re, Sms sms);

    public void enviarCorreoProformaInscripcionLinkTramite(RegpLiquidacion re, Sms sms);
    
    public void enviarCorreoSolicitudIncripcion(PubSolicitud solicitud, List<ActosRequisito> requisitos);

    public void enviarCorreoSolicitudIncripcionObservaciones(PubSolicitud solicitud);
    
    public void firmarDocumentosEnviarCorreo(RegpLiquidacion liquidacion);
    
    public File generarFirmaInscripciones(Long tramite);
    
    public void enviarCorreoCertificados(RegpLiquidacion re, List<File> archivos);
    
    public void enviarCorreoInscripcion(RegpLiquidacion re, List<File> archivos);
    
    public void enviarNotificacionFinTramite(RegpLiquidacion re);
    
    public void firmarActasInscripcion(Long tramite);
    
    public void enviarCorreoTituloCredito(RegpLiquidacion re, String archivo, String usuario);
    
    public void enviarCorreoNotaDevolutiva(RegpLiquidacion re, String archivo, String usuario);
    
    public void enviarCorreoFinTramite(RegpLiquidacion re, List<String> archivos, String usuario);
    
    public Boolean reenviarCorreoDocumentos(Long numTramite, String correo, String usuario);
    
    public void enviarCorreoFinTramite(Long tramite, String correo, List<File> archivos, String usuario);
    
    public void enviarCorreoNoIngreso(RegpLiquidacion re, String usuario);
    
    public void enviarCorreoTramiteOnline(RegpLiquidacion re, String usuario);
    
}
