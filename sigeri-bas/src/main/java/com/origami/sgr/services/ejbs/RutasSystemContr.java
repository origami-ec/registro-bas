package com.origami.sgr.services.ejbs;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

@ApplicationScoped
public class RutasSystemContr {

    @Inject
    private ServletContext ctx;

    public String getRootPath() {
        return ctx.getRealPath("/");
    }

    private Logger logger() {
        return Logger.getLogger(this.getClass().getName());
    }

}
