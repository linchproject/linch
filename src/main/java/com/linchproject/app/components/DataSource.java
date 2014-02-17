package com.linchproject.app.components;

import com.linchproject.ioc.Component;
import com.linchproject.servlet.AppConfig;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author Georg Schmidl
 */
public class DataSource implements javax.sql.DataSource, Component {

    private javax.sql.DataSource dataSource;

    private AppConfig appConfig;

    @Override
    public void init() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(appConfig.get("jdbc.driver"));
        basicDataSource.setUrl(appConfig.get("jdbc.url"));
        basicDataSource.setUsername(appConfig.get("jdbc.user"));
        basicDataSource.setPassword(appConfig.get("jdbc.password"));
        this.dataSource = basicDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
}
