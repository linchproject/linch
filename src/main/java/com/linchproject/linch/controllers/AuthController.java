package com.linchproject.linch.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.linch.Controller;
import com.linchproject.linch.Settings;
import com.linchproject.linch.entities.Remember;
import com.linchproject.linch.entities.User;
import org.jasypt.util.password.PasswordEncryptor;

import java.util.UUID;

/**
 * @author Georg Schmidl
 */
public class AuthController extends Controller {

    protected PasswordEncryptor passwordEncryptor;

    public Result login(Params params) {
        return render(context()
                .put("hideNavigationLogin", true)
                .put("next", params.get("next") != null? params.get("next"): "/"));
    }

    public Result doLogin(Params params) {
        String username = params.get("username");

        User user = userDao.findByUsername(username);
        if (user != null) {
            String password = params.get("password");
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                sessionService.setValue(Settings.SESSION_USER_KEY, params.get("username"));

                if ("true".equals(params.get("remember"))) {
                    String uuid = UUID.randomUUID().toString();
                    Remember remember = rememberDao.findByUserId(user.getId());
                    if (remember == null) {
                        remember = new Remember();
                        remember.setUuid(uuid);
                        remember.setUserId(user.getId());
                        rememberDao.save(remember);
                    }
                    cookieService.addCookie(Settings.COOKIE_NAME, remember.getUuid(), Settings.COOKIE_MAX_AGE);

                } else {
                    cookieService.removeCookie(Settings.COOKIE_NAME);
                }

                return redirect(params.get("next") != null? params.get("next"): "/");
            }
        }
        return render("login", context()
                .put("hideNavigationLogin", true)
                .put("error", true)
                .put("username", params.get("username"))
                .put("remember", "true".equals(params.get("remember")))
                .put("next", params.get("next") != null? params.get("next"): "/"));
    }

    public Result logout(Params params) {
        sessionService.setValue(Settings.SESSION_USER_KEY, null);
        cookieService.removeCookie("linch");
        return redirect("/");
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
}
