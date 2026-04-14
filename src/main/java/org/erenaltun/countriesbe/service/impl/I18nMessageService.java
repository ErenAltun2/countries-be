package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.service.interfaces.II18nMessageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class I18nMessageService implements II18nMessageService {
    //Bana şu anahtarın, şu dildeki karşılığını getir dememızı sağlar

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Locale locale , Object... args){
        return messageSource.getMessage(code,args,locale);
    }

}
