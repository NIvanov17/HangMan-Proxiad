package service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import enums.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;

public class GameService {
	private static final int SMALL_Z = 122;
	private static final int SMALL_A = 97;
	private static GameService gameService;
	private static WordsRepository wordsRepository;
	private Game game;

	private GameService(WordsRepository wordsRepository) {
		this.wordsRepository = wordsRepository;
	}

	public static GameService getGameService() {
		if (gameService == null) {
			gameService = new GameService(wordsRepository);
		}
		return gameService;
	}

	public String wordWithSpaces(Game wordWithoutSpaces) {
		StringBuilder sb = new StringBuilder();
		String word = wordWithoutSpaces.getWord();
		for (int i = 0; i < word.length(); i++) {
			sb.append(word.charAt(i)).append(' ');
		}
		return sb.toString().trim();
	}

	public char[] wordToReturn(String word) {
		char[] currentState = new char[word.length()];
		currentState[0] = word.charAt(0);
		currentState[word.length() - 1] = word.charAt(word.length() - 1);

		for (int i = 1; i < word.length() - 1; i++) {
			if (word.charAt(i) == currentState[0]) {
				currentState[i] = word.charAt(i);
				continue;
			} else if (word.charAt(i) == currentState[word.length() - 1]) {
				currentState[i] = word.charAt(i);
				continue;
			} else {
				currentState[i] = '_';
			}

		}

		return currentState;
	}

	public boolean contains(Game wordToFind, char guess) {
		String word = wordToFind.getWord();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess) {
				return true;
			}

		}
		return false;
	}

	public String putLetterOnPlace(Game wordToFind, char guess, char[] currentState) {
		StringBuilder sb = new StringBuilder();
		String currentstateWithoutSpaces = new String(currentState);
		currentstateWithoutSpaces = currentstateWithoutSpaces.replaceAll(" ", "");

		for (int i = 0; i < wordToFind.getWord().length(); i++) {
			if (currentstateWithoutSpaces.charAt(i) >= SMALL_A && currentstateWithoutSpaces.charAt(i) <= SMALL_Z) {
				sb.append(currentstateWithoutSpaces.charAt(i));
			} else if (wordToFind.getWord().charAt(i) == guess) {
				sb.append(wordToFind.getWord().charAt(i));
			} else {
				sb.append('_');
			}
			// System.out.println(sb.toString());
		}

		String currentWordWithSpaces = wordWithSpaces(sb.toString().trim());
		return currentWordWithSpaces;
	}

	public boolean checkFailedTries(int triesLeft) {
		return triesLeft == 0;
	}

	public boolean isWordGuessed(Game wordToFind, String currentState) {
		return currentState.equals(wordWithSpaces(wordToFind));
	}

	public char getFirstLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(0);
	}

	public char getLastLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(wordWithoutSpaces.length() - 1);
	}

	public void restartGame(HttpSession session, Set<Character> usedCharacters) {
		game = wordsRepository.getRandomGame();
		usedCharacters.removeAll(usedCharacters);
		String currentState = wordWithSpaces(new String(wordToReturn(game.getWord())));
		System.out.println("New word after restart: " + game.getWord());
		System.out.println("Restart method called and clean the used: " + usedCharacters);
		if (wordsRepository.getHistory().containsKey(game)) {
			game = wordsRepository.getRandomGame();
		}
		String word = game.getWord();
		session.setAttribute("word", game.getWord());
		session.setAttribute("currentState", currentState);
		session.setAttribute("triesLeft", 6);
		session.setAttribute("gameStatus", "");
		session.setAttribute("isFinished", false);
		session.setAttribute("usedCharacters", usedCharacters);
	}

	public String wordWithSpaces(String word) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			sb.append(word.charAt(i)).append(' ');
		}
		return sb.toString().trim();
	}

	public void resumeGame(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Map<Game, History> history) throws ServletException, IOException {
		String wordToSet = (String) request.getParameter("currentWord");
		Game selectedgame = null;

		for (Game g : history.keySet()) {

			if (g.getWord().equals(wordToSet)) {
				selectedgame = g;
				break;
			}
		}
		History gamesHistory = history.get(selectedgame);
		session.setAttribute("word", selectedgame);
		session.setAttribute("currentState", gamesHistory.getWordState());
		session.setAttribute("triesLeft", gamesHistory.getTriesLeft());
		session.setAttribute("isFinished", gamesHistory.isFinished());
		session.setAttribute("usedCharacters", gamesHistory.getUsedChars());
		session.setAttribute("wordCategory", gamesHistory.getCategory());
		session.setAttribute("gameStatus", "");
		// forward
		request.getRequestDispatcher("/gameStarted.jsp").forward(request, response);
	}

	public void newGameStarted(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Map<Game, History> history) throws ServletException, IOException {
		Game game = wordsRepository.getRandomGame();
		if (history.containsKey(game)) {
			game = wordsRepository.getRandomGame();
		}
		Category wordCategory = game.getCategory();
		String wordWithoutSpaces = new String(wordToReturn(game.getWord()));
		String wordToReturn = wordWithSpaces(wordWithoutSpaces);
		char firstLetter = getFirstLetter(wordWithoutSpaces);
		char lastLetter = getLastLetter(wordWithoutSpaces);
		Set<Character> usedCharacters = new HashSet<Character>();
		usedCharacters.add(firstLetter);
		usedCharacters.add(lastLetter);
		int triesLeft = 6;
		String mode = "Single Player";

		session.setAttribute("word", game);
		session.setAttribute("wordCategory", wordCategory);
		session.setAttribute("triesLeft", triesLeft);
		session.setAttribute("currentState", wordToReturn);
		session.setAttribute("isFinished", false);
		session.setAttribute("usedCharacters", usedCharacters);
		session.setAttribute("mode", mode);
		request.getRequestDispatcher("/gameStarted.jsp").forward(request, response);
	}

	public void tryGuess(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Set<Character> usedCharacters, Map<Game, History> history) throws IOException {
		Game wordToFind = (Game) session.getAttribute("word");
		int triesLeft = (int) session.getAttribute("triesLeft");
		String currentStateAsStr = (String) session.getAttribute("currentState");
		boolean isFinished = (boolean) session.getAttribute("isFinished");
		Category wordCategory = (Category) session.getAttribute("wordCategory");
		String mode = (String) session.getAttribute("mode");
		char guess = request.getParameter("guess").charAt(0);
		char[] currentState = currentStateAsStr.toCharArray();
		usedCharacters.add(guess);

		if (contains(wordToFind, guess)) {
			String wordToReturn = putLetterOnPlace(wordToFind, guess, currentState);
			history.put(wordToFind,
					new History(wordToReturn, triesLeft, wordCategory, usedCharacters, isFinished, mode));

			session.setAttribute("currentState", wordToReturn);

			if (isWordGuessed(wordToFind, wordToReturn)) {

				history.get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", "Congratulations! You Won!");
			}
			response.sendRedirect("/gameStarted.jsp");
		} else {
			triesLeft--;
			history.put(wordToFind,
					new History(currentStateAsStr, triesLeft, wordCategory, usedCharacters, isFinished, mode));

			session.setAttribute("triesLeft", triesLeft);
			if (checkFailedTries(triesLeft)) {
				history.get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", "HAHAHA You lost! The word was " + wordToFind.getWord() + ".");
			}
			response.sendRedirect("/gameStarted.jsp");
		}
	}

	private boolean containsOtherLetters(String wordToGuess, char firstLetter, char lastLetter) {
		for (char letter : wordToGuess.toCharArray()) {
			if (letter != firstLetter && letter != lastLetter) {
				return true;
			} else if (firstLetter != lastLetter) {
				if (letter != firstLetter) {
					return true;
				} else if (letter != lastLetter) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isWordValid(String wordToGuess) {
		char firstLetter = getFirstLetter(wordToGuess);
		char lastLetter = getLastLetter(wordToGuess);
		if (wordToGuess.length() < 3) {
			return false;
		}
		if (containsOtherLetters(wordToGuess, firstLetter, lastLetter)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean historyContainsWord(Map<Game, History> history, String wordToGuess) {
		for (Map.Entry<Game, History> e : history.entrySet()) {
	        if (e.getKey().getWord().equals(wordToGuess)) {
	            return true; 
	        }
	    }
	    return false; 
	}
}
