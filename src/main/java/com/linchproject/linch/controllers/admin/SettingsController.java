package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.actions.EditAction;
import com.linchproject.linch.dao.SettingDao;
import com.linchproject.linch.entities.Setting;
import com.linchproject.validator.Data;
import com.linchproject.validator.DataValidator;

/**
 * @author Georg Schmidl
 */
public class SettingsController extends AdministratorController implements IndexAction, EditAction {

    protected SettingDao settingDao;

    public Result indexAction() {
        return render(context()
                .put("indexPath", settingDao.findByKey("indexPath")));
    }

    @Override
    public Result editAction() {
        Data data = new SettingsDataValidator().emptyData();

        final Setting indexPath = settingDao.findByKey("indexPath");
        if (indexPath != null) {
            data.set("indexPath", indexPath.getValue());
        }

        return render(context()
                .put("data", data));
    }

    @Override
    public Result doEditAction() {
        Data data = new SettingsDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            Setting indexPath = settingDao.findByKey("indexPath");
            if (indexPath == null) {
                indexPath = new Setting();
                indexPath.setKey("indexPath");
            }
            indexPath.setValue(data.<String>get("indexPath"));
            settingDao.save(indexPath);

            return redirect("index");
        }

        return render("edit", context()
                .put("data", data));
    }

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }

    public class SettingsDataValidator extends DataValidator {
        public SettingsDataValidator() {
            addField("indexPath");
            setAllRequired();
        }
    }
}
