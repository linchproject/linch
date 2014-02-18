package com.linchproject.app.components;

import com.linchproject.ioc.Component;
import com.linchproject.servlet.AppConfig;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author Georg Schmidl
 */
public class DataSource extends BasicDataSource implements Component{

    private AppConfig appConfig;

    @Override
    public void init() {
        setDriverClassName(appConfig.get("jdbc.driver"));
        setUrl(appConfig.get("jdbc.url"));
        setUsername(appConfig.get("jdbc.user"));
        setPassword(appConfig.get("jdbc.password"));
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
}
