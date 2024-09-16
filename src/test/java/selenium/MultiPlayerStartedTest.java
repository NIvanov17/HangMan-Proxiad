package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import enums.Category;
import pages.MultiPlayerPage;
import pages.MultiPlayerStartedPage;

public class MultiPlayerStartedTest {

	WebDriver driver;
	MultiPlayerStartedPage multiPlayerStartedPage;
	private MultiPlayerPage multiPlayerPage;

	@BeforeMethod
	public void setUp() {
		driver = new ChromeDriver();
		driver.get("http://www.localhost:8080/multiPlayer");

		multiPlayerPage = new MultiPlayerPage(driver);
		multiPlayerStartedPage = new MultiPlayerStartedPage(driver);
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testWordMakeTry() throws InterruptedException {

		multiPlayerPage.setWordToGuess("peaacheess");
		multiPlayerPage.setCategory(Category.FRUITS);
		multiPlayerPage.clickSubmitButton();
		Thread.sleep(2000);

		WebElement guessBtn = driver.findElement(By.cssSelector("button[type='submit']"));
		Thread.sleep(2000);
		guessBtn.click();
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("/multiplayerStarted"),
				"Should be redirected to multiplayerStarted view.");

		WebElement updatedWord= driver.findElement(By.id("currentState"));
		Assert.assertTrue(updatedWord.getText().contains("a"));
	}
	
	@Test
	public void testWordMakeTryFail() throws InterruptedException {

		multiPlayerPage.setWordToGuess("roboooots");
		multiPlayerPage.setCategory(Category.TECHNOLOGY);
		multiPlayerPage.clickSubmitButton();
		Thread.sleep(2000);

		WebElement guessBtn = driver.findElement(By.cssSelector("button[type='submit']"));
		Thread.sleep(2000);
		guessBtn.click();
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("/multiplayerStarted"));

		WebElement updatedWord= driver.findElement(By.id("currentState"));
		Assert.assertFalse(updatedWord.getText().contains("a"));
		Assert.assertTrue(multiPlayerStartedPage.getTriesLeft() < 6);
	}
	
	@Test
	public void testWordDisplayedAndBackToHome() throws InterruptedException {

		multiPlayerPage.setWordToGuess("home");
		multiPlayerPage.setCategory(Category.TECHNOLOGY);
		multiPlayerPage.clickSubmitButton();
		Thread.sleep(2000);

		multiPlayerStartedPage.clickHomeButton();
		String heading = driver.findElement(By.tagName("h2")).getText();
		Assert.assertEquals(heading, "Welcome, player!");
	}

}
