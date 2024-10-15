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
import org.springframework.test.web.servlet.MockMvc;

import app.HangManApp;
import enums.Category;
import model.Game;
import model.Word;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HangManApp.class)
@AutoConfigureMockMvc
 class SinglePlayerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	GameService gameService;
	
	@Autowired
	WordsRepository wordsRepository;
	
	Map<Word,Game> injectedHistory;

	@BeforeEach
	 void setup() {
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
		Word word = new Word();
		word.setWord("test");
		word.setCategory(Category.TECHNOLOGY);
		
		Game game = new Game();
		game.setCategory(Category.TECHNOLOGY);
		game.setFinished(false);
		game.setMode("Single Player");
		game.setTriesLeft(5);
		game.setUsedChars(Set.of('t'));
		game.setWordState("t _ _ t");
		injectedHistory.put(word, game);
		return wordToGuess;
	}
	
	MockHttpSession extractedSession() {
		MockHttpSession session = new MockHttpSession();
		Set<Character> usedCharacters = new HashSet<>();
		usedCharacters.add('t');
		Word wordToFind = new Word();
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
