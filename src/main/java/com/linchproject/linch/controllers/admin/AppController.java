package com.linchproject.linch.controllers.admin;

import com.linchproject.linch.AdministratorController;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class AppController extends AdministratorController {

    public Result index(Params params) {
        return render(context()
                .put("navIndex", true)
                .put("hello", "Hello Admin"));
    }

}
