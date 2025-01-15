//package org.example.easyrecruitbackend.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**") // Allow all API endpoints
//                        .allowedOrigins("http://localhost:4200") // Allow requests from Angular
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods to allow
//                        .allowedHeaders("*") // Allow all headers
//                        .allowCredentials(true); // Allow cookies if needed
//            }
//        };
//    }
//}
//
