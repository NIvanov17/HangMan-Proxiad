package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	public WelcomeController() {
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "welcomePage";
	}
	
	@GetMapping("/")
	public String index() {
		return "welcomePage";
	}


}
