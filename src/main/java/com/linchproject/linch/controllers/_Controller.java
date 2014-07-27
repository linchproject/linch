package com.linchproject.linch.controllers;

import com.linchproject.core.Result;
import com.linchproject.core.Route;
import com.linchproject.core.actions._Action;
import com.linchproject.linch.App;
import com.linchproject.linch.Controller;
import com.linchproject.linch.services.AppService;

/**
 * @author Georg Schmidl
 */
public class _Controller extends Controller implements _Action {

    protected AppService appService;

    @Override
    public Result _Action() {
        App app = appService.getApp(route.getController());
        if (app != null) {
            Route route = this.route
                    .changeControllerPackage(app.getAppPackage() + ".controllers")
                    .shift();
            return dispatch(route);
        }
        return dispatch();
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
