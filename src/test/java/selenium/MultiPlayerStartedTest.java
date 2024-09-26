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
import enums.Commands;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import pages.MultiPlayerPage;
import pages.MultiPlayerStartedPage;
import repository.WordsRepository;
import service.GameService;

@SpringBootTest(classes = { AppConfig.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MultiPlayerStartedTest extends AbstractTestNGSpringContextTests {

	WebDriver driver;
	MultiPlayerStartedPage multiPlayerStartedPage;
	private MultiPlayerPage multiPlayerPage;
	@Autowired
	private WordsRepository wordsRepository;

	@BeforeEach
	void setUp() {
		Map<Game, History> history = wordsRepository.getHistory();
		int size = wordsRepository.getHistory().size();
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

	}

	@org.junit.jupiter.api.Test
	void testWordMakeTry() throws InterruptedException {
		String randomString = GameService.generateRandomString(5);
		multiPlayerPage.setWordToGuess(randomString);
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();

		WebElement guessBtn = driver
				.findElement(By.cssSelector("button[value='" + randomString.charAt(1) + "']"));
		guessBtn.click();

		String updatedWord = driver.findElement(By.id("currentState")).getText();
		Assert.assertTrue(updatedWord.charAt(17) == (randomString.charAt(1)));
	}

	@org.junit.jupiter.api.Test
	void testWordMakeTryFail() throws InterruptedException {

		String randomString = GameService.generateRandomString(5);
		multiPlayerPage.setWordToGuess(randomString);
		multiPlayerPage.setCategory(Category.FRUITS);
		Thread.sleep(2000);
		multiPlayerPage.clickSubmitButton();
		
		char letterNotInWord = ' ';
	    for (char c : Commands.CHARACTERS.toCharArray()) {
	        if (randomString.indexOf(c) == -1) {  
	            letterNotInWord = c;
	            break;  
	        }
	    }

		WebElement guessBtn = driver
				.findElement(By.cssSelector("button[value='" + letterNotInWord + "']"));
		guessBtn.click();

		String updatedWord = driver.findElement(By.id("currentState")).getText();
		Assert.assertFalse(updatedWord.contains(String.valueOf(letterNotInWord)));
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
