package controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Welcome Controller")
public class WelcomeViewController {

	public WelcomeViewController() {
	}

	@GetMapping("view/welcome")
	public String welcome() {
		return "welcomePage";
	}
	
	@GetMapping("/")
	public String index() {
		return "welcomePage";
	}


}
