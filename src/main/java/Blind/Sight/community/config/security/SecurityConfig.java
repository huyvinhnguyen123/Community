package Blind.Sight.community.config.security;

import Blind.Sight.community.config.security.exception.CustomAccessDeniedHandler;
import Blind.Sight.community.config.security.exception.CustomAuthenticationEntryPoint;
import Blind.Sight.community.domain.service.custom.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String PRE = "PRE";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("api/v1/create/admin/ZbGkKnmOqkllQIe9").hasAnyRole(ADMIN,PRE)
                        .requestMatchers("api/v0/types/insert").hasRole(ADMIN)
                        .requestMatchers("api/v0/currencies/insert").hasAnyRole(ADMIN)
                        .requestMatchers("api/v0/type/insert").hasRole(ADMIN)
                        .requestMatchers("api/v1/users").hasRole(ADMIN)
                        .requestMatchers("api/v1/admin/users/update").hasRole(ADMIN)
                        .requestMatchers("api/v1/admin/users/delete-image").hasRole(ADMIN)
                        .requestMatchers("api/v1/admin/users/delete-1").hasRole(ADMIN)
                        .requestMatchers("api/v1/admin/import").hasRole(ADMIN)
                        .requestMatchers("api/v1/admin/export").hasRole(ADMIN)
                        .requestMatchers("api/v1/admin/upload-pdf").hasRole(ADMIN)
                        .requestMatchers("api/v1/categories/create").hasRole(ADMIN)
                        .requestMatchers("api/v1/categories/update").hasRole(ADMIN)
                        .requestMatchers("api/v1/categories/delete").hasRole(ADMIN)
                        .requestMatchers("api/v1/products/create").hasRole(ADMIN)
                        .requestMatchers("api/v1/products/update").hasRole(ADMIN)
                        .requestMatchers("api/v1/products/delete").hasRole(ADMIN)
                        .requestMatchers("api/v1/products/delete").hasRole(ADMIN)

                        .requestMatchers("api/v1/users/user/update").hasRole(USER)
                        .requestMatchers("api/v1/users/lock-account").hasRole(USER)

                        .requestMatchers("api/v1/system/prepare").permitAll()
                        .requestMatchers("api/v1/register").permitAll()
                        .requestMatchers("api/v1/send-reset-password").permitAll()
                        .requestMatchers("api/v1/reset-password").permitAll()
                        .requestMatchers("api/v1/send-unlock-account").permitAll()
                        .requestMatchers("api/v1/unlock-account").permitAll()
                        .requestMatchers("api/v1/login").permitAll()
                        .requestMatchers("api/v1/add-to-cart").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
