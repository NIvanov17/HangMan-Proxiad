package controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import enums.Category;
import enums.Commands;
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

	private static final long serialVersionUID = 1L;

	private GameService gameService;

	private Map<Game, History> history;

	public MultiPlayerController() {

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
			return;
		}
		if (wordToGuess == "") {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_FIELD_IS_EMPTY);
			response.sendRedirect("/multiPlayer");
			return;
		} else if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_FIELD_IS_LESS_SYMBOLS);
			response.sendRedirect("/multiPlayer");
			return;
		} else if (gameService.historyContainsWord(history, wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", Commands.WORD_EXISTING_IN_HISTORY);
			response.sendRedirect("/multiPlayer");
			return;
		}
		String categoryParam = request.getParameter("category");
		Category category = Category.valueOf(categoryParam);

		gameService.prepareWordToBeDisplayed(request, response, session, wordToGuess, category);
	}

}
