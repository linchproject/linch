package com.linchproject.linch;

import com.linchproject.core.Result;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public abstract class SecureController extends Controller implements _Action {

    @Override
    public Result _Action() {
        Result result;
        if (isActiveUser()) {
            result = dispatch(route);
        } else {
            result = redirect("/auth/login?next=" + route.getPath());
        }
        return result;
    }

    protected boolean isLoggedIn() {
        return getUser() != null;
    }

    protected boolean isActiveUser() {
        return isLoggedIn() && groupDao.isMember(getUserGroup(), getUser());
    }
}
