package com.alex.studentmanagementsystem.config;

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

import com.alex.studentmanagementsystem.repository.UserRepository;

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

    @Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception
	{
        http
			.authorizeHttpRequests(
                requests -> requests
					.requestMatchers(
						"api/v1/student",
						"api/v1/student/home",
						// user
						"api/v1/user",
						// student
						"api/v1/student/view",
						"api/v1/student/view/{studentName}",
						"api/v1/student/view/{studentRegister}",
						"api/v1/student/read",
						"api/v1/student/read/{studentName}",
						"api/v1/student/read/{studentRegister}",
						"api/v1/student/create",
						"api/v1/student/create/{studentRegister}",
						"api/v1/student/update",
						"api/v1/student/update/{studentRegister}",
						"api/v1/student/delete/{studentRegister}",
						// professor
						"api/v1/professor/view",
						"api/v1/professor/read",
						"api/v1/professor/read/{professorName}",
						"api/v1/professor/read/{uniqueCode}",
						"api/v1/professor/create",
						"api/v1/professor/create/{uniqueCode}",
						"api/v1/professor/update",
						"api/v1/professor/update/{uniqueCode}",
						"api/v1/professor/delete/{uniqueCode}",
						// course
						"api/v1/course",
						"api/v1/course/home",
						"api/v1/course/view",
						"api/v1/course/view/{courseId}",
						"api/v1/course/view/{courseName}",
						"api/v1/course/create/{courseId}",
						"api/v1/course/update/{courseId}",
						"api/v1/course/delete/{courseId}",
						// degree course
						"api/v1/degree_course/view",
						"api/v1/degree-course/read-courses",
						"api/v1/degree-course/courses/view",
						"api/v1/degree-course/read-professors",
						"api/v1/degree-course/professors/view",
						"api/v1/degree-course/read-students",
						"api/v1/degree-course/students/view",
						// examination
						"api/v1/examination/view",
						"api/v1/examination/course-name",
						"api/v1/examination/student-register",
						"api/v1/examination/professor-unique-code",
						"api/v1/examination/create"
					)
                    .hasRole("USER")
                    .anyRequest()
                    .permitAll()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout(LogoutConfigurer::permitAll);

		return http.build();
	}


}

