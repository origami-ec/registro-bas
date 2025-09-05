package com.origami.sgr.services.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.origami.config.SisVars;
import com.origami.sgr.models.DocumentoElectronico;
import com.origami.sgr.models.FirmaElectronica;
import com.origami.sgr.models.FirmaElectronicaModel;
import com.origami.sgr.services.interfaces.SignPDFService;
import com.origami.sgr.util.JsonDateDeserializer;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
public class PdfSigner implements SignPDFService {

    private static final Logger LOG = Logger.getLogger(PdfSigner.class.getName());

    @Override
    @Lock(LockType.READ)
    public String firmaEC(String filePdf, String tipo, String codigo, String clave, String FIRMA_PATH, String FIRMA_PASS) {
        if (Utils.isNotEmptyString(clave)) {
            FirmaElectronica firmaElectronica = new FirmaElectronica();
            try {
                filePdf = FilenameUtils.separatorsToSystem(filePdf);
                PDDocument doc = PDDocument.load(new File(filePdf));

                firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
                firmaElectronica.setArchivo(FIRMA_PATH);
                firmaElectronica.setClave(FIRMA_PASS);
                firmaElectronica.setUbicacion(Constantes.nombreRegistro);
                firmaElectronica.setMotivo(tipo + " " + codigo);
                firmaElectronica.setArchivoFirmar(filePdf);
                firmaElectronica.setTipoFirma("QR");
                firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
                firmaElectronica.setUrlQr("");

                Integer[] posicionFirma = getFontPosition(filePdf, clave, doc.getNumberOfPages());

                firmaElectronica.setPosicionX1(posicionFirma[0].toString());
                firmaElectronica.setPosicionY1(posicionFirma[1].toString());

                HttpResponse httpResponse;
                Gson gson = new Gson();

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(SisVars.urlFirmaEC + "firmaElectronica/generar");
                httpPost.setEntity(new StringEntity(gson.toJson(firmaElectronica), "UTF-8"));
                httpPost.setHeader("Content-type", "application/json; charset=utf-8");
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
                try {
                    httpResponse = futureResponse.get(60, TimeUnit.SECONDS);
                    if (httpResponse != null) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(httpResponse.getEntity().getContent()));
                        String inputLine;
                        StringBuilder sb = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            sb.append(inputLine);
                        }
                        in.close();

                        firmaElectronica = gson.fromJson(sb.toString(), FirmaElectronica.class);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
                    Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (firmaElectronica != null && firmaElectronica.getArchivoFirmado() != null) {
                    return firmaElectronica.getArchivoFirmado();
                } else {
                    return filePdf;
                    //return signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                }
            } catch (IOException e) {
                Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
                return filePdf;
                //return signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
            }
        } else {
            return filePdf;
            //return signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
        }
    }

    private Integer[] getFontPosition(String filePath, final String palabra, Integer pagina) throws IOException {
        final Integer[] result = new Integer[2];
        try {

            PdfReader pdfReader = new PdfReader(filePath);
            if (null == pagina) {
                pagina = pdfReader.getNumberOfPages();
            }
            new PdfReaderContentParser(pdfReader).processContent(pagina, new RenderListener() {
                public void beginTextBlock() {
                }

                public void renderText(TextRenderInfo textRenderInfo) {
                    String text = textRenderInfo.getText();
                    if (text != null && text.contains(palabra)) {
                        com.itextpdf.awt.geom.Rectangle2D.Float textFloat = textRenderInfo.getBaseline().getBoundingRectange();
                        float x = textFloat.x;
                        float y = textFloat.y;
                        result[0] = (int) x;
                        result[1] = (int) y + 70;
                    }
                }

                public void endTextBlock() {

                }

                public void renderImage(ImageRenderInfo renderInfo) {

                }
            });

        } catch (Exception e) {
            result[0] = 70;
            result[1] = 70;
        }
        return result;
    }
    
    
    
    /*
        METODOS DE FIRMA ELECTRONICA
    */
    
     @Override
    public String firmaEC(String filePdf, String motivo, String palabraClave, String firmaArchivo, String firmaClave) {
        FirmaElectronicaModel firmaElectronica = new FirmaElectronicaModel();
        try {
            filePdf = FilenameUtils.separatorsToSystem(filePdf);
            File f = new File(filePdf);
            PDDocument doc = PDDocument.load(f);

            firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
            firmaElectronica.setArchivo(SisVars.rutaFirmaEC + File.separator + firmaArchivo);
            firmaElectronica.setClave(firmaClave);
            firmaElectronica.setUbicacion("Registro de la Propiedad del Cantón Latacunga");
            firmaElectronica.setMotivo(motivo);
            firmaElectronica.setArchivoFirmar(filePdf);
            firmaElectronica.setTipoFirma("QR");
            firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
            firmaElectronica.setUrlQr("");
            firmaElectronica.setValidarFecha(Boolean.FALSE);

            Integer[] posicionFirma = getFontPosition(filePdf, palabraClave, doc.getNumberOfPages());

            firmaElectronica.setPosicionX1(posicionFirma[0].toString());
            firmaElectronica.setPosicionY1(posicionFirma[1].toString());

            firmaElectronica = servicioRESTFirma(firmaElectronica, "firmaElectronica/generar");
            doc.close();
            if (firmaElectronica != null && firmaElectronica.getArchivoFirmado() != null) {

                return firmaElectronica.getArchivoFirmado();
            } else {
                return "";
            }
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return "";

        }
    }

    @Override
    public DocumentoElectronico verificarDocumentoElectronico(FirmaElectronicaModel firmaElectronica) throws IOException {
        HttpResponse httpResponse;
        GsonBuilder builder = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .registerTypeAdapter(Date.class, new JsonDateDeserializer());
        Gson gson = builder.create();
        DocumentoElectronico documentoElectronico = null;

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlFirmaEC + "firmaElectronica/verificarDocumento");
        httpPost.setEntity(new StringEntity(gson.toJson(firmaElectronica), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            httpResponse = futureResponse.get(60, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                documentoElectronico = gson.fromJson(sb.toString(), DocumentoElectronico.class);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentoElectronico;
    }

    @Override
    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronicaModel firmaElectronica) throws IOException {
        return servicioRESTFirma(firmaElectronica, "firmaElectronica/validar");
    }

    private FirmaElectronicaModel servicioRESTFirma(FirmaElectronicaModel firmaElectronica, String path) {
        HttpResponse httpResponse;
        GsonBuilder builder = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .registerTypeAdapter(Date.class, new JsonDateDeserializer());
        Gson gson = builder.create();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SisVars.urlFirmaEC + path);
        httpPost.setEntity(new StringEntity(gson.toJson(firmaElectronica), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            httpResponse = futureResponse.get(60, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                firmaElectronica = gson.fromJson(sb.toString(), FirmaElectronicaModel.class);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return firmaElectronica;
    }


}
