package com.alex.universitymanagementsystem.config;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.alex.universitymanagementsystem.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements Serializable {

    /**
     * Password encoder for web security. Uses bcrypt algorithm.
     * @return PasswordEncoder
     */
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Provides a UserDetailsService bean for retrieving user details by username.
     * @param userRepository the UserRepository to access user data
     * @return UserDetailsService that searches for a user by username
     * @throws UsernameNotFoundException if the user is not found
     */
    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {

        return username -> {
			UserDetails userDetails = userRepository.findByUsername(username);

			if (userDetails != null)
				return userDetails;

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }


	/**
	 * Configures the security filter chain for the application.
	 * @param http the HttpSecurity object
	 * @return SecurityFilterChain for the application
	 * @throws Exception if an error occurs
	 */
    @Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(requests ->
			requests.requestMatchers(
				// URL accessibili a tutti gli utenti
				"/",
				"/login",
				"/logout",
				"/home",
				"/register"
			)
			.permitAll()
			.requestMatchers(
				// user
				"user/user-menu",
				"api/v1/user",
				"api/v1/user/view",
				"api/v1/user/read/read",
				"api/v1/user/create/create",
				"api/v1/user/update/update",
				"api/v1/user/delete/delete",
				// course
				"course/course-menu",
				"api/v1/course/view",
				"api/v1/course/read/read",
				"api/v1/course/create/create",
				"api/v1/course/update/update",
				"api/v1/course/delete/delete",
				// degree course
				"degree_course/degree-course-menu",
				"api/v1/degree_course/view",
				"degree_course/read/read-courses",
				"degree_course/read/read-professors",
				"degree_course/read/read-students",
				"api/v1/degree-course/professors/view",
				"api/v1/degree-course/courses/view",
				"api/v1/degree-course/students/view",
				// examination
				"examination/examination-menu",
				"api/v1/examination/view",
				"api/v1/examination/create/create",
				"api/v1/examination/update/update",
				"api/v1/examination/delete/delete",
				"api/v1/examination/read/course-name",
				"api/v1/examination/read/student-register",
				"api/v1/examination/read/professor-unique-code"
			)
            .hasRole("ADMIN")
			.requestMatchers(
            	// URL accessibili solo agli utenti con ruolo STUDENT
				"/student/user_student/student-home",
				"/student/student-menu",
				"/api/v1/student/view",
				"/api/v1/student/read/read",
				"/api/v1/student/create/create",
				"/api/v1/student/update/update",
				"/api/v1/student/delete/delete"
			)
			.hasAnyRole("STUDENT", "ADMIN")
			.requestMatchers(
				// URL accessibili solo agli utenti con ruolo PROFESSOR
				"/professor-menu",
				"/api/v1/professor/view",
				"/api/v1/professor/read/read",
				"/api/v1/professor/create/create",
				"/api/v1/professor/update/update",
				"/api/v1/professor/delete/delete"
			)
			.hasAnyRole("PROFESSOR", "ADMIN")
			.anyRequest()
			.authenticated()
		)
		.formLogin(form ->
			form.loginPage("/login")
				.permitAll()
				.successHandler(new CustomAuthenticationSuccessHandler()) // aggiungi questa riga
		)
		.logout(LogoutConfigurer::permitAll);

		return http.build();
	}


}

