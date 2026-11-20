package academy.backend.market_pulse.cli;

import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.repository.InstrumentRepository;
import academy.backend.market_pulse.util.InstrumentSortingUtils;
import academy.backend.market_pulse.util.InstrumentSortingUtils.SortField;
import academy.backend.market_pulse.util.InstrumentSortingUtils.SortOrder;
import academy.backend.market_pulse.util.ListFilterBuilder;
import academy.backend.market_pulse.util.ListFilterBuilder.PriceOperator;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @Option(names = "--price-op", converter = PriceOperatorConverter.class,
            description = "Оператор сравнения цены (GE, LE или EQ)")
    private PriceOperator priceOperator;

    @Option(names = "--price", description = "Пороговое значение цены (дивидендная доходность акции)")
    private BigDecimal price;

    @Option(names = "--sort-by", description = "Поле сортировки (TICKER, CURRENCY или PRICE)")
    private SortField sortBy;

    @Option(names = "--order", defaultValue = "ASC", description = "Порядок сортировки (ASC или DESC)")
    private SortOrder order;

    private final InstrumentRepository repository;

    public ListCommand(InstrumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer call() {
        if ((priceOperator == null && price != null) || (priceOperator != null && price == null)) {
            System.err.println("Укажи --price-op и --price вместе.");
            return ExitCode.USAGE;
        }

        Predicate<Instrument> filter = new ListFilterBuilder()
                .byType(type)
                .byTicker(ticker)
                .byCurrency(currency)
                .byPrice(priceOperator, price)
                .build();

        List<Instrument> matches = new ArrayList<>();
        for (Instrument instrument : repository) {
            if (filter.test(instrument)) {
                matches.add(instrument);
            }
        }
        if (sortBy != null) {
            InstrumentSortingUtils.sortWith(matches, InstrumentSortingUtils.comparator(sortBy, order));
        }
        for (Instrument instrument : matches) {
            System.out.println(instrument);
        }
        return 0;
    }

    public static class PriceOperatorConverter implements ITypeConverter<PriceOperator> {

        @Override
        public PriceOperator convert(String value) {
            switch (value) {
                case "GREATER_THAN":
                    return PriceOperator.GE;
                case "LESS_THAN":
                    return PriceOperator.LE;
                case "EQUALS":
                    return PriceOperator.EQ;
                default:
                    return PriceOperator.valueOf(value);
            }
        }
    }
}
