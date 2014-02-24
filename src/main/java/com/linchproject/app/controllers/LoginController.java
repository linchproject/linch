package com.linchproject.app.controllers;

import com.linchproject.app.Controller;
import com.linchproject.app.Settings;
import com.linchproject.app.models.Remember;
import com.linchproject.app.models.User;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import org.jasypt.util.password.PasswordEncryptor;

import java.util.UUID;

/**
 * @author Georg Schmidl
 */
public class LoginController extends Controller {

    protected PasswordEncryptor passwordEncryptor;

    public Result index(Params params) {
        if (params.getValue("submit") != null) {
            String username = params.getValue("username");

            User user = userDao.findByUsername(username);
            if (user != null) {
                String password = params.getValue("password");
                if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                    sessionService.setUserId(params.getValue("username"));

                    if ("true".equals(params.getValue("remember"))) {
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

                    return redirect(params.getValue("next"));
                }
            }
            return render("login", context()
                    .put("hideNavigationLogin", true)
                    .put("error", "Username or password incorrect")
                    .put("username", params.getValue("username"))
                    .put("remember", "true".equals(params.getValue("remember")))
                    .put("next", params.getValue("next")));
        } else {
            return render("login", context()
                    .put("hideNavigationLogin", true)
                    .put("next", params.getValue("next") == null? "/": params.getValue("next")));
        }
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

}
