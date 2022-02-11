package com.nanoib.infra.http;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={
	"com.nanoib.domain.entities", "com.nanoib.domain.services", "com.nanoib.infra.*", "com.nanoib.infra.security.*", "com.nanoib.infra.security.jwt.*"})
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}

