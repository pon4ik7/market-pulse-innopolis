package academy.backend.market_pulse.demo;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import academy.backend.market_pulse.cli.AddCommand;
import academy.backend.market_pulse.cli.ListCommand;
import academy.backend.market_pulse.cli.MarketPulseCli;
import academy.backend.market_pulse.cli.SearchCommand;
import academy.backend.market_pulse.repository.InMemoryInstrumentRepository;
import academy.backend.market_pulse.repository.InstrumentRepository;
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
        CommandLine cli = new CommandLine(new MarketPulseCli())
                .addSubcommand(new SearchCommand(repository))
                .addSubcommand(new AddCommand(repository))
                .addSubcommand(new ListCommand(repository));

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
                    case "search", "add", "list" -> cli.execute(commandArgs);
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
        System.out.println("  help                         — показать эту справку");
        System.out.println("  exit | quit                  — завершить работу");
        System.out.println();
    }
}