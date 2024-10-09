package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import app.HangManApp;
import enums.Category;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HangManApp.class)
@AutoConfigureMockMvc
 class MultiPlayerControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	@Autowired
	GameService gameService;
	
	@Autowired
	WordsRepository wordsRepository;
	
	Map<Game,History> injectedHistory;

	@BeforeEach
	 void setup() {
		injectedHistory = wordsRepository.getHistory();
	}

	@Test
	 void multiPlayerGetViewIT() throws Exception {
		mockMvc.perform(get("/multiPlayer"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiPlayer"));
	}

	@Test
	 void multiPlayerSendWordEmptyStringIT() throws Exception {
		String wordToGuess = "";
		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}
	
	@Test
	 void multiPlayerSendInvalidWordIT() throws Exception {
		String wordToGuess = "aab";
		
		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}
	
	@Test
	 void multiPlayerSendWordfromHistoryIT() throws Exception {
		String wordToGuess = setUpHistory();

		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}

	
	@Test
	 void multiPlayerResumeIT() throws Exception {
		String wordToGuess = setUpHistory();
		
		mockMvc.perform(post("/multiPlayer")
		.param("currentWord", wordToGuess)
		.param("action", "resume").session(extractedSession()))
		.andExpect(status().isOk())
		.andExpect(view().name("multiplayerStarted"));
	}
	

	@Test
	 void multiPlayerDisplayWord() throws Exception {
		String wordToGuess = "testov";
		Category category = Category.TECHNOLOGY;

		mockMvc.perform(post("/multiPlayer")
				.param("wordToGuess", wordToGuess)
				.param("category", category.name()))
				.andExpect(status().isOk())
				.andExpect(view().name("multiplayerStarted"));
	}
	
	@Test 
	 void multiPlayerGameStartedViewIT() throws Exception {
		mockMvc.perform(get("/multiplayer/game"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiplayerStarted"));
	}
	
	

	@Test 
	 void multiPlayerGameStartedTryWrongIT() throws Exception {
		mockMvc.perform(post("/multiplayer/guess").param("letter", "a").session(extractedSession()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiplayer/game"));
	}

	
	@Test 
	 void multiPlayerGameStartedTryCorrectIT() throws Exception {
		mockMvc.perform(post("/multiplayer/guess").param("letter", "e").session(extractedSession()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiplayer/game"));
	}
	
	String setUpHistory() {
		String wordToGuess = "test";
		Game game = new Game();
		game.setWord("test");
		game.setCategory(Category.TECHNOLOGY);
		
		History history = new History();
		history.setCategory(Category.TECHNOLOGY);
		history.setFinished(false);
		history.setMode("multiPlayer");
		history.setTriesLeft(5);
		history.setUsedChars(Set.of('t'));
		history.setWordState("t _ _ t");
		injectedHistory.put(game, history);
		return wordToGuess;
	}
	
	MockHttpSession extractedSession() {
		MockHttpSession session = new MockHttpSession();
		Set<Character> usedCharacters = new HashSet<>();
		Game wordToFind = new Game();
		wordToFind.setWord("test");
		Category category = Category.TECHNOLOGY;
		int triesLeft = 6;
		String currentState = "t _ _ t"; 
		boolean isFinished = false;
		String mode = "Multiplayer";
		
		session.setAttribute("usedCharacters", usedCharacters);
		session.setAttribute("word", wordToFind);
		session.setAttribute("category", category);
		session.setAttribute("triesLeft", triesLeft);
		session.setAttribute("currentState", currentState);
		session.setAttribute("isFinished", isFinished);
		session.setAttribute("mode", mode);
		return session;
	}
}
