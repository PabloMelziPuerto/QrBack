package com.qr.qr_generator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig  implements WebMvcConfigurer {
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // o "/api/**"
                .allowedOrigins("http://localhost:3000") // dominio de tu React
                .allowedMethods("GET", "POST");
    }
}
