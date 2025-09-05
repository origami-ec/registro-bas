/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/**
 *
 * @author Fernando
 */
public class JsfUti {

    public static String getRealPath(String subpath) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getRealPath(subpath);
    }

    public static Object getSessionBean(String sesionName) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get(sesionName);
    }

    public static Object setSessionBean(String sesionName, Object obj) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put(sesionName, obj);
    }

    public static Object removeSessionBean(String sesionName) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .remove(sesionName);
    }

    public static void messageInfo(String id, String main, String desc) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, main, desc));
    }

    public static Object getSpringBean(String beanName) {
        ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        return ctx.getBean(beanName);
    }

    public static void messageWarning(String id, String main, String desc) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_WARN, main, desc));
    }

    public static void messageError(String id, String main, String desc) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, main, desc));
    }

    public static void messageFatal(String id, String main, String desc) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_FATAL, main, desc));
    }

    public static void executeJS(String js) {
        PrimeFaces.current().executeScript(js);
    }

    public static void update(String target) {
        PrimeFaces.current().ajax().update(target);
    }

    public static void update(Collection<String> targets) {
        PrimeFaces.current().ajax().update(targets);
    }

    public static Boolean isAjaxRequest() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.isPostback();
    }

    public static void redirect(String url) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(JsfUti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void redirectNewTab(String url) {
        PrimeFaces.current().executeScript("window.open('" + url + "', '_blank');");
    }

    public static void redirectMultiple(String url, String url2, String url3, String url4) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        PrimeFaces.current().executeScript("window.location='" + ec.getRequestContextPath() + url
                + "';window.open('" + url2 + "', '_blank');window.open('"
                + url3 + "', '_blank');window.open('" + url4 + "', '_blank');");
    }

    /**
     * *
     * Usar asi:
     * FerFaces.redirectMultipleConIP_V2(urlMismaVentana,urlVentanasEmergentes);
     *
     * @param urlMismaVentana con su ip ipServidorAndPuerto
     * @param urlVentanasEmergentes LIST URL con su ip ipServidorAndPuerto
     */
    public static void redirectMultipleConIP_V2(String urlMismaVentana, List<String> urlVentanasEmergentes) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String urlCompleta = new String();
        if (urlMismaVentana != null) {
            if (urlVentanasEmergentes != null && !urlVentanasEmergentes.isEmpty()) {
                urlCompleta = "window.location='" + ec.getRequestContextPath() + urlMismaVentana + "';";
                for (String url : urlVentanasEmergentes) {
                    urlCompleta = urlCompleta + "window.open('" + url + "', '_blank');";
                }
                PrimeFaces.current().executeScript(urlCompleta);
            } else {
                try {
                    ec.redirect(urlMismaVentana);
                } catch (IOException ex) {
                    Logger.getLogger(JsfUti.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if (urlVentanasEmergentes != null && !urlVentanasEmergentes.isEmpty()) {
                for (String url : urlVentanasEmergentes) {
                    urlCompleta = urlCompleta + "window.open('" + url + "', '_blank');";
                }
                PrimeFaces.current().executeScript(urlCompleta);
            }
        }
    }

    /**
     * *
     * Usar asi: FerFaces.redirectFaces("/faces/admin/acl/usuarioEdit.xhtml?id="
     * + usuario.getId() ); dependiendo del uso, reemplazar el
     * requestcontextpath con el sisVar del contexto
     *
     * @param url Object
     */
    public static void redirectFaces(String url) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + url);
        } catch (IOException ex) {
            Logger.getLogger(JsfUti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void redirectFaces2(String url) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(JsfUti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *
     * usar asi: FerFaces.redirectFaces("/faces/admin/acl/usuarioEdit.xhtml?id="
     * + usuario.getId() ); dependiendo del uso, reemplazar el
     * requestcontextpath con el sisVar del contexto
     *
     * @param url Object
     */
    public static void redirectFacesNewTab(String url) {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            PrimeFaces.current().executeScript("window.open('" + ec.getRequestContextPath() + url + "', '_newtab');");
        } catch (Exception e) {
            Logger.getLogger(JsfUti.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void redirectFacesNewTab2(String url) {
        PrimeFaces.current().executeScript("window.open('" + url + "', '_newtab');");
    }

    public static String getHostContextUrl() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        return url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
    }

    /**
     *
     * @param urlFacelet
     */
    public static void openDialogFrame(String urlFacelet) {
        if (!urlFacelet.startsWith("/")) {
            urlFacelet = "/" + urlFacelet;
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "75%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public static String getCurrentPage() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uri = req.getRequestURI();
        return uri;
    }

    public static void redirectCurrentPage() {
        redirect(getCurrentPage());
    }

    public static UIComponent findComponent(final String id) {
        for (UIComponent component : FacesContext.getCurrentInstance().getViewRoot().getChildren()) {
            if (component.getId().contains(id)) {
                return component;
            }
        }
        return null;
    }

}
