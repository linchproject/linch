package com.linchproject.linch;

import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public abstract class AdministratorController extends SecureController {

    public Result _(Params params) {
        Result result;
        if (isAdministrator()) {
            result = dispatch(route);
        } else if (isLoggedIn()) {
            result = error("Not permitted");
        } else {
            result = redirect("/login?next=" + route.getPath());
        }
        return result;
    }

    protected boolean isAdministrator() {
        return isLoggedIn() && isActiveUser() && groupDao.isMember(getAdministratorGroup(), getUser());
    }
}
