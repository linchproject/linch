package com.linchproject.linch.dao;

import com.linchproject.linch.entities.Remember;
import com.linchproject.framework.db.Dao;
import com.linchproject.ioc.Initializing;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class RememberDao extends Dao<Remember> implements Initializing {

    @Override
    public Remember findById(Long id) {
        return query("select id, uuid, user_id from remember where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Remember.class);
    }

    @Override
    public List<Remember> findAll() {
        return query("select id, uuid, user_id from remember")
                .executeAndFetch(Remember.class);
    }

    @Override
    public void save(Remember remember) {
        if (remember.getId() == null) {
            remember.setId(query("insert into remember ( uuid, user_id ) " +
                    "values ( :uuid, :userId )", true)
                    .bind(remember)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            remember.setId(query("update remember set uuid = :uuid, user_id = :userId where id = :id", true)
                    .bind(remember)
                    .addParameter("id", remember.getId())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    @Override
    public void delete(Remember remember) {
        if (remember.getId() != null) {
            query("delete from remember where id = :id")
                    .addParameter("id", remember.getId())
                    .executeUpdate();
        }
    }

    public Remember findByUserId(Long userId) {
        return query("select id, uuid, user_id from remember where user_id = :userId")
                .addParameter("userId", userId)
                .executeAndFetchFirst(Remember.class);
    }

    public Remember findByUuid(String uuid) {
        return query("select id, uuid, user_id from remember where uuid = :uuid")
                .addParameter("uuid", uuid)
                .executeAndFetchFirst(Remember.class);
    }

    @Override
    public void init() {
        query("create table if not exists remember ( " +
                "id int(11) unsigned not null auto_increment, " +
                "uuid varchar(255) not null, " +
                "user_id int(11) unsigned not null, " +
                "primary key (id), " +
                "key uuid (uuid), " +
                "key user_id (user_id) " +
                ") engine=InnoDB default charset=utf8").executeUpdate();
    }
}
