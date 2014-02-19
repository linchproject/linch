package com.linchproject.app;

/**
 * @author Georg Schmidl
 */
public abstract class AdministratorController extends SecureController {

    @Override
    protected boolean isPermitted() {
        return super.isPermitted() && "admin".equals(route.getUserId());
    }
}
