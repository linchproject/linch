package com.linchproject.linch.dao;

import com.linchproject.framework.db.Dao;
import com.linchproject.ioc.Initializing;
import com.linchproject.linch.entities.Setting;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class SettingDao extends Dao<Setting> implements Initializing {

    public Setting findByKey(String key) {
        return query("select id, `key`, value from setting where `key` = :key")
                .addParameter("key", key)
                .executeAndFetchFirst(Setting.class);
    }

    @Override
    public Setting findById(Long id) {
        return query("select id, `key`, value from setting where id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Setting.class);
    }

    @Override
    public List<Setting> findAll() {
        return query("select id, `key`, value from setting")
                .executeAndFetch(Setting.class);
    }

    @Override
    public void save(Setting setting) {
        if (setting.getId() == null) {
            setting.setId(query("insert into setting ( `key`, value ) " +
                    "values ( :key, :value )", true)
                    .bind(setting)
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        } else {
            setting.setId(query("update setting set `key` = :key, value = :value where id = :id", true)
                    .bind(setting)
                    .addParameter("id", setting.getId())
                    .executeUpdate()
                    .<Long>getKey(Long.class));
        }
    }

    @Override
    public void delete(Setting setting) {
        if (setting.getId() != null) {
            query("delete from setting where id = :id")
                    .addParameter("id", setting.getId())
                    .executeUpdate();
        }
    }

    @Override
    public void init() {
        try {
            query("select count(*) from setting").executeScalar(Integer.class);
        } catch (Exception e) {
            query("create table setting ( " +
                    "id int(11) unsigned not null auto_increment, " +
                    "`key` varchar(255) not null, " +
                    "value varchar(255) not null, " +
                    "primary key (id) " +
                    ")").executeUpdate();

            query("create index `key` on setting (`key`)").executeUpdate();
        }
    }
}
