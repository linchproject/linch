package com.linchproject.app.components;

import com.linchproject.apps.App;
import com.linchproject.ioc.Component;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author Georg Schmidl
 */
public class DataSource extends BasicDataSource implements Component {

    private App app;

    @Override
    public void init() {
        setDriverClassName(app.get("jdbc.driver"));
        setUrl(app.get("jdbc.url"));
        setUsername(app.get("jdbc.user"));
        setPassword(app.get("jdbc.password"));
    }

    public void setApp(App app) {
        this.app = app;
    }
}
