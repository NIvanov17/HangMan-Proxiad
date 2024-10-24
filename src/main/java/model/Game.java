package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private Word word;

	@Column(name = "tries-left")
	private int triesLeft;

	@Column(name = "used-characters")
	private Set<Character> usedChars;

	@Column(name = "finished")
	private boolean isFinished;

	@Column(nullable = false)
	private String mode;

	@Column(name = "current-state")
	private String currentState;

	@OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
	private List<GamePlayer> playerInGames;
	
	@OneToOne(mappedBy = "game")
	private Statistic statistic;

	public Game() {
		this.usedChars = new HashSet<>();
		this.playerInGames = new ArrayList<>();
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	public int getTriesLeft() {
		return triesLeft;
	}

	public void setTriesLeft(int triesLeft) {
		this.triesLeft = triesLeft;
	}

	public Set<Character> getUsedChars() {
		return usedChars;
	}

	public void setUsedChars(Set<Character> usedChars) {
		this.usedChars = usedChars;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<GamePlayer> getPlayerInGames() {
		return playerInGames;
	}

	public void setPlayerInGames(List<GamePlayer> playerInGames) {
		this.playerInGames = playerInGames;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}


	public Statistic getStatistic() {
		return statistic;
	}


	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}
	

}