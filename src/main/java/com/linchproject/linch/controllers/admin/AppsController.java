package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.services.AppService;

/**
 * @author Georg Schmidl
 */
public class AppsController extends AdministratorController {

    protected AppService appService;

    public Result index(Params params) {
        return render(context()
                .put("navApps", true)
                .put("apps", appService.getApps()));
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
