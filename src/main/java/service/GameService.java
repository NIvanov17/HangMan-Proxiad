package service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import enums.Attributes;
import enums.Commands;
import enums.ErrorMessages;
import enums.GameStatus;
import enums.RoleName;
import model.Category;
import model.Game;
import model.GamePlayer;
import model.Player;
import model.Word;
import model.DTOs.GameDTO;
import model.DTOs.PlayerDTO;
import model.DTOs.PlayersDTO;
import repository.GamePlayerRepository;
import repository.GameRepository;

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

	public GameDTO resumeGame(Long gameId) {
		Game game = getById(gameId);
		List<GamePlayer> playerInGames = game.getPlayerInGames();
		Player guesser = getPlayerByRole(playerInGames, RoleName.GUESSER);
		Player giver = getPlayerByRole(playerInGames, RoleName.WORD_GIVER);
		PlayerDTO guesserDTO = new PlayerDTO(guesser.getId(), guesser.getUsername());
		PlayerDTO giverDTO = new PlayerDTO(giver.getId(), giver.getUsername());

		GameDTO dto = new GameDTO(gameId, game.getCurrentState(), game.getTriesLeft(), game.getUsedChars(),
				game.isFinished(), guesserDTO);

		if (game.getMode().equals("Single Player")) {
			return dto;

		} else {
			dto.setGiver(giverDTO);

			return dto;

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

		return gameRepository.findById(gameId).orElseThrow(
				() -> new IllegalArgumentException(String.format(ErrorMessages.GAME_ID_NOT_FOUND, gameId)));
	}

	public GameDTO newGameStarted(String username) {
		Word word = wordService.getRandomGame();
		Player player = playerService.getPlayerByUsername(username);
		return createNewSinglePlayerGame(word, player.getId());
	}

	public GameDTO tryGuess(char guess, long gameId) {
		Game game = getById(gameId);
		Word wordToFind = game.getWord();
		long id = playerService.getPlayerIdByGameId(gameId);
		Player player = playerService.getPlayerById(id);

		int triesLeft = game.getTriesLeft();

		Set<Character> usedChars = game.getUsedChars();
		usedChars.add(guess);
		game.setUsedChars(usedChars);
		PlayerDTO guesserDTO = new PlayerDTO(id, player.getUsername());

		GameDTO dto = new GameDTO(gameId, game.getCurrentState(), triesLeft, usedChars, false, guesserDTO);

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(game, guess);
			game.setCurrentState(wordToReturn);
			dto.setWordProgress(wordToReturn);
			gameRepository.save(game);

			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				dto.setFinished(true);
				playerService.incrementWins(id);
				dto.setGameStatus(GameStatus.GAME_STATUS_WON);
				gameRepository.save(game);
				statisticService.createStatistic(game);
			}
			return dto;
		} else {
			triesLeft--;
			game.setTriesLeft(triesLeft);
			gameRepository.save(game);
			dto.setTriesLeft(triesLeft);

			if (checkFailedTries(triesLeft)) {
				game.setFinished(true);
				game.setTriesLeft(0);
				dto.setFinished(true);
				dto.setTriesLeft(0);
				dto.setGameStatus(GameStatus.GAME_STATUS_LOSE);
				gameRepository.save(game);
				statisticService.createStatistic(game);
			}
			return dto;
		}
	}

	private List<Character> lettersForJSP() {
		return IntStream.rangeClosed('A', 'Z').mapToObj(c -> (char) c).collect(Collectors.toList());
	}

	private Game findById(long id) {
		return gameRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}

	private GameDTO createNewSinglePlayerGame(Word word, long id) {
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
		PlayerDTO guesserDTO = new PlayerDTO(id, player.getUsername());

		GameDTO dto = new GameDTO(game.getId(), wordToReturn, game.getTriesLeft(), hashSet, false, guesserDTO);

		return dto;
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

	public GameDTO prepareWordToBeDisplayed(String wordToGuess, String category, long giverId, long guesserId) {
		Category categoryByName = categoryservice.getCategoryByName(category);

		Word word = wordService.createWord(wordToGuess, categoryByName);
		Game game = createNewMultiPlayerGame(word);
		createPlayer(giverId, game, RoleName.WORD_GIVER);
		createPlayer(guesserId, game, RoleName.GUESSER);
		Player guesser = playerService.getPlayerById(guesserId);
		Player giver = playerService.getPlayerById(giverId);
		PlayerDTO guessDto = new PlayerDTO(guesserId, guesser.getUsername());
		PlayerDTO giverDto = new PlayerDTO(giverId, giver.getUsername());

		GameDTO dto = new GameDTO(game.getId(), game.getCurrentState(), game.getTriesLeft(), game.getUsedChars(),
				game.isFinished(), guessDto);
		dto.setGiver(giverDto);

		return dto;
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

	public GameDTO tryGuessMultiplayer(char guess, long gameId) throws IOException {
		Game game = getById(gameId);
		Word wordToFind = game.getWord();
		Set<Character> usedChars = game.getUsedChars();
		Long guesserId = getPlayerIdByRole(game, RoleName.GUESSER);
		Player guesserById = playerService.getPlayerById(guesserId);
		PlayerDTO guesserDTO = new PlayerDTO(guesserId, guesserById.getUsername());
		Long giverId = getPlayerIdByRole(game, RoleName.WORD_GIVER);
		Player giverById = playerService.getPlayerById(giverId);
		PlayerDTO giverDTO = new PlayerDTO(giverId, giverById.getUsername());
		usedChars.add(guess);
		game.setUsedChars(usedChars);
		GameDTO dto = new GameDTO(gameId, game.getCurrentState(), game.getTriesLeft(), usedChars, game.isFinished(),
				guesserDTO);
		dto.setGiver(giverDTO);

		if (wordService.contains(wordToFind, guess)) {
			String wordToReturn = wordService.putLetterOnPlace(game, guess);
			game.setCurrentState(wordToReturn);
			dto.setWordProgress(wordToReturn);
			gameRepository.save(game);

			if (isWordGuessed(wordToFind, wordToReturn)) {
				game.setFinished(true);
				dto.setFinished(true);
				playerService.incrementWins(guesserId);
				gameRepository.save(game);
				statisticService.createStatistic(game);
				dto.setGameStatus(Commands.CONGRATULATIONS_YOU_WON);

			}
			return dto;
		} else {
			int triesLeft = game.getTriesLeft();
			triesLeft--;
			game.setTriesLeft(triesLeft);
			dto.setTriesLeft(triesLeft);
			gameRepository.save(game);
			if (checkFailedTries(triesLeft)) {
				game.setFinished(true);
				dto.setFinished(true);
				playerService.incrementWins(giverId);
				dto.setGameStatus(Commands.GAME_STATUS_LOSS);
				gameRepository.save(game);
				statisticService.createStatistic(game);
			}
			return dto;
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
		double attempts = allGames.stream().mapToDouble(g -> {
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

	public boolean isUsernameValid(String username) {
		return playerService.isValid(username);
	}

	public PlayersDTO getPlayersWithIDs(long giverId, long guesserId) {
		Player playerGiver = playerService.getPlayerById(giverId);
		Player playerGuesser = playerService.getPlayerById(guesserId);
		PlayerDTO dtoGuesser = new PlayerDTO(playerGuesser.getId(), playerGuesser.getUsername());
		PlayerDTO dtoGiver = new PlayerDTO(playerGiver.getId(), playerGiver.getUsername());
		return new PlayersDTO(dtoGuesser, dtoGiver);

	}

	public boolean isValid(long gameId) {
		return gameRepository.findById(gameId).isPresent();
	}

	public GameDTO getGameCurrentState(long gameId) {
		Game game = getById(gameId);
		Long guesserId = getPlayerIdByRole(game, RoleName.GUESSER);
		Long giverId = getPlayerIdByRole(game, RoleName.WORD_GIVER);
		Player playerGiver = playerService.getPlayerById(giverId);
		Player playerGuesser = playerService.getPlayerById(guesserId);
		PlayerDTO dtoGuesser = new PlayerDTO(playerGuesser.getId(), playerGuesser.getUsername());
		PlayerDTO dtoGiver = new PlayerDTO(playerGiver.getId(), playerGiver.getUsername());

		GameDTO dto = new GameDTO(game.getId(), game.getCurrentState(), game.getTriesLeft(), game.getUsedChars(),
				game.isFinished(), dtoGuesser);
		dto.setGiver(dtoGiver);
		return dto;

	}

}
