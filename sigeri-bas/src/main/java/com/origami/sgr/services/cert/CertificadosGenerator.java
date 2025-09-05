package com.origami.sgr.services.cert;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.exception.SqlRuntimeException;
import com.origami.sgr.services.ejbs.HibernateEjbInterceptor;
import com.origami.sgr.services.ejbs.RegCertificadoService;
import com.origami.sgr.util.HiberUtil;
import com.origami.session.ServletSession;
import com.origami.sql.RppDataSource;

@ApplicationScoped
@Singleton @Lock(LockType.READ)
@Interceptors({HibernateEjbInterceptor.class})
public class CertificadosGenerator {
	
	@Inject
	private RppDataSource ds;
	@Inject
	private RegCertificadoService certServ;
	@Inject
    private ServletSession ss;
	
	public void streamCertificado(OutputStream os, Long idCertificado){
        RegCertificado certificado = certServ.find(idCertificado);
        
        if (certificado.getFechaEmision() != null && certificado.getObservacion() != null) {
        	
        }
        
        
	}
	
}
