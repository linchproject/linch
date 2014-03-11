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

    public Result index(Params params) {
        return render("profile/index", context().put("navIndex", true));
    }

    public Result edit(Params params) {
        Result result;
        if (params.get("submit") != null) {
            User user = getUser();
            user.setFirstName(params.get("firstName"));
            user.setLastName(params.get("lastName"));

            if (params.get("email") != null) {
                user.setEmail(params.get("email"));
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
        if (params.get("submit") != null) {
            User user = getUser();

            String currentPassword = params.get("currentPassword");
            String newPassword = params.get("newPassword");
            String newPassword2 = params.get("newPassword2");

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
