package selenium;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import config.AppConfig;
import enums.Category;
import enums.Commands;
import pages.MultiPlayerPage;
import pages.MultiPlayerStartedPage;
import pages.WelcomePage;
import repository.WordsRepository;
import service.GameService;

@SpringBootTest(classes = { AppConfig.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class MultiPlayerE2ETest extends AbstractTestNGSpringContextTests {

	WebDriver driver;
	MultiPlayerStartedPage multiPlayerStartedPage;
	private MultiPlayerPage multiPlayerPage;
	WelcomePage welcomePage;

	@BeforeEach
	void setUp() {
		driver = new ChromeDriver();
		multiPlayerPage = new MultiPlayerPage(driver);
		multiPlayerStartedPage = new MultiPlayerStartedPage(driver);
		welcomePage = new WelcomePage(driver);
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@org.junit.jupiter.api.Test
	void finishWholeMultiPlayerGame() throws InterruptedException {
		driver.get("http:/www.localhost:8080/welcome");
		welcomePage.clickMultiPlayerButton();
		String actual = driver.findElement(By.tagName("h2")).getText();
		String expected = "Multiplayer Game Started!";
		Assert.assertEquals(actual, expected);

		String randomString = GameService.generateRandomString(4);
		multiPlayerPage.setWordToGuess(randomString);
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();

		WebElement firstGuessBtn = driver.findElement(By.cssSelector("button[value='" + randomString.charAt(1) + "']"));
		firstGuessBtn.click();

		WebElement secondGuessBtn = driver
				.findElement(By.cssSelector("button[value='" + randomString.charAt(2) + "']"));
		secondGuessBtn.click();

		String actualStatus = driver.findElement(By.id("status")).getText();
		Assert.assertEquals(actualStatus, "Congratulations! You Won!");
	}

	@org.junit.jupiter.api.Test
	void finishWholeMultiPlayerGameFail() throws InterruptedException {
		driver.get("http:/www.localhost:8080/welcome");
		welcomePage.clickMultiPlayerButton();
		String actual = driver.findElement(By.tagName("h2")).getText();
		String expected = "Multiplayer Game Started!";
		Assert.assertEquals(actual, expected);

		String randomString = GameService.generateRandomString(4);
		multiPlayerPage.setWordToGuess(randomString);
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();

		List<Character> notInWord = new ArrayList<Character>();
		for (char c : Commands.CHARACTERS.toCharArray()) {
			if (randomString.indexOf(c) == -1) {
				notInWord.add(c);
			}
		}

		WebElement guessBtn1 = driver.findElement(By.cssSelector("button[value='" + notInWord.get(0) + "']"));
		guessBtn1.click();

		WebElement guessBtn2 = driver.findElement(By.cssSelector("button[value='" + notInWord.get(1) + "']"));
		guessBtn2.click();

		WebElement guessBtn3 = driver.findElement(By.cssSelector("button[value='" + notInWord.get(2) + "']"));
		guessBtn3.click();

		WebElement guessBtn4 = driver.findElement(By.cssSelector("button[value='" + notInWord.get(3) + "']"));
		guessBtn4.click();

		WebElement guessBtn5 = driver.findElement(By.cssSelector("button[value='" + notInWord.get(4) + "']"));
		guessBtn5.click();

		WebElement guessBtn6 = driver.findElement(By.cssSelector("button[value='" + notInWord.get(5) + "']"));
		guessBtn6.click();

		String actualStatus = driver.findElement(By.id("status")).getText();
		Assert.assertEquals(actualStatus, "HAHAHA You lost! The word was " + randomString + ".");
	}
}
