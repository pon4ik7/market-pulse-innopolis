package academy.backend.market_pulse.cli;

import academy.backend.market_pulse.model.WatchModel;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/**
 * Задача: CLI-команда {@code watch} — добавить тикер в список отслеживания (если указан
 * {@code --ticker}) или показать текущий список отслеживаемых инструментов (без опции).
 */
@Command(name = "watch", description = "Список отслеживаемых инструментов")
public class WatchCommand implements Callable<Integer> {

    @Parameters(index = "0", arity = "0..1", description = "Тикер для отслеживания")
    private String ticker;

    private final WatchModel watchModel = new WatchModel();

    @Override
    public Integer call() {
        if (ticker != null) {
            var wasAdded = watchModel.add(ticker);
            if (wasAdded) {
                System.out.println("Добавлен тикер: " + ticker);
            } else {
                System.out.println("Тикер уже добавлен: " + ticker);
            }
        } else {
            var tickers = watchModel.getTickers();
            for (var ticker : tickers) {
                System.out.println(ticker);
            }
        }
        return 0;
    }
}
