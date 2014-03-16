package com.linchproject.linch;

import com.github.mustachejava.TemplateFunction;
import com.linchproject.linch.dao.GroupDao;
import com.linchproject.linch.dao.RememberDao;
import com.linchproject.linch.dao.UserDao;
import com.linchproject.linch.entities.Group;
import com.linchproject.linch.entities.Remember;
import com.linchproject.linch.entities.User;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.framework.Controller {

    protected UserDao userDao;
    protected RememberDao rememberDao;
    protected GroupDao groupDao;

    private boolean cookieChecked;

    private User user;
    private Group userGroup;
    private Group administratorGroup;

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

    public Group getUserGroup() {
        if (userGroup == null) {
            userGroup = groupDao.findByName("users");
        }
        return userGroup;
    }

    public Group getAdministratorGroup() {
        if (administratorGroup == null) {
            administratorGroup = groupDao.findByName("administrators");
        }
        return administratorGroup;
    }

    @Override
    protected Context context() {
        return super.context()
                .put("user", getUser())
                .put("i18n", new TemplateFunction() {
                    @Override
                    public String apply(String input) {
                        return getI18n().getText(input);
                    }
                });
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setRememberDao(RememberDao rememberDao) {
        this.rememberDao = rememberDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }
}
