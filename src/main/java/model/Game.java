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
	
	@OneToMany(mappedBy = "game",cascade = CascadeType.PERSIST)
	private List<GamePlayer> playerInGames;

	

	public Game() {
		this.usedChars = new HashSet<>();
		this.playerInGames = new ArrayList<>();
	}

//	public void reset( int triesLeft, Category category, Set<Character> usedCharacters,
//			boolean isFinished) {
//		this.triesLeft = triesLeft;
//		this.usedChars.clear();
//		this.setFinished(false);
//	}

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




}