package com.linchproject.app.controllers.admin;

import com.linchproject.app.AdministratorController;
import com.linchproject.app.models.User;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class UsersController extends AdministratorController {

    public Result index(Params params) {
        return render("admin/users/index", context()
                .put("nav", context().put("users", true))
                .put("users", fetchAll()));
    }

    protected List<User> fetchAll() {
        return query("select id, username, password, full_name, email from user")
                .executeAndFetch(User.class);
    }
}
