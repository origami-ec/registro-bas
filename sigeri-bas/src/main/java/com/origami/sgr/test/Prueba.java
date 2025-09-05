/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.test;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.TarUsuarioTareas;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.NroTomoEnum;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import com.origami.sql.ConsultasSQLService;
import java.util.Arrays;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Prueba implements Serializable {

    protected RegRegistrador registrador;
    protected AclUser user;

    protected Long tipo;
    protected Boolean persona = true;
    protected String cedula = "";
    protected String nombre = "";
    protected String razonsocial = "";
    protected String visorUrl;
    protected String contenido;

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;

    @Inject
    private Entitymanager em;

    @Inject
    private SeqGenMan sec;

    @Inject
    private AsynchronousService as;

    @Inject
    private ConsultasSQLService sam;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.iniView();
        }
    }

    protected void iniView() {
        try {
            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            user = em.find(AclUser.class, us.getUserId());
            this.imprimirFichaRegistral();
            //visorUrl = "/rpp/pdfjs/web/visor.html?file=http://localhost:8081/rpp/DownLoadFiles";
            visorUrl = "/rpp/pdfjs/web/viewer.html?file=http://localhost:8081/rpp/Documento";
            //visorUrl = "/rpp/pdfjs/web/viewer.html?file=" + URLEncoder.encode("C:\\servers_files\\rpm\\certificado.pdf", "UTF-8");
            //visorUrl = "/rpp/pdfjs/web/visor.html?file=/rpp/pdfjs/web/compressed.tracemonkey-pldi-09.pdf";
            //visorUrl = "/rpp/pdfjs/web/visor.html?file=http://localhost:8081/rpp/pdfjs/web/compressed.tracemonkey-pldi-09.pdf";
            //System.out.println(visorUrl);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void probando() {
        System.out.println("// cedula: " + cedula);
        System.out.println("// nombre: " + nombre);
        System.out.println("// razonsocial: " + razonsocial);
    }

    public void imprimirFichaRegistral() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("CertificadoHistoria");
            ss.agregarParametro("ID_CERTIFICADO", 43283L);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
            ss.agregarParametro("USER_NAME", us.getName_user());
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getTituloNombreCompleto().trim().toUpperCase());
            }
            //ss.setNombreDocumento("C:\\servers_files\\rpm\\certificado.pdf");
            //ss.setEncuadernacion(Boolean.TRUE);
            //JsfUti.redirectNewTab("/rpp/Documento");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generarReporte() {
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        ss.setNombreReporte("anexoUafINT");
        ss.setNombreSubCarpeta("ingreso");
        ss.agregarParametro("PERIODO", "02-2018");
        JsfUti.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
    }

    public void testingCharge() {
        try {
            TarUsuarioTareas ut = sec.getUserForTask(8L, 1);
            System.out.println("//");
            System.out.println("// usuario: " + ut.getUsuario().getUsuario());
            System.out.println("// fecha: " + ut.getFecha());
            System.out.println("// dias:" + ut.getDias());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void testsendmail(String mail, Long tipo) {
        try {
            System.out.println("// " + mail + " ----- " + tipo);
            as.testSendEmail(mail, tipo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void convertirPDF(String nombre) {
        try {
            //Utils.convertPDFtoTIFF(SisVars.rutaTemporales + nombre + ".pdf", SisVars.rutaTemporales + nombre + ".tiff");
            JsfUti.messageWarning(null, "", "revisar archivo :" + nombre);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarDetalles(String fecha) {
        try {
            /*List<SamLiquidacionDetalles> detalles = sam.verificarPagosDetalles(fecha);
            System.out.println("// cantidad: " + detalles.size());
            for (SamLiquidacionDetalles sld : detalles) {
                System.out.println(sld);
            }*/
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void probarEnum(Integer parametro) {
        try {
            System.out.println("// 2enum: " + NroTomoEnum.fromUiParam(parametro));
            System.out.println("// 3enum: " + Arrays.toString(NroTomoEnum.values()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Boolean getPersona() {
        return persona;
    }

    public void setPersona(Boolean persona) {
        this.persona = persona;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getVisorUrl() {
        return visorUrl;
    }

    public void setVisorUrl(String visorUrl) {
        this.visorUrl = visorUrl;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}
