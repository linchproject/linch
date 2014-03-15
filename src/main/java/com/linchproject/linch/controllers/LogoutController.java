package com.linchproject.linch.controllers;

import com.linchproject.linch.Controller;
import com.linchproject.linch.Settings;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class LogoutController extends Controller {

    public Result index(Params params) {
        sessionService.setValue(Settings.SESSION_USER_KEY, null);
        cookieService.removeCookie("linch");
        return redirect("/");
    }
}
