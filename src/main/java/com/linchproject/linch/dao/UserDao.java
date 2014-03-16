package com.linchproject.linch.dao;

import com.linchproject.linch.entities.User;
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
        return query("select id, username, password, first_name, last_name, email from user order by username")
                .executeAndFetch(User.class);
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(query("insert into user ( username, password, first_name, last_name, email ) " +
                    "values ( :username, :password, :firstName, :lastName, :email )", true)
                    .addParameter("username", user.getUsername()) //.bind(model) not possible because of fullName
                    .addParameter("password", user.getPassword())
                    .addParameter("firstName", user.getFirstName())
                    .addParameter("lastName", user.getLastName())
                    .addParameter("email", user.getEmail())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            user.setId(query("update user set username = :username, password = :password, " +
                    "first_name = :firstName, last_name = :lastName, email = :email where id = :id", true)
                    .addParameter("id", user.getId()) //.bind(model) not possible because of fullName
                    .addParameter("username", user.getUsername())
                    .addParameter("password", user.getPassword())
                    .addParameter("firstName", user.getFirstName())
                    .addParameter("lastName", user.getLastName())
                    .addParameter("email", user.getEmail())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    @Override
    public void delete(User user) {
        if (user.getId() != null) {
            query("delete from user where id = :id")
                    .addParameter("id", user.getId())
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

        if (findAll().isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("N5lrX+ZfsB1MuC/J+frGztL/eRL0n+7J");
            save(user);
        }
    }
}
