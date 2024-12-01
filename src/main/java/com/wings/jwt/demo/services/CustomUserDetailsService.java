/**
 * 
 */
package com.wings.jwt.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wings.jwt.demo.repository.UserRepository;

/**
 * @author karan
 *
 */
public class CustomUserDetailsService implements UserDetailsService {

	public final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		com.wings.jwt.demo.model.User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}

		return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
				.password(user.getPassword()).roles(user.getRoles().toArray(new String[0])).build();
	}

}
