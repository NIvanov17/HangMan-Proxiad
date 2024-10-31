package controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import enums.Attributes;
import enums.Commands;
import enums.ErrorMessages;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import service.GameService;

@Controller
public class MultiPlayerController {

	private GameService gameService;

	@Autowired
	public MultiPlayerController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/{giverId}/{guesserId}/multiplayer")
	public String multiPlayerHome(@PathVariable long giverId, @PathVariable long guesserId, Model model) {

		model.addAttribute(Attributes.GIVVER_ID, giverId);
		model.addAttribute(Attributes.GUESSER_ID, guesserId);
		return "multiPlayer";
	}

	@GetMapping("/{gameId}/multiplayer")
	public String multiPlayerHome(@PathVariable long gameId, Model model) {

		return gameService.resumeGame(model, gameId);
	}

	@PostMapping("/{giverId}/{guesserId}/multiplayer")
	public String sendInputWord(@PathVariable long giverId, @PathVariable long guesserId,
			@RequestParam(required = false) String action, HttpServletRequest request, Model model) {

		boolean isValid = true;
		String wordToGuess = request.getParameter("wordToGuess");

		if (wordToGuess.equals("")) {
			isValid = false;
			model.addAttribute(Attributes.IS_WORD_VALID, isValid);
			model.addAttribute(Attributes.ERROR_MESSAGE, ErrorMessages.WORD_FIELD_IS_EMPTY);

			return "redirect:/{giverId}/{guesserId}/multiplayer";
		} else if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			model.addAttribute( Attributes.IS_WORD_VALID, isValid);
			model.addAttribute(Attributes.ERROR_MESSAGE, ErrorMessages.WORD_FIELD_IS_LESS_SYMBOLS);

			return "redirect:/{giverId}/{guesserId}/multiplayer";
		}
		String categoryParam = request.getParameter("category");
		model.addAttribute(Attributes.GIVVER_ID, giverId);
		model.addAttribute(Attributes.GUESSER_ID, guesserId);

		return gameService.prepareWordToBeDisplayed(model, wordToGuess, categoryParam, giverId, guesserId);
	}

	@PostMapping("/{giverId}/{guesserId}/multiplayer/guess")
	protected String multiPlayerGameGuess(@PathVariable long giverId, @PathVariable long guesserId,
			@RequestParam char letter, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException {
		long gameId = Long.parseLong(request.getParameter("gameId"));
		return gameService.tryGuessMultiplayer(letter, redirectAttributes, gameId);
	}

	@GetMapping("/{giverId}/{guesserId}/multiplayer/game")
	protected String multiPlayerGame(@PathVariable long giverId, @PathVariable long guesserId) {

		return "multiplayerStarted";
	}
}
