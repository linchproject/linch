package com.linchproject.linch.entities;

import com.linchproject.framework.db.Entity;

/**
 * @author Georg Schmidl
 */
public class Setting extends Entity {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
