package com.example.security;

import java.security.SecureRandom;
import java.util.Base64;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

public class ShiroPasswordEncoder {

	private static final int HASH_ITERATIONS = 1024;
	private static final int SALT_LENGTH = 16;

	public static String generateSalt() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] saltBytes = new byte[SALT_LENGTH];
		secureRandom.nextBytes(saltBytes);
		return Base64.getEncoder().encodeToString(saltBytes);
	}

	public static String hashPassword(String plainPassword, String salt) {
		return new Sha256Hash(plainPassword,
				ByteSource.Util.bytes(Base64.getDecoder().decode(salt)),
				HASH_ITERATIONS).toHex();
	}
}
