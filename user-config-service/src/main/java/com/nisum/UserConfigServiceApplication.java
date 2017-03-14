package com.nisum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class UserConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserConfigServiceApplication.class, args);
	}
}
