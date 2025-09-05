/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.config.SisVars;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.FirmaElectronica;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.models.DocumentoElectronico;
import com.origami.sgr.models.FirmaElectronicaModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.SignPDFService;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

/**
 *
 * @author Asilva
 */
@Stateless
public class FirmaDigitalEjb implements FirmaDigitalLocal {

    @Inject
    private RutasSystemContr rutas;
    @Inject
    private SignPDFService signer;
    @Inject
    private Entitymanager em;
    @Inject
    private OmegaUploader ou;
    @Inject
    private UserSession us;

    /**
     * Metodo que ingresa en el archivo la firma digital por certificado digital
     * configurado en los parametros
     *
     * @param ce
     * @return Object
     * @throws java.io.IOException
     */
    @Override
    public Boolean firmarCertificado(RegCertificado ce) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporte(ce, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(), ce.getTipoDocumento(), FIRMA_PATH, FIRMA_PASS);
                String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (Utils.isNotEmptyString(urlPdfFirmado)) {
                    File output = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(output)) {
                        Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                        if (oid != null) {
                            ce.setDocumento(oid);
                        }
                        em.update(ce);
                    }
                    File input = new File(filePdf);
                    input.delete();
                    //output.delete();
                    return true;
                } else {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public String generarReporte(RegCertificado ce, RegRegistrador registrador) {
        Map map;
        String reporte = "";
        try {
            map = this.llenarParametros(ce, registrador);
            switch (ce.getTipoDocumento()) {
                case "C01": //CERTIFICADO DE GRAVAMEN CON FICHA
                    reporte = "CertificadoGravamen";
                    break;
                case "C02": //CERTIFICADO HISTORIADO CON FICHA
                    reporte = "CertificadoGravamenHistoriado";
                    break;
                case "C03": //CERTIFICADO LINDERADO CON FICHA
                    reporte = "CertificadoGravamenLinderado";
                    break;
                case "C04": //CERTIFICADO DE VENTAS CON FICHA
                    reporte = "CertificadoGravamenVentas";
                    break;
                case "C05": //CERTIFICADO DE BIENES
                    reporte = "CertificadoBienes";
                    break;
                case "C06": //CERTIFICADO GENERAL
                    reporte = "CertificadoGeneral";
                    break;
                case "C07": //COPIA RAZON INSCRIPCION 
                    map = this.llenarParametrosRazon(ce, registrador);
                    reporte = "CopiaRazonInscripcion";
                    break;
            }
            String ruta = rutas.getRootPath() + "/reportes/certificados/" + reporte + ".jasper";
            String nombre = "C_" + ce.getNumCertificado() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametros(RegCertificado ce, RegRegistrador registrador) {
        try {
            Map map = new HashMap();
            map.put("ID_CERTIFICADO", ce.getId());
            map.put("EMISION", ce.getFechaEmision());
            map.put("SOLICITANTE", ce.getNombreSolicitante());
            map.put("USO_DOCUMENTO", ce.getUsoDocumento());
            map.put("SHOW_SIGN", true);
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "reportes/certificados/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            //map.put("HEADER_URL", rutas.getRootPath() + "/resources/image/header.png");
            //map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/watermark.png");
            //map.put("FOOTER_URL", rutas.getRootPath() + "/resources/image/footer.png");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosRazon(RegCertificado ce, RegRegistrador registrador) {
        try {
            List<RegCertificadoMovimiento> rcm = em.findAll(Querys.getMovsByCertificado, new String[]{"id"}, new Object[]{ce.getId()});
            //List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            Map map = new HashMap();
            map.put("ID_MOV", rcm.get(0).getMovimiento().getId());
            map.put("ID_CERTIFICADO", ce.getId());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "/reportes/certificados/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            //map.put("HEADER_URL", rutas.getRootPath() + "/resources/image/header.png");
            //map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/watermark.png");
            //map.put("FOOTER_URL", rutas.getRootPath() + "/resources/image/footer.png");
            map.put("SHOW_SIGN", true);
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosInforme(RegCertificado ce, RegRegistrador registrador) {
        try {
            Map map = new HashMap();
            map.put("ID_CERTIFICADO", ce.getId());
            map.put("NOMBRE", ce.getBeneficiario());
            map.put("BUSQUEDA", ce.getLinderosRegistrales());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "/reportes/certificados/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            //map.put("HEADER_URL", rutas.getRootPath() + "/resources/image/header.png");
            //map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/watermark.png");
            //map.put("FOOTER_URL", rutas.getRootPath() + "/resources/image/footer.png");
            map.put("SHOW_SIGN", true);
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    protected String buildJasper(String nombrePDF, String rutaJasper, Map parametros) throws SQLException {
        Connection conn = null;
        try {
            JasperPrint jasperPrint;
            String filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaFirmados) + nombrePDF;
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            jasperPrint = JasperFillManager.fillReport(rutaJasper, parametros, conn);
            /*List<JRPrintPage> pages = jasperPrint.getPages();
            JRPrintPage page;
            List<JRPrintElement> elements;
            for (int i = 1; i < pages.size() + 1; i++) {
                page = (JRPrintPage) pages.get(i - 1);
                elements = page.getElements();
                if (i % 2 != 0) {//IMPAR
                    for (JRPrintElement temp : elements) {
                        temp.setX(temp.getX() + 30);
                    }
                } else {//PAR
                    for (JRPrintElement temp : elements) {
                        temp.setX(temp.getX() - 30);
                    }
                }
            }*/
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
            return filePdf;
        } catch (SQLException | JRException je) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, je);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    protected String buildJasperSimple(String nombrePDF, String rutaJasper, Map parametros) throws SQLException {
        Connection conn = null;
        try {
            JasperPrint jasperPrint;
            String filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaFirmados) + nombrePDF;
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            jasperPrint = JasperFillManager.fillReport(rutaJasper, parametros, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
            return filePdf;
        } catch (SQLException | JRException je) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, je);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public String tareaFirmaCertificado(String filePdf) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String urlPdfFirmado;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //urlPdfFirmado = signer.firmaEC(filePdf, "FIRMA CERTIFICADO", "CERTIFICADO", "", FIRMA_PATH, FIRMA_PASS);
                urlPdfFirmado = signer.firmaEC(filePdf, "FIRMA CERTIFICADO", "CERTIFICADO",
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File f = new File(filePdf);
                    f.delete();
                }
                return urlPdfFirmado;
            }
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public String tareaFirmaCertificado(String filePdf, Long idCertificado) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String urlPdfFirmado;
        RegCertificado certificado = em.find(RegCertificado.class, idCertificado);
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //urlPdfFirmado = signer.firmaEC(filePdf, certificado.getClaseCertificado(), certificado.getNumCertificado().toString(), certificado.getTipoDocumento(), FIRMA_PATH, FIRMA_PASS);
                urlPdfFirmado = signer.firmaEC(filePdf, certificado.getClaseCertificado(), certificado.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File f = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(f)) {
                        Long oid = ou.upFileDocument(fs, f.getName(), "application/pdf");
                        if (oid != null) {
                            certificado.setDocumento(oid);
                        }
                        certificado.setSecuencia(1);
                        em.update(certificado);
                    }
                }
                return urlPdfFirmado;
            }
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public File firmarCertificadoPath(RegCertificado ce) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporte(ce, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(), ce.getTipoDocumento(), FIRMA_PATH, FIRMA_PASS);
                String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File output = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(output)) {
                        Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                        if (oid != null) {
                            ce.setDocumento(oid);
                        }
                        em.update(ce);
                    }
                    File input = new File(filePdf);
                    input.delete();
                    //output.delete();
                    return output;
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public File firmarInscripcionFile(RegMovimiento mo) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteRazonInscripcion(mo, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf, mo.getActo().getNombre(), mo.getNumRepertorio().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        mo.setDocumento(oid);
                    }
                    em.update(mo);
                }
                File input = new File(filePdf);
                input.delete();
                //output.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    public String generarReporteActaInscripcion(RegMovimiento mo, RegRegistrador registrador) {
        Map map;
        try {
            map = this.llenarParametrosActaInscripcion(mo, registrador);
            String ruta = rutas.getRootPath() + "/reportes/registro/ActaInscripcion.jasper";
            //String nombre = "ACTA_" + Utils.quitarTildes(mo.getLibro().getNombre().replace(" ", "_")) + "_" + mo.getNumRepertorio() + ".pdf";
            String nombre = "ACTA_" + mo.getId() + ".pdf";
            return this.buildJasperSimple(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String generarReporteRazonInscripcion(RegMovimiento mo, RegRegistrador registrador) {
        Map map;
        try {
            map = this.llenarParametrosRazonInscripcion(mo, registrador);
            String ruta = rutas.getRootPath() + "/reportes/registro/RazonInscripcion_v1.jasper";
            //String nombre = "RAZON_" + Utils.quitarTildes(mo.getLibro().getNombre().replace(" ", "_")) + "_" + mo.getNumRepertorio() + ".pdf";
            String nombre = "RAZON_" + mo.getId() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosActaInscripcion(RegMovimiento mo, RegRegistrador registrador) {
        try {
            Map map = new HashMap();
            map.put("P_MOVIMIENTO", mo.getId());
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("ACCION_PERSONAL", registrador.getRazonReporte());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "reportes/registro/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosRazonInscripcion(RegMovimiento mo, RegRegistrador registrador) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map map = new HashMap();
            Calendar cl = Calendar.getInstance();
            if (mo.getFechaRepertorio() != null) {
                cl.setTime(mo.getFechaRepertorio());
            } else {
                cl.setTime(mo.getFechaInscripcion());
            }
            Integer year = cl.get(Calendar.YEAR);
            map.put("INSCRIPTOR", mo.getUserCreador().getUsuario());
            map.put("REPERTORIO", mo.getNumRepertorio());
            map.put("INDEXADOR", mo.getResponseCatastro());
            map.put("ANIO", year.toString());
            map.put("FECHA_REP", mo.getFechaRepertorio());
            map.put("FECHA_REPERTORIO", sdf.format(mo.getFechaRepertorio()));
            map.put("TRAMITE", mo.getTramite().getTramite().getNumTramite());
            map.put("COMPROBANTE", mo.getTramite().getDetalle().getLiquidacion().getCodigoComprobante());
            map.put("COD_VERIFICACION", mo.getCodVerificacion());
            map.put("PROPIEDAD", mo.getLibro().getPropiedad());
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("ACCION_PERSONAL", registrador.getRazonReporte());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "reportes/registro/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List<File> descargarCertificados(Long tramite) {
        List<RegCertificado> certificados;
        List<File> files;
        String ruta;
        File file;
        try {
            files = new ArrayList<>();
            certificados = em.findAll(Querys.getCertificados, new String[]{"tramite"}, new Object[]{tramite});
            for (RegCertificado ce : certificados) {
                ruta = SisVars.rutaFirmados;
                if (ce.getDocumento() != null) {
                    ruta = ruta + ce.getCodVerificacion() + ".pdf";
                    file = new File(ruta);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        ou.streamFile(ce.getDocumento(), fos);
                        fos.close();
                    }
                    if (file.isFile()) {
                        files.add(file);
                    }
                }
            }
            return files;
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List<File> descargarInscripciones(Long tramite) {
        RegMovimiento movimiento;
        List<File> files;
        String ruta;
        File file;
        try {
            files = new ArrayList<>();
            movimiento = (RegMovimiento) em.find(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{tramite});
            if (movimiento != null && movimiento.getDocumento() != null) {
                ruta = SisVars.rutaFirmados;
                ruta = ruta + movimiento.getCodVerificacion() + ".pdf";
                file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(movimiento.getDocumento(), fos);
                    fos.close();
                }
                if (file.isFile()) {
                    files.add(file);
                }
            }
            return files;
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /*
        METODOS DE FIRMA ELECTRONICA
     */
    @Override
    public DocumentoElectronico verificarDocumentoElectronico(FirmaElectronicaModel firmaElectronica) throws IOException {
        return signer.verificarDocumentoElectronico(firmaElectronica);
    }

    @Override
    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronicaModel firmaElectronica) throws IOException {
        return signer.validarFirmaElectronica(firmaElectronica);
    }

    @Override
    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronica firmaElectronica, String clave) {
        FirmaElectronicaModel firmaElectronicaValidar = new FirmaElectronicaModel();
        firmaElectronicaValidar.setArchivo(SisVars.rutaFirmaEC + File.separator + firmaElectronica.getUid());
        firmaElectronicaValidar.setClave(clave);
        try {
            return signer.validarFirmaElectronica(firmaElectronicaValidar);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String generarDocumento(Long oid, String motivo) {
        String archivo = Utils.createDirectoryIfNotExist(SisVars.rutaTemporales) + motivo + new Date().getTime() + ".pdf";
        File file = new File(archivo);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ou.streamFile(oid, fos);
            fos.close();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        return archivo;
    }

    @Override
    public String generarDocumento(String rutaReporte, String nombre, Map map) {
        return generarJasper(rutaReporte, nombre, map);
    }

    @Override
    public DocumentoFirma grabarFirmaDocumento(Long id, Long referencia, Long oid, String tipo, String estado, String motivo, Long numTramite, Integer[] posicionFirma) {
        Map map = new HashMap();
        map.put("codename", tipo);
        CtlgItem ci = (CtlgItem) em.findObjectByParameter(CtlgItem.class, map);

        DocumentoFirma doc = new DocumentoFirma();
        doc.setId(id);
        doc.setReferencia(referencia);
        doc.setDocumento(oid);
        doc.setUsuario(us.getName_user());
        doc.setMotivo(motivo);
        doc.setFecha(new Date());
        doc.setTipo(ci);
        doc.setNumTramite(numTramite);
        doc.setPosicionX1(posicionFirma[0]);
        doc.setPosicionY1(posicionFirma[1]);
        doc.setNumeroPagina(posicionFirma[2]);
        map = new HashMap();
        map.put("codename", estado);
        ci = (CtlgItem) em.findObjectByParameter(CtlgItem.class, map);

        doc.setEstado(ci);
        em.persist(doc);
        return doc;
    }

    @Override
    public DocumentoFirma buscarFirmaDocumento(Long referencia, String tipo, String estado, String motivo, Long numTramite) {

        Map map = new HashMap();
        map.put("codename", tipo);
        CtlgItem ci = (CtlgItem) em.findObjectByParameter(CtlgItem.class, map);
        map = new HashMap();
        map.put("codename", estado);
        CtlgItem es = (CtlgItem) em.findObjectByParameter(CtlgItem.class, map);

        map = new HashMap();

        map.put("usuario", us.getName_user());
        map.put("referencia", referencia);
        map.put("numTramite", numTramite);
        if (motivo != null) {
            map.put("motivo", motivo);
        }
        map.put("tipo.id", ci.getId());
        map.put("estado.id", es.getId());

        DocumentoFirma doc = (DocumentoFirma) em.findObjectByParameter(DocumentoFirma.class, map);

        return doc;
    }

    public String generarJasper(String rutaReporte, String nombre, Map map) {

        Connection conn = null;
        try {

            String ruta = rutas.getRootPath() + "reportes/" + rutaReporte;
            String filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaTemporales) + nombre + ".pdf";
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, map, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
            return filePdf;
        } catch (SQLException | JRException je) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, je);
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public List<DocumentoFirma> documentosXtramite(Long numTramite) {
        List<DocumentoFirma> documentos = em.findAll(Querys.getDocumentosTramiteFirma, new String[]{"numTramite"}, new Object[]{numTramite});
        return documentos;
    }

    @Override
    public File firmarActaInscripcion(RegMovimiento mo) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteActaInscripcion(mo, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf, mo.getActo().getNombre(), mo.getNumRepertorio().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        mo.setDocumentoActa(oid);
                    }
                    em.update(mo);
                }
                File input = new File(filePdf);
                input.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    public String generarTituloCredito(Long liquidacion, Long tramite, String totalPagar) {
        try {
            Map map = new HashMap();
            map.put("ID_LIQUIDACION", liquidacion);
            map.put("VALOR_STRING", totalPagar);
            map.put("IMG_URL", rutas.getRootPath() + "/resources/image/formato_titulo.png");
            map.put("IMG_FIRMA", rutas.getRootPath() + "/resources/image/firma_titulo_credito.png");
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "/reportes/ingreso/");

            String rutaJasper = rutas.getRootPath() + "/reportes/ingreso/titulo_credito.jasper";
            String rutaPdf = SisVars.rutaTitulos + tramite.toString() + ".pdf";

            rutaPdf = this.buildJasperV2(rutaPdf, rutaJasper, map);
            File file = new File(rutaPdf);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        } catch (SQLException | IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    protected String buildJasperV2(String filePdf, String rutaJasper, Map parametros) throws SQLException, FileNotFoundException, IOException {
        Connection conn = null;
        try {
            JasperPrint jasperPrint;
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            jasperPrint = JasperFillManager.fillReport(rutaJasper, parametros, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
            return filePdf;
        } catch (SQLException | JRException je) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, je);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
