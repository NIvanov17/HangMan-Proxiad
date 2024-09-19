package controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import config.AppConfig;
import config.RootConfig;
import config.WebbInitialializer;
import enums.Category;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class,WebbInitialializer.class,RootConfig.class})
 class SinglePlayerIT {
	
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
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		injectedHistory = wordsRepository.getHistory();
	}

	@Test
	 void singlePlayerStartNewGame() throws Exception {
		mockMvc.perform(get("/hangMan"))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
	}

	@Test
	 void singlePlayerRestartGame() throws Exception {
		mockMvc.perform(get("/hangMan").param("action", "restart").session(extractedSession()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/hangMan"));
		
	}

	@Test
	 void singlePlayerResumeGame() throws Exception {
		String currentWord = setUpHistory();
		
		mockMvc.perform(get("/hangMan").param("action", "resume").param("currentWord", currentWord))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
	}

	@Test
	 void singlePlayerTryGuess() throws Exception {
		mockMvc.perform(post("/hangMan").param("guess", "e").session(extractedSession()))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
	}
	
	@Test
	 void singlePlayerTryWrongGuess() throws Exception {
		mockMvc.perform(post("/hangMan").param("guess", "a").session(extractedSession()))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
	}
	
	String setUpHistory() {
		String wordToGuess = "test";
		Game game = new Game();
		game.setWord("test");
		game.setCategory(Category.TECHNOLOGY);
		
		History history = new History();
		history.setCategory(Category.TECHNOLOGY);
		history.setFinished(false);
		history.setMode("Single Player");
		history.setTriesLeft(5);
		history.setUsedChars(Set.of('t'));
		history.setWordState("t _ _ t");
		injectedHistory.put(game, history);
		return wordToGuess;
	}
	
	MockHttpSession extractedSession() {
		MockHttpSession session = new MockHttpSession();
		Set<Character> usedCharacters = new HashSet<>();
		usedCharacters.add('t');
		Game wordToFind = new Game();
		wordToFind.setWord("test");
		Category category = Category.TECHNOLOGY;
		int triesLeft = 6;
		String currentState = "t _ _ t"; 
		boolean isFinished = false;
		String mode = "SinglePlayer";
		
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
