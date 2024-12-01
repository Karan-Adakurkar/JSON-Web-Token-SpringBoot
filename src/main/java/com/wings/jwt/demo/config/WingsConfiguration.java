/**
 * 
 */
package com.wings.jwt.demo.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.wings.jwt.demo.filter.JwtAuthenticationFilter;
import com.wings.jwt.demo.filter.JwtValidationFilter;
import com.wings.jwt.demo.model.User;
import com.wings.jwt.demo.repository.UserRepository;
import com.wings.jwt.demo.services.CustomUserDetailsService;

/**
 * @author karan
 *
 */
@Configuration
public class WingsConfiguration {

	@Autowired
	private UserRepository userRepository;
//	private final PasswordEncoder passwordEncoder;

//	public WingsConfiguration(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		this.userRepository = userRepository;
//		this.passwordEncoder = passwordEncoder;
//	}

	@Bean
	public CommandLineRunner initializeData() {
		return args -> {
			// Create and save users
			User user1 = new User();
			user1.setUsername("karan");
			user1.setPassword("$2a$12$npC7Z7QZsHv.QCXrTF9yj.mcLop7vFsoJDgElOg3wGj7twlYf1Jra"); // Encode raw password
			user1.setRoles(Set.of("USER", "ADMIN"));
			userRepository.save(user1);

//			User user2 = new User();
//			user2.setUsername("sharukh");
//			user2.setPassword(passwordEncoder.encode("password123"));
//			user2.setRoles(Set.of("USER"));
//			userRepository.save(user2);
//
//			User user3 = new User();
//			user3.setUsername("salman");
//			user3.setPassword(passwordEncoder.encode("password456"));
//			user3.setRoles(Set.of("USER", "MODERATOR"));
//			userRepository.save(user3);

			System.out.println("Initialized sample users in the database.");
		};
	}

//	@Bean
//	UserDetailsService userDetailsService() {
//		return new InMemoryUserDetailsManager(
//				User.withUsername("karan").password("$2a$12$npC7Z7QZsHv.QCXrTF9yj.mcLop7vFsoJDgElOg3wGj7twlYf1Jra")
//						.roles("USER", "ADMIN").build(),
//				User.withUsername("sharukh").password("{noop}password123").roles("USER").build(),
//				User.withUsername("salman").password("{noop}password456").roles("USER", "MODERATOR").build());
//	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepository) {
		return new CustomUserDetailsService(userRepository);
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						(requests) -> requests.requestMatchers("/login").permitAll().requestMatchers("/protected")
								.hasRole("ADMIN").requestMatchers("/home").hasRole("USER").anyRequest().authenticated())
				.formLogin(form -> form // Use Spring Security's default login page
						.permitAll().defaultSuccessUrl("/home", true)// Allow all to access the login page
				).addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
//	 return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return new BCryptPasswordEncoder();
	}
	//
	// /**
	// * From Spring Security 6.3 version
	// *
	// * @return
	// */
	// @Bean
	// public CompromisedPasswordChecker compromisedPasswordChecker() {
	// return new HaveIBeenPwnedRestApiPasswordChecker();
	// }

}
