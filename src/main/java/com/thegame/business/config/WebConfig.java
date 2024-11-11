package com.thegame.business.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Setzt CORS für alle Endpunkte
                .allowedOrigins("http://localhost:3000")  // Zulässige Ursprünge (dein Frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Zulässige HTTP-Methoden
                .allowedHeaders("*")  // Zulässige Header
                .allowCredentials(true);  // Erlaubt Credentials (wie Cookies, Auth-Header)
    }
}