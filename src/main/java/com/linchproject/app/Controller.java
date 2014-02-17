package com.linchproject.app;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.mvc.Controller {

    protected Connection connection;

    @Override
    public void init() {
        if (dataSource != null) {
            Sql2o sql2o = new Sql2o(dataSource);
            connection = sql2o.beginTransaction();
        }
        super.init();
    }

    @Override
    public void onError() {
        if (connection != null) {
            connection.rollback();
        }
        super.onError();
    }

    @Override
    public void onSuccess() {
        if (connection != null) {
            connection.commit();
        }
        super.onSuccess();
    }
}
