package com.example.controller.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.example.HangManApp;
import com.example.enums.CategoryName;
import com.example.enums.Commands;
import com.example.enums.RoleName;
import com.example.model.Category;
import com.example.model.Game;
import com.example.model.GamePlayer;
import com.example.model.Player;
import com.example.model.Word;
import com.example.model.DTOs.CreateGameInputDTO;
import com.example.model.DTOs.GameDTO;
import com.example.model.DTOs.GuessDTO;
import com.example.model.DTOs.LoginDTO;
import com.example.model.DTOs.MultiPlayerGameInputDTO;
import com.example.model.DTOs.PlayerDTO;
import com.example.model.DTOs.UpdateGameDTO;
import com.example.repository.CategoryRepository;
import com.example.repository.GamePlayerRepository;
import com.example.repository.GameRepository;
import com.example.repository.PlayerRepository;
import com.example.repository.WordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = HangManApp.class)
class GameAPIControllerTest {

	@LocalServerPort
	private int port;

	private Game testGame;

	private Category category;

	private Word word;

	private Player guesser;

	private Player giver;

	private GamePlayer gamePlayerGuesser;

	List<GamePlayer> gamePlayers;

	private GamePlayer gamePlayerGiver;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	private GameDTO expectedGameDTO;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;

		category = categoryRepository.findByCategoryName(CategoryName.TOOLS).orElseGet(()->{
			category = new Category();
			category.setCategoryName(CategoryName.TOOLS);
			return categoryRepository.save(category);
		});

		word = new Word();
		word.setName("testing");
		word.setCategory(category);
		wordRepository.save(word);

		guesser = playerRepository.findByUsername("testov").orElseGet(() -> {
			Player newPlayer = new Player();
			newPlayer.setUsername("testov");
			return playerRepository.save(newPlayer);
		});

		testGame = new Game();
		testGame.setWord(word);
		testGame.setTriesLeft(6);
		testGame.setFinished(false);
		testGame.setMode(Commands.SINGLE_PLAYER_MODE);
		testGame.setCurrentState("t _ _ t _ _ g");
		testGame = gameRepository.save(testGame);

		expectedGameDTO = new GameDTO(testGame.getId(), testGame.getCurrentState(), testGame.getTriesLeft(),
				testGame.getUsedChars(), false, new PlayerDTO(guesser.getId(), guesser.getUsername()));
		expectedGameDTO.setGameMode(Commands.SINGLE_PLAYER_MODE);

		gamePlayerGuesser = new GamePlayer();
		gamePlayerGuesser.setGame(testGame);
		gamePlayerGuesser.setPlayer(guesser);
		gamePlayerGuesser.setRole(RoleName.GUESSER);
		gamePlayerRepository.save(gamePlayerGuesser);

		gamePlayers = new ArrayList<>();
		gamePlayers.add(gamePlayerGuesser);
		testGame.setPlayerInGames(gamePlayers);
		guesser.setGamesWithRoles(gamePlayers);
	}

	@AfterEach
	public void tearDownd() {
		gamePlayerRepository.deleteById(gamePlayerGuesser.getId());
		gameRepository.deleteById(testGame.getId());
		wordRepository.deleteById(word.getId());
		playerRepository.deleteById(guesser.getId());

	}

	@Test
	void testResumeSinglePlayerGameSuccess() {
		given().queryParam("id", testGame.getId()).accept(ContentType.JSON).when().get("/api/v1/games").then()
				.statusCode(200).contentType(ContentType.JSON).body("gameId", equalTo((int) testGame.getId()))
				.body("triesLeft", equalTo(testGame.getTriesLeft())).body("finished", equalTo(testGame.isFinished()))
				.body("gameMode", equalTo(testGame.getMode().toString()))
				.body("wordProgress", equalTo(testGame.getCurrentState()))
				.body("_links.self.href", equalTo("http://localhost:" + port + "/api/v1/games?id=" + testGame.getId()))
				.body("_links.startNewGame.href", equalTo("http://localhost:" + port + "/api/v1/games"))
				.body("_links.makeGuess.href",
						equalTo("http://localhost:" + port + "/api/v1/games/" + testGame.getId()));
	}

	@Test
	void testResumeMultiPlayerGameSuccess() {
		giver = playerRepository.findByUsername("testovVtori").orElseGet(() -> {
			Player newPlayer = new Player();
			newPlayer.setUsername("testovVtori");
			return playerRepository.save(newPlayer);
		});

		gamePlayerGiver = new GamePlayer();
		gamePlayerGiver.setGame(testGame);
		gamePlayerGiver.setPlayer(giver);
		gamePlayerGiver.setRole(RoleName.WORD_GIVER);
		gamePlayerRepository.save(gamePlayerGiver);

		List<GamePlayer> gamePlayers = new ArrayList<>();
		gamePlayers.add(gamePlayerGiver);
		testGame.setPlayerInGames(gamePlayers);
		giver.setGamesWithRoles(gamePlayers);
		testGame.setMode(Commands.MULTI_PLAYER_MODE);
		gameRepository.save(testGame);

		given().queryParam("id", testGame.getId()).accept(ContentType.JSON).when().get("/api/v1/games").then()
				.statusCode(200).contentType(ContentType.JSON).body("gameId", equalTo((int) testGame.getId()))
				.body("triesLeft", equalTo(testGame.getTriesLeft())).body("finished", equalTo(testGame.isFinished()))
				.body("gameMode", equalTo(testGame.getMode().toString()))
				.body("wordProgress", equalTo(testGame.getCurrentState()))
				.body("_links.self.href", equalTo("http://localhost:" + port + "/api/v1/games?id=" + testGame.getId()))
				.body("_links.startNewGame.href", equalTo("http://localhost:" + port + "/api/v1/games"))
				.body("_links.makeGuess.href",
						equalTo("http://localhost:" + port + "/api/v1/games/" + testGame.getId()));

		gamePlayerRepository.deleteById(gamePlayerGiver.getId());
		playerRepository.deleteById(giver.getId());
	}

	@Test
	void makeSinglePlayerGuessSuccess() throws JsonProcessingException {

		expectedGameDTO.setWordProgress("t e _ t _ _ g");
		Response response = given().contentType(ContentType.JSON)
				.body(new UpdateGameDTO(new GuessDTO('e'), new LoginDTO(guesser.getUsername())))
				.pathParam("gameId", testGame.getId()).when().put("/api/v1/games/{gameId}");

		assertThat(expectedGameDTO).isEqualTo(toDTO(response.getBody().asString()));
	}

	@Test
	void makeMultiPlayerGuessSuccess() throws JsonProcessingException {

		giver = playerRepository.findByUsername("testovVtori").orElseGet(() -> {
			Player newPlayer = new Player();
			newPlayer.setUsername("testovVtori");
			return playerRepository.save(newPlayer);
		});

		gamePlayerGiver = new GamePlayer();
		gamePlayerGiver.setGame(testGame);
		gamePlayerGiver.setPlayer(giver);
		gamePlayerGiver.setRole(RoleName.WORD_GIVER);
		gamePlayerRepository.save(gamePlayerGiver);

		List<GamePlayer> gamePlayers = new ArrayList<>();
		gamePlayers.add(gamePlayerGiver);
		testGame.setPlayerInGames(gamePlayers);
		giver.setGamesWithRoles(gamePlayers);
		testGame.setMode(Commands.MULTI_PLAYER_MODE);
		gameRepository.save(testGame);

		expectedGameDTO.setWordProgress("t e _ t _ _ g");
		expectedGameDTO.setGiver(new PlayerDTO(giver.getId(), giver.getUsername()));
		expectedGameDTO.setGameMode(Commands.MULTI_PLAYER_MODE);
		Response response = given().contentType(ContentType.JSON)
				.body(new UpdateGameDTO(new GuessDTO('e'), new LoginDTO(guesser.getUsername())))
				.pathParam("gameId", testGame.getId()).when().put("/api/v1/games/{gameId}");

		String responseBody = response.getBody().asString();
		GameDTO jsonInput = toDTO(responseBody);

		assertThat(expectedGameDTO).isEqualTo(jsonInput);

		gamePlayerRepository.deleteById(giver.getId());
		playerRepository.deleteById(giver.getId());
	}

	private GameDTO toDTO(String json) throws JsonProcessingException {
		return objectMapper.readValue(json, GameDTO.class);

	}

	@Test
	void createSinglePlayerGame() {
		LoginDTO dto = new LoginDTO(guesser.getUsername());
		CreateGameInputDTO inputDTO = new CreateGameInputDTO();
		inputDTO.setPlayerDTO(dto);

		Response response = given()
		.contentType(ContentType.JSON)
		.body(inputDTO)
		.when()
		.post("api/v1/games")
		.then()
		.statusCode(HttpStatus.CREATED.value())
		.contentType(ContentType.JSON)
		.body("gameMode", equalTo("Singleplayer"))
		.extract()
		.response();
		
		long id = response.jsonPath().getLong("gameId");
		gameRepository.deleteById(id);
	}

	@Test
	void createMultiPlayerGame() {

		giver = playerRepository.findByUsername("testovVtori").orElseGet(() -> {
			Player newPlayer = new Player();
			newPlayer.setUsername("testovVtori");
			return playerRepository.save(newPlayer);
		});
		MultiPlayerGameInputDTO dto = new MultiPlayerGameInputDTO(giver.getUsername(), guesser.getUsername(), "testfromassure", testGame.getWord().getCategory().getCategoryName().name());
		
		CreateGameInputDTO inputDTO = new CreateGameInputDTO();
		inputDTO.setMultiPlayerGameInputDTO(dto);

		Response response = given()
		.contentType(ContentType.JSON)
		.body(inputDTO)
		.when()
		.post("api/v1/games")
		.then()
		.statusCode(HttpStatus.CREATED.value())
		.contentType(ContentType.JSON)
		.body("gameMode", equalTo("Multiplayer"))
		.extract()
		.response();
		
		long id = response.jsonPath().getLong("gameId");
//		gamePlayerRepository.deleteByGameId(id);
		gameRepository.deleteById(id);
	}

}
