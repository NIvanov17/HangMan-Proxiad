package com.example.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.security.JwtAuthenticationFilter;
import com.example.util.JwtUtils;

@Configuration
public class FilterConfig {
	
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils) {
        return new JwtAuthenticationFilter(jwtUtils);
    }

	 @Bean
	    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
	        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
	        registrationBean.setFilter(jwtAuthenticationFilter);
	        registrationBean.addUrlPatterns("/api/*"); // Apply filter to /api/* URLs
	        registrationBean.setOrder(1); // Set the order of the filter execution
	        return registrationBean;
	    }
}
