package com.linchproject.linch.dao;

import com.linchproject.framework.db.Dao;
import com.linchproject.ioc.Initializing;
import com.linchproject.linch.entities.Group;
import com.linchproject.linch.entities.User;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class GroupDao extends Dao<Group> implements Initializing {

    private UserDao userDao;

    public Group findByName(String name) {
        return query("select id, name from `group` where name = :name")
                .addParameter("name", name)
                .executeAndFetchFirst(Group.class);
    }

    @Override
    public Group findById(Long id) {
        return query("select id, name from `group` where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Group.class);
    }

    @Override
    public List<Group> findAll() {
        return query("select id, name from user")
                .executeAndFetch(Group.class);
    }

    @Override
    public void save(Group group) {
        if (group.getId() == null) {
            group.setId(query("insert into `group` ( name ) values ( :name )", true)
                    .bind(group)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            group.setId(query("update `group` set name = :name where id = :id", true)
                    .bind(group)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    @Override
    public void delete(Group group) {
        if (group.getId() != null) {
            query("delete from `group` where id = :id")
                    .addParameter("id", group.getId())
                    .executeUpdate();
        }
    }

    public void addMember(Group group, User user) {
        query("insert into group_user ( group_id, user_id ) values ( :group_id, :user_id )")
                .addParameter("group_id", group.getId())
                .addParameter("user_id", user.getId())
                .executeUpdate();
    }

    public boolean isMember(Group group, User user) {
        return query("select user_id from group_user where group_id = :group_id and user_id = :user_id")
                .addParameter("group_id", group.getId())
                .addParameter("user_id", user.getId())
                .executeAndFetchFirst(Long.class) != null;
    }

    public List<User> getMembers(Group group) {
        return query("select * from `group`_user inner join user on group_user.user_id = user.id " +
                "where group_user.group_id = :group_id")
                .addParameter("group_id", group.getId())
                .executeAndFetch(User.class);
    }

    @Override
    public void init() {
        query("create table if not exists `group` ( " +
                "id int(11) unsigned not null auto_increment, " +
                "name varchar(255) not null, " +
                "primary key (id), " +
                "key name (name) " +
                ") engine=InnoDB default charset=utf8").executeUpdate();

        query("create table if not exists group_user ( " +
                "group_id int(11) unsigned not null, " +
                "user_id int(11) unsigned not null, " +
                "primary key (user_id, group_id)" +
                ") engine=InnoDB default charset=utf8").executeUpdate();

        User admin = userDao.findByUsername("admin");

        Group users = findByName("users");
        if (users == null) {
            users = new Group();
            users.setName("users");
            save(users);

            if (admin != null) {
                addMember(users, admin);
            }
        }

        Group administrators = findByName("administrators");
        if (administrators == null) {
            administrators = new Group();
            administrators.setName("administrators");
            save(administrators);

            if (admin != null) {
                addMember(administrators, admin);
            }
        }
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
