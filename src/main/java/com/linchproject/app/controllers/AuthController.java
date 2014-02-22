package com.linchproject.app.controllers;

import com.linchproject.app.models.User;
import com.linchproject.mvc.Controller;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * @author Georg Schmidl
 */
public abstract class AuthController extends Controller {

    protected PasswordEncryptor passwordEncryptor;

    protected User fetch(String username) {
        return query("select id, username, password, full_name, email from user where username = :username")
                .addParameter("username", username)
                .executeAndFetchFirst(User.class);
    }

    protected void save(User user) {
        if (user.getId() == null) {
            user.setId(query("insert into user ( username, password, full_name, email ) " +
                    "values ( :username, :password, :full_name, :email )", true)
                    .bind(user)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            user.setId(query("update user set username = :username, password = :password," +
                    " full_name = :full_name, email = :email where id = :id", true)
                    .bind(user)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
}
