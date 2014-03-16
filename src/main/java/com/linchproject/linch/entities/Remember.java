package com.linchproject.linch.entities;

import com.linchproject.framework.db.Entity;

/**
 * @author Georg Schmidl
 */
public class Remember extends Entity {

    String uuid;
    Long userId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
