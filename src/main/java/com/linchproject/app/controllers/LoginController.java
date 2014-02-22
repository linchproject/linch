package com.linchproject.app.controllers;

import com.linchproject.app.models.User;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class LoginController extends AuthController {


    public Result _(Params params) {
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

            Map<String, Object> context = new HashMap<String, Object>();
            context.put("hideNavigationLogin", true);
            context.put("error", "Username or password incorrect");
            context.put("username", params.getValue("username"));
            context.put("next", params.getValue("next"));

            return render("login", context);
        } else {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("hideNavigationLogin", true);
            context.put("next", "/");

            return render("login", context);
        }
    }
}
