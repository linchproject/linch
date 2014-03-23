package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.forms.Form;
import com.linchproject.forms.Validator;
import com.linchproject.forms.validators.RequiredValidator;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.I18nForm;
import com.linchproject.linch.entities.Group;
import com.linchproject.linch.entities.User;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class GroupsController extends AdministratorController {


    public Result index(Params params) {
        return render("admin/groups/index", context()
                .put("navGroups", true)
                .put("groups", groupDao.findAll()));
    }

    public Result view(Params params) {
        Group group = groupDao.findByGroupname(params.get("groupname"));
        List<User> members = groupDao.getMembers(group);
        return render("admin/groups/view", context()
                .put("navGroups", true)
                .put("group", group)
                .put("members", members));
    }

    public Result create(Params params) {
        Result result;

        Form form = new I18nForm(getI18n())
                .addField("groupname", new RequiredValidator(), new GroupExistsValidator());

        if (params.get("submit") != null) {
            form.bind(params.getMap()).validate();

            if (form.isValid()) {
                Group group = new Group();
                group.setGroupname(form.get("groupname").getValue());
                groupDao.save(group);

                result = redirect("view?groupname=" + group.getGroupname());
            } else {
                result = render("admin/groups/create", context()
                        .put("navGroups", true)
                        .put("form", form));
            }

        } else {
            result = render("admin/groups/create", context()
                    .put("navGroups", true)
                    .put("form", form));
        }
        return result;
    }

    public Result delete(Params params) {
        Result result;
        Group group = groupDao.findByGroupname(params.get("groupname"));

        if (params.get("submit") != null) {
            groupDao.delete(group);
            result = redirect("index");

        } else {
            result = render("admin/groups/delete", context()
                    .put("navGroups", true)
                    .put("group", group));
        }
        return result;
    }

    public Result addMember(Params params) {
        Result result;
        Group group = groupDao.findByGroupname(params.get("groupname"));

        Form form = new I18nForm(getI18n())
                .addField("username", new RequiredValidator(), new UserNotExistsValidator());

        if (params.get("submit") != null) {
            form.bind(params.getMap()).validate();

            if (form.isValid()) {
                User user = userDao.findByUsername(form.get("username").getValue());
                groupDao.addMember(group, user);

                result = redirect("view?groupname=" + group.getGroupname());
            } else {
                result = render("admin/groups/addMember", context()
                        .put("navGroups", true)
                        .put("group", group)
                        .put("form", form));
            }

        } else {
            result = render("admin/groups/addMember", context()
                    .put("navGroups", true)
                    .put("group", group)
                    .put("form", form));
        }
        return result;
    }

    public Result removeMember(Params params) {
        Group group = groupDao.findByGroupname(params.get("groupname"));
        User user = userDao.findByUsername(params.get("username"));
        groupDao.removeMember(group, user);

        return redirect("view?groupname=" + group.getGroupname());
    }

    public class GroupExistsValidator implements Validator {
        @Override
        public String getErrorKey() {
            return "group.exists";
        }

        @Override
        public boolean isValid(String[] values, Form form) {
            String groupname = values[0];
            Group group = groupDao.findByGroupname(groupname);
            return group == null;
        }
    }

    public class UserNotExistsValidator implements Validator {
        @Override
        public String getErrorKey() {
            return "user.not.exists";
        }

        @Override
        public boolean isValid(String[] values, Form form) {
            String username = values[0];
            User user = userDao.findByUsername(username);
            return user != null;
        }
    }
}
