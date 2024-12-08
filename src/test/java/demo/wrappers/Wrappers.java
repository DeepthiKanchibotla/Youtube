package demo.wrappers;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class Wrappers {
    private static ChromeDriver driver;

    /*
     * Write your selenium wrappers here
     */
    public Wrappers(ChromeDriver driver) {

        this.driver = driver;
    }

    public void verifyCurrentURL(String expectedURL) {
        String currentURL = driver.getCurrentUrl();
        Assert.assertTrue(currentURL.equals(expectedURL));
        System.out.println("Landed on correct URL");
    }

    public void verifyAboutmssg() throws InterruptedException {
        try {
            WebElement aboutLink = driver.findElement(By.xpath("//a[text()='About']"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true)", aboutLink);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='About']")));

            aboutLink.click();

            Thread.sleep(30);

            String aboutText = driver.findElement(By.xpath("//h1[contains(text(),'About YouTube')]")).getText();
            System.out.println("Content on ABout Page : " + aboutText);

            String missionText = driver.findElement(By.xpath("//p[contains(text(),'Our mission')]")).getText();
            System.out.println(missionText);

            String communityText = driver.findElement(By.xpath("//p[contains(text(),'community')]")).getText();
            System.out.println(communityText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToTab(String tabName) throws InterruptedException {
        Thread.sleep(300);
        WebElement movieTab = driver.findElement(By.xpath("//yt-formatted-string[text()='" + tabName + "']"));
        movieTab.click();
    }

    public void verifyNextButton(ChromeDriver driver, String sectionName, int times) throws InterruptedException {

        Thread.sleep(3000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sectionElement = driver
                .findElement(By.xpath("//span[@id='title' and contains(text(),'" + sectionName + "')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", sectionElement);
        WebElement nextButtonElement = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='title' and contains(text(),'"
                        + sectionName + "')]//ancestor::div[@id='dismissible']//child::button[@aria-label='Next']")));
        try {
            for (int i = 0; i < times; i++) {
                if (nextButtonElement.isDisplayed() && nextButtonElement.isEnabled()) {
                    nextButtonElement.click();
                    System.out.println("Next click is Done");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void titleofLatestpost() throws InterruptedException {
        Thread.sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement latestNewsElement = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//span[contains(text(),'Latest news posts')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", latestNewsElement);

        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> titleElements = driver.findElements(
                By.xpath("//*[@class='style-scope ytd-post-renderer' and @id='header']/div[@id='author']/a/span"));
        List<WebElement> bodyElements = driver.findElements(
                By.xpath("//*[@class='style-scope ytd-post-renderer' and @id='body']//*[@id='home-content-text']"));

        for (int i = 0; i <= 3 && i < titleElements.size() && i < bodyElements.size(); i++) {
            System.out.println(titleElements.get(i).getText().trim());
            System.out.println(bodyElements.get(i).getText().trim());
        }
        Thread.sleep(1000);
    }

    public void nameOfLastPlayList(String sectionName) throws InterruptedException {
        List<WebElement> playListNames = driver.findElements(
                By.xpath("//a[contains(@title,'" + sectionName + "')]//ancestor::div[@id='dismissible']//child::h3"));
        WebElement lastPlayListElement = playListNames.get(playListNames.size() - 1);
        String playListHeadLineText = lastPlayListElement.getText().trim();
        System.out.println(playListHeadLineText);
        Thread.sleep(1000);
    }

    public int sumOfLikes() {
        int intvalue = 0;
        List<WebElement> allLikes = driver.findElements(By.xpath("//span[@id='vote-count-middle']"));
        int count = 0;
        for (WebElement eachLike : allLikes) {

            if (eachLike.isDisplayed()) {
                String likeValue = eachLike.getText().trim();
                intvalue = Integer.parseInt(likeValue);
                intvalue += intvalue;
                count++;
            } else {
                continue;
            }
            if (count == 3)
                break;
            else
                continue;
        }
        System.out.println(intvalue);
        return intvalue;
    }

    public void searchItem(String text) throws InterruptedException {
        WebElement searchBox = driver.findElement(By.xpath("//input[@id='search']"));
        searchBox.click();
        searchBox.sendKeys(text);
        WebElement searchIcon = driver.findElement(By.xpath("//button[@id='search-icon-legacy']"));
        searchIcon.click();
        Thread.sleep(5000);
    }

    public void totalCountofViews() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long totalViews = 0;

        while (totalViews < 100000000) {

            List<WebElement> views = driver.findElements(By.xpath("//div[@id=\"metadata-line\"]/span[1]"));
            for (WebElement eachview : views) {
                String viewsText = eachview.getText();
                if (viewsText.contains("views")) {
                    viewsText = viewsText.split(" ")[0];
                    totalViews += parseViews(viewsText);
                }

                if (totalViews >= 1000000000) {
                    break;

                }
            }

            js.executeScript("window.scrollBy(0, 1000);");
            Thread.sleep(2000); // Wait for new videos to load
        }

        System.out.println("Total views for " + totalViews);
    }

    public long parseViews(String viewsText) {
        long views = 0;
        if (viewsText.endsWith("K")) {
            views = (long) (Double.parseDouble(viewsText.replace("K", "")) * 1_000);
        } else if (viewsText.endsWith("M")) {
            views = (long) (Double.parseDouble(viewsText.replace("M", "")) * 1_000_000);
        } else if (viewsText.endsWith("B")) {
            views = (long) (Double.parseDouble(viewsText.replace("B", "")) * 1_000_000_000);
        } else {
            views = Long.parseLong(viewsText.replace(",", ""));
        }
        return views;
    }

    public void noOfTracks(String sectionName, String playListName) throws InterruptedException {
        try {
            WebElement tracksElement = driver.findElement(By.xpath(
                    "//a[contains(@title,'" + sectionName
                            + "')]//ancestor::div[@id='dismissible']//h3[contains(@title,'" + playListName
                            + "')]//ancestor::div[contains(@class,'yt-lockup-view-model-wiz yt-lockup-view-model-wiz')]//div[contains(@class,'badge-shape-wiz__text')]"));
            String tracks = tracksElement.getText();
            String[] trackArray = tracks.split(" ");
            System.out.println(trackArray[0]);
            int trackCount = Integer.parseInt(trackArray[0]);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(trackCount <= 50, "The number of tracks listed is greater than 50: " + trackCount);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Thread.sleep(1000);
    }

    public void verifyMovieMaturity() {
        try {

            Thread.sleep(300);
            // span[@id='title' and contains(text(),'Top
            // selling')]/ancestor::div[@id='title-container']
            // div[contains(@class,'badge-style-type-simple')]/p
            List<WebElement> ratings = driver
                    .findElements(By.xpath("//*[@class='badges style-scope ytd-grid-movie-renderer']/div[2]/p"));

            if (!ratings.isEmpty()) {
                WebElement lastElementRating = ratings.get(ratings.size() - 1);
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertEquals(lastElementRating.getText(), "U",
                        "This movie is not marked 'A' for Mature");

                softAssert.assertAll();
            } else {
                System.out.println("No Mature Elements found.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void movieCategory() {
        try {

            Thread.sleep(300);
            List<WebElement> alljoners = driver
                    .findElements(By.xpath("//span[contains(@class,'grid-movie-renderer-metadata style-scope')]"));
            if (!alljoners.isEmpty()) {
                WebElement lastJoner = alljoners.get(alljoners.size() - 1);
                String[] jonerType = lastJoner.getText().split(".");
                String jonerText = jonerType[0].trim();
                System.out.println(jonerText);
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertEquals(
                        jonerText.startsWith("Comedy") || jonerText.startsWith("Animation")
                                || jonerText.startsWith("Drama"),
                        "Joner Text is neither 'Comedy' nor 'Animation' nor 'Drama'");
                softAssert.assertAll();

            } else {
                System.out.println("No Joner Text availbale for the movie.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
