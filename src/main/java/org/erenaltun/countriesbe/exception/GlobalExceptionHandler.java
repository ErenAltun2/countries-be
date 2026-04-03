package org.erenaltun.countriesbe.exception;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.service.interfaces.II18nMessageService;
import org.erenaltun.countriesbe.util.GenericResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice  //burdakı anatasyon "controlleradvıce" ıle bu dosya projenın en ust dızesınde gıbı her yerdekı hataları tutabılıyor
public class GlobalExceptionHandler {

    private final II18nMessageService messageService;

    @ExceptionHandler(AlreadyExistsException.class) //hangı tur exceptıonları yakalasın ona bakıyor CountryAlreadyExceptıon zaten AlreadyExceptıonun  oglu
    public ResponseEntity<GenericResponse<Object>>handleAlreadyExistsException(AlreadyExistsException ex){
        String message = messageService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GenericResponse.builder().success(false).message(message).data(LocaleContextHolder.getLocale().toString()).build()
        );
    }
}

