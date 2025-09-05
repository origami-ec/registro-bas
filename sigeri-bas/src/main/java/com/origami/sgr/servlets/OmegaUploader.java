/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.origami.config.SisVars;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 *
 * @author Fernando
 */
@Named
@ApplicationScoped
public class OmegaUploader {

    public Connection getDocumentalConnection() {
        try {
            Class.forName(SisVars.docDriverClass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties props = new Properties();
        props.setProperty("user", SisVars.docUserName);
        props.setProperty("password", SisVars.docPassword);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(SisVars.docUrl, props);
        } catch (SQLException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, "ERROR CONNECTION DOCUMENT", ex);
        }
        return conn;
    }

    public Boolean streamFile(Long oid, OutputStream out) {
        Boolean flag = false;
        LargeObjectManager lobj;
        LargeObject obj = null;
        Connection conn = null;
        try {
            conn = this.getDocumentalConnection();
            // All LargeObject API calls must be within a transaction block
            if (conn == null) {
                return false;
            }
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            try {
                obj = lobj.open(oid, LargeObjectManager.READ);
            } catch (SQLException e) {
                System.out.println(e);
                flag = false;
            }
            if (obj != null) {
                flag = true;
            } else {
                return false;
            }
            byte buf[] = new byte[2048];
            int s;
            while ((s = obj.read(buf, 0, 2048)) > 0) {
                out.write(buf, 0, s);
                out.flush();
            }
            obj.close();
            conn.commit();
        } catch (SQLException | IOException ex) {
            flag = false;
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                flag = false;
                Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return flag;
    }

    public Long uploadFile(InputStream stream, String nombre, String contentType) {
        Connection conn = null;
        Long oid = null;
        try {
            conn = this.getDocumentalConnection();
            // All LargeObject API calls must be within a transaction block
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            LargeObjectManager lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            // Create a new large object
            oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            // Copy the data from the file to the large object
            // Open the large object for writing
            try (LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE)) {
                // Copy the data from the file to the large object
                byte buf[] = new byte[2048];
                int s;
                while ((s = stream.read(buf, 0, 2048)) > 0) {
                    obj.write(buf, 0, s);
                }
                // Close the large object
            }

            // Now insert the row into table
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO archivos.doc_file VALUES (?, ?, ?, ?, ?)")) {
                ps.setLong(1, oid);
                ps.setString(2, nombre);
                ps.setBoolean(3, false);
                ps.setTimestamp(4, new Timestamp((new Date()).getTime()));
                ps.setString(5, contentType);
                ps.executeUpdate();
            }
            stream.close();

            // Finally, commit the transaction.
            conn.commit();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return oid;
    }

    public Long upFileDocument(InputStream stream, String nombre, String contentType) {
        Connection conn = null;
        Long oid = null;
        try {
            conn = this.getDocumentalConnection();
            conn.setAutoCommit(false);
            LargeObjectManager lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            try (LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE)) {
                byte buf[] = new byte[2048];
                int s;
                while ((s = stream.read(buf, 0, 2048)) > 0) {
                    obj.write(buf, 0, s);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO archivos.doc_file VALUES (?, ?, ?, ?, ?)")) {
                ps.setLong(1, oid);
                ps.setString(2, nombre);
                ps.setBoolean(3, false);
                ps.setTimestamp(4, new Timestamp((new Date()).getTime()));
                ps.setString(5, contentType);
                ps.executeUpdate();
            }
            stream.close();
            conn.commit();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return oid;
    }

    public InputStream streamFile(Long oid) {
        LargeObjectManager lobj;
        LargeObject obj;
        InputStream is = null;
        Connection conn = null;
        try {
            conn = this.getDocumentalConnection();
            // All LargeObject API calls must be within a transaction block
            if (conn == null) {
                return null;
            }
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            try {
                obj = lobj.open(oid, LargeObjectManager.READ);
            } catch (SQLException e) {
                System.out.println(e);
                conn.close();
                return null;
            }
            byte buf[] = obj.read(obj.size());
            InputStream stream = new ByteArrayInputStream(buf);
            is = stream;
            obj.close();
        } catch (SQLException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return is;
    }

    public Long upFileDocEnable(InputStream stream, String nombre, String contentType) {
        Connection conn = null;
        Long oid = null;
        try {
            conn = this.getDocumentalConnection();
            conn.setAutoCommit(false);
            LargeObjectManager lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            try (LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE)) {
                byte buf[] = new byte[2048];
                int s;
                while ((s = stream.read(buf, 0, 2048)) > 0) {
                    obj.write(buf, 0, s);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO archivos.doc_file VALUES (?, ?, ?, ?, ?)")) {
                ps.setLong(1, oid);
                ps.setString(2, nombre);
                ps.setBoolean(3, false);
                ps.setTimestamp(4, new Timestamp((new Date()).getTime()));
                ps.setString(5, contentType);
                ps.executeUpdate();
            }
            stream.close();
            conn.commit();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return oid;
    }

    public InputStream streamFile(Long oid, int type) {
        LargeObjectManager lobj;
        LargeObject obj;
        InputStream is = null;
        Connection conn = null;
        try {
            conn = this.getDocumentalConnection();
            // All LargeObject API calls must be within a transaction block
            if (conn == null) {
                return null;
            }
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            try {
                obj = lobj.open(oid, LargeObjectManager.READ);
            } catch (SQLException e) {
                System.out.println(e);
                conn.close();
                return null;
            }
            byte buf[] = obj.read(obj.size());
            InputStream stream = new ByteArrayInputStream(buf);
            is = stream;
            obj.close();
        } catch (SQLException ex) {
            Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OmegaUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return is;
    }

}
