package com.linchproject.app.dao;

import java.util.List;

/**
 * @author Georg Schmidl
 */
public interface Dao<T, U> {

    T findById(U id);

    List<T> findAll();

    void save(T model);
}
