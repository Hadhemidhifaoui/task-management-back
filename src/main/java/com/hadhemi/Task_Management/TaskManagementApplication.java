package com.hadhemi.Task_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "http://localhost:4200")
@SpringBootApplication
public class TaskManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);

		ZonedDateTime currentTime = ZonedDateTime.now();

		// Formater l'heure dans un format spécifique
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		String formattedCurrentTime = currentTime.format(formatter);

		System.out.println("Current time with clock skew: " + formattedCurrentTime);
	}


}
