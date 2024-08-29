package controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;


public class HistoryController extends HttpServlet {

	private WordsRepository wordsRepository;


	public HistoryController() {
		
	}
	
	@Autowired
	public void setWordsRepository(WordsRepository wordsRepository) {
		this.wordsRepository = wordsRepository;
	}
	
	 @Override
	    public void init(ServletConfig config) throws ServletException {
	        super.init(config);

	        if (this.wordsRepository == null) {
	            ApplicationContext context = (ApplicationContext) config.getServletContext().getAttribute("springContext");
	            if (context != null) {
	                this.wordsRepository = context.getBean(WordsRepository.class);
	            } else {
	                throw new ServletException("Spring context is not initialized!");
	            }
	        }
	    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<Game, History> history = wordsRepository.getHistory();
		session.setAttribute("history", history);
		request.getRequestDispatcher("/history.jsp").forward(request, response);

	}




}
