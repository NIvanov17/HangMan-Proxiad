package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfiguration {

	@Bean
	public OpenAPI defineOpenAPI() {
		Server server = new Server();
		server.setUrl("hhtp://localhost:8080");
		server.setDescription("Development");

		Info info = new Info();
		info.setTitle("HangMan Game");
		info.setVersion("1.0");
		info.setDescription(
				"This is a hangMan game, which stores data about played, ongoing games, statistics and ranking of the players.");

		return new OpenAPI().info(info);
	}
}
