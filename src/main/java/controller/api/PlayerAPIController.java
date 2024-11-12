package controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import enums.ErrorMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Player;
import model.DTOs.PlayerDTO;
import model.DTOs.PlayersDTO;
import service.PlayerService;
import util.Validator;

@RestController
@Tag(name = "Player Controller")
public class PlayerAPIController {

	private final PlayerService playerService;

	private final Validator validator;

	@Autowired
	public PlayerAPIController(PlayerService playerService, Validator validator) {
		this.playerService = playerService;
		this.validator = validator;
	}

	@PostMapping("api/v1/players/word-giver/{username}")
	@Operation(summary = "User enters it's username and register if not yet")
	public ResponseEntity<PlayerDTO> createWordGiver(@PathVariable String username) {

		validator.isValid(username, String.format(ErrorMessages.USERNAME_IS_NOT_VALID, username));

		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		Player player = playerService.getPlayerByUsername(username);
		PlayerDTO dto = new PlayerDTO(player.getId(), player.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PostMapping("api/v1/players/word-guesser/{guesserUsername}")
	@Operation(summary = "Word-guesser enters it's username and register if not yet")
	public ResponseEntity<PlayersDTO> createWordGuesser(@RequestBody PlayerDTO playerDTO,
			@PathVariable String guesserUsername) {

		validator.isValid(guesserUsername, String.format(ErrorMessages.USERNAME_IS_NOT_VALID, guesserUsername));
		validator.areEqual(guesserUsername, playerDTO.getUsername(),
				String.format(ErrorMessages.USERNAMES_ARE_EQUAL, guesserUsername));

		if (!playerService.contains(guesserUsername)) {
			playerService.register(guesserUsername);

		}

		PlayersDTO playersDTO = playerService.createPlayersDTO(playerDTO.getId(), guesserUsername);

		return ResponseEntity.status(HttpStatus.CREATED).body(playersDTO);
	}

	@PostMapping("api/v1/players/{username}")
	@Operation(summary = "User enters it's username and register if not yet")
	public ResponseEntity<PlayerDTO> createPlayer(@PathVariable String username) {

		validator.isValid(username, String.format(ErrorMessages.USERNAME_IS_NOT_VALID, username));

		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		Player player = playerService.getPlayerByUsername(username);
		PlayerDTO dto = new PlayerDTO(player.getId(), player.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
}
