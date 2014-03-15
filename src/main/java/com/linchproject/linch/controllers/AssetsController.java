package com.linchproject.linch.controllers;

import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.framework.Controller;

/**
 * @author Georg Schmidl
 */
public class AssetsController extends Controller {

    public Result _(Params params) {
        return asset();
    }
}
