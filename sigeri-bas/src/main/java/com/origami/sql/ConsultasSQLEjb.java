/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sql;

import com.origami.sgr.services.ejbs.HibernateEjbInterceptor;
import com.origami.sgr.services.interfaces.DatabaseLocal;
import com.origami.sgr.util.Querys;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

/**
 *
 * @author eduar
 */
@Singleton(name = "consultasSQL")
@Interceptors(value = {HibernateEjbInterceptor.class})
@Lock(LockType.READ)
public class ConsultasSQLEjb implements ConsultasSQLService {

    @EJB(beanName = "dataSource")
    private DatabaseLocal ds;

    @Override
    public Integer getSecuenciaRepertorio(Integer anio) throws SQLException {
        try (Connection con = ds.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(Querys.getSecuenciaRepertorio)) {
                ps.setInt(1, anio);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    con.commit();
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
        return 0;
    }

    @Override
    public String getcontentTypeDoc(Long oid) {
        try (Connection con = ds.getDocs().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(Querys.findContentTypeDoc)) {
                ps.setLong(1, oid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    con.commit();
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
