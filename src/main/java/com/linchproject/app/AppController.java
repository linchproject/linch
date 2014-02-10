package com.linchproject.app;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class AppController extends Controller {

    public Result index(Params params) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("hello", "Hello Linch");

        return render("index", context);
    }
}
