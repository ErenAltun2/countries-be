package org.erenaltun.countriesbe.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Configuration
public class I18nConfig {
//en onemlı yer bu sınıftır cunku yazmıs oldugum mesajları spring e sunu söyler eğer bırı senden mesaj ısterse messages ısımlı propertıes a bak der
    @Bean
    public LocaleResolver localeResolver(){
        var localeResolver = new AcceptHeaderLocaleResolver(); //http isteğin header kısmına bak dil ne ise onu yap
        var defaultLocale=new Locale("tr","TR"); //varsayılan bir dil olusturuyorum
        localeResolver.setDefaultLocale(defaultLocale); //eger kullıcı dıl belırtmedıyse veya bızde onun dılı yoksa default devam et
        return localeResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource(){
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages"); //message klasorunun nerede oldugunu verıyorum
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());  //format verıyorum
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator(){  //Hata Mesajlarını Kim Yazacak?
        var bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;          //verı eksık formatı hatalı gıbı hataları yazmaya yarar
    }

}
