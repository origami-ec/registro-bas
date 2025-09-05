/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.FirmaElectronica;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.models.DocumentoElectronico;
import com.origami.sgr.models.FirmaElectronicaModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author asilva
 */
@Local
public interface FirmaDigitalLocal {

    public Boolean firmarCertificado(RegCertificado ce) throws IOException;

    public String tareaFirmaCertificado(String filePdf) throws IOException;

    public String tareaFirmaCertificado(String filePdf, Long idCertificado) throws IOException;

    public File firmarCertificadoPath(RegCertificado ce) throws IOException;

    public File firmarInscripcionFile(RegMovimiento mo) throws IOException;

    public List<File> descargarCertificados(Long tramite);

    public List<File> descargarInscripciones(Long tramite);
    
    public String generarTituloCredito(Long liquidacion, Long tramite, String totalPagar);

    /*
        METODOS DE FIRMA ELECTRONICA
     */
    public DocumentoElectronico verificarDocumentoElectronico(FirmaElectronicaModel firmaElectronica) throws IOException;

    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronicaModel firmaElectronica) throws IOException;

    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronica firmaElectronica, String clave);

    public String generarDocumento(Long oid, String motivo);

    public String generarDocumento(String rutaReporte, String nombre, Map map);

    public DocumentoFirma grabarFirmaDocumento(Long id, Long referencia, Long oid, String tipo, String estado, String motivo, Long numTramite, Integer[] posicionFirma);

    public DocumentoFirma buscarFirmaDocumento(Long referencia, String tipo, String estado, String motivo, Long numTramite);

    public List<DocumentoFirma> documentosXtramite(Long numTramite);
    
    public File firmarActaInscripcion(RegMovimiento mo) throws IOException;
    
}
