package com.example.model;


import com.example.enums.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_player")
public class GamePlayer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne()
	@JoinColumn(name = "game_id")
	private Game game;
	
	@ManyToOne()
	@JoinColumn(name = "player_id")
	private Player player;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleName role;
	
	public GamePlayer() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public RoleName getRole() {
		return role;
	}

	public void setRole(RoleName role) {
		this.role = role;
	}
	
	
	
}
