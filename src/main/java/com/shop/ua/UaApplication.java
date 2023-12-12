package com.shop.ua;

import com.shop.ua.configurations.LogConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UaApplication {

	public static void main(String[] args) {
		LogConfiguration.configure();
		SpringApplication.run(UaApplication.class, args);
	}
}
