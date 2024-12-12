package com.example.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlayerViewController {

	@GetMapping("/username")
	public String username() {

		return "player";
	}

	@GetMapping("/word-giver")
	public String wordGiver() {
		return "word-giver";
	}
	
	@GetMapping("/{id}/word-guesser")
	public String wordGuesser(@PathVariable long id, Model model) {
		
		model.addAttribute("id", id);
		return "word-guesser";
	}

}
