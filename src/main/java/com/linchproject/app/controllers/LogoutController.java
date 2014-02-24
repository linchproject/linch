package com.linchproject.app.controllers;

import com.linchproject.app.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class LogoutController extends Controller {

    public Result index(Params params) {
        sessionService.setUserId(null);
        cookieService.removeCookie("linch");
        return redirect("/");
    }
}
