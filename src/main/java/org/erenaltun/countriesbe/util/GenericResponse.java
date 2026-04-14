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

    // <T> yazmanın anlamı
//   bazen data liste bazen string bazen CountryDto seklınde donuyor her bırı ıcın farklı sınıf olusturmak yerıne
    //generıc response sınıfına dıyorum kı bu data nın tıpını daha bılmıyorum kodu yazarken sana soyleyecegım söz verıyorum
    // bu yuzden class ın basında verdıgım soz ıcın oraya placeholder koyuyorum.
}
