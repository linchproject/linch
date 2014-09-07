package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Result;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.actions.Crud;
import com.linchproject.linch.entities.User;
import com.linchproject.validator.*;
import com.linchproject.validator.validators.EqualsOtherValidator;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public class UsersController extends AdministratorController implements Crud {

    protected PasswordEncryptor passwordEncryptor;

    @Override
    public Result indexAction() {
        return render(context()
                .put("users", userDao.findAll()));
    }

    @Override
    public Result viewAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));
        return render(context()
                .put("theUser", user));
    }

    @Override
    public Result editAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));

        return render(context()
                .put("data", new EditUserDataValidator().dataFrom(user)));
    }

    @Override
    public Result doEditAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));

        Data data = new EditUserDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            data.writeTo(user, "firstName", "lastName", "email");
            userDao.save(user);

            return redirect("view?username=" + user.getUsername());
        }

        return render("edit", context()
                .put("data", data));
    }

    @Override
    public Result createAction() {
        return render(context()
                .put("data", new CreateUserDataValidator().emptyData()));
    }

    @Override
    public Result doCreateAction() {
        Data data = new CreateUserDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            User user = new User();
            data.writeTo(user);
            userDao.save(user);

            return redirect("view?username=" + user.getUsername());
        }

        return render("create", context()
                .put("data", data));
    }

    @Override
    public Result deleteAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));

        return render(context()
                .put("theUser", user));
    }

    @Override
    public Result doDeleteAction() {
        User user = userDao.findByUsername(route.getParams().get("username"));
        userDao.delete(user);

        return redirect("index");
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public class EditUserDataValidator extends DataValidator {
        public EditUserDataValidator() {
            addFields(User.class);
            setRequired("email");
            addValidator("email", Validators.EMAIL);
        }
    }

    public class CreateUserDataValidator extends DataValidator {
        CreateUserDataValidator() {
            addFields(User.class, "confirmPassword");
            setRequired("username", "email", "password", "confirmPassword");
            addValidator("username", new UserExistsValidator());
            addValidator("email", Validators.EMAIL);
            addValidator("confirmPassword", new EqualsOtherValidator("password"));
            addParser("password", new PasswordParser());
        }

        public class UserExistsValidator implements Validator {

            @Override
            public String getKey() {
                return "user.exists";
            }

            @Override
            public boolean isValid(Value value) {
                User user = userDao.findByUsername(value.getString());
                return user == null;
            }
        }

        public class PasswordParser implements Parser<String> {

            @Override
            public Class<String> getType() {
                return String.class;
            }

            @Override
            public String parse(Value value) throws ParseException {
                return passwordEncryptor.encryptPassword(value.getString());
            }

            @Override
            public String[] toStringArray(String s) {
                return null;
            }
        }
    }
}
