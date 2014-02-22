package com.linchproject.app.controllers.admin;

import com.linchproject.app.AdministratorController;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class AppController extends AdministratorController {

    public Result index(Params params) {
        return render("admin/index", context()
                .put("nav", context().put("index", true))
                .put("nav.admin", true)
                .put("hello", "Hello Admin"));
    }

}
