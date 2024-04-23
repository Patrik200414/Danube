package com.danube.danube.security;

import com.danube.danube.security.jwt.AuthEntryPoint;
import com.danube.danube.security.jwt.AuthTokenFilter;
import com.danube.danube.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthEntryPoint unauthorizedHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private final JwtUtils jwtUtils;

    @Autowired
    public SecurityConfig(AuthEntryPoint unauthorizedHandler, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/user/login").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/api/user/registration").permitAll()
                                .requestMatchers("/api/product/item/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/product").hasRole("SELLER")
                                .requestMatchers("/api/product/detail/**").hasRole("SELLER")
                                .requestMatchers(HttpMethod.GET, "/api/product").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/product/count").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/product/category").permitAll()
                                .requestMatchers("/api/product/subcategory/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/product/category/subcategory").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                ).authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter(userDetailsService, jwtUtils);
    }
}
