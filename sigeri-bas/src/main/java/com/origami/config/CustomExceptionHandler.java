/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.config;

import com.origami.sgr.util.JsfUti;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author ANGEL NAVARRO
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private static final Logger LOG = Logger.getLogger(CustomExceptionHandler.class.getName());

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable throwable = context.getException();

            FacesContext fc = FacesContext.getCurrentInstance();

            try {
                ExternalContext ext = fc.getExternalContext();
                Map<String, String> requestHeader = ext.getRequestHeaderMap();
                String urlRefered = requestHeader.get("referer");
                if (Objects.nonNull(urlRefered)) {
                    if (urlRefered.contains(".xhtml")) {
                        LOG.log(Level.WARNING, throwable.getMessage() + " - " + context.getComponent(), throwable);
                        JsfUti.redirect(urlRefered);
                    } else {
                        LOG.log(Level.SEVERE, throwable.getMessage() + " - " + context.getComponent(), throwable);
                    }
                } else {
                    LOG.log(Level.SEVERE, throwable.getMessage() + " - " + context.getComponent(), throwable);
                }
            } finally {
                iterator.remove();
            }
        }

        // Let the parent handle the rest
        getWrapped().handle();
    }
}
