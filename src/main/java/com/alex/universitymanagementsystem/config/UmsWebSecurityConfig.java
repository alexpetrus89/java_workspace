package com.alex.universitymanagementsystem.config;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.alex.universitymanagementsystem.component.UmsCustomAuthenticationSuccessHandler;
import com.alex.universitymanagementsystem.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class UmsWebSecurityConfig implements Serializable {

	// constant
	private static final String ADMIN = "ADMIN";
	private static final String STUDENT = "STUDENT";
	private static final String PROFESSOR = "PROFESSOR";

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
        return username -> userRepository
			.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }


	/**
	 * Provides an AuthenticationManager bean for managing authentication.
	 * @param userDetailsService the UserDetailsService to retrieve user details
	 * @return AuthenticationManager that uses the provided UserDetailsService
	 */
	@Bean
	AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
	) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authProvider);
	}


	/**
	 * Configures the security filter chain for the application.
	 * @param http the HttpSecurity object
	 * @return SecurityFilterChain for the application
	 * @throws Exception if an error occurs
	 */
    @Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws AccessDeniedException {
		try {
			http.authorizeHttpRequests(requests ->
				requests.requestMatchers(
					// URL accessibili a tutti gli utenti
					"/",
					"/shutdown",
					"/login",
					"/logout",
					"/home",
					"/register",
					"/static/css/**",
					"/static/js/ums-degree-courses.js",
					"/static/js/ums-password-rules.js",
					"/static/images/**",
					"/favicon.ico",
					"/exception/**",
					"/user_student/create/create-student-from-user",
					"/user_professor/create/create-professor-from-user",
					"/user/update/update-result",
					"/api/v1/user/create-admin",
					"/api/v1/user/create-student",
					"/api/v1/user/create-professor",
					"/api/v1/degree-course/ajax",
					"/ws/**"
				)
				.permitAll()
				.requestMatchers(
					// user
					"/user_admin/**",
					"/api/v1/user",
					"/api/v1/user/delete",
					// student
					"/api/v1/student/view",
					"/api/v1/student/read/read",
					"/api/v1/student/update/update",

					// professor
					"/api/v1/professor/view",
					"/api/v1/professor/read/read",
					"/api/v1/professor/update/update",

					// course
					"/course/course-menu",
					"/api/v1/course/view",
					"/api/v1/course/read/read",
					"/api/v1/course/create/create",
					"/api/v1/course/update/update",
					"/api/v1/course/delete/delete",

					// degree course
					"/degree_course/degree-course-menu",
					"/degree_course/read/read-courses",
					"/degree_course/read/read-professors",
					"/degree_course/read/read-students",
					"/api/v1/degree-course/view",
					"/api/v1/degree-course/professors/view",
					"/api/v1/degree-course/students/view",

					// examination
					"/examination/examination-menu",
					"/api/v1/examination/view",
					"/api/v1/examination/update/update",
					"/api/v1/examination/delete/delete",
					"/api/v1/examination/read/course-name",
					"/api/v1/examination/read/student-register",
					"/api/v1/examination/read/professor-unique-code"
				)
				.hasRole(ADMIN)
				.requestMatchers(
            		// URL accessibili solo agli utenti con ruolo STUDENT o ADMIN
					"/user_student/**",
					"/api/v1/user/create/student",
					"/api/v1/user/update/student",
					"/api/v1/study_plan/view",
					"/api/v1/study_plan/modify",
					"/api/v1/study_plan/change",
					"/api/v1/degree-course/courses/view",
					"/api/v1/degree-course/courses/ajax",
					"/api/v1/examination/create/create",
					"/api/v1/examination-appeal/available/student",
					"/api/v1/examination-appeal/booked/student",
					"/api/v1/examination-appeal/booked/{id}",
					"/api/v1/examination-appeal/delete-booked/{id}",
					"/api/v1/examination-outcome/view",
					"/api/v1/examination-outcome/outcome",
					"/api/v1/examination-outcome/confirm-refusal",
					"/api/v1/outcome-notifications"
				)
				.hasAnyRole(STUDENT, ADMIN)
				.requestMatchers(
					// URL accessibili solo agli utenti con ruolo PROFESSOR o ADMIN
					"/user_professor/**",
					"/api/v1/user/create/professor",
					"/api/v1/user/update/professor",
					"/api/v1/course/view/professor",
					"/api/v1/course/read/{courseId}",
					"/api/v1/examination-appeal/make",
					"/api/v1/examination-appeal/create",
					"/api/v1/examination-appeal/delete",
					"/api/v1/examination-outcome/make/{register}/{id}",
					"/api/v1/examination-outcome/notify"
				)
				.hasAnyRole(PROFESSOR, ADMIN)
				.requestMatchers(
					"/api/v1/user/update/build"
				)
				.hasAnyRole(STUDENT, PROFESSOR, ADMIN)
				.anyRequest()
				.authenticated()
			)
			.formLogin(form ->
				form.loginPage("/login")
					.permitAll()
					.successHandler(new UmsCustomAuthenticationSuccessHandler())
			)
			.logout(LogoutConfigurer::permitAll);

			// disabled cross site request forgery for web socket
			http.csrf(csrf -> csrf.ignoringRequestMatchers("/ws/**"));

			return http.build();
		} catch (Exception e) {
			throw new AccessDeniedException("Access Denied: " + e.getMessage(), e);
		}
	}


}

