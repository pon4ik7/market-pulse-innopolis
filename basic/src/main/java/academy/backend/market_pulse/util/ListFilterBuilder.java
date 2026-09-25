package academy.backend.market_pulse.util;

import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Stock;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Собирает итоговый {@code Predicate<Instrument>} для {@code ListCommand}: каждый непустой критерий
 * комбинируется в цепочку через {@code Predicate.and(...)}, а не заменяет предыдущий, поэтому
 * несколько опций {@code list} действуют одновременно (композиция в духе {@code Function.andThen}).
 */
public final class ListFilterBuilder {

    public enum PriceOperator {
        GE, LE, EQ
    }

    private Predicate<Instrument> filter = instrument -> true;

    public ListFilterBuilder byType(String type) {
        if (type != null) {
            filter = filter.and(instrument -> instrument.getType().equalsIgnoreCase(type));
        }
        return this;
    }

    public ListFilterBuilder byTicker(String ticker) {
        if (ticker != null) {
            filter = filter.and(instrument -> instrument.getTicker().toUpperCase().contains(ticker.toUpperCase()));
        }
        return this;
    }

    public ListFilterBuilder byCurrency(Currency currency) {
        if (currency != null) {
            filter = filter.and(instrument -> instrument.getCurrency() == currency);

            // Для сравнения байт-кода (этап 3.2, «План семинара.md»): та же самая проверка, написанная
            // через анонимный класс вместо лямбды. Чтобы увидеть разницу — закомментировать лямбду выше,
            // раскомментировать блок ниже, скомпилировать (javac) и найти в каталоге сборки новый файл
            // ListFilterBuilder$1.class, которого для лямбды не появляется.
            //
            // filter = filter.and(new Predicate<Instrument>() {
            //     @Override
            //     public boolean test(Instrument instrument) {
            //         return instrument.getCurrency() == currency;
            //     }
            // });
        }
        return this;
    }

    public ListFilterBuilder byPrice(PriceOperator operator, BigDecimal price) {
        if (operator != null) {
            filter = filter.and(instrument -> instrument instanceof Stock stock && matchesPrice(stock, operator, price));
        }
        return this;
    }

    public Predicate<Instrument> build() {
        return filter;
    }

    private static boolean matchesPrice(Stock stock, PriceOperator operator, BigDecimal price) {
        int comparison = stock.getDividendYield().compareTo(price);
        return switch (operator) {
            case GE -> comparison >= 0;
            case LE -> comparison <= 0;
            case EQ -> comparison == 0;
        };
    }
}
