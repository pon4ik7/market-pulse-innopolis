package academy.backend.market_pulse.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConcurrentModExceptionDemo {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        for (var elem : list) {
            list.removeIf(e -> e < 0);
        }

        var iterator = list.listIterator();
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next % 3 == 0) {
                iterator.remove();
            } else {
                iterator.add(next * 2);
            }
        }
        System.out.println(list);
    }
}
