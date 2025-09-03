package com.backend.foodproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableJpaAuditing
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@CrossOrigin(origins = "*")
public class FoodprojectApplication {

	@Bean
	public WebMvcConfigurer configure(){
		return new WebMvcConfigurer(){
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000",
								"http:// 172.17.17.25:3000")
						.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
						.allowedHeaders("*")
						.exposedHeaders("Authorization","Content-Type")
						.allowCredentials(true)
						.maxAge(3600);
			}
		};
	}


	public static void main(String[] args) {
		SpringApplication.run(FoodprojectApplication.class, args);
	}


}
