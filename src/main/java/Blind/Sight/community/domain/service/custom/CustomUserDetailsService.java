package Blind.Sight.community.domain.service.custom;

import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepositoryPg userRepositoryPg;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepositoryPg.findUserByEmail(email).orElseThrow(
                () -> {
                    log.error("Not found user: " + email);
                    return new UsernameNotFoundException("Not found this user: " + email);
                }
        );
        log.info("load user success: {}", user);
        return user;
    }
}
