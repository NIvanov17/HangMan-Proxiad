package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import enums.RoleName;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.Player;
import service.GameService;
import service.PlayerService;

@Controller
public class StatisticController {
//TODO: backward competible 
	private GameService gameService;
	private PlayerService playerService;

	@Autowired
	public StatisticController(GameService gameService, PlayerService playerService) {
		this.gameService = gameService;
		this.playerService = playerService;
	}

	@GetMapping("/history")
	public String history() {
		return "player-history";
	}

	@PostMapping("/history")
	public String getHistoryForPlayer(@RequestParam(required = true) String username,HttpSession session) {
		
		if(!playerService.contains(username)) {
			session.setAttribute("isValid", playerService.contains(username));
			session.setAttribute("errorMsg","Invalid username! Username is not existing.");
			return "redirect:/history";
		}

		return "redirect:/history/" + username;
	}

	@GetMapping("/history/{username}")
	public String getHistory(@PathVariable String username, HttpSession session) {
		List<Game> allGamesForPlayer = gameService.getAllGamesForPlayerByUsername(username, RoleName.GUESSER);
		session.setAttribute("username", username);
		session.setAttribute("allGames", allGamesForPlayer);
		return "history";

	}

	@GetMapping("/statistic")
	public String statistic(HttpSession session) {
		List<Game> games = gameService.getTopTenGames();
		session.setAttribute("games", games);
		session.setAttribute("attempts", gameService.averageAttempts());
		session.setAttribute("win-loss-ratio", gameService.calculateWinLossRatio());

		return "statistic";
	}

	@GetMapping("/ranking")
	public String ranking(HttpSession session) {
		List<Player> allPlayers = this.playerService.getAllPlayersByWins();
		session.setAttribute("allPlayers", allPlayers);
		return "allTime-ranking";
	}
	
	@GetMapping("/ranking/top")
	public String rankingTopTen(HttpSession session) {
		List<Player> players = this.playerService.getTopTenPlayersByWins();
		session.setAttribute("players", players);
		return "top-ranking";
	}

}
