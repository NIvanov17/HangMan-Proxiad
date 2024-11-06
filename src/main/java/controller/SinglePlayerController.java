package controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import model.Game;
import model.DTOs.GameDTO;
import service.GameService;

@RestController
@Tag(name = "SinglePlayer Controller")
public class SinglePlayerController {

	private GameService gameService;

	@Autowired
	public SinglePlayerController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/game/hangMan")
	@Operation(summary = "Start or resume singlePlayer game", description = "Starts a new game or resumes an existing one if 'resume' action is specified.")
	public ResponseEntity<?> singlePlayerGameStarted(@RequestParam(required = false) String action,
			@RequestParam(required = false) Long gameId, @RequestParam(required = true) String username) {

		if ("resume".equals(action)) {
			return ResponseEntity.ok(gameService.resumeGame(gameId));

		} else if (username == null || username.isEmpty() || !gameService.isUsernameValid(username)) {
			return ResponseEntity.badRequest().body("Valid Username is required to start a game!");
		}
		GameDTO gameDTO = gameService.newGameStarted(username);

		return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);

	}

	@PostMapping("/hangMan/{id}")
	@Operation(summary = "Guess letter in singlePlayer game", description = "Make a guess for current displayed word in singlePlayer mode.")
	public ResponseEntity<GameDTO> singlePlayerGameGuess(@PathVariable long id, @RequestParam char guess,
			HttpServletRequest request) throws IOException {
		GameDTO dto = gameService.tryGuess(guess, id);
		return ResponseEntity.ok(dto);
	}

}
