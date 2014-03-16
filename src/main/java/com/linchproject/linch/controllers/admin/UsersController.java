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

    private Form createForm;

    @Override
    public void init() {
        this.createForm = new I18nForm(getI18n())
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
    }

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

    public Result create(Params params) {
        Result result;

        if (params.get("submit") != null) {
            createForm.fill(params.getMap()).validate();

            if (createForm.isValid()) {
                User user = new User();
                user.setUsername(createForm.get("username").get());
                user.setFirstName(createForm.get("firstName").get());
                user.setLastName(createForm.get("lastName").get());
                user.setEmail(createForm.get("email").get());
                user.setPassword(passwordEncryptor.encryptPassword(createForm.get("password").get()));
                userDao.save(user);

                result = redirect("view?username=" + user.getUsername());
            } else {
                result = render("admin/users/create", context()
                        .put("navUsers", true)
                        .put("form", createForm));
            }

        } else {
            result = render("admin/users/create", context()
                    .put("navUsers", true)
                    .put("form", createForm));
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
