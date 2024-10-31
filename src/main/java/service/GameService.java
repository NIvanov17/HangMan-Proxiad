package service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import enums.Attributes;
import enums.Commands;
import enums.ErrorMessages;
import enums.GameStatus;
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
		return copy.isEmpty() ? false : true;
	}

	public boolean isWordValid(String wordToGuess) {
		if (wordToGuess.length() < Commands.MIN_LENGHT || !containsOnlyLetters(wordToGuess)) {
			return false;
		}

		return containsOtherLetters(wordToGuess);
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

	public String resumeGame(Model model, Long gameId) {
		Game game = getById(gameId);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player guesser = getPlayerByRole(playerInGames, RoleName.GUESSER);
		Player giver = getPlayerByRole(playerInGames, RoleName.WORD_GIVER);

		model.addAttribute(Attributes.WORD, game.getWord());
		model.addAttribute(Attributes.GAME, game);
		model.addAttribute(Attributes.GUESSER_ID, guesser.getId());
		model.addAttribute(Attributes.LETTERS, lettersForJSP());
		model.addAttribute(Attributes.GAME_STATUS, "");

		if (game.getMode().equals("Single Player")) {
			return "gameStarted";

		} else {
			model.addAttribute("giverId", giver.getId());
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

	private Game getById(long gameId) {

		return gameRepository.findById(gameId)
				.orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.GAME_ID_NOT_FOUND, gameId)));
	}

	public String newGameStarted(Model model, String username) {
		Word word = wordService.getRandomGame();
		Player player = playerService.getPlayerByUsername(username);
		Game game = createNewSinglePlayerGame(word, player.getId());

		model.addAttribute(Attributes.WORD, word);
		model.addAttribute(Attributes.GAME, game);
		model.addAttribute(Attributes.IS_FINISHED, game.isFinished());
		model.addAttribute(Attributes.MODE, game.getMode());
		model.addAttribute(Attributes.ID, game.getId());
		model.addAttribute(Attributes.GAME_STATUS, "");
		model.addAttribute(Attributes.LETTERS, lettersForJSP());

		return "gameStarted";
	}

	public String tryGuess(char guess, Model model, long gameId) throws IOException {
		Game game = getById(gameId);
		Word wordToFind = game.getWord();
		long id = playerService.getPlayerIdByGameId(gameId);
		int triesLeft = game.getTriesLeft();

		Set<Character> usedChars = game.getUsedChars();
		usedChars.add(guess);
		game.setUsedChars(usedChars);

		model.addAttribute("letters", lettersForJSP());

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(game, guess);
			game.setCurrentState(wordToReturn);
			model.addAttribute(Attributes.IS_FINISHED, game.isFinished());
			model.addAttribute(Attributes.WORD, wordToFind);
			model.addAttribute(Attributes.GAME, game);

			gameRepository.save(game);

			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				playerService.incrementWins(id);
				gameRepository.save(game);
				model.addAttribute(Attributes.GAME, game);
				model.addAttribute(Attributes.IS_FINISHED, game.isFinished());
				model.addAttribute(Attributes.GAME_STATUS, Commands.CONGRATULATIONS_YOU_WON);
				statisticService.createStatistic(game);
			}
			return "gameStarted";
		} else {
			triesLeft--;
			game.setTriesLeft(triesLeft);
			gameRepository.save(game);
			model.addAttribute("isFinished", game.isFinished());

			model.addAttribute(Attributes.GAME, game);
			if (checkFailedTries(triesLeft)) {
				game.setFinished(true);
				game.setTriesLeft(0);
				gameRepository.save(game);
				model.addAttribute(Attributes.GAME, game);
				model.addAttribute(Attributes.IS_FINISHED, game.isFinished());
				model.addAttribute(Attributes.GAME_STATUS, Commands.GAME_STATUS_LOSS + wordToFind.getName() + ".");
				statisticService.createStatistic(game);
			}
			return "gameStarted";
		}
	}

	private List<Character> lettersForJSP() {
		return IntStream.rangeClosed('A', 'Z').mapToObj(c -> (char) c).collect(Collectors.toList());
	}

	private Game findById(long id) {
		return gameRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}

	private Game createNewSinglePlayerGame(Word word, long id) {
		char firstLetter = getFirstLetter(word.getName());
		char lastLetter = getLastLetter(word.getName());
		HashSet<Character> hashSet = new HashSet<>();
		hashSet.add(firstLetter);
		hashSet.add(lastLetter);
		Game game = new Game();
		game.setFinished(false);
		game.setMode(Commands.SINGLE_PLAYER_MODE);
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
		game.setMode(Commands.MULTI_PLAYER_MODE);
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

	public String prepareWordToBeDisplayed(Model model, String wordToGuess, String category, long giverId,
			long guesserId) {
		Category categoryByName = categoryservice.getCategoryByName(category);

		Word word = wordService.createWord(wordToGuess, categoryByName);
		Game game = createNewMultiPlayerGame(word);
		createPlayer(giverId, game, RoleName.WORD_GIVER);
		createPlayer(guesserId, game, RoleName.GUESSER);

		model.addAttribute(Attributes.GAME_STATUS, "");
		model.addAttribute(Attributes.WORD, word);
		model.addAttribute(Attributes.GAME, game);
		model.addAttribute(Attributes.LETTERS, lettersForJSP());
		model.addAttribute(Attributes.GIVVER_ID, giverId);
		model.addAttribute(Attributes.GUESSER_ID, guesserId);
		model.addAttribute(Attributes.IS_WORD_VALID, true);

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

	public String tryGuessMultiplayer(char guess, RedirectAttributes redirectAttributes, long gameId)
			throws IOException {
		Game game = getById(gameId);
		Word wordToFind = game.getWord();
		Set<Character> usedChars = game.getUsedChars();
		Long guesserId = getPlayerIdByRole(game, RoleName.GUESSER);
		Long giverId = getPlayerIdByRole(game, RoleName.WORD_GIVER);
		usedChars.add(guess);
		game.setUsedChars(usedChars);
		redirectAttributes.addFlashAttribute(Attributes.LETTERS, lettersForJSP());

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(game, guess);
			game.setCurrentState(wordToReturn);
			redirectAttributes.addFlashAttribute(Attributes.IS_FINISHED, game.isFinished());
			redirectAttributes.addFlashAttribute(Attributes.GAME, game);
			gameRepository.save(game);

			redirectAttributes.addFlashAttribute(Attributes.CURRENT_STATE, wordToReturn);
			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				playerService.incrementWins(guesserId);
				gameRepository.save(game);
				statisticService.createStatistic(game);
				redirectAttributes.addFlashAttribute(Attributes.GAME, game);
				redirectAttributes.addFlashAttribute(Attributes.IS_FINISHED, true);
				redirectAttributes.addFlashAttribute(Attributes.GAME_STATUS, Commands.CONGRATULATIONS_YOU_WON);

			}
			return "redirect:/{giverId}/{guesserId}/multiplayer/game";
		} else {
			int triesLeft = game.getTriesLeft();
			triesLeft--;
			game.setTriesLeft(triesLeft);
			gameRepository.save(game);
			redirectAttributes.addFlashAttribute(Attributes.GAME, game);
			redirectAttributes.addFlashAttribute(Attributes.IS_FINISHED, game.isFinished());
			redirectAttributes.addFlashAttribute(Attributes.TRIES_LEFT, triesLeft);
			if (checkFailedTries(triesLeft)) {
				game.setFinished(true);
				playerService.incrementWins(giverId);
				redirectAttributes.addFlashAttribute(Attributes.GAME, game);
				redirectAttributes.addFlashAttribute(Attributes.IS_FINISHED, true);
				redirectAttributes.addFlashAttribute(Attributes.GAME_STATUS,
						Commands.GAME_STATUS_LOSS + wordToFind.getName() + ".");
				gameRepository.save(game);
				statisticService.createStatistic(game);
			}
			return "redirect:/{giverId}/{guesserId}/multiplayer/game";
		}
	}

	private Long getPlayerIdByRole(Game game, RoleName role) {
		return game.getPlayerInGames().stream().filter(gamePlayer -> gamePlayer.getRole() == role)
				.map(gamePlayer -> gamePlayer.getPlayer().getId()).findFirst().orElse(null);
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
		return gameRepository.findById(word.getId()).isPresent();
	}

	public List<String> getTopTenGames() {
		Pageable topTen = PageRequest.of(0, 10);
		gameRepository.findTopUsedWordIds(topTen);

		return gameRepository.getTopTenGames(topTen);
	}

	public double averageAttempts() {
		List<Game> allGames = gameRepository.findByIsFinishedTrue();
		double attempts = allGames.stream()
		.mapToDouble(g->{
			return Math.abs(g.getTriesLeft() - Commands.TOTAL_TRIES);
		}).sum();

		return attempts / 10;
	}

	public String calculateWinLossRatio() {
		List<Game> allGames = gameRepository.findByIsFinishedTrue();
		int wins = 0;
		int loss = 0;

		for (Game game : allGames) {
			String status = game.getStatistic().getStatus();
			if (status.equals(GameStatus.GAME_STATUS_WON)) {
				wins++;
			} else {
				loss++;
			}
		}
		return wins + "/" + loss;
	}

}
