package com.linchproject.app.controllers;

import com.linchproject.app.AuthController;
import com.linchproject.app.models.User;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class LoginController extends AuthController {

    public Result index(Params params) {
        if (params.getValue("submit") != null) {
            String username = params.getValue("username");

            User user = fetch(username);
            if (user != null) {
                String password = params.getValue("password");
                if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                    route.setUserId(params.getValue("username"));
                    return redirect(params.getValue("next"));
                }
            }
            return render("login", context()
                    .put("hideNavigationLogin", true)
                    .put("error", "Username or password incorrect")
                    .put("username", params.getValue("username"))
                    .put("next", params.getValue("next")));
        } else {
            return render("login", context()
                    .put("hideNavigationLogin", true)
                    .put("next", params.getValue("next") == null? "/": params.getValue("next")));
        }
    }
}
