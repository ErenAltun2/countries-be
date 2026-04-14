package org.erenaltun.countriesbe.exception;

import org.erenaltun.countriesbe.util.constants.i18n.I18nConstants;

public class ContinentNotFoundException extends NotFoundException {
    public ContinentNotFoundException() {
        super(I18nConstants.CONTINENT_NOT_FOUND);
    }
}
