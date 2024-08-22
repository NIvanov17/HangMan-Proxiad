package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import enums.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.OngoingGame;
import model.Word;
import repository.WordsRepository;
import service.GameService;

@WebServlet(name = "GameController", urlPatterns = "/hangMan")
public class GameController extends HttpServlet {

	private GameService gameService;
	private Map<Word, OngoingGame> onGoingWords;

	public GameController() {
		System.out.println("GameController created");
		this.gameService = GameService.getGameService();
		this.onGoingWords = WordsRepository.getOnGoingWordslist();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String action = request.getParameter("action");

		if ("resume".equals(action)) {

			String wordToSet = (String) request.getParameter("currentWord");
			Word selectedWord = null;

			for (Word w : onGoingWords.keySet()) {

				if (w.getName().equals(wordToSet)) {
					selectedWord = w;
					break;
				}
			}

				OngoingGame ongoingGame = onGoingWords.get(selectedWord);

				session.setAttribute("word", selectedWord);
				session.setAttribute("currentState", ongoingGame.getWordState());
				session.setAttribute("triesLeft", ongoingGame.getTriesLeft());
				session.setAttribute("isFinished", ongoingGame.isFinished());
				session.setAttribute("usedCharacters", ongoingGame.getUsedChars());
				session.setAttribute("wordCategory", ongoingGame.getCategory());
				//forwarding
				request.getRequestDispatcher("/gameStarted.jsp").forward(request, response);
			

		} else {
			Word word = WordsRepository.getRandomWord();
			Category wordCategory = word.getCategory();
			String wordWithoutSpaces = new String(gameService.wordToReturn(word.getName()));
			String wordToReturn = gameService.wordWithSpaces(wordWithoutSpaces);
			char firstLetter = gameService.getFirstLetter(wordWithoutSpaces);
			char lastLetter = gameService.getLastLetter(wordWithoutSpaces);
			Set<Character> usedCharacters = new HashSet<Character>();
			usedCharacters.add(firstLetter);
			usedCharacters.add(lastLetter);
			int triesLeft = 6;

			session.setAttribute("word", word);
			session.setAttribute("wordCategory", wordCategory);
			session.setAttribute("triesLeft", triesLeft);
			session.setAttribute("currentState", wordToReturn);
			session.setAttribute("isFinished", false);
			session.setAttribute("usedCharacters", usedCharacters);

			request.getRequestDispatcher("/gameStarted.jsp").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");

		if ("restart".equals(action)) {

			gameService.restartGame(session, usedCharacters);
			//redirect
			response.sendRedirect("/hangMan");
			return;
		}

		Word wordToFind = (Word) session.getAttribute("word");
		int triesLeft = (int) session.getAttribute("triesLeft");
		String currentStateAsStr = (String) session.getAttribute("currentState");
		boolean isFinished = (boolean) session.getAttribute("isFinished");
		Category wordCategory = (Category) session.getAttribute("wordCategory");
		char guess = request.getParameter("guess").charAt(0);
		char[] currentState = currentStateAsStr.toCharArray();
		usedCharacters.add(guess);

		if (gameService.contains(wordToFind, guess)) {
			String wordToReturn = gameService.putLetterOnPlace(wordToFind, guess, currentState);
			onGoingWords.put(wordToFind, new OngoingGame(wordToReturn, triesLeft, wordCategory, usedCharacters, isFinished));
			
			session.setAttribute("currentState", wordToReturn);
			
			if (gameService.isWordGuessed(wordToFind, wordToReturn)) {
				isFinished = true;
				onGoingWords.remove(wordToFind);
				
				session.setAttribute("isFinished", isFinished);
				session.setAttribute("gameStatus", "Congratulations! You Won!");
			}
			
			response.sendRedirect("/gameStarted.jsp");

		} else {
			triesLeft--;
			onGoingWords.put(wordToFind, new OngoingGame(currentStateAsStr, triesLeft, wordCategory, usedCharacters, isFinished));
			
			session.setAttribute("triesLeft", triesLeft);

			if (gameService.checkFailedTries(triesLeft)) {
				isFinished = true;
				onGoingWords.remove(wordToFind);
				
				session.setAttribute("isFinished", isFinished);
				session.setAttribute("gameStatus", "HAHAHA You lost! The word was " + wordToFind.getName() + ".");
			}

			response.sendRedirect("/gameStarted.jsp");
		}

	}

}
