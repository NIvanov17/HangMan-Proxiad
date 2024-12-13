package com.example.security;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

public class ShiroPasswordEncoder {

	private static final int HASH_ITERATIONS = 1024;

	public static String generateSalt() {
		return ByteSource.Util.bytes(String.valueOf(System.currentTimeMillis())).toHex();
	}

	public static String hashPassword(String plainPassword, String salt) {
		return new Sha256Hash(plainPassword, salt, HASH_ITERATIONS).toHex();
	}
}
