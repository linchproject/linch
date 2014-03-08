package com.linchproject.app;

import com.linchproject.app.dao.RememberDao;
import com.linchproject.app.dao.UserDao;
import com.linchproject.app.models.Remember;
import com.linchproject.app.models.User;
import com.linchproject.framework.Context;
import com.linchproject.http.CookieService;
import com.linchproject.http.SessionService;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.framework.Controller {

    protected SessionService sessionService;
    protected CookieService cookieService;

    protected UserDao userDao;
    protected RememberDao rememberDao;

    private User user;
    private boolean cookieChecked;

    protected User getUser() {
        String username = getUsernameFromSessionOrCookie();

        if (user == null && username != null) {
            user = userDao.findByUsername(username);
        }
        return user;
    }

    private String getUsernameFromSessionOrCookie() {
        String userId = sessionService.getValue(Settings.SESSION_USER_KEY);

        if (userId == null && !cookieChecked) {
            String uuid = cookieService.getCookieValue(Settings.COOKIE_NAME);
            if (uuid != null) {
                Remember remember = rememberDao.findByUuid(uuid);
                if (remember != null) {
                    User user = userDao.findById(remember.getUserId());
                    if (user != null) {
                        userId = user.getUsername();
                        sessionService.setValue(Settings.SESSION_USER_KEY, user.getUsername());
                        cookieService.addCookie(Settings.COOKIE_NAME, remember.getUuid(), Settings.COOKIE_MAX_AGE);
                    }
                }
            }
            cookieChecked = true;
        }
        return userId;
    }

    @Override
    protected Context context() {
        return super.context().put("user", getUser());
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setRememberDao(RememberDao rememberDao) {
        this.rememberDao = rememberDao;
    }
}
