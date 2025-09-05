package com.origami.sgr.services.ejbs;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.GeneracionDocs;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.Query;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.text.RandomStringGenerator;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

@Singleton
@Lock(LockType.READ)
@Interceptors(value = {HibernateEjbInterceptor.class})
public class RegCertificadoService {

    @Inject
    private ServletSession ss;
    @Inject
    private RutasSystemContr rutas;
    @Inject
    private Entitymanager em;
    @Inject
    private OmegaUploader ou;

    public RegCertificado find(Long id) {
        return (RegCertificado) HiberUtil.getSession().get(RegCertificado.class, id);
    }

    public String genCodigoVerif() {
        char[][] pairs = {{'a', 'z'}, {'0', '9'}};
        RandomStringGenerator sg = new RandomStringGenerator.Builder().withinRange(pairs).build();
        return sg.generate(10);
    }

    public String genCertificadoPdf(Long id) throws SQLException {
        RegCertificado ce = find(id);
        RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        ss.setNombreSubCarpeta("registro");
        ss.agregarParametro("ID_CERTIFICADO", ce.getId());
        ss.agregarParametro("SUBREPORT_DIR", rutas.getRootPath() + "reportes/registro/");
        ss.agregarParametro("USER_NAME", "sistemas");
        ss.agregarParametro("EMISION", ce.getFechaEmision());
        ss.agregarParametro("IMG_URL", rutas.getRootPath() + "resources/image/logo.png");
        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
        ss.agregarParametro("NOMBRES", "REGISTRO DE LA PROPIEDAD DEL CANTON LOJA");

        switch (ce.getTipoCertificado().intValue()) {
            case 1:
                ss.setNombreReporte("FDCertificadoHistoria");
                break;
            case 2:
                ss.setNombreReporte("FDCertificadoBienesRaices");
                break;
            case 3:
                ss.setNombreReporte("FDCertificadoGeneral");
                break;
            default:
                //JsfUti.messageInfo(null, Messages.error, "");
                return null;
        }
        buildJasper(ce);
        countPrint(ce);
        return firmarPdf(ce);
    }

    public void countPrint(RegCertificado ce) {
        try {
            ce.setPrintOnline(ce.getPrintOnline() + 1);
            ce.setDatePrintOnline(new Date());
            em.update(ce);

            GeneracionDocs gen = new GeneracionDocs();
            gen.setFechaGeneracion(new Date());
            gen.setCertificado(ce.getId());
            gen.setObservacion("DESCARGA ONLINE CON FIRMA DIGITAL");
            em.persist(gen);
        } catch (Exception e) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String genCertificadoValidatePdf(Long id) throws SQLException {
        RegCertificado ce = find(id);
        RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        ss.setNombreSubCarpeta("registro");
        ss.agregarParametro("ID_CERTIFICADO", ce.getId());
        ss.agregarParametro("SUBREPORT_DIR", rutas.getRootPath() + "reportes/registro/");
        ss.agregarParametro("USER_NAME", "sistemas");
        ss.agregarParametro("EMISION", ce.getFechaEmision());
        ss.agregarParametro("IMG_URL", rutas.getRootPath() + "resources/image/logo.png");
        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
        ss.agregarParametro("NOMBRES", "REGISTRO DE LA PROPIEDAD DE PORTOVIEJO");
        switch (ce.getTipoCertificado().intValue()) {
            case 1:
                ss.setNombreReporte("FD_CHD_Validate");
                break;
            case 2:
                ss.setNombreReporte("FD_CBR_Validate");
                break;
            case 3:
                ss.setNombreReporte("FD_CG_Validate");
                break;
            default:
                //JsfUti.messageInfo(null, Messages.error, "");
                return null;
        }
        return buildJasperValidate(ce);
    }

    protected void buildJasper(RegCertificado ce) throws SQLException {
        Connection conn = null;
        try {
            JasperPrint jasperPrint;
            String rutaPdf = pdfPath(ce, false);
            Map parametros = ss.getParametros();
            String ruta = rutas.getRootPath() + "/reportes/" + ss.getNombreSubCarpeta() + "/" + ss.getNombreReporte() + ".jasper";
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            jasperPrint = JasperFillManager.fillReport(ruta, parametros, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaPdf);
        } catch (SQLException | JRException je) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, je);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    protected String buildJasperValidate(RegCertificado ce) throws SQLException {
        Connection conn = null;
        try {
            JasperPrint jasperPrint;
            String rutaPdf = pdfPath(ce, false);
            Map parametros = ss.getParametros();
            String ruta = rutas.getRootPath() + "/reportes/" + ss.getNombreSubCarpeta() + "/" + ss.getNombreReporte() + ".jasper";
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            jasperPrint = JasperFillManager.fillReport(ruta, parametros, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaPdf);
            return rutaPdf;
        } catch (SQLException | JRException je) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, je);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    protected String pdfPath(RegCertificado ce, boolean firmado) {
        String r = SisVars.rutaFirmados + ss.getNombreReporte()
                + "_" + ce.getId();
        if (firmado) {
            r = r + "_firmado";
        }
        return r + ".pdf";
    }

    protected String firmarPdf(RegCertificado ce) {
        String FIRMA_PATH;
        String FIRMA_PASS;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                //String pdf = SisVars.rutaArchivoFirmadoPDF + ss.getNombreReporte() + ".pdf";
                String outDir = SisVars.rutaFirmados;
                String sufijo = "_firmado";
                PdfSignRectangle rec = new PdfSignRectangle(50, 50, 300, 100);
                //signer.signPDF(pdfPath(ce, false), outDir, sufijo, FIRMA_PATH, FIRMA_PASS, rec);
                File f = new File(pdfPath(ce, false));
                f.delete();
                File ff = new File(pdfPath(ce, true));
                ss.borrarDatos();
                return ff.getAbsoluteFile().toString();
            }
        } catch (Exception e) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public RegCertificado findByValidationCode(String validationCode) {
        Map map = new HashMap<String, Object>();
        map.put("codVerificacion", validationCode);
        RegCertificado certificado = (RegCertificado) em.findObjectByParameter(RegCertificado.class, map);
        return certificado;
    }

    public String generatePrintCertUrl(Long numTramite) {
        /*String printUrl = SisVars.urlVentanilla + "/descargar.xhtml?codigo="
                + numTramite.toString() + "&validate=" + Utils.encriptaEnMD5(numTramite.toString());
        return printUrl;*/
        return "";
    }

    public RegCertificado getUniqueByNumTramite(Long num) {
        final Session sess = HiberUtil.getSession();
        final Query q1 = sess.createQuery("SELECT ce1 FROM RegCertificado ce1 WHERE ce1.numTramite = :num");
        q1.setParameter("num", BigInteger.valueOf(num));
        q1.setMaxResults(1);
        return (RegCertificado) q1.getSingleResult();
    }

    public Long findByValidationCode(String validationCode, Integer tipo) {
        Map map = new HashMap<String, Object>();
        map.put("codVerificacion", validationCode);
        try {
            switch (tipo) {
                case 1:
                    RegCertificado certificado = (RegCertificado) em.findObjectByParameter(RegCertificado.class, map);
                    if (certificado != null && certificado.getDocumento() != null) {
                        return certificado.getDocumento();
                    }
                case 2:
                    RegMovimiento movimiento = (RegMovimiento) em.findObjectByParameter(RegMovimiento.class, map);
                    if (movimiento != null && movimiento.getDocumento() != null) {
                        return movimiento.getDocumento();
                    }
            }
            return 0L;
        } catch (Exception e) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, e);
            return 0L;
        }
    }

    public String findByValidationCodeDetalle(String validationCode, Integer tipo) {
        Map map = new HashMap<String, Object>();
        map.put("codVerificacion", validationCode);
        try {

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String contenido;
            switch (tipo) {
                case 1:
                    RegCertificado certificado = (RegCertificado) em.findObjectByParameter(RegCertificado.class, map);
                    if (certificado != null && certificado.getDocumento() != null) {
                        contenido = "Certificado: " + certificado.getNumCertificado() + " Trámite: "
                                + certificado.getNumTramite() + " Fecha vencimiento: " + dateFormat.format(certificado.getFechaVencimiento());
                        return contenido;
                    }
                case 2:
                    RegMovimiento movimiento = (RegMovimiento) em.findObjectByParameter(RegMovimiento.class, map);
                    if (movimiento != null && movimiento.getDocumento() != null) {
                        contenido = "# Inscripción " + movimiento.getNumInscripcion() + " Fecha de inscripción: "
                                + movimiento.getFechaInscripcion() + " ";
                        return contenido;
                    }
            }
            return "Datos no encontrados";
        } catch (Exception e) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, e);
            return "Datos no encontrados";
        }
    }

    public String findByValidationCode2(String validationCode) {
        String ruta;
        Map map = new HashMap<String, Object>();
        map.put("codVerificacion", validationCode);
        try {
            RegCertificado certificado = (RegCertificado) em.findObjectByParameter(RegCertificado.class, map);
            if (certificado != null && certificado.getDocumento() != null) {
                ruta = this.pdfPath(validationCode);
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(certificado.getDocumento(), fos);
                    fos.close();
                }
                return ruta;
            } else {
                RegMovimiento movimiento = (RegMovimiento) em.findObjectByParameter(RegMovimiento.class, map);
                if (movimiento != null && movimiento.getDocumento() != null) {
                    ruta = this.pdfPath(validationCode);
                    File file = new File(ruta);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        ou.streamFile(movimiento.getDocumento(), fos);
                        fos.close();
                    }
                    return ruta;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(RegCertificadoService.class.getName()).log(Level.SEVERE, null, e);
            ruta = SisVars.rutaFirmaEC + Constantes.DOC_NO_ENCONTRADO_PDF;
            return ruta;
        }
        ruta = SisVars.rutaFirmaEC + Constantes.DOC_NO_ENCONTRADO_PDF;
        return ruta;
    }

    protected String pdfPath(String codigoVerificacion) {
        return SisVars.rutaFirmados + codigoVerificacion + ".pdf";
    }

}
