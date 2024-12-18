//package com.sxy.snote.configuration;
//
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import com.sxy.snote.filter.JwtAuthConverter;
//import com.sxy.snote.filter.JwtFilter;
//import com.sxy.snote.filter.JwtRefreshTokenFilter;
//import com.sxy.snote.service.impl.LogoutHandlerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfigs {
//
////    @Autowired
////    private UserDetailsService userDetailsService;
////
////    @Autowired
////    @Lazy
////    private JwtFilter jwtFilter;
////
////    @Autowired
////    @Lazy
////    private JwtRefreshTokenFilter jwtRefreshTokenFilter;
//
//    @Autowired
//    private RSAKeyRecord rsaKeyRecord;
//    @Autowired
//    private LogoutHandlerService logoutHandlerService;
//
//    @Autowired
//    private JwtAuthConverter jwtAuthConverter;
//
////    @Bean
////    @Order(5)
////    public SecurityFilterChain securityFilterChainMain(HttpSecurity http) throws Exception {
////        http
////                .securityMatcher(new AntPathRequestMatcher("/user/**"))
////                .securityMatcher(new AntPathRequestMatcher("/note/**"))
////                .csrf(customizer->customizer.disable())
////                .authorizeHttpRequests(request-> request
////                        .anyRequest().authenticated())
////                .formLogin(withDefaults())
////                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
////                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                //.exceptionHandling((exception)-> exception.authenticationEntryPoint(new RestAuthenticationEntryPoint()) )
////                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
////        return http.build();
////    }
//
//    @Bean
//    @Order(4)
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher(new AntPathRequestMatcher("/auth/login/"))
//                .securityMatcher(new AntPathRequestMatcher("/auth/register/"))
//                .csrf(customizer->customizer.disable())
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }
//
////    @Order(2)
////    @Bean
////    public SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
////        return httpSecurity
////                .securityMatcher(new AntPathRequestMatcher("/refresh-token/**"))
////                .csrf(customizer->customizer.disable())
////                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
////                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .exceptionHandling(ex -> {
////                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
////                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
////                })
////                .addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class)
////                .httpBasic(withDefaults())
////                .build();
////    }
//
////    @Order(3)
////    @Bean
////    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
////        return httpSecurity
////                .securityMatcher(new AntPathRequestMatcher("/logout/**"))
////                .csrf(customizer->customizer.disable())
////                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
////                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
////                .addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class)
////                .logout(logout -> logout
////                        .logoutUrl("/logout")
////                        .addLogoutHandler(logoutHandlerService)
////                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
////                )
////                .exceptionHandling(ex -> {
////                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
////                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
////                })
////                .build();
////    }
//
////    @Bean
////    public AuthenticationProvider authenticationProvider()
////    {
////        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
////        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
////        provider.setUserDetailsService(userDetailsService);
////        return provider;
////    }
//
////    @Bean
////    @Order(1)
////    public SecurityFilterChain NoSecurity(HttpSecurity http) throws Exception {
////        http.authorizeHttpRequests(auth->auth.anyRequest().permitAll())
////                .csrf(csrf->csrf.disable())
////                .headers(headers -> headers.disable())
////                .httpBasic(httpBasic -> httpBasic.disable())
////                .formLogin(form -> form.disable());
////
////        return http.build();
////    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChainMain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher(new AntPathRequestMatcher("/users/**"))
//                .csrf(customizer->customizer.disable())
//                .authorizeHttpRequests(request-> request
//                        .anyRequest().authenticated())
//                .formLogin(withDefaults())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
//                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
//                ))
//                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    JwtDecoder jwtDecoder(){
//        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
//    }
//
//    @Bean
//    JwtEncoder jwtEncoder(){
//        JWK jwk = new RSAKey.Builder(rsaKeyRecord.rsaPublicKey()).privateKey(rsaKeyRecord.rsaPrivateKey()).build();
//        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
//        return new NimbusJwtEncoder(jwkSource);
//    }
//
//}
