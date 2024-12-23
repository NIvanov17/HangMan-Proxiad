package com.example.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.example.model.DTOs.JwtToken;
import com.example.util.JwtUtils;

public class JwtRealm extends AuthorizingRealm{
	
	private JwtUtils jwt;

	public JwtRealm(JwtUtils jwt) {
		super();
		this.jwt = jwt;
	}
	
	@Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // Add roles or permissions if required (fetch from database or cache)
        // For example:
        // authorizationInfo.addRole("USER");

        return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		 JwtToken jwtToken = (JwtToken) token;
	        String username = (String) jwtToken.getPrincipal();
	        String jwt = (String) jwtToken.getCredentials();

	        // Validate the JWT
	        if (!this.jwt.isTokenValid(jwt, username)) {
	            throw new AuthenticationException("Invalid or expired token");
	        }

	        // Return a simple authentication info object
	        return new SimpleAuthenticationInfo(username, jwt, getName());
	}

}
