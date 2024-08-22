package controller;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.OngoingGame;
import model.Word;
import repository.WordsRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "OnGoingGamesController", urlPatterns = "/onGoingGames")
public class OnGoingGamesController extends HttpServlet {

	private WordsRepository wordRepository;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		 Map<Word,OngoingGame> onGoingWords = wordRepository.getOnGoingWordslist();
		session.setAttribute("onGoingWords", onGoingWords);
		request.getRequestDispatcher("/onGoingGames.jsp").forward(request,response);


	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
