package academy.backend.market_pulse.repository;

import academy.backend.market_pulse.exception.DuplicateTickerException;
import academy.backend.market_pulse.model.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Задача: реализовать {@link InstrumentRepository} «в памяти» — хранить инструменты,
 * добавлять новые (с защитой от дубликатов по тикеру), искать по тикеру и перебирать список.
 * Тикеры трактуются без учёта регистра; при старте подставляются демо-данные для CLI.
 */
public class InMemoryInstrumentRepository implements InstrumentRepository {

    private final Map<String, Instrument> instruments = new HashMap<>();

    public InMemoryInstrumentRepository() {
        // Стартовые данные для демонстрации CLI: без них каждый запуск начинается с пустого списка.
        add(new Stock("SBER", "Сбер", Currency.RUB, "Финансы", new BigDecimal("6.5")));
        add(new Bond("OFZ26233", "ОФЗ 26233", Currency.RUB, new BigDecimal("8.5"), 2036));
        add(new Etf("FXUS", "FinEx FXUS", Currency.USD, "S&P 500"));
    }

    @Override
    public void add(Instrument instrument) {
        if (findByTicker(instrument.getTicker()).isPresent()) {
            throw new DuplicateTickerException(instrument.getTicker());
        }
        instruments.put(instrument.getTicker().toUpperCase(), instrument);
    }
    @Override
    public Optional<Instrument> findByTicker(String ticker) {
        return Optional.ofNullable(instruments.get(ticker.toUpperCase()));
    }

    @Override
    public Iterator<Instrument> iterator() {
        return instruments.values().iterator();
    }
}
