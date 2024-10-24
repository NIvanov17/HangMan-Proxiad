package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	public List<Player> getAllPlayersByWins() {
		return playerRepository.findAllByOrderByTotalWinsDesc();
	}
	
	public List<Player> getTopTenPlayersByWins() {
		Pageable topTen = PageRequest.of(0, 10);
		return playerRepository.findAllByOrderByTotalWinsDesc(topTen);
	}

	public boolean isValid(String username) {
		if(username.isBlank() || username.length() < 2) {
			return false;
		}
		boolean hasLetter = false;
		for(char symbol : username.toCharArray()) {
			if(Character.isLetter(symbol)) {
				hasLetter = true;
				break;
			}
		}
		if(hasLetter) {
			return true;
		}
	
		return false;
	}
}

