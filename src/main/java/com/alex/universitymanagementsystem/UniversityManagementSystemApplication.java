package com.alex.universitymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.universitymanagementsystem.component.UmsExitCodeGenerator;
import com.alex.universitymanagementsystem.config.UmsConfig;

@SpringBootApplication
@RestController
public class UniversityManagementSystemApplication {

	// injecting UmsConfig object
    private final UmsConfig umsConfig;

	public UniversityManagementSystemApplication(UmsConfig umsConfig) {
		this.umsConfig = umsConfig;
	}

    @GetMapping("/shutdown")
    public void shutdown() {
        umsConfig.shutDown(new UmsExitCodeGenerator());
    }

	/**
	 * Entry point of the program.
	 * @param args The command line arguments passed to the program.
	 */
	public static void main(String[] args) {
		SpringApplication.run(UniversityManagementSystemApplication.class, args);
	}


}
