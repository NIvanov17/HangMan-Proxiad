package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exception.InvalidUsernameException;
import com.example.service.PlayerService;

@Component
public class Validator {

	private final PlayerService playerService;

	@Autowired
	public Validator(PlayerService playerService) {
		this.playerService = playerService;
	}

	public void isValid(String username, String exMessage) {
		if (!playerService.isValid(username)) {
			throw new InvalidUsernameException(exMessage);
		}
	}

	public void areEqual(String guesserUsername, String username, String exMessage) {
		if(guesserUsername.equals(username)) {
			throw new InvalidUsernameException(exMessage);
		}
	}
}
