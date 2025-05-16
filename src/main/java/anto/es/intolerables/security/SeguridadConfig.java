    package anto.es.intolerables.security;

    import anto.es.intolerables.security.jwt.JwtAuthEntryPoint;
    import anto.es.intolerables.security.jwt.JwtAuthFilter;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.annotation.Order;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;

    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;


    @Configuration
    @EnableMethodSecurity
    @RequiredArgsConstructor
    public class SeguridadConfig {

        private final JwtAuthEntryPoint jwtAuthEntryPoint;
        private final JwtAuthFilter jwtAuthFilter;

        // Para encriptar contraseñas
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        @Order(1)
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // Configuración de CORS y Seguridad
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                         //publicos
                            .requestMatchers(
                                    "/api/auth/register-api",
                                    "/api/intolerancias",
                                    "/api/recetas/buscar",
                                    "/api/restaurantes/buscar",
                                    "/api/auth/login"

                            ).permitAll()
                            //privados
                            .requestMatchers(

                                    "/api/favoritos-restaurantes",
                                    "/api/favoritos-recetas",
                                    "/api/recetas",
                                    "/api/recetas/todas",
                                    "/api/intolerancias",
                                    "/api/intolerancias/seleccionar",
                                    "/api/recetas/buscar",
                                    "/api/restaurantes/buscar",
                                    "api/favoritos-recetas/spoonacular",
                                    "/api/auth/usuario/"


                                    ).authenticated()

                    )
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

//CORS para el frontend
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
    }
