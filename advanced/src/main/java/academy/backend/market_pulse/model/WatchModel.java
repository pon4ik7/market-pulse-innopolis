package academy.backend.market_pulse.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Задача: модель списка отслеживания — хранить тикеры, которые пользователь хочет наблюдать.
 * Гарантирует отсутствие дубликатов (тикеры без учёта регистра) и отдаёт список наружу
 * только в неизменяемом виде.
 */
public final class WatchModel {

    private final Set<String> tickers = new LinkedHashSet<>();

    public boolean add(String ticker) {
        return tickers.add(ticker.toUpperCase());
    }

    public Set<String> getTickers() {
        return Collections.unmodifiableSet(tickers);
    }
}
