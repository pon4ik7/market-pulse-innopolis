package academy.backend.market_pulse.cli;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/**
 * Задача: CLI-команда {@code watch} — добавить тикер в список отслеживания (если указан
 * {@code --ticker}) или показать текущий список отслеживаемых инструментов (без опции).
 */
@Command(name = "watch", description = "Список отслеживаемых инструментов")
public class WatchCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        return 0;
    }
}
