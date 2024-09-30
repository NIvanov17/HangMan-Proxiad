package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import app.HangManApp;
import service.GameService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HangManApp.class)
@AutoConfigureMockMvc
 class HistoryControllerIT {


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	GameService gameService;


	@Test
	 void testGetHistory() throws Exception {

		mockMvc.perform(get("/history"))
		.andExpect(status().isOk())
		.andExpect(view().name("history"))
		.andExpect(request().sessionAttribute("history", gameService.getHistory()));
	}

}
