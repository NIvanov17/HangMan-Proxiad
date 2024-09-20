package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class,WebbInitialializer.class,RootConfig.class})
class GlobalExceptionHandlerIT {

	private MockMvc mockMvc;
	
	@Autowired
	WebApplicationContext webApplicationContext;

	@BeforeEach
	 void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	void testInavlidURLIT() throws Exception {
		mockMvc.perform(get("/welc0me"))
		.andExpect(status().isBadRequest())
		.andExpect(view().name("error"))
		.andExpect(model().attribute("errorMessage", "The URL you are trying to reach is not in service at this time ( 400 Bad Request :-( )."));
	}
}
