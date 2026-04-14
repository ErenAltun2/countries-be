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
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                GenericResponse.builder().success(false).message(message).data(LocalDateTime.now().toString()).build()
        ); //:TODO ınternal server error kısmını duzelt
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericResponse<Object>>handleNotFound(NotFoundException ex){
        String message = messageService.getMessage(ex.getMessage(), LocaleContextHolder.getLocale());
        //LocaleContextHolder.getLocale()  bunu kullanarak otomatık olarak o esnadakı dil ne ise onu alıyoruz.
        //Normal akışta (Controller) parametre ile almak daha pratik, olağanüstü durumlarda (ExceptionHandler) ise Context'ten çekmek daha garantidir
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                GenericResponse.builder().success(false).message(message).data(LocalDateTime.now().toString()).build() //düzelt data kısmını
        );
    }
        //TODO :diğer hatalarıda ekle runtıme hatalarını vs.
}

