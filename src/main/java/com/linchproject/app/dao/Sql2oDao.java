package com.linchproject.app.dao;

import com.linchproject.mvc.ConnectionService;
import org.sql2o.Query;

/**
 * @author Georg Schmidl
 */
public class Sql2oDao {

    protected ConnectionService connectionService;

    protected Query query(String sql) {
        return connectionService.createQuery(sql);
    }

    protected Query query(String sql, boolean returnGeneratedKeys) {
        return connectionService.createQuery(sql, returnGeneratedKeys);
    }

    public void setConnectionService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }
}
