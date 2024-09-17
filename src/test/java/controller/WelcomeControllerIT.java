package controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import config.AppConfig;
import config.RootConfig;
import config.WebbInitialializer;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, WebbInitialializer.class, RootConfig.class })
@WebAppConfiguration
 class WelcomeControllerIT {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	 void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	 void testWelcome() throws Exception {
		mockMvc.perform(get("/welcome"))
		.andExpect(status().isOk())
		.andExpect(view().name("welcome"));
	}

	@Test
	 void testIndex() throws Exception {
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("welcome"));
	}
}
