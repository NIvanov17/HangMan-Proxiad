package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handleBadRequestException(NoHandlerFoundException ex, Model model) {
		ModelAndView modelView = new ModelAndView("error");
		modelView.setStatus(HttpStatus.BAD_REQUEST);
		modelView.addObject("errorMessage", "The URL you are trying to reach is not in service at this time ( 400 Bad Request :-( ).");
		
		return modelView;
	}
	
	@ExceptionHandler(InternalServerError.class)
	public ModelAndView handleBadRequestException(InternalServerError ex, Model model) {
		ModelAndView modelView = new ModelAndView("error");
		modelView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelView.addObject("errorMessage", "Something went wrong. ( 500 Internal Server Error :-( ).");
		
		return modelView;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ModelAndView handleBadRequestException(IllegalArgumentException ex, Model model) {
		ModelAndView modelView = new ModelAndView("error");
		modelView.setStatus(HttpStatus.NOT_FOUND);
		modelView.addObject("errorMessage", "Oops! Page Not Found( 404 Not Found :-( ).");
		
		return modelView;
	}
}
