package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                .allowedHeaders("*");
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite requisições de qualquer origem
        config.addAllowedOrigin("*");
        
        // Permite o envio de credenciais (cookies, cabeçalhos de autenticação)
        config.setAllowCredentials(false);
        
        // Permite todos os métodos HTTP
        config.addAllowedMethod("*");
        
        // Permite todos os cabeçalhos
        config.addAllowedHeader("*");
        
        // Aplica esta configuração para todas as rotas
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}