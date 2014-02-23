package com.linchproject.app.controllers;

import com.linchproject.app.SecureController;
import com.linchproject.app.models.User;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public class ProfileController extends SecureController {

    protected PasswordEncryptor passwordEncryptor;

    public Result _(Params params) {
        route.setUserId("admin");
        return super._(params);
    }

    public Result index(Params params) {
        return render("profile/index", context().put("navIndex", true));
    }

    public Result edit(Params params) {
        Result result;
        if (params.getValue("submit") != null) {
            User user = getUser();

            if (params.getValue("firstName") != null) {
                user.setFirstName(params.getValue("firstName"));
            }
            if (params.getValue("lastName") != null) {
                user.setLastName(params.getValue("lastName"));
            }
            if (params.getValue("email") != null) {
                user.setEmail(params.getValue("email"));
            }
            userDao.save(user);
            result = redirect("/profile");

        } else {
            result = render("profile/edit", context().put("navIndex", true));
        }
        return result;
    }

    public Result changePassword(Params params) {
        Result result;
        if (params.getValue("submit") != null) {
            User user = getUser();

            String currentPassword = params.getValue("currentPassword");
            String newPassword = params.getValue("newPassword");
            String newPassword2 = params.getValue("newPassword2");

            boolean currentPasswordsMatch = currentPassword != null &&
                    passwordEncryptor.checkPassword(currentPassword, user.getPassword());
            boolean newPasswordsMatch = newPassword != null && newPassword.equals(newPassword2);

            if (currentPasswordsMatch && newPasswordsMatch) {
                user.setPassword(passwordEncryptor.encryptPassword(newPassword));
                userDao.save(user);
                result = render("profile/changePassword", context().put("navChangePassword", true)
                        .put("success", "Your password has been changed"));

            } else if (!currentPasswordsMatch) {
                result = render("profile/changePassword", context().put("navChangePassword", true)
                        .put("error", "Current password is incorrect"));
            } else {
                result = render("profile/changePassword", context().put("navChangePassword", true)
                        .put("error", "New passwords don't match"));
            }


        } else {
            result = render("profile/changePassword", context().put("navChangePassword", true));
        }
        return result;
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
}
