package com.qr.qr_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableJpaRepositories(basePackages = "repository")
@SpringBootApplication
public class QrGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrGeneratorApplication.class, args);
	}

}
