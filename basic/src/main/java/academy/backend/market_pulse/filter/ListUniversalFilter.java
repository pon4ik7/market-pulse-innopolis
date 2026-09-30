package academy.backend.market_pulse.filter;

import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Stock;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class ListUniversalFilter {

    private Predicate<Instrument> filter = instrument -> true;

    public Predicate<Instrument> byCurrency(Currency currency) {
        return instrument -> instrument.getCurrency() == currency;
    }

    public Predicate<Instrument> byPrice(Operator operator, BigDecimal threshold) {
        return instrument -> {
            if (!(instrument instanceof Stock stock)) {
                return false;
            }
            int comparison = stock.getDividendYield().compareTo(threshold);
            return switch (operator) {
                case GREATER_THAN -> comparison >= 0;
                case LESS_THAN -> comparison <= 0;
                case EQUALS -> comparison == 0;
            };
        };
    }

    public Predicate<Instrument> byTicker(String query) {
        return instrument -> instrument.getTicker().toUpperCase().contains(query.toUpperCase());
    }

    public Predicate<Instrument> byType(String type) {
        return instrument -> instrument.getType().equalsIgnoreCase(type);
    }
}
