package academy.backend.market_pulse.filter;

import java.util.Locale;

import academy.backend.market_pulse.model.Instrument;

public class TickerFilter implements InstrumentFilter {

    private final String query;

    public TickerFilter(String query) {
        this.query = query.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean matches(Instrument instrument) {
        return instrument.getTicker().toUpperCase(Locale.ROOT).contains(query);
    }
}
