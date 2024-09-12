package controller;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(MockitoExtension.class)
public class SinglePlayerIT {

	private MockMvc mockMvc;

	@Mock
	private GameService gameService;

	@Mock
	private WordsRepository wordsRepository;

	@InjectMocks
	private SinglePlayerController controller;

	private Map<Game, History> mockHistory;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		mockHistory = new HashMap<>();
	}

	@Test
	public void singlePlayerStartNewGame() throws Exception {
		when(gameService.newGameStarted(any(HttpSession.class), eq(mockHistory))).thenReturn("gameStarted");

		mockMvc.perform(get("/hangMan"))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
	}

	@Test
	public void singlePlayerRestartGame() throws Exception {
		mockMvc.perform(get("/hangMan").param("action", "restart"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/hangMan"));
		
	}

	@Test
	public void singlePlayerResumeGame() throws Exception {
		when(gameService.resumeGame(eq("test"), any(HttpSession.class), eq(mockHistory))).thenReturn("gameStarted");

		mockMvc.perform(get("/hangMan").param("action", "resume").param("currentWord", "test"))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
	}

	@Test
	public void singlePlayerTryGuess() throws Exception {
		when(gameService.tryGuess(anyChar(), any(HttpSession.class), eq(mockHistory))).thenReturn("gameStarted");

		mockMvc.perform(post("/hangMan").param("guess", "a"))
		.andExpect(status().isOk())
		.andExpect(view().name("gameStarted"));
		
	}

}
