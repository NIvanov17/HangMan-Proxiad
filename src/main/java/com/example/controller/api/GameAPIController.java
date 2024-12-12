package com.example.controller.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enums.Commands;
import com.example.model.Game;
import com.example.model.DTOs.CreateGameInputDTO;
import com.example.model.DTOs.GameDTO;
import com.example.model.DTOs.GuessDTO;
import com.example.model.DTOs.LoginDTO;
import com.example.model.DTOs.MultiPlayerGameInputDTO;
import com.example.model.DTOs.UpdateGameDTO;
import com.example.service.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Games API Controller")
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class GameAPIController {

	private GameService gameService;

	@Autowired
	public GameAPIController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/games")
	@Operation(summary = "Get game")
	public ResponseEntity<EntityModel<GameDTO>> resumeGame(@RequestParam long id) {
		Game game = gameService.findById(id);
		GameDTO gameDTO = game.getMode().equals(Commands.SINGLE_PLAYER_MODE) ? gameService.resumeSingleGame(id)
				: gameService.resumeMultiPlayerGame(id);
		EntityModel<GameDTO> entityModel = EntityModel.of(gameDTO);

		entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(GameAPIController.class).resumeGame(id)).withSelfRel());

		entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(GameAPIController.class).createGame(
				new CreateGameInputDTO(new LoginDTO(gameDTO.getGuesser().getUsername()),
						new MultiPlayerGameInputDTO())))
				.withRel("startNewGame"));

		entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(GameAPIController.class).makeGameGuess(gameDTO.getGameId(),
				new UpdateGameDTO(new GuessDTO(), new LoginDTO()))).withRel("makeGuess"));

		return ResponseEntity.ok(entityModel);

	}

	@PostMapping("/games")
	@Operation(summary = "Start new MultiPlayer or Singleplayer game")
	public ResponseEntity<GameDTO> createGame(@RequestBody CreateGameInputDTO dto) {

		MultiPlayerGameInputDTO multiplayerDTO = dto.getMultiPlayerGameInputDTO();
		LoginDTO playerDTO = dto.getPlayerDTO();
		GameDTO gameDTO = null;
		if (multiplayerDTO == null) {
			gameDTO = gameService.newGameStarted(playerDTO.getUsername());
		} else {
			gameDTO = gameService.prepareWordToBeDisplayed(multiplayerDTO);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);
	}

	@PutMapping("/games/{gameId}")
	@Operation(summary = "Guess letter")
	public ResponseEntity<GameDTO> makeGameGuess(@PathVariable long gameId, @RequestBody UpdateGameDTO updateGameDto) {

		Game game = gameService.findById(gameId);
		LoginDTO playerDTO = updateGameDto.getPlayerDTO();
		GuessDTO guessDTO = updateGameDto.getGuessDTO();

		GameDTO dto = null;
		if (gameService.isPlayerGuesser(game, playerDTO)) {
			if (Commands.SINGLE_PLAYER_MODE.equals(game.getMode())) {
				dto = gameService.tryGuessSinglePlayer(guessDTO.getGuess(), gameId);
			} else {
				dto = gameService.tryGuessMultiplayer(guessDTO.getGuess(), gameId);
			}
		}
		return ResponseEntity.ok(dto);

	}

}
