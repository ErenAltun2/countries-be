package org.erenaltun.countriesbe.exception;

import org.erenaltun.countriesbe.util.constants.i18n.I18nConstants;

public class CountryNotFoundException  extends NotFoundException{

    public CountryNotFoundException(){
        super(I18nConstants.COUNTRY_NOT_FOUND);
    }
}
