package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import enums.Category;
import pages.MultiPlayerPage;


 class MultiPlayerTest {
	
	WebDriver webDriver;
	MultiPlayerPage multiPlayerPage;
	
	@BeforeEach
	 void setUp() {
		webDriver = new ChromeDriver();
		webDriver.get("http://www.localhost:8080/multiPlayer?action=newGame");
		
		multiPlayerPage = new MultiPlayerPage(webDriver);
	}
	
	@AfterEach
	 void tearDown() {
		if(webDriver != null) {
			webDriver.quit();
		}
	}

	@org.junit.jupiter.api.Test
	 void testSendWordAndCategory() throws InterruptedException {
		String word = "peach";
		multiPlayerPage.setWordToGuess(word);
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();
		String actual = webDriver.findElement(By.id("tries-left")).getText();
		String expected = "Tries left: 6";
		Assert.assertEquals(actual, expected);
	}
	
	@org.junit.jupiter.api.Test
	 void testSendWordAndCategoryFail() throws InterruptedException {
		String word = "peac4";
		multiPlayerPage.setWordToGuess(word);
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();
		String actual = webDriver.findElement(By.className("error-msg")).getText();
		String expected = "The word should contains more than 3 different symbols!";
		Assert.assertEquals(actual, expected);
	}
	
	@org.junit.jupiter.api.Test
	 void testSendWordAndCategoryEmpty() throws InterruptedException {
		multiPlayerPage.setWordToGuess("");
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(3000);
		multiPlayerPage.clickSubmitButton();
		String actual = webDriver.findElement(By.className("error-msg")).getText();
		String expected = "The word field can't be empty!";
		Assert.assertEquals(actual, expected);
	}
	
	@org.junit.jupiter.api.Test
	 void testBackToHome() throws InterruptedException {


		multiPlayerPage.clickHomeButton();
		String heading = webDriver.findElement(By.tagName("h2")).getText();
		Assert.assertEquals(heading, "Welcome, player!");
	}

}
