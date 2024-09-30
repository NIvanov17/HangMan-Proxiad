package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WelcomePage {
	
	WebDriver driver;

	@FindBy(id = "single")
	WebElement singlePlayerButton;
	
	@FindBy(id = "multi")
	WebElement multiPlayerButton;
	
	@FindBy(id = "history")
	WebElement historyButton;

	public WelcomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void clickSinglePlayerButton () {
		singlePlayerButton.click();
	}
	
	public void clickMultiPlayerButton () {
		multiPlayerButton.click();
	}
	
	public void clickHistoryButton () {
		historyButton.click();
	}
	
}
