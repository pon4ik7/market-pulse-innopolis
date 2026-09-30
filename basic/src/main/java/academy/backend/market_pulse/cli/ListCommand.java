package academy.backend.market_pulse.cli;

import academy.backend.market_pulse.filter.ListUniversalFilter;
import academy.backend.market_pulse.filter.Operator;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.repository.InstrumentRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Command(name = "list", description = "Список инструментов")
public class ListCommand implements Callable<Integer> {

    @Option(names = "--type", description = "Фильтр по типу инструмента")
    private String type;

    @Option(names = "--ticker", description = "Фильтр по подстроке в тикере")
    private String ticker;

    @Option(names = "--currency", description = "Фильтр по валюте инструмента")
    private Currency currency;

    @Option(names = "--price-op", description = "Оператор сравнения цены: GE, LE или EQ")
    private Operator priceOperator;

    @Option(names = "--price", description = "Пороговое значение цены (дивидендная доходность акции)")
    private BigDecimal price;

    private final InstrumentRepository repository;

    public ListCommand(InstrumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer call() {
        ListUniversalFilter filter = new ListUniversalFilter();
        Predicate<Instrument> predicate = instrument -> true;
        if (type != null) {
            predicate = predicate.and(filter.byType(type));
        }
        if (ticker != null) {
            predicate = predicate.and(filter.byTicker(ticker));
        }
        if (currency != null) {
            predicate = predicate.and(filter.byCurrency(currency));
        }
        if (priceOperator != null && price != null) {
            predicate = predicate.and(filter.byPrice(priceOperator, price));
        }
        for (var instrument : repository) {
            if (predicate.test(instrument)) {
                System.out.println(instrument);
            }
        }
        return 0;
    }
}
