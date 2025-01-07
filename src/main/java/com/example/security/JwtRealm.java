package com.example.security;

import java.util.List;

import org.apache.shiro.SecurityUtils;
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
		
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        
        JwtToken jwtToken = (JwtToken) SecurityUtils.getSubject().getPrincipal();
        List<String> roles = jwtToken.getRoles();

        authorizationInfo.addRoles(roles);

        return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		 JwtToken jwtToken = (JwtToken) token;
	        String username = (String) jwtToken.getPrincipal();
	        String jwt = (String) jwtToken.getCredentials();

	        if (!this.jwt.isTokenValid(jwt, username)) {
	            throw new AuthenticationException("Invalid or expired token");
	        }
	        
	        return new SimpleAuthenticationInfo(username, jwt, getName());
	}

}
