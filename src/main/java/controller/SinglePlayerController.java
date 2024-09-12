package controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@Controller
public class SinglePlayerController {

	private GameService gameService;
	private WordsRepository wordsRepository;
	private Map<Game, History> history;

	@Autowired
	public SinglePlayerController(GameService gameService, WordsRepository wordsRepository) {
		this.gameService = gameService;
		this.wordsRepository = wordsRepository;
		this.history = this.wordsRepository.getHistory();
	}

	@GetMapping("/hangMan")
	public String singlePlayerGameStarted(@RequestParam(name = "action", required = false) String action,
			HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if ("restart".equals(action)) {
			Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
			gameService.restartGame(session, usedCharacters);
			return "redirect:/hangMan";
		} else if ("resume".equals(action)) {
			String currentWord = request.getParameter("currentWord");
			return gameService.resumeGame(currentWord, session, history);
		} else {
			return gameService.newGameStarted(session, history);
		}
	}

	@PostMapping("/hangMan")
	public String singlePlayerGameGuess(HttpSession session, HttpServletRequest request)
			throws ServletException, IOException {
		char guess = request.getParameter("guess").charAt(0);
		return gameService.tryGuess(guess, session, history);
	}

}
