package com.linchproject.linch.controllers.admin;

import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.models.User;
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

    public Result view(Params params) {
        User user = userDao.findByUsername(params.get("username"));
        return render("admin/users/view", context()
                .put("navUsers", true)
                .put("theUser", user));
    }

    public Result edit(Params params) {
        Result result;
        User user = userDao.findByUsername(params.get("username"));

        if (params.get("submit") != null) {
            user.setFirstName(params.get("firstName"));
            user.setLastName(params.get("lastName"));
            if (params.get("email") != null) {
                user.setEmail(params.get("email"));
            }
            userDao.save(user);
            result = redirect("view?username=" + user.getUsername());

        } else {
            result = render("admin/users/edit", context()
                    .put("navUsers", true)
                    .put("theUser", user));
        }
        return result;
    }
}
