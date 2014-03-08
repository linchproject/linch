package com.linchproject.app;

import com.github.mustachejava.TemplateFunction;
import com.linchproject.app.dao.RememberDao;
import com.linchproject.app.dao.UserDao;
import com.linchproject.app.models.Remember;
import com.linchproject.app.models.User;
import com.linchproject.framework.Context;
import com.linchproject.framework.I18n;
import com.linchproject.framework.components.I18nService;
import com.linchproject.http.CookieService;
import com.linchproject.http.LocaleService;
import com.linchproject.http.SessionService;

/**
 * @author Georg Schmidl
 */
public class Controller extends com.linchproject.framework.Controller {

    protected SessionService sessionService;
    protected CookieService cookieService;
    protected LocaleService localeService;
    protected I18nService i18nService;

    protected UserDao userDao;
    protected RememberDao rememberDao;

    private boolean cookieChecked;

    private User user;
    private I18n i18n;

    protected User getUser() {
        String username = getUsernameFromSessionOrCookie();

        if (user == null && username != null) {
            user = userDao.findByUsername(username);
        }
        return user;
    }

    protected I18n getI18n() {
        if (i18n == null) {
            i18n = i18nService.getI18n(localeService.getLocale());
        }
        return i18n;
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
        return super.context()
                .put("user", getUser())
                .put("i18n", new TemplateFunction() {
                    @Override
                    public String apply(String input) {
                        return getI18n().getText(input);
                    }
                });
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setCookieService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    public void setLocaleService(LocaleService localeService) {
        this.localeService = localeService;
    }

    public void setI18nService(I18nService i18nService) {
        this.i18nService = i18nService;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setRememberDao(RememberDao rememberDao) {
        this.rememberDao = rememberDao;
    }
}
