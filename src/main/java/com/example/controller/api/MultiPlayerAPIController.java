package com.example.controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.DTOs.GameDTO;
import com.example.model.DTOs.MultiPlayerGameInputDTO;
import com.example.service.GameService;

import io.swagger.v3.oas.annotations.Operation;


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

	@PutMapping("/multi-player/games/{game}")
	@Operation(summary = "Update multiplayer game.")
	protected ResponseEntity<GameDTO> multiPlayerGameGuess(@PathVariable long game, @RequestParam long giverId,
			@RequestParam long guesserId, @RequestParam char letter) throws IOException {
		GameDTO dto = gameService.tryGuessMultiplayer(letter, game,null);
		return ResponseEntity.ok(dto);
	}

}
