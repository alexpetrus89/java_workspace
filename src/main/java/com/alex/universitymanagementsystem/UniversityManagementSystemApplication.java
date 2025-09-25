package com.alex.universitymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.universitymanagementsystem.component.UmsExitCodeGenerator;
import com.alex.universitymanagementsystem.config.UmsConfig;

@RestController
@EnableScheduling
@SpringBootApplication(exclude={
	// Exclude automatic configuration for DataSource and MailSender
	// as we are providing custom configurations for these.
	// This prevents Spring Boot from trying to auto-configure them
	// based on properties in application.properties.
	// Uncomment the below lines if you want to exclude these auto-configurations.
	// org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
	// org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class
})
public class UniversityManagementSystemApplication {

	// injecting UmsConfig object
    private final UmsConfig umsConfig;

	// constructor
	public UniversityManagementSystemApplication(UmsConfig umsConfig) {
		this.umsConfig = umsConfig;
	}


    @GetMapping("/shutdown")
    public void shutdown() {
        umsConfig.shutDown(new UmsExitCodeGenerator());
    }


	@GetMapping("/restart")
    public void restart() {
        umsConfig.restart();
    }


	/**
	 * Entry point of the program.
	 * @param args The command line arguments passed to the program.
	 */
	public static void main(String[] args) {
		UmsConfig.setMainArgs(args); // save the main args
		SpringApplication.run(UniversityManagementSystemApplication.class, args);
	}


}
