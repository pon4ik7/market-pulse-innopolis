package academy.backend.market_pulse.demo;

import academy.backend.market_pulse.cli.AddCommand;
import academy.backend.market_pulse.cli.ListCommand;
import academy.backend.market_pulse.cli.MarketPulseCli;
import academy.backend.market_pulse.cli.SearchCommand;
import academy.backend.market_pulse.repository.InMemoryInstrumentRepository;
import academy.backend.market_pulse.repository.InstrumentRepository;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Точка входа CLI в режиме REPL (Read-Eval-Print Loop). Команды регистрируются вручную через
 * {@code addSubcommand}, а не через атрибут {@code subcommands} аннотации {@code @Command}: у команд
 * нет конструктора без аргументов, picocli не смог бы создать их самостоятельно по одному классу —
 * каждой нужен уже готовый {@code repository}.
 * <p>
 * Пользователь вводит команду целиком одной строкой (например {@code search AAPL}), строка
 * разбивается на аргументы и передаётся в {@code CommandLine}. Цикл повторяется до тех пор, пока
 * не будет введена команда {@code exit} (или {@code quit}), либо пока stdin не будет закрыт.
 */
public class Main {

    public static void main(String[] args) {
        InstrumentRepository repository = new InMemoryInstrumentRepository();
        CommandLine cli = new CommandLine(new MarketPulseCli())
                .addSubcommand(new SearchCommand(repository))
                .addSubcommand(new AddCommand(repository))
                .addSubcommand(new ListCommand(repository));

        repl(cli, new Scanner(System.in), new PrintWriter(System.out, true));
    }

    /**
     * Основной цикл REPL.
     *
     * @param cli    сконфигурированная командная строка picocli
     * @param stdin  источник строк пользователя
     * @param stdout куда выводить приглашение и результаты
     */
    static void repl(CommandLine cli, Scanner stdin, PrintWriter stdout) {
        stdout.println("REPL запущен. Введите команду или 'exit' для выхода.");
        stdout.print("> ");
        stdout.flush();

        while (stdin.hasNextLine()) {
            String line = stdin.nextLine().trim();
            if (line.isEmpty()) {
                stdout.print("> ");
                stdout.flush();
                continue;
            }

            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                stdout.println("До свидания!");
                break;
            }

            // Пишем результат команд в тот же поток, что и приглашение.
            PrintWriter originalOut = cli.getOut();
            cli.setOut(stdout);
            try {
                cli.execute(splitArgs(line));
            } finally {
                cli.setOut(originalOut);
            }

            stdout.print("> ");
            stdout.flush();
        }
    }

    /**
     * Примитивная разбивка строки на аргументы по пробелам. Входные данные предназначены для
     * интерактивного ввода, поэтому поддержки кавычек и экранирования не добавляется.
     */
    private static String[] splitArgs(String line) {
        return Arrays.stream(line.split("\\s+"))
                .filter(part -> !part.isEmpty())
                .toArray(String[]::new);
    }
}