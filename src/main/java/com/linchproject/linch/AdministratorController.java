package com.linchproject.linch;

import com.linchproject.core.Result;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public abstract class AdministratorController extends SecureController implements _Action {

    @Override
    public Result _Action() {
        Result result;
        if (isAdministrator()) {
            result = dispatch(route);
        } else if (isLoggedIn()) {
            result = error("Not permitted");
        } else {
            result = redirect("/auth/login?next=" + route.getPath());
        }
        return result;
    }

    protected boolean isAdministrator() {
        return isLoggedIn() && isActiveUser() && groupDao.isMember(getAdministratorGroup(), getUser());
    }
}
