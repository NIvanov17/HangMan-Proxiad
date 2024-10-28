package service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Game;
import model.Statistic;
import repository.StatisticRepository;

@Service
public class StatisticService {

	private StatisticRepository statisticRepo;

	@Autowired
	public StatisticService(StatisticRepository statisticRepo) {
		this.statisticRepo = statisticRepo;
	}

	public void createStatistic(Game game) {

		String status = game.isFinished() && !game.getCurrentState().contains("_") ? "WON" : "LOSE";

		Statistic statistic = new Statistic();
		statistic.setGame(game);
		statistic.setFinishedAt(LocalDateTime.now());
		statistic.setStatus(status);
		statisticRepo.save(statistic);
	}
}
