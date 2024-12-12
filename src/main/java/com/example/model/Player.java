package com.example.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true)
	private String username;

	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
	private List<GamePlayer> gamesWithRoles;

	@Column(name = "total_wins")
	private int totalWins;

	@ManyToMany()
	@JoinTable(name = "player_role",
	joinColumns = @JoinColumn(name = "player_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	public Player() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}

	public List<GamePlayer> getGamesWithRoles() {
		return gamesWithRoles;
	}

	public void setGamesWithRoles(List<GamePlayer> gamesWithRoles) {
		this.gamesWithRoles = gamesWithRoles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
