package controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import enums.Category;
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
import service.GameService;

public class GameController extends HttpServlet {

	private GameService gameService;
	private Map<Game, History> history;

	public GameController() {

	}

	@Autowired
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	@Autowired
	public void setWordsRepository(WordsRepository wordsRepository) {
		this.history = wordsRepository.getHistory();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		ApplicationContext context = (ApplicationContext) config.getServletContext().getAttribute("springContext");

		this.gameService = context.getBean(GameService.class);
		this.history = context.getBean(WordsRepository.class).getHistory();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		String action = request.getParameter("action");
		if ("resume".equals(action)) {
			gameService.resumeGame(request, response, session, history);

		} else {
			gameService.newGameStarted(request, response, session, history);

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");

		if ("restart".equals(action)) {

			gameService.restartGame(session, usedCharacters);
			// redirect
			response.sendRedirect("/hangMan");
			return;
		}
		gameService.tryGuess(request, response, session, usedCharacters, history);

	}

}
