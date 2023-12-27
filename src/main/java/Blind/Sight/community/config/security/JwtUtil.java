package Blind.Sight.community.config.security;

import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.util.random.RandomString;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {
    private final String secretKey;
    private static final String REDIS_KEY_PREFIX = "jwt:";
    private static final String REDIS_KEY_BLACK_LIST = "black_list:";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public JwtUtil() {
        this.secretKey = RandomString.generateRandomString(10);
        log.info("Secret Key: {}",secretKey);
    }

    private void logTokenValue(String key) {
        String token = redisTemplate.opsForValue().get(key);
        if (token != null) {
            // Log the token value
            log.info("Token value for key [{}]: {}", key, token);
        } else {
            // Key doesn't exist in Redis
            log.warn("Key {} doesn't exist in Redis", key);
        }
    }

    public void setBlackListToken(String key) {
        String token = redisTemplate.opsForValue().get(key);
        String tokenBlackList = REDIS_KEY_BLACK_LIST + token;
        redisTemplate.opsForValue().set(tokenBlackList, "true");
        redisTemplate.expire(tokenBlackList,5, TimeUnit.SECONDS);
    }

    public String getBlackListToken(String key) {
        String token = redisTemplate.opsForValue().get(key);
        String tokenBlackList = REDIS_KEY_BLACK_LIST + token;
        return redisTemplate.opsForValue().get(tokenBlackList);
    }


    public String createToken(User user) {
        // create token with these fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",user.getUsername());
        claims.put("birthDate", user.getBirthDate().toString());
        claims.put("email",user.getEmail());
        claims.put("role",user.getRole());

        // store token & set expiration time in Redis
        String token = buildToken(claims, user.getEmail());
        String redisKey = REDIS_KEY_PREFIX + user.getEmail();
        redisTemplate.opsForValue().set(redisKey, token);
        redisTemplate.expire(redisKey,8, TimeUnit.HOURS);

        logTokenValue(redisKey);

        return token;
    }

    private String buildToken(Map<String, Object> claims, String subject) {
        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();
        // Add 8 hours
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        Date date = calendar.getTime();

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, userDetails.getUsername());
    }

    public Boolean validateToken(String token, User user) {
        final String email = extractEmail(token);
        String redisKey = REDIS_KEY_PREFIX + user.getEmail();

        // check if the token is existed in Redis
        Boolean exists = redisTemplate.hasKey(redisKey);

        return Boolean.TRUE.equals(exists) && email.equals(user.getEmail()) && !isTokenExpired(token);
    }
}
