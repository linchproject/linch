package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.linch.AdministratorController;

/**
 * @author Georg Schmidl
 */
public class AppController extends AdministratorController implements IndexAction {

    public Result indexAction() {
        return render(context()
                .put("hello", "Hello Admin"));
    }

}
