package com.linchproject.app.dao;

import com.linchproject.app.models.User;

/**
 * @author Georg Schmidl
 */
public interface UserDao extends Dao<User, Long> {

    User findByUsername(String username);
}
