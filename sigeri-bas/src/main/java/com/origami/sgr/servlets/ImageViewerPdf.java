/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import com.origami.archivosHist.ModelText;
import com.origami.sgr.models.index.TbBlob;
//import com.sun.media.jai.codec.ImageCodec;
//import com.sun.media.jai.codec.ImageDecoder;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.inject.Inject;
//import javax.media.jai.NullOpImage;
//import javax.media.jai.OpImage;
//import javax.media.jai.PlanarImage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author dfcalderio
 */
@WebServlet(name = "ArchivosTramites", urlPatterns = {"/ImageServletPdf"})
public class ImageViewerPdf extends HttpServlet {

    @Inject
    private ArchivosService archivoHist;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Connection con = null;
            OutputStream out = response.getOutputStream();
            if (request.getParameter("idBlob") != null && NumberUtils.isNumber(request.getParameter("idBlob"))
                    && (request.getParameter("anio") != null)) {
                try {
                    Long blobID = Long.parseLong(request.getParameter("idBlob"));
                    Long fechal = Long.parseLong(request.getParameter("anio"));
                    Date fecha = new Date(fechal);
                    TbBlob tbBlob = archivoHist.getTiffMarginacion(blobID, fecha);

                    if (tbBlob == null) {
                        return;
                    }
                    if (tbBlob.getImagen() == null) {
                        return;
                    }
                    response.setContentType("application/pdf");
                    RenderedImage op;
                    /*ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", new ByteArrayInputStream(tbBlob.getImagen()), null);
                    int pages = decoder.getNumPages();
                    if (pages == 0) {
                        return;
                    }*/
                    Document document = new Document(PageSize.A4, 0, 0, 0, 0);
                    PdfWriter writer = PdfWriter.getInstance(document, out);
                    document.open();
                    /*for (int i = 0; i < pages; i++) {
                        op = decoder.decodeAsRenderedImage(i);

                        PlanarImage pi = new NullOpImage(op, null, null, OpImage.OP_IO_BOUND);
                        BufferedImage imageBuffer = pi.getAsBufferedImage();
                        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                            if (tbBlob.getAnota() != null) {
                                List<ModelText> addWaterMarks = archivoHist.getMarginaciones(tbBlob.getAnota(), i + 1);
                                archivoHist.addTextWatermark2(addWaterMarks, "jpg", imageBuffer, os, false);
                            } else {
                                ImageIO.write(imageBuffer, "jpg", os);
                            }

                            document.newPage();
                            Image jpg = Image.getInstance(os.toByteArray());
                            jpg.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());

                            document.add(jpg);
                        }
                    }*/
                    document.close();
                    writer.flush();
                    writer.close();
                    out.flush();
                    out.close();
                //} catch (IOException | NumberFormatException | SQLException | DocumentException e) {
                    //Logger.getLogger(ImageViewerPdf.class.getName()).log(Level.SEVERE, null, e);
                } catch (Exception ex) {
                    Logger.getLogger(ImageViewerPdf.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageViewerPdf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
