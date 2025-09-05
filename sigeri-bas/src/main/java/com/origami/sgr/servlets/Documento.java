/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.origami.session.ServletSession;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Constantes;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

/**
 *
 * @author origami-idea
 */
@WebServlet(name = "Documento", urlPatterns = {"/Documento"})
public class Documento extends HttpServlet {

    @Inject
    private ServletSession ss;

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

        Map parametros;
        JasperPrint jasperPrint;
        OutputStream outStream;
        Connection conn = null;
        if (ss.estaVacio()) {
            response.setContentType("text/html");
            try (PrintWriter salida = response.getWriter()) {
                salida.println(Constantes.salidaReportes);
            }
            return;
        }
        parametros = ss.getParametros();
        response.setContentType("application/pdf");

        if (ss.tieneParametro("ciRuc")) {
            response.addHeader("Content-disposition", "filename=" + ss.getNombreReporte() + ss.retornarValor("ciRuc") + ".pdf");
        } else {
            response.addHeader("Content-disposition", "filename=" + ss.getNombreReporte() + ".pdf");
        }

        try {

            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            request.setCharacterEncoding("UTF-8");
            String ruta;
            if (ss.getNombreSubCarpeta() == null) {
                ruta = getServletContext().getRealPath("//reportes//" + ss.getNombreReporte() + ".jasper");
            } else {
                ruta = getServletContext().getRealPath("//reportes//" + ss.getNombreSubCarpeta() + "//" + ss.getNombreReporte() + ".jasper");
            }

            if (ss.getTieneDatasource()) {
                jasperPrint = JasperFillManager.fillReport(ruta, parametros, conn);
                if (ss.getEncuadernacion() != null && ss.getEncuadernacion()) {
                    Integer margen;
                    if (ss.getMargen() == null) {
                        margen = 30;
                    } else {
                        margen = ss.getMargen();
                    }
                    List<JRPrintPage> pages = jasperPrint.getPages();
                    JRPrintPage page;
                    List<JRPrintElement> elements;
                    for (int i = 1; i < pages.size() + 1; i++) {
                        page = (JRPrintPage) pages.get(i - 1);
                        elements = page.getElements();
                        if (i % 2 != 0) {//IMPAR
                            for (JRPrintElement temp : elements) {
                                temp.setX(temp.getX() + margen);
                            }
                        } else {//PAR
                            for (JRPrintElement temp : elements) {
                                temp.setX(temp.getX() - margen);
                            }
                        }
                    }
                }
                if (ss.getEncuadernacionV2() != null && ss.getEncuadernacionV2()) {
                    Integer margen;
                    if (ss.getMargen() == null) {
                        margen = 40;
                    } else {
                        margen = ss.getMargen();
                    }
                    List<JRPrintPage> pages = jasperPrint.getPages();
                    JRPrintPage page;
                    List<JRPrintElement> elements;
                    for (int i = 1; i < pages.size() + 1; i++) {
                        page = (JRPrintPage) pages.get(i - 1);
                        elements = page.getElements();
                        if (i % 2 == 0) {//SOLO PAGINAS PARES
                            for (JRPrintElement temp : elements) {
                                temp.setX(temp.getX() - margen);
                            }
                        }
                    }
                }
            } else {
                JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList());
                if (ss.getDataSource() != null) {
                    dataSource = new JRBeanCollectionDataSource(ss.getDataSource());
                }
                jasperPrint = JasperFillManager.fillReport(ruta, parametros, dataSource);

            }
            if (ss.getAgregarReporte() != null && ss.getAgregarReporte()) {
                for (Map reporte : ss.getReportes()) {
                    if (reporte.containsKey("nombreSubCarpeta")) {
                        ruta = getServletContext().getRealPath("//reportes//" + reporte.get("nombreSubCarpeta") + "//" + reporte.get("nombreReporte") + ".jasper");
                    } else {
                        ruta = getServletContext().getRealPath("//reportes//" + ss.getNombreSubCarpeta() + "//" + reporte.get("nombreReporte") + ".jasper");
                    }
                    JasperPrint jasperPrint2 = JasperFillManager.fillReport(ruta, reporte, conn);
                    if (jasperPrint2.getPages() != null && !jasperPrint2.getPages().isEmpty()) {
                        jasperPrint.addPage(jasperPrint2.getPages().get(0));
                    }
                }
            }
            outStream = response.getOutputStream();
            if (ss.getGeneraFile()) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, ss.getRutaDocumento());
                IOUtils.copy(new FileInputStream(ss.getRutaDocumento()), outStream);
            } else if (ss.getImprimir()) {
                JRPdfExporter pdfExporter = new JRPdfExporter();
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                SimplePdfExporterConfiguration pdfConfiguration = new SimplePdfExporterConfiguration();
                pdfConfiguration.setPdfJavaScript("this.print({bUI: true,bSilent: false, bShrinkToFit: true});\r");
                pdfConfiguration.setOverrideHints(Boolean.TRUE);
                pdfExporter.setConfiguration(pdfConfiguration);
                pdfExporter.exportReport();
            } else {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
            }

            outStream.flush();
            outStream.close();
            ss.borrarDatos();
            conn.close();
        } catch (SQLException | JRException | IOException e) {
            if (conn != null) {
                conn.close();
            }
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            ss.borrarDatos();
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
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
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
