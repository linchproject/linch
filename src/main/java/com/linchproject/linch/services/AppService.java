package com.linchproject.linch.services;

import com.linchproject.linch.App;

import java.util.Collection;

/**
 * @author Georg Schmidl
 */
public interface AppService {

    Collection<App> getApps();

    App getApp(String path);
}
