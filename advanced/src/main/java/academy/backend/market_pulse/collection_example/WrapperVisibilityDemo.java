package academy.backend.market_pulse.collection_example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Демонстрация важного свойства «иммутабельных» обёрток из {@link Collections}:
 * {@code unmodifiableXxx(...)} не копирует данные, а лишь запрещает изменять коллекцию
 * через саму обёртку. Исходная коллекция остаётся доступной, и любые изменения в ней
 * тут же «видны» через обёртку. Настоящей неизменности (как у {@code List.of(...)},
 * {@code Set.of(...)}) здесь нет.
 */
public class WrapperVisibilityDemo {

    public static void main(String[] args) {
        List<String> base = new ArrayList<>();
        base.add("A");
        base.add("B");

        // Обёртка: доступна только для чтения, но ссылается на ту же коллекцию.
        List<String> view = Collections.unmodifiableList(base);

        System.out.println("До изменения base:");
        System.out.println("  base = " + base);
        System.out.println("  view = " + view);

        // Меняем ИСХОДНУЮ коллекцию — обёртка тоже «изменилась».
        base.add("C");
        base.set(0, "X");
        base.remove("B");

        System.out.println("\nПосле изменения base:");
        System.out.println("  base = " + base);
        System.out.println("  view = " + view);

        // А вот запись через саму обёртку запрещена.
        try {
            view.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nЗапись через обёртку запрещена: " + e.getClass().getSimpleName());
        }
    }
}