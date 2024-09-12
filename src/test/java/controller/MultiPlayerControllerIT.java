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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import enums.Category;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(MockitoExtension.class)
public class MultiPlayerControllerIT {

	private MockMvc mockMvc;

	@Mock
	private GameService gameservice;

	@Mock
	private WordsRepository wordsRepository;

	private Map<Game, History> mockHistory;

	@InjectMocks
	private MultiPlayerController controller;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		mockHistory = wordsRepository.getHistory();
	}

	@Test
	public void multiPlayerGetViewIT() throws Exception {
		mockMvc.perform(get("/multiPlayer"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiPlayerView"));
	}

	@Test
	public void multiPlayerSendWordEmptyStringIT() throws Exception {
		String wordToGuess = "";
		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}
	
	@Test
	public void multiPlayerSendInvalidWordIT() throws Exception {
		String wordToGuess = "aab";
		when(gameservice.isWordValid(wordToGuess)).thenReturn(false);
		
		mockMvc.perform(post("/multiPlayer")
		.param("wordToGuess", wordToGuess)
		.param("action", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/multiPlayer"));
	}
	
	@Test
	public void multiPlayerSendWordfromHistoryIT() throws Exception {
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
	public void multiPlayerResumeIT() throws Exception {
		String wordToGuess = "test";
		when(gameservice.resumeGame(eq(wordToGuess), any(HttpSession.class), eq(mockHistory)))
		.thenReturn("multiplayerStartedView");
		
		mockMvc.perform(post("/multiPlayer")
		.param("currentWord", wordToGuess)
		.param("action", "resume"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiplayerStartedView"));
	}
	
	//gameService.prepareWordToBeDisplayed(session, wordToGuess, category);
	@Test
	public void multiPlayerDisplayWord() throws Exception {
		String wordToGuess = "test";
		Category technology = Category.TECHNOLOGY;
		when(gameservice.prepareWordToBeDisplayed(any(HttpSession.class), eq(wordToGuess), any(Category.class)))
		.thenReturn("multiplayerStartedView");
	
		mockMvc.perform(post("/multiPlayer")
				.param("wordToGuess", wordToGuess)
				.param("action", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("multiplayerStartedView"));
	}
	
	@Test 
	public void multiPlayerGameStartedViewIT() throws Exception {
		mockMvc.perform(get("/multiplayerStarted"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiplayerStartedView"));
	}
	
	
	//	TODO: test the last method and fix multiPlayerDisplayWord
//	@Test 
//	public void multiPlayerGameStartedTryCorrectIT() throws Exception {
//		String wordToGuess = "test";
//		char guess = 'a';
//		mockMvc.perform(post("/multiplayerStarted").param("guess", guess))
//		.andExpect(status().isOk())
//		.andExpect(view().name("multiplayerStartedView"));
//	}
}
