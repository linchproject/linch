package com.linchproject.app.controllers;

import com.linchproject.app.dao.UserDao;
import com.linchproject.app.models.User;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.mvc.Controller;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public class LoginController extends Controller {

    protected UserDao userDao;
    protected PasswordEncryptor passwordEncryptor;

    public Result index(Params params) {
        if (params.getValue("submit") != null) {
            String username = params.getValue("username");

            User user = userDao.findByUsername(username);
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

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
