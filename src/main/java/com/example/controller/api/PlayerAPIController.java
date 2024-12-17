package com.example.controller.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enums.ErrorMessages;
import com.example.model.Player;
import com.example.model.DTOs.JwtResponse;
import com.example.model.DTOs.LoginDTO;
import com.example.model.DTOs.PlayerDTO;
import com.example.model.DTOs.PlayersDTO;
import com.example.model.DTOs.RegisterDTO;
import com.example.service.PlayerService;
import com.example.util.JwtUtils;
import com.example.util.Validator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Player API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class PlayerAPIController {

	private final PlayerService playerService;

	private final Validator validator;
	
	private final JwtUtils jwt;

	@Autowired
	public PlayerAPIController(PlayerService playerService, Validator validator, JwtUtils jwt) {
		this.playerService = playerService;
		this.validator = validator;
		this.jwt = jwt;
	}

	@PostMapping("api/v1/players/{username}")
	@Operation(summary = "Returns guesser and user")
	public ResponseEntity<Object> createWordGuesser(@RequestBody(required = false) LoginDTO playerDTO,
			@PathVariable String username) {

		validator.isValid(username, String.format(ErrorMessages.USERNAME_IS_NOT_VALID, username));

		if (playerDTO != null) {
			validator.areEqual(username, playerDTO.getUsername(),
					String.format(ErrorMessages.USERNAMES_ARE_EQUAL, username));
			if (!playerService.contains(username)) {
				playerService.register(username);
			}
			PlayersDTO playersDTO = playerService.createPlayersDTO(username, playerDTO.getUsername());
			return ResponseEntity.status(HttpStatus.CREATED).body(playersDTO);
		}
		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		Player player = playerService.getPlayerByUsername(username);
		return ResponseEntity.status(HttpStatus.CREATED).body(new PlayerDTO(player.getId(), player.getUsername()));

	}

	@GetMapping("api/v1/players")
	@Operation(summary = "Get user by username")
	public ResponseEntity<PlayerDTO> getPlayer(@RequestParam String username) {

		validator.isValid(username, String.format(ErrorMessages.USERNAME_IS_NOT_VALID, username));

		Player player = playerService.getPlayerByUsername(username);
		PlayerDTO dto = new PlayerDTO(player.getId(), player.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@DeleteMapping("api/v1/players/{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		validator.isValid(username, String.format(ErrorMessages.USERNAME_IS_NOT_VALID, username));
		playerService.deleteByUsername(username);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("api/v1/players/registration")
	public ResponseEntity<String> registerUser(@RequestBody() RegisterDTO dto) {

		playerService.registerDTO(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body("Successful Registration!");
	}

	@PostMapping("api/v1/players/login")
	public ResponseEntity<JwtResponse> login(@RequestBody() LoginDTO dto) {
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(dto.getUsername(), dto.getPassword());
		
		try {
			currentUser.login(token);
			String authToken = jwt.generateToken(dto.getUsername());
			
			return ResponseEntity.ok(new JwtResponse(authToken, dto.getUsername()));
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}
