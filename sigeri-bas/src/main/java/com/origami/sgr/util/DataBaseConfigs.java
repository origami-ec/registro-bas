package com.origami.sgr.util;

import com.origami.archivosHist.BaseNameHistory;
import com.origami.config.SisVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author origami
 */
public class DataBaseConfigs {

    private String path;
    private BasicDataSource ds = null;
    private DataBaseParameters dbp = null;

    public DataBaseConfigs(String path) {
        this.path = path;
    }

    public DataBaseConfigs() {

    }

    private DataBaseParameters getParameters(int tcon) {
        DataBaseParameters p;
        try {
            p = new DataBaseParameters();
            switch (tcon) {
                case 1:
                    p.setDriverClass(SisVars.driverClass);
                    p.setUserName(SisVars.userName);
                    p.setPassword(SisVars.password);
                    p.setUrl(SisVars.url);
                    p.setMinPoolSize(SisVars.minPoolSize);
                    p.setMaxPoolSize(SisVars.maxPoolSize);
                    break;
                case 2:
                    p.setDriverClass(SisVars.docDriverClass);
                    p.setUserName(SisVars.docUserName);
                    p.setPassword(SisVars.docPassword);
                    p.setUrl(SisVars.docUrl);
                    p.setMinPoolSize(SisVars.docMinPoolSize);
                    p.setMaxPoolSize(SisVars.docMaxPoolSize);
                    break;
                default:
                    BaseNameHistory temp = BaseNameHistory.DEFAULT.byCode(tcon);
                    if (temp != null) {
                        p.setDriverClass(SisVars.docDriverClass);
                        p.setUserName(temp.getUser());
                        p.setPassword(temp.getPass());
                        p.setUrl(temp.getUrl());
                        p.setMinPoolSize(SisVars.minPoolSize);
                        p.setMaxPoolSize(SisVars.maxPoolSize);
                    }
                    break;
            }
        } catch (Exception e) {
            p = null;
            Logger.getLogger(DataBaseConfigs.class.getName()).log(Level.SEVERE, null, e);
        }
        return p;
    }

    public DataSource getDataSource(int p) {
        try {
            dbp = this.getParameters(p);
            if (dbp != null) {
                ds = new BasicDataSource();
                ds.setUrl(dbp.getUrl());
                ds.setDriverClassName(dbp.getDriverClass());
                ds.setUsername(dbp.getUserName());
                ds.setPassword(dbp.getPassword());
                ds.setMaxIdle(dbp.getMaxIdleTime());
                ds.setMaxActive(dbp.getMaxPoolSize());
                ds.setMinIdle(dbp.getMinPoolSize());
                ds.setDefaultAutoCommit(false);
            }
        } catch (Exception e) {
            Logger.getLogger(DataBaseConfigs.class.getName()).log(Level.SEVERE, null, e);
        }
        return ds;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
