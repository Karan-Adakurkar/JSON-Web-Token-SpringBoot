/**
 * 
 */
package com.wings.jwt.demo.exceptionhandling;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author karan
 *
 */
public class AuthEntryPoint implements AuthenticationEntryPoint {

	/**
	 * 
	 */
	public AuthEntryPoint() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.setStatus(404);
		response.getWriter().write("User not found");
	}

}
