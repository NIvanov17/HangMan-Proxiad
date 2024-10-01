package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import app.HangManApp;



@SpringBootTest(classes = HangManApp.class)
@AutoConfigureMockMvc
class GlobalExceptionHandlerIT {

	@Autowired
	private MockMvc mockMvc;
	


	
	@Test
	void testInavlidURLIT() throws Exception {
		mockMvc.perform(get("/welc0me"))
		.andExpect(status().isBadRequest())
		.andExpect(view().name("error"))
		.andExpect(model().attribute("errorMessage", "The URL you are trying to reach is not in service at this time ( 400 Bad Request :-( )."));
	}
}
