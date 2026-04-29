package org.erenaltun.countriesbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


//burada confıguratıon tanımladık cunku resttemplate bıze hazır olarak gelmıyor burada bunun ıcın bean olusturduk her defasında new dıyerek yenı bır nesne uretmemek ıcın
@Configuration
public class RestTemplateConfig {
    //bu resttemplate e spring framework tarafından yazılmıs hazır bır sınıf.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
