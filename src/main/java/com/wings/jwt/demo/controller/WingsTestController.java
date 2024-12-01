/**
 * 
 */
package com.wings.jwt.demo.controller;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wings.jwt.demo.constants.ApplicationConstants;
import com.wings.jwt.demo.model.LoginResponseDTO;
import com.wings.jwt.demo.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author karan
 *
 */
@RestController
public class WingsTestController {

	private final AuthenticationManager authenticationManager;
//	private final PasswordEncoder passwordEncoder;
	private final Environment env;

	@Autowired
	public WingsTestController(AuthenticationManager authenticationManager, Environment env) {
		this.authenticationManager = authenticationManager;
//		this.passwordEncoder = passwordEncoder;
		this.env = env;
	}

	@PostMapping("/loginPage")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody User loginRequest) {
		String jwt = "";
		Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(),
				loginRequest.getPassword());
		Authentication authenticationResponse = authenticationManager.authenticate(authentication);
		if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
			if (null != env) {
				String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
				SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
				jwt = Jwts.builder().issuer("Eazy Bank").subject("JWT Token")
						.claim("username", authentication.getName())
						.claim("authorities",
								authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority)
										.collect(Collectors.joining(",")))
						.issuedAt(new java.util.Date())
						.expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000)).signWith(secretKey)
						.compact();
			}
		}
		return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwt)
				.body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
	}

	// Protected resource: Only accessible if the JWT is valid
	@GetMapping("/protected")
	public String protectedResource(@RequestHeader(value = "Authorization", required = false) String token) {
		return "Protected Resource Accessed Successfully!";
	}

	@RequestMapping("/home")
	public String dashboard() {
		return "welcome karan";
	}

}
