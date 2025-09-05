/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.origami.session.ServletSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
 * @author Anyelo
 */
@WebServlet(name = "DownLoadFiles", urlPatterns = {"/DownLoadFiles"})
public class DownLoadFiles extends HttpServlet {

    @Inject
    private ServletSession ss;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        OutputStream os;
        try {
            if (ss.getNombreDocumento() != null) {
                File file = new File(ss.getNombreDocumento());
                if (file.exists()) {
                    String fileName = file.getName();
                    byte[] bytes;
                    try (FileInputStream fis = new FileInputStream(file)) {
                        bytes = new byte[fis.available()];
                        fis.read(bytes);
                    }
                    if (ss.getContentType() == null) {
                        response.setContentType("application/pdf");
                    } else {
                        response.setContentType(ss.getContentType());
                    }
                    response.setCharacterEncoding("UTF-8");
                    //response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
                    response.setContentLength(bytes.length);
                    os = response.getOutputStream();
                    os.write(bytes, 0, bytes.length);
                    os.flush();
                    os.close();
                }
            }
            ss.borrarDatos();
        } catch (IOException e) {
            Logger.getLogger(DownLoadFiles.class.getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(DownLoadFiles.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DownLoadFiles.class.getName()).log(Level.SEVERE, null, ex);
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
    }

}
