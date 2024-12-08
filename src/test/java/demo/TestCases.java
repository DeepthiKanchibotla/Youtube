package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;
        Wrappers wrappers;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */
        @Test
        public void testCase01() throws InterruptedException {

                System.out.println("Start of test case 01");
                driver.get("https://www.youtube.com/");
                wrappers.verifyCurrentURL("https://www.youtube.com/");
                wrappers.verifyAboutmssg();
                System.out.println("End of test case 01");
        }

         @Test
        public void testCase02() throws InterruptedException {
                System.out.println("Start of test case 02");
                driver.get("https://www.youtube.com/");
                wrappers.navigateToTab("Movies");
                wrappers.verifyNextButton(driver, "Top selling", 3);
                wrappers.verifyMovieMaturity();
                wrappers.movieCategory();
                System.out.println("End of test case 02");
        }

        @Test
        public void testCase03() throws InterruptedException {
                System.out.println("Start of test case 03");
                driver.get("https://www.youtube.com/");
                wrappers.navigateToTab("Music");
                wrappers.verifyNextButton(driver, "Biggest Hits", 2);
                wrappers.nameOfLastPlayList("Biggest Hits");
                wrappers.noOfTracks("Biggest Hits", "Bollywood Dance");
                System.out.println("End of test case 03");
        }

        @Test
        public void testCase04() throws InterruptedException {
                System.out.println("Start of test case 04");
                driver.get("https://www.youtube.com/");
                wrappers.navigateToTab("News");
                wrappers.titleofLatestpost();
                wrappers.sumOfLikes();
                System.out.println("End of test case 04");
        }

        @Test(dataProvider = "excelData",dataProviderClass = ExcelDataProvider.class)
        public void testCase05(String text) throws InterruptedException {
                System.out.println("Start of test case 05");
                driver.get("https://www.youtube.com/");
                wrappers.searchItem(text);
                wrappers.totalCountofViews();
                System.out.println("End of test case 05");
        }



        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);
                wrappers = new Wrappers(driver);

                driver.manage().window().maximize();
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}