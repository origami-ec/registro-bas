package com.origami.sgr.services.ejbs;

import com.origami.sgr.services.interfaces.DatabaseLocal;
import com.origami.sgr.util.DataBaseConfigs;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.sql.DataSource;

/**
 * @author origami
 */
@Singleton(name = "dataSource")
public class DataSourceEjb implements DatabaseLocal {

    private DataBaseConfigs dbc = null;
    private DataSource ds = null;
    private DataSource docs = null;

    @PostConstruct
    protected void init() {
        dbc = new DataBaseConfigs();
        ds = dbc.getDataSource(1);
        docs = dbc.getDataSource(2);
    }

    @Override
    public DataSource getDataSource() {
        return ds;
    }

    @Override
    public DataSource getDocs() {
        return docs;
    }

}
