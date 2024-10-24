package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import service.PlayerService;

@Controller
public class PlayerController {

	private PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@GetMapping("/username")
	public String username() {
		
		return "player";
	}

	@GetMapping("/word-giver")
	public String wordGiver() {
		return "word-giver";
	}

	@PostMapping("/word-giver")
	public String createWordGiver(@RequestParam(required = true) String username, HttpSession session) {
		
		if(!playerService.isValid(username)) {
			session.setAttribute("isValid", playerService.isValid(username));
			session.setAttribute("errorMsg","Invalid username! Username must contain letter and must be more than 1 symbol.");
			return "redirect:/word-giver";
		}

		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		return "redirect:/" + username + "/word-guesser";
	}

	@GetMapping("/{username}/word-guesser")
	public String wordGuesser(@PathVariable("username") String giverUsername, Model model) {
		model.addAttribute("username", giverUsername);
		return "word-guesser";
	}

	@PostMapping("/{username}/word-guesser")
	public String createWordGuesser(@PathVariable("username") String giverUsername,
			@RequestParam(required = true) String guesserUsername) {

		if (!playerService.contains(guesserUsername)) {
			playerService.register(guesserUsername);
		}

		return "redirect:/" + giverUsername + "/" + guesserUsername + "/multiplayer";
	}

	@PostMapping("/username")
	public String createPlayer(@RequestParam(required = true) String username, RedirectAttributes redirectAttributes,HttpSession session) {
		
		if(!playerService.isValid(username)) {
			session.setAttribute("isValid", playerService.isValid(username));
			session.setAttribute("errorMsg","Invalid username! Username must contain letter and must be more than 1 symbol.");
			return "redirect:/username";
		}

		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		redirectAttributes.addFlashAttribute("username", username);
		return "redirect:/game/hangMan";
	}
}
