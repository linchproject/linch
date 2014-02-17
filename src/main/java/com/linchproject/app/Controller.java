package com.linchproject.app;

import com.linchproject.core.Result;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.mvc.Controller {

    private Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            Sql2o sql2o = new Sql2o(dataSource);
            connection = sql2o.beginTransaction();
        }
        return connection;
    }

    @Override
    protected Result exit(Result result) {
        if (connection != null) {
            connection.commit();
        }
        return super.exit(result);
    }
}
