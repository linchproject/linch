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

    public Group findByGroupname(String groupname) {
        return query("select id, groupname from `group` where groupname = :groupname")
                .addParameter("groupname", groupname)
                .executeAndFetchFirst(Group.class);
    }

    @Override
    public Group findById(Long id) {
        return query("select id, groupname from `group` where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Group.class);
    }

    @Override
    public List<Group> findAll() {
        return query("select id, groupname from `group` order by groupname")
                .executeAndFetch(Group.class);
    }

    @Override
    public void save(Group group) {
        if (group.getId() == null) {
            group.setId(query("insert into `group` ( groupname ) values ( :groupname )", true)
                    .bind(group)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            group.setId(query("update `group` set groupname = :groupname where id = :id", true)
                    .bind(group)
                    .addParameter("id", group.getId())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    @Override
    public void delete(Group group) {
        if (group.getId() != null) {
            query("delete from group_user where group_id = :group_id")
                    .addParameter("group_id", group.getId())
                    .executeUpdate();
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

    public void removeMember(Group group, User user) {
        query("delete from group_user where group_id = :group_id and user_id = :user_id")
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
        return query("select id, username, password, first_name, last_name, email " +
                "from group_user inner join user on group_user.user_id = user.id " +
                "where group_user.group_id = :group_id")
                .addParameter("group_id", group.getId())
                .executeAndFetch(User.class);
    }

    @Override
    public void init() {
        query("create table if not exists `group` ( " +
                "id int(11) unsigned not null auto_increment, " +
                "groupname varchar(255) not null, " +
                "primary key (id), " +
                "key groupname (groupname) " +
                ") engine=InnoDB default charset=utf8").executeUpdate();

        query("create table if not exists group_user ( " +
                "group_id int(11) unsigned not null, " +
                "user_id int(11) unsigned not null, " +
                "primary key (user_id, group_id)" +
                ") engine=InnoDB default charset=utf8").executeUpdate();

        User admin = userDao.findByUsername("admin");

        Group users = findByGroupname("users");
        if (users == null) {
            users = new Group();
            users.setGroupname("users");
            save(users);

            if (admin != null) {
                addMember(users, admin);
            }
        }

        Group administrators = findByGroupname("administrators");
        if (administrators == null) {
            administrators = new Group();
            administrators.setGroupname("administrators");
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
