package com.linchproject.app;

import com.linchproject.app.dao.UserDao;
import com.linchproject.app.models.User;
import com.linchproject.mvc.Context;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.mvc.Controller {

    protected UserDao userDao;
    private User user;

    protected User getUser() {
        if (user == null && route.getUserId() != null) {
            user = userDao.findByUsername(route.getUserId());
        }
        return user;
    }

    @Override
    protected Context context() {
        return super.context().put("user", getUser());
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
