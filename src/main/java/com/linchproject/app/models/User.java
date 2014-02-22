package com.linchproject.app.models;

import java.util.List;

import static com.linchproject.mvc.Controller.query;

/**
 * @author Georg Schmidl
 */
public class User {

    private Long id;

    private String username;
    private String password;
    private String fullName;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static User fetch(String username) {
        return query("select id, username, password, full_name, email from user where username = :username")
                .addParameter("username", username)
                .executeAndFetchFirst(User.class);
    }

    public static List<User> fetchAll() {
        return query("select id, username, password, full_name, email from user")
                .executeAndFetch(User.class);
    }

    public void save() {
        if (getId() == null) {
            setId(query("insert into user ( username, password, full_name, email ) " +
                    "values ( :username, :password, :full_name, :email )", true)
                    .bind(this)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            setId(query("update user set username = :username, password = :password," +
                    " full_name = :full_name, email = :email where id = :id", true)
                    .bind(this)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }
}
