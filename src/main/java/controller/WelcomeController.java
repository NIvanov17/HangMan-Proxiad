package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Welcome Controller")
public class WelcomeController {

	public WelcomeController() {
	}

	@Operation(summary = "Get welcome message", description = "Returns a welcome message for the user.")
	@GetMapping("/welcome")
	public String welcome() {
		return "welcomePage";
	}
	
    @Operation(summary = "Get index message", description = "Returns a generic welcome message.")
	@GetMapping("/")
	public String index() {
		return "welcomePage";
	}


}
