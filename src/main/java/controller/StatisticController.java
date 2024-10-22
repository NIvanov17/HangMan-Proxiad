package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import model.Game;
import service.GameService;

@Controller
public class StatisticController {

	private GameService gameService;

	@Autowired
	public StatisticController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/history/{username}")
	public String getHistory(@PathVariable String username, HttpSession session) {
		List<Game> allGamesForPlayer = gameService.getAllGamesForPlayerByUsername(username);
		session.setAttribute("username", username);
		session.setAttribute("allGames", allGamesForPlayer);
		return "history";

	}

}
