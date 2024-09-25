package selenium;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;

import config.AppConfig;
import config.WebbInitialializer;
import enums.Category;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import pages.MultiPlayerPage;
import pages.MultiPlayerStartedPage;
import repository.WordsRepository;

@SpringBootTest(classes = { AppConfig.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MultiPlayerStartedTest extends AbstractTestNGSpringContextTests {

	WebDriver driver;
	MultiPlayerStartedPage multiPlayerStartedPage;
	private MultiPlayerPage multiPlayerPage;
	@Autowired
	private WordsRepository wordsRepository;

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	@BeforeEach
	void setUp() {
		Map<Game,History> history = wordsRepository.getHistory();
		history.clear();
		driver = new ChromeDriver();
		driver.get("http://www.localhost:8080/multiPlayer");
		multiPlayerPage = new MultiPlayerPage(driver);
		multiPlayerStartedPage = new MultiPlayerStartedPage(driver);
		
	}

	@AfterEach
	void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		Map<Game,History> history = wordsRepository.getHistory();
		history.clear();
	}

	@org.junit.jupiter.api.Test
	void testWordMakeTry() throws InterruptedException {

		multiPlayerPage.setWordToGuess("bananas");
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();

		WebElement guessBtn = driver.findElement(By.cssSelector("button[type='submit']"));
		guessBtn.click();

		WebElement updatedWord = driver.findElement(By.id("currentState"));
		Assert.assertTrue(updatedWord.getText().contains("a"));
	}

	@org.junit.jupiter.api.Test
	void testWordMakeTryFail() throws InterruptedException {

		multiPlayerPage.setWordToGuess("robots");
		multiPlayerPage.setCategory(Category.TECHNOLOGY);
		System.out.print(HttpSession.class);
		int history = wordsRepository.getHistory().size();
		multiPlayerPage.clickSubmitButton();
		Thread.sleep(2000);
		WebElement guessBtn = driver.findElement(By.cssSelector("button[type='submit']"));
		guessBtn.click();
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("/multiplayerStarted"));
		Thread.sleep(2000);
		WebElement updatedWord = driver.findElement(By.id("currentState"));
		Assert.assertFalse(updatedWord.getText().contains("a"));
		Assert.assertTrue(multiPlayerStartedPage.getTriesLeft() < 6);
	}

	@org.junit.jupiter.api.Test
	void testWordDisplayedAndBackToHome() {
		multiPlayerPage.setWordToGuess("home");
		multiPlayerPage.setCategory(Category.TECHNOLOGY);
		multiPlayerPage.clickSubmitButton();

		multiPlayerStartedPage.clickHomeButton();
		String heading = driver.findElement(By.tagName("h2")).getText();
		Assert.assertEquals(heading, "Welcome, player!");
	}

}
