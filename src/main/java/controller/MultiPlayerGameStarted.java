package controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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

public class MultiPlayerGameStarted extends HttpServlet {

	private GameService gameService;

	private Map<Game, History> history;

	public MultiPlayerGameStarted() {

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
		request.getRequestDispatcher("/multiplayerStarted.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
		

		
		Game wordToFind = (Game) session.getAttribute("word");
		Category category = (Category) session.getAttribute("category");
		int triesLeft = (int) session.getAttribute("triesLeft");
		String currentStateAsStr = (String) session.getAttribute("currentState");
		boolean isFinished = (boolean) session.getAttribute("isFinished");
		String mode = (String) session.getAttribute("mode");
		char guess = request.getParameter("guess").charAt(0);
		char[] currentState = currentStateAsStr.toCharArray();
		usedCharacters.add(guess);


		if (gameService.contains(wordToFind, guess)) {
			String wordToReturn = gameService.putLetterOnPlace(wordToFind, guess, currentState);
			history.put(wordToFind,
					new History(wordToReturn, triesLeft, category, usedCharacters, isFinished, mode));

			session.setAttribute("currentState", wordToReturn);
			
			if (gameService.isWordGuessed(wordToFind, wordToReturn)) {

				history.get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", "Congratulations! You Won!");
			}
			response.sendRedirect("/multiplayerStarted.jsp");
		} else {
			triesLeft--;
			history.put(wordToFind,
					new History(currentStateAsStr, triesLeft, category, usedCharacters, isFinished, mode));

			session.setAttribute("triesLeft", triesLeft);
			if (gameService.checkFailedTries(triesLeft)) {
				history.get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", "HAHAHA You lost! The word was " + wordToFind.getWord() + ".");
			}
			response.sendRedirect("/multiplayerStarted.jsp");
		}
	}

}
