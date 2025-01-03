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

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = jwtUtils.getTokenFromRequest(request);

		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String username = jwtUtils.extractUsername(jwt);
			Subject subject = SecurityUtils.getSubject();

			if (username != null && !subject.isAuthenticated()) {
				if (jwtUtils.isTokenValid(jwt, username)) {
					List<String> roles = jwtUtils.extractRoles(jwt);
					AuthenticationToken token = new JwtToken(username, jwt, roles);
					subject.login(token);
				} else {
					throw new SessionExpiredException("Session expired!");
				}
			}
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\": \"TokenExpired\", \"message\": \"Token has expired.\"}");
			return;
		} catch (SessionExpiredException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\": \"SessionExpired\", \"message\": \"Token expired.\"}");
			return;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid or expired token.\"}");
			return;
		}

		filterChain.doFilter(request, response);

	}

}
