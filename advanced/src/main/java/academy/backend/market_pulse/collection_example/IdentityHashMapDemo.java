package academy.backend.market_pulse.collection_example;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Демонстрация {@link IdentityHashMap}: в отличие от обычного {@link HashMap}, ключи
 * сравниваются по ссылке ({@code ==}), а не по {@code equals()}/{@code hashCode()}.
 *
 * <p>Два разных экземпляра, которые «равны» по {@code equals()}, для {@code IdentityHashMap}
 * являются разными ключами. Применяется, когда важна именно идентичность объекта, а не его
 * значение — например, для сериализации/десериализации ссылок или ведения карт-счётчиков
 * по конкретным объектам.
 */
public class IdentityHashMapDemo {

    public static void main(String[] args) {
        String key1 = new String("KEY");
        String key2 = new String("KEY"); // равно key1 по equals(), но это другой объект

        Map<String, String> hashMap = new HashMap<>();
        Map<String, String> identityMap = new IdentityHashMap<>();

        hashMap.put(key1, "value");
        hashMap.put(key2, "other");

        identityMap.put(key1, "value");
        identityMap.put(key2, "other");

        System.out.println("key1.equals(key2) = " + key1.equals(key2));
        System.out.println("key1 == key2      = " + (key1 == key2));
        System.out.println();

        System.out.println("HashMap (equals): size = " + hashMap.size());
        System.out.println("  key1 -> " + hashMap.get(key1));
        System.out.println("  key2 -> " + hashMap.get(key2));
        System.out.println();

        System.out.println("IdentityHashMap (==): size = " + identityMap.size());
        System.out.println("  key1 -> " + identityMap.get(key1));
        System.out.println("  key2 -> " + identityMap.get(key2));
    }
}