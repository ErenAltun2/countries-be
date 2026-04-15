package org.erenaltun.countriesbe.exception;

import org.erenaltun.countriesbe.util.constants.i18n.I18nConstants;

public class PhoneCodeNotFoundException extends NotFoundException {
    public PhoneCodeNotFoundException() {
        super(I18nConstants.PHONE_CODE_NOT_FOUND);
    }
}
