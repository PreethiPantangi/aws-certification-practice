package com.aws.certification_practice.config;

import com.aws.certification_practice.service.CustomAccessDeniedHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/certification").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/certification/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/certification/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/question").hasRole(("ADMIN"))
                        .requestMatchers(HttpMethod.POST, "/api/v1/question").hasRole(("ADMIN"))
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/question/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/question/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/option").hasRole(("ADMIN"))
                        .requestMatchers(HttpMethod.GET, "/api/v1/option").hasRole(("ADMIN"))
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/option/**").hasRole(("ADMIN"))
                        .anyRequest()
                        .authenticated()
                );
        httpSecurity.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(new CustomAccessDeniedHandlerService()));
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.headers(headers -> headers.httpStrictTransportSecurity(hsts -> hsts
//                .includeSubDomains(true)
//                .maxAgeInSeconds(31536000)
//        ));
        return httpSecurity.build();
    }
}
