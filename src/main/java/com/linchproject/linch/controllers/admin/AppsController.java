package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.services.AppService;

/**
 * @author Georg Schmidl
 */
public class AppsController extends AdministratorController implements IndexAction {

    protected AppService appService;

    @Override
    public Result indexAction() {
        return render(context()
                .put("navApps", true)
                .put("apps", appService.getApps()));
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
