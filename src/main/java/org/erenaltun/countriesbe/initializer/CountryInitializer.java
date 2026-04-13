package org.erenaltun.countriesbe.initializer;

import lombok.extern.slf4j.Slf4j;
import org.erenaltun.countriesbe.entity.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CountryInitializer {
    public static List<Country> readCountry(){
        String root = System.getProperty("user.dir"); //bulundugumuz dosyanın kok dızınıne ulasıyoruz
        List<Country>countryList=new ArrayList<>();

        try {
            File countryJson = new File(root,"assests/countries.json");
            Map<String, Map<String,Object>> result = new ObjectMapper().readValue(countryJson, HashMap.class);
            for (String code : result.keySet()){
                Map<String,Object>valueMap=result.get(code);
                String name = valueMap.get("name").toString();
                String nativeName=valueMap.get("native").toString();
                String continent=valueMap.get("continent").toString();
                String capital=valueMap.get("capital").toString();
                String currency=valueMap.get("currency").toString();
                String languages=valueMap.get("languages").toString();
                int phone;
                try {
                     phone = Integer.parseInt(valueMap.get("phone").toString());
                }catch (NumberFormatException exception){
                    phone = -1;
                }
                String flagUrl=generateFlagUrl(code);
                Country c = Country.builder()
                        .code(code)
                        .name(name)
                        .nativeName(nativeName)
                        .continent(continent)
                        .capital(capital)
                        .currency(currency)
                        .language(languages)
                        .phone(phone)
                        .flag(flagUrl)
                        .build();
                countryList.add(c);            }

        }catch (Exception ex){
            System.out.println("dosya ıslemlerınde bır hata meydana geldı");
            log.error("dosya ıslemlerınde bır hata meydana geldı: exception detail"+ ex.getMessage());
        }
        return countryList;
    }

    private static String generateFlagUrl(String id){
        return "http://aedemirsen.bilgimeclisi.com/country_flags/"+id+".svg";
    }
}
