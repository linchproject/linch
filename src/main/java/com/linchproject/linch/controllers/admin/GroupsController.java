package com.linchproject.linch.controllers.admin;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.linch.AdministratorController;
import com.linchproject.linch.actions.CreateAction;
import com.linchproject.linch.actions.DeleteAction;
import com.linchproject.linch.actions.ViewAction;
import com.linchproject.linch.entities.Group;
import com.linchproject.linch.entities.User;
import com.linchproject.validator.Data;
import com.linchproject.validator.DataValidator;
import com.linchproject.validator.Validator;
import com.linchproject.validator.Value;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public class GroupsController extends AdministratorController implements IndexAction, ViewAction, CreateAction, DeleteAction {

    public Result indexAction() {
        return render(context()
                .put("groups", groupDao.findAll()));
    }

    @Override
    public Result viewAction() {
        Group group = groupDao.findByGroupname(route.getParams().get("groupname"));
        List<User> members = groupDao.getMembers(group);
        return render(context()
                .put("group", group)
                .put("members", members));
    }

    @Override
    public Result createAction() {
        return render(context()
                .put("data", new CreateGroupDataValidator().emptyData()));
    }

    @Override
    public Result doCreateAction() {
        Data data = new CreateGroupDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            Group group = new Group();
            data.writeTo(group);
            groupDao.save(group);

            return redirect("view?groupname=" + group.getGroupname());
        }

        return render("create", context()
                .put("data", data));
    }

    @Override
    public Result deleteAction() {
        Group group = groupDao.findByGroupname(route.getParams().get("groupname"));

        return render(context()
                .put("group", group));
    }

    @Override
    public Result doDeleteAction() {
        Group group = groupDao.findByGroupname(route.getParams().get("groupname"));
        groupDao.delete(group);

        return redirect("index");
    }

    public Result addMemberAction() {
        Group group = groupDao.findByGroupname(route.getParams().get("groupname"));

        return render(context()
                .put("group", group)
                .put("data", new AddMemberDataValidator().emptyData()));
    }

    public Result doAddMemberAction() {
        Group group = groupDao.findByGroupname(route.getParams().get("groupname"));

        Data data = new AddMemberDataValidator().dataFrom(route.getParameterMap()).validate();

        if (data.isValid()) {
            User user = userDao.findByUsername(data.<String>get("username"));
            groupDao.addMember(group, user);

            return redirect("view?groupname=" + group.getGroupname());
        }

        return render("addMember", context()
                .put("group", group)
                .put("data", data));
    }

    public Result removeMemberAction() {
        Params params = route.getParams();
        Group group = groupDao.findByGroupname(params.get("groupname"));
        User user = userDao.findByUsername(params.get("username"));
        groupDao.removeMember(group, user);

        return redirect("view?groupname=" + group.getGroupname());
    }

    public class CreateGroupDataValidator extends DataValidator {
        public CreateGroupDataValidator() {
            addFields(Group.class);
            setAllRequired();
            addValidator("groupName", new GroupExistsValidator());
        }

        public class GroupExistsValidator implements Validator {

            @Override
            public String getKey() {
                return "group.exists";
            }

            @Override
            public boolean isValid(Value value) {
                Group group = groupDao.findByGroupname(value.getString());
                return group == null;
            }
        }
    }

    public class AddMemberDataValidator extends DataValidator {
        public AddMemberDataValidator() {
            addField("username");
            setAllRequired();
            addValidator("username", new UserNotExistsValidator());
        }

        public class UserNotExistsValidator implements Validator {

            @Override
            public String getKey() {
                return "user.not.exists";
            }

            @Override
            public boolean isValid(Value value) {
                User user = userDao.findByUsername(value.getString());
                return user != null;
            }
        }
    }
}
