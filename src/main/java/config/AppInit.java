package config;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Game;
import repository.WordsRepository;

@WebListener
public class AppInit implements ServletContextListener {
	
	
	
	private ApplicationContext context;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		

		try {
	        context = new ClassPathXmlApplicationContext("beans.xml");
	        sce.getServletContext().setAttribute("springContext", context);

			System.out.println("Listener called");
			WordsRepository wordRepository = context.getBean(WordsRepository.class);
			List<Game> list = wordRepository.initializeWords();
			wordRepository.getGameslist().addAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (context instanceof ClassPathXmlApplicationContext) {
            ((ClassPathXmlApplicationContext) context).close();
        }
    }

}
