package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MultiPlayerStartedPage {
	
	WebDriver webDriver;
	
	@FindBy(id = "currentState" )
	WebElement currentState;
	
	@FindBy(id = "category")
	WebElement category;
	
	@FindBy(name = "guess")
	WebElement guessButton;
	
	@FindBy(id = "homeButton")
	WebElement homeButton;
	
	@FindBy(id = "tries-left")
	int triesLeft;
	
	public MultiPlayerStartedPage(WebDriver webDriver) {
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}
	
	public void clickLetter() {
		guessButton.click();
	}

	public int getTriesLeft() {
		return triesLeft;
	}
	
	public void clickHomeButton() {
		homeButton.click();
	}

}
