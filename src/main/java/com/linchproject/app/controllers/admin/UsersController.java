package com.linchproject.app.controllers.admin;

import com.linchproject.app.AdministratorController;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class UsersController extends AdministratorController {

    public Result index(Params params) {
        return render("admin/users/index", context()
                .put("navUsers", true)
                .put("users", userDao.findAll()));
    }
}
