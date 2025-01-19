package org.example.easyrecruitbackend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// Désactive CSRF pour les API REST
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Active CORS avec la source configurée

                // Configuration de l'autorisation des requêtes
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Permet l'accès à toutes les routes sans authentification (désactivée pour le moment)
                )
//                .authorizeHttpRequests(auth -> auth
//                        // Permet l'accès à certaines routes sans authentification
//                        .requestMatchers("/public/**").permitAll()  // Routes publiques accessibles sans authentification
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/api/**").permitAll()
//                        .requestMatchers("/private/**").authenticated()  // Routes privées nécessitant une authentification
//                        .anyRequest().authenticated()  // Toutes les autres routes nécessitent une authentification
//                )
                // Configuration du serveur de ressources OAuth2 avec JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(JwtDecoders.fromIssuerLocation(keycloakBaseUrl+"/realms/master"))  // Spécifie l'émetteur
                                .jwtAuthenticationConverter(jwtAuthConverter)  // Injecte le convertisseur personnalisé pour les rôles
                        )
                )

                // Gestion des sessions (mode stateless pour les API REST)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Aucune session, pas de cookies
                );

        return http.build();  // Finalise la configuration
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://easy-recruit-front-aj.vercel.app")); // Remplacez "*" par des domaines spécifiques
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Si vous utilisez des cookies ou des sessions partagées

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
