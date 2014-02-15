package com.linchproject.app.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class AuthController extends Controller {

    public Result login(Params params) {
        // everybody is a user
        route.setUserId(params.getValue("username"));
        route.setPath(params.getValue("next"));
        return redirect(route);
    }

    public Result logout(Params params) {
        route.setUserId(null);
        route.setPath(params.getValue("next"));
        return redirect(route);
    }
}
