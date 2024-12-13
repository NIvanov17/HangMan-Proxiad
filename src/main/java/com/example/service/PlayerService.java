package com.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.enums.ErrorMessages;
import com.example.enums.PlayerRole;
import com.example.exception.InavlidPasswordException;
import com.example.exception.InvalidUsernameException;
import com.example.model.Player;
import com.example.model.Role;
import com.example.model.DTOs.PlayerDTO;
import com.example.model.DTOs.PlayerRankingDTO;
import com.example.model.DTOs.PlayersDTO;
import com.example.model.DTOs.RegisterDTO;
import com.example.repository.PlayerRepository;
import com.example.security.ShiroPasswordEncoder;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PlayerService {

	private PlayerRepository playerRepository;
	private RoleService roleService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository, RoleService roleService) {
		this.playerRepository = playerRepository;
		this.roleService = roleService;
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
		return playerRepository.findByUsername(username).orElseThrow(
				() -> new InvalidUsernameException(String.format(ErrorMessages.USERNAME_IS_NOT_VALID, username)));
	}

	public void incrementWins(long id) {
		Player player = getPlayerById(id);
		int totalWins = player.getTotalWins();
		player.setTotalWins(totalWins + 1);

	}

	public Page<Player> getAllPlayersByWins(Pageable pageable) {
		return playerRepository.findAllByOrderByTotalWinsDesc(pageable);
	}

	public Page<Player> getTopTenPlayersByWins() {
		Pageable topTen = PageRequest.of(0, 10);
		return playerRepository.findAllByOrderByTotalWinsDesc(topTen);
	}

	public boolean isValid(String username) {
		if (username.isBlank() || username.length() < 2) {
			return false;
		}
		boolean hasLetter = false;
		for (char symbol : username.toCharArray()) {
			if (Character.isLetter(symbol)) {
				hasLetter = true;
				break;
			}
		}
		if (hasLetter) {
			return true;
		}

		return false;
	}

	public Player getPlayerById(long id) {
		return playerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PLAYER_NOT_EXISTING));
	}

	public boolean areUsernamesEqual(long id, String guesserUsername) {
		Player player = getPlayerById(id);

		return player.getUsername().equals(guesserUsername);
	}

	public long getPlayerIdByGameId(long gameId) {
		return playerRepository.findByGameId(gameId);
	}

	public PlayersDTO createPlayersDTO(String guesserUsername,String giverUsername) {
		Player playerGuesser = getPlayerByUsername(guesserUsername);
		Player playerGiver = getPlayerByUsername(giverUsername);
		PlayerDTO dtoGuesser = new PlayerDTO(playerGuesser.getId(), playerGuesser.getUsername());
		PlayerDTO dtoGiver = new PlayerDTO(playerGiver.getId(), playerGiver.getUsername());
		return new PlayersDTO(dtoGuesser, dtoGiver);
	}

	public Page<PlayerRankingDTO> getAllPlayersDTOByWins(Pageable pageable) {
		return getAllPlayersByWins(pageable)
				.map(player->new PlayerRankingDTO(player.getId(), player.getUsername(), player.getTotalWins()));

//		Page<PlayerRankingDTO> playerRankingDTO = allPlayersByWins.stream().map(p -> {
//			PlayerRankingDTO dto = new PlayerRankingDTO();
//			dto.setId(p.getId());
//			dto.setUsername(p.getUsername());
//			dto.setTotalWins(p.getTotalWins());
//			return dto;
//		});
//		return playerRankingDTO;
	}

	public List<PlayerRankingDTO> getTopTenPlayersDTOByWins() {
//		return getTopTenPlayersByWins()
//				.map(player->new PlayerRankingDTO(player.getId(), player.getUsername(), player.getTotalWins()));

		Page<Player> topTenPlayersByWins = getTopTenPlayersByWins();
		
		List<PlayerRankingDTO> playerRankingDTO = topTenPlayersByWins.stream().map(p -> {
			PlayerRankingDTO dto = new PlayerRankingDTO();
			dto.setId(p.getId());
			dto.setUsername(p.getUsername());
			dto.setTotalWins(p.getTotalWins());
			return dto;
		}).collect(Collectors.toList());
		return playerRankingDTO;
	}

	public void deleteByUsername(String username) {
		Player player = getPlayerByUsername(username);
		playerRepository.delete(player);
	}

	public void registerDTO(RegisterDTO dto) {
		if(dto.getUsername().length() < 3) {
			throw new InvalidUsernameException("Username should be at least 3 symbols!");
		}else if(contains(dto.getUsername())) {
			throw new InvalidUsernameException("Username is already taken!");
		}
		
		if(dto.getPassword().length() < 6 || dto.getConfirmPassword().length() < 6) {
			throw new InavlidPasswordException("Password should be at least 6 characters!");
		}
		
		
		if(!dto.getPassword().equals(dto.getConfirmPassword())) {
			throw new InavlidPasswordException("Passwords should match!");
		}
		Role deafultRole = roleService.getRoleByName(PlayerRole.USER);
		String password = dto.getPassword();
		String salt = ShiroPasswordEncoder.generateSalt();
		String hashPassword = ShiroPasswordEncoder.hashPassword(password, salt);
		
		Player player = new Player();
		player.setRoles(List.of(deafultRole));
		player.setUsername(dto.getUsername());
		player.setPassword(dto.getPassword());
		player.setPassword(hashPassword);
		player.setSalt(salt);
		
		playerRepository.save(player);
		
	}
}
