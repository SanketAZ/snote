package com.sxy.snote.configuration;

import com.sxy.snote.filter.JwtAuthConverter;
import com.sxy.snote.service.impl.LogoutHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @Autowired(required = false)
    private LogoutHandlerService logoutHandlerService;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Bean
    @Order(3)
    @Profile("prod")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/auth/login"))
                .securityMatcher(new AntPathRequestMatcher("/auth/register"))
                .securityMatcher(new AntPathRequestMatcher("/auth/token/refresh"))
                .csrf(customizer->customizer.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();}

    @Bean
    @Order(2)
    @Profile("prod")
    public SecurityFilterChain securityFilterChainMain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/users/**"))
                .csrf(customizer->customizer.disable())
                .authorizeHttpRequests(request-> request
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
                ))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    @Order(1)
    @Profile("dev")
    public SecurityFilterChain NoSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth.anyRequest().permitAll())
                .csrf(csrf->csrf.disable())
                .headers(headers -> headers.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());
        return http.build();
    }

    @Order(1)
    @Bean
    @Profile("prod")
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/auth/logout"))
                .csrf(customizer->customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
                ))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                )
                .addFilterBefore(new BearerTokenAuthenticationFilter(authenticationManager), LogoutFilter.class)
                .build();
    }

    @Bean
    @Profile("prod")
    public AuthenticationManager authenticationManager(JwtDecoder jwtDecoder) {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtAuthConverter);
        return new ProviderManager(jwtAuthenticationProvider);
    }
}
