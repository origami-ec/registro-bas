/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.origami.session.ServletSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author origami-idea
 */
@WebServlet(name = "PdfFolio", urlPatterns = {"/PdfFolio"}, smallIcon = "/resources/paradise-layout/images/origami.ico")
public class GenerarPdfFolio extends HttpServlet {

    @Inject
    ServletSession servletSession;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException Exception
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        OutputStream outStream;
        if (servletSession.estaVacio()) {
            response.setContentType("text/html");
            try (PrintWriter salida = response.getWriter()) {
                salida.println(com.origami.sgr.util.Constantes.salidaReportes);
            }
            return;
        }
        Integer desde = (Integer) servletSession.getParametros().get("desde");
        Integer hasta = (Integer) servletSession.getParametros().get("hasta");
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "filename=" + servletSession.getNombreReporte() + ".pdf");
        try {
            outStream = response.getOutputStream();
            Document document = new Document(PageSize.A4, 25, 25, 25, 25);
            PdfWriter writer = PdfWriter.getInstance(document, outStream);
            writer.setInitialLeading(0);
            document.open();
            for (int i = desde; i <= hasta; i++) {
                System.out.println("Desde " + i);
//                if (i == desde) {
//                    PdfContentByte canvas = writer.getDirectContent();
//                    ColumnText.showTextAligned(canvas, 4, new Phrase("" + i), 20, 20, 0);
//                } else {

                document.newPage();
                HeaderFooter headerFooter = new HeaderFooter(new Phrase("" + i), false);
                headerFooter.setAlignment(HeaderFooter.ALIGN_RIGHT);
                headerFooter.disableBorderSide(Rectangle.TOP);
                headerFooter.disableBorderSide(Rectangle.BOTTOM);
                document.setHeader(headerFooter);
                writer.setPageEmpty(false);
//                }
            }
            document.addTitle(servletSession.getNombreDocumento());
            document.close();
            outStream.flush();
            outStream.close();
            servletSession.borrarDatos();
        } catch (IOException | DocumentException e) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, e);
        } finally {
        }
    }
    private static final int BLANK_THRESHOLD = 160;

    private void copyPdf(Document document, PdfWriter writer1, Integer desde, Integer hasta, ByteArrayOutputStream out, OutputStream outWriter) {
        try {
            PdfReader r = new PdfReader(out.toByteArray());
            PdfCopy writer = new PdfCopy(document, outWriter);
            writer1.freeReader(r);
            r.selectPages(desde + " - " + hasta);
            for (int i = 1; i <= r.getNumberOfPages(); i++) {
                // first check, examine the resource dictionary for /Font or
                // /XObject keys.  If either are present -> not blank.
                PdfDictionary pageDict = r.getPageN(i);
                PdfDictionary resDict = (PdfDictionary) pageDict.get(PdfName.RESOURCES);
                boolean noFontsOrImages = true;
                if (resDict != null) {
                    noFontsOrImages = resDict.get(PdfName.FONT) == null && resDict.get(PdfName.XOBJECT) == null;
                }
                System.out.println(i + " noFontsOrImages " + noFontsOrImages);
                if (!noFontsOrImages) {
                    byte bContent[] = r.getPageContent(i);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bs.write(bContent);
                    System.out.println(i + bs.size() + " > BLANK_THRESHOLD " + (bs.size() > BLANK_THRESHOLD));
                    if (bs.size() > BLANK_THRESHOLD) {
                        writer.addPage(writer.getImportedPage(r, i));
                    }
                }
            }
            writer.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
