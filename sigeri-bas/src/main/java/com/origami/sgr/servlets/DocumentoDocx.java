/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.MsgFormatoNotificacion;
import com.origami.sgr.util.EjbsCaller;
import com.origami.sgr.util.HiberUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

/**
 *
 * @author Origami
 */
@WebServlet(name = "DocumentoDocx", urlPatterns = {"/DocumentoWord"})
public class DocumentoDocx extends HttpServlet {

    private Map parametros;

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

        //ServletSession servletSession = (ServletSession) request.getSession().getAttribute("servletSession");
        JasperPrint jasperPrint;
        OutputStream outStream;
        Connection conn = null;
        if (servletSession == null || servletSession.estaVacio()) {
            PrintWriter salida = response.getWriter();
            MsgFormatoNotificacion msg = EjbsCaller.getTransactionManager().find(MsgFormatoNotificacion.class, 1L);
            salida.println(msg.getHeader());
            salida.println("<center><p>No hay datos que mostrar.</p></center>");
            salida.println(msg.getFooter());
            return;
        }
        parametros = servletSession.getParametros();
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;");
        if (servletSession.getNombreDocumento() == null) {
            response.addHeader("Content-disposition", "filename=" + servletSession.getNombreReporte() + "-" 
                    + new Date().getTime() + ".docx");
            //+ new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + ".docx");
        } else {
            response.addHeader("Content-disposition", "filename=" + servletSession.getNombreDocumento() + ".docx");
        }
        try {
            request.setCharacterEncoding("UTF-8");
            String ruta;
            if (servletSession.getNombreSubCarpeta() == null) {
                ruta = getServletContext().getRealPath("//reportes//" + servletSession.getNombreReporte() + ".jasper");
            } else {
                ruta = getServletContext().getRealPath("//reportes//"
                        + servletSession.getNombreSubCarpeta() + "//" + servletSession.getNombreReporte() + ".jasper");
            }
            if (servletSession.getTieneDatasource()) {
                Session sess = HiberUtil.getSession();
                SessionImplementor sessImpl = (SessionImplementor) sess;
                conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
                jasperPrint = JasperFillManager.fillReport(ruta, parametros, conn);
            } else {
                JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList());
                if (servletSession.getDataSource() != null) {
                    dataSource = new JRBeanCollectionDataSource(servletSession.getDataSource());
                }
                jasperPrint = JasperFillManager.fillReport(ruta, parametros, dataSource);
            }

            outStream = response.getOutputStream();

            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
            // Configuraciones del Reporte
            SimpleDocxReportConfiguration docxConf = new SimpleDocxReportConfiguration();
            docxConf.setFlexibleRowHeight(true);
            exporter.setConfiguration(docxConf);
            exporter.exportReport();

            servletSession.borrarDatos();

        } catch (SQLException | JRException | IOException e) {
            Logger.getLogger(DocumentoDocx.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (conn != null) {
                conn.close();
            }
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
            Logger.getLogger(DocumentoDocx.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DocumentoDocx.class.getName()).log(Level.SEVERE, null, ex);
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
