/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sql;

import com.origami.config.SisVars;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anyelo
 */
public class ConexionActiviti {

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionActiviti.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties props = new Properties();
        props.setProperty("user", SisVars.actuserName);
        props.setProperty("password", SisVars.actpassword);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(SisVars.acturl, props);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ConexionActiviti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

}
