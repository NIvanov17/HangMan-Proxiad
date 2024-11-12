package controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticViewController {

	@Autowired
	public StatisticViewController() {

	}

	@GetMapping("/player-history")
	public String playerHistoryPage() {
		return "player-history";
	}
}