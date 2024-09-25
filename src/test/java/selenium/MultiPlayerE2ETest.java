package selenium;

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
import pages.MultiPlayerPage;
import pages.MultiPlayerStartedPage;
import pages.WelcomePage;
import repository.WordsRepository;

@SpringBootTest(classes = { AppConfig.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class MultiPlayerE2ETest extends AbstractTestNGSpringContextTests {

	WebDriver driver;
	MultiPlayerStartedPage multiPlayerStartedPage;
	private MultiPlayerPage multiPlayerPage;
	WelcomePage welcomePage;
	@Autowired
	private WordsRepository wordsRepository;

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
	void finishWholeMultiPlayerGame() {
		driver.get("http:/www.localhost:8080/welcome");
		welcomePage.clickMultiPlayerButton();
		String actual = driver.findElement(By.tagName("h2")).getText();
		String expected = "Multiplayer Game Started!";
		Assert.assertEquals(actual, expected);

		multiPlayerPage.setWordToGuess("test");
		multiPlayerPage.setCategory(Category.FRUITS);
		multiPlayerPage.clickSubmitButton();

		WebElement buttonE = driver.findElement(By.xpath("//button[text()='E']"));
		buttonE.click();

		WebElement buttonS = driver.findElement(By.xpath("//button[text()='S']"));
		buttonS.click();
		
		String actualStatus = driver.findElement(By.id("status")).getText();
		Assert.assertEquals(actualStatus,"Congratulations! You Won!");
	}
	
	@org.junit.jupiter.api.Test
	void finishWholeMultiPlayerGameFail() throws InterruptedException {
		driver.get("http:/www.localhost:8080/welcome");
		welcomePage.clickMultiPlayerButton();
		String actual = driver.findElement(By.tagName("h2")).getText();
		String expected = "Multiplayer Game Started!";
		Assert.assertEquals(actual, expected);

		multiPlayerPage.setWordToGuess("onionss");
		multiPlayerPage.setCategory(Category.VEGETABLES);
		multiPlayerPage.clickSubmitButton();

		WebElement buttonW = driver.findElement(By.xpath("//button[text()='W']"));
		buttonW.click();

		WebElement buttonS = driver.findElement(By.xpath("//button[text()='D']"));
		buttonS.click();
		
		WebElement buttonZ = driver.findElement(By.xpath("//button[text()='Z']"));
		buttonZ.click();

		WebElement buttonY = driver.findElement(By.xpath("//button[text()='Y']"));
		buttonY.click();
		
		WebElement buttonB = driver.findElement(By.xpath("//button[text()='B']"));
		buttonB.click();

		WebElement buttonC = driver.findElement(By.xpath("//button[text()='C']"));
		buttonC.click();
		
		String actualStatus = driver.findElement(By.id("status")).getText();
		Assert.assertEquals(actualStatus,"HAHAHA You lost! The word was onionss.");
	}
}
