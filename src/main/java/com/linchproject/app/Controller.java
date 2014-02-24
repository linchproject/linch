package com.linchproject.app;

import com.linchproject.app.dao.UserDao;
import com.linchproject.app.models.User;
import com.linchproject.http.SessionService;
import com.linchproject.mvc.Context;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.mvc.Controller {

    protected SessionService sessionService;
    protected UserDao userDao;

    private User user;

    protected User getUser() {
        if (user == null && sessionService.getUserId() != null) {
            user = userDao.findByUsername(sessionService.getUserId());
        }
        return user;
    }

    @Override
    protected Context context() {
        return super.context().put("user", getUser());
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
