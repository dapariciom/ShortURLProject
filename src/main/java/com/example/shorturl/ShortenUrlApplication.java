package com.example.shorturl;

import com.example.shorturl.dao.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class ShortenUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortenUrlApplication.class, args);
	}

}
