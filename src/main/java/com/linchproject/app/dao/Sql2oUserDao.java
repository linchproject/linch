package com.linchproject.app.dao;

import com.linchproject.app.models.User;

import java.util.List;

import static com.linchproject.mvc.ConnectionThreadLocal.*;

/**
 * @author Georg Schmidl
 */
public class Sql2oUserDao implements UserDao {

    @Override
    public User findByUsername(String username) {
        return query("select id, username, password, full_name, email from user where username = :username")
                .addParameter("username", username)
                .executeAndFetchFirst(User.class);
    }

    @Override
    public User findById(Long id) {
        return query("select id, username, password, full_name, email from user where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(User.class);
    }

    @Override
    public List<User> findAll() {
        return query("select id, username, password, full_name, email from user")
                .executeAndFetch(User.class);
    }

    @Override
    public void save(User model) {
        if (model.getId() == null) {
            model.setId(query("insert into user ( username, password, full_name, email ) " +
                    "values ( :username, :password, :full_name, :email )", true)
                    .bind(this)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            model.setId(query("update user set username = :username, password = :password," +
                    " full_name = :full_name, email = :email where id = :id", true)
                    .bind(this)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }
}
