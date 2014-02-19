package com.linchproject.app.controllers.admin;

import com.linchproject.app.AdministratorController;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class AppController extends AdministratorController {

    public Result index(Params params) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("hello", "Hello Admin");

        return render("index", context);
    }

}
