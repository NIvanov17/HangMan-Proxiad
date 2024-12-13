package com.example.controller.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.example.HangManApp;
import com.example.model.Player;
import com.example.model.DTOs.GameDTO;
import com.example.repository.PlayerRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = HangManApp.class)
class StatisticAPIControllerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	private Player guesser;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		RestAssured.port = port;
	}
	
	@Test
	void testGetHistoryWithValidUsrname() {
		guesser = playerRepository.findByUsername("testov").orElseGet(() -> {
			Player newPlayer = new Player();
			newPlayer.setUsername("testov");
			newPlayer.setPassword("test");
			return playerRepository.save(newPlayer);
		});
		List<GameDTO> gamesHistory = Collections.singletonList(new GameDTO());
		
		given()
		.queryParam("username", guesser.getUsername())
		.when()
		.get("/api/v1/games/history")
		.then()
		.statusCode(HttpStatus.OK.value())
		.contentType(ContentType.JSON)
		.body("$", hasSize(0));
	}
	
	@Test
	void testGetHistoryWithInvalidUsrname() {
		guesser = new Player();
		guesser.setUsername("invalid");
		guesser.setPassword("test");
		
		given()
		.queryParam("username", guesser.getUsername())
		.when()
		.get("/api/v1/games/history")
		.then()
		.statusCode(HttpStatus.BAD_REQUEST.value())
		.contentType(ContentType.JSON)
		.body("message", equalTo("Username: " + guesser.getUsername() + " is not valid!"));
	}
	
	@Test
	void getTopTenGamesTest() {
		Response response = given()
		.when()
		.get("/api/v1/games/statistic")
		.then()
		.statusCode(200)
		.body("size()", greaterThan(0))
		.extract()
		.response();
		
		response.prettyPrint();
	}
}
