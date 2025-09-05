/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.session;

import com.origami.documental.models.UsuarioDocs;
import com.origami.sgr.entities.AclLogin;
import com.origami.sgr.services.interfaces.Entitymanager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import jodd.util.StringUtil;

/**
 *
 * @author Origami
 */
@Named
@SessionScoped
public class UserSession implements WfUserSession {

    private static final Logger LOG = Logger.getLogger(UserSession.class.getName());

    @EJB(beanName = "manager")
    private Entitymanager em;

    protected String name_user;
    protected String actKey;
    protected String taskID;
    protected Long userId = (long) -1;
    protected String name = "invitado";
    protected String urlSolicitada;
    protected Boolean temp = false;
    protected String departamento;
    protected String nombrePersonaLogeada;
    protected Boolean esDirector;
    protected String varTemp;
    protected String ipClient;
    protected String topPage;
    private String titlePage = "OrigamiGT";
    protected String perfil = "/resources/image/avatar.jpg";
    protected Boolean esSuperUser;
    protected List<Long> roles = new ArrayList<>();
    protected List<Long> depts = new ArrayList<>();
    private AclLogin aclLogin = new AclLogin();
    private Long idAclLogin;
    private Pattern macpt = null;
    
    protected String token;
    protected String macClient;
    protected String osClient;
    protected UsuarioDocs usuarioDocs;

    public String getNombreBienvenida() {
        if (this.esLogueado()) {
            return this.getNombrePersonaLogeada();
        }
        return "invitado";
    }

    public Boolean esLogueado() {
        return userId != null && userId > 0L;
    }

    public void redirectUrlSolicitada() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (this.getUrlSolicitada() != null || (this.getUrlSolicitada() != null && this.getUrlSolicitada().equals(ec.getRequestContextPath() + "/faces/login.xhtml"))) {
            try {
                HttpSession session = (HttpSession) ec.getSession(false);
                session.setAttribute("usuario", this.getName_user());
                ec.redirect(this.getUrlSolicitada());
                this.setUrlSolicitada(null);
                this.getDatosEquipo();
            } catch (IOException ex) {
                Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ec.redirect(ec.getRequestContextPath() + "/");
                this.getDatosEquipo();
            } catch (IOException ex) {
                Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void redirectInvitado() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String fullURI = servletRequest.getRequestURI();
        if (fullURI.equalsIgnoreCase(servletRequest.getContextPath() + "/faces/admin/users/recuperarClave.xhtml")
                || fullURI.equalsIgnoreCase(servletRequest.getContextPath() + "/faces/admin/users/cambioClave.xhtml")) {
            return;
        }
        if (temp) {
            //System.out.println(" done!!! ");
            return;
        }

        this.persistReqUrl();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void persistReqUrl() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String fullURI = servletRequest.getRequestURI();
        if (StringUtil.isNotEmpty(servletRequest.getQueryString())) {
            fullURI = fullURI + "?" + servletRequest.getQueryString();
        }
        this.getDatosEquipo();
        this.setUrlSolicitada(fullURI);
    }

    public void logout2() {
        if (aclLogin != null && aclLogin.getId() != null) {
            aclLogin.setFechaDoLogout(new Date());
            em.merge(aclLogin);
            aclLogin = null;
        }
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        this.setTemp(false);
        this.setUrlSolicitada(null);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDatosEquipo() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletRequest sr = (HttpServletRequest) ctx.getExternalContext().getRequest();
            sr.getHeader("X-FORWARDED-FOR");
            this.setIpClient(sr.getRemoteAddr());
            return sr.getRemoteAddr();
        } catch (Exception e) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, e.getMessage());
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String getMACAddressEquipo() {
        // Find OS and set command according to OS
        String OS = System.getProperty("os.name").toLowerCase();
        String[] cmd;
        if (OS.contains("win")) {
            // Windows
            macpt = Pattern.compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
            String[] a = {"arp", "-a", this.getIpClient()};
            cmd = a;
        } else {
            // Mac OS X, Linux
            macpt = Pattern.compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
            String[] a = {"arp", this.getIpClient()};
            cmd = a;
        }
        try {
            // Run command
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            // read output with BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            // Loop trough lines
//            System.out.println("Line " + line);
            while (line != null) {
                Matcher m = macpt.matcher(line);
                // when Matcher finds a Line then return it as result
                if (m.find()) {
                    //System.out.println("MAC: " + m.group(0));
                    return m.group(0);
                }
                line = reader.readLine();
            }
            if (line != null) {
                return line.toUpperCase();
            }
        } catch (IOException | InterruptedException e1) {
            LOG.log(Level.SEVERE, "", e1);
        }

        // Return empty string if no MAC is found
        return "";
    }

    @Override
    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getActKey() {
        return actKey;
    }

    public void setActKey(String actKey) {
        this.actKey = actKey;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrlSolicitada() {
        return urlSolicitada;
    }

    public void setUrlSolicitada(String urlSolicitada) {
        this.urlSolicitada = urlSolicitada;
    }

    @Override
    public Boolean getTemp() {
        return temp;
    }

    public void setTemp(Boolean temp) {
        this.temp = temp;
    }

    @Override
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String getNombrePersonaLogeada() {
        if (nombrePersonaLogeada == null || nombrePersonaLogeada.isEmpty()) {
            nombrePersonaLogeada = "Admin";
        }
        return nombrePersonaLogeada;
    }

    public void setNombrePersonaLogeada(String nombrePersonaLogeada) {
        this.nombrePersonaLogeada = nombrePersonaLogeada;
    }

    @Override
    public Boolean getEsDirector() {
        return esDirector;
    }

    public void setEsDirector(Boolean esDirector) {
        this.esDirector = esDirector;
    }

    public String getVarTemp() {
        return varTemp;
    }

    public void setVarTemp(String varTemp) {
        this.varTemp = varTemp;
    }

    @Override
    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Boolean getEsSuperUser() {
        return esSuperUser;
    }

    public void setEsSuperUser(Boolean esSuperUser) {
        this.esSuperUser = esSuperUser;
    }

    @Override
    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    @Override
    public List<Long> getDepts() {
        return depts;
    }

    public void setDepts(List<Long> depts) {
        this.depts = depts;
    }

    public AclLogin getAclLogin() {
        return aclLogin;
    }

    public void setAclLogin(AclLogin aclLogin) {
        this.aclLogin = aclLogin;
    }

    public Long getIdAclLogin() {
        return idAclLogin;
    }

    public void setIdAclLogin(Long idAclLogin) {
        this.idAclLogin = idAclLogin;
    }

    public String getTopPage() {
        return topPage;
    }

    public void setTopPage(String topPage) {
        this.topPage = topPage;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMacClient() {
        return macClient;
    }

    public void setMacClient(String macClient) {
        this.macClient = macClient;
    }

    public String getOsClient() {
        return osClient;
    }

    public void setOsClient(String osClient) {
        this.osClient = osClient;
    }

    public UsuarioDocs getUsuarioDocs() {
        return new UsuarioDocs(userId, nombrePersonaLogeada, name_user);
    }
    
}
