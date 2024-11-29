package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import repository.GamePlayerRepository;

@Service
@Transactional
public class GamePlayerService {

	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
	}
	
}
