package controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Player;
import model.DTOs.PlayerDTO;
import model.DTOs.PlayersDTO;
import service.PlayerService;

@RestController
@Tag(name = "Player Controller")
public class PlayerController {

	private PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@GetMapping("/username")
	@Operation(summary = "Get username page for singlePlayer game", description = "Displaying username page for singlePlayer game")
	public ResponseEntity<String> username() {
		
		return ResponseEntity.ok("Username page for singlePlayer game");
	}

	@GetMapping("/word-giver")
	@Operation(summary = "Get word-giver page for multiplayer game", description = "Displaying word-giver page for multiplayer game")
	public ResponseEntity<?> wordGiver() {
		return ResponseEntity.ok("Word giver page for multiplayer game");
	}

	@PostMapping("/word-giver")
	@Operation(summary = "Word-giver enters it's username for multiplayer game")
	public ResponseEntity<?> createWordGiver(@RequestParam(required = true) String username) {
		
		if(!playerService.isValid(username)) {
			return ResponseEntity.badRequest().body(Map.of(
					"status","error",
					"message", "Invalid username"));
		}
		if (!playerService.contains(username)) {
			playerService.register(username);
		}
		
		Player player = playerService.getPlayerByUsername(username);
		PlayerDTO dto = new PlayerDTO(player.getId(),player.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping("/{id}/word-guesser")
	@Operation(summary = "Get word-guesser page for multiplayer game", description = "Displaying word-guesser page for multiplayer game")
	public ResponseEntity<String> wordGuesser(@PathVariable long id) {
		
		return ResponseEntity.ok("Word guesser page and word giver with ID: " + id);
	}

	@PostMapping("/{id}/word-guesser")
	@Operation(summary = "Word-guesser enters it's username for multiplayer game")
	public ResponseEntity<?> createWordGuesser(@PathVariable long id,
			@RequestParam(required = true) String guesserUsername) {
		
		if(!playerService.isValid(guesserUsername) || playerService.areUsernamesEqual(id,guesserUsername)) {
			return ResponseEntity.badRequest().body("The provided username is invalid or matches the word giver's username.");
		}

		if (!playerService.contains(guesserUsername)) {
			playerService.register(guesserUsername);
		}
		
		PlayersDTO playersDTO = playerService.createPlayersDTO(id, guesserUsername);

		return ResponseEntity.status(HttpStatus.CREATED).body(playersDTO);
	}

	@PostMapping("/username")
	@Operation(summary = "User enters it's username for singlePlayer game")
	public ResponseEntity<?> createPlayer(@RequestParam(required = true) String username) {
		
		if(!playerService.isValid(username)) {
			return ResponseEntity.badRequest().body("Invalid username!");
		}

		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		Player player = playerService.getPlayerByUsername(username);
		PlayerDTO dto = new PlayerDTO(player.getId(),player.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
}
