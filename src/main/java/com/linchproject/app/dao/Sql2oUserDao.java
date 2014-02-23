package com.linchproject.app.dao;

import com.linchproject.app.models.User;
import com.linchproject.ioc.Component;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.StatementRunnable;

import javax.sql.DataSource;
import java.util.List;

import static com.linchproject.mvc.ConnectionThreadLocal.query;

/**
 * @author Georg Schmidl
 */
public class Sql2oUserDao implements UserDao, Component {

    protected DataSource dataSource;

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
        new Sql2o(dataSource).runInTransaction(new StatementRunnable() {
            @Override
            public void run(Connection connection, Object argument) throws Throwable {
                connection.createQuery("CREATE TABLE IF NOT EXISTS user ( " +
                        "id int(11) unsigned NOT NULL AUTO_INCREMENT, " +
                        "username varchar(255) NOT NULL DEFAULT '', " +
                        "password varchar(255) DEFAULT NULL, " +
                        "full_name varchar(255) DEFAULT NULL, " +
                        "email varchar(255) DEFAULT '', " +
                        "PRIMARY KEY (id), " +
                        "KEY username (username) " +
                        ")").executeUpdate();

                User user = connection.createQuery("select id from user")
                        .executeAndFetchFirst(User.class);

                if (user == null) {
                    connection.createQuery("insert into user ( username, password ) " +
                            "values ( :username, :password )")
                            .addParameter("username", "admin")
                            .addParameter("password", "N5lrX+ZfsB1MuC/J+frGztL/eRL0n+7J")
                            .executeUpdate();
                }
            }
        });
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
