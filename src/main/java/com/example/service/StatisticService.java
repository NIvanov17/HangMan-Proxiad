package com.example.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.enums.GameStatus;
import com.example.model.Game;
import com.example.model.Statistic;
import com.example.repository.StatisticRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StatisticService {

	private StatisticRepository statisticRepo;

	@Autowired
	public StatisticService(StatisticRepository statisticRepo) {
		this.statisticRepo = statisticRepo;
	}

	public void createStatistic(Game game) {

		String status = game.isFinished() && !game.getCurrentState().contains("_") ? GameStatus.GAME_STATUS_WON : GameStatus.GAME_STATUS_LOSE;

		Statistic statistic = new Statistic();
		statistic.setGame(game);
		statistic.setFinishedAt(LocalDateTime.now());
		statistic.setStatus(status);
		statisticRepo.save(statistic);
	}
}
