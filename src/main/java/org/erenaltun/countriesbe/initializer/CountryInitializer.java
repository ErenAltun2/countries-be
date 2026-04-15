package org.erenaltun.countriesbe.initializer;

import lombok.extern.slf4j.Slf4j;
import org.erenaltun.countriesbe.entity.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.erenaltun.countriesbe.entity.CountryLanguage;
import org.erenaltun.countriesbe.entity.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CountryInitializer {

    public static List<Country> readCountry() {
        String root = System.getProperty("user.dir"); // bulundugumuz dosyanın kok dızınıne ulasıyoruz
        List<Country> countryList = new ArrayList<>();

        // SİHİRLİ DOKUNUŞ: Tekrar eden dilleri önlemek için hafıza haritası (Cache)
        Map<String, Language> languageCache = new HashMap<>();

        try {
            File countryJson = new File(root, "assests/countries.json");
            Map<String, Map<String, Object>> result = new ObjectMapper().readValue(countryJson, HashMap.class);

            for (String code : result.keySet()) {
                Map<String, Object> valueMap = result.get(code);
                String name = valueMap.get("name").toString();
                String nativeName = valueMap.get("native").toString();
                String continent = valueMap.get("continent").toString();
                String capital = valueMap.get("capital").toString();
                String currency = valueMap.get("currency").toString();

                int phone;
                try {
                    phone = Integer.parseInt(valueMap.get("phone").toString());
                } catch (NumberFormatException exception) {
                    phone = -1;
                }

                String flagUrl = generateFlagUrl(code);

                Country c = Country.builder()
                        .code(code)
                        .name(name)
                        .nativeName(nativeName)
                        .continent(continent)
                        .capital(capital)
                        .currency(currency)
                        .countryLanguages(new ArrayList<>())
                        .phone(phone)
                        .flag(flagUrl)
                        .build();

                // 2. JSON'daki dilleri alıp temizliyoruz
                String languagesRaw = valueMap.get("languages").toString();
                languagesRaw = languagesRaw.replace("[", "").replace("]", "").trim();

                if (!languagesRaw.isEmpty()) {
                    String[] langCodes = languagesRaw.split(",");

                    for (String langCode : langCodes) {
                        langCode = langCode.trim();

                        if (!languageCache.containsKey(langCode)) {
                            Language newLang = new Language();
                            newLang.setCode(langCode);
                            languageCache.put(langCode, newLang);
                        }

                        Language currentLang = languageCache.get(langCode);

                        // 3. KAVŞAK OBJESİNİ OLUŞTUR VE BAĞLA!
                        CountryLanguage kavsak = new CountryLanguage();
                        kavsak.setCountry(c);
                        kavsak.setLanguage(currentLang);

                        // Kavşağı ülkenin listesine ekle
                        c.getCountryLanguages().add(kavsak);
                    } // İÇTEKİ FOR DÖNGÜSÜNÜN KAPANIŞI
                } // IF BLOĞUNUN KAPANIŞI

                // Ülkeyi ana listeye ekle
                countryList.add(c);

            } // DIŞTAKİ FOR DÖNGÜSÜNÜN KAPANIŞI (Ülkeler)

        } catch (Exception ex) {
            System.out.println("Dosya işlemlerinde bir hata meydana geldi");
            log.error("Dosya işlemlerinde bir hata meydana geldi: exception detail: " + ex.getMessage());
        }

        return countryList;
    }

    private static String generateFlagUrl(String id) {
        return "http://aedemirsen.bilgimeclisi.com/country_flags/" + id + ".svg";
    }
}