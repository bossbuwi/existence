package com.stargazerstudios.existence;

import com.stargazerstudios.existence.conductor.startup.Initializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ExistenceApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ExistenceApplication.class, args);
		ConfigurableApplicationContext ctx = SpringApplication.run(ExistenceApplication.class, args);

		if (Initializer.HAS_ERRORS) ctx.close();
	}
}
