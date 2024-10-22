package config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import model.Word;
import repository.WordRepository;
import service.WordService;

@Component
public class InitWords implements ApplicationListener<ContextRefreshedEvent> {

	private WordService wordService;
	private WordRepository wordRepository;

	@Autowired
	public InitWords(WordService wordService, WordRepository wordRepository) {
		this.wordService = wordService;
		this.wordRepository = wordRepository;

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			List<Word> all = wordRepository.findAll();
			if (all.isEmpty()) {
				wordService.initWords();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
