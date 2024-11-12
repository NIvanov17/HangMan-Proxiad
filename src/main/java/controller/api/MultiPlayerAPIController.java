package controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import model.DTOs.GameDTO;
import model.DTOs.MultiPlayerGameInputDTO;
import model.DTOs.PlayersDTO;
import service.GameService;

@RestController
@Tag(name = "MultiPlayer Controller")
@RequestMapping("/api/v1/")
public class MultiPlayerAPIController {

	private GameService gameService;

	@Autowired
	public MultiPlayerAPIController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("multi-player/games")
	@Operation(summary = "Get multiplayer game")
	public ResponseEntity<GameDTO> getMultiPlayerGame(@RequestParam long id) {

		return ResponseEntity.ok(gameService.resumeMultiPlayerGame(id));
	}

	@PostMapping("/multi-player/games")
	@Operation(summary = "Create multiplayer game")
	public ResponseEntity<GameDTO> createMultiplayerGame(@RequestBody MultiPlayerGameInputDTO multiplayerDTO) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(gameService.prepareWordToBeDisplayed(multiplayerDTO));

	}

	@PutMapping("/multi-player/games/{game}")
	@Operation(summary = "Update multiplayer game.")
	protected ResponseEntity<GameDTO> multiPlayerGameGuess(@PathVariable long game, @RequestParam long giverId,
			@RequestParam long guesserId, @RequestParam char letter, HttpServletRequest request) throws IOException {
		GameDTO dto = gameService.tryGuessMultiplayer(letter, game);
		return ResponseEntity.ok(dto);
	}

}
