package academy.backend.market_pulse.factory;

import java.math.BigDecimal;
import java.time.Year;

import academy.backend.market_pulse.model.Bond;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;

public class BondFactory implements InstrumentFactory {

    static {
        InstrumentFactories.register("BOND", new BondFactory());
    }

    @Override
    public Instrument create(String ticker, String name, Currency currency) {
        // TODO: создать Bond(ticker, name, currency, couponRate, maturityYear) — couponRate/
        // maturityYear через CLI пока не собираются, использовать значения по умолчанию.
        return new Bond(ticker, name, currency, BigDecimal.ZERO, Year.now().getValue());
    }
}
