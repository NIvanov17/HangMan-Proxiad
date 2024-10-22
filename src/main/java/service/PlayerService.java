package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Player;
import repository.PlayerRepository;

@Service
public class PlayerService {

	private PlayerRepository playerRepository;

	@Autowired
	public PlayerService (PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public boolean contains(String username) {
		return playerRepository.findByUsername(username).isPresent();
	}

	public void register(String username) {
		Player player = new Player();
		player.setUsername(username);
		playerRepository.save(player);
	}

	public Player getPlayerByUsername(String username) {
		return playerRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException(username));
	}

	public void incrementWins(String username) {
		Player playerByUsername = getPlayerByUsername(username);
		int totalWins = playerByUsername.getTotalWins();
		playerByUsername.setTotalWins(totalWins + 1);
		
	}
}

