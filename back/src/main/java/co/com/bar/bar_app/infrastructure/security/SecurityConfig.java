package co.com.bar.bar_app.infrastructure.security;

import co.com.bar.bar_app.infrastructure.security.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private static final String SUPER = "SUPER";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler, JwtFilter jwtFilter, CorsConfigurationSource corsConfigurationSource) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(authorize ->
                    authorize
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/check", "/uploads/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/usuarios").hasAnyRole(SUPER, ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyRole(SUPER, ADMIN)
                            .requestMatchers(HttpMethod.PATCH, "/api/usuarios/estado/*").hasAnyRole(SUPER, ADMIN)
                            .requestMatchers(HttpMethod.POST, "/api/marcas").hasAnyRole(SUPER, ADMIN)
                            .requestMatchers(HttpMethod.PATCH, "/api/marcas").hasAnyRole(SUPER, ADMIN)
                            .requestMatchers(HttpMethod.POST, "/api/categorias").hasAnyRole(SUPER, ADMIN)
                            .requestMatchers(HttpMethod.POST, "/api/mesas").hasAnyRole(SUPER, ADMIN)
                            .anyRequest()
                            .authenticated()
                ).csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }
}
