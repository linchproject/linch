package com.linchproject.app.controllers;

import com.linchproject.mvc.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

import java.io.InputStream;

/**
 * @author Georg Schmidl
 */
public class AssetsController extends Controller {

    @Override
    public Result _filter(Params params) {
        String fileName = "/assets/" + route.getAfterController();
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        return binary(inputStream);
    }
}
