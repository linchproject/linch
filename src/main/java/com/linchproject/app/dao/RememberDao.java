package com.linchproject.app.dao;

import com.linchproject.app.models.Remember;

/**
 * @author Georg Schmidl
 */
public interface RememberDao extends Dao<Remember, Long> {

    Remember findByUserId(Long userId);

    Remember findByUuid(String uuid);
}
