package model.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StatisticDTO {

	private List<String> word;

	private double avgAttempts;
	
	private String winLossRatio;
	
	
	
	public StatisticDTO() {
		super();
	}

	public StatisticDTO(List<String> word, double avgAttempts, String winLossRatio) {
		super();
		this.word = word;
		this.avgAttempts = avgAttempts;
		this.winLossRatio = winLossRatio;
	}

	public List<String> getWord() {
		return word;
	}

	public void setWord(List<String> word) {
		this.word = word;
	}

	public double getAvgAttempts() {
		return avgAttempts;
	}

	public void setAvgAttempts(double avgAttempts) {
		this.avgAttempts = avgAttempts;
	}

	public String getWinLossRatio() {
		return winLossRatio;
	}

	public void setWinLossRatio(String winLossRatio) {
		this.winLossRatio = winLossRatio;
	}


	
	
}
