package com.intern.e_commerce.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaAuditing
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;

    static final String[] PUBLIC_ENDPOINT = {
        "/users", "/auth/log-in", "/auth/introspect", "/auth/logout", "/auth/refresh",
    };
    static final String[] SWAGGER_ENDPOINT = {
        "/v3/api-docs/**", // Mở API docs
        "/swagger-ui/**", // Mở Swagger UI
        "/swagger-ui.html"
    };

    public SecurityConfig(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) //  Cấu hình CORS
                .csrf(csrf -> csrf.disable()) //  Tắt CSRF nếu dùng API stateless
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT)
                                .permitAll() //  Public endpoints
                                .requestMatchers(SWAGGER_ENDPOINT)
                                .permitAll()
                                .requestMatchers("/api/identity/auth/**")
                                .permitAll() //  Cho phép API login
                                .requestMatchers("/auth/google").permitAll()
                                .anyRequest()
                                .authenticated() //  Yêu cầu xác thực với các request còn lại
                        )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                        .decoder(jwtDecoder) //  Sử dụng JWT Decoder tùy chỉnh
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(
                                        new JwtAuthenticationEntryPoint()) //  Trả về lỗi 401 Unauthorized
                        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    //    @Bean
    //    public CorsFilter corsFilter() {
    //        CorsConfiguration corsConfiguration = new CorsConfiguration();
    //        corsConfiguration.addAllowedOrigin("*");
    //        corsConfiguration.addAllowedHeader("*");
    //        corsConfiguration.addAllowedMethod("*");
    //
    //        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    //        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
    //
    //        return new CorsFilter(urlBasedCorsConfigurationSource);
    //    }



}
