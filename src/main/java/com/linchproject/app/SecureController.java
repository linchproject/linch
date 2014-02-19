package com.linchproject.app;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.mvc.Controller;

/**
 * @author Georg Schmidl
 */
public abstract class SecureController extends Controller {

    public Result _(Params params) {
        Result result;
        if (isPermitted()) {
            result = dispatch(route);
        } else {
            result = error("Not permitted");
        }
        return result;
    }

    protected boolean isPermitted() {
        return route.getUserId() != null;
    }
}
