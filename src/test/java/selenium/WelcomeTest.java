package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import pages.WelcomePage;

 class WelcomeTest {

	WebDriver driver;
	WelcomePage welcomePage;

	@BeforeEach
	 void setup() {
		driver = new ChromeDriver();
		driver.get("http:/www.localhost:8080/welcome");

		welcomePage = new WelcomePage(driver);
	}

	@AfterEach
	 void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@org.junit.jupiter.api.Test
	 void testSinglePlayer() throws InterruptedException {
		welcomePage.clickSinglePlayerButton();
		String actual = driver.findElement(By.id("tries-left")).getText();
		Thread.sleep(2000);
		String expected = "Tries left: 6";
		Assert.assertEquals(actual, expected);

	}

	@org.junit.jupiter.api.Test
	 void testMultiPlayer() throws InterruptedException {
		welcomePage.clickMultiPlayerButton();
		String actual = driver.findElement(By.tagName("h2")).getText();
		Thread.sleep(2000);
		String expected = "Multiplayer Game Started!";
		Assert.assertEquals(actual, expected);

	}

	@org.junit.jupiter.api.Test
	 void testHistory() throws InterruptedException {
		welcomePage.clickHistoryButton();
		String actual = driver.findElement(By.tagName("h2")).getText();
		Thread.sleep(2000);
		String expected = "Hangman Games History";
		Assert.assertEquals(actual, expected);

	}

}
