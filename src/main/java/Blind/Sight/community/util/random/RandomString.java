package Blind.Sight.community.util.random;

import java.util.Random;

public class RandomString {

    private static final Random random = new Random();

    private RandomString() {}

    /**
     * Generate random String can be used for password, key, secret
     *
     * @param len - input your len sight
     * @return - String
     */
    public static String generateRandomString(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                +"jklmnopqrstuvwxyz!@#$%&";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }

}
