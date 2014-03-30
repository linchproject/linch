package com.linchproject.linch.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.Route;
import com.linchproject.linch.App;
import com.linchproject.linch.Controller;
import com.linchproject.linch.dao.SettingDao;
import com.linchproject.linch.entities.Setting;
import com.linchproject.linch.services.AppService;

/**
 * @author Georg Schmidl
 */
public class AppController extends Controller {

    protected SettingDao settingDao;
    protected AppService appService;

    public Result index(Params params) {
        Setting indexPathSetting = settingDao.findByKey("indexPath");
        if (indexPathSetting != null) {
            String indexPath = indexPathSetting.getValue();
            Route route = this.route.copy();

            App app = appService.getApp(indexPath);
            if (app != null) {
                route.setControllerPackage(app.getAppPackage() + ".controllers");
            } else {
                route.setPath(indexPath);
            }

            return dispatch(route);
        }

        return render(context().put("hello", "Hello Linch"));
    }

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
