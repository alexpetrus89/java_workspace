package com.alex.universitymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alex.universitymanagementsystem.component.SystemManager;

@EnableScheduling
@SpringBootApplication
public class UniversityManagementSystemApplication {

	/**
	 * Entry point of the program.
	 * @param args The command line arguments passed to the program.
	 */
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx =
            SpringApplication.run(UniversityManagementSystemApplication.class, args);

		SystemManager.setArgs(args);
		SystemManager.setContext(ctx);
	}


}
