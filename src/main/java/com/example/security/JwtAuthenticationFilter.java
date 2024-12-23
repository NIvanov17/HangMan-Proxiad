package com.example.security;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.exception.SessionExpiredException;
import com.example.model.DTOs.JwtResponse;
import com.example.model.DTOs.JwtToken;
import com.example.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;

	@Autowired
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		super();
		this.jwtUtils = jwtUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			System.out.println("JWT Filter: OPTIONS request bypassed for " + request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = jwtUtils.getTokenFromRequest(request);

		if (jwt == null) {
			System.out.println("JWT Filter: No token found for " + request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtils.extractUsername(jwt);
		try {

			System.out.println("Current server time: " + new Date());
			System.out.println("Token expiration time: " + jwtUtils.extractExpiration(jwt));
			Subject subject = SecurityUtils.getSubject();

			if (username != null && !subject.isAuthenticated()) {
				if (jwtUtils.isTokenValid(jwt, username)) {
					System.out.println("JWT Filter: Token valid for " + username);
					AuthenticationToken token = new JwtToken(username, jwt);
					subject.login(token);
					System.out.println("JWT Filter: Subject logged in for username: " + username);
				}else {
					throw new SessionExpiredException("Session expired!");
				}
			}
		} catch (Exception e) {
			System.out.println(
					"JWT Filter: Invalid or expired token for " + request.getRequestURI() + " username: " + username);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid or expired token.\"}");
			return;
		}

		filterChain.doFilter(request, response);

	}

}
