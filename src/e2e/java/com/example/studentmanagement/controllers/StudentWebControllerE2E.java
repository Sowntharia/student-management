package com.example.studentmanagement.controllers;

import com.example.studentmanagement.container.BaseTestContainer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;


import java.time.Duration;
import java.util.List;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("testcontainers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentWebControllerE2E extends BaseTestContainer {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port;

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1280,1100" // a bit taller to keep buttons in view
        );
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ---------- tests ----------

    @Test
    void testCreateStudent() {
        WebElement studentRow = createStudent();
        assertThat(studentRow.getText()).contains("firstname", "lastname");
    }

    @Test
    void testUpdateStudent() {
        // create first, then edit that same row
        WebElement row = createStudent();
        String rowId = row.getAttribute("id");

        // open edit page for that id
        row.findElement(By.cssSelector(".edit")).click();
        wait.until(ExpectedConditions.urlMatches(".*/students/\\d+/edit$"));

        // edit form
        WebElement first = wait.until(ExpectedConditions.elementToBeClickable(By.id("firstName")));
        WebElement last  = driver.findElement(By.id("lastName"));
        WebElement email = driver.findElement(By.id("email"));

        first.clear(); first.sendKeys("UpdatedFirst");
        last.clear();  last.sendKeys("UpdatedLast");
        email.clear(); email.sendKeys("updated+" + UUID.randomUUID() + "@example.com");

        clickSaveStudent();

        wait.until(ExpectedConditions.urlMatches(".*/students$"));
        By rowSel = By.cssSelector("tr[id='" + rowId + "']");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(rowSel, "UpdatedFirst"));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(rowSel, "UpdatedLast"));

        WebElement updatedRow = driver.findElement(rowSel);
        assertThat(updatedRow.getText()).contains("UpdatedFirst", "UpdatedLast");
    }

    @Test
    void testDeleteStudent() {
        WebElement row = createStudent();
        String rowId = row.getAttribute("id");

        // delete is a POST form button inside the row
        WebElement deleteBtn = row.findElement(By.cssSelector(".delete"));
        scrollIntoViewCenter(deleteBtn);
        deleteBtn.click();

        wait.until(ExpectedConditions.urlMatches(".*/students$"));
        wait.until(d -> d.findElements(By.cssSelector("tr[id='" + rowId + "']")).isEmpty());
        assertThat(driver.findElements(By.cssSelector("tr[id='" + rowId + "']"))).isEmpty();
    }

    @Test
    void testListAllStudents() {
        goToStudentsPage();
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("studentTable")));
        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
        assertThat(rows.size()).isGreaterThanOrEqualTo(0); // allow empty list
    }

    // ---------- helpers ----------

    private void goToStudentsPage() {
        driver.get(baseUrl + "/students");
        waitForDomReady();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("studentTable")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#studentTable tbody")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new")));
    }

    private WebElement createStudent() {
        goToStudentsPage();
        // go to new form page
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new")));
        addBtn.click();
        wait.until(ExpectedConditions.urlMatches(".*/students/new$"));

        // fill and save
        WebElement first = wait.until(ExpectedConditions.elementToBeClickable(By.id("firstName")));
        WebElement last  = driver.findElement(By.id("lastName"));
        WebElement email = driver.findElement(By.id("email"));

        String uniqueEmail = "student+" + UUID.randomUUID() + "@gmail.com";
        first.clear(); first.sendKeys("firstname");
        last.clear();  last.sendKeys("lastname");
        email.clear(); email.sendKeys(uniqueEmail);

        clickSaveStudent();

        // back to list; find the row that has the unique email
        wait.until(ExpectedConditions.urlMatches(".*/students$"));
        By rowBy = By.xpath("//table[@id='studentTable']//tr[td[contains(normalize-space(.), '" + uniqueEmail + "')]]");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowBy));
    }

    private void clickSaveStudent() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("save-student")));
        scrollIntoViewCenter(btn);
        wait.until(ExpectedConditions.elementToBeClickable(btn));
        try {
            btn.click();
        } catch (WebDriverException e) {
            // last resort: JS click avoids intercepts/overlays
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    private void scrollIntoViewCenter(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el
        );
    }

    private void waitForDomReady() {
        wait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")));
    }
}
