/**
 * 
 */
package com.wings.jwt.demo.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wings.jwt.demo.constants.ApplicationConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author karan
 *
 */
public class JwtValidationFilter extends OncePerRequestFilter {

	/**
	 * 
	 */
	public JwtValidationFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);
		if (jwt != null) {
			try {
				Environment env = getEnvironment();
				if (env != null) {
					String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
							ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
					SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
					if (secretKey != null) {
						Claims claim = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
						String username = String.valueOf(claim.get("username"));
						String authorities = String.valueOf(claim.get("authorities"));
						Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
								AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			} catch (Exception exception) {
				throw new BadCredentialsException("InvalidTokenReceived");
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/login");
	}

}
