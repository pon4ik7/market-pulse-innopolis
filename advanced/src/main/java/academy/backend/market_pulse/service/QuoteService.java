package academy.backend.market_pulse.service;

/**
 * Задача: сервис получения котировок по тикерам. Запросы ставятся в очередь и обрабатываются
 * пакетно, результат кэшируется (ограниченный кэш с вытеснением самых старых записей),
 * повторные запросы берутся из кэша без обращения к источнику.
 */
public class QuoteService {

    public QuoteService(QuoteSource quoteSource) {

    }
}
