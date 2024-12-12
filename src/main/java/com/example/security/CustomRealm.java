package com.example.security;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.model.Player;
import com.example.service.PlayerService;

public class CustomRealm extends JdbcRealm {

	private PlayerService playerService;
	
	

	public CustomRealm() {
		super();
	}

	@Autowired
	public CustomRealm(PlayerService playerService) {
		super();
		this.playerService = playerService;
	}

	/*
	Retrieves the username of the currently authenticated user.
	Fetches the corresponding user data (including roles) from the database.
	Constructs an AuthorizationInfo object containing the user's role(s).
	Returns this object to Shiro for authorization decisions.
	If the user does not exist in the database, returns null, meaning no roles or permissions are assigned.*/
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
//		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Player player = playerService.getPlayerByUsername(username);
		List<String> roles = player.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList());

		if (player != null) {
			// used to store the user's roles and permissions
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.addRoles(roles);
			// object is returned to Shiro and becomes part of the user's authorization data
			return info;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken userToken = (UsernamePasswordToken)token;
		Player player = playerService.getPlayerByUsername(userToken.getUsername());
		
		if(player != null) {
			return new SimpleAuthenticationInfo(player.getUsername(),player.getPassword(),getName());
		}
		throw new UnknownAccountException();
	}

}
