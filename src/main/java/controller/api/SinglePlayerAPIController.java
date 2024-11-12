package controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.DTOs.GameDTO;
import model.DTOs.GuessDTO;
import model.DTOs.PlayerDTO;
import service.GameService;

@RestController
@Tag(name = "SinglePlayer Controller")
public class SinglePlayerAPIController {

	private GameService gameService;

	@Autowired
	public SinglePlayerAPIController(GameService gameService) {
		this.gameService = gameService;
	}

	@PostMapping("/api/v1/single-player/games")
	@Operation(summary = "Start new singlePlayer game")
	public ResponseEntity<GameDTO> createSinglePlayerGame(@RequestBody PlayerDTO playerDTO) {

		GameDTO gameDTO = gameService.newGameStarted(playerDTO.getUsername());

		return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);

	}

	@GetMapping("/api/v1/single-player/games")
	@Operation(summary = "Get singlePlayer game")
	public ResponseEntity<GameDTO> resumeSinglePlayerGame(@RequestParam long id) {
		return ResponseEntity.ok(gameService.resumeSingleGame(id));
	}

	@PutMapping("/api/v1/single-player/games/{gameId}")
	@Operation(summary = "Guess letter in singlePlayer game")
	public ResponseEntity<GameDTO> singlePlayerGameGuess(@PathVariable long gameId, @RequestBody GuessDTO guessDTO) {
		GameDTO dto = gameService.tryGuess(guessDTO.getGuess(), gameId);
		return ResponseEntity.ok(dto);
	}

}
