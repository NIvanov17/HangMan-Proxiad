package com.example.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.security.CustomRealm;
import com.example.security.JwtAuthenticationFilter;
import com.example.service.PlayerService;
import com.example.util.JwtUtils;

import jakarta.servlet.Filter;

@Configuration
public class SecurityConfig {

	private final PlayerService playerService;

	@Autowired
	public SecurityConfig(PlayerService playerService) {
		super();
		this.playerService = playerService;
	}

	/*
	 * The Authenticator is responsible for authenticating users. It determines
	 * whether the provided credentials (username, password..) are valid.
	 */
	@Bean
	public Authenticator authenticator(Realm customRealm) {
		ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
		authenticator.setRealms(List.of(customRealm));
		return authenticator;
	}

	/*
	 * It acts as the bridge between Shiro and your user database. It defines how
	 * users are authenticated and authorized.
	 */
	@Bean
	public Realm customRealm() {
		return new CustomRealm(playerService);
	}

	// central point managing all security related operations
	// authentication, authorization, session management, and cryptographic
	// operations
	@Bean
	public SecurityManager securityManager(Authenticator authenticator, Realm customRealm, Authorizer authorizer,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// Responsible for authenticating users and authorizing them by roles and
		// permissions.
		// You pass this realm to the SecurityManager so it can delegate tasks to it.
		securityManager.setAuthenticator(authenticator);
		securityManager.setRealm(customRealm);
		securityManager.setAuthorizer(authorizer);
		securityManager.setSessionManager(sessionManager);
		SecurityUtils.setSecurityManager(securityManager);

		return securityManager;
	}

	// whether a user has a specific role or permission.
	@Bean
	public Authorizer authorizer() {
		return new ModularRealmAuthorizer();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition(SecurityManager securityManager, CorsFilter corsFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
		DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
		
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // Register custom filters
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("cors", corsFilter); // CORS filter
        filters.put("jwt", jwtAuthenticationFilter); // JWT filter
        factoryBean.setFilters(filters);

		// Define your URL filter chains
		definition.addPathDefinition("api/v1/players/login", "anon");
		definition.addPathDefinition("api/v1/players/logout", "logout"); // Logout path
		definition.addPathDefinition("/**", "anon"); // Allow anonymous access to all other paths

		definition.addPathDefinition("/**", "cors");
		return definition;
	}

	@Bean
	public SessionManager sessionManager() {
		return new DefaultWebSessionManager();
	}

}
