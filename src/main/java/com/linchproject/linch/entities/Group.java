package com.linchproject.linch.entities;

import com.linchproject.framework.db.Entity;

/**
 * @author Georg Schmidl
 */
public class Group extends Entity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
