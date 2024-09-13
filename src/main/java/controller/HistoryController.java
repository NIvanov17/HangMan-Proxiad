package controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;

@Controller
public class HistoryController{

	private WordsRepository wordsRepository;

	@Autowired
	public HistoryController(WordsRepository wordsRepository) {
		this.wordsRepository = wordsRepository;
	}


	@GetMapping("/history")
	public String getHistory(HttpSession session)
			throws ServletException, IOException {
		Map<Game, History> history = wordsRepository.getHistory();
		session.setAttribute("history", history);
		return "history";

	}

}
