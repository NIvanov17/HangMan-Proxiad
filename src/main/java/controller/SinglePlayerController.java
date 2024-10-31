package controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import service.GameService;

@Controller
public class SinglePlayerController {

	private GameService gameService;

	@Autowired
	public SinglePlayerController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/game/hangMan")
	public String singlePlayerGameStarted(@RequestParam(required = false) String action,
			@RequestParam(required = false) Long gameId, Model model)
			throws ServletException, IOException {
		if ("resume".equals(action)) {
			return gameService.resumeGame(model,gameId);

		} 
		String username = (String) model.asMap().get("username");
		return gameService.newGameStarted(model, username);

	}

	@PostMapping("/hangMan/{id}")
	public String singlePlayerGameGuess(@PathVariable long id,@RequestParam char guess, Model model,
			HttpServletRequest request) throws IOException {

		return gameService.tryGuess(guess, model,id);
	}

}
