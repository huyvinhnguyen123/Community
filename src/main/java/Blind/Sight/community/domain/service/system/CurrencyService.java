package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.entity.Currency;
import Blind.Sight.community.domain.repository.postgresql.CurrencyRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepositoryPg currencyRepositoryPg;

    public Currency createCurrency(String currencyName, String currencyCode, String symbol, Double exchangeRate,
                                   Double defaultCurrency, Boolean isDefaultCurrency) {
        Currency currency = new Currency();
        currency.setCurrencyName(currencyName);
        currency.setCurrencyCode(currencyCode);
        currency.setSymbol(symbol);
        currency.setExchangeRate(exchangeRate);
        currency.setDefaultCurrency(defaultCurrency);
        currency.setIsDefaultCurrency(isDefaultCurrency);
        currencyRepositoryPg.save(currency);

        log.info("Save currency success");
        return currency;
    }

    public void createDefaultCurrency() {
        List<Currency> currencies = new ArrayList<>();

        Currency vietnamCurrency = createCurrency("Vietnamese đồng", "VND", "₫", 0.0, 1.0, true);
        currencies.add(vietnamCurrency);

        Currency usaCurrency = createCurrency("United States dollar", "USD", "$", 24290.50, 1.0, false);
        currencies.add(usaCurrency);

        Currency euroCurrency = createCurrency("Euro", "EUR", "€", 26376.81, 1.0, false);
        currencies.add(euroCurrency);

        Currency japanCurrency = createCurrency("Japanese yen", "JPY", "¥", 160.61, 1.0, false);
        currencies.add(japanCurrency);

        Currency australianCurrency = createCurrency("Australian dollar", "AUD", "$", 15772.55, 1.0, false);
        currencies.add(australianCurrency);

        currencyRepositoryPg.saveAll(currencies);
        log.info("Create system default currencies success!");
    }
}
