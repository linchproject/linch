package com.linchproject.app.dao;

import com.linchproject.app.models.User;
import com.linchproject.ioc.Component;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class Sql2oUserDao extends Sql2oDao implements UserDao, Component {

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

    @Override
    public void init() {
        query("CREATE TABLE IF NOT EXISTS user ( " +
                "id int(11) unsigned NOT NULL AUTO_INCREMENT, " +
                "username varchar(255) NOT NULL DEFAULT '', " +
                "password varchar(255) DEFAULT NULL, " +
                "full_name varchar(255) DEFAULT NULL, " +
                "email varchar(255) DEFAULT '', " +
                "PRIMARY KEY (id), " +
                "KEY username (username) " +
                ")").executeUpdate();

        User user = query("select id from user").executeAndFetchFirst(User.class);

        if (user == null) {
            query("insert into user ( username, password ) " +
                    "values ( :username, :password )")
                    .addParameter("username", "admin")
                    .addParameter("password", "N5lrX+ZfsB1MuC/J+frGztL/eRL0n+7J")
                    .executeUpdate();
        }
    }
}
