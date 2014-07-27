package com.linchproject.linch.controllers;

import com.linchproject.core.Result;
import com.linchproject.core.actions._Action;
import com.linchproject.framework.Controller;

/**
 * @author Georg Schmidl
 */
public class AssetsController extends Controller implements _Action {

    @Override
    public Result _Action() {
        return asset();
    }
}
