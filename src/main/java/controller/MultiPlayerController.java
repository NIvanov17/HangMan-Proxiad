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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import enums.Attributes;
import enums.Commands;
import enums.ErrorMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import model.DTOs.GameDTO;
import model.DTOs.PlayersDTO;
import service.GameService;

@RestController
@Tag(name = "MultiPlayer Controller")
public class MultiPlayerController {

	private GameService gameService;

	@Autowired
	public MultiPlayerController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/{giverId}/{guesserId}/multiplayer")
	@Operation(summary = "Get view where giver gives a word.")
	public ResponseEntity<?> multiPlayerHome(@PathVariable long giverId, @PathVariable long guesserId) {

		PlayersDTO playersDTO = gameService.getPlayersWithIDs(giverId, guesserId);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(playersDTO);
	}

	@GetMapping("hangman/{game}/multiplayer")
	@Operation(summary = "Display word from resume logic.")
	public ResponseEntity<?> resumeLogic(@PathVariable long game) {

		if (!gameService.isValid(game)) {
			return ResponseEntity.badRequest().body("Invalid game ID!");
		}

		return ResponseEntity.ok(gameService.resumeGame(game));
	}

	@PostMapping("/games/multiplayer")
	@Operation(summary = "Display input from word giver.")
	public ResponseEntity<?> sendInputWord(@RequestParam(required = true) long giverId,
			@RequestParam(required = true) long guesserId, @RequestParam(required = true) String wordToGuess,
			@RequestParam(required = true) String catergory) {

		boolean isValid = true;

		if (wordToGuess.equals("")) {
			isValid = false;
			return ResponseEntity.badRequest().body(ErrorMessages.WORD_FIELD_IS_EMPTY);

		} else if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			return ResponseEntity.badRequest().body(ErrorMessages.WORD_FIELD_IS_LESS_SYMBOLS);
		}

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(gameService.prepareWordToBeDisplayed(wordToGuess, catergory, giverId, guesserId));
	}

	@PutMapping("/multiplayer/{game}")
	@Operation(summary = "Make a guess.")
	protected ResponseEntity<GameDTO> multiPlayerGameGuess(@PathVariable long game, @RequestParam long giverId,
			@RequestParam long guesserId, @RequestParam char letter, HttpServletRequest request) throws IOException {
		GameDTO dto = gameService.tryGuessMultiplayer(letter, game);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("hangman/multiplayer/{game}")
	@Operation(summary = "Get game current state.")
	protected ResponseEntity<GameDTO> multiPlayerGame(@PathVariable long game) {

		GameDTO dto = gameService.getGameCurrentState(game);
		return ResponseEntity.ok(dto);
	}
}
