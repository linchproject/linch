package com.linchproject.linch.dao;

import com.linchproject.linch.models.User;
import com.linchproject.framework.db.Dao;
import com.linchproject.ioc.Initializing;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class UserDao extends Dao<User> implements Initializing {

    public User findByUsername(String username) {
        return query("select id, username, password, first_name, last_name, email from user where username = :username")
                .addParameter("username", username)
                .executeAndFetchFirst(User.class);
    }

    @Override
    public User findById(Long id) {
        return query("select id, username, password, first_name, last_name, email from user where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(User.class);
    }

    @Override
    public List<User> findAll() {
        return query("select id, username, password, first_name, last_name, email from user")
                .executeAndFetch(User.class);
    }

    @Override
    public void save(User object) {
        if (object.getId() == null) {
            object.setId(query("insert into user ( username, password, first_name, last_name, email ) " +
                    "values ( :username, :password, :firstName, :lastName, :email )", true)
                    .addParameter("username", object.getUsername()) //.bind(model) not possible because of fullName
                    .addParameter("password", object.getPassword())
                    .addParameter("firstName", object.getFirstName())
                    .addParameter("lastName", object.getLastName())
                    .addParameter("email", object.getEmail())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            object.setId(query("update user set username = :username, password = :password, " +
                    "first_name = :firstName, last_name = :lastName, email = :email where id = :id", true)
                    .addParameter("id", object.getId()) //.bind(model) not possible because of fullName
                    .addParameter("username", object.getUsername())
                    .addParameter("password", object.getPassword())
                    .addParameter("firstName", object.getFirstName())
                    .addParameter("lastName", object.getLastName())
                    .addParameter("email", object.getEmail())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    @Override
    public void delete(User object) {
        if (object.getId() != null) {
            query("delete from user where id = :id")
                    .addParameter("id", object.getId())
                    .executeUpdate();
        }
    }

    @Override
    public void init() {
        query("create table if not exists user ( " +
                "id int(11) unsigned not null auto_increment, " +
                "username varchar(255) not null, " +
                "password varchar(255) not null, " +
                "first_name varchar(255), " +
                "last_name varchar(255), " +
                "email varchar(255), " +
                "primary key (id), " +
                "key username (username) " +
                ") engine=InnoDB default charset=utf8").executeUpdate();

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
