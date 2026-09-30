package academy.backend.market_pulse.demo;

import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Три способа применить лямбду — до перехода к рефакторингу {@code ListCommand} (см. «План
 * семинара.md», семинар 3, этап 2): лямбда как параметр метода, лямбда как результат метода
 * и композиция функций. Пример нарочно не трогает реальный CLI — только домен проекта.
 */
public class LambdaBasicsDemo {

    static class DemoType {
        private final String name;

        public DemoType(String name) {
            System.out.println("Object created with name " + name);
            this.name = name;
        }
    }

    public static void main(String[] args) {

        DemoType empty = new DemoType("empty");
        var a = Optional.ofNullable(empty).orElse(new DemoType("default"));
        var b = Optional.ofNullable(empty).orElseGet(() -> new DemoType("supplier"));

        List<Instrument> instruments = List.of(
                new Stock("SBER", "Сбер", Currency.RUB, "Финансы", new BigDecimal("6.5")),
                new Stock("AAPL", "Apple", Currency.USD, "Технологии", new BigDecimal("0.5")),
                new Stock("YNDX", "Яндекс", Currency.RUB, "Технологии", BigDecimal.ZERO)
        );
        Collections.sort(instruments, Comparator.comparing(Instrument::getTicker));

        // 1. Лямбда как параметр метода: printAll ничего не знает о том, как форматировать
        // инструмент — это решает вызывающий код, передавая конкретную реализацию Function.
        printAll(instruments, instrument -> instrument.getTicker() + ": " + instrument.getDescription());

        // 2. Метод, возвращающий лямбду: byCurrency создаёт готовый Predicate под конкретную
        // валюту, ничего не проверяя сам по себе — просто конструирует функцию.
        Predicate<Instrument> isRub = byCurrency(Currency.RUB);
        System.out.println("SBER в рублях? " + isRub.test(instruments.get(0)));

        // 3. Композиция: Predicate.and() строит новый Predicate из двух существующих, не меняя
        // ни один из них — исходный isRub можно использовать и дальше как есть.
        Predicate<Instrument> isRubStock = isRub.and(instrument -> instrument instanceof Stock);
        instruments.stream()
                .filter(isRubStock)
                .forEach(instrument -> System.out.println("RUB-акция: " + instrument.getTicker()));
        List<Stock> lst = new ArrayList();
        lst.add(new Stock("SBER", "Сбер", Currency.RUB, "Финансы", new BigDecimal("6.5")));
        for (var obj : lst) {
            System.out.println(((Stock) obj).getDescription());
        }
        System.out.println(lst.getClass());
    }

    private static void printAll(List<Instrument> instruments, Function<Instrument, String> formatter) {
        instruments.forEach(instrument -> System.out.println(formatter.apply(instrument)));
    }

    private static Predicate<Instrument> byCurrency(Currency currency) {
        return instrument -> instrument.getCurrency() == currency;
    }
}
