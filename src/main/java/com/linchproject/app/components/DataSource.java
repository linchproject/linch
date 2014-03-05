package com.linchproject.app.components;

import com.linchproject.apps.App;
import com.linchproject.ioc.Component;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author Georg Schmidl
 */
public class DataSource implements javax.sql.DataSource, Component {

    private App app;
    private ComboPooledDataSource comboPooledDataSource;

    @Override
    public void init() {
        this.comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(app.get("jdbc.driver"));
            comboPooledDataSource.setJdbcUrl(app.get("jdbc.url"));
            comboPooledDataSource.setUser(app.get("jdbc.user"));
            comboPooledDataSource.setPassword(app.get("jdbc.password"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        comboPooledDataSource.close();
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return comboPooledDataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return comboPooledDataSource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return comboPooledDataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        comboPooledDataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        comboPooledDataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return comboPooledDataSource.getLoginTimeout();
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
