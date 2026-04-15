package org.erenaltun.countriesbe.exception;

import org.aspectj.weaver.ast.Not;
import org.erenaltun.countriesbe.util.constants.i18n.I18nConstants;

public class CurrencyNotFoundException extends NotFoundException {
    public CurrencyNotFoundException() {
        super(I18nConstants.CURRENCY_NOT_FOUND);
    }
}
