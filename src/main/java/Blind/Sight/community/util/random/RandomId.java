package Blind.Sight.community.util.random;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RandomId {
    private static Random random;
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private RandomId(){}

    // you can use new random instead of using setter injection (your option)
    public static void setRandom(Random random) {
        RandomId.random = random;
    }

    /**
     * Generate next value increment
     *
     * @param name - using name combine with value
     * @return - string
     */
    public static String generateCounterIncrement(String name) {
        int nextId = idCounter.getAndIncrement();
        String time = LocalDateTime.now().toString();
        return name + nextId + time;
    }

    /**
     * Generate next value increment
     *
     * @param length - using name combine with value
     * @return - string
     */
    public static String generateUUIDString(int length) {
        String randomString = UUID.randomUUID().toString().replace("-", "");
        return randomString.substring(0, length);
    }
}
