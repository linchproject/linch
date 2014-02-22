package com.linchproject.app.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class LogoutController extends AuthController {

    public Result index(Params params) {
        route.setUserId(null);
        route.setPath(params.getValue("next"));
        return redirect(route);
    }
}
