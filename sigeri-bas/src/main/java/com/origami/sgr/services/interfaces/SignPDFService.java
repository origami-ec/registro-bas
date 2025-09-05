/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.models.DocumentoElectronico;
import com.origami.sgr.models.FirmaElectronicaModel;
import java.io.IOException;
import javax.ejb.Local;

/**
 *
 * @author Tech2Go
 */
@Local
public interface SignPDFService {

    public String firmaEC(String filePdf, String motivo, String codigo, String claseCertificado, String FIRMA_PATH, String FIRMA_PASS);

    public String firmaEC(String filePdf, String motivo, String palabraClave, String firmaArchivo, String firmaClave);

    public DocumentoElectronico verificarDocumentoElectronico(FirmaElectronicaModel firmaElectronica) throws IOException;

    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronicaModel firmaElectronica) throws IOException;

}
