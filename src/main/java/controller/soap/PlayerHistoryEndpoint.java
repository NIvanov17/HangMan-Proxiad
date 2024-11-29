package controller.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import enums.ErrorMessages;
import enums.RoleName;
import exception.InvalidUsernameException;
import model.DTOs.GameDTO;
import model.soap.GetHistoryForPlayerRequest;
import model.soap.GetHistoryForPlayerResponse;
import service.GameService;
import service.PlayerService;

@Endpoint
public class PlayerHistoryEndpoint {

	private static final String NAMESPACE_URI = "http://www.example.org/players";
//	http://localhost:8080/ws/players.wsdl

	private final GameService gameService;

	private final PlayerService playerService;

	@Autowired
	public PlayerHistoryEndpoint(GameService gameService, PlayerService playerService) {
		this.gameService = gameService;
		this.playerService = playerService;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHistoryForPlayerRequest")
	@ResponsePayload
	public GetHistoryForPlayerResponse getHistoryForPlayer(@RequestPayload GetHistoryForPlayerRequest request) {
		if (!playerService.isValid(request.getUsername())) {
			throw new InvalidUsernameException(String.format(ErrorMessages.INVALID_USERNAME, request.getUsername()));
		}
		GetHistoryForPlayerResponse response = new GetHistoryForPlayerResponse();
		List<GameDTO> games = gameService.getAllGamesDTOForPlayerByUsername(request.getUsername(), RoleName.GUESSER);
		for (GameDTO game : games) {
			response.getGames().add(game.getGameId().toString());
		}

		return response;
	}
}
