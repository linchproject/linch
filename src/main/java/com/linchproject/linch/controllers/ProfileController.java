package com.linchproject.linch.controllers;

import com.linchproject.forms.Form;
import com.linchproject.forms.Validator;
import com.linchproject.forms.validators.EmailValidator;
import com.linchproject.forms.validators.EqualsValidator;
import com.linchproject.forms.validators.RequiredValidator;
import com.linchproject.linch.I18nForm;
import com.linchproject.linch.SecureController;
import com.linchproject.linch.entities.User;
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
        return render("profile/edit", context()
                .put("navIndex", true)
                .put("form", getEditForm()
                        .put("firstName", getUser().getFirstName())
                        .put("lastName", getUser().getLastName())
                        .put("email", getUser().getEmail())));
    }

    public Result doEdit(Params params) {
        User user = getUser();
        Form form = getEditForm();

        form.bind(params.getMap()).validate();

        if (form.isValid()) {
            user.setFirstName(form.get("firstName").getValue());
            user.setLastName(form.get("lastName").getValue());
            user.setEmail(form.get("email").getValue());
            userDao.save(user);

            return redirect("index");
        }

        return render("profile/edit", context()
                .put("navIndex", true)
                .put("form", form));
    }

    public Result changePassword(Params params) {
        return render("profile/changePassword", context()
                .put("navChangePassword", true)
                .put("form", getChangePasswordForm()));
    }

    public Result doChangePassword(Params params) {
        User user = getUser();
        Form form = getChangePasswordForm();

        form.bind(params.getMap()).validate();

        if (form.isValid()) {
            user.setPassword(passwordEncryptor.encryptPassword(form.get("newPassword").getValue()));
            userDao.save(user);
            return render("profile/changePassword", context()
                    .put("navChangePassword", true)
                    .put("success", true));
        }

        return render("profile/changePassword", context()
                .put("navChangePassword", true)
                .put("form", form));
    }

    protected Form getEditForm() {
        return new I18nForm(getI18n())
                .addField("firstName")
                .addField("lastName")
                .addField("email", new RequiredValidator(), new EmailValidator());
    }

    protected Form getChangePasswordForm() {
        return new I18nForm(getI18n())
                .addField("currentPassword", new RequiredValidator(), new PasswordValidator())
                .addField("newPassword", new RequiredValidator())
                .addField("confirmNewPassword", new RequiredValidator(), new EqualsValidator("newPassword"));
    }

    public class PasswordValidator implements Validator {

        @Override
        public String getErrorKey() {
            return "password.incorrect";
        }

        @Override
        public boolean isValid(String[] values, Form form) {
            return passwordEncryptor.checkPassword(values[0], getUser().getPassword());
        }
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
}
