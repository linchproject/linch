package com.linchproject.app.controllers.admin;

import com.linchproject.app.AdministratorController;
import com.linchproject.app.dao.UserDao;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class UsersController extends AdministratorController {

    protected UserDao userDao;

    public Result index(Params params) {
        return render("admin/users/index", context()
                .put("nav", context().put("users", true))
                .put("users", userDao.findAll()));
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
