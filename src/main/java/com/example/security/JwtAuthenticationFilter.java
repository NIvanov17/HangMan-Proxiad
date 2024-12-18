package com.example.security;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.model.DTOs.JwtResponse;
import com.example.model.DTOs.JwtToken;
import com.example.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private JwtUtils jwtUtils;
	
	@Autowired
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		super();
		this.jwtUtils = jwtUtils;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = jwtUtils.getTokenFromRequest(request);
		
		if(jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		 try {
	            String username = jwtUtils.extractUsername(jwt);
	            Subject subject = SecurityUtils.getSubject();

	            if (username != null && !subject.isAuthenticated()) {
	                if (jwtUtils.isTokenValid(jwt, username)) {
	                    AuthenticationToken token = new JwtToken(username, jwt);
	                    subject.login(token);
	                }
	            }
	        } catch (Exception e) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid or expired token.\"}");
	            return;
	        }

	        filterChain.doFilter(request, response);
		
		}

}
