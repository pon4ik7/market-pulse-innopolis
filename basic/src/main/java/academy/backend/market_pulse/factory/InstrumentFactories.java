package academy.backend.market_pulse.factory;

import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.util.Registry;

import java.util.ServiceLoader;

/**
 * Реестр фабрик инструментов. До семинара 3 держался на {@code Map<String, InstrumentFactory>}
 * напрямую — теперь это частный случай обобщённого {@link Registry}{@code <K, V>} (см. «План
 * семинара.md», семинар 3, этап 5). Реализации {@link InstrumentFactory} по-прежнему не
 * регистрируют себя вручную — они обнаруживаются через {@link ServiceLoader} по записи в
 * {@code META-INF/services/academy.backend.market_pulse.factory.InstrumentFactory}, но саму загрузку
 * теперь делает конструктор {@link Registry}, а не ручной цикл здесь.
 */
public final class InstrumentFactories {

    private static final Registry<String, InstrumentFactory> REGISTRY = load();

    private InstrumentFactories() {
    }

    public static Instrument create(String type, String ticker, String name, Currency currency) {
        return REGISTRY.get(type.toUpperCase())
                .map(factory -> factory.create(ticker, name, currency))
                .orElseThrow(() -> new IllegalArgumentException("Unknown instrument type: " + type));
    }

    private static Registry<String, InstrumentFactory> load() {
        return new Registry<>(factory -> factory.type().toUpperCase()) {
        };
    }
}
