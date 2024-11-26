/**
 * 
 */
package com.wings.jwt.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.wings.jwt.demo.filter.JwtAuthenticationFilter;
import com.wings.jwt.demo.filter.JwtValidationFilter;

/**
 * @author karan
 *
 */
@Configuration
public class WingsConfiguration {

//	private final UserDetailsService userDetailsService;
//
//	// Constructor injection of UserDetailsService
//	public WingsConfiguration(UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService;
//	}
//
//	// Define AuthenticationManager bean
//	@Bean
//	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//		AuthenticationManagerBuilder authenticationManagerBuilder = http
//				.getSharedObject(AuthenticationManagerBuilder.class);
//		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//		return authenticationManagerBuilder.build();
//	}

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

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}
//
//	/**
//	 * From Spring Security 6.3 version
//	 *
//	 * @return
//	 */
//	@Bean
//	public CompromisedPasswordChecker compromisedPasswordChecker() {
//		return new HaveIBeenPwnedRestApiPasswordChecker();
//	}

}
