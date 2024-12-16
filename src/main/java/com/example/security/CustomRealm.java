package com.example.security;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("SHA-256");
		credentialsMatcher.setHashIterations(1024);
		this.setCredentialsMatcher(credentialsMatcher);
	}

	/*
	 * Retrieves the username of the currently authenticated user. Fetches the
	 * corresponding user data (including roles) from the database. Constructs an
	 * AuthorizationInfo object containing the user's role(s). Returns this object
	 * to Shiro for authorization decisions. If the user does not exist in the
	 * database, returns null, meaning no roles or permissions are assigned.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
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

		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		Player player = playerService.getPlayerByUsername(userToken.getUsername());
		String inputPassword = new String(userToken.getPassword()); // Password entered during login
		String providedSalt = player.getSalt(); // Stored salt from database
		String calculatedHash = ShiroPasswordEncoder.hashPassword(inputPassword, providedSalt);

		System.out.println("Input Password: " + inputPassword);
		System.out.println("Stored Salt: " + providedSalt);
		System.out.println("Calculated Hash: " + calculatedHash);
		System.out.println("Stored Hash: " + player.getPassword());

		if (player != null) {
			byte[] decodedSalt = Base64.getDecoder().decode(player.getSalt());
			
			System.out.println("Stored Username: " + player.getUsername());
	        System.out.println("Stored Salt (Base64): " + player.getSalt());
			System.out.println("Stored Hashed Password: " + player.getPassword());

			return new SimpleAuthenticationInfo(player.getUsername(), player.getPassword(),
					ByteSource.Util.bytes(decodedSalt), getName());
		}
		throw new UnknownAccountException("User not found: " + userToken.getUsername());
	}

}
