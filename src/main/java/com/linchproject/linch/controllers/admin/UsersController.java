package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.forms.Form;
import com.linchproject.forms.Validator;
import com.linchproject.forms.validators.EmailValidator;
import com.linchproject.forms.validators.EqualsValidator;
import com.linchproject.forms.validators.RequiredValidator;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.I18nForm;
import com.linchproject.linch.models.User;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public class UsersController extends AdministratorController {

    protected PasswordEncryptor passwordEncryptor;


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

        Form form = new I18nForm(getI18n())
                .addField("username")
                .addField("firstName")
                .addField("lastName")
                .addField("email")
                    .addValidator(new RequiredValidator())
                    .addValidator(new EmailValidator())
                .form();

        if (params.get("submit") != null) {
            form.fill(params.getMap()).validate();

            if (form.isValid()) {
                user.setUsername(form.get("username").get());
                user.setFirstName(form.get("firstName").get());
                user.setLastName(form.get("lastName").get());
                user.setEmail(form.get("email").get());
                userDao.save(user);

                result = redirect("view?username=" + user.getUsername());
            } else {
                result = render("admin/users/edit", context()
                        .put("navUsers", true)
                        .put("form", form));
            }

        } else {
            result = render("admin/users/edit", context()
                    .put("navUsers", true)
                    .put("form", form
                            .put("username", user.getUsername())
                            .put("firstName", user.getFirstName())
                            .put("lastName", user.getLastName())
                            .put("email", user.getEmail())));
        }
        return result;
    }

    public Result create(Params params) {
        Result result;

        Form form = new I18nForm(getI18n())
                .addField("username")
                    .addValidator(new RequiredValidator())
                    .addValidator(new UserExistsValidator())
                .addField("firstName")
                .addField("lastName")
                .addField("email")
                    .addValidator(new RequiredValidator())
                    .addValidator(new EmailValidator())
                .addField("password")
                    .addValidator(new RequiredValidator())
                .addField("confirmPassword")
                    .addValidator(new EqualsValidator("password"))
                .form();

        if (params.get("submit") != null) {
            form.fill(params.getMap()).validate();

            if (form.isValid()) {
                User user = new User();
                user.setUsername(form.get("username").get());
                user.setFirstName(form.get("firstName").get());
                user.setLastName(form.get("lastName").get());
                user.setEmail(form.get("email").get());
                user.setPassword(passwordEncryptor.encryptPassword(form.get("password").get()));
                userDao.save(user);

                result = redirect("view?username=" + user.getUsername());
            } else {
                result = render("admin/users/create", context()
                        .put("navUsers", true)
                        .put("form", form));
            }

        } else {
            result = render("admin/users/create", context()
                    .put("navUsers", true)
                    .put("form", form));
        }
        return result;
    }

    public Result delete(Params params) {
        Result result;
        User user = userDao.findByUsername(params.get("username"));

        if (params.get("submit") != null) {
            userDao.delete(user);
            result = redirect("index");

        } else {
            result = render("admin/users/delete", context()
                    .put("navUsers", true)
                    .put("theUser", user));
        }
        return result;
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public class UserExistsValidator implements Validator {
        @Override
        public String getKey() {
            return "user.exists";
        }

        @Override
        public boolean isValid(String[] values, Form form) {
            String username = values[0];
            User user = userDao.findByUsername(username);
            return user == null;
        }
    }
}
