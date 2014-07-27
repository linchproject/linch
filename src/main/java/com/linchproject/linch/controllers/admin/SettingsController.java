package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.forms.Form;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.I18nForm;
import com.linchproject.linch.actions.EditAction;
import com.linchproject.linch.dao.SettingDao;
import com.linchproject.linch.entities.Setting;

/**
 * @author Georg Schmidl
 */
public class SettingsController extends AdministratorController implements IndexAction, EditAction {

    protected SettingDao settingDao;

    @Override
    public Result indexAction() {
        return render(context()
                .put("navSettings", true)
                .put("indexPath", settingDao.findByKey("indexPath")));
    }

    @Override
    public Result editAction() {
        Form form = getEditForm();

        Setting indexPath = settingDao.findByKey("indexPath");
        if (indexPath != null) {
            form.put("indexPath", indexPath.getValue());
        }

        return render(context()
                .put("navSettings", true)
                .put("form", form));
    }

    @Override
    public Result doEditAction() {
        Form form = getEditForm();
        form.bind(route.getParameterMap()).validate();

        if (form.isValid()) {
            Setting indexPath = settingDao.findByKey("indexPath");
            if (indexPath == null) {
                indexPath = new Setting();
                indexPath.setKey("indexPath");
            }
            indexPath.setValue(form.get("indexPath").getValue());
            settingDao.save(indexPath);

            return redirect("index");
        }

        return render(context()
                .put("navSettings", true)
                .put("form", form));
    }

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }

    protected Form getEditForm() {
        return new I18nForm(getI18n())
                .addField("indexPath");
    }
}
