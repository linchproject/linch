package com.linchproject.app;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class AppController extends Controller {

    public Result index(Params params) {
        return success("Hello Linch");
    }
}
