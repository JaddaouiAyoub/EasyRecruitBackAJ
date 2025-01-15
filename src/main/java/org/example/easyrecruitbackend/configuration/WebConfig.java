package org.example.easyrecruitbackend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permettre CORS sur toutes les routes
                .allowedOrigins("http://localhost:4200","http://easyrecruit.s3-website.eu-north-1.amazonaws.com") // Origine autorisée
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes autorisées
                .allowedHeaders("*") // Autoriser tous les en-têtes
                .allowCredentials(false); // Autoriser les cookies, si nécessaire
    }
}
