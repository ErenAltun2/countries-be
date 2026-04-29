package org.erenaltun.countriesbe.service.interfaces;

import java.util.Map;

public interface ICurrencyService {
    /**
     * Verilen para biriminin TRY karşılığını getirir.
     * @param currencyCode Örn: USD, EUR
     * @return TRY cinsinden değer
     */
    Double getRateToTry(String currencyCode);

    /**
     * En değerli 5 para birimini TRY karşılıklarıyla birlikte getirir.
     * @return Map<Para Birimi Kodu, TRY Değeri>
     */
    Map<String, Double> getTop5Currencies();
}
