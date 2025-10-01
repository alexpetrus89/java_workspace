package com.alex.universitymanagementsystem.config;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.alex.universitymanagementsystem.component.UmsAccessDeniedHandler;
import com.alex.universitymanagementsystem.component.login.UmsAuthenticationSuccessHandler;
import com.alex.universitymanagementsystem.component.login.UmsOAuth2AuthenticationSuccessHandler;
import static com.alex.universitymanagementsystem.config.UmsConfig.ADMIN_URLS;
import static com.alex.universitymanagementsystem.config.UmsConfig.PROFESSOR_URLS;
import static com.alex.universitymanagementsystem.config.UmsConfig.PUBLIC_URLS;
import static com.alex.universitymanagementsystem.config.UmsConfig.STUDENT_URLS;
import com.alex.universitymanagementsystem.service.RedirectLoginService;
import com.alex.universitymanagementsystem.utils.CustomOAuth2User;
import com.alex.universitymanagementsystem.utils.CustomOidcUser;
import com.alex.universitymanagementsystem.utils.PrincipalExtractor;

@Configuration
@EnableWebSecurity
public class UmsWebSecurityConfig implements Serializable {

	// constant
	private static final String ADMIN = "ADMIN";
	private static final String STUDENT = "STUDENT";
	private static final String PROFESSOR = "PROFESSOR";
	private static final String LOGIN = "/login";

	private final transient RedirectLoginService redirectLoginService;
	private final transient List<PrincipalExtractor> principalExtractors;

	public UmsWebSecurityConfig(RedirectLoginService redirectLoginService, List<PrincipalExtractor> principalExtractors) {
		this.redirectLoginService = redirectLoginService;
		this.principalExtractors = principalExtractors;
	}



    /**
     * Password encoder for web security. Uses bcrypt algorithm.
     * @return PasswordEncoder
     */
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


	/**
	 * Provides an AuthenticationManager bean for managing authentication.
	 * @param authConfig the AuthenticationConfiguration bean
	 * @return AuthenticationManager that uses the provided UserDetailsService
	 */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


	/**
	 * Provides a custom AuthenticationSuccessHandler bean.
	 * @return UmsCustomAuthenticationSuccessHandler instance
	 */
	@Bean
    AuthenticationSuccessHandler umsAuthenticationSuccessHandler() {
        return new UmsAuthenticationSuccessHandler(redirectLoginService);
    }


	/**
	 * Provides a custom UmsOAuth2LoginSuccessHandler bean for handling OAuth2 login success.
	 * @param extractors the list of PrincipalExtractor beans
	 * @return UmsOAuth2LoginSuccessHandler instance
	 */
	@Bean
	UmsOAuth2AuthenticationSuccessHandler umsOAuth2AuthenticationSuccessHandler() {
		return new UmsOAuth2AuthenticationSuccessHandler(redirectLoginService, principalExtractors);
	}


	/**
	 * Provides a custom AccessDeniedHandler bean for handling access denied exceptions.
	 * @return UmsAccessDeniedHandler instance
	 */
	@Bean
	UmsAccessDeniedHandler umsAccessDeniedHandler() {
		return new UmsAccessDeniedHandler();
	}


	/**
	 * Provides a custom OidcUserService bean for handling OIDC user information.
	 * This implementation is tailored for Google OAuth2 login.
	 * @param uds the UserDetailsService to retrieve user details
	 * @return OidcUserService that loads user details and maps them to the application's User entity
	 * @throws OAuth2AuthenticationException if the user is not registered in the system
	 */
	@Bean
	OidcUserService googleOAuth2UserService(UserDetailsService uds) {

		return new OidcUserService() {
			@Override
			public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
            	// carica l'utente OIDC dal provider (Google, MS, ecc.)
				OidcUser oidcUser = super.loadUser(userRequest);
				String email = oidcUser.getEmail();

				UserDetails userDetails = uds.loadUserByUsername(email);

				// restituisci un principal che incapsula anche l'utente DB
				return new CustomOidcUser(userDetails, oidcUser.getIdToken(), oidcUser.getUserInfo());
			}
		};
	}


	/**
	 * Provides a custom OAuth2UserService bean for handling OAuth2 user information.
	 * This implementation is tailored for GitHub OAuth2 login.
	 * @param uds the UserDetailsService to retrieve user details
	 * @return OAuth2UserService that loads user details and maps them to the application's User entity
	 * @throws OAuth2AuthenticationException if the user is not registered in the system
	 */
	@Bean
	OAuth2UserService<OAuth2UserRequest, OAuth2User> gitHubOAuth2UserService(UserDetailsService uds) {

		return userRequest -> {
			OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

			String email = (String) oauth2User.getAttribute("email");
			UserDetails userDetails = uds.loadUserByUsername(email);

			return new CustomOAuth2User(userDetails, oauth2User);
		};
	}


	/**
	 * Configures the security filter chain for the application.
	 * @param http the HttpSecurity object
	 * @return SecurityFilterChain for the application
	 * @throws Exception if an error occurs
	 */
    @Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService uds)
		throws AccessDeniedException
	{
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
					.loginPage(LOGIN)
					.permitAll()
					.successHandler(umsAuthenticationSuccessHandler())
				)
				.oauth2Login(oauth2 -> oauth2
					.loginPage(LOGIN)
					.userInfoEndpoint(userInfo -> userInfo
						.oidcUserService(googleOAuth2UserService(uds))
						.userService(gitHubOAuth2UserService(uds))
					)
					.successHandler(umsOAuth2AuthenticationSuccessHandler())
				)
				.exceptionHandling(ex -> ex
					.accessDeniedHandler(umsAccessDeniedHandler())
				)
				.logout(LogoutConfigurer::permitAll)
				.csrf(csrf -> csrf.ignoringRequestMatchers("/ws/**")) // disabled cross site request forgery for web socket
				.build();

		} catch (Exception e) {
			throw new AccessDeniedException("Access Denied: " + e.getMessage(), e);
		}
	}



}

