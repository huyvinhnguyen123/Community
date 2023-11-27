package Blind.Sight.community.util.random;

import java.util.concurrent.atomic.AtomicInteger;

public class RandomOther {
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private RandomOther(){}

    public static int increment() {
        return idCounter.getAndIncrement();
    }
}
