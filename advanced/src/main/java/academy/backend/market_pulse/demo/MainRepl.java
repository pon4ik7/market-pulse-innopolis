package academy.backend.market_pulse.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import academy.backend.market_pulse.cli.AddCommand;
import academy.backend.market_pulse.cli.ListCommand;
import academy.backend.market_pulse.cli.MarketPulseCli;
import academy.backend.market_pulse.cli.QuoteCommand;
import academy.backend.market_pulse.cli.SearchCommand;
import academy.backend.market_pulse.cli.WatchCommand;
import academy.backend.market_pulse.factory.InstrumentFactories;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Quote;
import academy.backend.market_pulse.repository.InMemoryInstrumentRepository;
import academy.backend.market_pulse.repository.InstrumentRepository;
import academy.backend.market_pulse.service.QuoteService;
import academy.backend.market_pulse.service.QuoteSource;
import picocli.CommandLine;

/**
 * Точка входа интерактивного режима (REPL). В отличие от {@link Main}, команды не передаются
 * аргументами командной строки, а вводятся пользователем в консоль по одной за раз.
 *
 * <p>Поддерживаются те же команды, что и в CLI ({@code search}, {@code add}, {@code list}),
 * а также команды {@code help} и {@code exit}. Регистрация подкоманд выполняется вручную через
 * {@code addSubcommand} — так же, как в {@link Main}.</p>
 */
public class MainRepl {

    public static void main(String[] args) {
        InstrumentRepository repository = new InMemoryInstrumentRepository();
        QuoteService quoteService = new QuoteService(new QuoteSource(sampleQuotes()));
        CommandLine cli = new CommandLine(new MarketPulseCli())
                .addSubcommand(new SearchCommand(repository))
                .addSubcommand(new AddCommand(repository))
                .addSubcommand(new ListCommand(repository))
                .addSubcommand(new WatchCommand())
                .addSubcommand(new QuoteCommand(quoteService));

        printHelp();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("market-pulse> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }

                String[] tokens = input.split("\\s+");
                String command = tokens[0].toLowerCase(Locale.ROOT);
                String[] commandArgs = tokens.length >= 1
                        ? Arrays.copyOfRange(tokens, 0, tokens.length)
                        : new String[0];

                switch (command) {
                    case "exit", "quit" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    case "help" -> printHelp();
                    case "search", "add", "list", "watch", "quote" -> cli.execute(commandArgs);
                    default -> System.out.println("Неизвестная команда: " + command
                            + " (используйте help для списка команд)");
                }
            }
        }
    }

    private static void printHelp() {
        System.out.println("Интерактивный режим market-pulse. Доступные команды:");
        System.out.println("  search <тикер>               — поиск инструмента по тикеру");
        System.out.println("  add <тип> <тикер> <название> <валюта> — добавление инструмента");
        System.out.println("  list [--type ...] [--ticker ...] [--currency ...] [--price-op ...] [--price ...] — список инструментов");
        System.out.println("  quote <тикер> [<тикер> ...]  — получить котировки по одному или нескольким тикерам");
        System.out.println("  help                         — показать эту справку");
        System.out.println("  exit | quit                  — завершить работу");
        System.out.println();
    }

    /**
     * Заглушка внешнего источника котировок — заранее заготовленные данные вместо реального
     * сетевого запроса (реальный источник появится на семинаре 7).
     */
    private static Map<String, Quote> sampleQuotes() {
        Instrument sber = InstrumentFactories.create("STOCK", "SBER", "Сбербанк", Currency.RUB);
        Instrument gazp = InstrumentFactories.create("STOCK", "GAZP", "Газпром", Currency.RUB);
        Instrument yan = InstrumentFactories.create("STOCK", "YAND", "Яндекс", Currency.RUB);
        Instrument alpha = InstrumentFactories.create("STOCK", "Aplha", "Альфа", Currency.RUB);
        Instrument x5 = InstrumentFactories.create("STOCK", "X5", "X5 RG", Currency.RUB);
        Instrument rtf = InstrumentFactories.create("BOND", "RTF", "Российская что-то там", Currency.RUB);
        return Map.of(
                "SBER", new Quote(sber, new BigDecimal("250.00"), new BigDecimal("1.20")),
                "GAZP", new Quote(gazp, new BigDecimal("120.50"), new BigDecimal("-0.80")),
                "YAND", new Quote(yan, new BigDecimal("150.75"), new BigDecimal("0.50")),
                "ALPHA", new Quote(alpha, new BigDecimal("100.00"), new BigDecimal("0.00")),
                "X5", new Quote(x5, new BigDecimal("100.00"), new BigDecimal("0.00")),
                "RTF", new Quote(rtf, new BigDecimal("10000.00"), new BigDecimal("0.00")));
    }
}