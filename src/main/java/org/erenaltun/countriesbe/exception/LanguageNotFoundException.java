package org.erenaltun.countriesbe.exception;

import static org.erenaltun.countriesbe.util.constants.i18n.I18nConstants.LANGUAGE_NOT_FOUND;

public class LanguageNotFoundException extends NotFoundException {
    public LanguageNotFoundException() {
        super(LANGUAGE_NOT_FOUND);
    }
}
