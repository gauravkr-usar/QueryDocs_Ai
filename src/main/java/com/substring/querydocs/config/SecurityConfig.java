package com.substring.querydocs.config;

import com.substring.querydocs.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final AppProperties appProperties;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) {


        //customization

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Allow all CORS preflight
                                        .requestMatchers(
                                                "/error",
                                                "/api/v1/auth/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html").permitAll()
                                        .requestMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.toString())
                                        .requestMatchers("/api/v1/**").hasRole(Role.USER.toString())
                                        .anyRequest().authenticated()
                )
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) ->
                        {
                            System.out.println("Unauthorized request to: " + request.getRequestURI() + " - " + authException.getMessage());
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                        }))
        ;

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Provides a CorsConfigurationSource for Spring Security's CORS filter.
     * This ensures preflight OPTIONS requests get proper CORS headers BEFORE
     * Spring Security's authentication filters run.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        String originsRaw = appProperties.getCors().getAllowedOrigins();
        String methodsRaw = appProperties.getCors().getAllowedMethods();
        String headersRaw = appProperties.getCors().getAllowedHeaders();

        // Support wildcard pattern "*" for origins with credentials
        if ("*".equals(originsRaw.trim())) {
            config.addAllowedOriginPattern("*");
        } else {
            config.setAllowedOriginPatterns(Arrays.asList(originsRaw.split(",")));
        }

        config.setAllowedMethods("*".equals(methodsRaw.trim())
                ? List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                : Arrays.asList(methodsRaw.split(",")));

        config.setAllowedHeaders("*".equals(headersRaw.trim())
                ? List.of("*")
                : Arrays.asList(headersRaw.split(",")));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
