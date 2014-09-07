package com.linchproject.linch.controllers;

import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.linch.SecureController;
import com.linchproject.linch.actions.EditAction;
import com.linchproject.linch.entities.User;
import com.linchproject.validator.*;
import com.linchproject.validator.validators.EqualsOtherValidator;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public class ProfileController extends SecureController implements IndexAction, EditAction {

    protected PasswordEncryptor passwordEncryptor;

    @Override
    public Result indexAction() {
        return render();
    }

    @Override
    public Result editAction() {
        Data data = new UserDataValidator().dataFrom(getUser());

        return render(context()
                .put("data", data));
    }

    @Override
    public Result doEditAction() {
        User user = getUser();

        Data data = new UserDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            data.writeTo(user, "firstName", "lastName", "email");
            userDao.save(user);

            return redirect("index");
        }

        return render("edit", context().put("data", data));
    }

    public Result changePasswordAction() {
        return render(context()
                .put("data", new PasswordDataValidator().emptyData()));
    }

    public Result doChangePasswordAction() {
        User user = getUser();

        Data data = new PasswordDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            user.setPassword(passwordEncryptor.encryptPassword(data.<String>get("newPassword")));
            userDao.save(user);
            return render("changePassword", context()
                    .put("success", true));
        }

        return render("changePassword", context()
                .put("data", data));
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public class PasswordDataValidator extends DataValidator {
        public PasswordDataValidator() {
            addFields("currentPassword", "newPassword", "confirmNewPassword");
            setAllRequired();
            addValidator("currentPassword", new PasswordValidator());
            addValidator("confirmNewPassword", new EqualsOtherValidator("newPassword"));
        }

        public class PasswordValidator implements Validator {

            @Override
            public String getKey() {
                return "password.incorrect";
            }

            @Override
            public boolean isValid(Value value) {
                return passwordEncryptor.checkPassword(value.getString(), getUser().getPassword());
            }
        }
    }

    public class UserDataValidator extends DataValidator {
        public UserDataValidator() {
            addFields(User.class);
            setRequired("email");
            addValidator("email", Validators.EMAIL);
        }
    }
}
