package controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
