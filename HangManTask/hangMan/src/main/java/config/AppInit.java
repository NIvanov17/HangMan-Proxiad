package config;

import java.util.List;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Word;
import repository.WordsRepository;

@WebListener
public class AppInit implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			WordsRepository wordRepository = WordsRepository.getWordRepository();
			System.out.println("Listener called");
			List<Word> list = WordsRepository.initializeWords();
			WordsRepository.getWordslist().addAll(list);
			//System.out.println(list.getFirst().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
