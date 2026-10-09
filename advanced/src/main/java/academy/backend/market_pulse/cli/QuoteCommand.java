package academy.backend.market_pulse.cli;

import academy.backend.market_pulse.model.WatchModel;
import academy.backend.market_pulse.service.QuoteService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Задача: CLI-команда {@code quote} — получить котировки по одному или нескольким тикерам,
 * переданным позиционными аргументами, и вывести их (или сообщение об отсутствии данных).
 */
@Command(name = "quote", description = "Получить котировки по одному или нескольким тикерам")
public class QuoteCommand implements Callable<Integer> {

    @Parameters(index = "0", arity = "1..*", description = "Тикеры для получения котировок")
    private List<String> tickers;

    private final QuoteService quoteService;

    public QuoteCommand(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public Integer call() {
        if (tickers == null) {
            System.out.println("Не указан тикер");
            return 1;
        }
        for (var ticker : tickers) {
            quoteService.add(ticker);
        }
        quoteService.process();
        for (var ticker : tickers) {
            var quote = quoteService.get(ticker);
            quote.ifPresent(System.out::println);
        }
        return 0;
    }
}
