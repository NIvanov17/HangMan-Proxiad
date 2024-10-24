package controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import enums.Commands;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import service.GameService;

@Controller
public class MultiPlayerController {

	private GameService gameService;

	@Autowired
	public MultiPlayerController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/{giverUsername}/{guesserUsername}/multiplayer")
	public String multiPlayerHome(@PathVariable String giverUsername, @PathVariable String guesserUsername) {

		return "multiPlayer";
	}
	
	@GetMapping("/{gameId}/multiplayer")
	public String multiPlayerHome(@PathVariable long gameId,HttpSession session) {

		return gameService.resumeGame(session,gameId);
	}

	@PostMapping("/{giverUsername}/{guesserUsername}/multiplayer")
	public String sendInputWord(@PathVariable String giverUsername, @PathVariable String guesserUsername,
			@RequestParam(required = false) String action, HttpSession session, HttpServletRequest request, Model model)
			throws ServletException, IOException {

		boolean isValid = true;
		String wordToGuess = request.getParameter("wordToGuess");

		if (wordToGuess.equals("")) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_FIELD_IS_EMPTY);

			return "redirect:/{giverUsername}/{guesserUsername}/multiplayer";
		} else if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_FIELD_IS_LESS_SYMBOLS);

			return "redirect:/{giverUsername}/{guesserUsername}/multiplayer";
		}
		String categoryParam = request.getParameter("category");
		model.addAttribute("giverUsername", giverUsername);
		model.addAttribute("guesserUsername", guesserUsername);

		return gameService.prepareWordToBeDisplayed(session, wordToGuess, categoryParam,
				giverUsername, guesserUsername);
	}
	

	@PostMapping("/{giverUsername}/{guesserUsername}/multiplayer/guess")
	protected String multiPlayerGameGuess(@PathVariable String giverUsername, @PathVariable String guesserUsername,
			@RequestParam("letter") char letter, HttpSession session, HttpServletRequest request)
			throws ServletException, IOException {
		return gameService.tryGuessMultiplayer(letter, session);
	}

	@GetMapping("/{giverUsername}/{guesserUsername}/multiplayer/game")
	protected String multiPlayerGame(@PathVariable String giverUsername, @PathVariable String guesserUsername){

		return "multiplayerStarted";
	}
}
