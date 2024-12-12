package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.GamePlayerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GamePlayerService {

	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
	}
	
}
