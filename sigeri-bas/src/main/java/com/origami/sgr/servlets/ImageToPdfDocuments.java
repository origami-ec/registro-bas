/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.origami.documental.ejbs.DocumentsEjb;
import com.origami.documental.entities.TbBlob;
import com.origami.documental.entities.TbLibrerias;
import com.origami.session.ServletSession;
import com.origami.sgr.util.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Anyelo
 */
@WebServlet(name = "docsPdf", urlPatterns = {"/docsPdf"})
public class ImageToPdfDocuments extends HttpServlet {

    @Inject
    private DocumentsEjb de;
    @Inject
    private ServletSession ss;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            if (ss == null || ss.estaVacio()) {
                try (PrintWriter salida = response.getWriter()) {
                    salida.println(com.origami.sgr.util.Constantes.salidaReportes);
                }
                return;
            }
            response.setContentType("application/pdf");
            TbLibrerias bases = (TbLibrerias) ss.getParametros().get("libreria");
            if (bases != null) {
                response.addHeader("Content-disposition", "filename=" + ss.getParametros().get("idTransaccion") + ".pdf");
                Map<String, Object> paramt = new HashMap<>();
                paramt.put("idTransaccion", ss.getParametros().get("idTransaccion"));
                List<TbBlob> blobs = de.findAll(TbBlob.class, paramt);
                PDDocument document = new PDDocument();
                if (Utils.isNotEmpty(blobs)) {
                    for (TbBlob blob : blobs) {
                        PDPage page = new PDPage(new PDRectangle(blob.getWidthBlob(), blob.getHeightBlob()));
                        /*PDImageXObject img = PDImageXObject.createFromByteArray(document, blob.getObjBlob(), */
                        String name = (blob.getFileName() == null ? blob.getOrdSalida() : blob.getFileName()) + ".png";
                        PDImageXObject img = PDImageXObject.createFromByteArray(document, blob.getObjBlob(), name);
                        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                            contentStream.drawImage(img, 0, 0);
                        }
                        document.addPage(page);
                    }
                    document.save(response.getOutputStream());
                    document.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Pagina PNG!");
            //Logger.getLogger(ImageToPdfDocuments.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (IOException | SQLException | ServletException ex) {
            System.out.println("Pagina PNG!");
            //Logger.getLogger(ImageToPdfDocuments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Generador de pdf de imagenes.";
    }

}
