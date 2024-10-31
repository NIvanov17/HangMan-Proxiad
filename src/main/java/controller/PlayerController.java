package controller;

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
import jakarta.servlet.http.HttpSession;
import model.Player;
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
	public String createWordGiver(@RequestParam(required = true) String username, Model model) {
		
		if(!playerService.isValid(username)) {
			model.addAttribute(Attributes.IS_VALID, playerService.isValid(username));
			model.addAttribute("errorMsg",ErrorMessages.INVALID_USERNAME);
			return "redirect:/word-giver";
		}
		model.addAttribute("errorMsg","");
		if (!playerService.contains(username)) {
			playerService.register(username);
		}
		
		Player player = playerService.getPlayerByUsername(username);

		return "redirect:/" + player.getId() + "/word-guesser";
	}

	@GetMapping("/{id}/word-guesser")
	public String wordGuesser(@PathVariable long id, Model model) {
		
		model.addAttribute("id", id);
		return "word-guesser";
	}

	@PostMapping("/{id}/word-guesser")
	public String createWordGuesser(@PathVariable long id,
			@RequestParam(required = true) String guesserUsername,Model model) {
		
		if(!playerService.isValid(guesserUsername) || playerService.areUsernamesEqual(id,guesserUsername)) {
			model.addAttribute(Attributes.IS_VALID, playerService.isValid(guesserUsername));
			model.addAttribute(Attributes.ARE_EQUAL, playerService.areUsernamesEqual(id,guesserUsername));
			model.addAttribute("errorMsg",ErrorMessages.INVALID_USERNAME);
			return "redirect:/{id}/word-guesser";
		}

		if (!playerService.contains(guesserUsername)) {
			playerService.register(guesserUsername);
		}
		
		model.addAttribute("errorMsg","");
		
		Player playerGuesser = playerService.getPlayerByUsername(guesserUsername);

		return "redirect:/" + id + "/" + playerGuesser.getId() + "/multiplayer";
	}

	@PostMapping("/username")
	public String createPlayer(@RequestParam(required = true) String username, RedirectAttributes redirectAttributes,Model model) {
		
		if(!playerService.isValid(username)) {
			model.addAttribute(Attributes.IS_VALID, playerService.isValid(username));
			model.addAttribute("errorMsg",ErrorMessages.INVALID_USERNAME);
			return "redirect:/username";
		}

		model.addAttribute("errorMsg","");
		if (!playerService.contains(username)) {
			playerService.register(username);
		}

		redirectAttributes.addFlashAttribute("username", username);
		return "redirect:/game/hangMan";
	}
}
