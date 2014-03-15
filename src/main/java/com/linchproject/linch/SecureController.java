package com.linchproject.linch;

import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public abstract class SecureController extends Controller {

    public Result _(Params params) {
        Result result;
        if (isLoggedIn()) {
            result = dispatch(route);
        } else {
            result = redirect("/login?next=" + route.getPath());
        }
        return result;
    }

    protected boolean isLoggedIn() {
        return getUser() != null;
    }
}
