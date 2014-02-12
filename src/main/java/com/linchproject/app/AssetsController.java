package com.linchproject.app;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

import java.io.InputStream;

/**
 * @author Georg Schmidl
 */
public class AssetsController extends Controller {

    public Result _all(Params params) {
        String fileName = "/assets/" + route.getAction() + (route.getTail() != null? "/" + route.getTail(): "");
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        return binary(inputStream);
    }
}
