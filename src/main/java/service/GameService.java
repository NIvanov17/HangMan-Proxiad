package service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enums.Commands;
import enums.RoleName;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Game;
import model.GamePlayer;
import model.Player;
import model.Word;
import repository.GamePlayerRepository;
import repository.GameRepository;
import repository.WordRepository;

@Service
public class GameService {
	private GameRepository gameRepository;
	private PlayerService playerService;
	private WordService wordService;
	private CategoryService categoryservice;
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	public GameService(GameRepository gameRepository, PlayerService playerService, WordService wordService,
			CategoryService categoryservice, GamePlayerRepository gamePlayerRepository) {
		this.gameRepository = gameRepository;
		this.playerService = playerService;
		this.wordService = wordService;
		this.categoryservice = categoryservice;
		this.gamePlayerRepository = gamePlayerRepository;
	}

	public String wordWithSpaces(Word wordWithoutSpaces) {
		StringBuilder sb = new StringBuilder();
		String word = wordWithoutSpaces.getName();
		for (int i = 0; i < word.length(); i++) {
			sb.append(word.charAt(i)).append(' ');
		}
		return sb.toString().trim();
	}

//	public String putLetterOnPlace(Word wordToFind, char guess, char[] currentState) {
//		StringBuilder sb = new StringBuilder();
//		String currentstateWithoutSpaces = new String(currentState);
//		currentstateWithoutSpaces = currentstateWithoutSpaces.replaceAll(" ", "");
//
//		for (int i = 0; i < wordToFind.getWord().length(); i++) {
//			if (currentstateWithoutSpaces.charAt(i) >= Commands.SMALL_A
//					&& currentstateWithoutSpaces.charAt(i) <= Commands.SMALL_Z) {
//				sb.append(currentstateWithoutSpaces.charAt(i));
//			} else if (wordToFind.getWord().charAt(i) == guess) {
//				sb.append(wordToFind.getWord().charAt(i));
//			} else {
//				sb.append('_');
//			}
//		}
//
//		String currentWordWithSpaces = wordWithSpaces(sb.toString().trim());
//		return currentWordWithSpaces;
//	}

	public boolean checkFailedTries(int triesLeft) {
		return triesLeft == 0;
	}

	public boolean isWordGuessed(Word wordToFind, String currentState) {
		return currentState.equals(wordWithSpaces(wordToFind));
	}

	public char getFirstLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(0);
	}

	public char getLastLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(wordWithoutSpaces.length() - 1);
	}

//	public void restartGame(HttpSession session, Set<Character> usedCharacters) {
//		word = wordRepository.getRandomGame();
//		usedCharacters.removeAll(usedCharacters);
//		String currentState = wordWithSpaces(new String(wordToReturn(word.getWord())));
//		if (wordRepository.getHistory().containsKey(word)) {
//			word = wordRepository.getRandomGame();
//		}
//		session.setAttribute("word", word.getWord());
//		session.setAttribute("currentState", currentState);
//		session.setAttribute("triesLeft", 6);
//		session.setAttribute("gameStatus", "");
//		session.setAttribute("isFinished", false);
//		session.setAttribute("usedCharacters", usedCharacters);
//
//	}

	public boolean containsOtherLetters(String wordToGuess) {
		String copy = String.valueOf(wordToGuess);
		copy = copy.replaceAll(Character.toString(getFirstLetter(wordToGuess)), "");
		copy = copy.replaceAll(Character.toString(getLastLetter(wordToGuess)), "");
		if (copy.isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean isWordValid(String wordToGuess) {
		if (wordToGuess.length() < Commands.MIN_LENGHT || !containsOnlyLetters(wordToGuess)) {
			return false;
		}
		if (containsOtherLetters(wordToGuess)) {
			return true;
		}
		return false;
	}

	public boolean containsOnlyLetters(String wordToGuess) {
		for (char symbol : wordToGuess.toCharArray()) {
			if (Character.isLetter(symbol)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public String resumeGame(HttpSession session, Long gameId){
		Game game = getGameById(gameId);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player player = null;

		for (GamePlayer gamePlayer : playerInGames) {
			if (gamePlayer.getRole() == RoleName.GUESSER) {
				player = gamePlayer.getPlayer();
				break;
			}
		}
			session.setAttribute("word", game.getWord());
			session.setAttribute("game", game);
			session.setAttribute("username", player.getUsername());
			session.setAttribute("gameStatus", "");

			if (game.getMode().equals("Single Player")) {
				return "gameStarted";

			} else {
				return "multiplayerStarted";

			}
		
	}

	private Game getGameById(long gameId) {

		return gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game does not exist!"));
	}

	public String newGameStarted(HttpSession session, String username) throws ServletException, IOException {
		Word word = wordService.getRandomGame();
		Game game = createNewSinglePlayerGame(word, username);

		session.setAttribute("word", word);
		session.setAttribute("game", game);
		session.setAttribute("username", username);
		session.setAttribute("gameStatus", "");

		return "gameStarted";
	}

	public String tryGuess(char guess, HttpSession session) throws IOException {
		Word wordToFind = (Word) session.getAttribute("word");
		Game game = (Game) session.getAttribute("game");
		String username = (String) session.getAttribute("username");
		int triesLeft = game.getTriesLeft();

		Set<Character> usedChars = game.getUsedChars();
		usedChars.add(guess);
		game.setUsedChars(usedChars);

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(wordToFind, guess);
			wordToFind.setCurrentState(wordToReturn);
			session.setAttribute("word", wordToFind);
			gameRepository.save(game);

			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				playerService.incrementWins(username);
				gameRepository.save(game);
				session.setAttribute("game", game);
				session.setAttribute("gameStatus", Commands.CONGRATULATIONS_YOU_WON);

			}
			return "gameStarted";
		} else {
			triesLeft--;
			game.setTriesLeft(triesLeft);
			gameRepository.save(game);

			session.setAttribute("game", game);
			if (checkFailedTries(triesLeft)) {
				game.setFinished(true);
				game.setTriesLeft(0);
				gameRepository.save(game);
				session.setAttribute("game", game);
				session.setAttribute("gameStatus", Commands.GAME_STATUS_LOSS + wordToFind.getName() + ".");

			}
			return "gameStarted";
		}
	}

	private Game findById(long id) {
		return gameRepository.findById(id).get();
	}

	private Game createNewSinglePlayerGame(Word word, String username) {
		char firstLetter = getFirstLetter(word.getName());
		char lastLetter = getLastLetter(word.getName());
		HashSet<Character> hashSet = new HashSet<>();
		hashSet.add(firstLetter);
		hashSet.add(lastLetter);
		Game game = new Game();
		game.setFinished(false);
		game.setMode("Single Player");
		game.setTriesLeft(6);
		game.setUsedChars(hashSet);
		game.setWord(word);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player player = playerService.getPlayerByUsername(username);
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.setPlayer(player);
		gamePlayer.setGame(game);
		gamePlayer.setRole(RoleName.GUESSER);
		playerInGames.add(gamePlayer);

		gameRepository.save(game);
		gamePlayerRepository.save(gamePlayer);

		return game;
	}

	private Game createNewMultiPlayerGame(Word word, String username, RoleName role) {
		char firstLetter = getFirstLetter(word.getName());
		char lastLetter = getLastLetter(word.getName());
		HashSet<Character> hashSet = new HashSet<>();
		hashSet.add(firstLetter);
		hashSet.add(lastLetter);
		Game game = new Game();
		game.setFinished(false);
		game.setMode("Multiplayer");
		game.setTriesLeft(6);
		game.setUsedChars(hashSet);
		game.setWord(word);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player player = playerService.getPlayerByUsername(username);
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.setPlayer(player);
		gamePlayer.setGame(game);
		gamePlayer.setRole(role);
		playerInGames.add(gamePlayer);

		gameRepository.save(game);
		gamePlayerRepository.save(gamePlayer);

		return game;
	}

	public boolean containsWord(Word word) {
		return gameRepository.findWordById(word.getId()).isPresent() ? true : false;
	}

	public String prepareWordToBeDisplayed(HttpSession session, String wordToGuess, String category,
			String giverUsername, String guesserUsername) throws ServletException, IOException {
		Category categoryByName = categoryservice.getCategoryByName(category);

		Word word = wordService.createWord(wordToGuess, categoryByName);
		Game wordGiverGame = createNewMultiPlayerGame(word, giverUsername, RoleName.WORD_GIVER);
		Game wordGuesserGame = createNewMultiPlayerGame(word, guesserUsername, RoleName.GUESSER);

		session.setAttribute("gameStatus", "");
		session.setAttribute("word", word);
		session.setAttribute("giverGame", wordGiverGame);
		session.setAttribute("guesserGame", wordGuesserGame);
		session.setAttribute("triesLeft", wordGiverGame.getTriesLeft());
		session.setAttribute("mode", wordGiverGame.getMode());
		session.setAttribute("giverUsername", giverUsername);
		session.setAttribute("guesserUsername", guesserUsername);
		session.setAttribute("isWordValid", true);

		return "multiplayerStarted";
	}

	public String tryGuessMultiplayer(char guess, HttpSession session) throws IOException {

		Word wordToFind = (Word) session.getAttribute("word");
		Game wordGiverGame = (Game) session.getAttribute("giverGame");
		Game wordGuesserGame = (Game) session.getAttribute("guesserGame");
		Set<Character> usedChars = wordGuesserGame.getUsedChars();
		String guesserUsername = null;
		String giverUsername = null;
		for (GamePlayer gamePlayer : wordGuesserGame.getPlayerInGames()) {
			if (gamePlayer.getRole() == RoleName.GUESSER) {
				guesserUsername = gamePlayer.getPlayer().getUsername();
			}
		}
		for (GamePlayer gamePlayer : wordGiverGame.getPlayerInGames()) {
			if (gamePlayer.getRole() == RoleName.WORD_GIVER) {
				giverUsername = gamePlayer.getPlayer().getUsername();
			}
		}

		char[] currentState = wordToFind.getCurrentState().toCharArray();
		usedChars.add(guess);
		wordGiverGame.setUsedChars(usedChars);
		wordGuesserGame.setUsedChars(usedChars);

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(wordToFind, guess);
			wordToFind.setCurrentState(wordToReturn);

			session.setAttribute("currentState", wordToReturn);
			if (isWordGuessed(wordToFind, wordToReturn)) {
				wordGiverGame.setFinished(true);
				wordGuesserGame.setFinished(true);
				playerService.incrementWins(guesserUsername);
				gameRepository.save(wordGiverGame);
				gameRepository.save(wordGuesserGame);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.CONGRATULATIONS_YOU_WON);

			}
			return "redirect:/{giverUsername}/{guesserUsername}/multiplayer/game";
		} else {
			int triesLeft = wordGuesserGame.getTriesLeft();
			triesLeft--;
			wordGiverGame.setTriesLeft(wordGiverGame.getTriesLeft() - 1);
			wordGuesserGame.setTriesLeft(triesLeft);
			gameRepository.save(wordGuesserGame);
			gameRepository.save(wordGiverGame);
			session.setAttribute("triesLeft", triesLeft);
			if (checkFailedTries(triesLeft)) {
				wordGiverGame.setFinished(true);
				wordGuesserGame.setFinished(true);
				playerService.incrementWins(giverUsername);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.GAME_STATUS_LOSS + wordToFind.getName() + ".");

			}
			return "redirect:/{giverUsername}/{guesserUsername}/multiplayer/game";
		}
	}

	public static String generateRandomString(int length) {
		Random random = new Random();
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			result.append(Commands.CHARACTERS.charAt(random.nextInt(Commands.CHARACTERS.length())));
		}

		return result.toString();
	}

	public List<Game> getAllGamesForPlayerByUsername(String username) {

		Player playerByUsername = playerService.getPlayerByUsername(username);
		long id = playerByUsername.getId();
		return findAllGamesForPlayerById(id).stream().map(GamePlayer::getGame).collect(Collectors.toList());

	}

	private List<GamePlayer> findAllGamesForPlayerById(long id) {
		return gamePlayerRepository.findAllGamesForPlayerWithId(id);
	}

}
