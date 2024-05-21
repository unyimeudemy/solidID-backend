package com.unyime.solidID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableScheduling
public class SolidIdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolidIdApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings( CorsRegistry  registry){
				registry
						.addMapping("/**")
						.allowedOrigins(
                                "https://solidid-client.onrender.com",
								"https://solidid-client-1.onrender.com",
								"http://localhost:3000"
                        );
			}
		};

	}

}
