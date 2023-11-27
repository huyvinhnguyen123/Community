package Blind.Sight.community.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncrypt {
    private PasswordEncrypt() {}
    public static String bcryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
