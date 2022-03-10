package com.stargazerstudios.existence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExistenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExistenceApplication.class, args);
	}

	// TODO Create a class that will hold the backup properties of the ones defined in the application properties
	// TODO Add useful comments on the sources for easier reading
	// TODO There is a lot of code cleaning to be done
	// TODO Create utils for the entities on symphony project
	// TODO Check the codes where Optional are used and assert if it would be better to use try catch block
	// 	especially when doing database operations. Also determine if there are any database changes needed.
}
