package com.example.controller.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.enums.ErrorMessages;
import com.example.enums.RoleName;
import com.example.exception.InvalidUsernameException;
import com.example.model.DTOs.GameDTO;
import com.example.model.soap.GetHistoryForPlayerRequest;
import com.example.model.soap.GetHistoryForPlayerResponse;
import com.example.service.GameService;
import com.example.service.PlayerService;
import org.springframework.data.domain.Pageable;

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
