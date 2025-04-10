package com.alex.universitymanagementsystem.config;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
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

        return username -> {
			UserDetails userDetails = userRepository.findByUsername(username);

			if (userDetails == null)
				throw new UsernameNotFoundException("User '" + username + "' not found");

			return userDetails;
        };
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
					"/login",
					"/logout",
					"/home",
					"/register",
					"/static/css/**",
					"/favicon.ico",
					"/exception/**",
					"/validation/**",
					"/user_student/create/create-student-from-user",
					"/user_professor/create/create-professor-from-user",
					"/api/v1/user/create-admin",
					"/api/v1/user/create-student",
					"/api/v1/user/create-professor"
				)
				.permitAll()
				.requestMatchers(
					// user
				"/user_admin/admin-home",
					"/user/user-menu",
					"/user/update/update",
					"/user/delete/delete",
					"/api/v1/user",
					"/api/v1/user/update",
					"/api/v1/user/update/build",
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
				.hasRole(ADMIN)
				.requestMatchers(
            		// URL accessibili solo agli utenti con ruolo STUDENT o ADMIN
				"/user_student/student-home",
					"/user_student/study_plan/study_plan_courses",
					"/user_student/study_plan/study_plan_modify",
					"/user_student/examinations/examination_appeal/calendar",
					"/user_student/examinations/examination_appeal/booked-result",
					"/user_student/examinations/examination_appeal/delete-booked-result",
					"/api/v1/study_plan/view",
					"/api/v1/study_plan/courses",
					"/api/v1/degree-course/courses/view",
					"/api/v1/examination-appeal/available/student",
					"/api/v1/examination-appeal/booked/student",
					"/api/v1/examination-appeal/booked/{id}",
					"/api/v1/examination-appeal/delete-booked/{id}",
					"/api/v1/user/create/student"
				)
				.hasAnyRole(STUDENT, ADMIN)
				.requestMatchers(
					// URL accessibili solo agli utenti con ruolo PROFESSOR o ADMIN
				"/user_professor/professor-home",
					"/api/v1/user/create/professor",
					"/user_professor/examinations/examination_appeal/examination-appeal-menu",
					"/user_professor/examinations/examination_appeal/view/professor",
					"/user_professor/examinations/examination_appeal/create-examination-appeal",
					"/user_professor/examinations/examination_appeal/create-result",
					"/user_professor/examinations/examination_appeal/students-booked",
					"/api/v1/course/view/professor",
					"/api/v1/examination-appeal/create",
					"/api/v1/examination-appeal/delete"
				)
				.hasAnyRole(PROFESSOR, ADMIN)
				.anyRequest()
				.authenticated()
			)
			.formLogin(form ->
				form.loginPage("/login")
					.permitAll()
					.successHandler(new UmsCustomAuthenticationSuccessHandler())
			)
			.logout(LogoutConfigurer::permitAll);

			return http.build();
		} catch (Exception e) {
			throw new AccessDeniedException("Access Denied: " + e.getMessage(), e);
		}
	}


}

