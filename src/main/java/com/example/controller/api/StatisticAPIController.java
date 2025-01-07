package com.example.controller.api;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enums.ErrorMessages;
import com.example.enums.RoleName;
import com.example.model.DTOs.GameDTO;
import com.example.model.DTOs.PlayerRankingDTO;
import com.example.model.DTOs.StatisticDTO;
import com.example.service.GameService;
import com.example.service.PlayerService;
import com.example.util.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Statistic API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class StatisticAPIController {
	private GameService gameService;
	private PlayerService playerService;
	private final JwtUtils jwt;

	@Autowired
	public StatisticAPIController(GameService gameService, PlayerService playerService, JwtUtils jwt) {
		this.gameService = gameService;
		this.playerService = playerService;
		this.jwt = jwt;
	}

	@GetMapping("/games/history")
	@Operation(summary = "Get games for player")
	public ResponseEntity<Page<GameDTO>> getHistoryForPlayer(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size, HttpServletRequest request) {

		String token = jwt.getTokenFromRequest(request);
		String username = jwt.extractUsername(token);
		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity.ok(gameService.getAllGamesDTOForPlayerByUsername(username, RoleName.GUESSER,pageable));
	}

	@GetMapping("/games/statistic")
	@Operation(summary = "Get Top 10 games")
	public ResponseEntity<StatisticDTO> statistic() {

		return ResponseEntity.ok(gameService.getTopTenGamesAsDTO());
	}

	@GetMapping("/players/ranking")
	@Operation(summary = "Get All time player ranking")
	public ResponseEntity<Page<PlayerRankingDTO>> ranking(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PlayerRankingDTO> dtoPage = playerService.getAllPlayersDTOByWins(pageable);

		return ResponseEntity.ok(dtoPage);
	}

	@GetMapping("/players/ranking/top-ten")
	@Operation(summary = "Get Top 10 players ranking")
	public ResponseEntity<List<PlayerRankingDTO>> rankingTopTen() {
		return ResponseEntity.ok(playerService.getTopTenPlayersDTOByWins());
	}

}
