package com.linchproject.app.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class AdminController extends Controller {

    public Result _all(Params params) {
        return redirect(route.shift("admin"));
    }

    @Override
    public boolean isPermitted() {
        return route.getUserId() != null && "admin".equals(route.getUserId());
    }
}
