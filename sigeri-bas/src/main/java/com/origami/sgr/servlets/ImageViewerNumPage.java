/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.lowagie.text.DocumentException;
import com.origami.archivosHist.ModelText;
import com.origami.session.ServletSession;
import com.origami.sgr.models.index.TbBlob;
//import com.sun.media.jai.codec.ImageCodec;
//import com.sun.media.jai.codec.ImageDecoder;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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
import org.springframework.util.MimeTypeUtils;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author dfcalderio
 */
@WebServlet(name = "ImagenesMovimientoPage", urlPatterns = {"/ImageServletPage"})
public class ImageViewerNumPage extends HttpServlet {

    @Inject
    private ArchivosService archivoHist;
    @Inject
    private ServletSession ss;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Connection con = null;
            OutputStream out = response.getOutputStream();
            if (request.getParameter("idBlob") != null && NumberUtils.isNumber(request.getParameter("idBlob"))
                    && (request.getParameter("anio") != null) && (request.getParameter("page") != null)) {
                try {
                    Long blobID = Long.parseLong(request.getParameter("idBlob"));
                    Long fechal = Long.parseLong(request.getParameter("anio"));
                    Integer page = Integer.valueOf(request.getParameter("page"));
                    Date fecha = new Date(fechal);

                    TbBlob tbBlob = archivoHist.getTiffMarginacion(blobID, fecha);
                    if (tbBlob == null) {
                        System.out.println("No se encontro Regitro " + blobID + " fecha " + fecha);
                        return;
                    }
                    if (tbBlob.getImagen() == null) {
                        System.out.println("Regitro es nulo " + blobID + " fecha " + fecha);
                        return;
                    }
                    response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
                    RenderedImage op;
                    /*ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", new ByteArrayInputStream(tbBlob.getImagen()), null);
                    int pages = decoder.getNumPages();
                    if (pages == 0) {
                        System.out.println("Sin paginas " + blobID + " fecha " + fecha);
                        return;
                    }*/
                    //op = decoder.decodeAsRenderedImage(page - 1);
                    /*PlanarImage pi = new NullOpImage(op, null, null, OpImage.OP_IO_BOUND);
                    BufferedImage imageBuffer = pi.getAsBufferedImage();
                    if (tbBlob.getAnota() != null) {
                        List<ModelText> addWaterMarks = archivoHist.getMarginaciones(tbBlob.getAnota(), page);
                        archivoHist.addTextWatermark2(addWaterMarks, "jpg", imageBuffer, out);
                    } else {
                        ImageIO.write(imageBuffer, "jpg", out);
                        File temp = File.createTempFile(new Date().getTime() + "", null);
                        temp.deleteOnExit();
                        ss.setNombreDocumento(temp.getAbsolutePath());
                        ImageIO.write(imageBuffer, "jpg", temp);

                    }*/

                    out.flush();
                    out.close();
                //} catch (IOException | NumberFormatException | SQLException | DocumentException e) {
                    //Logger.getLogger(ImageViewerNumPage.class.getName()).log(Level.SEVERE, null, e);
                } catch (Exception ex) {
                    Logger.getLogger(ImageViewerNumPage.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ImageViewerNumPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
