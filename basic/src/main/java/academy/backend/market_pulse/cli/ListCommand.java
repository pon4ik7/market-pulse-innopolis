package academy.backend.market_pulse.cli;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import academy.backend.market_pulse.filter.CurrencyFilter;
import academy.backend.market_pulse.filter.InstrumentFilter;
import academy.backend.market_pulse.filter.NoOpFilter;
import academy.backend.market_pulse.filter.PriceFilter;
import academy.backend.market_pulse.filter.TickerFilter;
import academy.backend.market_pulse.filter.TypeFilter;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.repository.InstrumentRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

@Command(name = "list", description = "Список инструментов")
public class ListCommand implements Callable<Integer> {

    @Option(names = "--type", description = "Фильтр по типу инструмента")
    private String type;

    @Option(names = "--ticker", description = "Фильтр по подстроке")
    private String ticker;

    @Option(names = "--currency", description = "Фильтр по валюте")
    private Currency currency;

    @Option(names = "--price-op", description = "Оператор сравнения (GE, LE или EQ)")
    private PriceFilter.Operator priceOperator;

    @Option(names = "--price", description = "Порог доходности акции (дивиденды)")
    private BigDecimal price;

    private final InstrumentRepository repository;

    public ListCommand(InstrumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer call() {
        // TODO: реализовать отбор по функциональным требованиям из Javadoc InstrumentFilter
        //  (ФТ1-ФТ7): реализовать InstrumentFilter под каждое правило (тип, тикер, валюта, цена),
        //  добавить недостающие @Option (--ticker, --currency, --price-op, --price) и вывести
        //  описания подходящих инструментов (этап Strategy).
        if ((priceOperator == null && price != null) || (priceOperator != null && price == null)) {
            System.err.println("Параметры --price-op и --price должны идти вместе");
            return ExitCode.USAGE;
        }

        InstrumentFilter filter = new NoOpFilter();
        int filterCount = 0;

        if (type != null) {
            filter = new TypeFilter(type);
            filterCount++;
        }
        if (ticker != null) {
            filter = new TickerFilter(ticker);
            filterCount++;
        }
        if (currency != null) {
            filter = new CurrencyFilter(currency);
            filterCount++;
        }
        if (price != null) {
            filter = new PriceFilter(priceOperator, price);
            filterCount++;
        }

        if (filterCount > 1) {
            System.err.println("Разрешается только один фильтр");
            return ExitCode.USAGE;
        }

        for (Instrument instrument : repository) {
            if (filter.matches(instrument)) {
                System.out.println(instrument.getDescription());
            }
        }
        return 0;
    }
}
