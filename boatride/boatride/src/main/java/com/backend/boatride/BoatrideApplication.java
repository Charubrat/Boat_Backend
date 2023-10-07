package com.backend.boatride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class BoatrideApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoatrideApplication.class, args);
	}

}
