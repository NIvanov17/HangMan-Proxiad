package controller;

import java.lang.classfile.Attributes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import enums.Commands;
import enums.ErrorMessages;
import enums.RoleName;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.Player;
import service.GameService;
import service.PlayerService;

@Controller
public class StatisticController {
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
	public String getHistoryForPlayer(@RequestParam(required = true) String username,RedirectAttributes redirectAttributes) {
		
		if(!playerService.contains(username)) {
			redirectAttributes.addFlashAttribute(enums.Attributes.IS_VALID, playerService.contains(username));
			redirectAttributes.addFlashAttribute("errorMsg",ErrorMessages.USERNAME_NOT_EXISTING);
			return "redirect:/history";
		}

		return "redirect:/history/" + username;
	}

	@GetMapping("/history/{username}")
	public String getHistory(@PathVariable String username, Model model) {
		List<Game> allGamesForPlayer = gameService.getAllGamesForPlayerByUsername(username, RoleName.GUESSER);
		model.addAttribute(enums.Attributes.USERNAME, username);
		model.addAttribute(enums.Attributes.ALL_GAMES, allGamesForPlayer);
		return "history";

	}

	@GetMapping("/statistic")
	public String statistic(Model model) {
		List<String> games = gameService.getTopTenGames();
		model.addAttribute(enums.Attributes.GAMES, games);
		model.addAttribute(enums.Attributes.ATTEMPTS, gameService.averageAttempts());
		model.addAttribute(enums.Attributes.WIN_LOSS_RATIO, gameService.calculateWinLossRatio());

		return "statistic";
	}

	@GetMapping("/ranking")
	public String ranking(Model model) {
		List<Player> allPlayers = this.playerService.getAllPlayersByWins();
		model.addAttribute(enums.Attributes.ALL_PLAYERS, allPlayers);
		return "allTime-ranking";
	}
	
	@GetMapping("/ranking/top")
	public String rankingTopTen(Model model) {
		List<Player> players = this.playerService.getTopTenPlayersByWins();
		model.addAttribute(enums.Attributes.PLAYERS, players);
		return "top-ranking";
	}

}
