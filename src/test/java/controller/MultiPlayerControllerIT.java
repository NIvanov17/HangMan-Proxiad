package controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class MultiPlayerControllerIT {

	private MockMvc mockMvc;

	@Mock
	private GameService gameservice;

	@Mock
	private WordsRepository wordsRepository;

	private Map<Game, History> mockHistory;

	@Mock
	private HttpSession session;

	@InjectMocks
	private MultiPlayerController controller;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		mockHistory = new HashMap<>();
	}

	@Test
	public void multiPlayerGetView() throws Exception {
		mockMvc.perform(get("/multiPlayer"))
		.andExpect(status().isOk())
		.andExpect(view().name("multiPlayerView"));
	}
}
