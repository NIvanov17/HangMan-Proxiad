package com.example.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	
	private final long EXPIRATION_TIME = 3600000;
	private final Key SECRET_KEY = Keys.hmacShaKeyFor("MySuperSecureLongSecretKey123456!".getBytes());



	public String generateToken(String username) {
		
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SECRET_KEY)
				.compact();
	}
	
	
//	public boolean validateToken(String token) {
//		 try {
//
//		        Jwts.parser() 
//		                .setSigningKey(SECRET_KEY)
//		                .build() 
//		                .parseSignedClaims(token); 
//
//		        return true; 
//		    } catch (Exception e) {
//		        return false;
//		    }
//	}
	
	public String extractUsername(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	 public Claims extractAllClaims(String token) {
	        return Jwts.parser()
	                .setSigningKey(SECRET_KEY)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    }
	 
	 public String getTokenFromRequest(HttpServletRequest request) {
		    String bearerToken = request.getHeader("Authorization");
		    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
		        return bearerToken.substring(7); // Remove "Bearer " prefix
		    }
		    return null;
		}
	 
	 public boolean isTokenValid(String token, String username ) {
		 String extractedUsername = extractUsername(token);
		 return (extractedUsername.equals(username) && !isTokenExpired(token));
	 }


	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}


	private Date extractExpiration(String token) {
		Claims allClaims = extractAllClaims(token);
		
		return allClaims.getExpiration();
	}
}
