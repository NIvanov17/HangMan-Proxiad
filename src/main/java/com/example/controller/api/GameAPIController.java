package com.example.controller.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enums.Commands;
import com.example.model.Game;
import com.example.model.DTOs.CreateGameInputDTO;
import com.example.model.DTOs.GameDTO;
import com.example.model.DTOs.GameTokenDTO;
import com.example.model.DTOs.GuessDTO;
import com.example.model.DTOs.JwtMessage;
import com.example.model.DTOs.LoginDTO;
import com.example.model.DTOs.MultiPlayerGameInputDTO;
import com.example.model.DTOs.MultiPlayerTokenDTO;
import com.example.model.DTOs.UpdateGameDTO;
import com.example.service.GameService;
import com.example.util.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "Games API Controller")
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class GameAPIController {

	private GameService gameService;

	private final JwtUtils jwt;

	@Autowired
	public GameAPIController(GameService gameService, JwtUtils jwt) {
		this.gameService = gameService;
		this.jwt = jwt;
	}

	@GetMapping("/v1/games/{id}")
	@Operation(summary = "Get game")
	public ResponseEntity<EntityModel<GameDTO>> resumeGame(@PathVariable long id, HttpServletRequest request) {

		String token = jwt.getTokenFromRequest(request);
		String username = jwt.extractUsername(token);
		Game game = gameService.findById(id);
		GameDTO gameDTO = game.getMode().equals(Commands.SINGLE_PLAYER_MODE) ? gameService.resumeSingleGame(id)
				: gameService.getGameDTOWithId(id,username);
		EntityModel<GameDTO> entityModel = EntityModel.of(gameDTO);

		entityModel
				.add(WebMvcLinkBuilder.linkTo(methodOn(GameAPIController.class).resumeGame(id, request)).withSelfRel());

		entityModel.add(WebMvcLinkBuilder
				.linkTo(methodOn(GameAPIController.class).createGame(new CreateGameInputDTO(
						new LoginDTO(gameDTO.getGuesser().getUsername()), new MultiPlayerTokenDTO()), request))
				.withRel("startNewGame"));

		entityModel.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(GameAPIController.class).makeGameGuess(id, new GuessDTO(), request))
				.withRel("makeGuess"));

		return ResponseEntity.ok(entityModel);

	}

	@PostMapping("/v1/games")
	@Operation(summary = "Start new MultiPlayer or Singleplayer game")
	public ResponseEntity<Object> createGame(@RequestBody CreateGameInputDTO dto, HttpServletRequest req) {

		MultiPlayerTokenDTO multiplayerDTO = dto.getMultiPlayerTokenDTO();
//		LoginDTO playerDTO = dto.getPlayerDTO();
		Object gameDTO = null;
		Object codeDTO = null;
		String token = jwt.getTokenFromRequest(req);
		String username = jwt.extractUsername(token);
		if (multiplayerDTO == null) {
			gameDTO = gameService.newGameStarted(username);
			return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);
		} else {
			// gameDTO = gameService.prepareWordToBeDisplayed(multiplayerDTO,username);

			codeDTO = gameService.createMultiPlayerGameWithCode(multiplayerDTO, username);
			return ResponseEntity.status(HttpStatus.CREATED).body(codeDTO);
		}
	}

	@PutMapping("/v1/games/{gameId}")
	@Operation(summary = "Guess letter")
	public ResponseEntity<GameDTO> makeGameGuess(@PathVariable long gameId, @RequestBody GuessDTO guessDTO,
			HttpServletRequest request) {

		Game game = gameService.findById(gameId);
		String token = jwt.getTokenFromRequest(request);
		String username = jwt.extractUsername(token);

		GameDTO dto = null;
			if (Commands.SINGLE_PLAYER_MODE.equals(game.getMode())) {
				dto = gameService.tryGuessSinglePlayer(guessDTO.getGuess(), gameId);
			} else {
				dto = gameService.tryGuessMultiplayer(guessDTO.getGuess(), gameId,username);
			}
		return ResponseEntity.ok(dto);

	}

	@PostMapping("/v2/games")
	@Operation(summary = "Start new MultiPlayer or Singleplayer game with JWT")
	public ResponseEntity<Object> createGameV2(@RequestBody(required = false) CreateGameInputDTO dto,
			HttpServletRequest request) {
		String token = jwt.getTokenFromRequest(request);
		String username = jwt.extractUsername(token);
		Object gameDTO = null;
		if (dto == null) {
			gameDTO = gameService.newGameStarted(username);
		} else {
			MultiPlayerTokenDTO multiplayerTokenDTO = dto.getMultiPlayerTokenDTO();
			gameDTO = gameService.prepareWordToBeDisplayed(multiplayerTokenDTO, username);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);

	}

	@PostMapping("/v2/games/game/code")
	@Operation(summary = "Get game with code")
	public ResponseEntity<GameDTO> getGameWithCode(@RequestBody GameTokenDTO dto, HttpServletRequest request) {
		String token = jwt.getTokenFromRequest(request);
		String username = jwt.extractUsername(token);
		GameDTO gameDTO = gameService.getGameWithCode(dto, username);

		return ResponseEntity.ok(gameDTO);
	}
}
