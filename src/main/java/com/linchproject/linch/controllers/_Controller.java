package com.linchproject.linch.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.Route;
import com.linchproject.linch.App;
import com.linchproject.linch.Controller;
import com.linchproject.linch.services.AppService;

/**
 * @author Georg Schmidl
 */
public class _Controller extends Controller {

    protected AppService appService;

    public Result _(Params params) {
        App app = appService.getApp("/" + route.getController());
        if (app != null) {
            Route route = this.route.copy();
            route.setControllerPackage(app.getAppPackage() + ".controllers");
            route.shift();
            return dispatch(route);
        }
        return dispatch(route);
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
