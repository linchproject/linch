package com.linchproject.linch;

import com.linchproject.forms.Form;
import com.linchproject.forms.Texter;
import com.linchproject.framework.i18n.I18n;

/**
 * @author Georg Schmidl
 */
public class I18nForm extends Form {

    public I18nForm(final I18n i18n) {
        super(new Texter() {

            @Override
            public String getText(String fieldName, String validatorKey) {
                return i18n.getText("linch.error." + validatorKey);
            }
        });
    }
}
