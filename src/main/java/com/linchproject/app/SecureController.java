package com.linchproject.app;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.mvc.Controller;

/**
 * @author Georg Schmidl
 */
public abstract class SecureController extends Controller {

    @Override
    public Result _filter(Params params) {
        Result result;
        if (isPermitted()) {
            result = super._filter(params);
        } else {
            result = error("Not permitted");
        }
        return result;
    }

    protected boolean isPermitted() {
        return route.getUserId() != null;
    }
}
