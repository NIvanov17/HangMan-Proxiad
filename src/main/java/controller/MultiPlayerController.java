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

//add history and check in the if statement is this game appears in the history.
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
		String errorMessage = "";
		String wordToGuess = (String) request.getParameter("wordToGuess");
		if (!gameService.isWordValid(wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", "The word should contains more than 3 different symbols!");
			response.sendRedirect("/multiPlayer");
			return;
		}
		if (gameService.historyContainsWord(history, wordToGuess)) {
			isValid = false;
			session.setAttribute("isWordValid", isValid);
			session.setAttribute("errorMessage", "This word is already in history. :(");
			response.sendRedirect("/multiPlayer");
			return;
		}
		String categoryParam = request.getParameter("category");
		Category category = Category.valueOf(categoryParam);

		Game game = new Game();
		game = game.createNewGame(wordToGuess, category);

		String wordWithoutSpaces = new String(gameService.wordToReturn(wordToGuess));
		String wordToReturn = gameService.wordWithSpaces(wordWithoutSpaces);
		char firstLetter = gameService.getFirstLetter(wordWithoutSpaces);
		char lastLetter = gameService.getLastLetter(wordWithoutSpaces);
		Set<Character> usedCharacters = new HashSet<Character>();
		usedCharacters.add(firstLetter);
		usedCharacters.add(lastLetter);
		int triesLeft = 6;
		String mode = "Multiplayer";

		session.setAttribute("word", game);
		session.setAttribute("category", category);
		session.setAttribute("triesLeft", triesLeft);
		session.setAttribute("currentState", wordToReturn);
		session.setAttribute("isFinished", false);
		session.setAttribute("usedCharacters", usedCharacters);
		session.setAttribute("gameStatus", "");
		session.setAttribute("mode", mode);
		session.setAttribute("isWordValid", true);
//		session.setAttribute("errorMessage", errorMessage);
		request.getRequestDispatcher("/multiplayerStarted.jsp").forward(request, response);
	}

}
