package controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import enums.Category;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(MockitoExtension.class)
 class MultiPlayerControllerIT {

	private MockMvc mockMvc;

	@Mock
	private GameService gameservice;

	@Mock
	private WordsRepository wordsRepository;

	private Map<Game, History> mockHistory;

	@InjectMocks
	private MultiPlayerController controller;

	@BeforeEach
	 void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		mockHistory = wordsRepository.getHistory();
	}

	@Test
	 void multiPlayerGetViewIT() throws Exception {
		mockMvc.perform(get("/multiPlayer"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiPlayerView"));
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
		when(gameservice.isWordValid(wordToGuess)).thenReturn(false);
		
		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}
	
	@Test
	 void multiPlayerSendWordfromHistoryIT() throws Exception {
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
		mockHistory.put(game, history);
		lenient().when(gameservice.historyContainsWord(eq(mockHistory),eq(wordToGuess))).thenReturn(true);
		
		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}
	
	@Test
	 void multiPlayerResumeIT() throws Exception {
		String wordToGuess = "test";
		when(gameservice.resumeGame(eq(wordToGuess), any(HttpSession.class), eq(mockHistory)))
		.thenReturn("multiplayerStartedView");
		
		mockMvc.perform(post("/multiPlayer")
		.param("currentWord", wordToGuess)
		.param("action", "resume"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiplayerStartedView"));
	}
	

	@Test
	 void multiPlayerDisplayWord() throws Exception {
		String wordToGuess = "testov";
		Category category = Category.TECHNOLOGY;

		when(gameservice.prepareWordToBeDisplayed(any(HttpSession.class),
				eq(wordToGuess), 
				eq(category)))
		.thenReturn("multiplayerStartedView");
		
		when(gameservice.isWordValid(eq(wordToGuess)))
		.thenReturn(true);
	
		mockMvc.perform(post("/multiPlayer")
				.param("wordToGuess", wordToGuess)
				.param("category", category.name()))
				.andExpect(status().isOk())
				.andExpect(view().name("multiplayerStartedView"));
	}
	
	@Test 
	 void multiPlayerGameStartedViewIT() throws Exception {
		mockMvc.perform(get("/multiplayerStarted"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiplayerStartedView"));
	}
	
	

	@Test 
	 void multiPlayerGameStartedTryWrongIT() throws Exception {

		
		when(gameservice.tryGuessMultiplayer(eq('a'), any(HttpSession.class)))
		.thenReturn("redirect:/multiplayerStarted");

		mockMvc.perform(post("/multiplayerStarted").param("guess", "a"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiplayerStarted"));
	}
	
	@Test 
	 void multiPlayerGameStartedTryCorrectIT() throws Exception {

		
		when(gameservice.tryGuessMultiplayer(eq('e'), any(HttpSession.class)))
		.thenReturn("redirect:/multiplayerStarted");

		mockMvc.perform(post("/multiplayerStarted").param("guess", "e"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiplayerStarted"));
	}
}
