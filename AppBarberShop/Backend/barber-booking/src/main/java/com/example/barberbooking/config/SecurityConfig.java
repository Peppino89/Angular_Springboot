package com.example.barberbooking.config;

import com.example.barberbooking.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      return  http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/api/dashboard/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/services/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/services/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/services/**").authenticated()

                                .requestMatchers(HttpMethod.GET, "/api/bookings").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/bookings/status/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/bookings/booking/date/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/bookings/booking/**").authenticated()
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/uploads/**"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()
                ).exceptionHandling(exception ->
                      exception
                              .authenticationEntryPoint(customAuthenticationEntryPoint)
                              .accessDeniedHandler(customAccessDeniedHandler)

              )

              .addFilterBefore(jwtAuthFilter,
                      UsernamePasswordAuthenticationFilter.class)
        .build();



    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
