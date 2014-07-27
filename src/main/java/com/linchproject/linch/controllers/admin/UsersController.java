package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Result;
import com.linchproject.forms.Form;
import com.linchproject.forms.Validator;
import com.linchproject.forms.validators.EmailValidator;
import com.linchproject.forms.validators.EqualsValidator;
import com.linchproject.forms.validators.RequiredValidator;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.I18nForm;
import com.linchproject.linch.actions.Crud;
import com.linchproject.linch.entities.User;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public class UsersController extends AdministratorController implements Crud {

    protected PasswordEncryptor passwordEncryptor;

    @Override
    public Result indexAction() {
        return render(context()
                .put("navUsers", true)
                .put("users", userDao.findAll()));
    }

    @Override
    public Result viewAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));
        return render(context()
                .put("navUsers", true)
                .put("theUser", user));
    }

    @Override
    public Result editAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));

        return render(context()
                .put("navUsers", true)
                .put("form", getEditForm()
                        .put("username", user.getUsername())
                        .put("firstName", user.getFirstName())
                        .put("lastName", user.getLastName())
                        .put("email", user.getEmail())));
    }

    @Override
    public Result doEditAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));
        Form form = getEditForm();

        form.bind(route.getParameterMap()).validate();

        if (form.isValid()) {
            user.setFirstName(form.get("firstName").getValue());
            user.setLastName(form.get("lastName").getValue());
            user.setEmail(form.get("email").getValue());
            userDao.save(user);

            return redirect("view?username=" + user.getUsername());
        }

        return render("edit", context()
                .put("navUsers", true)
                .put("form", form));
    }

    @Override
    public Result createAction() {
        return render(context()
                .put("navUsers", true)
                .put("form", getCreateForm()));
    }

    @Override
    public Result doCreateAction() {
        Form form = getCreateForm();

        form.bind(route.getParameterMap()).validate();

        if (form.isValid()) {
            User user = new User();
            user.setUsername(form.get("username").getValue());
            user.setFirstName(form.get("firstName").getValue());
            user.setLastName(form.get("lastName").getValue());
            user.setEmail(form.get("email").getValue());
            user.setPassword(passwordEncryptor.encryptPassword(form.get("password").getValue()));
            userDao.save(user);

            return redirect("view?username=" + user.getUsername());
        }

        return render("create", context()
                .put("navUsers", true)
                .put("form", form));
    }

    @Override
    public Result deleteAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));

        return render(context()
                .put("navUsers", true)
                .put("theUser", user));
    }

    @Override
    public Result doDeleteAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));
        userDao.delete(user);

        return redirect("index");
    }

    protected Form getEditForm() {
        return new I18nForm(getI18n())
                .addField("username")
                .addField("firstName")
                .addField("lastName")
                .addField("email", new RequiredValidator(), new EmailValidator());
    }

    protected Form getCreateForm() {
        return new I18nForm(getI18n())
                .addField("username", new RequiredValidator(), new UserExistsValidator())
                .addField("firstName")
                .addField("lastName")
                .addField("email", new RequiredValidator(), new EmailValidator())
                .addField("password", new RequiredValidator())
                .addField("confirmPassword", new EqualsValidator("password"));
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
