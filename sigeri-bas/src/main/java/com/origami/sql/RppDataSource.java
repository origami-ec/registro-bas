package com.origami.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.origami.sgr.exception.SqlRuntimeException;
import com.origami.sgr.util.HiberUtil;

@ApplicationScoped
public class RppDataSource {
	
	public Connection getConnection(){
		try {
        	Session sess = HiberUtil.getSession();
    		SessionImplementor sessImpl = (SessionImplementor) sess;
			Connection conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
			return conn;
		} catch (SQLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "", e);
			throw new SqlRuntimeException(e);
		}
	}
	
	
	
}
