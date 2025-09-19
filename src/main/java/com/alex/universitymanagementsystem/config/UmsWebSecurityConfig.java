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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.alex.universitymanagementsystem.component.UmsCustomAuthenticationSuccessHandler;
import static com.alex.universitymanagementsystem.config.UmsConfig.ADMIN_URLS;
import static com.alex.universitymanagementsystem.config.UmsConfig.PROFESSOR_URLS;
import static com.alex.universitymanagementsystem.config.UmsConfig.PUBLIC_URLS;
import static com.alex.universitymanagementsystem.config.UmsConfig.STUDENT_URLS;
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

	@Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new UmsCustomAuthenticationSuccessHandler();
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
			return http
				.authorizeHttpRequests(requests -> requests
				.requestMatchers(PUBLIC_URLS)
				.permitAll()
				.requestMatchers(ADMIN_URLS)
				.hasRole(ADMIN)
				.requestMatchers(STUDENT_URLS)
				.hasAnyRole(STUDENT, ADMIN)
				.requestMatchers(PROFESSOR_URLS)
				.hasAnyRole(PROFESSOR, ADMIN)
				.requestMatchers("/api/v1/user/update/build")
				.hasAnyRole(STUDENT, PROFESSOR, ADMIN)
				.anyRequest()
				.authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.permitAll()
				.successHandler(new UmsCustomAuthenticationSuccessHandler())
			)
			.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
				.permitAll()
                .successHandler(new UmsCustomAuthenticationSuccessHandler())
            )
			.logout(LogoutConfigurer::permitAll)
			.csrf(csrf -> csrf.ignoringRequestMatchers("/ws/**")) // disabled cross site request forgery for web socket
			.build();

		} catch (Exception e) {
			throw new AccessDeniedException("Access Denied: " + e.getMessage(), e);
		}
	}


}

