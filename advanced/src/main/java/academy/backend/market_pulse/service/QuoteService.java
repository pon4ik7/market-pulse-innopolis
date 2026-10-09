package academy.backend.market_pulse.service;

import academy.backend.market_pulse.model.Quote;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * Задача: сервис получения котировок по тикерам. Запросы ставятся в очередь и обрабатываются
 * пакетно, результат кэшируется (ограниченный кэш с вытеснением самых старых записей),
 * повторные запросы берутся из кэша без обращения к источнику.
 */
public class QuoteService {
    private final int MAX_SIZE = 3;

    private final QuoteSource quoteSource;
    private final Queue<String> queue = new ArrayDeque<>();
    private final Map<String, Quote> cache = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Quote> eldest) {
            return size() > MAX_SIZE;
        }
    };

    public QuoteService(QuoteSource quoteSource) {
        this.quoteSource = quoteSource;
    }

    public void add(String ticker) {
        queue.add(ticker);
    }

    public void process() {
        while (!queue.isEmpty()) {
            var ticker = queue.poll();
            if (cache.containsKey(ticker)) {
                continue;
            }
            quoteSource.fetch(ticker).ifPresent(quote -> cache.put(ticker, quote));
        }
    }

    public Optional<Quote> get(String ticker) {
        return Optional.ofNullable(cache.get(ticker));
    }
}
