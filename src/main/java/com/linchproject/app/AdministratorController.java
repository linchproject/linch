package com.linchproject.app;

import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public abstract class AdministratorController extends Controller {

    public Result _(Params params) {
        Result result;
        if (isPermitted()) {
            result = dispatch(route);
        } else if (isLoggedIn()) {
            result = error("Not permitted");
        } else {
            result = redirect("/login?next=" + route.getPath());
        }
        return result;
    }

    protected boolean isLoggedIn() {
        return sessionService.getUserId() != null;
    }

    protected boolean isPermitted() {
        return isLoggedIn() && "admin".equals(sessionService.getUserId());
    }
}
