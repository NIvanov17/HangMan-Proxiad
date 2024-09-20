package controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import config.AppConfig;
import config.RootConfig;
import config.WebbInitialializer;
import model.Game;
import model.History;
import repository.WordsRepository;
import service.GameService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, WebbInitialializer.class, RootConfig.class })
@WebAppConfiguration
 class HistoryControllerIT {

	@Autowired
	WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	GameService gameService;

	@BeforeEach
	 void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	 void testGetHistory() throws Exception {

		mockMvc.perform(get("/history"))
		.andExpect(status().isOk())
		.andExpect(view().name("history"))
		.andExpect(request().sessionAttribute("history", gameService.getHistory()));
	}

}
