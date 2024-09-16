package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import enums.Category;

public class MultiPlayerPage {
	
	WebDriver webDriver;
	
	@FindBy(id = "wordToGuess")
	WebElement wordToGuess;
	
	@FindBy(id = "category")
	WebElement category;
	
	@FindBy(id = "submit-word")
	WebElement submitButton;
	
	@FindBy(id = "homeButton")
	WebElement homeButton;
	
	public MultiPlayerPage(WebDriver webDriver) {
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}
	
	
	public void setWordToGuess(String wordToSet) {
		wordToGuess.sendKeys(wordToSet);
	}
	
	
	public void setCategory(Category categoryToSet) {
		category.sendKeys(categoryToSet.name());
	}
	
	public void clickSubmitButton() {
		submitButton.click();
	}
	
	public void clickHomeButton() {
		homeButton.click();
	}
	
	
}
