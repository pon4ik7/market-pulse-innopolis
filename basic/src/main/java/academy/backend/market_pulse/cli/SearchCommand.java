package academy.backend.market_pulse.cli;

import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.repository.InstrumentRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "search", description = "Поиск инструмента по тикеру")
public class SearchCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Тикер инструмента")
    private String ticker;

    private final InstrumentRepository repository;

    public SearchCommand(InstrumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer call() {
        Optional<Instrument> instrumentBox = repository.findByTicker(ticker);
        if (instrumentBox.isEmpty()) {
            System.out.println("Инструмент не найден: " + ticker);
            return 1;
        }
        System.out.println(instrumentBox.get().getDescription());
        return 0;
    }
}
