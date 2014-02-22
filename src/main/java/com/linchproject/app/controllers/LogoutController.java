package com.linchproject.app.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.mvc.Controller;

/**
 * @author Georg Schmidl
 */
public class LogoutController extends Controller {

    public Result index(Params params) {
        route.setUserId(null);
        return redirect(params.getValue("next"));
    }
}
