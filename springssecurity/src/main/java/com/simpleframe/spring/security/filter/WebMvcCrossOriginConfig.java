package com.simpleframe.spring.security.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// ~ File Information
// ====================================================================================================================

@Configuration
public class WebMvcCrossOriginConfig {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	@Bean
	public WebMvcConfigurer corsConfigurer() {

		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**")
				.allowCredentials(true)
        .allowedOrigins("*")
        .allowedMethods("POST", "GET", "OPTIONS", "PUT", "DELETE")
        .allowedHeaders("*")
        .maxAge(3600);
			}
		};
	}
}
