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

    public QuoteCommand(QuoteService quoteService) {

    }

    @Override
    public Integer call() {
        //TODO
        return 0;
    }
}
