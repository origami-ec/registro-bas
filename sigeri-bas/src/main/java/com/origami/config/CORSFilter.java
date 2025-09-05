/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.config;

import com.origami.session.UserSession;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ANGEL NAVARRO
 */
@WebFilter(filterName = "CORSFilter", urlPatterns = {"*.xhtml"})
public class CORSFilter implements Filter {

    @Inject
    private UserSession session;

    /**
     * Default constructor.
     */
    public CORSFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // Authorize (allow) all domains to consume the content
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, UPDATE, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        //  allow user to proccede if url is login.xhtml or user logged in or user is accessing any page in //public folder
        String reqURI = request.getRequestURI();
//        System.out.println("uri " + reqURI);
        if (reqURI.contains("login.xhtml") || (session.getName_user() != null) || reqURI.contains("javax.faces.resource") || reqURI.contains("resource")/*) {*/) {
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException iOException) {
                response.sendRedirect(request.getContextPath() + "/faces/login.xhtml");
            }
        } else {// user didn't log in but asking for a page that is not allowed so take user to login page
            if (reqURI.equals("/procesos/dashBoard.xhtml")) {
                chain.doFilter(request, response);
            } else if (reqURI.contains("/rest/")) {
                chain.doFilter(request, response);
            } else if (reqURI.contains("admin/manage/recuperarClave.xhtml")) {
//                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/faces/login.xhtml");  // Anonymous user. Redirect to login page
            }

        }
//
//        // pass the request along the filter chain
//        chain.doFilter(request, servletResponse);
    }

    /**
     * @param fConfig
     * @throws javax.servlet.ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
