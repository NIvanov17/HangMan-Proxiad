package controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import enums.ErrorMessages;
import enums.RoleName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.DTOs.GameDTO;
import model.DTOs.PlayerRankingDTO;
import model.DTOs.StatisticDTO;
import service.GameService;
import service.PlayerService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Statistic API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class StatisticAPIController {
	private GameService gameService;
	private PlayerService playerService;

	@Autowired
	public StatisticAPIController(GameService gameService, PlayerService playerService) {
		this.gameService = gameService;
		this.playerService = playerService;
	}

	@GetMapping("/games/history")
	@Operation(summary = "Get games for player")
	public ResponseEntity<List<GameDTO>> getHistoryForPlayer(@RequestParam(required = true) String username) {

		if (!playerService.isValid(username)) {
			throw new IllegalArgumentException(String.format(ErrorMessages.USERNAME_NOT_EXISTING, username));
		}

		return ResponseEntity.ok(gameService.getAllGamesDTOForPlayerByUsername(username, RoleName.GUESSER));
	}

	@GetMapping("/games/statistic")
	@Operation(summary = "Get Top 10 games")
	public ResponseEntity<StatisticDTO> statistic() {

		return ResponseEntity.ok(gameService.getTopTenGamesAsDTO());
	}

	@GetMapping("/players/ranking")
	@Operation(summary = "Get All time player ranking")
	public ResponseEntity<Page<PlayerRankingDTO>> ranking(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size) {
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
