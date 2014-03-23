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
import com.linchproject.linch.entities.User;
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
                .addField("email", new RequiredValidator(), new EmailValidator());

        if (params.get("submit") != null) {
            form.bind(params.getMap()).validate();

            if (form.isValid()) {
                user.setFirstName(form.get("firstName").getValue());
                user.setLastName(form.get("lastName").getValue());
                user.setEmail(form.get("email").getValue());
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
                .addField("username", new RequiredValidator(), new UserExistsValidator())
                .addField("firstName")
                .addField("lastName")
                .addField("email", new RequiredValidator(), new EmailValidator())
                .addField("password", new RequiredValidator())
                .addField("confirmPassword", new EqualsValidator("password"));

        if (params.get("submit") != null) {
            form.bind(params.getMap()).validate();

            if (form.isValid()) {
                User user = new User();
                user.setUsername(form.get("username").getValue());
                user.setFirstName(form.get("firstName").getValue());
                user.setLastName(form.get("lastName").getValue());
                user.setEmail(form.get("email").getValue());
                user.setPassword(passwordEncryptor.encryptPassword(form.get("password").getValue()));
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
        public String getErrorKey() {
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
