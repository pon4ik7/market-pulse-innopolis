package academy.backend.market_pulse.filter;

import java.math.BigDecimal;

import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Stock;

public class PriceFilter implements InstrumentFilter {

    public enum Operator {
        GE,
        LE,
        EQ
    }

    private final Operator operator;
    private final BigDecimal price;

    public PriceFilter(Operator operator, BigDecimal price) {
        this.operator = operator;
        this.price = price;
    }

    @Override
    public boolean matches(Instrument instrument) {
        if (!(instrument instanceof Stock)) {
            return false;
        }

        Stock stock = (Stock) instrument;
        int comparison = stock.getDividendYield().compareTo(price);
        if (operator == Operator.GE) {
            return comparison >= 0;
        }
        if (operator == Operator.LE) {
            return comparison <= 0;
        }
        return comparison == 0;
    }
}
