package config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import model.Game;
import repository.WordsRepository;

@Component
public class InitWords implements ApplicationListener<ContextRefreshedEvent> {

	private WordsRepository wordRepository;

	@Autowired
	public InitWords(WordsRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<Game> list;
		try {
			list = wordRepository.initializeWords();
			wordRepository.getGameslist().addAll(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
