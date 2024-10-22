package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.GamePlayerRepository;

@Service
public class GamePlayerService {

	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
	}
	
}
