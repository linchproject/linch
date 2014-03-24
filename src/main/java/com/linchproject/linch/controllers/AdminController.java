package com.linchproject.linch.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.Route;

/**
 * @author Georg Schmidl
 */
public class AdminController extends Controller {

    public Result _(Params params) {
        Route route = this.route.copy();
        route.addSubPackage("admin");
        route.shift();
        return dispatch(route);
    }
}
