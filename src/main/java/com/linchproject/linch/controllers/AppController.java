package com.linchproject.linch.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.linch.Controller;
import com.linchproject.linch.dao.SettingDao;
import com.linchproject.linch.entities.Setting;

/**
 * @author Georg Schmidl
 */
public class AppController extends Controller {

    protected SettingDao settingDao;

    public Result index(Params params) {
        Setting indexPath = settingDao.findByKey("indexPath");
        if (indexPath != null) {
            return dispatch(indexPath.getValue());
        }

        return render(context().put("hello", "Hello Linch"));
    }

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }
}
