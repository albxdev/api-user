package com.emazon.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.emazon.users")
public class ApiUsersApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiUsersApplication.class, args);
	}
}
