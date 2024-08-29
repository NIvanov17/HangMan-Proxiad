package controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import enums.Category;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

public class MultiPlayerController extends HttpServlet {

	private GameService gameService;

	private Map<Game, History> history;

	public MultiPlayerController() {

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

		getServletContext().getRequestDispatcher("/multiPlayer.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		boolean isValid = true;
		String wordToGuess = (String) request.getParameter("wordToGuess");

		String action = request.getParameter("action");
		if ("resume".equals(action)) {
			gameService.resumeGame(request, response, session, history);

		}
		if(wordToGuess == "") {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", "The word field can't be empty!");
			response.sendRedirect("/multiPlayer");
			return;
		} else if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", "The word should contains more than 3 different symbols!");
			response.sendRedirect("/multiPlayer");
			return;
		}else if (gameService.historyContainsWord(history, wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", "This word is already in history. :(");
			response.sendRedirect("/multiPlayer");
			return;
		}
		String categoryParam = request.getParameter("category");
		Category category = Category.valueOf(categoryParam);

		gameService.prepareWordToBeDisplayed(request, response, session, wordToGuess, category);
	}

}
