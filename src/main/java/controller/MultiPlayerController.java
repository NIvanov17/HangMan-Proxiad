package controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import enums.Category;
import enums.Commands;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@Controller
public class MultiPlayerController {

	private GameService gameService;
	private WordsRepository wordsRepository;
	private Map<Game, History> history;

	@Autowired
	public MultiPlayerController(GameService gameService, WordsRepository wordsRepository) {
		this.gameService = gameService;
		this.wordsRepository = wordsRepository;
		this.history = this.wordsRepository.getHistory();
	}

	@GetMapping("/multiPlayer")
	public String multiPlayer() {

		return "multiPlayerView";
	}

	@PostMapping("/multiPlayer")
	public String sendInputWord(@RequestParam(name = "action", required = false) String action, HttpSession session,
			HttpServletRequest request) throws ServletException, IOException {

		boolean isValid = true;
		String wordToGuess = request.getParameter("wordToGuess");

		if ("resume".equals(action)) {
			String currentWord = request.getParameter("currentWord");
			return gameService.resumeGame(currentWord, session, history);
		}
		if (wordToGuess == "") {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_FIELD_IS_EMPTY);

			return "redirect:/multiPlayer";

		} else if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_FIELD_IS_LESS_SYMBOLS);

			return "redirect:/multiPlayer";

		} else if (gameService.historyContainsWord(history, wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_EXISTING_IN_HISTORY);

			return "redirect:/multiPlayer";
		}
		String categoryParam = request.getParameter("category");
		Category category = Category.valueOf(categoryParam);

		return gameService.prepareWordToBeDisplayed(session, wordToGuess, category);
	}
	
	@GetMapping("/multiplayerStarted")
	protected String multiPLayerGameStarted(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return "multiplayerStartedView";
	}

	
	@PostMapping("/multiplayerStarted")
	protected String multiPlayerGameGuess(HttpSession session,HttpServletRequest request)
			throws ServletException, IOException {
		char guess = request.getParameter("guess").charAt(0);

		return gameService.tryGuessMultiplayer(guess, session);
	}

}
