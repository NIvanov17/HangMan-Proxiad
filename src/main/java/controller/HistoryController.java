package controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@Controller
public class HistoryController{

	private GameService gameService;

	@Autowired
	public HistoryController(GameService gameService) {
		this.gameService = gameService;
	}


	@GetMapping("/history")
	public String getHistory(HttpSession session) {
		Map<Game, History> history = gameService.getHistory();
		session.setAttribute("history", history);
		return "history";

	}

}
