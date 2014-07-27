package com.linchproject.linch.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public class AdminController extends Controller implements _Action {

    @Override
    public Result _Action() {
        return dispatch(route.addSubpackage("admin").shift());
    }
}
