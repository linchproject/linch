package com.linchproject.linch.services;

import com.linchproject.linch.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Georg Schmidl
 */
public class ClassPathAppService implements AppService {

    private Map<String, App> apps;

    @Override
    public Collection<App> getApps() {
        return getAppsMap().values();
    }

    @Override
    public App getApp(String path) {
        return getAppsMap().get(path);
    }

    private Map<String, App> getAppsMap() {
        if (apps == null) {
            apps = new TreeMap<String, App>();

            URLClassLoader contextClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
            for (URL url : contextClassLoader.getURLs()) {
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});

                InputStream inputStream = urlClassLoader.getResourceAsStream("app.properties");
                if (inputStream != null) {
                    Properties properties = new Properties();
                    try {
                        properties.load(inputStream);
                        if (properties.getProperty("path") != null) {
                            App app = new App();
                            app.setName(properties.getProperty("name"));
                            app.setDescription(properties.getProperty("description"));
                            app.setPath(properties.getProperty("path"));
                            app.setAppPackage(properties.getProperty("package"));
                            apps.put(app.getPath(), app);
                        }
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }
        return apps;
    }
}
