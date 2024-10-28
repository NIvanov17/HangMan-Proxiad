package service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	private StatisticService statisticService;

	@Autowired
	public GameService(GameRepository gameRepository, PlayerService playerService, WordService wordService,
			CategoryService categoryservice, GamePlayerRepository gamePlayerRepository,
			StatisticService statisticService) {
		this.gameRepository = gameRepository;
		this.playerService = playerService;
		this.wordService = wordService;
		this.categoryservice = categoryservice;
		this.gamePlayerRepository = gamePlayerRepository;
		this.statisticService = statisticService;
	}

	public String wordWithSpaces(Word wordWithoutSpaces) {
		StringBuilder sb = new StringBuilder();
		String word = wordWithoutSpaces.getName();
		for (int i = 0; i < word.length(); i++) {
			sb.append(word.charAt(i)).append(' ');
		}
		return sb.toString().trim();
	}

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

	public String resumeGame(HttpSession session, Long gameId) {
		Game game = getGameById(gameId);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player guesser = getPlayerByRole(playerInGames, RoleName.GUESSER);
		Player giver = getPlayerByRole(playerInGames, RoleName.GUESSER);

		session.setAttribute("word", game.getWord());
		session.setAttribute("game", game);
		session.setAttribute("guesserId", guesser.getId());
		session.setAttribute("gameStatus", "");

		if (game.getMode().equals("Single Player")) {
			return "gameStarted";

		} else {
			session.setAttribute("giverId", giver.getId());
			return "multiplayerStarted";

		}

	}

	private Player getPlayerByRole(List<GamePlayer> playerInGames, RoleName role) {
		Player player = null;
		for (GamePlayer gamePlayer : playerInGames) {
			if (gamePlayer.getRole() == role) {
				player = gamePlayer.getPlayer();
				break;
			}
		}
		return player;
	}

	private Game getGameById(long gameId) {

		return gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game does not exist!"));
	}

	public String newGameStarted(HttpSession session, String username) throws ServletException, IOException {
		Word word = wordService.getRandomGame();
		Player player = playerService.getPlayerByUsername(username);
		Game game = createNewSinglePlayerGame(word, player.getId());

		session.setAttribute("word", word);
		session.setAttribute("game", game);
		session.setAttribute("id", player.getId());
		session.setAttribute("gameStatus", "");

		return "gameStarted";
	}

	public String tryGuess(char guess, HttpSession session) throws IOException {
		Word wordToFind = (Word) session.getAttribute("word");
		Game game = (Game) session.getAttribute("game");
		Long id = (Long) session.getAttribute("id");
		int triesLeft = game.getTriesLeft();

		Set<Character> usedChars = game.getUsedChars();
		usedChars.add(guess);
		game.setUsedChars(usedChars);

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(game, guess);
			game.setCurrentState(wordToReturn);
			session.setAttribute("word", wordToFind);
			gameRepository.save(game);

			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				playerService.incrementWins(id);
				gameRepository.save(game);
				session.setAttribute("game", game);
				session.setAttribute("gameStatus", Commands.CONGRATULATIONS_YOU_WON);
				statisticService.createStatistic(game);
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
				statisticService.createStatistic(game);
			}
			return "gameStarted";
		}
	}

	private Game findById(long id) {
		return gameRepository.findById(id).get();
	}

	private Game createNewSinglePlayerGame(Word word, long id) {
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

		String wordWithoutSpaces = new String(wordService.wordToReturn(word.getName()));
		String wordToReturn = wordService.wordWithSpaces(wordWithoutSpaces);
		game.setCurrentState(wordToReturn);

		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player player = playerService.getPlayerById(id);
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.setPlayer(player);
		gamePlayer.setGame(game);
		gamePlayer.setRole(RoleName.GUESSER);
		playerInGames.add(gamePlayer);

		gameRepository.save(game);
		gamePlayerRepository.save(gamePlayer);

		return game;
	}

	private Game createNewMultiPlayerGame(Word word) {
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
		String wordWithoutSpaces = new String(wordService.wordToReturn(word.getName()));
		String wordToReturn = wordService.wordWithSpaces(wordWithoutSpaces);
		game.setCurrentState(wordToReturn);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		gameRepository.save(game);

		return game;
	}

	public String prepareWordToBeDisplayed(HttpSession session, String wordToGuess, String category, long giverId,
			long guesserId) throws ServletException, IOException {
		Category categoryByName = categoryservice.getCategoryByName(category);

		Word word = wordService.createWord(wordToGuess, categoryByName);
		Game game = createNewMultiPlayerGame(word);
		createPlayer(giverId, game, RoleName.WORD_GIVER);
		createPlayer(guesserId, game, RoleName.GUESSER);

		session.setAttribute("gameStatus", "");
		session.setAttribute("word", word);
		session.setAttribute("game", game);
		session.setAttribute("giverId", giverId);
		session.setAttribute("guesserId", guesserId);
		session.setAttribute("isWordValid", true);

		return "multiplayerStarted";
	}

	private void createPlayer(long id, Game game, RoleName role) {
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player player = playerService.getPlayerById(id);
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.setPlayer(player);
		gamePlayer.setGame(game);
		gamePlayer.setRole(role);
		playerInGames.add(gamePlayer);
		gamePlayerRepository.save(gamePlayer);
	}

	public String tryGuessMultiplayer(char guess, HttpSession session) throws IOException {

		Word wordToFind = (Word) session.getAttribute("word");
		Game game = (Game) session.getAttribute("game");
		Set<Character> usedChars = game.getUsedChars();
		Long guesserId = getPlayerIdByRole(game,RoleName.GUESSER);
		
		Long giverId = getPlayerIdByRole(game, RoleName.WORD_GIVER);
		

		char[] currentState = game.getCurrentState().toCharArray();
		usedChars.add(guess);
		game.setUsedChars(usedChars);

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(game, guess);
			game.setCurrentState(wordToReturn);
			gameRepository.save(game);

			session.setAttribute("currentState", wordToReturn);
			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				playerService.incrementWins(guesserId);
				gameRepository.save(game);
				statisticService.createStatistic(game);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.CONGRATULATIONS_YOU_WON);

			}
			return "redirect:/{giverId}/{guesserId}/multiplayer/game";
		} else {
			int triesLeft = game.getTriesLeft();
			triesLeft--;
			game.setTriesLeft(triesLeft);
			gameRepository.save(game);
			session.setAttribute("triesLeft", triesLeft);
			if (checkFailedTries(triesLeft)) {
				game.setFinished(true);
				playerService.incrementWins(giverId);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.GAME_STATUS_LOSS + wordToFind.getName() + ".");
				gameRepository.save(game);
				statisticService.createStatistic(game);
			}
			return "redirect:/{giverId}/{guesserId}/multiplayer/game";
		}
	}

	private Long getPlayerIdByRole(Game game, RoleName role) {
		return game.getPlayerInGames().stream()
				.filter(gamePlayer -> gamePlayer.getRole() == role)
				.map(gamePlayer -> gamePlayer.getPlayer().getId())
				.findFirst()
				.orElse(null);
	}

	public static String generateRandomString(int length) {
		Random random = new Random();
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			result.append(Commands.CHARACTERS.charAt(random.nextInt(Commands.CHARACTERS.length())));
		}

		return result.toString();
	}

	public List<Game> getAllGamesForPlayerByUsername(String username, RoleName guesser) {

		Player playerByUsername = playerService.getPlayerByUsername(username);
		long id = playerByUsername.getId();
		return findAllGamesForPlayerById(id, guesser).stream().map(GamePlayer::getGame).collect(Collectors.toList());

	}

	private List<GamePlayer> findAllGamesForPlayerById(long id, RoleName guesser) {
		return gamePlayerRepository.findAllGamesForPlayerWithId(id, guesser);
	}

	public boolean containsWord(Word word) {
		return gameRepository.findWordById(word.getId()).isPresent() ? true : false;
	}

	public List<Game> getTopTenGames() {
		Pageable topTen = PageRequest.of(0, 10);
		return gameRepository.findTopTenGames(topTen);
	}

	public double averageAttempts() {
		List<Game> games = getTopTenGames();
		double attempts = 0.0;
		for (Game game : games) {
			attempts += Math.abs(game.getTriesLeft() - 6);
		}

		return attempts / 10;
	}

	public String calculateWinLossRatio() {
		List<Game> games = getTopTenGames();
		int wins = 0;
		int loss = 0;

		for (Game game : games) {
			String status = game.getStatistic().getStatus();
			if (status.equals("WON")) {
				wins++;
			} else {
				loss++;
			}
		}
		return wins + "/" + loss;
	}

}
