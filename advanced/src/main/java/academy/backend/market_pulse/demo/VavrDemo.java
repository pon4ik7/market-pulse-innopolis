package academy.backend.market_pulse.demo;

import academy.backend.market_pulse.factory.InstrumentFactories;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.repository.InMemoryInstrumentRepository;
import academy.backend.market_pulse.repository.InstrumentRepository;
import io.vavr.control.Try;

public class VavrDemo {

    public static void main(String[] args) {
        InstrumentRepository repository = new InMemoryInstrumentRepository();
        add(repository, "STOCK", "GAZP", "Газпром", Currency.RUB);
        add(repository, "STOCK", "GAZP", "Газпром", Currency.RUB);
        add(repository, "CRYPTO", "BTC", "Bitcoin", Currency.RUB);
    }

    private static void add(InstrumentRepository repository, String type, String ticker,
                            String name, Currency currency) {
        Try.of(() -> InstrumentFactories.create(type, ticker, name, currency))
                .andThenTry(repository::add)
                .onSuccess(instrument -> System.out.println("Добавлено: " + instrument.getDescription()))
                .onFailure(error -> System.out.println("Не удалось добавить инструмент: " + error.getMessage()));
    }
}
