package org.erenaltun.countriesbe.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse <T>{
 // generıc response ıle aslında olusacak olan verıye etıketleme yapıyoruz success ve message dıyerek
    private boolean success;
    private String message;
    private T data;

}
