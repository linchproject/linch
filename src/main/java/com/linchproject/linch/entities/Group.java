package com.linchproject.linch.entities;

import com.linchproject.framework.db.Entity;

/**
 * @author Georg Schmidl
 */
public class Group extends Entity {

    private String groupname;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
